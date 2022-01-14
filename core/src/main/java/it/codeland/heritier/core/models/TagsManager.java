package it.codeland.heritier.core.models;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import java.util.*;



@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TagsManager {


    private String[] allTags;
    private List <HashMap>  tagsHashMap = new ArrayList<>();

    @Inject
    private String[] tags;

    @PostConstruct
    protected void init() throws PersistenceException {

        if(tags!=null){
            allTags = new String[tags.length];
            for(int i=0; i<tags.length; i++){
                HashMap<String,String> tg = new HashMap();
               
                
                String[] splitedTag =  tags[i].split(":");
                if(splitedTag.length == 2){
                    allTags[i] = splitedTag[1];
                    tg.put("tag", splitedTag[1]);
                }else{
                    allTags[i] = splitedTag[0];
                    tg.put("tag", splitedTag[0]);
                }
                tg.put("old", tags[i]);
                tagsHashMap.add(tg);
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
    // tagsHashMap

    public List<HashMap>getTagsHashMap(){
        return tagsHashMap;
    }
}
