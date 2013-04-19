package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import user.UserPackage;

public class ServerUtils {

	public static final String loggedUsersFile = "users";

	/**
	 * Writes into a file the information about the new user
	 * 
	 * @param user
	 *            object containing the information
	 */
	public static void addUserInfo(UserPackage user) {

		FileWriter file = null;
		try {
			file = new FileWriter(loggedUsersFile);
			BufferedWriter outstream = new BufferedWriter(file);
			outstream.write(user.username + "\t" + user.password + "\t"
					+ user.userType + "\n");

			outstream.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * When a user is logging out the entry containing that user is deleted from
	 * the file
	 * 
	 * @param user
	 *            the user that is leaving the system
	 */
	public static void deleteUserInfo(UserPackage user) {
		File file = new File(loggedUsersFile);
		File tmpFile = new File(loggedUsersFile + ".tmp");
		BufferedReader br = null;
		PrintWriter pw = null;
		String line;

		// verify if the file exists
		if (!file.isFile()) {
			System.out.println("File doesn't exist");
			return;
		}

		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			System.err.println("File " + loggedUsersFile + "wasn't found");
			e2.printStackTrace();
		}
		try {
			pw = new PrintWriter(new FileWriter(tmpFile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// create a temporary file
		try {
			if (!tmpFile.exists())
				tmpFile.createNewFile();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * read from the old file and copy it into the temporary file except the
		 * received user
		 */
		try {
			while (null != (line = br.readLine())) {
				String username = line.split("\\s+")[0];
				if (!username.equals(user.username)) {
					pw.write(line + "\n");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pw.close();
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error closing read buffer");
			e.printStackTrace();
		}

		// delete the old file
		if (!file.delete()) {
			System.err.println("Error deleting the old file");
		}

		// rename the temporary file to the original file
		if (tmpFile.renameTo(file)) {
			System.err.println("Could not rename file");
		}

	}

	public static void chooseAction(Object recvObject) {
		UserPackage userPack;
		
		System.out.println(recvObject.getClass());
		
		if (recvObject instanceof UserPackage) {
			System.out.println("[SERVER} it is a user package");
			userPack = (UserPackage) recvObject;

			if (userPack.toDelete == 1)
				deleteUserInfo(userPack);
			else {
				System.out.println("[SERVER] Add a new user");
				addUserInfo(userPack);
			}
		}
	}

}
