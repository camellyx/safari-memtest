import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
public class Tabler{
    public static void main(String[] args){
        String newFileStat = "defaultStat";
        String newFileMisc = "defaultMisc";
        String newFileADD = "defaultAddress";
        String oldFile = "default";
        String line = null;
        try{
            String status;
            String time;
            Scanner scanny = new Scanner(System.in);
            System.out.print("Enter name of input file (placed in this folder): ");
            oldFile = scanny.nextLine();
            System.out.print("Enter name of output file for MC10_STATUS: ");
            newFileStat = scanny.nextLine();
            BufferedReader BRSTAT = new BufferedReader(new FileReader(oldFile));
            PrintWriter writeSTAT = new PrintWriter(newFileStat);
            BufferedWriter BWSTAT = new BufferedWriter(writeSTAT);
            while ((line = BRSTAT.readLine()) != null){
                
                if (line.contains("MC10_STATUS")){
                    time = line.substring(0, line.indexOf('M')-3);
                    status = line.substring(line.indexOf('=')+1, line.indexOf('(')-1);
                    BWSTAT.write(time+","+status+",");
                    BWSTAT.newLine();
                }
            }
            BRSTAT.close();
            BWSTAT.close();
            System.out.print("Enter name of output file for MC10_MISC: ");
            newFileMisc = scanny.nextLine();
            BufferedReader BRMISC = new BufferedReader(new FileReader(oldFile));
            PrintWriter writeMISC = new PrintWriter(newFileMisc);
            BufferedWriter BWMISC = new BufferedWriter(writeMISC);
            while ((line = BRMISC.readLine()) != null){
                
                if (line.contains("MC10_MISC")){
                    time = line.substring(0, line.indexOf('M')-3);
                    status = line.substring(line.indexOf('=')+1, line.length());
                    BWMISC.write(time+","+status+",");
                    BWMISC.newLine();
                }
            }
            BRMISC.close();
            BWMISC.close();
            System.out.print("Enter name of output file for MC10_ADDR: ");
            newFileADD = scanny.nextLine();
            BufferedReader BRADD = new BufferedReader(new FileReader(oldFile));
            PrintWriter writeADD = new PrintWriter(newFileADD);
            BufferedWriter BWADD = new BufferedWriter(writeADD);
            while ((line = BRADD.readLine()) != null){
                
                if (line.contains("MC10_ADDR")){
                    time = line.substring(0, line.indexOf('M')-3);
                    status = line.substring(line.indexOf('=')+1, line.length());
                    BWADD.write(time+","+status+",");
                    BWADD.newLine();
                }
            }
            BRADD.close();
            BWADD.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("Unable to open file: " + oldFile);
        }
        catch(IOException ex){
            System.out.println("Error reading file.");
        }
        
        System.out.println(new File(".").getAbsoluteFile());
                
    }
}
