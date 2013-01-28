package test.graphiti.nonemf.diagram.features;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.features.impl.DefaultUpdateDiagramFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;


/**
 * @author Nikolai Raitsev
 *
 */
public class NonEmfDefaultUpdateDiagramFeature extends DefaultUpdateDiagramFeature {

	private boolean hasDoneChanges = false;
	
	public NonEmfDefaultUpdateDiagramFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	
	public IReason updateNeeded(IUpdateContext context) {
		PictogramElement pe = context.getPictogramElement();
		if (pe instanceof Diagram) {
			Diagram d = (Diagram) pe;
			EList<Shape> children = d.getChildren();
			for (Shape shape : children) {
				UpdateContext updateContext = new UpdateContext(shape);
				IUpdateFeature updateFeature = getFeatureProvider().getUpdateFeature(updateContext);
				if (updateFeature != null && updateFeature.updateNeeded(updateContext).toBoolean()) {
					return Reason.createTrueReason();
				}
			}
			EList<Connection> connections = d.getConnections();
			for (Connection connection : connections) {
				UpdateContext updateContext = new UpdateContext(connection);
				IUpdateFeature updateFeature = getFeatureProvider().getUpdateFeature(updateContext);
				if (updateFeature != null && updateFeature.updateNeeded(updateContext).toBoolean()) {
					return Reason.createTrueReason();
				}
			}
		}
		return Reason.createFalseReason();
	}
		
	public boolean update(IUpdateContext context) {
		PictogramElement pe = context.getPictogramElement();
		if (pe instanceof Diagram) {
			Diagram d = (Diagram) pe;
			EList<Shape> children = d.getChildren();
			// Collect PEs for update. Execute update later, because otherwise we could get a 
			// ConcurrentModificationException if PEs get deleted etc.
			Map<IUpdateFeature, IUpdateContext> connToUpdate = new HashMap<IUpdateFeature, IUpdateContext>();
			for (Shape shape : children) {
				UpdateContext updateContext = new UpdateContext(shape);
				IUpdateFeature updateFeature = getFeatureProvider().getUpdateFeature(updateContext);
				if (updateFeature != null && updateFeature.canUpdate(updateContext)
						&& updateFeature.updateNeeded(updateContext).toBoolean()) {
					connToUpdate.put(updateFeature, updateContext);
				}
			}
			
			EList<Connection> connections = d.getConnections();
			for (Connection connection : connections) {
				UpdateContext updateContext = new UpdateContext(connection);
				IUpdateFeature updateFeature = getFeatureProvider().getUpdateFeature(updateContext);
				if (updateFeature != null && updateFeature.canUpdate(updateContext)
						&& updateFeature.updateNeeded(updateContext).toBoolean()) {
					connToUpdate.put(updateFeature, updateContext);
				}
			}

			// Reset hasDoneChanges flag because no changes have happened so far
			hasDoneChanges = false;
			for (IUpdateFeature feature : connToUpdate.keySet()) {
				feature.update(connToUpdate.get(feature));
				if (feature.hasDoneChanges()) {
					// At least one sub feature did some changes
					hasDoneChanges = true;
				}
			}
		}
		return true;
	}
	
	
	@Override
	public boolean hasDoneChanges() {
		return hasDoneChanges;
	}
}
