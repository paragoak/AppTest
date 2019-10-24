package qa.apptest.utilities;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({ "classpath:${env}.properties" })
public interface EnvironmentConfig extends Config {

	String appURL();

	String browser();
	
	String testdataPath();

}
