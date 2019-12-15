package application.mybatis.cfg;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义mybatis的配置类
 */
@Data
public class MybatisConfiguration {

    private String driver;
    private String url;
    private String username;
    private String password;

    private Map<String, Mapper> mappers = new HashMap<>();

    public void addMappers(Map<String, Mapper> mappers) {
        //此处需要使用追加的方式
        this.mappers.putAll(mappers);
    }
}
