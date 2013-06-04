package adapters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;


public final class GsonEnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
  private final Map<String, T> nameToConstant = new HashMap<String, T>();
  private final Map<T, String> constantToName = new HashMap<T, String>();

  public GsonEnumTypeAdapter(Class<T> classOfT) {
    try {
      for (T constant : classOfT.getEnumConstants()) {
        String name = constant.name();
        SerializedName annotation = classOfT.getField(name).getAnnotation(SerializedName.class);
        if (annotation != null) {
          name = annotation.value();
        }
        nameToConstant.put(name, constant);
        constantToName.put(constant, name);
      }
    } catch (NoSuchFieldException e) {
      throw new AssertionError();
    }
  }
  public T read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    if(in.peek() == JsonToken.BEGIN_OBJECT) {
      String enumName = null;
      in.beginObject();
      while (in.hasNext()) {
        if(in.nextName().equals("_name")) {
          enumName = in.nextString();
        }
        else
        {
          in.skipValue();
        }
      }
      in.endObject();
      
      return nameToConstant.get(enumName);
    }

     
    return nameToConstant.get(in.nextString());
  }

  public void write(JsonWriter out, T value) throws IOException {
    out.value(value == null ? null : constantToName.get(value));
  }
}