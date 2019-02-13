/**
 * 
 */
package com.transerainc.aim.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import com.transerainc.aim.login.LoginHandler;
import com.transerainc.aim.pojo.LoginStatus;
import com.transerainc.aim.util.LoginStatusUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class AgentPropertiesServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger lgr =
			Logger.getLogger(AgentPropertiesServlet.class.getName());

	public AgentPropertiesServlet() {

	}

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
		long ts1 = System.currentTimeMillis();
		String ipAddr = req.getRemoteAddr();
		String agentId = req.getParameter("agentId");
		
		try {
			resp.setContentType("text/xml; charset=UTF-8");
			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Got a request for Agent Login Status for agent: "
					+ agentId +  " from TPG" + ipAddr);
			}

			LoginHandler lHandler = new LoginHandler();
			LoginStatus status = lHandler.getLoginStatus(agentId);

			if (lgr.isLoggable(Level.FINER)) {
				lgr.finer("Returning agent login status " + status);
			}

			long ts2 = System.currentTimeMillis();

			String xmlStr = LoginStatusUtil.getLoginStatusAsXmlString(status);
			// Send the data (as xml) to the client.

			long ts3 = System.currentTimeMillis();

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Returning Login Status: " + xmlStr);
			}

			resp.getWriter().println(xmlStr);
			resp.getWriter().flush();

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Time taken to login: " + (ts2 - ts1)
					+ ", XML Conversion: " + (ts3 - ts2));
			}
		} catch (JAXBException e) {
			throw new ServletException(e);
		}

	}

}
