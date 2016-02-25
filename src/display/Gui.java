package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import logic.Settings;
import logic.Simulation;


/**
 * This class provides a graphical user interface to the boids simulation
 */
public class Gui extends JFrame {
    private Simulation sim;
    private OptionsPanel options;
    
    /**
     * Creates a new gui window. Loads Gui strings from 'messages' file.
     * 
     * @param sim simulation
     */
    public Gui(Simulation sim) {
        this.setTitle("EA");
        this.sim = sim;
        
        JPanel panel = new JPanel();
        this.setContentPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        
        options = new OptionsPanel(this.sim);
        panel.add(options);
        
        //this.createMenu();
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    
    /**
     * Creates the menubar.
     */
    private void createMenu() {
        JMenuBar menubar = new JMenuBar();
        JMenu simulationMenu = new JMenu("Simulation");
        JMenu optionsMenu = new JMenu("Options");
        simulationMenu.setMnemonic(KeyEvent.VK_S);
        optionsMenu.setMnemonic(KeyEvent.VK_O);
        
        JMenuItem newSimulation = new JMenuItem("New simulation", KeyEvent.VK_N);
        newSimulation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sim.stop();
                setTitle("EA");
            }
        });
        
        JMenuItem quit = new JMenuItem("Quit", KeyEvent.VK_Q);
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        simulationMenu.add(newSimulation);
        simulationMenu.addSeparator();
        simulationMenu.add(quit);
        

        simulationMenu.getPopupMenu().setLightWeightPopupEnabled(false); // avoid drawing area overlap
        optionsMenu.getPopupMenu().setLightWeightPopupEnabled(false); // avoid drawing area overlap
        
        menubar.add(simulationMenu);
        menubar.add(optionsMenu);
        this.setJMenuBar(menubar);
    }
    

    /**
     * Creates a gui and starts the simulation.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        Simulation sim = new Simulation(new Settings());
        Gui gui = new Gui(sim);
    }
    
}