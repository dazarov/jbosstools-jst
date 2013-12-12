package org.jboss.tools.jst.web.ui.palette;

import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.palette.PaletteDrawer;

public class MobileDrawerEditPart extends CustomDrawerEditPart {

	public MobileDrawerEditPart(PaletteDrawer drawer) {
		super(drawer);
	}

	@Override
	protected int getLayoutSetting() {
		return PaletteViewerPreferences.LAYOUT_ICONS;
	}
	
	public IFigure createFigure() {
		MobileDrawerFigure fig = new MobileDrawerFigure(getViewer().getControl()) {
			public IFigure getToolTip() {
				return createToolTip();
			}
		};
		fig.setExpanded(getDrawer().isInitiallyOpen());
		fig.setPinned(getDrawer().isInitiallyPinned());
		fig.getCollapseToggle().setRequestFocusEnabled(true);
		fig.getCollapseToggle().addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
				getViewer().select(MobileDrawerEditPart.this);
			}
			public void focusLost(FocusEvent fe) {
			}
		});
		return fig;
	}
}
