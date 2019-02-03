package appstringsgenerator.stringseditor;

import appstringsgenerator.models.StringModel;
import appstringsgenerator.viewmodel.JLabelDetailModel;
import appstringsgenerator.viewmodel.JLabelStringModel;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author Giovanni
 */
public class StringsEditorView extends JPanel implements StringsEditorContract.View {
    private final StringsEditorContract.Presenter presenter;
    
    private final JMenuBar menuBar = new JMenuBar();
    
    private final JPanel stringPanel = new JPanel();
    private final JButton addNewStringButton = new JButton("Add String");
    private JScrollPane stringListPanel = new JScrollPane();
    private final JPanel stringScrollPanel = new JPanel();
    
    private final JPanel detailPanel = new JPanel();
    private final JButton addNewDetailButton = new JButton("Add Detail");
    private JScrollPane detailListPanel = new JScrollPane();
    private final JPanel detailScrollPanel = new JPanel();
    
    private final JFileChooser fileChooser = new JFileChooser();
    
    public StringsEditorView(JFrame frame){
        presenter = new StringsEditorPresenter(this);
        
        //menu bar creation
        setupMenuBar();
        frame.setJMenuBar(menuBar);
        
        setupUI();
    }
    
    private void setupUI(){
        //Setup main components
        setLayout(new GridLayout(1,2));
        stringPanel.setLayout(new BoxLayout(stringPanel, BoxLayout.Y_AXIS));
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        
        //Setup buttons
        setupButtons();
        
        //Add components to panel
        stringScrollPanel.setLayout(new BoxLayout(stringScrollPanel, BoxLayout.PAGE_AXIS));
        stringScrollPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        stringListPanel = new JScrollPane(stringScrollPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        stringPanel.add(addNewStringButton);
        stringPanel.add(stringListPanel);
        
        detailScrollPanel.setLayout(new GridLayout(2, 4, 30, 30));
        detailScrollPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        detailListPanel = new JScrollPane(detailScrollPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        detailPanel.add(addNewDetailButton);
        detailPanel.add(detailListPanel);
        
        add(stringPanel);
        add(detailPanel);
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
            presenter.onMenuNewClicked();
        });
        menu.add(voice);
        
        voice = new JMenuItem("Apri", KeyEvent.VK_R);
        voice.addActionListener(v -> {
            presenter.onMenuOpenClicked();
        });
        menu.add(voice);
        
        voice = new JMenuItem("Salva", KeyEvent.VK_S);
        voice.addActionListener(v -> {
            presenter.onMenuSaveClicked();
        });
        menu.add(voice);
        
        menu.addSeparator();
        
        voice = new JMenuItem("Esporta per Android", KeyEvent.VK_A);
        voice.addActionListener(v -> {
            presenter.onMenuExportToAndroidClicked();
        });
        menu.add(voice);
        
        voice = new JMenuItem("Esporta per Swift", KeyEvent.VK_I);
        voice.addActionListener(v -> {
            presenter.onMenuExportToSwiftClicked();
        });
        menu.add(voice);
        
        menu.addSeparator();
        
        voice = new JMenuItem("Esci", KeyEvent.VK_E);
        voice.addActionListener(v -> {
            presenter.onMenuExitClicked();
        });
        menu.add(voice);
    }
    
    private void setupButtons(){
        addNewStringButton.addActionListener(v -> {
            presenter.onNewStringClicked();
        });
        
        addNewDetailButton.addActionListener(v -> {
            presenter.onNewDetailClicked();
        });
    }

    @Override
    public String onShowInputDialog(String message) {
        return JOptionPane.showInputDialog(message);
    }

    @Override
    public void onStringAdded(StringModel newStringModel) {
        JLabelStringModel s = new JLabelStringModel(newStringModel);
        s.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                presenter.onStringClicked(s.getStringModel());
            }
        });
        stringScrollPanel.add(s);
        stringScrollPanel.validate();
        stringListPanel.validate();        
    }

    @Override
    public String onSelectFileWithFileChooser() {
        int status = fileChooser.showOpenDialog(this);
        return status == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile().getAbsolutePath() : null;
    }

    @Override
    public void onDetailAdded(String pathFile, boolean newRow) {
        JLabelDetailModel d = new JLabelDetailModel(pathFile, presenter);
        
        if(newRow){
            ((GridLayout) detailScrollPanel.getLayout()).setRows(((GridLayout) detailScrollPanel.getLayout()).getRows() + 1);
        }
        
        detailScrollPanel.add(d);
        detailScrollPanel.validate();
        detailListPanel.validate();
    }

}

