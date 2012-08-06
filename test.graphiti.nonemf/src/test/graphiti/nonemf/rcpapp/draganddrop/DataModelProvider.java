package test.graphiti.nonemf.rcpapp.draganddrop;

import java.util.ArrayList;
import java.util.List;

public enum DataModelProvider
{
  INSTANCE;
  List<MyModel> list = new ArrayList<MyModel>();

  private DataModelProvider() {
    list.add((new MyModel("Sample1", "Sample1 Description"))
             .addChild(new MyModel("Sample1.1", "Sample1.1 Description")
                 .addChild(new MyModel("Sample1.1.1", "Sample1.1.1 Description"))
                 .addChild(new MyModel("Sample1.1.2", "Sample1.1.2 Description"))
                 )
             .addChild(new MyModel("Sample1.2", "Sample1.2 Description")
                 .addChild(new MyModel("Sample1.3", "Sample1.3 Description"))
                 )

        );


    list.add(new MyModel("Sample2", "Sample2 Description"));
    list.add(new MyModel("Sample3", "Sample3 Description"));
    list.add(new MyModel("Sample4", "Sample4 Description"));
    list.add(new MyModel("Sample5", "Sample5 Description"));
  }

  public List<MyModel> getModel(){
    return list;
  }

}
