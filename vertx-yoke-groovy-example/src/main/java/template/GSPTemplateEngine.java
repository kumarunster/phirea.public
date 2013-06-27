package template;

import grails.gsp.PageRenderer;
import groovy.lang.MissingPropertyException;
import groovy.text.Template;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine;
import org.codehaus.groovy.grails.web.pages.discovery.GrailsConventionGroovyPageLocator;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;

import com.jetdrone.vertx.yoke.Engine;
import com.jetdrone.vertx.yoke.engine.AbstractEngine;
import com.jetdrone.vertx.yoke.util.YokeAsyncResult;

public class GSPTemplateEngine  extends AbstractEngine<Template>{

//    private final TemplateEngine engine = new SimpleTemplateEngine();
    private final GroovyPagesTemplateEngine engine = new GroovyPagesTemplateEngine();
    private final PageRenderer pageRenderer = new PageRenderer(engine);
    
    public GSPTemplateEngine() {
    	pageRenderer.setGroovyPageLocator(new GrailsConventionGroovyPageLocator());
	}
    
    @Override
    public void render(final String filename, final Map<String, Object> context, final Handler<AsyncResult<Buffer>> next) {
        read(filename, new AsyncResultHandler<String>() {
            @Override
            public void handle(AsyncResult<String> asyncResult) {
                if (asyncResult.failed()) {
                    next.handle(new YokeAsyncResult<Buffer>(asyncResult.cause()));
                } else {
                    try {
//                        String result = internalRender(compile(filename, asyncResult.result()), context);
                    	Map<String, Object> parameter = new LinkedHashMap<String, Object>();
                    	parameter.put("template", filename);
                    	parameter.put("model", context);
                    	String result = pageRenderer.render(parameter);
                    	
                        next.handle(new YokeAsyncResult<Buffer>( new Buffer(result) ) );
                    } catch (Exception ex) {
                        next.handle(new YokeAsyncResult<Buffer>(ex));
                    }
                }
            }
        });
    }

    private Template compile(String filename, String templateText) throws IOException, ClassNotFoundException {
        Template template = getTemplateFromCache(filename);

        if (template == null) {
            // real compile
            template = engine.createTemplate(templateText);
            putTemplateToCache(filename, template);
        }

        return template;
    }

    private static String internalRender(Template template, final Map<String, Object> context) throws IOException {
        final StringBuilder buffer = new StringBuilder();

        template.make(context).writeTo(new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                buffer.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {
                // noop
            }

            @Override
            public void close() throws IOException {
                // noop
            }
        });

        return buffer.toString();
    }

}
