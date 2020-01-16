package com.ab.example.metastore.commons.util;

/**
 *
 * @author _amal
 */
public final class Constants {

    public static final String TOPIC_TABLE_NAME = "topics";
    public static final String TOPIC_ENTITY_TABLE_NAME = "topic_entities";
    public static final String TOPIC_ENTITY_ATTRIBUTE_TABLE_NAME = "topic_entity_attributes";
    public static final String METRIC_TABLE_NAME = "metrics";
    public static final String TOPIC_ENTITY_ATTRIBUTE_TYPE_TABLE_NAME = "topic_attribute_types";
    public static final String BUSINESS_ENTITY_ATTRIBUTE_TABLE_NAME = "business_entity_attributes";
    public static final String GRANULARITY_TABLE_NAME = "granularities";
    public static final String CLUSTER_TABLE_NAME = "clusters";

    private Constants() {
        throw new UnsupportedOperationException("Instantiating a utility class.");
    }

    public static final String WIDGET_GROUP_TABLE_NAME = "widget_groups";
    public static final String WIDGET_TABLE_NAME = "widgets";
    public static final String COMPONENT_TABLE_NAME = "components";
    public static final String METRIC_CONTEXT_TABLE_NAME = "contexts";
    public static final String METRIC_CONTEXT_TO_CONTEXT_MAP_TABLE_NAME = "metric_context_to_metric_context_maps";
    public static final String CORRELATION_TABLE_NAME = "metric_correlations";
    public static final String MODEL_VERSION_TABLE_NAME = "model_versions";
    public static final String MODEL_TABLE_NAME = "models";

    public static final String USER_TABLE_NAME =  "users";
    public static final String USER_PROFILE_TABLE_NAME = "user_profiles";
    public static final String TEAM_TABLE_NAME = "teams";
    public static final String TENANT_TABLE_NAME = "tenants";

    public static final String ALERT_TABLE_NAME = "alerts";
    public static final String ALERT_SUBSCRIPTION_TABLE_NAME = "alert_subscriptions";
    public static final String ALERT_SUBSCRIPTION_CHANNEL_TABLE_NAME = "alert_subscription_channels";
    public static final String ALERT_TYPE_TABLE_NAME = "alert_types";
    public static final String ALERT_INSTANCE_TABLE_NAME = "alert_instances";

    public static final String ROLE_TABLE_NAME =  "roles";
    public static final String PERMISSION_TABLE_NAME = "permissions";
    public static final String DASHBOARD_TABLE_NAME = "dashboards";
    public static final String TEMPLATE_TABLE_NAME = "templates";
}
