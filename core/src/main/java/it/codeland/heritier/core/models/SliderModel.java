package it.codeland.heritier.core.models;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SliderModel {
    private final List<Resource> slides = new ArrayList<>();
    private final List<Resource> orderedSlides = new ArrayList<>();

    @SlingObject
    private Resource currentResource;

    @Inject
    private String[] slideOrder;


    @PostConstruct
    protected void init() throws PersistenceException {
       
        Iterator<Resource> children = currentResource.listChildren();
        if (children == null) {
           return;
        }
       
       Resource currentChild;
	   	while (children.hasNext()) {
            currentChild = children.next();
			slides.add(currentChild);
	   	}
                
        if(slideOrder != null){
            for(String slideName : slideOrder){
                Resource activeSlide = slides.stream().filter(item-> {
                if (item.getName().equals(slideName)) {
                    return true;
                }return false;
                }).findAny().orElse(null);
                 
                if(activeSlide != null){ 
                    orderedSlides.add(activeSlide);
                 }
             }
        } 

        for(Resource sld : orderedSlides){
            ValueMap adaptedSld = sld.adaptTo(ValueMap.class);
            String[] originalTags = adaptedSld.get("tags", String[].class);
            List<String> tags = new ArrayList<>();
           
            if(originalTags != null){
                for(String tag :  originalTags){
                     if(tag.contains("/")){
                        tags.add(tag.substring(tag.lastIndexOf("/") + 1));
                    } 
                    else tags.add(tag.substring(tag.lastIndexOf(":") + 1));
                }
                try {
                    ModifiableValueMap map = sld.adaptTo(ModifiableValueMap.class);
                    String[] tagsArr = Iterables.toArray(tags, String.class);
                    map.put("tags", tagsArr);
                    // sld.getResourceResolver().commit();
                } catch (Exception e) {}
           }
        }
    }

    public List<Resource> getSlides(){
        return orderedSlides;
    }
}
