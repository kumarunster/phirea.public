package test.graphiti.nonemf.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.osgi.framework.internal.core.BundleURLConnection;
import org.eclipse.ui.IEditorInput;

import test.graphiti.nonemf.Activator;
import test.graphiti.nonemf.diagram.DiagramFeatureProvider;
import test.graphiti.nonemf.diagram.NonEmfDiagramEditorInput;
import test.graphiti.nonemf.diagram.POJOIndependenceSolver;
import test.graphiti.nonemf.domainmodel.Predicate;

import com.thoughtworks.xstream.XStream;


/**
 * @author Nikolai Raitsev
 *
 */
public class RepositoryUtils {
	
	private final static String EXTENSION = ".xml";
	private final static String STANDALONE_DIAGRAM_FILE_PATH = "/files/NonEmf.diagramNonEmf";
	private final static String STANDALONE_DATA_FILE_PATH = "/files/NonEmf.xml";
	public static POJOIndependenceSolver pojoIndependenceSolverStatic;
	
	/**
	 * @param editorInput
	 * @param pojoIndependenceSolver
	 */
	public static void persistPOJOObjects(DiagramEditorInput editorInput, 
			POJOIndependenceSolver pojoIndependenceSolver) {
		
		try {
			
			String dataFilePath = getDataFilePath(editorInput);
			
			if(dataFilePath != null) {
				
				File objectsFile = new File(dataFilePath);
				
				FileOutputStream fos = new FileOutputStream(objectsFile);
				XStream xStream = new XStream();
				xStream.omitField(POJOIndependenceSolver.class, "graphicalObjectsMap");
				xStream.toXML(pojoIndependenceSolver, fos);
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * @param editorInput
	 * @param dfp
	 */
	public static void loadPOJOObjects(DiagramEditorInput editorInput, DiagramFeatureProvider dfp) {
		
		try {
			
			String dataFilePath = getDataFilePath(editorInput);
			
			POJOIndependenceSolver loadedPOJOs = loadPOJOs(dataFilePath);
			dfp.setPojoIndependenceSolver(loadedPOJOs);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void loadPOJOs() throws Exception
	{
		URL entry = Activator.getDefault().getBundle().getEntry(STANDALONE_DATA_FILE_PATH);
		URL find = FileLocator.resolve(entry);
		System.out.println(entry.getFile());
		System.out.println(find.getFile());
		loadPOJOs(find.getFile());
	}


	private static POJOIndependenceSolver loadPOJOs(String dataFilePath) throws FileNotFoundException, IOException {
		if(dataFilePath != null) { 
		
			File objectsFile = new File(dataFilePath);
			
			if(objectsFile.exists()) {

				FileInputStream fis = new FileInputStream(objectsFile);
				
				XStream xStream = new XStream();
				POJOIndependenceSolver pojoIndependenceSolver = (POJOIndependenceSolver) xStream.fromXML(fis);
				fis.close();
				
				if(pojoIndependenceSolver.getPredicatesList().isEmpty()) {
					initPredicates(pojoIndependenceSolver);
				}
				else {
					Predicate predicate = pojoIndependenceSolver.getPredicatesMap().get(PredicatesUtil.STANDARD_PREDICATE_ID);
					PredicatesUtil.setStandardPredicate(predicate);
				}
				pojoIndependenceSolverStatic = pojoIndependenceSolver;
				return pojoIndependenceSolver;
			}
		}
		return null;
	}
	
	
	private static void initPredicates(POJOIndependenceSolver pojoIndependenceSolver) {
		
		Predicate standardPredicate = PredicatesUtil.getStandardPredicate();
		
		pojoIndependenceSolver.registerPredicate(standardPredicate);
	}
	
	
	private static String getDataFilePath(DiagramEditorInput editorInput) {
		String result = null;
		String diagramFileString = null;
		
		if(editorInput instanceof NonEmfDiagramEditorInput){
			result = ((NonEmfDiagramEditorInput) editorInput).getDataFileName();
			return result;
		}
		else {
			
			diagramFileString = resolveFilePathFromUri(editorInput.getUriString());
			
			if(diagramFileString != null) {
				
				File file = new File(diagramFileString);
				
				String path = file.getParent();
				String name = editorInput.getName();
				
				result = path + File.separatorChar + name + EXTENSION;
			}
		}
		return result;
	}

	private static String resolveFilePathFromUri(String uriString) {

		//its Eclipse URI!!!
		URI uriEc = URI.createURI(uriString);
		
        IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        
        IPath path = new Path(uriEc.toPlatformString(false));
  
        IFile file = myWorkspaceRoot.getFile(path);

        return file.getLocationURI().getRawPath();
    }

	public static IEditorInput getEditorInput() throws Exception {
		
		String diagramFileName = "";
		String dataFileName = "";
		
		URL entry = Activator.getDefault().getBundle().getEntry("");
		if (entry != null)
		{
			URLConnection connection = entry.openConnection();
			if (connection instanceof BundleURLConnection)
			{
				URL fileURL = ((BundleURLConnection) connection).getFileURL();
				java.net.URI uri = new java.net.URI(fileURL.toString());
				String basePath = new File(uri).getAbsolutePath();
				diagramFileName = basePath + STANDALONE_DIAGRAM_FILE_PATH ;
				dataFileName = basePath + STANDALONE_DATA_FILE_PATH ;
			}
		}
		
		File diagramFile = new File(diagramFileName);
		
		URI emfURI = URI.createURI(diagramFile.toURI().toString());
		
		NonEmfDiagramEditorInput result = null;
		
		if (emfURI != null) {

			//URI diagramUri = GraphitiUiInternal.getEmfService().mapDiagramFileUriToDiagramUri(emfURI);
			//Code from GraphitiUiInternal.getEmfService().mapDiagramFileUriToDiagramUri:
			URI diagramUri = emfURI.appendFragment("/0"); 
//			TransactionalEditingDomain domain = createResourceSetAndEditingDomain();
			
			result = new NonEmfDiagramEditorInput(diagramUri, null);
			result.setDataFileName(dataFileName);
			result.setDiagramFileName(diagramFileName);
		}
		
		return result;
	}
}
