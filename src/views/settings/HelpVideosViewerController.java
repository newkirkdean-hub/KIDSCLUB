
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.w3c.dom.Document;
import messageBox.messageBox;
import models.club.rCeipts;
import models.settings.Help;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class HelpVideosViewerController implements Initializable {
    @FXML private TableView<Help> gamesTable;
    @FXML private TableColumn<Help, String> gameNameColumn;
    @FXML private TableColumn<Help, String> gameLocationColumn;
    @FXML private TableColumn<Help, String> gameDepartmentColumn;
    @FXML private TableColumn<Help, String> gameIDColumn;
    @FXML private TextField searchField;
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Button newButton;
    @FXML private Button saveButton;
    @FXML private Button cancelEditButton;
    @FXML private Button filePickerButton;
    @FXML private Button editButton;    
    @FXML private TextField fTitle;    
    @FXML private TextField fKeyWords;    
    @FXML private TextField fLink;    
    @FXML private HBox editBox2;
    @FXML private Pane root;
    
    private static SortedList<Help> sortedList = null;
    private static FilteredList<Help> filterData = null;
    private static SceneChanger_Main sc = new SceneChanger_Main();
    private static dbStringPath dbsp = new dbStringPath();
    private static Desktop dsk = Desktop.getDesktop();
    private static Connection conn = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    public static String cssPath, MGR, ID = "f", rNumber;
    public static ArrayList<rCeipts> Receipts;
    private static ObservableList<Help> data;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        //root.getStylesheets().add(cssC.cssPath());
        root.getStylesheets().add(cssPath);
        editBox2.setVisible(false);
        editButton.setVisible(false);
        selectButton.setVisible(false);
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        gameLocationColumn.setCellValueFactory(new PropertyValueFactory<>("Search"));
        gameDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("Link"));
        //gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        new FocusedTextFieldHighlight().setHighlightListenergProblem(root);

        getTableItems();
        searchField.requestFocus();
        
    }    
    
    
    private void getTableItems() {
        try {
            fillList(1);
            System.out.println(data.get(data.size()-1).getId());
            //gamesTable.getItems().clear();
            //gamesTable.getItems().addAll(getGames());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
    }
        
    public void fillList(int a) throws SQLException {

        switch (a) {
            case 1:
                //filterData = new FilteredList<>(getGames(), p -> true);
                filterData = new FilteredList<>(getHelpDataXML(), p -> true);
                break;
            case 2:
        filterData = new FilteredList<>(gamesTable.getItems(), p -> true);
        searchField.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
            filterData.setPredicate(pers -> {
                if (newvalue == null || newvalue.isEmpty()) {
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                if (pers.getTitle().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getSearch().toLowerCase().contains(typedText)) {
                    return true;
                }
                return false;
            });                       
        });
        break;
        }
        sortedList = new SortedList<>(filterData);
        gamesTable.setItems(sortedList);
    }
    
    public void searchRecord(KeyEvent ke) throws SQLException {
        fillList(2);
    }

    
    
     public ObservableList<Help> getHelpDataXML(){
        ObservableList<Help> helpData = FXCollections.observableArrayList();
        try {
            Document document = new XMLDOMParseToObservableList().parseXmlFile(dbsp.pathNameXML + "Help.xml");
            System.out.println("1");
            helpData = extractDataFromXmlTOObservableList(document);
        } catch (Exception e) {
            System.out.println("-1- " + e + " " + dbsp.pathNameXML);
        }
        return helpData;
    }
    
    
       public ObservableList<Help> extractDataFromXmlTOObservableList(Document document) {
        data = FXCollections.observableArrayList();
        System.out.println("3");

        NodeList nodeList = document.getElementsByTagName("Help");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                // Extract the data from the element
                String videoID = element.getElementsByTagName("ID").item(0).getTextContent();
                String videoName = element.getElementsByTagName("Video_Title").item(0).getTextContent();
                String searchData = element.getElementsByTagName("Search_Data").item(0).getTextContent();
                String link = element.getElementsByTagName("Link").item(0).getTextContent();
                //ystem.out.println(videoName + " " + searchData + " " + link + " " + videoID);
                // Create a new YourData object and add it to the list
                data.add(new Help(videoName, searchData, link, videoID));
            }
        }

        return data;
    }

    
    

    public void openFileLocation() {
        String path = new GetReceipts().getReceipts(Receipts, "Videos");
        try {
            FileBrowser filebrowser = new FileBrowser();
            filebrowser.openFileLocation(fLink, path);
        } catch (Exception e) {
            System.out.println("here is the Exception " + e + " " + path);
            new Mail_JavaFX1().sendEmailTo("ERROR in HelpVideosViewer", "Error finding file " + e + " \n" + path, "errors");
        }
    }
    
    public void selectButtonPushed(ActionEvent event) throws IOException {
        dbsp.setName();
        boolean exists = false;
        TablePosition pos = gamesTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        String str = gamesTable.getItems().get(row).getLink();
        //File temp = new File(dbsp.pathNameVideos + str + ".MP4");
        File temp = new File(str);
        
        exists = temp.exists();
        //System.out.println(exists);
        if (exists) {
            try {
                //dsk.open(new File(dbsp.pathNameVideos +  str + ".MP4"));
                dsk.open(new File(str));
            } catch (IOException ex) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Error", "This video was not found");
            }
        } else {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "Error", "This video was not found" + temp.toString());
        }
    }
    
    public void newButtonGo() {
        editBox2.setVisible(true);
        fTitle.requestFocus();
    }
    
    public void editButtonGo() {
        editBox2.setVisible(true);
        TablePosition pos = gamesTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        ID = gamesTable.getItems().get(row).getId();
        System.out.println(ID);
        fTitle.setText(gamesTable.getItems().get(row).getTitle());
        fKeyWords.setText(gamesTable.getItems().get(row).getSearch());
        fLink.setText(gamesTable.getItems().get(row).getLink());
        fTitle.requestFocus();
        
        
    }
    
    public void saveButtonGo() {
        if (ID.equals("f")) {
            //new record
            int newID = Integer.parseInt(data.get(data.size()-1).getId()) + 1;
            Help newGame2 = new Help(fTitle.getText(), fKeyWords.getText(), fLink.getText(), String.valueOf(newID));
            try {
                new AddXmlNode().addXMLHelp(new dbStringPath().pathNameClubDBs + "\\XML\\Help.xml", newGame2);
            } catch (Exception ex) {
                System.out.println(ex);
            }
            //if (!newGame2.InsertHelpVideo(newGame2.getTitle(), newGame2.getSearch(), newGame2.getLink())) {
            //    System.out.println("error");
            //}
        } else {
            System.out.println("we are in help update. " + ID);
            Help newGame2 = new Help(fTitle.getText(), fKeyWords.getText(), fLink.getText(), ID);
            new ModifyXMLFileInJava().ModifyXMLHelp(new dbStringPath().pathNameClubDBs + "\\XML\\Help.xml", newGame2);
            if (Integer.parseInt(newGame2.getId()) > Integer.parseInt("12652")) {
                new Mail_JavaFX1().sendEmailTo("NOTE in HelpVideosViewer", "Edits were made to the help files. remember ID's from new items in the XML don't match the HelpDB file 1/29/2025", "errors");
            }
            //if (!newGame2.UpdateHelpVideo(newGame2.getTitle(), newGame2.getSearch(), newGame2.getLink(), newGame2.getId())) {
            //    System.out.println("error");
            //}
        }
        editBox2.setVisible(false);
        clearSearchField();
        //gamesTable.getItems().clear();
        //gamesTable.setItems(data);
        getTableItems();
/*        gamesTable.getItems().clear();
        try {
            gamesTable.getItems().addAll(getGames());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
*/
        fTitle.setText("");
        fKeyWords.setText("");
        fLink.setText("");
        ID = "f";
        searchField.requestFocus();
        
    }
    
    
    
    
    public void TableClicked(MouseEvent me) throws IOException {
        selectButton.setVisible(true);
        editButton.setVisible(true);
        //newButton.setVisible(false);

         if (me.getClickCount() == 2) {
            selectButton.fire();
         }
    }
    
    
    public ObservableList<Help> getGames() throws SQLException {
    ObservableList<Help> games = FXCollections.observableArrayList();
    games.clear();
    Help newGame = null;
    try {
            //conn = getConnectionFromPool();
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "Help.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT Help.* FROM Help ORDER BY Help.[Video Title];");
           //String ccn, String cid, String namef, String namel, LocalDate bdate, String areacode, String phone1, String phone2
            while (rs.next()) {
                newGame = new Help( rs.getString("Video Title"), rs.getString("Search Data"), rs.getString("Link"), rs.getString("ID"));
                games.add(newGame);
            }
    }
    catch (SQLException e) {
        System.err.println(e.getMessage());
    }
    finally {
    //if (rs != null);
    //    rs.close();
    if (st != null);
        st.close();
    if (conn != null);
        conn.close();
    }
    return games;
}   
 
    
    
    

    public void cancelEditButtonGo() throws IOException {
        fTitle.setText("");
        fKeyWords.setText("");
        fLink.setText("");
        ID = "f";
        editBox2.setVisible(false);
        gamesTable.getSelectionModel().clearSelection();
        editButton.setVisible(false);
        selectButton.setVisible(false);
        searchField.requestFocus();

    }
    
    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        if (searchField.getText().trim().length()>0) {
            clearSearchField();
            getTableItems();
            ID = "";
        } else {
            if (editBox2.isVisible()) {
                cancelEditButtonGo();
                return;
            } else {
                sc.changePopUp(event, "", "");
            }
        }
    }
    
    private void clearSearchField() {
        searchField.clear();
        searchField.requestFocus();
    }
    
    
    
     private void doEnterKey() {
         if (editBox2.isVisible()) {
             if (fTitle.isFocused()) {
                 fKeyWords.requestFocus();
                 return;
             }
             if (fKeyWords.isFocused()) {
                 fLink.requestFocus();
                 filePickerButton.fire();
                 return;
             }
             if (fLink.isFocused()) {
                 fTitle.requestFocus();
                 return;
             }
         } else {
             searchField.requestFocus();
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
