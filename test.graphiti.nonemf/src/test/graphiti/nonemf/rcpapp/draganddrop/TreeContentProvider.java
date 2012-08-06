package test.graphiti.nonemf.rcpapp.draganddrop;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TreeContentProvider implements ITreeContentProvider {

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
  }


  public void dispose() {
  }


  public Object[] getElements(Object inputElement) {
    return getChildren(inputElement);
  }


  public Object[] getChildren(Object parentElement) {

    if(parentElement instanceof MyModel)
    {
      return ((MyModel) parentElement).getChildren().toArray();
    }

    if(parentElement instanceof List<?>)
      return ((List) parentElement).toArray();

    return new Object[] { "item_0", "item_1", "item_2" };
  }
  public Object getParent(Object element) {
    return null;
  }
  public boolean hasChildren(Object element) {

    if(element instanceof MyModel)
    {
      return ((MyModel) element).getChildren().size() > 0;
    }

    return getChildren(element).length > 0;
  }
}