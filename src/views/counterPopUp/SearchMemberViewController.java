
package views.counterPopUp;

import JavaMail.Mail_JavaFX1;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.BarCode;
import commoncodes.ClubFunctions;
import commoncodes.IsItANumber;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.GetReceipts;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.HeadlessException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import models.club.DB;
import models.club.Member;
import models.club.Memtick;
import models.club.rCeipts;
import pReceipts.JavaPrinterOptions;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class SearchMemberViewController implements Initializable {
    @FXML private TableView<Member> memberTable;
    @FXML private TableColumn<Member, String> memberNameFColumn;
    @FXML private TableColumn<Member, String> memberNameLColumn;
    @FXML private TableColumn<Member, String> memberBdateColumn;
    @FXML private TableColumn<Member, String> memberIDColumn;
    @FXML private TableColumn<Member, String> memberAddressColumn;
    @FXML private TableColumn<Member, String> memberCCNColumn;
    @FXML private TableColumn<Member, String> memberPhoneColumn;
    @FXML private TextField searchField;
    @FXML private TextField nameFTextfield;
    @FXML private TextField nameLTextfield;
    @FXML private TextField ph1Textfield;
    @FXML private TextField ph2Textfield;
    @FXML private TextField ph3Textfield;
    @FXML private TextField bday1Textfield;
    @FXML private TextField bday2Textfield;
    @FXML private TextField bday3Textfield;
    @FXML private PasswordField empTextfield;
    @FXML private HBox searchBar;
    @FXML private Pane tablePane;
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Button searchButton;
    @FXML private Button resetButton;
    @FXML private Label errorLabel;
    @FXML private Pane root;
    
    
    
    public static DB db;
    public static String csspath;
    public static dbStringPath dbsp;
    public static String en, fn, ln;
    public static PrintWriter pw;


    private static final IsItANumber iin = new IsItANumber();
    private static final ClubFunctions cf = new ClubFunctions();
    private static final GetDay GETDAY = new GetDay();
    private static final GetReceipts getR = new GetReceipts();
    private static final JavaPrinterOptions JPO = new JavaPrinterOptions();
    private static final java.util.Date DT = new java.util.Date();
    private static final java.text.SimpleDateFormat SDF = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    
    private static int memberSize = 0;
    private static ObservableList<Member> members = null;
    public static ArrayList<rCeipts> receipts;
    private static Connection conn = null, connI = null;
    private static Statement statement = null, stI = null;
    private static ResultSet rs = null, rsI = null;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        setKeyCodes();
        root.getStylesheets().add(csspath);
        memberNameFColumn.setCellValueFactory(new PropertyValueFactory<>("NameF"));
        memberNameLColumn.setCellValueFactory(new PropertyValueFactory<>("NameL"));
        memberBdateColumn.setCellValueFactory(new PropertyValueFactory<>("BdateString"));
        memberPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneCombined"));
        memberAddressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));
        //memberIDColumn.setCellValueFactory(new PropertyValueFactory<>("CID"));
        memberCCNColumn.setCellValueFactory(new PropertyValueFactory<>("CCNMasked"));
        new FocusedTextFieldHighlight().setHighlightListener(root);
        addTextfieldListeners();
        init();
        Platform.runLater(()->setEmp());  
    }    
    
    private void init() {
        tablePane.setVisible(false);
        searchButton.setDisable(true);
        resetButton.setDisable(true);
        memberTable.setVisible(false);
        selectButton.setDisable(true);
        searchBar.setVisible(false);
        Platform.runLater(()->nameFTextfield.requestFocus());

}    
    

    public void GOSearchButton() {
       // if (tablePane.isVisible()) {
            
        //} else {
            //searchBar.setVisible(false);
            
            CheckSearchFields();
        //}
    }

    public void GOSelectButton(ActionEvent event) throws IOException {
        TablePosition pos = memberTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        String string = memberTable.getItems().get(row).getCCN();
        String ID = memberTable.getItems().get(row).getCID();

        Memtick mt = new Memtick(ID, en, GETDAY.getCurrentTimeStamp(), LocalDate.now(), 0.0, 0.0, 0.0, "SERCH", 0);
        db.InsertHistoricDataOneTimeXplain(mt.getMemID(), mt.getEmpNumber(), mt.getTranNumber(), "SERCH", "", "");
        //db.InsertData(mt);
        try {
            //print(memberTable.getItems().get(row).getNameF() + " " + memberTable.getItems().get(row).getNameL(), string);
            printEscPosSEARCHMEMBER(memberTable.getItems().get(row).getNameF() + " " + memberTable.getItems().get(row).getNameL(), string);
        } catch (PrintException ex) {
            System.out.println(ex);
            new Mail_JavaFX1().sendEmailTo("Error Printing Member Search REceipt", "there was an error when trying to print the member search receipt with a barcode on it. " + ex, "errors");
        }
        
        
        
        /*
        pw = new PrintWriter(new File(dbStringPath.pathNameLocal + "CCNLookupReceipt.txt"));
            pw.println(""); // to test if it works.
            pw.println(""); // to test if it works.
            pw.println(""); // to test if it works.
            pw.println();
            pw.println("");
            //pw.println("Employee: " + fn);
            pw.println("");
            pw.println("");
            pw.println(memberTable.getItems().get(row).getNameF() + " " + memberTable.getItems().get(row).getNameL());
            pw.println(string);
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
            pw.close();

            print pr = new print();
            pr.printReceipt("CCNLookupReceipt.txt");
        
        */
        resetFields();
        cancelButton.fire();

        
    }

    public void GOCancelButton(){
        en = null;
        fn = null;
        ln = null;
        //eFXX1 = null;
        if (memberSize > 0) {
            members.clear();
        }
        try {
            if (conn != null) {
                conn.close();
            }
            if (connI != null) {
                connI.close();
            }
        } catch (SQLException ex) {
            System.out.println("Closeing Inactive Members and Mmebers");
        }
        db.disConnect();
        Stage stage = (Stage) selectButton.getScene().getWindow();
        stage.close();
    }




    private void print(String name, String number) {

        JPO.clearCommands();
        JPO.initialize();

        JPO.newLine();

        JPO.doubleHeight(true);
        JPO.alignCenter();
        JPO.chooseFont(1);
        JPO.setText(name);
        
        JPO.newLine();

        JPO.doubleHeight(false);
        JPO.alignCenter();
        JPO.chooseFont(1);        
        JPO.setText("Employee: " + fn);

        JPO.newLine();

        JPO.addLineSeperator();

        JPO.newLine();

        JPO.alignCenter();
        JPO.chooseFont(1);
        System.out.println("========================================== " + GETDAY.Local_Date_Time_AMPM());
        JPO.setText(GETDAY.Local_Date_Time_AMPM());

        JPO.newLine();

        JPO.addLineSeperator();
        JPO.emphasized(false);

        JPO.newLine();

        JPO.doubleHeight(true);
        //JPO.tripleWidth(true);
        JPO.chooseFont(1);
        JPO.alignCenter();
        JPO.setText(number);
        //p.tripleHeight(false);
        //p.tripleWidth(false);
        

        JPO.newLine();
        
        /*
        p.feed((byte) 2);
        p.chooseFont(4);
        p.alignCenter();
        p.setText("T2156");
        
        p.newLine();
        */
        JPO.feed((byte) 6);
        JPO.finit();
        System.out.println("Command Set " + JPO.finalCommandSet() + " " + getR.getReceipts(receipts, "Printer"));
        
        JPO.feed((byte) 6);
        JPO.finit();

        JPO.Print(JPO.finalCommandSet().getBytes(), JPO.getPrinterName(getR.getReceipts(receipts, "Printer")));
        JPO.clearCommands();
        //JPO.resetPrinter();
    }



    private void printEscPosSEARCHMEMBER(String name, String number) throws IOException, PrintException{
        //printEscPos
        PrintService foundService = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob dpj = foundService.createPrintJob();
        
        System.out.println("This si the Default Printer " + foundService.getName());
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        outputStream.write(27); // ESC
        outputStream.write('@');
     
         
        
        //PRINT LOGO IF AVAILIBLE
        EscPos escpos = new EscPos(outputStream);
        Style Format = new Style();
         //PRINT EMP NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "Member: " + name);
        escpos.feed(1);        
        
         //PRINT DATE TIME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, GETDAY.Local_Date_Time_AMPM());
        escpos.feed(1);        
        
        
         //PRINT THE BAR CODE
        BarCode barcode = new BarCode();
        barcode.setHRIPosition(BarCode.BarCodeHRIPosition.BelowBarCode);
        barcode.setJustification(EscPosConst.Justification.Center);
        escpos.write(barcode, number);
        
       
        // feed 5 lines
        outputStream.write(27); // ESC
        outputStream.write('d');
        outputStream.write(5);

        // cut
        outputStream.write(29); // GS
        outputStream.write('V');
        outputStream.write(48);


        // do not forguet to close outputstream
        outputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());


        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc doc = new SimpleDoc(inputStream, flavor, null);
        dpj.print(doc, null);
    }





















    
    public void GOResetButton(){
        if (ph2Textfield.isDisable() || nameFTextfield.isDisable()) {
            resetFields();
        }
    }
    
    public void GOEnterKeyPressed() {
        if (nameFTextfield.isFocused()) {
            nameLTextfield.requestFocus();
            return;
        }
        if (nameLTextfield.isFocused()) {
            bday1Textfield.requestFocus();
            return;
        }
        if (bday1Textfield.isFocused()) {
            String nValue = bday1Textfield.getText();
            bday1Textfield.setText(GETDAY.getTwoDigitMonthDay(nValue));
            bday2Textfield.requestFocus();
            return;
        }
        if (bday2Textfield.isFocused()) {
            String nValue = bday2Textfield.getText();
            bday2Textfield.setText(GETDAY.getTwoDigitMonthDay(nValue));
            bday3Textfield.requestFocus();
            return;
        }
        if (bday3Textfield.isFocused()) {
            ph1Textfield.requestFocus();
            return;
        }
        if (ph1Textfield.isFocused()) {
            if (!cf.testPhone(1, ph1Textfield.getText().trim())) {
                showError("Area Code must have three digits");
                return;
            } else {
                showErrorClear();
                ph2Textfield.requestFocus();
                return;
            }
        }
        if (ph2Textfield.isFocused()) {
            if (!cf.testPhone(2, ph2Textfield.getText().trim())) {
                showError("Phone Prefix must have three digits");
                return;
            } else {
                showErrorClear();
                ph3Textfield.requestFocus();
                return;
            }
        }
        if (ph3Textfield.isFocused()) {
            if (!cf.testPhone(3, ph3Textfield.getText().trim())) {
                showError("Line Number must have four digits");
                return;
            } else {
                showErrorClear();
                nameFTextfield.requestFocus();
                return;
            }
        }
    }
 
    private void showErrorClear() {
            errorLabel.setText("");
            searchBar.setVisible(false);
    }
    
    private void showError(String error) {
            errorLabel.setText(error);
            searchBar.setVisible(true);
    }
   
    private void showResetButton() {
        resetButton.setDisable(false);
    }
    
    public void addTextfieldListeners() {
        ph1Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        iin.checkNumbers(newValue);
                    } catch (Exception e) {
                        showError("Area Code can only be Numbers");
                        ph1Textfield.setText("");
                        ph1Textfield.requestFocus();
                        showResetButton();
                        return;
                    }
                    if (CheckPhoneFields()) {
                        searchButton.setDisable(false);
                        return;
                    } else {
                        searchButton.setDisable(true);
                    }
                    if (newValue.length() >= 3) {
                        showErrorClear();
                        ph2Textfield.requestFocus();
                    }
                }
        );
        ph2Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        iin.checkNumbers(newValue);
                    } catch (Exception e) {
                        showError("PreFix can only be Numbers");
                        ph2Textfield.setText("");
                        ph2Textfield.requestFocus();
                        return;
                    }
                    if (CheckPhoneFields()) {
                        searchButton.setDisable(false);
                        return;
                    } else {
                        searchButton.setDisable(true);
                    }
                    if (newValue.length()==3) {
                        SetTopFieldsDisabled(1);            
                    } else {
                        SetTopFieldsDisabled(2);
                    }
                    if (newValue.length() == 3) {
                        showErrorClear();
                        ph3Textfield.requestFocus();    
                    }
                }
        );
        ph3Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        iin.checkNumbers(newValue);
                    } catch (Exception e) {
                        showError("Line Number can only be Numbers");
                        ph3Textfield.setText("");
                        ph3Textfield.requestFocus();
                        return;
                    }
                    if (CheckPhoneFields()) {
                        showResetButton();
                        searchButton.setDisable(false);
                        searchButton.fire();
                        //return;
                    } else {
                        resetButton.setDisable(true);
                        searchButton.setDisable(true);
                    }
                    if (newValue.length() == 4) {
                        showErrorClear();
                        searchButton.fire();
                    }
                }
        );
        nameFTextfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    nameFTextfield.setText(newValue.toUpperCase());
                    if (CheckTopFields()) {
                        searchButton.setDisable(false);
                        return;
                    } else {
                        searchButton.setDisable(true);
                        showResetButton();
                    }
                    if (newValue.length() > 1) {
                        SetBottomFieldsDisabled(1);
                    } else {
                        SetBottomFieldsDisabled(2);
                    }
                }
        );
        nameLTextfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    nameLTextfield.setText(newValue.toUpperCase());
                }
        );
        bday1Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        iin.checkNumbers(newValue);
                    } catch (Exception e) {
                        showError("Month can only be Numbers");
                        bday1Textfield.setText(oldValue);
                        bday1Textfield.requestFocus();
                        return;
                    }
                    if (CheckTopFields()) {
                        searchButton.setDisable(false);
                        return;
                    } else {
                        searchButton.setDisable(true);
                    }
                    if (newValue.length() >= 2) {
                        showErrorClear();
                        bday2Textfield.requestFocus();
                    }
                }
        );
        bday2Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        iin.checkNumbers(newValue);
                    } catch (Exception e) {
                        showError("Month can only be Numbers");
                        bday2Textfield.setText(oldValue);
                        bday2Textfield.requestFocus();
                        return;
                    }
                    if (CheckTopFields()) {
                        searchButton.setDisable(false);
                        return;
                    } else {
                        searchButton.setDisable(true);
                    }
                    if (newValue.length() >= 2) {
                        showErrorClear();
                        bday3Textfield.requestFocus();
                    }
                }
        );
        bday3Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        iin.checkNumbers(newValue);
                    } catch (Exception e) {
                        showError("Year can only be four (4) Numbers");
                        bday3Textfield.setText(oldValue);
                        bday3Textfield.requestFocus();
                        return;
                    }
                    if (CheckTopFields()) {
                        searchButton.setDisable(false);
                        searchButton.fire();
                        return;
                    } else {
                        searchButton.setDisable(true);
                    }
                    if (newValue.length() == 4) {
                        searchButton.setDisable(false);
                        searchButton.fire();
                    }
                }
        );
    }
    
    private boolean CheckPhoneFields() {
        boolean phoneFields = false;
        if (ph3Textfield.getText().length()==4 && ph2Textfield.getText().length()==3 && ph1Textfield.getText().length()==3) {
            phoneFields = true;
        } 
      return phoneFields;
    }
   
    private boolean CheckTopFields() {
        boolean topFields = false;
        if (nameFTextfield.getText().length()>3 && nameLTextfield.getText().length()>2 && bday1Textfield.getText().length()>0 && bday2Textfield.getText().length()>0 && bday3Textfield.getText().length()>3) {
            topFields = true;
        } 
      return topFields;
    }
        
    private void CheckSearchFields() {
        if (empTextfield.getText() == null) {
            showError("The Employee Number Cannot be Blank.");
            empTextfield.requestFocus();
            empTextfield.setDisable(false);
            empTextfield.setStyle("-fx-background-color: #FFFFFF;");
            return;
        }
        if (!ph1Textfield.getText().trim().isEmpty() && !ph2Textfield.getText().trim().isEmpty() && !ph3Textfield.getText().trim().isEmpty()) {
            runPhoneSearch();
            return;
        } else {
            if (nameFTextfield.getText().isEmpty()) {
                showError("The First Name Cannot be Blank.");
                nameFTextfield.requestFocus();
                return;
            }
            if (nameLTextfield.getText().isEmpty()) {
                showError("The Last Name Cannot be Blank.");
                nameLTextfield.requestFocus();
                return;
            }
            if (bday1Textfield.getText().isEmpty()) {
                showError("The Birthday Cannot be Blank.");
                bday1Textfield.requestFocus();
                return;
            }
            if (bday1Textfield.getText().trim().length() < 2) {
                String s = "0" + String.valueOf(bday1Textfield.getText());
                bday1Textfield.setText(s);
            }

            if (GETDAY.testMonth(Integer.parseInt(bday1Textfield.getText())) == false) {
                showError("MONTH of the Birthday fields cannot be less then 0 nor greater then 12");
                bday1Textfield.requestFocus();
                bday1Textfield.selectAll();
                return;
            }
            
            bday2Textfield.setText(GETDAY.getTwoDigitMonthDay(bday2Textfield.getText()));

           /* if (bday2Textfield.getText().trim().length() < 2) {
                String s = "0" + String.valueOf(bday2Textfield.getText());
                bday2Textfield.setText(s);
            }
            */
            if (GETDAY.testDay(Integer.parseInt(bday1Textfield.getText())) == false) {
                showError("DAY of the Birthday fields cannot be less then 0 nor greater then 31");
                bday1Textfield.requestFocus();
                bday1Textfield.selectAll();
                return;
            }

            if (GETDAY.testYear(bday3Textfield.getText()) == false) {
                showError("YEAR of the Bithday fields connot be less then four(4) digits not greater then four (4) digits");
                bday3Textfield.requestFocus();
                bday3Textfield.selectAll();
                return;
            }

            String bdayString = bday1Textfield.getText() + "/" + bday2Textfield.getText() + "/" + bday3Textfield.getText();
            if (!GETDAY.testDateValidity(bdayString)) {
                showError("YEAR of the Birthday fields is greater then the current day.");
                bday3Textfield.requestFocus();
                bday3Textfield.selectAll();
                return;
            }

            runPhoneSearch();
            return;
        }
    }

    private void runPhoneSearch() {
        memberTable.getItems().addAll(getGames());
        memberTable.getItems().addAll(getGames2());
        return;

    }

    public void searchagain() {
        if (!searchBar.isVisible()) {
            return;
        }
        CheckSearchFields();
        memberTable.getItems().addAll(getGames());
        memberTable.getItems().addAll(getGames2());
    }

    public void memberTableSelected(MouseEvent me) {
        selectButton.setDisable(false);
        if (me.getClickCount() == 2) {
            selectButton.fire();
         }
    }
        
    public ObservableList<Member> getGames() {
    members = FXCollections.observableArrayList();
    
    //System.out.println(counter++);
    String bdayString = bday1Textfield.getText() + "/" + bday2Textfield.getText() + "/" + bday3Textfield.getText();        
        
    try {
        conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "Member.accdb;immediatelyReleaseResources=true");
        statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        if (searchBar.isVisible()) {
            //second search with no birthday
            rs = statement.executeQuery("SELECT Member.[Customer Card Number], Member.[Customer ID], Member.[First Name], Member.[Last Name], Member.[Birth Date], Member.[Address], Member.Area_Code, Member.Phone_1, Member.Phone_2 FROM Member WHERE (((Member.[First Name])='" + nameFTextfield.getText() + "') AND ((Member.[Last Name])='" + nameLTextfield.getText() + "'))");
        } else {
            // first search with phone only
            if (!ph1Textfield.getText().trim().isEmpty() && !ph2Textfield.getText().trim().isEmpty() && !ph3Textfield.getText().trim().isEmpty()) {
                rs = statement.executeQuery("SELECT Member.[Customer Card Number], Member.[Customer ID], Member.[First Name], Member.[Last Name], Member.[Birth Date], Member.[Address], Member.Area_Code, Member.Phone_1, Member.Phone_2  FROM Member WHERE (((Member.[Area_Code])='" + ph1Textfield.getText() + "') AND ((Member.[Phone_1])='" + ph2Textfield.getText() + "') AND ((Member.[Phone_2])='" + ph3Textfield.getText() + "'))");
            } else {
            //firstsearch with Name and Birthday
                rs = statement.executeQuery("SELECT Member.[Customer Card Number], Member.[Customer ID], Member.[First Name], Member.[Last Name], Member.[Birth Date], Member.[Address], Member.Area_Code, Member.Phone_1, Member.Phone_2  FROM Member WHERE (((Member.[First Name])='" + nameFTextfield.getText() + "') AND ((Member.[Last Name])='" + nameLTextfield.getText() + "') AND ((Member.[Birth Date])=#" + bdayString + "#))");
            }
        }

        if (rs.last()) {
            int rows = rs.getRow();
            if (rows < 1) {
                showError("No Records Found");
            } else {
                rs.beforeFirst(); // Move to beginning
                while (rs.next()) {
                    Member newGame = new Member(rs.getString("Customer Card Number"), rs.getString("Customer ID"), rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Address"), rs.getDate("Birth Date").toLocalDate(), rs.getString("Area_Code"), rs.getString("Phone_1"), rs.getString("Phone_2"));
                    members.add(newGame);
                    memberSize++;
                }
                tablePane.setVisible(true);
                memberTable.setVisible(true);
                errorLabel.setText("");
                searchBar.setVisible(false);
                //nameFTextfield.requestFocus();
                //nameFTextfield.selectAll();
Platform.runLater(new Runnable()
{
    @Override
    public void run()
    {
        //memberTable.requestFocus();
        //memberTable.getSelectionModel().select(0);
        //memberTable.getFocusModel().focus(0);
        //selectButton.setVisible(true);
        //errorLabel.setText("");
        //searchBar.setVisible(false);
        
    }
});         
            }
        } else {
            if (searchBar.isVisible()) {
                if (ph2Textfield.isDisable()) { 
                    errorLabel.setText("Still No Records Found. Please check spelling.");                    
                } else {
                    errorLabel.setText("Still No Records Found. Please check Phone Number.");
                }
                searchBar.setVisible(true);
                nameFTextfield.requestFocus();
                nameFTextfield.selectAll();
            } else {
                if (ph2Textfield.isDisable()) {
                    errorLabel.setText("No Records Found - Press F7 to widen the search");
                } else {
                    errorLabel.setText("No Records Found - Try Name and Birthday Fields");
                }
                searchBar.setVisible(true);
                nameFTextfield.requestFocus();
                nameFTextfield.selectAll();
            }
        }
    }
    catch (Exception e) {
        System.err.println(e.getMessage());
    }
    finally {
        try {
        if (statement != null){
            statement.close();
        }
        if (rs != null){
            rs.close();
        }
        if (conn != null){
            conn.close();
        }
        } catch (SQLException ex) {
            System.out.println("here in getGames()");
        }
}

    return members;
}   
    
    
     public void TableClicked(MouseEvent me) throws IOException {
         if (me.getClickCount() == 2) {
            selectButton.fire();
         }
    }
    
    
    
    
    
    
    
    public ObservableList<Member> getGames2(){
    members = FXCollections.observableArrayList();
    
    String bdayString = bday1Textfield.getText() + "/" + bday2Textfield.getText() + "/" + bday3Textfield.getText();        
        
    try {
        connI = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "InactiveMembers.accdb;immediatelyReleaseResources=true");
        stI = connI.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        //System.out.println("SELECT Inactive.[Customer Card Number], Inactive.[Customer ID], Inactive.[First Name], Inactive.[Last Name], Inactive.[Address], Inactive.[Birth Date], Inactive.Area_Code, Inactive.Phone_1, Inactive.Phone_2 FROM Inactive WHERE (((Inactive.[First Name])='" + nameFTextfield.getText() + "') AND ((Inactive.[Last Name])='" + nameLTextfield.getText() + "') AND ((Inactive.City)<>'CLOSE'))");
        if (searchBar.isVisible()) {
            //second search with no birthday
            rsI = stI.executeQuery("SELECT Inactive.[Customer Card Number], Inactive.[Customer ID], Inactive.[First Name], Inactive.[Last Name], Inactive.[Address], Inactive.[Birth Date], Inactive.Area_Code, Inactive.Phone_1, Inactive.Phone_2 FROM Inactive WHERE (((Inactive.[First Name])='" + nameFTextfield.getText() + "') AND ((Inactive.[Last Name])='" + nameLTextfield.getText() + "') AND ((Inactive.City)<>'CLOSE'))");
        } else {
            // first search with phone only
            if (!ph1Textfield.getText().trim().isEmpty() && !ph2Textfield.getText().trim().isEmpty() && !ph3Textfield.getText().trim().isEmpty()) {
                rsI = stI.executeQuery("SELECT Inactive.[Customer Card Number], Inactive.[Customer ID], Inactive.[First Name], Inactive.[Last Name], Inactive.[Address], Inactive.[Birth Date], Inactive.Area_Code, Inactive.Phone_1, Inactive.Phone_2  FROM Inactive WHERE (((Inactive.[Area_Code])='" + ph1Textfield.getText() + "') AND ((Inactive.[Phone_1])='" + ph2Textfield.getText() + "') AND ((Inactive.[Phone_2])='" + ph3Textfield.getText() + "') AND ((Inactive.City)<>'CLOSE'))");
            } else {
            //firstsearch with Name and Birthday
            rsI = stI.executeQuery("SELECT Inactive.[Customer Card Number], Inactive.[Customer ID], Inactive.[First Name], Inactive.[Last Name], Inactive.[Address], Inactive.[Birth Date], Inactive.Area_Code, Inactive.Phone_1, Inactive.Phone_2 FROM Inactive WHERE (((Inactive.[First Name])='" + nameFTextfield.getText() + "') AND ((Inactive.[Last Name])='" + nameLTextfield.getText() + "') AND ((Inactive.City)<>'CLOSE'))");
                //rsI = stI.executeQuery("SELECT Inactive.[Customer Card Number], Inactive.[Customer ID], Inactive.[First Name], Inactive.[Last Name], Inactive.[Birth Date], Inactive.Area_Code, Inactive.Phone_1, Inactive.Phone_2  FROM Inactive WHERE (((Inactive.[First Name])='" + nameFTextfield.getText() + "') AND ((Inactive.[Last Name])='" + nameLTextfield.getText() + "') AND ((Inactive.[Birth Date])=#" + bdayString + "#))");
            }
        }

        if (rsI.last()) {
            int rows = rsI.getRow();
            if (rows < 1) {
                showError("No Records Found");
            } else {
                rsI.beforeFirst(); // Move to beginning
                while (rsI.next()) {
                    //if (!rsI.getString("City").equals("CLOSE")) {
                        Member newGame = new Member(rsI.getString("Customer Card Number"), rsI.getString("Customer ID"), rsI.getString("First Name"), rsI.getString("Last Name"), rsI.getString("Address"), rsI.getDate("Birth Date").toLocalDate(), rsI.getString("Area_Code"), rsI.getString("Phone_1"), rsI.getString("Phone_2"));
                        members.add(newGame);
                    //}
                }
                tablePane.setVisible(true);
                memberTable.setVisible(true);
                errorLabel.setText("");
                searchBar.setVisible(false);
                //nameFTextfield.requestFocus();
                //nameFTextfield.selectAll();

            }
        } else {
            if (searchBar.isVisible()) {
                if (ph2Textfield.isDisable()) { 
                    errorLabel.setText("Still No Records Found. Please check spelling.");                    
                } else {
                    errorLabel.setText("Still No Records Found. Please check Phone Number.");
                }
                searchBar.setVisible(true);
                nameFTextfield.requestFocus();
                nameFTextfield.selectAll();
            } else {
                if (ph2Textfield.isDisable()) {
                    errorLabel.setText("No Records Found - Press F7 to widen the search");
                } else {
                    errorLabel.setText("No Records Found - Try Name and Birthday Fields");
                }
                searchBar.setVisible(true);
                nameFTextfield.requestFocus();
                nameFTextfield.selectAll();
            }
        }
    }
    catch (HeadlessException | SQLException e) {
        System.err.println(e.getMessage() +"here");
    }
    finally {
        try {
        if (stI != null){
            stI.close();
        }
        if (rsI != null){
            rsI.close();
        }
        if (connI != null){
            connI.close();
        }
        } catch (SQLException ex) {
            System.out.println("Here in getGames2()");
        }
}

    return members;
}   
    
    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F2) {
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
            if (ke.getCode() == KeyCode.ESCAPE) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.TAB) {
                keyListener(ke);
                ke.consume();
                
            }
        });
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: break;
                    case F2: resetButton.fire(); break;
                    case F3: break;
                    case F4: break;
                    case F5: break;
                    case F6: searchButton.fire(); break;
                    case F7: searchagain(); break;
                    case F8: selectButton.fire(); break;
                    case F9: break;
                    case F10: break;
                    case F11: break;
                    case F12: break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: GOEnterKeyPressed(); break;
                default:
                    break;
                }
    }
    
    private void setEmp(){ //set employee number in the empTextfield
        empTextfield.setText(en);
        empTextfield.setDisable(true);
        empTextfield.setStyle("-fx-background-color: #7e7e7e;");
        //en =eFXX1.empNumber;
        //fn = eFXX1.nameF;
        //ln = eFXX1.nameL;
        //vAmt = eFXX.VAmt;
    }

    private void SetTopFieldsDisabled(int t) {
        if (t == 1) {
            nameFTextfield.setDisable(true);
            nameFTextfield.setStyle("-fx-background-color: #7e7e7e");
            nameLTextfield.setDisable(true);
            nameLTextfield.setStyle("-fx-background-color: #7e7e7e");
            bday1Textfield.setDisable(true);
            bday1Textfield.setStyle("-fx-background-color: #7e7e7e");
            bday2Textfield.setDisable(true);
            bday2Textfield.setStyle("-fx-background-color: #7e7e7e");
            bday3Textfield.setDisable(true);
            bday3Textfield.setStyle("-fx-background-color: #7e7e7e");
        } else {
            nameFTextfield.setDisable(false);
            nameFTextfield.setStyle("-fx-background-color: #FFFFFF");
            nameLTextfield.setDisable(false);
            nameLTextfield.setStyle("-fx-background-color: #FFFFFF");
            bday1Textfield.setDisable(false);
            bday1Textfield.setStyle("-fx-background-color: #FFFFFF");
            bday2Textfield.setDisable(false);
            bday2Textfield.setStyle("-fx-background-color: #FFFFFF");
            bday3Textfield.setDisable(false);
            bday3Textfield.setStyle("-fx-background-color: #FFFFFF");
        }
    }
    
    private void SetBottomFieldsDisabled(int t) {
        if (t == 1) {
            ph1Textfield.setDisable(true);
            ph1Textfield.setStyle("-fx-background-color: #7e7e7e");
            ph2Textfield.setDisable(true);
            ph2Textfield.setStyle("-fx-background-color: #7e7e7e");
            ph3Textfield.setDisable(true);
            ph3Textfield.setStyle("-fx-background-color: #7e7e7e");
        } else {
            if (t == 2) {
                ph1Textfield.setDisable(false);
                ph1Textfield.setStyle("-fx-background-color: #FFFFFF");
                ph2Textfield.setDisable(false);
                ph2Textfield.setStyle("-fx-background-color: #FFFFFF");
                ph3Textfield.setDisable(false);
                ph3Textfield.setStyle("-fx-background-color: #FFFFFF");
            }
        }
}

    private void resetFields() {
        ph2Textfield.clear();
        ph3Textfield.clear();
        nameFTextfield.clear();
        nameLTextfield.clear();
        bday1Textfield.setText("02");
        bday2Textfield.setText("02");
        bday3Textfield.setText("1994");
        errorLabel.setText("");
        searchBar.setVisible(false);
        if (members != null){
            members.clear();
        }
        if (memberTable.isVisible()) {
            memberTable.getItems().clear();
            tablePane.setVisible(false);
        }
        resetButton.setDisable(true);
        searchButton.setDisable(true);
        nameFTextfield.requestFocus();
        bday1Textfield.clear();
        bday2Textfield.clear();
        bday3Textfield.clear();
    }

   

    
}
