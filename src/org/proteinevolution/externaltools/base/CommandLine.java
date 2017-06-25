package org.proteinevolution.externaltools.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.proteinevolution.models.interfaces.Writeable;

/**
 * Helper method to construct a command line call. Some error checking for the option and argument
 * strings is also provided.
 * 
 * @author lzimmermann
 *
 */
public final class CommandLine implements AutoCloseable {

	// The executable
	private final File executable;

	// Option/value pairs
	private final List<String> optionsKeys;
	private final List<String> optionsValues;

	// Temporary files used for IO
	private final Map<String, File> files;

	private static final Pattern optionPattern = Pattern.compile("((-){1,2}[0-9a-zA-Z_]+)?");
	private static final Pattern valuePattern = Pattern.compile(String.format("[0-9a-zA-Z_\\.%s-]+", File.separator));

	/**
	 * Creates a new CommandLine invocation with the provided executable.
	 * 
	 * @param executable Which file denotes the executable for the command-line call.
	 * The file must exist and be executable.
	 * 
	 * @throws IllegalArgumentException If the file denoted by <code>executable</code> does either not exist or is not executable.
	 */
	public CommandLine(final File executable) throws IOException {

		if (executable == null) {

			throw new IllegalArgumentException("Provided executable is null!");
		}

		this.executable = executable.getCanonicalFile();

		if ( ! this.executable.exists()) {

			throw new IllegalArgumentException("Provided executable does not exist.");
		}
		if ( ! this.executable.canExecute()) {

			throw new IllegalArgumentException("Provided executable is not executable.");
		}

		this.optionsKeys = new ArrayList<String>();
		this.optionsValues = new ArrayList<String>();
		this.files = new HashMap<String, File>();
	}

	public void addFlag(final String flag) {

		this.optionsKeys.add(this.checkOption(flag));
		this.optionsValues.add(null);
	}

	public void addFlag(final String flag, final boolean isSet) {

		if (isSet) {

			this.addFlag(flag);
		}
	}

	/**
	 * Adds a new option to the CommandLine invocation.
	 * 
	 * @param option
	 * @param value
	 * @return
	 * @throws IllegalArgumentException
	 */
	public void addOption(String option, String value) throws IllegalArgumentException {

		option = this.checkOption(option);
		value = value.trim();

		if ( ! valuePattern.matcher(value).matches()) {

			throw new IllegalArgumentException("Provided value for option is not a valid value string");
		}

		this.optionsKeys.add(option);
		this.optionsValues.add(value);
	}

	/**
	 * Returns the file object associated with this key. <em>Note: </em> The file
	 * will be deleted once this instance of CommandLine is closed 
	 * 
	 * @param key The key which is associated with the returned file
	 * @return
	 */
	public File getFile(final String key) {

		return this.files.get(key);
	}

	/**
	 * Adds a new option to the CommandLine invocation, where the argument will be written
	 * to a temporary file. The temporary files are deleted once this CommandLine instance
	 * is <code>close()</code>ed.
	 * 
	 * @param option
	 * @param writeable
	 * @return
	 * @throws IOException
	 */
	public void addFile(String option, final Writeable writeable) throws IOException  {

		option = this.checkOption(option);

		// Make a new temporary file and ask writeable to write into it
		File tempFile = File.createTempFile("commandLine", "");
		tempFile.deleteOnExit();
		FileWriter fw = new FileWriter(tempFile);
		writeable.write(fw);
		fw.close(); 
		this.files.put(option, tempFile);
	}
	
	
	
	public void addFile(String option, final File file) throws IOException  {

		option = this.checkOption(option);

		// Check whether input file exists and is regular file
		if ( file == null || ! file.exists() || ! file.isFile()) {
			
			throw new IllegalArgumentException("Input file is null or does not refer to an existing file!");
		}
	
		// Make a new temporary file and ask writeable to write into it
		File tempFile = File.createTempFile("commandLine", "");
		tempFile.deleteOnExit();
		
		Files.copy(file.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		this.files.put(option, tempFile);
	}
	
	

	/**
	 * Adds a new option to the CommandLine invocation.
	 * 
	 * @param option
	 * @param value
	 * @return
	 * @throws IllegalArgumentException
	 */
	public void addOption(String option, double value) throws IllegalArgumentException {

		this.addOption(option, String.valueOf(value));
	}

	public void addOption(String option, int value) throws IllegalArgumentException {

		this.addOption(option, String.valueOf(value));
	}


	public void addOutputFile(String option) throws IOException {

		this.addOutputFile(option, "");
	}

	public void addOutputFile(String option, final String ext) throws IOException {

		option = this.checkOption(option);
		File tempFile = File.createTempFile("commandLine", ext);
		tempFile.deleteOnExit();
		this.files.put(option, tempFile);
	}

	public void addOutputDirectory(String option) throws IOException {

		option = this.checkOption(option);
		File tempDir = Files.createTempDirectory("commandLine").toFile();
		tempDir.deleteOnExit();
		this.files.put(option, tempDir);
	}

	public void addOutputDirectory(String option, String filePrefix) throws IOException {

		option = this.checkOption(option);
		File tempDir = Files.createTempDirectory("commandLine").toFile();
		tempDir.deleteOnExit();
		this.files.put(option, new File(tempDir, filePrefix));
	}


	private String checkOption(final String option) throws IllegalArgumentException {

		String result = option.trim();

		if ( ! optionPattern.matcher(result).matches()) {

			throw new IllegalArgumentException("Provided option is not a valid option string.");
		}
		return result;
	}

	@Override
	public void close() throws IOException {

		// Delete all input/output files
		for (File tempFile: this.files.values()) {

			// TODO Also ensure that direcotories are removed
			
			try {
				Files.delete(tempFile.toPath());

			} catch(IOException e) {

			}
		}
	}	

	public List<String> toStringList() {

		List<String> result = new ArrayList<String>(1 + this.optionsKeys.size() + this.files.size());
		result.add(this.executable.getAbsolutePath());

		for (int i = 0; i < this.optionsKeys.size(); ++i) {

			result.add(optionsKeys.get(i));
			String value = optionsValues.get(i);

			// Might be null if flag
			if (value != null) {

				result.add(value);	
			}
		}

		for (String option : this.files.keySet()) {

			result.add(option);
			result.add(this.files.get(option).getAbsolutePath());	
		}
		return result;
	}


	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(this.executable.getAbsolutePath());

		for (int i = 0; i < this.optionsKeys.size(); ++i) {

			sb.append(" ");
			sb.append(optionsKeys.get(i));

			String value = this.optionsValues.get(i);

			// Might be null if flag
			if (value != null) {

				sb.append(" ");
				sb.append(value);
			}
		}

		for (String option : this.files.keySet()) {

			sb.append(" ");
			sb.append(option);
			sb.append(" ");
			sb.append(this.files.get(option).getAbsolutePath());	
		}
		return sb.toString();
	}
}
