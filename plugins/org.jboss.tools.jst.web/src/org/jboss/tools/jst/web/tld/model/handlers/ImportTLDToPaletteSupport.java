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
package org.jboss.tools.jst.web.tld.model.handlers;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.eclipse.osgi.util.NLS;

import org.jboss.tools.common.meta.action.impl.DefaultWizardDataValidator;
import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.common.meta.action.impl.WizardDataValidator;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultCreateHandler;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.jst.web.WebModelPlugin;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.model.tree.AllTldsTree;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.jboss.tools.jst.web.tld.model.helpers.TLDToPaletteHelper;

public class ImportTLDToPaletteSupport extends SpecialWizardSupport {
	Map<String,XModelObject> groups;
	XModelObject palette;
	AllTldsTree tree = new AllTldsTree();

	public void reset() {
		initParentName();
		tree.setConstraint(new Object[]{getEntityData()[0].getAttributeData()[0].getAttribute()});
		tree.getChildren(tree.getRoot());
		selectedFile = (XModelObject)getProperties().get("initialSelection"); //$NON-NLS-1$
		if(selectedFile != null) {
			setAttributeValue(0, "tld", "" + tree.getValue(selectedFile)); //$NON-NLS-1$ //$NON-NLS-2$
			onPathModified();
		}
	}
	
	void initParentName() {
		groups = new TreeMap<String,XModelObject>();
		palette = PreferenceModelUtilities.getPreferenceModel().getByPath("%Palette%"); //$NON-NLS-1$
		groups.put("", palette); //$NON-NLS-1$
		collectParents(palette);
		XModelObject t = (XModelObject)getProperties().get("target"); //$NON-NLS-1$
		if(t != null && t != target) {
			String g = t.getAttributeValue("name"); //$NON-NLS-1$
			if(g != null && groups.containsKey(g)) {
				getProperties().setProperty("parent group", g); //$NON-NLS-1$
			}
		}
		
	}
	
	void collectParents(XModelObject palette) {
		XModelObject[] cs = palette.getChildren();
		for (int i = 0; i < cs.length; i++) {
			String kind = cs[i].getAttributeValue("element type"); //$NON-NLS-1$
			if(!"group".equals(kind) && !"sub-group".equals(kind)) continue; //$NON-NLS-1$ //$NON-NLS-2$
			String name = cs[i].getAttributeValue("name"); //$NON-NLS-1$
			groups.put(name, cs[i]);
		}
		getProperties().put("groups", groups.keySet().toArray(new String[0])); //$NON-NLS-1$
	}
	
	public void action(String name) throws Exception {
		if(OK.equals(name)) {
			execute();
			setFinished(true);
		} else if(CANCEL.equals(name)) {
			setFinished(true);
		}
	}
	
	protected void execute() throws Exception {
		Properties p0 = extractStepData(0);
		String name = p0.getProperty("name"); //$NON-NLS-1$
		String path = p0.getProperty("tld"); //$NON-NLS-1$
		XModelObject tld = getSelectedResource(path);
		String parentName = getProperties().getProperty("parent group"); //$NON-NLS-1$
		XModelObject parent = groups.get(parentName);
		if(parent == null) {
	        Properties p = new Properties();
	        p.setProperty("name", parentName); //$NON-NLS-1$
	        parent = getTarget().getModel().createModelObject("SharablePageTabHTML", p); //$NON-NLS-1$
	        DefaultCreateHandler.addCreatedObject(palette, parent, getProperties());
		}
		TLDToPaletteHelper h = new TLDToPaletteHelper();
		XModelObject added = h.createGroupByTLD(tld, getTarget().getModel());
		added.setAttributeValue("name", name); //$NON-NLS-1$
		added.setAttributeValue(URIConstants.LIBRARY_URI, getAttributeValue(0, URIConstants.LIBRARY_URI));
		added.setAttributeValue(URIConstants.DEFAULT_PREFIX, getAttributeValue(0, URIConstants.DEFAULT_PREFIX)); 
		PaletteAdopt.add(parent, getTarget(), added);

		parent.getModel().saveOptions();
	}

	XModelObject selectedFile;
    String path;
    
    XModelObject getSelectedResource(String path) {
    	if(path == null) return null;
    	if(path.equals(this.path)) return selectedFile;
    	this.path = path;
    	try {
    		selectedFile = tree.find(path); 
    	} catch (Exception e) {
			WebModelPlugin.getPluginLog().logError(e);
    	}
    	return selectedFile;
    }
    
    public void onPathModified() {
    	if(selectedFile == null) return;
    	String name = selectedFile.getAttributeValue("display-name"); //$NON-NLS-1$
    	if(name == null || name.length() == 0) name = selectedFile.getAttributeValue("shortname"); //$NON-NLS-1$
    	setAttributeValue(0, "name", name); //$NON-NLS-1$
    	String pref = TLDToPaletteHelper.getTldName(selectedFile);
    	setAttributeValue(0, URIConstants.DEFAULT_PREFIX, pref);
    	String uri = selectedFile.getAttributeValue("uri"); //$NON-NLS-1$
    	setAttributeValue(0, URIConstants.LIBRARY_URI, uri);
    }

    protected DefaultWizardDataValidator validator = new ImportTLDValidator();
    
    public WizardDataValidator getValidator(int step) {
    	validator.setSupport(this, step);
		return validator;    	
    }
    
    class ImportTLDValidator extends DefaultWizardDataValidator {
    	public void validate(Properties data) {
    		message = null;
    		XModelObject s = getSelectedResource(data.getProperty("tld")); //$NON-NLS-1$
    		super.validate(data);
    		if(message != null) return;
    		String name = data.getProperty("name"); //$NON-NLS-1$
    		String parentName = getProperties().getProperty("parent group"); //$NON-NLS-1$
    		if(parentName == null || parentName.length() == 0) {
    			message = WebUIMessages.PARENT_GROUP_MUST_BE_SPECIFIED;
    			return;
    		}
    		XModelObject group = groups.get(parentName);
    		if(group == null) {
///    			message = "Cannot find tab " + parentName + ".";
    		} else if(group.getChildByPath(name) != null) {
    			message = (parentName.length() == 0) ?
   					NLS.bind(WebUIMessages.PALETTE_ALREADY_CONTAINS_TAB, name):
   					NLS.bind(WebUIMessages.PALETTE_ALREADY_CONTAINS_TAB_2P, parentName, name);
    		}
    		if(message != null) return;
    		if(s == null) {
    			message = WebUIMessages.PATH_TO_TLD_ISNOT_CORRECT;
    			return;
    		}
    	}
    }
    
    public String getStepImplementingClass(int stepId) {
        return "org.jboss.tools.jst.web.ui.wizards.palette.ImportTLDPage"; //$NON-NLS-1$
    }

}
