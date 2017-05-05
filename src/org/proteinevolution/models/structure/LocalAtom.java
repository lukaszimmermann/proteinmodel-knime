package org.proteinevolution.models.structure;

import java.io.Serializable;

public class LocalAtom implements Point3D, Serializable {

	private static final long serialVersionUID = 5474490894177295581L;
	
	private final double x;
	private final double y;
	private final double z;
	private final AtomIdentification atomIdentification;
	
	
	public LocalAtom(final double x, final double y, final double z, final AtomIdentification atomIdentification) {
		
		this.x = x;
		this.y = y;
		this.z = z;
		this.atomIdentification = atomIdentification;
	}
	
	
	@Override
	public double getX() {

		return this.x;
	}
	@Override
	public double getY() {
		
		return this.y;
	}
	@Override
	public double getZ() {

		return this.z;
	}

	
	public AtomIdentification getAtomIdentification() {
		
		return this.atomIdentification;	
	}
}
