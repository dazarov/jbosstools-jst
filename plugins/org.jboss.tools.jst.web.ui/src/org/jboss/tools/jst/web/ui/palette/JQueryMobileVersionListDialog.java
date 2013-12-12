package org.jboss.tools.jst.web.ui.palette;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


public class JQueryMobileVersionListDialog extends TitleAreaDialog {
	String[] versionList = new String[]{"1.2.0", "1.2.5", "1.3.0", "1.3.2"};
	String result;
	
	private ContentProvider contentProvider = new ContentProvider();
	
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Select version of jQuery Mobile");
		Composite area = (Composite) super.createDialogArea(parent);
		//Composite contents = new Composite(area, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 5;
		
		//contents.setLayout(layout);
		
		TableViewer list = new TableViewer(area, SWT.MULTI
				| SWT.BORDER | SWT.V_SCROLL | SWT.VIRTUAL);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		list.getTable().setLayoutData(gridData);
		
		list.setContentProvider(contentProvider);
		list.setInput(new Object[0]);
		list.setItemCount(versionList.length);
		list.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection s = event.getSelection();
				if(!s.isEmpty() && s instanceof IStructuredSelection) {
					result = (String)((IStructuredSelection)s).getFirstElement();
				}
			}
		});
		
		return area;
	}
	
	public String getResult(){
		return result;
	}

	public JQueryMobileVersionListDialog(Shell parentShell) {
		super(parentShell);
	}
	
	private class ContentProvider implements IStructuredContentProvider{
		

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return versionList;
		}
		
	}

}
