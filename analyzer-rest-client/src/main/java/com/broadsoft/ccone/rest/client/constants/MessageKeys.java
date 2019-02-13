/**
 *
 */
package com.broadsoft.ccone.rest.client.constants;

/**
 * @author svytla
 */
public interface MessageKeys {

    String MESSAGE_ENTITY_CREATION = "message.entity.creation";

    String MESSAGE_ENTITY_UPDATION = "message.entity.updation";

    String MESSAGE_ENTITY_COPY = "message.entity.copy";

    String MESSAGE_ENTITY_DELETION = "message.entity.deletion";

    String MESSAGE_ENTITY_SUSPEND = "message.entity.suspend";

    String MESSAGE_ENTITY_PASSWORDRESET = "message.entity.passwordreset";

    String ERROR_RETRIEVE_ENTITIES = "error.retrieve.entities";

    String ERROR_INTERNAL_SERVER = "error.internal.server";

    String ERROR_RETRIEVE_METADATA = "error.retrieve.metadata";

    String ERROR_ENTITY_CREATION = "error.entity.creation";

    String ERROR_ENTITY_UPDATION = "error.entity.updation";

    String ERROR_ENTITY_COPY = "error.entity.copy";

    String ERROR_ENTITY_DELETION = "error.entity.deletion";

    String ERROR_ENTITY_SUSPEND = "error.entity.suspend";

    String ERROR_ENTITY_RETRIEVAL = "error.entity.retrieval";

    String ERROR_ENTITY_DATA_TYPE_REQUIRED = "error.entity.data.type.required";

    String ERROR_ENTITY_TYPE_REQUIRED = "error.entity.type.required";

    String ERROR_ENTITY_ATTRIBUTES_REQUIRED = "error.entity.attributes.required";

    String ERROR_ATTRIBUTE_DATA_TYPE_REQUIRED = "error.attribute.data.type.required";

    String ERROR_ENTITY_TENANT_REQUIRED = "error.entity.tenant.required";

    String ERROR_ENTITY_RECORDS_REQUIRED = "error.entity.records.required";

    String ERROR_ENTITY_DUPLICATE_RECORD = "error.entity.duplicate.record";

    String ERROR_ENTITY_DELETE_DEFAULT = "error.entity.cannot.delete.default";

    String ERROR_ENTITY_DEFAULT_REQUIRED = "error.entity.default.required";

    String ERROR_ENTITY_CANNOT_MAKE_NON_DEFAULT = "error.entity.cannot.make.non.default";

    String ERROR_ATTRIBUTE_DATA_TYPE_MISMATCH = "error.attribute.data.type.mismatch";

    String ERROR_ATTRIBUTE_LENGTH_EXCEEDED_LIMIT = "error.attribute.length.exceeded.limit";

    String ERROR_ATTRIBUTE_VALUE_REQUIRED = "error.attribute.value.required";

    String ERROR_NAME_REQUIRED = "error.name.required";

    String ERROR_TENANT_TYPE_REQUIRED = "error.tenant.type.required";

    String ERROR_TIMEZONE_REQUIRED = "error.timezone.required";

    String ERROR_PARENT_REQUIRED = "error.parent.required";

    String ERROR_ADMINISTRATOR_REQUIRED = "error.administrator.required";

    String ERROR_CUSTOMER_REQUIRED = "error.customer.required";

    String ERROR_DUPLILCATE_USER = "error.duplicate.user";

    String ERROR_LOGIN_NAME_REQUIRED = "error.login.required";

    String ERROR_EMAIL_REQUIRED = "error.email.required";

    String ERROR_PASSWORD_REQUIRED = "error.password.required";

    String ERROR_DUPLICATE_DN = "error.duplicate.dn";

    String ERROR_CSV_PARSE = "error.csv.parse";

    String MESSAGE_SAVED_SUCCESSFULLY = "message.saved.successfully";

    String ERROR_MEDIA_PROFILE_ASSIGNED_TO_MM_EXTENSION = "error.media.profile.assigned.media.extension";

    String ERROR_ENTITIES_UPDATION_FAILED = "error.entities.updation.failed";

    String ERROR_ENTITY_NOT_EXISTS = "error.entity.not.exists";

    String ERROR_ENTITY_NOT_BELONGS_TO_PARENT = "error.entity.not.belongs.to.parent";

    String ERROR_CSV_COLUMN_STRING = "error.csv.validation.alpha.numeric.with.space";

    String ERROR_CSV_COLUMN_EMAIL_ID = "error.csv.email.address";

    String ERROR_CSV_COLUMN_NUMBER = "error.csv.number";

    String MESSAGE_BULK_UPLOAD_INITIATION = "message.csv.bulk.upload.init";

    String MESSAGE_BULK_DELETE_INITIATION = "message.csv.bulk.delete.init";

    String ERROR_SETTING_MODEL_ATTRIBUTES = "error.setting.model.attribute";

    String ERROR_INVALID_CSV_HEADERS = "error.invalid.csv.headers";

    String ERROR_CSV_FIELDS_VALUES_MISMATCH = "error.csv.fields.values.mismatch";

    String ERROR_BLANK_FILE = "error.blank.file";

    String ERROR_INVALID_FILE_CONTENT = "error.tfn.mapping.invalid.content";

    String ERROR_NO_DN = "error.tfn.no.dn.provided";

    String ERROR_INVALID_ENTRY_POINT = "error.tfn.invalid.entry.point";

    String MESSAGE_TFN_UPLOAD_ADD_SUCCESS = "message.tfn.upload.add.success";

    String MESSAGE_TFN_UPLOAD_DELETE_SUCCESS = "message.tfn.upload.delete.success";

    String ERROR_TFN_UPLOAD_FAILURE = "message.tfn.upload.failure";

    String ERROR_TFN_INVALID_RECORD = "error.tfn.invalid.record";

    String MESSAGE_NO_DATA_TO_EXPORT = "message.no.data.to.export";

    String LABEL_TFN_FILE_NAME = "label.tfn.file.name";

    String ERROR_EXPORT_DATA = "error.export.data";

    String ENTRY_POINT_ID = "label.entry.point.id";

    String ENTRY_POINT_NAME = "label.entry.point.name";

    String DN = "label.dn";

    String SITE_ID = "label.site.id";

    String SITE_NAME = "label.site.name";
    String ERROR_REPLICATE_RECORD = "error.replicate.record";

    String ERROR_DELETE_HAS_DEPENDENCY = "error.delete.has.dependency";

    String ERROR_COMMUNICATION_INTERRUPTION = "error.communication.interruption";

    String ERROR_ENTITY_CONFLICT = "error.entity.conflict";

    String PRIMARY_MODE = "label.primary";

    String BACKUP_MODE = "label.backup";

    String EMAILS = "label.emails";

    String CHATS = "label.chats";

    String FAXS = "label.faxes";

    String VIDEOS = "label.video.calls";

    String OTHERS = "label.others";

    String ERROR_PASSWORD_LENGTH = "error.password.length";

    String ERROR_PASSWORD_REGEX = "error.password.regex";

    String ERROR_NO_ENTERPRISES = "error.no.enterprises";

    String MESSAGE_EXPIRY_DATE = "message.expiry.date";

    String MAXIMUM_LIMIT_EXCEEDED = "error.maximum.limit.exceeded";

    String MESSAGE_BULK_OPERATION_INITIATION = "message.bulk.operation.init";

    String ERROR_BULK_DELETE_INITIATION = "error.bulk.delete.initiation";

    String CAPACITY = "label.site.capacity";

    String PROVISIONED_ITEMS = "label.provisioned.items";

    String COMPANY = "label.company";

    String USER = "label.user";

    String ACTIVE_SITES = "label.active.sites.details";

    String PROVISIONED_ITEM_REPORT = "label.provisioned.items.report";

    String SITE = "label.site";

    String ID = "label.id";

    String NAME = "label.name";

    String PRIORITY = "label.priority";

    String TEAM_TYPE = "label.team.type";

    String TEAM_STATUS = "label.team.status";

    String TEAM_DN = "label.team.dn";

    String ACTIVE_TEAMS = "label.active.team.details";

    String ACTIVE_INBOUND_EP = "label.active.inbound.ep";

    String ACTIVE_INBOUND_QUEUES = "label.active.inbound.queues";

    String ACTIVE_OUTBOUND_EP = "label.active.outbound.ep";

    String ACTIVE_OUTBOUND_QUEUES = "label.active.outbound.queues";

    String ACTIVE_AGENTS = "label.active.agent.details";

    String PERMIT_MONITORING = "label.permit.monitoring";

    String PERMIT_PARKING = "label.permit.parking";

    String PERMIT_RECORDING = "label.permit.recording";

    String SERVICE_LVL_THRLD = "label.service.level.threshold";

    String MAXIMUM_ACTIVE_CALLS = "label.max.active.calls";

    String MAXIMUM_TIME_IN_QUEUE = "label.maximum.time.in.queue";

    String OVERFLOW_NUMBER = "label.overflow.number";

    String RECORD_ALL_CALLS = "label.record.all.calls";

    String DEDICATED = "label.dedicated";

    String PERMIT_RETRANSEFER = "label.permit.retransfer";

    String STATUS = "label.status";

    String YES = "label.yes";

    String NO = "label.no";

    String USERNAME = "label.username";

    String EXTERNAL_ID = "label.external.id";

    String AGENT_PROFILE = "label.agent.profile";

    String AGENT_SKILLS = "label.agent.skill";

    String TEAMS = "label.teams";

    String MESSAGE_NO_RECORDS_AVAILABLE = "message.no.records.available";

    String AGENT_ID = "label.agent.id";

    String AGENT_NAME = "label.agent.name";

    String SKILL = "label.skill.details";

    String SKILL_TYPE = "label.skill.type";

    String PROFILE_DATA = "label.profile.data";

    String SKILL_PROFILE_NAME = "label.skill.profile.name";

    String SKILL_PROFILE_ID = "label.skill.profile.id";

    String SKILL_NAME = "label.skill.name";

    String SKILL_ID = "label.skill.id";

    String SKILL_VALUE = "label.skill.value";

    String ACTIVE_SKILL_PROFILE = "label.active.skill.profile.details";

    String AGENT_PROFILE_DETAILS = "label.agent.profile.details";

    String GENERAL_INFORMATION = "label.general.information";

    String AUXILIARY_CODES = "label.auxiliary.codes";

    String DIAL_PLAN = "label.dail.plan";

    String AGENT_DN_VALIDATION = "label.agent.dn.validation";

    String AGENT_VIEWABLE_STATISTICS = "label.agent.viewable.statistics";

    String AGENT_THRESHOLDS = "label.agent.thresholds";

    String COLLABORATION = "label.collaboration";

    String DEPENDENCY_MODEL_HEADER_NAME = "label.dependency.model.header.name";

    String ERROR_TEAM_HAS_SKILL_ASSIGNMENT = "error.team.has.skill.assignment";

    String ERROR_DOWNLOAD_REPORT = "error.download.report";

    String ERROR_EMAIL_REPORT = "error.email.report";

    String MESSAGE_EMAIL_REPORT = "message.email.report";

    String DEPENDENCY_MODEL_HEADER_NAME_WITH_STRATEGIES = "label.dependency.model.header.name.with.strategies";

    String MM_EXTENSION_REFERENCE_VALIDATION = "label.multi.media.extension.reference.validation";

    String SPEED_DIAL_LIST = "label.speed.dial.list";

    String MESSAGE_CREATED = "message.created";

    String MESSAGE_UPDATED = "message.updated";

    String MESSAGE_DELETED = "message.deleted";

    String MESSAGE_GET = "message.get";

    String ERROR_CSV_ENTITY_ALREADY_DELETED = "error.csv.entity.already.deleted";

    String ERROR_CSV_AGENT_ALREADY_EXISTS_WITH_LOGIN = "error.csv.agent.already.exists.with.login";

    String TIMESTAMP = "label.time.stamp";

    String ACTION_TAKEN = "label.action.taken";

    String MODIFIED_BY = "label.modified.by";

    String TENANT_ID = "label.tenant.id";

    String AFFECTED_DATA = "label.affected.data";

    String ERROR_DUPLICATE_VENDOR_ID = "error.duplicate.vendor.id";

    String PROFILE_ID = "label.profile.id";

    String ERROR_CONFLICT_MESSAGE = "error.conflict.message";

    String ERROR_RETRIVING_CUSTOMER_DETAILS = "error.in.retrieving.customer.details";

    String ERROR_TEAM_ENTITY_CONFLICT = "error.team.conflict.message";

    String MESSAGE_CUSTOMIZATION_CREATION = "message.branding.saved.success";

    String TEAM_ID = "label.team.id";

    String ERROR_THRESHOLD_RULE_ENTITY_CONFLICT = "error.threshold.rule.conflict.message";

    String ERROR_NETWORK_ISSUE = "error.network.issue";

    String ERROR_500_MESSAGE = "error.500.message";

    /* Settings tab */
    String ERROR_USER_NOT_FOUND = "error.user.not.found";

    String ERROR_RESET_PASSWORD = "error.password.reset";

    String ERROR_INVALID_CREDENTIALS = "error.invalid.credentials";

    String ERROR_PASSWORD_EXPIRED = "error.password.expired";

    String ERROR_MUST_CHANGE_PASSWORD = "error.must.change.password";

    String ERROR_ACCOUNT_LOCKED = "error.account.locked";

    String ERROR_PASSWORD_POLICY = "error.password.policy";

    String MESSAGE_CHANGE_PASSWORD_SUCCESS = "message.change.password.success";

    String ERROR_CHANGE_PASSWORD = "error.password.change";

    String MESSAGE_RESET_PASSWORD_SUCCESS = "message.reset.password.success";

    String MESSAGE_TIMEZONE_UPDATE_SUCCESS = "message.time.zone.update.success";

    String ERROR_TIMEZONE_UPDATE = "error.time.zone.update";

    String ERROR_CSV_COLUMN_PASSWORD = "error.csv.validation.password";

    String MESSAGE_QUERY = "label.query";

    String ERROR_RELEASE_NOTES_FILE_UPLOAD = "error.release.notes.file.upload";

    String MESSAGE_RELEASE_NOTES_FILE_UPLOAD = "message.release.notes.file.upload";

    String SOURCE_NAME = "label.source.name";

    String SOURCE_ID = "label.source.id";

    String SOURCE_TYPE = "label.source.type";

    String DESTINATION_NAME = "label.destination.name";

    String DESTINATION_ID = "label.destination.id";

    String DESTINATION_TYPE = "label.destination.type";

    String ALL_ROUTING_DETAILS = "label.all.routing.details";

    String ACTIVE_ROUTING_DETAILS = "label.active.routing.details";

    String CURRENT_ROUTING_DETAILS = "label.current.routing.details";

    String REPORT = "label.report";

    String ERROR_GET_PASSWORD_POLICY = "error.get.password.policy";

    String ERROR_CONVERT_SALTED_PASSWORD = "error.convert.salted.policy";

    String ERROR_MAX_ALLOWED_USER_EXCEEDS = "error.max.allowed.user.exceeds";

    String ERROR_CSV_MAX_ALLOWED_AGENT_EXCEEDS = "error.csv.max.allowed.agent.exceeds";

    String ERROR_ENTITY_NOT_EXISTS_OR_NO_ACCESS = "error.entity.not.exists.or.no.access";

    String ERROR_ENTITY_NO_PARENT_OR_NO_ACCESS = "error.entity.not.belongs.to.parent.or.no.access";

    String ERROR_MAX_ALLOWED_AGENT_EXCEEDS = "error.max.allowed.agent.exceeds";

    String MESSAGE_ENTITY_SUCCESS = "message.entity.success";

    String ERROR_CCG_ENTRY_ALREADY_EXISTS = "error.ccg.entry.already.exists";

    String ERROR_INVALID_RANGE = "error.invalid.range";

    String ERROR_NON_NUMERIC = "error.non.numeric";

    String MONITORING_SCHEDULE = "label.monitoring.schedule";

    String RECORDING_SCHEDULE = "label.recording.schedule";

    String ROUTING_STRATEGIES = "label.routing.strategies";

    String QUEUE_PRIORITY_STRATEGY = "label.queue.priority.strategy";

    String MULTI_MEDIA_ENABLER = "label.multimedia.enabler";

    String MULTIMEDIA_EXTENSION = "label.multimedia.extension";

    String VPOP = "label.vpop";

    String OUTDIAL_CONFIGURATION = "label.outdial.configuration";

    String MEDIA_GATEWAY = "label.media.gateway";

    String CCG = "label.ccg";

    String TEAM = "label.teams";

    String TFN = "label.dn";

    String USERS = "label.users";

    String DEPENDENCY_MODEL_HEADER_NAME_WITH_ENUM_SKILL = "label.dependency.model.header.name.with.enum.skill";

    String VIRTUAL_TEAM = "label.virtual.teams";

    String ASSOCIATED_QUEUES = "label.associated.queues";

    String ERROR_VIRTUAL_TEAM_REFERENCE = "label.virtual.team.reference";

    String ERROR_AGENT_HAS_SKILL_ASSIGNMENT = "error.agent.has.skill.assignment";

    String ERROR_CSV_ACCOUNT_STATUS = "error.bulk.upload.invalid.account.status";

    String TENANT_CREATION_INFORMATION = "message.tenant.creation.information";

    String ERROR_MM_ENABLER_LABEL_CONFLICT = "error.mm.enabler.label.conflict";

    String ERROR_INVALID_EMAIL_ADDRESS = "error.invalid.email.address";

    String ERROR_AGENT_HAS_DEPENDENCIES = "error.agent.has.dependencies";

    String ERROR_AGENT_HAS_SKILL_PROFILE = "error.agent.has.skill.profile";

    String NONE = "label.none";

    String ERROR_COMPONENT_REFERENCED = "error.component.referenced";

    String MAXIMUM_TEXT_SKILL_LIMIT_EXCEEDED = "error.maximum.text.skill.limit.exceeded";

    String MESSAGE_PASSWORD_CHANGE_RESET_SUBJECT = "message.password.change.reset.subject";

    String SITES = "label.sites";

    String AGENTS = "label.agents";

    String INBOUND_EPS = "label.inbound.entry.points";

    String OUTDIAL_EPS = "label.outdial.entry.points";

    String INBOUND_QUEUES = "label.inbound.queues";

    String OUTDIAL_QUEUES = "label.outdial.queues";

    String SERVICE_PROVIDER = "label.service.provider";

    String TENANT = "label.tenant";

    String ERROR_CSV_LOGIN_ADDRESS = "error.csv.login.address";

    String ERROR_INVALID_DN = "message.validation.tfn.bulk.upload";

    String ERROR_GENERATE_API_KEY = "error.generate.api.key";

    String ERROR_REGENERATE_API_KEY = "error.regenerate.api.key";

    String ERROR_REVOKE_API_KEY = "error.revoke.api.key";

    String GENERATE_API_KEY_SUCCESS = "message.generate.api.key.success";

    String REGENERATE_API_KEY_SUCCESS = "message.regenerate.api.key.success";

    String REVOKE_API_KEY_SUCCESS = "message.revoke.api.key.success";
}
