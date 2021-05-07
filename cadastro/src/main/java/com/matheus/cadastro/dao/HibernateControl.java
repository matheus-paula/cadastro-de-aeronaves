package com.matheus.cadastro.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

class HibernateControl {
	private static SessionFactory factory;

	public HibernateControl() {
		if (factory == null)
			factory = new Configuration().configure().buildSessionFactory();
	}

	public synchronized SessionFactory getFactory() {
		return factory;
	}

	public synchronized void shutdownFactory() {
		factory.close();
	}

	public synchronized Session getSession() {
		return factory.openSession();
	}
}