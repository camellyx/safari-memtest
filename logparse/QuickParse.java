import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
public class QuickParse{
    public static void main(String[] args){
        String newFile = "default";
        String oldFile = "default";
        String line = null;
        try{
            Scanner scanny = new Scanner(System.in);
            if (args[0]!=null){
                oldFile = args[0];
            }else{
                System.out.print("Enter name of input file (placed in this folder): ");
                oldFile = scanny.nextLine();
            }
            if (args[1]!=null){
                newFile = args[1];
            }else{
                System.out.print("Enter name of output file: ");
                newFile = scanny.nextLine();
            }
            BufferedReader br = new BufferedReader(new FileReader(oldFile));
            PrintWriter write = new PrintWriter(newFile);
            BufferedWriter bw = new BufferedWriter(write);
            while ((line = br.readLine()) != null){
                if (line.contains("TEST SESSION") || line.contains("Finished")
                    || line.contains("Running") || line.contains("Transaction")
                    || line.contains("Model") || line.contains("MC10")
                    || line.contains("Starting")){
                    bw.write(line);
                    bw.newLine();
                }
            }
            br.close();
            bw.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("Unable to open file: " + oldFile);
        }
        catch(IOException ex){
            System.out.println("Error reading file.");
        }
        
        System.out.println("Ding! Hot and fresh out the kitchen.");
                
    }
}
