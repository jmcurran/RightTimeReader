/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package righttimereader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 *
 * @author James
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
    }
    
    String title;
    String author;
    Date revisionDate;
    int numPages;
    ArrayList<Page> pages;
    int currentPage;

    public Book() {
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
            
         }catch(IOException e){
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    
    
}
