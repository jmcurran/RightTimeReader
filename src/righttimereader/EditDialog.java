/*
 * Copyright (C) 2018 James
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.ZoneId;
import javafx.scene.control.DatePicker;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * @author James
 */
public class EditDialog extends javax.swing.JDialog {    
    private Book book;
    int currentPage;
    private JTabbedPane tabPane;
    private PagePanel pp;
    private JTextField caption;
    
    
    /**
     * Creates new form EditDialg
     * @param parent
     * @param book
     */
    public EditDialog(java.awt.Frame parent, Book book) {
        super(parent, true);
        //this.setUndecorated(true);
        this.setTitle("Edit a book...");
         this.setLocationRelativeTo(parent);
        
        if(book == null){
            JFileChooser fc = new JFileChooser();
            File theDirectory = new File("D:/Dropbox/ALL-RTR-FILES/JamieTestSoftware/mag/mag");
            fc.setCurrentDirectory(theDirectory);
            FileFilter filter = new FileNameExtensionFilter("RightTimerReader File", "ubk", "ubook");
            fc.setFileFilter(filter);

            int retval = fc.showOpenDialog(this);

            if(retval == JFileChooser.APPROVE_OPTION){
                this.book = new Book();
                this.book.loadUBK(fc.getSelectedFile());
                initComponents();
                this.pack();
            }else{
                this.setVisible(false);
            }
        }else{
            this.book = book;
            initComponents();
            this.pack();
        
        }
    }
    
    private void initComponents(){
        /*this.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.3;
        c.weighty = 0.2;
        c.anchor = GridBagConstraints.EAST;
        
        JLabel labelTitle  = new JLabel("Title:");
        this.add(labelTitle, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.7;
        
        JTextField tfTitle = new JTextField(book.getTitle());
        this.add(tfTitle, c);
        
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.3;
        c.weighty = 0.2;
        c.anchor = GridBagConstraints.EAST;
        
        JLabel labelAuthor  = new JLabel("Author:");
        this.add(labelAuthor, c);
        
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.7;
        
        JTextField tfAuthor = new JTextField(book.getAuthor());
        this.add(tfAuthor, c);
        
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.3;
        c.weighty = 0.2;
        
        JLabel labelRevisionDate  = new JLabel("Revision Date:");
        this.add(labelRevisionDate, c);
        
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.7;
        
        //DatePicker dp = new DatePicker(book.getRevisionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        //this.add(dp, c);
        
        c.gridx = 1;
        c.gridy = 3;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.2;
        
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        JButton nextButton = new JButton("Next");
        JButton cancelButton = new JButton("Cancel");
        panelButtons.add(nextButton);
        panelButtons.add(cancelButton);
        
        this.add(panelButtons, c);*/
        
        
        
       tabPane = new JTabbedPane();
       BookDetailsPanel panel1 = new BookDetailsPanel();
       JPanel panel2 = makeContentsPanel();
       tabPane.add("Book Details", panel1);
       tabPane.add("Book Contents", panel2);
       this.add(tabPane);
    }
    
    private BookDetailsPanel makePanel(){
        BookDetailsPanel panel = new BookDetailsPanel();
        
        return panel;
    }
    
    private JPanel makeContentsPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        JPanel pageListPanel = new JPanel();
        pageListPanel.setBorder(BorderFactory.createEtchedBorder());
        pageListPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        JList pageList = new JList(book.getPages());
        pageList.setDropMode(DropMode.INSERT);
        pageList.setDragEnabled(true);
        pageList.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int thePageNumber = pageList.getSelectedIndex();
                if(thePageNumber != -1){
                    book.setCurrentPage(thePageNumber);
                    pp.setPage(book.getCurrentPage());
                    caption.setText(book.getCurrentPageCaption());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(pageList);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
       
        pageListPanel.add(scrollPane, c);
        
        c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.3;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 5, 0, 0);
        
        panel.add(pageListPanel, c);

        JPanel pageDetailsPanel = new JPanel();
        pageDetailsPanel.setBorder(BorderFactory.createEtchedBorder());
        pageDetailsPanel.setLayout(new GridBagLayout());
        
        pp = new PagePanel();
        pp.setPage(book.getCurrentPage());
        c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0.7;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 0);
        pageDetailsPanel.add(pp, c);
        
        
        caption = new JTextField(book.getCurrentPageCaption());
        caption.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                Sentence s = new Sentence(caption.getText());
                
                if(s.getNumberOfWords() <= 2){
                    Object[] options = {"OK", "CANCEL"};
                    int ret = JOptionPane.showOptionDialog(null, 
                            "Warning: Your sentence has very few words in it (delimited by :). If this is what you intended, then click OK, otherwise click Cancel",
                            "Warning", 
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    
                    if(ret == JOptionPane.OK_OPTION){
                        book.getCurrentPage().setCaption(caption.getText());
                        pageDetailsPanel.revalidate();
                        pageDetailsPanel.repaint();
                    }else{
                        caption.setText(book.getCurrentPageCaption());
                    }
                }else{
                    book.getCurrentPage().setCaption(caption.getText());
                    pp.revalidate();
                    pp.repaint();
                }
            }
        });
        
        c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0.3;
        c.fill = GridBagConstraints.HORIZONTAL;
        pageDetailsPanel.add(caption, c);
        
        
        
        
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.7;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;       
        
        panel.add(pageDetailsPanel, c);
        
                
        return panel;
    }
    
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(400,300);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }
}
