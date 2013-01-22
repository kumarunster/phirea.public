package test.graphiti.nonemf.diagram.features;

import org.eclipse.graphiti.datatypes.ILocation;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.Connection;

import test.graphiti.nonemf.diagram.DiagramFeatureProvider;
import test.graphiti.nonemf.diagram.POJOIndependenceSolver;
import test.graphiti.nonemf.domainmodel.CardinalityType;
import test.graphiti.nonemf.domainmodel.StatementRule;
import test.graphiti.nonemf.domainmodel.TermClass;
import test.graphiti.nonemf.utils.PredicatesUtil;

/**
 * @author Nikolai Raitsev
 *
 */
public class CreateStatementRuleFeature extends AbstractCreateConnectionFeature {

	public CreateStatementRuleFeature(IFeatureProvider fp) {
		super(fp, "Association", "Create Association");
	}

    public boolean canCreate(ICreateConnectionContext context) {
        // return true if both anchors belong to an BusinessClass
    	// and those BusinessClasses are not identical
        TermClass source = getBusinessClass(context.getSourceAnchor());
        TermClass target = getBusinessClass(context.getTargetAnchor());
        if (source != null && target != null && source != target) {
            return true;
        }
        
        if (source != null && target == null && source != target) {
        	return true;
        }
        
        return false;
    }

 

    public boolean canStartConnection(ICreateConnectionContext context) {
        // return true if start anchor belongs to a BusinessClass
        if (getBusinessClass(context.getSourceAnchor()) != null) {
            return true;
        }
        return false;
    }

 

    public Connection create(ICreateConnectionContext context) {
        Connection newConnection = null;
        
        // get BusinessClasses which should be connected
        TermClass source = getBusinessClass(context.getSourceAnchor());
        TermClass target = getBusinessClass(context.getTargetAnchor());

        if (source != null && target != null) {
            // create new business object
            StatementRule statementRule = createStatementRule(source, target);
            // add connection for business object
            AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), context.getTargetAnchor());
            addContext.setNewObject(statementRule);
            newConnection = (Connection) getFeatureProvider().addIfPossible(addContext);
        }
        
        if (source != null && target == null) {
        
        	ICreateFeature[] createFeatures = getFeatureProvider().getCreateFeatures();
        	for(ICreateFeature createFeature: createFeatures) {
        		if(createFeature instanceof CreateTermClassFeature) {
        			try {
        				CreateContext createContext = new CreateContext();
        				ILocation targetLocation = context.getTargetLocation();
        				CreateConnectionContext createConContext = (CreateConnectionContext) context;
        				
						createContext.setLocation(targetLocation.getX(), targetLocation.getY());
												
						createContext.setTargetContainer(getDiagram());
						
						Object[] createdObj = createFeature.create((ICreateContext) createContext);
	        			target = (TermClass) createdObj[0];
	        			
	        			
	        			POJOIndependenceSolver pojoIndependenceSolver = ((DiagramFeatureProvider) getFeatureProvider()).getPojoIndependenceSolver();
	        			
	        			ChopboxAnchor targetAnchor = (ChopboxAnchor) pojoIndependenceSolver.getGraphicalObject(ChopboxAnchor.class, target);
	        			
	        			createConContext.setTargetAnchor(targetAnchor);
	        			
					} catch (Exception e) {
						e.printStackTrace();
					}
        			
        			
        		}
        	}
        	
        	if(target != null) {
        		// create new business object
                StatementRule association = createStatementRule(source, target);
                // add connection for business object
                AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), context.getTargetAnchor());
                addContext.setNewObject(association);
                newConnection = (Connection) getFeatureProvider().addIfPossible(addContext);
        	}
        }

        return newConnection;
    }

 

    /**

     * Returns the BusinessClass belonging to the anchor, or null if not available.

     */

    private TermClass getBusinessClass(Anchor anchor) {
        if (anchor != null) {
            Object object = getBusinessObjectForPictogramElement(anchor.getParent());
            
            if (object instanceof TermClass) {
                return (TermClass) object;
            }
        }

        return null;
    }

 
    /**

    * Creates a Association between two BusinessClasses.

    */

    private StatementRule createStatementRule(TermClass source, TermClass target) {
        StatementRule statementRule = new StatementRule();
        statementRule.setName("xxx");
        statementRule.setPredicate(PredicatesUtil.getStandardPredicate());
        statementRule.setFromClass(source);
        statementRule.setToClass(target);
        statementRule.setFromCardinality(CardinalityType.ZERO_OR_ONE);
        statementRule.setToCardinality(CardinalityType.ZERO_OR_MANY);
        return statementRule;
   }

}
