package org.proteinevolution.knime.porttypes.alignment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataType;
import org.knime.core.util.FileUtil;
import org.proteinevolution.models.interfaces.ISequenceAlignmentAnnotated;
import org.proteinevolution.models.interfaces.Writeable;
import org.proteinevolution.models.spec.AlignmentFormat;


/**
 * Objects of this class represent sequence alignments. The class needs to be instantiated with
 * provided factory methods. The class is also used to store a single sequence (which is a special case of an alignment).
 * 
 * For error prevention, objects of this instance are immutable.
 * 
 * @author lzimmermann
 *
 */
public final class SequenceAlignmentContent implements Serializable, Writeable, ISequenceAlignmentAnnotated {

	private static final long serialVersionUID = -4773393149609106987L;
	public static final DataType TYPE = DataType.getType(SequenceAlignmentCell.class);

	private final String[] headers;
	private final char[][] sequences;

	// Annotations for the alignment (such as secondary structure)
	private final List<char[]> annotations;

	// Specification of the alignment format
	private final AlignmentFormat alignmentformat;


	/**
	 * Adds an annotation to this alignment
	 * 
	 * @param annotation
	 */
	public void addAnnotation(final char[] annotation) {

		this.annotations.add(annotation);
	}

	public SequenceAlignmentContent(final InputStream in) throws IOException  {

		// Warning: in is not allowed to be closed here
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileUtil.copy(in, out);
		out.flush();
		out.close();
		ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
		SequenceAlignmentContent sequenceAlignment = null;

		try(ObjectInput ois = new ObjectInputStream(bis)) {

			sequenceAlignment = (SequenceAlignmentContent) ois.readObject();

			// Rethrow as IO Exception
		} catch (ClassNotFoundException e) {

			throw new IOException("Class: SequenceAlignment could not be found");
		}

		this.headers = sequenceAlignment.headers;
		this.sequences = sequenceAlignment.sequences;
		this.alignmentformat = sequenceAlignment.alignmentformat;
		this.annotations = sequenceAlignment.annotations;
	}

	private SequenceAlignmentContent(final String[] headers, final char[][] seqs) {

		this.headers = headers;
		this.sequences = seqs;
		this.annotations = new ArrayList<char[]>();
		this.alignmentformat = headers.length == 1 ? AlignmentFormat.SingleSequence : AlignmentFormat.FASTA;
	}

	@Override
	public int getLength() {

		return this.sequences[0].length;
	}

	public AlignmentFormat getAlignmentFormat() {

		return this.alignmentformat;
	}	

	@Override
	public void write(final Writer out) throws IOException {

		String linesep = System.lineSeparator();

		for (int i = 0; i <  this.headers.length; ++i) {

			out.write(">");
			out.write(this.headers[i]);
			out.write(linesep);
			char[] seq = this.sequences[i];

			// Write the reference sequence in 80 character chunks
			int end = 80;
			while (end <= seq.length) {

				// Write characters end-80 to end exclusively
				for (int j = end-80; j < end; ++j) {

					out.write(seq[j]);
				}
				end += 80;
				out.write(linesep);
			}
			for (int j = end-80; j < seq.length; ++j ) {

				out.write(seq[j]);
			}
			out.write(linesep);
		}
		out.flush();
	}


	public static SequenceAlignmentContent fromFASTA(final String filePath) throws FileNotFoundException, IOException {

		boolean notFirst = false;

		List<String> headers = new ArrayList<String>();
		List<String> sequences = new ArrayList<String>();

		StringBuilder currentSequence = new StringBuilder();

		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {

			String line;
			while ( (line = bufferedReader.readLine()) != null) {

				line = line.trim();

				if (line.isEmpty()) {

					continue;
				}

				if (line.startsWith(">")) {

					// Store previous if present
					if (notFirst) {
						sequences.add(currentSequence.toString());
						// If the size of the currentSequence does not match the first sequence, this is not an alignment
						if (currentSequence.length() != sequences.get(0).length()) {

							throw new NotAnAlignmentException("Sequences in FASTA File do not have equal length!" + currentSequence.length()  + " vs " +  sequences.get(0).length());
						}
						currentSequence.setLength(0);
					}
					headers.add(line.substring(1));
					notFirst = true;

				} else {
					currentSequence.append(line);
				}
			}
		}
		// End of file, add remaining sequence if present
		sequences.add(currentSequence.toString());

		int headersSize = headers.size();

		// Check some properties of the resulting lists
		if (headersSize != sequences.size()) {

			throw new IOException("Error while parsing FASTA file. Number of headers: " + headersSize + ". Number of sequences: " + sequences.size());
		}
		if (headersSize == 0) {

			throw new NotAnAlignmentException("Input file did not contain any sequences!");
		}

		String[] type = new String[0];

		// Write the strings to the char array
		// TODO Simplify
		char[][] content = new char[sequences.size()][sequences.get(0).length()];

		for (int i = 0; i < sequences.size(); i++) {

			char[] symbols = sequences.get(i).toCharArray();
			for (int j = 0; j < symbols.length; ++j) {

				content[i][j] = symbols[j];
			}
		}

		// Check if single sequence contains gaps (not allowed)
		if (headersSize == 1) {			
			for (char c : sequences.get(0).toCharArray()) {

				if (c == '-') {

					throw new NotAnAlignmentException("Single sequence in alignment, but gap encountered");
				}
			}
		}	
		return new SequenceAlignmentContent(headers.toArray(type), content);
	}


	@Override
	public char[] getSequenceAt(final int index)  {

		return this.sequences[index];
	}

	@Override
	public int getNumberSequences() {

		return this.sequences.length;
	}


	@Override
	public char[] getAnnotationAt(final int index) {

		return this.annotations.get(index);
	}

	@Override
	public int getNumAnnotations() {

		return this.annotations.size();
	}
}
