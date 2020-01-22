package utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesRead {
	private Properties inputProperties;

	public PropertiesRead(String fileName) {
		inputProperties = new Properties();

		try {
			inputProperties.load(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String key){
		
		if (inputProperties.getProperty(key)!=null)
			return inputProperties.getProperty(key).trim();
		else 
			return null;
	}


}

