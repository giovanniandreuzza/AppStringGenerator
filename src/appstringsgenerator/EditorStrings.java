package appstringsgenerator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import javafx.util.Pair;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Giovanni
 */
public class EditorStrings extends JFrame {
    private DefaultListModel strings = new DefaultListModel();
    private DefaultListModel details = new DefaultListModel();
    
    private final JPanel panel = new JPanel();
    private final JMenuBar menuBar = new JMenuBar();
    
    private final JPanel stringPanel = new JPanel();
    private final JPanel detailPanel = new JPanel();
    
    private final JButton addNewStringButton = new JButton("Add String");
    private final JButton addNewDetailButton = new JButton("Add Detail");
    
    private JScrollPane stringListPanel = new JScrollPane();
    private JScrollPane detailListPanel = new JScrollPane();
    
    private final JList stringList = new JList(strings);
    private final JList detailList = new JList(details);
    
    private final JFileChooser fileChooser = new JFileChooser();
    
    private int countStrings = 1;
    private int countDetails = 1;
    
    private final ArrayList<Pair> stringListModel = new ArrayList<>();
    
    public EditorStrings(){
        setupUI();
    }
    
    private void setupUI(){
        //menu bar creation
        setupMenuBar();
        setJMenuBar(menuBar);
        
        panel.setLayout(new GridLayout(1,2));
        stringPanel.setLayout(new BoxLayout(stringPanel, BoxLayout.Y_AXIS));
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        
        //setup buttons
        setupButtons();
        
        strings.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                
            }
        });
        
        //add frame's components
        stringList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        stringList.setLayoutOrientation(JList.VERTICAL_WRAP);
        stringList.setVisibleRowCount(countStrings);
        
        stringListPanel = new JScrollPane(stringList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        stringPanel.add(addNewStringButton);
        stringPanel.add(stringListPanel);
        
        detailList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        detailList.setLayoutOrientation(JList.VERTICAL_WRAP);
        detailList.setVisibleRowCount(countDetails);
        
        detailListPanel = new JScrollPane(detailList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        detailPanel.add(addNewDetailButton);
        detailPanel.add(detailListPanel);
        
        panel.add(stringPanel);//, "West");
        panel.add(detailPanel);//, "Center");
        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void setupMenuBar(){
        JMenu menu;
        JMenuItem voice;
        
        //menu file
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        
        //voice
        voice = new JMenuItem("Nuovo", KeyEvent.VK_N);
        voice.addActionListener(v -> {
            onNewClicked();
        });
        menu.add(voice);
        
        voice = new JMenuItem("Apri", KeyEvent.VK_R);
        voice.addActionListener(v -> {
            onOpenClicked();
        });
        menu.add(voice);
        
        voice = new JMenuItem("Salva", KeyEvent.VK_S);
        voice.addActionListener(v -> {
            onSaveClicked();
        });
        menu.add(voice);
        
        menu.addSeparator();
        
        voice = new JMenuItem("Esporta per Android", KeyEvent.VK_A);
        voice.addActionListener(v -> {
            onExportToAndroid();
        });
        menu.add(voice);
        
        voice = new JMenuItem("Esporta per Swift", KeyEvent.VK_I);
        voice.addActionListener(v -> {
            onExportToSwift();
        });
        menu.add(voice);
        
        menu.addSeparator();
        
        voice = new JMenuItem("Esci", KeyEvent.VK_E);
        voice.addActionListener(v -> {
            onExitClicked();
        });
        menu.add(voice);
    }
    
    private void setupButtons(){
        addNewStringButton.addActionListener(v -> {
            onNewStringClicked();
        });
        
        addNewDetailButton.addActionListener(v -> {
            onNewDetailClicked();
        });
    }

    private void onNewClicked() {
        //TODO
    }

    private void onOpenClicked() {
        //TODO
    }

    private void onSaveClicked() {
        //TODO
    }

    private void onExportToAndroid() {
        //TODO
    }

    private void onExportToSwift() {
        //TODO
    }

    private void onExitClicked() {
        System.exit(0);
    }

    private void onNewStringClicked() {
        String input = JOptionPane.showInputDialog(this, "Inserisci la chiave della stringa");
        if(input != null){
            stringList.setVisibleRowCount(countStrings++);
            strings.addElement(input);
        }
    }

    private void onNewDetailClicked() {
        int status = fileChooser.showOpenDialog(this);
        if(status == JFileChooser.APPROVE_OPTION){
            File f = fileChooser.getSelectedFile();
            detailList.setVisibleRowCount(countDetails++);
            details.addElement(new ImageIcon(f.getAbsolutePath()));
        }
        
    }
  
}

