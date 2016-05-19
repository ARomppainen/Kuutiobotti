package kuutiopc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.GroupLayout;
import javax.swing.JPanel;

public class KuutioPaneeli extends JPanel {
    
    private static final int KANTTI = 30;
    private static final int KUUTIO_KOKO = (KANTTI * 4) + 4;
    
    private Kuutio kuutio;
    private KuutioSivu[] sivut;
    
    public KuutioPaneeli() {
        sivut = new KuutioSivu[6];
        
        
        this.setSize(new Dimension(KUUTIO_KOKO*5, KUUTIO_KOKO*4));
        this.setPreferredSize(new Dimension(KUUTIO_KOKO * 5, KUUTIO_KOKO * 4));
        
        for(int i = 0 ; i < sivut.length ; i++) {
            sivut[i] = new KuutioSivu();
            this.add(sivut[i]);
        }
        
        GroupLayout gl = new GroupLayout(this);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
        this.setLayout(gl);
        
        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sivut[Kuutio.TAKA]))
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sivut[Kuutio.VASEN]))
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sivut[Kuutio.YLÄ])
                    .addComponent(sivut[Kuutio.ETU])
                    .addComponent(sivut[Kuutio.ALA]))
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sivut[Kuutio.OIKEA]))
                );
        
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sivut[Kuutio.YLÄ]))
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sivut[Kuutio.TAKA])
                    .addComponent(sivut[Kuutio.VASEN])
                    .addComponent(sivut[Kuutio.ETU])
                    .addComponent(sivut[Kuutio.OIKEA]))
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sivut[Kuutio.ALA]))
                );
    }
    
    public void setKuutio(Kuutio kuutio) {
        this.kuutio = kuutio;
    }
    
    public void update() {
        for(int i = 0 ; i < 6 ; i++) {
            sivut[i].setVärit(kuutio.getColors(i));
        }
    }
    
    class KuutioSivu extends JPanel {
        
        private Color[][] sivu;
        
        public KuutioSivu() {
            sivu = null;
            this.setPreferredSize(new Dimension(84,84));
        }
        
        public void setVärit(Color[][] c) {
            sivu = c;
            this.repaint();
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (sivu != null) {
                for(int i = 0 ; i < 4 ; i++) {
                    for(int j = 0 ; j < 4 ; j++) {
                        
                        g.setColor(sivu[i][j]);
                        
                        if (sivu[i][j].equals(Color.orange)) {
                            g.setColor(new Color(255, 100, 0));
                        }
                        else if (sivu[i][j].equals(Color.red)) {
                            g.setColor(new Color(255, 0, 0));
                        }
                            
                        g.fillRect(i * KANTTI + i, j * KANTTI + j, KANTTI, KANTTI);
                    }
                }
            }
        }
    }
}
