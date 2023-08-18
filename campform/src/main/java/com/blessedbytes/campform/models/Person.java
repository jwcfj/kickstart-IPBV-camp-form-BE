package com.blessedbytes.campform.models;

public class Person {

    private long id;
    private String pacote;
    private String name;
    private String cpf;
    private String phoneNumber;
    private String email;
    private String allergy;
    private String transport;

    // Getter and Setter methods for 'pacote'
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

    // Getter and Setter methods for 'pacote'
    public String getPacote() {
        return pacote;
    }
    
    public void setPacote(String pacote) {
        this.pacote = pacote;
    }
    
    // Getter and Setter methods for 'name'
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    // Getter and Setter methods for 'cpf'
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    // Getter and Setter methods for 'phoneNumber'
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    // Getter and Setter methods for 'email'
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    // Getter and Setter methods for 'allergy'
    public String getAllergy() {
        return allergy;
    }
    
    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }
    
    // Getter and Setter methods for 'transport'
    public String getTransport() {
        return transport;
    }
    
    public void setTransport(String transport) {
        this.transport = transport;
    }

}
