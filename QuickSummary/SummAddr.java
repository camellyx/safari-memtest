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
            String line2 = "";
            String line3 = "";
            String line4 = "";
            long currentTime = 0;
            java.util.Date startTime;
            Scanner scanny = new Scanner(System.in);
            Map<Long, Integer> addrs = new HashMap<Long, Integer>();
            int x1 = 0;
            int y1 = 0;
            int z1 = 0;
            double x2 = 0;
            double y2 = 0;
            double z2 = 0;
            Map<Long, String> addrsC = new HashMap<Long, String>();
            Map<Long, Integer> rows = new HashMap<Long, Integer>();
            Map<Long, String> rowsC = new HashMap<Long, String>();
            Map<Long, Integer> cols = new HashMap<Long, Integer>();
            Map<Long, String> colsC = new HashMap<Long, String>();
            ArrayList<File> files = new ArrayList<>();
            if (args.length>=2){
                for (int i =0; i<args.length;i++){
                    if (i!=args.length-1)
                        files.add(new File("./"+args[i]));
                    else
                        newFile = args[i];
                }
            }else{
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
            }
            PrintWriter write = new PrintWriter(newFile);
            BufferedWriter bw = new BufferedWriter(write);
            String status;
            int bits = 0;
            int testNo = 0;
            Map<Long, ArrayList<Integer>[]> failed = new HashMap<Long, ArrayList<Integer>[]>();
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
                        if (!line4.equals("")){
                            line4 = line4.substring(55,line4.length());
                            line4 = line4.substring(line4.indexOf('C'), line4.length());
                            bits = Integer.valueOf(line4.substring(line4.indexOf('C')+22, line4.indexOf(',')));
                        }
                        status = line.substring(line.indexOf('=')+1, line.length());
                        long addr = Long.parseLong(status, 16);
                        if (addrs.containsKey(addr)){
                            addrs.put(addr, addrs.get(addr)+1);
                            addrsC.put(addr, addrsC.get(addr)+" "+testNo+":"+flag);
                        }else{
                            addrs.put(addr, 1);
                            addrsC.put(addr, testNo+":"+flag);
                        }
                        if (failed.containsKey(addr)){
                            if (failed.get(addr)[flag]!=null)
                                failed.get(addr)[flag].add(bits);
                            else{
                                failed.get(addr)[flag]=new ArrayList<Integer>();
                                failed.get(addr)[flag].add(bits);
                            }
                        }else{
                            failed.put(addr, new ArrayList[12]);
                            failed.get(addr)[flag]=new ArrayList<Integer>();
                            failed.get(addr)[flag].add(bits);
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
                    line4 = line3;
                    line3 = line2;
                    line2 = line;
                }
                br.close();
                testNo++;
            }//end for files
            List failedL = new LinkedList(failed.entrySet());
            Iterator fi = failedL.iterator();
            while (fi.hasNext()){
                Map.Entry nextAddr = (Map.Entry)fi.next();
                if ((Long)nextAddr.getKey()!=0){
                ArrayList<Integer>[] failures = (ArrayList<Integer>[])nextAddr.getValue();
                int min1 = 600;
                int max1 = 0;
                int sum1 = 0;
                double min2 = 600;
                double max2 = 0;
                double sum2 = 0;
                double[] v2 = new double[12];
                int[] v1 = new int[12];
                for (int i=0;i<12;i++){
                    int min = 600;
                    int sum = 0;
                    int num = 0;
                    if (failures[i]!=null){
                        ArrayList<Integer> al = failures[i];
                        for (Integer j:al){
                            if (j<min){
                                min = j;
                            }
                            sum+=j;
                            num++;
                        }
                        v1[i]=min;
                        v2[i]=((double)sum)/((double)num);
                    }else{
                        v1[i]=0;
                        v2[i]=0;
                    }
                }
                for (int v:v1){
                    if (v<min1)
                        min1 = v;
                    if (v>max1)
                        max1 = v;
                    sum1+=v;
                }
                for (double b:v2){
                    if (b<min2)
                        min2 = b;
                    if (b>max2)
                        max2 = b;
                    sum2 +=b;
                }
                if (z1>512)
                    z1 = 512;
                if (z2>512)
                    z2 = 512;
                x1 += min1;
                y1 += max1;
                z1 += sum1;
                x2 += min2;
                y2 += max2;
                z2 += sum2;
                }
            }
            Comparator com = new Comparator(){
                public int compare(Object obj1, Object obj2){
                    return (((Comparable)(((Map.Entry) obj1).getValue())).compareTo((Comparable)(((Map.Entry) obj2).getValue())));
                }
            };
            Comparator comx = new Comparator(){
                public int compare(Object obj1, Object obj2){
                    return (((Comparable)(((Map.Entry) obj1).getKey())).compareTo((Comparable)(((Map.Entry) obj2).getKey())));
                }
            };
            List aList = new LinkedList(addrs.entrySet());
            Collections.sort(aList, com);
            List rList = new LinkedList(rows.entrySet());
            Collections.sort(rList, com);
            List cList = new LinkedList(cols.entrySet());
            Collections.sort(cList, com);
            /*bw.write("Addresses:\n");
            for (Iterator<Map.Entry> it = aList.iterator();it.hasNext();){
                Map.Entry ent = it.next();
                bw.write(ent.getKey()+": "+ent.getValue()+" errors: "+addrsC.get(ent.getKey()));
                bw.newLine();
            }*/
            /*bw.write("Rows:\n");
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
            bw.write("Bit error rate (addresses, excluding 0) : "+(((double)addrs.size()-1)/9395240960.0));
            bw.newLine();*/
            bw.write("Bit error rate (v1 minimum bits, excluding 0) : "+(x1/(9395240960.0*8)));
            bw.newLine();
            bw.write("Bit error rate (v2 minimum bits, excluding 0) : "+(x2/(9395240960.0*8)));
            bw.newLine();
            bw.write("Bit error rate (v1 maximum bits, excluding 0) : "+(y1/(9395240960.0*8)));
            bw.newLine();
            bw.write("Bit error rate (v2 maximum bits, excluding 0) : "+(y2/(9395240960.0*8)));
            bw.newLine();
            bw.write("Bit error rate (v1 sum bits, excluding 0) : "+(z1/(9395240960.0*8)));
            bw.newLine();
            bw.write("Bit error rate (v2 sum bits, excluding 0) : "+(z2/(9395240960.0*8)));
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
