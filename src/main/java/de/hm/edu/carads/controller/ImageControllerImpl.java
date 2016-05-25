package de.hm.edu.carads.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.glassfish.jersey.uri.PathPattern;

import de.hm.edu.carads.db.PropertiesLoader;
import de.hm.edu.carads.models.Image;

/**
 * Diese Klasse behandelt das Speichern und Lesen von Bildern auf dem Server.
 * @author BK
 *
 */
public class ImageControllerImpl implements ImageController {

	/**
	 * Der Pfad fuer alle Bilder die auf den Server geladen werden koennen.
	 * Der Ordner wird zusammengestellt aus dem Homeverzeichnis des Serverbenutzers
	 * und des Pfades, welcher in der Konfiguration angegeben ist.
	 */
	private static final String UPLOAD_PATH = System.getProperty("user.home") + PropertiesLoader.getInstance().getPropertyString("IMG_DIR");
	
	/**
	 * Alle erlaubten Bild-Datentypen.
	 */
	public static final String[] VALID_DATATYPES = new String[] { "JPG", "JPEG", "PNG" };

	@Override
	public Image saveImage(InputStream input, String datatype) throws Exception {

		Image imageData = new Image();

		int read = 0;
		byte[] bytes = new byte[1024];

		if (!isDatatypeValid(datatype))
			throw new IllegalArgumentException("wrong datatype");

		imageData.setId(UUID.randomUUID() + "." + datatype);

		
		createDirIfNotExist(UPLOAD_PATH);
		OutputStream out = new FileOutputStream(new File(UPLOAD_PATH
				+ imageData.getId()));
		while ((read = input.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();

		return imageData;
	}
	
	/**
	 * Falls der Ordner noch nicht besteht wird er 
	 * mit dieser Methode erstellt.
	 * @param dir
	 */
	private void createDirIfNotExist(String dir){
		File path = new File(dir);
		if(!path.exists()){
			path.mkdirs();
		}
	}

	@Override
	public byte[] getImage(String id) throws Exception {
		BufferedImage img = null;
		String dataType = id.split("\\.")[1];
		try {
			img = ImageIO.read(new File(UPLOAD_PATH + id));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, dataType, baos);
			byte[] imageData = baos.toByteArray();
			return imageData;
		} catch (IOException e) {
			throw new FileNotFoundException();
		}

	}

	/**
	 * Der Datentyp der Datei wird mit den erlaubten Datentypen abgeglichen. 
	 * @param type
	 * @return true wenn Datentyp erlaub ist
	 */
	private boolean isDatatypeValid(String type) {
		return Arrays.asList(VALID_DATATYPES).contains(type.toUpperCase());
	}

	@Override
	public void deleteImage(String id) throws Exception {
		File file = new File(UPLOAD_PATH + id);

		if (!file.exists())
			throw new FileNotFoundException();

		if (!file.delete())
			throw new Exception("Delete operation failed");

	}

}
