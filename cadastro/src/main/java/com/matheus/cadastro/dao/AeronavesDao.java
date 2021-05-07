package com.matheus.cadastro.dao;

import com.matheus.cadastro.model.Aeronave;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@SuppressWarnings({"unchecked","deprecation"})
public class AeronavesDao extends Dao {

	private static final long serialVersionUID = 5739355674644803588L;
	
	public List<Aeronave> readAeronaves() {
		Query<?> query = session.createQuery("from Aeronave");
		List<Aeronave> aeronaves = (List<Aeronave>) readManyObjects(query);
		return aeronaves;
	}
	
	public List<Object> byDecada(){
		Query<?> query = session.createSQLQuery("select LEFT(ano, 3) as decade , sum(cnt) from (select count(id) as cnt, ano from Aeronave group by ano)  as T1 group by LEFT(ano, 3)");
		List<Object> aeronaves = (List<Object>) readManyObjects(query);
		return aeronaves;
	}
	
	public List<Object> byMarca(){
		Query<?> query = session.createSQLQuery("SELECT marca, COUNT(*) FROM Aeronave GROUP BY marca ORDER BY 2 DESC");
		List<Object> aeronaves = (List<Object>) readManyObjects(query);
		return aeronaves;
	}
	
	public List<Object> byUltimaSemana(){
		Query<?> query = session.createSQLQuery("select count(id) from aeronave where created between date_sub(now(),INTERVAL 1 WEEK) and now()");
		List<Object> aeronaves = (List<Object>) readManyObjects(query);
		return aeronaves;
	}
	
	public Aeronave aeronavePorId(String id) {
		Query<?> query = session.createQuery("from Aeronave where id="+id);
		Aeronave aeronave = new Aeronave();
		aeronave = (Aeronave) readOneObject(query);
		return aeronave;
	}
	
	public List<Aeronave> aeronavesPorTermo(String term) {
		Query<?> query;
		if(term.matches("[+-]?\\d*(\\.\\d+)?")) {
			query = session.createQuery("from Aeronave where id="+term);
		}else {
			query = session.createQuery("from Aeronave where lower(modelo) like lower('%"+term+"%')");
		}
		return (List<Aeronave>) readManyObjects(query);
	}
}
