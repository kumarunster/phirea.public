package test.graphiti.nonemf.diagram.features;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import test.graphiti.nonemf.domainmodel.TermClass;
import test.graphiti.nonemf.rcpapp.objectsrepository.ObjectsRepositoryViewNonEmf;
import test.graphiti.nonemf.utils.EventCommunicationConstants;

/**
 * @author Nikolai Raitsev
 *
 */
public class DirectEditTermClassFeature extends AbstractDirectEditingFeature {

	public DirectEditTermClassFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public int getEditingType() {
		return TYPE_TEXT;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		Object bo = getBusinessObjectForPictogramElement(pe);
		TermClass bc = (TermClass) bo;
		return bc.getName();
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
        Object bo = getBusinessObjectForPictogramElement(pe);
        GraphicsAlgorithm ga = context.getGraphicsAlgorithm();

        // support direct editing, if it is a EClass, and the user clicked
        // directly on the text and not somewhere else in the rectangle
        if (bo instanceof TermClass && ga instanceof Text) {
            return true;
        }

        // direct editing not supported in all other cases
        return false;
	}
	
    @Override
    public String checkValueValid(String value, IDirectEditingContext context) {

        if (value.length() < 1)
            return "Please enter any text as class name.";

        if (value.contains(" "))
            return "Spaces are not allowed in class names.";

        if (value.contains("\n"))
            return "Line breakes are not allowed in class names.";

        // null means, that the value is valid
        return null;

    }
    
    @Override
    public void setValue(String value, IDirectEditingContext context) {

        // set the new name for the MOF class
        PictogramElement pe = context.getPictogramElement();
        TermClass bc = (TermClass) getBusinessObjectForPictogramElement(pe);
        bc.setName(value);

        // Explicitly update the shape to display the new value in the diagram
        // Note, that this might not be necessary in future versions of Graphiti
        // (currently in discussion)

        // we know, that pe is the Shape of the Text, so its container is the
        // main shape of the EClass
        updatePictogramElement(((Shape) pe).getContainer());
        
        publishViewUpdateEvent(bc);
    }

	private void publishViewUpdateEvent(TermClass bc) {
		BundleContext bundleContext = FrameworkUtil.getBundle(ObjectsRepositoryViewNonEmf.class).getBundleContext();
		ServiceReference<EventAdmin> srEventAdmin = bundleContext.getServiceReference(EventAdmin.class);
		EventAdmin eventAdmin = bundleContext.getService(srEventAdmin);
		
		Map<String,Object> properties = new HashMap<String, Object>();
		properties.put(EventCommunicationConstants.DATA, bc);

		
		Event event = new Event(EventCommunicationConstants.VIEWCOMMUNICATION + "/updatetermclass", properties);
		eventAdmin.postEvent(event);
		
	}

}
