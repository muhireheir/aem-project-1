package it.codeland.heritier.core.models;

import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class, adapters = SlideModel.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SlideModel {
    private static final Logger LOG = LoggerFactory.getLogger(SlideModel.class);

    @SlingObject
    private Resource currentResource;

    List<Map<String, String>> slides = new ArrayList<>();
    Map<String, String> overlay = new HashMap<>();

    public List<Map<String, String>> getSlides() {
        try {
            Resource slidesList = currentResource.getChild("slidesList");
            if (slidesList == null) {
                return Collections.emptyList();
            }
            if (slidesList != null) {
                for (Resource slide : slidesList.getChildren()) {
                    Map<String, String> slideMap = new HashMap<String, String>();
                    slideMap.put("slideType", slide.getValueMap().get("slideType", String.class));
                    slideMap.put("logoTheme", slide.getValueMap().get("logoTheme", String.class));
                    slideMap.put("title", slide.getValueMap().get("title", String.class));
                    slideMap.put("subTitle", slide.getValueMap().get("subTitle", String.class));
                    slideMap.put("description", slide.getValueMap().get("description", String.class));
                    slideMap.put("desktopImageReference",
                            slide.getValueMap().get("desktopImageReference", String.class));
                    slideMap.put("mobileImageReference", slide.getValueMap().get("mobileImageReference", String.class));
                    slideMap.put("category", slide.getValueMap().get("category", String.class));
                    slideMap.put("date", slide.getValueMap().get("date", String.class));

                    Date date = slide.getValueMap().get("date", Date.class);
                    SimpleDateFormat pattern = new SimpleDateFormat("E dd MMM yyyy");
                    String stringDate = pattern.format(date);

                    slideMap.put("date", stringDate);
                    slideMap.put("buttonText", slide.getValueMap().get("buttonText", String.class));
                    slideMap.put("link", slide.getValueMap().get("link", String.class));
                    slideMap.put("buttonLabel", slide.getValueMap().get("buttonLabel", String.class));

                    slideMap.put("overlayDesktopImageReference",
                            slide.getValueMap().get("overlayDesktopImageReference", String.class));
                    slideMap.put("overlayMobileImageReference",
                            slide.getValueMap().get("overlayMobileImageReference", String.class));
                    slideMap.put("overlayTitle", slide.getValueMap().get("overlayTitle", String.class));
                    slideMap.put("overlaySubTitle", slide.getValueMap().get("overlaySubTitle", String.class));
                    slideMap.put("overlayDescription", slide.getValueMap().get("overlayDescription", String.class));

                    slides.add(slideMap);
                }
            }
        } catch (Exception e) {
            LOG.error(" ****ERROR**** while getting The slides {} " + e.getMessage());
        }
        return slides;
    }

    public List<List<String>> getTags() {
        List<List<String>> tags = new ArrayList<>();
        try {
            Resource slidesList = currentResource.getChild("slidesList");
            if (slidesList == null) {
                return Collections.emptyList();
            }
            if (slidesList != null) {
                for (Resource slide : slidesList.getChildren()) {
                    String tag = slide.getValueMap().get("tags", String.class);
                    if (tag != null) {
                        String[] inputTags = slide.getValueMap().get("tags", String[].class);
                        List<String> tagList = new ArrayList<>(Arrays.asList(inputTags));
                        tags.add(tagList);
                    }
                    char[] d = {};
                    String dummyTags = new String(d);
                    List<String> tagList = new ArrayList<>(Arrays.asList(dummyTags));
                    tags.add(tagList);
                }
            }
        } catch (Exception e) {
            LOG.error(" ****ERROR**** while getting The slide tags {} " + e.getMessage());
        }
        return tags;
    }

    public int getSlidesLength() {
        return slides.size();
    }
}