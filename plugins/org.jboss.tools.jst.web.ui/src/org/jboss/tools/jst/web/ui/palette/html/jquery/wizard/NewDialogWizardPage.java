/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.ui.palette.html.jquery.wizard;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.common.ui.widget.editor.IFieldEditor;
import org.jboss.tools.jst.web.ui.palette.html.wizard.WizardMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewDialogWizardPage extends NewJQueryWidgetWizardPage {

	public NewDialogWizardPage() {
		super("newDialog", WizardMessages.newDialogWizardTitle);
		setDescription(WizardMessages.newDialogWizardDescription);
	}

	protected void createFieldPanel(Composite parent) {
		IFieldEditor title = JQueryFieldEditorFactory.createTitleEditor(WizardDescriptions.dialogTitle);
		title.setValue("Dialog");
		addEditor(title, parent);

		createIDEditor(parent, false);

		IFieldEditor close = JQueryFieldEditorFactory.createCloseButtonEditor();
		addEditor(close, parent);
	}

}
