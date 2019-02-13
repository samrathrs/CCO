/**
 * 
 */
package com.transerainc.aim.conf;

import java.io.FileReader;
import java.io.StringReader;
import java.net.InetAddress;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.transerainc.aim.conf.xsd.AgentInformationManager;
import com.transerainc.aim.conf.xsd.TenantType;
import com.transerainc.aim.conf.xsd.Xpath;
import com.transerainc.aim.pojo.TenantShutdownRequest;
import com.transerainc.aim.snmp.TrapManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.util.JAXBHelper;
import com.transerainc.security.SignatureHelper;
import com.transerainc.tam.config.AccessDetails;
import com.transerainc.tam.config.AccessDetails.AnalyzerAccessDetails;
import com.transerainc.tam.config.ConfigAgentHelper;
import com.transerainc.tam.config.PopMapping;
import com.transerainc.tam.config.ServerMapping;
import com.transerainc.tam.xslt.TAMXslTransformer;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class ConfigurationManager {
	private static Logger lgr =
			Logger.getLogger(ConfigurationManager.class.getName());
	private String hostname;
	private String instanceName;
	private String configAgentUrl;
	private String serverConfigXpath;
	private String xsltFile;
	private int operationMode = -1;
	private AgentInformationManager agentInformationManager;
	private HashMap<String, String> xpathMap;
	private String myUrl;
	private List<String> instanceList;
	private HashMap<String, TenantShutdownRequest> shutdownRequestMap;
	private HashMap<String, String> seratelUiMap;
	private AnalyzerAccessDetails analyzerAccessDetails;

	public ConfigurationManager(String instanceName, String configAgentUrl,
			String xpath, String xsltFile) throws AIMConfigurationException {
		try {
			hostname = InetAddress.getLocalHost().getCanonicalHostName();

			this.instanceName = instanceName;
			this.configAgentUrl = configAgentUrl;
			this.serverConfigXpath = xpath;
			this.xsltFile = xsltFile;

			// Now read the config file from the config agent
			readConfiguration();
			readServerMapping();			
			readSeratelUiServerMapping();
		} catch (Exception e) {
			lgr.log(Level.SEVERE, "Error while reading configuration", e);
			TrapManager tmgr =
					(TrapManager) AgentInformationManagerSystem
							.findBean("trapManager");

			tmgr.sendServerConfigurationError(instanceName + ".xml", hostname,
				configAgentUrl, xpath, xsltFile, e.getMessage());

			throw new AIMConfigurationException(e);
		}
	}

	/**
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void readServerMapping() throws Exception {
		List<Xpath> xpathList = agentInformationManager.getXpaths().getXpath();
		xpathMap = new HashMap<String, String>();

		for (Xpath xpath : xpathList) {
			String name = xpath.getName();
			String value = xpath.getValue();

			if (lgr.isLoggable(Level.FINER)) {
				lgr.finer("Adding XPath " + name + ":" + value);
			}

			xpathMap.put(name, value);
		}

		String xpath = xpathMap.get("aim.server.mapping");

		// String formattedXpath =
		// MessageFormat.format(xpath, hostname, instanceName);
		List<PopMapping> smList =
				ConfigAgentHelper.getPopMappings(configAgentUrl, xpath);

		instanceList = new ArrayList<String>();
		for (PopMapping sMapping : smList) {
			if (instanceName.equals(sMapping.getInstanceName())
				&& hostname.equals(sMapping.getHost())) {
				operationMode = sMapping.getOperatingMode();
				myUrl =
						"http://" + sMapping.getHost() + ":"
							+ sMapping.getPort() + "/aim";
			} else {
				String url =
						"http://" + sMapping.getHost() + ":"
							+ sMapping.getPort();
				// Avoid duplicates
				if (!instanceList.contains(url)) {
					instanceList.add(url);
				}
			}
		}

	}
	
	/**
	 * @throws Exception
	 * 
	 */
	public void readSeratelUiServerMapping() throws Exception {

		String xpath = xpathMap.get("seratelui.server.mappings");
		List<ServerMapping> smList =
				ConfigAgentHelper.getServerMappings(configAgentUrl, xpath);			
		seratelUiMap = new HashMap<String, String>();
		for (ServerMapping sMapping : smList) {
			if (0 == sMapping.getOperatingMode()){//primary
				long tenantId =sMapping.getEnterpriseId();
				String serviceUrl = "http://" + sMapping.getHost() + ":"
							+ sMapping.getPort() + "/seratelui/thirdPartyVendorIntegrationServlet";
				lgr.fine(" Detected Seratel UI entry: " + serviceUrl);
				seratelUiMap.put(String.valueOf(tenantId), serviceUrl);
				
			} else {
				lgr.warning("Ignoring Seratel UI entry " + sMapping.getHost() + " " +
			    sMapping.getPort() +" as its mode is " + sMapping.getOperatingMode());
			}
		}

	}

	public List<String> getInstanceList() {
		return instanceList;
	}

	public String getXpath(String name) {
		return xpathMap.get(name);
	}

	public String getMyUrl() {
		return myUrl;
	}

	public String getHostname() {
		return hostname;
	}

	/**
	 * @return the instanceName
	 */
	public String getInstanceName() {
		return instanceName;
	}

	/**
	 * @return the configAgentUrl
	 */
	public String getConfigAgentUrl() {
		return configAgentUrl;
	}

	/**
	 * @return the serverConfigXpath
	 */
	public String getServerConfigXpath() {
		return serverConfigXpath;
	}

	public AgentInformationManager getAgentInformationManager() {
		return agentInformationManager;
	}

	public List<String> getShutdownTenantIdList() {
		List<String> idList = new ArrayList<String>();

		if (shutdownRequestMap != null) {
			idList.addAll(shutdownRequestMap.keySet());
		}
		return idList;
	}

	public boolean isTenantShutdown(String tenantId) {
		boolean b = false;
		if (shutdownRequestMap.containsKey(tenantId)) {
			b = true;
		}

		return b;
	}

	public boolean isSubStatusAllowed(String tenantId, String subStatus) {
		boolean b = true;
		if (shutdownRequestMap.containsKey(tenantId)) {
			TenantShutdownRequest tsr = shutdownRequestMap.get(tenantId);
			b = tsr.isSubStateAllowed(subStatus);
		}

		return b;
	}

	/**
	 * @throws AIMConfigurationException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void readConfiguration() throws AIMConfigurationException {
		String xpath =
				MessageFormat.format(serverConfigXpath, hostname, instanceName);

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr
					.config("Will get the server configuration using xpath "
						+ xpath);
		}

		try {
			String serverConfigXML =
					ConfigAgentHelper.sendQuery(configAgentUrl, xpath, 16);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Got the following from config agent: "
					+ serverConfigXML);
			}

			String xformedXML =
					TAMXslTransformer.transform(new StringReader(
							serverConfigXML), new FileReader(xsltFile));

			System.out.println("Got back: " + xformedXML);

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Here is the transformed XML: " + xformedXML);
			}

			this.agentInformationManager =
					JAXBHelper.getAgentInformationManagerType(new InputSource(
							new StringReader(xformedXML)));

			HashMap<String, TenantShutdownRequest> tsrMap =
					new HashMap<String, TenantShutdownRequest>();
			List<TenantType> stList =
					agentInformationManager.getShutdownTenantList().getTenant();
			for (TenantType tType : stList) {
				String id = tType.getId();
				List<String> ssList = new ArrayList<>();
				String states = tType.getLogoutStates();
				if (states != null) {
					String[] tokens = states.split(",");
					for (String token : tokens) {
						ssList.add(token);
					}
				}

				TenantShutdownRequest tsr = new TenantShutdownRequest();
				tsr.setSubStatusList(ssList);
				tsr.setTenantId(id);

				tsrMap.put(id, tsr);
			}

			shutdownRequestMap = tsrMap;
			initializeAnalyzerDetails();
		} catch (Exception e) {
			lgr.log(Level.SEVERE,
				"Error while getting server config from config agent "
					+ configAgentUrl + " using xpath " + xpath, e);
			throw new AIMConfigurationException(e);
		}
	}

	private void initializeAnalyzerDetails() throws Exception {
		// Fetch the analyzer access details from ConfigAgent
		AccessDetails ad = getAccessDetails();
		if (ad != null) {
			AnalyzerAccessDetails analyzerAccessDetails = ad
					.getAnalyzerAccessDetails();
			if (analyzerAccessDetails != null) {
				String signedKey = new SignatureHelper().computeSignature(
						analyzerAccessDetails.getFrom(),
						analyzerAccessDetails.getApiKey());
				// Store it back as the API Key
				analyzerAccessDetails.setApiKey(signedKey);
				this.setAnalyzerAccessDetails(analyzerAccessDetails);
			}
		}

	}

	private AccessDetails getAccessDetails() throws Exception {
		String xpath = "/server/host[@id='analyzer-v1']/process[@id='access-details']";
		if (lgr.isLoggable(Level.FINER)) {
			lgr.finer("Fetching access details using xpath " + xpath + " from "
					+ configAgentUrl);
		}
		String configStr = ConfigAgentHelper.sendQuery(configAgentUrl, xpath,
				16);
		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("ConfigAgent " + configAgentUrl + " returned " + configStr
					+ " for xpath " + xpath);
		}
		AccessDetails ac = new Gson().fromJson(configStr, AccessDetails.class);
		return ac;
	}

	/**
	 * @return the operationMode
	 */
	public int getOperationMode() {
		return operationMode;
	}

	/**
	 * @param operationMode the operationMode to set
	 * 0 is primary, 1 is backup
	 */
	public void setOperationMode(int operationMode) {
		this.operationMode = operationMode;
	}
	
	public String getSeratelUiUrl(String tenantId){
		return seratelUiMap.get(tenantId);
	}

	public AnalyzerAccessDetails getAnalyzerAccessDetails() {
		return analyzerAccessDetails;
	}

	public void setAnalyzerAccessDetails(AnalyzerAccessDetails analyzerAccessDetails) {
		this.analyzerAccessDetails = analyzerAccessDetails;
	}
}
