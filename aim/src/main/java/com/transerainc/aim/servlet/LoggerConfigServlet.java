/*
 * Created on Jul 26, 2004
 *
 */
package com.transerainc.aim.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:ramesh.bobba@transerainc.com">Ramesh Bobba </a>
 * @version $Revision: 1.1 $
 */
public class LoggerConfigServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger myLgr =
			Logger.getLogger(LoggerConfigServlet.class.getName());

	/**
	 * 
	 */
	public LoggerConfigServlet() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		handleLoggerConfig(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		handleLoggerConfig(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @throws IOException
	 */
	private void handleLoggerConfig(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// The caller needs to either pass show or level. Passing show and
		// level does not work since show takes precedence and levels are
		// not changed. The value for level has to match the text value
		// of the static levels in the system. eg: INFO, SEVERE, FINE...
		// The urls look like:
		// http://localhost:8080/configLogger?show=true
		// http://localhost:8080/configLogger?level=WARNING
		String level = request.getParameter("level");

		ArrayList<Logger> lgrList = getLoggers();

		if (level == null) {
			showLoggers(response, lgrList);

			return;
		}

		Level newLevel = Level.parse(level);

		myLgr.info("New Level: " + newLevel);

		for (Iterator<Logger> itr = lgrList.iterator(); itr.hasNext();) {
			Logger lgr = itr.next();

			myLgr.info("Setting level to " + newLevel.getName()
				+ " for logger " + lgr.getName());

			lgr.setLevel(newLevel);
		}

		showLoggers(response, getLoggers());

		testLogger();
	}

	/**
	 * 
	 */
	private void testLogger() {
		// Log a message for each level
		myLgr.finest("LoggerConfigServlet test finest message");
		myLgr.finer("LoggerConfigServlet test finer message");
		myLgr.fine("LoggerConfigServlet test fine message");
		myLgr.config("LoggerConfigServlet test config message");
		myLgr.info("LoggerConfigServlet test info message");
		myLgr.warning("LoggerConfigServlet test warning message");
		myLgr.severe("LoggerConfigServlet test severe message");
	}

	/**
	 * @return
	 */
	private ArrayList<Logger> getLoggers() {
		LogManager lmgr = LogManager.getLogManager();

		Enumeration<String> en = lmgr.getLoggerNames();

		ArrayList<Logger> lgrList = new ArrayList<Logger>();

		while (en.hasMoreElements()) {
			String lgrName = en.nextElement();

			Logger lgr = lmgr.getLogger(lgrName);

			lgrList.add(lgr);
		}

		return lgrList;
	}

	/**
	 * @param response
	 * @param lgrList
	 * @throws IOException
	 */
	private void showLoggers(HttpServletResponse response,
			ArrayList<Logger> lgrList) throws IOException {
		PrintWriter writer = response.getWriter();

		writer.write("<html><body><center>");
		writer
				.write("<html><body><form action=/aim/configLogger method=POST><center>");

		writer.write("<h1>Available Loggers</h1>");

		writer
				.println("<table width=90%><tr bgcolor=#32cc32><td>Change level to:</td>");
		writer.println("<td><select name=\"level\">");
		writer.println("<option>ALL</option>");
		writer.println("<option>FINEST</option>");
		writer.println("<option>FINER</option>");
		writer.println("<option>FINE</option>");
		writer.println("<option>INFO</option>");
		writer.println("<option>CONFIG</option>");
		writer.println("<option>WARNING</option>");
		writer.println("<option>SEVERE</option>");
		writer.println("</select></td>");
		writer
				.println("<td><input type=submit name=change value=Change></td></tr></table>");

		writer.write("<table border=1 width=90%>");

		writer.write("<tr bgcolor=\"#ccccee\"><td align=center>");
		writer.write("Logger Name</td><td align=center>");
		writer.write("Current Log Level</td></tr>");

		for (Iterator<Logger> itr = lgrList.iterator(); itr.hasNext();) {
			Logger lgr = itr.next();

			String lgrName = lgr.getName();

			if ((lgrName != null) && (lgrName.length() == 0)) {
				lgrName = "Default";
			}

			writer.write("<tr><td>");
			writer.write(lgrName);
			writer.write("</td><td>");

			Level lvl = lgr.getLevel();

			if (lvl != null) {
				writer.write(lvl.getName());
			} else {
				writer.write("NONE");
			}

			writer.write("</td></tr>");
		}

		writer.write("</table></center></body></html>");
	}
}
