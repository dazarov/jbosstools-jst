package org.jboss.tools.jst.web.ui.palette;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class MobileDrawerFigure extends CustomDrawerFigure {
	private Control control;
	private static JQueryMobileVersionPopUp popup;
	
	public MobileDrawerFigure(Control control) {
		super(control);
		
		this.control = control;
		
		Figure collapseToggle = (Figure)getChildren().get(0);
		Figure title = (Figure)collapseToggle.getChildren().get(0);
		Figure pinFigure = (Figure)title.getChildren().get(0);
		Figure drawerFigure = (Figure)title.getChildren().get(1);
		
		VersionFigure label = new VersionFigure("1.3");
		GridLayout layout = new GridLayout(4, false);
		title.setLayoutManager(layout);
		
		layout.setConstraint(drawerFigure, new GridData(GridData.FILL_HORIZONTAL));
		title.add(drawerFigure);
		
		title.add(label);
		title.add(pinFigure);
	}
	
	public class VersionFigure extends Clickable{
		private Color backColor = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		private Color foreColor = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		
		public VersionFigure(String text){
			super(new Label(text));
			setRolloverEnabled(true);
			setBorder(new MarginBorder(2));
			addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
					//if(popup == null || popup.isShowing()){
						popup = new JQueryMobileVersionPopUp(control, VersionFigure.this);
						popup.show(new String[]{"1.3", "1.4"});
					//}
				}
			});
		}
		
		public void setVersion(String newVersion){
			((Label)getChildren().get(0)).setText(newVersion);
		}
		
		public String getVersion(){
			return ((Label)getChildren().get(0)).getText();
		}
		
		protected void paintFigure(Graphics graphics) {
			super.paintFigure(graphics);

			ButtonModel model = getModel();
			if (isRolloverEnabled() && model.isMouseOver()) {
				graphics.setBackgroundColor(backColor);
				graphics.fillRoundRectangle(getClientArea().getCopy().getExpanded(1, 1),	7, 7);

				graphics.setForegroundColor(foreColor);
				graphics.drawRoundRectangle(getClientArea().getCopy().getExpanded(1, 1),	7, 7);
			}
		}
	}
}
