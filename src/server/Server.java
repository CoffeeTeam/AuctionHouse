package server;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.net.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import constants.NetworkInfo;

import util.CustomFileAppender;
import util.NetworkPacketManager;

public class Server extends Thread {

	static Logger loggerServer = Logger.getLogger(Server.class);
	
	public static Selector selector;
	private ServerSocketChannel serverSocketChannel;
	
	private Hashtable<SelectionKey, byte[]> readBuffers;
	private static Hashtable<SelectionKey, ArrayList<byte[]>> writeBuffers;
	
	private ByteBuffer rBuffer = ByteBuffer.allocate(NetworkInfo.BUF_SIZE);
	private static ByteBuffer wBuffer = ByteBuffer.allocate(NetworkInfo.BUF_SIZE);
	
	/* A list keeping associations between user name and its selection key */
	public static Map<String, SelectionKey> registeredUsersChannels
			= new HashMap<>();

	public Server() {
		PropertyConfigurator.configure("log4j.properties");
		loggerServer.addAppender(CustomFileAppender.getCustomFileAppender("server.log").getFileAppender());
		
		readBuffers = new Hashtable<SelectionKey, byte[]>();
		writeBuffers = new Hashtable<SelectionKey, ArrayList<byte[]>>();
		initServer();
	}

	private void initServer() {
		try {
			selector = SelectorProvider.provider().openSelector();

			this.serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(NetworkInfo.IP, NetworkInfo.PORT));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startRunning() {
		this.start();
	}

	public void accept(SelectionKey key) throws IOException {

		loggerServer.info("ACCEPT: ");

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocateDirect(NetworkInfo.BUF_SIZE);
		socketChannel.register(key.selector(), SelectionKey.OP_READ, buf);

		loggerServer.info("Connection from: " + socketChannel.socket().getRemoteSocketAddress());
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
			loggerServer.warn("The socket was closed by the remote client"
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
		loggerServer.info("[Server] Number of read bytes " + numRead
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

		i += 4;

		// Read the serialized object
		if (i + length <= newBuf.length) {
			loggerServer.info("deserialize array");
			
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
		loggerServer.info("WRITE: ");

		SocketChannel socketChannel = (SocketChannel) key.channel();

		ArrayList<byte[]> wbuf = null;
		int offset;
		int toWrite;
		
		synchronized(key) {
			wbuf = writeBuffers.get(key);

			while (wbuf.size() > 0) {
				byte[] bbuf = wbuf.get(0);
				wbuf.remove(0);
				
				//write all the array
				if (bbuf.length < NetworkInfo.BUF_SIZE) {
					wBuffer.clear();
					wBuffer.put(bbuf);
					wBuffer.flip();
				} else {
					//write only the size of the buffer
					offset = 0;
					toWrite = (bbuf.length - offset < NetworkInfo.BUF_SIZE)? bbuf.length - offset: NetworkInfo.BUF_SIZE;
					wBuffer.clear();
					wBuffer.put(bbuf, offset, toWrite);
					wBuffer.flip();
				}
	
				int numWritten = socketChannel.write(wBuffer);
				loggerServer.info("wrote " + numWritten + " bytes on the socket associated with the key" + key);
	
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
			
			if (wbuf.size() == 0) {
				key.interestOps(SelectionKey.OP_READ);
			}
		}
	}
	
	/**
	 * Called when the server must send a response to a client
	 * @param key the key to communicate with the client
	 * @param data data to send
	 */
	public static void sendData(SelectionKey key, Object dataObj) {
		byte[] data = null;
		byte[] lengthPacket = null;
		
		try {
			//serialize the data to be sent
			data = NetworkPacketManager.serialize(dataObj);
			
			//compute the length of the package
			lengthPacket = NetworkPacketManager.packetLength(data);
			
		} catch (IOException e) {
			loggerServer.error("Error serializing object");
			e.printStackTrace();
		}
		
		loggerServer.info("To write " + data.length + 
				" bytes on the socket associated with the key " + key);
	
		ArrayList<byte[]> wbuf = null;
		
		synchronized (key) {
			wbuf = writeBuffers.get(key);
			if (wbuf == null) {
				wbuf = new ArrayList<byte[]>();
				writeBuffers.put(key, wbuf);
			}
			wbuf.add(lengthPacket);
			wbuf.add(data);
			if (key != null)
				key.interestOps(SelectionKey.OP_WRITE);
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
					SelectionKey key = it.next();
					it.remove();

					if (key.isAcceptable()) {
						accept(key);
					} else if (key.isReadable()) {
						read(key);
					} else if (key.isWritable()) {
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
