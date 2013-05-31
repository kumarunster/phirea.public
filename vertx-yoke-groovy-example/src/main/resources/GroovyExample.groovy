import java.util.Map;

import javax.crypto.Mac;

import org.vertx.groovy.core.file.FileSystem;

import com.jetdrone.vertx.yoke.middleware.*
import com.jetdrone.vertx.yoke.util.Utils;
import com.jetdrone.vertx.yoke.GYoke
import com.jetdrone.vertx.yoke.engine.GroovyTemplateEngine





final Properties msg  = new Properties()

FileSystem fileSystem = vertx.getFileSystem();
def propertiesString = fileSystem.readFileSync("translation.properties").toString();
msg.load(new StringReader(propertiesString));


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
  .use(new Session(mac))
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
  .use("/processLogin", new BodyParser())
  .use(new GRouter()
	  .get("/", { YokeRequest request -> 
		  checkAndSetSessionId(request);
		  request.response.redirect ( "/index"); })
      .get("/index", { YokeRequest request, next ->
		  checkAndSetSessionId(request);
		  println "matched index"
          request.put('session', request.getSessionId())
		  request.put('msg', msg)
          request.response.render 'views/index.html', next
      })
	  .get("/login", { YokeRequest request, next ->
		  checkAndSetSessionId(request);
		  println "matched login"
		  request.put('session', request.getSessionId())
		  request.put('user', "usernameXYZ")
		  request.put('msg', msg)
		  request.response.render 'views/login.html', next
	  })
	  .post("/login/processLogin", { YokeRequest request, next ->
		  checkAndSetSessionId(request);
		  println "matched /login/processLogin"
		  Map<String, String> body = request.formAttributes();
		  def fName = body.get("fname");
		  def lName = body.get("lname");
		  def email = body.get("email");
		  def gender = body.get("gender");
		  def passwd = body.get("passwd");
		  
		  def result = "get data from login form: [fName: ${fName}, lName: ${lName}, email: ${email}, gender: ${gender}, passwd: ${passwd} ]" 
		  
		  println result
		  
		  request.put('session', request.getSessionId())
		  request.put('user', "usernameXYZ")
		  request.put('msg', msg)
		  request.put('result', result.toString())
		  request.response.render 'views/loginResult.html', next
	  })
	  	  	
  )
  .listen(8080)
