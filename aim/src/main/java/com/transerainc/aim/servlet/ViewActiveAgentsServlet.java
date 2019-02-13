/**
 * 
 */
package com.transerainc.aim.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.springframework.util.StringUtils;

import com.transerainc.aim.lookup.Lookup;
import com.transerainc.aim.lookup.LookupHelper;
import com.transerainc.aim.pojo.ActiveAgent;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
/**
 * 
 */
public class ViewActiveAgentsServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger lgr =
			Logger.getLogger(ViewActiveAgentsServlet.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}

	/**
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String ipAddr = req.getRemoteAddr();
		String tenantId = req.getParameter("tenantId");
		String agentId = req.getParameter("agentId");
		String teamId = req.getParameter("teamId");
		String acgUrl = req.getParameter("acgUrl");
		String state = req.getParameter("subStatus");
		String channelType = req.getParameter("channelType");

		long ts1 = 0;
		long ts2 = 0;
		long ts3 = 0;

		if (lgr.isLoggable(Level.INFO)) {
			ts1 = System.currentTimeMillis();
		}
		
		if (!StringUtils.hasLength(channelType)) {
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Got request without channelType, for backward compatibility setting channelType to defaults for agent : "
						+ agentId);
			}
			channelType = "telephony";
		}

		Lookup lookup =
				LookupHelper
						.getLookup(agentId, tenantId, teamId, 
								   state, acgUrl, channelType);

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Got a view active agent request " + lookup + " from "
				+ ipAddr);
		}

		List<ActiveAgent> agentList =
				LookupHelper.getAgentsMatchingLookup(lookup);

		if (lgr.isLoggable(Level.INFO)) {
			ts2 = System.currentTimeMillis();
		}

		String xmlStr = null;
		try {
			xmlStr = LookupHelper.getActiveAgentsAsXML(agentList);
			if (lgr.isLoggable(Level.FINER)) {
				lgr.finer("Active agent xml : " + xmlStr);
			}
			resp.setContentType("text/xml; charset=UTF-8");
			resp.getWriter().println(xmlStr);
		} catch (JAXBException e) {
			throw new ServletException(e);
		}

		if (lgr.isLoggable(Level.INFO)) {
			ts3 = System.currentTimeMillis();
		}

		if (lgr.isLoggable(Level.INFO)) {
			lgr.info("Time taken: " + (ts2 - ts1) + ", " + (ts3 - ts2));
		}
	}

}
