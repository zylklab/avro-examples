package net.zylklab.avroExamples.protocols.utils;

import java.net.InetSocketAddress;

import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.netty.NettyServer;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.generated.MyTests;

/**
 * 
 *
 */
public class ProxyServerAvroRPC extends ProxyServerAvro {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyServerAvroRPC.class);

	private static Server server;

	private int port;
	private String host;

	private boolean close = false;

	public ProxyServerAvroRPC(String host, int port) {
		this.host = host;
		this.port = port;
	}

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