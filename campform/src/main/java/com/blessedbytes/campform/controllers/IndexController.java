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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import com.blessedbytes.campform.models.Person;

@Controller
public class IndexController {

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getOne(@PathVariable String id) throws CsvValidationException, NumberFormatException, IOException, FileNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
		try (CSVReader csvReader = new CSVReader(new FileReader("C:/Users/rormo/Downloads/hellow-world-java-spring/campform/src/main/java/com/blessedbytes/campform/database/test1.csv"))) {
        String[] nextLine;
        while ((nextLine = csvReader.readNext()) != null) {
            if (nextLine[0].equals(id)) {
                Person person = new Person();
                person.setId(Long.parseLong(nextLine[0]));
                person.setPacote(nextLine[1]);
                person.setName(nextLine[2]);
                person.setCpf(nextLine[3]);
                person.setPhoneNumber(nextLine[4]);
                person.setEmail(nextLine[5]);
                person.setAllergy(nextLine[6]);
                person.setTransport(nextLine[7]);
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
				person.setId(Long.parseLong(nextLine[0]));
				person.setPacote(nextLine[1]);
				person.setName(nextLine[2]);
				person.setCpf(nextLine[3]);
				person.setPhoneNumber(nextLine[4]);
				person.setEmail(nextLine[5]);
				person.setAllergy(nextLine[6]);
				person.setTransport(nextLine[7]);
	
				allPersons.add(person);
			}
		return allPersons;
		}
    }

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String post(@RequestBody String json) throws JsonProcessingException, IOException, CsvValidationException{
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(json);
			if(this.findId(jsonNode.get("id").asText())){
			}
			else{
				String[] csvData = {
					jsonNode.get("id").asText(),
					jsonNode.get("pacote").asText(),
					jsonNode.get("name").asText(),
					jsonNode.get("cpf").asText(),
					jsonNode.get("phoneNumber").asText(),
					jsonNode.get("email").asText(),
					jsonNode.get("allergy").asText(),
					jsonNode.get("transport").asText()
				};
				try (CSVWriter writer = new CSVWriter(new FileWriter("C:/Users/rormo/Downloads/hellow-world-java-spring/campform/src/main/java/com/blessedbytes/campform/database/test1.csv", true))) {
					writer.writeNext(csvData);
				}
				return "post";
			}
			return "post";
	}

	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable String id) throws FileNotFoundException, IOException, CsvValidationException{
		if(this.findId(id)){
			List<String[]> newCsvData = new ArrayList<>();
			try (CSVReader csvReader = new CSVReader(new FileReader("C:/Users/rormo/Downloads/hellow-world-java-spring/campform/src/main/java/com/blessedbytes/campform/database/test1.csv"))) {
				String[] nextLine;
				while ((nextLine = csvReader.readNext()) != null) {
					if (!nextLine[0].equals(id)) {
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

	private boolean findId(String id) throws FileNotFoundException, IOException, CsvValidationException{

		try (CSVReader csvReader = new CSVReader(new FileReader("C:/Users/rormo/Downloads/hellow-world-java-spring/campform/src/main/java/com/blessedbytes/campform/database/test1.csv"))) {
			String[] nextLine;
			while ((nextLine = csvReader.readNext()) != null) {
				if (nextLine[0].equals(id)) {
					return true;
				}
			}
			return false;
		}
	}
}