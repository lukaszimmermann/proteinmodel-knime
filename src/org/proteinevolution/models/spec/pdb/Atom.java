package org.proteinevolution.models.spec.pdb;

public enum Atom {

	CA(Element.C),
	CB(Element.C),
	CD(Element.C),
	CD1(Element.C),
	CD2(Element.C),
	CE(Element.C),
	CE1(Element.C),
	CE2(Element.C),
	CE3(Element.C),
	CG(Element.C),
	CG1(Element.C),
	CG2(Element.C),
	CH2(Element.C),
	CZ(Element.C),
	CZ2(Element.C),
	CZ3(Element.C),
	N(Element.N),
	ND1(Element.N),
	ND2(Element.N),
	NE(Element.N),
	NE1(Element.N),
	NE2(Element.N),
	NH1(Element.N),
	NH2(Element.N),
	NZ(Element.N),
	C(Element.C),
	O(Element.O),
	OD(Element.O),
	OD1(Element.O),
	OD2(Element.O),
	OE1(Element.O),
	OE2(Element.O),
	OG(Element.O),
	OG1(Element.O),
	OH(Element.O),
	OXT(Element.O),
	SD(Element.S),
	SG(Element.S);
	
	
	public final Element element;
	
	private Atom(final Element element) {
	
		this.element = element;
	}
	public static final String name = "ATOM";

	
	// Fields Coordinates in the lines of as PDB file
	public static final int FIELD_ATOM_SERIAL_NUMBER_START = 6;
	public static final int FIELD_ATOM_SERIAL_NUMBER_END = 11;

	public static final int FIELD_ATOM_NAME_START = 12;
	public static final int FIELD_ATOM_NAME_END = 16;

	public static final int FIELD_ATOM_ALTLOC_START = 16;
	public static final int FIELD_ATOM_ALTLOC_END = 17;

	public static final int FIELD_RESIDUE_NAME_START = 17;
	public static final int FIELD_RESIDUE_NAME_END = 20;

	public static final int FIELD_CHAIN_IDENTIFIER_START = 21;
	public static final int FIELD_CHAIN_IDENTIFIER_END = 22;

	public static final int FIELD_RESIDUE_SEQ_NUMBER_START = 22;
	public static final int FIELD_RESIDUE_SEQ_NUMBER_END = 26;

	public static final int FIELD_CODE_RESIDUE_INSERTION_START = 26;
	public static final int FIELD_CODE_RESIDUE_INSERTION_END = 26;

	public static final int FIELD_X_START = 30;
	public static final int FIELD_X_END = 38;

	public static final int FIELD_Y_START = 38;
	public static final int FIELD_Y_END = 46;

	public static final int FIELD_Z_START = 46;
	public static final int FIELD_Z_END = 54;

	public static final int FIELD_OCCUPANCY_START = 54;
	public static final int FIELD_OCCUPANCY_END = 60;

	public static final int FIELD_TEMPERATURE_FACTOR_START = 60;
	public static final int FIELD_TEMPERATURE_FACTOR_END = 66;

	public static final int FIELD_SEGMENT_IDENTIFIER_START = 72;
	public static final int FIELD_SEGMENT_IDENTIFIER_END = 76;

	public static final int FIELD_ELEMENT_SYMBOL_START = 76;
	public static final int FIELD_ELEMENT_SYMBOL_END = 78;

	public static boolean isRecord(String line) {

		return line.startsWith(Atom.name);
	}
}
