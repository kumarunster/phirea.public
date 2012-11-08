/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SAP AG - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.graphiti.examples.tutorial.features;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.examples.tutorial.TutorialUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.internal.Messages;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.platform.IPlatformImageConstants;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;

public class TutorialAssociateDiagramEClassFeature extends AbstractCustomFeature {

	public TutorialAssociateDiagramEClassFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "&Associate diagram"; //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return "Associate the diagram with this EClass"; //$NON-NLS-1$
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		boolean ret = false;
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length >= 1) {
			ret = true;
			for (PictogramElement pe : pes) {
				Object bo = getBusinessObjectForPictogramElement(pe);
				if (!(bo instanceof EClass)) {
					ret = false;
				}
			}
		}
		return ret;
	}
	
	protected Collection<Diagram> getDiagrams() {
		Collection<Diagram> result = Collections.emptyList();
		Resource resource = getDiagram().eResource();
		URI uri = resource.getURI();
		URI uriTrimmed = uri.trimFragment();
		if (uriTrimmed.isPlatformResource()){
			String platformString = uriTrimmed.toPlatformString(true);
			IResource fileResource = ResourcesPlugin.getWorkspace().getRoot().findMember(platformString);
			if (fileResource != null){
				IProject project = fileResource.getProject();
				result = TutorialUtil.getDiagrams(project);
			}
			
		}
		return result;
	}

	public void execute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		EClass eClasses[] = new EClass[pes.length];
		for (int i = 0; i < eClasses.length; i++) {
			eClasses[i] = (EClass) getBusinessObjectForPictogramElement(pes[i]);
		}

		System.out.println("ass");
		Diagram diagram = null;
		
		final Collection<Diagram> possibleDiagramsList = getDiagrams();
		
		if (!possibleDiagramsList.isEmpty()) {
			final Diagram[] possibleDiagrams = possibleDiagramsList.toArray(new Diagram[0]);
			if (possibleDiagramsList.size() == 1) {
				diagram = possibleDiagrams[0];
			} else {
				ListDialog dialog = new ListDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				dialog.setContentProvider(new IStructuredContentProvider() {
					public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					}
					public void dispose() {
					}
					public Object[] getElements(Object inputElement) {
						return possibleDiagramsList.toArray();
					}
				});
				dialog.setTitle(Messages.AbstractDrillDownFeature_1_xmsg);
				dialog.setMessage(Messages.AbstractDrillDownFeature_2_xmsg);
				dialog.setInitialSelections(new Diagram[] { possibleDiagrams[0] });
				dialog.setLabelProvider(new DiagramLabelProvider());
				dialog.setAddCancelButton(true);
				dialog.setHelpAvailable(false);
				dialog.setInput(new Object());
				dialog.open();
				Object[] result = dialog.getResult();
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
