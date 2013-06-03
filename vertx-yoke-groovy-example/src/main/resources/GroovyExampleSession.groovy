import javax.crypto.Mac

import com.jetdrone.vertx.yoke.GYoke
import com.jetdrone.vertx.yoke.engine.GroovyTemplateEngine
import com.jetdrone.vertx.yoke.middleware.*
import com.jetdrone.vertx.yoke.util.Utils





def checkAndSetSessionId = { YokeRequest request ->
	if(request.getSessionId() == null) {
		id = UUID.randomUUID().toString();
		request.setSessionId(id);
		println "session id does not exist, created new: '${id}'"
	}
}


Mac mac = Utils.newHmacSHA256("SecretPassword");

new GYoke(vertx)
  .engine('html', new GroovyTemplateEngine())
  
  .use(new CookieParser(mac))
  .use(new Session(mac))
    
  .use(new ErrorHandler(true))
  .use(new GRouter()
	  .get("/", { YokeRequest request -> 
		  checkAndSetSessionId(request);
		  request.response.redirect "/index"
	  })
	  .get("/index", { YokeRequest request, next ->
		  checkAndSetSessionId(request);
		  request.response.end "my session id is ${request.getSessionId()}"
	  })
	  .get("/sForm", { YokeRequest request ->
		  checkAndSetSessionId(request);
		  request.response.end "my session id is ${request.getSessionId()}"
	  })
  )
  .listen(8080)
