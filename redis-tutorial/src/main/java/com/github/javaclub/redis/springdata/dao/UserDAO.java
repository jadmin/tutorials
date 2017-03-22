/*
 * @(#)UserDAO.java	2017-03-22
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.redis.springdata.dao;

import com.github.javaclub.redis.springdata.User;

/**
 * UserDAO
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: UserDAO.java 2017-03-22 2017-03-22 15:31:59 Exp $
 */
public interface UserDAO {

	void saveUser(final User user);
	User getUser(final long id);
}
