package org.proteinevolution.models.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	public static void checkURIExtension(final PortObjectSpec inSpec, final String validExtension) throws InvalidSettingsException {
	
		Set<String> validExtensions = new HashSet<String>(1);
		validExtensions.add(validExtension);
		URIUtils.checkURIExtension(inSpec, validExtensions);
	}
	
	public static void checkURIExtension(final PortObjectSpec inSpec, final Set<String> validExtensions) throws InvalidSettingsException {
		
		if ( ! (inSpec instanceof URIPortObjectSpec)) {
			
			throw new InvalidSettingsException("Input port is not an URIPortObject!");
		}
		List<String> extensions = ((URIPortObjectSpec) inSpec).getFileExtensions();
		
		if (extensions.size() != 1) {
			
			throw new InvalidSettingsException("Only one extension allowed for the port object!");
		}
		
		if ( ! validExtensions.contains(extensions.get(0))) {
			
			throw new InvalidSettingsException("Invalid file extension for node: " + extensions.get(0));
		}
	}
}
