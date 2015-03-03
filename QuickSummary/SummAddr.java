import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.text.ParseException;
import java.util.ArrayList;
public class SummAddr{
    public static void main(String[] args){
        String newFile = "default.summary";
        try{
            String line = "";
            long currentTime = 0;
            java.util.Date startTime;
            Scanner scanny = new Scanner(System.in);
            ArrayList<File> files = new ArrayList<>();
            if (args.length!=0){
                int i = 0;
                for (String s:args){
                    files.add(new File(s));
                    i++;
                }
            }else{
                System.out.print("Enter log file names in order to be read. Enter blank line to quit: ");
                String input = "";
                for (int i = 0; !input.equals(""); i++){
                    input = scanny.nextLine();
                    if (input.equals(""))
                        break;
                    files.add(new File(input));
                }
            }
            System.out.print("Enter name of output file for summary: ");
            newFile = scanny.nextLine();
            PrintWriter write = new PrintWriter(newFile);
            BufferedWriter bw = new BufferedWriter(write);
            String status;
            for (File file:files){
                BufferedReader br = new BufferedReader(new FileReader(file));
                while (line != null){
                    line = br.readLine();
                    if (line == null){
                        break;
                    }
                    if (line.contains("MC10_ADDR")){
                        status = line.substring(line.indexOf('=')+1, line.length());
                        long addr = Long.parseLong(status, 16);
                        long row = addr/8192;
                        long column = addr%8192;
                        String rs = String.valueOf(row);
                        String cs = String.valueOf(column);
                        status = rs+" "+cs;
                        bw.write(status);
                        bw.newLine();
                    }
                }
                br.close();
            }
            bw.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("Unable to open file");
        }
        catch(IOException ex){
            System.out.println("Error reading file.");
        }
    }
}
