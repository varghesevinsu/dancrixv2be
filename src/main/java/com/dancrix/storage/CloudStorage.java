package com.dancrix.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

import org.apache.commons.io.IOUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;

public class CloudStorage {
	
	private static final XLogger LOGGER = XLoggerFactory.getXLogger(CloudStorage.class);
	private static final GCSConnection connection = new GCSConnection();

	public OutputStream download(String bucket, String file) {
		InputStream inputStream = null;
		try {
			inputStream = stream(bucket, file);
			OutputStream output = new ByteArrayOutputStream();
			com.google.api.client.util.IOUtils.copy(inputStream, output);
			return output;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	public void download(String bucket, String file, OutputStream output) {
		InputStream inputStream = null;
		try {
			inputStream = stream(bucket, file);
			com.google.api.client.util.IOUtils.copy(inputStream, output);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	public InputStream stream(String bucket, String file) {
		BlobId blob = BlobId.of(bucket, file);
		Storage storage = connection.get();
		ReadChannel reader = storage.reader(blob);
		InputStream inputStream = Channels.newInputStream(reader);
		return inputStream;
	}
}
