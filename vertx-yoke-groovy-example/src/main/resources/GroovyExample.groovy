import javax.crypto.Mac

import model.ModelConverter
import model.User
import model.types.GenderType

import org.vertx.groovy.core.Vertx
import org.vertx.groovy.core.eventbus.Message
import org.vertx.groovy.core.http.HttpServer
import org.vertx.groovy.platform.Container;
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.logging.Logger
import org.vertx.java.core.logging.impl.LoggerFactory

import com.jetdrone.vertx.yoke.GYoke
import com.jetdrone.vertx.yoke.engine.GroovyTemplateEngine
import com.jetdrone.vertx.yoke.middleware.*
import com.jetdrone.vertx.yoke.util.Utils




Map<String, User> userStore = new LinkedHashMap<String, User>();
Map<String, User> loggedInUserStore = new LinkedHashMap<String, User>();

User dummyUser = new User();
dummyUser.fName = "Dummy";
dummyUser.lName = "User";
dummyUser.passwd = "1";
dummyUser.email = "test@example.com";

userStore.put(dummyUser.email, dummyUser);
loggedInUserStore.put("fbd5050d-5eb7-44d3-b447-a2bd42d571b6", dummyUser);

dummyUser = new User();
dummyUser.fName = "Bilbo";
dummyUser.lName = "Baggins";
dummyUser.passwd = "1";
dummyUser.email = "bb@one.com";

userStore.put(dummyUser.email, dummyUser);


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
 

Logger logger = LoggerFactory.getLogger("GroovyVertxYokeExample");

println "do we have container? " + container;
println "container has a logger? " + ((Container) container).getLogger();

Logger vertxLogger = ((Container) container).getLogger();

GYoke gyoke = new GYoke(vertx, vertxLogger)
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
          request.response.render 'static/views/index.html', next
      })
	  .get("/login", { YokeRequest request, next ->
		  checkAndSetSessionId(request);
		  println "matched login"
		  request.put('session', request.getSessionId())
		  request.put('user', "usernameXYZ")
		  request.response.render 'static/views/login.html', next
	  })
	  .get("/signup-partial.html", { YokeRequest request, next ->
		  checkAndSetSessionId(request);
		  println "matched signup-partial"
		  request.put('session', request.getSessionId())
		  request.response.render 'static/views/signup-partial.html', next
	  })
	  .get("/login-partial.html", { YokeRequest request, next ->
		  checkAndSetSessionId(request);
		  println "matched login-partial"
		  request.put('session', request.getSessionId())
		  request.response.render 'static/views/login-partial.html', next
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
		  request.response.render 'static/views/loginResult.html', next
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
		
		userStore.put(user.getEmail(), user);
		
		println "user stored!"
		
		message.reply([answer: user])
	}
	else
		message.reply([answer: null])
}


((Vertx) vertx).eventBus.registerHandler("user.login.handler") { Message message ->
	
	println "received message" + message.body
	
	def sessionId = message.body.sessionId;
	def email = message.body.email;
	def password = message.body.password;
	if(email != null && email != null && sessionId != null) {
		
		User user = userStore.get(email);
		if(user != null && user.passwd.equals(password)) {
			println "user found! and logged in"
			message.reply([answer: user])
		}
		
		loggedInUserStore.put(sessionId, user);		
	}
	else
		message.reply([answer: null])
}


((Vertx) vertx).eventBus.registerHandler("user.logout.handler") { Message message ->
	
	println "received message" + message.body
	
	def sessionId = message.body.sessionId;
	if(sessionId != null) {
		loggedInUserStore.remove(sessionId);
		message.reply([answer: true]);	
	}
	else
		message.reply([answer: false]);
}


((Vertx) vertx).eventBus.registerHandler("user.service.handler") { Message message ->
	
	println "received message" + message.body
	
	def actionPayload = message.body.action;
	if(actionPayload != null) {
		
		if('findAllUser'.equals(actionPayload)) {
			def responseMsg = ModelConverter.createJson(userStore.values().asList());
			message.reply([answer: responseMsg]);
		}
		
		if('findUserForSession'.equals(actionPayload)) {
			def sessionId = message.body.sessionId;
			def responseUser = null;
			if(sessionId != null)
				responseUser = loggedInUserStore.get(sessionId);
			
			message.reply([answer: responseUser]);
		}
		
		if('findLoggedInUser'.equals(actionPayload)) {
			Map<String, User> result = new LinkedHashMap<String, User>();
			loggedInUserStore.values().each {
				if(!result.containsKey(it.email))
					result.put(it.email, it);
			}
			def responseMsg = ModelConverter.createJson(result.values().asList());
			message.reply([answer: responseMsg]);
		}
		
	}
	else
		message.reply([answer: null])
}


server.listen(8080);
