package com.automation.pojo;

/**
 * @author Raghotham.Madhusudan
 */

public class ConfigData {

	private UrlsPath urlsPath;
	private HeaderInfo headerInfo;
	private Assertions assertions;

	public UrlsPath getUrlsPath() {
	return urlsPath;
	}

	public void setUrlsPath(UrlsPath urlsPath) {
	this.urlsPath = urlsPath;
	}

	public HeaderInfo getHeaderInfo() {
	return headerInfo;
	}

	public void setHeaderInfo(HeaderInfo headerInfo) {
	this.headerInfo = headerInfo;
	}
	
	public Assertions getAssertions() {
	return assertions;
	}
	
	public void setAssertions(Assertions assertions) {
	this.assertions = assertions;
	}
}
