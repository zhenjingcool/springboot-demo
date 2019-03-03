package spring.cloud.dao;


import spring.cloud.model.User;

import java.util.List;


public interface UserDao {

	List<User> findAll();
}
