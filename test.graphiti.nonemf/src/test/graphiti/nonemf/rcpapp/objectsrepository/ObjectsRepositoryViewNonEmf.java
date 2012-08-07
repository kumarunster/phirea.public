package test.graphiti.nonemf.rcpapp.objectsrepository;


import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import test.graphiti.nonemf.domainmodel.TermClass;
import test.graphiti.nonemf.rcpapp.objectsrepository.dnd.DragListener;
import test.graphiti.nonemf.utils.EventCommunicationConstants;
import test.graphiti.nonemf.utils.RepositoryUtils;

public class ObjectsRepositoryViewNonEmf extends ViewPart {

	public static final String ID = "test.graphiti.nonemf.rcpapp.objectsrepository.ObjectsRepositoryViewNonEmf"; //$NON-NLS-1$
	private Action createNewAction;
	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private TreeViewer treeViewer;
	

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
			treeViewer = new TreeViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
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
		registerEventHandlers();
	}

	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		createNewAction = new Action() {
			@Override public void run() {
				super.run();
				System.out.println("Run Action!");
				createNewAction_Body();
			}
		};
		
		
		String text = "Create New Item in Repository";
		createNewAction.setText(text);
		createNewAction.setToolTipText(text);
		
		ImageDescriptor imgDescriptor = PlatformUI.getWorkbench()
										.getSharedImages()
										.getImageDescriptor(ISharedImages.IMG_OBJ_ADD);
		
		createNewAction.setImageDescriptor(imgDescriptor);
		createNewAction.setEnabled(true);
		
		
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
		
		
		tbm.add(createNewAction);
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager manager = getViewSite().getActionBars().getMenuManager();
		
		manager.add(createNewAction);

		MenuManager menuManager = new MenuManager();
		Menu treeMenu = menuManager.createContextMenu(treeViewer.getTree());
		// Set the MenuManager
		treeViewer.getTree().setMenu(treeMenu);
		getSite().registerContextMenu(menuManager, treeViewer);
		// Make the selection available
		getSite().setSelectionProvider(treeViewer);
		
		MenuItem menuItemCreateNew = new MenuItem(treeMenu, SWT.NONE);
		menuItemCreateNew.setImage(createNewAction.getImageDescriptor().createImage());
		menuItemCreateNew.setText(createNewAction.getText());
		menuItemCreateNew.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				System.out.println("selected");
				
				createNewAction.run();
			}			
		});
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
	

	private void registerEventHandlers() {
		BundleContext bundleContext = FrameworkUtil.getBundle(ObjectsRepositoryViewNonEmf.class).getBundleContext();
		
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void handleEvent(Event event) {	
				System.out.println("handle osgi service event!");
				
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						updateTreeView();
					}
				});
			}		
		};
		
		
		Dictionary<String,String> properties = new Hashtable<String, String>();
		properties.put(EventConstants.EVENT_TOPIC, EventCommunicationConstants.VIEWCOMMUNICATION + "/*");
		bundleContext.registerService(EventHandler.class, eventHandler, properties);
		
	}
	
	

	protected void createNewAction_Body() {
		
		TermClass newTermClass = new TermClass();
		newTermClass.setName("give me a name!");
		//as a side effect puts the bo into the objects map...
		RepositoryUtils.pojoIndependenceSolverStatic.getKeyForBusinessObject(newTermClass);
		
		updateTreeView();
	}


	private void updateTreeView() {
		
		treeViewer.setInput(RepositoryUtils.pojoIndependenceSolverStatic);
		
		treeViewer.expandToLevel(1);
	}	

}
