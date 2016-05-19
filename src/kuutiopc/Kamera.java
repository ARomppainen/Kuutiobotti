package kuutiopc;

/*
 * Created on 13-Apr-2005
 *
 * Grab a single frame from the capture device in regular time intervals.
 * Transform the frame into an image (Image, BufferedImage) and paint it inside
 * the panel by overwriting paintComponent() of the JPanel. Also paint the a
 * rotated date string.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import javax.media.Buffer;
import javax.media.CannotRealizeException;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Kamera extends JPanel {

    private Player player = null;
    private FrameGrabbingControl frameGrabber;

    public Kamera() {
                // Create capture device
        Vector devices = CaptureDeviceManager.getDeviceList(null);
        CaptureDeviceInfo cdi = null;
        for (Iterator i = devices.iterator(); i.hasNext();) {
            cdi = (CaptureDeviceInfo) i.next();
            /* Get the first Video For Windows (VFW) capture device.
             * Use the JMF registry tool in the bin directory of the JMF
             * distribution to detect available capture devices on your
             * computer.
             */
            if (cdi.getName().startsWith("vfw:")) {
                break;
            }
        }

        try {
            player = Manager.createRealizedPlayer(cdi.getLocator());
            player.start();
        } catch (NoPlayerException e) {
            e.printStackTrace();
        } catch (CannotRealizeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public BufferedImage getBuffImg() {
        
        // Grab a frame from the capture device
        frameGrabber = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");

        Buffer buf = frameGrabber.grabFrame();
        // Convert frame to an buffered image so it can be processed and saved
        BufferToImage bti = new BufferToImage((VideoFormat)buf.getFormat());
        Image img = bti.createImage(buf);
        //buffImg = new BufferedImage(400, 400,BufferedImage.TYPE_INT_RGB);
        
        if (img instanceof BufferedImage) {
            return (BufferedImage)img;
        } else {
            return null;
        }
    }
    
    public Color[][] getSide() {
        // Grab a frame from the capture device
        frameGrabber = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");

        Buffer buf = frameGrabber.grabFrame();
        // Convert frame to an buffered image so it can be processed and saved
        BufferToImage bti = new BufferToImage((VideoFormat) buf.getFormat());
        Image image = bti.createImage(buf);
        //buffImg = new BufferedImage(400, 400,BufferedImage.TYPE_INT_RGB);



        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        // VARIEN OTTO KUVASTA

        // aloitus x, y sijainti
        int x = 45;
        int y = 45;
        int x_temp;

        // palan leveys
        int palankoko = 45;

        int vari = 0;
        Color[][] varit = new Color[4][4];
        
        double redSum = 0;
        double greenSum = 0;
        double blueSum = 0;

        for (int i = 0; i < 4; i++) // rivi
        {
            x_temp = x; // x aloitusijaintiin palaaminen

            for (int j = 0; j < 4; j++) // palanen
            {
                /*
                int c = bimage.getRGB(x_temp, y);
                int red = (c & 0x00ff0000) >> 16;
                int green = (c & 0x0000ff00) >> 8;
                int blue = c & 0x000000ff;
                */
                
                int[] c = bimage.getRGB(x_temp, y, 7, 10, null, 0, 7);

                int red = 0;
                int green = 0;
                int blue = 0;
                
                for (int RGB : c){
                    red += (RGB & 0x00ff0000) >> 16;
                    green += (RGB & 0x0000ff00) >> 8;
                    blue += RGB & 0x000000ff;
                }
                
                red = red/c.length;
                green = green/c.length;
                blue = blue/c.length;
                
                Color color = new Color(red, green, blue);
                System.out.println(vari+1 + ": " + color);                  // POIS
                
                redSum += red;
                greenSum += green;
                blueSum += blue;
                
                double keltDist = Math.sqrt((Math.pow(210-red, 2)) + (Math.pow(185-green, 2)) + (Math.pow(62-blue, 2)));
                double vihrDist = Math.sqrt((Math.pow(42-red, 2)) + (Math.pow(180-green, 2)) + (Math.pow(61-blue, 2)));
                double punaDist = Math.sqrt((Math.pow(191-red, 2)) + (Math.pow(51-green, 2)) + (Math.pow(34-blue, 2)));
                double siniDist = Math.sqrt((Math.pow(0-red, 2)) + (Math.pow(135-green, 2)) + (Math.pow(230-blue, 2)));
                double valkDist = Math.sqrt((Math.pow(170-red, 2)) + (Math.pow(175-green, 2)) + (Math.pow(173-blue, 2)));
                double oranDist = Math.sqrt((Math.pow(225-red, 2)) + (Math.pow(108-green, 2)) + (Math.pow(0-blue, 2)));
                
                if (isLowest(punaDist, keltDist, vihrDist, siniDist, valkDist, oranDist)) {
                    color = color.red;
                    System.out.println("punainen");
                } else if (isLowest(vihrDist, punaDist, keltDist, siniDist, valkDist, oranDist)) {
                    color = color.green;
                    System.out.println("vihree");
                } else if (isLowest(siniDist, punaDist, keltDist, vihrDist, valkDist, oranDist)) {
                    color = color.blue;
                    System.out.println("sininen");
                } else if (isLowest(oranDist, punaDist, keltDist, siniDist, valkDist, vihrDist)) {
                    color = color.orange;
                    System.out.println("oranssi");
                }else if (isLowest(keltDist, punaDist, vihrDist, siniDist, valkDist, oranDist)) {
                    color = color.yellow;
                    System.out.println("keltanen");
                } else if (isLowest(valkDist, punaDist, keltDist, siniDist, vihrDist, oranDist)) {
                    color = color.white;
                    System.out.println("valkonen");
                }
                
                // lisays tauluun
                varit[j][i] = color;
                vari++;
                // kohdan siirto
                x_temp += palankoko;

            }

            y += palankoko;
        }
        
        System.out.println("RED AVG: "+ redSum / 16);
        System.out.println("GREEN AVG: "+ greenSum / 16);
        System.out.println("BLUE AVG: "+ blueSum / 16);
       
        return varit;
    }
    
    public boolean isLowest(double d1, double... d2) {
        
        for (double d : d2) {
            if (d < d1) {
                return false;
            }
        }
        
        return true;
    }
    
    public void closePlayer(){
        try{
            player.close();
        }catch(Exception e){
            
        }
    }
    
    // hakee kuution yhden sivun varit, palauttaa 16 pituisen Color taulukon
    public Color[] getSideColors(BufferedImage kuva) {
        // VARIEN OTTO KUVASTA
        
        // aloitus x, y sijainti
        int x = 30;
        int y = 30;
        int x_temp;
        
        // palan leveys
        int palankoko = 60;
        
        int vari = 0;
        Color[] varit = new Color[16];
        
        for (int i=0; i<4; i++) // rivi
        {
            x_temp = x; // x aloitusijaintiin palaaminen
            
            for (int j=0; j<4; j++) // palanen
            {
                int c = kuva.getRGB(x_temp, y);
                int red = (c & 0x00ff0000) >> 16;
                int green = (c & 0x0000ff00) >> 8;
                int blue = c & 0x000000ff;
                Color color = new Color(red, green, blue);
                System.out.println(color);
                
                // tunnista vari tietylta rangelta
                if (red > 230 && red < 250 && green > 230 && green < 250 && blue < 10)
                    color = color.YELLOW;
                else if (red > 20 && red < 40 && green > 190 && green < 210 && blue < 10)
                    color = color.GREEN;
                else if (red > 220 && red < 240 && green > 10 && blue < 10)
                    color = color.RED;
                else if (red < 10 && green > 150 && green < 170 && blue > 220 && blue < 240)
                    color = color.BLUE;
                
                //System.out.println(color);
                // lisays tauluun
                varit[vari] = color;
                vari++;
                // kohdan siirto
                x_temp += palankoko;
                
            }
            
            y+= palankoko;
        }
        
        return varit;
    }
    
	// This method returns a buffered image with the contents of an image
	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage)image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see Determining If an Image Has Transparent Pixels
		boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(
				image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	// This method returns true if the specified image has transparent pixels
	public static boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {
			BufferedImage bimage = (BufferedImage)image;
			return bimage.getColorModel().hasAlpha();
		}

		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		 PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
		}

		// Get the image's color model
		ColorModel cm = pg.getColorModel();
		return cm.hasAlpha();
	}
}
