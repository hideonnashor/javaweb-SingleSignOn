package pers.sivous.web.filter;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.sivous.service.LoginService;
import sun.misc.BASE64Encoder;

public class MyFilter01 implements Filter {
	
	private FilterConfig fg;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("filter01 destory");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		res.setDateHeader("Expires", -1);
		res.setHeader("Cache-Contr", "no-cache");
		res.setHeader("Pragma", "no-cache");
		
		//先检查是否有登陆
		String state = (String) req.getSession().getAttribute("state");
		if(state!=null) {
			req.getSession().setAttribute("message", "已获取state:"+req.getSession().getAttribute("state"));
			chain.doFilter(req, res);
			return;
		}
		//没登陆的情况
		//获取cookie
		Cookie cookie = null;
		Cookie cookies[] = req.getCookies();
		//cookie都没有
		if (cookies == null) {
			req.getSession().setAttribute("message","没有cookie");
			chain.doFilter(req, res);
			return;
		}
		for(int i =0;i<cookies.length;i++) {
			if(cookies[i].getName().equals("autologin")) {
				cookie = cookies[i];
				//检查有效期
				String values[] = cookie.getValue().split(":");
				if (values.length<3) {
					req.getSession().setAttribute("message","cookie长度不合法");
					chain.doFilter(req, res);
					return;
				}
				Long expiretime = Long.parseLong(values[1]);
				if (System.currentTimeMillis()>expiretime) {
					req.getSession().setAttribute("message","cookie时间超时");
					chain.doFilter(req, res);
					return;
				}
				//检查有效性
				String username = values[0];
				String password = values[2];
						
				System.out.println(username+password);
				//从“数据库”取得数据
				LoginService ls = new LoginService();
				Iterator it = ls.getMap().entrySet().iterator();
				while(it.hasNext()) {
					Entry entry = (Entry) it.next();
					String name = (String) entry.getKey();
					String pwd = (String) entry.getValue();
					System.out.println(name+pwd);
					//对密码进行md5
					MessageDigest md;
					try {
						md = MessageDigest.getInstance("md5");
						BASE64Encoder encoder = new BASE64Encoder();
						String value02 = pwd;
						byte result02[] = md.digest(value02.getBytes());
						pwd = encoder.encode(result02);
						System.out.println(name+pwd);
						if (username.equals(name)&&password.equals(pwd)) {
							req.getSession().setAttribute("username", username);
							req.getSession().setAttribute("message","cookie核对正确");
							req.getRequestDispatcher("/WEB-INF/jsp/loginSucceed.jsp").forward(req, res);
							chain.doFilter(req, res);
							return;
						}else {
							req.getSession().setAttribute("message","cookie信息不对,数据库："+name+","+pwd+"cookie中："+username+","+password);
							chain.doFilter(req, res);
							return;
						}
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
				}
				req.getSession().setAttribute("message","进入到cookie有效性检验，但直接放行"+cookie.getName());
				chain.doFilter(req, res);
			}
			req.getSession().setAttribute("message","进入cookie校检,但直接放行,循环执行到"+i+"名为"+cookies[i].getName());
		}
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		this.fg = filterConfig;
		System.out.println("filter01 init");
	}

}
