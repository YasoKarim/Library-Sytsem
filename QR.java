import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class QR {
	public static String ReadQr() {
		String resu;
		Webcam webcam = Webcam.getDefault(); // non-default (e.g. USB) webcam can be used too
		webcam.setViewSize(new Dimension(640,480));
		webcam.open();

		do {
	
		Result result = null;
		BufferedImage image = null;

		if (webcam.isOpen()) {
		    if ((image = webcam.getImage()) == null) {
		        continue;
		    }

		    LuminanceSource source = new BufferedImageLuminanceSource(image);
		    
		    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		    try {
		    	
		        result = new MultiFormatReader().decode(bitmap);
		    } catch (NotFoundException e) {
		    	//ImageIO.write(webcam.getImage(), "PNG", new File("C:\\Users\\THE KING IS HERE\\Desktop\\hello-world.png"));
		    	result=null;
		        // fall thru, it means there is no QR code in image
		    }
		}

		if (result != null) {
			Toolkit.getDefaultToolkit().beep();  
		    resu=result.getText();
		    return resu;
		    //try {
				//TimeUnit.SECONDS.sleep(1);
			//} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
		}
		}while(true);
	}

}
