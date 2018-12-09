/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tennisparsersimple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author precision
 */
public class TennisParserSimple {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String res = "tournament, date, aName, bName, aSets, bSets, aS1, aS2, aS3, aS4, aS5, bS1, bS2, bS3, bS4, bS5\n";
        // TODO code application logic here
        LocalDate sta= LocalDate.of(2018, 1, 1);
        LocalDate fin= LocalDate.of(2019, 1, 1);
        String datRange= sta +"_to_"+fin.minusDays(1);
        //LocalDate fin = LocalDate.now();
        println(sta +" "+fin);
        String direc;
        URL url;
        List< BufferedReader> lista = new ArrayList<>();
        while(sta.isBefore(fin)){
            System.out.println(sta);
            direc = "http://www.tennislive.net/atp-men/"+sta+"/";
                 
        try {
            sta = sta.plusDays(1);
            url = new URL(direc);
            System.out.println(url);
            BufferedReader reader= new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            lista.add(reader);
                
        } catch (MalformedURLException ex) {
            Logger.getLogger(LocalDate.class.getName()).log(Level.SEVERE, null, ex);
            println("pelation");
        }   catch (IOException ex) {
                Logger.getLogger(TennisParserSimple.class.getName()).log(Level.SEVERE, null, ex);
                println("pelation");
            }
        }
        
        //lista.forEach(item ->
        for(BufferedReader item : lista)
        {
            //println(item.getClass());
            
            Iterator< String> lis = item.lines().iterator();
            String page="";
            try{
                while(lis.hasNext()){
                page = page +"\n" +lis.next();
                }
            }catch(Exception ex){
                println(ex.getMessage());
                println(page.length());
                if(page.contains("<td rowspan=\"2\" class=\"beg\">"))
                    println(page.substring(page.indexOf("<td rowspan=\"2\" class=\"beg\">")+28,
                            page.indexOf("<td rowspan=\"2\" class=\"beg\">")+36));
            }
            page = page.substring(page.indexOf("<table"));
            page = page.substring(0, page.lastIndexOf("</table>")+8);
            //println(page);
            String[] matches = page.split("<tbody");
            String tournament="";
            for(int i =1; i< matches.length; i++){
                if(matches[i].contains("class=\"header\"")){
                //get tournamet
                String tor = matches[i].substring(matches[i].indexOf("/\" title=\"")+10);
                tor = tor.substring(0, tor.indexOf("\">"));
                //println(tor);
                tournament= tor;
                }
                String[] inters = matches[i].split("<tr ");
                //println(inters.length);
                if(inters.length>2){
                String a = inters[inters.length-2];
                String b = inters[inters.length-1];
                String date, level;
                String aName, aSets="", aS1="", aS2="", aS3="", aS4="", aS5="";
                String bName, bSets="", bS1="", bS2="", bS3="", bS4="", bS5="";
                //println(a);
                //println(b);
                date = a.substring(a.indexOf("\"beg\">")+6);
                date= date.substring(0,8);
                //println(date);
                String[] aPar = a.split("</td>");
                String[] bPar = b.split("</td>");
                //println(aPar.length);
                //println(bPar.length);
                //println(aPar[1]);
                //println(bPar[0]);
                aName = aPar[1].substring(aPar[1].lastIndexOf("/\">")+3);
                bName = bPar[0].substring(bPar[0].lastIndexOf("/\">")+3);
                aName =aName.substring(0,aName.indexOf("<"));
                bName =bName.substring(0,bName.indexOf("<"));
                //println(aName);
                //println(bName);
                if(aPar[2].length()>(aPar[2].indexOf("\">")+2))
                    aSets=aPar[2].charAt(aPar[2].indexOf("\">")+2)+"";
                if(bPar[1].length()>bPar[1].indexOf("\">")+2)
                    bSets=bPar[1].charAt(bPar[1].indexOf("\">")+2)+"";
                //println(aSets);
                //println(bSets);
                if(aPar[3].length()>(aPar[3].indexOf("\">")+2))
                    aS1=aPar[3].charAt(aPar[3].indexOf("\">")+2)+"";
                if(bPar[2].length()>bPar[2].indexOf("\">")+2)
                    bS1=bPar[2].charAt(bPar[2].indexOf("\">")+2)+"";
                //println(aS1);
                //println(bS1);
                if(aPar[4].length()>(aPar[4].indexOf("\">")+2))
                    aS2=aPar[4].charAt(aPar[4].indexOf("\">")+2)+"";
                if(bPar[3].length()>bPar[3].indexOf("\">")+2)
                    bS2=bPar[3].charAt(bPar[3].indexOf("\">")+2)+"";
                //println(aS2);
                //println(bS2);
                //println("TESTS");
                //println(aPar[5].length() +" ; "+ aPar[5].indexOf("\">")+2);
                //println(aPar[5].length()>(aPar[5].indexOf("\">")+2));
                if(aPar[5].length()>(aPar[5].indexOf("\">")+2))
                    aS3=aPar[5].charAt(aPar[5].indexOf("\">")+2)+"";
                if(bPar[4].length()>bPar[4].indexOf("\">")+2)
                    bS3=bPar[4].charAt(bPar[4].indexOf("\">")+2)+"";
                if(aPar[6].length()>aPar[6].indexOf("\">")+2)
                    aS4=aPar[6].charAt(aPar[6].indexOf("\">")+2)+"";
                if(bPar[5].length()>bPar[5].indexOf("\">")+2)
                    bS4=bPar[5].charAt(bPar[5].indexOf("\">")+2)+"";
                if(aPar[7].length()>aPar[7].indexOf("\">")+2)
                    aS5=aPar[7].charAt(aPar[7].indexOf("\">")+2)+"";
                if(bPar[6].length()>bPar[6].indexOf("\">")+2)
                    bS5=bPar[6].charAt(bPar[6].indexOf("\">")+2)+"";
                int y,m,d;
                //println(date + " | "+date.substring(0,2)+ " | "+date.substring(3,5));
                //println(date);
                //println(date.length());
                y = 2000 + Integer.parseInt(date.substring(6));
                m = Integer.parseInt(date.substring(3,5));
                d = Integer.parseInt(date.substring(0,2));
                LocalDate dat = LocalDate.of(y,m,d);
                res = res + (tournament +" ,"+dat+" ,"+aName+" ,"+bName+" ,"+aSets+" ,"+bSets+" ,"+aS1+" ,"+aS2+" ,"+aS3+" ,"+aS4+" ,"+aS5+" ,"+bS1+" ,"+bS2+" ,"+bS3+" ,"+bS4+" ,"+bS5+"\n");
                }
            } 
            //println(matches.length);
            
            
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("Data/data_"+datRange+".csv"));
        writer.write(res);
        writer.close();
    }
    public static void println(Object x){
        System.out.println(x.toString());
    }
    
}
