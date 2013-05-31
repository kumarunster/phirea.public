import javax.crypto.Mac;

import com.jetdrone.vertx.yoke.middleware.*
import com.jetdrone.vertx.yoke.util.Utils;
import com.jetdrone.vertx.yoke.GYoke
import com.jetdrone.vertx.yoke.engine.GroovyTemplateEngine
import template.GSPTemplateEngine

	
Mac secret = Utils.newHmacSHA256("MySecret");

new GYoke(vertx)
//  .engine('html', new GSPTemplateEngine())
  .engine('html', new GroovyTemplateEngine())
  .use(new CookieParser(secret))
  .use(new Session(secret))
  .use(new ErrorHandler(true))
//  .use("/web", {request, next -> staticHandler.handle(request, next)})
  .use("/static", new Static(".", 0, false, false))
  .use(new GRouter()
    .get("/hello", { request ->
        request.response.end "Hello World + dyn change!"
    }))

//  .use {request ->
//    request.response.end "maybe you should go to /hello or /template!"
//  }
  .use(new GRouter()
	  .get("/", { YokeRequest request -> 
		  if(request.getSessionId() == null)
		  	request.setSessionId(UUID.randomUUID().toString());
			  
		  request.response.redirect ( "/index"); 
	  })
      .get("/index", { YokeRequest request , next ->
		  
		  if(request.getSessionId() == null)
		  	request.setSessionId(UUID.randomUUID().toString());
		
		  
		  println "kolja..."
		  println "heyho change"
		  request.put('session', request.getSessionId())
          request.response.render 'views/index.html', next
      })
	  	  	
  )
  .listen(8080)  

