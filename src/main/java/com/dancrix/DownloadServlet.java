package com.dancrix;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.dancrix.storage.CloudStorage;

public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1863060592120981575L;
	private static final XLogger LOGGER = XLoggerFactory.getXLogger(CloudStorage.class);
	private static Properties properties = new Properties();
	static {
		try {
			properties.load(DownloadServlet.class.getResourceAsStream("/configuration.properties"));
		} catch (Exception e) {
			LOGGER.error("error loading properties", e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String filename = properties.getProperty("filename", "dancrix.zip");
		String bucket = properties.getProperty("bucket", "dancrix.appspot.com");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		CloudStorage storage = new CloudStorage();
		ServletOutputStream outputStream = response.getOutputStream();
		storage.download(bucket, filename, outputStream);
		outputStream.close();
	}

}