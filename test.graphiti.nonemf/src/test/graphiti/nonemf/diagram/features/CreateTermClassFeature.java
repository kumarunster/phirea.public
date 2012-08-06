package test.graphiti.nonemf.diagram.features;

import org.eclipse.graphiti.examples.common.ExampleUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;

import test.graphiti.nonemf.domainmodel.TermClass;

/**
 * @author Nikolai Raitsev
 *
 */
public class CreateTermClassFeature extends AbstractCreateFeature {

    private static final String TITLE = "Create BusinessClass";

    private static final String USER_QUESTION = "Enter new BusinessClass name";
	
	public CreateTermClassFeature(IFeatureProvider fp) {
		super(fp, "BusinessClass", "Create BusinessClass");
	}

	@Override
	public boolean canCreate(ICreateContext context) {
		return context.getTargetContainer() instanceof Diagram;
	}

	
	public Object[] create(ICreateContext context, String name) {
		TermClass newClass = new TermClass();
		
		newClass.setName(name);
		
		return processObjectGraphicalJobs(context, newClass);
	}
	
	@Override
	public Object[] create(ICreateContext context) {

		// ask user for EClass name
        String newClassName = ExampleUtil.askString(TITLE, USER_QUESTION, "");
        if (newClassName == null || newClassName.trim().length() == 0) {
            return EMPTY;
        }

 

        // create EClass
        TermClass newClass = new TermClass();
        // Add model element to resource.
        // We add the model element to the resource of the diagram for
        // simplicity's sake. Normally, a customer would use its own
        // model persistence layer for storing the business model separately.
//        getDiagram().eResource().getContents().add(newClass);
        newClass.setName(newClassName);


        
        // return newly created business object(s)
        return processObjectGraphicalJobs(context, newClass);
	}
	
	private Object[] processObjectGraphicalJobs(ICreateContext context, TermClass termClass)
	{
        // do the add
        addGraphicalRepresentation(context, termClass);

        // activate direct editing after object creation
        getFeatureProvider().getDirectEditingInfo().setActive(true);
        
		return new Object[] { termClass };	
	}

}
