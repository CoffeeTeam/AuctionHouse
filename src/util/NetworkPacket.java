package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NetworkPacket {

	/**
	 * Write the received object into a buffer, serialized
	 * @param obj the object to be sent
	 * @return byte array containing the data to be sent
	 * @throws IOException
	 */
	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
	}

	/**
	 * Deserialize the bytes and obtain the initial object
	 * @param bytes serialized object
	 * @return the initial object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserialize(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}

	
	/**
	 * Computes the length of the package to be sent over the network
	 * @param serialized the serialized object
	 * @return byte array containing the length of the packet
	 */
	public static byte[] packetLength(byte[] serialized) {
		byte[] req = new byte[4];
		req[0] = (byte) (((serialized.length >> 24) % 256) - 128);
		req[1] = (byte) (((serialized.length >> 16) % 256) - 128);
		req[2] = (byte) (((serialized.length >> 8) % 256) - 128);
		req[3] = (byte) ((serialized.length % 256) - 128);
		System.out.println("Length of array " + serialized.length);
		return req;
	}
	
	/**
	 * Compute the length of the received object
	 * @param lengthBytes array of bytes containing the length of the package
	 * @return the length of the object
	 */
	public static int getLength(byte[] lengthBytes) {
		int length = 0;
		int shift = 24;
		for (int i = 0; i < 4; ++i, shift -= 8) {
			length += ((128 + (int)lengthBytes[i]) << shift);
		}
		
		return length;
	}
}
