package kuutiopc;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Kontrolleri {
    
    public static final String ROBOT = "jonnebot";
    
    private RatkaisuThread ratkaisuthread;
    private Kamera kamera;
    private Ratkaisija ratkaisija;
    private Kuutio kuutio;
    private UI ui;
    private NXTComm nxtcomm;
    private NXTInfo info;
    private OutputStream os;
    private InputStream is;
    private DataOutputStream dos;
    private DataInputStream dis;
    private boolean connected;
    private boolean keskeytetty;
    private boolean lopeta;
    private ArrayDeque<String> jono = null;
    private ArrayList<String> komennot = null;
    private int maxmove;
    private Color[][] varit = null;
    
    public Kontrolleri(UI ui) {
        this.ui = ui;
        ratkaisija = new Ratkaisija();
        kuutio = new Kuutio();
        connected = false;
        keskeytetty = false;
        lopeta = false;
        maxmove = 0;
    }
    
    public void luoBTYhteys() {
        try {
            nxtcomm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
            System.out.println("NXT Factory suoritettu.");
        } catch (NXTCommException e) {
            System.out.println("Exception caught when trying to create nxtcomm.");
        }
        
        System.out.println("NXT comm null check");
        if (nxtcomm != null) {
            System.out.println("NXT comm !null");
            NXTInfo[] nxtinfo = null;
            System.out.println("NXT info = null");
            try {
                System.out.println("Etsitään robottia");
                nxtinfo = nxtcomm.search(ROBOT);
                System.out.println("Robotti löytyi");
            } catch (NXTCommException e) {
                System.out.println(ROBOT + " was not found.");
            }
            System.out.println("nxtinfo null check");
            if (nxtinfo != null) {
                System.out.println("NXTinfo !null");
                info = nxtinfo[0];

                try {
                    System.out.println("yritetään avata yhteys");
                    nxtcomm.open(info);
                    os = nxtcomm.getOutputStream();
                    dos = new DataOutputStream(os);
                    is = nxtcomm.getInputStream();
                    dis = new DataInputStream(is);
                    //gui.setConnectionStatus(true);
                    System.out.println("connectattu");
                    connected = true;

                } catch (NXTCommException e) {
                    System.out.println("Exception caught when trying to open connection.");
                }
            }
        }
    }
    
    public void aloita() {
        ratkaisuthread = new RatkaisuThread();
        ratkaisuthread.start();  
    }
    
    public void jatka() {
        keskeytetty = false;
    }
    
    public void keskeyta() {
        keskeytetty = true;
    }
        
    public void lopeta() {
        keskeytetty = false;
        lopeta = true;
    }
    
    public Kuutio getKuutio() {
        return kuutio;
    }
    
    private class RatkaisuThread extends Thread {
         /*
         * Timer paalle
         * Kuvat kuution sivuista.
         * Ratkaisija ratkaisee kuution.
         * maxMove maara asetetaan UIhin
         * aloittaa komentojen lähetyksen loopissa.
         */
  
        public void run() {
            kamera = new Kamera();
            System.out.println("Kaynnistetaan kamera...");
            // odotetaan kameran kaynnistymista
            try{
                Thread.sleep(3000);
            }catch(InterruptedException e){}
            
            System.out.println("Kamera kaynnistetty.");
            
            System.out.println("Otetaan kuva ja varit...");
            varit = kamera.getSide();
            kuutio.setSivu(Kuutio.ETU, varit);
            ui.updateKuutioPaneeli();
            System.out.println("Done.");
            
            try {
                // get komento
                // send komento
                System.out.println("Lähetetään");
                dos.writeUTF("S");
                dos.flush();
                // wait for answer
                System.out.println("Odotetaan");
                dis.readInt();
            } catch (IOException e) {}
            
            int sivu = 0;
            
            // kuvien ottaminen
            for (int k=1; k<=4; k++){
                
                switch(k) {
                    case 1: sivu = Kuutio.OIKEA; break;
                    case 2: sivu = Kuutio.TAKA; break;
                    case 3: sivu = Kuutio.VASEN; break;
                    case 4: sivu = Kuutio.ALA; break;
                }
                
                try {
                    sleep(3000);
                } catch (InterruptedException e) {}
                
                System.out.println("Otetaan kuva ja varit...");
                varit = kamera.getSide();
                kuutio.setSivu(sivu, varit);
                
                if (k == 4) {
                    kuutio.fixAla();
                }
                
                ui.updateKuutioPaneeli();
                System.out.println("Done.");
                
                // suorita kääntö
                try {
                    System.out.println("Suoritetaan sivun kaanto.");
                    dos.writeInt(1);
                    dos.flush();
                }catch (IOException e){
                    System.out.println("Komennon lahetys epaonnistui.");
                }
                // odota vastausta
                try {
                    System.out.println("Odotetaan vastausta...");
                    dis.readInt();
                }catch (IOException e){}
            }
            
            try {
                sleep(3000);
            } catch (InterruptedException e) {}
            
            System.out.println("Otetaan kuva ja varit...");
            varit = kamera.getSide();
            kuutio.setSivu(Kuutio.YLÄ, varit);
            kuutio.fixYla();
            ui.updateKuutioPaneeli();
            System.out.println("Done.");
            
            System.out.println("Tarkistetaan kuutio");
            
            if (!kuutio.kuutioOK()) {
                System.out.println("Kuutio virheellinen!");
                ui.lopeta();
                kamera.closePlayer();
                return;
            } else {
                System.out.println("Kuutio OK!");
            }
            
            
            int koko = 0;
            // komento osien suoritus
            //jono.clear();
            //komennot.clear();
            jono = new ArrayDeque<>();
            komennot = new ArrayList<>();

            // tee lopeta = true kun ratkaistu ja updatee UI napit
            
            if (!lopeta) {
                // hae ratkasu [i]
                //ratkaisija.jotai();
                komennot = ratkaisija.ratkaiseKuutio(kuutio);
                
                // for loop get ja aseta queueen komento, update maxmove in ui
                koko = komennot.size();
                maxmove += koko;
                ui.setMoveMax(Integer.toString(maxmove));

                for (int j = 0; j < koko; j++) {
                    jono.add(komennot.get(j));
                }

                while (!jono.isEmpty()) {
                    if (lopeta) {
                        break;
                    }
                    while (keskeytetty) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {}
                    }
                    try {
                        // get komento
                        // send komento
                        String komento = jono.poll();
                        dos.writeUTF(komento);
                        dos.flush();
                        // wait for answer
                        dis.readInt();
                        
                        kuutio.käännäSivua(komento);
                        ui.updateKuutioPaneeli();
                        ui.incMoveMaara();
                    } catch (IOException e) {}
                }
            }
            
            
            ui.lopeta();
            kamera.closePlayer();
        } 
    }
    
    public static void main(String[] args) {
       UI ui = new UI();
       Kontrolleri k = new Kontrolleri(ui);
       ui.setKontrolleri(k);
    }
}