package project1aSession;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import java.sql.Timestamp;


@WebServlet("/project1a")
public class server extends HttpServlet{

	protected static final int timeoutSecs = 30;
	protected static final String locationmetadata = "US East";
	protected static final String cookieName = "CS5300PROJ1SESSION";
	protected static ConcurrentHashMap<String, session> sessionStateTable = new ConcurrentHashMap<String, session>();

    String message = "Hello User";

	 @Override
	  public void doGet(HttpServletRequest request,
	                    HttpServletResponse response)
	      throws ServletException, IOException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	   
	  //  status updateStatus = this.updateStatus(request, response);
	    
	    String submit = request.getParameter("submit");
	    
	    if(submit!=null) {
	    	if(submit.equals("Replace")) {
	    		this.message = request.getParameter("replaceValue");
	    	}else if(submit.equals("Logout")) {
	    		status updateStatus = this.updateStatus(request, response);
	    		Cookie c = updateStatus.getCookie();
	    		session s = updateStatus.getSession();
	    		c.setMaxAge(0); 
				response.addCookie(c);
				sessionStateTable.remove(s.getSessionID());
	    		out.println("<!DOCTYPE html>");
	    		out.println("<html><head></head><body>");
	    		out.println("You have been logged out.");
	    		out.println("</body></html>");
	    		message = "Hello User";
	    		return;
	    	}
	    }
	    
	    //updateStatus contains cookie and session  
		status updateStatus = this.updateStatus(request, response); 
		try{
			//FileReader fr=new FileReader("/CS5300Proj1a/WebContent/resource/index.html");
			FileReader fr=new FileReader(getServletContext().getRealPath("resource/index.html"));
			BufferedReader br= new BufferedReader(fr);
			String content="";
			String s = "";
			while((s=br.readLine())!=null) {
				content=content+s;
			} 
			//replace the index.html with actual content in session
			content = content.replaceAll("sessionId",updateStatus.getSession().getSessionID());
			content = content.replaceAll("versionNumber",""+updateStatus.getSession().getVersionNumebr());
			Timestamp curTime = new Timestamp(System.currentTimeMillis());
			content = content.replaceAll("currentDate",curTime.toString());
			content = content.replaceAll("Hello User", updateStatus.getSession().getMessage());
			content = content.replaceAll("cookieNumber",updateStatus.getCookie().getValue());
			content = content.replaceAll("expiresDate",updateStatus.getSession().getExpireTime().toString());
			out.println(content);
			//System.out.println(content);

		}
		catch(Exception ex) {
			out.println(ex);
		}
	    
	  }
	 
	 
	 
	 //to create a new cookie with a newSession
	 public Cookie createCookie( HttpServletResponse response,session newSession) {
		 String cookieValue = newSession.getSessionID()+","+newSession.getVersionNumebr()+","+newSession.getLocationMetadata();
		 Cookie newCookie = new Cookie(cookieName,cookieValue);
		 newCookie.setMaxAge(timeoutSecs);
		 response.addCookie(newCookie);
		 return newCookie;
	 }
	 
	 //given a cookie c, finds the relative saved session in sessionStateTable
	 public session getSessionFromCookie(Cookie c) {
		 String sessionValue = c.getValue();
		 String[] sessionPieces = sessionValue.split(",");
		 session s = sessionStateTable.get(sessionPieces[0]);
		 return s;
	 }
	 
	 public status updateStatus(HttpServletRequest request,
			 HttpServletResponse response) {
		 status update = new status();
		 
		 Cookie[] cookies = request.getCookies();
		    Cookie c = null;
		    if(cookies!=null && cookies.length>0) {
		    	for(Cookie cookie : cookies){
		    		if(cookie.getName().equals(cookieName)){
		    			c = cookie;
		    		}
		    	}
		    }
		    
		    if(c==null) {
		    	//if cookie is null or do not have cookie named "CS5300PROJ1SESSION"
		    	//we create newSession and newCookie
		    	session newSession = new session(timeoutSecs*1000,locationmetadata,"Hello User");
		    	sessionStateTable.put(newSession.getSessionID(), newSession);
		    	Cookie newCookie = this.createCookie(response, newSession);
		    	response.addCookie(newCookie);
		    	update.setCookie(newCookie);
		    	update.setSession(newSession);
		    	
		    }else {
		    	//if we find the cookie and relative session in sessionStateTable
		    	session curSession = this.getSessionFromCookie(c);
		    	Timestamp curTime = new Timestamp(System.currentTimeMillis());
		    	//if cookie is expired
		    	// we remove the session, expire the cookie, create new session and new cookie
		    	if(curSession.getExpireTime().before(curTime)) {
		    		sessionStateTable.remove(curSession.getSessionID());
		    		c.setMaxAge(0); 
					response.addCookie(c);
					//we create newSession and newCookie
			    	session newSession = new session(timeoutSecs*1000,locationmetadata,message);
			    	sessionStateTable.put(newSession.getSessionID(), newSession);
			    	Cookie newCookie = this.createCookie(response, newSession);
			    	response.addCookie(newCookie);
			    	update.setCookie(newCookie);
			    	update.setSession(newSession);
		    	}else {
		    		//cookie not expired, just update the session
		    		curSession.increaseVersionNumber();
		    		curSession.setMessage(message);
		    		sessionStateTable.replace(curSession.getSessionID(), curSession);
		    		c.setMaxAge(0); 
					response.addCookie(c);
					Cookie newCookie = this.createCookie(response, curSession);
					response.addCookie(newCookie);
			    	update.setCookie(newCookie);
			    	update.setSession(curSession);
		    	}
		    }
		    return update;
	 }
	
}
