import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class porthunter extends JFrame {
    DefaultTableModel model;
    JButton btntara;
    JTable table;


    public porthunter(){
        String[] kolonlar = {"PID", "Program Adı", "Protokol", "Adres/Port", "Durum"};
        model = new DefaultTableModel(kolonlar,0);
        btntara = new JButton("taramayı başlat");
        setSize(800,600);

        setTitle("first try");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = getSize().width;
        int h = getSize().height;
        int x  = (dim.width - w)/2;
        int y = (dim.height - h)/2;
        setLocation(x,y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        table = new JTable(model);
        JScrollPane scroller = new JScrollPane(table);
        add(scroller, BorderLayout.CENTER);
        add(btntara, BorderLayout.NORTH);
        btntara.addActionListener(e -> scanner());
        setVisible(true);
    }
    public static void main(String[] args){
        porthunter porthunt = new porthunter();

    }
    public void scanner(){
        java.util.List<String> riskliportlar = java.util.Arrays.asList("21", "23", "445", "139", "3389", "4444", "6667");
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
                    String rawadress = pieces[1];
                    int splitindex = rawadress.lastIndexOf(":");
                    String ip = rawadress.substring(0,splitindex);
                    String port = rawadress.substring(splitindex+1);
                    String tehlike = "normal";


                    while ((line1 = bufferedReader1.readLine()) != null){
                        if(line1.isEmpty() || line1.startsWith("Image Name") || line1.startsWith("=")){
                            continue;
                        }
                        String[] pieces1 = line1.trim().split("\\s+");
                        String exe = pieces1[0];
                        if (riskliportlar.contains(port)){
                            tehlike = "tehlikeli portlar var ";
                        }
                        else if (exe.startsWith("nc") || exe.contains("ncat") || exe.contains("powershell")) {
                            tehlike = "💀 ARKA KAPI ŞÜPHESİ!";
                        }


                        model.addRow(new Object[]{pid, exe, pieces[0],port,pieces[3]+ " (" + ip + ")", tehlike});

                    }

                }   }
            bufferedReader.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
