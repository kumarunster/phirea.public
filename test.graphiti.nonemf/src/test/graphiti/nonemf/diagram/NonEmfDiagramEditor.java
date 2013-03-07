package test.graphiti.nonemf.diagram;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.dnd.TransferDropTargetListener;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

import test.graphiti.nonemf.diagram.dnd.NonEmfTransferDropTargetListener;
import test.graphiti.nonemf.utils.RepositoryUtils;

/**
 * @author Nikolai Raitsev
 *
 */
public class NonEmfDiagramEditor extends DiagramEditor {
	
	public static final String DIAGRAM_EDITOR_ID = "test.graphiti.nonemf.diagram.NonEmfDiagramEditor";

	NonEmfDomainModelChangeListener nonEmfDomainModelListener;
	private TransactionalEditingDomain transactionalEditingDomain;
	
	public NonEmfDiagramEditor(){
		super();
	}
	
	public NonEmfDiagramEditor(TransactionalEditingDomain transactionalEditingDomain) {
		super();
		this.transactionalEditingDomain = transactionalEditingDomain;		
	}

	protected void registerBOListener() {
		nonEmfDomainModelListener = new NonEmfDomainModelChangeListener(this);
		
		TransactionalEditingDomain eDomain = getEditingDomain();
		eDomain.addResourceSetListener(nonEmfDomainModelListener);
	}

	@SuppressWarnings("restriction")
	@Override
	public TransactionalEditingDomain getEditingDomain() {
		TransactionalEditingDomain result = super.getEditingDomain(); 
		if(result == null)
			result = transactionalEditingDomain;
		
		return result;
	}

	@SuppressWarnings("restriction")
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		
		IDiagramTypeProvider diagramTypeProvider = this.getDiagramTypeProvider();
		if(diagramTypeProvider instanceof DiagramTypeProvider) {
			DiagramTypeProvider dtp = (DiagramTypeProvider) diagramTypeProvider;
			
			DiagramFeatureProvider dfp = (DiagramFeatureProvider) dtp.getFeatureProvider();
			POJOIndependenceSolver pojoIndependenceSolver = dfp.getPojoIndependenceSolver();
			
			DiagramEditorInput editorInput = (DiagramEditorInput) this.getEditorInput();
			
			RepositoryUtils.persistPOJOObjects(editorInput, pojoIndependenceSolver);	
		}
		
		
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		this.getGraphicalViewer().addDropTargetListener(new NonEmfTransferDropTargetListener(this));
		
		getRefreshBehavior().refresh();
	}
	
	
	
	

}
