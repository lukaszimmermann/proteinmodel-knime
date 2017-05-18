package org.proteinevolution.models.knime.alignment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataCellDataInput;
import org.knime.core.data.DataCellDataOutput;
import org.knime.core.data.DataCellSerializer;

public class SequenceAlignmentCell extends DataCell {

	private static final long serialVersionUID = -2360592608089151987L;

	/**
	 * Serializer for {@link SequenceAlignmentCell}.
	 *
	 * @since 0.1
	 * @noreference This class is not intended to be referenced by clients.
	 */
	public static final class SequenceAlignmentCellSerializer implements DataCellSerializer<SequenceAlignmentCell> {

		@Override
		public SequenceAlignmentCell deserialize(final DataCellDataInput input) throws IOException {

			// Here we assume that the byte length of the sequenceAlignment object has been written to the output stream
			int length = input.readInt();
			byte[] bytes = new byte[length];
			input.readFully(bytes);
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

			SequenceAlignmentCell sequenceAlignmentCell = null;
			try(ObjectInput ois = new ObjectInputStream(bis)) {

				sequenceAlignmentCell =  new SequenceAlignmentCell((SequenceAlignmentContent) ois.readObject());

				// Rethrow as IO Exception
			} catch (ClassNotFoundException e) {

				throw new IOException("Class: SequenceAlignment could not be found");
			}
			return sequenceAlignmentCell;
		}

		@Override
		public void serialize(final SequenceAlignmentCell cell, final DataCellDataOutput output) throws IOException {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput objectOut = new ObjectOutputStream(bos);
			objectOut.writeObject(cell.m_content);
			objectOut.flush();

			byte[] byteArray = bos.toByteArray();
			bos.close();
			objectOut.close();

			output.writeInt(byteArray.length);
			output.write(byteArray);
		}
	}

	private final SequenceAlignmentContent m_content;



	public SequenceAlignmentCell(final SequenceAlignmentContent content) {

		if (content == null) {

			throw new NullPointerException("Argument must not be null.");
		}
		this.m_content = content;
	}



	@Override
	public String toString() {

		return this.m_content.toString();
	}

	@Override
	protected boolean equalsDataCell(DataCell dc) {
		
		SequenceAlignmentCell ic = (SequenceAlignmentCell) dc;
		
		// TODO Sequence Alignment does not yet implement 'equals;
		return  this.m_content.equals(ic.m_content);
	}

	// TODO Overriding equalContent necessary?

	@Override
	public int hashCode() {
	
		// TODO SequenceAlignment does not yet implement hashCode
		return this.m_content.hashCode();
	}
}
