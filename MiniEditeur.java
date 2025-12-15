package basedonnes;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class MiniEditeur extends JFrame {

    private JTextPane textPane;
    private JTabbedPane tabs;
    private int fileCounter=1;
    

  
    public MiniEditeur() {
        super("Mini Éditeur de Texte");
        setSize(700, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        tabs = new JTabbedPane();
        add(tabs, BorderLayout.CENTER);

        // premier document
        addNewDocument();

        //  Creation d'un menu 
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(0, 35)); // hauteur du menu

        Font menuFont = new Font("Segoe UI", Font.PLAIN, 16);
        // Fichier
        JMenu menuFichier = new JMenu("Fichier");
        menuFichier.setFont(menuFont);
     // ajouter 
        JMenuItem newItem = new JMenuItem("Nouveau");
        newItem.setFont(menuFont);
        newItem.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)
        );
        newItem.addActionListener(e -> addNewDocument());
        menuFichier.add(newItem);
     // Option Ouvrir
        JMenuItem openItem = new JMenuItem("Ouvrir");
        openItem.setFont(menuFont);
        openItem.setPreferredSize(new Dimension(220, 35));
        openItem.addActionListener(e -> openFile());
        menuFichier.add(openItem);
     // ou encore ctrl O
        openItem.setAccelerator(
        	    KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)
        	);

     // Option Enregistrer 
        JMenuItem saveRtfItem = new JMenuItem("Enregistrer (RTF)");
        saveRtfItem.setFont(menuFont);
        saveRtfItem.setPreferredSize(new Dimension(220, 35));
        saveRtfItem.addActionListener(e -> saveFileRTF());
        menuFichier.add(saveRtfItem);
     // Ctrl S 
        saveRtfItem.setAccelerator(
        	    KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)
        	);
        //Separateur
        menuFichier.addSeparator();
        
      // Option fermer 
        JMenuItem closeItem = new JMenuItem("Fermer le fichier");
        closeItem.setFont(menuFont);
        closeItem.setPreferredSize(new Dimension(220, 35));
        closeItem.addActionListener(e -> System.exit(0));
        menuFichier.add(closeItem);
        // ctrl Q 
        closeItem.setAccelerator(
        	    KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)
        	);
        // Ajouter le menu a la barre 
        menuBar.add(menuFichier);
        setJMenuBar(menuBar);
        // ==== BARRE D’OUTILS ====
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); // empeche le deplacement

        // --- BOUTON GRAS ---
        JButton boldBtn = new JButton("G");
        boldBtn.addActionListener(e -> toggleStyle(StyleConstants.Bold));
        toolBar.add(boldBtn);

        // --- BOUTON ITALIQUE ---
        JButton italicBtn = new JButton("I");
        italicBtn.addActionListener(e -> toggleStyle(StyleConstants.Italic));
        toolBar.add(italicBtn);

        // --- BOUTON SOULIGNE ---
        JButton underlineBtn = new JButton("S");
        underlineBtn.addActionListener(e -> toggleUnderline());
        toolBar.add(underlineBtn);

        // --- BOUTON COULEUR ---
        JButton colorBtn = new JButton("Couleur");
        colorBtn.addActionListener(e -> changeColor());
        toolBar.add(colorBtn);

        toolBar.addSeparator();

        // ==== COMBO TAILLE (REDUIT) ====
        toolBar.add(new JLabel(" Taille : "));

        String[] tailles = {"10", "12", "14", "18", "24", "32","50"};
        JComboBox<String> tailleBox = new JComboBox<>(tailles);

        // REduction visuelle 
        JPanel boxPanel = new JPanel(new BorderLayout());
        tailleBox.setFont(new Font("Arial", Font.PLAIN, 11));
        tailleBox.setPreferredSize(new Dimension(55, 22));  // <-- Taille reduite
        boxPanel.add(tailleBox);
        toolBar.add(boxPanel);
        // listener pour la taille 
        tailleBox.addActionListener(e ->
                changeFontSize(Integer.parseInt((String) tailleBox.getSelectedItem()))
        );

        add(toolBar, BorderLayout.NORTH);
        // add(scrollPane, BorderLayout.CENTER);
        // les polices
        toolBar.addSeparator();
        toolBar.add(new JLabel(" Police : "));

        // Liste des polices du systeme
        String[] polices = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();

        JComboBox<String> policeBox = new JComboBox<>(polices);

        // reduire la taille visuelle
        policeBox.setFont(new Font("Arial", Font.PLAIN, 11));
        policeBox.setPreferredSize(new Dimension(120, 22));
        
        // Ajouter le choix de polices 
        JPanel policePanel = new JPanel(new BorderLayout());
        policePanel.add(policeBox);
        toolBar.add(policePanel);
        
        // Ajouter le sourlinhage 
        JButton highlightBtn = new JButton("HL"); // Highlight
        highlightBtn.addActionListener(e -> highlightColor());
        toolBar.add(highlightBtn);
        
        // ajouter nouveau fichier 
      /*  JButton newFileBtn = new JButton("Nouveau");
        newFileBtn.addActionListener(e -> addNewDocument());
        toolBar.add(newFileBtn);
        
        // button to open a file 
        JButton openBtn = new JButton("Ouvrir");
        openBtn.addActionListener(e -> openFile());
        toolBar.add(openBtn);
        // button to close a file 
        JButton closeBtn = new JButton("Fermer");
        closeBtn.addActionListener(e -> closeCurrentTab());
        toolBar.add(closeBtn);

      */
        // action
        policeBox.addActionListener(e ->
                changeFontFamily((String) policeBox.getSelectedItem())
        );
        
     // ajouter * fichier au meme temps 
        tabs = new JTabbedPane();
        add(tabs, BorderLayout.CENTER);
     // Listener to add a new document 
        tabs.addChangeListener(e -> {
            JScrollPane sc = (JScrollPane) tabs.getSelectedComponent();
            textPane = (JTextPane) sc.getViewport().getView();
        });
        
        // Créer un premier document par défaut
        addNewDocument();
        setVisible(true);

        
    }
    



	// =================== MÉTHODES DE STYLE ===================

    private void toggleStyle(Object styleConstant) {
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("MyStyle", null);

        if (styleConstant == StyleConstants.Bold) {
            StyleConstants.setBold(style, true);
        } else if (styleConstant == StyleConstants.Italic) {
            StyleConstants.setItalic(style, true);
        }

        applyStyleToSelection(style, doc);
    }
      //==========Methode de sourlinhage avec l'option de choisir la coulour de sourhlinhage =========
 
    public void highlightColor() {
        Color chosen = JColorChooser.showDialog(this, "Choisir une couleur de surlignage", Color.YELLOW);
        if (chosen == null) return;

        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        if (start == end) {
            JOptionPane.showMessageDialog(this, "Sélectionnez d'abord du texte.");
            return;
        }

        Highlighter highlighter = textPane.getHighlighter();
        Highlighter.HighlightPainter painter = 
                new DefaultHighlighter.DefaultHighlightPainter(chosen);

        try {
            
            Highlighter.Highlight[] highlights = highlighter.getHighlights();
            
            // Supprimer les surlignages existants dans la zone sélectionnée
            for (Highlighter.Highlight h : highlights) {
                if (h.getStartOffset() >= start && h.getEndOffset() <= end) {
                    highlighter.removeHighlight(h);
                }
            }
            
            // Ajouter le nouveau surlignage
            highlighter.addHighlight(start, end, painter);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        
        textPane.setCaretPosition(end);
    }

    // methodes d'enregitrement 
    private void saveFileRTF() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Enregistrer en RTF");

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = chooser.getSelectedFile();

                // Forcer extension .rtf
                if (!file.getName().toLowerCase().endsWith(".rtf")) {
                    file = new java.io.File(file.getAbsolutePath() + ".rtf");
                }

                // Utiliser RTFEditorKit pour sauvegarder avec styles
                RTFEditorKit kit = new RTFEditorKit();
                FileOutputStream out = new FileOutputStream(file);

                kit.write(out, textPane.getDocument(), 0, textPane.getDocument().getLength());
                out.close();

                JOptionPane.showMessageDialog(this,
                        "Fichier RTF enregistré avec succès !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l’enregistrement RTF",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

      // ======= Methodes de Polices============
    private void changeFontFamily(String family) {
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("FontFamily", null);

        StyleConstants.setFontFamily(style, family);
        applyStyleToSelection(style, doc);
    }

    private void toggleUnderline() {
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("Underline", null);
        StyleConstants.setUnderline(style, true);
        applyStyleToSelection(style, doc);
    }

    private void changeColor() {
        Color color = JColorChooser.showDialog(this, "Choisir une couleur", Color.BLACK);
        if (color != null) {
            StyledDocument doc = textPane.getStyledDocument();
            Style style = textPane.addStyle("Color", null);
            StyleConstants.setForeground(style, color);
            applyStyleToSelection(style, doc);
        }
    }

    private void changeFontSize(int size) {
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("FontSize", null);
        StyleConstants.setFontSize(style, size);
        applyStyleToSelection(style, doc);
    }

    private void applyStyleToSelection(Style style, StyledDocument doc) {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();
        doc.setCharacterAttributes(start, end - start, style, false);
    }
    // Method to add a new doucument
    private void addNewDocument() {
        JTextPane newPane = new JTextPane();
        newPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(newPane);

        String title = "Document " + (tabs.getTabCount() + 1);
        tabs.addTab(title, scrollPane);

        int index = tabs.indexOfComponent(scrollPane);
        tabs.setTabComponentAt(index, new ClosableTabComponent(tabs, title));

        tabs.setSelectedIndex(index);
        textPane = newPane;
    }
    private void closeTab(int index) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment fermer ce fichier ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        tabs.removeTabAt(index);

        // S’il ne reste aucun onglet en créer un nouveau
        if (tabs.getTabCount() == 0) {
            addNewDocument();
        } else {
            JScrollPane sc = (JScrollPane) tabs.getSelectedComponent();
            textPane = (JTextPane) sc.getViewport().getView();
        }
    }

    // Method to open a file 
    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Ouvrir un fichier texte ou RTF");

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();

            try {
                // créer un nouvel onglet
                JTextPane newPane = new JTextPane();
                JScrollPane sc = new JScrollPane(newPane);

                // détecter si c’est du RTF ou du TXT
                String name = file.getName().toLowerCase();

                if (name.endsWith(".rtf")) {
                    RTFEditorKit kit = new RTFEditorKit();
                    newPane.setEditorKit(kit);
                    kit.read(new java.io.FileInputStream(file), newPane.getDocument(), 0);
                } else {
                    newPane.read(new java.io.FileReader(file), null);
                }

                // ajouter l’onglet
                tabs.addTab(file.getName(), sc);
                tabs.setSelectedComponent(sc);

                // mettre ce textPane comme courant
                textPane = newPane;

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture du fichier",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // Method to close a file 
    private void closeCurrentTab() {
        int index = tabs.getSelectedIndex();
        
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Aucun document à fermer.");
            return;
        }

        // Confirmation
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment fermer ce fichier ?",
                "Confirmer la fermeture",
                JOptionPane.YES_NO_OPTION
        );

        if(confirm!=JOptionPane.YES_OPTION) {
        	return;
        }
        // --- CAS 1 : un seul onglet ---
        if(tabs.getTabCount()==1) {
        	tabs.removeTabAt(0);
        	addNewDocument();// add an empty docuemnt 
        	return ;
        }
        // -----Cas2: Many Windows ---
        tabs.removeTabAt(index);
        // Finnaly Mettre a jour textPane actif 
        JScrollPane sc = (JScrollPane) tabs.getSelectedComponent();
        textPane=(JTextPane) sc.getViewport().getView();
    }
    //==========Class Close =========
    class ClosableTabComponent extends JPanel {

        public ClosableTabComponent(JTabbedPane pane, String title) {
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            JLabel label = new JLabel(title);
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            add(label);

            JButton closeButton = new JButton("X");
            closeButton.setFont(new Font("Arial", Font.BOLD, 12));
            closeButton.setFocusable(false);
            closeButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            closeButton.setContentAreaFilled(false);
            closeButton.setOpaque(false);
            closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            closeButton.addActionListener(e -> {
                int index = pane.indexOfTabComponent(this);
                if (index != -1) {
                    closeTab(index);
                }
            });

            add(closeButton);
        }
    }


    // =================== MAIN ===================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MiniEditeur::new);
    }
}
