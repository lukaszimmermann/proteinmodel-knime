package org.proteinevolution.models.spec.pdb;

public enum Element {

	C(1.7),
	N(1.55),
	O(1.52),
	S(1.8),
	P(1.8),
	H(1.2);
	
	public final double vdWRadius;
	
	private Element(double vdW) {
		
		this.vdWRadius = vdW;
	}
}
