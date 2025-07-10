package First;

public class Games {


    private String name;
    private String filePath;
    private String iconPath;
    private int gameTime;


    public Games(String a, String b, String c, int x){
        this.name = a;
        this.filePath = b;
        this.iconPath = c;
        this.gameTime = x;
    }

    public Games(int x){
        this.gameTime = x;
    }


    public Games(){

    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }
    
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getIconPath() {
        return this.iconPath;
    }
    
    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public int getGameTime() {
        return this.gameTime;
    }

}
