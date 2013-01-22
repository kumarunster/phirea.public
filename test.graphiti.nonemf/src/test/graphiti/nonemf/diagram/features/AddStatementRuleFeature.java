package test.graphiti.nonemf.diagram.features;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

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

        // CONNECTION WITH POLYLINE
        Connection connection = peCreateService
            .createFreeFormConnection(getDiagram());
        connection.setStart(addConContext.getSourceAnchor());
        connection.setEnd(addConContext.getTargetAnchor());

        IGaService gaService = Graphiti.getGaService();
        Polyline polyline = gaService.createPolyline(connection);
        polyline.setLineWidth(2);
        polyline.setForeground(manageColor(IColorConstant.BLACK));

        // create link and wire it
        link(connection, addedStatementRule);
        
        
        ConnectionDecorator fromCardinaltiyConnDecorator = 
        			peCreateService.createConnectionDecorator(connection, true, 0.05, true);
        
        Text fromCardinalityText = gaService.createDefaultText(this.getDiagram(), fromCardinaltiyConnDecorator);
        fromCardinalityText.setForeground(manageColor(IColorConstant.BLACK));
        gaService.setLocation(fromCardinalityText, 10, 0);
        fromCardinalityText.setValue(addedStatementRule.getFromCardinality().getCardinalityName());
        
        
        // add dynamic text decorator for the association name
        ConnectionDecorator textDecorator = peCreateService.createConnectionDecorator(connection, true, 0.5, true);

        Text text = gaService.createDefaultText(this.getDiagram(), textDecorator);
        text.setForeground(manageColor(IColorConstant.BLACK));
        gaService.setLocation(text, 5, 5);
        text.setValue(addedStatementRule.getPredicate().getName());
        
        
        ConnectionDecorator toCardinaltiyConnDecorator = 
    			peCreateService.createConnectionDecorator(connection, true, 0.95, true);
    
	    Text toCardinalityText = gaService.createDefaultText(this.getDiagram(), toCardinaltiyConnDecorator);
	    toCardinalityText.setForeground(manageColor(IColorConstant.BLACK));
	    gaService.setLocation(toCardinalityText, 0, -10);
	    toCardinalityText.setValue(addedStatementRule.getToCardinality().getCardinalityName());
        
        

        // add static graphical decorator (composition and navigable)
        ConnectionDecorator cd;
        cd = peCreateService.createConnectionDecorator(connection, false, 1.0, true);

        createArrow(cd);
        
        return connection;
	}
	
	
    private Polyline createArrow(GraphicsAlgorithmContainer gaContainer) {
        IGaService gaService = Graphiti.getGaService();
        Polyline polyline = gaService.createPolyline(gaContainer, new int[] { -12, 7, 0, 0, -12, -7 });
        polyline.setForeground(manageColor(IColorConstant.BLACK));
        polyline.setLineWidth(2);

        return polyline;
    }

}
