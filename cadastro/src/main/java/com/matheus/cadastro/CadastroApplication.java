package com.matheus.cadastro;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.matheus.cadastro")
@ServletComponentScan
public class CadastroApplication {
	private static SessionFactory factory;
	public static void main(String[] args) {
		if (factory == null)
			factory = new Configuration().configure().buildSessionFactory();
		SpringApplication.run(CadastroApplication.class, args);
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
