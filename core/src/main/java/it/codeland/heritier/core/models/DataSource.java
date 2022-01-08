package it.codeland.heritier.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;

import javax.annotation.PostConstruct;

import java.util.Iterator;
import java.util.*;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = SlingHttpServletRequest.class)
public class DataSource {

	@RequestAttribute
	private String href;

	private String message;

	@SlingObject
	private ResourceResolver resourceResolver;

	private Iterator<Resource> childComponents;

	private List<Resource> itemsOrder;

	private List<Resource> orderedSlides = new ArrayList<>();

	private List<Resource> allItems = new ArrayList<>();

	@PostConstruct
	public void init() {
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		String url = href;
		String parts = url.split("_cq_dialog.html")[1];
		String currentUri = parts.split("/jcr:content/slides")[0];
		message = parts;
		Page rootPage = pageManager.getPage(currentUri);

		// check if page has slides child
		if (rootPage.getContentResource("slides") != null) {
			Resource resource = rootPage.getContentResource("slides");
			Iterator<Resource> children = resource.listChildren();

			while (children.hasNext()) {
				Resource currentChild = children.next();
				allItems.add(currentChild);
			}
			String[] currentSlideOrder;
			if (resource.getValueMap().get("slideOrder") != null) {
				currentSlideOrder = resource.getValueMap().get("slideOrder", String[].class);
			} else {
				currentSlideOrder = new String[]{"slide1","slide2","slide3","slide4","slide5"};
			}
			childComponents = children;

			// order all items

			for (String slide : currentSlideOrder) {
				Resource activeSlide = allItems.stream().filter(item -> {
					if (item.getName().equals(slide)) {
						return true;
					}
					return false;
				}).findAny().orElse(null);

				if (activeSlide != null) {
					orderedSlides.add(activeSlide);
				}
			}
		}

	}

	public String getHref() {
		return href;
	}

	public Iterator<Resource> getChildComponents() {
		return childComponents;
	}

	// get message
	public String getMessage() {
		return message;
	}

	// get itemsOrder
	public List<Resource> getItemsOrder() {
		return itemsOrder;
	}

	// get allItems
	public List<Resource> getAllItems() {
		return allItems;
	}

	// get orderedSlides
	public List<Resource> getOrderedSlides() {
		if(orderedSlides.size() == 0) {
			return allItems;
		}
		return orderedSlides;
	}

}