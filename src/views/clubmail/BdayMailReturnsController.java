
package views.clubmail;

import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import messageBox.messageBox;
import models.club.DB;
import models.club.Member;
import models.club.Memtick;
//import clockoutatnight.employeeFX;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class BdayMailReturnsController implements Initializable {
    @FXML private TableView<Member> gamesTable;
    @FXML private TableColumn<Member, String> gameNameColumn;
    @FXML private TableColumn<Member, String> gameLocationColumn;
    @FXML private TableColumn<Member, String> gameDepartmentColumn;
    @FXML private TableColumn<Member, String> gameIDColumn;
    @FXML private CheckBox newMemberCheckBox;
    @FXML private CheckBox bdayCardCheckBox;
    @FXML private TextField searchField;
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Pane root;
    
    public static SceneChanger_Main sc;
    public static DB db;
    public static dbStringPath dbsp;
    public static String cssPath;
    
    private static Member m;
    private static Connection conn = null, connI = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    private static Double newBalance = 0.0;
    boolean selectionMade;
    public static String en;
    Memtick mt = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssPath);
        setKeyCodes();
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("NameF"));
        gameLocationColumn.setCellValueFactory(new PropertyValueFactory<>("NameL"));
        gameDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("CCN"));
        getData();
        Platform.runLater(()->searchField.requestFocus());
        
    }    
    
    
    private void getData() {
        try {
            gamesTable.getItems().removeAll();
            //gamesTable.getItems().clear();
            gamesTable.getItems().addAll(getGames());
            Platform.runLater(()->searchField.requestFocus());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    
    
    public void searchRecord(KeyEvent ke) throws SQLException {
        
       
//        FilteredList<Member> filterData = new FilteredList<>(getGames(), p -> true);
        FilteredList<Member> filterData = new FilteredList<>(gamesTable.getItems(), p -> true);
        searchField.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
            filterData.setPredicate(pers -> {
                if (newvalue == null || newvalue.isEmpty()) {
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                //System.out.println(typedText);
                if (pers.getNameF().toLowerCase().contains(typedText)) {

                    return true;
                }
                if (pers.getNameL().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getAddress().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getCCN().toLowerCase().contains(typedText)) {

                    return true;
                }

                return false;
            });
            SortedList<Member> sortedList = new SortedList<>(filterData);
            sortedList.comparatorProperty().bind(gamesTable.comparatorProperty());
            gamesTable.setItems(sortedList);
                       
            
        });

    }

    public void selectButtonPushed(ActionEvent event) throws IOException {
        String chkBox = "";
        boolean activeMember = true;
        int whichOne = 0;
        TablePosition pos = gamesTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        String stt = gamesTable.getItems().get(row).getCCN();
        m = gamesTable.getSelectionModel().getSelectedItem();
        
        if (!bdayCardCheckBox.isSelected() && !newMemberCheckBox.isSelected()) {
            new messageBox().showAlert(Alert.AlertType.INFORMATION, null, "Please Select", "Please select either the New Member CheckBox or the Birthday Card CheckBox");
            return;
        } else {
            
            if (bdayCardCheckBox.isSelected()) {
                chkBox = "BDC";
                whichOne = 3;
            //System.out.println("here =================================");
                mt = new Memtick(m.getCID(), en, null, LocalDate.now(), 0.0, 0.0, 0.0, chkBox, 0);
            }
            if (newMemberCheckBox.isSelected()) {
                chkBox = "WADD";
                whichOne = 2;
                mt = new Memtick(m.getCID(), en, null, LocalDate.now(), 0.0, 0.0, 0.0, chkBox, 0);
            }    
        }
        try {
                db.connect();
                db.CloseAccount(stt, whichOne);
                db.disConnect();
                
        } catch (SQLException ex) {
                System.out.println("error " + ex.toString());
        }
         // * Member ID	Transaction Number	Date	Time	Employee Number	Added	Bonus	Subtracted	Location
            try {
                if (!db.isMemberValid(m.getCCN())) {
                    System.out.println("Here insertInactiveData for inactivemembers");
                    if (!db.InsertInactiveData(mt)) {
                        System.out.println("error depositing member detail");
                    }
                db.connect();
                db.CloseInactiveAccount(stt, whichOne);
                db.disConnect();
                    
                } else {    
                    System.out.println("Here in insertData for MEMBERS");
                    if (!db.InsertData(mt)) {
                        System.out.println("error depositing member detail");
                    }
                db.connect();
                db.CloseAccount(stt, whichOne);
                db.disConnect();

                }   
            } catch (SQLException ex) {
                System.out.println(ex);
            }

        //newBalance = m.getBalance() + mt.getAdded() + mt.getBonus();
        //if (!db.updateTicketBalance(newBalance, mt)) {
        //    System.out.println("error updateing member balance");
       // }
        selectionMade(2);
        searchField.clear();
        Platform.runLater(()->searchField.requestFocus());
    
    }
    
    private void reset() {
        searchField.clear();
        Platform.runLater(()->searchField.requestFocus());
    }
    
    
    
    private void enterButtonPressed() {
        
        if (gamesTable.isFocused()) {
            gamesTable.getSelectionModel().selectNext();
        }
        if (searchField.isFocused()) {
            gamesTable.getSelectionModel().select(0);
            gamesTable.requestFocus();
        }
        }
    
    
    private void F6Pressed() {
        if (thisSelection()) {
            selectButton.fire();        
        }
    }
    
    
    private void selectionMade(int whichOne) {
        switch (whichOne) {
            case 1: 
            this.selectionMade = true;
            break;
            case 2:
            this.selectionMade = false;
            break;
                
        }
    }
    
    private boolean thisSelection() {
        return this.selectionMade;
    }
    
    
    
    
    public ObservableList<Member> getGames() throws SQLException {
    ObservableList<Member> games = FXCollections.observableArrayList();
    games.clear();
    Member newGame = null;
    try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "Member.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT Member.[Customer Card Number], Member.[First Name], Member.[Last Name], Member.Address, Member.[Customer ID] FROM Member ORDER BY Member.[Customer ID];");
           //String ccn, String cid, String namef, String namel, LocalDate bdate, String areacode, String phone1, String phone2
            while (rs.next()) {
                newGame = new Member( rs.getString("Customer Card Number"), rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Address"), rs.getString("Customer ID"));
                games.add(newGame);
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

//BELOW IS THE INACTIVE MEMBERS
    
    
    }

        try {
            connI=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "InactiveMembers.accdb;immediatelyReleaseResources=true");
            st = connI.createStatement();
            rs = st.executeQuery("SELECT Inactive.[Customer Card Number], Inactive.[First Name], Inactive.[Last Name], Inactive.Address, Inactive.[Customer ID] FROM Inactive ORDER BY Inactive.[Customer ID];");
           //String ccn, String cid, String namef, String namel, LocalDate bdate, String areacode, String phone1, String phone2
            while (rs.next()) {
                newGame = new Member( rs.getString("Customer Card Number"), rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Address"), rs.getString("Customer ID"));
                games.add(newGame);
            }
    }
    catch (SQLException e) {
        System.err.println(e.getMessage());
    }
    finally {
    if (connI != null);
        connI.close();
    if (st != null);
        st.close();
    if (rs != null);
        rs.close();
} 
    
    
    return games;
}   

    public void cancelButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        //sc.changeScenes(stageV, "/views/Main.fxml", "Pojo Main " + new employeeFX().titleBar);
        stageV.close();
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
                    case F9: cancelButton.fire(); break;
                    case F10: reset(); break;
                    case F11: F6Pressed(); break;
                    case SPACE: selectionMade(1); break;
                    case TAB: enterButtonPressed(); break;
                    case UP: gamesTable.getSelectionModel().selectPrevious(); break;
                    case DOWN: gamesTable.getSelectionModel().selectNext(); break;
                    case ESCAPE: cancelButton.fire();break;
                    case ENTER: enterButtonPressed(); break;
                default:
                    break;
                }
    }
    


 private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F1) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F2) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F3) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F4) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F5) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F6) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F7) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F8) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F9) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F10) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.TAB) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.INSERT) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.LEFT) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.RIGHT) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.UP) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.DOWN) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.ESCAPE) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.ENTER) {
                keyListener(ke);
                ke.consume();
            }
        });
    }




}
