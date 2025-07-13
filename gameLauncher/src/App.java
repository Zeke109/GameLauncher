import First.Games;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;



public class App {
    public static void main(String[] args) throws Exception {

        FileInputStream data = null; 
        ArrayList<Games> gameList = new ArrayList<Games>();

        while (true){
            try {
                File file = new File("src/Data/gameList.csv"); 
                if (file.createNewFile())

                if (file.length() == 0){
                    addGames();
                }

                data = new FileInputStream("src/Data/gameList.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(data));

                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null){

                    if (isFirstLine)
                        isFirstLine = false;

                    String[] part = line.split(",");
                    ArrayList<String> parts = new ArrayList<String>();
                    for (int i = 0; i < part.length; i++){
                        parts.add(part[i]);
                    }

                    if (parts.size() % 4 == 0){
                        while (parts.size() > 0){
                            Games newGame = new Games();
                            newGame.setName(parts.get(0));
                            newGame.setFilePath(parts.get(1));
                            newGame.setIconPath(parts.get(2));
                            try {
                                newGame.setGameTime(Integer.valueOf(parts.get(3)));
                            } 
                            catch (NumberFormatException e)
                            {
                                System.err.println("Invalid gameTime format");
                                System.exit(4);
                            }

                            gameList.add(newGame);
                            for (int i = 0; i < 4; i++){
                                parts.remove(0);
                            }
                        }
                    }
                    else{
                        System.err.println("Invalid csv format");
                        System.exit(4);
                    }
                }
                reader.close();
                break;
            }

            catch (FileNotFoundException e){
                System.err.println("File not found. Creating new Gamelist file");
                File file = new File("src/Data/gameList.csv");
                if (!(file.createNewFile())){
                    System.err.println("File Creation Unsuccessfull");
                    System.exit(4);
                }
            }

            catch (IOException e){
                
                System.err.println("File Read Error");
                System.exit(1);
            }
        }

        mainMenu(gameList);
        

        data.close();
    }

    public static void mainMenu(ArrayList<Games> gameList){
        JFrame uiWindow = new JFrame("Zeke's Game Launcher");
        uiWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        uiWindow.setSize(950, 540);

        JPanel homePanel = new JPanel();
        homePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        homePanel.setBackground(Color.darkGray);

        homePanel.add(Box.createRigidArea(new DimensionUIResource(10, 20)));

        homePanel.add(new JButton("Add New Games"));


        JPanel gamesPanel = new JPanel();
        gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.Y_AXIS));
        
        gamesPanel.setBackground(new Color(000051204)); // Blue 


        for (int i = 0; i < gameList.size(); i++){
            
            ImageIcon gameLogo = new ImageIcon(gameList.get(i).getIconPath());
            Image scaledImage = gameLogo.getImage().getScaledInstance(125, 125, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JButton icon = new JButton(gameList.get(i).getName(), scaledIcon);

            System.out.println("Path: " + gameList.get(i).getIconPath());
            System.out.println("File exists: " + new File(gameList.get(i).getIconPath()).exists());

            icon.setPreferredSize(new DimensionUIResource(125, 125));
            icon.setMaximumSize(new DimensionUIResource(125, 125));

            icon.setHorizontalTextPosition(SwingConstants.CENTER);
            icon.setVerticalTextPosition(SwingConstants.BOTTOM);

            File file = new File(gameList.get(i).getFilePath());
            icon.addActionListener(e -> runProgram(file));

            gamesPanel.add(Box.createRigidArea(new DimensionUIResource(10, 20)));
            gamesPanel.add(icon);
            
        }
        
        gamesPanel.revalidate();
        gamesPanel.repaint();

        JScrollPane scroll = new JScrollPane(gamesPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        uiWindow.setLayout(new BorderLayout());
        uiWindow.add(homePanel, BorderLayout.NORTH);
        uiWindow.add(scroll, BorderLayout.CENTER);
        uiWindow.setVisible(true);
    }
            

    public static void runProgram (File file){
        Desktop dt = Desktop.getDesktop();
        try{
            dt.open(file);
        }
        catch (IOException e){
            System.err.println("Cannot find requested executable");
            return; 
        }
    }

    public static void addGames() {
        System.out.println("Success");
    }
}
