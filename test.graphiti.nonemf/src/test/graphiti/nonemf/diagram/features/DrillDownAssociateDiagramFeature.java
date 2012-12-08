package test.graphiti.nonemf.diagram.features;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.internal.Messages;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.IPlatformImageConstants;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ListDialog;

import test.graphiti.nonemf.diagram.dialogs.IChangeValueCallBack;
import test.graphiti.nonemf.diagram.dialogs.SelectOrCreateListEntryDialog;
import test.graphiti.nonemf.domainmodel.TermClass;
import test.graphiti.nonemf.utils.RepositoryUtils;

public class DrillDownAssociateDiagramFeature extends AbstractCustomFeature {

	public DrillDownAssociateDiagramFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public String getDescription() {
		return "Associates a new or existing Diagram to given Element";
	}



	@Override
	public String getName() {
		return "&Associate Diagram";
	}
	
	
	

	@Override
	public boolean canExecute(ICustomContext context) {
		boolean canExecute = super.canExecute(context);
		System.out.println("can execute associate diagram? " + canExecute);
		return true;
	}



	@Override
	public void execute(ICustomContext context) {
		// TODO Auto-generated method stub
		System.out.println("Execute Associate Diagram");
		
		
		PictogramElement[] pes = context.getPictogramElements();
		TermClass eClasses[] = new TermClass[pes.length];
		for (int i = 0; i < eClasses.length; i++) {
			
			Object bo = getBusinessObjectForPictogramElement(pes[i]);
			if(bo instanceof TermClass)
				eClasses[i] = (TermClass) bo;
		}

		System.out.println("ass");
		Diagram diagram = null;
		
		final List<Diagram> possibleDiagramsList = RepositoryUtils.getDiagrams();
		
		String currentDiagramName = this.getDiagram().getName();
		
		//filter diagrams, remove current Diagram from possible List
		Diagram toRemove = null;
		for(Diagram d : possibleDiagramsList)
		{
			if(d.getName().equals(currentDiagramName))
				toRemove = d;
		}
		
		
		if (!possibleDiagramsList.isEmpty()) {
			

			if(toRemove != null)
				possibleDiagramsList.remove(toRemove);
			
			final Diagram[] possibleDiagrams = possibleDiagramsList.toArray(new Diagram[0]);
//			if (possibleDiagramsList.size() == 1) {
//				diagram = possibleDiagrams[0];
//			} else 
			
			{
//				ElementListSelectionDialog dialog = new ElementListSelectionDialog(
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), new DiagramLabelProvider());
//				dialog.setElements(possibleDiagramsList.toArray());
//				dialog.setContentProvider(new IStructuredContentProvider() {
//					public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//					}
//					public void dispose() {
//					}
//					public Object[] getElements(Object inputElement) {
//						return possibleDiagramsList.toArray();
//					}
//				});
//				dialog.setTitle(Messages.AbstractDrillDownFeature_1_xmsg);
//				dialog.setMessage(Messages.AbstractDrillDownFeature_2_xmsg);
//				
//				if(possibleDiagrams.length>0)
//					dialog.setInitialSelections(new Diagram[] { possibleDiagrams[0] });
//				
//				dialog.setLabelProvider(new DiagramLabelProvider());
				
				
				
//				dialog.setInput(new Object());
//				dialog.open();
//				Object[] result = dialog.getResult();
				
				IContentProvider contentProvider = new ArrayContentProvider();
				
				ILabelProvider labelProvider = new LabelProvider()
				{
					@Override
					public String getText(Object element) {
						String result = "";
						
						if(element instanceof Diagram) {
							result = ((Diagram) element).getName();
						}
						else
							super.getText(element);
						
						return result;
					}	
				};
				
				
				
				IChangeValueCallBack changeValueCallBack = new IChangeValueCallBack() {
					
					@Override
					public void setValue(Object input, Object element, Object value) {
						
						if(element instanceof Diagram && value instanceof String)
						{
							((Diagram)element).setName(value.toString());
							RepositoryUtils.saveDiagramToFile((Diagram)element);
						}
					}

					@Override
					public Object getValue(Object element) {
						String result = "";
						if(element instanceof Diagram)
						{
							result = ((Diagram)element).getName();
						}
						
						return result;
					}
				};

				
				
				
				final SelectOrCreateListEntryDialog newDialog = new SelectOrCreateListEntryDialog(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				
				newDialog.setDialogLabel("Dialog Label")
						.setDialogTitle("DialogTitle")
						.setContentProvider(contentProvider)
						.setLabelProvider(labelProvider)
						.setChangeValueCallback(changeValueCallBack)
						.setInput(possibleDiagramsList)
						.setCreateNewEntrySelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								String newElement = "newDiagram " + (possibleDiagramsList.size() + 1);
								Diagram newDiagram = RepositoryUtils.createNewDiagramFromTemplate(newElement);
								newDiagram.setName(newElement);
								
								RepositoryUtils.saveDiagramToFile(newDiagram);
								
								possibleDiagramsList.add(newDiagram);
								newDialog.selectAndFocusEntry(newElement);
								
							}}
						);
				
				newDialog.open();
				
				Object[] result = newDialog.getResult();
				
				if (result != null) {
					for (int i = 0; i < result.length; i++) {
						diagram = (Diagram) result[i];
						System.out.println("Result: " + diagram.getName());
					}
				}
			}

			if (diagram != null) {
				// associate selected EClass with diagram
				System.out.println("associate with: " + diagram.getName());
				link(diagram, eClasses);
			}
		}
		
	}
	
	
	protected List<Diagram> getDiagrams() {
		List<Diagram> result = Collections.emptyList();
		Resource resource = getDiagram().eResource();
		URI uri = resource.getURI();
		URI uriTrimmed = uri.trimFragment();
		if (uriTrimmed.isPlatformResource()){
			String platformString = uriTrimmed.toPlatformString(true);
			IResource fileResource = ResourcesPlugin.getWorkspace().getRoot().findMember(platformString);
			if (fileResource != null){
				IProject project = fileResource.getProject();
				result = getDiagrams(project);
			}
			
		}
		return result;
	}
	
	public static List<Diagram> getDiagrams(IProject p) {
		final List<IFile> files = getDiagramFiles(p);
		final List<Diagram> diagramList = new ArrayList<Diagram>();
		final ResourceSet rSet = new ResourceSetImpl();
		for (final IFile file : files) {
			final Diagram diagram = getDiagramFromFile(file, rSet);
			if (diagram != null) {
				diagramList.add(diagram);
			}
		}
		return diagramList;
	}
	

	private static List<IFile> getDiagramFiles(IContainer folder) {
		final List<IFile> ret = new ArrayList<IFile>();
		try {
			final IResource[] members = folder.members();
			for (final IResource resource : members) {
				if (resource instanceof IContainer) {
					ret.addAll(getDiagramFiles((IContainer) resource));
				} else if (resource instanceof IFile) {
					final IFile file = (IFile) resource;
					System.out.println("Location: " + file.getLocation().toString() );
					System.out.println("Location: " + file.getLocation().toFile().getAbsolutePath() );
					if (file.getName().endsWith(".diagram")) { //$NON-NLS-1$
						ret.add(file);
					}
				}
			}
		} catch (final CoreException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private static Diagram getDiagramFromFile(IFile file, ResourceSet resourceSet) {
		// Get the URI of the model file.
		final URI resourceURI = getFileURI(file, resourceSet);

		// Demand load the resource for this file.
		Resource resource;
		try {
			System.out.println("Has absolute path: "+ resourceURI.hasAbsolutePath());
			System.out.println(file.getLocation().toFile().getAbsolutePath());
			System.out.println("File URI: " + resourceURI.toString());
			resource = resourceSet.getResource(resourceURI, true);
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
	
	
	
	/**
	 * The Class DiagramLabelProvider.
	 */
	private class DiagramLabelProvider extends LabelProvider {

		Image image;

		/**
		 * Instantiates a new diagram label provider.
		 */
		public DiagramLabelProvider() {
			super();
		}

		@Override
		public String getText(Object o) {
			String ret = null;
			if (o instanceof Diagram) {
				Diagram diagram = (Diagram) o;
				ret = diagram.getName() + " (" + diagram.getDiagramTypeId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			return ret;
		}

		@Override
		public Image getImage(Object element) {
			if (this.image == null) {
				this.image = GraphitiUi.getImageService().getImageForId(IPlatformImageConstants.IMG_DIAGRAM);
			}
			return this.image;
		}

	}


}
