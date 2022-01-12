import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.lang.StringBuilder;

public class Stock {

public static void main(String[] args) {
    try{
        String val=("<td width=\"7%\" class=\"snapshot-td2-cp\" align=\"left\" title=\"cssbody=[tooltip_short_bdy] cssheader=[tooltip_short_hdr] body=[Market capitalization] offsetx=[10] offsety=[20] delay=[300]\">Market Cap</td><td width=\"8%\" class=\"snapshot-td2\" align=\"left\"><b>12.02B</b></td>");
        String last=fixError(val);
        System.out.println(last);
    }
    catch (Exception e)
    {
    }

}

public static String getData(String target) throws IOException{
    //formatting target is awlays cpatial first leet of words
    String SYM = "[Market capitalization]";
    URL url = new URL ("https://finviz.com/quote.ashx?t=AAL&ty=c&p=d&b=1");
    URLConnection urlConn = url.openConnection ();
    InputStreamReader inStream = new InputStreamReader (urlConn.getInputStream ());
    BufferedReader buff = new BufferedReader (inStream) ;   
    String line = buff.readLine ();
    String result = "didn't work";
    while (line!= null) {  
        if (line.contains ("[Market capitalization]")) 
        { 
            result=line;
            break; 
        }
        line=buff.readLine();
    }
    inStream.close();
    buff.close(); 
    return result;
    //System.out.println("work");
    
}

public static String fixError(String result){
    /*
    for (int i = 0; i < result.length(); i++) {
            //int i=5;
            System.out.println(result.substring(i,i+10));
            if(result.substring(i,i+5).equals("\"left\"><b>"))
            {
                int k=i+5;
                number=result.substring(i+10,k);
                while(!result.substring(k-1,k).equals("<"))//needs to be an indiviudal string
                {             
                    number=number+result.substring(k-1,k);
                    k++;
                    System.out.println("brace0");
                }
                System.out.println("brace1");
            }
            System.out.println(i +  " "  + result.length());
    }
    System.out.println("brace3");
    System.out.println(number);
    return number;
    */
    String value="didn't work";
    if(result.contains("</b></td>"))
    {
        value=result.substring(0,result.indexOf("</b></td>"));
    }
    String last=value;
    for (int i=value.length()-1; i>=0; i--) {
        System.out.println((Character.toString(value.charAt(i))));
       if((Character.toString(value.charAt(i))).equals(">"))
       {
            last=value.substring(i+1,value.length());
            break;
       }
    }
    return last;
}

}