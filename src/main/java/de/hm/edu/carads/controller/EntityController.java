package de.hm.edu.carads.controller;

public interface EntityController<E> {
	public E makeEntityFromDBObject();
}
