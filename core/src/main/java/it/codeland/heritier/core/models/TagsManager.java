package it.codeland.heritier.core.models;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
;


@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TagsManager {


    private String[] allTags;


    @Inject
    private String[] tags;

    @PostConstruct
    protected void init() throws PersistenceException {

        if(tags!=null){
            allTags = new String[tags.length];
            for(int i=0; i<tags.length; i++){
                String[] splitedTag =  tags[i].split(":");
                if(splitedTag.length == 2){
                    allTags[i] = splitedTag[1];
                }else{
                    allTags[i] = splitedTag[0];
                }
            }
        }
    }

    // get all tags
    public String[] getAllTags() {
        return allTags;
    }
    // get tags
    public String[] getTags() {
        return tags;
    }
}
