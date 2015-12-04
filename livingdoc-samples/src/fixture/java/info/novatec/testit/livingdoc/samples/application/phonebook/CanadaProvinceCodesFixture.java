package info.novatec.testit.livingdoc.samples.application.phonebook;

import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Property;

import info.novatec.testit.livingdoc.samples.application.phonebook.Country;
import info.novatec.testit.livingdoc.samples.application.phonebook.State;
import info.novatec.testit.livingdoc.samples.application.phonebook.hibernate.PhoneBookMemoryDatabase;

public class CanadaProvinceCodesFixture
{
	PhoneBookMemoryDatabase db;
	
	public CanadaProvinceCodesFixture() throws Exception
	{
    	db = new PhoneBookMemoryDatabase();
    	db.mount();
    	setUp();
	}
	
    public Set<State> query() throws Exception
    {
    	db.startSession();
    	Country canada = getByName("CANADA");
        Set<State>states = canada.getStates();
        
        return states;
    }
    
    private void setUp() throws Exception
    {
    	db.startSession();
    	db.beginTransaction();
    	Country canada = new Country("CANADA");
    	canada.addState("NEWFOUNDLAND and LABRADOR","NL");
    	canada.addState("ALBERTA","AB");
    	canada.addState("QUEBEC","QC");
    	canada.addState("ONTARIO","ON");
    	canada.addState("NUNAVUT","NU");
    	canada.addState("NEW BRUNSWICK","NB");
    	canada.addState("NOVA SCOTIA","NS");
    	canada.addState("MANITOBA","MB");
    	canada.addState("BRITISH COLUMBIA","BC");
    	canada.addState("SASKATCHEWAN","SK");
    	canada.addState("YUKON","YT");
    	canada.addState("PRINCE EDWARD ISLAND","PE");
    	db.getSession().save(canada);
    	db.commitTransaction();
    }
    
    private Country getByName(String name)
    {
        final Criteria crit = db.getSession().createCriteria(Country.class);
        crit.add(Property.forName("name").eq(name));
        return (Country) crit.uniqueResult();
    }

}
