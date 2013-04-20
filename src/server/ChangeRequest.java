package server;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ChangeRequest {
	public SelectionKey key;
	public SocketChannel socketChannel;
	public int newOps;
	
	public ChangeRequest(SelectionKey key, int newOps) {
		this.key = key;
		this.newOps = newOps;
		this.socketChannel = null;
	}
	
	public ChangeRequest(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
}