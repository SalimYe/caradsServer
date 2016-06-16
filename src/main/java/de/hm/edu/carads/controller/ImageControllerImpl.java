package de.hm.edu.carads.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.db.util.PropertieController;
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
	private static final String UPLOAD_PATH = System.getProperty("user.home") + PropertieController.getInstance().getPropertyString("IMG_DIR");
	
	private static final String IMG_MAX_SIZE = PropertieController.getInstance().getPropertyString("IMG_MAX_SIZE");
	/**
	 * Alle erlaubten Bild-Datentypen.
	 */
	public static final String[] VALID_DATATYPES = new String[] { "JPG", "JPEG", "PNG" };

	/**
	 * Logger
	 */
	final static Logger logger = Logger.getLogger(ImageControllerImpl.class);
	
	
	/**
	 * Diese Methode speichert ein Bild auf dem Server. Es werden nur JPG und PNG Datein erlaubt.
	 * @param InputStream des Bildes
	 * @param Datentyp der Datei
	 */
	@Override
	public Image saveImage(InputStream input, String datatype) throws Exception {

		Image imageData = new Image();

		int read = 0;
		byte[] bytes = new byte[this.parseAndCheckImgSize(IMG_MAX_SIZE)];

		if (!isDatatypeValid(datatype)){
			logger.error("Datatype "+datatype+" not valid");
			throw new IllegalArgumentException("wrong datatype");
		}
			

		//Die ID wird zufaellig generiert.
		imageData.setId(UUID.randomUUID() + "." + datatype);

		createDirIfNotExist(UPLOAD_PATH);
		OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + imageData.getId()));
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

	/**
	 * Das Bild wird aus dem Filesystem geladen und zurueck gegeben.
	 * @param ID des Bildes
	 */
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

	/**
	 * Das Bild mit der ID wird vom Filesystem geloescht.
	 */
	@Override
	public void deleteImage(String id) throws Exception {
		File file = new File(UPLOAD_PATH + id);

		if (!file.exists())
			throw new FileNotFoundException();

		if (!file.delete())
			throw new Exception("Delete operation failed");

	}
	
	/**
	 * Die maximale Gruesse eines Bildes wird nochmals auf Gueltigkeit ueberprueft.
	 * Falls die Groesse nicht gueltig ist wird ein Default (1024) zurueck gegeben.
	 * @param size
	 * @return maximale Bildgroesse
	 */
	private int parseAndCheckImgSize(String size){
		int imgSize = Integer.parseInt(size);
		if(imgSize>50 && imgSize <= 4096){
			return imgSize;
		}
		return 1024;
	}

}
