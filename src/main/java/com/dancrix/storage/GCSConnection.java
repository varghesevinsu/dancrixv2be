package com.dancrix.storage;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.google.appengine.api.utils.SystemProperty;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.StorageOptions.Builder;

public class GCSConnection {

	private static final XLogger LOGGER = XLoggerFactory.getXLogger(GCSConnection.class);

	public GCSConnection() {
	}

	public Storage get() {
		return getStorageInstance();
	}


	/**
	 * Method to initialize Storage
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private Storage initializeStorageService() throws URISyntaxException, IOException {

		Builder newBuilder = StorageOptions.newBuilder();
		newBuilder.setProjectId(SystemProperty.applicationId.get());
		newBuilder.setCredentials(getDefaultCredential());
		StorageOptions storagOptions = newBuilder.build();
		return storagOptions.getService();
	}

	private Credentials getDefaultCredential() {
		try {
			return GoogleCredentials.getApplicationDefault();
		} catch (IOException e) {
			LOGGER.error("error loading credential", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retrieves Storage instance
	 * 
	 * @return
	 */
	private Storage getStorageInstance() {
		try {
			return initializeStorageService();
		} catch (Exception e) {
			LOGGER.error("Error occured - ", e);
			throw new RuntimeException(e);
		}
	}

}
