
package views.cafe;

import views.toys.*;
import Css.cssChanger;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import models.cafe.CToys;
import models.cafe.CToysDB;
import models.toys.Toys;
import models.toys.ToysDB;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class CToysSearchTableViewController implements Initializable {
    @FXML private TableView<CToys> gamesTable;
    @FXML private TableColumn<CToys, String> gameNameColumn;
    @FXML private TableColumn<CToys, String> gameLocationColumn;
    @FXML private TableColumn<CToys, String> gameTicketsColumn;
    @FXML private TableColumn<CToys, String> gameIDColumn;
    @FXML private TextField searchField;
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Pane root;
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    dbStringPath dbsp = new dbStringPath();
    CToysDB CTDB = new CToysDB();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssC.cssPath());
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));
        gameLocationColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        gameTicketsColumn.setCellValueFactory(new PropertyValueFactory<>("StrTickets"));
        gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("Vendor"));
        try {
            gamesTable.getItems().addAll(CTDB.GetToys());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        searchField.requestFocus();
        
    }    
    
        
    
    
    
    
    public void searchRecord(KeyEvent ke) throws SQLException {
        
       
//        FilteredList<Member> filterData = new FilteredList<>(getGames(), p -> true);
        FilteredList<CToys> filterData = new FilteredList<>(gamesTable.getItems(), p -> true);
        searchField.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
            filterData.setPredicate(pers -> {
                if (newvalue == null || newvalue.isEmpty()) {
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                //System.out.println(pers.getVendor());
                if (pers.getNumber().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getName().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getStrTickets().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getVendor().toLowerCase().contains(typedText)) {
                    return true;
                }

                return false;
            });
            SortedList<CToys> sortedList = new SortedList<>(filterData);
            //sortedList.comparatorProperty().bind(gamesTable.comparatorProperty());
            gamesTable.setItems(sortedList);
                       
            
        });

    }

    public void selectButtonPushed(ActionEvent event) throws IOException {
        TablePosition pos = gamesTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        String st = gamesTable.getItems().get(row).getNumber();
        sc.changePopUp1(event, st);
    }
    
    
    public void TableClicked(MouseEvent me) throws IOException {
         if (me.getClickCount() == 2) {
            selectButton.fire();
         }
    }
    
    
    

    
    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        if (searchField.getText().trim().length()>0) {
            clearSearchField();
        } else {
            sc.changePopUp(event, "", "");
        }
    }
    
    private void clearSearchField() {
        searchField.clear();
        searchField.requestFocus();
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
