
package views.games;

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
import javax.swing.JOptionPane;
import models.games.Game;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class SearchTableViewController implements Initializable {
    @FXML private TableView<Game> gamesTable;
    @FXML private TableColumn<Game, String> gameNameColumn;
    @FXML private TableColumn<Game, String> gameLocationColumn;
    @FXML private TableColumn<Game, String> gameDepartmentColumn;
    @FXML private TableColumn<Game, String> gameIDColumn;
    @FXML private TextField searchField;
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Pane root;
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    dbStringPath dbsp = new dbStringPath();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssC.cssPath());
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));
        gameLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        gameDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("Department"));
        gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("gameNumber"));
        
        try {
            gamesTable.getItems().addAll(getGames());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        searchField.requestFocus();
        
    }    
    
    public void searchRecord(KeyEvent ke) throws SQLException {
        
       
        FilteredList<Game> filterData = new FilteredList<>(getGames(), p -> true);
        searchField.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
            filterData.setPredicate(pers -> {
                if (newvalue == null || newvalue.isEmpty()) {
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                //System.out.println(typedText);
                if (pers.getGameName().toLowerCase().contains(typedText)) {

                    return true;
                }
                if (pers.getDepartment().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getLocation().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getGameNumber().toLowerCase().contains(typedText)) {

                    return true;
                }

                return false;
            });
            SortedList<Game> sortedList = new SortedList<>(filterData);
            sortedList.comparatorProperty().bind(gamesTable.comparatorProperty());
            gamesTable.setItems(sortedList);
                       
            
        });

    }
    
    
    public void TableClicked(MouseEvent me) throws IOException {
         if (me.getClickCount() == 2) {
            selectButton.fire();
         }
    }
    

    public void selectButtonPushed(ActionEvent event) throws IOException {
        if (gamesTable.getItems().size() == 1) {
            gamesTable.getSelectionModel().select(0);
        }
        TablePosition pos = gamesTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        String st = gamesTable.getItems().get(row).getGameNumber();
        sc.changePopUp1(event, st);
    }
    
    public ObservableList<Game> getGames() throws SQLException {
    ObservableList<Game> games = FXCollections.observableArrayList();

    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "GamesDB.accdb");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT Games.[Game Name], Games.[Game Number], Games.Location, Games.Department, Games.[Game ID] FROM Games ORDER BY Games.[Game ID];");
            while (rs.next()) {
                Game newGame = new Game(rs.getString("Game Name"), null, rs.getString("Game Number"), rs.getString("Location"), rs.getString("Department"));
                games.add(newGame);
            }
    }
    catch (Exception e) {
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
        sc.changePopUp(event, "", "");
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: JOptionPane.showMessageDialog(null, "F1"); break;
                    case F2: JOptionPane.showMessageDialog(null, "F2"); break;
                    case F3: JOptionPane.showMessageDialog(null, "F3"); break;
                    case F4: break;
                    case F5: JOptionPane.showMessageDialog(null, "F5"); break;
                    case F6: JOptionPane.showMessageDialog(null, "F6"); break;
                    case F7: JOptionPane.showMessageDialog(null, "F7"); break;
                    case F8: JOptionPane.showMessageDialog(null, "F8"); break;
                    case F9: break;
                    case F10: JOptionPane.showMessageDialog(null, "F10"); break;
                    case F11: JOptionPane.showMessageDialog(null, "F11"); break;
                    case F12: JOptionPane.showMessageDialog(null, "F12"); break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: selectButton.fire(); break;
                default:
                    break;
                }
    }
    
}
