package kr.co.plgrim.hannah.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * 클래스명: <code>ContentTypeUtils</code>
 * <pre>
 * 컨텐츠타입따위를 관리하기위한 유틸
 * </pre>
 *
 * @author 이경연
 * @date 2010. 7. 6.
 */
public class ContentTypeUtils {

	/**
	 * 컨텐츠 타입을 저장하는 xml
	 */
	private final static String CONFIG_FILE_NAME = "ext2mimetype.xml";

	/**
	 * 데이터 properties
	 */
	private final Properties properties = new Properties();

	/**
	 * 싱글톤 instance
	 */
	private static ContentTypeUtils instance;

	private final static Object o = new Object();

	private final static char point = '.';

	/**
	 * 기본컨텐츠 타입
	 */
	private final static String DEFAULT_CONTENT_TYPE = "application/octet-stream";

	/**
	 * 컨텐츠 타입로더
	 *
	 * @throws IOException
	 */
	private ContentTypeUtils() throws IOException {
		this.properties.loadFromXML(this.getClass().getResourceAsStream(CONFIG_FILE_NAME));
	}

	/**
	 * <pre>
	 * 싱글톤 인스턴스 리턴
	 * </pre>
	 *
	 * @return
	 */
	static ContentTypeUtils getInstance() {
		if (instance == null) {
			synchronized (o) {
				try {
					instance = new ContentTypeUtils();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return instance;
	}

	/**
	 * <pre>
	 * 확장자에 해당하는 컨텐츠 타입리턴
	 * </pre>
	 *
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return this.properties.getProperty(key);
	}

	/**
	 * <pre>
	 * 파일명에 해당하는 컨텐츠 타입리턴
	 * </pre>
	 *
	 * @param fileName
	 * @return
	 */
	public static String getContentType(String fileName) {
		if (fileName == null || "".equals(fileName)) {
			return DEFAULT_CONTENT_TYPE;
		}

		int index = fileName.lastIndexOf(point);
		String extension = fileName.substring(index + 1);
		String contentType = getInstance().getString(extension.toLowerCase());
		return contentType == null ? DEFAULT_CONTENT_TYPE : contentType;
	}

	public static void main(String[] args) {
		System.out.println(ContentTypeUtils.getContentType("btn_cancel.GIF"));
	}
}
