package org.proteinevolution.knime.porttypes.structure;

import java.io.BufferedWriter;
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
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;
import org.proteinevolution.models.interfaces.Writeable;


/**
 * Objects of this class represent lists of hhsuite databases that can be selected.
 * 
 * @author lzimmermann
 *
 */
public final class StructureContent implements Serializable, Writeable {

	private static final NodeLogger logger = NodeLogger
			.getLogger(StructureContent.class);


	private static final long serialVersionUID = -6977340626626226386L;
	public static final DataType TYPE = DataType.getType(StructureCell.class);

	private final StructureImpl structure;

	// Whether or not hetero atoms will be written when write is used
	private boolean omitHET = false;

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

	public void setOmitHET(final boolean b) {

		this.omitHET = b;
	}


	@Override
	public void write(final Writer out) throws IOException {

		String result = this.structure.toPDB();

		if (this.omitHET) {

			try(BufferedWriter br = new BufferedWriter(out)) {

				for(String line : result.split("\\r?\\n")) {

					if( ! line.startsWith("HET")) {

						br.write(line);
						br.newLine();
					}
				}
			}

		} else {

			out.write(result);
		}
	}
}
