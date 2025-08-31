package com.ofss.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Customer")
public class Customer {
	@Id
	private int custId;
	private String firstName;
	private String lastName;
	private String password;
	private String city;
	private String emailId;
	private long phoneNumber;

	// A customer can have multiple transactions
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
	
	public Customer() {
		// default constructor, using which you can initialize default values
		// or call it as a no-arg constructor
		super();
	}
	
	// define a constructor to initialize the attributes
	// parametrized constructor
	public Customer(int custId, String firstName, String lastName, String city, String emailId,
			long phoneNumber) {
		this.custId = custId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.city = city;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransaction(List<Transaction> transactions) {
		this.transactions=transactions;
	}
}
