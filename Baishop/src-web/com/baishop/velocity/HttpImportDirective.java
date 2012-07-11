package com.baishop.velocity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

public class HttpImportDirective extends Directive {

	@Override
	public String getName() {
		return "httpImport";
	}

	@Override
	public int getType() {
		return LINE;
	}
	
    public void init(RuntimeServices rs, InternalContextAdapter context,
            Node node) throws TemplateInitException {
    	super.init( rs, context, node );
    	
    }
	

	@Override
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException, ResourceNotFoundException,
			ParseErrorException, MethodInvocationException {
		
		if ( node == null ) {
            rsvc.getLog().error("#include() null argument");
            return false;
        }

        Object value = node.jjtGetChild(0).value(context);        
        if ( value == null) {
            rsvc.getLog().error("#include() null argument");
            return false;
        }
		
		HttpServletRequest request = (HttpServletRequest) context.get("request");
        HttpServletResponse response = (HttpServletResponse) context.get("response");
        
        HttpInclude httpInclude = new HttpInclude(request, response);
        httpInclude.include(value.toString(), writer);
		
		
		return true;
	}
	
	
	
	/**
	 * 
	 * <pre>
	 * 完成于&lt;jsp:include page''/>相同的功能
	 * 用于include其它页面以用于布局,可以用于在freemarker,velocity的servlet环境应用中直接include其它http请求
	 * </pre>
	 * 
	 * <br />
	 * <b>Freemarker及Velocity示例使用:</b>
	 * <pre>
	 * ${httpInclude.include("http://www.google.com")};
	 * ${httpInclude.include("/servlet/head?p1=v1&p2=v2")};
	 * ${httpInclude.include("/head.jsp")};
	 * ${httpInclude.include("/head.do?p1=v1&p2=v2")};
	 * ${httpInclude.include("/head.htm")};
	 * </pre>
	 * 
	 * @author badqiu
	 *
	 */
	public static class HttpInclude {
	        private final static Log log = LogFactory.getLog(HttpInclude.class);
	        
	        public static String sessionIdKey = "JSESSIONID";
	        
	    private HttpServletRequest request;
	    private HttpServletResponse response;
	    
	    public HttpInclude(HttpServletRequest request, HttpServletResponse response) {
	        this.request = request;
	        this.response = response;
	    }

        public String include(String includePath) {
	                StringWriter sw = new StringWriter(8192);
	                include(includePath,sw);
	                return sw.toString();
	    }

        public void include(String includePath,Writer writer) {
	        try {
	            if(isRemoteHttpRequest(includePath)) {
	                getRemoteContent(includePath,writer);
	            }else {
	                getLocalContent(includePath,writer);
	            }
	        } catch (ServletException e) {
	            throw new RuntimeException("include error,path:"+includePath+" cause:"+e,e);
	        } catch (IOException e) {
	            throw new RuntimeException("include error,path:"+includePath+" cause:"+e,e);
	        }
	    }
	        
	    private static boolean isRemoteHttpRequest(String includePath) {
	        return  includePath != null && (
	                                includePath.toLowerCase().startsWith("http://") ||
	                                includePath.toLowerCase().startsWith("https://")
	                        );
	    }

	    private void getLocalContent(String includePath,Writer writer) throws ServletException, IOException {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(8192);
	        
	        CustomOutputHttpServletResponseWrapper customResponse = new CustomOutputHttpServletResponseWrapper(response,writer,outputStream);
	        request.getRequestDispatcher(includePath).include(request, customResponse);
	        
	        customResponse.flushBuffer();
	        if(customResponse.useOutputStream) {
	                writer.write(outputStream.toString(response.getCharacterEncoding())); // response.getCharacterEncoding()有可能为null
	        }
	        writer.flush();
	    }
	    
	    // handle cookies and http query parameters encoding
	    // set inheritParams from request
	    private void getRemoteContent(String urlString,Writer writer) throws MalformedURLException, IOException {
	        URL url = new URL(getWithSessionIdUrl(urlString));
	                URLConnection conn = url.openConnection();
	        setConnectionHeaders(urlString, conn);
	        InputStream input = conn.getInputStream();
	        try {
	                Reader reader = new InputStreamReader(input,Utils.getContentEncoding(conn,response));
	                Utils.copy(reader,writer);
	        }finally {
	                if(input != null) input.close();
	        }
	        writer.flush();
	    }

        private void setConnectionHeaders(String urlString, URLConnection conn) {
        	conn.setReadTimeout(6000);
	        conn.setConnectTimeout(6000);
	        String cookie = getCookieString();
            conn.setRequestProperty("Cookie", cookie);
            // 用于支持 httpinclude_header.properties
            //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
            //conn.setRequestProperty("Host", url.getHost());
            if(log.isDebugEnabled()) {
                    log.debug("request properties:"+conn.getRequestProperties()+" for url:"+urlString);
            }
        }
        
        // add session id with url
        private String getWithSessionIdUrl(String url) {
                return url;
        }

	    private static final String SET_COOKIE_SEPARATOR="; ";
	        private String getCookieString() {
	                StringBuffer sb = new StringBuffer(64);
	                Cookie[] cookies = request.getCookies();
	                if(cookies != null ) {
	                        for(Cookie c : cookies) {
	                                if(!sessionIdKey.equals(c.getName())) {
	                                        sb.append(c.getName()).append("=").append(c.getValue()).append(SET_COOKIE_SEPARATOR);
	                                }
	                        }
	                }
	                
	                String sessionId = Utils.getSessionId(request);
	                if(sessionId != null) {
	                        sb.append(sessionIdKey).append("=").append(sessionId).append(SET_COOKIE_SEPARATOR);
	                }
	                return sb.toString();
	        }
	        
	        public static class CustomOutputHttpServletResponseWrapper extends HttpServletResponseWrapper {
	        public boolean useWriter = false;
	        public boolean useOutputStream = false;

	        private PrintWriter printWriter;
	        private ServletOutputStream servletOutputStream;
	        
	        public CustomOutputHttpServletResponseWrapper(HttpServletResponse response,final Writer customWriter,final OutputStream customOutputStream) {
	            super(response);
	            this.printWriter = new PrintWriter(customWriter);
	            this.servletOutputStream = new ServletOutputStream() {
	                @Override
	                public void write(int b) throws IOException {
	                    customOutputStream.write(b);
	                }
	                @Override
	                public void write(byte[] b) throws IOException {
	                    customOutputStream.write(b);
	                }
	                @Override
	                public void write(byte[] b, int off, int len)throws IOException {
	                    customOutputStream.write(b, off, len);
	                }
	            };
	        }
	        @Override
	        public PrintWriter getWriter() throws IOException {
	            if(useOutputStream) throw new IllegalStateException("getOutputStream() has already been called for this response");
	            useWriter = true;
	            return printWriter;
	        }
	        @Override
	        public ServletOutputStream getOutputStream() throws IOException {
	            if(useWriter) throw new IllegalStateException("getWriter() has already been called for this response");
	            useOutputStream = true;
	            return servletOutputStream;
	        }
	        
	        @Override
	        public void flushBuffer() throws IOException {
	            if(useWriter) printWriter.flush();
	            if(useOutputStream) servletOutputStream.flush();
	        }
	        
        }

	    static class Utils { 
	        static String getContentEncoding(URLConnection conn,HttpServletResponse response) {
	                String contentEncoding = conn.getContentEncoding();
	            if(conn.getContentEncoding() == null) {
	                contentEncoding = parseContentTypeForCharset(conn.getContentType());
	                if(contentEncoding == null) {
	                        contentEncoding =  response.getCharacterEncoding();
	                }
	            } else {
	                contentEncoding = conn.getContentEncoding();
	            }
	                return contentEncoding;
	        }
	        static Pattern p = Pattern.compile("(charset=)(.*)",Pattern.CASE_INSENSITIVE);
	        private static String parseContentTypeForCharset(String contentType) {
	                if(contentType == null) return null;
	                Matcher m = p.matcher(contentType);
	                if(m.find()) {
	                        return m.group(2).trim();
	                }
	                        return null;
	                }

	                private static void copy(Reader in, Writer out) throws IOException {
	            char[] buff = new char[8192];
	            while(in.read(buff) >= 0) {
	                out.write(buff);
	            }
	        }
	        
	        private static String getSessionId(HttpServletRequest request) {
	                HttpSession session = request.getSession(false);
	                if(session == null) {
	                        return null;
	                }
	                return session.getId();
	        }
	    }
	}

}
