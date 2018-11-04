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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
    private JButton jButtonOpen;
    
    private JMenuBar jMenuBar;
    private JMenu jMenuFile;
    private JMenuItem jMenuItemOpen;
    private JMenuItem jMenuItemQuit;
    private JMenu jMenuHelp;
    private JMenuItem jMenuItemAbout;
    
    private JPopupMenu.Separator jSeparator1;
 
    public RightTimeReader() throws HeadlessException {
        this.setTitle("RightTimeReader");
        this.setResizable(true);
        this.setSize(1200, 600);
        
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
    
    private void initPanels(){
        this.toolbarPanel = new ToolbarPanel();
        this.bookPanel = new BookPanel();
        this.statusPanel = new StatusPanel();
        
        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        this.getContentPane().add(toolbarPanel, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        this.getContentPane().add(bookPanel, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        c.gridx = 0;
        c.gridy = 2;
        this.getContentPane().add(statusPanel, c);
    }
    
    private void initToolBar(){
        jToolBar = new JToolBar();
        jToolBar.setRollover(true);

        jButtonOpen = new JButton();
        jButtonOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/book32x32.png"))); // NOI18N
        jButtonOpen.setFocusable(false);
        jButtonOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonOpen.addActionListener((java.awt.event.ActionEvent evt) -> {
            openBookActionPerformed(evt);
        });
        jToolBar.add(jButtonOpen);

        jButtonExit = new JButton();
        jButtonExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit32x32.png"))); // NOI18N
        jButtonExit.setFocusable(false);
        jButtonExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar.add(jButtonExit);
        
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.X_AXIS));
        toolbarPanel.add(jToolBar);
        
        
    }
    
    private void initMenuBar(){
        // File Menu
        jMenuFile = new JMenu();
        jMenuFile.setText("File");

        jMenuItemOpen = new JMenuItem();
        jMenuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/book16x16.png"))); // NOI18N
        jMenuItemOpen.setText("Open");
        jMenuItemOpen.addActionListener((java.awt.event.ActionEvent evt) -> {
            openBookActionPerformed(evt);
        });
        jMenuFile.add(jMenuItemOpen);
        
        //Separator
        jSeparator1 = new JPopupMenu.Separator();
        jMenuFile.add(jSeparator1);

        jMenuItemQuit = new JMenuItem();
        jMenuItemQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemQuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/exit16x16.png"))); // NOI18N
        jMenuItemQuit.setText("Exit");
        jMenuFile.add(jMenuItemQuit);

        //Help Menu
        jMenuHelp = new JMenu();
        jMenuHelp.setText("Help");

        jMenuItemAbout = new JMenuItem();
        jMenuItemAbout.setText("About RightTimeReader...");
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
