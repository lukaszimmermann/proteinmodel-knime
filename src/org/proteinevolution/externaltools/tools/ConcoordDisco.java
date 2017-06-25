package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.parameters.IntegerBoundedParameter;
import org.proteinevolution.externaltools.parameters.StringSelectionParameter;
import org.proteinevolution.models.interfaces.Writeable;

public class ConcoordDisco extends ExternalToolInvocation<Writeable[], File[]>  {


	// Static Members
	private static final Map<String, String> atomsMarginsParam;
	private static final Map<String, String> bondsParam;
	static {
		atomsMarginsParam = new LinkedHashMap<String, String>(6);
		atomsMarginsParam.put("OPLS-UA (united atoms)", "oplsua");
		atomsMarginsParam.put("OPLS-AA (all atoms)", "oplsaa");
		atomsMarginsParam.put("PROLSQ repel", "repel");
		atomsMarginsParam.put("Yamber2", "yamber2");
		atomsMarginsParam.put("Li et al.", "li");
		atomsMarginsParam.put("OPLS-X", "oplsx");

		bondsParam = new LinkedHashMap<String, String>(2);
		bondsParam.put("Concoord default", ".noeh");
		bondsParam.put("Engh-Huber", "");
	}


	public ConcoordDisco(final Path concoordPath) throws IOException {


		super(concoordPath.resolve(Paths.get("bin", "disco")).toFile());

		this.addEnvKeyValue(new EnvKeyValue() {

			private Path tempLib = null;

			@Override
			public void close() throws Exception {

				if (this.tempLib != null) {

					ExternalToolInvocation.deleteRecursively(this.tempLib);
				}
			}

			@Override
			public String getKey() {

				return "CONCOORDLIB";
			}

			@Override
			public String getValue() throws IOException {

				// Copy lib directory to a temporary location (because some files need to be renamed there)
				this.tempLib = Files.createTempDirectory(String.valueOf(ThreadLocalRandom.current().nextInt()));

				ExternalToolInvocation.copyRecursively(concoordPath.resolve("lib"), tempLib);

				String atomMarginSuffix = atomsMarginsParam.get(ConcoordDisco.this.atoms_margin.get());

				Files.copy(this.tempLib.resolve(String.format("ATOMS_%s.DAT", atomMarginSuffix)),
						this.tempLib.resolve("ATOMS.DAT"), StandardCopyOption.REPLACE_EXISTING);

				Files.copy(this.tempLib.resolve(String.format("MARGINS_%s.DAT",atomMarginSuffix)),
						this.tempLib.resolve("MARGINS.DAT"), StandardCopyOption.REPLACE_EXISTING);

				Files.copy(this.tempLib.resolve(String.format("BONDS.DAT%s", bondsParam.get(ConcoordDisco.this.bonds.get()))),
						this.tempLib.resolve("BONDS.DAT"), StandardCopyOption.REPLACE_EXISTING);

				return this.tempLib.toAbsolutePath().toFile().getCanonicalPath();
			}
		});
	}

	// Parameters
	public final StringSelectionParameter atoms_margin = new StringSelectionParameter("OPLS-UA (united atoms)", atomsMarginsParam, "Van-der-Waals parameters");
	public final StringSelectionParameter bonds = new StringSelectionParameter("Concoord default", bondsParam, "bond/angle parameters");
	public final IntegerBoundedParameter no_structures = new IntegerBoundedParameter(500, 1, 1000, "Nr. of structures to be generated. Default: 500");


	@Override
	protected File[] getResult(final CommandLine cmd, final File standardOut) {

		File[] result = new File[1];
		result[0] = cmd.getFile("-op");
		return result;
	}
	@Override
	protected void setCmd(CommandLine cmd) throws IOException {

		cmd.addFile("-p", this.input[0]);
		cmd.addFile("-d", this.input[1]);
		cmd.addOutputDirectory("-op", "disco");

		//cmd.addOption("-op", new File(outputPDB, "prefix").getAbsolutePath());
		cmd.addOption("-n", this.no_structures.get());
	}
}
