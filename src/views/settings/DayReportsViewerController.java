package views.settings;

import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import XML_Code.XMLDOMParseToObservableList;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import messageBox.HTMLViewerController;
import models.settings.Announcements;
import models.settings.GreetingsClub;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sceneChangerFX.SceneChanger_Main;




public class DayReportsViewerController implements Initializable {

    @FXML private TableView<GreetingsClub> cTable;
    @FXML private TableColumn<GreetingsClub, String> C1;
    @FXML private TableColumn<GreetingsClub, String> C2;
    @FXML private Button cancelButton;
    @FXML private Pane root;
    @FXML private Pane buttonBox;

    
    private static Connection conn = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    private static Stage stageV;
    private static cssChanger cssC = new cssChanger();
    private static SceneChanger_Main sc = new SceneChanger_Main();
    private static dbStringPath dbsp = new dbStringPath();
    private static Bounds boundsInScene, boundsInScene2, boundsInScene3;
    public static String emailAddress;
    ObservableList<GreetingsClub> data = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScene2 = root.localToScene(root.getBoundsInLocal());
        dbsp.setName();
        root.getStylesheets().add(cssC.cssPath());
        setKeyCodes();
        cTable.setFixedCellSize(30.0);
        C1.setCellValueFactory(new PropertyValueFactory<>("C1"));
        C2.setCellValueFactory(new PropertyValueFactory<>("C2"));
        Platform.runLater(()->getStageV());
        Platform.runLater(()->getTableItems());
    }    
    
    
    
    
    public void tableItemSelected() {
        System.out.println("here is the itemSelected ");
        GreetingsClub itemSelected = cTable.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader();
        HTMLViewerController wController = (HTMLViewerController) fxmlLoader.getController();
        wController.eMailMsg = itemSelected.getC2();
        wController.eMailDate = itemSelected.getC1();
        wController.noEmailButton = true;
        wController.emailAddress = emailAddress;
        getStageV();
        System.out.println("here we are in DayReportsViewerController itemSelected ");
        try {
            //sc.getpassWord(stageV, "/messageBox/messageBoxFX.fxml", "Number", ":", 200.0, 00.0);
            sc.getpassWord(stageV, "/messageBox/HTMLViewer.fxml", "Number", ":", 300.0, 60.0);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        //sc.goToScene("HTMLViewer", stageV, "Name", null,boundsInScene);
        //sc.goToScene2("messageBoxFX", stageV, "Name", root,  -60.0, -600.0);

        //sc.ChangeScene("/views/HTMLViewer", stageV, "sceneTitle", root, boundsInScene, -60.0, -500.0);
    }
    
    private void HTMLViewerGO() {
        
    }
    
    
    // - - -HERE IS WHERE THE XML WILL GO - - - - - 
     public ObservableList<GreetingsClub> getDailyReportsDataXML() {
        ObservableList<GreetingsClub> annoucement = FXCollections.observableArrayList();
       // new Mail_JavaFX1().sendEmailTo("Game Problem Error", "-1- Announcment size: " + annoucement.size() + " " + dbsp.pathNameXML, "errors");
        try {
            Document document = new XMLDOMParseToObservableList().parseXmlFile(dbsp.pathNameXML + "GreaterThanDrpts.xml");
            annoucement = extractDataFromXmlTOObservableList(document);
            //new Mail_JavaFX1().sendEmailTo("Game Problem Error", "-2- Announcment size: " + annoucement.size() + " " + dbsp.pathNameXML, "errors");
            
            //Collections.reverse(gameProblems);
        } catch (Exception e) {
            System.out.println("-1- " + e + " " + dbsp.pathNameXML);
            new Mail_JavaFX1().sendEmailTo("Game Problem Error", "-1- " + e + " " + dbsp.pathNameXML, "errors");
        }
        return annoucement;
    }
    
    
    public ObservableList<GreetingsClub> extractDataFromXmlTOObservableList(Document document) {
        data = FXCollections.observableArrayList();

        NodeList nodeList = document.getElementsByTagName("DailyReports");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                // Extract the data from the element
                String drItem = element.getElementsByTagName("Item").item(0).getTextContent();
                String drString = element.getElementsByTagName("String").item(0).getTextContent();
                //String drID = element.getElementsByTagName("ID").item(0).getTextContent();

                System.out.println("Date: " + drItem + " \nString " + drString + " ID " + "r");
                data.add(new GreetingsClub(drItem, drString, "g", "w", "e", 0, 0));
            }
        }
            //new Mail_JavaFX1().sendEmailTo("Game Problem Error", "-3- Data Size: " + data.size() + " " + dbsp.pathNameXML, "errors");
        System.out.println("this is the size od Data: " + data.size());
        return data;
    }


    // - - - HERE IS WHERE THE XML ENDS - - -  - 

    private void getTableItems() {
        System.out.println("here we are in DayReportsViewerController getTableItems");
        cTable.getItems().clear();
        try {
            ///cTable.getItems().addAll(getDailyReportsDataXML());
            cTable.getItems().addAll(getdataB());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        cTable.setEditable(true);
    }
    
    
    public ObservableList<GreetingsClub> getdataB() throws SQLException {
    ObservableList<GreetingsClub> data = FXCollections.observableArrayList();

    try {
        conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "GreaterThan.accdb;immediatelyReleaseResources=true");
        st = conn.createStatement();
        rs = st.executeQuery("SELECT DailyReports.* FROM DailyReports;");
        while (rs.next()) {

            GreetingsClub newdata = new GreetingsClub(rs.getString("Item"), rs.getString("String"), rs.getString("Number"), rs.getString("Formatted"), rs.getString("Date"), rs.getInt("ID"), rs.getInt("ID2"));
            data.add(newdata);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } finally {
        if (rs != null){
        rs.close();}
        if (st != null){
        st.close();}
        if (conn != null){
        conn.close();}
    }

    return data;
}  
    
    
    private void getStageV() {
        stageV = (Stage) cancelButton.getScene().getWindow();
    }
    
    
    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F6) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F8) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F9) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.DOWN) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke); ke.consume();}
     });   
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
                    case DOWN: break;
                    case UP: break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: break;
                default:
                    break;
                }
    } 
    
    
    
    
    public void exitButtonPushed(ActionEvent event) throws IOException {
            stageV.close();
        }
    
}
