package org.springframework.data.redis.samples.retwisj.web;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.RetwisRepository;
import org.springframework.data.redis.samples.retwisj.RetwisSecurity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CookieInterceptor extends HandlerInterceptorAdapter {

	public static final String RETWIS_COOKIE = "retwisauth";

	@Inject
	private RetwisRepository twitter;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// all non-root requests get analyzed
		Cookie[] cookies = request.getCookies();

		if (!ObjectUtils.isEmpty(cookies)) {
			
			for (Cookie cookie : cookies) {
				if (RETWIS_COOKIE.equals(cookie.getName())) {
					String auth = cookie.getValue();
					String name = twitter.findNameForAuth(auth);
					if (name != null) {
						String uid = twitter.findUid(name);
						RetwisSecurity.setUser(name, uid);
					}
				}
			}
			
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		RetwisSecurity.clean();
	}

}
