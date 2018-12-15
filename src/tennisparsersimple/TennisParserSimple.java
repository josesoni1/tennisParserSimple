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
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


/**
 *Simple Parser for tennis Data from www.tennislive.net
 * 
 * @author SONIJOS
 */
public class TennisParserSimple {
    final static String ATP = "atp-men/";
    final static String WTA = "wta-women/";
    final static String ALL = "";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        String reSingles = "tournament, date, aName, bName, aSets, bSets, aS1, aS2, aS3, aS4, aS5, bS1, bS2, bS3, bS4, bS5\n";
        String reDobles = "tournament, date, aName, bName, aSets, bSets, aS1, aS2, aS3, aS4, aS5, bS1, bS2, bS3, bS4, bS5\n";
        String cat = "", fCat="";
        // TODO code application logic here
        LocalDate sta= LocalDate.of(2018, 1, 1);
        LocalDate fin= LocalDate.of(2018, 2, 1);
        Scanner sc = new Scanner(System.in);
        char op;
        boolean hi = true;
        while(hi){
            println("Select the Matches you want to parse: \n\tFor men:\tEnter 1\n\tFor women:\tEnter 2\n\tFor All:\tEnter 3");
            op = sc.next().charAt(0);
            hi = false;
            switch(op){
                case '1':
                    cat = ATP;
                    fCat="ATP";
                    break;
                case '2':
                    cat = WTA;
                    fCat = "WTA";
                    break;
                case '3':
                    cat = ALL;
                    fCat = "ALL";
                    break;
                default:
                    hi=true;
            }
        }
        String date1, date2;
        hi = true;
        while(hi){
            println("Date of start should be after 31/12/2002 \nPlease insert a date in format dd/mm/yyyy");
            date1 = sc.next();
            Pattern regex = Pattern.compile("[0-3][0-9]/[0-1][0-9]/[0-9][0-9][0-9][0-9]");
            Predicate<String> matcher = regex.asPredicate();
            if(matcher.test(date1)){
                //load the date1 to sta
                try{
                    sta = LocalDate.parse(date1, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    if(sta.isAfter(LocalDate.of(2002,12,31)))
                        hi = false;
                }catch(Exception e){
                    hi = true;
                }
            }
        }
        println("Start date is: "+sta);
        hi=true;
        while(hi){
            println("Date of end should be after initial \nPlease insert a date in format dd/mm/yyyy");
            date2 = sc.next();
            Pattern regex = Pattern.compile("[0-3][0-9]/[0-1][0-9]/[0-9][0-9][0-9][0-9]");
            Predicate<String> matcher = regex.asPredicate();
            if(matcher.test(date2)){
                //load the date2 to fin
                try{
                    fin = LocalDate.parse(date2, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    //println(sta+" : "+fin);
                    if(fin.isAfter(sta))
                        hi = false;
                }catch(Exception e){
                    hi = true;
                }
            }
        }
        
        String datRange= sta +"_to_"+fin.minusDays(1);
        println("Date range is: "+sta +" : "+fin);
        String direc;
        URL url;
        List< BufferedReader> listA = new ArrayList<>();
        while(sta.isBefore(fin)){
            println(sta);
            direc = "http://www.tennislive.net/"+cat+sta+"/";
                 
        try {
            sta = sta.plusDays(1);
            url = new URL(direc);
            println(url);
            BufferedReader reader= new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            listA.add(reader);
                
        } catch (MalformedURLException ex) {
            Logger.getLogger(LocalDate.class.getName()).log(Level.SEVERE, null, ex);
            println("WRONG URL "+direc);
        }   catch (IOException ex) {
                Logger.getLogger(TennisParserSimple.class.getName()).log(Level.SEVERE, null, ex);
                println("IO Error");
            }
        }
        
        for(BufferedReader item : listA)
        {
            //println(item.);
            Iterator< String> lis = item.lines().iterator();
            String page="";
            try{
                Instant ins = Instant.now();
                Instant intIns;
                while(lis.hasNext()){
                page = page +"\n" +lis.next();
                intIns = Instant.now();
                if((intIns.getEpochSecond() - ins.getEpochSecond())>6){//timeout at 6 seconds
                    println("timeout");
                    println("intIns: "+intIns.getEpochSecond());
                }
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
            boolean first = true;
            Instant instant = Instant.now();
            for(int i =1; i< matches.length; i++){
                Instant internalInstant = Instant.now();
                if((internalInstant.getEpochSecond()-instant.getEpochSecond())/1000000 >3){
                    println("timeout");
                    continue;
                }
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
                if(first){
                println(date);
                first= false;
                }
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
                int y=0,m=0,d=0;
                //println(date + " | "+date.substring(0,2)+ " | "+date.substring(3,5));
                //println(date);
                //println(date.length());
                Pattern regex = Pattern.compile("[0-3][0-9].[0-1][0-9].[0-9][0-9]");
                Predicate<String> matcher = regex.asPredicate();
                if( matcher.test(date) ){
                    y = 2000 + Integer.parseInt(date.substring(6));
                    m = Integer.parseInt(date.substring(3,5));
                    d = Integer.parseInt(date.substring(0,2));
                } else {
                    continue;
                }
                LocalDate dat;
                try{
                    dat = LocalDate.of(y,m,d);
                }catch( Exception e){
                    println("WRONG DATE:" +date);
                    println(aName+" : "+bName);
                    continue;
                    
                }
                
                if(aName.contains("/")&&bName.contains("/")){
                    reDobles = reDobles + ("\""+tournament +"\" ,"+dat+" ,\""+aName.replace("-"," ")+"\" ,\""+bName.replace("-"," ")+"\" ,"+aSets+" ,"+bSets+" ,"+aS1+" ,"+aS2+" ,"+aS3+" ,"+aS4+" ,"+aS5+" ,"+bS1+" ,"+bS2+" ,"+bS3+" ,"+bS4+" ,"+bS5+"\n");
                }else{
                    reSingles = reSingles + ("\""+tournament +"\" ,"+dat+" ,\""+aName.replace("-"," ")+"\" ,\""+bName.replace("-"," ")+"\" ,"+aSets+" ,"+bSets+" ,"+aS1+" ,"+aS2+" ,"+aS3+" ,"+aS4+" ,"+aS5+" ,"+bS1+" ,"+bS2+" ,"+bS3+" ,"+bS4+" ,"+bS5+"\n");
                }
                
                }
            } 
            //println(matches.length);
        }
        
            BufferedWriter writer = new BufferedWriter(new FileWriter("Data/Singles_"+fCat+"_"+datRange+".csv")); 
            writer.write(reSingles);
            writer.close();
            
            writer = new BufferedWriter(new FileWriter("Data/Doubles_"+fCat+"_"+datRange+".csv"));
            writer.write(reDobles);
            writer.close();
       
    }
    
    private static void println(Object x){
        System.out.println(x.toString());
    }
    
}
