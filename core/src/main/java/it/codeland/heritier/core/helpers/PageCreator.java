package it.codeland.heritier.core.helpers;
import com.day.cq.wcm.api.PageManager;

import java.util.Iterator;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;

public class PageCreator {
    ResourceResolver resourceResolver;
    PageManager pageManager;
    Session session;
    public PageCreator(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        session = this.resourceResolver.adaptTo(Session.class);
    }
    public void createPagesFromCsv(Iterator<String[]> csvData) {
        pageManager = resourceResolver.adaptTo(PageManager.class);

    }
}
