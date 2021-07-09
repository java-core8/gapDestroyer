package ru.tcreator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {
  protected final InetSocketAddress socketAddress = new InetSocketAddress("localhost", 44353);

  @Override
  public void run() {
    try {
      final SocketChannel socketChannel = SocketChannel.open();
      socketChannel.connect(socketAddress);
      try (Scanner scanner = new Scanner(System.in)) {
        final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
        while(true) {
          System.out.println("введистрокучтобыудалитьвсепробелыэтожетаккруто");
          String msg = scanner.nextLine();
          if("end".equals(msg)) break;
          socketChannel.write(ByteBuffer.wrap(
                  (msg).getBytes(StandardCharsets.UTF_8)
          ));

//          TimeUnit.MILLISECONDS.sleep(2000);
          int byteCount = socketChannel.read(inputBuffer);
          System.out.println(new String(inputBuffer.array(), 0, byteCount, StandardCharsets.UTF_8));
          inputBuffer.clear();
        }
      } finally {
        socketChannel.close();
      }
    }catch (IOException e) {
      e.printStackTrace();
    }
  }
}
