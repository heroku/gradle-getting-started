import ratpack.form.Form
import ratpack.groovy.template.TextTemplateModule

import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack

ratpack {

	bindings {
		module TextTemplateModule, { TextTemplateModule.Config config -> config.staticallyCompile = true }
	}

	handlers {
		get {
			render groovyTemplate("index.html", title: "Getting Started with Gradle on Heroku")
		}

		fileSystem "public", { f -> f.files() }
	}

}
