package org.proteinevolution.models.knime.structure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.biojava.nbio.structure.Structure;
import org.knime.core.data.DataType;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;

/**
 * Objects of this class represent lists of hhsuite databases that can be selected.
 * 
 * @author lzimmermann
 *
 */
public final class StructureContent implements Serializable {

	private static final long serialVersionUID = -6977340626626226386L;
	public static final DataType TYPE = DataType.getType(StructureCell.class);

	private final Structure structure;
	
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(StructureContent.class);

	public StructureContent(final InputStream in) throws IOException  {

		// Warning: in is not allowed to be closed here
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileUtil.copy(in, out);
		out.flush();
		out.close();
		ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
		StructureContent structureContent = null;
		
		try(ObjectInput ois = new ObjectInputStream(bis)) {

			structureContent = (StructureContent) ois.readObject();

			// Rethrow as IO Exception
		} catch (ClassNotFoundException e) {

			throw new IOException("Class: StructureContent could not be found");
		}
		this.structure = structureContent.structure;
	}

	private StructureContent(final Structure structure) {

		this.structure = structure;
	}
	
	
	public Structure getStructure() {
		
		return this.structure;
	}
	
	// Retrieves A Structure content from the PDB ID
	public static StructureContent fromPDBID(final String pdbid)  {
		
		// TODO Implement me
		return null;
	}
}
