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
            Map<Long, Integer> addrs = new HashMap<Long, Integer>();
            Map<Long, String> addrsC = new HashMap<Long, String>();
            Map<Long, Integer> rows = new HashMap<Long, Integer>();
            Map<Long, String> rowsC = new HashMap<Long, String>();
            Map<Long, Integer> cols = new HashMap<Long, Integer>();
            Map<Long, String> colsC = new HashMap<Long, String>();
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
            String status;
            int testNo = 0;
            for (File f:files){
                bw.write(testNo+"="+f.getName()+"\n");
                BufferedReader br = new BufferedReader(new FileReader(f));
                int flag=0;
                System.out.println("Reading file"+testNo);
                while ((line = br.readLine()) != null){
                    if (line == null){
                        break;
                    }
                    if (line.contains("test #")){
                        flag = Integer.valueOf(line.substring(line.indexOf('#')+1, line.indexOf('(')-1));
                        if (flag == 13)
                            flag = 11;
                    }
                    if (line.contains("MC10_ADDR")){
                        status = line.substring(line.indexOf('=')+1, line.length());
                        long addr = Long.parseLong(status, 16);
                        if (addrs.containsKey(addr)){
                            addrs.put(addr, addrs.get(addr)+1);
                            addrsC.put(addr, addrsC.get(addr)+" "+testNo+":"+flag);
                        }else{
                            addrs.put(addr, 1);
                            addrsC.put(addr, testNo+":"+flag);
                        }
                        long row = addr/8192;
                        if (rows.containsKey(row)){
                            rows.put(row, rows.get(row)+1);
                            rowsC.put(row, rowsC.get(row)+" "+testNo+":"+flag);
                        }else{
                            rows.put(row, 1);
                            rowsC.put(row, testNo+":"+flag);
                        }
                        long column = addr%8192;
                        if (cols.containsKey(column)){
                            cols.put(column, cols.get(column)+1);
                            colsC.put(column, colsC.get(column)+" "+testNo+":"+flag);
                        }else{
                            cols.put(column, 1);
                            colsC.put(column, testNo+":"+flag);
                        }
                    }
                }
                br.close();
                testNo++;
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
                bw.write(ent.getKey()+": "+ent.getValue()+" errors: "+addrsC.get(ent.getKey()));
                bw.newLine();
            }
            bw.write("Rows:\n");
            for (Iterator<Map.Entry> it = rList.iterator();it.hasNext();){
                Map.Entry ent = it.next();
                bw.write(ent.getKey()+": "+ent.getValue()+" errors: "+rowsC.get(ent.getKey()));
                bw.newLine();
            }
            bw.write("Columns:\n");
            for (Iterator<Map.Entry> it = cList.iterator();it.hasNext();){
                Map.Entry ent = it.next();
                bw.write(ent.getKey()+": "+ent.getValue()+" errors: "+colsC.get(ent.getKey()));
                bw.newLine();
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
