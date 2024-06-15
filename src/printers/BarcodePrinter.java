/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printers;

import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.File;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.PrintServiceAttribute;

import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import java.awt.print.*;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;

public final class BarcodePrinter
{
    public static boolean print(String pName, String pFilePath, int count) 
    {

      try
         {
            PrintService ps=PrintServiceLookup.lookupDefaultPrintService();
            DocPrintJob job=ps.createPrintJob();

            FileInputStream fis = new FileInputStream(pFilePath);
            Doc doc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.JPEG, null);

            PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
            PrintJobWatcher watcher = new PrintJobWatcher(job);
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

            // document copies
            Copies copies = new Copies(count);
            aset.add(copies);

            //document page size
            aset.add(OrientationRequested.PORTRAIT);
            aset.add(MediaSizeName.ISO_A6);//closed for now (this was the normal)
            //aset.add(MediaSizeName.ISO_A9);

            aset.add(new JobName("SHIPSHUK - " + pName, Locale.getDefault()));
            //System.out.println("Printing...");

            job.print(doc, aset);

            // wait for the job to be done
            watcher.waitForDone();
            //System.out.println("Job Completed!!");

            return true; 
        }
        catch(Exception e)
        {
            return false;
        }
    }  
    
    // Convert pixels to millimeters
    private static double convertPixelsToMM(int pixels) 
    {
        final double DPI = 300; // You may need to adjust this based on the DPI of your image
        final double INCHES_TO_MM = 25.4;
        return pixels * INCHES_TO_MM / DPI;
    }
    
    public static boolean printTest(String pName, String pFilePath, int count) 
    {

      try
         {
            PrintService ps=PrintServiceLookup.lookupDefaultPrintService();
            DocPrintJob job=ps.createPrintJob();

            FileInputStream fis = new FileInputStream(pFilePath);
            Doc doc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.PNG, null);

            PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
            PrintJobWatcher watcher = new PrintJobWatcher(job);
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

            // document copies
            Copies copies = new Copies(count);
            aset.add(copies);

            
            BufferedImage image = ImageIO.read(new File(pFilePath));

            // Calculate image dimensions in millimeters
            double imageWidthMM = convertPixelsToMM(image.getWidth());
            double imageHeightMM = convertPixelsToMM(image.getHeight());

            // Convert millimeters to micrometers (1 mm = 1000 micrometers)
            int imageWidthMicrometers = (int) (imageWidthMM * 1000);
            int imageHeightMicrometers = (int) (imageHeightMM * 1000);

            //document page size
            aset.add(OrientationRequested.PORTRAIT);
            //aset.add(MediaSizeName.ISO_A6);//closed for now (this was the normal)
            MediaPrintableArea mediaPrintableArea = new MediaPrintableArea(0, 0, imageWidthMicrometers, imageHeightMicrometers, MediaPrintableArea.MM);
            aset.add(mediaPrintableArea);
            //aset.add(MediaSize.getMediaSizeForName(new MediaPrintableArea(0, 0, 62, 29, MediaPrintableArea.MM)));

            //aset.add(MediaSizeName.ISO_A9);

            aset.add(new JobName("SHIPSHUK - " + pName, Locale.getDefault()));
            //System.out.println("Printing...");

            job.print(doc, aset);

            // wait for the job to be done
            watcher.waitForDone();
            //System.out.println("Job Completed!!");

            return true; 
        }
        catch(Exception e)
        {
            return false;
        }
    }  

    // Calculate DPI based on pixel count and physical dimension
    private static int calculateDPI(int pixels, double physicalSizeInches) 
    {
        return (int) (pixels / physicalSizeInches);
    }

    public static boolean printWBrother810(String pName, String pFilePath, int count)
    {
        try
        {
            
            // Load the image
            BufferedImage image = ImageIO.read(new File(pFilePath));

            PrinterJob printerJob = PrinterJob.getPrinterJob();

            // Assuming the paper size is in points, convert it to inches
            //double paperWidthInches  = 234 / 25.4;  // 1 inch = 25.4 mm//pageFormat.getWidth();// / 72.0;
            //double paperHeightInches = 109 / 25.4;//pageFormat.getHeight();// / 72.0;

            // PIXELS
            // ---------------------
            double paperWidthPixels  = image.getWidth();//62 / 25.4;
            double paperHeightPixels = image.getHeight();//29 / 25.4;

            // PIXEL to INCHES
            // ---------------------
            int iDPI = 300;//72 for images 300 for print images  ??
            double paperWidthInches  = paperWidthPixels / iDPI;//62 / 25.4;
            double paperHeightInches = paperHeightPixels / iDPI;//29 / 25.4;

            // POINTS
            // ---------------------
            int iTOPOINTS = 72;
            double paperWidthPoints  = paperWidthInches * iTOPOINTS;//62 / 25.4;
            double paperHeightPoints = paperHeightInches * iTOPOINTS;//29 / 25.4;

            

            //PageFormat pageFormat = printerJob.defaultPage();
            //pageFormat.setOrientation(PageFormat.PORTRAIT);
// Create a new PageFormat instance and set paper size and orientation
PageFormat pageFormat = new PageFormat();
Paper paper = new Paper();
paper.setSize(paperWidthPoints, paperHeightPoints);  // Convert inches to points
paper.setImageableArea(0, 0, paperWidthPoints, paperHeightPoints);  // Assuming full page is imageable
pageFormat.setPaper(paper);
pageFormat.setOrientation(PageFormat.PORTRAIT);  // or PageFormat.LANDSCAPE based on your requirement

        printerJob.setPrintable(new ImagePrintable(image), pageFormat);

        double xScale = 300 / 72.0;//dpi
        double yScale = 300 / 72.0;//dpi

        Rectangle2D deviceArea =
                                new Rectangle2D.Double(paper.getImageableX() * xScale,
                                                       paper.getImageableY() * yScale,
                                                       paper.getImageableWidth() * xScale,
                                                       paper.getImageableHeight() * yScale);
        int bandWidth = (int) deviceArea.getWidth();
        int deviceAreaHeight = (int)deviceArea.getHeight();

            // 1 
            double imageableHeight = pageFormat.getImageableHeight();
            double imageableWidth  = pageFormat.getImageableWidth();
            System.out.println("Image Dimensions: " + image.getWidth() + " x " + image.getHeight());
            System.out.println("Imageable Area Dimensions: " + imageableWidth + " x " + imageableHeight);

/*
            // 2
            // Calculate scaling factors to fit the image within the paper size
            double scaleX = paperWidthInches / image.getWidth();
            double scaleY = paperHeightInches / image.getHeight();
            double scale = Math.min(scaleX, scaleY);

            int scaledWidth = (int) (image.getWidth() * scale);
            int scaledHeight = (int) (image.getHeight() * scale);

            //3 
            // Calculate the position to center the image within the imageable area
            double imageableX = pageFormat.getImageableX();
            double imageableY = pageFormat.getImageableY();

            int x = (int) (imageableX + (imageableWidth - scaledWidth) / 2.0);
            int y = (int) (imageableY + (imageableHeight - scaledHeight) / 2.0);

            System.out.println("Imageable Area Dimensions: " + imageableWidth + " x " + imageableHeight);
            System.out.println("Scaled Image Dimensions: " + scaledWidth + " x " + scaledHeight);
            System.out.println("Image Position (x, y): " + x + ", " + y);
            System.out.println("Image Dimensions: " + image.getWidth() + " x " + image.getHeight());
*/

            //printerJob.print();
            //if (printerJob.printDialog()) 
            //{
                printerJob.print();
            //}

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    private static class ImagePrintable implements Printable
    {
        private BufferedImage image;
        //private double paperWidthInches;
        //private double paperHeightInches;


        public ImagePrintable(BufferedImage image) 
        {
            this.image = image;
            //this.paperWidthInches = paperWidthInches;
            //this.paperHeightInches = paperHeightInches;
        }
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
            if (pageIndex == 0) 
            {
                // Create a graphics2D object to apply additional margins
                Graphics2D g2d = (Graphics2D) graphics;
                /*
                double marginLeft = 0; // Adjust these margins as needed
                double marginTop = 0;
                g2d.translate(marginLeft, marginTop);
                */

                g2d.drawImage(image, 0, 0, (int) pageFormat.getWidth(), (int) pageFormat.getHeight(), null);

                return Printable.PAGE_EXISTS;
            } 
            else 
            {
                return Printable.NO_SUCH_PAGE;
            }
        }
    }
    
    
    
}


