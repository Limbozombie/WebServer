package application.mybatis.sqlsession;


import application.mybatis.cfg.MybatisConfiguration;
import application.mybatis.cfg.XMLConfigBuilder;
import application.mybatis.sqlsession.defaults.DefaultSqlSessionFactory;

import java.io.InputStream;

/**
 * 用于创建一个SqlSessionFactory对象
 */
public class SqlSessionFactoryBuilder {

    /**
     * 根据参数的字节输入流来构建一个SqlSessionFactory工厂
     */
    public SqlSessionFactory build(InputStream config) {
        MybatisConfiguration cfg = XMLConfigBuilder.loadConfiguration(config);
        return new DefaultSqlSessionFactory(cfg);
    }
}
