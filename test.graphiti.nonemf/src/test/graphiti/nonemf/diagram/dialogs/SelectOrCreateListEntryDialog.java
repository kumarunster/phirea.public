package test.graphiti.nonemf.diagram.dialogs;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;


public class SelectOrCreateListEntryDialog extends Dialog {
	
	public static int CREATE_ID = 356;
	
	public static String CREATE_LABEL = "Create";
	private Table table;
	
	private Button createNewEntryButton;
	SelectionListener createNewEntryListener;

	private TableViewer tableViewer;

	private Label lblSelectionTextTitle;

	private String dialogText;

	private String dialogTitle;

	private List<?> input;

	private IContentProvider contentProvider;

	private ILabelProvider labelProvider;
	
	private IChangeValueCallBack changeValueCallBack;

	protected Object[] dialogSelection;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public SelectOrCreateListEntryDialog(Shell parentShell) {
		super(parentShell);
	}
	
	
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		this.getShell().setText(dialogTitle);
		
		
		lblSelectionTextTitle = new Label(container, SWT.NONE);
		lblSelectionTextTitle.setText(dialogText);
		
		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		if(this.contentProvider != null && this.labelProvider != null)
		{
			
			TableViewerColumn tableViewerColumnSelection = new TableViewerColumn(tableViewer, SWT.NONE );
			tableViewerColumnSelection.getColumn().setWidth(20);
			tableViewerColumnSelection.setLabelProvider(new ColumnLabelProvider(){

				@Override
				public String getText(Object element) {
					return "";
				}
			});	
			
			
			
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			
			tableViewerColumn.getColumn().setWidth(300);
			
			tableViewerColumn.setEditingSupport(new EditingSupport(tableViewer) {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
				protected void setValue(Object element, Object value) {
					System.out.println("setting " + element + " to " + value);
					
					changeValueCallBack.setValue(input, element, value);
					
					getViewer().refresh();
				}
				
				@Override
				protected Object getValue(Object element) {
					return changeValueCallBack.getValue(element);
				}
				
				@Override
				protected CellEditor getCellEditor(Object element) {
					return new TextCellEditor(((TableViewer)this.getViewer()).getTable());
				}
				
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
			});
			tableViewerColumn.setLabelProvider(new ColumnLabelProvider()
			{	@Override
				public String getText(Object element) {
					return labelProvider.getText(element);
				}
				 
			});	
						
			
			tableViewer.setContentProvider(this.contentProvider);	
			tableViewer.setInput(input);
			
			tableViewer.getTable().addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					super.widgetSelected(e);
					
					
					TableItem[] selection = tableViewer.getTable().getSelection();
					if(selection != null && selection.length > 0)
					{
						dialogSelection = new Object[selection.length];
						
						for (int i = 0; i<selection.length; i++) {
							dialogSelection[i] = selection[i].getData();
						}
					}
					
					
					
				}
				
			});
			
			
		}

		return container;
	}
	
	public SelectOrCreateListEntryDialog setContentProvider(IContentProvider contentProvider)
	{
		this.contentProvider = contentProvider;
		return this;
	}
	
	
	public SelectOrCreateListEntryDialog setLabelProvider(ILabelProvider labelProvider)
	{
		this.labelProvider = labelProvider;
		return this;
	}
	
	
	public SelectOrCreateListEntryDialog setChangeValueCallback(IChangeValueCallBack changeValueCallBack)
	{
		this.changeValueCallBack = changeValueCallBack;
		return this;
	}
	
	
	public SelectOrCreateListEntryDialog setInput(List<?> input)
	{
		this.input = input;
		return this;
	}
	
	public SelectOrCreateListEntryDialog setDialogTitle(String title)
	{
		this.dialogTitle = title;
		return this;
	}
	
	public SelectOrCreateListEntryDialog setDialogLabel(String text)
	{
		this.dialogText = text;
		return this;
	}
	

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		if(createNewEntryListener != null) {
			createNewEntryButton = createButton(parent, CREATE_ID, CREATE_LABEL, true);
			
			createNewEntryButton.addSelectionListener(createNewEntryListener);
		}
		
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(400, 450);
		
	}
	
	public SelectOrCreateListEntryDialog setCreateNewEntrySelectionListener(
			SelectionListener selectionListener) {
		this.createNewEntryListener = selectionListener;
		return this;
	}
	
	
	
	public void updateView() {
		this.tableViewer.refresh();
	}
	
	
	public void selectAndFocusEntry(String element) {
		this.tableViewer.refresh();
		this.tableViewer.editElement(element, 0);
	}
	

	
	public Object[] getResult() {
		return dialogSelection;
	}
	
	
	public static void main(String[] args)
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		
		final List<String> entries = new ArrayList<String>();
		entries.add("List Entry 1");
		entries.add("List Entry 2");
		entries.add("List Entry 3");
		
		IContentProvider contentProvider = new ArrayContentProvider();
		
		ILabelProvider labelProvider = new LabelProvider();		
		
		final SelectOrCreateListEntryDialog newDialog = new SelectOrCreateListEntryDialog(shell);
		
		newDialog.setDialogLabel("Dialog Label")
				.setDialogTitle("DialogTitle")
				.setContentProvider(contentProvider)
				.setLabelProvider(labelProvider)
				.setInput(entries)
				.setCreateNewEntrySelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						String newElement = "newEntry " + (entries.size() + 1); 
						entries.add(newElement);
						newDialog.selectAndFocusEntry(newElement);
					}}
				);
		
		newDialog.open();
		
		
		
		while (newDialog.getShell() != null && 
				!newDialog.getShell().isDisposed()) {
		  if (!display.readAndDispatch()) {
		    display.sleep();
		  }
		}
		display.dispose();
	}



	

}
