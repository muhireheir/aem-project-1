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
public class Carousel {
    private Iterator<Resource> childComponents;


    @SlingObject
    private Resource currentResource;



    @PostConstruct
    protected void init() throws PersistenceException {
       
        Iterator<Resource> children = currentResource.listChildren();
        childComponents = children;
       
    }

    public Resource getcurrentResource(){
        return  currentResource;
    }
    
    public Iterator <Resource> getChildComponents(){
        return childComponents;
    }


    // get parent component
    
    public Resource getParentComponent() {
        return currentResource.getParent();
    }

    // get child components
    
}
