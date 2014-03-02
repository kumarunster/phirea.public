import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.logging.Logger;

import com.jetdrone.vertx.yoke.Middleware;
import com.jetdrone.vertx.yoke.middleware.YokeRequest;


public class Translation extends Middleware {

	protected String fileName;
	
	protected Properties translationProperties;
	
	public Translation(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	@Override
	public Middleware init(Vertx vertx, Logger logger, String mount) {
		super.init(vertx, logger, mount);
		

		String sProperties = vertx.fileSystem().readFileSync(fileName).toString();
		
		translationProperties = new Properties();
		
		try {
			translationProperties.load(new StringReader(sProperties));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return this;
	}



	@Override
	public void handle(YokeRequest request, Handler<Object> next) {
		request.put("msg", translationProperties);
		
		next.handle(null);
	}

	
}
