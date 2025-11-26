import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;


public class rawdata {
    public static void main(String[] args){
        try {
            String[]  commends = {"cmd","/c","netstat -ano"};
            ProcessBuilder builder = new ProcessBuilder(commends);
            builder.directory(new File("C://"));
            builder.redirectErrorStream();

            Process process = builder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                if (line.contains("LISTENING")){
                    String[] pieces = line.trim().split("\\s+");
                    String pid = pieces[pieces.length-1];
                    String[] tasklist = {"cmd","/c","tasklist /fi \"PID eq "+pid+"\""};
                    ProcessBuilder builder1 = new ProcessBuilder(tasklist);
                    builder1.directory(new File("C://"));
                    builder1.redirectErrorStream();
                    Process process1 = builder1.start();
                    BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(process1.getInputStream()));
                    String line1 = null;
                    while ((line1 = bufferedReader1.readLine()) != null){
                        if(line1.isEmpty() || line1.startsWith("Image Name") || line1.startsWith("=")){
                            continue;
                        }
                       String[] pieces1 = line1.trim().split("\\s+");
                       String exe = pieces1[0];
                       System.out.println("Port:  " +pieces[1]+ "  <---> Program:  "+exe+ " <---> PID:  " +pieces1[1]);

                    }

            }   }
            bufferedReader.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
