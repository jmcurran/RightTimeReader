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

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 *
 * @author James Curran <james.m.curran@gmail.com>
 */
public class Book {
    public class Page{
        int number;
        String caption;
        File imageFile;
        BufferedImage image;

        public Page(int number, Path imagePath, String caption) {
            this.number = number;
            this.caption = caption;
            this.imageFile = imagePath.toFile();
          
            try {
                this.image = ImageIO.read(imageFile);
            } catch (IOException ex) {
                Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void setCaption(String caption){
            this.caption = caption;
        }

        @Override
        public String toString() {
            return "Page " + number;
        }
        
        
    }
    
    private boolean isLoaded;
    private String title;
    private String author;
    private Date revisionDate;
    private int numPages;
    private ArrayList<Page> pages;
    private int currentPage;
    
    private final PropertyChangeSupport mPcs =
        new PropertyChangeSupport(this);

    public Book() {
        isLoaded = false;
        title = "";
        author = "";
        revisionDate = new Date();
        numPages = 0;
        pages = null;
        currentPage = 0;
    }
    
    public void addPage(Page p){
        if(pages == null){
            pages = new ArrayList<Page>();
        }
        pages.add(p);
        numPages++;
    }
    
    public Page[] getPages(){
        if(pages == null || pages.size() == 0)
            return null;
        
        return pages.toArray(new Page[pages.size()]);
    }
    
    public boolean getIsLoaded(){
        return isLoaded;
    }
    
    public void setIsLoaded(boolean state){
        boolean oldState = isLoaded;
        boolean newState = state;
        this.isLoaded = state;
        mPcs.firePropertyChange("isLoaded", oldState, newState);
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getAuthor(){
        return author;
    }
    
    public Date getRevisionDate(){
        return revisionDate;
    }
    
    public int getNumPages(){
        return numPages;
    }
    
    public int getCurrentPageNumber(){
        return currentPage;
    }
    
    public Page getCurrentPage(){
        if(pages == null || pages.size() == 0)
            return null;
        
        return pages.get(currentPage);
    }
    
    public void reorderPages(ArrayList<Integer> order){
        ArrayList<Page> newPages = new ArrayList<>();
        
        for(int i = 0; i < order.size(); i++){
            Page p = pages.get(order.get(i));
            p.number = i + 1;
            newPages.add(p);
        }
        
        pages = newPages;
        
    }
    
    public void setCurrentPage(int newPage){
        if(newPage < numPages && newPage >= 0){
            int oldCurrentPage = currentPage;
            currentPage = newPage;
            mPcs.firePropertyChange("currentPage", oldCurrentPage, newPage);
        }
    }
    
    public void setCurrentPage(String strPage){
        Pattern p = Pattern.compile("^Page (?<pagenum>[1-9]+[0-9]*)$");
        Matcher m = p.matcher(strPage);
        
        if(m.matches()){
            int newPage = Integer.parseInt(m.group("pagenum")) - 1;
            setCurrentPage(newPage);
        }
    }
    
    private String readAuthor(List<String> bookLines){
        Pattern p = Pattern.compile("^#AUTHOR *(.*)$");
        
        for(String line : bookLines){
            Matcher m = p.matcher(line);
            if(m.matches()){
                return m.group(1);
            }
        }
        
        return "None";
    }
    
    public BufferedImage getCurrentPagePicture(){
        return pages.get(currentPage).image;
    }
    
    public String getCurrentPageCaption(){
        if(pages == null || pages.size() == 0)
            return null;
        
        return pages.get(currentPage).caption;
    }
    
    public void nextPage(){
        currentPage = currentPage < numPages - 1 ? currentPage + 1 : 0;
    }
    
    public void prevPage(){
        currentPage = currentPage > 0 ? currentPage - 1 : 0;
    }
    
    private Date readRevDate(List<String> bookLines){
        Pattern p = Pattern.compile("^#REVDATE *(.*)$");
        
        for(String line : bookLines){
            Matcher m = p.matcher(line);
            if(m.matches()){
                String theDate = m.group(1);
                try {
                    return new SimpleDateFormat("dd/MM/yyy").parse(theDate);
                } catch (ParseException ex) {
                    Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return new Date();
    }
    
    private String readTitle(List<String> bookLines){
        Pattern p = Pattern.compile("^#TITLE *(.*)$");
        
        for(String line : bookLines){
            Matcher m = p.matcher(line);
            if(m.matches()){
                return m.group(1);
            }
        }
        
        return "None";
    }
    
    private ArrayList<Page> readPages(List<String> bookLines, File bookFileName){
        ArrayList<Page> thePages = new ArrayList<>();
        
        // Firstly find the page line numbers
        
        Pattern p = Pattern.compile("^#PAGE *([0-9]+)$");
        
        int lineNo = 1;
        ArrayList<Integer> pageLines = new ArrayList();
        for(String line : bookLines){
            Matcher m = p.matcher(line);
            if(m.matches()){
                pageLines.add(lineNo);
            }
            lineNo++;
        }
        
        // 
        
        Pattern pagePat = Pattern.compile("^#PAGE *([0-9]+)$");
        Pattern imagePat = Pattern.compile("^#IMAGE *(.*)$");
        Pattern captionPat = Pattern.compile("^#CAPTION *(.*)$");
        
        Path bookPath = bookFileName.getParentFile().toPath();
        
        for(Integer lineNum : pageLines){
            int pageNum = 0;
            Path imagePath = null;
            String caption = "";
            
            try{
                Matcher m = pagePat.matcher(bookLines.get(lineNum - 1));
          
                if(m.matches()){
                    pageNum = Integer.parseInt(m.group(1));
                }else{
                    throw new BookReadingException(String.format("%s is not a valid page number", m.group(0)));
                }
                
                m = imagePat.matcher(bookLines.get(lineNum));
                
                if(m.matches()){
                    imagePath = Paths.get(bookPath.toString(), m.group(1));
                }else{
                    throw new BookReadingException(String.format("%s is not a valid image name", m.group(0)));
                }
                
                m = captionPat.matcher(bookLines.get(lineNum + 1));
                
                if(m.matches()){
                    caption = m.group(1);
                }else{
                    throw new BookReadingException(String.format("%s is not a valid image name", m.group(0)));
                }
                
                thePages.add(new Page(pageNum, imagePath, caption));
            }catch(BookReadingException ex){
                Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
        
        return thePages;
    }
    
    public void loadUBK(File bookFileName){
        Path bookPath = bookFileName.toPath();
        
        try{
            List<String> lines = Files.readAllLines(bookPath);
            
            title = readTitle(lines);
            author = readAuthor(lines);
            revisionDate = readRevDate(lines);
            pages = readPages(lines, bookFileName);
            numPages = pages.size();
            currentPage = 0;
            
            setIsLoaded(true);
            
         }catch(IOException e){
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    public void writeUBK(File bookFileName){
        BufferedWriter bw = null;
        
        try{
            ArrayList<String> lines = new ArrayList<>();
            
            lines.add(String.format("#TITLE %s", this.title));
            lines.add(String.format("#AUTHOR %s", this.author));
            
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            lines.add(String.format("#REVDATE %s", dateFormat.format(new Date())));
            
            lines.add(String.format("#PAGES %d", this.numPages));
            
            Path bookRoot = bookFileName.getParentFile().toPath();
            
            for(Page p : pages){
                lines.add(String.format("#PAGE %d", p.number));
                
                Path relativeImagePath = bookRoot.relativize(p.imageFile.toPath());
                
                lines.add(String.format("#IMAGE %s", relativeImagePath.toString()));
                if(p.caption != null){
                    lines.add(String.format("#CAPTION %s", p.caption));
                }else{
                    lines.add(String.format("#CAPTION %s", ""));
                }
            }
            
            FileWriter fw = new FileWriter(bookFileName);
            bw = new BufferedWriter(fw);
            
            for(String line : lines){
                bw.write(line);
                bw.newLine();
            }            
        }catch(IOException e){
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, e);
        }finally
	{ 
	   try{
	      if(bw!=null)
		 bw.close();
	   }catch(Exception ex){
	       System.out.println("Error in closing the BufferedWriter"+ex);
	    }
	}
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        mPcs.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        mPcs.removePropertyChangeListener(listener);
    }   
    
}
