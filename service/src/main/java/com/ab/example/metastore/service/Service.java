package com.ab.example.metastore.service;

import com.ab.example.metastore.service.dao.*;
import com.ab.example.metastore.service.resources.*;
import com.codahale.metrics.JmxReporter;
import com.expedia.www.doppler.metastore.commons.entities.*;
import com.ab.example.metastore.service.bundle.PrimerResourceBundle;
import com.ab.example.metastore.service.bundle.SwitchableSwaggerBundle;
import com.ab.example.metastore.service.configuration.ServiceConfiguration;
import com.expedia.www.doppler.metastore.service.dao.*;
import com.ab.example.metastore.service.exception.MetaStoreExceptionMapper;
import com.ab.example.metastore.service.healthchek.DatabaseHealthCheck;
import com.ab.example.metastore.service.healthchek.HealthCheckResource;
import com.expedia.www.doppler.metastore.service.resources.*;
import com.ab.example.metastore.service.resources.coarse_grained.CoarseGrainedAlertAPIs;
import com.ab.example.metastore.service.resources.coarse_grained.CoarseGrainedTopicAPIs;
import com.ab.example.metastore.service.resources.coarse_grained.CoarseGrainedUserAPIs;
import com.ab.example.metastore.service.util.Constants;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author _amal
 *
 * Application main class.
 */
public class Service extends Application<ServiceConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    public static void main(String[] args) throws Exception {
        new Service().run(args);
    }

    private final HibernateBundle<ServiceConfiguration> hibernate =
            new HibernateBundle<ServiceConfiguration>(Alert.class, AlertInstance.class, AlertType.class,
                    AlertSubscription.class, SubscriptionChannel.class, Team.class,
                    Tenant.class, User.class, UserProfile.class, Component.class, ContextToContextMap.class,
                    Correlation.class, Dashboard.class, Context.class, Model.class, ModelVersion.class,
                    Permission.class, Role.class, Widget.class, WidgetGroup.class, Topic.class,
                    TopicEntity.class, TopicEntityAttribute.class, TopicEntityAttributeType.class, Metric.class,
                    BusinessEntityAttribute.class, Granularity.class, Cluster.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(ServiceConfiguration alertProcessorConfiguration) {
                    return alertProcessorConfiguration.getDataSourceFactory();
                }
            };

    private final  MigrationsBundle<ServiceConfiguration> migrations = new MigrationsBundle<ServiceConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(ServiceConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
        bootstrap.addBundle(new PrimerResourceBundle());
        bootstrap.addBundle(new SwitchableSwaggerBundle());
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(migrations);

        JmxReporter.forRegistry(bootstrap.getMetricRegistry())
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build()
                .start();
    }

    @Override
    public void run(ServiceConfiguration serviceConfiguration, Environment environment) throws Exception {
        final Client jerseyClient = new JerseyClientBuilder(environment)
                .using(serviceConfiguration.getHttpClientConfiguration()).build("HttpClientForSearchService");

        final AlertDao alertDao = new AlertDao(hibernate.getSessionFactory());
        final DashboardDao dashboardDao = new DashboardDao(hibernate.getSessionFactory());
        final AlertInstanceDao alertInstanceDao = new AlertInstanceDao(hibernate.getSessionFactory());
        final AlertSubscriptionDao alertSubscriptionDao = new AlertSubscriptionDao(hibernate.getSessionFactory());
        final AlertTypeDao alertTypeDao = new AlertTypeDao(hibernate.getSessionFactory());
        final SubscriptionChannelDao subscriptionChannelDao = new SubscriptionChannelDao(hibernate.getSessionFactory());
        final TeamDao teamDao = new TeamDao(hibernate.getSessionFactory());
        final TenantDao tenantDao = new TenantDao(hibernate.getSessionFactory());
        final UserDao userDao = new UserDao(hibernate.getSessionFactory());
        final UserProfileDao userProfileDao = new UserProfileDao(hibernate.getSessionFactory());
        final ComponentDao componentDao = new ComponentDao(hibernate.getSessionFactory());
        final ContextToContextMapDao contextToContextMapDao = new ContextToContextMapDao(hibernate.getSessionFactory());
        final CorrelationDao correlationDao = new CorrelationDao(hibernate.getSessionFactory());
        final WidgetDao widgetDao = new WidgetDao(hibernate.getSessionFactory());
        final WidgetGroupDao widgetGroupDao = new WidgetGroupDao(hibernate.getSessionFactory());
        final GranularityDao granularityDao = new GranularityDao(hibernate.getSessionFactory());
        final MetricDao metricDao = new MetricDao(hibernate.getSessionFactory());
        final ContextDao contextDao = new ContextDao(hibernate.getSessionFactory());
        final ModelDao modelDao = new ModelDao(hibernate.getSessionFactory());
        final ModelVersionDao modelVersionDao = new ModelVersionDao(hibernate.getSessionFactory());
        final PermissionDao permissionDao = new PermissionDao(hibernate.getSessionFactory());
        final RoleDao roleDao = new RoleDao(hibernate.getSessionFactory());
        final ClusterDao clusterDao = new ClusterDao(hibernate.getSessionFactory());
        final TopicDao topicDao = new TopicDao(hibernate.getSessionFactory());
        final TopicEntityDao topicEntityDao = new TopicEntityDao(hibernate.getSessionFactory());
        final TopicEntityAttributeDao topicEntityAttributeDao = new TopicEntityAttributeDao(hibernate
                .getSessionFactory());
        final TopicEntityAttributeTypeDao topicEntityAttributeTypeDao = new TopicEntityAttributeTypeDao(hibernate
                .getSessionFactory());
        final BusinessEntityAttributeDao businessEntityAttributeDao = new BusinessEntityAttributeDao(hibernate
                .getSessionFactory());


        /* For generating application.wadl xml. (XML describing all application resources.. available at
         /api/application.wadl) */
        final Map<String, Object> properties = new HashMap<>();
        properties.put(ServerProperties.WADL_FEATURE_DISABLE, false);
        environment.jersey().getResourceConfig().addProperties(properties);
        environment.jersey().register(MetaStoreExceptionMapper.class);
        environment.jersey().register(new EchoResource());

        environment.jersey().register(new AlertResource(alertDao, jerseyClient, serviceConfiguration.searchServiceURI));
        environment.jersey().register(new DashboardResource(dashboardDao, jerseyClient,
                serviceConfiguration.searchServiceURI));
        environment.jersey().register(new BusinessEntityAttributeResource(businessEntityAttributeDao, jerseyClient,
                serviceConfiguration.searchServiceURI));

        environment.jersey().register(new AlertInstanceResource(alertInstanceDao));
        environment.jersey().register(new AlertSubscriptionResource(alertSubscriptionDao));
        environment.jersey().register(new AlertTypeResource(alertTypeDao));
        environment.jersey().register(new SubscriptionChannelResource(subscriptionChannelDao));
        environment.jersey().register(new TeamResource(teamDao));
        environment.jersey().register(new TenantResource(tenantDao));
        environment.jersey().register(new UserProfileResource(userProfileDao));
        environment.jersey().register(new UserResource(userDao));
        environment.jersey().register(new ComponentResource(componentDao));
        environment.jersey().register(new ContextCorrelationResource(contextToContextMapDao));
        environment.jersey().register(new CorrelationResource(correlationDao));
        environment.jersey().register(new WidgetResource(widgetDao));
        environment.jersey().register(new WidgetGroupResource(widgetGroupDao));
        environment.jersey().register(new GranularityResource(granularityDao));
        environment.jersey().register(new MetricResource(metricDao));
        environment.jersey().register(new ContextResource(contextDao));
        environment.jersey().register(new ModelResource(modelDao));
        environment.jersey().register(new ModelVersionResource(modelVersionDao));
        environment.jersey().register(new PermissionResource(permissionDao));
        environment.jersey().register(new RoleResource(roleDao));
        environment.jersey().register(new ClusterResource(clusterDao));
        environment.jersey().register(new TopicResource(topicDao));
        environment.jersey().register(new TopicEntityResource(topicEntityDao));
        environment.jersey().register(new TopicEntityAttributeResource(topicEntityAttributeDao));
        environment.jersey().register(new TopicEntityAttributeTypeResource(topicEntityAttributeTypeDao));
        environment.jersey().register(new CoarseGrainedAlertAPIs(alertInstanceDao));
        environment.jersey().register(new CoarseGrainedTopicAPIs(businessEntityAttributeDao));
        environment.jersey().register(new CoarseGrainedUserAPIs(alertDao, dashboardDao, alertInstanceDao,
                userProfileDao, userDao, alertSubscriptionDao));


        registerHealthChecks(jerseyClient, environment, serviceConfiguration);
    }

    /**
     * Method to register all health checks configured. Also runs a scheduled executor service to perform
     * periodic health check, so that the results are reported via JMX.
     *
     * @param jerseyClient
     * @param environment
     * @param serviceConfiguration
     */
    private void registerHealthChecks(Client jerseyClient, Environment environment, ServiceConfiguration serviceConfiguration) {
        // Registering database health checks and health check resource
        environment.healthChecks().register("database",
                new DatabaseHealthCheck(serviceConfiguration.getDataSourceFactory()));
        environment.jersey().register(new HealthCheckResource(environment.healthChecks()));


        /* Running a scheduled executor of performing periodic health checks.
         *  the /health API is metered, so the api invocations and corresponding response code will be available in JMX.
         *  Alerts can be generated on that value, so that devs will be notified if there is a health check failure.
         *
         *  Look for com.expedia.www.doppler.metastore.service.healthchek.HealthCheckResource.runHealthChecks.exceptions
         *  metric in JMX MBeans.
         *
         * */
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        final WebTarget webTarget = jerseyClient.target("http://localhost:8080/" + Constants.API_V1_VERSION + "/health");
        final Invocation.Builder requestBuilder = webTarget.request(MediaType.APPLICATION_JSON);


        scheduler.scheduleWithFixedDelay(
                () -> {
                    final Response response = requestBuilder.post(Entity.entity(null, MediaType.APPLICATION_JSON));
                    LOGGER.info("Health check executed with response: " + response.getStatus());
                }, 1, 120, TimeUnit.MINUTES);
    }
}
