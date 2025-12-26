
package views.parties;

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
import models.birthdays.BdayParties;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class SearchTableBdayController implements Initializable {
    @FXML private TableView<BdayParties> gamesTable;
    @FXML private TableColumn<BdayParties, String> gameNameColumn;
    @FXML private TableColumn<BdayParties, String> gameLocationColumn;
    @FXML private TableColumn<BdayParties, String> gameDepartmentColumn;
    @FXML private TableColumn<BdayParties, String> gameIDColumn;
    @FXML private TableColumn<BdayParties, String> cID2;
    @FXML private TableColumn<BdayParties, String> gamePartyType;
    @FXML private TextField searchField;
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Pane root;
    
    public static SceneChanger_Main sc;
    public static String cssPath;
    public static dbStringPath dbsp;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    BdayParties newGame = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssPath);
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("Namef"));
        gameLocationColumn.setCellValueFactory(new PropertyValueFactory<>("Namel"));
        gameDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        gamePartyType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        //cID2.setCellValueFactory(new PropertyValueFactory<>("IDD"));
        
        try {
            gamesTable.getItems().addAll(getGames());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        searchField.requestFocus();
        
    }    
    
    public void searchRecord(KeyEvent ke) throws SQLException {
        
       
        FilteredList<BdayParties> filterData = new FilteredList<>(getGames(), p -> true);
        searchField.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
            filterData.setPredicate(pers -> {
                if (newvalue == null || newvalue.isEmpty()) {
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                if (pers.getNamef().toLowerCase().contains(typedText)) {

                    return true;
                }
                if (pers.getNamel().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getAddress().toLowerCase().contains(typedText)) {
                    return true;
                }
                //if (pers.getEname().toLowerCase().contains(typedText)) {
                //    return true;
                //}
                //System.out.println(pers.getType());
                if (pers.getType().toLowerCase().contains(typedText)) {
                    return true;
                }

                return false;
            });
            SortedList<BdayParties> sortedList = new SortedList<>(filterData);
            sortedList.comparatorProperty().bind(gamesTable.comparatorProperty());
            gamesTable.setItems(sortedList);
                       
            
        });

    }

    public void selectButtonPushed(ActionEvent event) throws IOException {
        TablePosition pos = gamesTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        String st = gamesTable.getItems().get(row).getID() + "/" + gamesTable.getItems().get(row).getIDD();
        sc.changePopUp1(event, String.valueOf(st));
    }
    
    public void TableClicked(MouseEvent me) throws IOException {
         if (me.getClickCount() == 2) {
            selectButton.fire();
         }
    }
    
    
    public ObservableList<BdayParties> getGames() throws SQLException {
    ObservableList<BdayParties> games = FXCollections.observableArrayList();

    try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "HistoricBDAY.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT Bdayprty.[Contact First], Bdayprty.[Contact Last], Bdayprty.Address , Bdayprty.Phone, Bdayprty.[Party Type], Bdayprty.[Bday ID], Bdayprty.[BDay ID2]  FROM Bdayprty;");
            while (rs.next()) {
                //System.out.println("here");
            newGame = new BdayParties("", rs.getString("Contact First"), rs.getString("Contact Last"),  rs.getString("Address"), "", "", "", rs.getString("Phone"), rs.getString("Party Type"), "", "", "", "", "", "", "", "", "", 0, 0, rs.getInt("Bday ID"), rs.getInt("BDay ID2"));
                games.add(newGame);
            }
            
    }
    catch (SQLException e) {
        System.err.println(e.getMessage());
    }
    finally {
    if (st != null);
        st.close();
    if (rs != null);
        rs.close();
    if (conn != null);
    try {
        conn.close();        
    } catch(SQLException e) {}    
}


    try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "BdayParties_access.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT Bdayprty.[Contact First], Bdayprty.[Contact Last], Bdayprty.Address , Bdayprty.Phone, Bdayprty.[Party Type], Bdayprty.[Bday ID], Bdayprty.[BDay ID2] FROM Bdayprty;");
            while (rs.next()) {
            newGame = new BdayParties("", rs.getString("Contact First"), rs.getString("Contact Last"),  rs.getString("Address"), "", "", "", rs.getString("Phone"), rs.getString("Party Type"), "", "", "", "", "", "", "", "", "", 0, 0, rs.getInt("Bday ID"), rs.getInt("BDay ID2"));
                games.add(newGame);
            }
            
    }
    catch (SQLException e) {
        System.err.println(e.getMessage());
    }
    finally {
    if (conn != null);
    try {
        conn.close();        
    } catch(SQLException e) {}    
    if (st != null);
        st.close();
    if (rs != null);
        rs.close();
}



    return games;
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
                    case F10:break;
                    case F11:break;
                    case F12:break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: selectButton.fire(); break;
                default:
                    break;
                }
    }
    
}
