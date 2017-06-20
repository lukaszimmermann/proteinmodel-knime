package org.proteinevolution.knime.porttypes.structure;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
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

import org.apache.commons.io.FileUtils;
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

	private final List<String> pdbStrings; // The PDB Strings of the structures
	private final List<StructureImpl> structureImpls;

	// Whether or not hetero atoms will be written when write is used
	private boolean omitHET = false;


	// Constructors
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
		this.pdbStrings = structureContent.pdbStrings;
		this.structureImpls = new ArrayList<StructureImpl>(this.pdbStrings.size());
		for (int i = 0; i < this.pdbStrings.size(); ++i) {

			this.structureImpls.add(null);
		}
	}
	
	// Copy constructor
	public StructureContent(final StructureContent structureContent) {

		// safe copy the lists
		this.pdbStrings = new ArrayList<String>(structureContent.pdbStrings);
		this.structureImpls = new ArrayList<StructureImpl>(structureContent.structureImpls);
		assert this.pdbStrings.size() == this.structureImpls.size();
	}

	public static StructureContent fromFile(final String path) throws IOException {

		List<String> input = new ArrayList<String>(1);
		input.add(FileUtils.readFileToString(new File(path)));

		return new StructureContent(input);
	}

	public static StructureContent fromDirectory(final String path) throws IOException {


		// List content of selected directory        
		File[] content =  new File(path).listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {

				String path = pathname.getName();
				return path.endsWith("pdb") || path.endsWith("ent");
			}
		});

		List<String> input = new ArrayList<String>(content.length);

		for(File currentFile : content) {

			input.add(FileUtils.readFileToString(currentFile));
		}
		return new StructureContent(input);
	}

	public StructureContent(final List<String> pdbStrings) {

		this.pdbStrings = new ArrayList<String>(pdbStrings);
		this.structureImpls = new ArrayList<StructureImpl>(pdbStrings.size());

		// At least one PDB string must be provided
		if (this.pdbStrings.size() < 1) {

			throw new IllegalArgumentException("At least one PDB Stribg must be provided to the constructor of StructureContent");
		}

		for(int i = 0; i < pdbStrings.size(); ++i) {

			this.structureImpls.add(null);
		}
	}

	public StructureContent(final String pdbString) {

		this.pdbStrings = new ArrayList<String>(1);
		this.pdbStrings.add(pdbString);

		this.structureImpls = new ArrayList<StructureImpl>(1);
		this.structureImpls.add(null);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public StructureImpl getStructureImpl(final int index) throws IOException {

		if (this.structureImpls.get(index) == null) {

			File tempFile = Files.createTempFile("structureContent", ".pdb").toFile();
			tempFile.deleteOnExit();

			try(FileWriter fw = new FileWriter(tempFile)) {

				fw.write(this.pdbStrings.get(index));
			}

			this.structureImpls.set(index, (StructureImpl) (new PDBFileReader()).getStructure(tempFile.getAbsoluteFile()));
			tempFile.delete();
		}
		return this.structureImpls.get(index);
	}

	public StructureImpl[] getAllStructureImpl() throws IOException {

		int nStructures = this.getNumberOfStructures();
		StructureImpl[] result = new StructureImpl[nStructures];

		for (int i = 0; i < nStructures; ++i) {

			result[i] = this.getStructureImpl(i);
		}
		return result;
	}

	public int getNumberOfStructures() {

		return this.pdbStrings.size();
	}


	public StructureContent concatenate(final StructureContent other) {

		List<String> pdbStrings = new ArrayList<String>(this.pdbStrings);
		pdbStrings.addAll(new ArrayList<String>(other.pdbStrings));

		return new StructureContent(pdbStrings);
	}

	public void setOmitHET(final boolean b) {

		this.omitHET = b;
	}

	public String getPdbString(final int index) {

		return this.pdbStrings.get(index);
	}

	public List<String> getAllPdbStrings() {

		// Provide safe copy
		return new ArrayList<String>(this.pdbStrings);
	}


	@Override
	public void write(final Writer out) throws IOException {

		try(BufferedWriter br = new BufferedWriter(out)) {

			for(String line : this.pdbStrings.get(0).split("\\r?\\n")) {

				if( ! this.omitHET || ! line.startsWith("HET")) {

					br.write(line);
					br.newLine();
				}
			}
		}
	}
}
