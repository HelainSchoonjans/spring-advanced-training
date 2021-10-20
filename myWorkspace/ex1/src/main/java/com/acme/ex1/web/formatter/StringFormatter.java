package com.acme.ex1.web.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/*
allows to specify how spring should handle input parameters
here we want to ensure that empty strings are handled as null
 */
@Component
public class StringFormatter implements Formatter<String> {

	@Override
	public String print(String object, Locale locale) {
		return object;
	}

	@Override
	public String parse(String text, Locale locale) throws ParseException {
		return StringUtils.hasText(text) ? text : null;
	}
}
