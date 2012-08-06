package test.graphiti.nonemf.diagram.tools;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.context.IDoubleClickContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.tb.ContextButtonEntry;
import org.eclipse.graphiti.tb.ContextEntryHelper;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonEntry;
import org.eclipse.graphiti.tb.IContextButtonPadData;

import test.graphiti.nonemf.diagram.features.CollapseDummyFeature;
import test.graphiti.nonemf.diagram.features.DoubleClickStatementRuleFeature;

/**
 * @author Nikolai Raitsev
 *
 */
public class ToolBehaviorProvider extends DefaultToolBehaviorProvider {

	public ToolBehaviorProvider(IDiagramTypeProvider diagramTypeProvider) {
		super(diagramTypeProvider);
	}

	
	@Override
    public IContextButtonPadData getContextButtonPad(IPictogramElementContext context) {
        IContextButtonPadData data = super.getContextButtonPad(context);
        PictogramElement pe = context.getPictogramElement();

        // 1. set the generic context buttons
        // note, that we do not add 'remove' (just as an example)
        setGenericContextButtons(data, pe, CONTEXT_BUTTON_DELETE | CONTEXT_BUTTON_UPDATE);

        // 2. set the collapse button
        // simply use a dummy custom feature (senseless example)
        CustomContext cc = new CustomContext(new PictogramElement[] { pe });
        ICustomFeature[] cf = getFeatureProvider().getCustomFeatures(cc);

        for (int i = 0; i < cf.length; i++) {
            ICustomFeature iCustomFeature = cf[i];
            if (iCustomFeature instanceof CollapseDummyFeature) {
                IContextButtonEntry collapseButton = ContextEntryHelper.createCollapseContextButton(true, iCustomFeature, cc);
                data.setCollapseContextButton(collapseButton);
                break;
            }
        }
        

        // 3. add one domain specific context-button, which offers all
        // available connection-features as drag&drop features...
        // 3.a. create new CreateConnectionContext
        CreateConnectionContext ccc = new CreateConnectionContext();
        ccc.setSourcePictogramElement(pe);
        Anchor anchor = null;
        if (pe instanceof Anchor) {
            anchor = (Anchor) pe;
        } else if (pe instanceof AnchorContainer) {
            // assume, that our shapes always have chopbox anchors
            anchor = Graphiti.getPeService().getChopboxAnchor((AnchorContainer) pe);
        }

        ccc.setSourceAnchor(anchor);

        // 3.b. create context button and add all applicable features
        ContextButtonEntry button = new ContextButtonEntry(null, context);
        button.setText("Create connection");
        button.setIconId(NonEmfImageProvider.IMG_EREFERENCE);
        ICreateConnectionFeature[] features = getFeatureProvider().getCreateConnectionFeatures();

        for (ICreateConnectionFeature feature : features) {
        	if (feature.isAvailable(ccc) && feature.canStartConnection(ccc))
                button.addDragAndDropFeature(feature);
        }

        // 3.c. add context button, if it contains at least one feature
        if (button.getDragAndDropFeatures().size() > 0) {
           data.getDomainSpecificContextButtons().add(button);
        }
        
        return data;
    }


	@Override
	public ICustomFeature getDoubleClickFeature(IDoubleClickContext context) {
		
		System.out.println("Double clicked: " + context);
		
		DoubleClickStatementRuleFeature doubleClickStRuleFeature = 
				new DoubleClickStatementRuleFeature(getFeatureProvider());
		
		if(doubleClickStRuleFeature.canExecute(context))
			return doubleClickStRuleFeature;
		
		return super.getDoubleClickFeature(context);
	}
	
	
	
	
	
	

}
