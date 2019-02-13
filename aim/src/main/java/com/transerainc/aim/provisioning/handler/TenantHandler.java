/**
 * 
 */
package com.transerainc.aim.provisioning.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.Tenant;
import com.transerainc.aim.store.TenantStore;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class TenantHandler {
	private static Logger lgr = Logger.getLogger(TenantHandler.class.getName());
	private HashMap<String, Tenant> tenantMap;
	private HashMap<String, String> cnameToTenantMap;
	private String dataDir;

	public TenantHandler(String dataDir) {
		this.dataDir = dataDir;

		TenantStore store = new TenantStore(dataDir);
		tenantMap = store.retrieveTenants();

		createCompanyNameToTenantIdMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr
					.config("Company Name to Tenant Id Mapping: "
						+ cnameToTenantMap);
		}
	}

	public TenantHandler(HashMap<String, Tenant> tenantMap, String dataDir) {
		this.tenantMap = tenantMap;
		this.dataDir = dataDir;

		createCompanyNameToTenantIdMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr
					.config("Company Name to Tenant Id Mapping: "
						+ cnameToTenantMap);
		}

		TenantStore store = new TenantStore(dataDir);
		store.storeTenants(tenantMap);
	}

	/**
	 * @param tempTenantMap
	 */
	public void updateTenants(HashMap<String, Tenant> tempTenantMap) {
		for (String tenantId : tempTenantMap.keySet()) {
			Tenant tenant = tempTenantMap.get(tenantId);

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Updating Tenant " + tenantId + " with " + tenant);
			}

			tenantMap.put(tenantId, tenant);
		}

		createCompanyNameToTenantIdMapping();

		TenantStore store = new TenantStore(dataDir);
		store.storeTenants(tenantMap);
	}

	/**
	 * @param tenantId
	 * @return
	 */
	public Tenant getTenant(String tenantId) {
		Tenant tenant = tenantMap.get(tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Returning tenant for (" + tenantId + "): " + tenant);
		}

		return tenant;
	}
	
	public Set<Tenant> getTenants() {
		Set<Tenant> set = new TreeSet<Tenant>();
		set.addAll(tenantMap.values());
		return set;
	}

	public Tenant getTenantForCompanyName(String cname) {
		Tenant tenant = null;
		String tenantId = cnameToTenantMap.get(cname);

		if (tenantId != null) {
			tenant = tenantMap.get(tenantId);
		} else {
			lgr.warning("Cannot find tenant id for company name " + cname);
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Returning tenant for (" + tenantId + ":" + cname + "): "
				+ tenant);
		}

		return tenant;
	}

	public String getTenantIdForCompany(String cname) {
		String tenantId = cnameToTenantMap.get(cname);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Returning tenant id " + tenantId + " for company name "
				+ cname);
		}

		return tenantId;
	}

	public Collection<String> getTenantIdList() {
		Collection<String> keys = tenantMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant id list returned - " + keys);
		}

		return keys;
	}

	public Collection<String> getCompanyNames() {
		Collection<String> keys = cnameToTenantMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for company names returned - " + keys);
		}

		return keys;
	}

	/**
	 * 
	 */
	private void createCompanyNameToTenantIdMapping() {
		this.cnameToTenantMap = new HashMap<String, String>();

		for (String tenantId : tenantMap.keySet()) {
			Tenant tenant = tenantMap.get(tenantId);
			String companyName = tenant.getCompanyName();

			if (lgr.isLoggable(Level.FINEST)) {
				lgr.finest("Mapping company " + companyName + " to tenant "
					+ tenantId);
			}

			cnameToTenantMap.put(companyName.toLowerCase(), tenantId);
		}
	}

}
