package com.project.sardscanner.optionbuilder;

import org.apache.commons.cli.CommandLine;

public class SardScannerParams {
	private String baseDir;
	private String dataDir;

	public SardScannerParams(CommandLine line) {
		this.baseDir = line.getOptionValue(SardScannerOpt.BASE_DIR.getName());
		this.dataDir = line.getOptionValue(SardScannerOpt.DATA_DIR.getName());

	}

	public String getBaseDir() {
		return baseDir;
	}

	public String getDataDir() {
		return dataDir;
	}

}
