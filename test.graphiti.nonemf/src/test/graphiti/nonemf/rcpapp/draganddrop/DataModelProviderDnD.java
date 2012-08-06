package test.graphiti.nonemf.rcpapp.draganddrop;

import java.util.ArrayList;
import java.util.List;

public enum DataModelProviderDnD
{
  INSTANCE;
  List<MyModel> list = new ArrayList<MyModel>();

  private DataModelProviderDnD() {
    list.add((new MyModel("Sample1 DnD", "Sample1 Description"))
             .addChild(new MyModel("Sample1.1 DnD", "Sample1.1 Description")
                 .addChild(new MyModel("Sample1.1.1 DnD", "Sample1.1.1 Description"))
                 .addChild(new MyModel("Sample1.1.2 DnD", "Sample1.1.2 Description"))
                 )
             .addChild(new MyModel("Sample1.2 DnD", "Sample1.2 Description")
                 .addChild(new MyModel("Sample1.3 DnD", "Sample1.3 Description"))
                 )

        );


    list.add(new MyModel("Sample2 DnD", "Sample2 Description"));
    list.add(new MyModel("Sample3 DnD", "Sample3 Description"));
    list.add(new MyModel("Sample4 DnD", "Sample4 Description"));
    list.add(new MyModel("Sample5 DnD", "Sample5 Description"));
  }

  public List<MyModel> getModel(){
    return list;
  }

}
