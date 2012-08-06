package test.graphiti.nonemf.diagram.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ChopboxAnchor;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import test.graphiti.nonemf.diagram.DiagramFeatureProvider;
import test.graphiti.nonemf.diagram.POJOIndependenceSolver;
import test.graphiti.nonemf.domainmodel.TermClass;

/**
 * @author Nikolai Raitsev
 *
 */
public class AddTermClassFeature extends AbstractAddShapeFeature {

	private static final IColorConstant CLASS_TEXT_FOREGROUND = new ColorConstant(51, 51, 153);

	private static final IColorConstant CLASS_FOREGROUND = new ColorConstant(255, 102, 0);

	private static final IColorConstant CLASS_BACKGROUND = new ColorConstant(255, 204, 153);

	public AddTermClassFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		boolean result = false;
		if (context.getNewObject() instanceof TermClass) {
            // check if user wants to add to a diagram
            if (context.getTargetContainer() instanceof Diagram) {
            	result = true;
            }
        }
        return result;
	}

	@Override
	public PictogramElement add(IAddContext context) {
		TermClass addedClass = (TermClass) context.getNewObject();

		Diagram targetDiagram = (Diagram) context.getTargetContainer();

		// CONTAINER SHAPE WITH ROUNDED RECTANGLE
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);

		// define a default size for the shape
		int width = 100;
		int height = 50;

		IGaService gaService = Graphiti.getGaService();
		{
			// create and set graphics algorithm
			RoundedRectangle roundedRectangle =
			gaService.createRoundedRectangle(containerShape, 5, 5);
			roundedRectangle.setForeground(manageColor(CLASS_FOREGROUND));
			roundedRectangle.setBackground(manageColor(CLASS_BACKGROUND));
			roundedRectangle.setLineWidth(2);
			gaService.setLocationAndSize(roundedRectangle,
			context.getX(), context.getY(), width, height);

			// if added Class has no resource we add it to the resource
			// of the diagram

			// in a real scenario the business model would have its own resource
//			if (addedClass.eResource() == null) {
//				getDiagram().eResource().getContents().add(addedClass);
//			}
			// create link and wire it
			link(containerShape, addedClass);
		}

		// SHAPE WITH LINE
		{
			// create shape for line
			Shape shape = peCreateService.createShape(containerShape, false);

			// create and set graphics algorithm
			Polyline polyline = gaService.createPolyline(shape, new int[] { 0, 20, width, 20 });
			polyline.setForeground(manageColor(CLASS_FOREGROUND));
			polyline.setLineWidth(2);

		}

		// SHAPE WITH TEXT

		{
			// create shape for text
			Shape shape = peCreateService.createShape(containerShape, false);

			// create and set text graphics algorithm
			Text text = gaService.createDefaultText(getDiagram(), shape,
					addedClass.getName());
			text.setForeground(manageColor(CLASS_TEXT_FOREGROUND));
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
			
			Font textFont = text.getFont();
			Font managedFont = gaService.manageFont(getDiagram(), textFont.getName(), textFont.getSize(), textFont.isItalic(), true);
			text.setFont(managedFont);
			
			gaService.setLocationAndSize(text, 0, 0, width, 20);

			// create link and wire it
			link(shape, addedClass);
		}
		
		ChopboxAnchor chopboxAnchor = peCreateService.createChopboxAnchor(containerShape);
		
		POJOIndependenceSolver pojoIndependenceSolver = ((DiagramFeatureProvider) getFeatureProvider()).getPojoIndependenceSolver();
		
		pojoIndependenceSolver.registerGraphicalObject(ChopboxAnchor.class, addedClass, chopboxAnchor);
		
		layoutPictogramElement(containerShape);
		
		return containerShape;
	}

}
