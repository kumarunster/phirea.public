package test.graphiti.nonemf.rcpapp;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import test.graphiti.nonemf.diagram.NonEmfDiagramEditor;
import test.graphiti.nonemf.utils.RepositoryUtils;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1000, 768));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setShowCoolBar(true);
        configurer.setTitle("Test Graphiti Standalone Editor"); //$NON-NLS-1$
    }

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		try {	
			IEditorInput editorInput = RepositoryUtils.getEditorInput();
			
	        page.openEditor(editorInput, NonEmfDiagramEditor.DIAGRAM_EDITOR_ID);
	    } catch (Exception e) {
	        // Handle the exception here
	    	e.printStackTrace();
	    }

	}

    
}
