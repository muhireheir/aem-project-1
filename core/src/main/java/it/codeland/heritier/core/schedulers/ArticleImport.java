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

import java.util.Iterator;

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

    @Activate
    protected void activate(ArticleConfig config) {
        if (config.enabled()) {
            schedulerId = config.schdulerName().hashCode();
        }
    }

    @Modified
    protected void modified(ArticleConfig config) {
        log.info("\n\n\n\n\n\n\n\n\n\n\n OSGI config changed \n\n\n\n\n\n\n\n\n\n\n");

        removeScheduler();
        schedulerId = config.schdulerName().hashCode();
        addScheduler(config);
    }

    private void removeScheduler() {
        log.info("\n\n\n\n\n\n\n\n\n Removing scheduler: {} \n\n\n\n\n", schedulerId);
        scheduler.unschedule(String.valueOf(schedulerId));
    }

    private void addScheduler(ArticleConfig config) {
        if (config.enabled()) {
            ScheduleOptions scheduleOptions = scheduler.EXPR(config.cronExpression());
            scheduleOptions.name(config.schdulerName());
            scheduleOptions.canRunConcurrently(false);
            scheduler.schedule(this, scheduleOptions);
            log.info("\n\n\n\n\n\n\n\n\n\n\n Schedure added \n\n\n\n\n\n\n\n\n\n\n");

        } else {
            log.info("\n\n\n\n\n\n\n\n\n\n\n ArticleImport is disabled \n\n\n\n\n\n\n\n\n\n\n");
        }

    }

    @Deactivate
    protected void deactivate(ArticleConfig config) {
        log.info("\n\n\n\n\n\n\n\n\n disactivate scheduler: {} \n\n\n\n\n", schedulerId);
        removeScheduler();
    }

    @Override
    public void run() {
        log.info("\n\n\n ######## JOB RUNNING smoothly! ######## \n\n\n");
        try {
            ResourceHelper resolver = new ResourceHelper(resourceFactory);
            ResourceResolver resourceResolver = resolver.getResourceResolver();
            CsvReader csvReader = new CsvReader(resourceResolver);
            Iterator<String[]> data = csvReader.getData();
            PageCreator pageCreator = new PageCreator(resourceResolver);
            pageCreator.createPagesFromCsv(data);


            // this loop might messup the programm
            while(data.hasNext()){
                log.info("\n\n\n **** data from file ****\n {}\n\n", data.next()[0]);
            }
        } catch (Exception e) {
            log.info("ERROR!!! {}",e.getMessage());
        }
    }
}

 