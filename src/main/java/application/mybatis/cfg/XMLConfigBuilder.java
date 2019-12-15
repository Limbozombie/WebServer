package application.mybatis.cfg;


import application.mybatis.annotations.Select;
import application.utils.Resources;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * 用于解析配置文件
 */
public class XMLConfigBuilder {

	/**
	 * 解析主配置文件，把里面的内容填充到DefaultSqlSession所需要的地方
	 * 使用的技术：
	 * dom4j+xpath
	 */
	public static MybatisConfiguration loadConfiguration(InputStream config) {
		try {
			//定义封装连接信息的配置对象（mybatis的配置对象）
			MybatisConfiguration cfg = new MybatisConfiguration();

			//1.获取SAXReader对象
			SAXReader reader = new SAXReader();
			//2.根据字节输入流获取Document对象
			Document document = reader.read(config);
			//3.获取根节点
			Element root = document.getRootElement();
			//4.使用xpath中选择指定节点的方式，获取所有property节点
			Map<String, String> properties = root.selectNodes("//property").stream()
					.collect(toMap(node -> ((Element) node).attributeValue("name"),
							node -> ((Element) node).attributeValue("value")));
			//5.设置MybatisConfiguration
			cfg.setDriver(properties.get("driver"));
			cfg.setUsername(properties.get("username"));
			cfg.setUrl(properties.get("url"));
			cfg.setPassword(properties.get("password"));
			//5.遍历节点
			//取出mappers中的所有mapper标签，判断他们使用了resource还是class属性
			List<Element> mapperElements = ((Element) root.selectSingleNode("//mappers")).elements("mapper");
			//遍历集合
			for (Element mapperElement : mapperElements) {
				//判断mapperElement使用的是哪个属性
				Attribute attribute = mapperElement.attribute("resource");
				if (attribute != null) {
					//有resource属性，用的是XML
					System.out.println("使用的是XML");
					//获取属性的值"com/itheima/dao/IUserDao.xml"
					String mapperPath = attribute.getValue();
					//把映射配置文件的内容获取出来，封装成一个map
					Map<String, Mapper> mappers = loadMapperConfiguration(mapperPath);
					//给configuration中的mappers赋值
					cfg.addMappers(mappers);
				} else {
					System.out.println("使用的是注解");
					//表示没有resource属性，用的是注解
					//获取class属性的值
					String daoClassPath = mapperElement.attributeValue("class");
					//根据daoClassPath获取封装的必要信息
					Map<String, Mapper> mappers = loadMapperAnnotation(daoClassPath);
					//给configuration中的mappers赋值
					cfg.addMappers(mappers);
				}
			}
			return cfg;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				config.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 根据传入的参数，解析XML，并且封装到Map中
	 *
	 * @param mapperPath 映射配置文件的位置
	 * @return map中包含了获取的唯一标识（key是由dao的全限定类名和方法名组成）
	 * 以及执行所需的必要信息（value是一个Mapper对象，里面存放的是执行的SQL语句和要封装的实体类全限定类名）
	 */
	private static Map<String, Mapper> loadMapperConfiguration(String mapperPath) {
		Map<String, Mapper> mappers = new HashMap<>();
		try {
			Element root = Resources.getRootElement(mapperPath);
			//dao全类名
			String namespace = root.attributeValue("namespace");
			//5.获取所有的select节点
			List<Node> selectNodes = root.selectNodes("//select");
			//6.遍历select节点集合
			for (Node selectNode : selectNodes) {
				Element node = (Element) selectNode;
				//方法名
				String methodName = node.attributeValue("id");
				//返回值类型
				String resultType = node.attributeValue("resultType");
				//查询语句
				String querySql = node.getText();
				//创建Key
				String key = namespace + "." + methodName;
				//创建Value
				Mapper mapper = new Mapper();
				mapper.setQueryString(querySql);
				mapper.setResultType(resultType);
				//把key和value存入mappers中
				mappers.put(key, mapper);
			}
			return mappers;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据传入的参数，得到dao中所有被select注解标注的方法。
	 * 根据方法名称和类名，以及方法上注解value属性的值，组成Mapper的必要信息
	 */
	private static Map<String, Mapper> loadMapperAnnotation(String daoClassPath) throws Exception {
		//定义返回值对象
		Map<String, Mapper> mappers = new HashMap<>();

		//1.得到dao接口的字节码对象
		Class daoClass = Class.forName(daoClassPath);
		//2.得到dao接口中的方法数组
		Method[] methods = daoClass.getMethods();
		//3.遍历Method数组
		for (Method method : methods) {
			//取出每一个方法，判断是否有select注解
			boolean isAnnotated = method.isAnnotationPresent(Select.class);
			if (isAnnotated) {
				//创建Mapper对象
				Mapper mapper = new Mapper();
				//取出注解的value属性值
				Select selectAnno = method.getAnnotation(Select.class);
				String queryString = selectAnno.value();
				mapper.setQueryString(queryString);
				//获取当前方法的返回值，还要求必须带有泛型信息
				Type type = method.getGenericReturnType();
				//判断type是不是参数化的类型
				if (type instanceof ParameterizedType) {
					//强转
					ParameterizedType ptype = (ParameterizedType) type;
					//得到参数化类型中的实际类型参数
					Type[] types = ptype.getActualTypeArguments();
					//取出第一个
					Class domainClass = (Class) types[0];
					//获取domainClass的类名
					String resultType = domainClass.getName();
					//给Mapper赋值
					mapper.setResultType(resultType);
				}
				//组装key的信息
				//获取方法的名称
				String methodName = method.getName();
				String className = method.getDeclaringClass().getName();
				String key = className + "." + methodName;
				//给map赋值
				mappers.put(key, mapper);
			}
		}
		return mappers;
	}


}
