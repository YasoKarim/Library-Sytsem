package Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.lang.reflect.Method;

public class client_management implements Runnable{
    Socket cs;
    client_management(Socket clientSocket){
        cs=clientSocket;
    }
    @Override
    public void run() {
        try {
            // if we switched the output with input a dealock happens
            ObjectOutputStream out = new ObjectOutputStream(cs.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(cs.getInputStream());

            String cls=(String)in.readObject();
            String meth=(String)in.readObject();
            //search for the class
            Class<?> c = Class.forName("Server."+cls);
            //creates object of the class
            Object obj = c.getDeclaredConstructor().newInstance();

            Method method = c.getDeclaredMethod(meth,ObjectOutputStream.class,ObjectInputStream.class);

            method.invoke(obj, out,in);
            /*Class c = Class.forName(cls);
            Object obj = c.getDeclaredConstructor().newInstance();
            Method method = c.getDeclaredMethod(meth, null);
            method.setAccessible(true);
            method.invoke(obj, out,in);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
