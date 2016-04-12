package de.hm.edu.carads.controller;

import java.io.InputStream;

import de.hm.edu.carads.models.Image;

public interface ImageController {
	public Image saveImage(InputStream input, String datatype) throws Exception;
	public byte[] getImage(String id) throws Exception;
}
