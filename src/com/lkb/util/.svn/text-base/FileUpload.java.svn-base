package com.lkb.util;

import java.io.IOException;
import java.util.Arrays;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SFTPv3Client;

/*
 * 文件上传到远程服务器
 * */
public class FileUpload {
	String ip = InfoUtil.getInstance().getInfo("server/server",
			"ip");
	int port = Integer.parseInt(InfoUtil.getInstance().getInfo("server/server",
			"port")); 
	String username = InfoUtil.getInstance().getInfo("server/server",
			"username");
	String password = InfoUtil.getInstance().getInfo("server/server",
			"password");
	
	
	
	
	public void getFile(String remoteDir, String localDir) {
//		String[] remoteFiles = { remoteDir };
//		getFiles(remoteFiles, localDir);
		createDir(remoteDir);	
		putFile(localDir, remoteDir);
			
	}
	

	/**
	 * @param remoteFiles
	 * @param localTargetDirectory
	 */
	public void getFiles(String[] remoteFiles, String localTargetDirectory) {
		Connection conn = new Connection(ip, port);
		try {
			conn.connect();
			authenticateWithPassword(conn);
			SCPClient client = new SCPClient(conn);
			client.get(remoteFiles, localTargetDirectory);
		} catch (IOException ioe) {
			String errorMessage = new StringBuffer("Error to get files ")
					.append(Arrays.toString(remoteFiles))
					.append(" from server[").append(ip).append(":")
					.append(port).append("] to local directory ")
					.append(localTargetDirectory).toString();
		
		} finally {
			conn.close();
		}
	}

	public void putFile(String localFile, String remoteTargetDirectory) {
		String[] localFiles = { localFile };
		putFiles(localFiles, remoteTargetDirectory);
	}

	/**
	 * copy files from local file system to remote sever.
	 * 
	 * @param localFiles
	 * @param remoteTargetDirectory
	 */
	public void putFiles(String[] localFiles, String remoteTargetDirectory) {
		Connection conn = new Connection(ip, port);
		try {
			conn.connect();
			authenticateWithPassword(conn);
			SCPClient client = new SCPClient(conn);
			client.put(localFiles, remoteTargetDirectory, "0777");
		} catch (IOException ioe) {
			String errorMessage = new StringBuffer("Error to put files ")
					.append(Arrays.toString(localFiles)).append(" to server[")
					.append(ip).append(":").append(port)
					.append("] in directory ").append(remoteTargetDirectory)
					.toString();
			
			
		} finally {
			conn.close();
		}
	}

	public void createDir(String createDir) {
		Connection conn = new Connection(ip, port);
		try {
			conn.connect();
			authenticateWithPassword(conn);
			SFTPv3Client sftpClient = new SFTPv3Client(conn);
			sftpClient.mkdir(createDir, 0777);
		} catch (IOException ioe) {
			String errorMessage = new StringBuffer("Error to create directory ")
					.append(createDir).toString();
		
		} finally {
			conn.close();
		}

	}

	private void authenticateWithPassword(final Connection conn)
			throws IOException {
		boolean authenticateSuccessful = conn.authenticateWithPassword(
				username, password);
		if (!authenticateSuccessful) {
			String errorMessage = getAuthenticateErrorMessage();
			
		}
	}

	private String getAuthenticateErrorMessage() {
		return new StringBuffer("Error to connect to sever[").append(ip)
				.append(":").append(port).append("] with username:")
				.append(username).append(" and password:").append(password)
				.toString();
	}

	
}
