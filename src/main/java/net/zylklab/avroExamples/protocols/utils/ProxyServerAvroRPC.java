package net.zylklab.avroExamples.protocols.utils;

import java.net.InetSocketAddress;

import org.apache.avro.ipc.netty.NettyServer;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.generated.MyTests;

/**
 * Synchronous server using Netty.
 */
public class ProxyServerAvroRPC extends ProxyServerAvro {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyServerAvroRPC.class);

	public ProxyServerAvroRPC(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * Run a new server on a separate thread.
	 */
	public void run() {
		try {
			server = new NettyServer(new SpecificResponder(MyTests.class, new MyTestsImpl()),
					new InetSocketAddress(host, port));
			LOGGER.info("Server started.");
			synchronized (this) {
				notifyAll();
			}
			while (!close) {
				Thread.sleep(1000);
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			ex.printStackTrace();
		} finally {
			server.close();
			LOGGER.info("Server closed.");
		}
		return;
	}

	public void setClose(boolean b) {
		close = b;
		return;
	}
}