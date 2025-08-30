package com.ofss.model;

// import java.time.LocalDate;
import java.util.List;

public class Customer {
	private int custId;
	private String firstName;
	private String lastName;
	private String city;
	private String emailId;
	private long phoneNumber;
	private List<Integer> stockIds;
	
	public Customer() {
		// default constructor, using which you can initialize default values
		// or call it as a no-arg constructor
		super();
	}
	
	// define a constructor to initialize the attributes
	// parametrized constructor
	public Customer(int custId, String firstName, String lastName, String city, String emailId,
			long phoneNumber,List<Integer> stockIds) {
		this.custId = custId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.city = city;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.stockIds=stockIds;
	}

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public List<Integer> getStockIds() { 
		return stockIds; 
	}
	
    public void setStockIds(List<Integer> stockIds) { 
    	this.stockIds = stockIds; 
    }

}
