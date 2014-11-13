package lim.handler;

import lim.session.Session;
import lim.session.SessionManager;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class StanzaHandler implements IoHandler{
	private MessageHandler messageHandler = new MessageHandler();
	private CommandHandler commandHandler = new CommandHandler();

	@Override
	public void exceptionCaught(IoSession session, Throwable e)
			throws Exception {
		e.printStackTrace();
		System.out.println("exceptionCaught");
	}

	@Override
	public void inputClosed(IoSession session) throws Exception {
		System.out.println("inputClosed");
		SessionManager.getInstance().removeById(session.getId());
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
        String content = message.toString();
        
        if(content.startsWith("/")){
        	commandHandler.commandReceived(session, message);
        }else{
        	messageHandler.messageReceived(session, message);
        }
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("messageSent:" + message);
		Session sess = SessionManager.getInstance().getById(session.getId());
		if("busy".equals(sess.getStatus())){
			sess.setStatus("online");
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		SessionManager.getInstance().removeById(session.getId());
		
        CloseFuture closeFuture = session.close(true);
        closeFuture.addListener(new IoFutureListener<IoFuture>() {
            public void operationComplete(IoFuture future) {
                if (future instanceof CloseFuture) {
                    ((CloseFuture) future).setClosed();
                }
            }
        });
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		session.write("+---------------------------------+");
		session.write("|                                 |");
		session.write("|   LIM                           |");
		session.write("|   A simple console chat app.    |");
		session.write("|                                 |");
		session.write("+---------------------------------+");
		
		SessionManager.getInstance().add(new Session(session));
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		System.out.println("sessionIdle");
		Session sess = SessionManager.getInstance().getById(session.getId());
		sess.setStatus("busy");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		session.write("Welcome to the great hall !");
		session.write("Please enjoy yourself!");
	}

}
