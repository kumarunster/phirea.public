package test.graphiti.nonemf.diagram.dnd;

import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.impl.CreateContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import test.graphiti.nonemf.diagram.NonEmfDiagramEditor;
import test.graphiti.nonemf.diagram.features.CreateTermClassFeature;
import test.graphiti.nonemf.domainmodel.TermClass;

public class NonEmfTransferDropTargetListener implements
		TransferDropTargetListener {

	private NonEmfDiagramEditor nonEmfDiagramEditor;
	
	public NonEmfTransferDropTargetListener(
			NonEmfDiagramEditor nonEmfDiagramEditor) {
		this.nonEmfDiagramEditor = nonEmfDiagramEditor;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		System.out.println("dragEnter" + event);
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
		System.out.println("dragLeave" + event);
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		System.out.println("dragOperationChanged" + event);
	}

	@Override
	public void dragOver(DropTargetEvent event) {
//		ILocation currentMouseLocation = this.nonEmfDiagramEditor.getCurrentMouseLocation();
		
		org.eclipse.draw2d.geometry.Point mouseLocation = this.nonEmfDiagramEditor.getMouseLocation();
		System.out.println("dragOver, mouse editor: " + mouseLocation);
		
		Point cursorLocation = PlatformUI.getWorkbench().getDisplay().getCursorLocation();
		System.out.println("dragOver, mouse rcpswt: " + cursorLocation);
		
		
		
		System.out.println("isEnabled, mouse event: (" + event.x + ", " + event.y + ")");
		
//		System.out.println("dragOver" + event);
		
		
	}

	@Override
	public void drop(DropTargetEvent event) {
		System.out.println("drop " + event);
		
		System.out.println("drop.data.class " + event.data.getClass().getSimpleName());
		
		if(event.data instanceof IStructuredSelection)
		{
//			MyModel myModel = (MyModel) ((IStructuredSelection) event.data).getFirstElement();
			System.out.println("drop instance is IStructuredSelection" );
//			ILocation currentMouseLocation = this.nonEmfDiagramEditor.getCurrentMouseLocation();
			
			Point point = new Point(event.x, event.y);
			
			Point relativePoint = Display.getCurrent().map(null, 
					this.nonEmfDiagramEditor.getGraphicalViewer().getControl(), 
					point);
			
			IFeatureProvider featureProvider = this.nonEmfDiagramEditor.getDiagramTypeProvider().getFeatureProvider();
			Diagram diagram = this.nonEmfDiagramEditor.getDiagramTypeProvider().getDiagram();
			
			ICreateFeature[] createFeatures = featureProvider.getCreateFeatures();
        	for(ICreateFeature createFeature: createFeatures) {
        		if(createFeature instanceof CreateTermClassFeature) {
        			try {
        				CreateContext createContext = new CreateContext();
//        				ILocation targetLocation = currentMouseLocation;
        				
        				
//        				System.out.println("Mouse location: " + targetLocation);
        				
//						createContext.setLocation(targetLocation.getX(), targetLocation.getY());
        				createContext.setLocation(relativePoint.x, relativePoint.y);
												
						createContext.setTargetContainer(diagram);
						
						Object[] createdObj = ((CreateTermClassFeature) createFeature)
								.create((ICreateContext) createContext, "");
						TermClass target = (TermClass) createdObj[0];
	        			
	        			System.out.println("created TermClass " + target.getName());
	        			
					} catch (Exception e) {
						e.printStackTrace();
					}
        			
        			
        		}
        	}
		}
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		System.out.println("dropAccept" + event);
	}

	@Override
	public Transfer getTransfer() {
		
//		return TextTransfer.getInstance();
		return LocalSelectionTransfer.getTransfer();
				
//		return null;
	}

	@Override
	public boolean isEnabled(DropTargetEvent event) {
		org.eclipse.draw2d.geometry.Point mouseLocation = this.nonEmfDiagramEditor.getMouseLocation();
//		ILocation currentMouseLocation = this.nonEmfDiagramEditor.getMouseLocation();
		System.out.println("isEnabled, mouse: " + mouseLocation);
		
		Point cursorLocation = PlatformUI.getWorkbench().getDisplay().getCursorLocation();
		System.out.println("isEnabled, mouse rcpswt: " + cursorLocation);
		
		System.out.println("isEnabled, mouse event: (" + event.x + ", " + event.y + ")");
		
		
		
		if(LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType))
		{
//			Object data = event.data;
//			System.out.println("isEnabled LocalSelTransfer: " + LocalSelectionTransfer.getTransfer() );
//			IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().getSelection();
//			if(selection != null)
//			{
//				data = selection.getFirstElement();
//				System.out.println("isEnabled: data from selection!");
//			}
//			if(data instanceof MyModel)
//			{
//				System.out.println("isEnabled: data is my model");
//				return true;
//			}
			return true;
		}
		return false;
	}

}
