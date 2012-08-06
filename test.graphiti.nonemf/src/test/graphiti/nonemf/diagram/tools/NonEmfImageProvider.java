package test.graphiti.nonemf.diagram.tools;

import org.eclipse.graphiti.ui.platform.AbstractImageProvider;

/**
 * @author Nikolai Raitsev
 *
 */
public class NonEmfImageProvider extends AbstractImageProvider {
	
	public final static String ID = "test.graphiti.nonemf.diagram.tools.NonEmfImageProvider"; 

    // The prefix for all identifiers of this image provider
    protected static final String PREFIX = "test.graphiti.nonemf.diagram.tools.";

    // The image identifier for an EReference.
    public static final String IMG_EREFERENCE= PREFIX + "ereference";
    
    
	@Override
	protected void addAvailableImages() {
		// register the path for each image identifier
        addImageFilePath(IMG_EREFERENCE, "icons/ereference.gif");
	}

}
