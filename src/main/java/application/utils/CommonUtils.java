package application.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 271636872@qq.com
 * @since 2020/9/5 9:03
 */
public class CommonUtils {
	public static String read(InputStream input) throws IOException {

		StringBuilder stringBuilder = new StringBuilder();

		int ch;
		while ((ch = input.read()) != -1) {
			stringBuilder.append((char) ch);
		}

		return stringBuilder.toString();
	}
}
