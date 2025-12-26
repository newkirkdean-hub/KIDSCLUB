
package views.timeclock;

import Css.cssChanger;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;
import models.timeclock.EmpFileFX;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class EmpDetailPopUpSearchController implements Initializable {
    @FXML private TableView<EmpFileFX> gamesTable;
    @FXML private TableColumn<EmpFileFX, String> gameNameColumn;
    @FXML private TableColumn<EmpFileFX, String> gameLocationColumn;
    @FXML private TableColumn<EmpFileFX, String> gameDepartmentColumn;
    @FXML private TableColumn<EmpFileFX, String> gameIDColumn;
    @FXML private TextField searchField;
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Pane root;
    Stage stageV;
    SceneChanger_Main sc = new SceneChanger_Main();
    
    cssChanger cssC = new cssChanger();
    dbStringPath dbsp = new dbStringPath();
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssC.cssPath());
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("FName"));
        gameLocationColumn.setCellValueFactory(new PropertyValueFactory<>("LName"));
        //gameDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        //gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("ENumber"));
        
        searchField.requestFocus();
        Platform.runLater(()->stageV = (Stage) cancelButton.getScene().getWindow());
        Platform.runLater(()->System.out.println("HERE IS THE TILE OF THE PAGE " + stageV.getTitle()));
        Platform.runLater(()->GetNames());

        
        
    }    
    
    
    public void TableClicked(MouseEvent me) throws IOException {
         if (me.getClickCount() == 2) {
            selectButton.fire();
         }
    }
    
    
    private void GetNames() {
        try {
            gamesTable.getItems().addAll(getGames());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    public void searchRecord(KeyEvent ke) throws SQLException {
        
       
//        FilteredList<Member> filterData = new FilteredList<>(getGames(), p -> true);
        FilteredList<EmpFileFX> filterData = new FilteredList<>(gamesTable.getItems(), p -> true);
        searchField.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
            filterData.setPredicate(pers -> {
                if (newvalue == null || newvalue.isEmpty()) {
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                //System.out.println(typedText);
                if (pers.getFName().toLowerCase().contains(typedText)) {

                    return true;
                }
                if (pers.getLName().toLowerCase().contains(typedText)) {
                    return true;
                }
                //if (pers.getAddress().toLowerCase().contains(typedText)) {
                //    return true;
                //}
                if (pers.getENumber().toLowerCase().contains(typedText)) {

                    return true;
                }

                return false;
            });
            SortedList<EmpFileFX> sortedList = new SortedList<>(filterData);
            sortedList.comparatorProperty().bind(gamesTable.comparatorProperty());
            gamesTable.setItems(sortedList);
                       
            
        });

    }

    public void selectButtonPushed(ActionEvent event) throws IOException {
        TablePosition pos = gamesTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        String st = gamesTable.getItems().get(row).getAddress();
        System.out.println("HERE IS ST BEFORE SENDING BACK TO OTHER SCREEN " + st);
        sc.changePopUp1(event, st);
    }
    
    
    
    
    
    public ObservableList<EmpFileFX> getGames() throws SQLException {
    ObservableList<EmpFileFX> games = FXCollections.observableArrayList();
    EmpFileFX newGame = null;
    String selectQuery = null;
    if (stageV.getTitle().equals("List of Corps") ) {
        selectQuery = "SELECT Employee.*, Employee.[Active]\n"
                    + "FROM Employee\n"
                    + "WHERE (((Employee.[Active])>0) AND ((Employee.Location)='Location - Corporate')) ORDER BY Employee.[Employee Number];";
    } else {
        selectQuery = "SELECT Employee.*, Employee.[Active]\n"
                    + "FROM Employee\n"
                    + "WHERE (((Employee.[Active])>0)) ORDER BY Employee.[Employee Number];";        
    }
    try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "Emps2.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            System.out.println(selectQuery);
            //rs = st.executeQuery("SELECT Employee.[Employee Number], Employee.[First Name], Employee.[Last Name], Employee.[Street Address] FROM Employee ORDER BY Employee.[Last Name];");
            //rs = st.executeQuery("SELECT Employee.*, Employee.[Active]\n"
            //        + "FROM Employee\n"
            //        + "WHERE (((Employee.[Active])>0)) ORDER BY Employee.[Employee Number];");
            rs = st.executeQuery(selectQuery);

            while (rs.next()) {
                newGame = new EmpFileFX( rs.getString("Employes ID"), rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Employee Number"));
                games.add(newGame);
                System.out.println(rs.getString("Employes ID") + " " + rs.getString("Employee Number"));
            }
    }
    catch (SQLException e) {
        System.err.println(e.getMessage());
    }
    finally {
        if (conn != null);
        conn.close();
    if (st != null);
        st.close();
    if (rs != null);
        rs.close();
}

    return games;
}   

    public void cancelButtonPushed(ActionEvent event) throws IOException {
        if (searchField.getText().trim().length()>0) {
            clearSearchField();
        } else {
            sc.changePopUp(event, "", "");
        }
    }
    
    private void clearSearchField() {
        System.out.println("back space fired");
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
                    case F11: break;
                    case BACK_SPACE: clearSearchField(); break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: selectButton.fire(); break;
                default:
                    break;
                }
    }
    
}
