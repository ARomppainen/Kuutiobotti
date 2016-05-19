package kuutiopc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Dimension;

public class UI extends JFrame {

    private Kontrolleri kontrolleri;
    private KuutioPaneeli kuutiopaneeli;
    private JPanel paneltimer, panelmove, panelcube, panelbuttons;
    private JLabel lbltimervalue, lblmove, lblmovevalue, lblmovetotal;
    private int wwidth = 800, wheight = 500;
    private JButton btnaloita, btnkeskeyta, btnlopeta, btnluoyhteys;
    private volatile boolean keskeytetty = false;
    private Ajastin ajastin;
    private long aika;
    
    public UI() {
        this.setTitle("sUPERSiK CUBE SOLVER 0.1");
        this.setPreferredSize(new Dimension(wwidth, wheight));
        this.setResizable(false);
        //this.kontrolleri = kontrolleri;
        // keskitys ruudulle
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - wwidth)/2;
        int y = (dim.height - wheight)/2;
        this.setLocation(x, y);
        
        GroupLayout gl = new GroupLayout(this.getContentPane());
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
        this.setLayout(gl);

        panelcube = new JPanel();
        paneltimer = new JPanel();
        panelmove = new JPanel();
        panelbuttons = new JPanel();

        panelcube.setBorder(BorderFactory.createLineBorder(Color.black));
        paneltimer.setBorder(BorderFactory.createLineBorder(Color.black));
        panelmove.setBorder(BorderFactory.createLineBorder(Color.black));
        panelbuttons.setBorder(BorderFactory.createLineBorder(Color.black));
        
        lbltimervalue = new JLabel();
        lbltimervalue.setFont(new Font("Arial", Font.BOLD, 24));
        lbltimervalue.setText("00:00:00");
        lblmove = new JLabel("Move");
        lblmovevalue = new JLabel("0");
        lblmovetotal = new JLabel("/ 0");

        btnluoyhteys = new JButton("Luo BT-yhteys");
        btnluoyhteys.setPreferredSize(new Dimension(120,25));
        btnaloita = new JButton("Aloita");
        btnaloita.setPreferredSize(new Dimension(120,25));
        btnkeskeyta = new JButton("Keskeyta");
        btnkeskeyta.setPreferredSize(new Dimension(120,25));
        btnlopeta = new JButton("Lopeta");
        btnlopeta.setPreferredSize(new Dimension(120,25));
        
        btnluoyhteys.addActionListener(new ActionForBtnLuoyhteys());
        btnaloita.addActionListener(new ActionForBtnAloita());
        btnkeskeyta.addActionListener(new ActionForBtnKeskeyta());
        btnlopeta.addActionListener(new ActionForBtnLopeta());

        btnaloita.setEnabled(false);
        btnkeskeyta.setEnabled(false);
        btnlopeta.setEnabled(false);

        panelcube.setBackground(Color.white);

        //Kuutio kuutio = new Kuutio();
        kuutiopaneeli = new KuutioPaneeli();
        //kuutiopaneeli.setKuutio(kuutio);
        //kuutiopaneeli.update();
        kuutiopaneeli.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(kuutiopaneeli);
        
        this.add(panelcube);
        this.add(paneltimer);
        this.add(panelmove);
        this.add(panelbuttons);

        paneltimer.add(lbltimervalue);

        panelmove.add(lblmove);
        panelmove.add(lblmovevalue);
        panelmove.add(lblmovetotal);

        panelbuttons.add(btnluoyhteys);
        panelbuttons.add(btnaloita);
        panelbuttons.add(btnkeskeyta);
        panelbuttons.add(btnlopeta);
        
        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(kuutiopaneeli)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(paneltimer, 150,150,150)
                    .addComponent(panelmove, 150,150,150)
                    .addComponent(panelbuttons, 150,150,150))
                );
        
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(kuutiopaneeli)
                    .addGroup(gl.createSequentialGroup()
                        .addComponent(paneltimer, 35, 35, 35)
                        .addComponent(panelmove, 35, 35, 35)
                        .addGap(240, 240, 240)
                        .addComponent(panelbuttons)))
                );

        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void setKontrolleri(Kontrolleri k){
        this.kontrolleri = k;
        kuutiopaneeli.setKuutio(k.getKuutio());
        kuutiopaneeli.update();
    }
    
    public void updateKuutioPaneeli() {
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                kuutiopaneeli.update();
            }
        });
    }
    
    public void setTimerValue(String s) {
        lbltimervalue.setText(s);
    }
    
    public void setMoveMax(String s)
    {
        lblmovetotal.setText("/ " + s);
    }
    
    public void setMoveMaara(String s)
    {  
        lblmovevalue.setText(s);
    }
    
    public void incMoveMaara()
    {
        int m = Integer.parseInt(lblmovevalue.getText());
        m++;
        lblmovevalue.setText(Integer.toString(m));
    }
    
    public void lopeta() {
            ajastin.lopeta();
            keskeytetty = false;
            btnaloita.setText("Aloita");
            btnaloita.setEnabled(true);
            btnkeskeyta.setEnabled(false);
            btnlopeta.setEnabled(false);
    }
    
    class ActionForBtnLuoyhteys implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            btnaloita.setEnabled(true);
            btnluoyhteys.setEnabled(false);
            kontrolleri.luoBTYhteys();
            //System.out.println("ActionForBtnLuoyhteys");
        }
    }

    class ActionForBtnAloita implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!keskeytetty) { 
                setMoveMax("0");
                setMoveMaara("0");
                btnaloita.setEnabled(false);
                btnkeskeyta.setEnabled(true);
                btnlopeta.setEnabled(true);
                //System.out.println("ActionForBtnAloita");
                
                ajastin = new Ajastin(0);                       // Alustetaan ajastin alkuarvolla 0
                ajastin.start();                                // Käynnistetään ajastin
                
                kontrolleri.aloita();
                //setMoveMax("100");
                //setMoveMaara("1");
                //incMoveMaara();                        
            } else {
                ajastin = new Ajastin(aika);                    // Alustetaan uusi ajastin keskeytettäessä otetulla ajalla
                ajastin.start();                                // Käynnistetään ajastin
                keskeytetty = false;
                btnaloita.setText("Aloita");
                btnaloita.setEnabled(false);
                btnkeskeyta.setEnabled(true);
                btnlopeta.setEnabled(true);
                //System.out.println("ActionForBtnAloita kun keskeytetty");
                kontrolleri.jatka();
            }
        }
    }

    class ActionForBtnKeskeyta implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            aika = ajastin.getTime();                           // Otetaan talteen mitattu aika, kun keskeytetään
            ajastin.lopeta();                                   // Sammutetaan ajastin
            keskeytetty = true;
            btnaloita.setText("Jatka");
            btnaloita.setEnabled(true);
            btnkeskeyta.setEnabled(false);
            //System.out.println("ActionForBtnKeskeyta");
        }
    }

    class ActionForBtnLopeta implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            ajastin.lopeta();
            keskeytetty = false;
            btnaloita.setText("Aloita");
            btnaloita.setEnabled(true);
            btnkeskeyta.setEnabled(false);
            btnlopeta.setEnabled(false);
            //System.out.println("ActionForBtnLopeta");
        }
    }
    
    class Ajastin extends Thread {
    
		private long alkuaika, curaika, saatuAika;
		private volatile boolean lopeta = false;
		
		public Ajastin (long aika)
		{
			saatuAika = aika;
		}
		
		@Override
		public void run() {
			getAlkuaika();

			while (!lopeta) {
				getTime();
				paivitaAika();
						 
				try {
					sleep(10);
				} catch (Exception e) {
				}
			}

		}
		
		public void lopeta() {
			lopeta = true;
		}
		
		public long getTime(){
			
			curaika = (long)(System.nanoTime() - alkuaika)/10000000 + saatuAika;
			return curaika;
		}
		
		public void getAlkuaika(){
			alkuaika = System.nanoTime();
		}
		
		public void paivitaAika() {
		 
			String s = "";
			
			int minuutit = (int) (curaika  / 100) / 60;
			int sekunnit = (int) (curaika  / 100) % 60;   
			int sadasosat = (int) curaika % 100;

			s = ((minuutit < 10 ? "0" : "") + minuutit
					+ ":" + (sekunnit < 10 ? "0" : "") + sekunnit
					+ ":" + (sadasosat < 10 ? "0" : "") + sadasosat);
			
			setTimerValue(s);
		}
	}
}
  
