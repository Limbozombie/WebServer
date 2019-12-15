package application.mybatis.cfg;

import lombok.Data;

/**
 * 用于封装执行的SQL语句和结果类型的全限定类名
 */
@Data
public class Mapper {

    //SQL
    private String queryString;
    //实体类的全限定类名
    private String resultType;

}
