package org.proteinevolution.models.spec.metrics;

import javax.swing.Icon;
import org.knime.core.node.util.StringIconOption;

public enum Distance implements StringIconOption {

	EUCLIDEAN, SASD;

	@Override
	public String getText() {
		
		return this.toString();
	}

	@Override
	public Icon getIcon() {
		return null;
	}	
}
