
package views.vouchers;

import ComboBoxAutoComplete.ComboBoxAutoCompleteVoucher;
import JavaMail.Mail_JavaFX1;
import XML_Code.AddXmlNode;
import XML_Code.ModifyXMLFileInJava;
import XML_Code.XMLDOMParseToObservableList;
import XML_Code.readXMLToArrayList;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.BarCode;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;
import commoncodes.GetReceipts;
import models.voucher.FXVoucherViewer;
import commoncodes.IsItANumber;
import commoncodes.IsItANumber1;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
import messageBox.messageBox;
import models.club.CheckBalanceDB;
import models.club.DB;
import models.club.Member;
import models.club.Memtick;
import models.club.rCeipts;
import models.settings.Announcements;
import models.settings.DailyBonus;
import models.toys.Toys;
import models.voucher.EaterCodes;
import models.voucher.VoucherFunctions;
import models.voucher.redeemVouchers;
import models.voucher.returnEaterCode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pReceipts.JavaPrinterOptions;
import pWordFX.empFX;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;
import views.MemDepositFXController;


    

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class FXVoucherDialogController implements Initializable {
    @FXML private Button printButton;
    @FXML private Button cancelButton;
    @FXML private Button depositButton;
    @FXML private TextField vAmountField;
    @FXML private TextField addingField;
    @FXML private Label addingLabel;
    @FXML private PasswordField empNumberField;
    @FXML private ComboBox cBoxReason;
    @FXML private Label cBoxLabel;
    @FXML private AnchorPane root;
    
    
    
    private static final IsItANumber IIN = new IsItANumber();
    private static final GetDay GD = new GetDay();
    private static final java.util.Date DT = new java.util.Date();
    private static final GetReceipts getR = new GetReceipts();
    private static final JavaPrinterOptions JPO = new JavaPrinterOptions();
    private static final java.text.SimpleDateFormat SDF = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm a");
    public static dbStringPath dbsp;
    public static String css;
    public static employeeFX eFXX;
    public static Double rCeiptvMount;
    public static int vAmt, eRedeemCount, enArcade, Super, superID;
    public static Mail_JavaFX1 jmail;
    public static messageBox mBox;
    public static ArrayList<rCeipts> receipts;
    public static ArrayList<EaterCodes> TEC;
    public static FXMLLoader FXLOADER;
    public static SceneChanger_Main sc;
    public static DB db;
    public static Member member;
    public static Memtick memtick;
    public static CheckBalanceDB chkBalance;
    
    
    private static Stage stageV;
    private static String superEN, superLN, superFN;
    private static String en, fn, ln, eID, currentTime;
    private static ObservableList<Announcements> data;
    private static ArrayList<FXVoucherViewer> vList = new ArrayList<>();
    private static ArrayList<FXVoucherViewer> listToRedeem  = new ArrayList<>();
    public static ArrayList<Toys> l;
    public static ArrayList<empFX> EE;
    public static FXVoucherViewer voucher = null;
    public static ArrayList<DailyBonus> Daily;
    public static PrintWriter pw;
    private static String nextVoucherID = "";
    private static int nextNumber = 0;
    
    private static final String RECEIPTS_DATE = "" + String.valueOf(LocalDate.now().getMonthValue()) + "" + new IsItANumber1().isLessThenTen(String.valueOf(LocalDate.now().getDayOfMonth())) + "";
    private static final DecimalFormat df = new DecimalFormat("#");
    private static final char THISPAGECHAR = 5;
    private static Style Format = new Style();




    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        FillcBoxReasonComboBox();
        root.getStylesheets().add(css);
        vAmountField.setStyle("-fx-background-color: #fdfdaf;");
        setKeyCodes();
        //addTextfieldListeners();
        cBoxLabel.setText("Please Enter A Reason");
        cBoxLabel.setVisible(false);
        cBoxReason.setVisible(false);
        Platform.runLater(() -> getStageV());
        Platform.runLater(() -> setEmp());
        Platform.runLater(() -> nextVoucherID = getNewVoucherNumber_XML());
        Platform.runLater(() -> {addingField.setVisible(false); vList= new readXMLToArrayList().getVoucherDataXML(); addingLabel.setVisible(false);});
        Platform.runLater(() -> vAmountField.requestFocus());

    }

    public void printButtonPushed(ActionEvent event) {
        String reasonValue = "";
        boolean GO = false;
        int vAmount;
        if (addingField.isVisible()){
            vAmount = Integer.parseInt(addingField.getText());
        } else {
            try{
                vAmount = Integer.parseInt(vAmountField.getText());
            }catch(NumberFormatException e) {
                vAmountField.setText("");
                vAmountField.requestFocus();
                return;
            }
        }

        if (vAmount > rCeiptvMount) { //HERE IS WHERE WE NEED TO GET THE AMOUNT FROM THE RECEIPTS
            if (!cBoxReason.isVisible()) {
                cBoxReason.setVisible(true);
                cBoxLabel.setVisible(true);
                cBoxReason.requestFocus();
                cBoxReason.show();
                return;
            } else {
                try {
                    if (!cBoxReason.getValue().equals("")) {
                        reasonValue = String.valueOf(cBoxReason.getValue());
                    }
                } catch (Exception e) {
                    cBoxReason.requestFocus();
                    cBoxReason.show();
                    cBoxLabel.setText("STOP! You must SELECT or TYPE in a Reason");
                    cBoxLabel.setTextFill(Color.web("#FFFF00"));
                    return;
                }
            }
        }
        if (vAmount > vAmt) {
            if (enArcade == 0 || enArcade == 2) {
                try {
                    sc.getpassWord(stageV, "/pWordFX/withdrawalPassword.fxml", "Number", "Enter Employee Number:", 200.00 + 350.0, 200.00);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
                if (!isSuperValidInArrayList(sc.getGameNumber())) {
                    GO = false;
                    mBox.showAlert(Alert.AlertType.ERROR, stageV, ln, "Not a Valid Employee Number");
                    return;
                } else {
                    GO = true;
                    System.out.println("Super integer = " + Super);
                    if (Super != 1 && Super != 3) {
                        mBox.showAlert(Alert.AlertType.ERROR, stageV, ln, "not a valid Supervisor number for this task");
                        return;
                    }
                }
            }

            //=mBox.showNewMemberAlert(Alert.AlertType.ERROR, stageV, "Too Many", "You are entering a Voucher Amount greater then you have permission to enter. \n\n Please see a supervisor");
            //=vAmountField.clear();
            //=vAmountField.requestFocus();
            //=return;
        }
        //String numbers = text.substring(Math.max(0, text.length() - 7));
        String machine = dbsp.localMachine.toString();
        machine = machine.split("/")[0].trim();
        //try {
            if (reasonValue.isEmpty()) {
                reasonValue = machine;
            } else {
                reasonValue += " " + machine;
            }
            if (GO) {
                reasonValue += " " + superFN;
            }
        //} catch (IllegalArgumentException e) {
        //    jmail.sendEmailTo("Error Incomplete Voucher 2", "error while in Voucher Screen (See FXVoucherDialogController) " + "\n" + e + dbsp.localMachine, "errors");
        //    System.out.println("something wrong with addxml().addxmlVouchers " + e);
        //    return;
       // }

            FXVoucherViewer vv = new FXVoucherViewer(eID, currentTime, GD.getday(), fn + " " + ln, vAmount, reasonValue);
            //String CoName = getR.getReceipts(receipts, "CoName");
            String tClockID = getVoucherID(vAmount);
            ///String tClockID = getNewVoucherNumber_XML();
            System.out.println("TclockID  after getVoucherID= : " + tClockID);
            ///int nextNumber = Integer.parseInt(tClockID) + 3;
            ///if (nextNumber > 98) {
            ///    nextNumber = 11;
            ///}
            ///System.out.println("TclockID = : " + nextNumber);
            ///new ModifyXMLFileInJava().ModifyXMLVoucherNewNumber(dbsp.pathNameXML + "newVNumber.xml", String.valueOf(nextNumber));            
            ///int d = LocalDate.now().getMonthValue();
            ///LocalDate year = LocalDate.parse(LocalDate.now().toString());        
            ///String mLetter = new GetDay().getVoucherMonth(d);
            ///tClockID = mLetter + LocalDate.now().getMonthValue() + LocalDate.now().getDayOfMonth() + getNewVamount(vAmount) + nextNumber;
            //-tClockID = mLetter + LocalDate.now().getDayOfMonth() + year.getYear() + nextNumber;
            Platform.runLater(() -> putNewVoucherInXML(dbsp.pathNameXML + "Voucher.xml", vv, tClockID));

                
            try {
                //new AddXmlNode().addXMLVouchers(dbsp.pathNameXML + "Voucher.xml", vv, tClockID);
                //print(vv, tClockID);
                printEscPosVOUCHER_NEW(vv, tClockID);
                //printEscPosVOUCHER(vv, tClockID);
            } catch (Exception ex) {
                jmail.sendEmailTo("Error Incomplete Voucher", "error while in Voucher Screen (See FXVoucherDialogController)" + "\n" + ex  + dbsp.localMachine + "\n " + vv.getC1() + " " + vv.getC2() + " " + vv.getC3() + " " + vv.getC4() + " " + vv.getC6() + " " + vv.getC7() + " " + tClockID, "errors");
                System.out.println("something wrong with addxml().addxmlVouchers " + ex);
            }

            
            System.out.println("got past printing");
                //jmail.sendEmailTo("new Voucher", tClockID + " " + vv.getC1() + " \n" + vv.getC2() + " \n" + vv.getC3() + " \n" + vv.getC4() + " \n" + vv.getC5() +  "\n" + vv.getC6() + "\n" + dbsp.localMachine, "errors");
                System.out.println("got past send email");
            if (addingField.isVisible()) {
                System.out.println("got past addingfield visible");
                new redeemVouchers().redeemVouchersNoThread(listToRedeem, eID, currentTime, fn, vList, dbsp, "combining Tickets ");
                System.out.println("got past redeemVouchers");
            }

            ///THIS IS WHERE THE OLD POST TO ACCESS CODE IS
            ///if (!vv.postVoucher(fn, en)) {
            ///    System.out.println("Error, ncomplete Voucher");
            ///} else {
            ///}
            cancelButton.fire();
    }
    
    
     private boolean isSuperValidInArrayList(String n) {
        boolean superValid = false;
        for (int y = 0; y < EE.size(); y++) {
            if (n.equals(EE.get(y).getEmpNumber())) {
                superValid = true;
                superEN = EE.get(y).getEmpNumber();
                superFN = EE.get(y).getNameF();
                superLN = EE.get(y).getNameL();
                Super = EE.get(y).getArcade();
                superID = EE.get(y).getEmpID();
            }
        }
        //System.out.println(superEN + " " + superFN + " " + superLN + " " + Super + " " + superID);
        return superValid;
    }
    
    
    
    private String getVoucherID(int vAmount) {
        String voucherID = "";
        String newMonthValue = "", newDayOfMonth = "";
        //nextVoucherID = getNewVoucherNumber_XML();
        System.out.println("TclockID = : " + nextVoucherID);
        nextNumber = Integer.parseInt(nextVoucherID) + 3;
        if (nextNumber > 98) {
            nextNumber = 11;
        }
        
        int d = LocalDate.now().getMonthValue();
        LocalDate year = LocalDate.parse(LocalDate.now().toString());
        String mLetter = new GetDay().getVoucherMonth(d);
        int monthValue = LocalDate.now().getMonthValue();
        if (monthValue < 10) {
            newMonthValue = "0" + String.valueOf(monthValue);
        } else {
            newMonthValue = String.valueOf(monthValue);
        }
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        if (dayOfMonth < 10) {
            newDayOfMonth = "0" + String.valueOf(dayOfMonth);
        } else {
            newDayOfMonth = String.valueOf(dayOfMonth);
        }
        voucherID = mLetter + newMonthValue + newDayOfMonth + getNewVamount(vAmount) + nextNumber;
        //voucherID = mLetter + LocalDate.now().getMonthValue() + LocalDate.now().getDayOfMonth() + getNewVamount(vAmount) + nextNumber;
        System.out.println("THIS IS VOUCHER ID IN GETVOUCHER() " + voucherID + "PLUS NEXTNUMBER " + nextNumber);
        
        putNextVoucherNumberThread(nextNumber);
        return voucherID;
    }
    
    
      public static void putNextVoucherNumberThread(int nextNumber) {
          System.out.println("HERE IS THE NEXT NUMBER in putnextvouchernumberhtread() " + nextNumber + "nextNumber String Value " + String.valueOf(nextNumber));
        Thread putNextVoucherNumber = new Thread() {
            @Override
            public void run() {
              new ModifyXMLFileInJava().ModifyXMLVoucherNewNumber(dbsp.pathNameXML + "newVNumber.xml", String.valueOf(nextNumber));
            }
        };
        putNextVoucherNumber.start();
    }
     
     
    public static void putNewVoucherInXML(String Path, FXVoucherViewer vv, String tClockID) {
        Thread putNewVoucherInXML = new Thread() {
            @Override
            public void run() {
                try {
                    new AddXmlNode().addXMLVouchers(dbsp.pathNameXML + "Voucher.xml", vv, tClockID);
                } catch (Exception ex) {
                    jmail.sendEmailTo("Error Incomplete Voucher", "error while in Voucher Screen (See FXVoucherDialogController)" + "\n" + ex  + dbsp.localMachine + "\n " + vv.getC1() + " " + vv.getC2() + " " + vv.getC3() + " " + vv.getC4() + " " + vv.getC6() + " " + vv.getC7() + " " + tClockID, "errors");
                    System.out.println("something wrong with addxml().addxmlVouchers " + ex);
                }
            }
        };
        putNewVoucherInXML.start();
    }
     
    
    private static String getNewVamount(int vAmount) {
        String newVamount = "";
         if (vAmount<10) {
                newVamount = "0000" + vAmount;
                System.out.println("LESS < 10, vAMount = " + vAmount + " " + "newVamount = " + newVamount);
                return newVamount;
            }
            if (vAmount<100) {
                newVamount = "000" + vAmount;
                System.out.println("LESS < 100, vAMount = " + vAmount + " " + "newVamount = " + newVamount);
                return newVamount;
            }
            if (vAmount<1000) {
                newVamount = "00" + vAmount;
                System.out.println("LESS < 1000, vAMount = " + vAmount + " " + "newVamount = " + newVamount);
                return newVamount;
            }
            if (vAmount<10000) {
                newVamount = "0" + vAmount;
                System.out.println("LESS < 10000, vAMount = " + vAmount + " " + "newVamount = " + newVamount);
                return newVamount;
            }
            if (vAmount<100000) {
                newVamount = "" + vAmount;
                System.out.println("LESS < 100000, vAMount = " + vAmount + " " + "newVamount = " + newVamount);
                return newVamount;
            }
            

            return newVamount;    

    }
    
    
    
    /*public void redeemVoucherXML(String vID, String empID) {
        //vList = new readXMLToArrayList().getVoucherDataXML();
        for (int i = 0; i < vList.size(); i++) {
            if (vID.equals(vList.get(i).getC7())) {
                System.out.println("FOUND THE VOUCHERS " + vList.get(i).getC7());
                if (vList.get(i).getC3().equals("Sun") || vList.get(i).getC3().equals("Mon") || vList.get(i).getC3().equals("Tue") || vList.get(i).getC3().equals("Wed") || vList.get(i).getC3().equals("Thu") || vList.get(i).getC3().equals("Fri") || vList.get(i).getC3().equals("Sat")) {
                    System.out.println("HEADING INTO MODIFY");
                    new ModifyXMLFileInJava().ModifyXMLVoucherRedeem(dbsp.pathNameXML + "Voucher.xml", vList.get(i).getC7(), empID, "combining Tickets ");
                } else {
                    //MBOX.showAlert(Alert.AlertType.ERROR, null, "HEY", "Error, \nThank You! \n This voucher redeemed already");
                    System.out.println("voucher iwth day of week not found " + vList.get(i).getC3());
                }
            
            } else {
                //MBOX.showAlert(Alert.AlertType.ERROR, null, "HEY", "Error, \nThank You! \n No Voucher Found");
                System.out.println("no voucher found " + vID + " " + vList.get(i).getC7());
            }
        }

    } 
    */
    
    
    


     private void printEscPosVOUCHER(FXVoucherViewer vv, String tClockID)  throws PrintException, IOException{
        //printEscPos
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
            githubBufferedImage = ImageIO.read(new File(dbsp.pathNameImages + "/ReceiptLogo/voucherLogo.png"));
            EscPosImage escposImage = new EscPosImage(githubBufferedImage, new BitonalThreshold());         
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, escposImage);
        } catch (IOException ex) {
            //jmail.sendEmailTo("NO RECEIPT LOGO FOUND","There is no logo found in the L://Images//ReceiptLogo/voucherLogo.png Directory", "errors");         
            System.out.println("There is no logo found in the L://Images//ReceiptLogo/voucherLogo.png Directory " + ex);
        }
        
        

        //PRINT THE COMPANY NAME
        Style Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, getR.getReceipts(receipts, "CoName"));
        escpos.feed(1);
         
        //PRINT THE SUBCOHEADING
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, getR.getReceipts(receipts, "SubCoHeading"));
        escpos.feed(1);
         
        
        //PRINT TICKET VOUCHER
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "TICKET VOUCHER");
        escpos.feed(2);

        //PRINT FIRST NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, fn);
        escpos.feed(1);        

        
        
        //PRINT LINE SEPERATOR
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "----------------------------------------");
        escpos.feed(1);     
        
        //PRINT THE DATE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, vv.getC2());
        escpos.feed(1);

        //PRINT LINE SEPERATOR        
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "----------------------------------------");
        escpos.feed(2);     


        //PRINT TITLE TICKET AMOUNT
        Format = new Style()
                .setFontSize(Style.FontSize._2, Style.FontSize._2)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "Ticket Amount:");       
        escpos.feed(2);

        //PRINT THE AMOUNT OF THE VOUCHER
        Format = new Style()
                .setFontSize(Style.FontSize._8, Style.FontSize._8)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, String.valueOf(vv.getC5() + " " ));
        escpos.feed(2);

        //PRINT THE BAR CODE
        BarCode barcode = new BarCode();
        barcode.setHRIPosition(BarCode.BarCodeHRIPosition.BelowBarCode);
        barcode.setJustification(EscPosConst.Justification.Center);
        escpos.write(barcode, tClockID);
        
       
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









        private void printEscPosVOUCHER_NEW(FXVoucherViewer vv, String tClockID)  throws PrintException, IOException{
        //printEscPos
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
            githubBufferedImage = ImageIO.read(new File(dbsp.pathNameImages + "/ReceiptLogo/voucherLogo.png"));
            EscPosImage escposImage = new EscPosImage(githubBufferedImage, new BitonalThreshold());         
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, escposImage);
        } catch (IOException ex) {
            //jmail.sendEmailTo("NO RECEIPT LOGO FOUND","There is no logo found in the L://Images//ReceiptLogo/voucherLogo.png Directory", "errors");         
            System.out.println("There is no logo found in the L://Images//ReceiptLogo/voucherLogo.png Directory " + ex);
        }
        
        
        //===================================================HERE ARE THE HEADERS ==================================================

        //PRINT THE COMPANY NAME
        String printthis = getR.getReceipts(receipts, "CoName");
        String getReceiptNumber = getR.getReceiptsNumber(receipts, "CoName");
    //JOptionPane.showMessageDialog(null, "we are here and the thing is " + printthis + " ----" + getReceiptNumber);
        char c = getReceiptNumber.charAt(THISPAGECHAR);
        if (Character.compare(c, '1') == 0) {
    //JOptionPane.showMessageDialog(null, "we are here and the thing is " + printthis + " ----" + getReceiptNumber + "==== " + c);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }

        System.out.println("=============================================================================");

         
        //PRINT THE SUBCOHEADING
        printthis = getR.getReceipts(receipts, "SubCoHeading");
        getReceiptNumber = getR.getReceiptsNumber(receipts, "SubCoHeading");
        c = getReceiptNumber.charAt(THISPAGECHAR);
    //JOptionPane.showMessageDialog(null, "we are here and the thing is " + printthis + " ----" + getReceiptNumber + "==== " + c);
        if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }

        
        //PRINT THE ADDRESS
        printthis = getR.getReceipts(receipts, "Address");
        getReceiptNumber = getR.getReceiptsNumber(receipts, "Address");
        c = getReceiptNumber.charAt(THISPAGECHAR);
        if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }


        
        //PRINT THE ADDRESS2
        printthis = getR.getReceipts(receipts, "Address2");
        getReceiptNumber = getR.getReceiptsNumber(receipts, "Address2");
        c = getReceiptNumber.charAt(THISPAGECHAR);
        if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }
         
        //PRINT THE PHONE
        printthis = getR.getReceipts(receipts, "Phone");
        getReceiptNumber = getR.getReceiptsNumber(receipts, "Phone");
        c = getReceiptNumber.charAt(THISPAGECHAR);
        if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }
        

        //PRINT THE WWW
        printthis = getR.getReceipts(receipts, "WWW");
        getReceiptNumber = getR.getReceiptsNumber(receipts, "WWW");
        c = getReceiptNumber.charAt(THISPAGECHAR);
        if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }
        
        
        
        
        
        //=========================================== END OF HEADERS ==================================
        
        
        
        
        //PRINT TICKET VOUCHER
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "TICKET VOUCHER");
        escpos.feed(2);

        //PRINT FIRST NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "Employee: " + fn);
        escpos.feed(1);        

        
        
        //PRINT LINE SEPERATOR
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "----------------------------------------");
        escpos.feed(1);     
        
        //PRINT THE DATE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, vv.getC2());
        escpos.feed(1);

        //PRINT LINE SEPERATOR        
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "----------------------------------------");
        escpos.feed(2);     


        //PRINT TITLE TICKET AMOUNT
        Format = new Style()
                .setFontSize(Style.FontSize._2, Style.FontSize._2)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "Ticket Amount:");       
        escpos.feed(2);

        //PRINT THE AMOUNT OF THE VOUCHER
        Format = new Style()
                .setFontSize(Style.FontSize._8, Style.FontSize._8)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, String.valueOf(vv.getC5() + " " ));
        escpos.feed(2);

        //PRINT THE BAR CODE
        BarCode barcode = new BarCode();
        barcode.setHRIPosition(BarCode.BarCodeHRIPosition.BelowBarCode);
        barcode.setJustification(EscPosConst.Justification.Center);
        escpos.write(barcode, tClockID);
        
       
        escpos.feed(2);        
        
        

        //  =============================================HERE ARE THE FOOTERS ===================================
        //PRINT THE FOOTER1
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        printthis = getR.getReceipts(receipts, "Footer1");
        getReceiptNumber = getR.getReceiptsNumber(receipts, "Footer1");
        if (getReceiptNumber.equals("1.0") || Double.parseDouble(getReceiptNumber) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printthis);
            escpos.feed(1);
            printthis = "";
            getReceiptNumber = "";
        } else {
            printthis = "";
            getReceiptNumber = "";
        }

        
        //PRINT THE FOOTER2
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        printthis = getR.getReceipts(receipts, "Footer2");
        getReceiptNumber = getR.getReceiptsNumber(receipts, "Footer2");
        if (getReceiptNumber.equals("1.0") || Double.parseDouble(getReceiptNumber) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printthis);
            escpos.feed(1);
            printthis = "";
            getReceiptNumber = "";
        } else {
            printthis = "";
            getReceiptNumber = "";
        }
        
        //PRINT THE FOOTER3
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        printthis = getR.getReceipts(receipts, "Footer3");
        getReceiptNumber = getR.getReceiptsNumber(receipts, "Footer3");
        if (getReceiptNumber.equals("1.0") || Double.parseDouble(getReceiptNumber) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printthis);
            escpos.feed(1);
            printthis = "";
            getReceiptNumber = "";
        } else {
            printthis = "";
            getReceiptNumber = "";
        }
        
        //PRINT THE FOOTER4
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        printthis = getR.getReceipts(receipts, "Footer4");
        getReceiptNumber = getR.getReceiptsNumber(receipts, "Footer4");
        if (getReceiptNumber.equals("1.0") || Double.parseDouble(getReceiptNumber) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printthis);
            escpos.feed(1);
            printthis = "";
            getReceiptNumber = "";
        } else {
            printthis = "";
            getReceiptNumber = "";
        }

        //PRINT THE FOOTER5
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        printthis = getR.getReceipts(receipts, "Footer5");
        getReceiptNumber = getR.getReceiptsNumber(receipts, "Footer5");
        if (getReceiptNumber.equals("1.0") || Double.parseDouble(getReceiptNumber) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printthis);
            escpos.feed(1);
            printthis = "";
            getReceiptNumber = "";
        } else {
            printthis = "";
            getReceiptNumber = "";
        }
        
        
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
    


    
    
    
    
    
    
    
    
    
  //XML PROCESSES - - - - - - - - - - - - - - - 
    
    public String getNewVoucherNumber_XML() {
        String newNumber = null;
        try {
            Document document = new XMLDOMParseToObservableList().parseXmlFile(dbsp.pathNameXML + "newVNumber.xml");
            //System.out.println("1");
            newNumber = extractDataFromXmlTOObservableList(document);

            //Collections.reverse(gameProblems);
        } catch (Exception e) {
            System.out.println("-1- " + e + " " + dbsp.pathNameXML);
            new Mail_JavaFX1().sendEmailTo("Game Problem Error", "-1- " + e + " " + dbsp.pathNameXML, "errors");
        }
        System.out.println("HERE IS THE NEW NUMBER IN GETNEWVOUCHERNUMBER_XML " + newNumber);
        return newNumber;
    }
    
    
    public String extractDataFromXmlTOObservableList(Document document) {
        String newNumber = null;

        NodeList nodeList = document.getElementsByTagName("Voucher");
        //System.out.println("nodeList = " + nodeList.toString());
        for (int i = 0; i < nodeList.getLength(); i++) {
            System.out.println("nodelist.length = " + nodeList.getLength());
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                // Extract the data from the element
                newNumber = element.getElementsByTagName("newNumber").item(0).getTextContent();

                //data.add(new Announcements(newNumber, "", "", ""));
            }
        }
        System.out.println("THIS IS NEWNUMBER WHILE LEAVING EXTRACTDATAFROMXMLTOOABSERVABLELIST: " + newNumber);
        return newNumber;
    }  
    
    
     public static void runRedeemVoucherThread(String vNumber) {
        Thread dVouchers = new Thread() {
            @Override
            public void run() {

        if (vList.isEmpty()){
            System.out.println("vlist is empty");
            vList= new readXMLToArrayList().getVoucherDataXML();
        }
        for (int i = 0; i < vList.size(); i++){
            if (vNumber.equals(vList.get(i).getC7())){
                
                if (vList.get(i).getC3().equals("Sun") || vList.get(i).getC3().equals("Mon") || vList.get(i).getC3().equals("Tue") || vList.get(i).getC3().equals("Wed") || vList.get(i).getC3().equals("Thu") || vList.get(i).getC3().equals("Fri") || vList.get(i).getC3().equals("Sat")) {
                    new Mail_JavaFX1().sendEmailToThisEmail("Redeeming Voucher", vList.get(i).getC1() + " " + vList.get(i).getC2() + " " + vList.get(i).getC3() + " " + vList.get(i).getC4() + " " + vList.get(i).getC5() + " " + vList.get(i).getC6() + " " + vList.get(i).getC7(), "newkirkdean@gmail.com");
                    voucher = new FXVoucherViewer(vList.get(i).getC1(),vList.get(i).getC2(), vList.get(i).getC3(),vList.get(i).getC4(),vList.get(i).getC5(),vList.get(i).getC6(),vList.get(i).getC7());
                    listToRedeem.add(eRedeemCount, voucher);
                    eRedeemCount++;
                } else {
                    //mBox.showAlert(Alert.AlertType.ERROR, null, "Stop", "Voucher has already been redeemed");
                }
            }
        }
                
                
            }
        };
        dVouchers.start();
    }
    
    
    //OLD ONE USING THREADRUN
    /*public boolean getVoucherXML(String vNumber) {
        boolean getVoucher = false;
        if (vList.isEmpty()){
            System.out.println("vlist is empty");
            vList= new readXMLToArrayList().getVoucherDataXML();
        }
        for (int i = 0; i < vList.size(); i++){
            if (vNumber.equals(vList.get(i).getC7())){
                
                if (vList.get(i).getC3().equals("Sun") || vList.get(i).getC3().equals("Mon") || vList.get(i).getC3().equals("Tue") || vList.get(i).getC3().equals("Wed") || vList.get(i).getC3().equals("Thu") || vList.get(i).getC3().equals("Fri") || vList.get(i).getC3().equals("Sat")) {
                    new Mail_JavaFX1().sendEmailToThisEmail("Test", vList.get(i).getC1() + " " + vList.get(i).getC2() + " " + vList.get(i).getC3() + " " + vList.get(i).getC4() + " " + vList.get(i).getC5() + " " + vList.get(i).getC6() + " " + vList.get(i).getC7(), "newkirkdean@gmail.com");
                    voucher = new FXVoucherViewer(vList.get(i).getC1(),vList.get(i).getC2(), vList.get(i).getC3(),vList.get(i).getC4(),vList.get(i).getC5(),vList.get(i).getC6(),vList.get(i).getC7());
                    listToRedeem.add(eRedeemCount, voucher);
                    eRedeemCount++;
                    getVoucher = true;
                } else {
                    mBox.showAlert(Alert.AlertType.ERROR, null, "Stop", "Voucher has already been redeemed");
                }
            }
        }
        System.out.println("GET VOUCHER = " + getVoucher);
        return getVoucher;
    }*/
     
    
    
    

    private void getStageV() {
        stageV = (Stage) vAmountField.getScene().getWindow();
        currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a"));
        vList.sort(Comparator.comparing(FXVoucherViewer::getC7));

    }

    public void addTextfieldListeners() {
        vAmountField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        IIN.checkNumbers(newValue);
                    } catch (Exception e) {
                        //JOptionPane.showMessageDialog(null, "Voucher Amount can only be Numbers");
                        mBox.showAlert(Alert.AlertType.ERROR, stageV, "STOP", "Voucher Amount can only be Numbers");
                        vAmountField.clear();
                        vAmountField.requestFocus();
                        return;
                    }
                }
        );
    }

    private void setEmp() {
        empNumberField.setText(eFXX.empNumber);
        empNumberField.setStyle("-fx-background-color: #d3d3d0;");
        empNumberField.setDisable(true);
        addingField.setFocusTraversable(false);
        addingField.setDisable(true);
        en = eFXX.empNumber;
        fn = eFXX.nameF;
        ln = eFXX.nameL;
        vAmt = eFXX.VAmt;
        eID = String.valueOf(eFXX.getBdayresos());
        for (int i = 0; i<TEC.size(); i++) {
            System.out.println(TEC.get(i).getEaterCode() + " " + TEC.get(i).getDropIt());
        }
    }

   
    
    
    
    
    
    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F6) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.ESCAPE) {
                keyListener(ke);
                ke.consume();
            }
        });
    }

    
    /*
    private returnEaterCode isTicketEaterCodeValid(String vAmountFeild) {
        returnEaterCode rec = null;
        for (int i = 0; i < TEC.size(); i++) {
            if (vAmountFeild.substring(0, 3).matches(TEC.get(i).geteaterCode())) {
                rec = new returnEaterCode(true, Integer.parseInt(vAmountFeild.substring(TEC.get(i).getStart(), TEC.get(i).getStop())));
            }
        }
        return rec;
    }
    
    */
    
    
    
    
    
    
    private void EnterKeyPressed() {
        int e, d, g;
            if (vAmountField.isFocused()) {
                if (vAmountField.getText().length() > 9) {  
                    VoucherFunctions VF = new VoucherFunctions();                    //THIS IS TICKET EATER
                    returnEaterCode rec = VF.isTicketEaterCodeValid(TEC, vAmountField.getText());
                    System.out.println("This is valid (" + rec.getValid() + ") this is eatercodeValue (" + rec.getEaterCodeValue() + ")");

                    //for (int i = 0; i < TEC.size(); i++) {
                        //if (vAmountField.getText().substring(0, 3).matches(TEC.get(i).geteaterCode())) {
                        if (rec.getValid()) {
                            addingField.setVisible(true);
                            addingLabel.setVisible(true);
                            depositButton.setVisible(true);

                            if (!addingField.getText().trim().isEmpty()) {
                                //e = Integer.parseInt(addingField.getText().trim());
                                //d = rec.getEaterCodeValue();
                                //d = Integer.parseInt(vAmountField.getText().substring(TEC.get(i).getStart(), TEC.get(i).getStop()));
                                //g = e + d;
                                g = Integer.parseInt(addingField.getText().trim()) + rec.getEaterCodeValue(); 
                                cBoxReason.setVisible(true);
                                cBoxReason.setValue("combining Tickets");
                                depositButton.setVisible(true);
                            } else {
                                //e = 0;
                                //d = rec.getEaterCodeValue();
                                //d = Integer.parseInt(vAmountField.getText().substring(TEC.get(i).getStart(), TEC.get(i).getStop()));
                                g = rec.getEaterCodeValue();
                            }

                            addingField.setText(String.valueOf(g));
                            String dt = vAmountField.getText().trim();
                            runRedeemVoucherThread(dt);
                            vAmountField.setText("");
                            vAmountField.requestFocus();
                            return;
                        }
                        //} 
                    //}
                }
                if (addingField.isVisible() && !vAmountField.getText().isEmpty()) {
                    //e = Integer.parseInt(addingField.getText().trim());
                    //d = Integer.parseInt(vAmountField.getText().trim());
                    g = Integer.parseInt(addingField.getText().trim()) + Integer.parseInt(vAmountField.getText().trim());
                    addingField.setText(String.valueOf(g));
                    vAmountField.setText("");
                    vAmountField.requestFocus();
                    cBoxReason.setVisible(true);
                    cBoxReason.setValue("combining Tickets");
                    depositButton.setVisible(true);
                    return;
                }
            printButton.fire();
        }
        try {
            //System.out.println(cBoxReason.getValue());
            if (!cBoxReason.getValue().equals("")) {
                //System.out.println(cBoxReason.getValue());            
                cBoxReason.hide();
                printButton.fire();
            }
        } catch (Exception ex) {
            //System.out.println("This is the CBoxReason error: " + ex);

        }
        if (cBoxReason.isFocused()) {
            cBoxReason.hide();
            printButton.fire();
        }

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
                depositButton.fire();
                break;
            case F5:
                break;
            case F6:
                printButton.fire();
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
                EnterKeyPressed();
                break;
            default:
                break;
        }
    }

    private void FillcBoxReasonComboBox() {
        List<String> myList;
        try {
            myList = Files.lines(Paths.get(dbsp.pathNameClub + "vReasons.txt")).collect(Collectors.toList());
            String[] cmbList = new String[myList.size()];
            for (int i = 0; i < myList.size(); i++) {
                cmbList[i] = myList.get(i) + "\n";
            }
            ComboBox<String> cmb = cBoxReason;
            cmb.getItems().clear();
            cmb.setEditable(true);
            cmb.setTooltip(new Tooltip());
            cmb.getItems().addAll(cmbList);
            new ComboBoxAutoCompleteVoucher<>(cmb);
        } catch (IOException e) {
            System.out.println("Can't find VoucherReasons.txt file" + e);
        }

    }

    public void exitButtonPushed(ActionEvent event) throws IOException {
        try {
        listToRedeem.clear();
        vList.clear();
        nextVoucherID = "";
        nextNumber = 0;
        } catch(Exception e) {}
        eRedeemCount = 0;
        en = null;
        fn = null;
        ln = null;
        eFXX.Flush();
        stageV.close();
    }


    
 
 
 public void GOdepositButton() throws IOException {
     String voucherDeposit = "";
        MemDepositFXController wController = (MemDepositFXController) FXLOADER.getController();   
        if(!addingField.isVisible()) {
            voucherDeposit = "0";
        } else {
            voucherDeposit = addingField.getText();
        }
        wController.sc = sc;
        wController.db = db;
        wController.dbsp = dbsp;
        wController.member = member;
        wController.memtick = memtick;
        wController.mBox = mBox;
        wController.E = EE;
        wController.l = l;
        wController.cssPath = css;
        wController.rCeipts = receipts;
        wController.jmail = jmail;
        wController.chkBalance = chkBalance;
        wController.FXLOADER = FXLOADER;
        wController.VIPPromosDailyBonus = Daily;
        wController.pw = pw;
        wController.voucherDeposit = voucherDeposit;
        wController.voucherEmployee = en;
        wController.listToRedeem = listToRedeem;
        wController.vList = vList;
        wController.currentTime = currentTime;
        getStageV();
        sc.loadSceneOverVoucher(stageV, "/views/MemDepositFX.fxml", null, 200.0, 50.0);
        cancelButton.fire();
        //sc.loadSceneRemoveDepPane(stageV, "/views/MemDepositFX.fxml", null, 200.0, 50.0);
        //backToCCNumberIn();
    }
  
}
