import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditor extends JFrame implements ActionListener, ChangeListener, MouseListener {
    JTextArea textArea;
    JScrollBar scrollBar;
    JScrollPane scrollPane;
    JSpinner fontSizeSpinner;
    JLabel fontLabel;
    JColorChooser fontColorChooser;
    JButton colorChooserBtn;
    JButton savedButton;
    JButton undoButton;
    JButton redoButton;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem saveItem;
    JMenuItem loadItem;
    JMenuItem exitItem;
    JFileChooser fileChooser;
    JComboBox fontBox;
    ImageIcon icon;
    ImageIcon saveIcon;
    ImageIcon loadIcon;
    ImageIcon exitIcon;
    ImageIcon fileIcon;
    Originator originator;
    CareTaker careTaker;
    int totalArticle = 0, currentArticle = 0;

    TextEditor(){
        textArea = new JTextArea();
        scrollBar = new JScrollBar();
        scrollPane = new JScrollPane(textArea);
        fontSizeSpinner = new JSpinner();
        fontLabel = new JLabel("Font Size: ");
        colorChooserBtn = new JButton("Font Color");
        String[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox(fontList);
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        saveItem = new JMenuItem("Save file");
        loadItem = new JMenuItem("Load file");
        exitItem = new JMenuItem("Exit");
        icon = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Pictures\\Saved Pictures\\flaticon\\document.png");
        saveIcon = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Pictures\\Saved Pictures\\flaticon\\diskette.png");
        loadIcon = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Pictures\\Saved Pictures\\flaticon\\loading2.png");
        exitIcon = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Pictures\\Saved Pictures\\flaticon\\remove.png");
        fileIcon = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Pictures\\Saved Pictures\\flaticon\\folder.png");
        savedButton = new JButton("save");
        undoButton = new JButton("undo");
        redoButton = new JButton("redo");
        originator = new Originator();
        careTaker = new CareTaker();

        scrollPane.setPreferredSize(new Dimension(450, 390));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        fontSizeSpinner.setPreferredSize(new Dimension(50,25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(this);

        fontLabel.setPreferredSize(new Dimension(70, 25));
        fontLabel.setFont(new Font("MV Boli", Font.PLAIN,11));
        fontLabel.setForeground(Color.white);
        fontLabel.setBackground(Color.pink);
        fontLabel.setOpaque(true);
        fontLabel.setHorizontalAlignment(JLabel.CENTER);

        colorChooserBtn.setFocusable(false);
        colorChooserBtn.setPreferredSize(new Dimension(90, 25));
        colorChooserBtn.addActionListener(this);
        colorChooserBtn.setBackground(Color.pink);
        colorChooserBtn.setFont(new Font("MV Boli", Font.PLAIN,11));
        colorChooserBtn.setForeground(Color.white);

        fontBox.addActionListener(this);
        fontBox.setSelectedItem("MV Boli");
        fontBox.setEditable(true);

        menuBar.add(fileMenu);
        menuBar.setBackground(Color.pink);

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        fileMenu.setFont(new Font("MV Boli", Font.PLAIN,15));
        fileMenu.setIcon(fileIcon);
        fileMenu.setMnemonic('f');

        loadItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);
        loadItem.setMnemonic('l');
        saveItem.setMnemonic('s');
        exitItem.setMnemonic('e');
        loadItem.setIcon(loadIcon);
        saveItem.setIcon(saveIcon);
        exitItem.setIcon(exitIcon);

        textArea.setLineWrap(true);
        textArea.setBackground(new Color(0x123456));
        textArea.setForeground(Color.white);
        textArea.setWrapStyleWord(true);
        textArea.setCaretColor(Color.pink);
        textArea.addMouseListener(this);
        textArea.setFont(new Font("MV Boli", Font.PLAIN,20));

        savedButton.addActionListener(new SavedBtnAction());
        undoButton.addActionListener(new UndoBtnAction());
        redoButton.addActionListener(new RedoBtnAction());

        this.setJMenuBar(menuBar);
        this.add(fontBox);
        this.add(colorChooserBtn);
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(scrollPane);
        this.add(savedButton);
        this.add(undoButton);
        this.add(redoButton);

        this.setIconImage(icon.getImage());
        this.setDefaultCloseOperation(3);
        this.setSize(500, 540);
        this.setTitle("Thanh Tùng Text Editor");
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(0x123456));
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == colorChooserBtn){
            fontColorChooser = new JColorChooser();
            Color color = fontColorChooser.showDialog(null, "Pick a color", Color.pink);
            textArea.setForeground(color);
        }else if (e.getSource() == fontBox){
            textArea.setFont(new Font((String)fontBox.getSelectedItem(), textArea.getFont().getStyle(), textArea.getFont().getSize()));
        }else if(e.getSource() == loadItem){
            fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt");
            fileChooser.setFileFilter(filter);
            int response = fileChooser.showOpenDialog(null);

            if (response == JFileChooser.APPROVE_OPTION){
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;

                try {
                    fileIn = new Scanner(file);
                    if (file.isFile()){
                        while (fileIn.hasNext()){
                            String line = fileIn.nextLine() + "\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } finally {
                    fileIn.close();
                }
            }
        }else if(e.getSource() == saveItem){
            fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int response = fileChooser.showSaveDialog(null);

            if  (response == JFileChooser.APPROVE_OPTION){
                File file;
                PrintWriter fileOut = null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } finally {
                    fileOut.close();
                }
            }
        }else if(e.getSource() == exitItem){
            System.exit(0);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == fontSizeSpinner){
            textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int)fontSizeSpinner.getValue()));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (textArea.getText().equals("Write something...")){
            textArea.setText("");
        }
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//        textArea.setText("");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (textArea.getText().equals("")){
            textArea.setText("Write something...");
        }
    }

    public class SavedBtnAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == savedButton){
                //take the text from textArea
                String article = textArea.getText();
                //saved the text to originator
                originator.set(article);
                //add the text in memento to the arrayList in careTaker
                careTaker.addMemento(originator.storedMemento());
                //increasing the number of totalArticle and currentArticle
                totalArticle++;
                currentArticle++;
                undoButton.setEnabled(true);
            }
        }
    }

    public class UndoBtnAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == undoButton){
                //if the current article is less than 0 then we set unable the undoButton
                if (currentArticle > 0){
                    currentArticle--;
                    //take the article from the saved memento and put it in textArea
                    String article = originator.restoreMemento(careTaker.getMemento(currentArticle));
                    textArea.setText(article);
                    redoButton.setEnabled(true);
                }else {
                    undoButton.setEnabled(false);
                }
            }
        }
    }

    public class RedoBtnAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == redoButton){
                //if the current article is greater than totalArticle then we set unable the redoButton
                if (currentArticle < (totalArticle - 1) ){
                    currentArticle++;
                    //take the article from the saved memento and put it in textArea
                    String article = originator.restoreMemento(careTaker.getMemento(currentArticle));
                    textArea.setText(article);
                    undoButton.setEnabled(true);
                }else {
                    redoButton.setEnabled(false);
                }
            }
        }
    }
}
