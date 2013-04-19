package network;


import java.io.*;

import constants.NetworkInfo;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;

import util.NetworkPacket;

import constants.NetworkInfo;

/**
 * Class which implements network functionalities.
 * It is a singleton class because, one client should
 * have only one open connection with the central server
 * 
 * @author Coffee Team
 *
 */
public class NetworkClient {
	
	private static NetworkClient server;
	private SocketChannel socketChannel	= null;
	
	private NetworkClient( ) {
		try {
			socketChannel = SocketChannel.open();
			
			//configure connection to be non-blocking
			socketChannel.configureBlocking(false);
			
			//connect to the remote server
			socketChannel.connect(new InetSocketAddress(NetworkInfo.IP, NetworkInfo.PORT));
			
			while(!socketChannel.finishConnect()) {
				System.out.println("Waiting....");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error opening client channel");
			e.printStackTrace();
		}
	}
	
	public static NetworkClient getClientObject() {
		if (server == null) {
			System.out.println("I created a new object");
			server = new NetworkClient();
		}
		return server;
	}
	
	public boolean sendData(Object dataToSend) {
		byte[]	bytesToSend;
		byte[]	lengthPack;
		ByteBuffer wBuff = ByteBuffer.allocate(8192);
		
		try {
			//serialize the result
			bytesToSend = NetworkPacket.serialize(dataToSend);
			
			//find the length of the packet
			lengthPack = NetworkPacket.packetLength(bytesToSend);

			
			//send the length of the object through the channel
			wBuff.clear();
			wBuff.put(lengthPack);
			wBuff.flip();
			socketChannel.write(wBuff);
			
			//send the object through the channel
			wBuff = ByteBuffer.allocate(8192);
			
			wBuff.clear();
			wBuff.put(bytesToSend);
			wBuff.flip();
			socketChannel.write(wBuff);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	
}