package org.proteinevolution.models.knime.hhsuitedb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataType;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.util.FileUtil;

/**
 * Objects of this class represent lists of hhsuite databases that can be selected.
 * 
 * @author lzimmermann
 *
 */
public final class HHsuiteDBContent implements Serializable {

	private static final long serialVersionUID = -6977340626626226386L;
	public static final DataType TYPE = DataType.getType(HHsuiteDBCell.class);

	// Maps database name to prefix path
	private final Map<String, String> prefixes;
	
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(HHsuiteDBContent.class);

	public HHsuiteDBContent(final InputStream in) throws IOException  {

		// Warning: in is not allowed to be closed here
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileUtil.copy(in, out);
		out.flush();
		out.close();
		ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
		HHsuiteDBContent hhsuitedb = null;
		
		try(ObjectInput ois = new ObjectInputStream(bis)) {

			hhsuitedb = (HHsuiteDBContent) ois.readObject();

			// Rethrow as IO Exception
		} catch (ClassNotFoundException e) {

			throw new IOException("Class: HHSuiteDB could not be found");
		}
	
		this.prefixes = hhsuitedb.prefixes;
	}


	private HHsuiteDBContent(final Map<String, String> prefixes) {

		this.prefixes = prefixes;
	}
	
	
	public String getPrefix(final String name) {
		
		return this.prefixes.get(name);
	}
	
	public String[] getNames() {
		
		Set<String> keySet = this.prefixes.keySet();
		return keySet.toArray(new String[keySet.size()]);
	}

	
	public static HHsuiteDBContent fromDirectory(final String filePath)  {
		
		File input = new File(filePath);
		String identificationString = "_a3m.ffdata";
		
		
		if ( ! input.exists() ||  ! input.isDirectory()) {
		
			throw new IllegalArgumentException("filePath provided to fromDirectory does not denote a directory");
		}
		
	
		File[] files = input.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				
				return name.endsWith(identificationString);  //  Suffix a3m.ffdata currently used to identify databases. 
			}
		});
		
		Map<String, String> result = new HashMap<String, String>();		

		// assemble prefices
		for (File currentFile : files) {
				
			// get absolute Path
			String absolutePath = currentFile.getAbsolutePath();
						
			// Name of Database
			String name = currentFile.getName();
			name = name.substring(0, name.length() - identificationString.length());
			String prefix = absolutePath.substring(0, absolutePath.length() - identificationString.length());
						
			// Accept if all of the above files are present
			if (   (new File(prefix + "_a3m.ffindex")).exists() 
				&& (new File(prefix + "_cs219.ffdata")).exists()
				&& (new File(prefix + "_cs219.ffindex")).exists()
				&& (new File(prefix + "_hhm.ffdata")).exists() 
				&& (new File(prefix + "_hhm.ffindex")).exists()) {
				
				result.put(name, prefix);
			}
			
		}
		return new HHsuiteDBContent(result);	
	}
}
