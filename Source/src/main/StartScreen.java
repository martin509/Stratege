
package main;

/**
 *
 * @author Martin
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 * Main menu
 * @author Martin Rivard
 */
public class StartScreen extends JPanel{
    static JSlider foliageSlider, swampSlider, hillSlider, scaleSlider;
        static JLabel foliageLabel, swampLabel, hillLabel, scaleLabel;
    public static JButton genButton, loadButton; //public as they are used in EndScreen
    private static JButton logo;
    private static JTextArea credits;
    
    /**
     * 
     * @author Martin Rivard
     */
    public StartScreen(){
        initGenButton();
        initLoadButton();
        initLogo();
        initCredits();
        
        foliageSlider = initSlider();
        foliageLabel = initLabel("Foliage Amount");
        
        swampSlider = initSlider();
        swampLabel = initLabel("Wetness");
        
        hillSlider = initSlider();
        hillLabel = initLabel("Hilliness");
        
        scaleSlider = initSlider(200, 400);
        scaleLabel = initLabel("Terrain Regularity");

        setSize(960, 512);
        setLayout(new AbsoluteLayout());
        setBackground(Color.BLACK);
        add(genButton, new AbsoluteConstraints(640, 320, 256, 64));
        add(loadButton, new AbsoluteConstraints(640, 416 , 256, 64));
        add(logo, new AbsoluteConstraints(640, 32, 256, 64));
        add(credits, new AbsoluteConstraints(640, 112, 256, 192));
        addSliders(new JSlider[]{foliageSlider, swampSlider, hillSlider, scaleSlider}, new JLabel[]{foliageLabel, swampLabel, hillLabel, scaleLabel});
    }
    
    /**
     * 
     * @author Martin Rivard
     */
    public void addSliders(JSlider[] sliders, JLabel[] labels){
        if (sliders.length == labels.length){
            for (int n = 0; n < sliders.length; n++){
                this.add(labels[n], new AbsoluteConstraints(64, (32 + 128*n), 512, 32));
                this.add(sliders[n], new AbsoluteConstraints(64, (64 + 128*n), 512, 32));
            }
        }
    }
    
    /**
     * 
     * @author Martin Rivard
     */
    private static void initCredits(){
        credits = new JTextArea();
        
        credits.setBorder(null);
        credits.setSize(256,208);
        credits.setFont(new Font("Monospaced", Font.PLAIN, 12));
        credits.setBorder(new LineBorder(Color.GREEN, 1));
        credits.setBackground(Color.BLACK);
        credits.setForeground(Color.GREEN);
        credits.setVisible(true);
    }
    
    /**
     * 
     * @author Martin Rivard
     */
    private static void initLogo(){
        
        logo = new JButton();
        
        //logo.setBorder(null);
        Main.initButtonUI(logo, true, Color.GREEN);
        logo.setSize(256,64);
        
        logo.setText("Stratege v. 1.0.1");
        logo.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent me) { }
            
            @Override
            public void mousePressed(MouseEvent me) {
                if (!credits.getText().equals("")){
                    credits.setText("");
                }else{
                    credits.setText("Credits" + 
                        "\nRendering/UI: Martin Rivard" + 
                        "\nSavegame System: Martin Rivard" + 
                        "\nUnit System: Alex Dingwall" + 
                        "\nExternal Resources: Alex Dingwall" + 
                        "\nTurn System: Jacob Willemse" + 
                        "\nTextures: Jacob Willemse");
                }
                
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {  }
            
            @Override
            public void mouseEntered(MouseEvent me) {  }
            
            @Override
            public void mouseExited(MouseEvent me) {  }
        });
    }
    
    /**
     * 
     * @author Martin Rivard
     */
    private static void initGenButton(){
        genButton = new JButton("New Game");
        genButton.setBorder(null);
        
        Main.initButtonUI(genButton, true, Color.GREEN);
        
        //regenButtonLabel = new JLabel();
        //regenButtonLabel.setIcon(new ImageIcon("regenbutton.png"));
        //GenButton.add(regenButtonLabel, new AbsoluteConstraints(0, 0, 128, 32));
        
        genButton.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent me) { }
            
            @Override
            public void mousePressed(MouseEvent me) {
                Main.goToGame(false);
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {  }
            
            @Override
            public void mouseEntered(MouseEvent me) {  }
            
            @Override
            public void mouseExited(MouseEvent me) {  }
        });
    }
    
    /**
     * 
     * @author Martin Rivard
     */
    private static void initLoadButton(){
        loadButton = new JButton("Load Game");
        loadButton.setBorder(null);
        
        Main.initButtonUI(loadButton, true, Color.GREEN);
        
        loadButton.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent me) { }
            
            @Override
            public void mousePressed(MouseEvent me) {
                Main.goToGame(true);
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {  }
            
            @Override
            public void mouseEntered(MouseEvent me) {  }
            
            @Override
            public void mouseExited(MouseEvent me) {  }
        });
    }
    
    /**
     * 
     * @author Martin Rivard
     */
    private static JLabel initLabel(String text){
        JLabel label = new JLabel();
        
        label.setBackground(Color.black);
        label.setForeground(Color.green);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Monospaced", Font.PLAIN, 18));
        label.setText(text);
        label.setVisible(true);
        
        return label;
    }
    
    /**
     * 
     * @author Martin Rivard
     */
    private static JSlider initSlider(){
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 400, 600, 500);
            
        slider.setBackground(Color.black);
        slider.setForeground(Color.green);
        
        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(5);
        slider.setMajorTickSpacing(10);
        slider.setSnapToTicks(true);
        
        return slider;
    }
    
    /**
     * 
     * @author Martin Rivard
     */
    private static JSlider initSlider(int min, int max){
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, (min+max)/2);
        
        int range = max-min;
        int minorSpacing = (int)((double)range)/40;
        int majorSpacing = (int)((double)range)/20;
        
        slider.setBackground(Color.black);
        slider.setForeground(Color.green);
        
        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(minorSpacing);
        slider.setMajorTickSpacing(majorSpacing);
        slider.setSnapToTicks(true);
        
        return slider;
    }
    
    
    
}
