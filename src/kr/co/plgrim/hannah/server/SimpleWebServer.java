package kr.co.plgrim.hannah.server;

import kr.co.plgrim.hannah.utils.Closer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 간단한 웹 서버를 구현하는 클래스.
 *  <pre>
 * 이 클래스는 지정된 호스트 이름과 포트 번호를 사용하여 서버 소켓을 생성하고,
 * 클라이언트의 연결 요청을 수락하여 각 요청을 처리하는 스레드를 생성한다.
 * </pre>
 * <p>
 * @author hannah <br/>
 * @data 2024.07.04
 * </p>
 */
public class SimpleWebServer {

	private static final String HOST_NAME = "localhost";
	private static final int DEFAULT_PORT = 8080;

	/**
	 * 웹 서버를 시작하고 클라이언트 연결을 대기하는 메서드
	 * <pre>
	 * 이 메서드는 지정된 호스트 이름과 포트 번호를 사용하여 서버 소켓을 생성하고 바인딩한다.
	 * 클라이언트의 연결 요청을 수락하고,각 연결에 대해 새로운 ServerThread 생성하여 요청을 처리한다.
	 * </pre>
	 *
	 */
	public void start() {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(HOST_NAME, DEFAULT_PORT));

			System.out.println("[연결 대기중]");
			Socket connectionSocket;
			while ((connectionSocket = serverSocket.accept()) != null) { // 순환을 돌면서 클라이언트의 접속을 받는다.

				InetSocketAddress isa = (InetSocketAddress) connectionSocket.getRemoteSocketAddress();
				System.out.println("[연결 수락] : " + isa.getHostName());

				ServerThread serverThread = new ServerThread(connectionSocket);
				serverThread.start();
			}

		} catch (IOException e) {
			throw new RuntimeException();
		} finally {
			Closer.close(serverSocket);
		}
	}
}
