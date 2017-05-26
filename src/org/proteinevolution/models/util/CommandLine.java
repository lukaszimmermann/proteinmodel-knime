package org.proteinevolution.models.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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
	private List<String> optionsKeys;
	private List<String> optionsValues;

	// Temporary files used for IO
	private final Map<String, File> files;
	
	private static final Pattern optionPattern = Pattern.compile("(-){1,2}[0-9a-zA-Z]+");
	private static final Pattern valuePattern = Pattern.compile(String.format("[0-9a-zA-Z_\\.%s-]+", File.separator));


	/**
	 * Creates a new CommandLine invocation with the provided executable.
	 * 
	 * @param executable Which file denotes the executable for the command-line call.
	 * The file must exist and be executable.
	 * 
	 * @throws IllegalArgumentException If the file denoted by <code>executable</code> does either not exist or is not executable.
	 */
	public CommandLine(final File executable) throws IllegalArgumentException {

		if ( ! executable.exists()) {

			throw new IllegalArgumentException("Provided executable does not exist.");
		}
		if ( ! executable.canExecute()) {

			throw new IllegalArgumentException("Provided executable is not executable.");
		}

		this.executable = executable;
		this.optionsKeys = new ArrayList<String>();
		this.optionsValues = new ArrayList<String>();
		
		this.files = new HashMap<String, File>();
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

	public String getAbsoluteFilePath(final String key) {
		
		return this.files.get(key).getAbsolutePath();
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
	public void addInput(String option, Writeable writeable) throws IOException  {

		option = this.checkOption(option);
		
		// Make a new temporary file and ask writeable to write into it
		File tempFile = File.createTempFile("commandLine", "");
		tempFile.deleteOnExit();
		FileWriter fw = new FileWriter(tempFile);
		writeable.write(fw);
		fw.close(); 
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


	public void addOutput(String option) throws IOException {

		option = this.checkOption(option);
		// Make a new temporary file and ask writeable to write into it
		File tempFile = File.createTempFile("commandLine", "");
		tempFile.deleteOnExit();
		
		this.files.put(option, tempFile);
	
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

			Files.delete(tempFile.toPath());
		}
	}	

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(this.executable.getAbsolutePath());

		for (int i = 0; i < this.optionsKeys.size(); ++i) {
			
			sb.append(" ");
			sb.append(optionsKeys.get(i));
			sb.append(" ");
			sb.append(optionsValues.get(i));	
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
