package test.graphiti.nonemf.rcpapp;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
	  
	  layout.addView(ObjectsRepositoryViewNonEmf.ID, IPageLayout.LEFT,
              0.3f, IPageLayout.ID_EDITOR_AREA);
	  
	  
	}
}
