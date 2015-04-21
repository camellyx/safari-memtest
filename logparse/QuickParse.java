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
            if (args.length!=0){
                oldFile = args[0];
            }else{
                System.out.print("Enter name of input file (placed in this folder): ");
                oldFile = scanny.nextLine();
            }
            if (args.length>1){
                newFile = args[1];
            }else{
                System.out.print("Enter name of output file: ");
                newFile = scanny.nextLine();
            }
            BufferedReader br = new BufferedReader(new FileReader(oldFile));
            PrintWriter write = new PrintWriter(newFile);
            BufferedWriter bw = new BufferedWriter(write);
            while ((line = br.readLine()) != null){
                if(line.contains("MC10_ADDR")){
                    bw.write(line.susbstring(line.indexOf('='), line.length));
                    bw.newLine();
                }else if (line.contains("test #"){
                    bw.write(line.substring(line.indexOf('[')+1, line.indexOf('[')));
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
