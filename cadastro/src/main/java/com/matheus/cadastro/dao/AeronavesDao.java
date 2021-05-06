package com.matheus.cadastro.dao;

import com.matheus.cadastro.model.Aeronaves;


import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AeronavesDao extends Dao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5739355674644803588L;
	@SuppressWarnings("unchecked")
	public List<Aeronaves> readAeronaves() {
		Query<?> query = session.createQuery("from Aeronaves");
		List<Aeronaves> aeronaves = (List<Aeronaves>) readManyObjects(query);
		return aeronaves;
	}
	
	public Aeronaves aeronavePorId(String id) {
		System.out.println("MEU ID"+id);
		Query<?> query = session.createQuery("from Aeronaves where id ="+id);
		Aeronaves aeronave = new Aeronaves();
		aeronave = (Aeronaves) readOneObject(query);
		return aeronave;
		
	}
	
}
