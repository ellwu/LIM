package lim.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lim.util.StringUtil;

public class SessionManager {
	private List<Session> sessions;
	private Map<String, Session> idsMap;
	
	private static SessionManager SESSION_MANAGER = new SessionManager();
	
	private SessionManager(){
		sessions = new ArrayList<Session>();
		idsMap = new HashMap<String, Session>();
	}
	
	public static SessionManager getInstance(){
		return SESSION_MANAGER;
	}

	public void add(Session session) {
		this.sessions.add(session);
		this.idsMap.put(session.getId(), session);
	}

	public void removeById(long ioSessionId) {
		String strId = ioSessionId + "";
		removeById(strId);
	}
	
	public void removeById(String ioSessionId) {
		Session session = this.idsMap.get(ioSessionId);
		if (session != null) {
			this.sessions.remove(session);
			this.idsMap.remove(ioSessionId);
		}
	}
	
	public Session getById(long ioSessionId){
		String strId = ioSessionId + "";
		return this.idsMap.get(strId);
	}
	
	public Session getByNick(String nick){
		if(StringUtil.isEmpty(nick)){
			return null;
		}
		for (Session sess : this.sessions) {
			if (nick.equals(sess.getNick())) {
				return sess;
			}
		}
		return null;
	}

	public List<Session> getByRoom(String room) {
		if(StringUtil.isEmpty(room)){
			return getSessionWithoutRoom();
		}
		List<Session> matchSessions = new ArrayList<Session>();
		
		for (Session sess : this.sessions) {
			if (room.equals(sess.getRoom())) {
				matchSessions.add(sess);
			}
		}
		
		return matchSessions;
	}
	
	public List<Session> getAll(){
		return this.sessions;
	}
	
	public List<Session> getSessionWithoutRoom() {
		List<Session> matchSessions = new ArrayList<Session>();
		
		for (Session sess : this.sessions) {
			if (StringUtil.isEmpty(sess.getRoom())) {
				matchSessions.add(sess);
			}
		}
		
		return matchSessions;
	}
}
