package it.codeland.heritier.core.helpers;

import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

import java.util.Iterator;

import javax.jcr.Session;
import com.day.cq.wcm.api.Page;

import javax.jcr.Node;

import org.apache.sling.api.resource.Resource;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

@Component
public class PageCreator {
    ResourceResolver resourceResolver;
    PageManager pageManager;
    Session session;
    Page newPage;
    Iterator<String[]> csvData;
    private final String TEMPLATE = "/apps/ucs-exercise-heritier/templates/article";
    private final String ARTICLES_PAGE = "/content/ucs-exercise-heritier/magazine";
    private final String SLING_RESOURCE_TYPE = "ucs-exercise-heritier/components/structure/article";

    public PageCreator(ResourceResolver resourceResolver, Iterator<String[]> csvData) {
        this.resourceResolver = resourceResolver;
        session = this.resourceResolver.adaptTo(Session.class);
        pageManager = this.resourceResolver.adaptTo(PageManager.class);
        this.csvData = csvData;
    }

    public String createPagesFromCsv() {
        try {
            Page articleRootPage = pageManager.getPage(ARTICLES_PAGE);
            if (articleRootPage == null) {
                return "OOps! No root page for articles found!";
            }
            while (csvData.hasNext()) {
                String[] row = csvData.next();
                if(row[2].equals("Description") || row[2].equals("description")) {
                    continue;
                }
                String pageName = row[0];
                String pageTitle = row[0];
                String image = row[1];
                String[] tags = row[3].split("&");
                String description = row[2];
                String pagePath = ARTICLES_PAGE;
                if(pageManager.getPage(pagePath + "/" + pageName) != null) {
                    continue;
                }
                newPage = pageManager.create(pagePath, pageName, TEMPLATE, pageTitle);
                if (newPage != null) {
                    Node articlePageNode = newPage.adaptTo(Node.class);
                    Node jcr = articlePageNode.getNode("jcr:content");
                    jcr.setProperty("jcr:title", pageTitle);
                    jcr.setProperty("description", description);
                    jcr.setProperty("picture", image);
                    jcr.setProperty("cq:tags", tags);
                    jcr.setProperty("date", "2017-01-01T00:00:00.000+0000");
                    jcr.setProperty("sling:resourceType", SLING_RESOURCE_TYPE);

                }
            }
            session.save();
            return "🔥  🔥  🔥 pages created! 🔥 🔥 🔥";
        } catch (Exception e) {
            return "OOps! Something went wrong! " + e.getMessage();
        }

    }
}
