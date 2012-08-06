package test.graphiti.nonemf.rcpapp.draganddrop;

import java.util.ArrayList;
import java.util.List;

public class MyModel
{
  String name;

  String description;

  List<MyModel> children;

  public MyModel()
  {
  }

  public MyModel(String name, String description)
  {
    super();
    this.name = name;
    this.description = description;
  }

  public String getName()
  {
    return name;
  }

  public MyModel setName(String name)
  {
    this.name = name;
    return this;
  }

  public String getDescription()
  {
    return description;
  }

  public MyModel setDescription(String description)
  {
    this.description = description;
    return this;
  }

  public List<MyModel> getChildren()
  {
    if(children == null)
      children = new ArrayList<MyModel>();

    return children;
  }

  public void setChildren(List<MyModel> children)
  {
    this.children = children;
  }

  public MyModel addChild(MyModel child)
  {
    this.getChildren().add(child);
    return this;
  }

}
