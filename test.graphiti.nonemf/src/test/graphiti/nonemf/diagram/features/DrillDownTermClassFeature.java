package test.graphiti.nonemf.diagram.features;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.internal.ExternalPictogramLink;
import org.eclipse.graphiti.mm.Property;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.AbstractDrillDownFeature;
import org.eclipse.graphiti.ui.internal.services.GraphitiUiInternal;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import test.graphiti.nonemf.diagram.NonEmfDiagramEditor;

import test.graphiti.nonemf.diagram.NonEmfDiagramEditorInput;
import test.graphiti.nonemf.domainmodel.TermClass;
import test.graphiti.nonemf.utils.RepositoryUtils;

public class DrillDownTermClassFeature extends AbstractDrillDownFeature {

	public DrillDownTermClassFeature(IFeatureProvider fp) {
		super(fp);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public String getDescription() {
		return "Opens a Diagram (if a linked diagram exist";
	}



	@Override
	public String getName() {
		return "&Open associated Diagram";
	}
	
	

	
	@Override
	public void execute(ICustomContext context) {
		super.execute(context);
		
		System.out.println("execute DrillDOwn!");
	}


	@Override
	public boolean canExecute(ICustomContext context) {
		boolean result = false;
		PictogramElement[] pes = context.getPictogramElements();
		// first check, if one EClass is selected
		if (pes != null && pes.length == 1) {
			Object bo = getBusinessObjectForPictogramElement(pes[0]);
			if (bo instanceof TermClass) {
				// then forward to super-implementation, which checks if
				// this EClass is associated with other diagrams
				result = super.canExecute(context);
			}
		}
		
		System.out.println("Can Execute DrillDown? " + result);
		return result;
	}


	@Override
	protected Collection<Diagram> getDiagrams() {
		
		List<Diagram> diagrams = RepositoryUtils.getDiagrams();
		
		
		return diagrams;
	}
	
	private class StringTransformer {
		private final static String marker = "__independentN"; //$NON-NLS-1$

		String[] decode(String value) {
			if (!value.startsWith(marker)) {
				return new String[] { value };
			} else {
				value = value.substring(marker.length(), value.length());
				return value.split(marker);
			}
		}

		String encode(String[] segments) {
			if (segments.length == 1) {
				return segments[0];
			}
			StringBuffer sb = new StringBuffer();
			for (String string : segments) {
				sb.append(marker);
				sb.append(string);
			}
			return sb.toString();
		}
	}
	
	StringTransformer st = new StringTransformer();
	
	@Override
	protected Collection<Diagram> getLinkedDiagrams(PictogramElement pe) {
		final Collection<Diagram> ret = new HashSet<Diagram>();

		final Object[] businessObjectsForPictogramElement = getAllBusinessObjectsForPictogramElement(pe);

		final Collection<Diagram> allDiagrams = getDiagrams();
		for (final Diagram d : allDiagrams) {
			final Diagram currentDiagram = getDiagram();
			if (!EcoreUtil.equals(currentDiagram, d)) { // always filter out the
														// current
				// diagram
				
				System.out.println(d);
				
				TermClass termClass = (TermClass) businessObjectsForPictogramElement[0];
				
				EList<Property> properties = d.getProperties();
				for (Property property : properties) {
					if(property.getKey().equals(ExternalPictogramLink.KEY_INDEPENDENT_PROPERTY) && 
							property.getValue().contains(termClass.getId()) ) {
						
						ret.add(d);
						break;
					}
				}
				
			}
		}

		return ret;
	}
	
	@Override
	protected void openDiagramEditor(Diagram diagram) {
		// Found a diagram to open
//		String diagramTypeProviderId = GraphitiUi.getExtensionManager().getDiagramTypeProviderId(diagram.getDiagramTypeId());
//		GraphitiUiInternal.getWorkbenchService().openDiagramEditor(diagram, diagramTypeProviderId,
//				getDiagramEditorId(diagram));
		
		
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		
		try {
			
			final IEditorInput editorInput = RepositoryUtils.getEditorInput(diagram);
			
			
			Display.getCurrent().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					try {
						NonEmfDiagramEditorInput nonEmfDiagramEditorInput = (NonEmfDiagramEditorInput) editorInput;
						System.out.println("open editor with diagram: " + nonEmfDiagramEditorInput.getDiagramFileName());
						IEditorPart openEditor = page.openEditor(editorInput, NonEmfDiagramEditor.DIAGRAM_EDITOR_ID, true);
						openEditor.setFocus();

					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
	        
	    } catch (Exception e) {
	        // Handle the exception here
	    	e.printStackTrace();
	    }

	}

}
