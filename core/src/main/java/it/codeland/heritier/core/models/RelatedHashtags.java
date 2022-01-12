package it.codeland.heritier.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;

import javax.annotation.PostConstruct;

import java.util.*;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = SlingHttpServletRequest.class)
public class RelatedHashtags {

    @SlingObject
    private ResourceResolver resourceResolver;

    List<String> hashtagList = new ArrayList<>();

    @Optional
    @RequestAttribute
    private String currentUri;

    List <Map <String,String>> cpTags = new ArrayList<>();

    @PostConstruct
    public void init() {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

        // ######

        String url = currentUri;
        String parts = url.split("_cq_dialog.html")[1];
        String requestUri = parts.split("/jcr:content/related-tags")[0];
        Page requestPage = pageManager.getPage(requestUri);


        // #######
        Page rootPage = pageManager.getPage("/content/ucs-exercise-heritier/magazine");
        Iterator<Page> childPageIterator = rootPage.listChildren();
        while (childPageIterator.hasNext()) {
            Page currentPage = childPageIterator.next();
            Resource page = currentPage.getContentResource();
            if (page.getValueMap().get("cq:tags") != null) {
                if (requestPage.getContentResource("related-tags") != null) {
                    Resource tagsComponent = requestPage.getContentResource("related-tags");
                    String[] allTags = new String[0];
                    if( tagsComponent.getValueMap().get("tags")!=null){
                        allTags =tagsComponent.getValueMap().get("tags", String[].class);
                    }
                    String[] currentHashtags = page.getValueMap().get("cq:tags", String[].class);
                    for (String currentHashtag : currentHashtags) {
                        if (!hashtagList.contains(currentHashtag)) {
                            Map <String,String> map = new HashMap<>();
                            String isActive = "false";
                            for (String tag : allTags) {
                                String cleanTag;
                                if(tag.contains("/")){
                                    cleanTag=tag.substring(tag.lastIndexOf("/") + 1);
                                }else{
                                    cleanTag=tag.split(":")[0];
                                }
                                if(currentHashtag.contains(cleanTag)){
                                    isActive = "true";
                                }
                            }
                            String withoutSlash =  currentHashtag.substring(currentHashtag.lastIndexOf("/") + 1);
                            String withoutColon =  withoutSlash.split(":")[0];
                            map.put("tag",withoutColon);
                            map.put("isActive",isActive);
                            cpTags.add(map);
                            hashtagList.add(currentHashtag);
                        }
                    }
                }

            }

        }

    }

    public List<String> getHashtagList() {
        return hashtagList;
    }
    // cpTags

    public List<Map<String,String>> getCpTags(){
        return cpTags;
    }
}