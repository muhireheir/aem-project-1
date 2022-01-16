package it.codeland.heritier.core.helpers;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResourceHelper {
  
    ResourceResolver resourceResolver;
    private static final Logger log = LoggerFactory.getLogger(ResourceHelper.class);

    public  ResourceHelper(ResourceResolverFactory resourceFactory){
        try {
            resourceResolver = resourceFactory.getAdministrativeResourceResolver(null);
        } catch (Exception e) {
            log.info("\n\n\n\nLoginException {}", e.getMessage());
        }
    }
    public ResourceResolver getResourceResolver(){
        return resourceResolver;
    }
}
