package it.codeland.heritier.core.schedulers.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "ArticleImportSchedure", description = "ArticleImportSchedure configuration")
public @interface ArticleConfig {


	@AttributeDefinition(
			name = "Scheduler name", 
			description = "Name of the scheduler", 
			type = AttributeType.STRING)
	public String schdulerName() default "Custom Sling Scheduler Configuration";


	@AttributeDefinition(
			name = "Enabled", 
			description = "True, if scheduler service is enabled", 
			type = AttributeType.BOOLEAN)
	public boolean enabled() default true;
	

	@AttributeDefinition(
			name = "Cron Expression", 
			description = "Cron expression used by the scheduler", 
			type = AttributeType.STRING)
	public String cronExpression() default "*/10 * * * * ?";

	@AttributeDefinition(
			name = "Custom Parameter", 
			description = "Custom parameter to be displayed by the scheduler", 
			type = AttributeType.STRING)
	public String customParameter() default "AEM Scheduler Demo";
}