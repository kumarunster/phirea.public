package test.graphiti.nonemf.rcpapp.objectsrepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import test.graphiti.nonemf.diagram.POJOIndependenceSolver;
import test.graphiti.nonemf.domainmodel.INonEmfDomainObject;
import test.graphiti.nonemf.domainmodel.Predicate;
import test.graphiti.nonemf.domainmodel.TermClass;

public class NonEmfTreeContentProvider implements ITreeContentProvider {

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof POJOIndependenceSolver) {
			Map<String, Object> objectMap = ((POJOIndependenceSolver) parentElement)
					.getObjectMap();
			Map<String, Predicate> predicatesMap = ((POJOIndependenceSolver) parentElement)
					.getPredicatesMap();

//			Object[] result = new Object[] { objectMap, predicatesMap };
			Object[] result = new Object[] { objectMap };
			return result;
		}
		
		if(parentElement instanceof Map<?, ?>){
			
			List<INonEmfDomainObject> resultObjects = new ArrayList<INonEmfDomainObject>();
			Collection<?> objList = ((Map<?,?>) parentElement).values();
			for(Object domainObj : objList)
			{
				if(domainObj instanceof TermClass)
					resultObjects.add((INonEmfDomainObject) domainObj);
			}
			
			return resultObjects.toArray();
		}

		if (parentElement instanceof List<?>)
			return ((List<?>) parentElement).toArray();

		return new Object[]{};
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		
		return getChildren(element).length > 0;
	}
}