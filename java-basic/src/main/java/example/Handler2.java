package example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.management.RuntimeErrorException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// Handler value: example.Handler
public class Handler2 implements RequestHandler<WeatherData, WeatherData>{
  Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
private WeatherData wd;
  @Override
  public WeatherData handleRequest(WeatherData event, Context context)
  {
	  LambdaLogger logger = context.getLogger();
	try {  
		BufferedReader br = new BufferedReader(
			     new FileReader("/mnt/lambda/json.txt"));
			   
			    //convert the json string back to object
		String obj = gson.toJson(wd,WeatherData.class);
		logger.log("Json from last run is" + obj);
		
		
    String response = "200 OK";
    // log execution details
    logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.log("CONTEXT: " + gson.toJson(context));
    // process event
    logger.log("EVENT: " + gson.toJson(event));
    logger.log("EVENT TYPE: " + event.getClass());
    
    wd= new WeatherData();
    wd.setHumidityPct(10.2);
    wd.setPressureHPa(1000);
    wd.setTemperatureK(event.getTemperatureK());
    wd.setWindKmh(76);
    
    logger.log("throwing an exception");
    int i =0;
    if(i==1) {
    throw new CustomException("custom exception");
    }
    
	}catch(FileNotFoundException e) {
		logger.log("");
	}
	catch(Exception e) {
		logger.log("");
		//throw e;
	}
	logger.log("RESULT: " + gson.toJson(wd,WeatherData.class));
	FileWriter file = null;
	try {
		file = new FileWriter("/mnt/lambda/json.txt");
		file.write(gson.toJson(wd,WeatherData.class));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		logger.log(e.getMessage());
		throw new RuntimeErrorException(new Error(e));
	}finally {
		try {
			if(file!=null) file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    return wd;
  }
}