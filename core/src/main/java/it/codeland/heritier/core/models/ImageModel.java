/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package it.codeland.heritier.core.models;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

// import javax.inject.Inject;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;
import org.apache.sling.api.resource.ValueMap;

import java.util.Arrays;
import java.util.List;
import java.util.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class ImageModel {

    @ValueMapValue(name = PROPERTY_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Default(values = "No resourceType")
    protected String resourceType;

    @SlingObject
    Resource resource;

    String desktopImage;

    String mobileImage;

    @SlingObject
    private Resource currentResource;
    @SlingObject
    private ResourceResolver resourceResolver;

    private static final Logger log = LoggerFactory.getLogger(ImageModel.class);

    //
    private String desktopRendition;
    private String mobileRendition;
    private String tabletRendition;

    private String desktopImagePath;
    private String mobileImagePath;
    private String tabletImagePath;

    @PostConstruct
    protected void init() {
        String currentResourceName = currentResource.getName();
        Resource rendableResource = null;
        if (currentResourceName.equals("image-1")) {
            rendableResource = currentResource.getParent();
        }else{
            rendableResource = currentResource;
        }
        ContentPolicyManager policyManager = resourceResolver.adaptTo(ContentPolicyManager.class);
        ContentPolicy contentPolicy = policyManager.getPolicy(rendableResource);
        ValueMap contentPolicyProps = contentPolicy.getProperties();
        this.desktopRendition = contentPolicyProps.get("desktopRendition", String.class);
        this.tabletRendition = contentPolicyProps.get("tabletRendition", String.class);
        this.mobileRendition = contentPolicyProps.get("mobileRendition", String.class);
        // current resource name

       


        if (currentResourceName.equals("image-1")) {
            getImagesRenditions(currentResource);
        }else if (currentResource.getChild("image-1") != null) {
            getImagesRenditions(resource.getChild("image-1"));
        }
    }

    public String getDesktopRendition() {
        return desktopImagePath;
    }

    public String getMobileRendition() {
        return mobileImagePath;
    }

    public String getTableRendition() {
        return tabletImagePath;
    }

    public void getImagesRenditions(Resource resource) {
        Resource imageNode = resource;
            if (imageNode.getValueMap().get("desktopImagePath") != null) {
                desktopImage = imageNode.getValueMap().get("desktopImagePath", String.class);
            }
            if (imageNode.getValueMap().get("mobileImagePath") != null) {
                mobileImage = imageNode.getValueMap().get("mobileImagePath", String.class);
            } else {
                mobileImage = imageNode.getValueMap().get("desktopImagePath", String.class);
            }

            if (desktopImage != null && mobileImage != null) {
                String dampath = "content/dam/ucs-exercise-heritier/";
                String mobileImageName = mobileImage.split(dampath)[1];
                String desktopImageName = desktopImage.split(dampath)[1];
                mobileImagePath = "/content/dam/ucs-exercise-heritier/" + mobileImageName + ".transform/"
                        + this.mobileRendition + "/image.png";
                desktopImagePath = "/content/dam/ucs-exercise-heritier/" + desktopImageName + ".transform/"
                        + this.desktopRendition + "/image.png";
                tabletImagePath = "/content/dam/ucs-exercise-heritier/" + desktopImageName + ".transform/"
                        + this.tabletRendition + "/image.png";
            }
    }
}
