import First.Games;
import java.io.*;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.Image;
import java.awt.Desktop;


public class App {
    public static void main(String[] args) throws Exception {

        FileInputStream data = null; 
        try {
            data = new FileInputStream("src/Data/gameList.csv");
        }
        catch (IOException e){
            System.err.println("File Read Error");
            System.exit(1);
        }
        

        //while (true) {

            //Games game = new Games("temp", "temp", "temp", 0);
            Games newGame = new Games();
            newGame.setFilePath("C:/Users/Zach/AppData/Local/Discord/app-1.0.9199/Discord.exe");

            newGame.setGameTime(0);
            
            JFrame uiWindow = new JFrame("Launcher");
            uiWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            uiWindow.setSize(950, 540);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            

            //for (int i = 0; i < 100; i++){
                
                ImageIcon gameLogo = new ImageIcon("src/images/game.png");
                Image scaledImage = gameLogo.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                ImageIcon gameLogo2 = new ImageIcon(scaledImage);
                JButton temp = new JButton(gameLogo2);
                
                
                temp.setPreferredSize(new DimensionUIResource(100, 100));
                temp.setMaximumSize(new DimensionUIResource(100, 100));
                File file = new File(newGame.getFilePath());
                
                

                temp.addActionListener(e -> runProgram(file));

                panel.add(Box.createRigidArea(new DimensionUIResource(5, 20)));
                panel.add(temp);
            //}
            
            JScrollPane scroll = new JScrollPane(panel);
            scroll.getVerticalScrollBar().setUnitIncrement(20);

            uiWindow.add(scroll);
            uiWindow.setVisible(true);

            data.close();
    }
            

        //}

        

    public static void runProgram (File file){
        Desktop dt = Desktop.getDesktop();
        try{
            dt.open(file);
        }
        catch (IOException e){
            System.err.println("Cannot find requested executable");
            System.exit(4);
        }
    }
}
