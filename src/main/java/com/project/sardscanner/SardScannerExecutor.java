package com.project.sardscanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.project.sardscanner.optionbuilder.SardScannerParams;

public class SardScannerExecutor {
	private final SardScannerParams params;
	private static final char DEFAULT_SEPARATOR = ',';
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String OUTPUT_FILE = "sardScan.csv";
	private static Logger logger = LogManager.getLogger(SardScannerExecutor.class);

	public SardScannerExecutor(SardScannerParams params) {
		this.params = params;

	}

	public void execute() {

		File parentDir = new File(params.getBaseDir());
		Collection<File> files = FileUtils.listFiles(parentDir, null, true);
		StringBuilder sb = new StringBuilder();
		sb.append("testCase");
		sb.append(DEFAULT_SEPARATOR);
		sb.append("File");
		sb.append(NEW_LINE_SEPARATOR);
		logger.info("Scanner started");

		for (File file : files) {
			writeCsv(file, sb);

		}
		logger.info("Scanner execution finished");

	}

	private void writeCsv(File file, StringBuilder sb) {
		String outputFile = params.getDataDir() + File.separator + OUTPUT_FILE;
		try (PrintWriter writer = new PrintWriter(new File(outputFile))) {

			sb.append(file.getParent());
			sb.append(DEFAULT_SEPARATOR);
			sb.append(file);
			sb.append(NEW_LINE_SEPARATOR);

			writer.write(sb.toString());

		} catch (FileNotFoundException e) {
			logger.error("Error in generation output file " + outputFile, e);

		}

	}
}
