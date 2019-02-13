/**
 * 
 */
package com.transerainc.aim.util;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.ws.jaxme.impl.JMMarshallerImpl;
import org.xml.sax.InputSource;

import com.transerainc.agent.profile.AgentProfile;
import com.transerainc.aha.gen.agent.ChannelSet;
import com.transerainc.aha.gen.agent.RoutingData;
import com.transerainc.aha.gen.agent.SkillSet;
import com.transerainc.aim.conf.xsd.AgentInformationManager;
import com.transerainc.aim.conf.xsd.AuthenticationFailureListType;
import com.transerainc.aim.tpgintf.ActiveAgentListType;
import com.transerainc.aim.tpgintf.LoginStatusType;
import com.transerainc.aim.tpgintf.NotificationWrapperType;
import com.transerainc.provisioning.notificationserver.NotificationServerMessage;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */

public class JAXBHelper {
	private static JAXBContext aamContext;
	private static JAXBContext aimTpgContext;
	private static JAXBContext nsContext;
	private static JAXBContext ahaContext;
	private static JAXBContext agentContext;

	public static AgentInformationManager getAgentInformationManagerType(
			InputSource reader) throws JAXBException {
		if (aamContext == null) {
			aamContext = JAXBContext
					.newInstance("com.transerainc.aim.conf.xsd");
		}

		Unmarshaller unmarshaller = aamContext.createUnmarshaller();

		return (AgentInformationManager) unmarshaller.unmarshal(reader);
	}

	public static String getAgentInformationManagerAsString(
			AgentInformationManager data, boolean format) throws JAXBException {
		if (aamContext == null) {
			aamContext = JAXBContext
					.newInstance("com.transerainc.aim.conf.xsd");
		}

		StringWriter sw = new StringWriter();
		Marshaller marshaller = aamContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
				Boolean.TRUE);
		marshaller.marshal(data, sw);

		return sw.toString();
	}

	public static AuthenticationFailureListType getAuthorizationFailureListType(
			InputSource reader) throws JAXBException {
		if (aamContext == null) {
			aamContext = JAXBContext
					.newInstance("com.transerainc.aim.conf.xsd");
		}

		Unmarshaller unmarshaller = aamContext.createUnmarshaller();

		return (AuthenticationFailureListType) unmarshaller.unmarshal(reader);
	}

	public static String getAuthorizationFailureListAsString(
			AuthenticationFailureListType data, boolean format)
			throws JAXBException {
		if (aamContext == null) {
			aamContext = JAXBContext
					.newInstance("com.transerainc.aim.conf.xsd");
		}

		StringWriter sw = new StringWriter();
		Marshaller marshaller = aamContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
				Boolean.TRUE);
		marshaller.marshal(data, sw);

		return sw.toString();
	}

	public static LoginStatusType getLoginStatusType(InputSource reader)
			throws JAXBException {
		if (aimTpgContext == null) {
			aimTpgContext = JAXBContext
					.newInstance("com.transerainc.aim.tpgintf");
		}

		Unmarshaller unmarshaller = aimTpgContext.createUnmarshaller();

		return (LoginStatusType) unmarshaller.unmarshal(reader);
	}

	public static String getLoginStatusAsString(LoginStatusType data,
			boolean format) throws JAXBException {
		if (aimTpgContext == null) {
			aimTpgContext = JAXBContext
					.newInstance("com.transerainc.aim.tpgintf");
		}

		StringWriter sw = new StringWriter();
		Marshaller marshaller = aimTpgContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
				Boolean.TRUE);
		marshaller.marshal(data, sw);

		return sw.toString();
	}

	public static JAXBContext getTPGContext() throws JAXBException {
		if (aimTpgContext == null) {
			aimTpgContext = JAXBContext
					.newInstance("com.transerainc.aim.tpgintf");
		}

		return aimTpgContext;
	}

	public static ActiveAgentListType getActiveAgentListType(InputSource reader)
			throws JAXBException {
		if (aimTpgContext == null) {
			aimTpgContext = JAXBContext
					.newInstance("com.transerainc.aim.tpgintf");
		}

		Unmarshaller unmarshaller = aimTpgContext.createUnmarshaller();

		return (ActiveAgentListType) unmarshaller.unmarshal(reader);
	}

	public static String getActiveAgentListAsString(ActiveAgentListType data,
			boolean format) throws JAXBException {
		if (aimTpgContext == null) {
			aimTpgContext = JAXBContext
					.newInstance("com.transerainc.aim.tpgintf");
		}

		StringWriter sw = new StringWriter();
		Marshaller marshaller = aimTpgContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
				Boolean.TRUE);
		marshaller.marshal(data, sw);

		return sw.toString();
	}

	public static NotificationWrapperType getNotificationWrapperType(
			InputSource reader) throws JAXBException {
		if (aimTpgContext == null) {
			aimTpgContext = JAXBContext
					.newInstance("com.transerainc.aim.tpgintf");
		}

		Unmarshaller unmarshaller = aimTpgContext.createUnmarshaller();

		return (NotificationWrapperType) unmarshaller.unmarshal(reader);
	}

	public static String getNotificationWrapperAsString(
			NotificationWrapperType data, boolean format) throws JAXBException {
		if (aimTpgContext == null) {
			aimTpgContext = JAXBContext
					.newInstance("com.transerainc.aim.tpgintf");
		}

		StringWriter sw = new StringWriter();
		Marshaller marshaller = aimTpgContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
				Boolean.TRUE);
		marshaller.marshal(data, sw);

		return sw.toString();
	}

	public static String getNotificationServerMessageAsString(
			NotificationServerMessage msg) throws JAXBException {
		if (nsContext == null) {
			nsContext = JAXBContext
					.newInstance("com.transerainc.provisioning.notificationserver");
		}

		StringWriter sw = new StringWriter();
		Marshaller marshaller = nsContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
				Boolean.TRUE);
		marshaller.marshal(msg, sw);

		return sw.toString();
	}

	public static NotificationServerMessage getNotificationServerMessage(
			InputSource src) throws JAXBException {
		if (nsContext == null) {
			nsContext = JAXBContext
					.newInstance("com.transerainc.provisioning.notificationserver");
		}

		Unmarshaller unmarshaller = nsContext.createUnmarshaller();

		return (NotificationServerMessage) unmarshaller.unmarshal(src);
	}

	public static NotificationServerMessage getNotificationServerMessage(
			InputStream src) throws JAXBException {
		if (nsContext == null) {
			nsContext = JAXBContext
					.newInstance("com.transerainc.provisioning.notificationserver");
		}

		Unmarshaller unmarshaller = nsContext.createUnmarshaller();

		return (NotificationServerMessage) unmarshaller.unmarshal(src);
	}

	/**
	 * @param routingData
	 * @return
	 * @throws JAXBException
	 */
	public static String getRoutingDataAsString(RoutingData routingData)
			throws JAXBException {
		if (ahaContext == null) {
			ahaContext = JAXBContext
					.newInstance("com.transerainc.aha.gen.agent");
		}

		StringWriter sw = new StringWriter();
		Marshaller marshaller = ahaContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
				Boolean.TRUE);
		marshaller.marshal(routingData, sw);

		return sw.toString();
	}

	public static String getChannelSetAsString(ChannelSet channelSet)
			throws JAXBException {
		if (ahaContext == null) {
			ahaContext = JAXBContext
					.newInstance("com.transerainc.aha.gen.agent");
		}

		StringWriter sw = new StringWriter();
		Marshaller marshaller = ahaContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
				Boolean.TRUE);
		marshaller.marshal(channelSet, sw);

		return sw.toString();
	}

	public static String getSkillSetAsString(SkillSet skillSet)
			throws JAXBException {
		if (ahaContext == null) {
			ahaContext = JAXBContext
					.newInstance("com.transerainc.aha.gen.agent");
		}

		StringWriter sw = new StringWriter();
		Marshaller marshaller = ahaContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
				Boolean.TRUE);
		marshaller.marshal(skillSet, sw);

		return sw.toString();
	}

	public static String getMediaProfileAsString(
			com.transerainc.aha.gen.agent.MediaProfile mprofile)
			throws JAXBException {
		if (ahaContext == null) {
			ahaContext = JAXBContext
					.newInstance("com.transerainc.aha.gen.agent");
		}

		StringWriter sw = new StringWriter();
		Marshaller marshaller = ahaContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		marshaller.setProperty(JMMarshallerImpl.JAXME_XML_DECLARATION,
				Boolean.TRUE);
		marshaller.marshal(mprofile, sw);

		return sw.toString();
	}
	
	public static AgentProfile getAgentProfile(
			String apText) throws JAXBException {
		if (agentContext == null) {
			agentContext = JAXBContext
					.newInstance("com.transerainc.agent.profile");
		}

		Unmarshaller unmarshaller = agentContext.createUnmarshaller();

		return (AgentProfile) unmarshaller.unmarshal(new StreamSource( new StringReader( apText ) ) );
	}
}
