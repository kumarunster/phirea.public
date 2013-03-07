package test.graphiti.nonemf.diagram;

import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.notification.INotificationService;
import org.eclipse.graphiti.platform.IDiagramEditor;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;
import org.eclipse.graphiti.ui.editor.DefaultRefreshBehavior;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;

import test.graphiti.nonemf.diagram.tools.ToolBehaviorProvider;
import test.graphiti.nonemf.utils.RepositoryUtils;

/**
 * @author Nikolai Raitsev
 *
 */
public class DiagramTypeProvider extends AbstractDiagramTypeProvider {

	NonEmfNotificationService nonEmfNotificationService;
	
	NonEmfDiagramEditor nonEmfDiagramEditor;
	
	IToolBehaviorProvider[] toolBehaviorProviders;
	
	public DiagramTypeProvider() {
		super();
		setFeatureProvider(new DiagramFeatureProvider(this));
	}
	
	
    @Override
    public IToolBehaviorProvider[] getAvailableToolBehaviorProviders() {
        if (toolBehaviorProviders == null) {
            toolBehaviorProviders =
                new IToolBehaviorProvider[] { new ToolBehaviorProvider(this) };
        }
        return toolBehaviorProviders;
    }

	@Override
	public INotificationService getNotificationService() {
		if (this.nonEmfNotificationService == null) {
			this.nonEmfNotificationService = new NonEmfNotificationService(this);
		}
		return this.nonEmfNotificationService;
	}

	@Override
	public void init(Diagram diagram, IDiagramEditor diagramEditor) {
		super.init(diagram, diagramEditor);
		if(diagramEditor instanceof NonEmfDiagramEditor) {
			NonEmfDiagramEditor nonEmfDiagramEditor = (NonEmfDiagramEditor) diagramEditor;
			DiagramEditorInput editorInput = (DiagramEditorInput) nonEmfDiagramEditor.getEditorInput();
			
			DiagramFeatureProvider dfp = (DiagramFeatureProvider) getFeatureProvider();
			
			RepositoryUtils.loadPOJOObjects(editorInput, dfp);
			
			DefaultRefreshBehavior refreshBehavior = ((NonEmfDiagramEditor) diagramEditor).getRefreshBehavior();
			refreshBehavior.refreshRenderingDecorators(diagram);
		}
	}


	@Override
	public boolean isAutoUpdateAtStartup() {
		return true;
	}
	

//	@Override
//	public IDiagramEditor getDiagramEditor() {
//		
//		if(nonEmfDiagramEditor == null) {
//			Diagram diagram = getDiagram();
//			TransactionalEditingDomain transactionalEditingDomain = TransactionUtil.getEditingDomain(diagram);
//			nonEmfDiagramEditor = new NonEmfDiagramEditor(transactionalEditingDomain);
//		}
//		
//		return nonEmfDiagramEditor;
//	}
	
	
	
	
	

}
