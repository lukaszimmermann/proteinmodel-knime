package org.proteinevolution.models.spec.databases;

import javax.swing.Icon;

import org.knime.core.node.util.StringIconOption;

public enum Uniprot implements StringIconOption {

	RefSeq_Protein("P_REFSEQ_AC"),
	UniProtKB_ID("ID"),
	PDB("PDB_ID");
	

	private final String id;
	
	private Uniprot(final String id) {
		
		this.id = id;
	}
		
	@Override
	public String getText() {

		return this.toString();
	}
	
	public String getID() {
		
		return this.id;
	}
	

	@Override
	public Icon getIcon() {
		
		// No icon here
		return null;
	}
}


/*
UniProtKB AC/ID	ACC+ID	from
UniProtKB AC	ACC	both
UniProtKB ID	ID	both
UniParc	UPARC	both
UniRef50	NF50	both
UniRef90	NF90	both
UniRef100	NF100	both
Gene name	GENENAME	both
Category:Sequence databases
EMBL/GenBank/DDBJ	EMBL_ID	both
EMBL/GenBank/DDBJ CDS	EMBL	both
Entrez Gene (GeneID)	P_ENTREZGENEID	both
GI number	P_GI	both
PIR	PIR	both
RefSeq Nucleotide	REFSEQ_NT_ID	both
RefSeq Protein	P_REFSEQ_AC	both
UniGene	UNIGENE_ID	both
Category:3D structure databases
PDB	PDB_ID	both
DisProt	DISPROT_ID
*/