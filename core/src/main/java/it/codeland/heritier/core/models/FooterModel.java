package it.codeland.heritier.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
// import javax.inject.Inject;
import java.util.*;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

@Model(adaptables = Resource.class)
public class FooterModel {

    private static final Logger LOG = LoggerFactory.getLogger(FooterModel.class);

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @SlingObject
    Resource resource;
    @SlingObject
    private Resource currentResource;
    @SlingObject
    private ResourceResolver resourceResolver;

    @PostConstruct
    protected void init() {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        String currentPagePath = Optional.ofNullable(pageManager)
                .map(pm -> pm.getContainingPage(currentResource))
                .map(Page::getPath).orElse("");
    }

    public List<Map<String, String>> getListMapNavFooterNav1() {
        List<Map<String, String>> listMapNav = new ArrayList<>();
        try {
            Resource bookDetail = resource.getChild("linksMapFooterNav1");
            if (bookDetail != null) {
                for (Resource navItem : bookDetail.getChildren()) {
                    Map<String, String> mapNavItem = new HashMap<>();
                    mapNavItem.put("title", navItem.getValueMap().get("title", String.class));
                    mapNavItem.put("path", navItem.getValueMap().get("path", String.class));
                    mapNavItem.put("target", navItem.getValueMap().get("target", String.class));
                    listMapNav.add(mapNavItem);
                }
            }
        } catch (Exception e) {
            LOG.info("\n ERROR while getting Nav Items {} ", e.getMessage());
        }
        return listMapNav;
    }

    public List<Map<String, String>> getLinksMapFooterMiddle() {
        List<Map<String, String>> listMapNav = new ArrayList<>();
        try {
            Resource bookDetail = resource.getChild("linksMapFooterMiddle");
            if (bookDetail != null) {
                for (Resource navItem : bookDetail.getChildren()) {
                    Map<String, String> mapNavItem = new HashMap<>();
                    mapNavItem.put("title", navItem.getValueMap().get("title", String.class));
                    mapNavItem.put("path", navItem.getValueMap().get("path", String.class));
                    mapNavItem.put("target", navItem.getValueMap().get("target", String.class));
                    listMapNav.add(mapNavItem);
                }
            }
        } catch (Exception e) {
            LOG.info("\n ERROR while getting Nav Items {} ", e.getMessage());
        }
        return listMapNav;
    }

    //

    public List<Map<String, String>> getFooterUtils() {
        List<Map<String, String>> listMapNav = new ArrayList<>();
        try {
            Resource bookDetail = resource.getChild("footerUtils");
            if (bookDetail != null) {
                for (Resource navItem : bookDetail.getChildren()) {
                    Map<String, String> mapNavItem = new HashMap<>();
                    mapNavItem.put("title", navItem.getValueMap().get("title", String.class));
                    mapNavItem.put("path", navItem.getValueMap().get("path", String.class));
                    mapNavItem.put("target", navItem.getValueMap().get("target", String.class));
                    listMapNav.add(mapNavItem);
                }
            }
        } catch (Exception e) {
            LOG.info("\n ERROR while getting Nav Items {} ", e.getMessage());
        }
        return listMapNav;
    }

}