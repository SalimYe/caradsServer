package de.hm.edu.carads.controller.util;

import java.util.Collection;

import javax.ws.rs.core.NoContentException;

import de.hm.edu.carads.models.User;

public interface AbstractEntityController<E> {
	public Collection<E> getAllEntities();
	public E addEntity(E entity) throws Exception;
	public E getEntity(String id) throws Exception;
	public E getWithSubEntityId(String id) throws Exception;
	public E getEntityByMail(String mail)throws Exception;
	public void deleteEntity(String id) throws NoContentException;
	public void changeEntity(String id, E updatedEntity) throws Exception;
	public long getEntityCount();
}
