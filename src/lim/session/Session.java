package lim.session;

import lim.util.StringUtil;

import org.apache.mina.core.session.IoSession;

public class Session{
	private String id;
	private String nick;
	private String room;
	private String status;
	private IoSession ioSession;
	
	public Session(IoSession ioSession){
		this.ioSession = ioSession;
		String addr = ioSession.getRemoteAddress().toString();
		
		this.setId(ioSession.getId() + "");
		this.nick = StringUtil.removeFirstLetter(addr);
		this.room = null;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public IoSession getIoSession() {
		return ioSession;
	}

	public void setIoSession(IoSession ioSession) {
		this.ioSession = ioSession;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	} 
	
	public void write(Object content){
		this.ioSession.write(content);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
