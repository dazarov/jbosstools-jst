/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.jst.web.model.handlers;

import java.io.StringReader;
import java.util.Set;
import org.w3c.dom.*;
import org.jboss.tools.common.xml.*;

public class TaglibSetXHTML extends TaglibSet {

	protected void doGetTaglibsFromTemplate(String body, Set<String> existing) {
		int ib = body.toLowerCase().indexOf("<html");
		if(ib < 0) return;
		int ie = body.indexOf(">", ib);
		if(ie < 0) return;
		String html = body.substring(ib, ie);
		if(html.endsWith("/")) html += ">"; else html += "/>";
		
		Element e = XMLUtilities.getElement(new StringReader(html), XMLEntityResolver.getInstance());
		if(e == null) return;
		NamedNodeMap as = e.getAttributes();
		for (int i = 0; i < as.getLength(); i++) {
			Node n = as.item(i);
			String name = n.getNodeName();
			if(!name.startsWith("xmlns:")) continue;
			String prefix = name.substring(6);
			String uri = e.getAttribute(name);
			if(prefix.length() > 0 && uri.length() > 0) {
				appendURIFound(prefix, uri, existing);
			}
		}
	}

	protected boolean doModifyBody(String body, String[] selected, StringBuffer sb) {
		if(body == null || body.length() == 0) {
			body = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + "\n" +
				   "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +				
				   "</html>";			
		}
		int ib = body.toLowerCase().indexOf("<html");
		if(ib < 0) return false;
		int ie = body.indexOf(">", ib);
		if(ie < 0) return false;
		String html = body.substring(ib, ie) + "/>";
		Element e = XMLUtilities.getElement(new StringReader(html), XMLEntityResolver.getInstance());
		if(e == null) return false;
		sb.append(body.substring(0, ib + 5));

		int ac = 0;
		NamedNodeMap as = e.getAttributes();
		for (int i = 0; i < as.getLength(); i++) {
			Node n = as.item(i);
			String name = n.getNodeName();
			if(name.startsWith("xmlns:")) continue;
			if(ac > 0) sb.append("\n     ");
			String value = e.getAttribute(name);
			sb.append(" " + name + "=\"" + value + "\"");
			++ac;
		}
		for (int i = 0; i < selected.length; i++) {
			if(ac > 0) sb.append("\n     ");
			String uri = getURIByDescription(selected[i]);
			String prefix = getPrefix(selected[i]);
			String name = "xmlns:" + prefix;
			String value = uri;
			sb.append(" " + name + "=\"" + value + "\"");
			++ac;
		}
		sb.append(">");
		
		sb.append(body.substring(ie + 1));
		return true;
	}

}
