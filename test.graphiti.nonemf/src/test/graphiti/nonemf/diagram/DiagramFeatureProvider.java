package test.graphiti.nonemf.diagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDeleteContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;

import test.graphiti.nonemf.diagram.features.AddStatementRuleFeature;
import test.graphiti.nonemf.diagram.features.AddTermClassFeature;
import test.graphiti.nonemf.diagram.features.CollapseDummyFeature;
import test.graphiti.nonemf.diagram.features.CreateStatementRuleFeature;
import test.graphiti.nonemf.diagram.features.CreateTermClassFeature;
import test.graphiti.nonemf.diagram.features.DeleteFeature;
import test.graphiti.nonemf.diagram.features.DirectEditStatementRuleFeature;
import test.graphiti.nonemf.diagram.features.DirectEditTermClassFeature;
import test.graphiti.nonemf.diagram.features.DrillDownAssociateDiagramFeature;
import test.graphiti.nonemf.diagram.features.DrillDownTermClassFeature;
import test.graphiti.nonemf.diagram.features.GEFLayoutDiagramFeature;
import test.graphiti.nonemf.diagram.features.LayoutTermClassFeature;
import test.graphiti.nonemf.diagram.features.NonEmfDefaultUpdateDiagramFeature;
import test.graphiti.nonemf.diagram.features.UpdateStatementRuleFeature;
import test.graphiti.nonemf.diagram.features.UpdateTermClassFeature;
import test.graphiti.nonemf.diagram.features.ZestLayoutDiagramFeature;
import test.graphiti.nonemf.domainmodel.StatementRule;
import test.graphiti.nonemf.domainmodel.TermClass;

/**
 * @author Nikolai Raitsev
 *
 */
public class DiagramFeatureProvider extends DefaultFeatureProvider {

	POJOIndependenceSolver pojoIndependenceSolver;

	public DiagramFeatureProvider(IDiagramTypeProvider dtp) {
		super(dtp);
		
		if(pojoIndependenceSolver == null)
			pojoIndependenceSolver = new POJOIndependenceSolver();
		
		setIndependenceSolver(pojoIndependenceSolver);
	}
	
	

	@Override
	public IAddFeature getAddFeature(IAddContext context) {
		// is object for add request a TermClass?
		if (context.getNewObject() instanceof TermClass) {
			return new AddTermClassFeature(this);
		} else if (context.getNewObject() instanceof StatementRule) {
			return new AddStatementRuleFeature(this);
		}

		return super.getAddFeature(context);
	}

	@Override
	public ICreateFeature[] getCreateFeatures() {
		return new ICreateFeature[] { new CreateTermClassFeature(this) };
	}

	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context) {
		PictogramElement pictogramElement = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pictogramElement);
		if (bo instanceof TermClass) {
			return new LayoutTermClassFeature(this);
		}

		return super.getLayoutFeature(context);
	}

	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures() {
		return new ICreateConnectionFeature[] {	new CreateStatementRuleFeature(this) };
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(
			IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		
		if(bo instanceof TermClass) {
			return new DirectEditTermClassFeature(this);
		}
		
		EObject eContainer = pe.eContainer();
		if(eContainer instanceof Connection) {
			
			bo = getBusinessObjectForPictogramElement( (Connection) eContainer);
			if(bo instanceof StatementRule) {
				return new DirectEditStatementRuleFeature(this);
			}
		}
		
		return super.getDirectEditingFeature(context);
	}

	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context) {
		
		//Overwrites default Graphiti diagram feature, updates connections...
		if (context.getPictogramElement() instanceof Diagram) {
			return new NonEmfDefaultUpdateDiagramFeature(this);
		}
				
		
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		
		if(bo instanceof TermClass) {
			return new UpdateTermClassFeature(this);
		}
		
		if(bo instanceof StatementRule) {
			return new UpdateStatementRuleFeature(this);
		}
		
		return super.getUpdateFeature(context);
	}

	@Override
	public ICustomFeature[] getCustomFeatures(ICustomContext context) {
		List<ICustomFeature> features = new ArrayList<ICustomFeature>(Arrays.asList(super.getCustomFeatures(context)));

		features.add(new CollapseDummyFeature(this));
		
		ZestLayoutDiagramFeature zestLayoutFeature = new ZestLayoutDiagramFeature(this);
        if (zestLayoutFeature.canExecute(context)) {
            features.add(zestLayoutFeature);
        }
		
        GEFLayoutDiagramFeature gefLayoutFeature = new GEFLayoutDiagramFeature(this);
        if(gefLayoutFeature.canExecute(context))
        	features.add(gefLayoutFeature);
        
        features.add(new DrillDownTermClassFeature(this));
        features.add(new DrillDownAssociateDiagramFeature(this));
        
		return features.toArray(new ICustomFeature[0]);
	}


	public POJOIndependenceSolver getPojoIndependenceSolver() {
		return pojoIndependenceSolver;
	}



	public void setPojoIndependenceSolver(
			POJOIndependenceSolver pojoIndependenceSolver) {
		this.pojoIndependenceSolver = pojoIndependenceSolver;
		this.setIndependenceSolver(this.pojoIndependenceSolver);
	}



	@Override
	public IDeleteFeature getDeleteFeature(IDeleteContext context) {
		return new DeleteFeature(this);
	}
	
	
	
	
}
