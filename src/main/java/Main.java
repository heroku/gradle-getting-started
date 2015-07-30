import ratpack.server.RatpackServer;
import ratpack.groovy.template.TextTemplateModule;
import ratpack.guice.Guice;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;
import static ratpack.groovy.Groovy.groovyTemplate;
import static ratpack.groovy.Groovy.ratpack;

import static javax.measure.unit.SI.KILOGRAM;
import javax.measure.quantity.Mass;
import org.jscience.physics.model.RelativisticModel;
import org.jscience.physics.amount.Amount;

import java.util.*;
import java.sql.*;
import com.heroku.sdk.jdbc.DatabaseUrl;

public class Main {
  public static void main(String... args) throws Exception {
    RatpackServer.start(b -> {
        ServerConfig serverConfig = ServerConfig.findBaseDir()
          .env()
          .build();
        b
          .serverConfig(serverConfig)
          .registry(
            Guice.registry(s -> s
                .module(TextTemplateModule.class, conf ->
                    conf.setStaticallyCompile(true)
                )
            )
          )
          .handlers(c -> {
            c
              .get(ctx -> ctx.render(groovyTemplate("index.html")))
              .get("hello", ctx -> {
                ctx.render("Hello!");
              })
              .assets("public");
          });
      }
    );
  }
}
