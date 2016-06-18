package de.hm.edu.carads.controller.exceptions;

/**
 * Diese Exception wird ausgegeben wenn eine Entitaet nicht geloescht werden kann.
 * Es handelt sich in der Regel um Abhaengigkeiten die zu anderen Entitaeten bestehen.
 * @author BK
 *
 */
public class HasConstraintException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HasConstraintException(){
		super();
	}
}
