import com.jetdrone.vertx.yoke.middleware.*
import com.jetdrone.vertx.yoke.GYoke
import com.jetdrone.vertx.yoke.engine.GroovyTemplateEngine


new GYoke(vertx)
  .engine('html', new GroovyTemplateEngine())
  .use(new ErrorHandler(true))
//  .use("/web", {request, next -> staticHandler.handle(request, next)})
  .use("/static", new Static(".", 0, false, false))
//  .use(new GRouter()
//    .get("/hello") { request ->
//        request.response.end "Hello World!"
//    }

//  .use {request ->
//    request.response.end "maybe you should go to /hello or /template!"
//  }
  .use(new GRouter()
	  .get("/", { request -> request.response.redirect ( "/index"); })
      .get("/index", { request, next ->
		  println "matched index"
          request.put('session', [id: '1'])
          request.response.render 'views/index.html', next
      })
	  	  	
  )
  .listen(8080)