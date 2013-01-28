package test.graphiti.nonemf.diagram.features;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IPeService;

import test.graphiti.nonemf.diagram.utils.GraphitiPictogramElementProperties.StatementRulePropertyType;
import test.graphiti.nonemf.domainmodel.StatementRule;

/**
 * @author Nikolai Raitsev
 *
 */
public class UpdateStatementRuleFeature extends AbstractUpdateFeature {

	public UpdateStatementRuleFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}
	
	
	public boolean canUpdate(IUpdateContext context) {
        // return true, if linked business object is a EClass
        Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
        return (bo instanceof StatementRule);
    }

 

    public IReason updateNeeded(IUpdateContext context) {
    	IReason result = Reason.createFalseReason();;

    	IPeService peService = Graphiti.getPeService();
    	
    	PictogramElement pe = context.getPictogramElement();
    	
    	if (! (pe instanceof Connection) ) {
    		return result;
    	}
        
        {
        	//check the decorators first
	        int decoratorSize = 0;
	        
            for( EObject eObj : pe.eContents()) {
				if(eObj instanceof ConnectionDecorator && 
						((ConnectionDecorator) eObj).getGraphicsAlgorithm() instanceof Text ) {
					Text txt = (Text) ((ConnectionDecorator) eObj).getGraphicsAlgorithm();
					decoratorSize ++;
					
					Property property = peService.getProperty(txt, StatementRulePropertyType.StatementRulePropertyKey);
					if(property == null) {
						result = Reason.createTrueReason("StatementRule representation does containt StatementRuleProperties");
					}
					
				}
            }
	        
	        if(!result.toBoolean() && StatementRulePropertyType.values().length != decoratorSize)
	        {
	        	result = Reason.createTrueReason("StatementRule representation does not showing all needed data");
	        }
        }
            
        
        if(!result.toBoolean())
        {
        	
        	Object bo = getBusinessObjectForPictogramElement(pe);
        	StatementRule statementRule = null;
            if (bo instanceof StatementRule) {
            	statementRule = (StatementRule) bo;
            }
        	
	    	 for( EObject eObj : pe.eContents()) {
				if(eObj instanceof ConnectionDecorator && 
						((ConnectionDecorator) eObj).getGraphicsAlgorithm() instanceof Text ) {
					Text txt = (Text) ((ConnectionDecorator) eObj).getGraphicsAlgorithm();
					
					String propertyValue = peService.getPropertyValue(txt, StatementRulePropertyType.StatementRulePropertyKey);
					StatementRulePropertyType propertyType = StatementRulePropertyType.findByValue(propertyValue);
					
					String value = txt.getValue();
					
					if(propertyType != null) {
						switch(propertyType) {
						case FROM:
							if(!value.equals(statementRule.getFromCardinality().getCardinalityName())) {
								result = Reason.createTrueReason("From-cardinality is outdated");
							}
							break;
						case TO:
							if(!value.equals(statementRule.getToCardinality().getCardinalityName())) {
								result = Reason.createTrueReason("To-cardinality is outdated");
							}
							break;
						case PREDICATE_NAME:
							if(!value.equals(statementRule.getPredicate().getName())) {
								result = Reason.createTrueReason("Predicate is outdated");
							}
							break;
						default:
							break;
						}
					}
					
					
					//FOund true result....
					if(result.toBoolean())
						break;
					
				}
	         }
        }
        
        return result;
    }

 

    public boolean update(IUpdateContext context) {
        PictogramElement pe = context.getPictogramElement();
        
        //first remove all old decorators
        List<PictogramElement> toRemove = new ArrayList<PictogramElement>();
        for( EObject eObj : pe.eContents()) {
			if(eObj instanceof ConnectionDecorator && 
					((ConnectionDecorator) eObj).getGraphicsAlgorithm() instanceof Text ) {
				toRemove.add((ConnectionDecorator) eObj);
			}
        }
        
        for(PictogramElement removePe : toRemove) {
        	Graphiti.getPeService().deletePictogramElement(removePe);
        }
        

        StatementRule statementRule = (StatementRule) getBusinessObjectForPictogramElement(pe);
        
        //Create new connectionDecorators for statement rule
        AddStatementRuleFeature.createStetementRuleTextDecorators(statementRule, (Connection) pe, getDiagram());
        
        return true;
    }

}