package com.baishop.velocity;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.app.event.EventHandlerUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.directive.Parse;
import org.apache.velocity.runtime.directive.StopCommand;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

public class ImportDirective extends Parse {

	private int maxDepth;

	@Override
	public String getName() {
		return "import";
	}

	@Override
	public int getType() {
		return LINE;
	}

	/**
	 * iterates through the argument list and renders every argument that is
	 * appropriate. Any non appropriate arguments are logged, but render()
	 * continues.
	 * 
	 * @param context
	 * @param writer
	 * @param node
	 * @return True if the directive rendered successfully.
	 * @throws IOException
	 * @throws ResourceNotFoundException
	 * @throws ParseErrorException
	 * @throws MethodInvocationException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException, ResourceNotFoundException,
			ParseErrorException, MethodInvocationException {
		/*
		 * did we get an argument?
		 */
		if (node.jjtGetNumChildren() == 0) {
			throw new VelocityException("#parse(): argument missing at "
					+ Log.formatFileString(this));
		}

		/*
		 * does it have a value? If you have a null reference, then no.
		 */
		Object value = node.jjtGetChild(0).value(context);
		if (value == null && rsvc.getLog().isDebugEnabled()) {
			rsvc.getLog().debug(
					"#parse(): null argument at " + Log.formatFileString(this));
		}

		/*
		 * get the path linpn -- 修改本处代码
		 */
		String sourcearg = value == null ? null : value.toString();
		if (!sourcearg.startsWith("/")) {
			String uri = context.getCurrentTemplateName();
			String path = uri.lastIndexOf("/")==-1?"":uri.substring(0, uri.lastIndexOf("/"));
			sourcearg = path + "/" + sourcearg;
		}

		/*
		 * check to see if the argument will be changed by the event cartridge
		 */
		String arg = EventHandlerUtil.includeEvent(rsvc, context, sourcearg,
				context.getCurrentTemplateName(), getName());

		/*
		 * a null return value from the event cartridge indicates we should not
		 * input a resource.
		 */
		if (arg == null) {
			// abort early, but still consider it a successful rendering
			return true;
		}

		if (maxDepth > 0) {
			/*
			 * see if we have exceeded the configured depth.
			 */
			Object[] templateStack = context.getTemplateNameStack();
			if (templateStack.length >= maxDepth) {
				StringBuffer path = new StringBuffer();
				for (int i = 0; i < templateStack.length; ++i) {
					path.append(" > " + templateStack[i]);
				}
				rsvc.getLog().error(
						"Max recursion depth reached (" + templateStack.length
								+ ')' + " File stack:" + path);
				return false;
			}
		}

		/*
		 * now use the Runtime resource loader to get the template
		 */

		Template t = null;

		try {
			t = rsvc.getTemplate(arg, getInputEncoding(context));
		} catch (ResourceNotFoundException rnfe) {
			/*
			 * the arg wasn't found. Note it and throw
			 */
			rsvc.getLog().error(
					"#parse(): cannot find template '" + arg + "', called at "
							+ Log.formatFileString(this));
			throw rnfe;
		} catch (ParseErrorException pee) {
			/*
			 * the arg was found, but didn't parse - syntax error note it and
			 * throw
			 */
			rsvc.getLog().error(
					"#parse(): syntax error in #parse()-ed template '" + arg
							+ "', called at " + Log.formatFileString(this));
			throw pee;
		}
		/**
		 * pass through application level runtime exceptions
		 */
		catch (RuntimeException e) {
			rsvc.getLog().error(
					"Exception rendering #parse(" + arg + ") at "
							+ Log.formatFileString(this));
			throw e;
		} catch (Exception e) {
			String msg = "Exception rendering #parse(" + arg + ") at "
					+ Log.formatFileString(this);
			rsvc.getLog().error(msg, e);
			throw new VelocityException(msg, e);
		}

		/**
		 * Add the template name to the macro libraries list
		 */
		List macroLibraries = context.getMacroLibraries();

		/**
		 * if macroLibraries are not set create a new one
		 */
		if (macroLibraries == null) {
			macroLibraries = new ArrayList();
		}

		context.setMacroLibraries(macroLibraries);

		macroLibraries.add(arg);

		/*
		 * and render it
		 */
		try {
			preRender(context);
			context.pushCurrentTemplateName(arg);

			((SimpleNode) t.getData()).render(context, writer);
		} catch (StopCommand stop) {
			if (!stop.isFor(this)) {
				throw stop;
			}
		}
		/**
		 * pass through application level runtime exceptions
		 */
		catch (RuntimeException e) {
			/**
			 * Log #parse errors so the user can track which file called which.
			 */
			rsvc.getLog().error(
					"Exception rendering #parse(" + arg + ") at "
							+ Log.formatFileString(this));
			throw e;
		} catch (Exception e) {
			String msg = "Exception rendering #parse(" + arg + ") at "
					+ Log.formatFileString(this);
			rsvc.getLog().error(msg, e);
			throw new VelocityException(msg, e);
		} finally {
			context.popCurrentTemplateName();
			postRender(context);
		}

		/*
		 * note - a blocked input is still a successful operation as this is
		 * expected behavior.
		 */

		return true;
	}

}
