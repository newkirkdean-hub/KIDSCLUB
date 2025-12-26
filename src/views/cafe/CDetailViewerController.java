
package views.cafe;

import views.toys.*;
import Css.cssChanger;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import models.toys.ToysDB;
import models.toys.ToysDetail;
import sceneChangerFX.SceneChanger_Main;


/**
 * FXML Controller class
 *
 * @author Dean
 */
public class CDetailViewerController implements Initializable {
    @FXML private TableView<ToysDetail> memPurchTable;
    @FXML private TableColumn<ToysDetail, LocalDate> toysPurchDateColumn;
    @FXML private TableColumn<ToysDetail, String> toysPurchEmpNumberColumn;
    @FXML private TableColumn<ToysDetail, String> toysInvoiceNumberColumn;
    @FXML private TableColumn<ToysDetail, String> toysNumberUnitsColumn;
    @FXML private TableColumn<ToysDetail, Integer> toysUnitTypeColumn;
    @FXML private TableColumn<ToysDetail, Double> toysPricePerUnitColumn;
    @FXML private TableColumn<ToysDetail, Double> toysTotalColumn;
    @FXML private Label searchField;
    @FXML private Button selectButton;
    @FXML private Button salesButton;
    @FXML private Pane root;
    SceneChanger_Main sc = new SceneChanger_Main();
    public static final cssChanger cssC = new cssChanger();
    public static String css;
    dbStringPath dbsp = new dbStringPath();
    public static String memID, cName;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(css);
        SetKeyCodes();
        NumberFormat $format = DecimalFormat.getCurrencyInstance();
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        toysPurchDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchDate"));
        toysPurchDateColumn.setCellFactory(column -> {
            return new TableCell<ToysDetail, LocalDate>() {
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
        toysPricePerUnitColumn.setCellValueFactory(new PropertyValueFactory<>("PPU"));
        toysPricePerUnitColumn.setCellFactory(column -> {
            return new TableCell<ToysDetail, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    //System.out.println(item);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date.
                        setText($format.format(item));
                    }
                }
            };
        }); 
        toysTotalColumn.setCellValueFactory(new PropertyValueFactory<>("ATP"));
        toysTotalColumn.setCellFactory(column -> {
            return new TableCell<ToysDetail, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    //System.out.println(item);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date.
                        setText($format.format(item));
                    }
                }
            };
        }); 
        toysPurchEmpNumberColumn.setCellValueFactory(new PropertyValueFactory<>("empID"));
        toysInvoiceNumberColumn.setCellValueFactory(new PropertyValueFactory<>("InvoiceNumb"));
        toysNumberUnitsColumn.setCellValueFactory(new PropertyValueFactory<>("QTY"));
        toysUnitTypeColumn.setCellValueFactory(new PropertyValueFactory<>("UnitType"));
        memPurchTable.getItems().clear();
        memPurchTable.getItems().addAll(new ToysDB().getAllDetail(memID));
        searchField.setText(cName);
        //Platform.runLater(() -> selectButton.requestFocus());
        
    }    
    
   

    public void selectButtonPushed(ActionEvent event) throws IOException {
        System.out.println("here in select Button");
       sc.changePopUp1(event, "");
    }
    
    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        System.out.println("here in cancel Button");
        sc.changePopUp1(event, "");
        
    }
    
    public void SalesButtonPushed(ActionEvent event) throws IOException {
        System.out.println("here in Sales Button");
        memPurchTable.getItems().clear();
        memPurchTable.getItems().addAll(new ToysDB().getAllDetailSales(memID));
        memPurchTable.refresh();
        new ToysDB().disConnect();
        
    }

    
    
    private void SetKeyCodes() {
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
                    case F11: break;
                    case F12: break;
                    case ESCAPE: selectButton.fire(); break;
                    case ENTER: break;
                default:
                    break;
                }
    }
    
}
