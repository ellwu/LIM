package lim;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import lim.handler.StanzaHandler;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Server {
	private SocketAcceptor acceptor;
	
	public Server(){
		this.acceptor = new NioSocketAcceptor();
	}
	
	public boolean run(int port){
		DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
		
		filterChain.addLast("codec",  
	    	    new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue())));  
		acceptor.setHandler(new StanzaHandler());
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
		
		try {
			acceptor.bind(new InetSocketAddress(port));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		int port = 3456;
		if(args != null &&args.length > 0){
			port = Integer.parseInt(args[0]);
		}
		System.out.println("LIM is running at port " + port);
		new Server().run(port);
	}
}
