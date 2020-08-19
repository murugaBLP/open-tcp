package com.iot.service.utils;

/**
 * @author Muruganandam
 *
 */

public interface Constants {

	public static final String configFileName = "/../conf/config.properties";
	public static final String loggerFileName = "/../conf/logger_service.properties";
	public static final String PORTS = "port";
	public static final String SPLIT_STR = "<";
	public static final String STR_STARTS_WITH = ",";
	public static final String STR_ENDS_WITH = ";";
	public static final String LIVE_URL = "live_url";
	public static final String HISTORY_URL = "history_url";
	public static final String CONTENT_TYPE = "Content-type";
	public static final String APP_JSON = "application/json";
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final Integer REFRESH_TIME = 60000;
	public static final String PARSER_CONF_FILE = "/../conf/parserConfig.json";
	public static final String INFO_CONF_FILE = "/../conf/deviceInfo.json";
	public static final String DEVICE_INFO = "device_info";
	public static final String PARSER_INFO = "parser_info";
	public static final String EMAIL_ID = "mail_id";
	public static final String EMAIL_PASS = "password";
	public static final String EMAIL_SUB = "email_subject";
	public static final String EMAIL_MSG = "email_text";
	public static final String EMAIL_TXT = "email_text";
	public static final String EMAIL_HOST = "mail.smtp.host";
	public static final String EMAIL_HOST_SMTP = "smtp.gmail.com";
	public static final String EMAIL_SMTP_PORT = "mail.smtp.port";
	public static final String EMAIL_SMTP_HOST_PORT = "587";
	public static final String EMAIL_AUTH = "mail.smtp.auth";
	public static final String EMAIL_AUTH_TRUE = "true";
	public static final String EMAIL_SMTP_ENABLED = "mail.smtp.starttls.enable";
	public static final String EMAIL_INTERVAL = "email_interval";
	public static final String DATA_TYPR = "Modbus";
	public static final String UN_ONE = "_1";
	public static final String UN_ZERO = "_0";
	public static final String STARTS_ZERO = "0";
	public static final String SPLIT_COM = ",";
	public static final String HITS = "hits";
	public static final String SOURCE = "_source";
	public static final String IMEI_NO = "imeiNo";
}
