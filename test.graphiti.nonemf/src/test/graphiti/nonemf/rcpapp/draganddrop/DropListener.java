package test.graphiti.nonemf.rcpapp.draganddrop;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

public class DropListener extends ViewerDropAdapter
{

  public DropListener(Viewer viewer)
  {
    super(viewer);
  }

  @Override
  public void drop(DropTargetEvent event)
  {
    int location = this.determineLocation(event);
    Object determineTarget = determineTarget(event);
    MyModel myModelTarget = null;
    MyModel source = null;
    if(determineTarget instanceof MyModel)
    {
      myModelTarget = (MyModel) determineTarget;
    }
    if(LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType))
    {
    	source = (MyModel) ((IStructuredSelection) event.data).getFirstElement();
    }
    String translatedLocation ="";
    int targetPos = 0;
    switch (location){
    case LOCATION_BEFORE :
      translatedLocation = "Dropped before the target ";
      targetPos = DataModelProviderDnD.INSTANCE.getModel().indexOf(myModelTarget);
      DataModelProviderDnD.INSTANCE.getModel().add(targetPos, source);
      
      break;
    case LOCATION_AFTER :
      translatedLocation = "Dropped after the target ";
      targetPos = DataModelProviderDnD.INSTANCE.getModel().indexOf(myModelTarget);
      
      //Insert to Last Position
      if(targetPos + 1 == DataModelProviderDnD.INSTANCE.getModel().size()) {
    	  DataModelProviderDnD.INSTANCE.getModel().add(source);
      }else {
    	  DataModelProviderDnD.INSTANCE.getModel().add(targetPos + 1, source);
      }
      
      break;
    case LOCATION_ON :
      translatedLocation = "Dropped on the target ";
      myModelTarget.getChildren().add(source);
      break;
    case LOCATION_NONE :
      translatedLocation = "Dropped into nothing ";
      DataModelProviderDnD.INSTANCE.getModel().add(source);
      break;
    }
    System.out.println(translatedLocation);
    System.out.println("The drop was done on the element: " + myModelTarget);
    super.drop(event);
  }



  @Override
  public boolean performDrop(Object data)
  {
    System.out.println("performDrop");
    System.out.println(data);

//    if(data instanceof MyModel)
//    {
//      DataModelProviderDnD.INSTANCE.getModel().add((MyModel) data);
//      
//    }
//    if(data instanceof String)
//    {
//    	String[] myStrData = ((String) data).split(",");
//    	if(myStrData.length == 2)
//    	{
//    		MyModel myModel = new MyModel(myStrData[0], myStrData[1]);
//    		return performDrop(myModel);
//    	}
//    }
//    if(data instanceof IStructuredSelection)
//    {
//    	Object obj = ((IStructuredSelection) data).getFirstElement();
//    	return performDrop(obj);
//    }
    
    this.getViewer().setInput(DataModelProviderDnD.INSTANCE.getModel());
    return false;
  }

  @Override
  public boolean validateDrop(Object arg0, int arg1, TransferData arg2)
  {
    System.out.println("ValidateDrop");
    System.out.println(arg0);
    System.out.println(arg1);
    System.out.println(arg2);
    return true;
  }

}
