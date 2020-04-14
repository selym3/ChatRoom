package server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientManager {

	private List<Client> clients;
	private List<ManagerThread> threads;

	private Server server;

	public ClientManager(Server server) {
		clients = new CopyOnWriteArrayList<Client>();
		threads = new CopyOnWriteArrayList<ManagerThread>();

		this.server = server;
	}

	/*
	 * ConcurrentModificationError: It occurred when disconnecting with multiple
	 * users were connecting and leaving at the same time
	 * 
	 * Prevents a manager thread from closing, thus preventing Server
	 * from disconnecting after told to 
	 * 
	 * Fix: the costly CopyOnWriteArrayList<>()
	 */

	public void add(Client c) {
		clients.add(c);
		ManagerThread m = new ManagerThread(server, this, c);
		threads.add(m);
		m.start();
	}

	public void remove(Client c) {
		for (ManagerThread thread : threads) {
			if (thread.getClient() == c) {
				thread.interrupt();
			}
			threads.remove(thread);
			thread = null;
		}

		clients.remove(c);
		c.stop();
		c = null;
	}

	public int getUsers() {
		return clients.size();
	}

	/**
	 * A server-wide broadcast. A message is sent to all clients.
	 * 
	 * @param message message
	 */
	public void broadcast(String message) {
		for (Client client : clients) {
			client.write(message);
		}
	}

	/**
	 * A broadcast sent to all clients except one.
	 * 
	 * @param message message
	 * @param exclude client to exclude
	 */
	public void broadcast(String message, Client exclude) {
		for (Client client : clients) {
			if (client != exclude) {
				client.write(message);
			}
		}
	}

	/**
	 * Close all threads and clients.
	 */
	public void stop() {
		for (Client c : clients) {
			remove(c);
			c = null;
		}
	}

}
