/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.util;

//import com.independentsoft.office.word.WordDocument;
import java.io.File;


/**
 *
 * @author jallisson
 */
public class editararquivoword {

    public static void main(String[] args)
    {
        try
        {
            //WordDocument doc = new WordDocument("c:\\test\\input.docx");
           
            //doc.replace("#[Nome]","John Smith");
            
            File diretorio = new File("D:\\doc");
            diretorio.mkdir();
           
            //doc.save("C:\\doc\\output.docx", true);
            Runtime.getRuntime().exec("rundll32 url.dll FileProtocolHandler " + "c:\\test\\output.docx");
                
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
