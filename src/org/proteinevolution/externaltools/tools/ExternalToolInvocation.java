package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FileUtils;
import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.base.Sentinel;

public abstract class ExternalToolInvocation<A, B> implements Callable<B>, AutoCloseable {

	protected interface EnvKeyValue extends AutoCloseable {

		public String getKey();
		public String getValue() throws IOException;
	}

	protected static final void deleteRecursively(final Path directory) throws IOException {

		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	protected static final void copyRecursively(final Path source, final Path target) throws IOException {

		Files.walkFileTree(source, new SimpleFileVisitor<Path>(){

			private Path current = source;

			@Override
			public FileVisitResult preVisitDirectory(final Path dir,
					final BasicFileAttributes attrs) throws IOException {
				if (this.current == null) {
					this.current = dir;
				} else {
					Files.createDirectories(target.resolve(this.current.relativize(dir)));
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(final Path file,
					final BasicFileAttributes attrs) throws IOException {
				Files.copy(file, target.resolve(this.current.relativize(file)));
				return FileVisitResult.CONTINUE;
			}
		});
	}

	// The working directory of this ExternalToolInvocation
	private Path workingDirectory = null;	

	// The command line of this external tool invocation
	private final File executable;
	private CommandLine cmd;

	// The programs input
	protected A input = null;

	// The sentinel for the tool execution process
	private Sentinel sentinel;
		
	// File containing the standard out of the Tool
	private File standardOut = null;

	// The key value pairs for the environment
	private final List<EnvKeyValue> envp = new ArrayList<EnvKeyValue>();

	private final Thread shutdownThread = new Thread() {		

		@Override
		public void run() {

			try {
				ExternalToolInvocation.this.close();
			} catch(Exception e) {
				this.interrupt();
			}
		}
	};

	protected abstract B getResult(final CommandLine cmd, final File standardOut);
	protected abstract void setCmd(final CommandLine cmd) throws IOException;

	public ExternalToolInvocation(final File executable) throws IOException {

		Runtime.getRuntime().addShutdownHook(this.shutdownThread);

		// It is the task of CommandLine to perform checks on the provided executable
		this.executable = executable;
		if (this.envp == null) {

			throw new IllegalArgumentException("envp is not allowed to be null!");
		}
		if (this.executable == null) {

			throw new IllegalArgumentException("Executable cannot be null!");
		}

		this.sentinel = new Sentinel() {

			@Override
			public boolean isHappy() {
				return true;
			}
		};
	}


	protected void addEnvKeyValue(final EnvKeyValue envKeyValue) {

		this.envp.add(envKeyValue);
	}


	public void setInput(final A input)  {

		if (input == null) {
			
			throw new IllegalArgumentException("Input cannot be null!");
		} else {

			this.input = input;
		}
	}

	public void setSentinel(final Sentinel sentinel) {

		this.sentinel = sentinel;
	}

	@Override
	public B call() throws Exception {

		if (this.input == null) {
			
			throw new IllegalStateException("Input has not been set for tool. Cannot execute");
		}

		this.cmd = new CommandLine(this.executable);

		this.standardOut = File.createTempFile("commandLine", "");
		this.setCmd(this.cmd);
		this.workingDirectory = Files.createTempDirectory(String.valueOf(ThreadLocalRandom.current().nextInt()));

		// Translate to key_value pairs
		String[] envp = new String[this.envp.size()];
		for (int i = 0; i < envp.length; ++i) {

			EnvKeyValue pair = this.envp.get(i);
			envp[i] = String.format("%s=%s", pair.getKey(), pair.getValue());
		}		
		System.out.println(cmd.toString());
		final Process process = Runtime.getRuntime().exec(cmd.toString(), envp, this.workingDirectory.toFile());
		FileUtils.copyInputStreamToFile(process.getInputStream(), this.standardOut);
		
		while(process.isAlive()) {

			if ( ! this.sentinel.isHappy()) {

				process.destroyForcibly();
			}
		}

		if (process.waitFor() != 0) {

			throw new Exception("Execution of process from ExternalToolInvocation has failed");
		}
		return this.getResult(this.cmd, this.standardOut);
	}



	@Override
	public final void close() throws Exception {

		try {
			for (EnvKeyValue envKeyValue : this.envp) {

				envKeyValue.close();
			}
		} finally {

			if (this.standardOut != null) {
				
				Files.delete(this.standardOut.toPath());
			}
			try {
				if (this.cmd != null) {

					cmd.close();
				}

			} finally {

				try {
					if (this.workingDirectory != null) {

						deleteRecursively(this.workingDirectory);
					}
				} finally {

					Runtime.getRuntime().removeShutdownHook(this.shutdownThread);
				}
			}
		}
	}
}
