/**************************************
*TestErrCounter.java
*Nolan Dickey
*
*
*
*Outputs a text file with the number of
*errors found in each test in Memtest86.
***************************************/

import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;
public class TestErrCounter{
    public static void main(String[] args){
        String newFile = "default";
        String oldFile = "default";
        String line = null;
        int[] tests = {0,0,0,0,0,0,0,0,0,0,0,0};
        int flag = 0;
        try{
            Scanner scanny = new Scanner(System.in);
            ArrayList<File> files = new ArrayList<>();
            System.out.print("Enter directory: ");
            File folder = new File(scanny.nextLine());
            System.out.print("Specify start or end of filename? ");
            String ext="";
            switch (scanny.nextLine().toLowerCase().charAt(0)){
                case 's':
                    System.out.print("Enter beginning of logfile name: ");
                    ext = scanny.nextLine();
                    break;
                case 'e':
                    System.out.print("Enter logfile extension/end: ");
                    ext = scanny.nextLine();
                    break;
            }            
            File[] listF = folder.listFiles();
            for (File f:listF){
                if (f.getName().startsWith(ext))
                    files.add(f);
            }    
            System.out.print("Enter name of output file: ");    
            newFile = scanny.nextLine();
            PrintWriter write = new PrintWriter(newFile);
            BufferedWriter bw = new BufferedWriter(write);
            for (File f:files){
                BufferedReader br = new BufferedReader(new FileReader(f));
                while ((line = br.readLine()) != null){
                    if(line.contains("MC10_ADDR")){
                        tests[flag]++;
                    }else if (line.contains("test #")){
                        flag = Integer.valueOf(line.substring(line.indexOf('#')+1, line.indexOf('(')-1));
                        if (flag == 13)
                            flag = 11;
                    }
                }
                br.close();            
            }
            int totalErrors = 0;
            for (int i:tests){
                totalErrors+=i;
            }
            int j = 0;
            for (int i:tests){
                bw.write(String.format("Test %d: %d (%2.2f",j,i,((double)i/(double)totalErrors)*100)+"%)");
                bw.newLine();
                j++;
            }
            bw.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("Unable to open file: " + oldFile);
        }
        catch(IOException ex){
            System.out.println("Error reading file.");
        }
    }
}
