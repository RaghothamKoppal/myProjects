package com.automation.pojo;

import java.util.Map;

/**
 * @author Raghotham.Madhusudan
 */

public class AuxillaryData {

	private String id;
	private String type;
	private Map<String,Object> attributes;
	private String login;
	private String emailAddress;
	private String password;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String,Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String,Object> attributes) {
		this.attributes = attributes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
