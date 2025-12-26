
package views.timeclock;

import ComboBoxAutoComplete.ComboBoxAutoComplete_EmpDocs;
import JavaMail.Mail_JavaFX;
import JavaMail.Mail_JavaFX1;
import commoncodes.ClubFunctions;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.GetReceipts;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer.Form;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import jboxlist.FileBrowser;
import messageBox.messageBox;
import models.club.rCeipts;
import models.timeclock.EmpDocs;
import models.timeclock.EmpFileFX;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class EmpDocsViewerController implements Initializable {
    @FXML private TableView<EmpDocs> gamesTable;
    @FXML private TableColumn<EmpDocs, String> gameNameColumn;
    @FXML private TableColumn<EmpDocs, String> gameLocationColumn;
    @FXML private TableColumn<EmpDocs, String> gameDepartmentColumn;
    @FXML private TableColumn<EmpDocs, String> gameDateColumn;
    @FXML private TextField searchField;
    @FXML private DatePicker DatePicker;
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Button newButton;
    @FXML private Button saveButton;
    @FXML private Button cancelEditButton;
    @FXML private Button filePickerButton;
    @FXML private Button filePickerButtonStorage;
    @FXML private Button filePickerButtonStorage2;
    @FXML private Button filePickerButtonStorage3;
    @FXML private Button filePickerButtonStorage4;
    @FXML private Button filePickerButtonStorage5;
    @FXML private Button editButton;    
    @FXML private Label fLabel2;   
    @FXML private Label fLabel3;   
    @FXML private Label fLabel4;   
    @FXML private Label fLabel5;   
    @FXML private Label fLableLink;
    @FXML private TextField fKeyWords;    
    @FXML private TextField fLink;    
    @FXML private TextField fLinkStorage;
    @FXML private TextField fLinkStorage2;
    @FXML private TextField fLinkStorage3;
    @FXML private TextField fLinkStorage4;
    @FXML private TextField fLinkStorage5;
    @FXML private HBox editBox2;
    @FXML private Pane root;
    @FXML private GridPane gridPane;
    @FXML private ComboBox fTitleComboBox;
    @FXML private ComboBox fTitleComboBox2;
    @FXML private ComboBox fTitleComboBox3;
    @FXML private ComboBox fTitleComboBox4;
    @FXML private ComboBox fTitleComboBox5;
    
    private static SortedList<EmpDocs> sortedList = null;
    private static FilteredList<EmpDocs> filterData = null;
    private static final SceneChanger_Main SC = new SceneChanger_Main();
    private static final FileBrowser FILE_BROWSER = new FileBrowser();
    private static final DirectoryChooser FOLDER_BROWSER = new DirectoryChooser();
    //cssChanger cssC = new cssChanger();
    private static final dbStringPath DBSP = new dbStringPath();
    private static final Desktop DSK = Desktop.getDesktop();
    private static Connection conn = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    public static String cssPath, MGR, ID = "f", rNumber, path;
    public static ArrayList<rCeipts> Receipts;
    public static EmpFileFX Employee;
    public static Path empDocsPath;
    private static boolean editMode = false, isChanged = false;
    private static List <String> sourceFile = new ArrayList <> ();
    private static List <String> comboBoxs = new ArrayList <> ();
    private static final messageBox MBOX = new messageBox();

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBSP.setName();
        //root.getStylesheets().add(cssC.cssPath());
        root.getStylesheets().add(cssPath);
        editBox2.setVisible(false);
        editButton.setVisible(false);
        selectButton.setVisible(false);
        gameDateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        gameNameColumn.setCellValueFactory(new PropertyValueFactory<>("DocTitle"));
        gameLocationColumn.setCellValueFactory(new PropertyValueFactory<>("Search"));
        gameDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("Link"));
        //gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        FillREASONSComboBox();
        new FocusedTextFieldHighlight().setHighlightListenerBdays(gridPane);
        addTextfieldListeners();
        DatePicker.setValue(LocalDate.now());
        getTableItems();
        Platform.runLater(() -> {
            CheckDirectory();
            getTableItems();
            path = new GetReceipts().getReceipts(Receipts, "EmpDocsStorage");
        });
        Platform.runLater(() -> verifyFiles());
    }

    private void getTableItems() {
        try {
            fillList(1);
            //gamesTable.getItems().clear();
            //gamesTable.getItems().addAll(getGames());
        } catch (SQLException ex) {
            System.out.println(ex);
        }

    }

    private void addTextfieldListeners() {
        fLinkStorage.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (editMode) {
                        if (newValue == null ? oldValue != null : !newValue.equals(oldValue)) {
                            isChanged = true;
                            //System.out.println("oldVal " + oldValue + " newVal " + newValue + " isChanged " + isChanged);
                        }
                    }
                });
    }

    public void fillList(int a) throws SQLException {

        switch (a) {
            case 1:
                filterData = new FilteredList<>(getGames(), p -> true);
                break;
            case 2:
                filterData = new FilteredList<>(gamesTable.getItems(), p -> true);
                searchField.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
                    filterData.setPredicate(pers -> {
                        if (newvalue == null || newvalue.isEmpty()) {
                            return true;
                        }
                        String typedText = newvalue.toLowerCase();
                        if (pers.getDocTitle().toLowerCase().contains(typedText)) {
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

    public void fireOpenLocationSource(ActionEvent event) {
        openFileLocationSource(event);
    }

    public void openFileLocationSource(ActionEvent event) {
        //System.out.println(((Button)event.getSource()).getText());
        String buttonName = ((Button) event.getSource()).getText();
        if (buttonName.equals("Get File 1")) {
            String oldVal = fLinkStorage.getText().trim();
            FILE_BROWSER.openFileLocation(fLinkStorage, path);
            String newVal = fLinkStorage.getText().trim();
            if (!oldVal.equals(newVal)) {
                isChanged = true;
            }
            System.out.println("newVal " + newVal + " oldVal " + oldVal + " isChanged" + isChanged);
        }
        if (buttonName.equals("Get File 2")) {
            FILE_BROWSER.openFileLocation(fLinkStorage2, path);
        }
        if (buttonName.equals("Get File 3")) {
            FILE_BROWSER.openFileLocation(fLinkStorage3, path);
        }
        if (buttonName.equals("Get File 4")) {
            FILE_BROWSER.openFileLocation(fLinkStorage4, path);
        }
        if (buttonName.equals("Get File 5")) {
            FILE_BROWSER.openFileLocation(fLinkStorage5, path);
        }
    }

    public void openFileLocationSaveTo() {
        String docsPath;
        int alert = MBOX.confirmMakeThisChange(Alert.AlertType.ERROR, null, "Confirm!", "Are you sure you need to change this folder to save the documents for this employee?");
        if (alert == 1) {
            if (fLink.getText().isEmpty()) {
                docsPath = new GetReceipts().getReceipts(Receipts, "EmpDocs");
            } else {
                docsPath = fLink.getText();
            }
            FOLDER_BROWSER.setInitialDirectory(new File(docsPath));
            File selectedDirectory = FOLDER_BROWSER.showDialog(null);
            //System.out.println(docsPath);
            fLink.setText(selectedDirectory.toString());
        }
    }

    public void selectButtonPushed(ActionEvent event) throws IOException {
        DBSP.setName();
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
                DSK.open(new File(str));
            } catch (IOException ex) {
                MBOX.showAlert(Alert.AlertType.ERROR, null, "Error", "This video was not found");
            }
        } else {
            MBOX.showAlert(Alert.AlertType.ERROR, null, "Error", "This video was not found" + temp.toString());
        }
    }

    private void FillREASONSComboBox() {
        List<String> myList;
        try {
            myList = Files.lines(Paths.get(DBSP.pathNameTclock + "DocsReasons.txt")).collect(Collectors.toList());
            String[] cmbList = new String[myList.size()];
            for (int i = 0; i < myList.size(); i++) {
                cmbList[i] = myList.get(i) + "\n";
            }
            ComboBox<String> cmb = fTitleComboBox;
            cmb.getItems().clear();
            cmb.setTooltip(new Tooltip());
            cmb.getItems().addAll(cmbList);
            new ComboBoxAutoComplete_EmpDocs<String>(cmb);
            ComboBox<String> cmb2 = fTitleComboBox2;
            cmb2.getItems().clear();
            cmb2.setTooltip(new Tooltip());
            cmb2.getItems().addAll(cmbList);
            new ComboBoxAutoComplete_EmpDocs<String>(cmb2);
            ComboBox<String> cmb3 = fTitleComboBox3;
            cmb3.getItems().clear();
            cmb3.setTooltip(new Tooltip());
            cmb3.getItems().addAll(cmbList);
            new ComboBoxAutoComplete_EmpDocs<String>(cmb3);
            ComboBox<String> cmb4 = fTitleComboBox4;
            cmb4.getItems().clear();
            cmb4.setTooltip(new Tooltip());
            cmb4.getItems().addAll(cmbList);
            new ComboBoxAutoComplete_EmpDocs<String>(cmb4);
            ComboBox<String> cmb5 = fTitleComboBox5;
            cmb5.getItems().clear();
            cmb5.setTooltip(new Tooltip());
            cmb5.getItems().addAll(cmbList);
            new ComboBoxAutoComplete_EmpDocs<String>(cmb5);
        } catch (IOException e) {
            System.out.println("Can't find Depart.txt file" + e);
        }

    }

    private void CheckDirectory() {
        //System.out.println(empDocsPath.toString());
        try {
            Path CHKpath = Paths.get(empDocsPath.toString());
            if (!Files.exists(CHKpath)) {
                //Path path = Paths.get(new GetReceipts().getReceipts(Receipts, "EmpDocs") + "\\" + E.getFName() + E.getEid());
                Files.createDirectories(CHKpath);
                //System.out.println("Directory is created!");
            } else {
                //System.out.println("Directory Exist! " + empDocsPath.toString());
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
            new Mail_JavaFX1().sendEmailTo("ERROR EMP Viewer", "error in Emp New Employee CreateDirectory (See EmpViewerController) ", "errors");
        }
    }

    public void newButtonGo() {
        editBox2.setVisible(true);
        fKeyWords.requestFocus();
        newButton.setVisible(false);
        editButton.setVisible(false);
        Platform.runLater(() -> {
            fLink.setText(empDocsPath.toString());
        });
    }

    public void editButtonGo() {
        System.out.println("here we are " + ID);
        TablePosition pos = gamesTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        ID = gamesTable.getItems().get(row).getId();
        //gridPane.setVisible(true);
        editBox2.setVisible(true);
        setEditFeilds();
        DatePicker.setValue(gamesTable.getItems().get(row).getDate().toLocalDate());
        fTitleComboBox.setValue(gamesTable.getItems().get(row).getDocTitle());
        fKeyWords.setText(gamesTable.getItems().get(row).getSearch());
        fLinkStorage.setText(gamesTable.getItems().get(row).getLink());
        fLink.setText(empDocsPath.toString());
        newButton.setVisible(false);
        editButton.setVisible(false);
        System.out.println("here we are " + ID);
        fKeyWords.requestFocus();

    }

    public void saveButtonGo() {

//save button should not light up till a file has been selected.
//we should confirm we have the files moved bfore deleting
//there should be a check on the opening of docs that check sall the files listed have a doc in the doc folder. or run this duering the week
        
        //System.out.println("SAVE BUTTON GO - - - - - - -");
        
        buildArrayLists();
        if (CheckforDuplicates()) {
            //fKeyWords.requestFocus();
            clearArrays();
            MBOX.showAlert(Alert.AlertType.ERROR, null, "Stop", "You have a duplicate file in one of the feilds \n Please check the feilds for Duplicates.");
            return;
        }
        //new ClubFunctions().clearApostophies(gridPane);
        for (int i = 0; i < sourceFile.size(); i++) {
            //System.out.println("Size fo sourceFile " + sourceFile.size() + " " + i);
            String fileName = sourceFile.get(i);
            //System.out.println("Size of fileName " + fileName);
            File theFile = new File(fileName);
            //System.out.println("Size of theFile " + theFile);
            String newFileName = fLink.getText() + "\\" + theFile.getName();
            Path targetPath = Paths.get(fileName);
            //System.out.println("going into move files and this is the tagert path going into the Model: " + targetPath);

            if (ID.equals("f")) {
                //new record
                EmpDocs newGame2 = new EmpDocs(comboBoxs.get(i), fKeyWords.getText(), newFileName, Date.valueOf(DatePicker.getValue().toString()), fLink.getText(), Employee.getEid(), conn, targetPath, isChanged);
                if (!newGame2.InsertHelpVideo()) {

                }
            } else {
                EmpDocs newGame2 = new EmpDocs(comboBoxs.get(i), fKeyWords.getText(), newFileName, Date.valueOf(DatePicker.getValue().toString()), ID, Employee.getEid(), conn, targetPath, isChanged);
                //update record
                try { 
                    newGame2.UpdateHelpVideo();
                } catch (SQLException ex) {
                    System.out.println("error");
                    System.out.println(ex);
                }
            }
        }

        //editBox2.setVisible(false);
        clearSearchField();
        verifyFiles();
        setEditFeildsTrue();
        fKeyWords.setText("");
        clearFeilds();
        resetfLinkSaveFileToPath();
        isChanged = false;
        ID = "f";
        comboBoxs.clear();
        sourceFile.clear();
        editBox2.setVisible(false);
        newButton.setVisible(true);
        getTableItems();
        searchField.requestFocus();

    }

    private void verifyFiles() {
        ObservableList<EmpDocs> empDocItems = gamesTable.getItems();
        for (int i = 0; i < empDocItems.size(); i++) {
            //System.out.println(empDocItems.get(i).getLink());
            File file = new File(empDocItems.get(i).getLink());
            if (file.exists() && !file.isDirectory()) {
                //System.out.println("File does exist " + file.toString());
            } else {
                //System.out.println("File does ((NOT)) exist " + file.toString());
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Vefified", "File missing from Folder " + file.toString());
                new Mail_JavaFX().SendMail2("Errror in EmpDocs", "File missing from Folder " + file.toString(), "error");
            }
        }
    }

    
    private void buildArrayLists() {
        if (fLinkStorage.getText().trim().length() > 1) {
            sourceFile.add(fLinkStorage.getText());
            try {
                //if (!fTitleComboBox.getValue().toString().trim().isEmpty()){ 
                    comboBoxs.add(fTitleComboBox.getValue().toString().trim());
                //} else {
                //    MBOX.showAlert(Alert.AlertType.ERROR, null, "Error", "You have not selected a Cod Type.");
                //    fTitleComboBox.requestFocus();
                //    return;
                //}
            } catch (Exception e) {
                fTitleComboBox.requestFocus();
                fTitleComboBox.show();
                clearArrays();
                //MBOX.showAlert(Alert.AlertType.ERROR, null, "error", "the Document type is blank");
                return;
            }
        } else {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "error", "The First File selection feild cannot be empty \n Please choose a file");
            return;
        }

        if (fLinkStorage2.getText().trim().length() > 1) {
            sourceFile.add(fLinkStorage2.getText());
            try {
                //System.out.println("HERE IS THE FtITLEcOMBObOX2 " + fTitleComboBox2.getValue().toString().trim());
                comboBoxs.add(fTitleComboBox2.getValue().toString().trim());
            } catch (Exception e) {
                //new messageBox().showAlert(Alert.AlertType.ERROR, null, "error", "the Document type is blank");
                fTitleComboBox2.requestFocus();
                fTitleComboBox2.show();
                clearArrays();
                return;
            }
        }
        if (fLinkStorage3.getText().trim().length() > 1) {
            sourceFile.add(fLinkStorage3.getText());
            try {
                comboBoxs.add(fTitleComboBox3.getValue().toString().trim());
            } catch (Exception e) {
                //new messageBox().showAlert(Alert.AlertType.ERROR, null, "error", "the Document type is blank");
                fTitleComboBox3.requestFocus();
                fTitleComboBox3.show();
                clearArrays();
                return;
            }
        }
        if (fLinkStorage4.getText().trim().length() > 1) {
            sourceFile.add(fLinkStorage4.getText());
            try {
                comboBoxs.add(fTitleComboBox4.getValue().toString().trim());
            } catch (Exception e) {
                //new messageBox().showAlert(Alert.AlertType.ERROR, null, "error", "the Document type is blank");
                fTitleComboBox4.requestFocus();
                fTitleComboBox4.show();
                clearArrays();
                return;
            }
        }
        if (fLinkStorage5.getText().trim().length() > 1) {
            sourceFile.add(fLinkStorage5.getText());
            try {
                comboBoxs.add(fTitleComboBox5.getValue().toString().trim());
            } catch (Exception e) {
                //new messageBox().showAlert(Alert.AlertType.ERROR, null, "error", "the Document type is blank");
                fTitleComboBox5.requestFocus();
                fTitleComboBox5.show();
                clearArrays();
                return;
            }
        }
    }
    
    
    public void TableClicked(MouseEvent me) throws IOException {
        selectButton.setVisible(true);
        editButton.setVisible(true);
        newButton.setVisible(false);
        //editButtonGo();
         if (me.getClickCount() == 2) {
            selectButton.fire();
         }
         
    }
    
    
    private boolean CheckforDuplicates() {
        boolean dups = false;
        int i = 0;
        Set<String> uniqueSet = new HashSet<>();
        List<String> dupesList = new ArrayList<>();
        for (String a : sourceFile) {
            if (uniqueSet.contains(a)) {
                dupesList.add(a);
                //System.out.println(" here is the a " + a);
                dups = true;
            } else {
                uniqueSet.add(a);
            }
            i++;
        }
            getBackToFeild(i);
            //System.out.println(uniqueSet.size() + " distinct words: " + uniqueSet);
            //System.out.println(dupesList.size() + " dupesList words: " + dupesList);
            return dups;
    }
    
    
    private void getBackToFeild(int i) {
        if (i==1){
            fLinkStorage.requestFocus();
        }
        if (i==2){
            fLinkStorage2.requestFocus();
        }
        if (i==3){
            fLinkStorage3.requestFocus();
        }
        if (i==4){
            fLinkStorage4.requestFocus();
        }
        if (i==5){
            fLinkStorage5.requestFocus();
        }
    }
    
    private void setEditFeilds() {
            fLinkStorage2.setVisible(false);
            fLinkStorage3.setVisible(false);
            fLinkStorage4.setVisible(false);
            fLinkStorage5.setVisible(false);
            fLink.setVisible(false);
            filePickerButton.setVisible(false);
            fTitleComboBox2.setVisible(false);
            fTitleComboBox3.setVisible(false);
            fTitleComboBox4.setVisible(false);
            fTitleComboBox5.setVisible(false);
            filePickerButtonStorage2.setVisible(false);
            filePickerButtonStorage3.setVisible(false);
            filePickerButtonStorage4.setVisible(false);
            filePickerButtonStorage5.setVisible(false);
            fLabel2.setVisible(false);
            fLabel3.setVisible(false);
            fLabel4.setVisible(false);
            fLabel5.setVisible(false);
            fLableLink.setVisible(false);
    }
    
     private void setEditFeildsTrue() {
            fLinkStorage2.setVisible(true);
            fLinkStorage3.setVisible(true);
            fLinkStorage4.setVisible(true);
            fLinkStorage5.setVisible(true);
            fLink.setVisible(true);
            filePickerButton.setVisible(true);
            fTitleComboBox2.setVisible(true);
            fTitleComboBox3.setVisible(true);
            fTitleComboBox4.setVisible(true);
            fTitleComboBox5.setVisible(true);
            filePickerButtonStorage2.setVisible(true);
            filePickerButtonStorage3.setVisible(true);
            filePickerButtonStorage4.setVisible(true);
            filePickerButtonStorage5.setVisible(true);
            fLabel2.setVisible(true);
            fLabel3.setVisible(true);
            fLabel4.setVisible(true);
            fLabel5.setVisible(true);
            fLableLink.setVisible(true);
    }
     
     
    
    public ObservableList<EmpDocs> getGames() throws SQLException {
    ObservableList<EmpDocs> games = FXCollections.observableArrayList();
    games.clear();
    EmpDocs newGame = null;
    try {
            //System.out.println(Employee.getEid());
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ DBSP.pathNameClubDBs + "EmpDocs.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
         rs = st.executeQuery("SELECT EmpDocs.*\n"
                + "FROM EmpDocs\n"
                + "WHERE (((EmpDocs.EmployeeID)='" + Employee.getEid() + "'))\n"
                + "ORDER BY EmpDocs.Date;");
           //Employee.getID();
            while (rs.next()) {
                newGame = new EmpDocs( rs.getString("Video Title"), rs.getString("Search Data"), rs.getString("Link"), rs.getDate("Date"), rs.getString("ID"), rs.getString("EmployeeID"), conn, null, false);
                games.add(newGame);
            }
    }
    catch (SQLException e) {
        System.err.println("ERROR in get Empdocs " + e.getMessage());
    }
    finally {
    //if (rs != null);
    //    rs.close();
    //if (st != null);
    //    st.close();
    if (conn != null);
    //    conn.close();
    }
    return games;
}   
    

      
    
    

   /* public void cancelEditButtonGo(ActionEvent event) throws IOException {
        //fTitle.setText("");
        fKeyWords.setText("");
        fLink.setText("");
        ID = "f";
        clearFeilds();
        sourceFile.clear();
        comboBoxs.clear();
        searchField.requestFocus();
        editBox2.setVisible(false);

    }*/

    public void cancelButtonPushed(ActionEvent event) throws IOException {
        if (editBox2.isVisible()) {
            editBox2.setVisible(false);
            editMode = false;
            newButton.setVisible(true);
            //editButton.setVisible(true);
            fKeyWords.setText("");
            isChanged = false;
            setEditFeildsTrue();
            resetfLinkSaveFileToPath();
            ID = "f";
            clearFeilds();
            clearArrays();
            clearArrays();
            getTableItems();
            searchField.requestFocus();
            return;
        }
        if (editButton.isVisible()) {
            editButton.setVisible(false);
            getTableItems();
            searchField.requestFocus();
            return;
        }
        if (searchField.getText().trim().length() > 0) {
            clearSearchField();
            ID = "";
        } else {

            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            SC.changePopUp(event, "", "");
        }
    }
    
    private void resetfLinkSaveFileToPath() {
        fLink.setText(empDocsPath.toString());
        
    }
    
    private void clearArrays() {
        comboBoxs.clear();
        sourceFile.clear();
    }
    
    
    private void clearFeilds() {
        fLinkStorage.setText("");
        fLinkStorage2.setText("");
        fLinkStorage3.setText("");
        fLinkStorage4.setText("");
        fLinkStorage5.setText("");
        fTitleComboBox.setValue("");
        fTitleComboBox2.setValue("");
        fTitleComboBox3.setValue("");
        fTitleComboBox4.setValue("");
        fTitleComboBox5.setValue("");
    }
    

    private void clearSearchField() {
        searchField.clear();
        searchField.requestFocus();
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
            case SPACE:
                selectButton.fire();
                break;
            case ESCAPE:
                cancelButton.fire();
                break;
            case ENTER:
                EnterKeyPressed();
                break;
            case TAB:
                EnterKeyPressed();
                break;
            default:
                break;
        }
    }

    private void EnterKeyPressed() {
        if (DatePicker.isFocused()) {
            fKeyWords.requestFocus();
            return;
        }
        if (fKeyWords.isFocused()) {
            fTitleComboBox.requestFocus();
            fTitleComboBox.show();
            return;
        }
        if (fTitleComboBox.isFocused()) {
            fLinkStorage.requestFocus();
            filePickerButtonStorage.fire();
            return;
        }
        if (fLinkStorage.isFocused()) {
            fTitleComboBox2.requestFocus();
            fTitleComboBox2.show();
            return;
        }
        if (fTitleComboBox2.isFocused()) {
            fLinkStorage2.requestFocus();
            filePickerButtonStorage2.fire();
            return;
        }
        if (fLinkStorage2.isFocused()) {
            fTitleComboBox3.requestFocus();
            fTitleComboBox3.show();
            return;
        }
        if (fTitleComboBox3.isFocused()) {
            fLinkStorage3.requestFocus();
            filePickerButtonStorage3.fire();
            return;
        }
        if (fLinkStorage3.isFocused()) {
            fTitleComboBox4.requestFocus();
            fTitleComboBox4.show();
            return;
        }
        if (fTitleComboBox4.isFocused()) {
            fLinkStorage4.requestFocus();
            filePickerButtonStorage4.fire();
            return;
        }
        if (fLinkStorage4.isFocused()) {
            fTitleComboBox5.requestFocus();
            fTitleComboBox5.show();
            return;
        }
        if (fTitleComboBox5.isFocused()) {
            fLinkStorage5.requestFocus();
            filePickerButtonStorage5.fire();
            return;
        }
        if (fLinkStorage5.isFocused()) {
            fKeyWords.requestFocus();
            return;
        }
        
    }

}
