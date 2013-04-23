package network;

import java.io.*;

import constants.NetworkInfo;

import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.SwingWorker;

import util.FileService;
import util.NetworkPacketManager;

/**
 * Class which implements network functionalities. It is a singleton class
 * because, one client should have only one open connection with the central
 * server
 * 
 * @author Coffee Team
 * 
 */
@SuppressWarnings("rawtypes")
public class NetworkClient extends SwingWorker {
	public static INetwork network;
	private static NetworkClient netClient;
	private SocketChannel socketChannel = null;
	private SelectionKey key;
	private Selector selector;
	private ByteBuffer rBuffer = ByteBuffer.allocate(NetworkInfo.BUF_SIZE);
	private byte[] readBuf = null;
	private static ArrayList<byte[]> wbuf = new ArrayList<byte[]>();
	private Object dataToSend;
	private boolean running;
	private int offset = 0;
	private int sizeToWrite = 0;

	private NetworkClient(INetwork network) {
		// set network IF NOT ALREADY SET
		if (NetworkClient.network == null) {
			NetworkClient.network = network;
		} else if (NetworkClient.network != network) {
			// Signalizes an error if there is an attempt to set a static field
			// to 2 different values
			System.err.println("[Network Client] Static field network is "
					+ "already set to another value !!");
		}

		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open();

			// configure connection to be non-blocking
			socketChannel.configureBlocking(false);
			running = true;

			// connect to the remote server
			socketChannel.connect(new InetSocketAddress(NetworkInfo.IP,
					NetworkInfo.PORT));

			socketChannel.register(selector, SelectionKey.OP_READ);

		} catch (IOException e) {
			System.err.println("Error opening client channel");
			e.printStackTrace();
		}

	}

	public void connect(SelectionKey key) throws IOException {

		System.out.print("CONNECT: ");

		SocketChannel socketChannel = (SocketChannel) key.channel();
		if (!socketChannel.finishConnect()) {
			System.err.println("Eroare finishConnect");
			running = false;
		}
		this.key = key;

		System.out.println("[Client] Finished connecting");
		key.interestOps(SelectionKey.OP_READ);
	}

	public static NetworkClient getClientObject(INetwork network) {
		if (netClient == null) {
			System.out.println("I created a new object");
			netClient = new NetworkClient(network);
		}
		return netClient;
	}

	public void sendData(Object dataObject) {
		this.dataToSend = dataObject;

		byte[] data = null;
		byte[] lengthPacket = null;

		try {
			// serialize the data to be sent
			data = NetworkPacketManager.serialize(dataToSend);

			// compute the length of the package
			lengthPacket = NetworkPacketManager.packetLength(data);

		} catch (IOException e) {
			System.err.println("Error serializing object");
			e.printStackTrace();
		}

		sizeToWrite = data.length;
		offset = 0;

		System.out.println("[Client] I want to write " + data.length
				+ " bytes on the socket associated with the key " + key);

		synchronized (key) {
			wbuf.add(lengthPacket);
			wbuf.add(data);

			key.interestOps(SelectionKey.OP_WRITE);
		}
		selector.wakeup();
	}

	public void write(SelectionKey key) throws IOException {
		System.out.println("WRITE: ");
		FileService fileService;
		ByteBuffer wBuff = ByteBuffer.allocate(NetworkInfo.BUF_SIZE);
		SocketChannel socketChannel = (SocketChannel) key.channel();

		int toWrite;

		synchronized (key) {
			while (wbuf.size() > 0) {
				byte[] bbuf = wbuf.get(0);
				wbuf.remove(0);

				// write all the array
				if (bbuf.length < NetworkInfo.BUF_SIZE) {
					wBuff.clear();
					wBuff.put(bbuf);
					wBuff.flip();
				} else {
					// write only the size of the buffer
					toWrite = NetworkInfo.BUF_SIZE;
					wBuff.clear();
					wBuff.put(bbuf,0,toWrite);
					wBuff.flip();
				}

				int numWritten = socketChannel.write(wBuff);
				System.out.println("[Client] I wrote " + numWritten
						+ " bytes on the socket associated with the key" + key);

				if (this.dataToSend instanceof FileService) {
					fileService = (FileService) dataToSend;
					offset += numWritten;
					network.med.acceptFileTransfer(fileService.serviceName, 100 * offset / sizeToWrite);
				}
				
				if (numWritten < bbuf.length) {
					byte[] newBuf = new byte[bbuf.length - numWritten];

					// Copy data which is still unwritten into newBuf
					for (int i = numWritten; i < bbuf.length; i++) {
						newBuf[i - numWritten] = bbuf[i];
					}

					wbuf.add(0, newBuf);
					break;
				}

				if (wbuf.size() == 0) {
					key.interestOps(SelectionKey.OP_READ);
				}
			}
		}
	}

	public void read(SelectionKey key) throws IOException,
			ClassNotFoundException {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		this.rBuffer.clear();

		System.out.println("[Client] Read");

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
		rbuf = readBuf;

		int rbuflen = 0;
		if (rbuf != null) {
			rbuflen = rbuf.length;
			System.out.println("[Client] there was data in the buffer "
					+ rbuflen);
		}

		byte[] currentBuf = rBuffer.array();
		System.out.println("[Client] Number of read bytes " + numRead
				+ " associated with the key " + key + " : " + currentBuf);

		byte[] newBuf = new byte[rbuflen + numRead];

		/*
		 * Copy received data into newBuf rbuf - contains data previously
		 * received but incomplete currentBuf - contains data received during
		 * the current read
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

		length = NetworkPacketManager.getLength(new byte[] { newBuf[0],
				newBuf[1], newBuf[2], newBuf[3] });

		System.out.println("[Client] The received length is = " + length);
		i += 4;

		// Read the serialized object
		if (i + length <= newBuf.length) {
			System.out.println("[Client] deserialize received object");

			Object st = NetworkPacketManager.deserialize(Arrays.copyOfRange(
					newBuf, 4, length + 4));
			System.out.println("Before choose action");
			ClientUtils.chooseAction(st);
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

		readBuf = finalBuf;
	}

	@Override
	protected Object doInBackground() throws Exception {
		socketChannel.register(selector, SelectionKey.OP_CONNECT);

		while (running) {
			selector.select();

			for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it
					.hasNext();) {
				SelectionKey key = it.next();
				it.remove();
				try {
					if (key.isReadable()) {
						read(key);
					} else if (key.isWritable()) {
						write(key);
					} else if (key.isConnectable()) {
						connect(key);
					}

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		}

		return null;

	}

}
