package org.proteinevolution.models.knime.alignment;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataType;
import org.knime.core.util.FileUtil;

/**
 * Objects of this class represent sequence alignments. The class needs to be instantiated with
 * provided factory methods. The class is also used to store a single sequence (which is a special case of an alignment).
 * 
 * For error prevention, objects of this instance are immutable.
 * 
 * @author lzimmermann
 *
 */
public final class SequenceAlignment implements Serializable {

	private static final long serialVersionUID = -4773393149609106987L;
	public static final DataType TYPE = DataType.getType(SequenceAlignmentCell.class);

	// The first sequence of an alignment is stored here
	private final String referenceHeader;
	private final String referenceSequence;

	// All other sequences of an alignment, unordered
	private final Map<String, String> sequences;


	public SequenceAlignment(final InputStream in) throws IOException  {

		// Warning: in is not allowed to be closed here
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileUtil.copy(in, out);
		out.flush();
		out.close();
		ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
		SequenceAlignment sequenceAlignment = null;
		
		try(ObjectInput ois = new ObjectInputStream(bis)) {

			sequenceAlignment = (SequenceAlignment) ois.readObject();

			// Rethrow as IO Exception
		} catch (ClassNotFoundException e) {

			throw new IOException("Class: SequenceAlignment could not be found");
		}
		
		this.referenceHeader = sequenceAlignment.referenceHeader;
		this.referenceSequence = sequenceAlignment.referenceSequence;
		this.sequences = sequenceAlignment.sequences;
	}

	
	
	private SequenceAlignment(final List<String> headers, final List<String> seqs) {

		int nSequences = headers.size();
		this.sequences = new HashMap<String, String>(nSequences - 1);
		this.referenceHeader = headers.get(0);
		this.referenceSequence = seqs.get(0);

		for (int i = 1; i < nSequences; ++i) {

			this.sequences.put(headers.get(i), seqs.get(i));
		}		
	}

	public int getNumberOfSequences() {

		return this.sequences.size() + 1;
	}

	public int getLength() {

		return this.referenceSequence.length();
	}


	public void writeFASTA(final Writer out) throws IOException {
		
		String linesep = System.lineSeparator();
		// Write first sequence
		out.write(">");
		out.write(this.referenceHeader);
		out.write(linesep);
		
		// Write the reference sequence in 80 character chunks
		int end = 80;
		while (end <= this.referenceSequence.length()) {
			
			out.write(this.referenceSequence.substring(end-80, end));
			end += 80;
			out.write(linesep);
		}
		// Write remaining chunk
		out.write(this.referenceSequence.substring(end-80));
		
		// Write the remaining sequences
		for ( String header : this.sequences.keySet()) {
			
			out.write(">");
			out.write(this.referenceHeader);
			out.write(linesep);
			
			String seq = this.sequences.get(header);
			
			// Write the reference sequence in 80 character chunks
			end = 80;
			while (end <= seq.length()) {
				
				out.write(seq.substring(end-80, end));
				end += 80;
				out.write(linesep);
			}
		}
	}

	
	public static SequenceAlignment fromFASTA(final String filePath) throws NotAnAlignmentException, FileNotFoundException, IOException {
		
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
		return new SequenceAlignment(headers, sequences);
	}
}
