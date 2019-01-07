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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author James Curran <james.m.curran@gmail.com>
 */
public class EditDialog extends javax.swing.JDialog {

    private Book book;
    private ChangeLog changeLog;
    int currentPage;
    private JTabbedPane tabPane;
    private PagePanel pp;
    private JTextField caption;
    
    private JPanel pageListPanel;
    private JList pageList, list;
    private File currentWorkingDirectory;
    private JButton buttonSave;
    private JButton buttonCancel;
    private BookDetailsPanel detailsPanel;
    private JPanel contentsPanel;

    /**
     * Creates new form EditDialg
     *
     * @param parent parent frame
     * @param book the book that is being edited. 
     */
    public EditDialog(java.awt.Frame parent, Book book) {
        super(parent, true);
        //this.setUndecorated(true);
        this.setTitle("Edit a book...");
        this.setLocationRelativeTo(parent);
        changeLog = new ChangeLog();
        changeLog.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            if((Integer)pce.getNewValue() > 0){
                if(contentsPanel != null){
                    buttonSave.setEnabled(true);
                }else{
                    buttonSave.setEnabled(false);
                }
            }
        });
 
        if (book == null) {
            currentWorkingDirectory = new File("D:/Dropbox/ALL-RTR-FILES/JamieTestSoftware/mag/mag");
            JFileChooser fc = new JFileChooser(currentWorkingDirectory);
            FileFilter filter = new FileNameExtensionFilter("RightTimerReader File", "ubk", "ubook");
            fc.setFileFilter(filter);

            int retval = fc.showOpenDialog(this);

            if (retval == JFileChooser.APPROVE_OPTION) {
                this.book = new Book();
                this.book.loadUBK(fc.getSelectedFile());
                initComponents();
                this.pack();
            } else {
                this.dispose();
                this.setVisible(false);
            }
        } else {
            currentWorkingDirectory = new File("D:/Dropbox/ALL-RTR-FILES/JamieTestSoftware/mag/mag");
            this.book = book;
            initComponents();
            this.pack();

        }
    }

    /**
     *
     * @return
     */
    public boolean isChanged() {
        return changeLog.getSize() > 0;
    }

    /**
     *
     */
    public void close() {
        if (isChanged()) {
            int ret = JOptionPane.showConfirmDialog(this,
                    "This book contains unsaved changes.\n"
                    + "Would you like to save them?",
                    "Unsaved changes",
                    JOptionPane.YES_NO_OPTION);
            if (ret == JOptionPane.YES_OPTION) {
                saveChanges();
            }
            this.dispose();
        } else {
            this.dispose();
        }
    }

    private void initComponents() {
        tabPane = new JTabbedPane();
        detailsPanel = ((book == null) ? new BookDetailsPanel(this, null) : new BookDetailsPanel(this, book));
        contentsPanel = makeContentsPanel();
        tabPane.add("Book Details", detailsPanel);
        tabPane.add("Book Contents", contentsPanel);
        this.add(tabPane);
    }
    
    void addPages(){
        JFileChooser fc = new JFileChooser(currentWorkingDirectory);
        fc.setMultiSelectionEnabled(true);
        FileNameExtensionFilter ff = new FileNameExtensionFilter("Image files (*.bmp;*.gif;*.jpg;*.jpeg;*.png",
                                        "bmp", "gif", "jpg", "jpeg", "png");

        fc.setFileFilter(ff);
        fc.setDialogTitle("Choose images...");

        int retVal = fc.showOpenDialog(pp);
        
        if(retVal == JFileChooser.APPROVE_OPTION){
            File[] files = fc.getSelectedFiles();
            int pageNumber = 1;
            
            for(File imgFile : files){
                Book.Page p  = book.new Page(pageNumber++, imgFile.toPath(), null);
                book.addPage(p);
                changeLog.addEvent(new EditAction(EditAction.EditType.ADD, p.number));
            }
        }
        
        pageList.setListData(book.getPages());
        
        
        
    }
    
    void insertNewPage(boolean below, int selectedIndex, Path imagePath){
        Book.Page p = book.new Page(book.getNumPages(), imagePath, null);
        book.addPage(p);
 
        ListModel lm = list.getModel();
        ArrayList<Object> data = new ArrayList();

        for (int i = 0; i < lm.getSize(); i++) {
            if(below){
                data.add(lm.getElementAt(i));
                
                if (i == selectedIndex) {
                    data.add("Page " + book.getNumPages());
                }
            }else{
                if (i == selectedIndex) {
                    data.add("Page " + book.getNumPages());
                }

                data.add(lm.getElementAt(i));
            }
        }

        Object[] odata = data.toArray(new Object[data.size()]);
        list.setListData(odata);
        
        changeLog.addEvent(new EditAction(EditAction.EditType.ADD, 
                                         selectedIndex));
    }
    
    void movePage(boolean down, int selectedIndex) {
        ListModel lm = list.getModel();

        ArrayList<Object> data = new ArrayList();

        for (int i = 0; i < lm.getSize(); i++) {
            data.add(lm.getElementAt(i));
        }
        
        int move = 0;
        if(down){
            move = +1;
        }else{
            move = -1;
        }

        Object tmp = data.get(selectedIndex + move);
        data.set(selectedIndex + move, data.get(selectedIndex));
        data.set(selectedIndex, tmp);

        Object[] odata = data.toArray(new Object[data.size()]);
        list.setListData(odata);

        changeLog.addEvent(new EditAction(EditAction.EditType.ORDER,
                selectedIndex,
                selectedIndex,
                selectedIndex + move));
    }
    
    public ArrayList<Integer> getPageOrder() throws PageNumberException{
        ListModel lm  = pageList.getModel();
        ArrayList<Integer> order = new ArrayList<>();

        for(int i = 0; i < lm.getSize(); i++){
            String strPage = (String)lm.getElementAt(i).toString();
            Pattern p = Pattern.compile("^Page (?<pagenum>[1-9]+[0-9]*)$");
            Matcher m = p.matcher(strPage);

            if(m.matches()){
                order.add(Integer.parseInt(m.group("pagenum")) - 1);
            }else{
                throw new PageNumberException(strPage);
            }
        }
        
        return order;
    }
    
    void removePage(int selectedIndex) {
        ListModel lm = list.getModel();

        changeLog.addEvent(new EditAction(EditAction.EditType.REMOVE,
                selectedIndex,
                lm.getElementAt(selectedIndex)));

        ArrayList<Object> data = new ArrayList();

        for (int i = 0; i < lm.getSize(); i++) {
            if (i != selectedIndex) {
                data.add(lm.getElementAt(i));
            }
        }

        Object[] odata = data.toArray(new Object[data.size()]);
        list.setListData(odata);
    }

    /**
     *
     * @return
     */
    public JTabbedPane getTabPane() {
        return tabPane;
    }

    private BookDetailsPanel makePanel() {
        BookDetailsPanel panel = ((book == null) ? new BookDetailsPanel(this, null) : new BookDetailsPanel(this, book));

        return panel;
    }

    private JPanel makeContentsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        pageListPanel = new JPanel();
        pageListPanel.setBorder(BorderFactory.createEtchedBorder());
        pageListPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        if(book.getPages() !=  null){
            pageList = new JList(book.getPages());
        }else{
            pageList = new JList();
        }
        pageList.setDropMode(DropMode.INSERT);
        pageList.setDragEnabled(true);
        pageList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)) {
                    list = (JList) me.getSource();

                    int selectedIndex = list.getSelectedIndex();
                    int listLength = list.getModel().getSize();
                    
                    if(listLength == 0){
                        JPopupMenu menu = new JPopupMenu();

                        JMenuItem miAddPages = new JMenuItem("Add Pages");
                        miAddPages.addActionListener((ActionEvent ae) -> {
                            addPages();
                        });
                        menu.add(miAddPages);
                        menu.show(me.getComponent(), me.getX(), me.getY());
                    }else{
                        if (selectedIndex != -1) {
                            JPopupMenu menu = new JPopupMenu();

                            JMenuItem moveUp = new JMenuItem("Move Up");
                            moveUp.addActionListener((ActionEvent ae) -> {
                                movePage(false, selectedIndex);
                            });

                            JMenuItem moveDown = new JMenuItem("Move Down");
                            moveDown.addActionListener((ActionEvent ae) -> {
                                movePage(true, selectedIndex);
                            });

                            JMenuItem miRemovePage = new JMenuItem("Remove Page");
                            miRemovePage.addActionListener((ActionEvent ae) -> {
                                removePage(selectedIndex);
                            });

                            JMenuItem addPageAbove = new JMenuItem("Add Page Above");
                            addPageAbove.addActionListener((ActionEvent ae) -> {
                                JFileChooser fc = new JFileChooser(currentWorkingDirectory);
                                FileNameExtensionFilter ff = new FileNameExtensionFilter("Image files (*.bmp;*.gif;*.jpg;*.jpeg;*.png",
                                        "bmp", "gif", "jpg", "jpeg", "png");

                                fc.setFileFilter(ff);
                                fc.setDialogTitle("Choose an image...");

                                int retVal = fc.showOpenDialog(pp);

                                if (retVal == JFileChooser.APPROVE_OPTION) {
                                   insertNewPage(false, selectedIndex, fc.getSelectedFile().toPath());
                                   pageList.setSelectedIndex(selectedIndex - 1);
                                   pageList.ensureIndexIsVisible(selectedIndex - 1);
                                }
                            });

                            JMenuItem addPageBelow = new JMenuItem("Add Page Below");
                            addPageBelow.addActionListener((ActionEvent ae) -> {
                                JFileChooser fc = new JFileChooser(currentWorkingDirectory);
                                FileNameExtensionFilter ff = new FileNameExtensionFilter("Image files (*.bmp;*.gif;*.jpg;*.jpeg;*.png",
                                        "bmp", "gif", "jpg", "jpeg", "png");

                                fc.setFileFilter(ff);
                                fc.setDialogTitle("Choose an image...");

                                int retVal = fc.showOpenDialog(pp);

                                if (retVal == JFileChooser.APPROVE_OPTION) {
                                    insertNewPage(true, selectedIndex, fc.getSelectedFile().toPath());
                                    pageList.setSelectedIndex(selectedIndex + 1);
                                    pageList.ensureIndexIsVisible(selectedIndex + 1);
                                }
                            });

                            if (selectedIndex != 0) {
                                menu.add(moveUp);
                            }

                            if (selectedIndex < (listLength - 1)) {
                                menu.add(moveDown);
                            }

                            menu.add(addPageAbove);
                            menu.add(addPageBelow);
                            menu.add(miRemovePage);

                            menu.show(me.getComponent(), me.getX(), me.getY());
                        }
                    }
                }
            }
        });
        
        pageList.addListSelectionListener((ListSelectionEvent lse) -> {
            int selectedIndex = pageList.getSelectedIndex();
            if (selectedIndex != -1) {
                String pageNo = pageList.getModel().getElementAt(selectedIndex).toString();
                book.setCurrentPage(pageNo);
                pp.setPage(book.getCurrentPage());
                caption.setText(book.getCurrentPageCaption());
            }
        });
        JScrollPane scrollPane = new JScrollPane(pageList);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
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

        ////////////////////////////////////////////////
        //  Page Details                              //
        ////////////////////////////////////////////////
        JPanel pageDetailsPanel = new JPanel();
        pageDetailsPanel.setBorder(BorderFactory.createEtchedBorder());
        pageDetailsPanel.setLayout(new GridBagLayout());

        pp = new PagePanel();
        pp.setPage(book.getCurrentPage());
        pp.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)) {
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem menuItemChangeImage = new JMenuItem("Change Image");

                    menuItemChangeImage.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            JFileChooser fc = new JFileChooser();
                            FileNameExtensionFilter ff = new FileNameExtensionFilter("Image files (*.bmp;*.gif;*.jpg;*.jpeg;*.png",
                                    "bmp", "gif", "jpg", "jpeg", "png");

                            fc.setFileFilter(ff);
                            fc.setDialogTitle("Choose a different image...");

                            int ret = fc.showOpenDialog(pp);

                        }
                    });

                    popup.add(menuItemChangeImage);
                    popup.show(pp, me.getX(), me.getY());
                }
            }

        });

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 0.7;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 0);
        pageDetailsPanel.add(pp, c);

        String strCaption = book.getCurrentPageCaption();
        
        if(strCaption != null){
            caption = new JTextField(strCaption);
        }else{
            caption = new JTextField();
        }
        caption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                updateCaption();
            }
        });
        caption.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent fe) {
            }

            @Override
            public void focusLost(FocusEvent fe) {
                updateCaption();
            }
        });

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 0.15;
        c.fill = GridBagConstraints.HORIZONTAL;
        pageDetailsPanel.add(caption, c);

        buttonSave = new JButton("Save");
        buttonSave.setEnabled(false);
        
        buttonSave.addActionListener((ActionEvent ae) -> {
            saveChanges();
        });

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;
        c.weighty = 0.15;
        c.fill = GridBagConstraints.HORIZONTAL;
        pageDetailsPanel.add(buttonSave, c);

        buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                close();
            }
        });

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.5;
        c.weighty = 0.15;
        c.fill = GridBagConstraints.HORIZONTAL;
        pageDetailsPanel.add(buttonCancel, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.7;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        panel.add(pageDetailsPanel, c);

        return panel;
    }

    /**
     *
     * @return
     */
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(400, 300);
    }

    /**
     *
     * @return
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 300);
    }

    /**
     *
     */
    public void saveChanges() {
        JFileChooser fc = new JFileChooser(currentWorkingDirectory);
        FileNameExtensionFilter ff = new FileNameExtensionFilter("RightTimeReader Book files (*.ubk)", "ubk");

        fc.setFileFilter(ff);
        fc.setDialogTitle("Save a book...");

        int retVal = fc.showSaveDialog(pp);

        if (retVal == JFileChooser.APPROVE_OPTION) {
            boolean saveFile = true;
            File fname = fc.getSelectedFile();
            
            if(!fname.toString().endsWith(".ubk")){
                fname = new File(fname.toString() + ".ubk");
            }

            if (fc.getSelectedFile().exists()) {
                int ret = JOptionPane.showConfirmDialog(this,
                        "This file already exists.\n"
                        + "Would you like to overwrite it?",
                        "File already exists",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (ret == JOptionPane.NO_OPTION) {
                    saveFile = false;
                }
            }
            if (saveFile) {
                try{
                    book.reorderPages(getPageOrder());
                }catch(PageNumberException pne){
                    Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, pne);
                }
                book.writeUBK(fc.getSelectedFile());
                this.dispose();
            }
        }
    }
    
    public void updateCaption(){
        Sentence s = new Sentence(caption.getText());
                
        if(s.containsSpacesNoColons()){
            int ret = JOptionPane.showConfirmDialog(null,
                "Your sentence contains whitespace but no word delimiting colons.\n"
                + "Would you like to change the whitespace into colons?",
                "No words found",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ret == JOptionPane.YES_OPTION) {
                s.rebuildSentence();
                caption.setText(s.toString());
            }
        }

        if (s.getNumberOfWords() <= 2) {
            Object[] options = {"OK", "CANCEL"};
            int ret = JOptionPane.showOptionDialog(null,
                    "Warning: Your sentence has very few words in it (delimited by :). If this is what you intended, then click OK, otherwise click Cancel",
                    "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);

            if (ret == JOptionPane.OK_OPTION) {
                book.getCurrentPage().setCaption(caption.getText());
                pp.setSentence(s);
                pp.revalidate();
                pp.repaint();
            } else {
                caption.setText(book.getCurrentPageCaption());
            }
        } else {
            book.getCurrentPage().setCaption(caption.getText());
            pp.setSentence(s);
            pp.revalidate();
            pp.repaint();
        }
    }
}

