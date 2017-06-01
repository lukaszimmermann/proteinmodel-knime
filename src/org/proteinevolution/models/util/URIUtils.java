package org.proteinevolution.models.util;

import java.util.List;

import org.knime.core.data.uri.URIPortObjectSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortObjectSpec;

/**
 * Helper for URI related routines.
 * 
 * @author lzimmermann
 *
 */
public final class URIUtils {
	
	// Prevent instantiation
	private URIUtils() {
		
		throw new AssertionError();
	}
	
	public static void checkURIExtension(final PortObjectSpec inSpec, final String extension) throws InvalidSettingsException {
		
		if ( ! (inSpec instanceof URIPortObjectSpec)) {
			
			throw new InvalidSettingsException("Input port is not an URIPortObject!");
		}
		List<String> extensions = ((URIPortObjectSpec) inSpec).getFileExtensions();
		
		if (extensions.size() != 1) {
			
			throw new InvalidSettingsException("Only one extension allowed for the port object!");
		}
		
		if ( ! extensions.get(0).equals(extension)) {
			
			throw new InvalidSettingsException("Node expects extension " + extension + " but " + extensions.get(0) + " given!");
		}
	}
}
