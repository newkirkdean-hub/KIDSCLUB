
package views.timeclock;

import JavaMail.Mail_JavaFX;
import JavaMail.Mail_JavaFX1;
import JavaMail.Mail_JavaFX_Inline_Pic;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.IsItANumber;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import models.club.Member;
import models.club.Memtick;
import models.club.rCeipts;
import models.timeclock.EmpFileFX;
import models.timeclock.EmpFileFXDB;
import models.timeclock.EmpFileFXDetail;
import models.toys.Toys;
import pWordFX.empFX;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;


    

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class EmpDetailPopUpCounterController implements Initializable {
    @FXML private Button printButton;
    @FXML private Button cancelButton;
    @FXML private Button BpopUpButton;
    @FXML private Label purposeTF;
    @FXML private Label nameLabel;
    @FXML private TextArea notesTF;
    @FXML private AnchorPane root;
    
    
    
    

    private static final GetDay gd = new GetDay();
    private static final IsItANumber iin = new IsItANumber();
    private static final java.text.SimpleDateFormat tdf = new java.text.SimpleDateFormat("hh:mm a");
    private static final java.text.SimpleDateFormat ddf = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final DecimalFormat df = new DecimalFormat(".00");
    private static final DecimalFormat dff = new DecimalFormat("#");
    private static final DecimalFormat dfff = new DecimalFormat("###,###,###");

    public static ArrayList<rCeipts> rCeipts;

    //private static final dbStringPath DBSP = new dbStringPath();
    private static final settingsFXML SETTINGS = new settingsFXML();
    
    
    public static SceneChanger_Main sc;
    public static dbStringPath dbsp;
    public static ArrayList<empFX> EE;
    public static String cssPath, rNumber = "";
    public static employeeFX eFXX;



    private static EmpFileFXDetail g = null;
    private static String tMessage = "", E = null, V = null, s1 = null;
    private static Bounds bboundsInScene;
    public static String en, fn, ln, empID, empName, MGR, updateVar, screen, empNumber;
    public static int vAmt;

    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        bboundsInScene = nameLabel.localToScene(nameLabel.getBoundsInLocal());
        root.getStylesheets().add(cssPath);
        setKeyCodes();
        setToUpper();
        new FocusedTextFieldHighlight().setHighlightListenerBdays(root);
        addTextfieldListeners();
        nameLabel.setText(empName);
        printButton.setDisable(true);
        setTextFieldFocus();
        System.out.println("HERE WE ARE IN THE BEGINING OF POPUPCOUNTER AND THE UPDATEvAR = " + updateVar);
        Platform.runLater(()->BpopUpButton.requestFocus());
        Platform.runLater(()->BpopUpButton.fire());
        
    }    
    
    
    public void printButtonPushed(ActionEvent event) {
        java.util.Date dt = new java.util.Date();
        String currentTime = tdf.format(dt);
        if (purposeTF.getText().trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP!", "The Name Field Cannot be Blank.");
            BpopUpButton.requestFocus();
            BpopUpButton.fire();
            return;
        }
        if (notesTF.getText().trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP!", "The Notes Field Cannot be Blank.");
            notesTF.requestFocus();
            return;
        }
        if (empNumber.trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP!", "The Employee Name is invalid");
            BpopUpButton.requestFocus();
            BpopUpButton.fire();
            return;
        }
        s1 = notesTF.getText().trim();
        s1 = s1.replaceAll("'", "`");
        EmpFileFX corpEmployee = null;
        System.out.println("THE UPDATE VAR IS : " + updateVar + "en " + en + "empID " + empID + "empNumber " + empNumber);
        if (updateVar.equals("1")) {
            try {
                corpEmployee = new EmpFileFXDB().getEmployee(empNumber);
                sendCorpEmail(purposeTF.getText(), s1, corpEmployee.getEmail());
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            //EmpFileFX corpEmployee = new EmpFileFX(empID);
            
        } else {
            if (!purposeTF.getText().isEmpty()) {
        System.out.println("PURPOSE IS NOT EMPTY");
                s1 = notesTF.getText().trim();
                s1 = s1.replaceAll("'", "`");
                g = new EmpFileFXDetail(screen, screen, nameLabel.getText(), currentTime, notesTF.getText(), "0", 0.0, empID, LocalDate.now());
                if (!new EmpFileFXDB().AddEmpDetail(g)) {
                    new Mail_JavaFX().SendMail("Error", "error in Emp New Employee upDateEMP Detail (See EmpViewerCounterController) ");
                    System.out.println("error in Emp New Employee upDateEMP Detail ");
                } else {
                    Platform.runLater(() -> new Mail_JavaFX1().sendEmailTo("Employee Bridge Note", "This Employee " + purposeTF.getText() + " has had a note added to his/her file: \n Superviosr: " + nameLabel.getText() + "\nNotes added to file: " + s1 + "\nDate: " + LocalDate.now() + "\nTime: " + LocalTime.now(), "EmpBridgeNote"));
                    cancelButton.fire();
                    //recent bridge nots and full details to file.
                }
            } else {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Empty Field", "The field Employee Name cannot be empty");
            }
        }
    }
    
    
    
    private String getScreen() {
        String screenName = "";
        dbsp.setName();
        File file = new File(dbsp.pathNameLocal + "configCounterFXML.properties");
        if (file.exists() && file.canRead()) {
            switch (SETTINGS.getCounterSettings("stage")) {
                case "1":
                    screenName = "BRIDGE NOTE";        
                    break;
                case "2":
                    screenName = "CAFE NOTE";        
                    break;
                case "3":
                    screenName = "COUNTER NOTE";        
                    break;
                case "4":
                    screenName = "TOUCH NOTE";        
                    break;
                case "5":
                    break;
                case "6":
                    break;
            }
            
        } else {
            screenName = "CORP NOTE";        

       }
        return screenName;
    }
    
    
    
    
    
    
    private void setTextFieldFocus() {
        //showErrorClear();
        //purposeTF.focusedProperty().addListener((obs, oldVal, newVal) -> {
            //if (newVal) {
                //if (editMode == 1) {
                    //if (purposeTF.getText().length()!=0) {
                    //    purposeTF.clear();
                    //    BpopUpButton.setVisible(true);
                    //    BpopUpButton.fire();
                    //} else {
              //          BpopUpButton.setVisible(true);
                        //showError("Press the DOWN ARROW Key for the Selection List");
                    //}
                //}
            //} else {
              //  BpopUpButton.setVisible(false);
            //}
        //});
    }
    
    public void popUP1(ActionEvent event) throws IOException {
        //if (purposeTF.isFocused()) {
            getPopUpScreen(event, 1);
        //}
    }
    
    
    private void getPopUpScreen(ActionEvent event, int x) throws IOException {
        switch (x) {
            case 1: 
                if (updateVar.equals("1")) {
                    sc.changePopUp(event, "/views/timeclock/EmpDetailPopUpSearch.fxml", "List of Corps");
                } else {
                    sc.changePopUp(event, "/views/timeclock/EmpDetailPopUpSearch.fxml", "List of Employees");
                }
                //sc.getPopUp(event, "/views/timeclock/EmpDetailPopUpSearch.fxml", "Purpose", "Select One:", purposeTF.getText(), bboundsInScene.getMaxX()+355.0, bboundsInScene.getMaxY()+105.0);
                if (!sc.getGameNumber().equals("Number")) {
                    purposeTF.setText(sc.getGameNumber());
                    if (isEMPValidInArrayList(sc.getGameNumber())) {
                        System.out.println("HERE IS THE EMPLOYEE NUMBER " + sc.getGameNumber());
                        notesTF.requestFocus();
                        purposeTF.setText(fn + " " + ln);
                    } else {
                        purposeTF.setText("Employee Not Found " + sc.getGameNumber());
                        BpopUpButton.requestFocus();
                    }
                } else {
                    purposeTF.setText("Employee Not Found");
                    BpopUpButton.requestFocus();
                }
                break;
        }
    }
    
    
    private boolean isEMPValidInArrayList(String n) {
        boolean empValid = false;
        for (int y = 0; y < EE.size(); y++) {
            if (n.trim().equals(String.valueOf(EE.get(y).getEmpNumber()))) {
                empValid = true;
                empID = String.valueOf(EE.get(y).getEmpID());
                empNumber = n;
                fn = EE.get(y).getNameF();
                ln = EE.get(y).getNameL();
                printButton.setDisable(false);
                //empID = String.valueOf(EE.get(y).getEmpID());
            }
        }
        return empValid;
    }

    
    
    
    
    
     private void setToUpper() {
        for (Node node : root.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).textProperty().addListener((ov, oldValue, newValue) -> {((TextField) node).setText(newValue.toUpperCase());});
            }
        }
        notesTF.textProperty().addListener((ov, oldValue, newValue) -> {notesTF.setText(newValue.toUpperCase());});
    }
    
    
    public void addTextfieldListeners() {
        purposeTF.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        //iin.checkNumbers(newValue);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Voucher Amount can only be Numbers");
                        //vAmountField.clear();
                        //vAmountField.requestFocus();
                        return;
                    }
                }
        );
    }
    
    
    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F6) {keyListener(ke); ke.consume();}
         //if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
         //if (ke.getCode() == KeyCode.F8) {keyListener(ke); ke.consume();}
         //if (ke.getCode() == KeyCode.F9) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ENTER) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke); ke.consume();}
     });   
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: break;
                    case F2: break;
                    case F3: break;
                    case F4: break;
                    case F5: break;
                    case F6: printButton.fire(); break;
                    case F7: break;
                    case F8: break;
                    case F9: break;
                    case F10: break;
                    case F11: break;
                    case DOWN: break;
                    case UP: break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: doEnterKey(); break;
                default:
                    break;
                }
    } 
    
    
    private void doEnterKey() {
        if (purposeTF.isFocused()) {
            notesTF.requestFocus();
            return;
        }
        if (notesTF.isFocused()) {
            printButton.fire();
            return;
        }
    }
    
    
    
   public void exitButtonPushed(ActionEvent event) throws IOException {
       en = null;
       fn = null;
       ln = null;
       empID = null;
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        stageV.close();
    } 

    
 private String getReceipts(String n, ArrayList<rCeipts> rCeipts) {
        String StringItem = null;
        //System.out.println("--------------------  " + rCeipts.size() + " " + n);
        for (int y = 0; y < rCeipts.size(); y++) {
            if (n.trim().equals(rCeipts.get(y).getrItem())) {
                //System.out.println(rCeipts.get(y).getrItem() + " " + rCeipts.get(y).getrString() + " " + rCeipts.get(y).getrNumber());
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
    
 
   
    private void sendCorpText(String Fname, String theMessage, String eMail) {
        new Mail_JavaFX1().sendCorpTextTo(Fname, theMessage, eMail);

    }


    public void sendCorpEmail(String Fname, String theMessage, String eMail) {
            System.out.println("---------------------22222222222222222222222 " + Fname + " " + theMessage + " " + eMail);
        try {
            //if ( eMail.trim().length() > 3 || !eMail.trim().equals("BAD")) {
                //System.out.println("--------------------------------------------");
            //} else {
                //System.out.println("---------------------22222222222222222222222");
            //    return;
            //}
        } catch (Exception e) {
            //System.out.println("---------------------22222222222222222222222 " + e);
            return;
        }
        
        
        System.out.println(eMail);

        String h1 = getReceipts("CoName", rCeipts);
        String h2 = getReceipts("ClubEmailHeaderLine3", rCeipts);
        String h3 = getReceipts("ClubEmailHeaderLine2", rCeipts);
        String c1 = getReceipts("ClubEmailColor", rCeipts);

        StringBuilder body = new StringBuilder("<html>");
        body.append("<center><img src=\"cid:image1\"></center><br>");
        body.append("<table BORDER=\"0\" bgcolor=\"" + c1 + "\" width=100%>");
        body.append("<tr><td colspan=\"2\" WIDTH=\"100%\"><center><H1 style=\"color:#FFFFFF;\"><Center>" + h1 + "</Center></H1></td></tr>");
        body.append("<tr><td colspan=\"2\" WIDTH=\"100%\"><center><H1 style=\"color:#FFFFFF;\"><Center>" + h3 + "</Center></H1></td></tr>");
        body.append("<tr><td colspan=\"2\" WIDTH=\"100%\"><center><H1 style=\"color:#FFFFFF;\"><Center>A Message From POJOS</Center></H1></td></tr>");
        body.append("</table>");

        body.append("<table BORDER=\"0\" width=100%>");
        body.append("<tr><td colspan=\"4\" WIDTH=\"100%\" style=\"text-align:center\"><p><font color=\"#000000\"><b>TO:</b> " + Fname + "&nbsp;");
        body.append("<tr><td colspan=\"4\" WIDTH=\"100%\" style=\"text-align:center\"><p><font color=\"#000000\">\n\nTODAYS DATE:&nbsp;");
        body.append(formatter.format(new GetDay().NOW_LOCAL_DATE()));
        body.append("</p></td></tr>\n");

        body.append("<tr><td colspan=\"4\" WIDTH=\"100%\" style=\"text-align:center\"><p><font color=\"#000000\"><b>. . . . . . . . . . . . . . . . . .</b>");
        //body.append("<tr><td WIDTH=\"33%\" >&nbsp;</td><td colspan=\"2\" WIDTH=\"34%\">. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .</td><td WIDTH=\"33%\" >&nbsp;</td></tr>\n");
        //body.append("<tr><td WIDTH=\"35%\" >&nbsp;</td><td WIDTH=\"15%\" style=\"text-align:left\"><p><font color=\"#000000\">PREVIOUS BALANCE: </td><td WIDTH=\"10%\" style=\"text-align:right\">" + dfff.format(preBalance) + "</p></td><td WIDTH=\"40%\" >&nbsp;</td></tr>\n");

        
        body.append("<tr><td colspan=\"4\" WIDTH=\"100%\" style=\"text-align:center\">" + theMessage + " </td></tr>\n");
        body.append("<tr><td colspan=\"4\" WIDTH=\"100%\" style=\"text-align:center\"><b>Sent By:</b>" + nameLabel.getText() + " </td></tr>\n");
        body.append("<tr><td colspan=\"4\" WIDTH=\"100%\" style=\"text-align:center\">&nbsp;</td></tr>\n");
        body.append("<tr><td colspan=\"4\" WIDTH=\"100%\" style=\"text-align:center\">&nbsp;</td></tr>\n");
        
        //body.append("<tr><td WIDTH=\"100%\" style=\"text-align:center\"><p><font color=\"#000000\">SERVER:&nbsp;</td><td WIDTH=\"80%\">" + fn + "</p></td></tr>");
        body.append("</table>\n");
        //body.append("The first image is a chart:<br>");
//        body.append("<img src=\"cid:image2\" width=\"1%\" height=\"1%\" /><br>");
        String EmailAdd1 = getReceipts("EmailAdd1", rCeipts);
        if (!EmailAdd1.trim().isEmpty()) {
            body.append("<table BORDER=\"0\" width=100%>");
            body.append("<tr><td WIDTH=\"25%\"><center>");
            body.append("<img src=\"cid:image2\"></center>");
            body.append("<td WIDTH=\"75%\">" + EmailAdd1);
            body.append("</td></tr></table>");
        }
        String eAdd2 = getReceipts("ClubEmailAdd2", rCeipts);
        if (getrNumber().equals("1.0")) {
            if (!eAdd2.trim().isEmpty()) {
                body.append("<table BORDER=\"0\" width=100%>");
                body.append("<tr><td WIDTH=\"25%\"><center>");
                body.append("<img src=\"cid:image3\"></center>");
                body.append("<td WIDTH=\"75%\">" + eAdd2);
                body.append("</td></tr></table>");
            }
        } else {
            eAdd2 = "";
        }
        //body.append("The second one is a cube:<br>");
        //body.append("<img src=\"cid:image3\"><br>");
        //body.append("<img src=\"cid:image4\"><br>");
        //body.append("End of message.");
        body.append("</html>");

        Map<String, String> inlineImages = new HashMap<>();
        inlineImages.put("image1", "//POJOS-NET-DRIVE//clubdb//Images//EmailReceipt//Clubheader.jpg\\");
        if (!EmailAdd1.trim().isEmpty()) {
            inlineImages.put("image2", "//POJOS-NET-DRIVE//clubdb//Images//EmailReceipt//ClubEmailone.png\\");
        }
        if (!eAdd2.trim().isEmpty()) {
            inlineImages.put("image3", "//POJOS-NET-DRIVE//clubdb//Images//EmailReceipt//ClubEmailtwo.png\\");
        }
        //inlineImages.put("image3", "j://Kidsclub//Images//EmailReceipt//two.png\\");
        //inlineImages.put("image4", "j://Kidsclub//Images//EmailReceipt//three.png\\");

        //System.out.println(body.toString());
        new Mail_JavaFX_Inline_Pic().sendEmailToHTML("POJOS MESSAGE", body.toString(), eMail, inlineImages, rCeipts);
        //sendCorpText("Dean", theMessage + " " + nameLabel.getText(), "2082848223@tmomail.net");
        cancelButton.fire();
    }

    
}
