/**
 * 
 */
package com.transerainc.aim.provisioning.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.pojo.Site;
import com.transerainc.aim.store.SiteStore;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class SiteHandler {
	private static Logger lgr = Logger.getLogger(SiteHandler.class.getName());
	private HashMap<String, Site> siteMap;
	private HashMap<String, List<String>> tenantToSiteMap;

	public SiteHandler(String dataDir) {
		SiteStore store = new SiteStore(dataDir);
		siteMap = store.retrieveSites();

		createTenantToSiteMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To Site Mapping: " + tenantToSiteMap);
			lgr.config("Sites: " + siteMap);
		}
	}

	public SiteHandler(HashMap<String, Site> siteMap, String dataDir) {
		this.siteMap = siteMap;

		createTenantToSiteMapping();

		if (lgr.isLoggable(Level.CONFIG)) {
			lgr.config("Tenant To Site Mapping: " + tenantToSiteMap);
			lgr.config("Sites: " + siteMap);
		}

		SiteStore store = new SiteStore(dataDir);
		store.storeSites(siteMap);
	}

	public Site getSite(String id) {
		Site site = siteMap.get(id);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for site " + id + " returned - " + site);
		}

		return site;
	}

	public Collection<String> getSiteIds() {
		Collection<String> keys = siteMap.keySet();

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for site ids returned - " + keys);
		}

		return keys;
	}

	public List<String> getSiteIdsForTenant(String tenantId) {
		List<String> idList = tenantToSiteMap.get(tenantId);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following site ids - " + idList
				+ ", from the map " + tenantToSiteMap);
		}

		return idList;
	}

	public List<Site> getSitesForTenant(String tenantId) {
		List<String> idList = getSiteIdsForTenant(tenantId);
		List<Site> siteList = new ArrayList<Site>();

		if (idList != null) {
			for (String id : idList) {
				Site site = siteMap.get(id);

				siteList.add(site);
			}
		} else {
			lgr.warning("No sites found for tenant " + tenantId);
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Lookup for tenant " + tenantId
				+ " returned the following sites - " + siteList);
		}

		return siteList;
	}

	/**
	 * 
	 */
	private void createTenantToSiteMapping() {
		this.tenantToSiteMap = new HashMap<String, List<String>>();

		for (String siteId : siteMap.keySet()) {
			Site site = siteMap.get(siteId);
			String tenantId = site.getTenantId();
			if (lgr.isLoggable(Level.FINEST)) {
				lgr.finest("Adding site " + siteId + " to tenant " + tenantId);
			}

			List<String> siteIdList = tenantToSiteMap.get(tenantId);
			if (tenantToSiteMap.containsKey(tenantId)) {
				siteIdList = tenantToSiteMap.get(tenantId);
			} else {
				siteIdList = new ArrayList<String>();
				tenantToSiteMap.put(tenantId, siteIdList);
			}

			siteIdList.add(siteId);
		}
	}
}
