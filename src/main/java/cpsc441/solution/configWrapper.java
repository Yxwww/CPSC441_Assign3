package cpsc441.solution;

import java.util.*;
import java.io.*;

/**
 * Created by Yx on 11/17/2014.
 */
public class configWrapper {
    public int NEM_ID,ARQ_TIMER,COST_INFTY;
    // Wrapper for conifg
    public configWrapper(String fileName){
        //System.out.println("Reading from: " + fileName);
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            for(String line; (line = br.readLine()) != null; ) {
                StringTokenizer tokens = new StringTokenizer(line);
                String attribute =tokens.nextToken();
                String value = tokens.nextToken();
                if(attribute.equalsIgnoreCase("NEM_ID")){
                    this.NEM_ID = Integer.parseInt(value);
                }else if(attribute.equalsIgnoreCase("ARQ_TIMER"))
                {
                    this.ARQ_TIMER = Integer.parseInt(value);
                }else if(attribute.equalsIgnoreCase("COST_INFTY")){
                    this.COST_INFTY = Integer.parseInt(value);
                }else{
                    System.out.println(" Unrecongnized conifg attribute. Life is hard !");
                }
            }
            System.out.println("#Config file successfully loaded");
            // line is not visible here.
        }catch(IOException e){
            System.out.println("Unable to read from "+ fileName + ". due to:" + e);
        }
    }

    public String toString() {
        return "NEM_ID: " + this.NEM_ID + "\tARQ_TIMER: "+ this.ARQ_TIMER + "\tCOST_INFTY: "+this.COST_INFTY;
    }
}
