package it.codeland.heritier.core.models;

import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DataSource {

   private String[] cars;
   HashMap<String, String> items = new HashMap<String, String>();

    @PostConstruct
    protected void init() throws PersistenceException {

        cars = new String[]{"Audi", "BMW", "Mercedes", "Volvo"};
        items.put("", "Audi");
       
     
    }
    // get cars
    public String[] getCars(){
        return cars;
    }
    public HashMap<String,String> getItems(){
        return items;
    }

}
