/**
 * 
 */
package com.transerainc.aim.db;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.transerainc.aim.conf.ConfigurationManager;
import com.transerainc.aim.conf.xsd.SqlStatement;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class SqlStatementManager {
	private static Logger lgr =
			Logger.getLogger(SqlStatementManager.class.getName());
	private HashMap<String, String> queryMap;

	public SqlStatementManager(ConfigurationManager cmgr) {
		init(cmgr);
	}

	@SuppressWarnings("unchecked")
	public void init(ConfigurationManager cmgr) {
		HashMap<String, String> qMap = new HashMap<String, String>();

		List<SqlStatement> queryList =
				cmgr.getAgentInformationManager().getDatabase()
						.getSqlStatement();

		for (SqlStatement sqlStmt : queryList) {
			String name = sqlStmt.getName();
			String query = sqlStmt.getStatement();

			if (lgr.isLoggable(Level.CONFIG)) {
				lgr.config("Adding query (name:" + name + ") - " + query);
			}

			qMap.put(name, query);
		}

		queryMap = qMap;
	}

	public String getQuery(String name) {
		return queryMap.get(name);
	}
}
