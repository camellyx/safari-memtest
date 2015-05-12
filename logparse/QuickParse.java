import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;
public class QuickParse{
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
            System.out.print("Enter logfile extension: ");
            String ext = scanny.nextLine();
            File[] listF = folder.listFiles();
            for (File f:listF){
                if (f.getName().endsWith(ext))
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
            bw.write(String.format("%7d%7d%7d%7d%7d%7d%7d%7d%7d%7d%7d%7d",0,1,2,3,4,5,6,7,8,9,10,13));
            bw.newLine();
            for (int i:tests){
                bw.write(String.format("%7d",i));;
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
