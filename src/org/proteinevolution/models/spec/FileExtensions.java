package org.proteinevolution.models.spec;


/**
 *
 * This class encompasses static fields for common file extensions
 * 
 * @author Lukas Zimmermann
 *
 */
public final class FileExtensions {

	// Prevent instantiation
	private FileExtensions() {
		
		throw new AssertionError();
	}

	public static final String PDB = "pdb";
	public static final String SS = "ss";
	public static final String SS2 = "ss2";
	public static final String MTX = "mtx";
	public static final String CHK = "chk";
	public static final String HORIZ = "horiz";
}
