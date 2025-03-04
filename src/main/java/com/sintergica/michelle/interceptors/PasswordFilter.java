package com.sintergica.michelle.interceptors;

import com.sintergica.michelle.configuration.DBConfig;
import com.sintergica.michelle.configuration.Logger;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class PasswordFilter implements Filter {
	private final DBConfig dbConfig;
	private final Logger logger;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (isPasswordCorrect(httpRequest)) {
			chain.doFilter(request, response);
		} else {
			logger.logText("REQUEST ATTEMPT: INCORRECT PASSWORD");
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	public boolean isPasswordCorrect(HttpServletRequest request) {
		String headerPassword = getHeaderPassword(request);
		return headerPassword != null
			&& headerPassword.equals(dbConfig.getPassword());
	}

	private String getHeaderPassword(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null && !header.isEmpty()) {
			String[] strings = header.split(" ");
			if (strings.length > 0) {
				return strings[strings.length - 1];
			}
		}
		return null;
	}
}
