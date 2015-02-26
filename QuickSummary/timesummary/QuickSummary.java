import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Locale;
import java.text.ParseException;
public class QuickSummary{
    public static void main(String[] args){
        String newFile = "default.summary";
        try{
            java.util.Date time;
            String prevline = null;
            String prevprev = null;
            String line = "";
            long currentTime = 0;
            java.util.Date startTime;
            File[] files = new File[100];
            if (args.length!=0){
                int i = 0;
                for (String s:args){
                    files[i] = new File(s);
                    i++;
                }
            }else{
                System.out.print("Enter log file names in order to be read, or q to quit (only up to 100 files): ");
                String input = "";
                for (int i = 0; !input.equalsIgnoreCase("q"); i++){
                    input = scanny.nextLine();
                    if (input.equalsIgnoreCase("q"))
                        break;
                    files[i] = new File(input);
                }
            }
            System.out.print("Enter name of output file for summary: ");
            newFile = scanny.nextLine();
            PrintWriter write = new PrintWriter(newFile);
            BufferedWriter bw = new BufferedWriter(write);
            for (File file:files){
                if (file == null)
                    break;
                BufferedReader br = new BufferedReader(new FileReader(file));
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                line = br.readLine();
                startTime = format.parse(line.substring(0, 19));
                startTime.setTime(startTime.getTime()-currentTime);
                bw.write(startTime.getTime() + " 0");
                bw.newLine();
                while (line != null){
                    prevprev = prevline;
                    prevline = line;
                    line = br.readLine();
                    if (line == null){
                        time = format.parse(prevprev.substring(0, 19));
                        currentTime = (time.getTime()-startTime.getTime());
                        bw.write((currentTime/1000) + " 0");
                        bw.newLine();
                        break;
                    }
                    if (line.contains("MC10_STATUS")){
                        time = format.parse(line.substring(0, 19));
                        currentTime = (time.getTime()-startTime.getTime());
                        bw.write((currentTime/1000) + " 1");
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
        catch(ParseException ex){
            System.out.println("Unable to parse date.");
        }          
    }
}
