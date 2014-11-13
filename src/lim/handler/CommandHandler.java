package lim.handler;

import lim.command.CommandDispatcher;

import org.apache.mina.core.session.IoSession;

public class CommandHandler {
	private CommandDispatcher commandDispatcher = new CommandDispatcher();
	
	public void commandReceived(IoSession session, Object message)
			throws Exception {
		String command = message.toString();
		
		commandDispatcher.dispatch(command, session);
	}
}
