package server;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.net.*;
import java.util.*;

import constants.NetworkInfo;

import util.NetworkPacketManager;

public class Server extends Thread {

	private final String IP = "127.0.0.1";
	private final int PORT = 30001;
	private Hashtable<SelectionKey, byte[]> readBuffers;
	private static Hashtable<SelectionKey, ArrayList<byte[]>> writeBuffers;
	private ByteBuffer rBuffer = ByteBuffer.allocate(NetworkInfo.BUF_SIZE);
	private static ByteBuffer wBuffer = ByteBuffer.allocate(NetworkInfo.BUF_SIZE);
	private static LinkedList<ChangeRequest> changeRequestQueue;
	public static Selector selector;
	private ServerSocketChannel serverSocketChannel;
	
	/* A list keeping associations between user name and its selection key */
	public static Map<String, SelectionKey> registeredUsersChannels
			= new HashMap<>();

	public Server() {
		readBuffers = new Hashtable<SelectionKey, byte[]>();
		writeBuffers = new Hashtable<SelectionKey, ArrayList<byte[]>>();
		initServer();
	}

	private void initServer() {
		try {
			selector = SelectorProvider.provider().openSelector();

			this.serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(IP, PORT));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startRunning() {
		this.start();
	}

	public void accept(SelectionKey key) throws IOException {

		System.out.print("ACCEPT: ");

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocateDirect(NetworkInfo.BUF_SIZE);
		socketChannel.register(key.selector(), SelectionKey.OP_READ, buf);

		System.out.println("Connection from: "
				+ socketChannel.socket().getRemoteSocketAddress());
	}

	public void read(SelectionKey key) throws Exception {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		this.rBuffer.clear();

		int numRead = 0;

		try {
				numRead = socketChannel.read(rBuffer);
		} catch (Exception e) {
			numRead = -1000000000;
		}

		if (numRead <= 0) {
			System.out.println("The socket was closed by the remote client"
					+ key);
			key.channel().close();
			key.cancel();
			return;
		}

		byte[] rbuf = null;
		rbuf = readBuffers.get(key);

		int rbuflen = 0;
		if (rbuf != null) {
			rbuflen = rbuf.length;
		}

		byte[] currentBuf = rBuffer.array();
		System.out.println("[Server] Number of read bytes " + numRead
				+ " associated with the key " + key + " : " + currentBuf);

		byte[] newBuf = new byte[rbuflen + numRead];

		/* Copy received data into newBuf
		 * rbuf - contains data previously received but incomplete
		 * currentBuf - contains data received during the current read
		 */
		for (int i = 0; i < rbuflen; i++) {
			newBuf[i] = rbuf[i];
		}

		for (int i = rbuflen; i < newBuf.length; i++) {
			newBuf[i] = currentBuf[i - rbuflen];
		}

		int i = 0;
		int length = 0;
		// Read size of sent object
		if (i + 4 > newBuf.length)
			return;

		length = NetworkPacketManager.getLength(new byte[] { newBuf[0], newBuf[1],
				newBuf[2], newBuf[3] });

		System.out.println("[Server] The received length is = " + length);
		i += 4;

		// Read the serialized object
		if (i + length <= newBuf.length) {
			System.out.println("[SERVER] deserialize array");
			
			Object st = NetworkPacketManager.deserialize(Arrays.copyOfRange(newBuf, 4,
					length + 4));

			ServerUtils.chooseAction(st, key);
			i += length;
		} else
			i -= 4;

		byte[] finalBuf = null;
		if (i > 0) {
			finalBuf = new byte[newBuf.length - i];
			for (int j = i; j < newBuf.length; j++) {
				finalBuf[j - i] = newBuf[j];
			}
		} else {
			finalBuf = newBuf;
		}

		readBuffers.put(key, finalBuf);
	}


	public static void write(SelectionKey key) throws IOException {
		//TODO add length of the package when writing the package
		System.out.println("WRITE: ");

		SocketChannel socketChannel = (SocketChannel) key.channel();

		ArrayList<byte[]> wbuf = null;
		
		synchronized(key) {
			wbuf = writeBuffers.get(key);

			while (wbuf.size() > 0) {
				byte[] bbuf = wbuf.get(0);
				wbuf.remove(0);
				
				wBuffer.clear();
				wBuffer.put(bbuf);
				wBuffer.flip();
	
				int numWritten = socketChannel.write(wBuffer);
				System.out.println("[Server] I wrote " + numWritten + " bytes on the socket associated with the key" + key);
	
				if (numWritten < bbuf.length) {
					byte[] newBuf = new byte[bbuf.length - numWritten];
	
					// Copy data which is still unwritten into newBuf
					for (int i = numWritten; i < bbuf.length; i++) {
						newBuf[i - numWritten] = bbuf[i];
					}
					
					wbuf.add(0, newBuf);
					break;
				}
			}
			
		//	key.interestOps(SelectionKey.OP_READ);
			if (wbuf.size() == 0) {
				key.interestOps(SelectionKey.OP_READ);
			}
//				synchronized (changeRequestQueue) {
//					changeRequestQueue.add(new ChangeRequest(key, SelectionKey.OP_READ));
//				}
//			}
		}
//		selector.wakeup();
	}
	
	/**
	 * Called when the server must send a response to a client
	 * @param key the key to communicate with the client
	 * @param data data to send
	 */
	public static void sendData(SelectionKey key, Object dataObj) {
		byte[] data = null;
		
		try {
			data = NetworkPacketManager.serialize(dataObj);
		} catch (IOException e) {
			System.err.println("Error serializing object");
			e.printStackTrace();
		}
		
		System.out.println("[Server] I want to write " + data.length + " bytes on the socket associated with the key " + key);
		
		ArrayList<byte[]> wbuf = null;
		
		synchronized (key) {
			wbuf = writeBuffers.get(key);
			if (wbuf == null) {
				wbuf = new ArrayList<byte[]>();
				writeBuffers.put(key, wbuf);
			}

			wbuf.add(data);
//			synchronized (changeRequestQueue) {
//				changeRequestQueue.add(new ChangeRequest(key, SelectionKey.OP_READ | SelectionKey.OP_WRITE));
			key.interestOps(SelectionKey.OP_WRITE);
			//}
		}
				
		selector.wakeup();
	}

	@Override
	public void run() {
		try {
			while (true) {
				selector.select();
				for (Iterator<SelectionKey> it = selector.selectedKeys()
						.iterator(); it.hasNext();) {
					System.out.println("I have connections");
					SelectionKey key = it.next();
					it.remove();

					if (key.isAcceptable()) {
						System.out.println("Incoming connection to accept");
						accept(key);
					} else if (key.isReadable()) {
						System.out.println("I read things");
						read(key);
					} else if (key.isWritable()) {
						System.out.println("I write things");
						write(key);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Server server = new Server();

		server.startRunning();

	}

}
