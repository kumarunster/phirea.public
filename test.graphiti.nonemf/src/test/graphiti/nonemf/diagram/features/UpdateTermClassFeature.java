package test.graphiti.nonemf.diagram.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

import test.graphiti.nonemf.domainmodel.TermClass;

/**
 * @author Nikolai Raitsev
 *
 */
public class UpdateTermClassFeature extends AbstractUpdateFeature {

	public UpdateTermClassFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}
	
	
	public boolean canUpdate(IUpdateContext context) {
        // return true, if linked business object is a EClass
        Object bo = getBusinessObjectForPictogramElement(context.getPictogramElement());
        return (bo instanceof TermClass);
    }

 

    public IReason updateNeeded(IUpdateContext context) {

        // retrieve name from pictogram model
        String pictogramName = null;
        PictogramElement pictogramElement = context.getPictogramElement();
        
        if (pictogramElement instanceof Shape) {
            Shape shape = (Shape) pictogramElement;
                if (shape.getGraphicsAlgorithm() instanceof Text) {
                    Text text = (Text) shape.getGraphicsAlgorithm();
                    pictogramName = text.getValue();
            }
        }
        
        if (pictogramElement instanceof ContainerShape) {
            for(Shape shape: ((ContainerShape) pictogramElement).getChildren() ) {
	            if (shape.getGraphicsAlgorithm() instanceof Text) {
	                Text text = (Text) shape.getGraphicsAlgorithm();
	                pictogramName = text.getValue();
	                
	                break;
	            }
            }
        }
 
        // retrieve name from business model
        String businessName = null;
        Object bo = getBusinessObjectForPictogramElement(pictogramElement);
        if (bo instanceof TermClass) {
        	TermClass bc = (TermClass) bo;
            businessName = bc.getName();
        }

        // update needed, if names are different
        boolean updateNameNeeded = ((pictogramName == null && businessName != null) || 
        		(pictogramName != null && !pictogramName.equals(businessName)));

        if (updateNameNeeded) {
        	
        	System.out.println("****** need update on pictogram name: " + pictogramName + "; businessName: " + businessName);
        	
            return Reason.createTrueReason("Name is out of date");

        } else {
            return Reason.createFalseReason();
        }
    }

 

    public boolean update(IUpdateContext context) {

        // retrieve name from business model
        String businessName = null;
        PictogramElement pictogramElement = context.getPictogramElement();
        Object bo = getBusinessObjectForPictogramElement(pictogramElement);
        if (bo instanceof TermClass) {
        	TermClass bc = (TermClass) bo;
            businessName = bc.getName();
        }

        // Set name in pictogram model
        if (pictogramElement instanceof ContainerShape) {
            ContainerShape cs = (ContainerShape) pictogramElement;
            for (Shape shape : cs.getChildren()) {
                if (shape.getGraphicsAlgorithm() instanceof Text) {
                    Text text = (Text) shape.getGraphicsAlgorithm();
                    text.setValue(businessName);
                    return true;
                }
            }
        }
        
        return false;
    }

}