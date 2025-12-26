package views.vouchers;



import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import XML_Code.XMLDOMParseToObservableList;
import models.voucher.FXVoucherViewer;
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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pWordFX.employeeFX;
import commoncodes.ClubFunctions4;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import models.settings.Announcements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sceneChangerFX.SceneChanger_Main;


public class FXVoucherViewerController implements Initializable {
    @FXML private Pane root;
    @FXML private Button cancelButton;
    @FXML private Button updateButton;
    @FXML private Button vMenuButton;
    @FXML private Label c1L;
    @FXML private TextField c2T;
    @FXML private TextField c3T;
    @FXML private TextField c4T;
    @FXML private TextField c5T;
    @FXML private TextArea noteT;
    @FXML public static int tableID;
    @FXML private HBox hbox;
    @FXML private TextField searchField;

    @FXML private TableView<FXVoucherViewer> cTable;
    @FXML private TableColumn<FXVoucherViewer, String> C1;
    @FXML private TableColumn<FXVoucherViewer, String> C2;
    @FXML private TableColumn<FXVoucherViewer, String> C3;
    @FXML private TableColumn<FXVoucherViewer, String> C4;
    @FXML private TableColumn<FXVoucherViewer, Integer> C5;
    @FXML private TableColumn<FXVoucherViewer, String> C6;
    @FXML private TableColumn<FXVoucherViewer, Integer> C7;
    
    
    
    public static String cssPath;
    public static dbStringPath dbsp;
    private static Bounds boundsInSceneV;
    ContextMenu contextMenu = new ContextMenu();
    private static ObservableList<FXVoucherViewer> data = FXCollections.observableArrayList();



    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssPath);
        boundsInSceneV = vMenuButton.localToScene(vMenuButton.getBoundsInLocal());
        setKeyCodes();
        C2.setCellValueFactory(new PropertyValueFactory<>("C2"));
        C3.setCellValueFactory(new PropertyValueFactory<>("C3"));
        C4.setCellValueFactory(new PropertyValueFactory<>("C4"));
        C5.setCellValueFactory(new PropertyValueFactory<>("C5"));
        C6.setCellValueFactory(new PropertyValueFactory<>("C6"));
        getTableItems();
        buildMenuButton();
        cTable.setFixedCellSize(30.0);
        //cTable.addEventFilter(ScrollEvent.ANY, Event::consume);
        Platform.runLater(() -> searchField.requestFocus());

    }

    private void getTableItems() {
        cTable.getItems().clear();
        try {
            //cTable.getItems().addAll(getAncementsDataXML());
            cTable.getItems().addAll(getdataB());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        cTable.setEditable(true);

    }

    
    
    
    //XML PROCESSES - - - - - - - - - - - - - - - 
    
    public ObservableList<FXVoucherViewer> getAncementsDataXML() {
        ObservableList<FXVoucherViewer> annoucement = FXCollections.observableArrayList();
        try {
            Document document = new XMLDOMParseToObservableList().parseXmlFile(dbsp.pathNameXML + "Voucher.xml");
            //System.out.println("1");
            annoucement = extractDataFromXmlTOObservableList(document);

            //Collections.reverse(gameProblems);
        } catch (Exception e) {
            System.out.println("-1- " + e + " " + dbsp.pathNameXML);
            new Mail_JavaFX1().sendEmailTo("Game Problem Error", "-1- " + e + " " + dbsp.pathNameXML, "errors");
        }
        return annoucement;
    }
    
    
    public ObservableList<FXVoucherViewer> extractDataFromXmlTOObservableList(Document document) {
        //data = FXCollections.observableArrayList();

        NodeList nodeList = document.getElementsByTagName("Voucher");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                // Extract the data from the element
                String empID = element.getElementsByTagName("Employee_ID").item(0).getTextContent();
                String dDate = element.getElementsByTagName("Date").item(0).getTextContent();
                String dDay = element.getElementsByTagName("Day").item(0).getTextContent();
                String eName = element.getElementsByTagName("Emp_Name").item(0).getTextContent();
                String vAmount = element.getElementsByTagName("Voucher_Amount").item(0).getTextContent();
                String tclockID = element.getElementsByTagName("Tclock_ID").item(0).getTextContent();
                String vReason = element.getElementsByTagName("Reason").item(0).getTextContent();

                //System.out.println("ID: " + videoID + " Name " + videoName + " SearchData " + searchData + " Link " + link);
                data.add(new FXVoucherViewer(empID, dDate, dDay, eName, Integer.parseInt(vAmount), tclockID, vReason));
            }
        }
        System.out.println("this is the size od Data: " + data.size());
        return data;
    }
    
    //XML PROCESSES - - - - - - - - - - - - - - - 
    
    
    
    
    
    
    
    public ObservableList<FXVoucherViewer> getdataB() throws SQLException {
        ObservableList<FXVoucherViewer> data = FXCollections.observableArrayList();

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "Vouchers.accdb");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT Voucher.* FROM Voucher ORDER BY Voucher.Date DESC;");
            while (rs.next()) {
                FXVoucherViewer newdata = new FXVoucherViewer(rs.getString("Employee_ID"), rs.getString("Date"), rs.getString("Day"), rs.getString("Emp_Name"), rs.getInt("Voucher_Amount"), "Tclock_ID", rs.getString("Reason"));
                data.add(newdata);
            }
        } catch (SQLException e) {
            System.err.println("Voucher Model Error: " + e.getMessage());
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }

        return data;
    }

    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
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
            if (ke.getCode() == KeyCode.DOWN) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.ESCAPE) {
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
            case UP:
                break;
            case ESCAPE:
                cancelButton.fire();
                break;
            case ENTER:
                break;
            default:
                break;
        }
    }

    public void searchRecord(KeyEvent ke) throws SQLException {

        FilteredList<FXVoucherViewer> filterData = new FilteredList<>(getdataB(), p -> true);
        searchField.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
            filterData.setPredicate(pers -> {
                if (newvalue == null || newvalue.isEmpty()) {
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                if (pers.getC1().toLowerCase().contains(typedText)) {

                    return true;
                }
                if (pers.getC4().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (String.valueOf(pers.getC5()).toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getC3().toLowerCase().contains(typedText)) {

                    return true;
                }

                return false;
            });
            SortedList<FXVoucherViewer> sortedList = new SortedList<>(filterData);
            sortedList.comparatorProperty().bind(cTable.comparatorProperty());
            cTable.setItems(sortedList);

        });

    }

    public void buildMenuButton() {
        MenuItem editReasons = new MenuItem(" Edit Reasons ");
        MenuItem exitItem = new MenuItem("      Exit     ");

        editReasons.setOnAction((ActionEvent event) -> {
            new ClubFunctions4().whichdropedit(dbStringPath.pathNameClub + "vReasons.txt");
        }
        );

        exitItem.setOnAction((ActionEvent event) -> {
            cancelButton.fire();
        } //clock in out
        );

        contextMenu.getItems().addAll(editReasons, new SeparatorMenuItem(), exitItem);
        vMenuButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(vMenuButton, boundsInSceneV.getMaxX() + 300, boundsInSceneV.getMaxY());
            }
        });
        vMenuButton.setOnContextMenuRequested((ContextMenuEvent event) -> {
            contextMenu.show(vMenuButton, event.getScreenX(), event.getScreenY());
        });
    }

    public void menuItemsButtonMouseOver() {
        contextMenu.show(vMenuButton, boundsInSceneV.getMaxX() + 300, boundsInSceneV.getMaxY());
    }

    public void exitButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        stageV.close();
    }


}
