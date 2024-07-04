package kr.co.plgrim.hannah.server;

/**
 * 웹 서버를 시작하는 클래스
 * <p>
 * 이 클래스는 웹 서버를 초기화하고 시작하는 역할을 한다.
 * </p>
 */
public class ServerMain {

	public static void main(String [] args) {
		SimpleWebServer webServer = new SimpleWebServer();
		webServer.start();
	}
}
