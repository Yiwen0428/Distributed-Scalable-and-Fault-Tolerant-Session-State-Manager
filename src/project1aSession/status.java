package project1aSession;

import javax.servlet.http.Cookie;

public class status {
	private Cookie cookie = null;
	private session s = null;
	status(){
		
	}
	status(Cookie c,session s) {
		this.cookie = c;
		this.s = s;
	}
	
	public void setCookie(Cookie c) {
		this.cookie = c;
	}
	public Cookie getCookie(){
		return this.cookie;
	}
	
	public void setSession(session s) {
		this.s = s;
	}
	public session getSession() {
		return this.s;
	}
}
