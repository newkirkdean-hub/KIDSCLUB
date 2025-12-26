package views.settings;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;
import commoncodes.GetReceipts;
import commoncodes.IsItANumber1;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.JOptionPane;
import models.club.Memtick;
import models.club.rCeipts;
import models.settings.BridgeReceipts;
import models.toys.Toys;
import pReceipts.print;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class BridgeReceiptsController implements Initializable {
    @FXML private Pane root;
    //@FXML private Button cancelButton;
    //@FXML private Button updateButton;
    @FXML private GridPane gPane;
    @FXML private Label c1L;
    @FXML public static String tableID;
    
    @FXML private TableView<BridgeReceipts> cTable;
    @FXML private TableColumn<BridgeReceipts, String> C1;
    @FXML private TableColumn<BridgeReceipts, String> receipts;
    @FXML private TableColumn<BridgeReceipts, String> bridge;
    @FXML private TableColumn<BridgeReceipts, String> cafe;
    
    
    
    public static employeeFX eFX;
    private static SceneChanger_Main sc = new SceneChanger_Main();
    public static String cssC;
    private static SimpleDateFormat timeformatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    ObservableList<BridgeReceipts> data = FXCollections.observableArrayList();
    public static final dbStringPath DBSP = new dbStringPath();
    public static int superLevel;
    private PrintWriter pw;
    private static BridgeReceipts itemSelected;
    private static BridgeReceipts newdata;
    public static String WHSTAGE, rNumber = "";
    int datasize = 0;
    public static ArrayList<rCeipts> rCeipts;
    private static Style Format = new Style();
    private static final GetReceipts getR = new GetReceipts();
    private static final String RECEIPTS_DATE = "" + String.valueOf(LocalDate.now().getMonthValue()) + "" + new IsItANumber1().isLessThenTen(String.valueOf(LocalDate.now().getDayOfMonth())) + "";



    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBSP.setName();
        root.getStylesheets().add(cssC);
        setKeyCodes();
        C1.setCellValueFactory(new PropertyValueFactory<>("Title"));
        receipts.setCellValueFactory(new PropertyValueFactory<>("Receipt"));
        bridge.setCellValueFactory(new PropertyValueFactory<>("Bridge"));
        cafe.setCellValueFactory(new PropertyValueFactory<>("Cafe"));
        getTableItems();
        //hbox.setVisible(false);
        //updateButton.setVisible(false);
        System.out.println(WHSTAGE);
        cTable.setFixedCellSize(30.0);
        cTable.addEventFilter(ScrollEvent.ANY, Event::consume);

    }

    private void getTableItems() {
        cTable.getItems().clear();
        try {
            
            cTable.getItems().addAll(getDataBridgeReceipts());
            gPane.setGridLinesVisible(false);
            if (datasize == 0) {
            System.out.println("the datasize is ---------------0 : " + datasize + " " + data.get(0).getTitle());
                gPane.setPrefHeight(200);
                root.setPrefHeight(201);
            }
            if (datasize == 1) {
            System.out.println("the datasize is ---------------1 : " + datasize + " " + data.get(0).getTitle());
                gPane.setPrefHeight(datasize * 60);
                root.setPrefHeight(datasize * 61);
            }
            if (datasize == 2) {
                gPane.setPrefHeight(datasize * 50);
                root.setPrefHeight(datasize * 51);
            }
            if (datasize == 3) {
                gPane.setPrefHeight(datasize * 45);
                root.setPrefHeight(datasize * 46);
            }
            if (datasize == 4) {
                gPane.setPrefHeight(datasize * 40);
                root.setPrefHeight(datasize * 41);
            }
            if (datasize >= 5) {
                gPane.setPrefHeight(datasize * 40);
                root.setPrefHeight(datasize * 41);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        cTable.setEditable(true);
        //C2.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    public void tableItemSelected(MouseEvent e) {
        itemSelected = cTable.getSelectionModel().getSelectedItem();
        //c1L.setText(itemSelected.getTitle());
        tableID = itemSelected.getOther();
        System.out.println(itemSelected.getBridge());
        //updateButton.setVisible(true); 
        System.out.println("Click Count " + e.getClickCount() + " " + tableID);
        if (e.getClickCount() == 2) {
            setReceipts();
            //printReceipt();
        }

    }

    public void tableItemTouched() {
        itemSelected = cTable.getSelectionModel().getSelectedItem();
        //c1L.setText(itemSelected.getTitle());
        tableID = itemSelected.getOther();
        //updateButton.setVisible(true); 
        //setReceipts();
        printReceipt();

    }

    public ObservableList<BridgeReceipts> getDataBridgeReceipts() throws SQLException {
        //ObservableList<BridgeReceipts> data = FXCollections.observableArrayList();
        data.clear();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + DBSP.pathNameClubDBs + "Other.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();

        if ("Cafe".equals(WHSTAGE)) {
            rs = st.executeQuery("SELECT BridgeReceipts.*, BridgeReceipts.StartDate, BridgeReceipts.EndDate\n"
                    + "FROM BridgeReceipts\n"
                    + "WHERE (((BridgeReceipts.StartDate)<#" + new GetDay().asSQLDate(LocalDate.now()) + "#) AND ((BridgeReceipts.EndDate)>=#" + new GetDay().asSQLDate(LocalDate.now()) + "#) AND (( BridgeReceipts.Receipt) = '4')) OR ((( BridgeReceipts.Receipt) = '3'))\n"
                    + "ORDER BY BridgeReceipts.Sort;");

        } else {
            rs = st.executeQuery("SELECT BridgeReceipts.*, BridgeReceipts.StartDate, BridgeReceipts.EndDate\n"
                    + "FROM BridgeReceipts\n"
                    + "WHERE (((BridgeReceipts.StartDate)<#" + new GetDay().asSQLDate(LocalDate.now()) + "#) AND ((BridgeReceipts.EndDate)>=#" + new GetDay().asSQLDate(LocalDate.now()) + "#) AND (( BridgeReceipts.Receipt) != '4'))\n"
                    + "ORDER BY BridgeReceipts.Sort;");
        }
            while (rs.next()) {
                datasize++;
                newdata = new BridgeReceipts(rs.getString("Name"), rs.getString("Initials"), rs.getInt("Tickets"), rs.getDouble("Multiply"), rs.getString("Other"), rs.getDate("StartDate").toLocalDate(), rs.getDate("EndDate").toLocalDate(), rs.getInt("Repeat"), rs.getDouble("QTY"), rs.getInt("ID"), rs.getInt("Sort"), rs.getInt("Receipt"), rs.getString("CafeR"), rs.getString("BridgeR"));
                data.add(newdata);
                
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null);
            conn.close();
            if (st != null);
            st.close();
            if (rs != null);
            rs.close();
        }
        if (data.isEmpty()) {
            System.out.println("================================here is data.size " + data.size());
            newdata = new BridgeReceipts("No Receipts", "NOR", 0, 0.0, "", LocalDate.now(), LocalDate.now(), 0, 0.0, 0, 0, 0, "No Cafe Receipts", "No Bridge Receipts");
            data.add(newdata);
        }      
        System.out.println("================================here is data.size " + data.get(0).getTitle());
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
                //cancelButton.fire();''
                exitButtonPushed();
                break;
            case ENTER:
                break;
            default:
                break;
        }
    }

    private void resetFields() {
        //c2T.setText("");
        //c3T.setText("");
        // noteT.setText("");
        //hbox.setVisible(false);
        //updateButton.setVisible(false);
    }

    public void updateData() {
        //if (c2T.getText().isEmpty()) {
        //    JOptionPane.showMessageDialog(null, "The field must have something in it." );
        //    return;
        //}

        itemSelected = cTable.getSelectionModel().getSelectedItem();
        //MemberGreet greeting = new MemberGreet(itemSelected.getC1(), c2T.getText());
        //greeting.UpdateGreetingsTable();
        printReceipt();
        //setReceipts();
        //resetFields();
        //getTableItems();
        //exitButtonPushed();

    }
    
    
    
    
    
      private String getReceipts(String n) {
        String StringItem = null;
        for (int y = 0; y < rCeipts.size(); y++) {
            if (n.trim().equals(rCeipts.get(y).getrItem())) {
                setrNumber(rCeipts.get(y).getrNumber());
                StringItem = rCeipts.get(y).getrString();
            }
        }
        return StringItem;
    }
    
    
    private void setrNumber(String r) {
        this.rNumber = r;
    }

    private String getrNumber() {
        return this.rNumber;
    }
    
    
    
    
    private void printEscPosBRIDGERECEIPTS(boolean header, boolean footer, int whichReceipt)  throws PrintException, IOException{
        //printEscPos
        Date f = new Date();
        PrintService foundService = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob dpj = foundService.createPrintJob();
        
        System.out.println("This si the Default Printer " + foundService.getName());
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        outputStream.write(27); // ESC
        outputStream.write('@');
     
         
        
        //PRINT LOGO IF AVAILIBLE
        EscPos escpos = new EscPos(outputStream);

        RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();
        BufferedImage  githubBufferedImage = null;
        try {
            githubBufferedImage = ImageIO.read(new File(DBSP.pathNameImages + "/ReceiptLogo/bridgeLogo.png"));
            EscPosImage escposImage = new EscPosImage(githubBufferedImage, new BitonalThreshold());         
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, escposImage);
        } catch (IOException ex) {
            //jmail.sendEmailTo("NO RECEIPT LOGO FOUND","There is no logo found in the L://Images//ReceiptLogo/voucherLogo.png Directory", "errors");         
            System.out.println("There is no logo found in the L://Images//ReceiptLogo/withdrawalLogo.png Directory " + ex);
        }
        
        
        //===================================================HERE ARE THE HEADERS ==================================================

        
        if(header){
        //PRINT THE COMPANY NAME
        String printthis = getR.getReceipts(rCeipts, "CoName");
        //char c = getrNumber().charAt(8);
        //if (Character.compare(c, '1') == 0) {
            //JOptionPane.showConfirmDialog(null, "we are here and the thing is " + printthis);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            this.setrNumber("");
            printthis = "";
        //} else {
        //    this.setrNumber("");
        //    printthis = "";            
        //}



         
        //PRINT THE SUBCOHEADING
        printthis = getR.getReceipts(rCeipts, "SubCoHeading");
        //c = getrNumber().charAt(8);
        //if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            this.setrNumber("");
            printthis = "";
        //} else {
        //    this.setrNumber("");
        //    printthis = "";
        //}

        
        //PRINT THE ADDRESS
        printthis = getR.getReceipts(rCeipts, "Address");
        //c = getrNumber().charAt(8);
        //if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            this.setrNumber("");
            printthis = "";
        //} else {
        //    this.setrNumber("");
        //    printthis = "";
        //}


        
        //PRINT THE ADDRESS2
        printthis = getR.getReceipts(rCeipts, "Address2");
        //c = getrNumber().charAt(8);
        //if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            this.setrNumber("");
            printthis = "";
        //} else {
        //    this.setrNumber("");
        //    printthis = "";
        //}
         
        //PRINT THE PHONE
        printthis = getR.getReceipts(rCeipts, "Phone");
        //c = getrNumber().charAt(8);
        //if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            this.setrNumber("");
            printthis = "";
        //} else {
        //    this.setrNumber("");
        //    printthis = "";
        //}
        

        //PRINT THE WWW
        printthis = getR.getReceipts(rCeipts, "WWW");
        //c = getrNumber().charAt(8);
        //if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            this.setrNumber("");
            printthis = "";
        //} else {
        //    this.setrNumber("");
        //    printthis = "";
        //}
        
        
        } //END IF HEADER
        
        
        //=========================================== END OF HEADERS ==================================
           Format = new Style()
                   .setFontName(Style.FontName.Font_A_Default)
                   .setFontSize(Style.FontSize._1, Style.FontSize._1)
                   .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                   .setBold(true)
                   .setJustification(EscPosConst.Justification.Center);
        
        
        switch(whichReceipt){
            case 1: //@BRIDGE BRIGE;
            escpos.write(Format, itemSelected.getTitle());
            escpos.feed(1);
            escpos.write(Format, timeformatter.format(f));
            escpos.feed(1);
            escpos.feed(1);
            escpos.write(Format, "Employee: " + eFX.getNameF());
            escpos.feed(1);
            escpos.feed(1);
            escpos.write(Format, itemSelected.getCafe());
            escpos.feed(1);
            break;

            case 2: //@BRIDGE CAFE;
            escpos.write(Format, itemSelected.getTitle());
            escpos.feed(1);
            escpos.write(Format, timeformatter.format(f));
            escpos.feed(1);
            escpos.feed(1);
            escpos.write(Format, "Employee: " + eFX.getNameF());
            escpos.feed(1);
            escpos.feed(1);
            escpos.write(Format, itemSelected.getBridge());
            escpos.feed(1);
                
            break;
            case 3: //@BRIDGE & CAFE BOTH
            escpos.write(Format, itemSelected.getTitle());
            escpos.feed(1);
            escpos.write(Format, timeformatter.format(f));
            escpos.feed(1);
            escpos.feed(1);
            escpos.write(Format, "Employee: " + eFX.getNameF());
            escpos.feed(1);
            escpos.feed(1);
            escpos.write(Format, itemSelected.getCafe());
            escpos.feed(1);
            escpos.feed(1);
            escpos.feed(1);
            escpos.feed(1);
            escpos.feed(1);
            escpos.feed(1);
            escpos.write(Format, itemSelected.getTitle());
            escpos.feed(1);
            escpos.write(Format, timeformatter.format(f));
            escpos.feed(1);
            escpos.feed(1);
            escpos.write(Format, "Employee: " + eFX.getNameF());
            escpos.feed(1);
            escpos.feed(1);
            escpos.write(Format, itemSelected.getBridge());
            escpos.feed(1);
                
            break;
            case 4: //@CAFE CAFE
            escpos.write(Format, itemSelected.getTitle());
            escpos.feed(1);
            escpos.write(Format, timeformatter.format(f));
            escpos.feed(1);
            escpos.feed(1);
            escpos.write(Format, "Employee: " + eFX.getNameF());
            escpos.feed(1);
            escpos.feed(1);
            escpos.write(Format, itemSelected.getBridge());
            escpos.feed(1);
            break;
        }
        
        
        
        
        
        
        
        

        escpos.feed(2);        
        
        

        //  =============================================HERE ARE THE FOOTERS ===================================
        //PRINT THE FOOTER1
        if (footer) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        String printFOOTER1 = getReceipts("Footer1");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printFOOTER1);
            escpos.feed(1);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }

        
        //PRINT THE FOOTER2
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        String printFOOTER2 = getReceipts("Footer2");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printFOOTER2);
            escpos.feed(1);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        
        //PRINT THE FOOTER3
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        String printFOOTER3 = getReceipts("Footer3");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printFOOTER3);
            escpos.feed(1);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        
        //PRINT THE FOOTER4
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        String printFOOTER4 = getReceipts("Footer4");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printFOOTER4);
            escpos.feed(1);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }

        //PRINT THE FOOTER5
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        String printFOOTER5 = getReceipts("Footer5");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printFOOTER5);
            escpos.feed(1);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        } //END IF FOOTER
        
        // ================================================END OF FOOTERS =============================================
        
        
       
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
    
    
    public void setReceipts() {
        Date f = new Date();
        try {
        switch(itemSelected.getReceipt()){
            case 1:
                printEscPosBRIDGERECEIPTS(false,false,1);                
            break;
            case 17:
                printEscPosBRIDGERECEIPTS(true,false,1);                
                break;
            case 18:
                printEscPosBRIDGERECEIPTS(true,true,1);                
                break;
            case 2:
                printEscPosBRIDGERECEIPTS(false,false,2);                
            break;
            case 27:
                printEscPosBRIDGERECEIPTS(true,false,2);                
                break;
            case 28:
                printEscPosBRIDGERECEIPTS(true,true,2);                
                break;
            case 3:
                printEscPosBRIDGERECEIPTS(false,false,3);                
            break;
            case 37:
                printEscPosBRIDGERECEIPTS(true,false,3);                
                break;
            case 38:
                printEscPosBRIDGERECEIPTS(true,true,3);                
                break;
            case 4:
                printEscPosBRIDGERECEIPTS(false,false,4);                
            break;
            case 47:
                printEscPosBRIDGERECEIPTS(true,false,4);                
                break;
            case 48:
                printEscPosBRIDGERECEIPTS(true,true,4);                
                break;
        }
        } catch (PrintException | IOException ex) {
            System.out.println(ex);
        }
        

    }

    
       
       
       
    

    public void printReceipt() {
        Date f = new Date();
        System.out.println("Title: " + itemSelected.getTitle() + " Receipt" + itemSelected.getReceipt() + " /bridge " + itemSelected.getBridge() + " /cafe " + itemSelected.getCafe());
        try {
            pw = new PrintWriter(new File(DBSP.pathNameLocal + "BridgeReceipt.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        if (itemSelected.getReceipt() == 0) {
            pw.println(itemSelected.getTitle());
            pw.println(timeformatter.format(f));
            pw.println("");
            pw.println("Employee: " + eFX.getNameF());
            pw.println("");
            pw.println("");
            pw.println(itemSelected.getCafe());
        }
        pw.println("");
        if (itemSelected.getReceipt() == 1) {
            pw.println(itemSelected.getTitle());
            pw.println(timeformatter.format(f));
            pw.println("");
            pw.println("Employee: " + eFX.getNameF());
            pw.println("");
            pw.println("");
            pw.println(itemSelected.getBridge());
        }
        pw.println(" ");
        if (itemSelected.getReceipt() == 2) {
            pw.println(itemSelected.getTitle());
            pw.println(timeformatter.format(f));
            pw.println(" ");
            pw.println("Employee: " + eFX.getNameF());
            pw.println("");
            pw.println(itemSelected.getBridge());
            pw.println("");
            pw.println("");
            pw.println("");
            pw.println("");
            pw.println("");
            pw.println("");
            pw.println(itemSelected.getTitle());
            pw.println(timeformatter.format(f));
            pw.println("");
            pw.println("Employee: " + eFX.getNameF());
            pw.println("");
            pw.println(itemSelected.getCafe());
            pw.println("");
            pw.println("");
            pw.println("");
        }
        if (itemSelected.getReceipt() == 4) {
            pw.println(itemSelected.getTitle());
            pw.println(timeformatter.format(f));
            pw.println("");
            pw.println("Employee: " + eFX.getNameF());
            pw.println("");
            pw.println("");
            pw.println(itemSelected.getBridge());
        }
        pw.println("");
        pw.println("");
       /* pw.println("");
        pw.println("");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");*/
        pw.close();

        print pr = new print();
        //pr.printReceipt("WithdrawalReceipt.txt");
        Platform.runLater(()->pr.printReceipt("BridgeReceipt.txt"));
        //System.out.println(pw.toString());
        //cancelButton.fire();
        //setReceipts();
        exitButtonPushed();

    }

    public void exitButtonPushed() {
        System.out.println("we are in the exit zone");
        Stage stageV = (Stage) cTable.getScene().getWindow();
        stageV.close();
    }

}
