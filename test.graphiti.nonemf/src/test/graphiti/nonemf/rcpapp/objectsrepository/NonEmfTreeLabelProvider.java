package test.graphiti.nonemf.rcpapp.objectsrepository;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import test.graphiti.nonemf.domainmodel.NonEmfDomainObject;
import test.graphiti.nonemf.domainmodel.Predicate;
import test.graphiti.nonemf.domainmodel.StatementRule;
import test.graphiti.nonemf.domainmodel.TermClass;

public class NonEmfTreeLabelProvider extends LabelProvider implements ITableLabelProvider {

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

    if(paramObject instanceof NonEmfDomainObject)
    {
      switch (paramInt)
      {
      case 0:
        result = ((NonEmfDomainObject) paramObject).getName();
        break;
      case 1:
        result = paramObject.getClass().getSimpleName() + " (" + ((NonEmfDomainObject) paramObject).getId() + ")";
        break;

      default:
        break;
      }
    }
    
    if(paramObject instanceof Map<?, ?>)
    {
    	switch (paramInt) {
		case 0:
			Map<?, ?> repository = (Map<?, ?>) paramObject;
			if(repository != null && !repository.isEmpty()) {
				Entry<?, ?> firstElement = repository.entrySet().iterator().next();
				Object value = firstElement.getValue();
				if(value instanceof TermClass || value instanceof StatementRule)
					result = "TermClasses and StatementRules";
				
				if(value instanceof Predicate)
					result = "Predicates";
			}
			break;

		default:
			break;
		}
    	
    }


    return result;
  }


}