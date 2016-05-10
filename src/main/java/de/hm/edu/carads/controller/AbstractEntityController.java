package de.hm.edu.carads.controller;

import java.util.Collection;

import de.hm.edu.carads.db.DatabaseController;


public interface AbstractEntityController<E> {
	public Collection<E> getAllEntities();
	public E addEntity(E entity) throws Exception;
	public E getEntity(String id) throws Exception;
	public E getEntityByMail(String mail)throws Exception;
	public void deleteEntity(String id) throws Exception;
	public E changeEntity(String id, E updatedEntity) throws Exception;
	public long getEntityCount();
}
