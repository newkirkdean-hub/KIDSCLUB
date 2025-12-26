
package views.counterPopUp;

import Css.cssChanger;
import models.club.UtilMember;
import dbpathnames.dbStringPath;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import pReceipts.print;
import sceneChangerFX.SceneChanger_Main;

/**
 * DuplicateMemberPopUp is used in the NewMembers screen when a name is type in the first and last name
 * and it searches for that name to see if there is already an account. It is not tied to the SearchMemberView Screen.
 * @author Dean
 */
public class DuplicateMemberPopUpController implements Initializable {
    @FXML private TableView<UtilMember> gamesTable;
    @FXML private TableColumn<UtilMember, String> _FirstNameColumn;
    @FXML private TableColumn<UtilMember, String> _LastNameColumn;
    @FXML private TableColumn<UtilMember, String> _AddressColumn;
    @FXML private TableColumn<UtilMember, String> _IDColumn;
    @FXML private TableColumn<UtilMember, String> _Bdate;
    @FXML private TableColumn<UtilMember, String> _Phone;
    @FXML private Button cancelButton;
    @FXML private Button buttonPrint;
    @FXML private Pane root;
    
    public static SceneChanger_Main sc;
    public static dbStringPath dbsp;
    
    private static Connection conn = null, connI = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    private static UtilMember newGame = null;
    public static String CID, css;
    public static ArrayList<String> cCardNumbers; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(css);
        //root.getStylesheets().add(cssC.cssPath());
        _FirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("NameF"));
        _LastNameColumn.setCellValueFactory(new PropertyValueFactory<>("NameL"));
        _AddressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        _IDColumn.setCellValueFactory(new PropertyValueFactory<>("CCNMasked"));
        _Bdate.setCellValueFactory(new PropertyValueFactory<>("Bdate"));
        _Phone.setCellValueFactory(new PropertyValueFactory<>("phoneCombined"));
        
        try {
            gamesTable.getItems().addAll(getGames(CID));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
       // searchField.requestFocus();
        
    }    
    
     public void TableClicked(MouseEvent me) throws IOException {
         if (me.getClickCount() == 2) {
            buttonPrint.fire();
         }
    }
    

    public void selectButtonPushed(ActionEvent event) throws IOException {
          
        TablePosition pos = gamesTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        try (PrintWriter pw = new PrintWriter(new File(dbStringPath.pathNameLocal + "CCNLookupReceipt.txt"))) {
            pw.println(""); // to test if it works.
            pw.println(""); // to test if it works.
            pw.println(""); // to test if it works.
            pw.println();
            pw.println("");
            pw.println("");
            pw.println("");
            pw.println(gamesTable.getItems().get(row).getNameF() + " " + gamesTable.getItems().get(row).getNameL());
            pw.println(gamesTable.getItems().get(row).getCCN());
            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
        } // to test if it works.

            print pr = new print();
            pr.printReceipt("CCNLookupReceipt.txt");
            cancelButton.fire();

    }
    
    public ObservableList<UtilMember> getGames(String CID) throws SQLException {
    ObservableList<UtilMember> games = FXCollections.observableArrayList();
        try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "Member.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();

            for (int i = 0; i < cCardNumbers.size(); i++) {
                //System.out.println(cCardNumbers.get(i));
                rs = st.executeQuery("SELECT Member.[Customer Card Number], Member.[Customer ID], Member.[First Name], Member.[Last Name], Member.Address, Member.[Birth Date], Member.City, Member.State, Member.[Zip Code], Member.[Orig Date], Member.[Ticket Balance], Member.Area_Code, Member.Phone_1, Member.Phone_2 FROM Member WHERE (((Member.[Customer Card Number])='" + cCardNumbers.get(i) + "'))");
                while (rs.next()) {
                    newGame = new UtilMember(rs.getString("Customer Card Number"), rs.getString("Customer ID"), rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Address"), rs.getDate("Birth Date").toLocalDate(), rs.getString("Area_Code"), rs.getString("Phone_1"), rs.getString("Phone_2"));
                    games.add(newGame);
                }
            } //END OF FOR LOOP

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (st != null);
            st.close();
            if (rs != null);
            rs.close();
            if (conn != null);
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        try {
            connI = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "InactiveMembers.accdb;immediatelyReleaseResources=true");
            st = connI.createStatement();

            for (int i = 0; i < cCardNumbers.size(); i++) {
                //System.out.println(cCardNumbers.get(i));
                rs = st.executeQuery("SELECT Inactive.*FROM Inactive WHERE (((Inactive.[Customer Card Number])='" + cCardNumbers.get(i) + "'))");
                while (rs.next()) {
                    newGame = new UtilMember(rs.getString("Customer Card Number"), rs.getString("Customer ID"), rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Address"), rs.getDate("Birth Date").toLocalDate(), rs.getString("Area_Code"), rs.getString("Phone_1"), rs.getString("Phone_2"));
                    games.add(newGame);
                }
            } //END OF FOR LOOP

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (st != null);
            st.close();
            if (rs != null);
            rs.close();
            if (conn != null);
            try {
                conn.close();
            } catch (SQLException e) {
            }
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
                    case ENTER: buttonPrint.fire(); break;
                default:
                    break;
                }
    }
    
}
