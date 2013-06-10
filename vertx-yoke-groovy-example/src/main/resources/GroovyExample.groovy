import javax.crypto.Mac

import model.ModelConverter
import model.User
import model.types.GenderType

import org.vertx.groovy.core.Vertx
import org.vertx.groovy.core.eventbus.Message;
import org.vertx.groovy.core.http.HttpServer
import org.vertx.groovy.core.http.ServerWebSocket
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject

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


HttpServer server = ((Vertx)vertx).createHttpServer();


Map para1 = [prefix: '/eventbus'];
List para2 = [
		// Allow calls to ...
		[
		  address: 'test.handler'
		],
	] 

JsonObject obj = new JsonObject();
obj.putString("prefix", "/eventbus");
 

GYoke gyoke = new GYoke(vertx)
  .engine('html', new GroovyTemplateEngine())
  
  .use(new Translation("translation.properties"))
  
  .use(new CookieParser(mac))
  .use(new Session(mac))
  
  .use(new ErrorHandler(true))
  .use("/static", new Static(".", 0, false, false))
  
  .use("/login/processLogin", new BodyParser())
  .use("/ajaxRequest", new BodyParser())
  .use(new GRouter()
	  .get("/", { YokeRequest request -> 
		  checkAndSetSessionId(request);
		  request.response.redirect ( "/index"); })
      .get("/index", { YokeRequest request, next ->
		  checkAndSetSessionId(request);
		  println "matched index"
          request.put('session', request.getSessionId())
          request.response.render 'views/index.html', next
      })
	  .get("/login", { YokeRequest request, next ->
		  checkAndSetSessionId(request);
		  println "matched login"
		  request.put('session', request.getSessionId())
		  request.put('user', "usernameXYZ")
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
		  request.put('result', result.toString())
		  request.response.render 'views/loginResult.html', next
	  })
	  .post("/ajaxRequest", { YokeRequest request, next ->
		  checkAndSetSessionId(request);
		  println "matched /ajaxRequest"

		  def result = "leer"
		  
		  println result
		  
		  def body = request.body();
		  println "body: " + body
		  
		  String responseJSON = "";
		  
		  if(body != null) {
		  	User user = ModelConverter.createUser(body.toString());
			user.setGender(GenderType.MALE);
			user.setlName("Mustermann!");
			
			responseJSON = ModelConverter.createJson(user);
		  }
		  
		  
		  request.put('session', request.getSessionId())
		  request.put('user', "usernameXYZ")
		  request.put('result', result.toString())
		  request.response.end responseJSON
	  })
	  	  	
  ).listen(server);

List<Object> permitted = new ArrayList<>();

//  ((Vertx)vertx).toJavaVertx().createSockJSServer(server.toJavaServer())
//		  .bridge( obj, new JsonArray(permitted), new JsonArray());
  
//		  gyoke.listen(server);

def config = ["prefix": "/eventbus"]

((Vertx)vertx).createSockJSServer(server).bridge(config, [[:]], [[:]])

  
((Vertx) vertx).eventBus.registerHandler("user.signup.handler") { Message message ->
	
	println "received message" + message.body
	
	def userPayload = message.body.user;
	if(userPayload != null) {
		User user = ModelConverter.createUser(userPayload);
		user.setGender(GenderType.MALE);
		user.setlName("Mustermann with SockJS!");
		
		message.reply([answer: user])
	}
	else
		message.reply([answer: null])
}

server.listen(8080);
