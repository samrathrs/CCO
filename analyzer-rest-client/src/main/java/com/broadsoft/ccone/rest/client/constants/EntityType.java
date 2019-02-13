/**
 *
 */
package com.broadsoft.ccone.rest.client.constants;

/**
 * @author svytla
 */
public interface EntityType {

    String TENANT = "tenant";
    String SERVICE_PROVIDER = "service-provider";
    String CUSTOMER = "customer";
    // String TENANT_TYPE = "tenant-type";
    String VIRTUAL_TEAM = "virtual-team";
    String SITE = "site";
    String TEAM = "team";
    String USER = "user";

    String AGENT_PROFILE = "agent-profile";

    String TFN = "tfn";
    String TFN_DID_MAPPING = "tfn-did-mapping";
    String VTEAM_TFN_MAPPING = "vteam-tfn-mapping";

    String WORK_TYPE = "work-type";
    String AUX_CODE = "aux-code";

    String AREA_CODE = "area-code";

    String TIMEZONE = "timezone";
    String TENANT_GROUP_MAPPING = "tenant-group-mapping";
    String TENANT_BRANDING = "tenant-branding";

    String AGENT_SKILL = "agent-skill";
    // String ENUM_SKILLS_MAPPING = "enum-skills-mapping";
    String ENUM_SKILLS = "skill-enum-value";
    String SKILL_PROFILE_ASSIGNMENT = "skill-profile-assignment";
    String SKILL_PROFILE = "skill-profile";
    String SKILL_PROFILE_DETAILS = "skill-profile-details";
    String SKILL_STATUS = "skill-status";
    String SKILL_TYPE = "skill-type";
    String SKILL = "skill";

    // String TENANT_ADDRESS = "tenant-address";
    String ADHOC_DIAL_PROPERTIES = "adhoc-dial-properties";

    String AGENT_THRESHOLD_RULE_MAPPING = "agent-threshold-rule-mapping";
    String AGENT_TEAM_MAPPING = "agent-team-mapping";
    String BULK_DELETE = "bulk-delete";
    String BULK_DELETE_STATUS = "bulk-delete-status";
    String BUSINESS_METRIC = "business-metric";
    String CAD_VARIABLE = "cad-variable";
    String CCG = "ccg";
    String CCG_TENANT = "ccg-tenant";
    String CDS_COLLATERAL = "cds-collateral";
    String TEAM_CAPACITY_STRATEGY = "team-capacity-strategy";
    String TEAM_CAPACITY_STRATEGY_DETAILS = "team-capacity-strategy-details";
    String CDS_TEAM_CAPACITY = "cds-team-capacity";
    String DELETED_GRS_MAPPING = "deleted-grs-mapping";
    String DELETED_CALL_DISTRIBUTION_STRATEGY = "deleted-call-distribution-strategy";
    String GRS_MAPPING = "grs-mapping";
    String ROUTING_STRATEGY = "routing-strategy";
    String CHAT_PROFILE = "chat-profile";
    String COMPONENT_TYPE = "component-type";
    String TENANT_CONTACT = "tenant-contact";
    String DIAL_PLAN = "dial-plan";
    String DIAL_PLAN_RULE = "dial-plan-rule";
    String ID = "ID";
    String DIAL_PLAN_TRUNK = "dial-plan-trunk";
    String DID = "did";
    String DN = "dn";
    String DN_POOL_MAPPING = "dn-pool-mapping";
    String EMAIL_PROFILE = "email-profile";
    String ENTITY_TYPE = "entity-type";
    String EP_QUEUE_GROUP = "ep-queue-group";
    String FAX_PROFILE = "fax-profile";
    String GROUP = "group";
    String GW_INVENTORY = "gw-inventory";
    String CALL_FLOW_IMAGE = "call-flow-image";
    String CALL_FLOW_SCRIPT = "call-flow-script";
    String LOOKUP = "lookup";
    String MEDIA_PROFILE_ASSIGNMENT = "media-profile-assignment";
    String MEDIA_PROFILE = "media-profile";
    String MEDIA_SERVER = "media-server";
    String MEDIA_TYPE = "media-type";
    String MG_TRUNK_MAPPING = "mg-trunk-mapping";
    String MODULE = "module";
    String BROADCAST_MESSAGE = "broadcast-message";
    String BROADCAST_MESSAGE_MAPPING = "broadcast-message-mapping";
    String OTHER_PROFILE = "other-profile";
    String OUTDIAL_ANI = "outdial-ani";
    String OUTDIAL_ANI_LIST = "outdial-ani-list";
    String OUTDIAL_ANI_LIST_ENTRY = "outdial-ani-list-entry";
    String OUTDIAL_CCG_MAPPING = "outdial-ccg-mapping";
    String OUTDIAL_CONFIG = "outdial-configuration";
    String PLAN_STATUS = "plan-status";
    String POP_MAPPING = "pop-mapping";
    String PORT_TYPE = "port-type";
    String POSITION = "position";
    String PROFILE_THRESHOLD_DETAILS = "profile-threshold-details";
    String PUBLISH_CAD = "publish-cad";
    String PWD_HISTORY = "pwd-history";
    String PWD_POLICY_RULE = "pwd-policy-rule";
    String PWD_POLICY = "password-policy";
    String QUEUE_DN_POOL = "queue-dn-pool";
    String QUEUE_PRIORITY_STRATEGY = "queue-priority-strategy";
    String SERVER_MAPPING = "server-mapping";
    String SFDC_CREDENTIALS = "sfdc-credentials";
    String SPEED_DIAL_LIST = "speed-dial-list";
    String SPEED_DIAL_LIST_ENTRY = "speed-dial-list-entry";
    String SYSTEM_CAD_VARIABLE = "system-cad-variable";
    String THIRD_PARTY_PROFILE = "third-party-profile";
    String THIRD_PARTY_PROFILE_ASSIGNMENT = "third-party-profile-assignment";
    String THIRD_PARTY_VENDOR = "third-party-vendor";
    String CALL_RECORDING_SCHEDULE = "call-recording-schedule";
    String CALL_MONITORING_SCHEDULE = "call-monitoring-schedule";
    String CALL_MONITORING_REQUEST = "call-monitoring-request";
    String FILTER_VARIABLE = "filter-variable";
    String FILTER_VARIABLE_VALUE = "filter-variable-value";

    String THRESHOLD_METRIC = "threshold-metric";
    String THRESHOLD_RULE = "threshold-rule";
    String THRESHOLD_TOPIC = "threshold-topic";

    String TRUNK = "trunk";
    String VIDEO_PROFILE = "video-profile";
    // String VIRTUAL_TEAM_TO_TFN_MAPPING = "virtual_team_to_tfn_mapping";
    String VPOP = "vpop";
    String POP_MEDIA_SERVER_MAPPING = "pop-media-server-mapping";
    String VPOP_OB_MG_MAPPING = "vpop-ob-mg-mapping";
    String BULK_UPLOAD_STATUS = "bulk-upload-status";
    String ROLE = "role";

    String PERMISSION = "permission";
    String USER_PROFILE = "user-profile";

    String ATTRIBUTE = "attribute";
    String POLICY = "policy";
    String POLICY_SET = "policy-set";
    String RULE = "rule";

    String PWD_RESET = "password-reset";

    String MEDIA_FILE = "media-file";
    String AGENT_UNLOCK = "agent-unlock";
    String BULK_OPERATION_STATUS = "bulk-operations-status";

    String TENANT_DID = "tenant-did";
    String TENANT_CONFIGURATION = "tenant-configuration";

    String RECORDING_CONFIGURATION = "recording-configuration";

    String SETTINGS = "settings";

    String WCB_REQUEST = "wcb-request";

    String RELEASE_NOTES = "release-notes";

    String FB_MESSANGER = "fb-messenger";

    String SIP_ATTRIBUTES = "sip-attributes";

    String NETWORK_ATTRIBUTES = "network-attributes";

    String LICENSE_INFO = "license-info";

    String OUTDIAL_CONFIGURATION_TEMPLATE = "outdial-configuration-template";

}
