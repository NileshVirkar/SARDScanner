package com.project.sardscanner.optionbuilder;


public enum SardScannerOpt {
	BASE_DIR("b", "Base Dir"), DATA_DIR("d", "Data Dir");

	private String name;
	private String longName;

	SardScannerOpt(String name, String longName) {
		this.name = name;
		this.longName = longName;
	}

	public String getName() {
		return name;
	}

	public String getLongName() {
		return longName;
	}

}
