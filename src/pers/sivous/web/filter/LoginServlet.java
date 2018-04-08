package pers.sivous.web.filter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.sivous.service.LoginService;
import pers.sivous.utils.GetMd5Cookie;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/servlet/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		LoginService ls = new LoginService();
		if(ls.Login(username, password)) {
			//给用户设置cookie username:expirestime:md5(password:expirestime:username)
			try {
				//构造cookie并发送
				Cookie cookie = GetMd5Cookie.createCookie(username, 6000, password);
				response.addCookie(cookie);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.getRequestDispatcher("/WEB-INF/jsp/loginSucceed.jsp").forward(request, response);
		}else {
			String message = "登录失败";
			request.setAttribute("message", message);
			request.getRequestDispatcher("/WEB-INF/jsp/message.jsp").forward(request, response);
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
