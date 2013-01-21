package test.graphiti.nonemf.diagram;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;

public class NonEmfDiagramEditorInput extends DiagramEditorInput {
	
	protected String diagramFileName;
	
	protected String dataFileName;

//	public NonEmfDiagramEditorInput(String diagramUriString, TransactionalEditingDomain domain, String providerId) {
//		super(diagramUriString, providerId);
//	}
//	
//	public NonEmfDiagramEditorInput(String diagramUriString,
//			TransactionalEditingDomain domain, String providerId,
//			boolean disposeEditingDomain) {
//		super(diagramUriString, providerId, disposeEditingDomain);
//		// TODO Auto-generated constructor stub
//	}
//
//	public NonEmfDiagramEditorInput(URI diagramUri,
//			TransactionalEditingDomain domain, String providerId,
//			boolean disposeEditingDomain) {
//		super(diagramUri, domain, providerId, disposeEditingDomain);
//		// TODO Auto-generated constructor stub
//	}
//
//	public NonEmfDiagramEditorInput(URI diagramUri,
//			TransactionalEditingDomain domain, String providerId) {
//		super(diagramUri, domain, providerId);
//		// TODO Auto-generated constructor stub
//	}
	

	public NonEmfDiagramEditorInput(URI diagramUri, String providerId) {
		super(diagramUri, providerId);
		// TODO Auto-generated constructor stub
	}


	public String getDiagramFileName() {
		return diagramFileName;
	}

	public void setDiagramFileName(String diagramFileName) {
		this.diagramFileName = diagramFileName;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}


	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		
		if(obj == null)
			return result;
		
		if(! (obj instanceof NonEmfDiagramEditorInput) )
			return result;
		
		NonEmfDiagramEditorInput otherInput = (NonEmfDiagramEditorInput) obj;
		
//		if(this.dataFileName.equals(otherInput.dataFileName) && 
//				this.diagramFileName.equals(otherInput.diagramFileName))
		if(this.diagramFileName.equals(otherInput.diagramFileName))
			result = true;
		else
			result = false;
		
		return result;
	}
	
	
}
