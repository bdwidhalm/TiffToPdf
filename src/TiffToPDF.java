import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import com.itextpdf.text.io.FileChannelRandomAccessSource;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;

public class TiffToPDF
{
  public static void main(String[] args)
  {
    String fileList = "/home/brian/Documents/fileList.txt";
    BufferedReader br = null;
    try
    {
      br = new BufferedReader(new FileReader(fileList));
      // line example:
      // CCITT_1.TIF|/home/brian/Documents/CCITT_1.TIF|/home/brian/Documents/CCITT_1.pdf
      String line;
      while((line = br.readLine()) != null)
      {
        String[] tokens = line.split("\\|");
        System.out.println(tokens[0]);
        System.out.println(tokens[1]);
        System.out.println(tokens[2]);
        convertTiff(tokens[1], tokens[2]);
      }
    }
    catch (Exception e)
    {
      
    }
    
    // convertTiff("/home/brian/Documents/CCITT_1.TIF", "/home/brian/Documents/CCITT_1.pdf");
  }
  
  private static void convertTiff(String inFile, String outFile)
  {
    // in example: /home/brian/Documents/CCITT_1.TIF
    //   out file: /home/brian/Documents/CCITT_1.pdf
    try
    {
      RandomAccessFile aFile = new RandomAccessFile(inFile, "r");
      FileChannel inChannel = aFile.getChannel();
      FileChannelRandomAccessSource fcra =  new FileChannelRandomAccessSource(inChannel);
      Document document = new Document();
      PdfWriter.getInstance(document,  new FileOutputStream(outFile));
      document.open();              
      RandomAccessFileOrArray rafa = new RandomAccessFileOrArray(fcra);
      int pages = TiffImage.getNumberOfPages(rafa);
      Image image;
      for (int i = 1; i <= pages; i++) {            
          image = TiffImage.getTiffImage(rafa, i);
          Rectangle pageSize = new Rectangle(image.getWidth(),
                  image.getHeight());
          document.setPageSize(pageSize);
          document.newPage(); 
          document.add(image);
      }
      document.close();
      aFile.close();
    }
    catch (Exception e)
    {
      System.out.println(e.toString());
    }
    
  }
}