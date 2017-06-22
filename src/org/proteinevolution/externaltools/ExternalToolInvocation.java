package org.proteinevolution.externaltools;

import java.io.File;
import java.util.concurrent.Callable;

import org.proteinevolution.models.util.CommandLine;

public abstract class ExternalToolInvocation<T> implements Callable<T>, AutoCloseable {


	// The command line interface for the Tool invocation
	protected final CommandLine cmd;
	private final Sentinel sentinel;

	public abstract T getResult();

	public ExternalToolInvocation(final File executable) {

		this.cmd = new CommandLine(executable);
		this.sentinel = new Sentinel() {

			@Override
			public boolean isHappy() {
				return true;
			}
		};
		this.checkExecutable(executable);
	}


	public ExternalToolInvocation(final File executable, final Sentinel sentinel) {

		this.cmd = new CommandLine(executable);
		this.sentinel = sentinel;
		this.checkExecutable(executable);
	}


	@Override
	public T call() throws Exception {

		Process process = Runtime.getRuntime().exec(cmd.toString());

		while(process.isAlive()) {

			if ( ! sentinel.isHappy()) {

				process.destroy();
			}
		}

		if (process.waitFor() != 0) {

			throw new Exception("Execution of process from ExternalToolInvocation has failed");
		}
		return this.getResult();
	}


	private void checkExecutable(final File executable) {

		if ( ! executable.isFile()) {

			throw new IllegalArgumentException("Provided file " + executable.getAbsolutePath() + " is not a file!");
		}

		if ( ! executable.canExecute()) {

			throw new IllegalArgumentException("Provided file " + executable.getAbsolutePath() + " is not executanle!");
		}
	}


	@Override
	public final void close() throws Exception {

		cmd.close();
	}
}
