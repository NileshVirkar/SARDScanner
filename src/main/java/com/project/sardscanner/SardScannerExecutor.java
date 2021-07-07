package com.project.sardscanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.project.sardscanner.db.ReadH2Dao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.project.sardscanner.optionbuilder.SardScannerParams;

public class SardScannerExecutor {
	private final SardScannerParams params;
	private static final char DEFAULT_SEPARATOR = ',';
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String OUTPUT_FILE = "SARD_Scan.csv";
	private static Logger logger = LogManager.getLogger(SardScannerExecutor.class);
	private ReadH2Dao readH2Dao;
	StringBuilder codeIssues;

	public SardScannerExecutor(SardScannerParams params) throws SARDScannerException {
		this.params = params;
		this.readH2Dao = new ReadH2Dao(H2DBConnector.getConnection(
				"jdbc:h2:" + params.getDataDir() + "/corona.db;user=coronadb;password=coronadb;WRITE_DELAY=25000"));
	}

	public void execute() throws SARDScannerException {

		int issuesCount = 0;
		File parentDir = new File(params.getBaseDir());
		Collection<File> files = FileUtils.listFiles(parentDir, null, true);
		StringBuilder sb = new StringBuilder();
		addHeader(sb);

		logger.info("Scanner started");

		for (File file : files) {
			List<String> ciList;
			File parentFile = new File(params.getBaseDir());
			String relativePath = stripPath(file.getAbsolutePath(), parentFile.getParent(), true);
			try {
				codeIssues = new StringBuilder();
				codeIssues.append("[");
				ciList = this.readH2Dao.getCIForFile(relativePath);
				for (String issue : ciList) {

					codeIssues.append(issue);
					codeIssues.append(" | ");

				}
				codeIssues.append("]");
				issuesCount = ciList.size();
				writeCsv(file, sb, codeIssues.toString(), issuesCount);
			} catch (SQLException e) {
				logger.error("Error getting code issues for file " + file, e);

			}

		}
		logger.info("Scanner execution finished");

	}

	private void addHeader(StringBuilder sb) {
		sb.append("testCase");
		sb.append(DEFAULT_SEPARATOR);
		sb.append("File");
		sb.append(DEFAULT_SEPARATOR);
		sb.append("CI");
		sb.append(DEFAULT_SEPARATOR);
		sb.append("embold issue count");
		sb.append(NEW_LINE_SEPARATOR);

	}

	public String stripPath(String absPath, String prefix, boolean unixStyle) {
		if (StringUtils.isEmpty(absPath)) {
			return absPath;
		} else if (StringUtils.isEmpty(prefix)) {
			return FilenameUtils.separatorsToUnix(absPath);
		}

		String path = "";
		String convAbsPath = FilenameUtils.normalize(new File(absPath).toString());
		String prefixPath = FilenameUtils.normalize(new File(prefix).toString());

		if (StringUtils.equals(convAbsPath, prefixPath)) {
			path = convAbsPath;
		} else {
			path = StringUtils.substringAfter(convAbsPath, prefixPath);
			path = StringUtils.removeStart(path, File.separator);
		}

		if (unixStyle)
			path = FilenameUtils.separatorsToUnix(path);

		return path;
	}

	private void writeCsv(File file, StringBuilder sb, String ci, int count) {
		String outputFile = params.getDataDir() + File.separator + OUTPUT_FILE;
		try (PrintWriter writer = new PrintWriter(new File(outputFile))) {

			sb.append(file.getParent());
			sb.append(DEFAULT_SEPARATOR);
			sb.append(file);
			sb.append(DEFAULT_SEPARATOR);
			sb.append(ci);
			sb.append(DEFAULT_SEPARATOR);
			sb.append(count);
			sb.append(NEW_LINE_SEPARATOR);

			writer.write(sb.toString());

		} catch (FileNotFoundException e) {
			logger.error("Error in generation output file " + outputFile, e);

		}

	}
}
