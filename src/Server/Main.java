package Server;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String args[]) throws Exception{
        ServerSocket serverSocket=new ServerSocket(2003);
        Socket clientSocket;
        //listens to emails about
       // new Thread(new email_listener()).start();
        while(true){
            Socket accepter=serverSocket.accept();

            new Thread(new client_management(accepter)).start();
            System.out.println("accepted and thrown to a thread");

        }
    }
}


/*
Class c = Class.forName("DemoTest");
        Object obj = c.newInstance();

        Method method = c.getDeclaredMethod("sampleMethod", null);
        method.setAccessible(true);
        method.invoke(obj, null);
 */