package com.matheus.cadastro.controller;


import java.util.Date;
import javax.servlet.http.HttpServletRequest;

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
import com.matheus.cadastro.model.Aeronaves;
import com.matheus.cadastro.model.CommitStatus;
import com.matheus.cadastro.model.Toastr;
import com.matheus.cadastro.model.ToastrType;

@Controller
public class MainController implements WebMvcConfigurer{
	 	@Autowired (required=true)
	 	private AeronavesDao aeronaveDao;
		
	 	@RequestMapping("/*")
		  @ResponseBody
		  public String main () {
		      return "ok";
		  }
		
	
	 	@CrossOrigin(origins = "*", maxAge = 3600)
		@RequestMapping(value = {"/aeronaves/{id}"}, method = RequestMethod.GET)
		@ResponseBody
		public String getAeronaveById (@PathVariable(value="id") String id) {
			String jsonInString;
			Aeronaves aeronave = aeronaveDao.aeronavePorId(id);
			if(aeronave != null) {
				Gson gson = new GsonBuilder()
				        .serializeNulls()
				        .setPrettyPrinting().create(); 
				jsonInString = gson.toJson(aeronave);
				
			}else {
				jsonInString = "sda";
			}
			return jsonInString;
		}
	
		@RequestMapping(value = {"/aeronaves"}, method = RequestMethod.POST)
		@ResponseBody
		public String addAeronave (HttpServletRequest request) {
			
			Aeronaves aeronave = new Aeronaves();
			Toastr toastrJson = new Toastr();
			aeronave.setCreated(new Date());
			aeronave.setUpdated(new Date());
			aeronave.setNome(request.getParameter("aeronave.nome"));
			aeronave.setDescricao(request.getParameter("aeronave.descricao"));
			aeronave.setAno(Integer.parseInt(request.getParameter("aeronave.ano")));
			aeronave.setVendido(request.getParameter("aeronave.vendido") == "true"? true: false);
			aeronave.setMarca(request.getParameter("aeronave.marca"));
			
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
			Gson gson = new GsonBuilder()
			        .serializeNulls()
			        .setPrettyPrinting().create(); 
			
			return gson.toJson(toastrJson);
			
		}
		
		
		
		@RequestMapping(value = {"/aeronaves/{id}"}, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public String removeAeronave (@PathVariable(value="id") String id) {
		
			Toastr toastrJson = new Toastr();
			Aeronaves aeronave = aeronaveDao.aeronavePorId(id);
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
			Gson gson = new Gson();
		return gson.toJson(toastrJson);
	}
		
		
		
		@RequestMapping(value = {"/aeronaves/{id}"}, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public String updateAeronave (@PathVariable(value="id") String id, HttpServletRequest request) {
			Toastr toastrJson = new Toastr();
			if(id != null) {
				Aeronaves aeronave = aeronaveDao.aeronavePorId(id);
				if(aeronave != null) {
					aeronave.setUpdated(new Date());
					if(request.getParameter("aeronave.nome") != null) {
						aeronave.setNome(request.getParameter("aeronave.nome"));
					}
					if(request.getParameter("aeronave.descricao") != null) {
						aeronave.setDescricao(request.getParameter("aeronave.descricao"));
					}
					if(request.getParameter("aeronave.ano") != null) {
						aeronave.setAno(Integer.parseInt(request.getParameter("aeronave.ano")));
					}
					if(request.getParameter("aeronave.vendido") != null) {
						aeronave.setVendido(request.getParameter("aeronave.vendido") == "true"? true: false);
					}
					if(request.getParameter("aeronave.marca") != null) {
						aeronave.setMarca(request.getParameter("aeronave.marca"));
					}
					
					CommitStatus commit = null;
					commit = aeronaveDao.update(aeronave);
					if (commit.isCommited()) {
						toastrJson.setType(ToastrType.success);
						toastrJson.setTitle("Cadastro atualizado com sucesso!");
						toastrJson.setMessage(
								"O avião foi atualizado com sucesso!");
					} else {
						toastrJson.setType(ToastrType.failure);
						toastrJson.setTitle("Cadastro não atualizado");
						toastrJson.setMessage(
								"Aconteceu algo de errado durante a tentativa de atualização do avião!");
					}
					
				}else {
					toastrJson.setType(ToastrType.success);
					toastrJson.setTitle("Cadastro não existe!");
					toastrJson.setMessage(
							"Este cadastro não existe no sistema!");

				}
				
			}
			Gson gson = new Gson();
			return gson.toJson(toastrJson);
		}
}
