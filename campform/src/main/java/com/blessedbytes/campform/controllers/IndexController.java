package com.blessedbytes.campform.controllers;

import org.springframework.http.MediaType;

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
//import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import com.blessedbytes.campform.models.Person;

@Controller
public class IndexController {

	@RequestMapping(value = "/{cpf}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getOne(@PathVariable String cpf) throws CsvValidationException, NumberFormatException, IOException, FileNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
		try (CSVReader csvReader = new CSVReader(new FileReader("C:/Users/rormo/Downloads/hellow-world-java-spring/campform/src/main/java/com/blessedbytes/campform/database/test1.csv"))) {
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
					return json;
				}
			}
			return "error";
    	}
    }

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Person> getAll() throws FileNotFoundException, IOException, CsvValidationException, NumberFormatException {
        List<Person> allPersons = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader("C:/Users/rormo/Downloads/hellow-world-java-spring/campform/src/main/java/com/blessedbytes/campform/database/test1.csv"))) {
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
			return allPersons;
		}
    }

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String post(@RequestBody String json) throws JsonProcessingException, IOException, CsvValidationException{
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(json);
			if(this.findCpf(jsonNode.get("cpf").asText())){
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
				//try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/rormo/Downloads/hellow-world-java-spring/campform/src/main/java/com/blessedbytes/campform/database/test1.csv", true), StandardCharsets.UTF_8))) {
				try (CSVWriter writer = new CSVWriter(new FileWriter("C:/Users/rormo/Downloads/hellow-world-java-spring/campform/src/main/java/com/blessedbytes/campform/database/test1.csv", true))){
					writer.writeNext(csvData);
				}
				return "post";
			}
			return "post";
	}

	@RequestMapping(value = "/{cpf}", method=RequestMethod.DELETE)
	public String delete(@PathVariable String cpf) throws FileNotFoundException, IOException, CsvValidationException{
		if(this.findCpf(cpf)){
			List<String[]> newCsvData = new ArrayList<>();
			try (CSVReader csvReader = new CSVReader(new FileReader("C:/Users/rormo/Downloads/hellow-world-java-spring/campform/src/main/java/com/blessedbytes/campform/database/test1.csv"))) {
				String[] nextLine;
				while ((nextLine = csvReader.readNext()) != null) {
					if (!nextLine[5].equals(cpf)) {
						newCsvData.add(nextLine);
					}
				}
			}
			try (CSVWriter csvWriter = new CSVWriter(new FileWriter("C:/Users/rormo/Downloads/hellow-world-java-spring/campform/src/main/java/com/blessedbytes/campform/database/test1.csv"))) {
				csvWriter.writeAll(newCsvData);
			}
			return "delete";
		}
		return "delete";
	}

	private boolean findCpf(String cpf) throws FileNotFoundException, IOException, CsvValidationException{

		try (CSVReader csvReader = new CSVReader(new FileReader("C:/Users/rormo/Downloads/hellow-world-java-spring/campform/src/main/java/com/blessedbytes/campform/database/test1.csv"))) {
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