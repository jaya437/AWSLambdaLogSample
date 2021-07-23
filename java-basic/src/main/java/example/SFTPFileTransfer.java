package example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPFileTransfer {

	private static final String REMOTE_HOST = "ec2-3-82-204-86.compute-1.amazonaws.com";
	private static final String USERNAME = "ec2-user";
	private static final String PASSWORD = "";
	private static final int REMOTE_PORT = 22;
	private static final int SESSION_TIMEOUT = 10000;
	private static final int CHANNEL_TIMEOUT = 5000;

	public static void main(String[] args) {
		runCommand();
		getFile1();
	}

	public static void getFile1() {

		String localFile = "/home/ec2-user/test.txt";
		String remoteFile = "/home/ec2-user/test.txt";

		Session jschSession = null;

		try {

			JSch jsch = new JSch();
//	            String knownHostPublicKey = "ec2-3-89-110-14.compute-1.amazonaws.com ecdsa-sha2-nistp256 AAAAB3NzaC1yc2EAAAADAQABAAABAQDSucyVTh/JGnhT4Kow5aUiWocsA3uPscKVqimhtEI5yQqyi8lDQvBLATyIp4DwhclZl1dDMTK/IBDeDIeCF9WnRGsbK3eyRxGg4hetqU5gTb3HCwRcLSGPG\r\n" + 
//	            		"wxjEUY00x3ry3uKEF/hFOeSrVxw5Es5MwiSW+1/zCCz6MbJtk0hHqYyVkWHzI/efAFByYJmkj8JkKiJ5UxPrZz/kizKInsjEY+LWtURS4A3qt6/KQ20bcEsJXcl4auam+4LbcJU9qjLx7nz0UuixF5zH+iDyM\r\n" + 
//	            		"c+TwYVLeqP/z5hvr9rDwt/fLvzc5ltArHehJx11AYP1ob8UnLdKfjj0gkcKTLWFIst";
//	            //Util.fromBase64(Util.str2byte(knownHostPublicKey), 0, knownHostPublicKey.length());
//	            jsch.setKnownHosts(new ByteArrayInputStream(knownHostPublicKey.getBytes()));
			// jsch.setKnownHosts("/home/mkyong/.ssh/known_hosts");
			jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
			java.util.Properties config = new java.util.Properties();
			//config.put("StrictHostKeyChecking", "no");
			jschSession.setConfig(config);
			// authenticate using private key
			jsch.addIdentity("/home/ec2-user/linux.pem");
			
			jsch.setKnownHosts("/home/ec2-user/known_hosts");

			// authenticate using password
			// jschSession.setPassword(PASSWORD);

			// String fingerPrint = jschSession.getHostKey().getFingerPrint(jsch);
			// System.out.println("finger print is "+fingerPrint);

			// 10 seconds session timeout
			jschSession.connect(SESSION_TIMEOUT);

			Channel sftp = jschSession.openChannel("sftp");

			// 5 seconds timeout
			sftp.connect(CHANNEL_TIMEOUT);

			ChannelSftp channelSftp = (ChannelSftp) sftp;

			// transfer file from local to remote server
			// channelSftp.put(localFile, remoteFile);

			// download file from remote server to local
			channelSftp.get(remoteFile, localFile);
			Stream<String> lines;
			try {
				lines = Files.lines(Paths.get("/home/ec2-user/test.txt"));
				lines.forEach(System.out::println);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// does not preserve order

			channelSftp.exit();

		} catch (JSchException | SftpException e) {

			e.printStackTrace();

		} finally {
			if (jschSession != null) {
				jschSession.disconnect();
			}
		}

		System.out.println("Done");

	}
	
	public static void getFile() {

		String localFile = "/tmp/test.txt";
		String remoteFile = "/home/ec2-user/test.txt";

		Session jschSession = null;

		try {

			JSch jsch = new JSch();
//	            String knownHostPublicKey = "ec2-3-89-110-14.compute-1.amazonaws.com ecdsa-sha2-nistp256 AAAAB3NzaC1yc2EAAAADAQABAAABAQDSucyVTh/JGnhT4Kow5aUiWocsA3uPscKVqimhtEI5yQqyi8lDQvBLATyIp4DwhclZl1dDMTK/IBDeDIeCF9WnRGsbK3eyRxGg4hetqU5gTb3HCwRcLSGPG\r\n" + 
//	            		"wxjEUY00x3ry3uKEF/hFOeSrVxw5Es5MwiSW+1/zCCz6MbJtk0hHqYyVkWHzI/efAFByYJmkj8JkKiJ5UxPrZz/kizKInsjEY+LWtURS4A3qt6/KQ20bcEsJXcl4auam+4LbcJU9qjLx7nz0UuixF5zH+iDyM\r\n" + 
//	            		"c+TwYVLeqP/z5hvr9rDwt/fLvzc5ltArHehJx11AYP1ob8UnLdKfjj0gkcKTLWFIst";
//	            //Util.fromBase64(Util.str2byte(knownHostPublicKey), 0, knownHostPublicKey.length());
//	            jsch.setKnownHosts(new ByteArrayInputStream(knownHostPublicKey.getBytes()));
			// jsch.setKnownHosts("/home/mkyong/.ssh/known_hosts");
			jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
			java.util.Properties config = new java.util.Properties();
			//config.put("StrictHostKeyChecking", "no");
			jschSession.setConfig(config);
			// authenticate using private key
			jsch.addIdentity("/tmp/linux.pem");
			
			jsch.setKnownHosts("/tmp/known_hosts");

			// authenticate using password
			// jschSession.setPassword(PASSWORD);

			// String fingerPrint = jschSession.getHostKey().getFingerPrint(jsch);
			// System.out.println("finger print is "+fingerPrint);

			// 10 seconds session timeout
			jschSession.connect(SESSION_TIMEOUT);

			Channel sftp = jschSession.openChannel("sftp");

			// 5 seconds timeout
			sftp.connect(CHANNEL_TIMEOUT);

			ChannelSftp channelSftp = (ChannelSftp) sftp;

			// transfer file from local to remote server
			// channelSftp.put(localFile, remoteFile);

			// download file from remote server to local
			channelSftp.get(remoteFile, localFile);
			Stream<String> lines;
			try {
				lines = Files.lines(Paths.get("/tmp/test.txt"));
				lines.forEach(System.out::println);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// does not preserve order

			channelSftp.exit();

		} catch (JSchException | SftpException e) {

			e.printStackTrace();

		} finally {
			if (jschSession != null) {
				jschSession.disconnect();
			}
		}

		System.out.println("Done");

	}

	private static void runCommand() {
		System.out.println("initiating command line execution");
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		ProcessBuilder builder = new ProcessBuilder();
		Map<String, String> environment = builder.environment();
		environment.forEach((key, value) -> System.out.println(key+ ">>" + value));
		builder.directory(new File("/home/ec2-user"));
		if (isWindows) {
			builder.command("cmd.exe", "/c", "dir");
		} else {
			System.out.println("is windows flag is " + isWindows);
			builder.command("sh","-c","ssh-keyscan ec2-3-82-204-86.compute-1.amazonaws.com > known_hosts");
			///usr/bin/ssh-keyscan ec2-3-82-204-86.compute-1.amazonaws.com > known_hosts
		}
		// builder.directory(new File(System.getProperty("user.home")));

		Process process = null;
		try {
			System.out.println("running process using runtime");
			//process = Runtime.getRuntime().exec("ssh-keyscan ec2-3-82-204-86.compute-1.amazonaws.com > known_hosts");
			 process = builder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
		Executors.newSingleThreadExecutor().submit(streamGobbler);
		int exitCode = 0;
		try {
			exitCode = process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("command is executed and exit code is " + exitCode);

	}

	private static class StreamGobbler implements Runnable {
		private InputStream inputStream;
		private Consumer<String> consumer;

		public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
			this.inputStream = inputStream;
			this.consumer = consumer;
		}

		@Override
		public void run() {
			System.out.println("command output");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			bufferedReader.lines().forEach(consumer);
			bufferedReader.lines().forEach(System.out::println);
		}
	}
}
