package liblib.QR;
import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import liblib.libmain_control;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.Socket;

public class qr_handel implements Runnable{
    public libmain_control control;
    Webcam webcam;
    public int flag;
    public qr_handel(libmain_control r){
        control=r;
    }
    public  void ReadQr() {
        String resu;

        webcam.open();

        do {

            Result result = null;
            BufferedImage image = null;
            // check if the webcam is open
            if (webcam.isOpen()) {
                //we assign the value of image to webimage
                if ((image = webcam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);

                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                try {
                    /**try to decode a qr from image**/
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    //ImageIO.write(webcam.getImage(), "PNG", new File("C:\\Users\\THE KING IS HERE\\Desktop\\hello-world.png"));
                    result=null;
                    // fall thru, it means there is no QR code in image
                }
            }

            if (result != null) {
                //windows default beep sound
                Toolkit.getDefaultToolkit().beep();

                resu=result.getText();
                control.add_qr_read(resu);
                //try {
                //TimeUnit.SECONDS.sleep(1);
                //} catch (InterruptedException e) {
                // TODO Auto-generated catch block
                //	e.printStackTrace();
                //}
            }

        }while(flag==1);
    }

    @Override
    public void run() {
        flag=1;
        webcam = Webcam.getDefault(); // non-default (e.g. USB) webcam can be used too
        webcam.setViewSize(new Dimension(640,480));
        ReadQr();
        webcam.close();
    }
}
