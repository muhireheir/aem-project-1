package it.codeland.heritier.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.Iterators;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.servlet.Servlet;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.codeland.heritier.core.helpers.ArticleImportStatus;
import it.codeland.heritier.core.helpers.CountArticles;
import it.codeland.heritier.core.helpers.CsvReader;
import it.codeland.heritier.core.helpers.PageCreator;
import it.codeland.heritier.core.helpers.ResourceHelper;

@Component(service = { Servlet.class })
@SlingServletPaths(value = { "/bin/article-tool" })
@ServiceDescription("import articles Servlet")
public class ArticleImport extends SlingAllMethodsServlet {

    private final Logger LOG = LoggerFactory.getLogger(ArticleImport.class);
    private static final long serialVersionUID = 1L;

    PageManager pageManager;

    Gson gson = new Gson();

    @Reference
    protected ResourceResolverFactory resolverFactory;

    ArticleImportStatus status = new ArticleImportStatus();
    @Override
    protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
        try {
            List<String[]> articles = new ArrayList<String[]>();
            ResourceResolver resolver = new ResourceHelper(resolverFactory).getResourceResolver();
            CsvReader csvReader = new CsvReader(resolver);
            Iterator<String[]> data = csvReader.getData();
            status = new ArticleImportStatus();
            while(data.hasNext()){
                articles.add(data.next());
            }
            Long lastModified = csvReader.getLastModified();
            PageCreator pageCreator = new PageCreator(resolver,articles,status);
            pageCreator.createPagesFromCsv(lastModified,articles);
            String json = gson.toJson(status);
            resp.setContentType("text/json");
            resp.getWriter().write(json);
        } catch (Exception e) {
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
        try {
            ResourceResolver resolver = new ResourceHelper(resolverFactory).getResourceResolver();


            
            // istatus.getSize();
            String json = gson.toJson(status);
            resp.setContentType("text/json");
            resp.getWriter().write(json);


            
        } catch (Exception e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(e.getMessage());
        }
    }

}