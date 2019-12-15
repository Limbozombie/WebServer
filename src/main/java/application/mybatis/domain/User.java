package application.mybatis.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

	private static final long serialVersionUID = 952838532027894588L;
	private Integer id;
	private String username;
	private Date birthday;
	private String sex;
	private String address;

}
