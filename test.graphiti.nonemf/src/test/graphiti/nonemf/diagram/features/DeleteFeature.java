package test.graphiti.nonemf.diagram.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.ui.features.DefaultDeleteFeature;

import test.graphiti.nonemf.diagram.DiagramFeatureProvider;
import test.graphiti.nonemf.diagram.POJOIndependenceSolver;

public class DeleteFeature extends DefaultDeleteFeature {

	public DeleteFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	protected void deleteBusinessObjects(Object[] businessObjects) {
		super.deleteBusinessObjects(businessObjects);
		
		IFeatureProvider featureProvider = getFeatureProvider();
		if(featureProvider instanceof DiagramFeatureProvider) {
			POJOIndependenceSolver pojoIndependenceSolver = ((DiagramFeatureProvider) featureProvider).getPojoIndependenceSolver();
			for(Object obj : businessObjects) {
				pojoIndependenceSolver.removeBusinessObject(obj);
			}
		}
	}
}
