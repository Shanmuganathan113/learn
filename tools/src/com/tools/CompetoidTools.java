package com.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;



/*
 * create thumbnails
 * create DTP pics
 * compress pics
 */
public class CompetoidTools {

   private Frame mainFrame;
   private JLabel headerLabel;
   private Label statusLabel;
   private Panel controlPanel;
   
   private String activeFolder;
   private String compressedFolder;
   private Integer compressionType = 0;
   
   public CompetoidTools(){
      prepareGUI();
   }

   private void prepareGUI(){
      mainFrame = new Frame("Competoid-Learn and win");
      mainFrame.setSize(700,600);
      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      headerLabel = new JLabel();
      statusLabel = new Label();        
      statusLabel.setAlignment(Label.CENTER);
      statusLabel.setSize(350,100);

      controlPanel = new Panel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);  
   }

   private void showTextFieldDemo(){
      headerLabel.setText("<html><h1><center> COMPETOID - Picture Compressor</center> </h1></html> ");
      headerLabel.setHorizontalAlignment(SwingConstants.CENTER); 

      JLabel  namelabel= new JLabel("Folder: ");
      JLabel lineBreak = new JLabel("<html><br/><br/><br/><br/><br/><br></html>", SwingConstants.LEFT);
      JLabel  keySlNo= new JLabel("Sl No: ", Label.LEFT);
      namelabel.setHorizontalAlignment(SwingConstants.LEFT);
      
      final TextField path = new TextField(70);
      final TextField serialNo = new TextField(5);
      JLabel  exampleFolder= new JLabel("<html>Example: D:\\Competitions_dev\\Testing\\. This is parent folder should end with \\ &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
      		+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
      		+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; </html>");
      
      Button formPackage = new Button("Form package");
      Button createPackage = new Button("Create package");
      Button compressPics = new Button("Normal Compression ");
      Button createThumbNails = new Button("Create Thumbnails ");
      
      createPackage.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
        	 File[] files = new File(path.getText()).listFiles();
        	 for (File file : files) {
        		 activeFolder = path.getText() + file.getName();
	        	 compressionType = 3;
	        	 if(createDirectory()){
	        		 statusLabel.setText("Folder has been created successfully");
	        		 parsePictures();
	        	 }	 
	        	 else
	        		 statusLabel.setText("Folder creation failed"); 
        	 }
         	}
      	}); 
      
      formPackage.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
         	 File[] files = new File(path.getText()).listFiles();
         	 String slNo;
         	 int i = 0;
         	 Integer x = 0 ;
         	 
         	 for (File file : files) {
         		 activeFolder = path.getText() + file.getName();
         		 x = Integer.parseInt(serialNo.getText())+i++;
         		 
         		 slNo = "00000"+(x).toString();
         		 System.out.println(" file name  "+slNo.substring(slNo.length()-5, slNo.length()));
 	        	    try {
 	        	    	   Path src = Paths.get("D:\\Competitions_dev\\Maintenance\\Tools\\formats\\General Formats\\text.txt"); 
 	 	        	       Path dest = Paths.get(activeFolder+"//"+file.getName()+".txt"); 
 	 	        	       Files.copy(src, dest);
 	 	        	       
 	 	        	       Path src1 = Paths.get("D:\\Competitions_dev\\Maintenance\\Tools\\formats\\General Formats\\Common-QA -4-AlpbtsV5.xlsm"); 
	 	        	       Path dest1 = Paths.get(activeFolder+"//"+file.getName()+slNo.substring(slNo.length()-5, slNo.length())+".xlsm");
	 	        	       
	 	        	       System.out.println("destination "+dest1);
	 	        	       Files.copy(src1, dest1);
	 	        	       
 	        	    }catch(Exception e1){
 	        	    	e1.printStackTrace();
 	        	    	statusLabel.setText("Error in copying" + e1.getMessage());
 	        	    }
         	 }
		}
       	}); 
      
      compressPics.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
         	activeFolder = path.getText();
         	compressionType = 1;
         	 if(createDirectory()){
         		 statusLabel.setText("Folder has been created successfully");
         		 parsePictures();
         	 }	 
         	 else
         		 statusLabel.setText("Folder creation failed");
          } 
       }); 
      
      createThumbNails.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          	 activeFolder = path.getText();
          	 compressionType = 2;
          	 if(createDirectory()){
          		 statusLabel.setText("Folder has been created successfully");
          		 parsePictures();
          	 }	 
          	 else
          		 statusLabel.setText("Folder creation failed");
           } 
        }); 
      
      controlPanel.add(keySlNo);
      controlPanel.add(serialNo);
      controlPanel.add(lineBreak);
      
      controlPanel.add(namelabel);
      controlPanel.add(path);
      controlPanel.add(lineBreak);
      
      controlPanel.add(exampleFolder);
      controlPanel.add(lineBreak);
      
      controlPanel.add(formPackage);
      controlPanel.add(createPackage);
      controlPanel.add(compressPics);
      controlPanel.add(createThumbNails);
      controlPanel.revalidate();
      //mainFrame.setVisible(true);  
   }
   
   public void parsePictures() {
		try {
			File folder = new File(activeFolder);
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				if (file.isFile() && (file.getName().contains("jpg") || file.getName().contains("JPG"))) {
					this.handleFileUpload(file.getName());
					statusLabel.setText("Compressing >> " + file.getName());
				} else if(file.isFile() && compressionType == 3){
					Files.copy(Paths.get(activeFolder + "\\" + file.getName()),
					Paths.get(compressedFolder + "//" + file.getName()));
				}
			} 
		} catch (Exception e) {
		}
	}
   
   public String handleFileUpload(String picName) {
       try {
       		picName = picName.replace(".JPG", ".jpg");
				File file1 = new File(activeFolder+"\\"+picName);
				BufferedImage img1 = ImageIO.read(file1);
				if(compressionType == 1){
					ImageIO.write(img1, "jpg", new File(compressedFolder+"//"+picName));
				}
				else if(compressionType == 2){ // thumbnail image of pixel 150 X 150
					Mode exact=Mode.FIT_EXACT;
					BufferedImage scaledImage= Scalr.resize(img1,exact,150,150);
					ImageIO.write(scaledImage, "jpg", new File(compressedFolder+"//"+picName));
					}
				else{
				Mode exact=Mode.FIT_EXACT;
				BufferedImage scaledImage= Scalr.resize(img1,exact,1000,1300);
				ImageIO.write(scaledImage, "jpg", new File(compressedFolder+"//"+picName));
				}
       } catch (Exception e) {
           e.printStackTrace();
       }
		return null;
	}
   
   
   private boolean createDirectory(){
 	  try {
 		  		String[] folders = activeFolder.split("\\\\");
 		  		File dir = null;
 		  		String newFolder="";
 		  		if(compressionType == 1)
 		  			newFolder = activeFolder+"\\"+folders[folders.length-1]+"-compressed";	
				else if(compressionType == 2)
					newFolder = activeFolder+"\\"+folders[folders.length-1]+"-thumbnails";
				else if(compressionType == 3)
					newFolder = "D:\\Competitions_dev\\Maintenance\\Uploads\\"+folders[folders.length-1]+"-package";
				
 		  		dir = new File(newFolder);
				Boolean dirCreated =  dir.mkdirs();
				if(dirCreated){
					compressedFolder = newFolder;
					return true;
				}else
					return false;				
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
   }
   
   public Integer writeFile(String folder, String pictureId, Integer width, Integer height){
		try
		{
			File file1 = new File(pictureId);
			BufferedImage img1 = ImageIO.read(file1);
			Mode exact=Mode.FIT_EXACT;
			BufferedImage scaledImage= Scalr.resize(img1,exact,width,height);
			ImageIO.write(scaledImage, "jpg", new File(folder+pictureId));
			img1.flush();
			img1=null;
			file1=null;
			System.gc();
			return 1;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
 	
 	 public static void main(String[] args){
 	      CompetoidTools  competoidTools = new CompetoidTools();
 	      competoidTools.showTextFieldDemo();
 	     //competoidTools.createDirectory();
 	    		 /*(path.getText()+"\\"+folders[folders.length-1]+"-package") == 1)
         	statusLabel.setText("Folder "+folders[folders.length-1]+"-package has been created");
         else
         	statusLabel.setText("!!!!Folder <<< "+folders[folders.length-1]+"-package >>> creation failed");*/
         
 	 }
 	 
}