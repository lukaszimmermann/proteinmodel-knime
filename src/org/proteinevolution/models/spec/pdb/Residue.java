package org.proteinevolution.models.spec.pdb;


import java.util.Arrays;

import javax.swing.Icon;
import org.knime.core.node.util.StringIconOption;

/**
 * Contains specifications for the residues in a PDB structure
 * @author lzimmermann
 *
 */
public enum Residue implements StringIconOption {
	
	// Amino acids
	ALA(Atom.CB),
	ARG(Atom.CB, Atom.CG, Atom.CD, Atom.NE, Atom.CZ, Atom.NH1, Atom.NH2),
	ASN(Atom.CB, Atom.CG, Atom.OD1, Atom.ND2),
	ASP(Atom.CB, Atom.CG, Atom.OD1, Atom.OD2),
	CYS(Atom.CB, Atom.SG),
	GLN(Atom.CB, Atom.CG, Atom.CD, Atom.OE1, Atom.NE2),
	GLU(Atom.CB, Atom.CG, Atom.CD, Atom.OE1, Atom.OE2),
	GLY,
	HIS(Atom.CB, Atom.CG,  Atom.ND1, Atom.CD2, Atom.CE1, Atom.NE2),
	ILE(Atom.CB, Atom.CG1, Atom.CG2, Atom.CD1),
	LEU(Atom.CB, Atom.CG,  Atom.CD1, Atom.CD2),
	LYS(Atom.CB, Atom.CG, Atom.CD, Atom.CE, Atom.NZ),
	MET(Atom.CB, Atom.CG, Atom.SD, Atom.CE),
	PHE(Atom.CB, Atom.CG, Atom.CD1, Atom.CD2, Atom.CE1, Atom.CE2, Atom.CZ),
	PRO(Atom.CB, Atom.CG, Atom.CD),
	SER(Atom.CB, Atom.OG),
	THR(Atom.CB, Atom.OG1, Atom.CG2),
	TRP(Atom.CB, Atom.CG, Atom.CD1, Atom.CD2, Atom.NE1, Atom.CE2, Atom.CE3, Atom.CZ2, Atom.CZ3, Atom.CH2),
	TYR(Atom.CB, Atom.CG, Atom.CD1, Atom.CD2, Atom.CE1, Atom.CE2, Atom.CZ, Atom.OH),
	VAL(Atom.CB, Atom.CG1, Atom.CG2),

	// Nucleotides
	U,
	G,
	C,
	A,
	
	DT,
	DA,
	DG,
	DC,
	DU,
	
	// Unknown
	UNK;
	
	
	public static Atom[] getBackbone() {
		
		return new Atom[] {
			Atom.N,
			Atom.CA,
			Atom.C,
			Atom.O
		};
	}
	
	
	private final Atom[] sidechain;
	
	private Residue(Atom... sideChainAtoms) {
		
		this.sidechain = sideChainAtoms;
	}
	

	public Atom[] getSidechain() {
		return Arrays.copyOf(this.sidechain, this.sidechain.length);
	}
	

	@Override
	public String getText() {
		return this.toString();
	}
	@Override
	public Icon getIcon() {

		// No icons for the residues
		return null;
	}
}
