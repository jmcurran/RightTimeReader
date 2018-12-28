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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
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
 * @author James
 */
public class EditDialog extends javax.swing.JDialog {

    private Book book;
    private ArrayDeque<editAction> changeLog;
    int currentPage;
    int numNewPages;
    private JTabbedPane tabPane;
    private PagePanel pp;
    private JTextField caption;
    
    private JPanel pageListPanel;
    private JList pageList, list;

    /**
     * Creates new form EditDialg
     *
     * @param parent
     * @param book
     */
    public EditDialog(java.awt.Frame parent, Book book) {
        super(parent, true);
        //this.setUndecorated(true);
        this.setTitle("Edit a book...");
        this.setLocationRelativeTo(parent);
        changeLog = new ArrayDeque<>();
        numNewPages = 0;

        if (book == null) {
            JFileChooser fc = new JFileChooser();
            File theDirectory = new File("D:/Dropbox/ALL-RTR-FILES/JamieTestSoftware/mag/mag");
            fc.setCurrentDirectory(theDirectory);
            FileFilter filter = new FileNameExtensionFilter("RightTimerReader File", "ubk", "ubook");
            fc.setFileFilter(filter);

            int retval = fc.showOpenDialog(this);

            if (retval == JFileChooser.APPROVE_OPTION) {
                this.book = new Book();
                this.book.loadUBK(fc.getSelectedFile());
                initComponents();
                this.pack();
            } else {
                this.setVisible(false);
            }
        } else {
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
        return changeLog.size() > 0;
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
        } else {
            this.setVisible(false);
        }
    }

    private void initComponents() {
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
        BookDetailsPanel panel1 = ((book == null) ? new BookDetailsPanel(this, null) : new BookDetailsPanel(this, book));
        JPanel panel2 = makeContentsPanel();
        tabPane.add("Book Details", panel1);
        tabPane.add("Book Contents", panel2);
        this.add(tabPane);
    }
    
    void insertNewPage(boolean before, int selectedIndex, Path imagePath){
        Book.Page p = book.new Page(book.getNumPages(), imagePath, null);
        book.addPage(p);
 
        ListModel lm = list.getModel();
        ArrayList<Object> data = new ArrayList();

        for (int i = 0; i < lm.getSize(); i++) {
            if(before){
                if (i == selectedIndex) {
                    data.add("New Page " + numNewPages);
                    numNewPages++;
                }

                data.add(lm.getElementAt(i));
            }else{
                data.add(lm.getElementAt(i));
                
                if (i == selectedIndex) {
                    data.add("New Page " + numNewPages);
                    numNewPages++;
                }
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

        pageList = new JList(book.getPages());
        pageList.setDropMode(DropMode.INSERT);
        pageList.setDragEnabled(true);
        pageList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)) {
                    list = (JList) me.getSource();

                    int selectedIndex = list.getSelectedIndex();
                    int listLength = list.getModel().getSize();

                    if (selectedIndex != -1) {
                        JPopupMenu menu = new JPopupMenu();

                        JMenuItem moveUp = new JMenuItem("Move Up");
                        moveUp.addActionListener((ActionEvent ae) -> {
                            ListModel lm = list.getModel();

                            ArrayList<Object> data = new ArrayList();

                            for (int i = 0; i < lm.getSize(); i++) {
                                data.add(lm.getElementAt(i));
                            }

                            Object tmp = data.get(selectedIndex - 1);
                            data.set(selectedIndex - 1, data.get(selectedIndex));
                            data.set(selectedIndex, tmp);

                            Object[] odata = data.toArray(new Object[data.size()]);
                            list.setListData(odata);

                            changeLog.addLast(new editAction(editAction.editType.ORDER,
                                    selectedIndex,
                                    selectedIndex,
                                    selectedIndex - 1));
                        });

                        JMenuItem moveDown = new JMenuItem("Move Down");
                        moveDown.addActionListener((ActionEvent ae) -> {
                            ListModel lm = list.getModel();

                            ArrayList<Object> data = new ArrayList();

                            for (int i = 0; i < lm.getSize(); i++) {
                                data.add(lm.getElementAt(i));
                            }

                            Object tmp = data.get(selectedIndex + 1);
                            data.set(selectedIndex + 1, data.get(selectedIndex));
                            data.set(selectedIndex, tmp);

                            Object[] odata = data.toArray(new Object[data.size()]);
                            list.setListData(odata);

                            changeLog.addLast(new editAction(editAction.editType.ORDER,
                                    selectedIndex,
                                    selectedIndex,
                                    selectedIndex + 1));
                        });

                        JMenuItem removePage = new JMenuItem("Remove Page");
                        removePage.addActionListener((ActionEvent ae) -> {
                            ListModel lm = list.getModel();

                            changeLog.addLast(new editAction(editAction.editType.REMOVE,
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
                        });

                        JMenuItem addPageAbove = new JMenuItem("Add Page Above");
                        addPageAbove.addActionListener((ActionEvent ae) -> {
                            JFileChooser fc = new JFileChooser();
                            FileNameExtensionFilter ff = new FileNameExtensionFilter("Image files (*.bmp;*.gif;*.jpg;*.jpeg;*.png",
                                    "bmp", "gif", "jpg", "jpeg", "png");

                            fc.setFileFilter(ff);
                            fc.setDialogTitle("Choose an image...");

                            int retVal = fc.showOpenDialog(pp);

                            if (retVal == JFileChooser.APPROVE_OPTION) {
                               insertNewPage(false, selectedIndex, fc.getSelectedFile().toPath());
                            }
                        });

                        JMenuItem addPageBelow = new JMenuItem("Add Page Below");
                        addPageBelow.addActionListener((ActionEvent ae) -> {
                            JFileChooser fc = new JFileChooser();
                            FileNameExtensionFilter ff = new FileNameExtensionFilter("Image files (*.bmp;*.gif;*.jpg;*.jpeg;*.png",
                                    "bmp", "gif", "jpg", "jpeg", "png");

                            fc.setFileFilter(ff);
                            fc.setDialogTitle("Choose an image...");

                            int retVal = fc.showOpenDialog(pp);

                            if (retVal == JFileChooser.APPROVE_OPTION) {
                                insertNewPage(true, selectedIndex, fc.getSelectedFile().toPath());
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
                        menu.add(removePage);

                        menu.show(me.getComponent(), me.getX(), me.getY());
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

        caption = new JTextField(book.getCurrentPageCaption());
        caption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Sentence s = new Sentence(caption.getText());

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
        });

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 0.15;
        c.fill = GridBagConstraints.HORIZONTAL;
        pageDetailsPanel.add(caption, c);

        JButton buttonSave = new JButton("Save");
        buttonSave.setEnabled(false);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;
        c.weighty = 0.15;
        c.fill = GridBagConstraints.HORIZONTAL;
        pageDetailsPanel.add(buttonSave, c);

        JButton buttonCancel = new JButton("Cancel");
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

    }
}
