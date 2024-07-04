package kr.co.plgrim.hannah.server;

import kr.co.plgrim.hannah.utils.Closer;
import kr.co.plgrim.hannah.utils.ContentTypeUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * HTTP 요청을 처리하는 스레드를 나타내는 클래스.
 * <pre>
 * 이 클래스는 클라이언트로부터의 HTTP GET 요청을 처리하고,요청된 파일을 클라이언트에게 전송한다.
 * 요청된 파일이 존재하지 않거나 잘못된 요청인 경우, 적절한 HTTP 오류 응답을 반환한다.
 * </pre>
 * @author hannah
 * @data  2024.07.04
 */
public class ServerThread extends Thread {
	private final Socket connectionSocket;

	private static final String DEFAULT_DIRECTORY = "C:\\server\\";

	private static final String REQUEST_METHOD = "GET";

	private static final String SLASH = "/";
	private static final String HTTP_PROTOCOL = "HTTP/1.1";

	/**
	 * <pre>
	 *    ServerThread 생성자
	 * </pre>
	 * @param connectionSocket Socket
	 */
	public ServerThread(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	/**
	 * <pre>
	 *  클라이언트로부터 HTTP 요청을 처리하는 스레드의 실행 메서드
	 * <p>
	 * 이 메서드는 요청 헤더를 한 글자씩 읽어와 헤더의 끝을 인식하고,
	 * 요청된 파일을 찾은 후 해당 파일을 클라이언트에 전송한다.
	 * 요청 파일이 없거나 잘못된 요청일 경우 적절한 HTTP 오류 응답을 반환한다.
	 * </pre>
	 */
	@Override
	public void run() {
		System.out.println("New Client Connect>>>>>>");

		InputStream in = null;
		OutputStream out = null;

		try {

			in = connectionSocket.getInputStream();    // 클라이언트의 요청 데이터
			out = connectionSocket.getOutputStream(); // 클라이언트에 대한 응답 데이터

			InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
			StringBuilder requestHeader = new StringBuilder();

			int ch;
			int state = 0;  // (0 : 초기, 1 : /r(13) ,  2 : /n(10), 3 : /r(13, 두번째) )
			boolean isHeaderEnd = false;

			while ((ch = isr.read()) != -1) {
				requestHeader.append((char) ch); // 현자 문자를 전체 요청 헤더에 추가
				switch (state) {
					case 0:
						if (ch == 13) {
							state = 1;
						}
						break;
					case 1:
						if (ch == 10) {
							state = 2;
						} else {
							state = 0; // /r(13) 다음 글자가 /n(10)이 아니라면
						}
						break;
					case 2:
						if (ch == 13) {
							state = 3;
						} else {
							state = 0; // /n(10) 다음 글자가 두번째 /r(13)이 아니라면
						}
						break;
					case 3:
						if (ch == 10) {
							isHeaderEnd = true;  // 두 번째 /n(10)을 만났을 때 파싱 종료를 위한 flag 변경
						} else {
							state = 0;
						}
						break;
				}

				if (isHeaderEnd) {
					break;
				}
			}

			System.out.println("Request Header>>");
			System.out.println(requestHeader);

			String fileName = "";

			if (!requestHeader.toString().startsWith(REQUEST_METHOD)) {
				sendErrorResponse(out, HttpStatus.BAD_REQUEST);
			} else { // 요청 헤더의 첫번째 줄에서 파일 이름 추출
				int reqMethodIdx = requestHeader.toString().indexOf(SLASH);
				int protocolIdx = requestHeader.toString().indexOf(HTTP_PROTOCOL);
				fileName = requestHeader.toString().substring(reqMethodIdx, protocolIdx).trim();
			}

			StringBuilder httpResponse = new StringBuilder();
			File file = new File(DEFAULT_DIRECTORY, fileName);

			if (!file.exists()) {
				throw new FileNotFoundException();
			} else {
				String contentType = ContentTypeUtils.getContentType(fileName);
				httpResponse.append("HTTP/1.1 ").append(HttpStatus.OK.getCode()).append(" ").append(HttpStatus.OK.getReason()).append("\r\n");
				httpResponse.append("Content-Length: ").append(file.length()).append("\r\n");
				httpResponse.append("Content-type: ").append(contentType);
				httpResponse.append("\r\n\r\n");

				System.out.println("fileName> " + fileName);
				System.out.println("Response Header>>");
				System.out.println(httpResponse);

				out.write(httpResponse.toString().getBytes(StandardCharsets.UTF_8));
				Files.copy(file.toPath(), out);
				out.flush();
			}

		} catch (FileNotFoundException e) {
			sendErrorResponse(out, HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			sendErrorResponse(out, HttpStatus.BAD_REQUEST);

		} finally {
			Closer.close(in, out, connectionSocket);
			System.out.println("Resource Close >>>>");
		}
	}

	/**
	 * HTTP 헤더의 응답 에러 메세지를 출력한다.
	 * @param out 출력 스트림
	 * @param status HttP 상태 코드와 이유를 나타내는 열거형
	 */
	private void sendErrorResponse(OutputStream out, HttpStatus status) {

		StringBuilder errorResponse = new StringBuilder();
		errorResponse.append("HTTP/1.1 ").append(status.getCode()).append(" ").append(status.getReason()).append("\r\n\r\n");
		try {
			out.write(errorResponse.toString().getBytes(StandardCharsets.UTF_8));
			out.flush();
		} catch (IOException ioException) {
			throw new RuntimeException();
		}
		System.out.println("Response Header>");
		System.out.println(errorResponse);
	}

	/**
	 * HTTP 상태 코드를 나타내는 열거형.
	 */
	enum HttpStatus {
		OK(200, "OK"),
		BAD_REQUEST(400, "Bad Request"),
		NOT_FOUND(404, "Not Found");

		private final int code;
		private final String reason;

		HttpStatus(int code, String reason) {
			this.code = code;
			this.reason = reason;
		}

		public int getCode() {
			return code;
		}

		public String getReason() {
			return reason;
		}
	}



}

