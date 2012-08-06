package test.graphiti.nonemf.diagram;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

/**
 * @author Nikolai Raitsev
 *
 */
public class NonEmfNotificationService extends DefaultNotificationService {

	public NonEmfNotificationService(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}

	@Override
	public void updatePictogramElements(PictogramElement[] dirtyPes) {
		// TODO Auto-generated method stub
		super.updatePictogramElements(dirtyPes);
	}

	@Override
	public PictogramElement[] calculateRelatedPictogramElements(
			Object[] changedBOs) {
		// TODO Auto-generated method stub
		return super.calculateRelatedPictogramElements(changedBOs);
	}
	
	

}
