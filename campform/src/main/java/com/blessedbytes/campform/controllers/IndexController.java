package com.blessedbytes.campform.controllers;

import org.springframework.beans.factory.annotation.Value;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.blessedbytes.campform.models.Person;
import com.blessedbytes.campform.payment.Checkout;

@Controller
public class IndexController {

	private static final String REGISTRATION_INTERNAL_ERROR = "Unable to complete registration.";
	private static final String REQUEST_INTERNAL_ERROR = "Unable to complete request.";
	@Value("${local.file.path}")
	private String CSV_FILE_PATH;
	@Value("${secretkey.pagarme}")
    private String username;

	@RequestMapping(value = "/tabela-inscritos/{cpf}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Object> getOne(@PathVariable String cpf)
			throws CsvValidationException, IOException, FileNotFoundException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> response = new HashMap<>();
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
					person.setPayment(nextLine[11]);
					person.setAggregate(nextLine[12]);

					response.put("request", "OK");
					response.put("data", objectMapper.writeValueAsString(person));
					return ResponseEntity.status(HttpStatus.OK).body(response);
				}
			}
			response.put("request", "There is no CPF: " + cpf);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} catch (FileNotFoundException e1) {
			response.put("request", REQUEST_INTERNAL_ERROR);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (IOException e2) {
			response.put("request", REQUEST_INTERNAL_ERROR);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (CsvValidationException e3) {
			response.put("request", REQUEST_INTERNAL_ERROR);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@RequestMapping(value = "/tabela-inscritos", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Object> getAll()
			throws FileNotFoundException, IOException, CsvValidationException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Person> allPersons = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();
		try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
			String[] nextLine;
			String[] tableHeader;
			tableHeader = csvReader.readNext(); // TÁ PULANDO A PRIMEIRA LINHA
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
				person.setPayment(nextLine[11]);
				person.setAggregate(nextLine[12]);

				allPersons.add(person);
			}
			response.put("request", "OK");
			response.put("header", objectMapper.writeValueAsString(tableHeader));
			response.put("data", objectMapper.writeValueAsString(allPersons));
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (FileNotFoundException e1) {
			response.put("request", REQUEST_INTERNAL_ERROR);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (JsonProcessingException e2) {
			response.put("request", REQUEST_INTERNAL_ERROR);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (IOException e3) {
			response.put("request", REQUEST_INTERNAL_ERROR);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (CsvValidationException e4) {
			response.put("request", REQUEST_INTERNAL_ERROR);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> post(@RequestBody String json)
			throws JsonProcessingException, JsonMappingException, IOException, CsvValidationException, InterruptedException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> response = new HashMap<>();
		try {
			JsonNode jsonNode = objectMapper.readTree(json);
			if (this.findCpf(jsonNode.get("cpf").asText())) {
				response.put("registration", "CPF already exists.");
				response.put("data", "");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			} else {
				String[] csvData = {
						jsonNode.get("name").asText(),
						jsonNode.get("birthday").asText(),
						jsonNode.get("rg").asText(),
						jsonNode.get("rgShipper").asText(),
						jsonNode.get("rgShipperState").asText(),
						jsonNode.get("cpf").asText(),
						jsonNode.get("cellPhone").asText(),
						jsonNode.get("isWhatsApp").asText(),
						jsonNode.get("email").asText(),
						jsonNode.get("hasAllergy").asText()+" ---- "+jsonNode.get("allergy").asText(),
						jsonNode.get("package").asText(),
						jsonNode.get("formPayment").get("formPayment").asText()
				};
				try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH, true))) {
					writer.writeNext(csvData);
				} catch (IOException e1) {
					response.put("registration", REGISTRATION_INTERNAL_ERROR);
					response.put("data", "");
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
				}
				//GoogleDriveService.uploadWithConversion();
				if(jsonNode.get("formPayment").get("formPayment").asText().equals("inPerson")){
					response.put("registration", "Registered correctly");
					response.put("data", "");
					return ResponseEntity.status(HttpStatus.CREATED).body(response);
				}

				response.put("registration", "Registered correctly");
				Map<String, String> data = new HashMap<>();
				Checkout checkout = new Checkout();
				String payment_url = checkout.create(jsonNode.get("name").asText(), jsonNode.get("email").asText(), jsonNode.get("pacote").asText(), username);
        		data.put("payment_url", payment_url);
				response.put("data", data);
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}
		} catch (CsvValidationException e2) {
			response.put("registration", REGISTRATION_INTERNAL_ERROR);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (JsonMappingException e3) {
			response.put("registration", REGISTRATION_INTERNAL_ERROR);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (JsonProcessingException e4) {
			response.put("registration", REGISTRATION_INTERNAL_ERROR);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch(InterruptedException e5) {
			response.put("registration", REGISTRATION_INTERNAL_ERROR);
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@RequestMapping(value = "/tabela-inscritos/{cpf}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> delete(@PathVariable String cpf)
			throws FileNotFoundException, IOException, CsvValidationException {
		Map<String, Object> response = new HashMap<>();
		if (this.findCpf(cpf)) {
			List<String[]> newCsvData = new ArrayList<>();
			try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
				String[] nextLine;
				while ((nextLine = csvReader.readNext()) != null) {
					if (!nextLine[5].equals(cpf)) {
						newCsvData.add(nextLine);
					}
				}
			} catch (CsvValidationException e1) {
				response.put("request", REQUEST_INTERNAL_ERROR);
				response.put("data", "");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} catch (FileNotFoundException e2) {
				response.put("request", REQUEST_INTERNAL_ERROR);
				response.put("data", "");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} catch (IOException e3) {
				response.put("request", REQUEST_INTERNAL_ERROR);
				response.put("data", "");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			try (CSVWriter csvWriter = new CSVWriter(new FileWriter(CSV_FILE_PATH))) {
				csvWriter.writeAll(newCsvData);
			} catch (IOException e4) {
				response.put("request", REQUEST_INTERNAL_ERROR);
				response.put("data", "");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			response.put("request", "CPF correctly deleted.");
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		response.put("request", "There is no CPF: " + cpf);
		response.put("data", "");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@RequestMapping(value = "/tabela-inscritos/{cpf}", method = RequestMethod.PUT)
	public ResponseEntity<Object> update(@PathVariable String cpf, @RequestBody String json)
			throws FileNotFoundException, IOException, CsvValidationException, JsonProcessingException, JsonMappingException {
		Map<String, Object> response = new HashMap<>();
		if (this.findCpf(cpf)) {
			List<String[]> newCsvData = new ArrayList<>();
			ObjectMapper objectMapper = new ObjectMapper();

			try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
				JsonNode jsonNode = objectMapper.readTree(json);
				String[] nextLine;
				while ((nextLine = csvReader.readNext()) != null) {
					if (nextLine[5].equals(cpf)) {
						//nextLine[11] = jsonNode.get("formPayment").asText();
						nextLine[11] = "PAGO";
					}
					newCsvData.add(nextLine);
				}
			} catch (CsvValidationException e1) {
				response.put("request", REQUEST_INTERNAL_ERROR);
				response.put("data", "");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} catch (FileNotFoundException e2) {
				response.put("request", REQUEST_INTERNAL_ERROR);
				response.put("data", "");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			} catch (IOException e3) {
				response.put("request", REQUEST_INTERNAL_ERROR);
				response.put("data", "");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

			try (CSVWriter csvWriter = new CSVWriter(new FileWriter(CSV_FILE_PATH))) {
				csvWriter.writeAll(newCsvData);
			} catch (IOException e4) {
				response.put("request", REQUEST_INTERNAL_ERROR);
				response.put("data", "");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

			response.put("request", "CPF correctly updated.");
			response.put("data", "");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}

		response.put("request", "There is no CPF: " + cpf);
		response.put("data", "");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	private boolean findCpf(String cpf) throws FileNotFoundException, IOException, CsvValidationException {
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
