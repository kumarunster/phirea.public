package test.graphiti.nonemf.utils;

import java.beans.DesignMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.graphiti.mm.pictograms.Diagram;
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
	public final static String DIAGRAM_EXTENSION = ".diagramNonEmf";

	private final static String STANDALONE_DIAGRAM_FILE_PATH = File.separator + "files" + File.separator + "NonEmf.diagramNonEmf";
	private final static String TEMPLATE_DIAGRAM_FILE_PATH = File.separator + "files" + File.separator +  "template.diagramNonEmf.xml";
	private final static String TEMPLATE_DIAGRAM_FILES_BASEPATH = File.separator + "files" + File.separator;
	private final static String STANDALONE_DATA_FILE_PATH = File.separator + "files" + File.separator + "NonEmf.xml";
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
			
//			String dataFilePath = getDataFilePath(editorInput);
			
//			POJOIndependenceSolver loadedPOJOs = loadPOJOs(dataFilePath);
			
			
			dfp.setPojoIndependenceSolver(pojoIndependenceSolverStatic);
			
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
		
		return getEditorInput(STANDALONE_DIAGRAM_FILE_PATH);
	}
	
	
	public static IEditorInput getEditorInput(String diagramFilePath) throws Exception {
		
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
				diagramFileName = basePath + diagramFilePath ;
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
	
	public static Diagram createNewDiagramFromTemplate(String newDiagramFileName)
	{
		Diagram result = null;
		
		File fileDestination = null;
		try {
			URL entry = Activator.getDefault().getBundle().getEntry(TEMPLATE_DIAGRAM_FILE_PATH);
			URL find = FileLocator.resolve(entry);
			
			String filePath = find.getFile();
			
			File fileSource = new File(filePath);
			fileDestination = new File(fileSource.getParent() + File.separatorChar + newDiagramFileName + DIAGRAM_EXTENSION);
			
			FileInputStream sourceIS = new FileInputStream(fileSource);
			FileOutputStream destinationOS = new FileOutputStream(fileDestination);
			
			FileChannel sourceChannel = sourceIS.getChannel();
			FileChannel destinationChannel = destinationOS.getChannel();
			
			destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
			
			sourceIS.close();
			destinationOS.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(fileDestination != null )
		{
				
			result = getDiagramFromFile(fileDestination, new ResourceSetImpl());
			
			result.setName(newDiagramFileName);
		}
				
		return result;
	}
	
	public static List<Diagram> getDiagrams() {
		
		List<Diagram> result = new ArrayList<Diagram>();
		try {
			URL entry = Activator.getDefault().getBundle().getEntry(TEMPLATE_DIAGRAM_FILE_PATH);
			URL find = FileLocator.resolve(entry);
			
			String filePath = find.getFile();
			
			File file = new File(filePath);
			
			File[] listFiles = file.getParentFile().listFiles();
			for (File childFile : listFiles) {
				
				if(childFile.getName().endsWith(DIAGRAM_EXTENSION))
				{
					Diagram diagram = getDiagramFromFile(childFile, new ResourceSetImpl());
					if(diagram != null)
						result.add(diagram);
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	

	private static Diagram getDiagramFromFile(final File file, ResourceSet resourceSet) {
		// Demand load the resource for this file.
		
		URI emfURI = URI.createURI(file.toURI().toString());
		
		
		Resource resource;
		try {
			resource = resourceSet.getResource(emfURI, true);
			if (resource != null) {
				// does resource contain a diagram as root object?
				final EList<EObject> contents = resource.getContents();
				for (final EObject object : contents) {
					if (object instanceof Diagram) {
						return (Diagram) object;
					}
				}
			}
		} catch (final WrappedException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static URI getFileURI(IFile file, ResourceSet resourceSet) {
		final String pathName = file.getLocation().toFile().getAbsolutePath();
		URI resourceURI = URI.createFileURI(pathName);
		resourceURI = resourceSet.getURIConverter().normalize(resourceURI);
		
		System.out.println("getFileURI: " + pathName + "; resourceURI: " + resourceURI.toString());
		return resourceURI;
	}


	public static void saveDiagramToFile(Diagram diagram) {
		System.out.println(" ***** ");
		if(diagram == null || diagram.eResource() == null) {
			System.out.println("cannot save, diagram or diagram resource is null");
			return;
		}
		System.out.println(" ***** Diagram to Save URI: " + diagram.eResource().getURI() );
		try {
			URI oldUri = diagram.eResource().getURI();
			
			
			String oldPath = oldUri.path();
			
			String newFileName = diagram.getName() + DIAGRAM_EXTENSION;
			
			System.out.println("Path separator: " + File.separator);
			int lastIndex = oldPath.lastIndexOf(File.separator);
			oldPath = oldPath.substring(0, lastIndex + 1);
			
			String newPath = oldPath + newFileName;
			
			URI newUri = URI.createFileURI(newPath);
			
			
			System.out.println("set new URI: " + newUri.toString());
			diagram.eResource().setURI(newUri);
			
			System.out.println(" ***** Diagram to Save URI: " + diagram.eResource().getURI() );
			diagram.eResource().save(Collections.EMPTY_MAP);
			
//			oldUri.
			System.out.println("old uri path: " + oldUri.path());
			
			URL fileURL = new URL("file:" + oldUri.path());
						
			File file = URIUtil.toFile(fileURL.toURI());
			
//			File file = new File(fileURL.getFile());
			
			if(file.exists())
			{
				boolean deleted = file.delete();
				System.out.println("file ['" + file.getAbsolutePath() + "'] deleted: " + deleted);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}


	public static IEditorInput getEditorInput(Diagram diagram) {
		NonEmfDiagramEditorInput result = null;
		
		try {
			URI diagramUri = diagram.eResource().getURI();
			
			URL diagramFileURL = new URL("file:" + diagramUri.path());
			File file = URIUtil.toFile(diagramFileURL.toURI());
			
			
			String dataFileName = "";
			String diagramFileName = file.getAbsolutePath();
			
			URL entry = Activator.getDefault().getBundle().getEntry("");
			if (entry != null)
			{
				URLConnection connection = entry.openConnection();
				if (connection instanceof BundleURLConnection)
				{
					URL fileURL = ((BundleURLConnection) connection).getFileURL();
					java.net.URI uri = new java.net.URI(fileURL.toString());
					String basePath = new File(uri).getAbsolutePath();
					dataFileName = basePath + STANDALONE_DATA_FILE_PATH ;
				}
			}
			
			if (diagramUri != null) {

				result = new NonEmfDiagramEditorInput(diagramUri, null);
				result.setDataFileName(dataFileName);
				result.setDiagramFileName(diagramFileName);
			}
			
			return result;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
}
