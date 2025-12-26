
package views.toys;

import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import dbpathnames.dbStringPath;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import models.toys.ToysDB;
import models.toys.ToysLabels;
import reports.toys.XLSLabelPrint;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class ToysLabelsTableViewController implements Initializable {
    @FXML private TableView<ToysLabels> gamesTable;
    @FXML private TableColumn<ToysLabels, String> gameNameColumn;
    @FXML private TableColumn<ToysLabels, String> gameLocationColumn;
    @FXML private TableColumn<ToysLabels, String> gameTicketsColumn;
    //@FXML private TableColumn<Toys, String> gameIDColumn;
    @FXML private TextField searchField;
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Pane root;
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    dbStringPath dbsp = new dbStringPath();
    ToysDB TDB = new ToysDB();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssC.cssPath());
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        gameLocationColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));
        gameTicketsColumn.setCellValueFactory(new PropertyValueFactory<>("Tickets"));
        try {
            gamesTable.getItems().addAll(TDB.getLabels());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        cancelButton.requestFocus();
        
    }    
    
        

    public void selectButtonPushed(ActionEvent event) throws IOException {
        PrintLabels();
        
        
        
        //TablePosition pos = gamesTable.getSelectionModel().getSelectedCells().get(0);
        //int row = pos.getRow();
        //String st = gamesTable.getItems().get(row).getNumber();
        //sc.changePopUp1(event, st);
    }
    
    
    
    public void PrintLabels() {
            try {
                //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "HEY", "THIE IS WHERE THE COUNT LIST WILL RUN");
                XLSLabelPrint XRW = new XLSLabelPrint();  
                XRW.LabelPrintListReport();
            } catch (FileNotFoundException | SQLException ex) {
                System.out.println(ex);
            } catch (IOException ex) {
                System.out.println(ex);
            }
    }
    
    
    
    
    
    public void EmptyTable() {
        gamesTable.getItems().clear();
        TDB.EmptyTable();
        try {
            gamesTable.getItems().addAll(TDB.getLabels());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
    }
    
    
    
    
    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
            sc.changePopUp(event, "", "");
    }
    
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: break;
                    case F2: break;
                    case F3: break;
                    case F4: break;
                    case F5: break;
                    case F6: break;
                    case F7: break;
                    case F8: break;
                    case F9: break;
                    case F10: break;
                    case SPACE: selectButton.fire(); break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: selectButton.fire(); break;
                default:
                    break;
                }
    }
    
}
