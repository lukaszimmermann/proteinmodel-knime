package org.proteinevolution.models.structure;

import java.io.Serializable;
import java.util.Objects;

import org.biojava.nbio.structure.AminoAcid;
import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.ResidueNumber;
import org.proteinevolution.models.spec.pdb.PDBAtom;
import org.proteinevolution.models.spec.pdb.Residue;

/**
 * Objects of this class can be used to identify a particular atom in a PDB file
 *
 * @author lzimmermann
 *
 */
public final class AtomIdentification implements Serializable {

	private static final long serialVersionUID = -433456703706419607L;
	
	private final PDBAtom pdbatom;
	private final Residue residue;
	private final ResidueNumber residueNumber;
	
	public AtomIdentification(final Atom atom) {
		
		this.pdbatom = PDBAtom.of(atom.getName());
		AminoAcid aa = (AminoAcid) atom.getGroup();
		this.residue = Residue.aaOf(aa.getAminoType());
		this.residueNumber = aa.getResidueNumber();
	}
		
	public PDBAtom getAtom() {

		return this.pdbatom;
	}

	public Residue getResidue() {

		return this.residue;
	}
	
	public String getChainId() {
		
		return this.residueNumber.getChainId();
	}
	
	public int getResidueSeqNum() {
		
		return this.residueNumber.getSeqNum();
	}
	
	@Override
	public boolean equals(final Object o) {

		if (o == this) {

			return true;
		}

		if ( ! (o instanceof AtomIdentification)) {

			return false;
		}
		AtomIdentification other = (AtomIdentification) o;

		return     other.pdbatom == this.pdbatom
				&& other.residue == this.residue
				&& other.residueNumber.equals(this.residueNumber);
	}

	@Override
	public int hashCode() {

		return Objects.hash(this.pdbatom, this.residue, this.residueNumber);
	}
}
