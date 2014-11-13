package lim.command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lim.session.Session;
import lim.session.SessionManager;
import lim.util.StringUtil;

public class CommonCommands {
	public void nick(List params, Session session) {
		if (params.size() == 0) {
			return;
		}
		String newNick = (String) params.get(0);

		if (!StringUtil.isEmpty(newNick)) {
			session.setNick(newNick);
		}
	}

	public void room(List params, Session session) {
		if (params.size() == 0) {
			return;
		}

		String newRoom = (String) params.get(0);

		if (!StringUtil.isEmpty(newRoom)) {
			session.setRoom(newRoom);
		}
	}

	public void ls(List params, Session session) {
		String room = null;
		List<Session> sessions = null;

		if (params.size() > 0) {
			room = (String) params.get(0);
			sessions = SessionManager.getInstance().getByRoom(room);
		} else {
			sessions = SessionManager.getInstance().getSessionWithoutRoom();
		}

		StringBuilder roomInfo = new StringBuilder();

		if (StringUtil.isEmpty(room)) {
			room = "*Hall*";
		}

		roomInfo.append(room).append(":\n");

		for (Session sess : sessions) {
			roomInfo.append(sess.getNick()).append(" [")
					.append(StringUtil.empty(sess.getStatus(), "online")).append("] ")
					.append("\n");
		}
		
		if(sessions == null || sessions.size() == 0){
			roomInfo.append("(empty)");
		}

		replyCommand(roomInfo.toString(), session);
	}
	
	public void pwd(List params, Session session) {
		replyCommand(StringUtil.empty(session.getRoom(), "*Hall*"), session);
	}

	public void status(List params, Session session) {
		if (params.size() == 0) {
			return;
		}
		String newStatus = (String) params.get(0);

		if (!StringUtil.isEmpty(newStatus)) {
			session.setStatus(newStatus);
		}
	}

	public void lsr(List params, Session session) {
		HashMap<String, Integer> rooms = new HashMap<String, Integer>();
		
		List<Session> sessions = SessionManager.getInstance().getAll();
		String room = null;
		Integer count = null;
		for(Session sess : sessions){
			room = sess.getRoom();
			
			count = rooms.get(room);
			
			if(count == null){
				count = 1;
			}else{
				count += 1;
			}
			
			rooms.put(room, count);
		}
		
		StringBuilder roomsInfo = new StringBuilder();
		
		for(String r : rooms.keySet()){
			roomsInfo.append(StringUtil.empty(r, "*Hall*")).append(" [").append(rooms.get(r)).append("]\n");
		}
		
		replyCommand(roomsInfo.toString(), session);
	}

	public void help(List params, Session session) {
		StringBuilder helpInfo = new StringBuilder();

		helpInfo.append(
				"help (command): \n\tget command help. (): optional, []: required.")
				.append("\n");
		helpInfo.append("status [status]:\n\tset status").append("\n");
		helpInfo.append("ls (root): \n\tlist room members").append("\n");
		helpInfo.append("nick [nick]: \n\tset new nick name").append("\n");
		helpInfo.append("room [room]: \n\tcreate new room").append("\n");
		helpInfo.append("lsr: \n\tlist all rooms").append("\n");
		helpInfo.append("q: \n\tquit current room or quit app").append("\n");
		helpInfo.append("to [nick] [message]: \n\tquit current room or quit app").append("\n");
		helpInfo.append("broad [room] [message]: \n\tbroad message to room members").append("\n");

		replyCommand(helpInfo.toString(), session);
	}
	
	public void q(List params, Session session){
		
		String room = session.getRoom();
		
		if(StringUtil.isEmpty(room)){
			replyCommand("Will leave this app", session);
			SessionManager.getInstance().removeById(session.getId());
			session.getIoSession().close(true);
		}else{
			replyCommand("Will leave room " + session.getRoom(), session);
			session.setRoom(null);
		}
	}
	
	public void to(List params, Session session){
		if(params == null || params.size() < 2){
			return;
		}
		
		String toNick = (String) params.get(0);
		StringBuilder message = new StringBuilder();
		
		for(int i = 1; i < params.size(); i++){
			message.append(params.get(i)).append(" ");
		}
		
		Session toSession = SessionManager.getInstance().getByNick(toNick);
		if(toSession == null){
			return;
		}
		
		StringBuilder msg = new StringBuilder();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String datetime = sdf.format(new Date());
        msg.append("[").append(datetime).append("] ");
        
        String room = session.getRoom();
        if(StringUtil.isEmpty(room)){
        	msg.append("[").append(session.getNick()).append("] ");
        }else{
        	msg.append("[").append(room).append("/").append(session.getNick()).append("] ");
        }
        
        msg.append(message.toString());
		
		toSession.write(msg);
	}
	
	public void broad(List params, Session session){
		if(params == null || params.size() < 2){
			return;
		}
		
		String toRoom = (String) params.get(0);
		String message = (String) params.get(1);
		
		if("*Hall*".equals(toRoom)){
			toRoom = null;
		}
		
		List<Session> toSessions = SessionManager.getInstance().getByRoom(toRoom);
		if(toSessions == null || toSessions.size() == 0){
			return;
		}
		
		StringBuilder msg = new StringBuilder();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String datetime = sdf.format(new Date());
        msg.append("[").append(datetime).append("] ");
        
        String room = session.getRoom();
        if(StringUtil.isEmpty(room)){
        	msg.append("[").append(session.getNick()).append("] ");
        }else{
        	msg.append("[").append(room).append("/").append(session.getNick()).append("] ");
        }
        
        msg.append(message);
		
        for(Session sess : toSessions){
        	sess.write(msg);
        }
	}
	
	public void replyCommand(String msg, Session session){
		StringBuilder message = new StringBuilder();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String datetime = sdf.format(new Date());
        message.append("[").append(datetime).append("] ");
        message.append("[").append("*lim*").append("]\n");
        
        message.append(msg);
		session.write(message.toString());
	}
}
