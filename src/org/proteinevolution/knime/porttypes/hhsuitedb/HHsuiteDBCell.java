package org.proteinevolution.knime.porttypes.hhsuitedb;

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

public class HHsuiteDBCell extends DataCell {

	private static final long serialVersionUID = 2833266670643350235L;

	/**
	 * Serializer for {@link HHsuiteDBCell}.
	 *
	 * @since 0.1
	 * @noreference This class is not intended to be referenced by clients.
	 */
	public static final class HHsuiteDBCellSerializer implements DataCellSerializer<HHsuiteDBCell> {

		@Override
		public HHsuiteDBCell deserialize(final DataCellDataInput input) throws IOException {

			// Here we assume that the byte length of the hhsuiteDB object has been written to the output stream
			int length = input.readInt();
			byte[] bytes = new byte[length];
			input.readFully(bytes);
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

			HHsuiteDBCell sequenceAlignmentCell = null;
			try(ObjectInput ois = new ObjectInputStream(bis)) {

				sequenceAlignmentCell =  new HHsuiteDBCell((HHsuiteDBContent) ois.readObject());

				// Rethrow as IO Exception
			} catch (ClassNotFoundException e) {

				throw new IOException("Class: HHsuiteDB could not be found");
			}
			return sequenceAlignmentCell;
		}

		@Override
		public void serialize(final HHsuiteDBCell cell, final DataCellDataOutput output) throws IOException {

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

	private final HHsuiteDBContent m_content;



	public HHsuiteDBCell(final HHsuiteDBContent content) {

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
		
		HHsuiteDBCell ic = (HHsuiteDBCell) dc;
		
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
