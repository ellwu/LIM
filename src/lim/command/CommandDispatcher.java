package lim.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import lim.session.Session;
import lim.session.SessionManager;
import lim.util.StringUtil;

import org.apache.mina.core.session.IoSession;

public class CommandDispatcher {
	private static CommonCommands common = new CommonCommands();

	public void dispatch(String command, IoSession session) {
		/**
		 * command format /command param1 param2 param3
		 */
		if(StringUtil.isEmpty(command)){
			return;
		}
		
		String[] inputs = command.split("\\s");
		
		String cmd = StringUtil.removeFirstLetter(inputs[0]);
		
		List<String> params = new ArrayList<String>();
		for(int i = 1; i < inputs.length; i++){
			params.add(inputs[i]);
		}
		
		Session sess = SessionManager.getInstance().getById(session.getId());
		
		try {
			Method method = Class.forName("lim.command.CommonCommands").getMethod(cmd, List.class, Session.class);
			method.invoke(common, params, sess);
			
		} catch (Exception e) {
			common.replyCommand("command not found", sess);
		}
	}
	
}
