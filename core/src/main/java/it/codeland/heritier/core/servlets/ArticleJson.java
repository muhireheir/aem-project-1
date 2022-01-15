package it.codeland.heritier.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import com.google.gson.Gson;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import it.codeland.heritier.core.dto.Article;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = "cq:Page", methods = HttpConstants.METHOD_GET, extensions = "json", selectors = "export")
@ServiceDescription("export article to json format")
public class ArticleJson extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        final Resource resource = req.getResource();
        final Resource article = resource.getChild(JcrConstants.JCR_CONTENT);
        try {
            String title = (String) article.getValueMap().get(JcrConstants.JCR_TITLE);
            String description = (String) article.getValueMap().get("description");
            String image = (String) article.getValueMap().get("picture");
            String link = resource.getPath() + ".html";
            String[] tags = (String[]) article.getValueMap().get("cq:tags");
            Article exprt = new Article(title, link, tags, image, description);
            Gson gson = new Gson();
            String json = gson.toJson(exprt);
            resp.setContentType("text/json");
            resp.getWriter().write(json);
        } catch (Exception e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(e.getMessage());
        }
    }
}
