package gui;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class User {

	private static final List<String> V_CHARS = Arrays.asList("abcdefghijklmnopqrstuvwxyz1234567890_".split(""));
	
	private static boolean validName(String name) {
		if (name.length() > 16) return false;
		for (String c : name.split("")) {
			if (!V_CHARS.contains(c.toLowerCase())) return false;
		}
		return true;
	}
	
	private String name;
	private String host;
	private int port;

	private boolean validPort;

	public User(String name, String hostport) {
		if (name == null || hostport == null) {
			validPort = false;
		} else {
			this.name = name;
			
			this.host = hostport.split(":")[0];
			
			validPort = true;
			try {
				this.port = Integer.parseInt(hostport.split(":")[1]);
				if (port < 0 || port > 65535) {
					validPort = false;
				}
			} catch (NumberFormatException e) {
				validPort = false;
			} catch (Exception e) {
				validPort = false;
			}
		}

	}

	public boolean isValid() { // 0 to 65535
		return name != null &&  // check if name null
				validName(name) &&
				host != null && // check if host null
				validPort && // check if the port could be retrieved
				!Pattern.matches("[^A-Za-z0-9]+", name); // check if the name contains only valid chars
	}
	
	public String getName() {
		return name;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return isValid() ? name + " on " + host + ":" + port : "invalid user";
	}

}
