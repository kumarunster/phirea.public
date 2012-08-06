package test.graphiti.nonemf.rcpapp.draganddrop;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class TreeLabelProvider extends LabelProvider implements ITableLabelProvider {

  public Image getImage(Object element) {
    return super.getImage(element);
  }


  public String getText(Object element) {
    return super.getText(element);
  }


  @Override
  public Image getColumnImage(Object paramObject, int paramInt)
  {
    return null;
  }


  @Override
  public String getColumnText(Object paramObject, int paramInt)
  {
    String result = "";

    if(paramObject instanceof MyModel)
    {
      switch (paramInt)
      {
      case 0:
        result = ((MyModel) paramObject).getName();
        break;
      case 1:
        result = ((MyModel) paramObject).getDescription();
        break;

      default:
        break;
      }
    }


    return result;
  }


}