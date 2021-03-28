/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printers;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.PrintServiceAttribute;
import jaxesa.label.BarcodePackage;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;

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

       
    
}


