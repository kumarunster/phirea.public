package template;

/*
 * Copyright 2012. the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */


import groovy.lang.Binding;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.lang.Writable;
import groovy.text.Template;
import groovy.text.TemplateEngine;
import groovy.util.GroovyScriptEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * http://www.antoniogoncalves.org/xwiki/bin/view/Blog/TemplateWithTemplatesInGroovy
 * <p/>
 * This simple template engine uses JSP <% %> script, <%= %> and <@ @> expression syntax.  It also lets you use normal groovy expressions in
 * the template text much like the new JSP EL functionality.  The variable 'out' is bound to the writer that the template is being written to.
 *
 * @author sam
 * @author Christian Stein
 * @author Paul King
// */

public class IncludeTemplateEngine {
	
}

//public class IncludeTemplateEngine extends TemplateEngine {
//        private static final Logger logger = Logger.getLogger(IncludeTemplateEngine.class.getName());
//        private boolean verbose;
//        private static long counter = 1;
//        private GroovyShell groovyShell;
//
//        //private GroovyScriptEngine groovyScriptEngine;
//
////        public IncludeTemplateEngine() {
////                this(GroovyShell.class.getClassLoader());
////        }
//
//        public IncludeTemplateEngine(GroovyScriptEngine groovyScriptEngine) {
//                this(groovyScriptEngine.getGroovyClassLoader());
//                //this.groovyScriptEngine = groovyScriptEngine;
//        }
//
//        public IncludeTemplateEngine(boolean verbose) {
//                this(GroovyShell.class.getClassLoader());
//                setVerbose(verbose);
//        }
//
//        public IncludeTemplateEngine(ClassLoader parentLoader, CompilerConfiguration config) {
//                this(new GroovyShell(parentLoader, new Binding(), config));
//
//        }
//
//        public IncludeTemplateEngine(ClassLoader parentLoader) {
//                this(new GroovyShell(parentLoader, new Binding()));
//
//        }
//
//        public IncludeTemplateEngine(GroovyShell groovyShell) {
//                this.groovyShell = groovyShell;
//        }
//
//
//        public Template createTemplate(Reader reader) throws CompilationFailedException, IOException {
//                SimpleTemplate template = new SimpleTemplate();
//                SimpleTemplate.InheritedTemplateInfo inheritedTemplate = new SimpleTemplate.InheritedTemplateInfo();
//                try {
//                        String script = template.parse(reader, true, inheritedTemplate);
//
//                        if (inheritedTemplate.inherited) {
//                                Reader sr = new BufferedReader(new StringReader(inheritedTemplate.getMergedContents().toString()));
//                                script = template.parse(sr, true, new SimpleTemplate.InheritedTemplateInfo());
//                        }
//
//
//                        //template.script = groovyShell.parse(script, "SimpleTemplateScript" + counter++ + ".groovy");
//                        String uniqueScriptName = "SimpleTemplateScript" + counter++ + ".groovy";
//
//                        //template.setTemplateName(uniqueScriptName.substring(0, uniqueScriptName.length() - 7));
//                        if (!RequestThreadInfo.get().errorOccurred())
//                                RequestThreadInfo.get().setUniqueTemplateScriptName(uniqueScriptName);
//
//                        synchronized (groovyShell) {
//                                template.script = groovyShell.parse(script, uniqueScriptName);
//                                groovyShell.notifyAll();
//                        }
//
//                        //template.script = groovyScriptEngine.createScript(script,requestedUrl, uniqueScriptName, binding);
//                        //StaticControllerMethods.addMethods(template.script.getClass());
//                } catch (Exception e) {
//                        throw new GroovyRuntimeException("Failed to parse template script (your template may contain an error or be trying to use expressions not currently supported): " + e.getMessage());
//                }
//
//                return template;
//        }
//
//        public Template createTemplate(Reader reader, String requestedUrl, String scriptFileName, Binding binding) throws CompilationFailedException, IOException, InheritedTemplateException, ParentTemplateException, ScriptBlockException {
//                SimpleTemplate template = new SimpleTemplate();
//                SimpleTemplate.InheritedTemplateInfo inheritedTemplate = new SimpleTemplate.InheritedTemplateInfo();
//                String script = template.parse(reader, true, inheritedTemplate);
//
//                if (inheritedTemplate.inherited) {
//                        Reader sr = new BufferedReader(new StringReader(inheritedTemplate.getMergedContents().toString()));
//                        script = template.parse(sr, true, new SimpleTemplate.InheritedTemplateInfo());
//                }
//
//                try {
//
//                        //template.script = groovyShell.parse(script, "SimpleTemplateScript" + counter++ + ".groovy");
//
//                        String uniqueScriptName = "SimpleTemplateScript" + counter++ + ".groovy";
//                        //template.setTemplateName(uniqueScriptName.substring(0, uniqueScriptName.length() - 7));
//                        if (!RequestThreadInfo.get().errorOccurred())
//                                RequestThreadInfo.get().setUniqueTemplateScriptName(uniqueScriptName);
//
//                        //RequestThreadInfo.get().getRequestError().setTemplatePath(requestedUrl);
//                        template.script = groovyShell.parse(script, uniqueScriptName);
//                        //template.script = groovyScriptEngine.createScript(script,requestedUrl, uniqueScriptName, binding);
//                        //StaticControllerMethods.addMethods(template.script.getClass());
//                } catch (Exception e) {
//                        throw new GroovyRuntimeException("Failed to parse template script (your template may contain an error or be trying to use expressions not currently supported): " + e.getMessage());
//                }
//                return template;
//        }
//
//        public void setVerbose(boolean verbose) {
//                this.verbose = verbose;
//        }
//
//        public boolean isVerbose() {
//                return verbose;
//        }
//
//        protected static class SimpleTemplate implements Template {
//                protected Script script;
//
//                public Writable make() {
//                        return make(null);
//                }
//
//                private String templateName;
//
//                public String getTemplateName() {
//                        return templateName;
//                }
//
//                public void setTemplateName(String templateName) {
//                        this.templateName = templateName;
//                }
//
//                public Script getScript() {
//                        return script;
//                }
//
//                public Writable make(final Map map) {
//                        return new Writable() {
//                                /**
//                                 * Write the template document with the set binding applied to the writer.
//                                 *
//                                 * @see groovy.lang.Writable#writeTo(java.io.Writer)
//                                 */
//                                public Writer writeTo(Writer writer) {
//                                        Binding binding;
//                                        if (map == null)
//                                                binding = new Binding();
//                                        else
//                                                binding = new Binding(map);
//
//                                        Script scriptObject = InvokerHelper.createScript(script.getClass(), binding);
//                                        //PrintWriter pw = new PrintWriter(writer);
//                                        scriptObject.setProperty("rout", writer);
//                                        scriptObject.run();
//                                        try {
//                                                writer.flush();
//                                        } catch (IOException e) {
//                                                throw new RuntimeException(e);
//                                        }
//                                        return writer;
//                                }
//
//                                /**
//                                 * Convert the template and binding into a result String.
//                                 *
//                                 * @see java.lang.Object#toString()
//                                 */
//                                public String toString() {
//                                        StringWriter sw = new StringWriter();
//                                        writeTo(sw);
//                                        return sw.toString();
//                                }
//                        };
//                }
//
//                /**
//                 * Parse the text document looking for <% or <%= and then call out to the appropriate handler, otherwise copy the text directly
//                 * into the script while escaping quotes.
//                 *
//                 * @param reader a reader for the template text
//                 * @return the parsed text
//                 * @throws IOException if something goes wrong
//                 */
//                protected String parse(Reader reader, boolean rootTemplate, InheritedTemplateInfo templateInfo) throws IOException, Exception {
//                        if (!reader.markSupported()) {
//                                reader = new BufferedReader(reader);
//                        }
//
//                        StringWriter sw = new StringWriter();
//                        if (rootTemplate)
//                                startScript(sw);
//
//                        int c;
//                        while ((c = reader.read()) != -1) {
//                                if (c == '<') {
//                                        reader.mark(1);
//                                        c = reader.read();
//                                        if (c != '%') {
//                                                sw.write('<');
//                                                reader.reset();
//
//                                        } else {
//                                                reader.mark(1);
//                                                c = reader.read();
//                                                if (c == '=') {
//                                                        groovyExpression(reader, sw);
//                                                } else if (c == '@') {
//                                                        processDirective(reader, sw, templateInfo);
//
//                                                } else {
//                                                        reader.reset();
//                                                        groovySection(reader, sw);
//                                                }
//                                        }
//                                        continue;// at least '<' is consumed Ã¢â‚¬Â¦ read next chars.
//                                }
//                                if (c == '$') {
//                                        reader.mark(1);
//                                        c = reader.read();
//                                        if (c != '{') {
//                                                sw.write('$');
//                                                reader.reset();
//                                        } else {
//                                                reader.mark(1);
//                                                sw.write("${");
//                                                processGSstring(reader, sw);
//                                        }
//                                        continue;// at least '$' is consumed Ã¢â‚¬Â¦ read next chars.
//                                }
//                                if (c == '\"') {
//                                        sw.write('\\');
//                                }
//                                /*
//                                * Handle raw new line characters.
//                                */
//                                if (c == '\n' || c == '\r') {
//                                        if (c == '\r') {// on Windows, "\r\n" is a new line.
//                                                reader.mark(1);
//                                                c = reader.read();
//                                                if (c != '\n') {
//                                                        reader.reset();
//                                                }
//                                        }
//                                        sw.write("\\n\");\nrout.print(\"");
//                                        continue;
//                                }
//                                sw.write(c);
//                        }
//
//                        if (!templateInfo.isInherited())
//                                endScript(sw);
//
//                        return sw.toString();
//                }
//
//                private void startScript(StringWriter sw) {
//                        //sw.write("/* Generated by SimpleTemplateEngine */\n");
//                        sw.write("rout.print(\"");
//                }
//
//                private void endScript(StringWriter sw) {
//                        sw.write("\");\n");
//                }
//
//                private void processDirective(Reader reader, StringWriter sw, InheritedTemplateInfo inheritedTemplateInfo) throws IOException
//                        {
//                        Directive directive = parseDirective(reader);
//
//                        if (directive.isInclude()) {
//                                processIncludeDirective(sw, directive.parts[2]);
//                        } else if (directive.isScriptBlock()) {
//                                parseSkipBlock(reader, sw);
//                        } else if (directive.isInheritedTemplated()) {
//                                inheritedTemplateInfo.inherited = true;
//                                inheritedTemplateInfo.mergedContents = new StringWriter();
//                                inheritedTemplateInfo.setParentTemplate(directive.parts[2]);
//                                inheritedTemplateInfo.setChildTemplate(RequestThreadInfo.get().getParsedRequest().getRequestURI());
//
//                                String path = directive.parts[2];
//
//                                File file = null;
//                                File f = null;
//                                if (path.charAt(0) == '/') {      //root request
//                                        if (File.separator.equals("/")) {
//                                                file = new File(RequestThreadInfo.get().getApplication().getAppPath() + path);
//                                        } else {
//                                                file = new File(RequestThreadInfo.get().getApplication().getAppPath() + path.replaceAll("/", "\\\\"));
//                                        }
//                                } else {
//                                        f = new File(RequestThreadInfo.get().getParsedRequest().getRequestFilePath()).getParentFile();
//
//                                        if (File.separator.equals("/")) {
//                                                file = new File(f.getAbsolutePath() + File.separator + path.trim());
//                                        } else {
//                                                file = new File(f.getAbsolutePath() + File.separator + path.trim().replaceAll("/", "\\\\"));
//                                        }
//                                }
//
//                                RequestThreadInfo.get().getTemplateInfo().setTemplateRoot(file.getCanonicalFile().getParent());
//
//                                processChildTemplate(reader, inheritedTemplateInfo.mergedContents, file.getName(), inheritedTemplateInfo);
//
//                        }
//                }
//
//                private String processChildTemplate(Reader reader, StringWriter sw, String parentFile, InheritedTemplateInfo inheritedTemplateInfo) throws InheritedTemplateException, ParentTemplateException {
//                        Map<String, BlockContents> blocks = new HashMap();
//
//                        try {
//                                if (!reader.markSupported()) {
//                                        reader = new BufferedReader(reader);
//                                }
//
//                                int c;
//                                while ((c = reader.read()) != -1) {
//                                        if (c == '<') {
//                                                reader.mark(1);
//                                                c = reader.read();
//                                                if (c != '%') {
//                                                        sw.write('<');
//                                                        reader.reset();
//                                                } else {
//                                                        reader.mark(1);
//                                                        c = reader.read();
//                                                        if (c == '@') {
//                                                                Directive d = parseDirective(reader);
//                                                                if (d.isStartBlock()) {
//                                                                        parseChildBlock(reader, d.getBlockName(), d, blocks);
//                                                                        continue;
//                                                                }
//                                                        }
//                                                }
//                                        }
//
//                                }
//                        } catch (IOException e) {
//                                throw new InheritedTemplateException("IOException occurring processing child template. child: " + inheritedTemplateInfo.getChildTemplate() + ", parent: " + inheritedTemplateInfo.getParentTemplate() + e.getMessage(), e);
//                        }
//
//                        return mergeContentsWithParentTemplate(sw, blocks, parentFile, inheritedTemplateInfo);
//                }
//
//                public String mergeContentsWithParentTemplate(StringWriter sw, Map<String, BlockContents> blocks, String parentFile, InheritedTemplateInfo inheritedTemplateInfo) throws ParentTemplateException {
//                        String fileEncoding = System.getProperty("groovy.source.encoding");
//
//                        Reader reader = null;
//                        try {
//                                File file = null;
//
//                                String templateRoot;
//                                if (parentFile.charAt(0) == '/') {      //root request
//
//                                        if (File.separator.equals("/")) {
//                                                file = new File(RequestThreadInfo.get().getApplication().getAppPath() + parentFile.trim());
//                                        } else {
//                                                file = new File(RequestThreadInfo.get().getApplication().getAppPath() + parentFile.trim().replaceAll("/", "\\\\"));
//                                        }
//                                        templateRoot = RequestThreadInfo.get().getApplication().getAppPath();
//                                } else {
//                                        if (File.separator.equals("/")) {
//                                                file = new File(RequestThreadInfo.get().getTemplateInfo().getTemplateRoot() + File.separator + parentFile.trim());
//                                        } else {
//                                                file = new File(RequestThreadInfo.get().getTemplateInfo().getTemplateRoot() + File.separator + parentFile.trim().replaceAll("/", "\\\\"));
//                                        }
//
//                                        templateRoot = RequestThreadInfo.get().getTemplateInfo().getTemplateRoot();
//                                }
//
//                                RequestThreadInfo.get().getTemplateInfo().getChildren().add(file.getAbsolutePath());
//                                reader = fileEncoding == null ? new FileReader(file) : new InputStreamReader(new FileInputStream(file), fileEncoding);
//
//
//                                if (!reader.markSupported()) {
//                                        reader = new BufferedReader(reader);
//                                }
//
//                                int c;
//                                while ((c = reader.read()) != -1) {
//                                        if (c == '<') {
//                                                reader.mark(1);
//                                                c = reader.read();
//                                                if (c != '%') {
//                                                        sw.write('<');
//                                                        reader.reset();
//                                                } else {
//                                                        reader.mark(1);
//                                                        c = reader.read();
//                                                        if (c == '@') {
//                                                                Directive d = parseDirective(reader);
//                                                                if (d.isStartBlock()) {
////                                                                        for (String s : d.parts) {
////                                                                                System.out.println("parts:" + s);
////                                                                        }
////                                                                        System.out.println("blocks name:" + d.getBlockName());
////                                                                        System.out.println("blocks passThruParentContent = " + blocks.get(d.getBlockName()).getDirective().passThruParentContent());
//
//                                                                        parseParentBlock(sw, reader, d.getBlockName(), blocks.get(d.getBlockName()).getDirective().passThruParentContent(), blocks, inheritedTemplateInfo);
//                                                                        continue;
//                                                                } else {
//                                                                        sw.write(d.directive);
//                                                                }
//
//                                                        } else {
//                                                                sw.write("<%");
//                                                                sw.write(c);
//                                                        }
//                                                }
//                                                continue;// at least '<' is consumed Ã¢â‚¬Â¦ read next charshttp://feedproxy.google.com/%7Er/Techcrunch/%7E3/HjXrnVivdQ4/.
//                                        }
//
//                                        sw.write(c);
//                                }
//                                //System.out.println(sw.toString());
//                                return sw.toString();
//                        } catch (IOException e) {
//                                throw new ParentTemplateException(e);
//                        } finally {
//                                closeReader(reader);
//                        }
//                }
//
//                private void parseParentBlock(StringWriter contents, Reader reader, String blockName, boolean passThruParentContent, Map<String, BlockContents> blocks, InheritedTemplateInfo inheritedTemplateInfo) throws ParentTemplateException {
//
//                        int c;
//                        try {
//                                while ((c = reader.read()) != -1) {
//                                        if (c == '<') {
//                                                reader.mark(1);
//                                                c = reader.read();
//                                                if (c != '%') {
//                                                        if (passThruParentContent)
//                                                                contents.write('<');
//
//                                                        reader.reset();
//                                                } else {
//                                                        reader.mark(1);
//                                                        c = reader.read();
//                                                        if (c == '@') {
//                                                                Directive directive = parseDirective(reader);
//
//                                                                if (directive.isEndBlock()) {
//                                                                        if (blocks.containsKey(blockName)) {
//                                                                                contents.write(blocks.get(blockName).getContents());
//                                                                        }
//                                                                        break;
//                                                                } else {
//                                                                        if (passThruParentContent)
//                                                                                contents.write(directive.directive);
//                                                                }
//
//                                                        } else {
//                                                                if (passThruParentContent) {
//                                                                        contents.write("<%");
//                                                                        contents.write(c);
//                                                                }
//                                                        }
//                                                }
//                                                continue;// at least '<' is consumed Ã¢â‚¬Â¦ read next chars.
//                                        }
//
//                                        if (passThruParentContent)
//                                                contents.write(c);
//                                }
//                        } catch (IOException e) {
//                                throw new ParentTemplateException("IOException occurring processing child template. child: " + inheritedTemplateInfo.getChildTemplate() + ", parent: " + inheritedTemplateInfo.getParentTemplate() + e.getMessage(), e);
//                        }
//                }
//
//                private void parseSkipBlock(Reader reader, StringWriter sw) throws ScriptBlockException {
//
//                        int c;
//                        StringWriter contents = new StringWriter();
//
//                        try {
//                                while ((c = reader.read()) != -1) {
//                                        if (c == '<') {
//                                                reader.mark(1);
//                                                c = reader.read();
//                                                if (c != '%') {
//                                                        contents.write('<');
//                                                        reader.reset();
//                                                } else {
//                                                        reader.mark(1);
//                                                        c = reader.read();
//                                                        if (c == '@') {
//                                                                Directive directive = parseDirective(reader);
//                                                                if (directive.isEnd()) {
//                                                                        //blocks.put(blockName, new BlockContents(startDirective, contents.toString()));
//                                                                        break;
//                                                                } else {
//                                                                        contents.write(directive.directive);
//                                                                }
//                                                        } else {
//                                                                contents.write("<%");
//                                                                contents.write(c);
//                                                        }
//                                                }
//                                                continue;
//                                        }
//
////                                if (c == '\"') {
////                                        contents.write('\\');
////                                }
//
//                                        contents.write(c);
//                                }
//
//
//                                SimpleTemplate simpleTemplate = new SimpleTemplate();
//                                String script = simpleTemplate.parse(new StringReader(contents.toString().replaceAll("\\\\","\\\\\\\\").replaceAll("\\$", "\\\\\\$")),
//                                        false,
//                                        new InheritedTemplateInfo());
//                                sw.write(script);
//                                sw.write("rout.print(\"");
//
//                        } catch (Exception e) {
//                                throw new ScriptBlockException(e);
//                        }
//
//
//                }
//
//                private void parseChildBlock(Reader reader, String blockName, Directive startDirective, Map<String, BlockContents> blocks) throws IOException {
//                        int c;
//                        StringWriter contents = new StringWriter();
//                        while ((c = reader.read()) != -1) {
//                                if (c == '<') {
//                                        reader.mark(1);
//                                        c = reader.read();
//                                        if (c != '%') {
//                                                contents.write('<');
//                                                reader.reset();
//                                        } else {
//                                                reader.mark(1);
//                                                c = reader.read();
//                                                if (c == '@') {
//                                                        Directive directive = parseDirective(reader);
//                                                        if (directive.isEndBlock()) {
//                                                                blocks.put(blockName, new BlockContents(startDirective, contents.toString()));
//                                                                break;
//                                                        } else {
//                                                                contents.write(directive.directive);
//                                                        }
//                                                } else {
//                                                        contents.write("<%");
//                                                        contents.write(c);
//                                                }
//                                        }
//                                        continue;
//                                }
//
////                                if (c == '\"') {
////                                        contents.write('\\');
////                                }
//
//                                contents.write(c);
//                        }
//                }
//
//                private Directive parseDirective(Reader reader) throws IOException {
//                        StringWriter directiveWriter = new StringWriter();
//                        int c = -1;
//                        directiveWriter.write("<%@");
//
//                        while ((c = reader.read()) != -1) {
//                                if (c == '%') {
//                                        c = reader.read();
//                                        if (c != '>') {
//                                                directiveWriter.write('%');
//                                                directiveWriter.write(c);
//                                        } else {
//                                                directiveWriter.write("%>");
//                                                break;
//                                        }
//                                } else {
//                                        directiveWriter.write(c);
//                                }
//                        }
//
//                        String parts[] = parseSections(directiveWriter.toString().trim());
//                        Directive d = new Directive(directiveWriter.toString(), parts, parts[1]);
//
//                        return d;
//                }
//
//                private String trimQuotes(String s) {
//                        String val = s.trim();
//
//                        if (val.charAt(0) == '\'' && val.charAt(val.length() - 1) == '\'') {
//                                return val.substring(1, val.charAt(val.length() - 1));
//                        } else if (val.charAt(0) == '\"' && val.charAt(val.length() - 1) == '\"') {
//                                return val.substring(1, val.charAt(val.length() - 1));
//                        }
//
//                        return s;
//                }
//
//                private String[] parseSections(String s) {
//                        List<String> sections = new ArrayList();
//                        boolean singleQuoteStart = false;
//                        boolean doubleQuoteStart = false;
//                        StringBuffer sb = new StringBuffer();
//                        for (int i = 0; i < s.length(); i++) {
//                                if (s.charAt(i) == '\"' && singleQuoteStart == false && doubleQuoteStart == false) {
//                                        doubleQuoteStart = true;
//                                        continue;
//                                } else if (s.charAt(i) == '\'' && singleQuoteStart == false && doubleQuoteStart == true) {
//                                        doubleQuoteStart = false;
//                                        continue;
//                                }
//
//                                if (s.charAt(i) == '\'' && singleQuoteStart == false && doubleQuoteStart == false) {
//                                        singleQuoteStart = true;
//                                        continue;
//                                } else if (s.charAt(i) == '\'' && singleQuoteStart == true && doubleQuoteStart == false) {
//                                        singleQuoteStart = false;
//                                        continue;
//                                }
//
//                                if (singleQuoteStart || doubleQuoteStart) {
//                                        sb.append(s.charAt(i));
//                                        continue;
//                                }
//
//                                if (s.charAt(i) != ' ') {
//                                        sb.append(s.charAt(i));
//                                } else {
//                                        sections.add(sb.toString());
//                                        sb.setLength(0);
//                                }
//                        }
//
//                        String[] sc = new String[sections.size()];
//                        sections.toArray(sc);
////                        for (String x : sc) {
////                                System.out.println("sections: " + x);
////                        }
////
////                        System.out.println("");
//                        return sc;
//                }
//
//                private void processIncludeDirective(StringWriter sw, String path) throws IncludeDirectiveException, InheritedTemplateException, ParentTemplateException, ScriptBlockException {
//                        try {
//                                String templateRoot;
//                                File f = null;
//                                if (path.charAt(0) == '/') {      //root request
//
//                                        if (File.separator.equals("/")) {
//                                                f = new File(RequestThreadInfo.get().getApplication().getAppPath() + path.trim());
//                                        } else {
//                                                f = new File(RequestThreadInfo.get().getApplication().getAppPath() + path.trim().replaceAll("/", "\\\\"));
//                                        }
//                                        templateRoot = RequestThreadInfo.get().getApplication().getAppPath();
//                                } else {
//                                        if (File.separator.equals("/")) {
//                                                f = new File(RequestThreadInfo.get().getTemplateInfo().getTemplateRoot() + File.separator + path.trim());
//                                        } else {
//                                                f = new File(RequestThreadInfo.get().getTemplateInfo().getTemplateRoot() + File.separator + path.trim().replaceAll("/", "\\\\"));
//                                        }
//
//                                        templateRoot = RequestThreadInfo.get().getTemplateInfo().getTemplateRoot();
//                                }
//
//                                RequestThreadInfo.get().getTemplateInfo().getChildren().add(f.getAbsolutePath());
//
////                                List entry = RequestThreadInfo.get().getTemplateInfo().getCache();
////                                entry.add(f.getAbsolutePath());
//
//                                //log.fine("including file : " + f.getAbsolutePath() + ",  into parent file: " + f.getParentFile().getCanonicalPath());
//
//                                SimpleTemplate template = new SimpleTemplate();
//                                String script = null;
//
//                                RequestThreadInfo.get().getTemplateInfo().setTemplateRoot(f.getParentFile().getCanonicalPath());
//                                script = template.parse(new FileReader(f), false, new InheritedTemplateInfo());
//                                RequestThreadInfo.get().getTemplateInfo().setTemplateRoot(templateRoot);
//
//                                sw.write(script);
//                                sw.write("rout.print(\"");
//                        } catch (IOException e) {
//                                throw new IncludeDirectiveException(" error processing include directive for path: " + path, e);
//                        }
//
//                }
//
//                private void processGSstring(Reader reader, StringWriter sw) throws IOException {
//                        int c;
//                        while ((c = reader.read()) != -1) {
//                                if (c != '\n' && c != '\r') {
//                                        sw.write(c);
//                                }
//                                if (c == '}') {
//                                        break;
//                                }
//                        }
//                }
//
//                private void closeReader(Reader reader) {
//                        if (reader != null) {
//                                try {
//                                        reader.close();
//                                } catch (IOException ignore) {
//                                        // e.printStackTrace();
//                                }
//                        }
//
//
//                }
//
//                /**
//                 * Closes the currently open write and writes out the following text as a GString expression until it reaches an end %>.
//                 *
//                 * @param reader a reader for the template text
//                 * @param sw     a StringWriter to write expression content
//                 * @throws IOException if something goes wrong
//                 */
//                private void groovyExpression(Reader reader, StringWriter sw) throws IOException {
//                        sw.write("\");rout.print(\"${");
//                        int c;
//                        while ((c = reader.read()) != -1) {
//                                if (c == '%') {
//                                        c = reader.read();
//                                        if (c != '>') {
//                                                sw.write('%');
//                                        } else {
//                                                break;
//                                        }
//                                }
//                                if (c != '\n' && c != '\r') {
//                                        sw.write(c);
//                                }
//                        }
//                        sw.write("}\");\nrout.print(\"");
//                }
//
//                /**
//                 * Closes the currently open write and writes the following text as normal Groovy script code until it reaches an end %>.
//                 *
//                 * @param reader a reader for the template text
//                 * @param sw     a StringWriter to write expression content
//                 * @throws IOException if something goes wrong
//                 */
//                private void groovySection(Reader reader, StringWriter sw) throws IOException {
//                        sw.write("\");");
//                        int c;
//                        while ((c = reader.read()) != -1) {
//                                if (c == '%') {
//                                        c = reader.read();
//                                        if (c != '>') {
//                                                sw.write('%');
//                                        } else {
//                                                break;
//                                        }
//                                }
//                                /* Don't eat EOL chars in sections - as they are valid instruction separators.
//                                * See http://jira.codehaus.org/browse/GROOVY-980
//                                */
//                                // if (c != '\n' && c != '\r') {
//                                sw.write(c);
//                                //}
//                        }
//                        sw.write(";\nrout.print(\"");
//                }
//
//                public static class InheritedTemplateInfo {
//                        boolean inherited = false;
//                        StringWriter mergedContents;
//                        String parentTemplate;
//                        String childTemplate;
//
//                        public InheritedTemplateInfo() {
//
//                        }
//
//                        public InheritedTemplateInfo(boolean inherited, StringWriter mergedContents) {
//                                this.inherited = inherited;
//                                this.mergedContents = mergedContents;
//                        }
//
//                        public boolean isInherited() {
//                                return inherited;
//                        }
//
//                        public void setInherited(boolean inherited) {
//                                this.inherited = inherited;
//                        }
//
//                        public StringWriter getMergedContents() {
//                                return mergedContents;
//                        }
//
//                        public void setMergedContents(StringWriter mergedContents) {
//                                this.mergedContents = mergedContents;
//                        }
//
//                        public String getParentTemplate() {
//                                return parentTemplate;
//                        }
//
//                        public void setParentTemplate(String parentTemplate) {
//                                this.parentTemplate = parentTemplate;
//                        }
//
//                        public String getChildTemplate() {
//                                return childTemplate;
//                        }
//
//                        public void setChildTemplate(String childTemplate) {
//                                this.childTemplate = childTemplate;
//                        }
//                }
//
//                private class BlockContents {
//                        Directive directive;
//                        String contents;
//
//                        BlockContents(Directive directive, String contents) {
//                                this.directive = directive;
//                                this.contents = contents;
//                        }
//
//                        public Directive getDirective() {
//                                return directive;
//                        }
//
//                        public void setDirective(Directive directive) {
//                                this.directive = directive;
//                        }
//
//                        public String getContents() {
//                                return contents;
//                        }
//
//                        public void setContents(String contents) {
//                                this.contents = contents;
//                        }
//
//                        @Override
//                        public String toString() {
//                                return contents;
//                        }
//                }
//
//                private class Directive {
//                        String directive;
//                        String parts[];
//                        String name;
//
//                        public Directive(String directive, String[] parts, String name) {
//                                this.directive = directive;
//                                this.parts = parts;
//                                this.name = name;
//                        }
//
//                        public boolean isStartBlock() {
//                                return name.equalsIgnoreCase("block");
//                        }
//
//                        public boolean isEndBlock() {
//                                return name.equalsIgnoreCase("endblock");
//                        }
//
//                        public boolean isInclude() {
//                                return name.equalsIgnoreCase("include");
//                        }
//
//                        public boolean isScriptBlock() {
//                                return name.equalsIgnoreCase("script");
//                        }
//
//                        public boolean isEnd() {
//                                return name.equalsIgnoreCase("endscript");
//                        }
//
//                        public boolean isInheritedTemplated() {
//                                return name.equalsIgnoreCase("extends");
//                        }
//
//                        public String getBlockName() {
//                                if (parts.length > 2)
//                                        return parts[2];
//                                else
//                                        return "";
//
//                        }
//
//                        public boolean passThruParentContent() {
//                                if (parts.length > 3) {
//                                        return parts[3].equalsIgnoreCase("add");
//                                } else
//                                        return false;
//
//                        }
//
//                }
//        }
//
//
//}
