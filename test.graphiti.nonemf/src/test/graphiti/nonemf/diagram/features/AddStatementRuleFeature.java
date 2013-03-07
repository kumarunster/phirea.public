package test.graphiti.nonemf.diagram.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;

import test.graphiti.nonemf.diagram.utils.GraphitiPictogramElementProperties.StatementRulePropertyType;
import test.graphiti.nonemf.domainmodel.StatementRule;

/**
 * @author Nikolai Raitsev
 *
 */
public class AddStatementRuleFeature extends AbstractAddFeature{

	public AddStatementRuleFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canAdd(IAddContext context) {
        // return true if given business object is an EReference
        // note, that the context must be an instance of IAddConnectionContext
        if (context instanceof IAddConnectionContext
            && context.getNewObject() instanceof StatementRule) {
            return true;
        }
        return false;
	}

	@Override
	public PictogramElement add(IAddContext context) {
        IAddConnectionContext addConContext = (IAddConnectionContext) context;

        StatementRule addedStatementRule = (StatementRule) context.getNewObject();
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        IPeService peService = Graphiti.getPeService();
        

        // CONNECTION WITH POLYLINE
//        Connection connection = peCreateService
//            .createFreeFormConnection(getDiagram());
        Connection connection = peCreateService.createManhattanConnection(getDiagram());
        connection.setStart(addConContext.getSourceAnchor());
        connection.setEnd(addConContext.getTargetAnchor());

        IGaService gaService = Graphiti.getGaService();
        Polyline polyline = gaService.createPolyline(connection);
        polyline.setLineWidth(2);
        polyline.setForeground(manageColor(IColorConstant.BLACK));

        // create link and wire it
        link(connection, addedStatementRule);
        
        
        createStetementRuleTextDecorators(addedStatementRule, connection, getDiagram());
        
	    

        // add static graphical decorator (composition and navigable)
        ConnectionDecorator cd;
        cd = peCreateService.createConnectionDecorator(connection, false, 1.0, true);

        createArrow(cd);
        
        return connection;
	}

	public static void createStetementRuleTextDecorators(StatementRule addedStatementRule,
														Connection connection,
														Diagram diagram) {
		
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IPeService peService = Graphiti.getPeService();
		IGaService gaService = Graphiti.getGaService();
		
		
		ConnectionDecorator fromCardinaltiyConnDecorator = 
        			peCreateService.createConnectionDecorator(connection, true, 0.05, true);
        
        Text txtFromCardinality = gaService.createDefaultText(diagram, fromCardinaltiyConnDecorator);
        Color color = Graphiti.getGaService().manageColor(diagram, IColorConstant.BLACK);
        txtFromCardinality.setForeground(color);
        gaService.setLocation(txtFromCardinality, 10, 0);
        txtFromCardinality.setValue(addedStatementRule.getFromCardinality().getCardinalityName());
        
        // add dynamic text decorator for the association name
        ConnectionDecorator textDecorator = peCreateService.createConnectionDecorator(connection, true, 0.5, true);

        Text txtPredicateName = gaService.createDefaultText(diagram, textDecorator);
        txtPredicateName.setForeground(color);
        gaService.setLocation(txtPredicateName, 5, 5);
        txtPredicateName.setValue(addedStatementRule.getPredicate().getName());
        
        
        ConnectionDecorator toCardinaltiyConnDecorator = 
    			peCreateService.createConnectionDecorator(connection, true, 0.95, true);
    
	    Text txtToCardinality = gaService.createDefaultText(diagram, toCardinaltiyConnDecorator);
	    txtToCardinality.setForeground(color);
	    gaService.setLocation(txtToCardinality, 0, -10);
	    txtToCardinality.setValue(addedStatementRule.getToCardinality().getCardinalityName());
        
	    peService.setPropertyValue(txtFromCardinality, StatementRulePropertyType.StatementRulePropertyKey, StatementRulePropertyType.FROM.getPropertyValue());
	    peService.setPropertyValue(txtToCardinality, StatementRulePropertyType.StatementRulePropertyKey, StatementRulePropertyType.TO.getPropertyValue());
	    peService.setPropertyValue(txtPredicateName, StatementRulePropertyType.StatementRulePropertyKey, StatementRulePropertyType.PREDICATE_NAME.getPropertyValue());
	}
	
	
    private Polyline createArrow(GraphicsAlgorithmContainer gaContainer) {
        IGaService gaService = Graphiti.getGaService();
        Polyline polyline = gaService.createPolyline(gaContainer, new int[] { -12, 7, 0, 0, -12, -7 });
        polyline.setForeground(manageColor(IColorConstant.BLACK));
        polyline.setLineWidth(2);

        return polyline;
    }

}
