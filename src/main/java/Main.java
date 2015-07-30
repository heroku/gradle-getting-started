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
              .get("index.html", ctx -> {
                ctx.redirect(301, "/");
              })
              .get(ctx -> ctx.render(groovyTemplate("index.html")))
              .get("hello", ctx -> {
                RelativisticModel.select();
                Amount<Mass> m = Amount.valueOf("12 GeV").to(KILOGRAM);
                ctx.render("E=mc^2: 12 GeV = " + m.toString());
              })
              .assets("public");
          });
      }
    );
  }
}
