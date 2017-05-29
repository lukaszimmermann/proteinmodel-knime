package org.proteinevolution.knime.porttypes.alignment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.knime.core.data.image.ImageContent;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.knime.core.node.port.image.ImagePortObjectSpec;
import org.proteinevolution.knime.porttypes.alignment.view.JAlignmentView;

public class SequenceAlignmentPortObject extends AbstractPortObject {

	public static final class Serializer extends AbstractPortObjectSerializer<SequenceAlignmentPortObject> {}
	
    /** Convenience accessor for the port type. */
    public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(SequenceAlignmentPortObject.class);
	
    private SequenceAlignmentContent m_content;
    private SequenceAlignmentPortObjectSpec m_spec;
    
    /** Empty framework constructor. <b>Do not use!</b> */
    public SequenceAlignmentPortObject() {
    	    	
        // no op
    }
  
    
    
    /**
     * Create new port object based on the given arguments. The cell class that
     * is generated by the {@link ImageContent#toImageCell()} method must be
     * compatible to all DataValue of the spec's
     * {@link ImagePortObjectSpec#getDataType()} return value.
     *
     * @param content The SequenceAlignment Object
     * @param spec The spec.
     * @throws NullPointerException If either argument is null.
     */
    public SequenceAlignmentPortObject(
    		final SequenceAlignmentContent content,
            final SequenceAlignmentPortObjectSpec spec) {
    	
        if (spec == null || content == null) {
            throw new NullPointerException("Argument must not be null.");
        }
        this.m_content = content;
        this.m_spec = spec;    
    }
    
    
    public SequenceAlignmentContent getAlignment() {
        	
    	return this.m_content;
    }


	@Override
	public String getSummary() {
				
		return "Number of sequences: " + this.m_content.getNumberSequences() + "\nLength: " + this.m_content.getLength();
	}

	@Override
	public PortObjectSpec getSpec() {
		
		return this.m_spec;
	}

	
    /** {@inheritDoc} */
    @Override
    public JComponent[] getViews() {
 	
    	return new JComponent[] {			
    			new JAlignmentView(this.m_content)
    	};
    }

    /** {@inheritDoc} */
    @Override
    protected void save(
    		final PortObjectZipOutputStream out,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
           
        out.putNextEntry(new ZipEntry(m_content.getClass().getName()));  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput objectOut = new ObjectOutputStream(bos);
        objectOut.writeObject(this.m_content);
        objectOut.flush();
        out.write(bos.toByteArray());
        bos.close();
        out.close();
        objectOut.close();
    }

	@Override
	protected void load(
			final PortObjectZipInputStream in,
			final PortObjectSpec spec,
			final ExecutionMonitor exec) throws IOException, CanceledExecutionException {

	    ZipEntry nextEntry = in.getNextEntry();
	    String contentClName = nextEntry.getName();

	    Class<SequenceAlignmentContent> contentCl;
	    try {
	        contentCl = (Class<SequenceAlignmentContent>) Class.forName(contentClName);
	        
	    } catch (ClassNotFoundException ex) {
	        throw new IOException("Error converting to SequenceAlignment Class", ex);
	    }
	    
	    Constructor<SequenceAlignmentContent> cons;
	    try {
	        cons = contentCl.getConstructor(InputStream.class);
	    } catch (Exception ex) {
	        throw new IOException("SequenceAlignment class '" + contentClName + "' "
	                + "is missing a required constructor, see javadoc", ex);
	    }

	    try {
	        this.m_content = cons.newInstance(in);
	    } catch (Exception ex) {
	        throw new IOException("Could not create an instance of '"
	                + contentClName + "'", ex);
	    }
	    in.close();
	    this.m_spec = (SequenceAlignmentPortObjectSpec) spec;
	}
}
