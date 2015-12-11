/* Copyright (c) 2007 Pyxis Technologies inc.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org. */

package info.novatec.testit.livingdoc.samples.application.phonebook;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "COUNTRY")
@SuppressWarnings("serial")
public class Country extends AbstractEntity {
    private String name;
    private Set<State> states = new TreeSet<State>();

    public Country(String name) {
        super();

        this.name = name;
    }

    @Basic
    @Column(name = "NAME", unique = true, nullable = false, length = 255)
    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    public Set<State> getStates() {
        return states;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    public void addState(String stateName, String code) {
        states.add(new State(this, stateName, code));
    }

    public void removeState(State state) {
        states.remove(state);
    }

    @Override
    public boolean equals(Object o) {
        if ( ! ( o instanceof Country )) {
            return false;
        }

        Country countryCompared = ( Country ) o;

        return getName().equals(countryCompared.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
