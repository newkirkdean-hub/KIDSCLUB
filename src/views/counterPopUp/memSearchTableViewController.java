
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
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.club.LastMemberTransaction;
import models.club.Member;
import sceneChangerFX.SceneChanger_Main;


/**
 * FXML Controller class
 *
 * @author Dean
 */
public class memSearchTableViewController implements Initializable {
    @FXML private TableView<Member> gamesTable;
    @FXML private TableColumn<Member, String> gameNameColumn;
    @FXML private TableColumn<Member, String> gameLocationColumn;
    @FXML private TableColumn<Member, String> gameDepartmentColumn;
    @FXML private TableColumn<Member, String> gameIDColumn;
    @FXML private TextField searchField;
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Button fullListLMTButton;
    @FXML private Pane root;
    
    
    public static SceneChanger_Main sc;
    public static String cssPath;
    public static dbStringPath dbsp;
    public static Connection conn;
    private static Statement st;
    private static ResultSet rs;
    public static FXMLLoader FXLOADER;
    //public static int iLastTran;
    private static Stage stageV;
    private static int beenhereonce = 0;
    public static ArrayList<LastMemberTransaction> lastMember;
    
    private static ObservableList<Member> memberList = FXCollections.observableArrayList();

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssPath);
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("NameF"));
        gameLocationColumn.setCellValueFactory(new PropertyValueFactory<>("NameL"));
        gameDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("CCN"));
        try {
            gamesTable.getItems().clear();
            gamesTable.getItems().addAll(getMemberList());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        Platform.runLater(()->getStageV());
        Platform.runLater(()->searchField.requestFocus());
        
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
        Member member = gamesTable.getItems().get(row); 
        UtilMember utilmember = new UtilMember(member.getCCN(), member.getCID(), member.getNameF(), member.getNameL(), member.getAddress(), member.getCity(), member.getState(), member.getZip(), null, member.getBdate(), String.valueOf(member.getBalance()), null, null, member.getOdate(), member.getAreaCode(), member.getPhone1(), member.getPhone2(), "");
        sc.changePopUpGetMember(event, utilmember);
    }
    
    
    public void TableClicked(MouseEvent me) throws IOException {
         if (me.getClickCount() == 2) {
            selectButton.fire();
         }
    }
    
    
     public void CCNClicked(MouseEvent me) {
        me.consume();
        System.out.println("Mouse event = " + me.getButton().SECONDARY);
        if (me.getClickCount() == 2) {
           fullListLMTButton.fire();
            return;
        }
        MouseButton mb = me.getButton();
        if (mb == MouseButton.SECONDARY) {
           fullListLMTButton.fire();
        }
    }
     
     
      /*public void getLastTransactionMemberVIP() {
        String empNumb, lastCCN;
        try {
            empNumb = lastMember.get(iLastTran).getlastEmpNumber();
            lastCCN = lastMember.get(iLastTran).getLastMember();
            sc.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", 550.0, 50.0);
            boolean GO = isEMPValidInArrayList(sc.getGameNumber());
            if (en.equals(empNumb)) {
                //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "Approved!", "Here is the last member number " + lastCCN);
                CCNumberIn.setText(lastCCN);
                Platform.runLater(() -> enterKeyPressed());
            }
        } //counter screens
        catch (IOException ex) {
            System.out.println(ex);
        }
        empNumb = "";
        lastCCN = "";
    }*/

    public void getFullListLastTransactionMemberVIP(ActionEvent ae) {
        try {
            //sc.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", 550.0, 50.0);
            //boolean GO = isEMPValidInArrayList(sc.getGameNumber());
            //if (newEFX.getBcarsales() != 1) {
             //   return;
            //}
            lastMemberTransactionViewController wController = (lastMemberTransactionViewController) FXLOADER.getController();
            wController.sc = sc;
            wController.dbsp = dbsp;
            wController.cssPath = cssPath;
            System.out.println("here is the size of lastmember " + lastMember.size());
            wController.lastMember = lastMember;
            //wController.rs = 
            sc.changePopUp(ae, "/views/counterPopUp/lastMemberTransactionView.fxml", "List of Activity");
            cancelButtonPushed(ae);
            //sc.getpassWord(stageV, "/views/counterPopUp/lastMemberTransactionView.fxml", "Number", "Enter Employee Number:", 550.0, 50.0);
            //CCNumberIn.setText(sc.getCCN());
            LastMemberTransaction LMT = sc.GetLastMemberTransaction();
            searchField.setText(LMT.getLastMember());
            List<Member> memberArrayList = new ArrayList<>(memberList);
            for (int i = 0; i<memberArrayList.size(); i++) {
                if (LMT.getLastMember().equals(memberArrayList.get(i).getCCN())) {
                    UtilMember utilmember = new UtilMember(memberArrayList.get(i).getCCN(), memberArrayList.get(i).getCID(), memberArrayList.get(i).getNameF(), memberArrayList.get(i).getNameL(), memberArrayList.get(i).getAddress(), memberArrayList.get(i).getCity(), memberArrayList.get(i).getState(), memberArrayList.get(i).getZip(), null, memberArrayList.get(i).getBdate(), String.valueOf(memberArrayList.get(i).getBalance()), null, null, memberArrayList.get(i).getOdate(), memberArrayList.get(i).getAreaCode(), memberArrayList.get(i).getPhone1(), memberArrayList.get(i).getPhone2(), "");
                    sc.changePopUpGetMember(ae, utilmember);

                }
            }
           
            //char[] listLastMemberChar = new char[LMT.getLastMember().length()];
            //for (int i=0; i < listLastMemberChar.length; i++) {
              //  listLastMemberChar[i] = LMT.getLastMember().charAt(i);
               // KeyEvent ke = getKey(String.valueOf(listLastMemberChar[i]));
               // System.out.println("here is the keyevent " + ke.getCode());
                //searchField.setText(searchField.getText() + LMT.getLastMember().charAt(i));
                //searchField.setText(press.getCharacter());
                //System.out.println("Here is the Press.getCharactor() " + ke.getCode());
                
               
            //}

            //Platform.runLater(() -> selectButton.fire());
        } //counter screens
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
     
    
     
     private void getStageV() {
         stageV = (Stage) selectButton.getScene().getWindow();
    }
     
     
     
     
     
     
     
     
     
     

    
    public ObservableList<Member> getMemberList() throws SQLException {
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

    
    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        if (searchField.getText().trim().length()>0) {
            clearSearchField();
        } else {
            try{
                conn.close();
                //memberList.clear();
                gamesTable.getItems().clear();
            }catch(Exception e) {}
            UtilMember utilmember = new UtilMember("100001", "235", "POJOS", "MEMBERS", "7736 FAIRVIEW AVE", "BOISE", "ID", "83642", null, LocalDate.now(), "0", null, null, LocalDate.now(), "208", "376", "6981", "");
            sc.changePopUpGetMember(event, utilmember);
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
