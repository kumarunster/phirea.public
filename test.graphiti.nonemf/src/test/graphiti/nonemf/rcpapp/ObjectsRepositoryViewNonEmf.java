package test.graphiti.nonemf.rcpapp;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import test.graphiti.nonemf.rcpapp.draganddrop.DragListener;
import test.graphiti.nonemf.rcpapp.draganddrop.NonEmfTreeContentProvider;
import test.graphiti.nonemf.rcpapp.draganddrop.NonEmfTreeLabelProvider;
import test.graphiti.nonemf.utils.RepositoryUtils;

public class ObjectsRepositoryViewNonEmf extends ViewPart {

	public static final String ID = "test.graphiti.nonemf.rcpapp.ObjectsRepositoryViewNonEmf"; //$NON-NLS-1$
	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());

	public ObjectsRepositoryViewNonEmf() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = toolkit.createComposite(parent, SWT.NONE);
		toolkit.paintBordersFor(container);
		container.setLayout(new GridLayout(1, false));
		{
			TreeViewer treeViewer = new TreeViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
			Tree treeWithViewer = treeViewer.getTree();
			TreeColumn trclmnNewColumn = new TreeColumn(treeWithViewer, SWT.NONE);
			trclmnNewColumn.setWidth(80);
			trclmnNewColumn.setText("Name");

			trclmnNewColumn = new TreeColumn(treeWithViewer, SWT.NONE);
			trclmnNewColumn.setWidth(100);
			trclmnNewColumn.setText("Description");

			treeWithViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			treeWithViewer.setHeaderVisible(true);
			treeWithViewer.setLinesVisible(true);
			toolkit.paintBordersFor(treeWithViewer);

			int operations = DND.DROP_COPY | DND.DROP_MOVE;

			Transfer[] transferTypes = new Transfer[]{LocalSelectionTransfer.getTransfer()};
//			treeViewer.addDropSupport(operations, transferTypes, new DropListener(treeViewer));
			treeViewer.addDragSupport(operations, transferTypes, new DragListener(treeViewer));

			treeViewer.setLabelProvider(new NonEmfTreeLabelProvider());
			treeViewer.setContentProvider(new NonEmfTreeContentProvider());
			if(RepositoryUtils.pojoIndependenceSolverStatic != null)
				treeViewer.setInput(RepositoryUtils.pojoIndependenceSolverStatic);
		}

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager manager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
