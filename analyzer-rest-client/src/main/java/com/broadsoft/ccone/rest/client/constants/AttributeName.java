/**
 *
 */
package com.broadsoft.ccone.rest.client.constants;

/**
 * @author svytla
 */
public interface AttributeName {

    String ID = "id";

    String TID = "tid";

    String DB_ID = "dbId__l";

    String TENANT = "tenantId__s";

    String STATUS = "status__i";

    String NAME = "name__s";

    String DEFAULT = "isDefault__i";

    String AUXCODE_TYPE = "auxCodeType__i";

    String TENANT_TYPE = "tenantType__s";

    String TYPE = "type__s";

    String TYPE_INT = "type__i";

    String VTEAM = "virtualTeamId__s";

    String SITE = "siteId__s";

    String TEAM = "teamId__s";

    String DN = "dn__s";

    String TFN = "tfn__s";

    String PROFILE_DATA = "profileData__s";

    String PROFILE_TEXT = "profileText__s";

    String LIST_ID = "listId__s";

    String TEAM_IDS = "teamIds__sa";

    String DESCRIPTION = "description__s";

    String PARENTTYPE = "parentType__i";

    String PARENTID = "parentId__s";

    String NUMBER = "number__s";

    String CALLCENTER_ENABLED = "callCenterEnabled__i";

    String PASSWORD = "password__s";

    String SITE_ID = "siteId__s";

    String USER_ID = "userIds__sa";

    String SKILL_TYPE = "type__i";

    String SERVICE_LVL_THRLD = "serviceLevelThreshold__i";

    String SKILL_ID_INT = "skillId__i";

    String MAXCHANNELS = "maxChannels__l";

    String SKILL_ID = "skillId__s";

    String QUEUE_ID = "queueId__s";

    String DNLIST = "dnList__s";

    String DNPOOLSTATUS = "dnPoolStatus__i";

    String CCONE_QUEUE = "ccOneQueue__i";

    String ACD_TYPE = "acdType__s";

    String LAST_MODIFIED_TIME_STAMP = "_lmts__l";

    String IVRDNLIST = "ivrDnList__s";

    String FIRST_NAME = "firstName__s";

    String LAST_NAME = "lastName__s";

    String DEFAULT_DN = "defaultDn__s";

    String EMAIL = "email__s";

    String EXTERNAL_ID = "externalId__s";

    String LOGIN = "login__s";

    String SKILL_PROFILE_ID = "skillProfileId__s";

    String AGENT_PROFILE_ID = "agentProfileId__s";

    String MM_PROFILE_ID = "multimediaProfileId__s";

    String MM_EXT_PROFILE_ID = "extMultimediaProfileId__s";

    String ROLE_ID = "roleId__s";

    String ATTRIBUTE_USER_ID = "userId__s";

    String CHANNEL_TYPE = "channelType__i";

    String VENDOR_ID = "vendorId__s";

    String CONF_ENABLED = "conferenceEnabled__i";

    String CAS_ENABLED = "casEnabled__i";

    String THIRD_PARTY_PROFILE_ID = "thirdPartyProfileId__s";

    String MMEDIA_PROFILE_ID = "mmProfileId__s";

    String NUMBER_OF_CHATS = "numberOfChats__l";

    String NUMBER_OF_EMAILS = "numberOfEmails__l";

    String NUMBER_OF_VIDEOS = "numberOfVideoCalls__l";

    String NUMBER_OF_FAXS = "numberOfFaxes__l";

    String NUMBER_OF_OTHERS = "numberOfOther__l";

    String LOCKED = "locked__i";

    String BATCH_NAME = "batchName__s";

    String USER_NAME = "userName__s";

    String UPLOAD_STATUS = "uploadStatus__i";

    String FAILURE_REASON = "failureReason__s";

    String ENTITY_TYPE = "entityType__i";

    String VPOP_ID = "vpopId__s";

    String MODULE_PERMISSION = "permissions__s";

    String MODULE = "modules__sa";

    String OTHER_PERMISSIONS = "otherPermissions__sa";

    String SITES = "sites__sa";

    String EPS = "entryPoints__sa";

    String QUEUES = "queues__sa";

    String TEAMS = "teams__sa";

    String ENTRY_POINT_ID = "entryPointId__s";

    String PWD_LENGTH = "length__i";

    String PWD_REGEX = "regex__s";

    String TFN_FLAG = "tfnFlag__i";

    String CHANGEPASSWORD = "changePassword__i";

    String PWD_POLICY_ID = "passwordPolicyId__s";

    String TENANT_ARRAY = "tenantIds__sa";

    String MULTI_MEDIA_ENABLED = "multiMediaEnabled__i";

    String THIRD_PARTY_ENABLED = "thirdPartyEnabled__i";

    String MAP_VIEW_ENABLED = "mapViewEnabled__i";

    String CAPACITY = "capacity__l";

    String TEAM_TYPE = "teamType__i";

    String TEAM_STATUS = "teamStatus__s";

    String TEAM_DN = "teamDn__s";

    String PRIORITY = "priority__i";

    String SBR_ENABLED = "sbrEnabled__i";

    String HOST = "host__s";

    String PORT = "port__i";

    String PORT_TYPE = "portType__i";

    String ACTIVATION_TIME = "effectiveFrom__l";

    String EXPIRATION_TIME = "expiryDate__l";

    String OPERATING_MODE = "operatingMode__i";

    String MODE = "mode__i";

    // String ATTRIBUTE_GROUP_ID = "groupId__s";

    String INSTANCE_GROUP = "instanceGroup__s";

    String INSTANCE_NAME = "instanceName__s";

    String COMPONENT_TYPE = "componentType__s";

    String SCRIPT = "script__s";

    String LOGIN_NAME = "loginName__s";

    String OPERATION_STATUS = "operationStatus__i";

    String SCR_ENABLED = "cccEnabled__i";

    String NUMBER_OF_CAD_VARIABLES = "numberOfCadVariables__i";

    String MAXIMUM_ADDRESS_BOOKS = "maximumAddressBooks__i";

    String MAXIMUM_THRESHOLD_RULES = "maximumThresholdRules__i";

    String MAXIMUM_VIRTUAL_TEAMS = "maximumVirtualTeams__i";

    String URI = "uri__s";

    String PERMIT_MONITORING = "permitMonitoring__i";

    String PERMIT_RECORDING = "permitRecording__i";

    String PERMIT_PARKING = "permitParking__i";

    String MAXIMUM_ACTIVE_CALLS = "maximumActiveCalls__i";

    String MAXIMUM_TIME_IN_QUEUE = "maxTimeInQueue__l";

    String OVERFLOW_NUMBER = "overflowUri__s";

    String IVR_PARK_URL = "ivrParkUrl__s";

    String IVR_REQUEUE_URL = "ivrRequeueUrl__s";

    String RECORD_ALL_CALLS = "recordAllCalls__i";

    String DEDICATED = "isDedicated__i";

    String PERMIT_TRANSEFER = "permitRetransfer__i";

    String DEACTIVATED = "status__i";

    String CCG_ID = "ccgId__s";

    String CONFIGURATION_ID = "configurationId__s";

    String ACTIVE = "active__i";

    String HTTP_PORT = "httpPort__i";

    String IP_ADDRESS = "ipAddress__s";

    String SIP_PORT = "sipPort__i";

    String POP_ID = "popId__s";

    String GENERAL_INFORMATION = "generalInformation__s";

    String AUXILIARY_CODES = "auxiliaryCodes__s";

    String DIAL_PLAN = "dialPlan__s";

    String AGENT_DN_VALIDATION = "agentDnValidation__s";

    String AGENT_VIEWABLE_STATISTICS = "agentViewableStatistics__s";

    String AGENT_THRESHOLDS = "agentThresholds__s";

    String COLLABORATION = "collaboration__s";

    String SKILL_PROFILE_NAME = "skillProfileName__s";

    String SKILL_NAME = "skillName__s";

    String SKILL_TYPE_STRING = "skillType__s";

    String SKILL_VALUE = "skillValue__s";

    String MAXIMUM_SKILLS = "maximumSkills__i";

    String ALL_ENTRY_POINTS = "allEntryPoints__i";

    String ALL_QUEUES = "allQueues__i";

    String ALL_SITES = "allSites__i";

    String ALL_TEAMS = "allTeams__i";

    String PASSWORD_LAST_MODIFIED_TIME = "passwordLastModifiedTime__l";

    String PASSWORD_HISTORY_DATA = "passwordHistoryData__s";

    String DOMAIN = "domain__s";

    String TIMEZONE = "timezone__s";

    String ADDITIONAL_INFO = "additionalInfo__s";

    String WORKTYPE_ID = "workTypeId__s";

    String EXPIRED_FLAG = "expiredFlag__i";

    String THRESHOLD_ALERT_ENABLED = "thresholdAlertsEnabled__i";

    String MAP_GROUP = "mapGroup__s";

    String LATITUDE = "latitude__d";

    String LONGITUDE = "longitude__d";

    String USER_PROFILE_ID = "profileId__s";

    String TIMESTAMP = "timeStamp__s";

    String ACTION_TAKEN = "actionTaken__s";

    String MODIFIED_BY = "modifiedby__s";

    String AFFECTED_DATA = "affectedData__s";

    String ENTITY_TYPE_LONG = "entityType__l";

    String AGENT_VIEWABLE = "agentViewable__i";

    String IS_DRAFT = "isDraft__i";

    String PROFILE_ID = "profileId__s";

    String AGENT_ID = "agentId__s";

    String PROFILE_EPS = "entryPoints__sa";

    String PROFILE_QUEUES = "queues__sa";

    String PROFILE_SITES = "sites__sa";

    String PROFILE_TEAMS = "teams__sa";

    String METRIC_ID = "metricId__i";

    String FEATURE_ARRAY = "features__sa";

    String FEATURE_ATTRIBUTE = "featuresAttribute__sa";

    String FEATURE_LAUNCH_MODE_ARRAY = "featuresLaunchMode__sa";

    String FEATURE_COMPONENT_TYPE_ARRAY = "featuresComponentType__sa";

    String FEATURE_ORDER_ARRAY = "feauresOrder__ia";

    String LAUNCH_MODE = "launchMode__s";

    String ORDER = "order__i";

    String OFFSET = "offset__l";

    String INVALID_ATTEMPTS = "invalidAttempts__i";

    String MULTIPLE_TIMEZONE_ENABLED = "multipleTimeZoneEnabled__i";

    String MEDIA_SERVER_IDS = "mediaServerIds__sa";

    String MODULE_ATTRIBUTE = "attribute__s";

    String SEATMAP_ENABLED = "seatMapEnabled__i";

    String JUKEBOX_ENABLED = "jukeboxEnabled__i";

    String BANNER_COLOR = "bannerColor__s";

    // String LOGIN_IMAGE_TYPE = "loginImageType__s";

    String PORTAL_MINI_IMAGE_TYPE = "portalMiniImageType__s";

    String PORTAL_IMAGE_TYPE = "portalImageType__s";

    String AGENT_DESKTOP_IMAGE_TYPE = "agentDesktopImageType__s";

    // String LOGIN_IMAGE_ID = "loginImageId__s";

    String PORTAL_IMAGE_ID = "portalImageId__s";

    String PORTAL_MINI_IMAGE_ID = "portalMiniImageId__s";

    String AGENT_DESKTOP_IMAGE_ID = "agentDesktopImageId__s";

    String NOTIFICATION_RECEIVERS = "notificationReceivers__sa";

    String TEXT_NOTIFICATION_RECEIVERS = "textNotificationReceivers__sa";

    String DATA_TYPE = "dataType__s";

    String ENTITY_ID = "entityId__s";

    String THRESHOLD_METRIC_ID = "thresholdMetricId__i";

    String METRICS_DATA_PRECEDENCE = "metricsDataPrecedence__sa";

    String ROTATION_TYPE = "rotationType__s";

    String ENABLE_KEY_ROTATION = "enableKeyRotation__i";

    String LAST_ROTATION_TIME_STAMP = "lastRotationTimestamp__l";

    String THRESHOLD_RULE_IDS = "thresholdRuleIds__sa";

    String TOTAL_SIZE = "totalSize__l";

    String RECORD_MODIFIED_DATE = "recordModifiedDate__l";

    String AVERAGE_SIZE = "averageSize__l";

    String TOTAL_SIZE_IN_A_DAY = "totalSizeInADay__l";

    String AVERAGE_SIZE_IN_A_DAY = "averageSizeInADay__l";

    String BLOB_ID = "blobId__s";

    String TOTAL_COUNT = "totalCount__l";

    String AVERAGE_COUNT = "AverageCount__l";

    String LAST_CATALOG_UPDATED = "lastCatalogUpdated__l";

    String CURRENT_STATUS = "currentStatus__i";

    String STRATEGY_STATUS = "strategyStatus__s";

    String MAXIMUM_USERS = "maximumUsers__i";

    String MAXIMUM_ACTIVE_AGENTS = "maximumActiveAgents__i";

    String GRS = "grs__i";

    String ALL_MODULES = "allModules__i";

    String REQUEST_ID = "id";

    String FIRST_CALL_BACK_ATTEMPTED_TIMESTAMP = "firstCallbackAttemptedTimestamp__l";

    String LAST_CALL_BACK_ATTEMPTED_TIMESTAMP = "lastCallbackAttemptedTimestamp__l";

    String OUTDIAL_ENTRY_POINT_ID = "entryPointId__i";

    String CALL_BACK_NUMBER = "callBackNumber__s";

    String RESULT_OF_LAST_CALLBACK_ATTEMPTED = "resultOfLastCallBackAttempted__i";

    String CREATED_DATE = "createdDate__l";

    String CALL_BACK_TIME = "callBackTime__l";

    String ACTUAL_NUMBER_OF_CALL_BACK_ATTEMPTED = "actualNumberOfCallBackAttempted__i";

    String MM_PROVISIONING_URL = "provisioningUrl__s";

    String MM_AGENT_URL = "agentUrl__s";

    String MM_REPORT_URL = "reportUrl__s";

    String MM_PROV_DISPLAY_LABEL = "displayLabel__s";

    String MM_AGENT_DISPLAY_LABEL = "agentDisplayLabel__s";

    String MM_REPORT_DISPLAY_LABEL = "reportDisplayLabel__s";

    String TRIGGER_VALUE = "triggerValue__d";

    String CRITICAL_VALUE = "criticalValue__d";

    String PRUNING_STRATEGY = "pruningStrategy__s";

    String PRUNING_VALUE = "pruningValue__i";

    String STRATEGY = "strategy__s";

    String SUPER_USER = "superUser__i";

    String WATSON_ANALYTICS_BASE_URL = "watsonAnalyticsBaseUrl__s";

    String WATSON_ANALYTICS_USERNAME = "watsonAnalyticsUserName__s";

    String WATSON_ANALYTICS_PASSWORD = "watsonAnalyticsPassword__s";

    String SYSTEM_CODE = "isSystemCode__i";

    String TALK_DURATION = "talkDuration__l";

    String START_TIMESTAMP = "cstts";

    String END_TIMESTAMP = "cetts";

    String DELETED_FLAG = "deletedRecord__i";

    String RECORD_FILE_SIZE = "recordfilesize__l";

    String SESSION_ID = "sid";

    String CAPACITY_DATA = "capacityData__s";

    String DNIS = "dnis__s";

    String ANI = "ani__s";

    String WRAP_UP_CODE_ID = "wrapupCodeId__s";

    String QUEUE_ANALYZER_ID = "queueSystemId__s";

    String SITE_ANALYZER_ID = "siteSystemId__s";

    String TEAM_ANALYZER_ID = "teamSystemId__s";

    String AGENT_ANALYZER_ID = "agentSystemId__s";

    String WRAP_UP_ANALYZER_ID = "wrapupCodeSystemId__s";

    String DELETED_TIMESTAMP = "deletedrecordtimestamp__l";

    String AGENT_IDS = "agentIds__sa";

    String MAXIMUM_TEXT_SKILLS = "maximumTextSkills__i";

    String ALLOW_AGENT_THRESHOLDS = "allowAgentThresholds__i";

    String LOGIN_URL = "loginUrl__s";

    String CLIENT_ID = "clientId__s";

    String CLIENT_SECRET = "clientSecret__s";

    String LOGIN_SECRET = "loginSecret__s";

    String API_VERSION = "apiVersion__s";

    String ENABLED = "enabled__i";

    String PAUSE_RESUME_ENABLED = "pauseResumeEnabled__i";

    String RECORDING_PAUSE_DURATION = "recordingPauseDuration__i";

    String CALLFLOW_SCRIPT_URL = "callFlowScriptUrl__s";

    String CCXML_PARENT_DOC_URL = "ccxmlParentDocUrl__s";

    String OUTDIAL_PRIMARY_DID_URL = "outdialPrimaryDidUrl__s";

    String OUTDIAL_BACKUP_DID_URL = "outdialBackupDidUrl__s";

    String DN_TIMEOUT = "dnTimeout__i";

    String PRIMARY_PING_URL = "primaryPingUrl__s";

    String BACKUP_PING_URL = "backupPingUrl__s";

    String CHECK_AGENT_AVAILABILITY = "checkAgentAvailability__i";

    String BLOCKED_AREA_CODE_RECONSIDER_ON_ERROR = "blockedAreaCodeReconsiderOnError__i";

    String MAX_DN_RETRIES = "maximumDnRetries__i";

    String BLOCK_AREA_CODES = "blockAreaCodes__i";

    String AREA_CODES = "areaCodesToBlock__s";

    String LABELS = "labels__s";

    String P_ASSERTED_IDENTITY = "pAssertedIdentity__i";

    String DIVERSION_HEADER = "diversionHeader__i";

    String OTG = "otg__i";

    String DTG = "dtg__i";

    String LABEL = "label__s";

    String TEXT = "text__s";

    String PRIMARY_START_CALL_URL = "primaryStartCallUrl__s";

    String BACKUP_START_CALL_URL = "backupStartCallUrl__s";

    String LICENSE = "license__s";

    String PROVISIONING_TICKET = "ticketId__s";

    String OUTDIAL_ANI = "outdialAni__s";

    String TENANT_XML_URL = "tenantXmlUrl__s";

    String STREET = "street__s";

    String POSTAL_CODE = "postalCode__s";

    String STATE = "state__s";

    String COUNTRY = "country__s";

    String SPEECH_ENABLED_IVR = "configureSpeechEnabledIVREnabled__i";

    String CAMPAIGN_MANAGEMENT = "campaignManagerEnabled__i";

    String WORK_FORCE_BUNDLE = "wfoEnabled__i";

    String WEB_CALL_BACK = "webCallBackEnabled__i";

    String NUMBER_OF_AGENTS = "numberOfAgents__i";

    String NUMBER_OF_PREMIUM_AGENTS = "numberOfPremiumAgents__i";

    String NUMBER_OF_STANDARD_AGENTS = "NumberOfStandardAgents__i";

    String CITY = "city__s";

    String CCG_IDS = "ccgIds__sa";

    String CALL_INTERFACE_URL = "callInterfaceUrl__s";

    String VERSION = "version__i";

    String IP_SUBNET = "ipSubnet__s";
    
    String DEFAULT_CHAT_EP_SCRIPT = "isDefaultChatEp__i";
    
    String DEFAULT_CHAT_QUEUE_SCRIPT = "isDefaultChatQueue__i";

}
