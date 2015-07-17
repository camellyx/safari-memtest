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
            Map<Long, ArrayList<Integer>> v1 = new HashMap<Long, ArrayList<Integer>>();
            Map<Long, ArrayList<Double>> v2 = new HashMap<Long, ArrayList<Double>>();
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
            for (File f:files){
                Map<Long, ArrayList<Integer>> failed = new HashMap<Long, ArrayList<Integer>>();
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
                            failed.get(addr).add(bits);
                        }else{
                            failed.put(addr, new ArrayList<Integer>());
                            failed.get(addr).add(bits);
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
                List failedL = new LinkedList(failed.entrySet());
                for (Iterator<Map.Entry> it = failedL.iterator();it.hasNext();){
                    Map.Entry aList = it.next();
                    int minBits = ((ArrayList<Integer>)aList.getValue()).get(0);
                    int sum = 0;
                    for (int bits2:(ArrayList<Integer>)aList.getValue()){
                        if (bits2<minBits){
                            minBits = bits2;
                        }
                        sum+=bits2;
                    }
                    double averageBits = (double)sum/(double)((ArrayList<Integer>)aList.getValue()).size();
                    if (v1.containsKey(aList.getKey())){
                        v1.get(aList.getKey()).add(minBits);
                    }else{
                        v1.put((Long)aList.getKey(), new ArrayList<Integer>());
                        v1.get(aList.getKey()).add(minBits);
                    }
                    if (v2.containsKey(aList.getKey())){
                        v2.get(aList.getKey()).add(averageBits);
                    }else{
                        v2.put((Long)aList.getKey(), new ArrayList<Double>());
                        v2.get(aList.getKey()).add(averageBits);
                    }
                }
                testNo++;
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
            List v1L = new LinkedList(v1.entrySet());
            Collections.sort(v1L, comx);
            List v2L = new LinkedList(v2.entrySet());
            Collections.sort(v2L, comx);
            int tv1Max = 0;
            double tv2Max = 0;
            int tv1Min = 0;
            double tv2Min = 0;
            int tv1Sum = 0;
            double tv2Sum = 0;
            /*bw.write("Addresses:\n");
            for (Iterator<Map.Entry> it = aList.iterator();it.hasNext();){
                Map.Entry ent = it.next();
                bw.write(ent.getKey()+": "+ent.getValue()+" errors: "+addrsC.get(ent.getKey()));
                bw.newLine();
            }*/
            Iterator<Map.Entry> v1i = v1L.iterator();
            Iterator<Map.Entry> v2i = v2L.iterator();
            while (v1i.hasNext()){
                int v1Max = 0;
                double v2Max = 0;
                int v1Min = 600;
                double v2Min = 600;
                int v1Sum = 0;
                double v2Sum = 0;
                Map.Entry v1e = v1i.next();
                Map.Entry v2e = v2i.next();
                if ((long)v1e.getKey()!=0){
                    for (int v1Bits:(ArrayList<Integer>)(v1e.getValue())){
                        if (v1Max<v1Bits)
                            v1Max=v1Bits;
                        if (v1Min>v1Bits)
                            v1Min=v1Bits;
                        v1Sum+=v1Bits;
                    }
                }
                if ((long)v2e.getKey()!=0){
                    for (double v2Bits:(ArrayList<Double>)(v2e.getValue())){
                        if (v2Max<v2Bits)
                            v2Max=v2Bits;
                        if (v2Min>v2Bits)
                            v2Min=v2Bits;
                        v2Sum+=v2Bits;
                    }
                }
                if (v2Sum>512)
                    v2Sum=512;
                if (v1Sum>512)
                    v1Sum=512;
                tv1Max+=v1Max;
                tv2Max+=v2Max;
                tv1Min+=v1Min;
                tv2Min+=v2Min;
                tv1Sum+=v1Sum;
                tv2Sum+=v2Sum;
            }
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
            bw.write("Bit error rate (v1 minimum bits, excluding 0) : "+(tv1Min/(9395240960.0*8)));
            bw.newLine();
            bw.write("Bit error rate (v2 minimum bits, excluding 0) : "+(tv2Min/(9395240960.0*8)));
            bw.newLine();
            bw.write("Bit error rate (v1 maximum bits, excluding 0) : "+(tv1Max/(9395240960.0*8)));
            bw.newLine();
            bw.write("Bit error rate (v2 maximum bits, excluding 0) : "+(tv2Max/(9395240960.0*8)));
            bw.newLine();
            bw.write("Bit error rate (v1 sum bits, excluding 0) : "+(tv1Sum/(9395240960.0*8)));
            bw.newLine();
            bw.write("Bit error rate (v2 sum bits, excluding 0) : "+(tv2Sum/(9395240960.0*8)));
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
