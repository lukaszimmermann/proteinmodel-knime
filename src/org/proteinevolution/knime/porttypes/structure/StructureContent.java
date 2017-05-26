package org.proteinevolution.knime.porttypes.structure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.Writer;

import org.biojava.nbio.structure.StructureImpl;
import org.knime.core.data.DataType;
import org.knime.core.util.FileUtil;
import org.proteinevolution.models.interfaces.Writeable;


/**
 * Objects of this class represent lists of hhsuite databases that can be selected.
 * 
 * @author lzimmermann
 *
 */
public final class StructureContent implements Serializable, Writeable {

	private static final long serialVersionUID = -6977340626626226386L;
	public static final DataType TYPE = DataType.getType(StructureCell.class);

	private final StructureImpl structure;
	
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

	public StructureImpl getStructureImpl() {
		
		return this.structure;
	}
	
	
	public StructureContent(final StructureImpl structure) {

		this.structure = structure;
	}


	@Override
	public void write(final Writer out) throws IOException {
		
		// TODO Only writing PDB format supported
		out.write(this.structure.toPDB());
	}
}
