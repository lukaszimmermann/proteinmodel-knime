package org.proteinevolution.knime.util;


/**
 * Classes that can consume lines in a file and do stuff with it
 * @author lzimmermann
 *
 */
public interface LineParser {

	/**
	 * Provides another line to this object
	 * @param line Line to be provided
	 */
	public void parse(String line);
	
	
	/**
	 * Tells the object that no more lines will follows
	 */
	public void finish();
}
