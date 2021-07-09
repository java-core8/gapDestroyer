package ru.tcreator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable {
  protected final InetSocketAddress socketAddress = new InetSocketAddress("localhost", 44353);

  @Override
  public void run() {

    try {
      ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
      serverSocketChannel.bind(socketAddress);
      while (true) {
        try(SocketChannel socketChannel = serverSocketChannel.accept()) {
          final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

          while (socketChannel.isConnected()) {
            int bytesCount = socketChannel.read(inputBuffer);
            if(bytesCount == -1) break;

            String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);

            inputBuffer.clear();

            socketChannel.write(ByteBuffer.wrap(
                    (msg.replaceAll("\\s+","").trim())
                            .getBytes(StandardCharsets.UTF_8)
            ));
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
