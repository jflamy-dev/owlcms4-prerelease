/***
 * Copyright (c) 2009-2020 Jean-François Lamy
 *
 * Licensed under the Non-Profit Open Software License version 3.0  ("Non-Profit OSL" 3.0)
 * License text at https://github.com/jflamy/owlcms4/blob/master/LICENSE.txt
 */
package app.owlcms.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.MDC;

import app.owlcms.init.OwlcmsSession;

/**
 * The Class LoggerUtils.
 */
public class LoggerUtils {

    public static void setWhere(String where) {
        MDC.put("page", where);
        OwlcmsSession.withFop(fop -> MDC.put("currentGroup", fop.getGroup() != null ? fop.getGroup() : "-"));
    }

    /**
     * Where from.
     *
     * @return the string
     */
    public static String stackTrace() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int i = 0;
        for (StackTraceElement ste : trace) {
            String string = ste.toString();
            if (string.startsWith("com.vaadin.flow.server.communication")) {
                break;
            }
            if (i > 1) {
                pw.println("\t" + string);
            }

            i++;
        }
        return sw.toString();
    }

    /**
     * @param t
     * @return
     */
    public static String stackTrace(Throwable t) {
        // IDEA: skip from "at javax.servlet.http.HttpServlet.service" to line starting
        // with "Caused by"
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * Where from.
     *
     * @return the string
     */
    public static String whereFrom() {
        return Thread.currentThread().getStackTrace()[3].toString();
    }

    /**
     * Where from, additional depth
     *
     * @return the string
     */
    public static String whereFrom(int depth) {
        return Thread.currentThread().getStackTrace()[3 + depth].toString();
    }
}
