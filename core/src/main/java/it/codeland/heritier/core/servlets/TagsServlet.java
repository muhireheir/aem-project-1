
package it.codeland.heritier.core.servlets;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.jcr.api.SlingRepository;
import java.util.HashMap;
import com.google.gson.*;

@Component(service = { Servlet.class }) 
@SlingServletPaths(
            value={"/bin/relatedTags"})
@ServiceDescription("Related hashtags component api")
public class TagsServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    private  List<Node>foundNodes = new ArrayList<>();
    protected Session session;

    @Reference
    protected SlingRepository repository;


    @Override
    protected void doGet(final SlingHttpServletRequest req,
        final SlingHttpServletResponse resp) throws ServletException, IOException {
            
            ArrayList<HashMap>articles = new ArrayList<>();
            final String tag = req.getParameter("tag");
       try {
        session = repository.loginAdministrative(null);
        Workspace workspace = session.getWorkspace();
        QueryManager queryManager = workspace.getQueryManager();
        String maxLen = req.getParameter("max") == null ? "20" : req.getParameter("max");
        String rawQuery="SELECT * FROM [cq:PageContent] AS magazines WHERE ISDESCENDANTNODE ([/content/ucs-exercise-heritier/magazine])  AND magazines.[cq:tags] = '"+tag+"'  ORDER BY [jcr:created] ASC";
        Query query = queryManager.createQuery(rawQuery, Query.JCR_SQL2);
        query.setLimit(Integer.parseInt(maxLen));
        QueryResult result = query.execute();
        NodeIterator magazineIterator = result.getNodes();
        while (magazineIterator.hasNext()) {
             Node currentNode = (Node)magazineIterator.next();
            if(!foundNodes.contains(currentNode)) {
                foundNodes.add(currentNode);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("E dd MMM yyyy");

        for (Node node : foundNodes) {
            HashMap<String, String> map = new HashMap<>();
            map.put("title", node.getProperty("jcr:title").getString());
            map.put("link", node.getParent().getPath());
            map.put("hashtag", tag);
            map.put("date",sdf.format(node.getProperty("jcr:created").getDate().getTime()));
            map.put("image", node.getProperty("picture").getString());
            map.put("text", node.getProperty("description").getString());
            articles.add(map);

        }
        foundNodes.clear();
        Gson gson = new Gson(); 
        String json = gson.toJson(articles);
        articles.removeAll(articles);
        resp.setContentType("application/json");
        resp.getWriter().println(json);
       } catch (Exception e) {
           //TODO: handle exception
           resp.setContentType("application/json");
           resp.getWriter().write(e.getMessage());
       }
       
    }
}
