/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.angularjs.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.XModelObjectConstants;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.editors.dnd.IDropWizard;
import org.jboss.tools.common.model.ui.internal.editors.PaletteItemResult;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicConstants;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.IonicVersion;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewContentWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewContentWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewFooterBarWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewFooterBarWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewHeaderBarWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewHeaderBarWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewIonicWidgetWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewIonicWidgetWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewScrollWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewScrollWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTabWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTabWizardPage;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTabsWizard;
import org.jboss.tools.jst.angularjs.internal.ionic.palette.wizard.NewTabsWizardPage;
import org.jboss.tools.jst.jsp.test.palette.AbstractPaletteEntryTest;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.JQueryConstants;
import org.jboss.tools.jst.web.ui.palette.html.jquery.wizard.NewJQueryWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.html.wizard.AbstractNewHTMLWidgetWizard;
import org.jboss.tools.jst.web.ui.palette.model.PaletteModel;
import org.jboss.tools.jst.web.ui.test.HTML5PaletteWizardTest;

/**
 * @author Alexey Kazakov
 */
public class IonicPaletteTest extends AbstractPaletteEntryTest implements IonicConstants {
	IEditorPart editor = null;

	public IonicPaletteTest() {}

	@Override
	public void setUp() {
		super.setUp();
		editor = openEditor(getFileName());
	}

	@Override
	protected void tearDown() throws Exception {
		if(currentDialog != null) {
			currentDialog.close();
		}
		if(editor != null) {
			editor.getSite().getPage().closeEditor(editor, false);
			editor = null;
		}
		super.tearDown();
	}

	protected String getFileName() {
		return "a14.html";
	}

	protected IonicVersion getVersion() {
		return IonicVersion.IONIC_1_0;
	}

	public IWizardPage runToolEntry(String entry, boolean wizardExpected) {
		IWizardPage result = runToolEntry("Ionic", entry, wizardExpected);
		if(wizardExpected) {
			assertTrue(result instanceof NewIonicWidgetWizardPage);
			NewIonicWidgetWizardPage page = (NewIonicWidgetWizardPage)result;
			assertEquals(getVersion(), page.getWizard().getVersion());
		}
		return result;
	}

	public void testNewContentWizard() {
		IWizardPage currentPage = runToolEntry("Content", true);

		assertTrue(currentPage instanceof NewContentWizardPage);

		NewContentWizardPage wizardPage = (NewContentWizardPage)currentPage;
		NewContentWizard wizard = (NewContentWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Content");

		assertTextExists(wizard, TAG_ION_CONTENT);

		assertTextDoesNotExist(wizard, ATTR_SCROLL);
		wizardPage.setEditorValue(ATTR_SCROLL, FALSE);
		assertAttrExists(wizard, ATTR_SCROLL, FALSE);
		wizardPage.setEditorValue(ATTR_SCROLL, TRUE);
		assertTextDoesNotExist(wizard, ATTR_SCROLL);

		assertTextDoesNotExist(wizard, ATTR_OVERFLOW_SCROLL);
		wizardPage.setEditorValue(ATTR_OVERFLOW_SCROLL, TRUE);
		assertAttrExists(wizard, ATTR_OVERFLOW_SCROLL, TRUE);
		wizardPage.setEditorValue(ATTR_OVERFLOW_SCROLL, FALSE);
		assertTextDoesNotExist(wizard, ATTR_OVERFLOW_SCROLL);

		assertTextDoesNotExist(wizard, ATTR_DIRECTION);
		wizardPage.setEditorValue(ATTR_DIRECTION, "xy");
		assertAttrExists(wizard, ATTR_DIRECTION, "xy");
		wizardPage.setEditorValue(ATTR_DIRECTION, "");
		assertTextDoesNotExist(wizard, ATTR_DIRECTION);
		wizardPage.setEditorValue(ATTR_DIRECTION, "xy");

		assertTextDoesNotExist(wizard, ATTR_PADDING);
		wizardPage.setEditorValue(ATTR_PADDING, TRUE);
		assertAttrExists(wizard, ATTR_PADDING, TRUE);
		wizardPage.setEditorValue(ATTR_PADDING, FALSE);
		assertAttrExists(wizard, ATTR_PADDING, FALSE);
		wizardPage.setEditorValue(ATTR_PADDING, "");
		assertTextDoesNotExist(wizard, ATTR_PADDING);

		assertTextDoesNotExist(wizard, ATTR_START_Y);
		wizardPage.setEditorValue(ATTR_START_Y, "11");
		assertAttrExists(wizard, ATTR_START_Y, "11");
		wizardPage.setEditorValue(ATTR_START_Y, "");
		assertTextDoesNotExist(wizard, ATTR_START_Y);

		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_X);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_X, FALSE);
		assertAttrExists(wizard, ATTR_SCROLLBAR_X, FALSE);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_X, TRUE);
		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_X);

		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_Y);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_Y, FALSE);
		assertAttrExists(wizard, ATTR_SCROLLBAR_Y, FALSE);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_Y, TRUE);
		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_Y);

		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL);
		wizardPage.setEditorValue(ATTR_ON_SCROLL, "x()");
		assertAttrExists(wizard, ATTR_ON_SCROLL, "x()");
		wizardPage.setEditorValue(ATTR_ON_SCROLL, "");
		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL);

		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL_COMPLETE);
		wizardPage.setEditorValue(ATTR_ON_SCROLL_COMPLETE, "x()");
		assertAttrExists(wizard, ATTR_ON_SCROLL_COMPLETE, "x()");
		wizardPage.setEditorValue(ATTR_ON_SCROLL_COMPLETE, "");
		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL_COMPLETE);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewScrollWizard() {
		IWizardPage currentPage = runToolEntry("Scroll", true);

		assertTrue(currentPage instanceof NewScrollWizardPage);

		NewScrollWizardPage wizardPage = (NewScrollWizardPage)currentPage;
		NewScrollWizard wizard = (NewScrollWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Scroll");

		assertTextExists(wizard, TAG_ION_SCROLL);

		assertTextDoesNotExist(wizard, ATTR_DIRECTION);
		wizardPage.setEditorValue(ATTR_DIRECTION, "xy");
		assertAttrExists(wizard, ATTR_DIRECTION, "xy");
		wizardPage.setEditorValue(ATTR_DIRECTION, "");
		assertTextDoesNotExist(wizard, ATTR_DIRECTION);
		wizardPage.setEditorValue(ATTR_DIRECTION, "xy");

		assertTextDoesNotExist(wizard, ATTR_HAS_BOUNCING);
		wizardPage.setEditorValue(ATTR_HAS_BOUNCING, TRUE);
		assertAttrExists(wizard, ATTR_HAS_BOUNCING, TRUE);
		wizardPage.setEditorValue(ATTR_HAS_BOUNCING, FALSE);
		assertAttrExists(wizard, ATTR_HAS_BOUNCING, FALSE);
		wizardPage.setEditorValue(ATTR_HAS_BOUNCING, "");
		assertTextDoesNotExist(wizard, ATTR_HAS_BOUNCING);

		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_X);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_X, FALSE);
		assertAttrExists(wizard, ATTR_SCROLLBAR_X, FALSE);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_X, TRUE);
		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_X);

		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_Y);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_Y, FALSE);
		assertAttrExists(wizard, ATTR_SCROLLBAR_Y, FALSE);
		wizardPage.setEditorValue(ATTR_SCROLLBAR_Y, TRUE);
		assertTextDoesNotExist(wizard, ATTR_SCROLLBAR_Y);

		assertTextDoesNotExist(wizard, ATTR_PAGING);
		wizardPage.setEditorValue(ATTR_PAGING, TRUE);
		assertAttrExists(wizard, ATTR_PAGING, TRUE);
		wizardPage.setEditorValue(ATTR_PAGING, FALSE);
		assertTextDoesNotExist(wizard, ATTR_PAGING);

		assertTextDoesNotExist(wizard, ATTR_ZOOMING);
		wizardPage.setEditorValue(ATTR_ZOOMING, TRUE);
		assertAttrExists(wizard, ATTR_ZOOMING, TRUE);
		wizardPage.setEditorValue(ATTR_ZOOMING, FALSE);
		assertTextDoesNotExist(wizard, ATTR_ZOOMING);
		wizardPage.setEditorValue(ATTR_ZOOMING, TRUE);
		assertAttrExists(wizard, ATTR_ZOOMING, TRUE);

		assertTextDoesNotExist(wizard, ATTR_MIN_ZOOM);
		wizardPage.setEditorValue(ATTR_MIN_ZOOM, "0.1");
		assertAttrExists(wizard, ATTR_MIN_ZOOM, "0.1");
		wizardPage.setEditorValue(ATTR_MIN_ZOOM, "");
		assertTextDoesNotExist(wizard, ATTR_MIN_ZOOM);

		assertTextDoesNotExist(wizard, ATTR_MAX_ZOOM);
		wizardPage.setEditorValue(ATTR_MAX_ZOOM, "10");
		assertAttrExists(wizard, ATTR_MAX_ZOOM, "10");
		wizardPage.setEditorValue(ATTR_MAX_ZOOM, "");
		assertTextDoesNotExist(wizard, ATTR_MAX_ZOOM);

		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL + "=");
		wizardPage.setEditorValue(ATTR_ON_SCROLL, "x()");
		assertAttrExists(wizard, ATTR_ON_SCROLL, "x()");
		wizardPage.setEditorValue(ATTR_ON_SCROLL, "");
		assertTextDoesNotExist(wizard, ATTR_ON_SCROLL + "=");

		assertTextDoesNotExist(wizard, ATTR_ON_REFRESH);
		wizardPage.setEditorValue(ATTR_ON_REFRESH, "x()");
		assertAttrExists(wizard, ATTR_ON_REFRESH, "x()");
		wizardPage.setEditorValue(ATTR_ON_REFRESH, "");
		assertTextDoesNotExist(wizard, ATTR_ON_REFRESH);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewHeaderWizard() {
		IWizardPage currentPage = runToolEntry("Header Bar", true);
		assertTrue(currentPage instanceof NewHeaderBarWizardPage);

		NewHeaderBarWizardPage wizardPage = (NewHeaderBarWizardPage)currentPage;
		NewHeaderBarWizard wizard = (NewHeaderBarWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Header Bar");

		assertTextExists(wizard, ">Title<");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_TITLE, "Title1");
		assertTextExists(wizard, ">Title1<");

		wizardPage.setEditorValue(EDITOR_ID_BAR_COLOR, "bar-calm");
		assertAttrExists(wizard, ATTR_CLASS, "bar-calm");
		wizardPage.setEditorValue(EDITOR_ID_BAR_COLOR, "");
		
		assertTextDoesNotExist(wizard, ATTR_ALIGN_TITLE);
		wizardPage.setEditorValue(ATTR_ALIGN_TITLE, "left");
		assertAttrExists(wizard, ATTR_ALIGN_TITLE, "left");
		wizardPage.setEditorValue(ATTR_ALIGN_TITLE, "");
		assertTextDoesNotExist(wizard, ATTR_ALIGN_TITLE);
	
		assertTextExists(wizard, "Left button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LEFT_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, "Left button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LEFT_BUTTON, TRUE);
		assertTextExists(wizard, "Left button");

		assertTextExists(wizard, "Right button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, "Right button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON, TRUE);
		assertTextExists(wizard, "Right button");

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewFooterWizard() {
		IWizardPage currentPage = runToolEntry("Footer Bar", true);
		assertTrue(currentPage instanceof NewFooterBarWizardPage);

		NewFooterBarWizardPage wizardPage = (NewFooterBarWizardPage)currentPage;
		NewFooterBarWizard wizard = (NewFooterBarWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Footer Bar");

		assertTextExists(wizard, ">Title<");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_TITLE, "Title1");
		assertTextExists(wizard, ">Title1<");

		wizardPage.setEditorValue(EDITOR_ID_BAR_COLOR, "bar-calm");
		assertAttrExists(wizard, ATTR_CLASS, "bar-calm");
		wizardPage.setEditorValue(EDITOR_ID_BAR_COLOR, "");
		
		assertTextDoesNotExist(wizard, ATTR_ALIGN_TITLE);
		wizardPage.setEditorValue(ATTR_ALIGN_TITLE, "left");
		assertAttrExists(wizard, ATTR_ALIGN_TITLE, "left");
		wizardPage.setEditorValue(ATTR_ALIGN_TITLE, "");
		assertTextDoesNotExist(wizard, ATTR_ALIGN_TITLE);
	
		assertTextExists(wizard, "Left button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LEFT_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, "Left button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_LEFT_BUTTON, TRUE);
		assertTextExists(wizard, "Left button");

		assertTextExists(wizard, "Right button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON, FALSE);
		assertTextDoesNotExist(wizard, "Right button");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_RIGHT_BUTTON, TRUE);
		assertTextExists(wizard, "Right button");

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewTabsWizard() {
		IWizardPage currentPage = runToolEntry("Tabs", true);
		assertTrue(currentPage instanceof NewTabsWizardPage);

		NewTabsWizardPage wizardPage = (NewTabsWizardPage)currentPage;
		NewTabsWizard wizard = (NewTabsWizard)wizardPage.getWizard();

		compareUIAndNonUIWizards(wizard, "Tabs");

		assertEquals("3", wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_NUMBER_OF_ITEMS));

		assertTextDoesNotExist(wizard, "Tab 4");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_NUMBER_OF_ITEMS, "4");
		assertEquals("4", wizardPage.getEditorValue(JQueryConstants.EDITOR_ID_NUMBER_OF_ITEMS));
		assertTextExists(wizard, "Tab 4");		
	
		wizardPage.setEditorValue(EDITOR_ID_TABS_COLOR, "tabs-calm");
		assertAttrExists(wizard, ATTR_CLASS, "tabs-calm");
		wizardPage.setEditorValue(EDITOR_ID_TABS_COLOR, "");

		String tabsIconLeft = "tabs-icon-left";
		assertTextDoesNotExist(wizard, tabsIconLeft);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_ICON_POS, "left");
		assertAttrExists(wizard, ATTR_CLASS, tabsIconLeft);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_ICON_POS, "");
		assertTextDoesNotExist(wizard, tabsIconLeft);

		assertTextDoesNotExist(wizard, CLASS_TABS_ITEM_HIDE);
		wizardPage.setEditorValue(CLASS_TABS_ITEM_HIDE, TRUE);
		assertAttrExists(wizard, ATTR_CLASS, CLASS_TABS_ITEM_HIDE);
		wizardPage.setEditorValue(CLASS_TABS_ITEM_HIDE, FALSE);
		assertTextDoesNotExist(wizard, CLASS_TABS_ITEM_HIDE);

		compareGeneratedAndInsertedText(wizard);
	}

	public void testNewTabWizard() {
		IWizardPage currentPage = runToolEntry("Tab", true);
		assertTrue(currentPage instanceof NewTabWizardPage);

		NewTabWizardPage wizardPage = (NewTabWizardPage)currentPage;
		NewTabWizard wizard = (NewTabWizard)wizardPage.getWizard();

		assertAttrExists(wizard, ATTR_TITLE, "Title");
		wizardPage.setEditorValue(ATTR_TITLE, "x");
		assertAttrExists(wizard, ATTR_TITLE, "x");
		wizardPage.setEditorValue(ATTR_TITLE, "Title");

		assertTextDoesNotExist(wizard, ATTR_HREF);
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_URL, "www");
		assertAttrExists(wizard, ATTR_HREF, "www");
		wizardPage.setEditorValue(JQueryConstants.EDITOR_ID_URL, "");
		assertTextDoesNotExist(wizard, ATTR_HREF);

		assertTextDoesNotExist(wizard, ATTR_ICON);
		wizardPage.setEditorValue(ATTR_ICON, "ion-alert");
		assertAttrExists(wizard, ATTR_ICON, "ion-alert");
		wizardPage.setEditorValue(ATTR_ICON, "");
		assertTextDoesNotExist(wizard, ATTR_ICON);

		assertTextDoesNotExist(wizard, ATTR_ICON_ON);
		wizardPage.setEditorValue(ATTR_ICON_ON, "ion-alert");
		assertAttrExists(wizard, ATTR_ICON_ON, "ion-alert");
		wizardPage.setEditorValue(ATTR_ICON_ON, "");
		assertTextDoesNotExist(wizard, ATTR_ICON_ON);

		assertTextDoesNotExist(wizard, ATTR_ICON_OFF);
		wizardPage.setEditorValue(ATTR_ICON_OFF, "ion-alert");
		assertAttrExists(wizard, ATTR_ICON_OFF, "ion-alert");
		wizardPage.setEditorValue(ATTR_ICON_OFF, "");
		assertTextDoesNotExist(wizard, ATTR_ICON_OFF);

		assertTextDoesNotExist(wizard, ATTR_BADGE);
		wizardPage.setEditorValue(ATTR_BADGE, "1");
		assertAttrExists(wizard, ATTR_BADGE, "1");
		wizardPage.setEditorValue(ATTR_BADGE, "");
		assertTextDoesNotExist(wizard, ATTR_BADGE);

		assertTextDoesNotExist(wizard, ATTR_BADGE_STYLE);
		wizardPage.setEditorValue(ATTR_BADGE_STYLE, "tabs-positive");
		assertAttrExists(wizard, ATTR_BADGE_STYLE, "tabs-positive");
		wizardPage.setEditorValue(ATTR_BADGE_STYLE, "");
		assertTextDoesNotExist(wizard, ATTR_BADGE_STYLE);

		assertTextDoesNotExist(wizard, ATTR_ON_SELECT);
		wizardPage.setEditorValue(ATTR_ON_SELECT, "x()");
		assertAttrExists(wizard, ATTR_ON_SELECT, "x()");
		wizardPage.setEditorValue(ATTR_ON_SELECT, "");
		assertTextDoesNotExist(wizard, ATTR_ON_SELECT);

		assertTextDoesNotExist(wizard, ATTR_ON_DESELECT);
		wizardPage.setEditorValue(ATTR_ON_DESELECT, "x()");
		assertAttrExists(wizard, ATTR_ON_DESELECT, "x()");
		wizardPage.setEditorValue(ATTR_ON_DESELECT, "");
		assertTextDoesNotExist(wizard, ATTR_ON_DESELECT);

		assertTextDoesNotExist(wizard, ATTR_NG_CLICK);
		wizardPage.setEditorValue(ATTR_NG_CLICK, "c()");
		assertAttrExists(wizard, ATTR_NG_CLICK, "c()");
		wizardPage.setEditorValue(ATTR_NG_CLICK, "");
		assertTextDoesNotExist(wizard, ATTR_NG_CLICK);


		compareUIAndNonUIWizards(wizard, "Tab");

	}

	private void compareUIAndNonUIWizards(IDropWizard wizard, String itemName) {
		String startText = wizard.getWizardModel().getElementGenerator().generateStartTag();
		String endText = wizard.getWizardModel().getElementGenerator().generateEndTag();
		PaletteItemResult result = NewIonicWidgetWizard.runWithoutUi(textEditor, getVersion(), itemName);
		assertNotNull(result);
		assertEquals(startText, result.getStartText());
		assertEquals(endText, result.getEndText());
	}

	public void testPaletteItemsWithoutUI() {
		XModelObject g = PreferenceModelUtilities.getPreferenceModel().getByPath(PaletteModel.MOBILE_PATH + "/Ionic");
		List<String> failures = new ArrayList<String>(); 
		XModelObject[] cs = g.getChildren();
		for (XModelObject c: cs) {
			String name = c.getAttributeValue(XModelObjectConstants.ATTR_NAME);
			if(name.indexOf('.') >= 0) name = name.substring(name.indexOf('.') + 1);
			HTML5PaletteWizardTest.runPaletteItemsWithoutUI(failures, textEditor, c, name);
		}
		if(!failures.isEmpty()) {
			StringBuilder text = new StringBuilder();
			text.append("The following Palette wizards failed:\n");
			for (String s: failures) {
				text.append(s).append("\n");
			}
			fail(text.toString());
		}
	}

	protected void compareGeneratedAndInsertedText(NewIonicWidgetWizard<?> wizard) {
		String generatedText = wizard.getTextForTextView();

		wizard.performFinish();

		String insertedText = getInsertedText();
		assertTrue(isSameHTML(generatedText, insertedText));
	}

	void assertAttrExists(AbstractNewHTMLWidgetWizard wizard, String attr, String value) {
		assertTextExists(wizard, attr + "=\"" + value + "\"");
	}

	void assertTextExists(AbstractNewHTMLWidgetWizard wizard, String text) {
		assertTrue(wizard.getTextForTextView().indexOf(text) >= 0);
	}

	void assertTextDoesNotExist(AbstractNewHTMLWidgetWizard wizard, String text) {
		assertTrue(wizard.getTextForTextView().indexOf(text) < 0);
	}

	void assertTextIsInserted(String text) {
		String content = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput()).get();
		assertTrue(content.indexOf(text) > 0);
	}

	void assertAttrIsInserted(String attr, String value) {
		assertTextIsInserted(attr + "=\"" + value + "\"");
	}
}