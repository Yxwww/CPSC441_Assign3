package cpsc441.solution;

import cpsc441.doNOTmodify.Topology;

import java.io.*;
import java.util.*;

/**
 * Created by Yx on 11/17/2014.
 */
public class TopologyWrapper {
    public int routerNum;
    public int[][] linkCostMatrix;
    // parsing topology file
    public TopologyWrapper(String topoFileName){
        try(BufferedReader br = new BufferedReader(new FileReader(topoFileName))) {
            String firstLine = br.readLine();
            String[] tokens = firstLine.split(" ");
            if(tokens[0].equalsIgnoreCase("5")){
                this.routerNum = Integer.parseInt(tokens[0]);
                this.linkCostMatrix = new int[5][5];// init matrix to the right number of routers
                int rowCounter = 0;
                for(String line; (line = br.readLine()) != null; ) {
                    System.out.println(line);
                    StringTokenizer tempTokens = new StringTokenizer(line);
                    if(tempTokens.countTokens() == this.routerNum){
                        for(int i=0;i<5;i++){
                            this.linkCostMatrix[rowCounter][i] = Integer.parseInt(tempTokens.nextToken());
                        }
                        rowCounter++;
                    }else{
                        System.out.println("*Problem reading topology: line tokens length "+tempTokens.countTokens()+" doesn't match routerNum: "+this.routerNum);
                        System.exit(-1);
                    }
                }
            }else{
                // handles wrong number of routers
                System.exit(-1);
            }
            this.toString();
            System.out.println("#Topology successfully loaded.");

            // line is not visible here.
        }catch(IOException e){
            System.out.println("Unable to read from "+ topoFileName + ". due to:" + e);
        }
    }//     END of constructor

    public String toString(){
       String returnString = "";
        for(int i =0;i<this.routerNum;i++){
            for(int j=0;j<this.routerNum;j++){
                returnString += this.linkCostMatrix[i][j] + "\t";
            }
            returnString += "\n";
        }
        return "routner number: "+this.routerNum+"\n"+returnString;
    }
}
