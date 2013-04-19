package server;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.net.*;
import java.util.*;

import util.NetworkPacket;

public class Server extends Thread {

	private final int BUF_SIZE = 8192;
	private final String IP = "127.0.0.1";
	private final int PORT = 30001;
	private Hashtable<SelectionKey, byte[]> readBuffers;
	private ByteBuffer rBuffer = ByteBuffer.allocate(BUF_SIZE);
	public Selector selector;
	private ServerSocketChannel serverSocketChannel;

	public Server() {
		readBuffers = new Hashtable<SelectionKey, byte[]>();
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
		ByteBuffer buf = ByteBuffer.allocateDirect(BUF_SIZE);
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

		// Copiaza datele primite in newBuf (rbuf sunt datele primite
		// anterior si care nu formeaza o cerere completa), iar currentBuf
		// contine datele primite la read-ul curent.
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

		length = NetworkPacket.getLength(new byte[] { newBuf[0], newBuf[1],
				newBuf[2], newBuf[3] });

		System.out.println("[Server] The received length is = " + length);
		i += 4;

		// Read the serialized object
		if (i + length <= newBuf.length) {
			System.out.println("[SERVER] deserialize array");
			
			Object st = NetworkPacket.deserialize(Arrays.copyOfRange(newBuf, 4,
					length + 4));

			ServerUtils.chooseAction(st);
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

	public void write(SelectionKey key) throws IOException {

		System.out.println("WRITE: ");

		int bytes;
		ByteBuffer buf = (ByteBuffer) key.attachment();
		SocketChannel socketChannel = (SocketChannel) key.channel();

		try {
			while ((bytes = socketChannel.write(buf)) > 0)
				;

			if (!buf.hasRemaining()) {
				buf.clear();
				key.interestOps(SelectionKey.OP_READ);
			}

		} catch (IOException e) {
			System.out.println("Connection closed: " + e.getMessage());
			socketChannel.close();

		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			while (true) {
				this.selector.select();
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
					} else if (key.isWritable())
						write(key);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		Server server = new Server();

		server.startRunning();

	}

}
