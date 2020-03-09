package com.automation.pojo;

/**
 * @author Raghotham.Madhusudan
 */

public class HeaderInfo {
	
	private String fromHeader;
	private String authorizationHeader;
	private String contentTypeHeader;
	private String from;
	private String authorization;
	private String contentType;
	private String tenantId;
	private String awsUrl;
	

	public String getFrom() {
	return from;
	}

	public void setFrom(String from) {
	this.from = from;
	}

	public String getAuthorization() {
	return authorization;
	}

	public void setAuthorization(String authorization) {
	this.authorization = authorization;
	}

	public String getContentType() {
	return contentType;
	}

	public void setContentType(String contentType) {
	this.contentType = contentType;
	}

	public String getTenantId() {
	return tenantId;
	}

	public void setTenantId(String tenantId) {
	this.tenantId = tenantId;
	}

	public String getAwsUrl() {
		return awsUrl;
	}

	public void setAwsUrl(String awsUrl) {
		this.awsUrl = awsUrl;
	}

	public String getFromHeader() {
		return fromHeader;
	}

	public void setFromHeader(String fromHeader) {
		this.fromHeader = fromHeader;
	}

	public String getAuthorizationHeader() {
		return authorizationHeader;
	}

	public void setAuthorizationHeader(String authorizationHeader) {
		this.authorizationHeader = authorizationHeader;
	}

	public String getContentTypeHeader() {
		return contentTypeHeader;
	}

	public void setContentTypeHeader(String contentTypeHeader) {
		this.contentTypeHeader = contentTypeHeader;
	}
}
