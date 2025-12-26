
package views.settings;

import JavaMail.Mail_JavaFX1;
import XML_Code.AddXmlNode;
import XML_Code.ModifyXMLFileInJava;
import XML_Code.XMLDOMParseToObservableList;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.GetReceipts;
import dbpathnames.dbStringPath;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import jboxlist.FileBrowser;
import messageBox.messageBox;
import models.club.rCeipts;
import models.voucher.EaterCodes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class TicketEaterCodesController implements Initializable {
    @FXML private TableView<EaterCodes> EaterTable;
    @FXML private TableColumn<EaterCodes, String> EaterEaterCodes;
    @FXML private TableColumn<EaterCodes, String> EaterDropit;
    @FXML private TableColumn<EaterCodes, Integer> EaterStart;
    @FXML private TableColumn<EaterCodes, Integer > EaterStop;
    @FXML private Button cancelButton;
    //@FXML private Button selectButton;
    @FXML private Button newButton;
    @FXML private Button saveButton;
    @FXML private Button cancelEditButton;
    @FXML private Button editButton;    
    @FXML private TextField fEaterCodes;    
    @FXML private TextField fDropIt;    
    @FXML private TextField fStart;    
    @FXML private TextField fStop;    
    @FXML private HBox editBox2;
    @FXML private Pane root;

    private static SortedList<EaterCodes> sortedList = null;
    private static FilteredList<EaterCodes> filterData = null;
    private static final SceneChanger_Main sc = new SceneChanger_Main();
    private static final dbStringPath dbsp = new dbStringPath();
    private static Desktop dsk = Desktop.getDesktop();
    //private static Connection conn = null;
    //private static Statement st = null;
    //private static ResultSet rs = null;
    public static ArrayList<rCeipts> Receipts;
    public static String cssPath, MGR, ID = "f";
    public static int superLevel;
    private static ObservableList<EaterCodes> data;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("wea are herer =====================================================");
        dbsp.setName();
        //root.getStylesheets().add(cssC.cssPath());
        root.getStylesheets().add(cssPath);
        editBox2.setVisible(false);
        editButton.setVisible(false);
        //selectButton.setVisible(false);
        SetButtons();
        EaterEaterCodes.setCellValueFactory(new PropertyValueFactory<>("eaterCode"));
        EaterDropit.setCellValueFactory(new PropertyValueFactory<>("dropIt"));
        EaterStart.setCellValueFactory(new PropertyValueFactory<>("Start"));
        EaterStop.setCellValueFactory(new PropertyValueFactory<>("Stop"));
        new FocusedTextFieldHighlight().setHighlightListenergProblem(root);
        //gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        getTableItems();
        newButton.requestFocus();
        
    }    
    
    
    private void getTableItems() {
        //fillList(1);
        EaterTable.getItems().clear();
        EaterTable.getItems().addAll(getEaterCodesDataXML());
        
    }
   
    
    
    public void newButtonGo() {
        editBox2.setVisible(true);
        fEaterCodes.setDisable(false);
        fEaterCodes.requestFocus();
    }
    
    public void editButtonGo() {
        editBox2.setVisible(true);
        TablePosition pos = EaterTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        ID = EaterTable.getItems().get(row).getEaterCode();
        System.out.println(ID);
        fEaterCodes.setText(EaterTable.getItems().get(row).getEaterCode());
        fDropIt.setText(EaterTable.getItems().get(row).getDropIt());
        fStart.setText(String.valueOf(EaterTable.getItems().get(row).getStart()));
        fStop.setText(String.valueOf(EaterTable.getItems().get(row).getStop()));
        fEaterCodes.setDisable(true);
        fDropIt.requestFocus();
        
        
    }
    
    
    private boolean checkForDuplicates(String newCode){
        boolean t = false;
        for (int ii = 0; ii < data.size(); ii++) {
            if (data.get(ii).getEaterCode().equals(newCode)){
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Stop!", newCode + " already exist!");
                t = true;
            }
        }
        return t;
    }
    
    
    
    
    
    public void saveButtonGo() {
        if (ID.equals("f")) { //ADD NEW EATER CODE
        if (checkForDuplicates(fEaterCodes.getText())) {
            return;
        }
            EaterCodes newGame2 = new EaterCodes(fEaterCodes.getText(), fDropIt.getText(), Integer.parseInt(fStart.getText()), Integer.parseInt(fStop.getText()));
            try {
                System.out.println("========================== we got through new one");
                new AddXmlNode().addXMLEaterCodes(new dbStringPath().pathNameClubDBs + "\\XML\\TicketEaterCodes.xml", newGame2);
            } catch (Exception ex) {
                System.out.println(ex);
            }

        } else { //UPDATE EATER CODE
            String IDID = ID;
            EaterCodes newGame2 = new EaterCodes(fEaterCodes.getText(), fDropIt.getText(), Integer.parseInt(fStart.getText()), Integer.parseInt(fStop.getText()));
            System.out.println("========================== we got through update");
            new ModifyXMLFileInJava().ModifyXMLEaterCodes(new dbStringPath().pathNameClubDBs + "\\XML\\TicketEaterCodes.xml", newGame2, IDID);
        }
        editBox2.setVisible(false);
        getTableItems();
        fEaterCodes.setDisable(false);
        fEaterCodes.setText("");
        fDropIt.setText("");
        fStart.setText("");
        ID = "f";
        
    }
    
    public void SetButtons(){
        if (superLevel == 2) {
            editBox2.setVisible(false);
            editButton.setVisible(false);
            //selectButton.setVisible(false);
            newButton.setVisible(false);
        }
        if (superLevel == 3) {
            editBox2.setVisible(false);
            editButton.setVisible(false);
            //selectButton.setVisible(false);            
            newButton.setVisible(true);
        }
    }
    
    
    public void TableClicked(MouseEvent me) throws IOException {
        //selectButton.setVisible(true);
        //if (superLevel == 2) {
        //    editButton.setVisible(false);
        //    newButton.setVisible(false);            
        //}
        //if (superLevel == 3) {
            editButton.setVisible(true);
            newButton.setVisible(true);
        //}

         //if (me.getClickCount() == 2) {
            //selectButton.fire();
         //}
    }
    
    
    
    //XML PROCESSES - - - - - - - - - - - - - - - 
    
    public ObservableList<EaterCodes> getEaterCodesDataXML() {
        ObservableList<EaterCodes> eatercodes = FXCollections.observableArrayList();
        try {
            Document document = new XMLDOMParseToObservableList().parseXmlFile(dbsp.pathNameXML + "TicketEaterCodes.xml");
            //System.out.println("1");
            eatercodes = extractDataFromXmlTOObservableList(document);

            //Collections.reverse(gameProblems);
        } catch (Exception e) {
            System.out.println("-1- " + e + " " + dbsp.pathNameXML);
            new Mail_JavaFX1().sendEmailTo("Game Problem Error", "-1- " + e + " " + dbsp.pathNameXML, "errors");
        }
        return eatercodes;
    }
    
    
    public ObservableList<EaterCodes> extractDataFromXmlTOObservableList(Document document) {
        data = FXCollections.observableArrayList();

        NodeList nodeList = document.getElementsByTagName("TEC");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                // Extract the data from the element
                String videoID = element.getElementsByTagName("EaterCode").item(0).getTextContent();
                String videoName = element.getElementsByTagName("DropIt").item(0).getTextContent();
                String searchData = element.getElementsByTagName("Start").item(0).getTextContent();
                String link = element.getElementsByTagName("Stop").item(0).getTextContent();

                System.out.println("EaterCode: " + videoID + " DropIt " + videoName + " Start " + searchData + " Stop " + link);
                data.add(new EaterCodes(videoID, videoName, Integer.parseInt(searchData), Integer.parseInt(link)));
            }
        }
        System.out.println("this is the size od Data: " + data.size());
        return data;
    }
    
    //XML PROCESSES - - - - - - - - - - - - - - - 
    
    
   
   
    public void cancelEditButtonGo() throws IOException {
        fEaterCodes.setText("");
        fDropIt.setText("");
        fStart.setText("");
        fStop.setText("");
        ID = "f";
        editBox2.setVisible(false);
        EaterTable.getSelectionModel().clearSelection();
        editButton.setVisible(false);
        //selectButton.setVisible(false);
        //searchField.requestFocus();
        

    }
    
    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
            if (editBox2.isVisible()) {
                cancelEditButtonGo();
                return;
            } else {
                sc.changePopUp(event, "", "");
            }
    }
    
    
    
     private void doEnterKey() {
         if (editBox2.isVisible()) {
             if (fEaterCodes.isFocused()) {
                 fDropIt.requestFocus();
                 return;
             }
             if (fDropIt.isFocused()) {
                 fStart.requestFocus();
                 return;
             }
             if (fStart.isFocused()) {
                 fStop.requestFocus();
                 return;
             }
             if (fStop.isFocused()) {
                 fEaterCodes.requestFocus();
                 return;
             }
         } else {
             return;
         }
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
                    case TAB: doEnterKey(); break;
                    //case SPACE: selectButton.fire(); break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: doEnterKey(); break;
                default:
                    break;
                }
    }
    
}
