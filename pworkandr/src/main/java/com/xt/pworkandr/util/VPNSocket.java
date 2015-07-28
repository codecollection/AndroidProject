package com.xt.pworkandr.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class VPNSocket {
	
	private String ip = null;
	private int port = 0;
	private Socket conn = null;
	private InputStream sin = null; // 网络输入流
	private OutputStream sou = null;// 网络输出流

	public VPNSocket(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public int connect() {
		try {
			conn = new Socket(ip, port);

			sin = conn.getInputStream();
			sou = conn.getOutputStream();
			return 0;
		} catch (Exception e) {
			e.getMessage();
			return -1;
		}
	}

	public int sendData(String data) {
		try {
			sou.write(data.getBytes());
			sou.flush();
			sin.reset();
			return 0;
		} catch (Exception e) {
			return -1;
		}
	}

	public int recvData(byte[] data) {
		try {
			// 新版客户端使用代码
			int dataLen = 0;
			int currentLen = 0;

			byte[] len = new byte[4];// 头四个字节是数据长度 + 后面数据内容

			int headlen = sin.read(len);
			while (headlen < 4) {
				headlen += sin.read(len, headlen, 4 - headlen);
			}
			dataLen = Integer.parseInt(new String(len));

			while (currentLen < dataLen) {
				currentLen += sin.read(data, currentLen, dataLen - currentLen);
			}

			/*
			 * 老版客户端使用代码 dataLen = sin.read(data);
			 */
			return dataLen;
		} catch (IOException e) {
			return -1;
		}
	}

	public void close() {
		try {
			sin.close();
			sou.close();
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
