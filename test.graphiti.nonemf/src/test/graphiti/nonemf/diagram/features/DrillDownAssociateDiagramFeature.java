package test.graphiti.nonemf.diagram.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;

public class DrillDownAssociateDiagramFeature extends AbstractCustomFeature {

	public DrillDownAssociateDiagramFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public String getDescription() {
		return "Associates a new or existing Diagram to given Element";
	}



	@Override
	public String getName() {
		return "&Associate Diagram";
	}
	
	
	

	@Override
	public boolean canExecute(ICustomContext context) {
		boolean canExecute = super.canExecute(context);
		System.out.println("can execute associate diagram? " + canExecute);
		return true;
	}



	@Override
	public void execute(ICustomContext context) {
		// TODO Auto-generated method stub
		System.out.println("Execute Associate Diagram");
	}

}
