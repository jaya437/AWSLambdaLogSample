package example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import example.s3.S3FetchObjects;

// Handler value: example.Handler
public class Handler implements RequestHandler<WeatherData, WeatherData> {
	private static final Logger logger1 = LoggerFactory.getLogger(Handler.class);
	
	Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	private WeatherData wd;

	public Handler() {
		
		logger1.info("looging from constructor - wantedly failing the function");
		logger1.info("will throuh a custom exception");
		
		//throw new CustomException("Constructor exception");
	}

	@Override
	public WeatherData handleRequest(WeatherData event, Context context) {
		LambdaLogger logger = context.getLogger();

		try {
			logger1.debug("debug string");
			logger1.info("logger info string");
			logger1.error("logger error string");
			logger1.trace("logger trace string");
			
			if(event.getTemperatureK() < 10) throw new CustomException("too cold exception");
			
//		BufferedReader br1 = new BufferedReader(
//			     new FileReader("/mnt/lambda/json.txt"));
//			   
//			    //convert the json string back to object
//		String obj = gson.toJson(br1,WeatherData.class);
//		logger.log("Json from last run is" + obj);

			// read file form S3
			System.out.println("reading file from S3");
			S3FetchObjects.s3GetObject();

			logger.log("user home is " + System.getProperty("user.home"));
			// runCommand(logger);

			System.out.println("some print statement");
			String response = "200 OK";
			// log execution details
			// logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
			// logger.log("CONTEXT: " + gson.toJson(context));
			// process event
			// logger.log("EVENT: " + gson.toJson(event));

			wd = new WeatherData();
			wd.setHumidityPct(10.2);
			wd.setPressureHPa(1000);
			wd.setTemperatureK(event.getTemperatureK());
			wd.setWindKmh(76);

			logger.log("throwing an exception");
			int i = 0;
			if (i == 1) {
				throw new CustomException("custom exception");
			}

			System.out.println("reading file from ec2 via sftp");
			SFTPFileTransfer.getFile();

		} catch (CustomException e) {
			logger.log("error in functionality");
			logger.log(e.getMessage());
			throw e;
		}catch (Exception e) {
			logger.log("error in functionality");
			logger.log(e.getMessage());
			//throw e;
		}
		logger.log("RESULT: " + gson.toJson(wd, WeatherData.class));

//	FileWriter file = null;
//	try {
//		file = new FileWriter("/mnt/lambda/json.txt");
//		file.write(gson.toJson(wd,WeatherData.class));
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		logger.log(e.getMessage());
//		throw new RuntimeErrorException(new Error(e));
//	}finally {
//		try {
//			if(file!=null) file.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

		return wd;
	}

	private void runCommand(LambdaLogger logger) {
		System.out.println("initiating command line execution");
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		ProcessBuilder builder = new ProcessBuilder();
		Map<String, String> environment = builder.environment();
		environment.forEach((key, value) -> System.out.println(key + value));
		builder.directory(new File("/tmp"));
		if (isWindows) {
			builder.command("cmd.exe", "/c", "dir");
		} else {
			System.out.println("is windows flag is " + isWindows);
			builder.command("sh", "-c", "/tmp/ssh-keyscan ec2-3-82-204-86.compute-1.amazonaws.com > known_hosts1");
			// ssh-keyscan ec2-3-82-204-86.compute-1.amazonaws.com > known_hosts
		}
		// builder.directory(new File(System.getProperty("user.home")));

		Process process = null;
		try {
			System.out.println("running process using runtime");
			// process= Runtime.getRuntime().exec("/tmp/ssh-keyscan
			// ec2-3-89-110-14.compute-1.amazonaws.com");
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
		logger.log("command is executed and exit code is " + exitCode);
		;

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