package test.graphiti.nonemf.diagram.features;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import test.graphiti.nonemf.diagram.DiagramFeatureProvider;
import test.graphiti.nonemf.diagram.POJOIndependenceSolver;
import test.graphiti.nonemf.domainmodel.Predicate;
import test.graphiti.nonemf.domainmodel.StatementRule;

/**
 * @author Nikolai Raitsev
 *
 */
public class DirectEditStatementRuleFeature extends AbstractDirectEditingFeature {

	public DirectEditStatementRuleFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
//		return TYPE_TEXT;
		return TYPE_DROPDOWN;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context) {
		String result = "";
		PictogramElement pe = context.getPictogramElement();
		
		StatementRule statementRule = getStatementRuleFromPictogramm(pe);
		if(statementRule != null)
			result = statementRule.getPredicate().getName();
		
		return result;
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		GraphicsAlgorithm ga = context.getGraphicsAlgorithm();
        
		StatementRule statementRule = getStatementRuleFromPictogramm(pe);
		
        // support direct editing, if it is a EClass, and the user clicked
        // directly on the text and not somewhere else in the rectangle
        if (statementRule != null && (
        		statementRule instanceof StatementRule && ga instanceof Text) ) {
            return true;
        }

        // direct editing not supported in all other cases
        return false;
	}
	
    @Override
    public String checkValueValid(String value, IDirectEditingContext context) {

        if (value.length() < 1)
            return "Please enter any text as class name.";

//        if (value.contains(" "))
//            return "Spaces are not allowed in class names.";

        if (value.contains("\n"))
            return "Line breakes are not allowed in class names.";

        // null means, that the value is valid
        return null;

    }
    
    @Override
    public void setValue(String value, IDirectEditingContext context) {

        // set the new name for the MOF class
        PictogramElement pe = context.getPictogramElement();
        
        StatementRule statementRule = getStatementRuleFromPictogramm(pe);
        if(statementRule != null) {
        	Predicate predicate = statementRule.getPredicate();
        	
        	if(!value.equals(predicate.getName())) {
        		
        		Predicate selectedPredicate = null;
        		
        		POJOIndependenceSolver pojoIndependenceSolver = ((DiagramFeatureProvider) getFeatureProvider()).getPojoIndependenceSolver();
        		List<Predicate> predicatesList = pojoIndependenceSolver.getPredicatesList();
        		for(Predicate pr : predicatesList) {
        			if(pr.getName().equals(value)) {
        				selectedPredicate = pr;
        				break;
        			}
        		}
        		
        		if(selectedPredicate == null) {
        			selectedPredicate = new Predicate();
        			selectedPredicate.setName(value);
        			pojoIndependenceSolver.registerPredicate(selectedPredicate);
        		}
        		
        		statementRule.setPredicate(selectedPredicate);
        	}
        }

        // Explicitly update the shape to display the new value in the diagram
        // Note, that this might not be necessary in future versions of Graphiti
        // (currently in discussion)

        // we know, that pe is the Shape of the Text, so its container is the
        // main shape of the EClass
        GraphicsAlgorithm ga = pe.getGraphicsAlgorithm();
        if(ga instanceof Text) {
        	((Text) ga).setValue(value);
        }
        	
        updatePictogramElement((ConnectionDecorator) pe);

    }
    
    
    private StatementRule getStatementRuleFromPictogramm(PictogramElement pe) {
    	StatementRule result = null;
    	
    	EObject eContainer = pe.eContainer();
		if(eContainer instanceof Connection) {
			
			Object bo = getBusinessObjectForPictogramElement( (Connection) eContainer);
			if(bo instanceof StatementRule)
				result = (StatementRule) bo;
		}
		
		return result;
    }

	@Override
	public String[] getPossibleValues(IDirectEditingContext context) {
		
		List<Predicate> predicatesList = ((DiagramFeatureProvider) getFeatureProvider()).getPojoIndependenceSolver().getPredicatesList();
		
		List<String> result = new ArrayList<String>();
		for(Predicate predicate : predicatesList) {
			result.add(predicate.getName());
		}
		
		String [] stringArray = new String[]{""};
		return result.toArray(stringArray);
	}

	@Override
	public boolean stretchFieldToFitText() {
		return true;
	}

}
