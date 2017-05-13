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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataType;
import org.knime.core.util.FileUtil;

/**
 * Objects of this class represent lists of hhsuite databases that can be selected.
 * 
 * @author lzimmermann
 *
 */
public final class HHsuiteDB implements Serializable {

	private static final long serialVersionUID = -6977340626626226386L;
	public static final DataType TYPE = DataType.getType(HHsuiteDBCell.class);

	// Maps database name to prefix path
	private final Map<String, String> prefixes;
	
	

	public HHsuiteDB(final InputStream in) throws IOException  {

		// Warning: in is not allowed to be closed here
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileUtil.copy(in, out);
		out.flush();
		out.close();
		ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
		HHsuiteDB hhsuitedb = null;
		
		try(ObjectInput ois = new ObjectInputStream(bis)) {

			hhsuitedb = (HHsuiteDB) ois.readObject();

			// Rethrow as IO Exception
		} catch (ClassNotFoundException e) {

			throw new IOException("Class: HHSuiteDB could not be found");
		}
		this.prefixes = hhsuitedb.prefixes;
	}


	private HHsuiteDB(final Map<String, String> prefixes) {

		this.prefixes = prefixes;
	}

	
	public Set<String> getNames() {
		
		return new HashSet<String>(this.prefixes.keySet());		
	}

	
	public static HHsuiteDB fromDirectory(final String filePath)  {
		
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
			
			// get absolue Path
			String absolutePath = currentFile.getAbsolutePath();
			
			// Name of Database
			String name = currentFile.getName();
			name = name.substring(name.length() - identificationString.length());
			String prefix = absolutePath.substring(absolutePath.length() - identificationString.length());
						
			File file1 = new File(prefix + "_a3m.ffindex");
			File file2 = new File(prefix + "_cs219.ffdata");
			File file3 = new File(prefix + "_cs219.ffindex");
			File file4 = new File(prefix + "_hhm.ffdata");
			File file5 = new File(prefix + "_hhm.ffindex");
			
			// Accept if all of the above files are present
			if (file1.exists() && file2.exists() && file3.exists() && file4.exists() && file5.exists()) {
				
				result.put(name, prefix);
			}
			
		}
		return new HHsuiteDB(result);	
	}
}
