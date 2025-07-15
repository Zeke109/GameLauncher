import First.Games;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import javax.sound.midi.SysexMessage;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
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
                    addGamesMenu(gameList, null);
                }

                data = new FileInputStream("src/Data/gameList.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(data));

                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null){

                    if (isFirstLine)
                        isFirstLine = false;

                    String[] part = line.split("; ");
                    ArrayList<String> parts = new ArrayList<String>();
                    for (int i = 0; i < part.length; i++){
                        parts.add(part[i]);
                    }

                    for (int i = 0; i < parts.size(); i++){
                        System.out.println(parts.get(i));
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


        mainMenu(gameList, null);

        data.close();

    }

    public static void mainMenu(ArrayList<Games> gameList, JFrame previous){
        JFrame uiWindow = new JFrame("Zeke's Game Launcher");
        uiWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        uiWindow.setSize(950, 540);

        JPanel homePanel = createHomePanel(gameList, uiWindow);

        JPanel gamesPanel = new JPanel();
        gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.Y_AXIS));
        
        gamesPanel.setBackground(new Color(000051204)); // Blue 

        gamesPanel.revalidate();
        gamesPanel.repaint();

        for (int i = 0; i < gameList.size(); i++){
            
            ImageIcon gameLogo = new ImageIcon(gameList.get(i).getIconPath());
            Image scaledImage = gameLogo.getImage().getScaledInstance(125, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JButton icon = new JButton(gameList.get(i).getName(), scaledIcon);

            icon.setPreferredSize(new DimensionUIResource(125, 125));
            icon.setMaximumSize(new DimensionUIResource(125, 125));

            icon.setHorizontalTextPosition(SwingConstants.CENTER);
            icon.setVerticalTextPosition(SwingConstants.BOTTOM);

            File file = new File(gameList.get(i).getFilePath());
            icon.addActionListener(e -> runProgram(file));

            gamesPanel.add(Box.createRigidArea(new DimensionUIResource(10, 20)));
            gamesPanel.add(icon);
            
        }
        

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
        catch (IllegalArgumentException f) {
            System.err.println("Invalid File Path for Execuatable");
        }
        catch (IOException e){
            System.err.println("Cannot find requested executable");
            return; 
        }
    }

    public static void addGamesMenu(ArrayList<Games> gameList, JFrame previous) {

        JFrame uiWindow = new JFrame("Zeke's Game Launcher");
        uiWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        uiWindow.setSize(950, 540);
        uiWindow.setBackground(Color.gray);

        JPanel homePanel = createHomePanel(gameList, uiWindow);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JLabel gameLabel1 = new JLabel("\nEnter the game's name: ");
        JTextField gameField1 = new JTextField(1); 
        gameField1.setPreferredSize(new Dimension(1500, 30));
        gameField1.setMaximumSize(new Dimension(1500, 30));
        gameField1.setAlignmentX(-100);

        JLabel gameLabel2 = new JLabel("\nEnter the game's path");
        JButton gpathButton = new JButton("Enter File Path"); 

        
        final String[] gamePath = new String[1];
        final String[] imagePath = new String[1];
        gamePath[0] = "0";
        imagePath[0] = "0";

        gpathButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int res = fc.showOpenDialog(uiWindow);
        
            if (res == JFileChooser.APPROVE_OPTION) {
                File selectFile = fc.getSelectedFile();
                gamePath[0] = selectFile.getAbsolutePath();
            }
        });

        JLabel gameLabel3 = new JLabel("\nEnter the path for the game icon");
        JButton ipathButton = new JButton("Enter File Path");
        
        ipathButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int res = fc.showOpenDialog(uiWindow);
        
            if (res == JFileChooser.APPROVE_OPTION) {
                File selectFile = fc.getSelectedFile();
                imagePath[0] = selectFile.getAbsolutePath();
            }
        });

        JLabel gameLabel4 = new JLabel("\nEnter how many hours you have ");
        JTextField gameField4 = new JTextField(1); 

        gameField4.setPreferredSize(new Dimension(60, 30));
        gameField4.setMaximumSize(new Dimension(60, 30));
        gameField4.setAlignmentX(-100);

        JButton submitButton = new JButton("\nAdd Game");

        submitButton.addActionListener(e -> {
            Games newGame = new Games();
            newGame.setName(gameField1.getText());
            gameField1.setText("");
            newGame.setFilePath(gamePath[0]);
            newGame.setIconPath(imagePath[0]);
            try{
                newGame.setGameTime(Integer.valueOf(gameField4.getText()));
            } 
            catch(NumberFormatException i){
                System.out.println("Invalid Hours Number. Try Again!");
            }
            gameField4.setText("");
            gameList.add(newGame);

            populateCSV(newGame);
        });

        inputPanel.add(gameLabel1);
        inputPanel.add(gameField1);
        inputPanel.add(gameLabel2);
        inputPanel.add(gpathButton);
        inputPanel.add(gameLabel3);
        inputPanel.add(ipathButton);
        inputPanel.add(gameLabel4);
        inputPanel.add(gameField4);

        inputPanel.add(Box.createVerticalStrut(5));
        inputPanel.add(submitButton);


        uiWindow.add(homePanel, BorderLayout.NORTH);
        uiWindow.add(inputPanel, BorderLayout.CENTER);
        uiWindow.setVisible(true);
    }

    public static JPanel createHomePanel(ArrayList<Games> gameList, JFrame previous){

        JPanel homePanel = new JPanel();

        homePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        homePanel.setBackground(Color.darkGray);

        homePanel.add(Box.createRigidArea(new DimensionUIResource(1, 20)));

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> mainMenu(gameList, previous));
        homeButton.addActionListener(e-> previous.dispose());

        JButton newGameButton = new JButton("Add New Games");
        newGameButton.addActionListener(e -> addGamesMenu(gameList, previous));
        newGameButton.addActionListener(e-> previous.dispose());

        JButton removeButton = new JButton("Remove Game");
        removeButton.addActionListener(e -> removeGameMenu(gameList, previous)); 
        removeButton.addActionListener(e -> previous.dispose());

        homePanel.add(homeButton);
        homePanel.add(newGameButton);
        homePanel.add(removeButton);

        return homePanel;
    }

    public static void cleaner(ArrayList<Games> gameList){

        BufferedWriter csvFile = null;
             
        try{
            csvFile = new BufferedWriter(new FileWriter("src/Data/gameList.csv"));
            csvFile.write("");
            csvFile = new BufferedWriter(new FileWriter("src/Data/gameList.csv", true));
    
            for (int i = 0; i < gameList.size(); i++){
                populateCSV(gameList.get(i));
            }
        }
        catch (IOException e){
            System.err.println("Failed to update CSV file. Additions or deletions may not have been properly saved");
            System.exit(9);
        }
    
    }

    public static void populateCSV(Games g){

        BufferedWriter w = null;

        try {
            w = new BufferedWriter(new FileWriter("src/Data/gameList.csv", true));
            w.write(g.getName() + "; " + g.getFilePath() + "; " + g.getIconPath() + "; " + g.getGameTime() + "\n");
            w.close();
        }
        catch (IOException e){
            System.err.println("File Write Error");
            System.exit(7);
        }
        
    }

    public static void removeLoop (ArrayList<Games> gameList, JPanel inputPanel){

        for (int j = 0; j < gameList.size(); j++){
    
            ImageIcon gameLogo = new ImageIcon(gameList.get(j).getIconPath());
            Image scaledImage = gameLogo.getImage().getScaledInstance(125, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JButton icon = new JButton(gameList.get(j).getName(), scaledIcon);
    
            icon.setPreferredSize(new DimensionUIResource(125, 125));
            icon.setMaximumSize(new DimensionUIResource(125, 125));
    
            icon.setHorizontalTextPosition(SwingConstants.CENTER);
            icon.setVerticalTextPosition(SwingConstants.BOTTOM);
    
            int index = j;
            icon.addActionListener(e -> 
            {
                gameList.remove(index);
                cleaner(gameList);
                icon.setVisible(false);
            });
            
            inputPanel.add(Box.createRigidArea(new DimensionUIResource(10, 20)));
            inputPanel.add(icon);
        }

        
    
    }

    public static void removeGameMenu (ArrayList<Games> gameList, JFrame previous){

        JFrame uiWindow = new JFrame("Zeke's Game Launcher");
        uiWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        uiWindow.setSize(950, 540);


        JPanel homePanel = createHomePanel(gameList, uiWindow);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(new Color(234057085));

        inputPanel.revalidate();

        removeLoop(gameList, inputPanel);

        inputPanel.revalidate();

        uiWindow.add(homePanel, BorderLayout.NORTH);
        uiWindow.add(inputPanel, BorderLayout.CENTER);
        uiWindow.setVisible(true);
    }

}




