package com.project.sardscanner;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.project.sardscanner.optionbuilder.SardScannerOptions;
import com.project.sardscanner.optionbuilder.SardScannerParams;

public class SardScannerMain {
	private static Logger logger = LogManager.getLogger(SardScannerParams.class);
	List<String> folders = new ArrayList<String>();

	public static void main(String[] args) {
		CommandLineParser parser = new DefaultParser();
		SardScannerOptions options = new SardScannerOptions();
		SardScannerParams params = null;
		try {
			CommandLine line = parser.parse(options.getOptions(), args);
			params = new SardScannerParams(line);
			SardScannerExecutor executor = new SardScannerExecutor(params);
			executor.execute();

		} catch (ParseException e) {
			logger.error("Error parsing build options", e);
		}

	}
}
