var utils = require('../utils/commonUtils');

var HomePage = function() {
	const LOCATORS = {

		firstName : by.name('firstname'),
		lastName : by.name('lastname'),
		emailAddress : by.name('reg_email__'),
		password : by.name('reg_passwd__'),
		gender : by.xpath("//input[@name='sex' and @value='2']"),
		signUp : by.xpath("//button[text()='Sign Up']")
	};
	
	this.enterFirstName = function(){
		utils.waitAndSendKeys(LOCATORS.firstName,"Mickey");
	};

	this.enterLastName = function(){
		utils.waitAndSendKeys(LOCATORS.lastName,"Mouse");
	};

	this.enterEmailAddress = function(){
		utils.waitAndSendKeys(LOCATORS.emailAddress,"micky.mouse@gmail.com");
	};

	this.enterPassword = function(){
		utils.waitAndSendKeys(LOCATORS.password,"disney123");
	};

	this.selectGender = function(){
		utils.waitAndClick(LOCATORS.gender);
	};

	this.clickSignUp = function(){
		utils.waitAndClick(LOCATORS.signUp)
	};

  
};
module.exports = new HomePage();
