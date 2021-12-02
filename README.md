# Web Automation Framework

A cucumber selenium framework built to automate the SwagLabs website.
https://www.saucedemo.com/

## Prerequisites and Assumptions

To run this framework you will need to install Google Chrome (
https://www.google.com/intl/en_uk/chrome/) and the Java JDK the project was built against jdk-17.0.1. It is assumed the
user will be executing tests via a Windows machine.Optionally you can install the latest version of Intellij IDE and
execute via the ide itself.

## Project Structure

* Feature files/Gherkin and cucumber can be located within **src\test\resources**
* Step definitions/code can be located within **src\test\java\com\tyl\bdd**
* chromedriver.exe can be found under **src\main\resources**
* html reports can be found under the **target** folder


## Test execution

Run tests by executing the following command (**gradlew.bat cucumber**) via command prompt from within the framework.
Google Chrome should launch you should see the command prompt populate with the results from the run and as mentioned
above a html report will be created within the target directory.
