
package views.counterPopUp;

import models.club.UtilMember;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
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
import models.club.LastMemberTransaction;
import models.club.Member;
import sceneChangerFX.SceneChanger_Main;
import static views.MemberViewController.beenhereonce;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class lastMemberTransactionViewController implements Initializable {
    @FXML private TableView<LastMemberTransaction> gamesTable;
    @FXML private TableColumn<LastMemberTransaction, String> gameNameColumn;
    @FXML private TableColumn<LastMemberTransaction, String> gameLocationColumn;
    @FXML private TableColumn<LastMemberTransaction, String> gameDepartmentColumn;
    @FXML private TableColumn<LastMemberTransaction, String> gameIDColumn;
    @FXML private TextField searchField;
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Pane root;
    
    public static SceneChanger_Main sc;
    public static ArrayList<LastMemberTransaction> lastMember;
    public static String cssPath;
    public static dbStringPath dbsp;
    public static Connection conn;
    private static Statement st;
    private static ResultSet rs;
    private static LastMemberTransaction lmt = null; 
    
    private static ObservableList<LastMemberTransaction> memberList = FXCollections.observableArrayList();

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssPath);
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastMember"));
        gameLocationColumn.setCellValueFactory(new PropertyValueFactory<>("lastLastName"));
        gameDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("lastEmpName"));
        gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("lastActivity"));
        Platform.runLater(()->memberList = FXCollections.observableArrayList(lastMember));
        Platform.runLater(()->gamesTable.getItems().addAll(memberList));
        Platform.runLater(()->searchField.requestFocus());
        
    }    
    
    
    
    
    
    
    
    public void searchRecord(KeyEvent ke) throws SQLException {
        
       
//        FilteredList<Member> filterData = new FilteredList<>(getGames(), p -> true);
        FilteredList<LastMemberTransaction> filterData = new FilteredList<>(gamesTable.getItems(), p -> true);
        searchField.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
            filterData.setPredicate(pers -> {
                if (newvalue == null || newvalue.isEmpty()) {
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                //System.out.println(typedText);
                if (pers.getLastMember().toLowerCase().contains(typedText)) {

                    return true;
                }
                if (pers.getLastLastName().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getLastEmpName().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getLastActivity().toLowerCase().contains(typedText)) {

                    return true;
                }

                return false;
            });
            SortedList<LastMemberTransaction> sortedList = new SortedList<>(filterData);
            //sortedList.comparatorProperty().bind(gamesTable.comparatorProperty());
            gamesTable.setItems(sortedList);
                       
            
        });

    }

    public void selectButtonPushed(ActionEvent event) throws IOException {
        if (gamesTable.getItems().size() == 1) {
            gamesTable.getSelectionModel().select(0);
        }
        TablePosition pos = gamesTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        //String string = gamesTable.getItems().get(row).getCCN();
        //sc.changePopUp1(event, string);
        LastMemberTransaction member = gamesTable.getItems().get(row); 
        lmt = new LastMemberTransaction(member.getLastMember(), member.getlastEmpNumber(), member.getLastEmpName(), member.getLastLastName(), member.getLastActivity());
        sc.changePopUpGetLastMemberTransaction(event, lmt);
    }
    
    
    public void TableClicked(MouseEvent me) throws IOException {
         if (me.getClickCount() == 2) {
            selectButton.fire();
         }
    }
    
    
    /*public ObservableList<Member> getMemberList() throws SQLException {
    //beenhereonce++;
    if (beenhereonce == 0) {
        System.out.println("===================================== BEEN HERE ONCE = " + beenhereonce);
    memberList.clear();
    Member newGame = null;
    try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "Member.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            //rs = st.executeQuery("SELECT Member.[Customer Card Number], Member.[First Name], Member.[Last Name], Member.Address, Member.[Customer ID] FROM Member ORDER BY Member.[Customer ID];");
            rs = st.executeQuery("SELECT Member.* FROM Member ORDER BY Member.[Customer ID];");
           //String ccn, String cid, String namef, String namel, LocalDate bdate, String areacode, String phone1, String phone2
            while (rs.next()) {
                newGame = new Member(rs.getString("Customer Card Number"), rs.getString("Customer ID"), rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Address"), rs.getString("City"), rs.getString("State"), rs.getString("Zip Code"), null, rs.getDate("Birth Date").toLocalDate(), String.valueOf(rs.getString("Ticket Balance")), true, true, rs.getDate("Orig Date").toLocalDate(), rs.getString("Area_Code"), rs.getString("Phone_1"), rs.getString("Phone_2"), "");
                memberList.add(newGame);
            }
    }
    catch (SQLException e) {
        System.err.println(e.getMessage());
    }
    finally {
        if (st != null) {
            st.close();}
        if (rs != null) {
            rs.close();}
        if (conn != null) {
            conn.close();}
}

        try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "InactiveMembers.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            //rs = st.executeQuery("SELECT Inactive.[Customer Card Number], Inactive.[First Name], Inactive.[Last Name], Inactive.Address, Inactive.[Customer ID] FROM Inactive WHERE (((Inactive.City)<>\"CLOSE\")) ORDER BY Inactive.[Customer ID];");
            rs = st.executeQuery("SELECT Inactive.* FROM Inactive WHERE (((Inactive.City)<>\"CLOSE\")) ORDER BY Inactive.[Customer ID];");
           //String ccn, String cid, String namef, String namel, LocalDate bdate, String areacode, String phone1, String phone2
            while (rs.next()) {
                newGame = new Member(rs.getString("Customer Card Number"), rs.getString("Customer ID"), rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Address"), "INACTIVE", rs.getString("State"), rs.getString("Zip Code"), null, rs.getDate("Birth Date").toLocalDate(), String.valueOf(rs.getString("Ticket Balance")), true, true, rs.getDate("Orig Date").toLocalDate(), rs.getString("Area_Code"), rs.getString("Phone_1"), rs.getString("Phone_2"), "");
                memberList.add(newGame);
                //newGame = new Member( rs.getString("Customer Card Number"), rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Address"), rs.getString("Customer ID"));
                //games.add(newGame);
            }
    }
    catch (SQLException e) {
        System.err.println(e.getMessage());
    }
    finally {
        if (st != null){
            st.close();}
        if (rs != null){
            rs.close();}
        if (conn != null){
            conn.close();}
}
    } else {
        System.out.println("===================================== BEEN HERE ONCE2 = " + beenhereonce);
        
    }
    return memberList;
}   
*/
    
    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        if (searchField.getText().trim().length()>0) {
            clearSearchField();
        } else {
            try{
                conn.close();
                //memberList.clear();
                gamesTable.getItems().clear();
            }catch(Exception e) {}
            //UtilMember utilmember = new UtilMember("100001", "235", "POJOS", "MEMBERS", "7736 FAIRVIEW AVE", "BOISE", "ID", "83642", null, LocalDate.now(), "0", null, null, LocalDate.now(), "208", "376", "6981", "");
            lmt = new LastMemberTransaction("1", "1", "1", "1", "1");
            sc.setActivity("1");
            sc.changePopUpGetLastMemberTransaction(event, lmt);
            //Stage stageV = (Stage) cancelButton.getScene().getWindow();
            //stageV.close();

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
