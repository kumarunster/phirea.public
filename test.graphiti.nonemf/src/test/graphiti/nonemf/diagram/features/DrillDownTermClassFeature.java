package test.graphiti.nonemf.diagram.features;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.ui.features.AbstractDrillDownFeature;

public class DrillDownTermClassFeature extends AbstractDrillDownFeature {

	public DrillDownTermClassFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public String getDescription() {
		return "Opens a Diagram (if a linked diagram exist";
	}



	@Override
	public String getName() {
		return "&Open associated Diagram";
	}
	
	

	
	@Override
	public void execute(ICustomContext context) {
		super.execute(context);
		
		System.out.println("execute DrillDOwn!");
	}


	@Override
	public boolean canExecute(ICustomContext context) {
		boolean canExecute = super.canExecute(context);
		System.out.println("can execute drill down? " + canExecute);
		return true;
	}


	@Override
	protected Collection<Diagram> getDiagrams() {
		System.out.println("returning Diagrams for the drill down feature");
		return new ArrayList<Diagram>();
	}

}
