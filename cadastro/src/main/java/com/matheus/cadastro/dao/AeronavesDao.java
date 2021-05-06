package com.matheus.cadastro.dao;

import com.matheus.cadastro.model.Aeronaves;


import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
		Query<?> query = session.createQuery("from Aeronaves where id="+id);
		Aeronaves aeronave = new Aeronaves();
		aeronave = (Aeronaves) readOneObject(query);
		return aeronave;
	}
	
	@SuppressWarnings("unchecked")
	public List<Aeronaves> aeronavesPorTermo(String term) {
		Query<?> query;
		if(term.matches("[+-]?\\d*(\\.\\d+)?")) {
			query = session.createQuery("from Aeronaves a where a.id="+term);
		}else {
			query = session.createQuery("from Aeronaves a where lower(a.nome) like lower('%"+term+"%')");
		}
		List<Aeronaves> aeronaves = new ArrayList<Aeronaves>();
		aeronaves = (List<Aeronaves>) readManyObjects(query);
		return aeronaves;
	}
	
}
