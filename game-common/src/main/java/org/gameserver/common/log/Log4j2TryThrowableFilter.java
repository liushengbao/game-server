package org.gameserver.common.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;

/** 拦截全局异常 过滤器 */
@Plugin(name = "Log4j2TryThrowableFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
public class Log4j2TryThrowableFilter extends AbstractFilter {

	public static final Logger logger = LogManager.getLogger(Log4j2TryThrowableFilter.class);

	public Log4j2TryThrowableFilter() {
		super(Result.ACCEPT, Result.NEUTRAL);
	}

	@Override
	//所有的日志都需要经过这里
	public Result filter(LogEvent event) {
		if (event.getThrown() != null) { //只处理Throwable类型
			logger.error("{} - {} ", event.getLoggerName(), event.getMessage().getFormattedMessage(), event.getThrown());
		}
		return super.filter(event);
	}
	
	
	@PluginFactory
	public static Log4j2TryThrowableFilter createFilter(@PluginConfiguration final Configuration config) {
		return new Log4j2TryThrowableFilter();
	}

}
