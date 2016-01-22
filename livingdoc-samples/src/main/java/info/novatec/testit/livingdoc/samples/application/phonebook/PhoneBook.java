package info.novatec.testit.livingdoc.samples.application.phonebook;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "PHONEBOOK")
@SuppressWarnings("serial")
public class PhoneBook extends AbstractEntity {
    private List<PhoneBookEntry> entries = new ArrayList<PhoneBookEntry>();
    private String name;

    public PhoneBook(String name) {
        super();

        this.name = name;
    }

    @Basic
    @Column(name = "NAME", unique = true, nullable = false, length = 255)
    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "phoneBook", cascade = CascadeType.ALL)
    public List<PhoneBookEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<PhoneBookEntry> entries) {
        this.entries = entries;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void insert(String firstName, String lastName, String number) {
        entries.add(new PhoneBookEntry(this, firstName, lastName, number));
    }

    @Override
    public boolean equals(Object o) {
        if ( ! ( o instanceof PhoneBook )) {
            return false;
        }

        PhoneBook phoneBookCompared = ( PhoneBook ) o;

        return getName().equals(phoneBookCompared.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
