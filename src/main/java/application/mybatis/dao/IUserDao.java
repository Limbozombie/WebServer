package application.mybatis.dao;


import application.mybatis.annotations.Select;
import application.mybatis.domain.User;

import java.util.List;

/**
 * 用户的持久层接口
 */
public interface IUserDao {

	/**
	 * 查询所有操作
	 */
	@Select("select * from user")
	List<User> findAll();
}
