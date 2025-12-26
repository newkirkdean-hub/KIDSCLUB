package views.timeclock;

import JavaMail.Mail_JavaFX1;
import XML_Code.readXMLToArrayList;
import XML_Code.writeArrayListToXMLFile;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.IsItANumber;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.File;
import javafx.scene.control.Label;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import messageBox.messageBox;
import models.timeclock.message;
import models.timeclock.tClockDB;
import models.voucher.FXVoucherViewer;
import pReceipts.print;
import pWordFX.empFX;
import pWordFX.employeeFX;


/**
 * FXML Controller class
 *
 * @author Dean
 */
public class TClockDialogFXController implements Initializable {
    @FXML private Button printBtn;
    @FXML private Button cancelButton;
    @FXML private Button clockOut;
    @FXML private Button clockIn;
    @FXML private ComboBox<String> departmentField;
    @FXML private PasswordField empNumberIn;
    @FXML private TextField clocktypevar;
    @FXML private Label empNameLabel;
    @FXML private Label jLabel1;
    @FXML private Label F7Print;
    @FXML private Label scrollTextLable;
    @FXML private AnchorPane root;
    
    private static final GetDay gd = new GetDay();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat timeformatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final tClockDB db = new tClockDB();

    public static ArrayList<empFX> EE;
    public static String cssPath;
    public static dbStringPath dbsp;
    public static employeeFX eFX;
    public static messageBox mbox;
    public static Mail_JavaFX1 jmail;

    private static empFX newEFX = null;
    private static String msg = "No";
    public static String tMessage = "";
    private static int F7pressed = 1;
    private static Connection conn = null, connM = null, connt = null;
    private static Statement s = null, ss=null;
    private static ResultSet rs = null;
    private static ResultSet rsMSG = null;
    private static ResultSet rstLong = null;
    private static Stage stageV; 
    private static String inDtTime = "";
    private static String emp = null, empName = "", dPartment = null, tt = "";
    private static int clocktype = 0;
    private static Date sdt = null;
    private static Date sdp = null;



    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        scrollTextLable.setText("Urgent Employee Message Coming . . .");
        playText();
        addTextfieldListeners();
        root.getStylesheets().add(cssPath);
        setKeyCodes();
        resetFields();
        df.setMaximumFractionDigits(2);
        inDtTime = timeformatter.format(new Date());
        new FocusedTextFieldHighlight().setHighlightListener(root);
        Platform.runLater(()->getStageV());  
        Platform.runLater(()->clockIn.requestFocus());  
        Platform.runLater(()->fillComboBox());
        Platform.runLater(()->setTimeClockMessage());
        //new Thread(taskGetMessageXML).start();
    }    
    
    public void addTextfieldListeners() {
        empNumberIn.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        new IsItANumber().checkNumbers(newValue);
                    } catch (Exception e) {
                        //JOptionPane.showMessageDialog(null, "Employee Number can Only be Numbers");
                        mbox.showAlert(Alert.AlertType.ERROR, stageV, "Stop", "Employee Number can Only be Numbers");
                        empNumberIn.clear();
                        empNumberIn.requestFocus();
                        return;
                    }
                }
        );
    }
    
    
    public void printBtnDo() { 
    emp = empNumberIn.getText().trim();
    if (printBtn.isVisible()) {
        if (emp.trim().isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Employee Number cannot be blank");
            mbox.showAlert(Alert.AlertType.ERROR, stageV, "Stop", "Employee Number cannot be blank");
            empNumberIn.setText("");
            empNumberIn.setStyle("-fx-background-color: Yellow");
            empNameLabel.setText("");
            empNameLabel.setVisible(false);
            empNumberIn.requestFocus();
            return;
        }else{
            if (!isEMPValidInArrayList(emp)){
            //if (eFX.isemployeevalid(emp)!=true){
                empNumberIn.setText("");
                resetFields();
                return;
            }
        }
        empName = newEFX.nameF + " " + newEFX.nameL;
        if (empName.trim().isEmpty()) {
            resetFields();
            return;
        }else{
            empNameLabel.setVisible(true);
            getEmpName();
        }
        Date f = new Date();
        GetDay t = new GetDay();
        String theday = t.getIntDate(f);
        clocktype = Integer.parseInt(clocktypevar.getText());
//CLOCK IN 
        switch (clocktype) {
            case 1: // Clock IN
            
            
            if (eFX.isClockedIn(String.valueOf(newEFX.getEmpID()))) {
                //JOptionPane.showMessageDialog(null, "You are already Clocked in.");
                mbox.showAlert(Alert.AlertType.ERROR, stageV, "STOP", "You are already Clocked in.");
                resetFields();
                return;
            }
        dPartment = String.valueOf(departmentField.getValue());
        if (dPartment.trim().isEmpty() || dPartment.trim().equals("null")) {
            //JOptionPane.showMessageDialog(null, "You Must Select a Department");
            mbox.showAlert(Alert.AlertType.ERROR, stageV, "STOP", "You Must Select a Department");
            return;
        }
            db.insertTimeIN(newEFX.empID, formatter.format(f), theday, empName, inDtTime, "Pojos Fv", dPartment);
                this.resetFields();
                stageV.close();
                if (!tMessage.equals("")) {
                    //Platform.runLater(()->new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "Important Message", tMessage));
                    Platform.runLater(()->mbox.showTimeClockMessage(Alert.AlertType.INFORMATION, stageV, "Important Message", tMessage));
                    //Platform.runLater(()->dp.justDialog(tMessage));
                }
            break;
// CLOCK OUT ======================================
            case 2: {
            
            
            if (!eFX.isClockedIn(String.valueOf(newEFX.getEmpID()))) {
                //JOptionPane.showMessageDialog(null, "You are not Clocked in.");
                mbox.showAlert(Alert.AlertType.ERROR, stageV, "STOP", "You are not Clocked in.");
                resetFields();
                return;
            } else {                               
                sdt = StringToDate(db.getClockInTime(newEFX.getEmpID()));
                sdp = StringToDate(inDtTime);
            }
            db.insertClockOut(newEFX.empID, inDtTime, timeTotal(sdt, sdp));
            //==============================================================================================

/////this is where we need the printer to come in and also a question asking if they want a report.
            int n = F7pressed;

            switch (n) {
                case 1: //No

       try {
                //s = conn.createStatement();
                String ts = "SELECT Memtime.Date, Memtime.Day, Memtime.Emp_Name, Memtime.Time_IN, Memtime.Time_OUT, Memtime.Total_Time FROM Memtime WHERE (((Memtime.Employee_ID)='" + newEFX.getEmpID() + "') AND ((Memtime.Date)=#" + formatter.format(f) + "#))";
                rstLong = db.printTimeOut(ts);
                PrintWriter pw = new PrintWriter(new File(dbStringPath.pathNameLocal + "tClockReceipt.txt"));
                pw.println("Pojos of ID. Inc."); // to test if it works.
                pw.println("Time Clock Hours Today"); // to test if it works.
                pw.println("======================"); // to test if it works.
                pw.println(f);
                pw.println(" ");
                pw.println(newEFX.nameF + " " + newEFX.nameL);
                while (rstLong.next()) { 
              //String inT = String.valueOf(rstLong.getObject(4));
              //String outT = String.valueOf(rstLong.getDate(5));
                    
                    pw.println("Time In: " + rstLong.getObject(4));
                    pw.println("Time Out: "+ rstLong.getObject(5));
                    pw.println("Total: " + rstLong.getObject(6));
            Double totalT = (Double) rstLong.getObject(6);
            Double newTotalTime = timeTotal(sdt, sdp);
                pw.println(" ");
            
                }
            //if (!newTotalTime.equals(totalT)) {
            //    Platform.runLater(()->new Mail_JavaFX1().sendEmailTo("Time Clock Test", eFX.nameF + " " + eFX.nameL + "\n" + sdt + " \t" + sdp + " \t" + newTotalTime));
            //}    
                pw.println(" ");
                pw.println("EMPLOYEE COPY");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.close();
                print pr = new print();
                pr.printReceipt("tClockReceipt.txt");

            } catch (SQLException ex) {
                System.out.println(" 2" + ex);
            } catch (IOException ex) {
                System.out.println(" 3" + ex);
            } finally {
                try { rstLong.close(); } catch (SQLException ex) {}
            } 
            break;
                   
                case 0: //Yes
            try {
                Calendar c = Calendar.getInstance();
                int newDOW = c.get(Calendar.DAY_OF_WEEK);
                if (newDOW==1) {
                    newDOW = 8;
                }
                int timesThis = newDOW - 2;
                Date nDate = new Date(System.currentTimeMillis());
                nDate = new Date(nDate.getTime() - 24 * 60 * 60 * 1000 * timesThis);


                //s = conn.createStatement();
                String ts = "SELECT Memtime.Date, Memtime.Day, Memtime.Emp_Name, Memtime.Time_IN, Memtime.Time_OUT, Memtime.Total_Time FROM Memtime WHERE (((Memtime.Employee_ID)='" + newEFX.getEmpID() + "') AND ((Memtime.Date)>=#" + formatter.format(nDate) + "#))";
                rstLong = db.printTimeOut(ts);
                //rstLong = s.executeQuery(ts);
                PrintWriter pw = new PrintWriter(new File(dbStringPath.pathNameLocal + "tClockReceipt.txt"));
                pw.println("Pojos of ID. Inc."); // to test if it works.
                pw.println("Time Clock Hours This Week"); // to test if it works.
                pw.println("======================"); // to test if it works.
                pw.println(f);
                pw.println(" ");
                pw.println(newEFX.nameF + " " + newEFX.nameL);
                while (rstLong.next()) {
                    pw.println("Time In: " + rstLong.getObject(4));
                    pw.println("Time Out: "+ rstLong.getObject(5));
                    pw.println("Total: " + df.format(rstLong.getObject(6)));
                }
                pw.println(" ");
                pw.println("EMPLOYEE COPY");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.println(" ");
                pw.close();
                print pr = new print();
                pr.printReceipt("tClockReceipt.txt");

            } catch (SQLException | IOException ex) {
               jmail.sendEmailTo("Error TClockDialogFX", ex.toString(), "errors");
            } finally {
            }
                break;
            }

        }
            
            
            
        try {
            if (rstLong != null);
            //System.out.println("TimeClock rstLong = " + rstLong.isClosed());
            rstLong.close();
            //System.out.println("TimeClock rstLong = " + rstLong.isClosed());
        } catch (SQLException ex) {
           jmail.sendEmailTo("Error TClockDialogFX", ex.toString(), "errors");
        }
        }
        this.resetFields();
        exitButtonPushed();
        }
    }      
    
    public double timeTotal(Date d1, Date d2) {
        double timeTotalValue = 0.0;
        long diff = d2.getTime() - d1.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        Double diffM = (diffMinutes / 60.0);
        timeTotalValue = (diffHours + diffM);
        return timeTotalValue;
    }
    
    
    
    public static Date StringToDate(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date1 = null;
        try {
            date1 = format.parse(strDate);
        } catch (ParseException ex) {
            System.out.println(" 8" + ex);
        }
    return date1;
}
    
    
    private void setTimeClockMessage() {
        if (!tMessage.isEmpty()) {
            msg = "Important";
            scrollTextLable.setVisible(false);
        } else {
            msg = "No";
            scrollTextLable.setVisible(false);
        }
    }
    
    
    
     /*Task<Void> taskGetMessageXML = new Task<Void>() {
         @Override protected Void call() throws Exception {

        ArrayList<message> messageList = new readXMLToArrayList().getempMessageDataXML();
        System.out.println("This would be the date to search for; " + LocalDate.now());
        System.out.println(" BEFORE: " + messageList.size());
        for (int i = 0; i < messageList.size(); i++) {
            String startDatefirstTenChars = messageList.get(i).getDateStart().toString().substring(0, 10);
            String endDatefirstTenChars = messageList.get(i).getDateEnd().toString().substring(0, 10);
            /*
            int sDate = LocalDate.now().compareTo(new GetDay().setToLocalDateforXML(startDatefirstTenChars));
            int eDate = LocalDate.now().compareTo(new GetDay().setToLocalDateforXML(endDatefirstTenChars));
            int ssDate = new GetDay().setToLocalDateforXML(startDatefirstTenChars).getDayOfYear();
            int eeDate = new GetDay().setToLocalDateforXML(endDatefirstTenChars).getDayOfYear();
            int dateTodayINT = LocalDate.now().getDayOfYear();
            
            System.out.println("======================================== today" + ssDate + " " + dateTodayINT  + " " + eeDate);
            if (ssDate <=dateTodayINT && eeDate >= dateTodayINT) {
                System.out.println(":===================================== " + ssDate + " " + eeDate);
            }
            */
            
            //if (new GetDay().setToLocalDate(vouchersFullList.get(i).getC1()).isBefore(LocalDate.now().minusDays(80))){
      /*      if (new GetDay().setToLocalDateforXML(startDatefirstTenChars).isEqual(LocalDate.now()) || new GetDay().setToLocalDateforXML(startDatefirstTenChars).isBefore(LocalDate.now())) {
                if (new GetDay().setToLocalDateforXML(endDatefirstTenChars).isEqual(LocalDate.now()) || new GetDay().setToLocalDateforXML(endDatefirstTenChars).isAfter(LocalDate.now())) {
            System.out.println("======================================== enddate" + endDatefirstTenChars);
                    System.out.println(" Winner: startDate = to LocalDate.now()" + messageList.size());
                    tMessage = messageList.get(i).getHTML();
                    if (!tMessage.isEmpty()) {
                        msg = "Important";
                        scrollTextLable.setVisible(false);
                    } else {
                        msg = "No";
                        scrollTextLable.setVisible(false);
                    }
                }
            } //END OF IF
        } //END OF FOR
        return null;
        }
    };
    */
    
    
    
    
        
    Task<Void> taskGetMessage = new Task<Void>() {
         @Override protected Void call() throws Exception {
        Date f = new Date();
        try {
            connM = DriverManager.getConnection("jdbc:ucanaccess://" + dbStringPath.pathNameClubDBs + "Message.accdb;immediatelyReleaseResources=true");
            s = connM.createStatement();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String qMSG = ("SELECT empMSG.MSG FROM empMSG WHERE (((empMSG.stDate)<=#" + formatter.format(f) + "#) AND ((empMSG.endDate)>=#" + formatter.format(f) + "#));");
            rsMSG = s.executeQuery(qMSG);
            while (rsMSG.next()) {
                tMessage = rsMSG.getString("MSG");
            }
            if (!tMessage.equals("")) {
                msg = "Important";
                scrollTextLable.setVisible(false);
            } else {
                msg = "No";
                scrollTextLable.setVisible(false);
            }
        } catch(SQLException eq) {
          jmail.sendEmailTo("Error TClockDialogFX", eq.toString(), "errors");
        } finally {
            //System.out.println("Message RS = " + rsMSG.isClosed());
            try { rsMSG.close(); } catch (SQLException ec) { /* ignored */ }
            //System.out.println("Message RS = " + rsMSG.isClosed());
            //System.out.println("Message S = " + s.isClosed());
            try { s.close(); } catch (SQLException ec) { /* ignored */ }
            //System.out.println("Message S = " + s.isClosed());
            //System.out.println("Message connM = " + connM.isClosed());
            try { connM.close(); } catch (SQLException ec) { new Mail_JavaFX1().sendEmailTo("Error TClockDialogFX", ec.toString(), "errors");  }
            //System.out.println("Message connM = " + connM.isClosed());
        }
        return null;
         }
    };
    
    private void fillComboBox() {
        List<String> myList;
        try {
            myList = Files.lines(Paths.get(dbsp.pathNameTclock + "Depart.txt")).collect(Collectors.toList());
            departmentField.setItems(FXCollections.observableArrayList(myList));
        } catch (IOException e) {
           jmail.sendEmailTo("Error TClockDialogFX", "Can't find Depart.txt file" + e, "errors");
        }
    }
    
    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F3) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F4) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F6) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.TAB) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.UP) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.DOWN) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ENTER) {keyListener(ke); ke.consume();}
     });   
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: break;
                    case F2: break;
                    case F3: clockIn.fire(); break;
                    case F4: clockOut.fire(); break;
                    case F5: break;
                    case F6: printBtn.fire(); break;
                    case F7: F7keyPressed(); break;
                    case F8: break;
                    case F9: break;
                    case F10: break;
                    case F11: break;
                    case TAB: moveCursor(); break;
                    case DOWN: 
                        if (departmentField.isFocused()) {
                            departmentField.show();
                        } else {
                            moveCursor(); 
                        }
                        break;
                    case UP: moveCursor(); break;
                    case ESCAPE: exitButtonPushed(); break;
                    case ENTER: moveCursor(); break;
                default:
                    break;
                }
    } 
    
    private void F7keyPressed() {
        F7pressed = 0;
        printBtn.fire();
    }
    
    private void resetFields(){
        clocktypevar.setText("0");
        empNumberIn.setText("");
        empNameLabel.setText("");
        empNameLabel.setVisible(false);
        clockIn.setVisible(true);
        clockOut.setVisible(true);
        F7Print.setVisible(false);
        F7pressed = 1;
        jLabel1.setVisible(false);
        departmentField.setVisible(false);
        //msg = null;
        tt="";
        scrollTextLable.setVisible(false);
        printBtn.setVisible(false);
        empNumberIn.setVisible(false);
        clockIn.requestFocus();

    }
    
    private void playText(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.1), scrollTextLable);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.play();

    }
    
    public void clockOutButtonPushed() {
        jLabel1.setVisible(true);
        clocktypevar.setText("2");
        empNumberIn.setVisible(true);
        clockIn.setVisible(false);
        clockOut.setVisible(false);
        printBtn.setVisible(true);
        F7Print.setVisible(true);
        empNumberIn.requestFocus();
    }
    
    public void clockInButtonPushed() {
        jLabel1.setVisible(true);
        if (msg.equals("Important")) {
            scrollTextLable.setVisible(true);
        }
        clocktypevar.setText("1");
        empNumberIn.setVisible(true);
        departmentField.setVisible(true);
        clockIn.setVisible(false);
        clockOut.setVisible(false);
        printBtn.setVisible(true);
        empNumberIn.requestFocus();
        
        }
    
    private void moveCursor(){
        if (empNumberIn.isVisible()) {
            empNumberIn.selectAll();
            emp = empNumberIn.getText().trim();
            if (empNumberIn.getSelectedText() == null) {
                empNameLabel.setVisible(true);
                empNameLabel.setText("Employee Number cannot be blank.");
                empNumberIn.setStyle("-fx-background-color: Yellow");
                empNumberIn.requestFocus();
                return;
            } else {
                if (!isEMPValidInArrayList(emp)){
                    empNumberIn.setText("");
                    return;
                }
                //if (!eFX.isemployeevalid(emp)) {
                //    empNumberIn.setText("");
                //    return;
                //}
                if (eFX.isClockedIn(String.valueOf(newEFX.getEmpID())) && Integer.parseInt(clocktypevar.getText()) == 1) {
                    mbox.showAlert(Alert.AlertType.ERROR, stageV, "Stop", "You are already Clocked in.");
                    empNumberIn.setText("");
                    resetFields();
                    return;
                } else {
                    empNumberIn.setStyle("-fx-background-color: white");
                    getEmpName();
                    if (clocktypevar.getText().equals("2")) {
                        printBtn.requestFocus();
                    }
                    if (clocktypevar.getText().equals("1")) {
                        departmentField.setValue(db.getDepartment(newEFX.getEmpID()));
                        departmentField.requestFocus();
                        if (msg.equals("Important")) {
                            scrollTextLable.setVisible(true);
                        }
                    }
                }
            }
        }
    }
    
    
    
     private boolean isEMPValidInArrayList(String n) {
        boolean empValid = false;
        for (int y = 0; y < EE.size(); y++) {
            if (n.trim().equals(EE.get(y).getEmpNumber())) {
                empValid = true;
                newEFX = new empFX(EE.get(y).getNameF(), EE.get(y).getNameL(), EE.get(y).getEmpNumber(), EE.get(y).getVAmt(), EE.get(y).getBdayresos(), EE.get(y).getChangerEdit(), EE.get(y).getEmpID(), EE.get(y).getGProb(), EE.get(y).getActive(), EE.get(y).getArcade(), EE.get(y).getBcarsales());
            }
        }
        return empValid;
    }
    
    private void getStageV() {
        stageV = (Stage) printBtn.getScene().getWindow();
    }
    
    private void getEmpName() {
        emp = empNumberIn.getText().trim();
        if (emp.trim().isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Employee number Cannot be Blank");
            mbox.showAlert(Alert.AlertType.ERROR, stageV, "Stop", "Employee number Cannot be Blank");
            empNumberIn.setStyle("-fx-background-color: yellow");
            empNumberIn.requestFocus();
            return;
        }
        empName = "";
        if (!isEMPValidInArrayList(emp)){
        //if (eFX.isemployeevalid(emp) != true) {
            empNumberIn.setText("");
            mbox.showAlert(Alert.AlertType.ERROR, null, "Error", "Something went wrong try again. \nIf it continues please find a supervisor.");
            return;
        }
        empName = newEFX.getNameF() + " " + newEFX.getNameL();
        //empName = eFX.nameF + " " + eFX.nameL;
        if (empName.trim().isEmpty()) {
            return;
        } else {
            empNameLabel.setVisible(true);
            empNameLabel.setText(empName);
        }
    }
    
 
    public void exitButtonPushed() {
        db.disConnect();
        if (printBtn.isVisible()) {
            resetFields();
            eFX.Flush();
        } else {
            stageV.close();
        }
    } 


   
    
}
