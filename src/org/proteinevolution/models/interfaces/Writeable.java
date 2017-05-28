package org.proteinevolution.models.interfaces;

import java.io.IOException;
import java.io.Writer;

/** 
 * Classes implementing this interface can write their encompassed content to a writer, which might
 * subsequently used as input to another program.
 * 
 * Points to consider in the future:
 * <ul>
 * <li> Should setting the write mode (e.g. FASTA, CLUSTAL for sequence alignments) also be incorporated?</li>
 * <li> Is the <code>Writer</code> class the correct argument type for the <code>write</code> method?</li>
 * </ul>
 * 
 * @author Lukas Zimmermann
 */
public interface Writeable {

	public void write(final Writer out) throws IOException;
}
