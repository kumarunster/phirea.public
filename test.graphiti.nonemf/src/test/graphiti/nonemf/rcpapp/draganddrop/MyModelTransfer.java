package test.graphiti.nonemf.rcpapp.draganddrop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
/**
 * Class for serializing myModels to/from a byte array
 */
public class MyModelTransfer extends ByteArrayTransfer {
   private static MyModelTransfer instance = new MyModelTransfer();
   private static final String TYPE_NAME = "mymodel-transfer-format";
   private static final int TYPEID = registerType(TYPE_NAME);

   /**
    * Returns the singleton myModel transfer instance.
    */
   public static MyModelTransfer getInstance() {
      return instance;
   }
   /**
    * Avoid explicit instantiation
    */
   private MyModelTransfer() {
   }
   protected MyModel[] fromByteArray(byte[] bytes) {
     if(bytes != null)
     {
      DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));

      try {
         /* read number of myModels */
         int n = in.readInt();
         /* read myModels */
         MyModel[] myModels = new MyModel[n];
         for (int i = 0; i < n; i++) {
            MyModel myModel = readMyModel(null, in);
            if (myModel == null) {
               return null;
            }
            myModels[i] = myModel;
         }
         return myModels;
      } catch (IOException e) {
         return null;
      }
     }
     return null;
   }
   /*
    * Method declared on Transfer.
    */
   protected int[] getTypeIds() {
      return new int[] { TYPEID };
   }
   /*
    * Method declared on Transfer.
    */
   protected String[] getTypeNames() {
      return new String[] { TYPE_NAME };
   }
   /*
    * Method declared on Transfer.
    */
   protected void javaToNative(Object object, TransferData transferData) {
     if(object != null && object instanceof MyModel)
     {
      byte[] bytes = toByteArray((MyModel)object);
      if (bytes != null)
         super.javaToNative(bytes, transferData);
     }
   }
   /*
    * Method declared on Transfer.
    */
   protected Object nativeToJava(TransferData transferData) {
      byte[] bytes = (byte[])super.nativeToJava(transferData);
      return fromByteArray(bytes);
   }
   /**
    * Reads and returns a single myModel from the given stream.
    */
   private MyModel readMyModel(MyModel parent, DataInputStream dataIn) throws IOException {
      /**
       * MyModel serialization format is as follows:
       * (String) name of myModel
       * (int) number of child myModels
       * (MyModel) child 1
       * ... repeat for each child
       */
      String name = dataIn.readUTF();
      String description = dataIn.readUTF();
      int n = dataIn.readInt();
      MyModel newParent = new MyModel(name, description);
      for (int i = 0; i < n; i++) {
         readMyModel(newParent, dataIn);
      }
      return newParent;
   }
   protected byte[] toByteArray(MyModel myModel) {
      /**
       * Transfer data is an array of myModels.  Serialized version is:
       * (int) number of myModels
       * (MyModel) myModel 1
       * (MyModel) myModel 2
       * ... repeat for each subsequent myModel
       * see writeMyModel for the (MyModel) format.
       */
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(byteOut);

      byte[] bytes = null;

      try {
         /* write number of markers */
         out.writeInt(myModel.getChildren().size());

         /* write markers */
         for (MyModel child : myModel.getChildren()) {
            writeMyModel(child, out);
         }
         out.close();
         bytes = byteOut.toByteArray();
      } catch (IOException e) {
         //when in doubt send nothing
      }
      return bytes;
   }
   /**
    * Writes the given myModel to the stream.
    */
   private void writeMyModel(MyModel myModel, DataOutputStream dataOut) throws IOException {
      /**
       * MyModel serialization format is as follows:
       * (String) name of myModel
       * (int) number of child myModels
       * (MyModel) child 1
       * ... repeat for each child
       */
      dataOut.writeUTF(myModel.getName());
      dataOut.writeUTF(myModel.getDescription());

      dataOut.writeInt(myModel.getChildren().size());
      for (MyModel child : myModel.getChildren()) {
         writeMyModel(child, dataOut);
      }
   }
}