package handler;

import io.netty.buffer.ByteBuf;

import java.util.Scanner;

/**
 * Created by neikila on 19.10.15.
 */
public class MyFileReader {

    public MyFileReader() {
        System.out.println("Test");
    }

    public static long getFile(String rootDirectory, String fileName, ByteBuf byteBuf) {

        long size = 0;
        Scanner file = new Scanner(rootDirectory + fileName);

        while (file.hasNext()) {
            ++size;
            byteBuf.writeByte(file.nextByte());
        }
        return size;

    }
}
