package Server;

import javax.mail.*;
import java.util.Properties;

public class email_listener implements Runnable{

    @Override
    public void run() {
        Properties prop = new Properties();

        //prop.put("mail.pop3.host","pop.gmail.com");
        //prop.put("mail.imaps.port","995");
        //prop.put("mail.pop3.stattls.enable","true");
        prop.put("mail.store.protocol", "imaps");

        Session eses = Session.getDefaultInstance(prop);
        Store store = null;
        try {
            store = eses.getStore("imaps");
            store.connect("imap.gmail.com","lib.sys.proj@gmail.com","rootrt*1");

            Folder efol = store.getFolder("INBOX");
            efol.open(Folder.READ_ONLY);
            //while(true){
                try {
                    Message mess[]=efol.getMessages();

                    int i=(mess.length)-1;
                    Message message=mess[i];

                    if(message.getSubject().equals("password")){
                        db_login_connect.uppass(message.getFrom().toString(),message.getContent().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //}
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
