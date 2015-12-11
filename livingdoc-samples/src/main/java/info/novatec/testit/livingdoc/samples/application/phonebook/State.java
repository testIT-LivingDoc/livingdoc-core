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

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "STATE")
@SuppressWarnings("serial")
public class State extends AbstractEntity implements Comparable<State> {
    private Country country;
    private String name;
    private String code;

    public State(Country country, String name, String code) {
        super();

        this.country = country;
        this.name = name;
        this.code = code;
    }

    @Basic
    @Column(name = "CODE", unique = true, nullable = false, length = 255)
    public String getCode() {
        return code;
    }

    @Basic
    @Column(name = "NAME", unique = true, nullable = false, length = 255)
    public String getName() {
        return name;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "COUNTRY_ID")
    public Country getCountry() {
        return country;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public int compareTo(State o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if ( ! ( o instanceof State )) {
            return false;
        }

        State stateCompared = ( State ) o;

        return getName().equals(stateCompared.getName())
                && getCode().equals(stateCompared.getCode())
                && getCountry().equals(stateCompared.getCountry());
    }

    @Override
    public int hashCode() {
        return getName().hashCode() + getCode().hashCode() + getCountry().hashCode();
    }
}
