package com.project.sardscanner.optionbuilder;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class SardScannerOptions {
	private final Options options = new Options();

	public SardScannerOptions() {
		buildOptions();
	}

	private void buildOptions() {
		final Option baseDir = new Option(SardScannerOpt.BASE_DIR.getName(), SardScannerOpt.BASE_DIR.getLongName(), true,
				"Base Directory path");
		baseDir.setRequired(true);
		this.options.addOption(baseDir);

		final Option dataDir = new Option(SardScannerOpt.DATA_DIR.getName(), SardScannerOpt.DATA_DIR.getLongName(), true,
				"Data Directory path");
		dataDir.setRequired(true);
		this.options.addOption(dataDir);

	}

	public Options getOptions() {
		return options;
	}
}
