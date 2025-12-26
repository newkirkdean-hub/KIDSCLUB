package views.gameproblems;

import JavaMail.Mail_JavaFX1;
import XML_Code.XMLDOMParseToObservableList;
import XML_Code.writeArrayListToXMLFile;
import static commoncodes.BarcodePrinter.generateAsciiBarcode;
import commoncodes.FocusedTextFieldHighlight;
import dbpathnames.dbStringPath;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.gameproblems.Problem;
import pReceipts.print;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pWordFX.empFX;
import pWordFX.employeeFX;
import settings.settingsFXML;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class GamesProblemsController implements Initializable  {
    @FXML private TableView<Problem> probTable;
    @FXML private TableColumn<Problem, String> gameNameColumn;
    @FXML private TableColumn<Problem, String> dateColumn;
    @FXML private TableColumn<Problem, String> fixedDateColumn;
    @FXML private TableColumn<Problem, String> empNameColumn;
    @FXML private TableColumn<Problem, String> fixedNameColumn;
    @FXML private TableColumn<Problem, String> notesColumn;
    //@FXML private TableColumn<Problem, String> IDColumn;
    @FXML private Button x_it;
    @FXML private AnchorPane root;
    @FXML private Pane buttonPane;
    @FXML private Stage stageV;
    @FXML private Pane depPane;
    @FXML private PasswordField mgr;
    @FXML private TextField gameName;
    @FXML private TextArea fixedNotes;
    @FXML private Button addbtn;
    @FXML private Button delbtn;
    @FXML private Button printBtn;
    @FXML private Button updateBtn;
    @FXML private Button updateBtn1;
    
    public static String en, fn, ln, count="";
    public static int vAmt, gameListIndex; 
    public static String eNumberField, gameID, cssPath;
    public static employeeFX eFXX;
    public static empFX newEFX;;
    public static dbStringPath dbsp;

    private static String s1 = null;
    private static Connection conn = null;
    private static Statement s = null;
    private static ResultSet rs = null;
    //employeeFX eFX = new employeeFX();
    boolean addnewtrue = false;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    private static final SimpleDateFormat dFormatter = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat tFormatter = new SimpleDateFormat("hh:mm:ss a");
    private static ObservableList<Problem> data;
    private static Problem newdata;
    private static ObservableList<Problem> sortedData = null;
    //ClubFunctions CF = new ClubFunctions();
    @Override
    
    
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssPath);

        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("GameName"));
        empNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        ///dateColumn.setCellValueFactory(new PropertyValueFactory<>("newdatestring"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("newXMLdatestring"));
        ///fixedDateColumn.setCellValueFactory(new PropertyValueFactory<>("newdate2string"));
        fixedDateColumn.setCellValueFactory(new PropertyValueFactory<>("newXMLdate2string"));
        fixedNameColumn.setCellValueFactory(new PropertyValueFactory<>("fixedBy"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("Notes"));
        //gameNameColumn.setSortable(false);
        //empNameColumn.setSortable(false);
        //dateColumn.setSortable(false);
        //fixedDateColumn.setSortable(false);
        //fixedNameColumn.setSortable(false);
        notesColumn.setSortable(false);
        //IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        new FocusedTextFieldHighlight().setHighlightListenergProblem(root);

        //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, ln, "start of gameProblems after resize");
        setKeyCodes();
        Platform.runLater(() -> probTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS));
        Platform.runLater(() -> setEmp());
        Platform.runLater(() -> getTableItems());
        Platform.runLater(() -> resetFields());
        Platform.runLater(() -> setCloseCatch());
    }
    
    
    
    

    private void getTableItems() {
        probTable.getItems().clear();
        ///try {
        ///    probTable.getItems().addAll(getdataB());
            probTable.getItems().addAll(getGproblemsDataXML());
        probTable.setFixedCellSize(30.0);
        ///} catch (SQLException ex) {
        ///    System.out.println(ex);
        ///}
        probTable.setEditable(true);
        //tablesort();
        //C2.setCellFactory(TextFieldTableCell.forTableColumn());

    }



    public ObservableList<Problem> getGproblemsDataXML() {
        ObservableList<Problem> gameProblems = FXCollections.observableArrayList();
        try {
            Document document = new XMLDOMParseToObservableList().parseXmlFile(dbsp.pathNameXML + "Problems.xml");
            //System.out.println("1");
            gameProblems = extractDataFromXmlTOObservableList(document);

            //Collections.reverse(gameProblems);
        } catch (Exception e) {
            System.out.println("-1- " + e + " " + dbsp.pathNameXML);
            new Mail_JavaFX1().sendEmailTo("Game Problem Error", "-1- " + e + " " + dbsp.pathNameXML, "errors");
        }
        return gameProblems;
    }

    public ObservableList<Problem> extractDataFromXmlTOObservableList(Document document) {
        data = FXCollections.observableArrayList();

        NodeList nodeList = document.getElementsByTagName("Problems");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                // Extract the data from the element
                String gamepName = element.getElementsByTagName("Game_Name").item(0).getTextContent();
                String emppName = element.getElementsByTagName("Name").item(0).getTextContent();
                String dpDate = element.getElementsByTagName("Date").item(0).getTextContent();
                String dpDateFixed = element.getElementsByTagName("Date_Fixed").item(0).getTextContent();
                String fixedpBy = element.getElementsByTagName("Fixed_By").item(0).getTextContent();
                String fixedpNotes = element.getElementsByTagName("Fixed_Notes").item(0).getTextContent();
                String idp = element.getElementsByTagName("ID").item(0).getTextContent();

                System.out.println(gamepName + " " + emppName + " " + dpDate + " " + dpDateFixed + " " + fixedpBy + " " + fixedpNotes + " " + idp);
                data.add(new Problem(gamepName, emppName, dpDate, dpDateFixed, fixedpBy, fixedpNotes, idp));
            }
        }
        return data;
    }

    private void saveToXMLFile(ObservableList<Problem> sortedData) {
        //ArrayList<Problem> gProblems = probTable.getItems();
        //ObservableList<Problem> gProblems = probTable.getItems();
        ArrayList<Problem> gProblemList = new ArrayList<>(sortedData);
        //Collections.reverse(gProblems);    
        new writeArrayListToXMLFile().writeGproblemsToXmlFile(gProblemList);
    }




    public void GoDeletButton() {
        //String df = dbStringPath.tblrowid;
        TablePosition pos = probTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        String df = probTable.getItems().get(row).getID();
        String emp = en;
        if (emp.trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "STOP", "Emp Number Cannot be Empty");
            //JOptionPane.showMessageDialog(null, "Emp Number Cannot be Empty");
            mgr.requestFocus();
            return;
        }
        Boolean nmgr;

        //RemoveFromTable
        data = probTable.getItems();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getID().equals(df)) {
                System.out.println("this is where the item is :" + i + " data ID = " + data.get(i).getID());
                data.remove(i);
            }
        }

        saveToXMLFile(data);

        //try {
        //    conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbStringPath.pathNameClubDBs + "GameProblems.accdb");
        //    s = conn.createStatement();
        //    String sql = "DELETE * FROM [Problems] where ID=" + df;
        //    s.executeUpdate(sql);
        //} catch (SQLException ex) {
        //    new Mail_JavaFX1().sendEmailTo("Game Problem Error", ex.toString(), "errors");
        //JOptionPane.showMessageDialog(null, ex);
        //} finally {
        //try { rs.close(); } catch (Exception ec) {/* ignored */}
        //    try {
        //  s.close();
        //    } catch (SQLException ec) {
        //    /* ignored */ }
        //    try {
        //        conn.close();
        //    } catch (SQLException ec) {
        //      /* ignored */ }
        //}
        //getTableItems();
        sortedData = tablesort(data);
        saveToXMLFile(sortedData);
        //refreshTable(sortedData);
        getTableItems();
        resetFields();
        probTable.getSelectionModel().clearSelection();

    }

    public void GoAddNewButton() {
        //System.out.println("here we are in go new button");
        String newID = "0";
        int lastID = 0;
        ObservableList<Problem> sortedData = null;
        gameName.setText(gameName.getText().toUpperCase());
        s1 = fixedNotes.getText().trim();
        String fixednotes = s1.replaceAll("'", "");
        String s2 = gameName.getText().trim();
        String gamename = s2.replaceAll("'", "");
        if (gameName.getText().trim().isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Game Name Cannot be Blank");
            new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "STOP", "Game Name Cannot be Blank");
            gameName.requestFocus();
            return;
        } else {
            gameName.setText(gameName.getText().toUpperCase());
        }
        String emp = en;
        if (emp.trim().isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Emp Number Cannot be Empty");
            new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "STOP", "Emp Number Cannot be Empty");
            mgr.requestFocus();
            return;
        }
        data = probTable.getItems();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getID().equals(gameID)) {
                System.out.println("this is where the item is :" + i + " data ID = " + data.get(i).getID());
                return;
            }
        }
        Date dayDate = new Date();
        Date f = new Date(dayDate.getTime());
        String stringDate = formatter.format(f);

        //HERE BELOW IS THE XML CODE
        if (data.isEmpty()) {
            newID = "3456";
        } else {
            //data.sort( (a, b) -> { return -1 * a.compareTo(b); } );
            sortedData = data.sorted(Comparator.comparing(Problem::getID).reversed());
            //System.out.println("Below is sorted list");
            //System.out.println(sortedData);
            //String newIDS = "";
            for (int i = 0; i < sortedData.size(); i++) {
                lastID = Integer.parseInt(sortedData.get(0).getID());
                System.out.println("this is lastID:" + lastID);
            }
            //int newIDS2 = Integer.parseInt(lastID) + 1;
            newID = String.valueOf(lastID + 1);
        }
        //data = probTable.getItems();
        newdata = new Problem(gameName.getText(), fn + " " + ln.substring(0, 1), stringDate, "|", "|", fixednotes, newID);
        data.add(0, newdata);
        //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, ln, "here 6 " + newdata.getGameName() + " " + newdata.getID());

        //HERE BELOW IS THE OLD DATABASE CODE
        //try{
        //    conn=DriverManager.getConnection("jdbc:ucanaccess://" + dbStringPath.pathNameClubDBs + "GameProblems.accdb");
        //    s = conn.createStatement();
        //    String q = "INSERT INTO Problems (Game_Name, Fixed_Notes, Name, [Date]) VALUES('" + gamename + "', '" + fixednotes + "', '" + fn + " " + ln.substring(0, 1) + "', #" + formatter.format(f) + "#)";
        //JOptionPane.showMessageDialog(null, q);
        //    s.executeUpdate(q);
        //} catch(SQLException eq) {
        //    new Mail_JavaFX1().sendEmailTo("Game Problem Error", gamename + " \t" + fixednotes + " \t" + fn + " \t" + f + " \t\n\n" + eq, "errors");
        //JOptionPane.showMessageDialog(null, eq);
        //    return;
        //} finally {
        //    try { s.close(); } catch (SQLException ec) { /* ignored */ }
        //    try { conn.close(); } catch (SQLException ec) { /* ignored */ }
        //}
        //getTableItems();
        addnewtrue = false;
        //System.out.println("Right before email");
        if (getPropertiesFile() == 1) {
            sendEmail(gameName.getText(), s1, fn, formatter.format(f));
        }
        //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, ln, "here 7");
        sortedData = tablesort(data);
        //data = sortedData;
        saveToXMLFile(sortedData);
        getTableItems();
        resetFields();
        probTable.getSelectionModel().select(0);

    }

    public void GOpartsOrderedButton() {
        if (gameName.getText().trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "STOP", "Game Name Cannot be Blank");
            //JOptionPane.showMessageDialog(null, "Game Name Cannot be Blank");
            gameName.requestFocus();
            return;
        } else {
            String lGameName = gameName.getText();
            gameName.setText(lGameName.toUpperCase());
        }
        String emp = en;
        if (emp.trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "STOP", "Emp Number Cannot be Blank");
            //JOptionPane.showMessageDialog(null, "Emp Number Cannot be Empty");
            mgr.requestFocus();
            return;
        }
        /*try {
                //if (!e.isemployeevalid(emp)) {
                    return;
                }   } catch (FileNotFoundException ex) {
                Logger.getLogger(GproblemsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        Date dayDate = new Date();
        //System.out.println("================================ " + dayDate + " ");
        Date f = new Date(dayDate.getTime());
        TablePosition pos = probTable.getSelectionModel().getSelectedCells().get(0);
        String stringFixedDate = dFormatter.format(f);
        int row = pos.getRow();
        String value = probTable.getItems().get(row).getID();
        String newFixedNotes = "Parts Ordered " + dFormatter.format(f) + " " + fn + " \n\n" + fixedNotes.getText();

        //data = probTable.getItems();
        //newdata = new Problem(gameName.getText(), fn + " " + ln.substring(0, 1), stringDate, "|", "|", fixednotes, newID);
        /*
        ObservableList<Toys> Toylistremove = SalesTable.getItems();
        Toys tlist = getToyFromArrayList(t.getNumber(), 1);
        for(int i= 0; i<Toylistremove.size();i++) {
        if (Toylistremove.get(i).getName().equals(tlist.getName())) {
        System.out.println("this is where the item is :" + i + " tlist.getQTY() = " + Toylistremove.get(i).getQTY() +  " " + q);
        toyListIndex = i;
         remove = true;
        //Toylistremove.clear();
        }
        }
         */
        data = probTable.getItems();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getID().equals(gameID)) {
                System.out.println("================================ " + gameID + " " + data.get(i).getID() + " " + data.get(i).getNewXMLdatestring());
                gameListIndex = i;
                data.set(i, new Problem(gameName.getText(), data.get(i).getName(), data.get(i).getNewXMLdatestring(), stringFixedDate, "Parts Ordered", newFixedNotes, data.get(i).getID()));
                //data.set(vAmt, newdata);
            }
        }
        //newdata = new Problem(gameName.getText(), fn + " " + ln.substring(0, 1), stringDate, "|", "|", fixednotes, newID);
        //data.add(newdata);
        //data.addFirst(newdata);
        //listMemberSalesItems = FXCollections.observableList(list);

        //int row = jTableExport.getSelectedRow();
        //String value = jTableExport.getModel().getValueAt(row, 7).toString();
        ///try {
        ///    conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "GameProblems.accdb");
        ///    s = conn.createStatement();
        ///    String q = "UPDATE Problems SET Problems.Game_Name = '" + gameName.getText() + "', Problems.Date_Fixed = #" + formatter.format(f) + "#, Problems.Fixed_By = 'Parts Ordered', Problems.Fixed_Notes = '" + newFixedNotes + "' WHERE (((Problems.ID)=" + value + "));";
        ///    s.executeUpdate(q);
        ///} catch (SQLException eq) {
        ///    new Mail_JavaFX1().sendEmailTo("Game Problem Error", gameName.getText() + " \t" + s1 + " \t" + fn + " \t" + f + " \t\n\n" + eq, "errors");
            //JOptionPane.showMessageDialog(null, eq);
        ///    return;
        ///} finally {
        ///    try {
        ///        s.close();
        ///    } catch (SQLException ec) {
        ///        /* ignored */ }
        ///    try {
        ///        conn.close();
        ///    } catch (SQLException ec) {
        ///        /* ignored */ }
        ///}
        addnewtrue = false;
        sortedData = tablesort(data);
        saveToXMLFile(sortedData);
        getTableItems();
        resetFields();
        probTable.getSelectionModel().select(getTableRowIndex(sortedData, value));

        //getTableItems();
        //probTable.getSelectionModel().select(row);
    }

    public void GOfixedButton() {
        if (gameName.getText().trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "STOP", "Game Name Cannot be Blank");
            //JOptionPane.showMessageDialog(null, "Game Name Cannot be Blank");
            gameName.requestFocus();
            return;
        } else {
            String lGameName = gameName.getText();
            gameName.setText(lGameName.toUpperCase());
        }
        String emp = en;
        if (emp.trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "STOP", "Emp Number Cannot be Empty");
            //JOptionPane.showMessageDialog(null, "Emp Number Cannot be Empty");
            mgr.requestFocus();
            return;
        }
        /*try {
                //if (!e.isemployeevalid(emp)) {
                    return;
                }   } catch (FileNotFoundException ex) {
                Logger.getLogger(GproblemsDialog.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        Date dayDate = new Date();
        Date f = new Date(dayDate.getTime());
        String stringFixedDate = dFormatter.format(f);
        TablePosition pos = probTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        String value = probTable.getItems().get(row).getID();
        String newFixedNotes = "FIXED " + dFormatter.format(f) + " " + fn + "\n\n" + fixedNotes.getText();

        data = probTable.getItems();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getID().equals(gameID)) {
                System.out.println("================================ " + gameID + " " + data.get(i).getID() + " " + data.get(i).getNewXMLdatestring());
                gameListIndex = i;
                data.set(i, new Problem(gameName.getText(), data.get(i).getName(), data.get(i).getNewXMLdatestring(), stringFixedDate, "FIXED", newFixedNotes, data.get(i).getID()));
                //data.set(vAmt, newdata);
            }
        }

        //int row = jTableExport.getSelectedRow();
        //String value = jTableExport.getModel().getValueAt(row, 7).toString();
        //try {
        //    conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbStringPath.pathNameClubDBs + "GameProblems.accdb");
        //    s = conn.createStatement();
        //    String q = "UPDATE Problems SET Problems.Game_Name = '" + gameName.getText() + "', Problems.Date_Fixed = #" + formatter.format(f) + "#, Problems.Fixed_By = 'Fixed', Problems.Fixed_Notes = '" + newFixedNotes + "' WHERE (((Problems.ID)=" + value + "));";
        //    s.executeUpdate(q);
        //} catch (SQLException eq) {
        //    new Mail_JavaFX1().sendEmailTo("Game Problem Error", gameName.getText() + " \t" + s1 + " \t" + fn + " \t" + f + " \t\n\n" + eq, "errors");
        //    //JOptionPane.showMessageDialog(null, eq);
        //    return;
        //} finally {
        //    try {
        //        if (s!=null) {
        //            s.close();
        //        }
        //        if (conn !=null){ 
        //            conn.close();
        //        }
        //    } catch (SQLException ec) {
        //        /* ignored */ }
        //}
        addnewtrue = false;
        sortedData = tablesort(data);
        saveToXMLFile(sortedData);
        getTableItems();
        resetFields();
        probTable.getSelectionModel().select(getTableRowIndex(sortedData, value));

        //probTable.getSelectionModel().clearSelection();
        //getTableItems();
        //probTable.getSelectionModel().select(row);
    }

    private int getTableRowIndex(ObservableList<Problem> ol, String v) {
        int listIndex = 0;
        for (int i = 0; i < ol.size(); i++) {
            if (sortedData.get(i).getID().equals(v)) {
                System.out.println("================================ " + gameID + " " + data.get(i).getID() + " " + data.get(i).getNewXMLdatestring());
                listIndex = i;
            }
        }
        return listIndex;
    }



    public void GOprintButton() {
        String value = "";
        String value1 = "";
        try {
            ///   conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbStringPath.pathNameClubDBs + "GameProblems.accdb");
         ///   s = conn.createStatement();
        ///    rs = s.executeQuery("SELECT Problems.Game_Name, Problems.Date, Problems.Fixed_Notes, Problems.Date_Fixed FROM Problems WHERE (((Problems.Date_Fixed) Is Null));");

            
            data = probTable.getItems();
            for (int i = 0; i < data.size(); i++) {
                if (!data.get(i).getNewXMLdate2string().equals("|")) {
                    System.out.println("here in GoPrintButton 2" + data.get(i).getGameName());

                } else {
                    //System.out.println("this is where the item is :" + i + " data ID = " + data.get(i).getGameName());

                    PrintWriter pw = null;
                    Date f = new Date();
                    pw = new PrintWriter(new File(dbStringPath.pathNameLocal + "GProblemsList.txt"));
                    pw.println("Pojos of ID. Inc."); // to test if it works.
                    pw.println("Game Problems List"); // to test if it works.
                    pw.println("======================"); // to test if it works.
                    pw.println(f);

                    System.out.println("here in GoPrintButton3");

                    ///while (rs.next()) {
                 //JOptionPane.showMessageDialog(null, dFormatter.format(rs.getDate("Date")) + " " + tFormatter.format(rs.getTime("Date")));
           ///   value = rs.getString("Game_Name") + " " + dFormatter.format(rs.getDate("Date")) + " " + tFormatter.format(rs.getTime("Date"));
                 ///   value1 = rs.getString("Fixed_Notes");
                 String[] arr = data.get(i).getNewXMLdatestring().split(" ");
                    //Date date1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(arr[0]);
                    //String newstring = new SimpleDateFormat("MM/dd/yyyy").format(date1);
                    value = data.get(i).getGameName() + " " + arr[0] + " " + arr[1] + " " + arr[2];
                    value1 = data.get(i).getNotes();
                    pw.println("");
                    pw.println("");
                    pw.println(value.trim());
                    pw.println("");
                    pw.println(value1.trim());
                    pw.println("");
                    pw.println("");
                    //pw.println("");

                    pw.close();
                }
            }
         ///} catch (SQLException ex) {
        ///    new Mail_JavaFX1().sendEmailTo("Game Problem Error", gameName.getText() + " \t" + s1 + " \t" + fn + " \t" + ex, "errors");
            //JOptionPane.showMessageDialog(null, ex);
        } catch (FileNotFoundException ex) {
            System.out.println("11111 " + ex);
        }
        ///} finally {
            try {
            if (rs != null) {
                rs.close();
            }
            if (s != null) {
                s.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ec) {
            /* ignored */ }
        ///}
        print pr = new print();
        pr.printReceipt("GProblemsList.txt");

        gameName.requestFocus();
    

    ///}   catch (FileNotFoundException ex) {
    ///        Logger.getLogger(GamesProblemsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    

    public int getPropertiesFile() {
        new dbStringPath().setName();
        int p = 0;
        File file = new File(new dbStringPath().pathNameLocal + "configCounterFXML.properties");
        if (file.exists() && file.canRead()) {
            p = Integer.parseInt(new settingsFXML().getCounterSettings("stage"));
        }

        return p;
    }

    private void sendEmail(String gName, String details, String fName, String d) {
        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date 
        int t = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        calendar.get(Calendar.HOUR);        // gets hour in 12h format
        calendar.get(Calendar.MONTH);
        if (t >= 10 && t <= 17) {
            new Mail_JavaFX1().sendEmailTo("Game Problem", gName + " \t" + fName + " \t" + d + "\n" + details, "gameProblems");
        }
    }

    private ObservableList<Problem> tablesort(ObservableList<Problem> d) {
        sortedData = null;
        sortedData = d.sorted(Comparator.comparing(Problem::getID).reversed());
        return sortedData;
    }


    public void resetFields() {
        x_it.setText("Exit");
        addbtn.setVisible(true);
        mgr.setText(en);
        switch (vAmt) {
            case 1:
                delbtn.setVisible(false);
                updateBtn.setVisible(false);
                updateBtn1.setVisible(false);
                printBtn.setVisible(false);
                fixedNotes.setVisible(true);
                break;
            case 2:
                delbtn.setVisible(false);
                updateBtn.setVisible(true);
                updateBtn1.setVisible(false);
                fixedNotes.setVisible(true);
                printBtn.setVisible(true);
                break;
            case 3:
                delbtn.setVisible(true);
                updateBtn.setVisible(true);
                updateBtn1.setVisible(true);
                fixedNotes.setVisible(true);
                printBtn.setVisible(true);
                break;
            default:
                delbtn.setVisible(false);
                updateBtn.setVisible(false);
                fixedNotes.setVisible(false);
                printBtn.setVisible(false);
        }
        gameName.clear();
        fixedNotes.clear();
        gameName.requestFocus();
        gameListIndex = 0;
        gameID = "";
        probTable.setFixedCellSize(30.0);
        //probTable.getSelectionModel().clearSelection();
        //tablesort();


        
    }

    
    //THIS VOID IS USED ON THE FXML FILE ONLY UNDER THE ONCLICK OF THE TABLE
    public void getTableRow() {
    
    Problem tableRowValue = probTable.getSelectionModel().getSelectedItem();
    gameID = tableRowValue.getID();
    System.out.println("here in tableRowValue " + gameID);
    gameName.setText(tableRowValue.getGameName());
    System.out.println("here in tableRowValue " + gameName);
    fixedNotes.setText(tableRowValue.getNotes());
    System.out.println("here in tableRowValue " + fixedNotes);
    eNumberField = tableRowValue.getID();
    System.out.println("here in tableRowValue " + eNumberField);

    }


    private void getStageV() {
        stageV = (Stage) x_it.getScene().getWindow();
    }
     
    private void setCloseCatch() {
        getStageV();
        stageV.setOnCloseRequest((WindowEvent we) -> {
            System.out.println("Stage is closing");
        });
    }   

    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: printBtn.fire(); break;
                    case F2: GoAddNewButton(); break;
                    case F3: updateBtn.fire(); break;
                    case F4: break;
                    case F5: break;
                    case F6: break;
                    case F7: break;
                    case F8: updateBtn1.fire(); break;
                    case F9: break;
                    case F10: break;
                    case F11: break;
                    case DOWN: break;
                    case TAB: DoEnterKeyPressed(event); break;
                    case ESCAPE: x_it.fire(); break;
                    case ENTER: DoEnterKeyPressed(event); break;
                default:
                    break;
                }
    }   
    
    private void DoEnterKeyPressed(KeyEvent event) {
        if (fixedNotes.isFocused()) {
            if (gameName.getText().isEmpty()) {
                event.consume();
                gameName.requestFocus();
            } else {
                event.consume();
                addbtn.fire();
            }
            return;
        }
        if (gameName.isFocused()) {
            if (gameName.getText().isEmpty()) {
                new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "STOP", "Emp Number Cannot be Empty");
                //JOptionPane.showMessageDialog(null, "Game Name Cannot be blank");
                gameName.requestFocus();
                return;
            } else {
                gameName.setText(gameName.getText().toUpperCase());
                fixedNotes.requestFocus();
                event.consume();
            }
        }
    }
        
    private void setKeyCodes() {
         root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F3) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F4) {
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
            if (ke.getCode() == KeyCode.TAB) {
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
    
    private void setEmp() {
        mgr.setText(newEFX.empNumber);
        mgr.setStyle("-fx-background-color: #d3d3d0;");
        mgr.setDisable(true);
        en = newEFX.empNumber;
        fn = newEFX.nameF;
        ln = newEFX.nameL;
        vAmt = newEFX.GProb;
    }

    public void exitProcessDO() {
        if (!gameName.getText().isEmpty()) {
            resetFields();
        } else {
            if (!data.isEmpty()) {
                data.clear();
            }
            gameID = "";
            gameListIndex = 0;
            eFXX=null;
            Stage stage = (Stage) x_it.getScene().getWindow();
            stage.close();
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
        /*private void refreshTable(ObservableList<Problem> sortedData) {
        ObservableList<Problem> gameProblems = FXCollections.observableArrayList();
        gameProblems = sortedData;
        probTable.getItems().clear();
        probTable.getItems().addAll(gameProblems);
        probTable.setFixedCellSize(30.0);
        
    }*/

       /* public ObservableList<Problem> getdataB() throws SQLException {
        
        
    ObservableList<Problem> data = FXCollections.observableArrayList();

  try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "GameProblems.accdb");
            s = conn.createStatement();
            rs = s.executeQuery("SELECT Problems.* FROM Problems ORDER BY Problems.ID DESC;");
                                                    //Game_Name	Name	Date	Date_Fixed	Fixed_By	Fixed_Notes	ID
            while (rs.next()) {
                newdata = new Problem(rs.getString("Game_Name"), rs.getString("Name"), rs.getString("Date"), rs.getString("Date_Fixed"), rs.getString("Fixed_By"), rs.getString("Fixed_Notes"), rs.getString("ID"));
                //Problem newdata = new Problem(rs.getString("Game_Name"), rs.getString("Name"), rs.getString("Date"), rs.getString("Fixed_By"), rs.getString("Fixed_Notes"), rs.getString("ID"));
                data.add(newdata);
            }
    }
    catch (SQLException e) {
        new Mail_JavaFX1().sendEmailTo("Game Problem Error", e.toString(), "errors");
        System.err.println("Game Problem Model Error: " + e.getMessage());
    }
    finally {
      if (s != null);
      s.close();
      if (rs != null);
      rs.close();
      if (conn != null);
      conn.close();
}

    return data;
}   
*/
    
}
