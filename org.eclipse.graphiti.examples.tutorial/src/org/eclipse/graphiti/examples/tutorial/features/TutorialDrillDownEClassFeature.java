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
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.graphiti.examples.tutorial.TutorialUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.AbstractDrillDownFeature;


public class TutorialDrillDownEClassFeature extends AbstractDrillDownFeature {

	public TutorialDrillDownEClassFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public String getName() {
		return "Open associated diagram"; //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return "Open the diagram associated with this EClass"; //$NON-NLS-1$
	}

	@Override
	public boolean canExecute(ICustomContext context) {
		boolean result = false;
		PictogramElement[] pes = context.getPictogramElements();
		// first check, if one EClass is selected
		if (pes != null && pes.length == 1) {
			Object bo = getBusinessObjectForPictogramElement(pes[0]);
			if (bo instanceof EClass) {
				// then forward to super-implementation, which checks if
				// this EClass is associated with other diagrams
				result = super.canExecute(context);
			}
		}
		
		System.out.println("Can Execute DrillDown? " + result);
		return result;
	}
	
	public boolean canExecuteSuper(ICustomContext context) {
		boolean result = false;
		final PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length == 1) {
			final PictogramElement pe = context.getPictogramElements()[0];
			final Collection<Diagram> possibleDiagramsList = getLinkedDiagrams(pe);

			result = possibleDiagramsList.size() >= 1;
		}
		
		System.out.println("canExecutesUPER, result: " + result);
		return result;
	}
	
	
	protected Collection<Diagram> getLinkedDiagrams(PictogramElement pe) {
		final Collection<Diagram> ret = new HashSet<Diagram>();

		final Object[] businessObjectsForPictogramElement = getAllBusinessObjectsForPictogramElement(pe);

		final Collection<Diagram> allDiagrams = getDiagrams();
		System.out.println("getLinkedDiagrams, allDiagrams " + allDiagrams.size());
		for (final Diagram d : allDiagrams) {
			final Diagram currentDiagram = getDiagram();
			if (!EcoreUtil.equals(currentDiagram, d)) { // always filter out the
														// current
				// diagram
//				final Object[] businessObjectsForDiagram = getAllBusinessObjectsForPictogramElement(d);
//				for (int i = 0; i < businessObjectsForDiagram.length; i++) {
//					final Object diagramBo = businessObjectsForDiagram[i];
//					for (int j = 0; j < businessObjectsForPictogramElement.length; j++) {
//						final Object currentBo = businessObjectsForPictogramElement[j];
//						if (getFeatureProvider().getDiagramTypeProvider().getCurrentToolBehaviorProvider()
//								.equalsBusinessObjects(currentBo, diagramBo)) {
//							
//						}
//					}
//				}
				
				//do not filter!
				ret.add(d);
			}
		}
		
		System.out.println("getLinkedDiagrams, returning " + ret.toString());
		return ret;
	}

	@Override
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
		
		System.out.println("getDiagrams, returning " + resource.toString());
		return result;
	}
	
	 
}
