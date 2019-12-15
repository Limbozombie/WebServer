package application.mybatis.sqlsession.defaults;


import application.mybatis.cfg.MybatisConfiguration;
import application.mybatis.sqlsession.SqlSession;
import application.mybatis.sqlsession.SqlSessionFactory;

/**
 * SqlSessionFactory接口的实现类
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private MybatisConfiguration cfg;

    public DefaultSqlSessionFactory(MybatisConfiguration cfg) {
        this.cfg = cfg;
    }

    /**
     * 用于创建一个新的操作数据库对象
     */
    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(cfg);
    }
}
