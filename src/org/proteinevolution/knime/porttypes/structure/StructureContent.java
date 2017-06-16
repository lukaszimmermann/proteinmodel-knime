package org.proteinevolution.knime.porttypes.structure;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.biojava.nbio.structure.StructureImpl;
import org.biojava.nbio.structure.io.PDBFileReader;
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

	private final List<String> pdbString; // The PDB String of the structure
	private StructureImpl structureImpl;

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
		this.structureImpl = null;
		this.pdbString = structureContent.pdbString;
	}

	public StructureImpl getStructureImpl() throws IOException {

		if (this.structureImpl == null) {

			File tempFile = Files.createTempFile("structureContent", ".pdb").toFile();
			tempFile.deleteOnExit();
			
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
				
				for (String line : this.pdbString) {
					
					bw.write(line);
				}
			}
		
			this.structureImpl = (StructureImpl) (new PDBFileReader()).getStructure(tempFile.getAbsoluteFile());
			tempFile.delete();
		}
		return this.structureImpl;
	}


	public StructureContent(final List<String> pdbString) {

		this.pdbString = new ArrayList<String>(pdbString);
		this.structureImpl = null;
	}

	public void setOmitHET(final boolean b) {

		this.omitHET = b;
	}

	public String getPdbString() {
		
		String linesep = System.lineSeparator();
		
		StringBuilder sb = new StringBuilder();
		for (String line : this.pdbString) {
			sb.append(line);
			sb.append(linesep);
		}
		return sb.toString();
	}
	
	@Override
	public void write(final Writer out) throws IOException {


		try(BufferedWriter br = new BufferedWriter(out)) {

			for(String line : this.pdbString) {

				if( ! this.omitHET || ! line.startsWith("HET")) {

					br.write(line);
					br.newLine();
				}
			}
		}
	}
}
