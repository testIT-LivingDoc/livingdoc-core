package info.novatec.testit.livingdoc.samples.application.bank;

import info.novatec.testit.livingdoc.extensions.guice.GuiceSystemUnderDevelopment;

public class BankSystemUnderDevelopment extends GuiceSystemUnderDevelopment {

	public BankSystemUnderDevelopment() throws Exception {
		super();
		addImport("info.novatec.testit.livingdoc.samples.application.bank");
		addImport("info.novatec.testit.livingdoc.samples.application.mortgage");
	}
}