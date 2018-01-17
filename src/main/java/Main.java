import ratpack.exec.Blocking;
import ratpack.hikari.HikariModule;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;
import ratpack.groovy.template.TextTemplateModule;
import ratpack.guice.Guice;

import static ratpack.groovy.Groovy.groovyTemplate;

import java.util.*;
import java.sql.*;

import javax.sql.DataSource;

public class Main {
  public static void main(String... args) throws Exception {
    RatpackServer.start(s -> s
        .serverConfig(c -> c
          .baseDir(BaseDir.find())
          .env())

        .registry(Guice.registry(b -> {
          if (System.getenv("JDBC_DATABASE_URL") != null) {
            b.module(HikariModule.class, conf -> {
              conf.addDataSourceProperty("URL", System.getenv("JDBC_DATABASE_URL"));
              conf.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
            });
          }
          b.module(TextTemplateModule.class, conf -> conf.setStaticallyCompile(true));
        }))

        .handlers(chain -> chain
            .get(ctx -> ctx.render(groovyTemplate("index.html")))

            .get("hello", ctx -> {
              ctx.render("Hello!");
            })

            .get("db", ctx -> {
              Blocking.get(() -> {
                try (Connection connection = ctx.get(DataSource.class).getConnection()) {
                  Statement stmt = connection.createStatement();
                  stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
                  stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
                  ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

                  ArrayList<String> output = new ArrayList<>();
                  while (rs.next()) {
                    output.add("Read from DB: " + rs.getTimestamp("tick"));
                  }
                  Map<String, Object> attributes = new HashMap<>();
                  attributes.put("results", output);

                  return attributes;
                }
              }).onError(throwable -> {
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("message", "There was an error: " + throwable);
                ctx.render(groovyTemplate(attributes, "error.html"));
              }).then(attributes -> {
                ctx.render(groovyTemplate(attributes, "db.html"));
              });
            })

            .files(f -> f.dir("public"))
        )
    );
  }
}