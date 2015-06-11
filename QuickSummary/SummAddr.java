import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.text.ParseException;
import java.util.*;
public class SummAddr{
    public static void main(String[] args){
        String newFile = "default.summary";
        try{
            String line = "";
            long currentTime = 0;
            java.util.Date startTime;
            Scanner scanny = new Scanner(System.in);
            ArrayList<File> files = new ArrayList<>();
            Map<Long, Integer> addrs = new HashMap<Long, Integer>();
            Map<Long, Integer> rows = new HashMap<Long, Integer>();
            Map<Long, Integer> cols = new HashMap<Long, Integer>();
            if (args.length!=0){
                int i = 0;
                for (String s:args){
                    files.add(new File(s));
                    i++;
                }
            }else{
                System.out.print("Enter log file names in order to be read. Enter blank line to quit: ");
                String input = " ";
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
                        if (addrs.containsKey(addr))
                            addrs.put(addr, addrs.get(addr)+1);
                        else
                            addrs.put(addr, 1);
                        long row = addr/8192;
                        if (rows.containsKey(row))
                            rows.put(row, rows.get(row)+1);
                        else
                            rows.put(row, 1);
                        long column = addr%8192;
                        if (cols.containsKey(column))
                            cols.put(column, cols.get(column)+1);
                        else
                            cols.put(column, 1);
                    }
                }
                br.close();
            }
            Comparator com = new Comparator(){
                public int compare(Object obj1, Object obj2){
                    return (((Comparable)(((Map.Entry) obj1).getValue())).compareTo((Comparable)(((Map.Entry) obj2).getValue())));
                }
            };
            List aList = new LinkedList(addrs.entrySet());
            Collections.sort(aList, com);
            List rList = new LinkedList(rows.entrySet());
            Collections.sort(rList, com);
            List cList = new LinkedList(cols.entrySet());
            Collections.sort(cList, com);
            bw.write("Addresses:\n");
            for (Iterator<Map.Entry> it = aList.iterator();it.hasNext();){
                Map.Entry ent = it.next();
                bw.write(ent.getKey()+": "+ent.getValue()+" errors.\n");
            }
            bw.write("Rows:\n");
            for (Iterator<Map.Entry> it = rList.iterator();it.hasNext();){
                Map.Entry ent = it.next();
                bw.write(ent.getKey()+": "+ent.getValue()+" errors.\n");
            }
            bw.write("Columns:\n");
            for (Iterator<Map.Entry> it = cList.iterator();it.hasNext();){
                Map.Entry ent = it.next();
                bw.write(ent.getKey()+": "+ent.getValue()+" errors.\n");
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
