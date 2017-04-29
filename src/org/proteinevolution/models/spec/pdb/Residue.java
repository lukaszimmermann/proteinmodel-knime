package org.proteinevolution.models.spec.pdb;


import javax.swing.Icon;
import org.knime.core.node.util.StringIconOption;

/**
 * Contains specifications for the residues in a PDB structure
 * @author lzimmermann
 *
 */
public enum Residue implements StringIconOption {

		ALA,
		ARG,
		ASN,
		ASP,
		CYS,
		GLN,
		GLU,
		GLY,
		HIS,
		ILE,
		LEU,
		LYS,
		MET,
		PHE,
		PRO,
		SER,
		THR,
		TRP,
		TYR,
		VAL;
	
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
