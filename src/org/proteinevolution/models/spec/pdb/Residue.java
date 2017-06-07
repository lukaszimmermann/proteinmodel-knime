package org.proteinevolution.models.spec.pdb;


import java.util.Arrays;
import javax.swing.Icon;
import org.knime.core.node.util.StringIconOption;

/**
 * Contains specifications for the residues in a PDB structure.
 * 
 * @author lzimmermann
 *
 */
public enum Residue implements StringIconOption {
	
	// Amino acids
	ALA('A', PDBAtom.CB),
	ARG('R', PDBAtom.CB, PDBAtom.CG, PDBAtom.CD, PDBAtom.NE, PDBAtom.CZ, PDBAtom.NH1, PDBAtom.NH2),
	ASN('N', PDBAtom.CB, PDBAtom.CG, PDBAtom.OD1, PDBAtom.ND2),
	ASP('D', PDBAtom.CB, PDBAtom.CG, PDBAtom.OD1, PDBAtom.OD2),
	CYS('C', PDBAtom.CB, PDBAtom.SG),
	GLN('Q', PDBAtom.CB, PDBAtom.CG, PDBAtom.CD, PDBAtom.OE1, PDBAtom.NE2),
	GLU('E', PDBAtom.CB, PDBAtom.CG, PDBAtom.CD, PDBAtom.OE1, PDBAtom.OE2),
	GLY('G'),
	HIS('H', PDBAtom.CB, PDBAtom.CG,  PDBAtom.ND1, PDBAtom.CD2, PDBAtom.CE1, PDBAtom.NE2),
	ILE('I', PDBAtom.CB, PDBAtom.CG1, PDBAtom.CG2, PDBAtom.CD1),
	LEU('L', PDBAtom.CB, PDBAtom.CG,  PDBAtom.CD1, PDBAtom.CD2),
	LYS('K', PDBAtom.CB, PDBAtom.CG, PDBAtom.CD, PDBAtom.CE, PDBAtom.NZ),
	MET('M', PDBAtom.CB, PDBAtom.CG, PDBAtom.SD, PDBAtom.CE),
	PHE('F', PDBAtom.CB, PDBAtom.CG, PDBAtom.CD1, PDBAtom.CD2, PDBAtom.CE1, PDBAtom.CE2, PDBAtom.CZ),
	PRO('P', PDBAtom.CB, PDBAtom.CG, PDBAtom.CD),
	SER('S', PDBAtom.CB, PDBAtom.OG),
	THR('T', PDBAtom.CB, PDBAtom.OG1, PDBAtom.CG2),
	TRP('W', PDBAtom.CB, PDBAtom.CG, PDBAtom.CD1, PDBAtom.CD2, PDBAtom.NE1, PDBAtom.CE2, PDBAtom.CE3, PDBAtom.CZ2, PDBAtom.CZ3, PDBAtom.CH2),
	TYR('Y', PDBAtom.CB, PDBAtom.CG, PDBAtom.CD1, PDBAtom.CD2, PDBAtom.CE1, PDBAtom.CE2, PDBAtom.CZ, PDBAtom.OH),
	VAL('V', PDBAtom.CB, PDBAtom.CG1, PDBAtom.CG2),

	// Nucleotides
	U('U'),
	G('G'),
	C('C'),
	A('A'),
	
	DT('T'),
	DA('A'),
	DG('G'),
	DC('C'),
	DU('U'),
	
	// Unknown
	UNK('U');
	
	
	public static PDBAtom[] getBackbone() {
		
		return new PDBAtom[] {
			PDBAtom.N,
			PDBAtom.CA,
			PDBAtom.C,
			PDBAtom.O
		};
	}
	
	private final PDBAtom[] sidechain;
	private final Character olc;
		
	private Residue(final Character olc, final PDBAtom... sideChainAtoms) {
		
		this.sidechain = sideChainAtoms;
		this.olc = olc;
	}
	
	public Character getOLC() {
		
		return this.olc;
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
