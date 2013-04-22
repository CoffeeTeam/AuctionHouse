package util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FileService implements Serializable {

	public byte[] fileContent;
	public String fileName;
	public String buyer;
	public String seller;
	public String serviceName;
}
