package org.proteinevolution.models.structure;

import java.util.Objects;

public final class UnorderedAtomPair {

	private final AtomIdentification atom1;
	private final AtomIdentification atom2;
	
	public UnorderedAtomPair(final AtomIdentification atom1, 
			                final AtomIdentification atom2) {
		this.atom1 = atom1;
		this.atom2 = atom2;
	}
	
	
	public AtomIdentification getFirst() {
		
		return this.atom1;
	}
	
	public AtomIdentification getSecond() {
		
		return this.atom2;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o == this) {
			
			return true;
		}
		if ( ! ( o instanceof UnorderedAtomPair)) {
			
			return false;
		}
		UnorderedAtomPair other = (UnorderedAtomPair) o;
		
		// pair is symmetric
		return   (other.atom1.equals(this.atom1) && other.atom2.equals(this.atom2))
			  || (other.atom1.equals(this.atom2) && other.atom2.equals(this.atom1));
	}
	
	@Override
	public int hashCode() {
			
		return Objects.hash(this.atom1.hashCode() + this.atom2.hashCode());
	}
}
