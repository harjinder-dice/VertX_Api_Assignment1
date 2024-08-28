package com.Assignment.APIAssignment_VertX;

import io.ebean.DB;
import io.ebean.DatabaseFactory;
import io.ebean.Transaction;
import io.ebean.Database;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

//import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    Router router = Router.router(vertx);
    Map<String,String> users=new HashMap<String,String>();

    DatabaseConfig cfg = new DatabaseConfig();

    Properties properties = new Properties();
    properties.put("ebean.db.ddl.generate", "true");
    properties.put("ebean.db.ddl.run", "true");
    properties.put("datasource.db.username", "root");
    properties.put("datasource.db.password", "root");
    properties.put("datasource.db.databaseUrl","jdbc:mysql://localhost:3306/WeatherApi");
    properties.put("datasource.db.databaseDriver", "com.mysql.cj.jdbc.Driver");

    cfg.loadFromProperties(properties);
    Database server = DatabaseFactory.create(cfg);

    //get JWT token
    JWTAuthOptions jwtAuthOptions = new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions()
        .setAlgorithm("HS256")
        .setBuffer("keyboard cat"));
    JWTAuth provider = JWTAuth.create(vertx, jwtAuthOptions);

    router.route().path("/api/weather/:city").produces("application/json").handler(JWTAuthHandler.create(provider)).handler(this::handleWeather);

    // Sign-Up Route
    router.post("/api/signup").handler(BodyHandler.create()).handler(context->{
      JsonObject user = context.getBodyAsJson(); // Correct way to get the body as JsonObject
      if (user == null) {
        context.response().setStatusCode(400).end("Invalid request body");
        return;
      }

      String username = user.getString("username");
      String password = user.getString("password");

      if (username == null || password == null) {
        context.response().setStatusCode(400).end("Username and password are required");
        return;
      }
      UserCred userCred=new UserCred(username,password);

      try(Transaction txn= DB.beginTransaction()){
        DB.save(userCred);
        txn.commit();
      }
      catch (Exception e){
        System.err.println(e.getMessage());
      }
      users.put(username, password);
      context.response().setStatusCode(201).end("User created");
    });

    // Login Route
    router.post("/api/login").handler(BodyHandler.create()).handler(context->{
      JsonObject credentials = context.getBodyAsJson(); // Correct way to get the body as JsonObject
      if (credentials == null) {
        context.response().setStatusCode(400).end("Invalid request body");
        return;
      }

      String username = credentials.getString("username");
      String password = credentials.getString("password");

      if (username == null || password == null) {
        context.response().setStatusCode(400).end("Username and password are required");
        return;
      }

      if (isValidUser(username, password, users,server)) {
        JsonObject claims = new JsonObject().put("sub", username);
        JWTOptions options = new JWTOptions().setExpiresInMinutes(30);

        String token = provider.generateToken(claims, options);
        context.response()
          .putHeader("Content-Type", "application/json")
          .end(new JsonObject().put("token", token).encode());
      } else {
        context.response().setStatusCode(401).end("Invalid credentials");
      }
    });

    vertx.createHttpServer().requestHandler(router).listen(8080);
  }

  public void handleSignUp(RoutingContext context,Map<String,String> users) {
    JsonObject user = context.getBodyAsJson();
    if (user == null) {
      context.response().setStatusCode(400).end("Invalid request body");
      return;
    }

    String username = user.getString("username");
    String password = user.getString("password");

    if (username == null || password == null) {
      context.response().setStatusCode(400).end("Username and password are required");
      return;
    }

    UserCred userCred=new UserCred(username,password);
    users.put(username, password);
    context.response().setStatusCode(201).end("User created");
  }

  public boolean isValidUser(String username, String password,Map<String,String> users,Database database) {
      String DBPassword=database.sqlQuery("select password from user_cred where username='"+username+"'").findOne().getString("password");
      if(DBPassword==null){return  false;}
      return DBPassword.equals(password);
  }
  public void handleWeather(RoutingContext ctx){

    WebClientOptions options = new WebClientOptions()
      .setDefaultPort(443)
      .setDefaultHost("api.weatherapi.com")
      .setSsl(true);
    WebClient webClient = WebClient.create(vertx, options);
    String apiKey = "0d3a7555712c47dca7951346242708";
    String query = ctx.pathParam("city");
    String baseUrl = "http://api.weatherapi.com/v1";
    String url = baseUrl + "/current.json?key=" + apiKey + "&q=" + query;
//    System.out.println(url);
    webClient.get(url).send(request -> {
      if (request.succeeded()) {
        // Obtaining response
        System.out.println("Success");
        HttpResponse<Buffer> response = request.result();
        //return response
        ctx.response().end(response.bodyAsString());


      } else {
        System.out.println(request.cause().getMessage());
      }
    });
  }
}

