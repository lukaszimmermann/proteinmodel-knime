package org.proteinevolution.models.knime.alignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Objects of this class represent sequence alignments. The class needs to be instantiated with
 * provided factory methods. The class is also used to store a single sequence (which is a special case of an alignment).
 * 
 * For error prevention, objects of this instance are immutable
 * 
 * @author lzimmermann
 *
 */
public final class SequenceAlignment {

	// Prevent regular instantiation
	private SequenceAlignment() {}			
	
	private static final String referenceHeader;
	private static final String referenceSequence;
	
	
	
	public static SequenceAlignment fromFASTA(final String filePath) throws NotAnAlignmentException, FileNotFoundException {
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
		
		
		
	}
}
