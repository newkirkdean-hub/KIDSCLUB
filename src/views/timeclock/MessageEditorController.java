package views.timeclock;

import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import XML_Code.AddXmlNode;
import XML_Code.ModifyXMLFileInJava;
import XML_Code.XMLDOMParseToObservableList;
import commoncodes.ClubFunctions1;
import commoncodes.FocusedTextFieldHighlight;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import messageBox.messageBoxFXController;
import models.settings.GreetingsClub;
import models.timeclock.message;
import models.timeclock.messageDB;
import models.timeclock.tClock;
import models.timeclock.tClockDB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;


/**
 * FXML Controller class
 *
 * @author Dean
 */
public class MessageEditorController implements Initializable {
    @FXML private Pane root;
    @FXML private Button x_it;
    @FXML private Button updateBtn;
    @FXML private Button delbtn;
    @FXML private Button addbtn;
    @FXML private Button viewButton;
    @FXML private Label c1L;
    @FXML private TextArea fixedNotes;
    @FXML private TextArea titelField;
    @FXML public static int rowID;
    @FXML private HBox jPanel1;
    @FXML private DatePicker dateTF;
    @FXML private DatePicker dateTF1;

    @FXML private TableView<message> timeTable;
    @FXML private TableColumn<message, LocalDate> dateStart;
    @FXML private TableColumn<message, LocalDate> dateEnd;
    @FXML private TableColumn<message, String> dbFixedNotes;
    @FXML private TableColumn<message, String> dbTitle;
    
    
    
    employeeFX eFX = new employeeFX();
    messageDB DB = new messageDB();
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    dbStringPath dbsp = new dbStringPath();
    String timeTableID = "", typedText = "";
    message t = null;
    ObservableList<tClock> r = null;
    SortedList<tClock> sortedList = null;
    FilteredList<tClock> filterData = null;
    DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    ContextMenu contextMenu = new ContextMenu();
    Bounds boundsInScene;
    private static Stage stageV;
    private static String empMSGPath = "";
    ObservableList<message> data = FXCollections.observableArrayList();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssC.cssPath());
        jPanel1.setVisible(false);
        setKeyCodes();
        addTextfieldListeners();
        new FocusedTextFieldHighlight().setHighlightListener(root);
        dateTF.setValue(LocalDate.now());
        dateTF1.setValue(LocalDate.now());

        dateStart.setCellValueFactory(new PropertyValueFactory<>("dateStart"));
        dateStart.setCellFactory(column -> {
            return new TableCell<message, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });
        
        dateEnd.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
        dateEnd.setCellFactory(column -> {
            return new TableCell<message, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });
        dbFixedNotes.setCellValueFactory(new PropertyValueFactory<>("HTML"));
        dbTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        empMSGPath = dbsp.pathNameXML + "empMSG.xml";
        Platform.runLater(() -> getStageV());
        Platform.runLater(() -> GetDB());        
        Platform.runLater(() -> fixedNotes.requestFocus());

    }

    
    
    private void GetDB() {
        ///try {
            timeTable.getItems().clear();
            data.clear();
            timeTable.getItems().addAll(getMessageDataXML());            
            ///timeTable.getItems().addAll(DB.getdataMessage());
            timeTable.setFixedCellSize(30.0);
        ///} catch (SQLException ex) {
        ///    System.out.println(ex);
        ///} 
    }
    
    
    
    
        // - - -HERE IS WHERE THE XML WILL GO - - - - - 
     public ObservableList<message> getMessageDataXML() {
        ObservableList<message> annoucement = FXCollections.observableArrayList();
        try {
            Document document = new XMLDOMParseToObservableList().parseXmlFile(dbsp.pathNameXML + "empMSG.xml");
            //System.out.println("1");
            annoucement = extractDataFromXmlTOObservableList(document);
        System.out.println(" == = = = = = here = = = = = = 2");

            //Collections.reverse(gameProblems);
        } catch (Exception e) {
            System.out.println("-1- " + e + " " + dbsp.pathNameXML);
            new Mail_JavaFX1().sendEmailTo("Game Problem Error", "-1- " + e + " " + dbsp.pathNameXML, "errors");
        }
        return annoucement;
    }
    
    
    public ObservableList<message> extractDataFromXmlTOObservableList(Document document) {
        //data = FXCollections.observableArrayList();

        NodeList nodeList = document.getElementsByTagName("empMSG");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                // Extract the data from the element
                String msg = element.getElementsByTagName("MSG").item(0).getTextContent();
                String stDate = element.getElementsByTagName("stDate").item(0).getTextContent();
                String endDate = element.getElementsByTagName("endDate").item(0).getTextContent();
                String title = element.getElementsByTagName("Title").item(0).getTextContent();
                String msgID = element.getElementsByTagName("ID").item(0).getTextContent();

                //System.out.println("msg: " + msg + " \n stDate " + stDate + " ID " + "r");
                data.add(new message(msg, LocalDate.parse(stDate.substring(0, 10)), LocalDate.parse(endDate.substring(0, 10)), title, msgID));
            }
        }
        System.out.println("this is the size od Data: " + data.size());
        return data;
    }


    // - - - HERE IS WHERE THE XML ENDS - - -  - 
    
    
    
    
    
    
    
    public void Update() {
        Date dayDate = new Date();
        Date f = new Date(dayDate.getTime());
        if (fixedNotes.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Message Box Cannot be Empty");
            return;
        }
        //System.out.println("==================== 123" + dateTF.getValue() + " " + dateTF1.getValue());
        message empMSG = new message(fixedNotes.getText(), dateTF.getValue(), dateTF1.getValue(), titelField.getText(), timeTableID);
        new ModifyXMLFileInJava().ModifyXMLEmpMSG(empMSGPath, empMSG);
        
        
        
        ///String q = "UPDATE empMSG SET empMSG.stDate = #" + formatter.format(new ClubFunctions1().asSQLDate(dateTF.getValue())) + "#, empMSG.endDate = #" + formatter.format(new ClubFunctions1().asSQLDate(dateTF1.getValue())) + "#, empMSG.MSG = '" + fixedNotes.getText() + "', empMSG.title = '" + titelField.getText() + "' WHERE (((empMSG.ID)=" + timeTableID + "));";
        ///new messageDB().UpdateRec(q);
        GetDB();
        addbtn.setVisible(true);
        fixedNotes.setText("");
        titelField.setText("");
    }                               
    
    
    
    public void Add() {
        dbsp.setName();
        Date dayDate = new Date();
        Date f = new Date(dayDate.getTime());
        if (fixedNotes.getText().isEmpty()) {
            dateTF.setValue(LocalDate.now());
            dateTF1.setValue(LocalDate.now());
            fixedNotes.setText(" <HTML>\n"
                    + "<body>\n"
                    + "<center>\n"
                    + "<body style=\"background-color:Lightblue;\">\n"
                    + "<h1>TYPE MSG TITLE HERE</h1> \n"
                    + "<p style=\"font-size:35px;\">\n"
                    + "\n"
                    + "TYPE MSG BODY HERE. . . .\n"
                    + "<br>\n"
                    + "<br>\n"
                    + "<br>\n"
                    + "<br>\n"
                    + "<br>\n"
                    + "<br>\n"
                    + "<br>\n"
                    + "<br>\n"
                    + "\n"
                    + "</body>\n"
                    + "</HTML> ");
            titelField.setText("SET TITLE or TV NAME/IP ADDRESS");
            x_it.setText("Clear");
            jPanel1.setVisible(true);
            updateBtn.setVisible(false);
            //JOptionPane.showMessageDialog(null, "Message Box Cannot be Empty");
            return;
        }
        int newID = Integer.parseInt(data.get(data.size()-1).getC9()) + 1;

        
        message empMSG = new message(fixedNotes.getText(), dateTF.getValue(), dateTF1.getValue(), titelField.getText(), String.valueOf(newID));
        try {
        System.out.println("got this far ===========================================1 " + empMSGPath);
            new AddXmlNode().addXMLempMSG(empMSGPath, empMSG);
        System.out.println("got this far ===========================================2");
            GetDB();
        System.out.println("got this far ===========================================3");
            //#" + formatter.format(new ClubFunctions1().asSQLDate(dateTF.getValue())) +
            //"#, #" + formatter.format(new ClubFunctions1().asSQLDate(dateTF1.getValue())) + "#,
            //'" + fixedNotes.getText() + "', '" + titelField.getText()
        } catch (Exception ex) {
            System.out.println(ex);
        }
        updateBtn.setVisible(true);
        fixedNotes.setText("");
        titelField.setText("");
        ///String q = "INSERT INTO empMSG (stDate, endDate, MSG, title) VALUES(#" + formatter.format(new ClubFunctions1().asSQLDate(dateTF.getValue())) + "#, #" + formatter.format(new ClubFunctions1().asSQLDate(dateTF1.getValue())) + "#, '" + fixedNotes.getText() + "', '" + titelField.getText() + "')";
        ///new messageDB().AddRec(q);
        ///GetDB();
    }               
    
    
    
    
    
    
    
    private void getStageV() {
        stageV = (Stage) x_it.getScene().getWindow();
    }
    
    
    public void viewButtonPushed() {
        if (fixedNotes.equals("")) {
            JOptionPane.showMessageDialog(null, "Please Select a Message to View");
            return;
        } else {
        try {
        FXMLLoader fxmlLoader = new FXMLLoader();
        messageBoxFXController wController = (messageBoxFXController) fxmlLoader.getController();
        wController.tString = fixedNotes.getText();
            sc.getpassWord(stageV, "/messageBox/messageBoxFX.fxml", "Number", ":", 200.0, 00.0);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        }
       // new messageBox1().showTimeClockMessage(Alert.AlertType.NONE, stageV, "Important Message", fixedNotes.getText());
    }
    
    public void addTextfieldListeners() {
        fixedNotes.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.length() > 0) {
                        x_it.setText("Clear");
                    } else {
                        x_it.setText("Exit");
                    }
                }
        );

    }

    private void getToRowID() {
        timeTable.requestFocus();
        timeTable.getItems().stream()
                .filter(item -> (item.getC9() == null ? timeTableID == null : item.getC9().equals(timeTableID)))
                .findAny()
                .ifPresent(item -> {
                    timeTable.getSelectionModel().select(item);
                    timeTable.scrollTo(item);
                });
    }

    public void DeleteButtonPushed() {
        new tClockDB().deleteTimeRecord(timeTableID);
        fixedNotes.requestFocus();
    }

    public void getTableRow() throws IOException {
        TablePosition pos = timeTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        timeTableID = timeTable.getItems().get(row).getC9();
        t = timeTable.getSelectionModel().getSelectedItem();
        dateTF.setValue(t.getDateStart());
        dateTF1.setValue(t.getDateEnd());
        fixedNotes.setText(t.getHTML());
        titelField.setText(t.getTitle());
        x_it.setText("Clear");
        addbtn.setVisible(false);
        jPanel1.setVisible(true);
    }

    private void enterKeyPressed() {
        if (dateTF.isFocused()) {
            dateTF1.requestFocus();
            return;
        }
        if (dateTF1.isFocused()) {
            fixedNotes.requestFocus();
            return;
        }
        if (fixedNotes.isFocused()) {
            titelField.requestFocus();
            return;
        }
        if (titelField.isFocused()) {
            dateTF.requestFocus();
            return;
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
            if (ke.getCode() == KeyCode.F11) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F12) {
                keyListener(ke);
                ke.consume();
            }
            /*if (ke.getCode() == KeyCode.DOWN) {
                keyListener(ke);
                ke.consume();
            }*/
            if (ke.getCode() == KeyCode.ESCAPE) {
                keyListener(ke);
                ke.consume();
            }
            /*if (ke.getCode() == KeyCode.ENTER) {
                keyListener(ke);
                ke.consume();
            }*/
            /*if (ke.getCode() == KeyCode.DOWN) {
                keyListener(ke);
                ke.consume();
            }*/
            if (ke.getCode() == KeyCode.TAB) {
                keyListener(ke);
                ke.consume();
            }
        });
    }

    public void keyListener(KeyEvent event) {
        switch (event.getCode()) {
            case F1:
                break;
            case F2:
                break;
            case F3:
                break;
            case F4:
                break;
            case F5:
                break;
            case F6:
                break;
            case F7:
                break;
            case F8:
                break;
            case F9:
                break;
            case F10:
                break;
            case F11:
                break;
            case DOWN:
                break;
            case TAB:
                enterKeyPressed();
                break;
            case UP:
                break;
            case ESCAPE:
                //resetExit();
                x_it.fire();
                break;
            case ENTER:
                //enterKeyPressed();
                break;
            default:
                break;
        }
    }


    public void resetExit() {
        if (x_it.getText().equals("Clear")) {
            dateTF.setValue(LocalDate.now());
            dateTF1.setValue(LocalDate.now());
            fixedNotes.setText("");
            titelField.setText("");
            updateBtn.setVisible(true);
            addbtn.setVisible(true);
            x_it.setText("Exit");
        }else {
            try {
                exitButtonPushed();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

    public void exitButtonPushed() throws IOException {
        stageV = (Stage) x_it.getScene().getWindow();
        stageV.close();
    }

    

}
