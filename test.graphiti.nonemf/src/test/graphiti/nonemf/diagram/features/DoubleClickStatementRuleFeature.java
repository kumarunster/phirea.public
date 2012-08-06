package test.graphiti.nonemf.diagram.features;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import test.graphiti.nonemf.diagram.DiagramFeatureProvider;
import test.graphiti.nonemf.diagram.POJOIndependenceSolver;
import test.graphiti.nonemf.dialogs.StatementRulePropertiesDialog;
import test.graphiti.nonemf.domainmodel.StatementRule;

public class DoubleClickStatementRuleFeature extends AbstractCustomFeature {

	private boolean hasDoneChanges = false;

	public DoubleClickStatementRuleFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Re&name EClass"; //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return "Change the name of the EClass"; //$NON-NLS-1$
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			PictogramElement pe = context.getPictogramElements()[0];
			
			StatementRule statementRule = getStatementRuleFromPictogramm(pe);
			
	        // support direct editing, if it is a EClass, and the user clicked
	        // directly on the text and not somewhere else in the rectangle
	        if (statementRule != null && (
	        		statementRule instanceof StatementRule) ) {
	            return true;
	        }
		}

        // direct editing not supported in all other cases
        return false;
	}

	@Override
	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			
			PictogramElement pe = pes[0];
			
			StatementRule statementRule = getStatementRuleFromPictogramm(pe);
			
	        // support direct editing, if it is a EClass, and the user clicked
	        // directly on the text and not somewhere else in the rectangle
	        if (statementRule != null && (
	        		statementRule instanceof StatementRule) ) {
				// ask user for a new class name
				
				StatementRulePropertiesDialog stRulePropertiesDialog = new StatementRulePropertiesDialog(
						Display.getCurrent().getActiveShell(), SWT.NONE);
				POJOIndependenceSolver pojoIndependenceSolver = ((DiagramFeatureProvider) getFeatureProvider()).getPojoIndependenceSolver();
				stRulePropertiesDialog.init(statementRule, pojoIndependenceSolver);
				stRulePropertiesDialog.open();
				
				if(stRulePropertiesDialog.isBtnOkPressed()) {
					System.out.println("Yeppie, OK is pressed!");
					statementRule = stRulePropertiesDialog.getModifiedStatementRule();
					this.hasDoneChanges = true;
					
					// we know, that pe is the Shape of the Text, so its container is the
			        // main shape of the EClass
					for( EObject eObj : pe.eContents()) {
						if(eObj instanceof ConnectionDecorator && 
								((ConnectionDecorator) eObj)
								.getGraphicsAlgorithm() instanceof Text ) {
							
							System.out.println(eObj);
							((Text) ((ConnectionDecorator) eObj)
									.getGraphicsAlgorithm())
									.setValue(statementRule.getPredicate().getName());
							updatePictogramElement((ConnectionDecorator) eObj);
						}
					}
			        	
			        
				}
			}
		}
	}

	@Override
	public boolean hasDoneChanges() {
		return this.hasDoneChanges;
	}
	
	
    private StatementRule getStatementRuleFromPictogramm(PictogramElement pe) {
    	StatementRule result = null;
    	
    	EObject eContainer = pe;
		if(pe instanceof Connection) {
			
			Object bo = getBusinessObjectForPictogramElement( (Connection) eContainer);
			if(bo instanceof StatementRule)
				result = (StatementRule) bo;
		}
		
		return result;
    }
}

