package views.corp;

import ComboBoxAutoComplete.ComboBoxAutoComplete_Corp;
import Css.cssChanger;
import models.corp.ChangerSales;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.corp.CorpDataSales;
import models.corp.CorpDataFXDB;
import models.corp.PdoutsSales;
import reports.corp.Reports;
import commoncodes.ClubFunctions;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class CorpForm_MainFXController implements Initializable {
    @FXML private AnchorPane root;
    @FXML private Pane depPane;
    @FXML private Pane paidOutsPane;
    @FXML private Pane changerPane;
    @FXML private TabPane corpTabPane;
    @FXML private Label exitLabel;
    @FXML private Label depReports;
    @FXML private Label outsReports;
    @FXML private Label changerReports;
    @FXML private Label depDropEdits;
    @FXML private Label outsDropEdits;
    
    @FXML private DatePicker salesDate;
    @FXML private ComboBox salesDept;
    @FXML private ComboBox salesType;
    @FXML private ComboBox salesItem_Name;
    @FXML private TextField salesAMT;
    @FXML private Button salesAddEdit;
    @FXML private Button salesDelete;
    
    
    @FXML private DatePicker pdoutsDate;
    @FXML private ComboBox pdoutsDept;
    @FXML private ComboBox pdoutsType;
    @FXML private ComboBox pdoutsItem_Name;
    @FXML private TextField pdoutsAMT;
    @FXML private TextField pdoutsMGR;
    @FXML private TextField pdoutsMemo;
    @FXML private Button pdoutsAddEdit;
    @FXML private Button pdoutsDelete;
    
    
    
    @FXML private DatePicker changerDt;
    @FXML private TextField changer_ATM;
    @FXML private TextField changer_bCars;
    @FXML private TextField changer_1;
    @FXML private TextField changer_2;
    @FXML private TextField changer_3;
    @FXML private TextField changer_4;
    @FXML private TextField changer_5;
    @FXML private TextField changer_8;
    @FXML private TextField changer_9;
    @FXML private TextField changer_12;
    @FXML private TextField changer_14;
    @FXML private Button changerAddEdit;
    @FXML private Button changerDelete;
    @FXML private GridPane basePanel;
    
    
    
    @FXML public TableView<CorpDataSales> SalesTable;
    @FXML public TableColumn<CorpDataSales, LocalDate> cSalesDate;
    @FXML public TableColumn<CorpDataSales, String> cDay;
    @FXML public TableColumn<CorpDataSales, String> cMGR;
    @FXML public TableColumn<CorpDataSales, String> cDepart;
    @FXML public TableColumn<CorpDataSales, String> cType;
    @FXML public TableColumn<CorpDataSales, String> cItemName;
    @FXML public TableColumn<CorpDataSales, Double> cAmount;
    @FXML public TableColumn<CorpDataSales, LocalDate> cPostDate;
    
    @FXML public TableView<PdoutsSales> pdoutsTable;
    @FXML public TableColumn<PdoutsSales, LocalDate> cPdoutsDate;
    @FXML public TableColumn<PdoutsSales, String> cPdoutsDay;
    @FXML public TableColumn<PdoutsSales, String> cPdoutsMGR;
    @FXML public TableColumn<PdoutsSales, String> cPdoutsDepart;
    @FXML public TableColumn<PdoutsSales, String> cPdoutsType;
    @FXML public TableColumn<PdoutsSales, String> cPdoutsItemName;
    @FXML public TableColumn<PdoutsSales, Double> cPdoutsAmount;
    @FXML public TableColumn<PdoutsSales, String> cPdoutsMemo;
    @FXML public TableColumn<PdoutsSales, LocalDate> cPdoutsPostDate;
    
    
    @FXML public TableView<ChangerSales> changerTable;
    @FXML public TableColumn<ChangerSales, LocalDate> changerDate;
    @FXML public TableColumn<ChangerSales, String> changerMGR;
    @FXML public TableColumn<ChangerSales, Double> changerATM;
    @FXML public TableColumn<ChangerSales, Double> bCars;
    @FXML public TableColumn<ChangerSales, Double> changer1;
    @FXML public TableColumn<ChangerSales, Double> changer2;
    @FXML public TableColumn<ChangerSales, Double> changer3;
    @FXML public TableColumn<ChangerSales, Double> changer4;
    @FXML public TableColumn<ChangerSales, Double> changer5;
    @FXML public TableColumn<ChangerSales, Double> changer8;
    @FXML public TableColumn<ChangerSales, Double> changer9;
    @FXML public TableColumn<ChangerSales, Double> changer12;
    @FXML public TableColumn<ChangerSales, Double> changer14;
    
    
    public static dbStringPath dbsp;
    public static String cssPath, MGR;

    
    private static final GetDay gd = new GetDay();
    private static SingleSelectionModel<Tab> selectionModel;
    private static Reports rPorts = new Reports();
    private static ContextMenu depContextMenu = new ContextMenu();
    private static ContextMenu outsContextMenu = new ContextMenu();
    private static ContextMenu depDropEditsContextMenu = new ContextMenu();
    private static ContextMenu outsDropEditsContextMenu = new ContextMenu();
    private static ContextMenu changersContextMenu = new ContextMenu();
    private static ContextMenu changersDropEditsContextMenu = new ContextMenu();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    private static final DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final NumberFormat $format = DecimalFormat.getCurrencyInstance();
    private static CorpDataSales trv = null;
    private static PdoutsSales pTrv = null;
    private static ChangerSales cTrv = null;
    private static int d = 0;
    private static Bounds boundsRoot, boundsMenu1, boundsMenu2, boundsMenu3, boundsMenu4;
    private static Stage stageV;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsMenu1 = depReports.localToScene(depReports.getBoundsInLocal());
        boundsMenu2 = outsReports.localToScene(outsReports.getBoundsInLocal());
        boundsMenu3 = depDropEdits.localToScene(depDropEdits.getBoundsInLocal());
        boundsMenu4 = outsDropEdits.localToScene(outsDropEdits.getBoundsInLocal());
        Date nDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        salesDate.setValue(LocalDate.now().minusDays(1));
        pdoutsDate.setValue(LocalDate.now().minusDays(1));
        changerDt.setValue(LocalDate.now().minusDays(1));
        root.getStylesheets().add(cssPath);
        dbsp.setName();
        BuildDepReports();
        BuildOutsReports();
        BuildChangerReports();
        BuildDepDropEdits();
        BuildOutsDropEdits();
        SetKeyCodes();
        FillSalesComboBox();        
        CleanTables();

        
        cSalesDate.setCellValueFactory(new PropertyValueFactory<>("SalesDate"));
        cSalesDate.setCellFactory(column -> {
            return new TableCell<CorpDataSales, LocalDate>() {
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
        cDay.setCellValueFactory(new PropertyValueFactory<>("Day"));
        cDepart.setCellValueFactory(new PropertyValueFactory<>("Depart"));
        cType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        cItemName.setCellValueFactory(new PropertyValueFactory<>("Item_Name"));
        cAmount.setCellValueFactory(new PropertyValueFactory<>("Amt"));
        cAmount.setCellFactory(column -> {
            return new TableCell<CorpDataSales, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    //System.out.println(item);
                    if (item == null || empty) {
                        setText(null);
                        //cAmount.setStyle("-fx-prompt-text-fill: black;");
                        //setStyle("-fx-text-fill: derive(-fx-control-inner-background,-100%)");
                        //setStyle("");
                    } else {
                        // Format date.
                        //cAmount.setStyle("-fx-prompt-text-fill: black;");
                        setText($format.format(item));
                    }
                }
            };
        }); 
        cPostDate.setCellValueFactory(new PropertyValueFactory<>("PostDate"));
        cPostDate.setCellFactory(column -> {
            return new TableCell<CorpDataSales, LocalDate>() {
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
        cMGR.setCellValueFactory(new PropertyValueFactory<>("Mgr"));
        SalesTable.setFixedCellSize(30.0);

        
        
        cPdoutsDate.setCellValueFactory(new PropertyValueFactory<>("SalesDate"));
        cPdoutsDate.setCellFactory(column -> {
            return new TableCell<PdoutsSales, LocalDate>() {
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
        cPdoutsDay.setCellValueFactory(new PropertyValueFactory<>("Day"));
        cPdoutsDepart.setCellValueFactory(new PropertyValueFactory<>("Depart"));
        cPdoutsType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        cPdoutsItemName.setCellValueFactory(new PropertyValueFactory<>("Item_Name"));
        cPdoutsAmount.setCellValueFactory(new PropertyValueFactory<>("Amt"));
        cPdoutsAmount.setCellFactory(column -> {
            return new TableCell<PdoutsSales, Double>() {
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
        cPdoutsMemo.setCellValueFactory(new PropertyValueFactory<>("Memo"));
        cPdoutsPostDate.setCellValueFactory(new PropertyValueFactory<>("PostDate"));
        cPdoutsPostDate.setCellFactory(column -> {
            return new TableCell<PdoutsSales, LocalDate>() {
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
        cPdoutsMGR.setCellValueFactory(new PropertyValueFactory<>("Mgr"));
        pdoutsTable.setFixedCellSize(30.0);


        
        
        changerDate.setCellValueFactory(new PropertyValueFactory<>("changerDate"));
        changerDate.setCellFactory(column -> {
            return new TableCell<ChangerSales, LocalDate>() {
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
        changerMGR.setCellValueFactory(new PropertyValueFactory<>("ChangerMGR"));
        changerATM.setCellValueFactory(new PropertyValueFactory<>("ChangerATM"));
        changerATM.setCellFactory(column -> {
            return new TableCell<ChangerSales, Double>() {
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
        
        bCars.setCellValueFactory(new PropertyValueFactory<>("BCars"));
        bCars.setCellFactory(column -> {
            return new TableCell<ChangerSales, Double>() {
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
        changer1.setCellValueFactory(new PropertyValueFactory<>("changer1"));
        changer1.setCellFactory(column -> {
            return new TableCell<ChangerSales, Double>() {
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
        changer2.setCellValueFactory(new PropertyValueFactory<>("changer2"));
        changer2.setCellFactory(column -> {
            return new TableCell<ChangerSales, Double>() {
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
        changer3.setCellValueFactory(new PropertyValueFactory<>("changer3"));
        changer3.setCellFactory(column -> {
            return new TableCell<ChangerSales, Double>() {
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
        changer4.setCellValueFactory(new PropertyValueFactory<>("changer4"));
        changer4.setCellFactory(column -> {
            return new TableCell<ChangerSales, Double>() {
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
        changer5.setCellValueFactory(new PropertyValueFactory<>("changer5"));
        changer5.setCellFactory(column -> {
            return new TableCell<ChangerSales, Double>() {
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
        changer8.setCellValueFactory(new PropertyValueFactory<>("changer8"));
        changer8.setCellFactory(column -> {
            return new TableCell<ChangerSales, Double>() {
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
        changer9.setCellValueFactory(new PropertyValueFactory<>("changer9"));
        changer9.setCellFactory(column -> {
            return new TableCell<ChangerSales, Double>() {
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
        changer12.setCellValueFactory(new PropertyValueFactory<>("changer12"));
        changer12.setCellFactory(column -> {
            return new TableCell<ChangerSales, Double>() {
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
        changer14.setCellValueFactory(new PropertyValueFactory<>("changer14"));
        changer14.setCellFactory(column -> {
            return new TableCell<ChangerSales, Double>() {
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
        //cAmount.setStyle("-fx-prompt-text-fill: #000000;");
        changerTable.setFixedCellSize(30.0);
        salesDept.setValue("CAFE");
        salesItem_Name.setValue("CAFE CASH");
        salesType.setValue("CASH");
        pdoutsDept.setValue("CAFE");
        pdoutsItem_Name.setValue("CAFE");
        pdoutsType.setValue("GROSS SALES");
        Platform.runLater(()->TabPaneClicked());
        Platform.runLater(()->WhichTabPane());
    }    
    
    
    
    
    
    
    
    
    //SALES TABLE / TAB
    public void DeleteSalesRecord() {
        int n = new messageBox().showNewMemberAlert(Alert.AlertType.CONFIRMATION, stageV, "Are You Sure", "QUESTION: \n Are you sure you want to delete this record? ");
        if (n > 0) {
        try {
            new CorpDataFXDB().DeleteSalesRecord(String.valueOf(trv.getTrID()));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        GetSalesTable();
        salesresetfields();
        }
    }
    
    public void AddEditSalesRecord() {
        boolean t = false;
        CorpDataSales cds = null;
        if (salesAMT.getText().trim().isEmpty()) {
            salesAMT.setText(String.valueOf(0.0));
        }
        if ("Add".equals(salesAddEdit.getText())) {t = true;}

        if (t) {
            cds = new CorpDataSales(gd.getIntDate(Date.valueOf(salesDate.getValue())),  MGR, salesDept.getValue().toString(), salesType.getValue().toString(), salesItem_Name.getValue().toString(), salesDate.getValue(), LocalDate.now(), Double.parseDouble(salesAMT.getText()), 0);
        } else {
            cds = new CorpDataSales(gd.getIntDate(Date.valueOf(salesDate.getValue())), MGR, salesDept.getValue().toString(), salesType.getValue().toString(), salesItem_Name.getValue().toString(), salesDate.getValue(), LocalDate.now(), Double.parseDouble(salesAMT.getText()), trv.getTrID());
        }
        try {
            new CorpDataFXDB().PutSalesRecord(cds, t);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        GetSalesTable();
        salesresetfields();
                                        
    }
    
    public void GetSalesTableRow() {
        trv = SalesTable.getSelectionModel().getSelectedItem();
        salesDept.setValue(trv.getDepart());
        salesItem_Name.setValue(trv.getItem_Name());
        salesType.setValue(trv.getType());
        salesAMT.setText(String.valueOf(trv.getAmt()));
        salesDate.setValue(trv.getSalesDate());
        salesAddEdit.setText("Update");
        salesDelete.setDisable(false);
    }
    
    private void salesresetfields() {
        salesDelete.setDisable(true);
        salesAddEdit.setText("Add");
        salesAMT.setText("");
        trv = null;
        salesDept.requestFocus();
        salesDept.show();
    }
    
    private void GetSalesTable() {
        SalesTable.getItems().clear();
        try {SalesTable.getItems().addAll(new CorpDataFXDB().GetDeposits());} catch (SQLException ex) {System.out.println(ex);}
    } 
    
    private void FillSalesComboBox() {
        List<String> myList;
        try {
            myList = Files.lines(Paths.get(dbsp.pathNameCorp2 + "Depot_1.txt")).collect(Collectors.toList());
            String[] cmbList = new String[myList.size()];            
            for (int i = 0; i<myList.size(); i++) {
                cmbList[i] = myList.get(i) + "\n";            
            }
            ComboBox<String> cmb = salesDept;
            cmb.getItems().clear();
            cmb.setTooltip(new Tooltip());
            cmb.getItems().addAll(cmbList);
            new ComboBoxAutoComplete_Corp<String>(cmb);
        } catch (IOException e) {
            System.out.println("Can't find Depart.txt file" + e);
        }
        
        
        
        
        List<String> myList2;
        try {
            myList2 = Files.lines(Paths.get(dbsp.pathNameCorp2 + "Depot_3.txt")).collect(Collectors.toList());
            String[] cmbList2 = new String[myList2.size()];            
            for (int i = 0; i<myList2.size(); i++) {
                cmbList2[i] = myList2.get(i) + "\n";            
            }
            ComboBox<String> cmb2 = salesType;
            cmb2.getItems().clear();
            cmb2.setTooltip(new Tooltip());
            cmb2.getItems().addAll(cmbList2);
            new ComboBoxAutoComplete_Corp<String>(cmb2);
        } catch (IOException e) {
            System.out.println("Can't find Depart.txt file" + e);
        }
        
        
        
        
        
        
        List<String> myList3;
        try {
            myList3 = Files.lines(Paths.get(dbsp.pathNameCorp2 + "Depot_2.txt")).collect(Collectors.toList());
            String[] cmbList3 = new String[myList3.size()];            
            for (int i = 0; i<myList3.size(); i++) {
                cmbList3[i] = myList3.get(i) + "\n";            
            }
            ComboBox<String> cmb3 = salesItem_Name;
            cmb3.getItems().clear();
            cmb3.setTooltip(new Tooltip());
            cmb3.getItems().addAll(cmbList3);
            new ComboBoxAutoComplete_Corp<String>(cmb3);
        } catch (IOException e) {
            System.out.println("Can't find Depart.txt file" + e);
        }
    
    }
    //END SALES TABLE / TAB
    
    
    
    
    //PDOUTS TABLE / TAB
    public void DeletePdoutRecord() {
        int n = new messageBox().showNewMemberAlert(Alert.AlertType.CONFIRMATION, stageV, "Are You Sure", "QUESTION: \n Are you sure you want to delete this record? ");
        if (n > 0) {
        try {
            new CorpDataFXDB().DeletePdoutRecord(String.valueOf(pTrv.getTrID()));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        GetPdoutsTable();
        Pdoutsresetfields();
        }
    }
    
    public void AddEditPdoutsRecord() {
        boolean t = false;
        PdoutsSales pds = null;
        if (pdoutsAMT.getText().trim().isEmpty()) {
            pdoutsAMT.setText(String.valueOf(0.0));
        }
        if ("Add".equals(pdoutsAddEdit.getText())) {t = true;}
        if (t) {
            pds = new PdoutsSales(gd.getIntDate(Date.valueOf(pdoutsDate.getValue())),  MGR, pdoutsDept.getValue().toString(), pdoutsType.getValue().toString(), pdoutsItem_Name.getValue().toString(), pdoutsDate.getValue(), LocalDate.now(), Double.parseDouble(pdoutsAMT.getText()), pdoutsMemo.getText(), 0);
        } else {
            pds = new PdoutsSales(gd.getIntDate(Date.valueOf(pdoutsDate.getValue())), MGR, pdoutsDept.getValue().toString(), pdoutsType.getValue().toString(), pdoutsItem_Name.getValue().toString(), pdoutsDate.getValue(), LocalDate.now(), Double.parseDouble(pdoutsAMT.getText()), pdoutsMemo.getText(), pTrv.getTrID());
        }
        try {
            new CorpDataFXDB().PutPdoutsRecord(pds, t);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        GetPdoutsTable();
        Pdoutsresetfields();
                                        
    }
    
    public void GetPdoutTableRow() {
        pTrv = pdoutsTable.getSelectionModel().getSelectedItem();
        pdoutsDept.setValue(pTrv.getDepart());
        pdoutsItem_Name.setValue(pTrv.getItem_Name());
        pdoutsType.setValue(pTrv.getType());
        pdoutsAMT.setText(String.valueOf(pTrv.getAmt()));
        pdoutsMemo.setText(pTrv.getMemo());
        pdoutsDate.setValue(pTrv.getSalesDate());
        pdoutsAddEdit.setText("Update");
        pdoutsDelete.setDisable(false);
    } 
    
    private void Pdoutsresetfields() {
        pdoutsDelete.setDisable(true);
        pdoutsAddEdit.setText("Add");
        pdoutsAMT.setText("");
        pdoutsMemo.setText("");
        pTrv = null;
        pdoutsDept.requestFocus();
        pdoutsDept.show();
    }
    
    private void GetPdoutsTable() {
        pdoutsTable.getItems().clear();
        try {pdoutsTable.getItems().addAll(new CorpDataFXDB().GetPdouts());} catch (SQLException ex) {System.out.println(ex);}
    } 
    
    private void FillPDComboBox() {
        List<String> myList;
        try {
            myList = Files.lines(Paths.get(dbsp.pathNameCorp2 + "Depot_1.txt")).collect(Collectors.toList());
            String[] cmbList = new String[myList.size()];            
            for (int i = 0; i<myList.size(); i++) {
                cmbList[i] = myList.get(i) + "\n";            
            }
            ComboBox<String> cmb = pdoutsDept;
            cmb.getItems().clear();
            cmb.setTooltip(new Tooltip());
            cmb.getItems().addAll(cmbList);
            new ComboBoxAutoComplete_Corp<String>(cmb);
        } catch (IOException e) {
            System.out.println("Can't find Depart.txt file" + e);
        }
        
        
        
        /*
        List<String> myList;
        try {
            myList = Files.lines(Paths.get(dbsp.pathNameCorp2 + "Depot_1.txt")).collect(Collectors.toList());
            pdoutsDept.setItems(FXCollections.observableArrayList(myList));
            pdoutsDept.getSelectionModel().select(0);
        } catch (IOException e) {
            System.out.println("Can't find Depart.txt file" + e);
        }
        */
        
        List<String> myList2;
        try {
            myList2 = Files.lines(Paths.get(dbsp.pathNameCorp2 + "Pdouts_3.txt")).collect(Collectors.toList());
            String[] cmbList2 = new String[myList2.size()];            
            for (int i = 0; i<myList2.size(); i++) {
                cmbList2[i] = myList2.get(i) + "\n";            
            }
            ComboBox<String> cmb2 = pdoutsItem_Name;
            cmb2.getItems().clear();
            cmb2.setTooltip(new Tooltip());
            cmb2.getItems().addAll(cmbList2);
            new ComboBoxAutoComplete_Corp<String>(cmb2);
        } catch (IOException e) {
            System.out.println("Can't find Depart.txt file" + e);
        }
        
        /*
        List<String> myList2;
        try {
            myList = Files.lines(Paths.get(dbsp.pathNameCorp2 + "Pdouts_3.txt")).collect(Collectors.toList());
            pdoutsItem_Name.setItems(FXCollections.observableArrayList(myList));
            pdoutsItem_Name.getSelectionModel().select(0);
        } catch (IOException e) {
            System.out.println("Can't find Pdouts2.txt file" + e);
        }
        */
        
        List<String> myList3;
        try {
            myList3 = Files.lines(Paths.get(dbsp.pathNameCorp2 + "Pdouts_2.txt")).collect(Collectors.toList());
            String[] cmbList3 = new String[myList3.size()];            
            for (int i = 0; i<myList3.size(); i++) {
                cmbList3[i] = myList3.get(i) + "\n";            
            }
            ComboBox<String> cmb3 = pdoutsType;
            cmb3.getItems().clear();
            cmb3.setTooltip(new Tooltip());
            cmb3.getItems().addAll(cmbList3);
            new ComboBoxAutoComplete_Corp<String>(cmb3);
        } catch (IOException e) {
            System.out.println("Can't find Depart.txt file" + e);
        }
        
        /*
        List<String> myList3;
        try {
            myList2 = Files.lines(Paths.get(dbsp.pathNameCorp2  + "Pdouts_2.txt")).collect(Collectors.toList());
            pdoutsType.setItems(FXCollections.observableArrayList(myList2));
            pdoutsType.getSelectionModel().select(0);
        } catch (IOException e) {
            System.out.println("Can't find Pdouts3.txt file" + e);
        }
        */
    }
    //END PDOUTS TABLE / TAB
    
    
    
    
    //CHANGER TABLE / TAB
    public void DeleteChnagersRecord() {
        int n = new messageBox().showNewMemberAlert(Alert.AlertType.CONFIRMATION, stageV, "Are You Sure", "QUESTION: \n Are you sure you want to delete this record? ");
        if (n > 0) {
        try {
            new CorpDataFXDB().DeleteChangerRecord(String.valueOf(cTrv.getTrID()));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        GetChangerTable();
        ChangersResetFields();
        }
    }
    
    public void AddEditChangerRecord() {
        new ClubFunctions().CheckForZero(basePanel);
        boolean t = false;
        ChangerSales cds = null;
        if (changer_12.getText().trim().isEmpty()) {
            changer_12.setText(String.valueOf(0.0));
        }
        if ("Add".equals(changerAddEdit.getText())) {t = true;}
        if (t) {
                                //String changerMGR, LocalDate changerDate, Double changerATM, Double bCars, Double changer3, Double changer4, Double changer5, Double changer8, Double changer9, Double changer12, Double changer14, int trID
            cds = new ChangerSales(MGR, changerDt.getValue(), Double.parseDouble(changer_ATM.getText()) * -1, Double.parseDouble(changer_bCars.getText()), Double.parseDouble(changer_1.getText()) * -1, Double.parseDouble(changer_2.getText()) * -1, Double.parseDouble(changer_3.getText()) * -1, Double.parseDouble(changer_4.getText()) * -1, Double.parseDouble(changer_5.getText()) * -1, Double.parseDouble(changer_8.getText()) * -1, Double.parseDouble(changer_9.getText()) * -1, Double.parseDouble(changer_12.getText()) * -1, Double.parseDouble(changer_14.getText()) * -1, 0);
        } else {
            cds = new ChangerSales(MGR, changerDt.getValue(), Double.parseDouble(changer_ATM.getText()) * -1, Double.parseDouble(changer_bCars.getText()), Double.parseDouble(changer_1.getText()) * -1, Double.parseDouble(changer_2.getText()) * -1, Double.parseDouble(changer_3.getText()) * -1, Double.parseDouble(changer_4.getText()) * -1, Double.parseDouble(changer_5.getText()) * -1, Double.parseDouble(changer_8.getText()) * -1, Double.parseDouble(changer_9.getText()) * -1, Double.parseDouble(changer_12.getText()) * -1, Double.parseDouble(changer_14.getText()) * -1,  cTrv.getTrID());
        }
        try {
            new CorpDataFXDB().PutChangerRecord(cds, t);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        GetChangerTable();
        ChangersResetFields();
                                        
    }
    
    private void ChangersResetFields() {
        changerDelete.setDisable(true);
        changerAddEdit.setText("Add");
        changer_ATM.setText("");
        changer_bCars.setText("");
        changer_1.setText("");
        changer_2.setText("");
        changer_3.setText("");
        changer_4.setText("");
        changer_5.setText("");
        changer_8.setText("");
        changer_9.setText("");
        changer_12.setText("");
        changer_14.setText("");
        cTrv = null;
        changer_bCars.requestFocus();
    }
    
    private void GetChangerTable() {
        changerTable.getItems().clear();
        try {changerTable.getItems().addAll(new CorpDataFXDB().GetChangers());} catch (SQLException ex) {System.out.println(ex);}
    } 
    
    public void GetChangersTableRow() {
        cTrv = changerTable.getSelectionModel().getSelectedItem();
        System.out.println("======= " + cTrv.getChangerDate());
        changerDt.setValue(cTrv.getChangerDate());
        changer_ATM.setText(String.valueOf(cTrv.getChangerATM() * -1));
        changer_bCars.setText(String.valueOf(cTrv.getBCars()));
        changer_1.setText(String.valueOf(cTrv.getChanger1() * -1));
        changer_2.setText(String.valueOf(cTrv.getChanger2() * -1));
        changer_3.setText(String.valueOf(cTrv.getChanger3() * -1));
        changer_4.setText(String.valueOf(cTrv.getChanger4() * -1));
        changer_5.setText(String.valueOf(cTrv.getChanger5() * -1));
        changer_8.setText(String.valueOf(cTrv.getChanger8() * -1));
        changer_9.setText(String.valueOf(cTrv.getChanger9() * -1));
        changer_12.setText(String.valueOf(cTrv.getChanger12() * -1));
        changer_14.setText(String.valueOf(cTrv.getChanger14() * -1));
        changerAddEdit.setText("Update");
        changerDelete.setDisable(false);
    }

    //END CHANGER TABLE / TAB
    
    
    
    
    
    //BEGINGING OF ALL TAB CODE
    
    private void EnterKeyPressed() {
       switch(selectionModel.getSelectedIndex()) {
           case 0:
               if (salesDept.isFocused()) {
                   salesDept.hide();
                   salesType.requestFocus();
                   salesType.show();
                   return;
               }
               if (salesAMT.isFocused()){
                   AddEditSalesRecord();
                   //salesDate.requestFocus();
               }
               if (salesItem_Name.isFocused()) {
                   salesAMT.requestFocus();
                   return;
               }
               if (salesType.isFocused()) {
                   salesItem_Name.requestFocus();
                   salesItem_Name.show();
                   return;
               }
               if (salesDate.isFocused()) {
                   salesDept.requestFocus();
                   //salesDept.setEditable(true);
                   salesDept.show();
               }
               break;
               
           case 1:
               if (pdoutsDate.isFocused()) {
                   pdoutsDept.requestFocus();
                   pdoutsDept.show();
                   return;
               }
               if (pdoutsType.isFocused()) {
                   pdoutsItem_Name.requestFocus();
                   pdoutsItem_Name.show();
                   return;
               }
               if (pdoutsDept.isFocused()) {
                   pdoutsType.requestFocus();
                   pdoutsType.show();
                   return;
               }
               if (pdoutsMemo.isFocused()){
                   AddEditPdoutsRecord();
                   //pdoutsDate.requestFocus();
                   return;
               }
               if (pdoutsAMT.isFocused()){
                   pdoutsMemo.requestFocus();
                   return;
               }
               if (pdoutsItem_Name.isFocused()) {
                   pdoutsAMT.requestFocus();
               }
               break;
               
           case 2:
               if (changer_14.isFocused()){
                   AddEditChangerRecord();
                   //changerDt.requestFocus();
               }
               if (changer_12.isFocused()) {
                   changer_14.requestFocus();
               }
               if (changer_9.isFocused()) {
                   changer_12.requestFocus();
               }
               if (changer_8.isFocused()) {
                   changer_9.requestFocus();
               }
               if (changer_5.isFocused()) {
                   changer_8.requestFocus();
               }
               if (changer_4.isFocused()) {
                   changer_5.requestFocus();
               }
               if (changer_3.isFocused()) {
                   changer_4.requestFocus();
               }
               if (changer_2.isFocused()) {
                   changer_3.requestFocus();
               }
               if (changer_1.isFocused()) {
                   changer_2.requestFocus();
               }
               if (changer_bCars.isFocused()) {
                   changer_1.requestFocus();
               }
               if (changer_ATM.isFocused()) {
                   changer_bCars.requestFocus();
               }
               if (changerDt.isFocused()) {
                   changer_ATM.requestFocus();
               }
               break;
       }
    }
    
        private void reverseEnterKey() {
       switch(selectionModel.getSelectedIndex()) {
           case 0:
               if (salesDept.isFocused()) {
                   salesDept.hide();
                   salesDate.requestFocus();
                   return;
               }
               if (salesType.isFocused()) {
                   salesType.hide();
                   salesDept.requestFocus();
                   salesDept.show();
                   return;
               }
               if (salesItem_Name.isFocused()) {
                   salesItem_Name.hide();
                   salesType.requestFocus();
                   salesType.show();
                   return;
               }
               if (salesAMT.isFocused()) {
                   salesItem_Name.requestFocus();
                   salesItem_Name.show();
               }
               break;
               case 1:
               if (pdoutsDept.isFocused()) {
                   pdoutsDate.requestFocus();
               }
               if (pdoutsType.isFocused()) {
                   pdoutsDept.requestFocus();
                   pdoutsDept.show();
               }
               if (pdoutsItem_Name.isFocused()) {
                   pdoutsType.requestFocus();
                   pdoutsType.show();
               }
               if (pdoutsAMT.isFocused()){
                   pdoutsItem_Name.requestFocus();
                   pdoutsItem_Name.show();
               }
               if (pdoutsMemo.isFocused()){
                   pdoutsAMT.requestFocus();
               }
               break;
               
           case 2:
               if (changer_ATM.isFocused()) {
                   changerDt.requestFocus();
               }
               if (changer_bCars.isFocused()) {
                   changer_ATM.requestFocus();
               }
               if (changer_1.isFocused()) {
                   changer_bCars.requestFocus();
               }
               if (changer_2.isFocused()) {
                   changer_1.requestFocus();
               }
               if (changer_3.isFocused()) {
                   changer_2.requestFocus();
               }
               if (changer_4.isFocused()) {
                   changer_3.requestFocus();
               }
               if (changer_5.isFocused()) {
                   changer_4.requestFocus();
               }
               if (changer_8.isFocused()) {
                   changer_5.requestFocus();
               }
               if (changer_9.isFocused()) {
                   changer_8.requestFocus();
               }
               if (changer_12.isFocused()) {
                   changer_9.requestFocus();
               }
               if (changer_14.isFocused()){
                   changer_12.requestFocus();
               }
               break;
        }
        return;
}

    
    public void Whichdropedit(int g){
        // the other three frames rewuire this function
        String fName = "";
        switch (g) {
            case 1:
                fName = dbStringPath.pathNameCorp2 + "Depot_1.txt";
                break;
            case 2:
                fName = dbStringPath.pathNameCorp2 + "Depot_3.txt";
                break;
            case 3:
                fName = dbStringPath.pathNameCorp2 + "Depot_2.txt";
                break;
            case 4:
                fName = dbStringPath.pathNameCorp2 + "Pdouts_2.txt";
                break;
            case 5:
                fName = dbStringPath.pathNameCorp2 + "Pdouts_3.txt";
                break;
                
        }
        Desktop dsk = Desktop.getDesktop();
        try {  
            dsk.open(new File(fName));
        } catch (IOException ex) {
            System.out.println(ex);
        }

           
    }
    
    public void TabPaneClicked() {
        selectionModel = corpTabPane.getSelectionModel();
        corpTabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            WhichTabPane();
        });
    }
    
    private void CleanTables() {
        if (gd.getday().equals("Tue") || gd.getday().equals("Thu")) {
            try {
                new CorpDataFXDB().CleanData(gd.asSQLDate(LocalDate.now()));
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }

    private void WhichTabPane() {
        switch(selectionModel.getSelectedIndex()) {
            case 0:
            depReports.setVisible(true);
            outsReports.setVisible(false);
            changerReports.setVisible(false);
            depDropEdits.setVisible(true);
            depDropEdits.setTranslateX(-150);
            outsDropEdits.setVisible(false);
            FillSalesComboBox();
            GetSalesTable();
            salesresetfields();
            Platform.runLater(() -> {
                SalesTable.requestFocus();
                SalesTable.getSelectionModel().select(SalesTable.getItems().size()-1);
                SalesTable.scrollTo(SalesTable.getItems().size()-1);
                SalesTable.getFocusModel().focus(SalesTable.getItems().size()-1);
            });
            Platform.runLater(()->{salesDept.requestFocus(); salesDept.show(); salesDelete.setDisable(true);});
            break;
            case 1:
            depReports.setVisible(false);
            outsReports.setVisible(true);
            outsReports.setTranslateX(-138);
            changerReports.setVisible(false);
            depDropEdits.setVisible(false);
            outsDropEdits.setVisible(true);
            outsDropEdits.setTranslateX(-288);
            FillPDComboBox();
            GetPdoutsTable();
            Pdoutsresetfields();
            Platform.runLater(() -> {
                pdoutsTable.requestFocus();
                pdoutsTable.getSelectionModel().select(pdoutsTable.getItems().size()-1);
                pdoutsTable.scrollTo(pdoutsTable.getItems().size()-1);
                pdoutsTable.getFocusModel().focus(pdoutsTable.getItems().size()-1);
            });
            Platform.runLater(()->{pdoutsDept.requestFocus(); pdoutsDept.show(); pdoutsDelete.setDisable(true);});
            break;
            case 2:
            depReports.setVisible(false);
            outsReports.setVisible(false);
            changerReports.setVisible(true);
            changerReports.setTranslateX(-438);
            depDropEdits.setVisible(false);
            outsDropEdits.setVisible(false);
            GetChangerTable();
            ChangersResetFields();
            Platform.runLater(() -> {
                changerTable.requestFocus();
                changerTable.getSelectionModel().select(changerTable.getItems().size()-1);
                changerTable.scrollTo(changerTable.getItems().size()-1);
                changerTable.getFocusModel().focus(changerTable.getItems().size()-1);
        });
        Platform.runLater(()->{changer_ATM.requestFocus(); changerDelete.setDisable(true);});
            break;
                
                
        }
    }
    
    private void BuildDepReports() {
        MenuItem item1 = new MenuItem(" Yesturday ");
        MenuItem item2 = new MenuItem(" 7 Day ");
        MenuItem item3 = new MenuItem(" 30 Day ");
        MenuItem item4 = new MenuItem(" 31 Day ");
        MenuItem item5 = new MenuItem(" Month to Date ");

        Menu menu1 = new Menu(" Jim's Favorite ");
        MenuItem csItem1 = new MenuItem(" 1 Day ");
        MenuItem csItem2 = new MenuItem(" 7 Days ");
        MenuItem csItem3 = new MenuItem(" Month To Date ");
        menu1.getItems().addAll(csItem1, csItem2, csItem3);

        item1.setOnAction((ActionEvent event) -> {rPorts.salesreport(gd.asSQLDate(salesDate.getValue()), 1); });
        item2.setOnAction((ActionEvent event) -> {rPorts.salesreport(gd.asSQLDate(salesDate.getValue()), 4); });
        item3.setOnAction((ActionEvent event) -> {rPorts.salesreport(gd.asSQLDate(salesDate.getValue()), 2); });
        item4.setOnAction((ActionEvent event) -> {rPorts.salesreport(gd.asSQLDate(salesDate.getValue()), 3); });
        item5.setOnAction((ActionEvent event) -> {rPorts.salesreport(gd.asSQLDate(salesDate.getValue()), 5); });

        csItem1.setOnAction((ActionEvent event) -> {rPorts.jimsfav(gd.asSQLDate(salesDate.getValue()), 1); });
        csItem2.setOnAction((ActionEvent event) -> {rPorts.jimsfav(gd.asSQLDate(salesDate.getValue()), 7); });
        csItem3.setOnAction((ActionEvent event) -> {rPorts.jimsfav(gd.asSQLDate(salesDate.getValue()), 5); });

        depContextMenu.getItems().addAll(item1, item2, item3, item4, item5, menu1);

        depReports.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                depContextMenu.show(depReports, boundsMenu1.getMinX()+270, boundsMenu1.getMinY()+50);
            }
        });

        depReports.setOnContextMenuRequested((ContextMenuEvent event) -> {
            depContextMenu.show(depReports, boundsMenu1.getMaxX()+270, boundsMenu1.getMaxY()+50);
        });
    }
    
    private void BuildOutsReports() {
        MenuItem item1 = new MenuItem(" Yesturday ");
        MenuItem item2 = new MenuItem(" 7 Day ");
        MenuItem item3 = new MenuItem(" 30 Day ");
        MenuItem item4 = new MenuItem(" 31 Day ");
        MenuItem item5 = new MenuItem(" Month to Date ");

        item1.setOnAction((ActionEvent event) -> {rPorts.couponreport(gd.asSQLDate(salesDate.getValue()), 1); });
        item2.setOnAction((ActionEvent event) -> {rPorts.couponreport(gd.asSQLDate(salesDate.getValue()), 4); });
        item3.setOnAction((ActionEvent event) -> {rPorts.couponreport(gd.asSQLDate(salesDate.getValue()), 2); });
        item4.setOnAction((ActionEvent event) -> {rPorts.couponreport(gd.asSQLDate(salesDate.getValue()), 3); });
        item5.setOnAction((ActionEvent event) -> {rPorts.couponreport(gd.asSQLDate(salesDate.getValue()), 5); });

        outsContextMenu.getItems().addAll(item1, item2, item3, item4, item5);

        outsReports.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                outsContextMenu.show(outsReports, boundsMenu2.getMinX()+270, boundsMenu2.getMinY()+50);
            }
        });

        outsReports.setOnContextMenuRequested((ContextMenuEvent event) -> {
            outsContextMenu.show(outsReports, boundsMenu2.getMaxX()+270, boundsMenu2.getMaxY()+50);
        });
    }

    private void BuildDepDropEdits() {
        MenuItem item1 = new MenuItem(" Departments ");
        MenuItem item2 = new MenuItem(" Type ");
        MenuItem item3 = new MenuItem(" Item Name ");
        MenuItem item4 = new MenuItem(" Refresh Drop Edits ");

        item1.setOnAction((ActionEvent event) -> {Whichdropedit(1);});
        item2.setOnAction((ActionEvent event) -> {Whichdropedit(2);});
        item3.setOnAction((ActionEvent event) -> {Whichdropedit(3);});
        item4.setOnAction((ActionEvent event) -> {FillSalesComboBox();});

        depDropEditsContextMenu.getItems().addAll(item1, item2, item3, item4);

        depDropEdits.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                depDropEditsContextMenu.show(depDropEdits, boundsMenu3.getMinX()+270, boundsMenu3.getMinY()+50);
            }
        });

        depDropEdits.setOnContextMenuRequested((ContextMenuEvent event) -> {
            depDropEditsContextMenu.show(depDropEdits, boundsMenu3.getMaxX()+270, boundsMenu3.getMaxY()+50);
        });
    }
    
    private void BuildOutsDropEdits() {
        MenuItem item1 = new MenuItem(" Type ");
        MenuItem item2 = new MenuItem(" Item Name ");
        MenuItem item3 = new MenuItem(" Refresh Drop Edits ");

        item1.setOnAction((ActionEvent event) -> {Whichdropedit(4);});
        item2.setOnAction((ActionEvent event) -> {Whichdropedit(5);});

        outsDropEditsContextMenu.getItems().addAll(item1, item2, item3);

        outsDropEdits.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                outsDropEditsContextMenu.show(outsDropEdits, boundsMenu4.getMinX()+270, boundsMenu4.getMinY()+50);
            }
        });

        outsDropEdits.setOnContextMenuRequested((ContextMenuEvent event) -> {
            outsDropEditsContextMenu.show(outsDropEdits, boundsMenu4.getMaxX()+270, boundsMenu4.getMaxY()+50);
        });
    }
    
    
    
      private void BuildChangerReports() {
        //MenuItem item1 = new MenuItem(" Yesturday ");
        MenuItem item2 = new MenuItem(" 7 Day ");
        MenuItem item3 = new MenuItem(" 30 Day ");
        MenuItem item4 = new MenuItem(" 31 Day ");
        //MenuItem item5 = new MenuItem(" Month to Date ");

        //item1.setOnAction((ActionEvent event) -> {rPorts.couponreport(gd.asSQLDate(salesDate.getValue()), 1); });
        item2.setOnAction((ActionEvent event) -> {rPorts.RunChangerReport(gd.asSQLDate(changerDt.getValue()), 7); });
        item3.setOnAction((ActionEvent event) -> {rPorts.RunChangerReport(gd.asSQLDate(changerDt.getValue()), 30); });
        item4.setOnAction((ActionEvent event) -> {rPorts.RunChangerReport(gd.asSQLDate(changerDt.getValue()), 31); });
        //item5.setOnAction((ActionEvent event) -> {rPorts.couponreport(gd.asSQLDate(salesDate.getValue()), 5); });

        changersContextMenu.getItems().addAll(item2, item3, item4);

        changerReports.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                changersContextMenu.show(changerReports, boundsMenu2.getMinX()+390, boundsMenu2.getMinY()+70);
            }
        });

        changerReports.setOnContextMenuRequested((ContextMenuEvent event) -> {
            changersContextMenu.show(outsReports, boundsMenu2.getMaxX()+270, boundsMenu2.getMaxY()+50);
        });
    }
    
    
    
    
    public void RunChangerReport() {
        FileOutputStream out = null;
        java.util.Date date_s = gd.asSQLDate(changerDt.getValue());
        //JOptionPane.showMessageDialog(null, date_s);
        SimpleDateFormat dts = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        java.util.Date date = date_s;
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
        //JOptionPane.showMessageDialog(null,dt1.format(date));

        //String dt2 = dt4.getText();
        //dt.getText();
        //JOptionPane.showMessageDialog(null, Date.parse(dt2));
        try {
            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://" + dbStringPath.pathNameCorp2 + "Corp_access.accdb");
            Statement s = conn.createStatement();

            ResultSet rs = s.executeQuery("SELECT * FROM [Changers] where DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 7 DAY) AND DDate < #" + dt1.format(date) + "# ");
            System.out.println("SELECT * FROM [Changers] where DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 7 DAY) AND DDate < #" + dt1.format(date) + "# ");
            new File(dbStringPath.pathNameCorp2 + "Rpt_Changers.xls").setWritable(true);
            out = new FileOutputStream(dbStringPath.pathNameCorp2 + "Rpt_Changers.xls");
            Workbook wb = new HSSFWorkbook();
            Sheet st = wb.createSheet();
            int rowcount = 0;
            CellStyle cellStyle = wb.createCellStyle();
            CellStyle cs2 = wb.createCellStyle();
            CellStyle head = wb.createCellStyle();
            CreationHelper createHelper = wb.getCreationHelper();

            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("mm/dd/yyyy"));
            cs2.setDataFormat((short)8);

            head.setAlignment(HorizontalAlignment.CENTER);
            org.apache.poi.ss.usermodel.Font boldFont = wb.createFont();
            boldFont.setFontHeightInPoints((short)14);
            head.setFont(boldFont);

            st.setMargin(st.LeftMargin, 0.25);
            st.setMargin(st.RightMargin, 0.25 /* inches */ );
            st.autoSizeColumn(0);

            String[] columns = {"Date", "Emp", "ATM", "BCars", "Ch 3", "Ch 4", "Ch 5", "Ch 8", "Ch 9", "Ch 12", "Ch 14"};

            // Create a Row
            Row headerRow = st.createRow(0);
            for(int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(head);
                st.setColumnWidth((short) (i), (short) ((50 * 3) / ((double) 1 / 20)));
            }

            rowcount = 1;
            while (rs.next()) {
                Row row = st.createRow(rowcount);
                Cell cell = row.createCell(0);
                cell.setCellValue(rs.getDate("DDate"));
                cell.setCellStyle(cellStyle);
                cell = row.createCell(1);
                cell.setCellValue(rs.getString("MGR"));
                cell = row.createCell(2);
                cell.setCellValue(rs.getDouble("atm"));
                cell.setCellStyle(cs2);
                cell = row.createCell(3);
                cell.setCellValue(rs.getDouble("Bumper_cars"));
                cell.setCellStyle(cs2);
                cell = row.createCell(4);
                cell.setCellValue(rs.getDouble("Changer_3"));
                cell.setCellStyle(cs2);
                cell = row.createCell(5);
                cell.setCellValue(rs.getDouble("Changer_4"));
                cell.setCellStyle(cs2);
                cell = row.createCell(6);
                cell.setCellValue(rs.getDouble("Changer_5"));
                cell.setCellStyle(cs2);
                cell = row.createCell(7);
                cell.setCellValue(rs.getDouble("Changer_8"));
                cell.setCellStyle(cs2);
                cell = row.createCell(8);
                cell.setCellValue(rs.getDouble("Changer_9"));
                cell.setCellStyle(cs2);
                cell = row.createCell(9);
                cell.setCellValue(rs.getDouble("CHanger_12"));
                cell.setCellStyle(cs2);
                cell = row.createCell(10);
                cell.setCellValue(rs.getDouble("CHanger_14"));
                cell.setCellStyle(cs2);
                rowcount++;
            }

            ResultSet rs2 = s.executeQuery("SELECT SUM(atm) as attl, SUM(Bumper_cars) as bttl, SUM(Changer_3) as 3ttl, SUM(Changer_4) as 4ttl, SUM(Changer_5) as 5ttl, SUM(Changer_8) as 8ttl, SUM(Changer_9) as 9ttl, SUM(Changer_12) as 12ttl, SUM(Changer_14) as 14ttl FROM [Changers] where DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 7 DAY) AND DDate < #" + dt1.format(date) + "# ");
            while (rs2.next()) {
                Row row = st.createRow(rowcount);
                Cell cell = row.createCell(0);
                cell.setCellValue("Total");
                cell.setCellStyle(head);
                cell = row.createCell(1);
                cell.setCellValue("");
                cell = row.createCell(2);
                cell.setCellValue(rs2.getDouble("attl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(3);
                cell.setCellValue(rs2.getDouble("Bttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(4);
                cell.setCellValue(rs2.getDouble("3ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(5);
                cell.setCellValue(rs2.getDouble("4ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(6);
                cell.setCellValue(rs2.getDouble("5ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(7);
                cell.setCellValue(rs2.getDouble("8ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(8);
                cell.setCellValue(rs2.getDouble("9ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(9);
                cell.setCellValue(rs2.getDouble("12ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(10);
                cell.setCellValue(rs2.getDouble("14ttl"));
                cell.setCellStyle(cs2);
                rowcount++;
            }

            st.autoSizeColumn(0);
            st.autoSizeColumn(1);
            st.autoSizeColumn(2);
            st.autoSizeColumn(3);
            st.autoSizeColumn(4);
            st.autoSizeColumn(5);
            st.autoSizeColumn(6);

            wb.write(out);
            out.close();
            conn.close();
            s.close();
            new File(dbStringPath.pathNameCorp2 + "Rpt_Changers.xls").setReadOnly();
            //this.setAlwaysOnTop(false);
            Desktop dsk = Desktop.getDesktop();
            dsk.open(new File(dbStringPath.pathNameCorp2 + "Rpt_Changers.xls"));
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }


    public void DepReportsMouseOver() {depContextMenu.show(depReports, boundsMenu1.getMaxX() + 390, boundsMenu1.getMaxY()+90);}
    public void OutsReportsMouseOver() {outsContextMenu.show(outsReports, boundsMenu2.getMaxX() + 390, boundsMenu2.getMaxY()+90);}
    public void DepDropEditsMouseOver() {depDropEditsContextMenu.show(depDropEdits, boundsMenu3.getMaxX() + 530, boundsMenu3.getMaxY()+90);}
    public void OutsDropditsMouseOver() {outsDropEditsContextMenu.show(outsDropEdits, boundsMenu4.getMaxX() + 530, boundsMenu4.getMaxY()+90);}
    
    
    
    private void SetKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.TAB && ke.isShiftDown()) {
                ke.consume();
                reverseEnterKey();
            } else {
                if (ke.getCode() == KeyCode.TAB) {
                    KeyListener(ke);
                    ke.consume();
                }
            }
            if (ke.getCode() == KeyCode.F1) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F2) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F3) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F4) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F5) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F6) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F7) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F8) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F9) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F10) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F11) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F12) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.DOWN) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.ESCAPE) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.ENTER) {
                KeyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.DOWN) {
                KeyListener(ke);
                ke.consume();
            }
        });
    }

    public void KeyListener(KeyEvent event) {
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
                //printButton.fire();
                break;
            case F9:
                break;
            case F10:
                break;
            case F11:
                break;
            case F12:
                break;
            case ESCAPE:
                ExitButtonPushed();
                break;
            case ENTER:
                EnterKeyPressed();
                break;
            case TAB:
                EnterKeyPressed();
                break;
            case DOWN:
                break;
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void ExitButtonPushed() {
        try {
            new CorpDataFXDB().DisConnect();
        } catch(Exception e) {}
        stageV = (Stage) exitLabel.getScene().getWindow();
        stageV.close();
    }
    

}
