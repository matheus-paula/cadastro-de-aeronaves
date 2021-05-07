package com.matheus.cadastro.controller;


import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matheus.cadastro.dao.AeronavesDao;
import com.matheus.cadastro.model.Aeronave;
import com.matheus.cadastro.model.CommitStatus;
import com.matheus.cadastro.model.Toastr;
import com.matheus.cadastro.model.ToastrType;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class MainController implements WebMvcConfigurer{
	 	@Autowired (required=true)
	 	private AeronavesDao aeronaveDao;
	 	private Gson gson = new GsonBuilder()
		        .serializeNulls()
		        .setPrettyPrinting().create(); 
		
	 	/* AERONAVE POR ID */
	 	@RequestMapping(value = {"/aeronaves/{id}"}, method = RequestMethod.GET)
		@ResponseBody
		public String aeronaveById(@PathVariable(value="id") String id) {
	 		Aeronave aeronave = aeronaveDao.aeronavePorId(id);
	 		return (aeronave != null) ? gson.toJson(aeronave) : "{}";
		}
	 	
	 	/* AERONAVES POR MARCA (Contagem) */
	 	@RequestMapping("/aeronaves/marca")
		@ResponseBody
		public String byMarca() {
			List<Object> marca = aeronaveDao.byMarca();
			return (marca != null) ? gson.toJson(marca) : "{}";
		}
	 	
	 	/* AERONAVE ADICIONADAS NA SEMANA */
	 	@RequestMapping("/aeronaves/semana")
		@ResponseBody
		public String byUltimaSemana() {
			List<Object> semana =  aeronaveDao.byUltimaSemana();
			return (semana != null) ? gson.toJson(semana) : "{}";
		}
	 	
	 	/* AERONAVE POR DECADA */
	 	@RequestMapping("/aeronaves/decada")
		@ResponseBody
		public String byDecada() {
	 		List<Object> decadas = aeronaveDao.byDecada(); 
	 		return (decadas != null) ? gson.toJson(decadas) : "{}";
		}
	 	
	 	/* AERONAVES POR ID OU MODELO */
	 	@RequestMapping(value = {"/aeronaves/term/{term}"}, method = RequestMethod.GET)
		@ResponseBody
		public String getAeronavesById (@PathVariable(value="term") String term) {
			List<Aeronave> aeronaves = aeronaveDao.aeronavesPorTermo(term);
			return (aeronaves != null) ? gson.toJson(aeronaves) : "{}";
		}
	
	 	/* ADICIONAR AERONAVE */
	 	@RequestMapping(value = {"/aeronaves"}, method = RequestMethod.POST)
	    @ResponseBody
		public String addAeronave (HttpServletRequest request, HttpServletResponse response) {
	    	Toastr toastrJson = new Toastr();
			Aeronave aeronave = new Aeronave();
			try {
				aeronave = gson.fromJson(request.getReader().readLine(), Aeronave.class);
				aeronave.setCreated(new Date());
				aeronave.setUpdated(new Date());
				CommitStatus commit = null;
				commit = aeronaveDao.save(aeronave);
				if (commit.isCommited()) {
					toastrJson.setType(ToastrType.success);
					toastrJson.setTitle("Cadastro realizado com sucesso!");
					toastrJson.setMessage(
							"O avião foi cadastrado com sucesso!");
				} else {
					toastrJson.setType(ToastrType.failure);
					toastrJson.setTitle("Cadastro não realizado");
					toastrJson.setMessage(
							"Aconteceu algo de errado durante a tentativa de cadastro do avião!");
				}
			} catch (IOException e) {
				toastrJson.setType(ToastrType.failure);
				toastrJson.setTitle("Cadastro não realizado");
				toastrJson.setMessage(
						"Aconteceu algo de errado durante a tentativa de cadastro do avião!");
			}
			return gson.toJson(toastrJson);
		}
		
	 	/* REMOVER AERONAVE */
	 	@RequestMapping(
	 			value = {"/aeronaves/{id}"}, method = RequestMethod.DELETE, 
	 			produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public String removeAeronave (@PathVariable(value="id") String id) {		
			Toastr toastrJson = new Toastr();
			Aeronave aeronave = aeronaveDao.aeronavePorId(id);
			if(aeronave != null) {
				CommitStatus commit = null;
				commit = aeronaveDao.delete(aeronave);
				if (commit.isCommited()) {
					toastrJson.setType(ToastrType.success);
					toastrJson.setTitle("Cadastro removido com sucesso!");
					toastrJson.setMessage(
							"O avião foi removido com sucesso!");
				} else {
					toastrJson.setType(ToastrType.failure);
					toastrJson.setTitle("Cadastro não removido");
					toastrJson.setMessage(
							"Aconteceu algo de errado durante a tentativa de remoção do avião!");
				}
			}else {
				toastrJson.setType(ToastrType.success);
				toastrJson.setTitle("Cadastro não existe!");
				toastrJson.setMessage(
						"Este cadastro não existe no sistema!");
	
			}
			return gson.toJson(toastrJson);
		}
		
	 	/* ATUALIZAR AERONAVE */
		@RequestMapping(
				value = {"/aeronaves/{id}"}, method = RequestMethod.PUT, 
				produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public String updateAeronave (@PathVariable(value="id") String id, HttpServletRequest request) {
			Toastr toastrJson = new Toastr();
			Gson gson = new GsonBuilder()
			        .serializeNulls()
			        .setPrettyPrinting().create(); 
			try {
				Aeronave aeronave = aeronaveDao.aeronavePorId(id);
				Aeronave updatedAeronave = gson.fromJson(request.getReader().readLine(), Aeronave.class);
				aeronave.setUpdated(new Date());
				aeronave.setAno(updatedAeronave.getAno());
				if(!updatedAeronave.getDescricao().isEmpty()) {
					aeronave.setDescricao(updatedAeronave.getDescricao());
				}
				if(!updatedAeronave.getModelo().isEmpty()) {
					aeronave.setModelo(updatedAeronave.getModelo());
				}
				if(!updatedAeronave.getMarca().isEmpty()) {
					aeronave.setMarca(updatedAeronave.getMarca());
				}
				aeronave.setVendido(updatedAeronave.isVendido());
				CommitStatus commit = null;
				commit = aeronaveDao.save(aeronave);
				if (commit.isCommited()) {
					toastrJson.setType(ToastrType.success);
					toastrJson.setTitle("Cadastro atualizado com sucesso!");
					toastrJson.setMessage(
							"O avião foi atualizado com sucesso!");
				} else {
					toastrJson.setType(ToastrType.failure);
					toastrJson.setTitle("Cadastro não atualizado");
					toastrJson.setMessage(
							"Aconteceu algo de errado durante a tentativa de atualização do cadastro!");
				}
			} catch (IOException e) {
				toastrJson.setType(ToastrType.failure);
				toastrJson.setTitle("Cadastro não realizado");
				toastrJson.setMessage(
						"Aconteceu algo de errado durante a tentativa de atualização do cadastro!");
			}
			return gson.toJson(toastrJson);
		}
}
