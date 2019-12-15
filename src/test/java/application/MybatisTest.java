package application;


import application.mybatis.dao.IUserDao;
import application.mybatis.sqlsession.SqlSession;
import application.mybatis.sqlsession.SqlSessionFactory;
import application.mybatis.sqlsession.SqlSessionFactoryBuilder;
import application.utils.Resources;

import java.io.InputStream;

/**
 * mybatis的入门案例
 */
public class MybatisTest {

	/**
	 * 入门案例
	 */
	public static void main(String[] args) throws Exception {
		//1.读取配置文件
		InputStream in = Resources.getResourceAsStream("conf\\SqlMapConfig.xml");
		//2.创建SqlSessionFactory工厂
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		SqlSessionFactory factory = builder.build(in);
		//3.使用工厂生产SqlSession对象
		SqlSession session = factory.openSession();
		//4.使用SqlSession创建Dao接口的代理对象
		IUserDao userDao = session.getMapper(IUserDao.class);
		//5.使用代理对象执行方法
		userDao.findAll().forEach(System.out::println);
		//6.释放资源
		session.close();
		in.close();
	}
}
