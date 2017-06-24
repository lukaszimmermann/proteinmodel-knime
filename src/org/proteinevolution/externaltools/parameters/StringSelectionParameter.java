package org.proteinevolution.externaltools.parameters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StringSelectionParameter extends Parameter<String> {

	private final Map<String, String> options;
	
	public StringSelectionParameter(final String def, final Map<String, String> options, final String label) {
		
		super(def, label);
		if (options == null) {
			
			throw new IllegalArgumentException("Options argument of StringSelectionParameter is not allowed to be null!");
		}
		this.options = new LinkedHashMap<String, String>(options);
		
		if ( ! this.options.containsKey(def)) {
			
			throw new IllegalArgumentException("Default value is not an option for this StringSelectionParameter!");
		}
	}
	
	@Override
	public void set(final String value) {
		
		if ( ! this.options.containsKey(value)) {
			
			throw new IllegalArgumentException("Value " + value + " is not among the allowed options!");
		} else {
			
			this.value = value;
		}
	}
	
	public List<String> getOptionKeys() {
		
		// Safe copy
		return new ArrayList<String>(this.options.keySet());
	}
}
