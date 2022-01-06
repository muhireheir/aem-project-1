package it.codeland.heritier.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Model(adaptables = Resource.class)
public class Socials {
    private static final Logger LOGGER = LoggerFactory.getLogger(Socials.class);


    @SlingObject
    private Resource currentResource;

    public String getTest() {
        return "test";
    }

    public List<Map<String, String>> getSocialMedias() {
        List<Map<String, String>> socialsMap = new ArrayList<>();
        try {
            Resource navsDialog=currentResource.getChild("socials");
            if(navsDialog!=null){
                for (Resource navItem : navsDialog.getChildren()) {
                    Map<String,String> mapNavItem = new HashMap<>();
                    mapNavItem.put("link",navItem.getValueMap().get("link",String.class));
                    mapNavItem.put("icon",navItem.getValueMap().get("icon",String.class));
                    mapNavItem.put("target",navItem.getValueMap().get("target",String.class));
                    socialsMap.add(mapNavItem);
                }
            }
        }catch (Exception e){
            LOGGER.info("ERROR while getting socials ",e.getMessage());
        }
        return socialsMap;
    }
}