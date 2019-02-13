/**
 * 
 */
package com.transerainc.aim.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import com.transerainc.aim.login.LoginHandler;
import com.transerainc.aim.pojo.LoginStatus;
import com.transerainc.aim.snmp.TrapManager;
import com.transerainc.aim.system.AgentInformationManagerSystem;
import com.transerainc.aim.util.LoginStatusUtil;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba</a>
 * @version $Rivision:$
 */
public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger lgr = Logger.getLogger(LoginServlet.class.getName());

	public LoginServlet() {

	}

	@Override
	public void init() throws ServletException {
		super.init();
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
		String login = req.getParameter("login");
		String cName = req.getParameter("cname");
		lgr.info("Character encoding: " + req.getCharacterEncoding());
		lgr.info("Url encoded login: " + URLEncoder.encode(login, "UTF-8"));
		try {
			String password = req.getParameter("password");

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Agent " + login + " for company " 
				+ cName + " requesting permission to login. TPG @ " + ipAddr);
			}

			LoginHandler lHandler = new LoginHandler(login, password, cName);
			lHandler.setCasAuthenticated(false);
			LoginStatus status = lHandler.login();

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
			
			resp.setContentType("text/xml; charset=UTF-8");
			resp.getWriter().println(xmlStr);
			resp.getWriter().flush();

			if (lgr.isLoggable(Level.INFO)) {
				lgr.info("Time taken to login: " + (ts2 - ts1)
					+ ", XML Conversion: " + (ts3 - ts2));
			}
		} catch (Exception e) {
			// Raise a trap
			// invalidLogin(login, companyName);
			TrapManager tmgr =
					(TrapManager) AgentInformationManagerSystem
							.findBean("trapManager");
			tmgr.sendInvalidLogin(login, cName, ipAddr);
			throw new ServletException(e);
		}
	}

}