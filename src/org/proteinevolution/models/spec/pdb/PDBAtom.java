package org.proteinevolution.models.spec.pdb;


import org.biojava.nbio.structure.Element;

public enum PDBAtom {

	// Carbon
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
	C7(Element.C),
	C2_(Element.C, "C2'"),
	C1_(Element.C, "C1'"),
	C8(Element.C),
	C5(Element.C),
	C6(Element.C),
	C2(Element.C),
	C(Element.C),
	C9(Element.C),
	C4(Element.C),	
	N(Element.N),
	ND1(Element.N),
	ND2(Element.N),
	NE(Element.N),
	NE1(Element.N),
	NE2(Element.N),
	NH1(Element.N),
	NH2(Element.N),
	NZ(Element.N),
	O(Element.O),
	OD(Element.O),
	OD1(Element.O),
	OD2(Element.O),
	OE1(Element.O),
	OE2(Element.O),
	O5(Element.O),
	OG(Element.O),
	OG1(Element.O),
	OH(Element.O),
	OXT(Element.O),
	SD(Element.S),
	SG(Element.S),
	_1H(Element.H, "1H"),
	_2H(Element.H, "2H"),
	_3H(Element.H, "3H"),
	HA(Element.H),
	HB(Element.H),
	_1HG1(Element.H, "1HG1"),
	_2HG1(Element.H, "2HG1"),
	_3HG1(Element.H, "3HG1"),
	_1HG2(Element.H, "1HG2"),
	_2HG2(Element.H),
	_3HG2(Element.H),
	_1HB(Element.H),
	_2HB(Element.H),
	_1HG(Element.H),
	_2HG(Element.H),
	_1HD(Element.H),
	_2HD(Element.H),
	H(Element.H),
	HG(Element.H),
	_1HD1(Element.H),
	_2HD1(Element.H),
	_3HD1(Element.H),
	_1HD2(Element.H),
	_2HD2(Element.H),
	_3HD2(Element.H),
	HD1(Element.H),
	HD2(Element.H),
	HE1(Element.H),
	HE2(Element.H),
	HH(Element.H),
	_1HE(Element.H),
	_2HE(Element.H),
	_3HE(Element.H),
	HE(Element.H),
	_1HH1(Element.H),
	_2HH1(Element.H),
	_1HH2(Element.H),
	_2HH2(Element.H),
	_3HB(Element.H),
	_1HE2(Element.H),
	_2HE2(Element.H),
	_1HA(Element.H),
	_2HA(Element.H),
	_1HE3(Element.H),
	HE3(Element.H),
	HZ2(Element.H),
	HZ3(Element.H),
	HH2(Element.H),
	_1HZ(Element.H),
	_2HZ(Element.H),
	_3HZ(Element.H),
	HG1(Element.H),
	HZ(Element.H),
	H1(Element.H),
	HB2(Element.H),
	HB3(Element.H),
	HG2(Element.H),
	HG3(Element.H),
	HD3(Element.H),
	HH11(Element.H),
	HH12(Element.H),
	HH21(Element.H),
	HH22(Element.H),
	HZ1(Element.H),
	HG12(Element.H),
	HG13(Element.H),
	HG21(Element.H),
	HG22(Element.H),
	HG23(Element.H),
	HD11(Element.H),
	HD12(Element.H),
	HD13(Element.H),
	HD21(Element.H),
	HD22(Element.H),
	HE22(Element.H),
	HB1(Element.H),
	HD23(Element.H),
	HE21(Element.H),
	HA2(Element.H),
	HA3(Element.H),
	HG11(Element.H),
	H2(Element.H),
	H3(Element.H),
	P(Element.P),
	O5_(Element.O),
	C5_(Element.C),
	C4_(Element.C),
	O4_(Element.O),
	C3_(Element.C),
	O3_(Element.O),
	O6(Element.O),
	O2_(Element.O),

	N9(Element.N),
	N7(Element.N),
	N3(Element.N),
	N6(Element.N),
	N1(Element.N),
	N2(Element.N),
	N4(Element.N),

	OP1(Element.O),
	O2(Element.O),
	O4(Element.O),
	OP2(Element.O),
	OP3(Element.O),
	H5_(Element.H),
	H5(Element.H),
	H21(Element.H),
	H22(Element.H),
	H72(Element.H),
	H73(Element.H),
	H6(Element.H),
	H8(Element.H),
	H61(Element.H),
	H62(Element.H),
	H4_(Element.H),
	H3_(Element.H),
	H2_(Element.H),
	H2__(Element.H),
	H1_(Element.H),
	H41(Element.H),
	H42(Element.H),
	HO5_(Element.H),
	HO2_(Element.H),
	HO3_(Element.H),
	H5__(Element.H),
	HN3(Element.H),
	H71(Element.H);
	
	public final Element element;
	public final String repr;
	
	
	public static PDBAtom toAtom(final String value) {
		
		return Character.isDigit(value.charAt(0)) ? PDBAtom.valueOf("_" + value) 
			: (value.charAt(value.length() - 1) == '\'' ? PDBAtom.valueOf(value.replace('\'', '_')) :  PDBAtom.valueOf(value));
	}
	
	
	private PDBAtom(final Element element, final String repr) {
	
		this.element = element;
		this.repr = repr;
	}
	
	private PDBAtom(final Element element) {
		
		this.element = element;
		this.repr = this.name();
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

		return line.startsWith(PDBAtom.name);
	}
}
