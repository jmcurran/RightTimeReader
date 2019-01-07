/*
 * Copyright (C) 2018  James Curran <james.m.curran@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package righttimereader;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author James
 */
public class RightTimeReader extends JFrame{
    private Book theBook;
    
    // UI items
    private ToolbarPanel toolbarPanel;
    private BookPanel bookPanel;
    private StatusPanel statusPanel;
    
    private JToolBar jToolBar;
    private JButton jButtonExit;
    private JButton jButtonEdit;
    private JButton jButtonNew;
    private JButton jButtonOpen;
    
    
    private JMenuBar jMenuBar;
    private JMenu jMenuFile;
    private JMenuItem jMenuItemEdit;
    private JMenuItem jMenuItemOpen;
    private JMenuItem jMenuItemNew;
    private JMenuItem jMenuItemQuit;
    private JMenu jMenuHelp;
    private JMenuItem jMenuItemAbout;
    
    private JPopupMenu.Separator jSeparator1, jSeparator2;
 
    public RightTimeReader() throws HeadlessException {
        Splash splash = new Splash();
        splash.setVisible(true);
        splash.setLocationRelativeTo(null);
        try{
            for(int i = 0; i < 50; i++){
                Thread.sleep(20);
                splash.progressBar.setValue(i);
            }
        }catch(Exception e){
            
        }
        splash.setVisible(false);
        this.setTitle("RightTimeReader");
        this.setResizable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Icons/rr16x16.png"));
        Image img = icon.getImage();
        this.setIconImage(img);
        
        initPanels();
        initToolBar();
        initMenuBar();
    
        this.setVisible(true);
        this.pack();
        //did you notice that closing the frame did not end the program? Here is how to do it:
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         //odd but true: the statement below centres the frame on screen
        this.setLocationRelativeTo(null);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(800,600);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 600);
    }
    
    
    
    private void initPanels(){
        this.toolbarPanel = new ToolbarPanel();
        this.bookPanel = new BookPanel();
        this.statusPanel = new StatusPanel();
        
        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 4.0/60;
        this.getContentPane().add(toolbarPanel, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 54.0 / 60;
        this.getContentPane().add(bookPanel, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 2.0 / 60;
        this.getContentPane().add(statusPanel, c);
    }
    
    private void initToolBar(){
        jToolBar = new JToolBar();
        jToolBar.setRollover(true);

        jButtonOpen = new JButton();
        jButtonOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/book32x32.png"))); // NOI18N
        jButtonOpen.setToolTipText("Open a book");
        jButtonOpen.setFocusable(false);
        jButtonOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonOpen.addActionListener((java.awt.event.ActionEvent evt) -> {
            openBookActionPerformed(evt);
        });
        jToolBar.add(jButtonOpen);
        
        jButtonEdit = new JButton();
        jButtonEdit.setToolTipText("Edit a book");
        jButtonEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit32x32.png"))); // NOI18N
        jButtonEdit.setFocusable(false);
        jButtonEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonEdit.addActionListener((java.awt.event.ActionEvent evt) -> {
            editBookActionPerformed(evt);
        });
        jToolBar.add(jButtonEdit);
        
        jButtonNew = new JButton();
        jButtonNew.setToolTipText("New a book");
        jButtonNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/new32x32.png"))); // NOI18N
        jButtonNew.setFocusable(false);
        jButtonNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNew.addActionListener((java.awt.event.ActionEvent evt) -> {
            newBookActionPerformed(evt);
        });
        jToolBar.add(jButtonNew);

        jButtonExit = new JButton();
        jButtonExit.setToolTipText("Quit RightTimeReader");
        jButtonExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit32x32.png"))); // NOI18N
        jButtonExit.setFocusable(false);
        jButtonExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExit.addActionListener((java.awt.event.ActionEvent evt) -> {
            exitActionPerformed(evt);
        });
        jToolBar.add(jButtonExit);
        
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.X_AXIS));
        toolbarPanel.add(jToolBar);
        
        
    }
    
    private void initMenuBar(){
        // File Menu
        jMenuFile = new JMenu();
        jMenuFile.setText("File");

        jMenuItemOpen = new JMenuItem();
        jMenuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        jMenuItemOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/book16x16.png"))); // NOI18N
        jMenuItemOpen.setText("Open");
        jMenuItemOpen.addActionListener((java.awt.event.ActionEvent evt) -> {
            openBookActionPerformed(evt);
        });
        jMenuFile.add(jMenuItemOpen);
        
        //Separator
        jSeparator1 = new JPopupMenu.Separator();
        jMenuFile.add(jSeparator1);
        
        jMenuItemEdit = new JMenuItem();
        jMenuItemEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        jMenuItemEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/edit16x16.png"))); // NOI18N
        jMenuItemEdit.setText("Edit");
        jMenuItemEdit.addActionListener((java.awt.event.ActionEvent evt) -> {
            openBookActionPerformed(evt);
        });
        jMenuFile.add(jMenuItemEdit);
        
        jMenuItemNew = new JMenuItem();
        jMenuItemNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        jMenuItemNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/new16x16.png"))); // NOI18N
        jMenuItemNew.setText("New");
        jMenuItemNew.addActionListener((java.awt.event.ActionEvent evt) -> {
            newBookActionPerformed(evt);
        });
        jMenuFile.add(jMenuItemNew);
        
        
        //Separator
        jSeparator2 = new JPopupMenu.Separator();
        jMenuFile.add(jSeparator2);

        jMenuItemQuit = new JMenuItem();
        jMenuItemQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        jMenuItemQuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit16x16.png"))); // NOI18N
        jMenuItemQuit.setText("Exit");
        jMenuItemQuit.addActionListener((java.awt.event.ActionEvent evt) -> {
            exitActionPerformed(evt);
        });
        jMenuFile.add(jMenuItemQuit);

        //Help Menu
        jMenuHelp = new JMenu();
        jMenuHelp.setText("Help");

        jMenuItemAbout = new JMenuItem();
        jMenuItemAbout.setText("About RightTimeReader...");
        jMenuItemAbout.addActionListener((java.awt.event.ActionEvent evt) -> {
            aboutActionPerformed(evt);
        });
        jMenuHelp.add(jMenuItemAbout);

        // Menu bar
        jMenuBar = new JMenuBar();
        jMenuBar.add(jMenuFile);
        jMenuBar.add(jMenuHelp);

        setJMenuBar(jMenuBar);
    }
    
    private Book loadUBK(){
        JFileChooser fc = new JFileChooser();
        File theDirectory = new File("D:/Dropbox/ALL-RTR-FILES/JamieTestSoftware/mag/mag");
        fc.setCurrentDirectory(theDirectory);
        FileFilter filter = new FileNameExtensionFilter("RightTimerReader File", "ubk", "ubook");
        fc.setFileFilter(filter);
        
        int retval = fc.showOpenDialog(this);
        
        if(retval == JFileChooser.APPROVE_OPTION){
            Book b = new Book();
            b.loadUBK(fc.getSelectedFile());
            return b;
        }else{
            return null;
        }
    }
    
    private void aboutActionPerformed(java.awt.event.ActionEvent evt) {   
        AboutDialog dlg = new AboutDialog();
        pack();
        dlg.setLocationRelativeTo(this.getParent());
        dlg.setVisible(true);
    }                                
    
    private void exitActionPerformed(java.awt.event.ActionEvent evt) {   
        System.exit(0);
    }   
    private void editBookActionPerformed(java.awt.event.ActionEvent evt) {            
        EditDialog dlg  = new EditDialog(this, theBook);
        pack();
        dlg.setLocationRelativeTo(this.getParent());
        dlg.setVisible(true);
    }
    private void newBookActionPerformed(java.awt.event.ActionEvent evt) {            
        theBook = new Book();
        
        EditDialog dlg  = new EditDialog(this, theBook);
        pack();
        dlg.setLocationRelativeTo(this.getParent());
        dlg.setVisible(true);
        
        
    }
    private void openBookActionPerformed(java.awt.event.ActionEvent evt) {                                              
        Book b = loadUBK();
        
        if(b != null){
            theBook = b;
            statusPanel.setBook(theBook);
            bookPanel.setBook(theBook);
        }
            
        
    }                                             
    

    public static void main(String[] args){
        JFrame frame = new RightTimeReader();
    }
    
}
