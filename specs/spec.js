// spec.js
//var chartPageHeader = require('../components/GoogleChartHeader');
var homePage = require('../components/HomePage');

describe('facebook signup', function() {
	
  beforeAll(function() {
    browser.waitForAngularEnabled(false);
	console.log("in before all");
    browser.get('https://facebook.com/signup'); 
  });
   
 it('verify facebook page title', function() {
    expect(browser.getTitle()).toContain('Facebook');

  });

  it('enter signup form', function() {

    homePage.enterFirstName();
    homePage.enterLastName();
    homePage.enterEmailAddress();
    homePage.enterPassword();
    homePage.selectGender();
    homePage.clickSignUp();
  });

});



