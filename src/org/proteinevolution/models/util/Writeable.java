package org.proteinevolution.models.util;

import java.io.IOException;
import java.io.Writer;

/** 
 * Classes implementing this interface represent formats which can be written in plain text to a file
 * 
 * @author lzimmermann
 *
 */
public interface Writeable {

	public void write(final Writer out) throws IOException;
}
