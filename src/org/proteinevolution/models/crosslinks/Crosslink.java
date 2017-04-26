package org.proteinevolution.models.crosslinks;

public interface Crosslink {

	/**
	 * 
	 * @return The String Abbreviation of the Crosslink
	 */
	public  String getAbbrev();
	
	/**
	 * 
	 * @return The minimal length the crosslinker can link two residues
	 */
	public double getMinLengthEuclidean();
	
	/**
	 * 
	 * @return The maximal length the crosslinker can link two residues *
	 */
	public double getMaxLengthEuclidean();
	
}
