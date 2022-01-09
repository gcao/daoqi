package jagoclient;

/**
 * see http://forum.java.sun.com/thread.jspa?threadID=568948&tstart=240
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeoutInputStream extends InputStream {
	private final static Logger logger
			= Logger.getLogger(TimeoutInputStream.class.getName());

	private static class Buffer {
		private int[] buffer;
		private int start = 0;
		private int end = 0;
		private int size = 0;

		public Buffer(int size) {
			buffer = new int[size];
		}

		public synchronized void write(int b) {
			while (size == buffer.length) try {
				wait(); // Block until room is available
			}
			catch (InterruptedException e) {
			}
			buffer[end++] = b;
			size++;
			if (end == buffer.length) {
				end = 0;
			}
			notify();
		}

		public synchronized int read() {
			while (size == 0) try {
				wait(); // Block until data is available
			}
			catch (InterruptedException e) {
			}
			size--;
			if (start == buffer.length) {
				start = 0;
			}
			try {
				return buffer[start++];
			}
			finally {
				notify();
			}
		}

		public int size() {
			return this.size;
		}
	}

	private static class Reader extends Thread {
		private static int nextId = 1;
		private final static List ThreadPool
				= Collections.synchronizedList(new Vector());

		private Buffer buffer;
		private InputStream in;
		private IOException error;
		private boolean closed = true;

		private Reader() {
			super("TimoutInputStream.Reader[" + nextId() + "]");
			setDaemon(true);
		}

		public static Reader getInstance(Buffer buffer, InputStream in) {
			Reader instance = getInstance();
			instance.buffer = buffer;
			instance.in = in;
			instance.open();
			return instance;
		}

		private static Reader getInstance() {
			synchronized (ThreadPool) {
				if (ThreadPool.size() > 0) try {
					return (Reader) ThreadPool.get(0);
				}
				finally {
					ThreadPool.remove(0);
				}
				else {
					Reader reader = new Reader();
					reader.start();
					return reader;
				}
			}
		}

		public void run() {
			try {
				while (true) try {
					synchronized (this) {
						if (closed) {
							wait();
						}
					}
					int c = in.read();
					buffer.write(c);
					if (c == -1) {
						_close();
					}
				}
				catch (IOException e) {
					this.error = e;
					_close();
				}
			}
			catch (Throwable e) {
				logger.log(Level.SEVERE, "Unexpected error", e);
			}
			finally {
				_close();
			}
		}

		public synchronized IOException getError() {
			return error;
		}

		public void close() throws IOException {
			try {
				IOException e = _close();
				if (e != null) {
					throw e;
				}
			}
			finally {
				ThreadPool.add(this);
			}
		}

		private static synchronized int nextId() {
			return nextId++;
		}

		private synchronized IOException _close() {
			if (closed) {
				return null;
			} else {
				closed = true;
			}

			try {
				if (in != null) {
					in.close();
				}
				return null;
			}
			catch (IOException e) {
				return e;
			}
		}

		private synchronized void open() {
			closed = false;
			error = null;
			notifyAll();
		}
	}

	// State:
	private Buffer buffer = new Buffer(1024);
	private Reader reader;
	private long timeout;
	private boolean closed = false;

	public TimeoutInputStream(InputStream in, long timeout) {
		init(Reader.getInstance(buffer, in), timeout);
	}

	private void init(Reader reader, long timeout) {
		this.timeout = timeout;
		this.reader = reader;
	}

	public int read() throws IOException {
		if (closed) {
			return -1;
		}
		if (buffer.size() == 0) {
			synchronized (buffer) {
				try {
					buffer.wait(timeout);
				}
				catch (InterruptedException e) {
				}
			}
			if (buffer.size() == 0) {
				IOException err = reader.getError();
				reader.close();
				if (err == null) {
					err = new InterruptedIOException("Timeout");
				}
				throw err;
			}
		}
		int c = buffer.read();
		if (c == -1) {
			close();
		}
		return c;
	}

	public void close() throws IOException {
		if (!closed) {
			this.reader.close();
			this.closed = true;
		}
	}

	// -------------------------------------------------------------------------
	// Test
	public static void main(String[] args) throws Exception {
		Thread server = new Thread("SocketServer") {
			ServerSocket server = new ServerSocket(1234);
			Vector opened = new Vector();
			private boolean stop = false;

			public void run() {
				try {
					while (!stop) {
						Socket s = server.accept();
						new PrintStream(s.getOutputStream()).println("Welcome");
						try {
							Thread.sleep(100);
						}
						catch (InterruptedException e) {
						}
						// s.close(); // uncomment this to test normal operation
						opened.add(s);
					}
				}
				catch (IOException e) {
				}
			}

			public void interrupt() {
				stop = true;
				try {
					server.close();
				}
				catch (IOException e) {
				}
				for (int i = opened.size(); i-- > 0;)
					try {
						((Socket) opened.get(i)).close();
					}
					catch (IOException e) {
					}
				super.interrupt();
			}

		};
		server.setDaemon(true);
		server.start();

		for (int i = 0; i < 5; i++) {
			URL url = new URL("http://localhost:1234");
			TimeoutInputStream in = new TimeoutInputStream(url.openStream(), 1000);

			try {
				int ch;
				while ((ch = in.read()) != -1) {
					System.err.print((char) ch);
				}
				in.close();
				System.err.println("Finished");
			}
			catch (InterruptedIOException e) {
				System.err.println("Timeout");
			}
			finally {
				in.close();
			}
		}

		server.interrupt();
	}
}
