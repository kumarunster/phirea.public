package test.graphiti.nonemf.rcpapp;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {

//	  layout.addView(ObjectsRepositoryView.ID, IPageLayout.LEFT,
//	                 0.3f, IPageLayout.ID_EDITOR_AREA);
//	  layout.addView(ObjectsRepositoryViewDnD.ID, IPageLayout.BOTTOM,
//                   0.5f, ObjectsRepositoryView.ID);
	  
	  layout.addView(ObjectsRepositoryViewNonEmf.ID, IPageLayout.LEFT,
              0.3f, IPageLayout.ID_EDITOR_AREA);
	  
	  
	}
}
