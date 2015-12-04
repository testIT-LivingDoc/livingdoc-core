package info.novatec.testit.livingdoc.samples.application.phonebook;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "PHONEBOOK_ENTRY")
@SuppressWarnings("serial")
public class PhoneBookEntry extends AbstractEntity {
    private PhoneBook phoneBook;
    private String firstName;
    private String lastName;
    private String number;

    public PhoneBookEntry(PhoneBook phoneBook, String firstName, String lastName, String number) {
        super();

        this.phoneBook = phoneBook;
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
    }

    @Basic
    @Column(name = "FIRSTNAME", unique = true, nullable = false, length = 255)
    public String getFirstName() {
        return firstName;
    }

    @Basic
    @Column(name = "LASTNAME", unique = true, nullable = false, length = 255)
    public String getLastName() {
        return lastName;
    }

    @Basic
    @Column(name = "NUMBER", unique = true, nullable = false, length = 255)
    public String getNumber() {
        return number;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "PHONEBOOK_ID")
    public PhoneBook getPhoneBook() {
        return phoneBook;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPhoneBook(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    @Override
    public boolean equals(Object o) {
        if ( ! ( o instanceof PhoneBookEntry )) {
            return false;
        }

        PhoneBookEntry entryCompared = ( PhoneBookEntry ) o;
        if (getFirstName().equals(entryCompared.getFirstName()) && getLastName().equals(entryCompared.getLastName())
            && getNumber().equals(entryCompared.getNumber()) && getPhoneBook().equals(entryCompared.getPhoneBook())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getFirstName().hashCode() + getLastName().hashCode() + getNumber().hashCode() + getPhoneBook().hashCode();
    }
}
