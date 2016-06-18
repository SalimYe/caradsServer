package de.hm.edu.carads.controller.exceptions;

/**
 * Falls eine Entitaet bereits angelegt wurde wird diese Exception ausgegeben.
 * @author BK
 *
 */
public class AlreadyExistsException extends Exception{
	private static final long serialVersionUID = 1L;

	public AlreadyExistsException(){
		super();
	}
}
