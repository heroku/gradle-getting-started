```
:::-- rundoc
email = ENV['HEROKU_EMAIL'] || `heroku auth:whoami`

Rundoc.configure do |config|
  config.project_root = "gradle-getting-started"
  config.filter_sensitive(email => "developer@example.com")
end
```
<!--
  rundoc src:
  https://github.com/heroku/gradle-getting-started/blob/master/gradle-getting-started.md

  Command:
  $ rundoc build --path gradle-getting-started.md
-->

<h2 data-next-message="I'm ready to start">Introduction</h2>

This tutorial will have you deploying a Gradle app in minutes.

Hang on for a few more minutes to learn how it all works, so you can make the most out of Heroku.

The tutorial assumes that you have:

* a free [Heroku account](https://signup.heroku.com/signup/dc)
* Java 8 installed

If you'd prefer to use Maven instead of Gradle, please see the [Getting Started on Heroku with Java](getting-started-with-java) guide.

<h2 data-next-message="I have installed the Heroku CLI">Set up</h2>

In this step you will install the Heroku Command Line Interface (CLI), formerly known at the Heroku Toolbelt.  You will use the CLI to manage and scale your applications, to provision add-ons, to view the logs of your application as it runs on Heroku, as well as to help run your application locally.

Download and run the installer for your platform:

<div class="cli-download">
  <div class="text-center">
    <div class="download-wrap">
      <div class="download-column panel">
        <div class="download-title"><img src="/images/cli-apple-logo.svg" class="logo" alt="apple logo" />macOS</div>
        <p>
        <a href="https://cli-assets.heroku.com/heroku.pkg" class="btn btn-primary">Download the installer</a></p>
        <p><sub>Also available via Homebrew:</sub></p>
        <pre class=" language-term"><code class=" language-term"><span class="token input"><span class="token prompt">$ </span>brew install heroku/brew/heroku</span></code></pre>
      </div>
      <div class="download-column panel">
        <div class="download-title"><img src="/images/cli-windows-logo.svg" class="logo" alt="windows logo"/>Windows</div>
        <p>Download the appropriate installer for your Windows installation:</p>
        <p>
        <a href="https://cli-assets.heroku.com/heroku-x64.exe" class="btn btn-primary">64-bit installer</a></p>
        <p><a href="https://cli-assets.heroku.com/heroku-x86.exe" class="btn btn-primary">32-bit installer</a></p>
      </div>
    </div>
    <div class="download-wrap">
      <div class="download-column panel">
        <div class="download-title"><img src="/images/cli-ubuntu-logo.svg" class="logo" alt="ubuntu logo" />Ubuntu 16+</div>
        <p>Run the following from your terminal:</p>
        <pre class=" language-term"><code class=" language-term"><span class="token input"><span class="token prompt">$ </span>sudo snap install heroku --classic</span></code></pre>
        <p><sub><a href="https://snapcraft.io">Snap is available on other Linux OS's as well</a>.
</sub></p>
      </div>
    </div>
  </div>
</div>

When installation completes, you can use the `heroku` command from your terminal.

<div class="only-windows">On Windows, start the Command Prompt (cmd.exe) or Powershell to access the command shell.</div>

Once installed, you can use the `heroku` command from your command shell.

```term
$ heroku login
heroku: Press any key to open up the browser to login or q to exit
 ›   Warning: If browser does not open, visit
 ›   https://cli-auth.heroku.com/auth/browser/***
heroku: Waiting for login...
Logging in... done
Logged in as me@example.com
```

Authenticating is required to allow both the `heroku` and `git` commands to operate.

Note that if you’re behind a firewall that requires use of a proxy to connect with external HTTP/HTTPS services, [you can set the `HTTP_PROXY` or `HTTPS_PROXY` environment variables](articles/using-the-cli#using-an-http-proxy) in your local development environment before running the `heroku` command.

<h2 data-next-message="My app is ready">Prepare the app</h2>

In this step, you will prepare a simple application that can be deployed.

Execute the following commands to clone the sample application:

```term
:::>- $ git clone https://github.com/heroku/gradle-getting-started.git
:::>- $ cd gradle-getting-started
```

You now have a functioning Git repository that contains a simple application as well as a `build.gradle` file, which is used by the Gradle dependency manager.

<h2 data-next-message="I have deployed my app on Heroku">Deploy the app</h2>

In this step you will deploy the app to Heroku.

Create an app on Heroku, which prepares Heroku to receive your source code:

```term
:::>> $ heroku create
```

When you create an app, a Git remote (called `heroku`) is also created and associated with your local Git repository.

Heroku generates a random name (in this case `warm-eyrie-9006`) for your app, or you can pass a parameter to specify your own app name.

Now deploy your code:

```term
:::>- $ git push heroku master
:::-> | (head -6; echo "..."; tail -3)
```

The application is now deployed.   Ensure that at least one instance of the app is running:

```term
:::>- $ heroku ps:scale web=1
```

Now visit the app at the URL generated by its app name.  As a handy shortcut, you can open the website as follows:

```term
:::>- $ heroku open
```

<h2 data-next-message="I've learned how to see my logs">View logs</h2>

Heroku treats logs as streams of time-ordered events aggregated from the output streams of all your app and Heroku components, providing a single channel for all of the events.

View information about your running app using one of the [logging commands](logging), `heroku logs`:

```term
$ heroku logs --tail
2019-02-07T18:17:07.989806+00:00 app[web.1]: 2019-02-07 18:17:07.989  INFO 4 --- [           main] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page template: index
2019-02-07T18:17:08.543833+00:00 heroku[web.1]: State changed from starting to up
2019-02-07T18:17:08.538347+00:00 app[web.1]: 2019-02-07 18:17:08.538  INFO 4 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 48080 (http) with context path ''
2019-02-07T18:17:08.543461+00:00 app[web.1]: 2019-02-07 18:17:08.543  INFO 4 --- [           main] com.example.heroku.HerokuApplication     : Started HerokuApplication in 4.827 seconds (JVM running for 5.594)
2019-02-07T18:17:20.728559+00:00 app[web.1]: 2019-02-07 18:17:20.728  INFO 4 --- [io-48080-exec-3] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2019-02-07T18:17:20.728692+00:00 app[web.1]: 2019-02-07 18:17:20.728  INFO 4 --- [io-48080-exec-3] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2019-02-07T18:17:20.742328+00:00 app[web.1]: 2019-02-07 18:17:20.742  INFO 4 --- [io-48080-exec-3] o.s.web.servlet.DispatcherServlet        : Completed initialization in 14 ms
```

Visit your application in the browser again, and you'll see another log message generated. Press `Control+C` to stop streaming the logs.

<h2 data-next-message="I know what a Procfile is">Define a Procfile</h2>

Use a [Procfile](procfile), a text file in the root directory of your application, to explicitly declare what command should be executed to start your app.

The `Procfile` in the example app you deployed looks like this:

```yaml
:::-> $ cat Procfile
```

This declares a single process type, `web`, and the command needed to run it.  The name `web` is important here.  It declares that this process type will be attached to the [HTTP routing](http-routing) stack of Heroku, and receive web traffic when deployed.

<div class="only-windows">
<p>The syntax of the <code>Procfile</code> is important. Heroku runs the exact command specified here when it starts a web dyno and it runs it in a Unix environment. </p>

<p>When running locally under Windows, you may receive an error because the Unix way of passing in environment variables (<code>$JAVA_OPTS</code>) and of concatenating paths (<code>:</code>) is incompatible.</p>
</div>

Procfiles can contain additional process types.  For example, you might declare one for a background worker process that processes items off of a queue.

<h2 data-next-message="I know how to scale my app">Scale the app</h2>

Right now, your app is running on a single web [dyno](dynos).  Think of a dyno as a lightweight container that runs the command specified in the `Procfile`.

You can check how many dynos are running using the `ps` command:

```term
:::>> $ heroku ps
```

By default, your app is deployed on a free dyno. Free dynos will sleep after a half hour of inactivity (if they don't receive any traffic).  This causes a delay of a few seconds for the first request upon waking. Subsequent requests will perform normally.  Free dynos also consume from a monthly, account-level quota of [free dyno hours](free-dyno-hours) - as long as the quota is not exhausted, all free apps can continue to run.

To avoid dyno sleeping, you can upgrade to a hobby or professional dyno type as described in the [Dyno Types](dyno-types) article. For example, if you migrate your app to a professional dyno, you can easily scale it by running a command telling Heroku to execute a specific number of dynos, each running your web process type.

For abuse prevention, scaling the application requires [account verification](account-verification). If your account has not been verified, you will be directed to visit the [verification site](https://heroku.com/verify).

<h2 data-next-message="I've installed the app dependencies locally">Declare app dependencies</h2>

Heroku recognizes an app as a Gradle app by the existence of a `gradlew` or `build.gradle` file in the root directory.

The demo app you deployed already has a `build.gradle` ([see it here](https://github.com/heroku/gradle-getting-started/blob/master/build.gradle)). Here's an excerpt:

```groovy
:::-> $ sed -n '25,35p' build.gradle
```

The `build.gradle` file specifies dependencies that should be installed with your application.  When an app is deployed, Heroku reads this file and installs the  dependencies using the `./gradlew build` command.

Another file, `system.properties`, determines the version of Java to use. (Heroku supports many [different versions](https://devcenter.heroku.com/articles/java-support#supported-java-versions)). The contents of this file, which is optional, are quite straightforward:

```
:::-> $ cat system.properties
```

Run the Gradle `build` task in your local directory to install the dependencies, preparing your system for running the app locally.  Note that this app requires Java 8, but that you can push your own apps using a different version of Java.

On Windows, run this comannd

```term
> gradlew.bat build
```

On Mac and Linux run this command:

```term
$ ./gradlew build
```

In either case, you'll see output like this:

```term
:::-- $ ./gradlew build
:::-> | tail -2
```

If you see an error such as `Unsupported major.minor version 52.0`, then Gradle is trying to use Java 7. Check that your `JAVA_HOME` environment variable is set correctly.

The Gradle process will copy the dependencies into a single JAR file in your application's `build/libs` directory. This process is called "vendoring", and it is done by default in a Spring app, such as the sample. But it can also be done manually as described in the [Deploying Gradle Apps on Heroku](https://devcenter.heroku.com/articles/deploying-gradle-apps-on-heroku#verify-that-your-build-file-is-set-up-correctly) guide.

Once dependencies are installed, you will be ready to run your app locally.

<h2 data-next-message="I can run my app locally">Run the app locally</h2>

To run the app locally, first ensure that you've run the `gradlew build` task as described in the previous section. Then start your application using the `heroku local` command, which was installed as part of the Heroku CLI.

<div class="only-windows">
On Windows, use the command `gradlew.bat bootRun` instead.
</div>

```term
:::>- background.start("heroku local web", name: "local1", wait: "Tomcat started", timeout: 75)
:::-> | (echo "..."; tail -4)
:::-- $ curl -f localhost:5000
:::-- background.stop(name: "local1")
```

Just like Heroku, `heroku local` examines the `Procfile` to determine what to run. It also defines the port your app will bind to by setting the `PORT` environment variable, which is configured as `server.port` in the file `src/main/resources/application.properties`.

Your app will now be running at [http://localhost:5000](http://localhost:5000). Test that it's working with `curl` or a web browser, then `Ctrl+C` to exit.

`heroku local` doesn't just run your app - it also sets "config vars", something you'll encounter in a later tutorial.

<h2 data-next-message="I can push local changes">Push local changes</h2>

In this step you'll learn how to propagate a local change to the application through to Heroku.  As an example, you'll modify the application to add an additional dependency and the code to use it.

Modify `build.gradle` to include a dependency for `jscience` in the `dependencies` section like this:

```groovy
:::>> file.append build.gradle#30
compile "org.jscience:jscience:4.3.1"
```

Modify `src/main/java/com/example/heroku/HerokuApplication.java` so that it imports this library at the start.

```java
:::>> file.append src/main/java/com/example/heroku/HerokuApplication.java#19
import static javax.measure.unit.SI.KILOGRAM;
import javax.measure.quantity.Mass;
import org.jscience.physics.model.RelativisticModel;
import org.jscience.physics.amount.Amount;
```

Add the following `hello ` method to `HerokuApplication.java`:

```java
:::>> file.append src/main/java/com/example/heroku/HerokuApplication.java#59
@RequestMapping("/hello")
String hello(Map<String, Object> model) {
    RelativisticModel.select();
    Amount<Mass> m = Amount.valueOf("12 GeV").to(KILOGRAM);
    model.put("science", "E=mc^2: 12 GeV = " + m.toString());
    return "hello";
}
```

```html
:::>> file.write src/main/resources/templates/hello.html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" th:replace="~{fragments/layout :: layout (~{::body},'hello')}">
<body>
  <div class="container">
    <p th:text="${science}"/>
  </div>
</body>
</html>
```

[Here's the final source code](https://github.com/heroku/gradle-getting-started/blob/localchanges/src/main/java/com/example/heroku/HerokuApplication.java) for `HerokuApplication.java` - yours should look similar.  [Here's a diff](https://github.com/heroku/gradle-getting-started/compare/localchanges) of all the local changes you should have made.

Now test locally:

```term
:::>- $ ./gradlew build
:::-> | (echo "..."; tail -3)

:::>- background.start("heroku local web", name: "local2", wait: "Tomcat started", timeout: 30)
:::-> | (echo "..."; tail -4)
:::-- $ curl -f localhost:5000/hello
:::-- background.stop(name: "local2")
```

Visiting your application on the `/hello` route at [http://localhost:5000/hello](http://localhost:5000/hello), you should see some great scientific conversions displayed:

```
E=mc^2: 12 GeV = (2.139194076302506E-26 ± 1.4E-42) kg
```

Now deploy.  Almost every deploy to Heroku follows this same pattern.  First, add the modified files to the local Git repository:

```term
:::>- $ git add .
```

Now commit the changes to the repository:

```term
:::>- $ git commit -m "Demo"
```

Now deploy, just as you did previously:

```term
:::>- $ git push heroku master
```

Finally, check that everything is working:

```term
:::>- $ heroku open
```

<h2 data-next-message="I've used an add-on">Provision add-ons</h2>

<!-- needs to be after config vars -->

Add-ons are third-party cloud services that provide out-of-the-box additional services for your application, from persistence through logging to monitoring and more.

By default, Heroku stores 1500 lines of logs from your application.  However, it makes the full log stream available as a service - and several add-on providers have written logging services that provide things such as log persistence, search, and email and SMS alerts.

In this step you will provision one of these logging add-ons, Papertrail.

Provision the [Papertrail](papertrail) logging add-on:

```term
:::>> $ heroku addons:create papertrail
```

To help with abuse prevention, provisioning an add-on requires [account verification](account-verification). If your account has not been verified, you will be directed to visit the [verification site](https://heroku.com/verify).

The add-on is now deployed and configured for your application.  You can list add-ons for your app like this:

```term
$ heroku addons
```

To see this particular add-on in action, visit your application's Heroku URL a few times. Each visit will generate more log messages, which should now get routed to the Papertrail add-on.  Visit the Papertrail console to see the log messages:

```term
:::>- $ heroku addons:open papertrail
```

Your browser will open up a Papertrail web console, showing the latest log events.  The interface lets you search and set up alerts:

![Image](https://s3.amazonaws.com/heroku-devcenter-files/article-images/2680-imported-1443570632-2680-imported-1443555091-360-original.jpg 'add-on sample output')


<h2 data-next-message="I created a one-off dyno that ran a console">Start a one-off dyno</h2>

You can run a command, typically scripts and applications that are part of your app, in a [one-off dyno](one-off-dynos) using the `heroku run` command.   It can also be used to launch a REPL process attached to your local terminal for experimenting in your app's environment, or code that you deployed with your application:

```term
:::>- $ heroku run java -version
:::-> | tail -4
```

If you receive an error, `Error connecting to process`, then you may need to [configure your firewall](https://devcenter.heroku.com/articles/one-off-dynos#timeout-awaiting-process).

Don’t forget to type `exit` to exit the shell and terminate the dyno.

<h2 data-next-message="I understand config vars">Define config vars</h2>

Heroku lets you externalize configuration - storing data such as encryption keys or external resource addresses in [config vars](config-vars).

At runtime, config vars are exposed as environment variables to the application.  For example, modify `src/main/java/com/example/heroku/HerokuApplication.java` so that the method repeats grabs an energy value from the `ENERGY` environment variable:

```
:::-- $ sed -e '56,68d' src/main/java/com/example/heroku/HerokuApplication.java
```

```java
:::>> file.append src/main/java/com/example/heroku/HerokuApplication.java#56
@RequestMapping("/hello")
String hello(Map<String, Object> model) {
    RelativisticModel.select();
    String energy = System.getenv().get("ENERGY");
    if (energy == null) {
       energy = "12 GeV";
    }
    Amount<Mass> m = Amount.valueOf(energy).to(KILOGRAM);
    model.put("science", "E=mc^2: " + energy + " = "  + m.toString());
    return "hello";
}
```

Now compile the app again so that this change is integrated by running `./gradlew build` or `gradlew.bat build` respectively.

The `heroku local` command will automatically set up the environment based on the contents of the `.env` file in your local directory.  In the top-level directory of your project there is already a `.env` file that has the following contents:

```
ENERGY=20 GeV
```

If you run the app with `heroku local web` and visit it at [http://localhost:5000](http://localhost:5000), you'll see the conversion value for 20 GeV.

To set the config var on Heroku, execute the following:

```term
:::>> $ heroku config:set ENERGY="20 GeV"
```

View the config vars that are set using `heroku config`:

```term
:::>> $ heroku config
```

Deploy your changed application to Heroku to see this in action.

<h2 data-next-message="I understand how to use a database">Use a database</h2>

The [add-on marketplace](https://elements.heroku.com/addons/categories/data-stores) has a large number of data stores, from Redis and MongoDB providers, to Postgres and MySQL.  In this step you will learn about the free Heroku Postgres add-on.

To begin, attach a new instance of the PostgreSQL add-on to your app by running this command:

```term
:::>> $ heroku addons:create heroku-postgresql
```

You can find out a little more about the database provisioned for your app using the `addons` command in the CLI:

```term
:::>> $ heroku addons
```

Listing the config vars for your app will display the URL that your app is using to connect to the database, `DATABASE_URL`:

```term
:::>> $ heroku config
```

Heroku also provides a `pg` command that shows a lot more:

```term
:::>> $ heroku pg
```

This indicates I have a hobby database (free), running Postgres 9.3.3, with a single row of data.

The example app you deployed already has database functionality, which you should be able to reach by visiting your app’s URL and appending `/db`.  For example, if your app was deployed to `https://wonderful-app-287.herokuapp.com/` then visit `https://wonderful-app-287.herokuapp.com/db`.

The code to access the database is straightforward. Here's the method to insert values into a table called `tick`:

```java
:::-> $ sed -n '45,50p;78,109p'  src/main/java/com/example/heroku/HerokuApplication.java
```

This ensures that when you access your app using the `/db` route, a new row will be added to the `tick` table, and all the rows will then be returned so that they can be rendered in the output.

The `DatabaseUrl.extract()` method, which is made available via the `heroku-jdbc` dependency in the `build.gradle`, retrieves the `DATABASE_URL` environment variable, set by the database add-on, and establishes a connection.

Deploy your change to Heroku by committing the changes to Git, and then running `git push heroku master`.

Now, when you access your app's `/db` route, you will see something like this:

```
Database Output
* Read from DB: 2014-08-08 14:48:25.155241
* Read from DB: 2014-08-08 14:51:32.287816
* Read from DB: 2014-08-08 14:51:52.667683
```

Assuming that you have [Postgres installed locally](https://devcenter.heroku.com/articles/heroku-postgresql#local-setup), use the `heroku pg:psql` command to connect to the remote database and see all the rows:

```term
$ heroku pg:psql
psql (10.1, server 9.6.10)
SSL connection (protocol: TLSv1.2, cipher: ECDHE-RSA-AES256-GCM-SHA384, bits: 256, compression: off)
Type "help" for help.

DATABASE=> SELECT * FROM ticks;
            tick
----------------------------
2018-03-01 20:53:27.148139
2018-03-01 20:53:29.288995
2018-03-01 20:53:29.957118
2018-03-01 21:07:28.880162
(4 rows)
=> \q
```

Read more about [Heroku PostgreSQL](heroku-postgresql).

A similar technique can be used to install [MongoDB or Redis add-ons](https://elements.heroku.com/addons/categories/data-stores).


## Next steps

You now know how to deploy an app, change its configuration, view logs, scale, and attach add-ons.

Here's some recommended reading.  The first, an article, will give you a more firm  understanding of the basics.  The last is a pointer to the main Java category here on Dev Center:

* Read [How Heroku Works](how-heroku-works) for a technical overview of the concepts you’ll encounter while writing, configuring, deploying and running applications.
* Read [Deploying Gradle Apps on Heroku](deploying-gradle-apps-on-heroku) to understand how to take an existing Java app and deploy it to Heroku.
* Visit the [Java category](/categories/java-support) to learn more about developing and deploying Java applications.
