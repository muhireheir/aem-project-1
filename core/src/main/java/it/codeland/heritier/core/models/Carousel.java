package it.codeland.heritier.core.models;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Carousel {
    private Iterator<Resource> childComponents;

    private List<Resource> orderedSlides = new ArrayList<>();
    // allItems 
    private List<Resource> slides = new ArrayList<>();


    @SlingObject
    private Resource currentResource;


    @Inject
    private String[] slideOrder;



    @PostConstruct
    protected void init() throws PersistenceException {
       
        Iterator<Resource> children = currentResource.listChildren();
        childComponents = children;

        while (children.hasNext()) {
            Resource currentChild = children.next();
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
    }


    
    public Iterator <Resource> getChildComponents(){
        return childComponents;
    }

    // get all slides
    public List<Resource> getSlides() {
        return slides;
    }

    public List<Resource> getOrderedSlides(){
        return orderedSlides;
    }
    
}
