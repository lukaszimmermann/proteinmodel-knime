package org.proteinevolution.models.knime.alignment;


/**
 * Class that represents an Exception which should be thrown if the incoming sequence was not an alignment
 * 
 * @author lzimmermann
 *
 */
public final class NotAnAlignmentException extends Exception{

	private static final long serialVersionUID = -4033853055846372918L;

	public NotAnAlignmentException(String message) {
		
		super(message);	
	}
}
