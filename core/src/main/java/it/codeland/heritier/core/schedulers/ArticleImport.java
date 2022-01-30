package it.codeland.heritier.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import it.codeland.heritier.core.schedulers.config.ArticleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.codeland.heritier.core.helpers.ArticleImportStatus;
import it.codeland.heritier.core.helpers.CsvReader;
import it.codeland.heritier.core.helpers.PageCreator;
import it.codeland.heritier.core.helpers.ResourceHelper;

import org.apache.sling.jcr.api.SlingRepository;

@Component(immediate = true, service = ArticleImport.class)
@Designate(ocd = ArticleConfig.class)
public class ArticleImport implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ArticleImport.class);
    @Reference
    private Scheduler scheduler;

    @Reference
    protected SlingRepository repository;

    private int schedulerId;

    @Reference
    private ResourceResolverFactory resourceFactory;

    @Modified
    @Activate
    protected void activate(ArticleConfig config) {

        schedulerId = config.schdulerName().hashCode();
        addScheduler(config);
    }

    // protected void modified(ArticleConfig config) {
    // log.info("\n\n\n\n\n\n\n\n\n\n\n OSGI config changed
    // \n\n\n\n\n\n\n\n\n\n\n");

    // removeScheduler();
    // schedulerId = config.schdulerName().hashCode();
    // addScheduler(config);
    // }

    // private void removeScheduler() {
    // log.info("\n\n\n\n\n\n\n\n\n Removing scheduler: {} \n\n\n\n\n",
    // schedulerId);
    // scheduler.unschedule(String.valueOf(schedulerId));
    // }

    private void addScheduler(ArticleConfig config) {
        ScheduleOptions scheduleOptions = scheduler.EXPR(config.cronExpression());
        scheduleOptions.name(config.schdulerName());
        scheduleOptions.canRunConcurrently(false);
        scheduler.schedule(this, scheduleOptions);
        log.info("\n\n\n\n\n\n\n\n\n\n\n Schedure added \n\n\n\n\n\n\n\n\n\n\n");
    }

    // @Deactivate
    // protected void deactivate(ArticleConfig config) {
    // log.info("\n\n\n\n\n\n\n\n\n disactivate scheduler: {} \n\n\n\n\n",
    // schedulerId);
    // removeScheduler();
    // }

    @Override
    public void run() {
        ArticleImportStatus status = new ArticleImportStatus();
        log.info("\n######## JOB RUNNING ########\n");
        try {
            ResourceHelper resolver = new ResourceHelper(resourceFactory);
            ResourceResolver resourceResolver = resolver.getResourceResolver();
            CsvReader csvReader = new CsvReader(resourceResolver);
            Iterator<String[]> data = csvReader.getData();
            List<String[]> articles = new ArrayList<String[]>();

            while (data.hasNext()) {
                articles.add(data.next());
            }
            Long lastModified = csvReader.getLastModified();
            PageCreator pageCreator = new PageCreator(resourceResolver, articles, status);
            String result = pageCreator.createPagesFromCsv(lastModified, articles);
            log.info("\n\n\n{}\n\n\n", result);
        } catch (Exception e) {
            log.info("ERROR!!! {}", e.getMessage());
        }
    }
}
