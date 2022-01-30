package it.codeland.heritier.core.helpers;

import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.google.common.collect.Iterators;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.day.cq.commons.jcr.JcrConstants;

import javax.jcr.Session;
import com.day.cq.wcm.api.Page;

import javax.jcr.Node;

import org.apache.commons.logging.Log;
import org.apache.sling.api.resource.Resource;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.DateTime;

@Component
public class PageCreator {
    ResourceResolver resourceResolver;
    PageManager pageManager;
    Session session;
    Page newPage;
    int createdPages=0;
    int skippedPages=0;
    List<String[]> csvData;
    ArticleImportStatus status;
    private final String TEMPLATE = "/apps/ucs-exercise-heritier/templates/article";
    private final String ARTICLES_PAGE = "/content/ucs-exercise-heritier/magazine";
    private final String SLING_RESOURCE_TYPE = "ucs-exercise-heritier/components/structure/article";
    private Page articleRootPage;
    private static final Logger log = LoggerFactory.getLogger(PageCreator.class);

    public PageCreator(ResourceResolver resourceResolver, List<String[]> csvData,ArticleImportStatus status) {
       try {
        this.resourceResolver = resourceResolver;
        session = this.resourceResolver.adaptTo(Session.class);
        pageManager = this.resourceResolver.adaptTo(PageManager.class);
        this.status = status;
        int size = csvData.size()-1;
        this.status.setSize(size);
        this.articleRootPage = pageManager.getPage(ARTICLES_PAGE);
       } catch (Exception e) {
           log.error(e.getMessage());
           
       }
    }

    public String createPagesFromCsv(Long lastModified, List<String[]> csvData) {
        this.csvData = csvData;
        DateTime now = new DateTime();
        String date = now.toString();
        try {


            if (articleRootPage == null) {
                log.info("No root page for articles found!");
                return "OOps! No root page for articles found!";

            }
            if (!this.isCsvModified(lastModified)) {
                this.status.setModified();
                log.info("####### File was not changed/modified ##########");
                return "####### File was not changed/modified ##########";
            }
            for (String[] data : csvData) {
                
          
                log.info("####### Creating page ##########");

                String[] row = data;
                if (row[2].equals("Description") || row[2].equals("description")) {
                    continue;
                }
                String pageName = row[0];
                String pageTitle = row[0];
                String image = row[1];
                String[] tags = row[3].split(",");
                String description = row[2];
                String pagePath = ARTICLES_PAGE;
                if (pageManager.getPage(pagePath + "/" + pageName) != null) {
                    skippedPages++;
                    status.setSkipped(skippedPages);
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
                    jcr.setProperty("date",now.toString());
                    jcr.setProperty("sling:resourceType", SLING_RESOURCE_TYPE);
                    session.save();
                    createdPages++;
                    this.status.setCreated(createdPages);
                }
            }
            log.info("################ RESLUTS : ################ \n\t\t Created Pages:" + createdPages + "\n\t\t Skipped pages:" + skippedPages + "\n\n\n ################################################");
            return "################ RESLUTS : ################ \n\t\t Created Pages:" + createdPages + "\n\t\t Skipped pages:" + skippedPages + "\n\n\n ################################################";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "OOps! Something went wrong! " + e.getMessage();
        }

    }

    private Boolean isCsvModified(Long lastModified) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(lastModified);
            Date assetLastModified = calendar.getTime();
            Date currDateTime = new Date();
            Resource pageResource = this.articleRootPage.getContentResource();
            Node pgNode = pageResource.adaptTo(Node.class);
            if (pageResource.getValueMap().get("lastJobExecTime") != null) {
                Long lastJobExecTime = pageResource.getValueMap().get("lastJobExecTime", Long.class);
                Calendar lexect = Calendar.getInstance();
                lexect.setTimeInMillis(lastJobExecTime);
                Date execTime = lexect.getTime();
                if (execTime.before(assetLastModified)) {
                    pgNode.setProperty("lastJobExecTime", currDateTime.getTime());
                    session.save();
                    log.info("*********** returning true");
                    return true;
                }
                log.info("*********** returning false");
                return false;
            } else {
                log.info("*********** setting property");
                pgNode.setProperty("lastJobExecTime", currDateTime.getTime());
                session.save();
                return true;
            }
        } catch (Exception e) {
            log.info("🔥🏔 🏔 🏔🔥🔥 🏔🔥 🏔🔥 🏔🔥 🏔  ### ERROR {}", e.getMessage());
            return false;
        }
    }

}