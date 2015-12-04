/**
 * Copyright (c) 2009 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package info.novatec.testit.livingdoc.samples.application.system;

import info.novatec.testit.livingdoc.reflect.CollectionProvider;
import info.novatec.testit.livingdoc.util.StringUtil;

public class AccountFixture
{
	private final AccountManager accountManager = new AccountManager();

	private String user;
	private String group;
	private String listOfGroups;

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public String getListOfGroups()
	{
		return listOfGroups;
	}

	public void setListOfGroups(String listOfGroups)
	{
		this.listOfGroups = listOfGroups;
	}

	public void enterRow()
	{
		if (!StringUtil.isEmpty(user))
		{
			insertUser(user);
		}

		if (!StringUtil.isEmpty(group))
		{
			insertGroup(group);
		}

		if (!StringUtil.isEmpty(listOfGroups))
		{
			if (StringUtil.isEmpty(user))
			{
				throw new SystemException("You must specify a user to associate groups");
			}

			String[] tokens = listOfGroups.split(",");
			
			for (String token : tokens)
			{
				associateUserWithGroup(user, token.trim());
			}
		}
	}

	@CollectionProvider
	public String[] getUsers()
	{
		return accountManager.getAllUsers();
	}

	public String[] getGroups()
	{
		return accountManager.getAllGroups();
	}

	public boolean associated()
	{
		return accountManager.isUserAssociatedToGroup(user, group);
	}

	public boolean insertGroup(String name)
	{
		return accountManager.insertGroup(name);
	}

	public boolean deleteGroup(String name)
	{
		return accountManager.deleteGroup(name);
	}

	public boolean insertUser(String name)
	{
		return accountManager.insertUser(name);
	}

	public boolean deleteUser(String name)
	{
		return accountManager.deleteUser(name);
	}

	public boolean associateUserWithGroup(String user, String group)
	{
		return accountManager.associateUserWithGroup(user, group);
	}
	
	private String token1 = "2009/03/18";
	private String token2 = "2009/03/25";
	
	public String theMessageWithoutToken(String messageWithToken)
	{
		return messageWithToken.replaceAll("\\$\\{startDate\\}", token1).replaceAll("\\$\\{endDate\\}", token2);
	}
	
	public boolean checkThatTaliaSays(String messageWithToken)
	{
		return getMessage().equals(theMessageWithoutToken(messageWithToken));
	}
	
	private String getMessage()
	{
		return "Date between 2009/03/18 and 2009/03/25";
	}
}