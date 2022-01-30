package it.codeland.heritier.core.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.text.csv.Csv;
import java.io.InputStream;
import java.util.Iterator;


public class CsvReader {

    private static final Logger log = LoggerFactory.getLogger(CsvReader.class);

    Resource resource;
    ResourceResolver resourceResolver;
    private long lastModified;
    
    public  CsvReader(ResourceResolver resourceResolver ){
        this.resourceResolver=resourceResolver;
    }
    public Iterator<String[]> getData(){
        try {
            resource = resourceResolver.getResource("/content/dam/ucs-exercise-heritier/articles.csv");
            if(resource==null){
                log.error("Resource not found");
                return null;
            }
            Asset asset = resource.adaptTo(Asset.class);
            Rendition assetRendition = asset.getOriginal();
            lastModified = assetRendition.getAsset().getLastModified();
            log.info("last modified: {}", lastModified);
            InputStream inputStream = assetRendition.getStream();
            Csv csv = new Csv();
            Iterator<String[]> csvData = csv.read(inputStream,null);
            return csvData;
        } catch (Exception e) {
            log.info("Hey!  Error " + e.getMessage());
            return null;
        }
       
    }

    public long getLastModified(){
        return lastModified;
    }
}
