package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Retrives information from the database file according to some filters
 * @author mozzie
 *
 */
public class FileInfoRetriever {

	private String filename;

	public FileInfoRetriever(String filename) {
		// TODO Auto-generated constructor stub
		this.filename = filename;
	}

	
	private BufferedReader getFileReader(){
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Error opening database file");
			e.printStackTrace();
			System.exit(1);
		}
		return br;
	}
	
	/**
	 * Get services that a user provides or that he is interested in
	 * 
	 * @param userName
	 *            the name of the user
	 * @return list of services for the user received as parameter
	 */
	public List<String> userService(String userName, String userType) {
		List<String> serviceList = new Vector<String>();
		String currentLine;
		StringTokenizer strTk;
		BufferedReader br = getFileReader();
		
		if(br == null) {
			System.err.println("Error opening file");
			System.exit(1);
		}

		try {
			//Read until the end of file
			while ((currentLine = br.readLine()) != null) {
				if(currentLine.isEmpty())
					break;
				strTk = new StringTokenizer(currentLine);
				
				//read services only for the received user
				if (strTk.nextElement().equals(userName)) {
					if(!strTk.nextElement().equals(userType)) {
						break;
					}
					while (strTk.hasMoreTokens()) {
						serviceList.add(strTk.nextToken());
					}
					break;
				}

			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error reading database file");
			e.printStackTrace();
		}

		return serviceList;
	}
	
	/**
	 * Get the list of users that provide a service
	 * @param service the name of the service
	 * @return the list of sellers
	 */
	public List<String> getUserList(String service, String userType) {
		List<String> users = new Vector<String>();
		BufferedReader br = getFileReader();
		String currentLine, user, tmpService;
		StringTokenizer strTk;
		
		if(br == null) {
			System.err.println("Error opening file");
			System.exit(1);
		}
		
		try {
			//Read until the end of file
			while ((currentLine = br.readLine()) != null) {
				System.out.println("Read line " + currentLine);
				strTk = new StringTokenizer(currentLine);
				user = strTk.nextToken();

				//reads services for all users
				if (!strTk.nextElement().equals(userType)) {
					while (strTk.hasMoreTokens()) {
						tmpService = strTk.nextToken();
						if(tmpService.equals(service))
							users.add(user);
					}
				}

			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Error reading database file");
			e.printStackTrace();
		}

		return users;
	}
}
