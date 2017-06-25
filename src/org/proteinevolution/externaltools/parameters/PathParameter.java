package org.proteinevolution.externaltools.parameters;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class PathParameter extends Parameter<Path> {

	private final Set<String> validExtensions;
	
	public PathParameter(final Path def, final String label, final Set<String> validExtensions) {
		super(def, label);
		this.validExtensions = new HashSet<String>(validExtensions);
	}
	
	public PathParameter(final Path def, final String label, final String validExtensions) {
		super(def, label);
		
		String[] spt = validExtensions.split("|");
		this.validExtensions = new HashSet<String>();
		
		for (String ext : spt) {
			
			this.validExtensions.add(ext);
		}
	}
	
	public Set<String> getValidExtensions() {
		
		return new HashSet<String>(this.validExtensions);
	}
}
