package com.blessedbytes.campform.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.blessedbytes.campform.models.Person;

@Controller
public class IndexController {

	private static final String REGISTRATION_INTERNAL_ERROR = "Unable to complete registration.";
	private static final String REQUEST_INTERNAL_ERROR = "Unable to complete request.";
	private static final String CSV_FILE_PATH = "./src/main/java/com/blessedbytes/campform/database/test1.csv";

	@RequestMapping(value = "/{cpf}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getOne(@PathVariable String cpf) throws CsvValidationException, IOException, FileNotFoundException, JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
		try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
			String[] nextLine;
			while ((nextLine = csvReader.readNext()) != null) {
				if (nextLine[5].equals(cpf)) {
					Person person = new Person();
					person.setName(nextLine[0]);
					person.setBirthday(nextLine[1]);
					person.setRg(nextLine[2]);
					person.setOrgaoExpedidor(nextLine[3]);
					person.setEstadoOrgaoExpedidor(nextLine[4]);
					person.setCpf(nextLine[5]);
					person.setPhoneNumber(nextLine[6]);
					person.setwhatsapp(nextLine[7]);
					person.setEmail(nextLine[8]);
					person.setAllergy(nextLine[9]);
					person.setPacote(nextLine[10]);
					String json = objectMapper.writeValueAsString(person);
					return ResponseEntity.status(HttpStatus.OK).body(json);
				}
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no CPF: "+cpf);
    	} catch (FileNotFoundException e1){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REQUEST_INTERNAL_ERROR);
		} catch (IOException e2){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REQUEST_INTERNAL_ERROR);
		} catch (CsvValidationException e3){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REQUEST_INTERNAL_ERROR);
		}
    }

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getAll() throws FileNotFoundException, IOException, CsvValidationException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
        List<Person> allPersons = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
			String[] nextLine;
			csvReader.readNext(); //TÁ PULANDO A PRIMEIRA LINHA
			while ((nextLine = csvReader.readNext()) != null) {
				Person person = new Person();
				person.setName(nextLine[0]);
				person.setBirthday(nextLine[1]);
				person.setRg(nextLine[2]);
				person.setOrgaoExpedidor(nextLine[3]);
				person.setEstadoOrgaoExpedidor(nextLine[4]);
				person.setCpf(nextLine[5]);
				person.setPhoneNumber(nextLine[6]);
				person.setwhatsapp(nextLine[7]);
				person.setEmail(nextLine[8]);
				person.setAllergy(nextLine[9]);
				person.setPacote(nextLine[10]);
	
				allPersons.add(person);
			}
			return ResponseEntity.status(HttpStatus.OK).body(objectMapper.writeValueAsString(allPersons));
		} catch (FileNotFoundException e1){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REQUEST_INTERNAL_ERROR);
		} catch (JsonProcessingException e2){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REQUEST_INTERNAL_ERROR);
	 	} catch (IOException e3){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REQUEST_INTERNAL_ERROR);
		} catch (CsvValidationException e4){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REQUEST_INTERNAL_ERROR);
		}
    }

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> post(@RequestBody String json) throws JsonProcessingException, JsonMappingException, IOException, CsvValidationException{
			ObjectMapper objectMapper = new ObjectMapper();
			try{
				JsonNode jsonNode = objectMapper.readTree(json);
				if(this.findCpf(jsonNode.get("cpf").asText())){
					return ResponseEntity.status(HttpStatus.FORBIDDEN).body("CPF already exists.");
				}
				else{
					String[] csvData = {
						jsonNode.get("name").asText(),
						jsonNode.get("birthday").asText(),
						jsonNode.get("rg").asText(),
						jsonNode.get("orgaoExpedidor").asText(),
						jsonNode.get("estadoOrgaoExpedidor").asText(),
						jsonNode.get("cpf").asText(),
						jsonNode.get("phoneNumber").asText(),
						jsonNode.get("whatsapp").asText(),
						jsonNode.get("email").asText(),
						jsonNode.get("allergy").asText(),
						jsonNode.get("pacote").asText()
					};
					try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH, true))){
						writer.writeNext(csvData);
					} catch (IOException e1) {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REGISTRATION_INTERNAL_ERROR);
					}
					return ResponseEntity.status(HttpStatus.CREATED).body("Registered correctly.");
				}
			} catch (CsvValidationException e2){
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REGISTRATION_INTERNAL_ERROR);
			} catch (JsonMappingException e3){
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REGISTRATION_INTERNAL_ERROR);
			} catch (JsonProcessingException e4){
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REGISTRATION_INTERNAL_ERROR);
			}
	}

	@RequestMapping(value = "/{cpf}", method=RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable String cpf) throws FileNotFoundException, IOException, CsvValidationException{
		if(this.findCpf(cpf)){
			List<String[]> newCsvData = new ArrayList<>();
			try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
				String[] nextLine;
				while ((nextLine = csvReader.readNext()) != null) {
					if (!nextLine[5].equals(cpf)) {
						newCsvData.add(nextLine);
					}
				}
			} catch (CsvValidationException e1){
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REQUEST_INTERNAL_ERROR);
			} catch (FileNotFoundException e2){
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REQUEST_INTERNAL_ERROR);
			} catch (IOException e3){
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REQUEST_INTERNAL_ERROR);
			}
			try (CSVWriter csvWriter = new CSVWriter(new FileWriter(CSV_FILE_PATH))) {
				csvWriter.writeAll(newCsvData);
			} catch (IOException e4){
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(REQUEST_INTERNAL_ERROR);
			}
			return ResponseEntity.status(HttpStatus.OK).body("CPF correctly deleted.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no CPF: "+cpf);
	}

	private boolean findCpf(String cpf) throws FileNotFoundException, IOException, CsvValidationException{
		try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
			String[] nextLine;
			while ((nextLine = csvReader.readNext()) != null) {
				if (nextLine[5].equals(cpf)) {
					return true;
				}
			}
			return false;
		}
	}
}