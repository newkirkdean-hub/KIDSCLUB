package CounterFX;

import dbpathnames.dbStringPath;
import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;

/**
 *
 * @author Dean
 */
public class CounterFX extends Application {
    private static final dbStringPath DBSP = new dbStringPath();
    private static final settingsFXML SETTINGS = new settingsFXML();
    
    
    public static void main (String[] args){
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        DBSP.setName();
        File file = new File(DBSP.pathNameLocal + "configCounterFXML.properties");
        if (file.exists() && file.canRead()) {
            switch (SETTINGS.getCounterSettings("stage")) {
                case "1":
                    new SceneChanger_Main().MainStage(stage, "/views/Bridge.fxml", "Bridge");        
                    break;
                case "2":
                    new SceneChanger_Main().MainStage(stage, "/views/Cafe.fxml", "Cafe");        
                    break;
                case "3":
                    new SceneChanger_Main().MainStage(stage, "/views/Counter.fxml", "Counter");        
                    break;
                case "4":
                    new SceneChanger_Main().MainStage(stage, "/views/Main.fxml", "Pojos Main");        
                    break;
                case "6":
                    new SceneChanger_Main().MainStage(stage, "/views/CounterChooser.fxml", "Counter Chooser");        
                    break;
                case "9":
                    new SceneChanger_Main().MainStage(stage, "/views/tvScreen.fxml", "Tv Screen");        
                    break;
            } 
                    
                    
            
        } else {
            SETTINGS.setCounterSettings();
            SETTINGS.setSettings(2);
            SETTINGS.setProp("Css", "99", 2);
            new SceneChanger_Main().MainStage(stage, "/views/CounterChooser.fxml", "Counter Chooser");        

       }
    }



}
