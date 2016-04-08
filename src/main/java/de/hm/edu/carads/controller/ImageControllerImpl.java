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
import javax.ws.rs.WebApplicationException;

import de.hm.edu.carads.models.Image;

public class ImageControllerImpl implements ImageController{

	private static final String UPLOAD_PATH = "/tmp/";
	public static final String[] VALID_DATATYPES = new String[] {"jpg","png","gif"};
	
	@Override
	public Image saveImage(InputStream input, String datatype) throws Exception{
		
		Image imageData = new Image();
		
        int read = 0;
        byte[] bytes = new byte[1024];

        if(!isDatatypeValid(datatype))
        	throw new IllegalArgumentException("wrong datatype");
        
        imageData.setId(/*UPLOAD_PATH + */UUID.randomUUID() + "." + datatype);
        imageData.setDatatype(datatype);
        
        OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + imageData.getId()));
        while ((read = input.read(bytes)) != -1) 
        {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
	    
		return imageData;
	}

	@Override
	public byte[] getImage(String id) throws Exception{
		BufferedImage img = null;
		String dataType = id.split("\\.")[1];
		try{
		    img = ImageIO.read(new File(UPLOAD_PATH + id)); // eventually C:\\ImageTest\\pic2.jpg
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ImageIO.write(img, dataType, baos);
		    byte[] imageData = baos.toByteArray();
		    return imageData;
		}catch(IOException e){
			throw new FileNotFoundException();
		}
		
	}
	
	private boolean isDatatypeValid(String type){
		return Arrays.asList(VALID_DATATYPES).contains(type);
	}

}
