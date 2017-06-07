package org.proteinevolution.models.structure;

import java.io.Serializable;
import java.util.Objects;

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
	private PDBAtom atom;
	private Residue residue;
	private int resi;
	private String chain;


	public AtomIdentification(final PDBAtom atom, final Residue residue, final int resi, final String chain) {

		this.atom = atom;
		this.residue = residue;
		this.resi = resi;
		this.chain = chain;
	}

	public PDBAtom getAtom() {

		return this.atom;
	}

	public Residue getResidue() {

		return this.residue;
	}

	public int getResi() {

		return this.resi;
	}

	public String getChain() {

		return this.chain;
	}

	@Override
	public boolean equals(Object o) {

		if (o == this) {

			return true;
		}

		if (o == null || ! (o instanceof AtomIdentification)) {

			return false;
		}
		AtomIdentification other = (AtomIdentification) o;

		return     other.atom == this.atom
				&& other.residue == this.residue
				&& other.resi == this.resi
				&& other.chain.equals(this.chain);
	}

	@Override
	public int hashCode() {

		return Objects.hash(this.atom, this.residue, this.resi, this.chain);
	}
}
