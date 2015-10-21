package handler;

import java.io.*;

/**
 * Created by neikila on 19.10.15.
 */
public class MyFileReader {

    public MyFileReader() {
        System.out.println("Test");
    }

    public static byte[] getFile(String fileName) throws IOException {
        ByteArrayOutputStream out = null;
        InputStream input = null;
        try{
            out = new ByteArrayOutputStream();
            input = new BufferedInputStream(new FileInputStream(fileName));
            int data = 0;
            while ((data = input.read()) != -1){
                out.write(data);
            }
        }
        finally{
            if (null != input){
                input.close();
            }
            if (null != out){
                out.close();
            }
        }
        return out.toByteArray();
    }
}