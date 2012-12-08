package test.graphiti.nonemf.diagram.dialogs;

import java.util.List;

public interface IChangeValueCallBack {

	void setValue(Object input, Object element, Object value);
	
	Object getValue(Object element);

}
