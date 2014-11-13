package lim.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lim.session.Session;
import lim.session.SessionManager;
import lim.util.StringUtil;

import org.apache.mina.core.session.IoSession;

public class MessageHandler {
	public void messageReceived(IoSession session, Object message) throws Exception {
		String content = message.toString();
		
        SessionManager sm = SessionManager.getInstance();
        Session fromSession = sm.getById(session.getId());
        
        String room = fromSession.getRoom();
        List<Session> sessions = SessionManager.getInstance().getByRoom(room);
        
        StringBuilder msg = new StringBuilder();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String datetime = sdf.format(new Date());
        msg.append("[").append(datetime).append("] ");
        
        if(StringUtil.isEmpty(room)){
        	msg.append("[").append(fromSession.getNick()).append("] ");
        }else{
        	msg.append("[").append(room).append("/").append(fromSession.getNick()).append("] ");
        }
        
        msg.append(content);
        
        for (Session sess : sessions) {
            sess.write(msg);
        }
	}
}
