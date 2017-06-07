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
	ALA(PDBAtom.CB),
	ARG(PDBAtom.CB, PDBAtom.CG, PDBAtom.CD, PDBAtom.NE, PDBAtom.CZ, PDBAtom.NH1, PDBAtom.NH2),
	ASN(PDBAtom.CB, PDBAtom.CG, PDBAtom.OD1, PDBAtom.ND2),
	ASP(PDBAtom.CB, PDBAtom.CG, PDBAtom.OD1, PDBAtom.OD2),
	CYS(PDBAtom.CB, PDBAtom.SG),
	GLN(PDBAtom.CB, PDBAtom.CG, PDBAtom.CD, PDBAtom.OE1, PDBAtom.NE2),
	GLU(PDBAtom.CB, PDBAtom.CG, PDBAtom.CD, PDBAtom.OE1, PDBAtom.OE2),
	GLY,
	HIS(PDBAtom.CB, PDBAtom.CG,  PDBAtom.ND1, PDBAtom.CD2, PDBAtom.CE1, PDBAtom.NE2),
	ILE(PDBAtom.CB, PDBAtom.CG1, PDBAtom.CG2, PDBAtom.CD1),
	LEU(PDBAtom.CB, PDBAtom.CG,  PDBAtom.CD1, PDBAtom.CD2),
	LYS(PDBAtom.CB, PDBAtom.CG, PDBAtom.CD, PDBAtom.CE, PDBAtom.NZ),
	MET(PDBAtom.CB, PDBAtom.CG, PDBAtom.SD, PDBAtom.CE),
	PHE(PDBAtom.CB, PDBAtom.CG, PDBAtom.CD1, PDBAtom.CD2, PDBAtom.CE1, PDBAtom.CE2, PDBAtom.CZ),
	PRO(PDBAtom.CB, PDBAtom.CG, PDBAtom.CD),
	SER(PDBAtom.CB, PDBAtom.OG),
	THR(PDBAtom.CB, PDBAtom.OG1, PDBAtom.CG2),
	TRP(PDBAtom.CB, PDBAtom.CG, PDBAtom.CD1, PDBAtom.CD2, PDBAtom.NE1, PDBAtom.CE2, PDBAtom.CE3, PDBAtom.CZ2, PDBAtom.CZ3, PDBAtom.CH2),
	TYR(PDBAtom.CB, PDBAtom.CG, PDBAtom.CD1, PDBAtom.CD2, PDBAtom.CE1, PDBAtom.CE2, PDBAtom.CZ, PDBAtom.OH),
	VAL(PDBAtom.CB, PDBAtom.CG1, PDBAtom.CG2),

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
	
	
	public static PDBAtom[] getBackbone() {
		
		return new PDBAtom[] {
			PDBAtom.N,
			PDBAtom.CA,
			PDBAtom.C,
			PDBAtom.O
		};
	}
	
	
	private final PDBAtom[] sidechain;
	
	private Residue(PDBAtom... sideChainAtoms) {
		
		this.sidechain = sideChainAtoms;
	}
	

	public PDBAtom[] getSidechain() {
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
