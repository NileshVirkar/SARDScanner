package com.project.sardscanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.project.sardscanner.db.ReadH2Dao;
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
	private ReadH2Dao readH2Dao;

	public SardScannerExecutor(SardScannerParams params) throws SARDScannerException {
		this.params = params;
		String dataDir = ""; //TODO: this should come from SardScannerParams
		this.readH2Dao = new ReadH2Dao(H2DBConnector.getConnection("jdbc:h2:"+dataDir+"/corona.db;user=coronadb;password=coronadb;WRITE_DELAY=25000"));
	}

	public void execute() throws SARDScannerException {

		File parentDir = new File(params.getBaseDir());
		Collection<File> files = FileUtils.listFiles(parentDir, null, true);
		StringBuilder sb = new StringBuilder();
		sb.append("testCase");
		sb.append(DEFAULT_SEPARATOR);
		sb.append("File");
		sb.append(NEW_LINE_SEPARATOR);
		logger.info("Scanner started");

		for (File file : files) {
			//TODO: Changes require
			try {
				List<String> list = this.readH2Dao.getCIForFile(file.getPath());
			} catch (SQLException throwables) {
				throw new SARDScannerException(throwables);
			}

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
