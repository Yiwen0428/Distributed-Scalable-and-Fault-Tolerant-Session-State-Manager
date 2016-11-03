package project1aSession;

import java.sql.Timestamp;
import java.util.*;

public class session {
	private String sessionID = "";
	private int versionNumber = 0;
	private Timestamp expireTime = new Timestamp(0);
	private String locationmetadata = "";
	private String message="Hello User";
	
	session(int timeout_milliseconds,String metadata,String message) {
		this.setUUSessionID();
		this.setVersonNumber(0);
		this.expireTime = new Timestamp(System.currentTimeMillis()+timeout_milliseconds);
		this.setMessage(message);
	}
	
	
	public void setUUSessionID() {
		this.sessionID = UUID.randomUUID().toString();
	}
	public void setSessionID(String sID) {
		this.sessionID = sID;
	}
	public String getSessionID() {
		return this.sessionID;
	}
	
	
	public void setVersonNumber(int num) {
		this.versionNumber=num;
	}
	public void increaseVersionNumber(){
		this.versionNumber++;
	}
	public int getVersionNumebr() {
		return this.versionNumber;
	}
	
	public Timestamp getExpireTime(){
		return this.expireTime;
	}
	
	public void setLocationMetadata(String metadata) {
		this.locationmetadata = metadata;
	}
	public String getLocationMetadata() {
		return this.locationmetadata;
	}
	
	
	public void setMessage(String message){
		this.message = message;
	}
	public String getMessage() {
		return this.message;
	}
}
