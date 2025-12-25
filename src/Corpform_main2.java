


import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
//import javafx.beans.value.ObservableValue;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import net.proteanit.sql.DbUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import models.corp.CurrencyCellRenderer1;
import models.corp.DateCellRenderer;
import pWordFX.employeeFX;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JONAH
 */
public class Corpform_main2 extends javax.swing.JFrame implements ChangeListener, KeyListener {
        private static SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyy");
        private static tpdreports g = new tpdreports();
        private static dbStringPath sgpth = new dbStringPath();
        private static GetDay gd = new GetDay();
        ///private static employeeFX e = new employeeFX();
        private static ArrayList<String> list = new ArrayList<>();
        private static Connection conn=null;
        private static final String PATHCORP = "//POJOS-NET-DRIVE\\clubdb\\Corp\\";
        private static final String PATHCORP2 = "//POJOS-NET-DRIVE\\clubdb\\Corp\\Corp2\\";
    /**
     * Creates new form firstForm
     */
    public Corpform_main2() {
        //this.setUndecorated(true);
        initComponents();
        init();
        
    }
    
    
    public void init() {
        //JOptionPane.showMessageDialog(null, "1 " + PATHCORP + "Corp_access.accdb ");
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xsize;
        xsize = (int) tk.getScreenSize().getWidth();
        int ysize;
        ysize = (int) tk.getScreenSize().getHeight();
        this.setSize(xsize, ysize);
        sgpth.setName();
        ImageIcon img = new ImageIcon(sgpth.pathNameImages + "CorpOfficeBackGround/icon.png");
        this.setIconImage(img.getImage());
        depositReportsMenu.setVisible(true);
        depositDropEditMenu.setVisible(true);
        System.out.println("========================================================1");
        Date nDate = new Date(System.currentTimeMillis()-24*60*60*1000);
        Date date_s = nDate;
        Date date = date_s;
        dt4.setDate(date);
        dt5.setDate(date);
        dt6.setDate(date);

//Init the changersPane
        try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + PATHCORP + "Corp_access.accdb");
            Statement s = conn.createStatement();
//delete one year and more ago
            if (gd.getday().equals("Tue") || gd.getday().equals("Thu")) {
                String sqldel = "DELETE * FROM Changers WHERE (((Changers.DDate)<DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 60 DAY)))";
                s.executeUpdate(sqldel);
            }
            ResultSet rs = s.executeQuery("SELECT * FROM [Changers]");
            //while (rs.next()) {
                changerTable.setModel(DbUtils.resultSetToTableModel(rs));
            //}
            //conn.close();
            s.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "1 " + PATHCORP + "Corp_access.accdb " + e);
        }
        //String emp = getEmployeeNumberFromTxtFile();
        list = get_arraylist_from_file();
    //JOptionPane.showMessageDialog(null, "1 " + PATHCORP + "Corp_access.accdb " + list.get(0));
        if (!list.get(0).trim().isEmpty()) {
            changersmgr.setText(list.get(0));
            changersmgr.setBackground(Color.LIGHT_GRAY);
            pdoutsmgr.setText(list.get(0));
            pdoutsmgr.setBackground(Color.LIGHT_GRAY);
            salesmgr.setText(list.get(0));
            salesmgr.setBackground(Color.LIGHT_GRAY);
        } else {
            System.out.println("there was not emplyee file " + list.get(0));
            JOptionPane.showMessageDialog(null, "1 " + PATHCORP  + list.get(0));
        }
        
//END Init Changers
//Init pdouts
     try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
//delete one year and more ago
            //JOptionPane.showMessageDialog(null, dt1.format(date));
            if (gd.getday().equals("Tue") || gd.getday().equals("Thu")) {
                String sqldel = "DELETE * FROM Sales WHERE (((Sales.Sales_Date)<DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 370 DAY)))";
                s.executeUpdate(sqldel);
            }
            //JOptionPane.showMessageDialog(null, dt1.format(date));
            ResultSet rs = s.executeQuery("SELECT Sales.*\n" +
                                          "FROM Sales ORDER BY Sales.Sales_Date;");
            //while (rs.next()) {
                salesTable.setModel(DbUtils.resultSetToTableModel(rs));
            //}
            rs.close();
            //conn.close();
            s.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
//delete one year and more ago
            if (gd.getday().equals("Tue") || gd.getday().equals("Thu")) {
                String sqldel = "DELETE * FROM pDouts WHERE (((pDouts.Sales_Date)<DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 370 DAY)))";
                s.executeUpdate(sqldel);
            }
            ResultSet rs = s.executeQuery("SELECT * FROM [pDouts]");
            //while (rs.next()) {
                pdoutsTable.setModel(DbUtils.resultSetToTableModel(rs));
            //}
            rs.close();
            //conn.close();
            s.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
      
        pdoutsTableRefresh();
        changerTableRefresh();
        salesrefreshJboxes();
        salesresetfields();
        salesTableRefresh();
        salespDouts_1.grabFocus();
   
    //changerresetfields();
    //atm.grabFocus();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        basePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        depositsPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        salesxitbutton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        salesaddbutton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        salesmgr = new javax.swing.JPasswordField();
        dt6 = new com.toedter.calendar.JDateChooser();
        salespDouts_1 = new javax.swing.JComboBox<>();
        salespDouts_2 = new javax.swing.JComboBox<>();
        salespDouts_3 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        salesch12 = new javax.swing.JFormattedTextField();
        salesdelbutton = new javax.swing.JButton();
        paidoutsPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        pdoutsxitbutton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        pdoutsTable = new javax.swing.JTable();
        pdoutsaddbutton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        pdoutsch10 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        pdoutsmgr = new javax.swing.JPasswordField();
        dt5 = new com.toedter.calendar.JDateChooser();
        pDouts_1 = new javax.swing.JComboBox<>();
        pDouts_2 = new javax.swing.JComboBox<>();
        pDouts_3 = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        pdoutsch13 = new javax.swing.JFormattedTextField();
        pdoutsdelbutton = new javax.swing.JButton();
        changersPanel = new javax.swing.JPanel();
        changersPanelBasePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        changerTable = new javax.swing.JTable();
        changersFieldPane = new javax.swing.JPanel();
        bcars = new javax.swing.JTextField();
        ch9 = new javax.swing.JTextField();
        atm = new javax.swing.JTextField();
        ch14 = new javax.swing.JTextField();
        ch12 = new javax.swing.JTextField();
        ch7 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        changersmgr = new javax.swing.JPasswordField();
        dt4 = new com.toedter.calendar.JDateChooser();
        ch3 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        ch4 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        ch5 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        ch2 = new javax.swing.JTextField();
        ch1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        ch8 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        changerdelbtn = new javax.swing.JButton();
        report = new javax.swing.JButton();
        changeraddbtn = new javax.swing.JButton();
        changerx_itbutton = new javax.swing.JButton();
        report1 = new javax.swing.JButton();
        report2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        depositReportsMenu = new javax.swing.JMenu();
        salesyestday = new javax.swing.JMenuItem();
        salessevenday = new javax.swing.JMenuItem();
        salesthirtydays = new javax.swing.JMenuItem();
        salesthirtyonedays = new javax.swing.JMenuItem();
        salesmonthToDaterpt = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jimsfavdoneday = new javax.swing.JMenuItem();
        jimsfav7days = new javax.swing.JMenuItem();
        monthToDateJimsrpt = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        depositDropEditMenu = new javax.swing.JMenu();
        menu_dropedit_deprt = new javax.swing.JMenuItem();
        menu_dropedit_Dsales_Type = new javax.swing.JMenuItem();
        salesjMenuItem1 = new javax.swing.JMenuItem();
        salesjMenuItem2 = new javax.swing.JMenuItem();
        pdOutReportsMenu = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        monthToDaterpt1 = new javax.swing.JMenuItem();
        pdDropEditMenu = new javax.swing.JMenu();
        menu_dropedit_D_Type1 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Corp Data Main");
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setExtendedState(getExtendedState());
        setSize(new java.awt.Dimension(600, 400));

        basePanel.setBackground(new java.awt.Color(153, 51, 0));
        basePanel.setMinimumSize(new java.awt.Dimension(600, 400));
        basePanel.setPreferredSize(new java.awt.Dimension(600, 4));

        jTabbedPane1.setToolTipText("CTRL + PGUP (or PGDN) switches pages");
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(153, 51, 0));
        jPanel3.setPreferredSize(new java.awt.Dimension(800, 1159));

        salesxitbutton.setText("Exit");
        salesxitbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesxitbuttonActionPerformed(evt);
            }
        });

        salesTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        salesTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        salesTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        salesTable.setGridColor(new java.awt.Color(204, 0, 0));
        salesTable.setRowHeight(36);
        salesTable.setRowMargin(3);
        salesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salesTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(salesTable);

        salesaddbutton.setText("Add");
        salesaddbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesaddbuttonActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), new java.awt.Color(204, 51, 0)));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Date");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setText("Department");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setText("Amount");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("MGR #");

        salesmgr.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        salesmgr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salesmgrFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                salesmgrFocusLost(evt);
            }
        });
        salesmgr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                salesmgrKeyPressed(evt);
            }
        });

        dt6.setDateFormatString("MM/dd/yyyy");
        dt6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dt6KeyPressed(evt);
            }
        });

        salespDouts_1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salespDouts_1FocusGained(evt);
            }
        });
        salespDouts_1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                salespDouts_1KeyPressed(evt);
            }
        });

        salespDouts_2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salespDouts_2FocusGained(evt);
            }
        });
        salespDouts_2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                salespDouts_2KeyPressed(evt);
            }
        });

        salespDouts_3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salespDouts_3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                salespDouts_3FocusLost(evt);
            }
        });
        salespDouts_3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                salespDouts_3KeyPressed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setText("Item Name");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setText("Type");

        salesch12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        salesch12.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        salesch12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        salesch12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                salesch12KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(dt6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(salespDouts_1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salespDouts_2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(107, 107, 107)
                        .addComponent(jLabel19)
                        .addGap(56, 56, 56)
                        .addComponent(jLabel20))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(salespDouts_3, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesch12, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(salesmgr, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dt6, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                    .addComponent(salespDouts_1)
                    .addComponent(salespDouts_2)
                    .addComponent(salespDouts_3)
                    .addComponent(salesmgr)
                    .addComponent(salesch12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        salesdelbutton.setText("Delete");
        salesdelbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesdelbuttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(salesaddbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(salesxitbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(salesdelbutton)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1252, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 273, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {salesaddbutton, salesdelbutton, salesxitbutton});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(salesaddbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesxitbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesdelbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(215, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout depositsPanelLayout = new javax.swing.GroupLayout(depositsPanel);
        depositsPanel.setLayout(depositsPanelLayout);
        depositsPanelLayout.setHorizontalGroup(
            depositsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1536, Short.MAX_VALUE)
        );
        depositsPanelLayout.setVerticalGroup(
            depositsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(" Deposits ", depositsPanel);

        jPanel2.setBackground(new java.awt.Color(153, 51, 0));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 1159));

        pdoutsxitbutton.setText("Exit");
        pdoutsxitbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdoutsxitbuttonActionPerformed(evt);
            }
        });

        pdoutsTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pdoutsTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pdoutsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        pdoutsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        pdoutsTable.setGridColor(new java.awt.Color(204, 0, 0));
        pdoutsTable.setRowHeight(36);
        pdoutsTable.setRowMargin(3);
        pdoutsTable.getTableHeader().setReorderingAllowed(false);
        pdoutsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pdoutsTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(pdoutsTable);

        pdoutsaddbutton.setText("Add");
        pdoutsaddbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdoutsaddbuttonActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), new java.awt.Color(204, 51, 0)));

        pdoutsch10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pdoutsch10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pdoutsch10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdoutsch10ActionPerformed(evt);
            }
        });
        pdoutsch10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pdoutsch10KeyPressed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Date");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Department");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Memo");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Amount");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("MGR #");

        pdoutsmgr.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pdoutsmgr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pdoutsmgrKeyPressed(evt);
            }
        });

        dt5.setDateFormatString("MM/dd/yyyy");
        dt5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dt5KeyPressed(evt);
            }
        });

        pDouts_1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pDouts_1FocusGained(evt);
            }
        });
        pDouts_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pDouts_1ActionPerformed(evt);
            }
        });
        pDouts_1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pDouts_1KeyPressed(evt);
            }
        });

        pDouts_2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pDouts_2FocusGained(evt);
            }
        });
        pDouts_2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pDouts_2KeyPressed(evt);
            }
        });

        pDouts_3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pDouts_3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                pDouts_3FocusLost(evt);
            }
        });
        pDouts_3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pDouts_3KeyPressed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Item Name");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("Type");

        pdoutsch13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pdoutsch13.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        pdoutsch13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pdoutsch13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pdoutsch13KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(dt5, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(pDouts_1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pDouts_2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pDouts_3, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(pdoutsch10, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(132, 132, 132)
                        .addComponent(jLabel14))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pdoutsch13, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pdoutsmgr, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dt5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pdoutsmgr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pDouts_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pDouts_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pdoutsch10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pDouts_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pdoutsch13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel15)
                                .addComponent(jLabel12)
                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap()))
                    .addComponent(jLabel10)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {dt5, pDouts_1, pDouts_2, pDouts_3, pdoutsch10, pdoutsch13, pdoutsmgr});

        pdoutsdelbutton.setText("Delete");
        pdoutsdelbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdoutsdelbuttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pdoutsdelbutton, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                            .addComponent(pdoutsaddbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pdoutsxitbutton, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))))
                .addContainerGap(237, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(pdoutsaddbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pdoutsxitbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pdoutsdelbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(234, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout paidoutsPanelLayout = new javax.swing.GroupLayout(paidoutsPanel);
        paidoutsPanel.setLayout(paidoutsPanelLayout);
        paidoutsPanelLayout.setHorizontalGroup(
            paidoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1536, Short.MAX_VALUE)
        );
        paidoutsPanelLayout.setVerticalGroup(
            paidoutsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(" Paid Outs ", paidoutsPanel);

        changersPanelBasePanel.setBackground(new java.awt.Color(153, 51, 0));
        changersPanelBasePanel.setPreferredSize(new java.awt.Dimension(800, 1159));

        changerTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        changerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        changerTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        changerTable.setGridColor(new java.awt.Color(204, 0, 0));
        changerTable.setRowHeight(36);
        changerTable.setRowMargin(3);
        changerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                changerTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(changerTable);

        changersFieldPane.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 204, 204), new java.awt.Color(204, 51, 0)));

        bcars.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        bcars.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bcars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcarsActionPerformed(evt);
            }
        });
        bcars.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bcarsKeyPressed(evt);
            }
        });

        ch9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ch9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ch9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ch9KeyPressed(evt);
            }
        });

        atm.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        atm.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        atm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                atmKeyPressed(evt);
            }
        });

        ch14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ch14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ch14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ch14KeyPressed(evt);
            }
        });

        ch12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ch12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ch12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ch12KeyPressed(evt);
            }
        });

        ch7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ch7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ch7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ch7KeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Date");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("ATM");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Bcars");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Chgr 8");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Chgr 9");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Chgr 12");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Chgr 14");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("MGR #");

        changersmgr.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        changersmgr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                changersmgrKeyPressed(evt);
            }
        });

        dt4.setDateFormatString("MM/dd/yyyy");
        dt4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dt4KeyPressed(evt);
            }
        });

        ch3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ch3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ch3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ch3KeyPressed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setText("Chgr 3");

        ch4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ch4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ch4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ch4KeyPressed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel24.setText("Chgr 4");

        ch5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ch5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ch5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ch5ActionPerformed(evt);
            }
        });
        ch5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ch5KeyPressed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setText("Chgr 5");

        ch2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ch2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ch2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ch2KeyPressed(evt);
            }
        });

        ch1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ch1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ch1KeyPressed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setText("Chgr 1");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel27.setText("Chgr 2");

        ch8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ch8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ch8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ch8KeyPressed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel28.setText("Chgr 7");

        javax.swing.GroupLayout changersFieldPaneLayout = new javax.swing.GroupLayout(changersFieldPane);
        changersFieldPane.setLayout(changersFieldPaneLayout);
        changersFieldPaneLayout.setHorizontalGroup(
            changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changersFieldPaneLayout.createSequentialGroup()
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(changersFieldPaneLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(changersFieldPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(dt4, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(atm, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(changersFieldPaneLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(changersFieldPaneLayout.createSequentialGroup()
                        .addComponent(bcars, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ch1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ch2)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ch3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(ch4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(ch5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addComponent(ch7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(changersFieldPaneLayout.createSequentialGroup()
                        .addComponent(ch8, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(ch9, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(ch12, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(ch14, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(99, 99, 99)
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(changersmgr, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap())
        );

        changersFieldPaneLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {atm, bcars, ch1});

        changersFieldPaneLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ch7, ch8});

        changersFieldPaneLayout.setVerticalGroup(
            changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changersFieldPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(changersFieldPaneLayout.createSequentialGroup()
                        .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ch7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ch9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ch12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ch14, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(changersmgr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ch5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ch4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ch3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ch2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ch8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(changersFieldPaneLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel9)
                                .addGap(108, 108, 108))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, changersFieldPaneLayout.createSequentialGroup()
                                .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(179, 179, 179))))
                    .addGroup(changersFieldPaneLayout.createSequentialGroup()
                        .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(atm, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bcars, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ch1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(dt4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(changersFieldPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jLabel26)
                                .addComponent(jLabel27)
                                .addComponent(jLabel23)
                                .addComponent(jLabel24)
                                .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        changersFieldPaneLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ch14, changersmgr});

        changerdelbtn.setText("Delete");
        changerdelbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changerdelbtnActionPerformed(evt);
            }
        });

        report.setText("7 Day Rpt");
        report.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportActionPerformed(evt);
            }
        });

        changeraddbtn.setText("Add");
        changeraddbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeraddbtnActionPerformed(evt);
            }
        });

        changerx_itbutton.setText("Exit");
        changerx_itbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changerx_itbuttonActionPerformed(evt);
            }
        });

        report1.setText("30 Month");
        report1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                report1ActionPerformed(evt);
            }
        });

        report2.setText("31 Month");
        report2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                report2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(changeraddbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                    .addComponent(changerx_itbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(changerdelbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(report, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(report1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(report2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {changeraddbtn, changerdelbtn, changerx_itbutton, report});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeraddbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changerdelbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(report1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(report, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                        .addComponent(report2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(changerx_itbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {changeraddbtn, changerdelbtn, changerx_itbutton, report});

        javax.swing.GroupLayout changersPanelBasePanelLayout = new javax.swing.GroupLayout(changersPanelBasePanel);
        changersPanelBasePanel.setLayout(changersPanelBasePanelLayout);
        changersPanelBasePanelLayout.setHorizontalGroup(
            changersPanelBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changersPanelBasePanelLayout.createSequentialGroup()
                .addGroup(changersPanelBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(changersPanelBasePanelLayout.createSequentialGroup()
                        .addComponent(changersFieldPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1093, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1405, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(127, Short.MAX_VALUE))
        );
        changersPanelBasePanelLayout.setVerticalGroup(
            changersPanelBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changersPanelBasePanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(changersPanelBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(changersFieldPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(206, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout changersPanelLayout = new javax.swing.GroupLayout(changersPanel);
        changersPanel.setLayout(changersPanelLayout);
        changersPanelLayout.setHorizontalGroup(
            changersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changersPanelLayout.createSequentialGroup()
                .addComponent(changersPanelBasePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1536, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        changersPanelLayout.setVerticalGroup(
            changersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(changersPanelBasePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(" Changers ", changersPanel);

        javax.swing.GroupLayout basePanelLayout = new javax.swing.GroupLayout(basePanel);
        basePanel.setLayout(basePanelLayout);
        basePanelLayout.setHorizontalGroup(
            basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(12, 12, 12))
        );
        basePanelLayout.setVerticalGroup(
            basePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basePanelLayout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(basePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        jMenu1.setText("Exit");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jMenu1.setMargin(new java.awt.Insets(0, 10, 0, 10));
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        depositReportsMenu.setText("Reports");
        depositReportsMenu.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        depositReportsMenu.setMargin(new java.awt.Insets(0, 10, 0, 10));

        salesyestday.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        salesyestday.setText("Yesturday");
        salesyestday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesyestdayActionPerformed(evt);
            }
        });
        depositReportsMenu.add(salesyestday);

        salessevenday.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        salessevenday.setText("7 Days");
        salessevenday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salessevendayActionPerformed(evt);
            }
        });
        depositReportsMenu.add(salessevenday);

        salesthirtydays.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        salesthirtydays.setText("30 Days");
        salesthirtydays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesthirtydaysActionPerformed(evt);
            }
        });
        depositReportsMenu.add(salesthirtydays);

        salesthirtyonedays.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        salesthirtyonedays.setText("31 Days");
        salesthirtyonedays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesthirtyonedaysActionPerformed(evt);
            }
        });
        depositReportsMenu.add(salesthirtyonedays);

        salesmonthToDaterpt.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        salesmonthToDaterpt.setText("Month To Date");
        salesmonthToDaterpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesmonthToDaterptActionPerformed(evt);
            }
        });
        depositReportsMenu.add(salesmonthToDaterpt);

        jMenu3.setText("Jim's Fav");
        jMenu3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        jimsfavdoneday.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jimsfavdoneday.setText("1 Day");
        jimsfavdoneday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jimsfavdonedayActionPerformed(evt);
            }
        });
        jMenu3.add(jimsfavdoneday);

        jimsfav7days.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jimsfav7days.setText("7 Days");
        jimsfav7days.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jimsfav7daysActionPerformed(evt);
            }
        });
        jMenu3.add(jimsfav7days);

        monthToDateJimsrpt.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        monthToDateJimsrpt.setText("Month To Date");
        monthToDateJimsrpt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthToDateJimsrptActionPerformed(evt);
            }
        });
        jMenu3.add(monthToDateJimsrpt);

        depositReportsMenu.add(jMenu3);
        depositReportsMenu.add(jSeparator2);

        jMenuBar1.add(depositReportsMenu);

        depositDropEditMenu.setText("Drop Edits");
        depositDropEditMenu.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        depositDropEditMenu.setMargin(new java.awt.Insets(0, 10, 0, 10));

        menu_dropedit_deprt.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        menu_dropedit_deprt.setText("Department");
        menu_dropedit_deprt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_dropedit_deprtActionPerformed(evt);
            }
        });
        depositDropEditMenu.add(menu_dropedit_deprt);

        menu_dropedit_Dsales_Type.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        menu_dropedit_Dsales_Type.setText("Type");
        menu_dropedit_Dsales_Type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_dropedit_Dsales_TypeActionPerformed(evt);
            }
        });
        depositDropEditMenu.add(menu_dropedit_Dsales_Type);

        salesjMenuItem1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        salesjMenuItem1.setText("Item Name");
        salesjMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesjMenuItem1ActionPerformed(evt);
            }
        });
        depositDropEditMenu.add(salesjMenuItem1);

        salesjMenuItem2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        salesjMenuItem2.setText("Refresh Drop Boxes");
        salesjMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salesjMenuItem2ActionPerformed(evt);
            }
        });
        depositDropEditMenu.add(salesjMenuItem2);

        jMenuBar1.add(depositDropEditMenu);

        pdOutReportsMenu.setText("Reports");
        pdOutReportsMenu.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        pdOutReportsMenu.setMargin(new java.awt.Insets(0, 10, 0, 10));

        jMenuItem3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jMenuItem3.setText("Single Day");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        pdOutReportsMenu.add(jMenuItem3);

        jMenuItem7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jMenuItem7.setText("7 Day");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        pdOutReportsMenu.add(jMenuItem7);

        jMenuItem8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jMenuItem8.setText("30 Days");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        pdOutReportsMenu.add(jMenuItem8);

        jMenuItem9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jMenuItem9.setText("31 Day");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        pdOutReportsMenu.add(jMenuItem9);

        monthToDaterpt1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        monthToDaterpt1.setText("Month To Date");
        monthToDaterpt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthToDaterpt1ActionPerformed(evt);
            }
        });
        pdOutReportsMenu.add(monthToDaterpt1);

        jMenuBar1.add(pdOutReportsMenu);

        pdDropEditMenu.setText("Drop Edits");
        pdDropEditMenu.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        pdDropEditMenu.setMargin(new java.awt.Insets(0, 10, 0, 10));

        menu_dropedit_D_Type1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        menu_dropedit_D_Type1.setText("Type");
        menu_dropedit_D_Type1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu_dropedit_D_Type1ActionPerformed(evt);
            }
        });
        pdDropEditMenu.add(menu_dropedit_D_Type1);

        jMenuItem10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jMenuItem10.setText("Item Name");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        pdDropEditMenu.add(jMenuItem10);

        jMenuItem11.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jMenuItem11.setText("Refresh Drop Boxes");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        pdDropEditMenu.add(jMenuItem11);

        jMenuBar1.add(pdDropEditMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(basePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1570, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(basePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    
    
     public ArrayList<String> get_arraylist_from_file() {
         
        File file = new File("C:\\Jar_Files_Local\\employee.txt");
        Scanner scanner = null;
            try {
                scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(null, ex);
            }
        scanner.close();
        //JOptionPane.showMessageDialog(null, "here in scanner " + list.get(0));
        return list;
    }
    
    
    
    
    
    
    public void exitProcess(){
        if (jTabbedPane1.getSelectedIndex()==0) {
        if ("Update".equals(changeraddbtn.getText())) {
            changerresetfields();
            return;
        }else {
        //employeee e = new employeee();
        //e.EmptyVariables();        
        //this.setVisible(false);
        this.dispose();
        }
        }
        
        if (jTabbedPane1.getSelectedIndex()==1) {
        if ("Update".equals(pdoutsaddbutton.getText())) {
            pdoutsresetfields();
            return;
        }else {
        //employeee e = new employeee();
        //e.EmptyVariables();
        //this.setVisible(false);
        this.dispose();
        }
        }
        
        if (jTabbedPane1.getSelectedIndex()==2) {
        if ("Update".equals(salesaddbutton.getText())) {
            salesresetfields();
            return;
        }else {
        //employeee e = new employeee();
        //e.EmptyVariables();
        //this.setVisible(false);
        this.dispose();
        }
        }
        //this.setVisible(false);
        this.dispose();
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Corpform_main2.class.getName()).log(Level.SEVERE, null, ex);
            }
        System.exit(0);
        
        
    }
    
    
   /* public void openframe(int g) {
        //JOptionPane.showMessageDialog(null, g);
        switch (g) {
            case 1: 
            //changers
            Frame[] allframes = Frame.getFrames();
            for (Frame fr:allframes) {
                String specificFrameName = fr.getClass().getName();
            if(specificFrameName.equals("dposits_view") || specificFrameName.equals("paid_outs_view")){
                //JOptionPane.showMessageDialog(null, specificFrameName);
                fr.dispose();
            }
            }
            case 2: 
            //deposits
            allframes = Frame.getFrames();
            for (Frame fr:allframes) {
                String specificFrameName = fr.getClass().getName();
            if(specificFrameName.equals("changers") || specificFrameName.equals("paid_outs_view")){
                //JOptionPane.showMessageDialog(null, specificFrameName);
                fr.dispose();
            }
            }
            case 3: 
            //paid_outs_view
            allframes = Frame.getFrames();
            for (Frame fr:allframes) {
                String specificFrameName = fr.getClass().getName();
            if(specificFrameName.equals("changers") || specificFrameName.equals("dposits_view")){
                //JOptionPane.showMessageDialog(null, specificFrameName);
                fr.dispose();
            }
            }
            case 4: 
            // all frames for exiting
                //JOptionPane.showMessageDialog(null, g);
            allframes = Frame.getFrames();
            for (Frame fr:allframes) {
                String specificFrameName = fr.getClass().getName();
            if(specificFrameName.equals("changers") || specificFrameName.equals("dposits_view") || specificFrameName.equals("paid_outs_view")){
                //JOptionPane.showMessageDialog(null, specificFrameName);
                fr.dispose();
            }
            }   

        }
    }
    */
    public void pushexitbtn(java.awt.event.KeyEvent evt) {
    if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
        exitProcess();
    }
    }

    
    public void whichdropedit(int g){
        // the other three frames rewuire this function
        String fName = "";
        switch (g) {
            case 1:
                //fName = dbStringPath.pathNameCorp + "Depot_1.txt";
                fName = PATHCORP + "Depot_1.txt";
                break;
            case 2:
                fName = PATHCORP + "Depot_3.txt";
                break;
            case 3:
                fName = PATHCORP + "Depot_2.txt";
                break;
            case 4:
                fName = PATHCORP + "Pdouts_2.txt";
                break;
            case 5:
                fName = PATHCORP + "Pdouts_3.txt";
                break;
                
        }
        Desktop dsk = Desktop.getDesktop();
        try {  
            dsk.open(new File(fName));
        } catch (IOException ex) {
            Logger.getLogger(Corpform_main2.class.getName()).log(Level.SEVERE, null, ex);
        }

           
    }
            
    
    private void changerx_itbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changerx_itbuttonActionPerformed
        exitProcess();
    }//GEN-LAST:event_changerx_itbuttonActionPerformed

    private void changerTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changerTableMouseClicked
        int column = 14;
        int row = changerTable.getSelectedRow();
        String value = changerTable.getModel().getValueAt(row, column).toString();
        changeraddbtn.setText("Update");
        changerdelbtn.setVisible(true);
        dbStringPath stnm = new dbStringPath();
        stnm.setrowid(value);
        //System.out.println("-------------------------------------- " + value);
        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM [Changers] where ID=" + value);
            if (rs.next()) {
                dt4.setDate(rs.getDate("DDate"));
                atm.setText(rs.getString("atm"));
                bcars.setText(rs.getString("Bumper_cars"));
                ch1.setText(rs.getString("CHGR_1"));
                ch2.setText(rs.getString("CHGR_2"));
                ch3.setText(rs.getString("Changer_3"));
                ch4.setText(rs.getString("Changer_4"));
                ch5.setText(rs.getString("Changer_5"));
                ch7.setText(rs.getString("CHGR_7"));
                ch8.setText(rs.getString("Changer_8"));
                ch9.setText(rs.getString("Changer_9"));
                ch12.setText(rs.getString("Changer_12"));
                ch14.setText(rs.getString("Changer_14"));
            } else{
                JOptionPane.showMessageDialog(null, "No Resultset " + value);
            }
            rs.close();
            //conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        atm.grabFocus();
        atm.selectAll();

    }//GEN-LAST:event_changerTableMouseClicked

    private void changeraddbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeraddbtnActionPerformed
        String emp = new String(changersmgr.getPassword());
        String nmgr = null;
        Date dt2 = dt4.getDate();
        Date f = new Date(dt2.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        for(Component c : basePanel.getComponents()){
            if(c instanceof JTextField){
                JTextField fd = (JTextField) c;
                String t = fd.getText();
                if (t==null || t.trim().isEmpty()) {
                    fd.setText("0.00");
                }
            }
        }
        Double atmdata = Double.parseDouble(ch1.getText());
        if (atmdata >0 ) {
            ch1.setText(String.valueOf(atmdata *-1));
        }
        atmdata = Double.parseDouble(ch2.getText());
        if (atmdata >0 ) {
            ch2.setText(String.valueOf(atmdata *-1));
        }
        atmdata = Double.parseDouble(ch3.getText());
        if (atmdata >0 ) {
            ch3.setText(String.valueOf(atmdata *-1));
        }
        atmdata = Double.parseDouble(ch4.getText());
        if (atmdata >0 ) {
            ch4.setText(String.valueOf(atmdata *-1));
        }
        atmdata = Double.parseDouble(ch5.getText());
        if (atmdata >0 ) {
            ch5.setText(String.valueOf(atmdata *-1));
        }
        atmdata = Double.parseDouble(ch7.getText());
        if (atmdata >0 ) {
            ch7.setText(String.valueOf(atmdata *-1));
        }
        atmdata = Double.parseDouble(ch8.getText());
        if (atmdata >0 ) {
            ch8.setText(String.valueOf(atmdata *-1));
        }
        atmdata = Double.parseDouble(ch9.getText());
        if (atmdata >0 ) {
            ch9.setText(String.valueOf(atmdata *-1));
        }
        atmdata = Double.parseDouble(ch12.getText());
        if (atmdata >0 ) {
            ch12.setText(String.valueOf(atmdata *-1));
        }
        atmdata = Double.parseDouble(ch14.getText());
        if (atmdata >0 ) {
            ch14.setText(String.valueOf(atmdata *-1));
        }
        atmdata = Double.parseDouble(atm.getText());
        if (atmdata >0 ) {
            atmdata = atmdata *-1;
        }

        nmgr = changersvalidmgr(emp);
        if (nmgr == null || nmgr.trim().isEmpty()) {
            return;
        }

        // here is wehre we need to add to table then refresh jtable
        try {
            String df = dbStringPath.tblrowid;
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
            String q = "INSERT INTO changers(DDate, MGR, Atm, Bumper_Cars, CHGR_1, CHGR_2, Changer_3, Changer_4, Changer_5, CHGR_7, Changer_8, Changer_9, Changer_12, Changer_14) VALUES (#" + formatter.format(f) + "#, '"+ nmgr +"', '"+ atmdata +"', '" + bcars.getText() + "', '"+ch1.getText()  + "', '"+ch2.getText() + "', '"+ch3.getText()+"', '"+ch4.getText()+"', '"+ch5.getText()+"', '"+ch7.getText()+"', '"+ch8.getText()+"', '"+ch9.getText()+"', '"+ch12.getText()+"', '"+ch14.getText()+"')";

            String q2 = "UPDATE Changers SET Changers.DDate = #" + formatter.format(f) + "#, Changers.MGR = '"+ nmgr +"', Changers.ATM = '"+ atmdata +"', Changers.Bumper_Cars = '" + bcars.getText() + "', Changers.CHGR_1 = '"+ch1.getText() + "', Changers.CHGR_2 = '"+ch2.getText() + "', Changers.Changer_3 = '"+ch3.getText()+"', Changers.Changer_4 = '"+ch4.getText()+"', Changers.Changer_5 = '"+ch5.getText()+"', Changers.CHGR_7 = '"+ch7.getText() +"', Changers.Changer_8 = '"+ch8.getText()+"', Changers.Changer_9 = '"+ch9.getText()+"', Changers.Changer_12 = '"+ch12.getText()+"', Changers.Changer_14 = '"+ch14.getText()+"' WHERE (((Changers.ID)=" + df +"))";
            //s = conn.createStatement();
            if ("Add".equals(changeraddbtn.getText())) {
                s.executeUpdate(q);
            }else {
                s.executeUpdate(q2);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        changerTableRefresh();
        changerresetfields();

    }//GEN-LAST:event_changeraddbtnActionPerformed

    private void reportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportActionPerformed
        FileOutputStream out = null;
        Date date_s = dt4.getDate();
        //JOptionPane.showMessageDialog(null, date_s);
        SimpleDateFormat dts = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date date = date_s;
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
        //JOptionPane.showMessageDialog(null,dt1.format(date));

        //String dt2 = dt4.getText();
        //dt.getText();
        //JOptionPane.showMessageDialog(null, Date.parse(dt2));
        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();

            ResultSet rs = s.executeQuery("SELECT * FROM [Changers] where DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 7 DAY) AND DDate <= #" + dt1.format(date) + "# ");
                                                                   // where DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 7 DAY) AND DDate <= #" + dt1.format(date) + "# ")
            new File(PATHCORP + "Rpt_Changers.xls").setWritable(true);
            out = new FileOutputStream(PATHCORP + "Rpt_Changers.xls");
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

            String[] columns = {"Date", "Emp", "ATM", "BCars", "Ch 1", "Ch 2", "Ch 3", "Ch 4", "Ch 5", "Ch 7", "Ch 8", "Ch 9", "Ch 12", "Ch 14"};

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
                cell.setCellValue(rs.getDouble("CHGR_1"));
                cell.setCellStyle(cs2);
                cell = row.createCell(5);
                cell.setCellValue(rs.getDouble("CHGR_2"));
                cell.setCellStyle(cs2);
                cell = row.createCell(6);
                cell.setCellValue(rs.getDouble("Changer_3"));
                cell.setCellStyle(cs2);
                cell = row.createCell(7);
                cell.setCellValue(rs.getDouble("Changer_4"));
                cell.setCellStyle(cs2);
                cell = row.createCell(8);
                cell.setCellValue(rs.getDouble("Changer_5"));
                cell.setCellStyle(cs2);
                cell = row.createCell(9);
                cell.setCellValue(rs.getDouble("CHGR_7"));
                cell.setCellStyle(cs2);
                cell = row.createCell(10);
                cell.setCellValue(rs.getDouble("Changer_8"));
                cell.setCellStyle(cs2);
                cell = row.createCell(11);
                cell.setCellValue(rs.getDouble("Changer_9"));
                cell.setCellStyle(cs2);
                cell = row.createCell(12);
                cell.setCellValue(rs.getDouble("CHanger_12"));
                cell.setCellStyle(cs2);
                cell = row.createCell(13);
                cell.setCellValue(rs.getDouble("CHanger_14"));
                cell.setCellStyle(cs2);
                rowcount++;
            }

            ResultSet rs2 = s.executeQuery("SELECT SUM(atm) as attl, SUM(Bumper_cars) as bttl, SUM(CHGR_1) as 1ttl, SUM(CHGR_2) as 2ttl, SUM(Changer_3) as 3ttl, SUM(Changer_4) as 4ttl, SUM(Changer_5) as 5ttl, SUM(CHGR_7) as 7ttl, SUM(Changer_8) as 8ttl, SUM(Changer_9) as 9ttl, SUM(Changer_12) as 12ttl, SUM(Changer_14) as 14ttl FROM [Changers] where DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 7 DAY) AND DDate <= #" + dt1.format(date) + "# ");
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
                cell.setCellValue(rs2.getDouble("1ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(5);
                cell.setCellValue(rs2.getDouble("2ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(6);
                cell.setCellValue(rs2.getDouble("3ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(7);
                cell.setCellValue(rs2.getDouble("4ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(8);
                cell.setCellValue(rs2.getDouble("5ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(9);
                cell.setCellValue(rs2.getDouble("7ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(10);
                cell.setCellValue(rs2.getDouble("8ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(11);
                cell.setCellValue(rs2.getDouble("9ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(12);
                cell.setCellValue(rs2.getDouble("12ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(13);
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
            st.autoSizeColumn(7);
            st.autoSizeColumn(8);
            st.autoSizeColumn(9);
            st.autoSizeColumn(10);
            st.autoSizeColumn(11);
            st.autoSizeColumn(12);
            st.autoSizeColumn(13);

            wb.write(out);
            out.close();
            //conn.close();
            s.close();
            new File(PATHCORP + "Rpt_Changers.xls").setReadOnly();
            this.setAlwaysOnTop(false);
            Desktop dsk = Desktop.getDesktop();
            dsk.open(new File(PATHCORP + "Rpt_Changers.xls"));
            atm.grabFocus();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        } catch (IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_reportActionPerformed

    
    public void changerresetfields() {
     for(Component c : basePanel.getComponents()){
    if(c instanceof JTextField){
        JTextField fd = (JTextField) c;
        String t = fd.getText();
                fd.setBackground(Color.WHITE);
        }
    }
     
    changerdelbtn.setVisible(false);
    changeraddbtn.setText("Add");
    atm.setText("");
    bcars.setText("");
    ch1.setText("");
    ch2.setText("");
    ch3.setText("");
    ch4.setText("");
    ch5.setText("");
    ch7.setText("");
    ch8.setText("");
    ch9.setText("");
    ch12.setText("");
    ch14.setText("");
    atm.setBackground(Color.LIGHT_GRAY);
    atm.grabFocus();
 }
 
    public String changersvalidmgr(String emp) {
    if (emp == null || emp.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "MGR Number cannot be blank");
        changersmgr.setHorizontalAlignment(changersmgr.LEFT);
        changersmgr.setBackground(Color.LIGHT_GRAY);
        changersmgr.setText("");
        changersmgr.grabFocus();
        return "";
    } else {
        ///employeee id = new employeee();
        boolean df1 = false;
        ///try {
        df1 = true;
        ///} catch (FileNotFoundException ex) {
        ///    Logger.getLogger(Corpform_main2.class.getName()).log(Level.SEVERE, null, ex);
        ///}
        //JOptionPane.showMessageDialog(null, df);
        if (df1 == true) {
            String nmgr = list.get(1);
            changersmgr.setBackground(Color.LIGHT_GRAY);
            return nmgr;
            //return "";
        }else{
            JOptionPane.showMessageDialog(null, "Invalid MGR Number");
            changersmgr.setHorizontalAlignment(changersmgr.LEFT);
            changersmgr.setBackground(Color.LIGHT_GRAY);
            changersmgr.setText("");
            changersmgr.grabFocus();
            return "";
        }
    }
 }
    
    
 public void changerTableRefresh() {
     changerTable.getColumnModel().getColumn(0).setCellRenderer(new DateCellRenderer());
        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
            //ResultSet rs = s.executeQuery("SELECT * FROM [Changers]");
            ResultSet rs = s.executeQuery("SELECT Changers.DDate, Changers.MGR, Changers.ATM, Changers.Bumper_Cars, Changers.CHGR_1, Changers.CHGR_2, Changers.Changer_3, Changers.Changer_4, Changers.Changer_5, Changers.CHGR_7, Changers.Changer_8, Changers.Changer_9, Changers.Changer_12, Changers.Changer_14, Changers.ID\n" +
"FROM Changers;");
            //while (rs.next()) {
                changerTable.setModel(DbUtils.resultSetToTableModel(rs));
            //}
            //conn.close();
            s.close();
            int lastIndex =changerTable.getRowCount()-1;
        changerTable.changeSelection(lastIndex, 0,false,false);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    changerTable.getColumnModel().getColumn(0).setCellRenderer(new DateCellRenderer());
    changerTable.getColumnModel().getColumn(2).setCellRenderer(new CurrencyCellRenderer1());
    changerTable.getColumnModel().getColumn(3).setCellRenderer(new CurrencyCellRenderer1());
    changerTable.getColumnModel().getColumn(4).setCellRenderer(new CurrencyCellRenderer1());
    changerTable.getColumnModel().getColumn(5).setCellRenderer(new CurrencyCellRenderer1());
    changerTable.getColumnModel().getColumn(6).setCellRenderer(new CurrencyCellRenderer1());
    changerTable.getColumnModel().getColumn(7).setCellRenderer(new CurrencyCellRenderer1());
    changerTable.getColumnModel().getColumn(8).setCellRenderer(new CurrencyCellRenderer1());
    changerTable.getColumnModel().getColumn(9).setCellRenderer(new CurrencyCellRenderer1());
    changerTable.getColumnModel().getColumn(10).setCellRenderer(new CurrencyCellRenderer1());
    changerTable.getColumnModel().getColumn(11).setCellRenderer(new CurrencyCellRenderer1());
    changerTable.getColumnModel().getColumn(12).setCellRenderer(new CurrencyCellRenderer1());
    changerTable.getColumnModel().getColumn(13).setCellRenderer(new CurrencyCellRenderer1());
    //jTableExport.getColumnModel().getColumn(9).setCellRenderer(new CurrencyCellRenderer1());
 }   
    
    
    private void changerdelbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changerdelbtnActionPerformed
        String df = dbStringPath.tblrowid;
        String emp = new String(changersmgr.getPassword());
        String nmgr = null;
        nmgr = changersvalidmgr(emp);
        if (nmgr == null || nmgr.trim().isEmpty()) {
            return;
        }
        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://" + dbStringPath.pathNameCorp + "Corp_access.accdb");
            Statement s = conn.createStatement();
            String sql = "DELETE * FROM [Changers] where ID=" + df;
            s.executeUpdate(sql);

            //conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        changerTableRefresh();
        changerresetfields();

    }//GEN-LAST:event_changerdelbtnActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
       stateChanged(evt, jTabbedPane1.getSelectedIndex());
       //atm.grabFocus();
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void salesyestdayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesyestdayActionPerformed
        Date date_s = dt6.getDate();
        g.salesreport(date_s, 1);
    }//GEN-LAST:event_salesyestdayActionPerformed

    private void salessevendayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salessevendayActionPerformed
        Date date_s = dt6.getDate();
        g.salesreport(date_s, 4);
    }//GEN-LAST:event_salessevendayActionPerformed

    private void salesthirtydaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesthirtydaysActionPerformed
        Date date_s = dt6.getDate();
        g.salesreport(date_s, 2);
    }//GEN-LAST:event_salesthirtydaysActionPerformed

    private void salesthirtyonedaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesthirtyonedaysActionPerformed
        Date date_s = dt6.getDate();
        g.salesreport(date_s, 3);
    }//GEN-LAST:event_salesthirtyonedaysActionPerformed

    private void salesmonthToDaterptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesmonthToDaterptActionPerformed
        Date date_s = dt6.getDate();
        g.salesreport(date_s, 5);
    }//GEN-LAST:event_salesmonthToDaterptActionPerformed

    private void jimsfavdonedayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jimsfavdonedayActionPerformed
        Date date_s = dt6.getDate();
        g.jimsfav(date_s, 1);
    }//GEN-LAST:event_jimsfavdonedayActionPerformed

    private void jimsfav7daysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jimsfav7daysActionPerformed
        Date date_s = dt6.getDate();
        g.jimsfav(date_s, 7);
    }//GEN-LAST:event_jimsfav7daysActionPerformed

    private void monthToDateJimsrptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthToDateJimsrptActionPerformed
        Date date_s = dt6.getDate();
        g.jimsfav(date_s, 5);
    }//GEN-LAST:event_monthToDateJimsrptActionPerformed

    private void menu_dropedit_deprtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_dropedit_deprtActionPerformed
        Corpform_main2 crp = new Corpform_main2();
        crp.whichdropedit(1);
    }//GEN-LAST:event_menu_dropedit_deprtActionPerformed

    private void menu_dropedit_Dsales_TypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_dropedit_Dsales_TypeActionPerformed
        Corpform_main2 crp = new Corpform_main2();
        crp.whichdropedit(2);
    }//GEN-LAST:event_menu_dropedit_Dsales_TypeActionPerformed

    private void salesjMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesjMenuItem1ActionPerformed
        Corpform_main2 crp = new Corpform_main2();
        crp.whichdropedit(3);
    }//GEN-LAST:event_salesjMenuItem1ActionPerformed

    public void refreshJboxes() {
         DefaultComboBoxModel model = new DefaultComboBoxModel();
        String fileName = (PATHCORP + "Depot_1.txt");
        File file = new File(fileName);
            try (Scanner in = new Scanner(file)){
              while (in.hasNextLine()) {
                  String line;
                  line = in.nextLine();
                  model.addElement(line);
                  //pDouts_1.setModel(model);
              }  
              in.close();
            }catch (FileNotFoundException ex){
                JOptionPane.showMessageDialog(null, ex);
            }
           
        //second comboBox
        DefaultComboBoxModel model2 = new DefaultComboBoxModel();
        File file2 = new File(PATHCORP + "Depot_3.txt");
            try (Scanner in2 = new Scanner(file2)){
              while (in2.hasNextLine()) {
                  String line2;
                  line2 = in2.nextLine();
                  model2.addElement(line2);
                  //pDouts_2.setModel(model2);
              }  
              in2.close();
            }catch (FileNotFoundException ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        
        //third comboBox
        DefaultComboBoxModel model3 = new DefaultComboBoxModel();
        String fileName3 = (PATHCORP + "Depot_2.txt");
        File file3 = new File(fileName3);
            try (Scanner in3 = new Scanner(file3)){
              while (in3.hasNextLine()) {
                  String line3;
                  line3 = in3.nextLine();
                  model3.addElement(line3);
                  //pDouts_3.setModel(model3);

              }
              in3.close();
            }catch (FileNotFoundException ex) {
                      JOptionPane.showMessageDialog(null, ex);
            }
    }
    
    
    
    private void salesjMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesjMenuItem2ActionPerformed
        refreshJboxes();
    }//GEN-LAST:event_salesjMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        this.setAlwaysOnTop(false);
        Date date_s = dt5.getDate();
        g.couponreport(date_s, 1);
        //this.setAlwaysOnTop(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Date date_s = dt5.getDate();
        g.couponreport(date_s, 4);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        Date date_s = dt5.getDate();
        g.couponreport(date_s, 2);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        Date date_s = dt5.getDate();
        g.couponreport(date_s, 3);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void monthToDaterpt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthToDaterpt1ActionPerformed
        Date date_s = dt5.getDate();
        g.couponreport(date_s, 5);
    }//GEN-LAST:event_monthToDaterpt1ActionPerformed

    private void menu_dropedit_D_Type1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu_dropedit_D_Type1ActionPerformed
        Corpform_main2 crp = new Corpform_main2();
        crp.whichdropedit(4);
    }//GEN-LAST:event_menu_dropedit_D_Type1ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        Corpform_main2 crp = new Corpform_main2();
        crp.whichdropedit(5);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        refreshJboxes();
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
       exitProcess();
    }//GEN-LAST:event_jMenu1MouseClicked

    
    
    /* 
    PAIDOUTS PAIDOUTS PAIDOUTS PAIDOUTS PAIDOUTS PAIDOUTS PAIDOUTS PAIDOUTS PAIDOUTS PAIDOUTS PAIDOUTS  
    */
    
    
    
    
    
    
    
    private void pdoutsxitbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdoutsxitbuttonActionPerformed
        if ("Update".equals(pdoutsaddbutton.getText())) {
            pdoutsresetfields();
            return;
        }else {
            this.setVisible(false);
            this.dispose();
        }

    }//GEN-LAST:event_pdoutsxitbuttonActionPerformed

    private void pdoutsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdoutsTableMouseClicked
        pdoutsdelbutton.setVisible(true);
        int column = 10;
        int row = pdoutsTable.getSelectedRow();
        String value = pdoutsTable.getModel().getValueAt(row, column).toString();
        pdoutsaddbutton.setText("Update");
        dt4.grabFocus();
        dbStringPath stnm = new dbStringPath();
        stnm.setrowid(value);
        //jboxfinditemCORP thisname = new jboxfinditemCORP();
        //JOptionPane.showMessageDialog(null, value);

        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM [Pdouts] where ID=" + value);
            if (rs.next()) {
                dt5.setDate(rs.getDate("Sales_date"));
                pDouts_1.setSelectedIndex(getJboxitemNamepd1(rs.getString("Department")));
                pDouts_2.setSelectedIndex(getJboxitemNamepd2(rs.getString("Type")));
                pDouts_3.setSelectedIndex(getJboxitemNamepd3(rs.getString("Item_Name")));
                pdoutsch10.setText(rs.getString("Memo"));
                double angle = rs.getDouble("Amount");
                DecimalFormat df = new DecimalFormat("#.00");
                String angleFormated = df.format(angle);
                //System.out.println(angleFormated);
                //salesch12.setText(angleFormated);
                pdoutsch13.setText(angleFormated);
                //ch14.setText(rs.getString("Changer_14"));
            } else{
                JOptionPane.showMessageDialog(null, "No Resultset");
            }
            rs.close();
            //conn.close();
            s.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        pDouts_1.grabFocus();
    }//GEN-LAST:event_pdoutsTableMouseClicked

    private void pdoutsaddbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdoutsaddbuttonActionPerformed
        String emp = new String(pdoutsmgr.getPassword());
        String nmgr = null;
        Date dt2 = dt5.getDate();
        Date f = new Date(dt2.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date n = new Date();
        String amt = pdoutsch13.getText();
        if (amt.trim().isEmpty()) {
            pdoutsch13.setText(String.valueOf(0.0));
        }
        //JOptionPane.showMessageDialog(null, formatter.format(f));

        String atmdata = String.valueOf(pDouts_1.getSelectedItem());
        nmgr = pdvalidmgr(emp);
        if (nmgr == null || nmgr.trim().isEmpty()) {
            return;
        }

        getDay t = new getDay();
        String theday = t.getIntDate(dt2);
        //JOptionPane.showMessageDialog(null, formatter.format(n));
        // here is wehre we need to add to table then refresh jtable
        try {
            String df = dbStringPath.tblrowid;
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
            String q = "INSERT INTO Pdouts(Sales_date, day, MGR, Post_date, Location, Department, Type, Item_Name, Memo, Amount) VALUES (#" + formatter.format(f) + "#, '"+ theday +"', '"+ nmgr +"', #" + formatter.format(n) + "#,  'Pojos Fv', '"+ atmdata +"', '" + pDouts_2.getSelectedItem() + "', '" + pDouts_3.getSelectedItem() + "', '"+pdoutsch10.getText()+"', '"+pdoutsch13.getText()+"')";

            String q2 = "UPDATE Pdouts SET Pdouts.Sales_date = #" + formatter.format(f) + "#, Pdouts.day = '"+ theday +"', Pdouts.MGR = '"+ nmgr +"', Pdouts.Post_date = #" + formatter.format(n) + "#, Pdouts.Location = 'Pojos Fv', Pdouts.Department = '" + atmdata + "', Pdouts.Type = '" + pDouts_2.getSelectedItem() + "', Pdouts.Item_Name = '" + pDouts_3.getSelectedItem() + "', Pdouts.Memo = '"+pdoutsch10.getText()+"', Pdouts.Amount = '"+pdoutsch13.getText()+"' WHERE (((Pdouts.ID)=" + df +"))";
            s = conn.createStatement();
            if ("Add".equals(pdoutsaddbutton.getText())) {
                s.executeUpdate(q);
            }else {
                s.executeUpdate(q2);
            }
            s.close();
            //conn.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return;
        }
        pdoutsTableRefresh();
        pdoutsresetfields();

    }//GEN-LAST:event_pdoutsaddbuttonActionPerformed

    public String pdvalidmgr(String emp) {
    if (emp == null || emp.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "MGR Number cannot be blank");
        pdoutsmgr.setHorizontalAlignment(pdoutsmgr.LEFT);
        pdoutsmgr.setBackground(Color.LIGHT_GRAY);
        pdoutsmgr.setText("");
        pdoutsmgr.grabFocus();
        return "";
    }else {
        ///employeee id = new employeee();
        boolean df1 = false;
        ///try {
        df1 = true;
        ///} catch (FileNotFoundException ex) {
        ///    Logger.getLogger(Corpform_main2.class.getName()).log(Level.SEVERE, null, ex);
        ///}
        //JOptionPane.showMessageDialog(null, df);
        if (df1 == true) {
            String nmgr = list.get(1);
            pdoutsmgr.setBackground(Color.LIGHT_GRAY);
            return nmgr;
            //return "";
        }else{
            JOptionPane.showMessageDialog(null, "Invalid MGR Number");
            pdoutsmgr.setHorizontalAlignment(pdoutsmgr.LEFT);
            pdoutsmgr.setBackground(Color.LIGHT_GRAY);
            pdoutsmgr.setText("");
            pdoutsmgr.grabFocus();
            return "";
        }
    }
    }
    
    
    private void pdoutsch10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pdoutsch10KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pdoutsch13.grabFocus();
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_pdoutsch10KeyPressed

    private void pdoutsmgrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pdoutsmgrKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pdoutsaddbutton.doClick();
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_pdoutsmgrKeyPressed

    private void dt5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dt5KeyPressed

        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pDouts_1.grabFocus();
        }
    }//GEN-LAST:event_dt5KeyPressed

    private void pDouts_1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pDouts_1FocusGained

        pDouts_1.showPopup();
    }//GEN-LAST:event_pDouts_1FocusGained

    public void pdoutsTableRefresh() {
        pdoutsTable.getColumnModel().getColumn(0).setCellRenderer(new DateCellRenderer());
        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM [Pdouts]");
            while (rs.next()) {
                pdoutsTable.setModel(DbUtils.resultSetToTableModel(rs));
            }
            //conn.close();
            s.close();
        pdoutsTable.getColumnModel().getColumn(0).setCellRenderer(new DateCellRenderer());
        pdoutsTable.getColumnModel().getColumn(9).setCellRenderer(new DateCellRenderer());
        pdoutsTable.getColumnModel().getColumn(6).setCellRenderer(new CurrencyCellRenderer1());
        int lastIndex =pdoutsTable.getRowCount()-1;
        pdoutsTable.changeSelection(lastIndex, 0,false,false);
        } catch(SQLException e) {
        }
        //pDouts_1.grabFocus();
    }
    
    private void pDouts_1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pDouts_1KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pDouts_1.setPopupVisible(false);
            pDouts_2.grabFocus();
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_pDouts_1KeyPressed

    private void pDouts_2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pDouts_2FocusGained
        pDouts_2.showPopup();
    }//GEN-LAST:event_pDouts_2FocusGained

    private void pDouts_2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pDouts_2KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pDouts_3.grabFocus();
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_pDouts_2KeyPressed
               
public void pdoutsresetfields() {
    pdoutsdelbutton.setVisible(false);
    pdoutsaddbutton.setText("Add");
    pdoutsch10.setText("");
    pdoutsch13.setValue(null);
    pDouts_1.grabFocus();
    }

    private void pDouts_3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pDouts_3FocusGained
        pDouts_3.showPopup();
    }//GEN-LAST:event_pDouts_3FocusGained

    private void pDouts_3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pDouts_3FocusLost
        // TODO add your handling code here:
        //memo.setText("");
    }//GEN-LAST:event_pDouts_3FocusLost

    private void pDouts_3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pDouts_3KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            pdoutsch10.grabFocus();
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_pDouts_3KeyPressed

    private void pdoutsch13KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pdoutsch13KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String nmgr = new String(changersmgr.getPassword());
            if (nmgr == null || nmgr.trim().isEmpty()) {
                changersmgr.grabFocus();
            }else{
                pdoutsaddbutton.doClick();
            }
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_pdoutsch13KeyPressed

    public void pdoutsrefreshJboxes() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        String fileName = (PATHCORP + "Depot_1.txt");
       // JOptionPane.showMessageDialog(null, fileName);
        File file = new File(fileName);
            try (Scanner in = new Scanner(file)){
              while (in.hasNextLine()) {
                  String line;
                  line = in.nextLine();
                  model.addElement(line);
                  pDouts_1.setModel(model);
              }  
              in.close();
            }catch (FileNotFoundException ex){
                JOptionPane.showMessageDialog(null, "235" + ex);
            }
        //second comboBox
        DefaultComboBoxModel model2 = new DefaultComboBoxModel();
        File file2 = new File(PATHCORP + "Pdouts_2.txt");
            try (Scanner in2 = new Scanner(file2)){
              while (in2.hasNextLine()) {
                  String line2;
                  line2 = in2.nextLine();
                  model2.addElement(line2);
                  pDouts_2.setModel(model2);
              }  
              in2.close();
            }catch (FileNotFoundException ex){
                JOptionPane.showMessageDialog(null, "235" + ex);
            }
        
        //third comboBox
        DefaultComboBoxModel model3 = new DefaultComboBoxModel();
        String fileName3 = (PATHCORP + "Pdouts_3.txt");
        File file3 = new File(fileName3);
            try (Scanner in3 = new Scanner(file3)){
              while (in3.hasNextLine()) {
                  String line3;
                  line3 = in3.nextLine();
                  model3.addElement(line3);
                  pDouts_3.setModel(model3);

              }
              in3.close();
            }catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "235" + ex);
            }
    }
    
    private void pdoutsdelbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdoutsdelbuttonActionPerformed
        String df = dbStringPath.tblrowid;
        String emp = new String(changersmgr.getPassword());
        String nmgr = null;
        nmgr = pdvalidmgr(emp);
        if (nmgr == null || nmgr.trim().isEmpty()) {
            return;
        }
        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
            String sql = "DELETE * FROM [Pdouts] where ID=" + df;
            s.executeUpdate(sql);

            //conn.close();
            s.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        pdoutsTableRefresh();
        pdoutsresetfields();

    }//GEN-LAST:event_pdoutsdelbuttonActionPerformed

    
    
    
    /* 
    SALES SALES SALES SALES SALES SALES SALES SALES SALES SALES SALES SALES SALES SALES 
    */
    
    
    
    
    
    
    
    
    
    
    
    
    private void salesxitbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesxitbuttonActionPerformed
        exitProcess();

    }//GEN-LAST:event_salesxitbuttonActionPerformed

    private void salesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesTableMouseClicked
        //JOptionPane.showMessageDialog(null, "here");
        salesdelbutton.setVisible(true);
        int column = 9;
        int row = salesTable.getSelectedRow();
        String value = salesTable.getModel().getValueAt(row, column).toString();
        salesaddbutton.setText("Update");
        dt6.grabFocus();
        dbStringPath stnm = new dbStringPath();
        stnm.setrowid(value);
        //jboxfinditemCORP thisname = new jboxfinditemCORP();

        try {
            //JOptionPane.showMessageDialog(null, "here");
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM [Sales] where ID=" + value);
            if (rs.next()) {
                dt6.setDate(rs.getDate("Sales_date"));
                salespDouts_1.setSelectedIndex(getJboxitemNamepd4(rs.getString("Department")));
                salespDouts_2.setSelectedIndex(getJboxitemNamepd5(rs.getString("Type")));
                salespDouts_3.setSelectedIndex(getJboxitemNamepd6(rs.getString("Item_Name")));
                //ch9.setText(rs.getString("Memo"));
            //JOptionPane.showMessageDialog(null, "here2");
                double angle = rs.getDouble("Amount");
                DecimalFormat df = new DecimalFormat("#.00");
                String angleFormated = df.format(angle);
                //System.out.println(angleFormated);
                salesch12.setText(angleFormated);
                //ch14.setText(rs.getString("Changer_14"));
            } else{
                JOptionPane.showMessageDialog(null, "No Resultset");
            }
            s.close();
            rs.close();
            //conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        //salespDouts_1.grabFocus();

    }//GEN-LAST:event_salesTableMouseClicked

    private void salesaddbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesaddbuttonActionPerformed
        String emp = new String(salesmgr.getPassword());
        String nmgr = null;
        Date dt2 = dt6.getDate();
        Date f = new Date(dt2.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date n = new Date();
        String amt = salesch12.getText();
        if (amt.trim().isEmpty()) {
            salesch12.setText(String.valueOf(0.0));
        }
        //JOptionPane.showMessageDialog(null, formatter.format(f));

        String atmdata = String.valueOf(salespDouts_1.getSelectedItem());
        nmgr = salesvalidmgr(emp);
        if (nmgr == null || nmgr.trim().isEmpty()) {
            return;
        }

        getDay t = new getDay();
        String theday = t.getIntDate(dt2);
        //JOptionPane.showMessageDialog(null, formatter.format(n));
        // here is wehre we need to add to table then refresh jtable
        try {
            String df = dbStringPath.tblrowid;
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
            String q = "INSERT INTO Sales(Sales_date, day, MGR, Post_date, Location, Department, Item_Name, Type, Amount) VALUES (#" + formatter.format(f) + "#, '"+ theday +"', '"+ nmgr +"', #" + formatter.format(n) + "#,  'Pojos Fv', '"+ atmdata +"', '" + salespDouts_3.getSelectedItem() + "', '" + salespDouts_2.getSelectedItem() + "', '"+salesch12.getText()+"')";

            String q2 = "UPDATE Sales SET Sales.Sales_date = #" + formatter.format(f) + "#, Sales.day = '"+ theday +"', Sales.MGR = '"+ nmgr +"', Sales.Post_date = #" + formatter.format(n) + "#, Sales.Location = 'Pojos Fv', Sales.Department = '" + atmdata + "', Sales.Item_Name = '" + salespDouts_3.getSelectedItem() + "', Sales.Type = '" + salespDouts_2.getSelectedItem() + "', Sales.Amount = '"+salesch12.getText()+"' WHERE (((Sales.ID)=" + df +"))";
            //s = conn.createStatement();
            if ("Add".equals(salesaddbutton.getText())) {
                s.executeUpdate(q);
            }else {
                s.executeUpdate(q2);
            }
            //conn.close();
            s.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return;
        }
        salesTableRefresh();
        salesresetfields();

    }//GEN-LAST:event_salesaddbuttonActionPerformed

    private void salesmgrFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salesmgrFocusGained
        this.setBackground(Color.LIGHT_GRAY);
    }//GEN-LAST:event_salesmgrFocusGained

    private void salesmgrFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salesmgrFocusLost
        this.setBackground(Color.WHITE);
    }//GEN-LAST:event_salesmgrFocusLost

    private void salesmgrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salesmgrKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            salesaddbutton.doClick();
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_salesmgrKeyPressed

    private void dt6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dt6KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            salespDouts_1.grabFocus();
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_dt6KeyPressed

    private void salespDouts_1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salespDouts_1FocusGained
        salespDouts_1.showPopup();
    }//GEN-LAST:event_salespDouts_1FocusGained

    private void salespDouts_1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salespDouts_1KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            salespDouts_2.grabFocus();
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_salespDouts_1KeyPressed

    private void salespDouts_2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salespDouts_2FocusGained
        salespDouts_2.showPopup();
    }//GEN-LAST:event_salespDouts_2FocusGained

    private void salespDouts_2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salespDouts_2KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            salespDouts_3.grabFocus();
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_salespDouts_2KeyPressed

    private void salespDouts_3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salespDouts_3FocusGained
        salespDouts_3.showPopup();
    }//GEN-LAST:event_salespDouts_3FocusGained

    private void salespDouts_3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salespDouts_3FocusLost
        // TODO add your handling code here:
        //memo.setText("");
    }//GEN-LAST:event_salespDouts_3FocusLost

    private void salespDouts_3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salespDouts_3KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            salesch12.grabFocus();
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_salespDouts_3KeyPressed

    private void salesdelbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesdelbuttonActionPerformed
        String df = dbStringPath.tblrowid;
        String emp = new String(salesmgr.getPassword());
        String nmgr;
        nmgr = salesvalidmgr(emp);
        if (nmgr == null || nmgr.trim().isEmpty()) {
            return;
        }
        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
            String sql = "DELETE * FROM [Sales] where ID=" + df;
            s.executeUpdate(sql);

            //conn.close();
            s.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        salesTableRefresh();
        salesresetfields();

    }//GEN-LAST:event_salesdelbuttonActionPerformed

    private void pdoutsch10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdoutsch10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pdoutsch10ActionPerformed

    private void pDouts_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pDouts_1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pDouts_1ActionPerformed

    private void salesch12KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salesch12KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String nmgr = new String(salesmgr.getPassword());
            if (nmgr == null || nmgr.trim().isEmpty()) {
                salesmgr.grabFocus();
            }else{
                salesaddbutton.doClick();
            }
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_salesch12KeyPressed

    private void report1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_report1ActionPerformed
        FileOutputStream out = null;
        Date date_s = dt4.getDate();
        //JOptionPane.showMessageDialog(null, date_s);
        SimpleDateFormat dts = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date date = date_s;
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
        //JOptionPane.showMessageDialog(null,dt1.format(date));

        //String dt2 = dt4.getText();
        //dt.getText();
        //JOptionPane.showMessageDialog(null, Date.parse(dt2));
        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();

            ResultSet rs = s.executeQuery("SELECT * FROM [Changers] where DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 30 DAY) AND DDate <= #" + dt1.format(date) + "# ");
            new File(PATHCORP + "Rpt_Changers.xls").setWritable(true);
            out = new FileOutputStream(PATHCORP + "Rpt_Changers.xls");
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

            String[] columns = {"Date", "Emp", "ATM", "BCars", "Ch 1", "Ch 2","Ch 3", "Ch 4", "Ch 5", "Ch 7", "Ch 8", "Ch 9", "Ch 12", "Ch 14"};

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
                cell.setCellValue(rs.getDouble("CHGR_1"));
                cell.setCellStyle(cs2);
                cell = row.createCell(5);
                cell.setCellValue(rs.getDouble("CHGR_2"));
                cell.setCellStyle(cs2);
                cell = row.createCell(6);
                cell.setCellValue(rs.getDouble("Changer_3"));
                cell.setCellStyle(cs2);
                cell = row.createCell(7);
                cell.setCellValue(rs.getDouble("Changer_4"));
                cell.setCellStyle(cs2);
                cell = row.createCell(8);
                cell.setCellValue(rs.getDouble("Changer_5"));
                cell.setCellStyle(cs2);
                cell = row.createCell(9);
                cell.setCellValue(rs.getDouble("CHGR_7"));
                cell.setCellStyle(cs2);
                cell = row.createCell(10);
                cell.setCellValue(rs.getDouble("Changer_8"));
                cell.setCellStyle(cs2);
                cell = row.createCell(11);
                cell.setCellValue(rs.getDouble("Changer_9"));
                cell.setCellStyle(cs2);
                cell = row.createCell(12);
                cell.setCellValue(rs.getDouble("CHanger_12"));
                cell.setCellStyle(cs2);
                cell = row.createCell(13);
                cell.setCellValue(rs.getDouble("CHanger_14"));
                cell.setCellStyle(cs2);
                rowcount++;
            }

            ResultSet rs2 = s.executeQuery("SELECT SUM(Changers.atm) as attl, SUM(Changers.Bumper_cars) as bttl, SUM(Changers.CHGR_1) as 1ttl, SUM(Changers.CHGR_2) as 2ttl, SUM(Changers.Changer_3) as 3ttl, SUM(Changers.Changer_4) as 4ttl, SUM(Changers.Changer_5) as 5ttl, SUM(Changers.CHGR_7) as 7ttl, SUM(Changers.Changer_8) as 8ttl, SUM(Changers.Changer_9) as 9ttl, SUM(Changers.Changer_12) as 12ttl, SUM(Changers.Changer_14) as 14ttl FROM [Changers] where DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 30 DAY) AND DDate <= #" + dt1.format(date) + "# ");
            //System.out.println("SELECT SUM(Changers.atm) as attl, SUM(Changers.Bumper_cars) as bttl, SUM(Changers.CHGR_1) as 1ttl, SUM(Changers.CHGR_2) as 2ttl, SUM(Changers.Changer_3) as 3ttl, SUM(Changers.Changer_4) as 4ttl, SUM(Changers.Changer_5) as 5ttl, SUM(Changers.Changer_8) as 8ttl, SUM(Changers.Changer_9) as 9ttl, SUM(Changers.Changer_12) as 12ttl, SUM(Changers.Changer_14) as 14ttl FROM [Changers] where DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 30 DAY) AND DDate <=#" + dt1.format(date) + "# ");
            while (rs2.next()) {                                                                                                                                                                                                                                                                                                                                                                                              // DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 30 DAY) 
                //System.out.println("HERE IS THE 1 TOTAL: " + rs2.last());
                System.out.println("HERE IS THE 1 TOTAL: " + rs2.getDouble("attl") + " " +  "DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 30 DAY)");
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
                cell.setCellValue(rs2.getDouble("1ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(5);
                cell.setCellValue(rs2.getDouble("2ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(6);
                cell.setCellValue(rs2.getDouble("3ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(7);
                cell.setCellValue(rs2.getDouble("4ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(8);
                cell.setCellValue(rs2.getDouble("5ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(9);
                cell.setCellValue(rs2.getDouble("7ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(10);
                cell.setCellValue(rs2.getDouble("8ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(11);
                cell.setCellValue(rs2.getDouble("9ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(12);
                cell.setCellValue(rs2.getDouble("12ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(13);
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
            st.autoSizeColumn(7);
            st.autoSizeColumn(8);
            st.autoSizeColumn(9);
            st.autoSizeColumn(10);
            st.autoSizeColumn(11);
            st.autoSizeColumn(12);
            st.autoSizeColumn(13);

            wb.write(out);
            out.close();
            //conn.close();
            s.close();
            new File(PATHCORP + "Rpt_Changers.xls").setReadOnly();
            this.setAlwaysOnTop(false);
            Desktop dsk = Desktop.getDesktop();
            dsk.open(new File(PATHCORP + "Rpt_Changers.xls"));
            atm.grabFocus();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        } catch (IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_report1ActionPerformed

    private void report2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_report2ActionPerformed
       FileOutputStream out = null;
        Date date_s = dt4.getDate();
        //JOptionPane.showMessageDialog(null, date_s);
        SimpleDateFormat dts = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date date = date_s;
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
        //JOptionPane.showMessageDialog(null,dt1.format(date));

        //String dt2 = dt4.getText();
        //dt.getText();
        //JOptionPane.showMessageDialog(null, Date.parse(dt2));
        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();

            ResultSet rs = s.executeQuery("SELECT * FROM [Changers] where DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 31 DAY) AND DDate <= #" + dt1.format(date) + "# ");
            new File(PATHCORP + "Rpt_Changers.xls").setWritable(true);
            out = new FileOutputStream(PATHCORP + "Rpt_Changers.xls");
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

            String[] columns = {"Date", "Emp", "ATM", "BCars", "Ch 1", "Ch 2","Ch 3", "Ch 4", "Ch 5", "Ch 7", "Ch 8", "Ch 9", "Ch 12", "Ch 14"};

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
                cell.setCellValue(rs.getDouble("CHGR_1"));
                cell.setCellStyle(cs2);
                cell = row.createCell(5);
                cell.setCellValue(rs.getDouble("CHGR_2"));
                cell.setCellStyle(cs2);
                cell = row.createCell(6);
                cell.setCellValue(rs.getDouble("Changer_3"));
                cell.setCellStyle(cs2);
                cell = row.createCell(7);
                cell.setCellValue(rs.getDouble("Changer_4"));
                cell.setCellStyle(cs2);
                cell = row.createCell(8);
                cell.setCellValue(rs.getDouble("Changer_5"));
                cell.setCellStyle(cs2);
                cell = row.createCell(9);
                cell.setCellValue(rs.getDouble("CHGR_7"));
                cell.setCellStyle(cs2);
                cell = row.createCell(10);
                cell.setCellValue(rs.getDouble("Changer_8"));
                cell.setCellStyle(cs2);
                cell = row.createCell(11);
                cell.setCellValue(rs.getDouble("Changer_9"));
                cell.setCellStyle(cs2);
                cell = row.createCell(12);
                cell.setCellValue(rs.getDouble("CHanger_12"));
                cell.setCellStyle(cs2);
                cell = row.createCell(13);
                cell.setCellValue(rs.getDouble("CHanger_14"));
                cell.setCellStyle(cs2);
                rowcount++;
            }

            ResultSet rs2 = s.executeQuery("SELECT SUM(atm) as attl, SUM(Bumper_cars) as bttl, SUM(CHGR_1) as 1ttl, SUM(CHGR_2) as 2ttl,SUM(Changer_3) as 3ttl, SUM(Changer_4) as 4ttl, SUM(Changer_5) as 5ttl, SUM(Changers.CHGR_7) as 7ttl, SUM(Changer_8) as 8ttl, SUM(Changer_9) as 9ttl, SUM(Changer_12) as 12ttl, SUM(Changer_14) as 14ttl FROM [Changers] where DDate > DATE_SUB(#" + dt1.format(date) + "#, INTERVAL 31 DAY) AND DDate <= #" + dt1.format(date) + "# ");

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
                cell.setCellValue(rs2.getDouble("1ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(5);
                cell.setCellValue(rs2.getDouble("2ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(6);
                cell.setCellValue(rs2.getDouble("3ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(7);
                cell.setCellValue(rs2.getDouble("4ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(8);
                cell.setCellValue(rs2.getDouble("5ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(9);
                cell.setCellValue(rs2.getDouble("7ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(10);
                cell.setCellValue(rs2.getDouble("8ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(11);
                cell.setCellValue(rs2.getDouble("9ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(12);
                cell.setCellValue(rs2.getDouble("12ttl"));
                cell.setCellStyle(cs2);
                cell = row.createCell(13);
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
            st.autoSizeColumn(7);
            st.autoSizeColumn(8);
            st.autoSizeColumn(9);
            st.autoSizeColumn(10);
            st.autoSizeColumn(11);
            st.autoSizeColumn(12);
            st.autoSizeColumn(13);

            wb.write(out);
            out.close();
            //conn.close();
            s.close();
            new File(PATHCORP + "Rpt_Changers.xls").setReadOnly();
            this.setAlwaysOnTop(false);
            Desktop dsk = Desktop.getDesktop();
            dsk.open(new File(PATHCORP + "Rpt_Changers.xls"));
            atm.grabFocus();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        } catch (IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_report2ActionPerformed

    private void ch8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ch8KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ch9.grabFocus();
            ch9.selectAll();
            ch8.setBackground(Color.WHITE);
            ch9.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_ch8KeyPressed

    private void ch1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ch1KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ch2.grabFocus();
            ch2.selectAll();
            ch1.setBackground(Color.WHITE);
            ch2.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_ch1KeyPressed

    private void ch2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ch2KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ch3.grabFocus();
            ch3.selectAll();
            ch2.setBackground(Color.WHITE);
            ch3.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_ch2KeyPressed

    private void ch5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ch5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ch7.grabFocus();
            ch7.selectAll();
            ch5.setBackground(Color.WHITE);
            ch7.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_ch5KeyPressed

    private void ch5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ch5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ch5ActionPerformed

    private void ch4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ch4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ch5.grabFocus();
            ch5.selectAll();
            ch4.setBackground(Color.WHITE);
            ch5.setBackground(Color.LIGHT_GRAY);
        }
    }//GEN-LAST:event_ch4KeyPressed

    private void ch3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ch3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ch4.grabFocus();
            ch4.selectAll();
            ch3.setBackground(Color.WHITE);
            ch4.setBackground(Color.LIGHT_GRAY);
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_ch3KeyPressed

    private void dt4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dt4KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            atm.grabFocus();
            atm.setBackground(Color.LIGHT_GRAY);
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_dt4KeyPressed

    private void changersmgrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_changersmgrKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            changersmgr.setBackground(Color.WHITE);
            changeraddbtn.doClick();
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_changersmgrKeyPressed

    private void ch7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ch7KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ch8.grabFocus();
            ch8.selectAll();
            ch7.setBackground(Color.WHITE);
            ch8.setBackground(Color.LIGHT_GRAY);
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_ch7KeyPressed

    private void ch12KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ch12KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ch14.grabFocus();
            ch14.selectAll();
            ch12.setBackground(Color.WHITE);
            ch14.setBackground(Color.LIGHT_GRAY);
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_ch12KeyPressed

    private void ch14KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ch14KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String nmgr = new String(changersmgr.getPassword());
            if (nmgr == null || nmgr.trim().isEmpty()) {
                changersmgr.grabFocus();
                changersmgr.setBackground(Color.LIGHT_GRAY);
                ch14.setBackground(Color.WHITE);
            }else{
                ch14.setBackground(Color.WHITE);
                changeraddbtn.doClick();
            }
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_ch14KeyPressed

    private void atmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_atmKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bcars.grabFocus();
            atm.setBackground(Color.WHITE);
            bcars.setBackground(Color.LIGHT_GRAY);
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_atmKeyPressed

    private void ch9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ch9KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ch12.grabFocus();
            ch12.selectAll();
            ch9.setBackground(Color.WHITE);
            ch12.setBackground(Color.LIGHT_GRAY);
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_ch9KeyPressed

    private void bcarsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bcarsKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ch1.grabFocus();
            ch1.selectAll();
            bcars.setBackground(Color.WHITE);
            ch1.setBackground(Color.LIGHT_GRAY);
        }
        pushexitbtn(evt);
    }//GEN-LAST:event_bcarsKeyPressed

    private void bcarsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcarsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bcarsActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        atm.grabFocus();
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    public void salesTableRefresh() {
        salesTable.getColumnModel().getColumn(0).setCellRenderer(new DateCellRenderer());
        try {
            //Connection conn=DriverManager.getConnection("jdbc:ucanaccess://L://Corp//Corp_access.accdb");
            Statement s = conn.createStatement();
//            ResultSet rs = s.executeQuery("SELECT Sales.* FROM Sales ORDER BY Sales.Sales_Date;");
            ResultSet rs = s.executeQuery("SELECT Sales.* FROM Sales;");
            while (rs.next()) {
                salesTable.setModel(DbUtils.resultSetToTableModel(rs));
            }
            //conn.close();
            s.close();
        salesTable.getColumnModel().getColumn(0).setCellRenderer(new DateCellRenderer());
        salesTable.getColumnModel().getColumn(8).setCellRenderer(new DateCellRenderer());
        salesTable.getColumnModel().getColumn(6).setCellRenderer(new CurrencyCellRenderer1());
        int lastIndex =salesTable.getRowCount()-1;
        salesTable.changeSelection(lastIndex, 0,false,false);
        } catch(SQLException e) {
        }
    }
    
    public String salesvalidmgr(String emp) {
    if (emp == null || emp.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "MGR Number cannot be blank");
        salesmgr.setHorizontalAlignment(salesmgr.LEFT);
        salesmgr.setText("");
        salesmgr.grabFocus();
        return "";
    }else {
        ///employeee id = new employeee();
        boolean df1 = false;
        ///try {
        df1 = true;
        ///} catch (FileNotFoundException ex) {
        ///    Logger.getLogger(Corpform_main2.class.getName()).log(Level.SEVERE, null, ex);
        ///}
        //JOptionPane.showMessageDialog(null, df);
        if (df1 == true) {
            String nmgr = list.get(1);
            return nmgr;
            //return "";
        }else{
            JOptionPane.showMessageDialog(null, "Invalid MGR Number");
            salesmgr.setHorizontalAlignment(salesmgr.LEFT);
            salesmgr.setText("");
            salesmgr.grabFocus();
            return "";
        }
    }
        
    }
    
    public void salesresetfields() {
    salesdelbutton.setVisible(false);
    salesaddbutton.setText("Add");
    salesch12.setValue(null);
    salespDouts_1.grabFocus();
//    JOptionPane.showMessageDialog(null, "here");
    }
    
    
    public void salesrefreshJboxes() {
         DefaultComboBoxModel model = new DefaultComboBoxModel();
         sgpth.setName();
        
        String fileName = (PATHCORP + "Depot_1.txt");
       // JOptionPane.showMessageDialog(null, "1" + fileName);
        File file = new File(fileName);
            try (Scanner in = new Scanner(file)){
              while (in.hasNextLine()) {
                  String line;
                  line = in.nextLine();
                  model.addElement(line);
                  salespDouts_1.setModel(model);

              }  
              in.close();
            }catch (FileNotFoundException ex){
                JOptionPane.showMessageDialog(null, "234" + ex);
            }
        //second comboBox
        DefaultComboBoxModel model2 = new DefaultComboBoxModel();
        File file2 = new File(PATHCORP + "Depot_3.txt");
            try (Scanner in2 = new Scanner(file2)){
              while (in2.hasNextLine()) {
                  String line2;
                  line2 = in2.nextLine();
                  model2.addElement(line2);
                  salespDouts_2.setModel(model2);
              } 
              in2.close();
            }catch (FileNotFoundException ex){
                JOptionPane.showMessageDialog(null, "234" + ex);
            }
        
        //third comboBox
        DefaultComboBoxModel model3 = new DefaultComboBoxModel();
        String fileName3 = (PATHCORP + "Depot_2.txt");
        File file3 = new File(fileName3);
            try (Scanner in3 = new Scanner(file3)){
              while (in3.hasNextLine()) {
                  String line3;
                  line3 = in3.nextLine();
                  model3.addElement(line3);
                  salespDouts_3.setModel(model3);

              }
              in3.close();
            }catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "234" + ex);
            }
    }
    
    
    
    public int getJboxitemNamepd1(String g) {

String jBoxItem = "";
int stringindex = 0;
        String fileName3 = (PATHCORP + "Depot_1.txt");
        //JOptionPane.showMessageDialog(null, "2" + fileName3);
        File file3 = new File(fileName3);
            try (Scanner in3 = new Scanner(file3)){
              while (in3.hasNextLine()) {
                  jBoxItem = in3.nextLine().trim();
                  if (!jBoxItem.equals(g.toUpperCase())) {
                      stringindex++;
                  } else {
                      //JOptionPane.showMessageDialog(null, g);
                      break;
                  }
                }
              in3.close();
            }catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "123: " + ex);
            }

 return stringindex;    
}
public int getJboxitemNamepd2(String g) {

String jBoxItem = "";
int stringindex = 0;
        String fileName3 = (PATHCORP + "Pdouts_2.txt");
        File file3 = new File(fileName3);
            try (Scanner in3 = new Scanner(file3)){
              while (in3.hasNextLine()) {
                  jBoxItem = in3.nextLine().trim();
                  if (!jBoxItem.equals(g.toUpperCase())) {
                      //JOptionPane.showMessageDialog(null, g);
                      stringindex++;
                  }else{
                      break;
                  }
            }
            }catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
            }

 return stringindex;    
}

     
     
     
public int getJboxitemNamepd3(String g) {
String jBoxItem = "";
Boolean outbounds = false;
int stringindex = 0;
    String fileName3 = (PATHCORP + "Pdouts_3.txt");
        File file3 = new File(fileName3);
            try (Scanner in3 = new Scanner(file3)){
              while (in3.hasNextLine()) {
                  jBoxItem = in3.nextLine().trim();
                  if (!jBoxItem.equals(g.toUpperCase())) {
                      //JOptionPane.showMessageDialog(null, g);
                      stringindex++;
                  } else {
                      outbounds = true;
                      break;
                  }
            }
  
            }catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
    }
            if (outbounds != true) {
                stringindex--;
            }
return stringindex;    
}


public int getJboxitemNamepd4(String g) {

String jBoxItem = "";
int stringindex = 0;
        String fileName3 = (PATHCORP + "Depot_1.txt");
        File file3 = new File(fileName3);
            try (Scanner in3 = new Scanner(file3)){
              while (in3.hasNextLine()) {
                  jBoxItem = in3.nextLine().trim();
                  if (!jBoxItem.equals(g.toUpperCase())) {
                      stringindex++;
                  } else {
                      break;
                  }
            }
            }catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
            }

 return stringindex;    
}
public int getJboxitemNamepd5(String g) {
String jBoxItem = "";
int stringindex = 0;
        String fileName3 = (PATHCORP + "Depot_3.txt");
        File file3 = new File(fileName3);
            try (Scanner in3 = new Scanner(file3)){
              while (in3.hasNextLine()) {
                  jBoxItem = in3.nextLine().trim();
                  if (!jBoxItem.equals(g.toUpperCase())) {
                      stringindex++;
                  } else {
                      break;
                  }
            }
            }catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
            }
 return stringindex;    
}
    
public int getJboxitemNamepd6(String g) {
Boolean outbounds = false;
String jBoxItem = "";
int stringindex = 0;
        String fileName3 = (PATHCORP + "Depot_2.txt");
        File file3 = new File(fileName3);
            try (Scanner in3 = new Scanner(file3)){
              while (in3.hasNextLine()) {
                  jBoxItem = in3.nextLine().trim();
                  if (!jBoxItem.equals(g.toUpperCase())) {
                      stringindex++;
                  } else {
                      outbounds = true;
                      break;
                  }
            }
            }catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
            }

            if (outbounds != true) {
                stringindex--;
            }
 return stringindex;    
}    
    
    
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Corpform_main2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Corpform_main2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Corpform_main2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Corpform_main2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Corpform_main2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField atm;
    private javax.swing.JPanel basePanel;
    private javax.swing.JTextField bcars;
    private javax.swing.JTextField ch1;
    private javax.swing.JTextField ch12;
    private javax.swing.JTextField ch14;
    private javax.swing.JTextField ch2;
    private javax.swing.JTextField ch3;
    private javax.swing.JTextField ch4;
    private javax.swing.JTextField ch5;
    private javax.swing.JTextField ch7;
    private javax.swing.JTextField ch8;
    private javax.swing.JTextField ch9;
    private javax.swing.JTable changerTable;
    private javax.swing.JButton changeraddbtn;
    private javax.swing.JButton changerdelbtn;
    private javax.swing.JPanel changersFieldPane;
    private javax.swing.JPanel changersPanel;
    private javax.swing.JPanel changersPanelBasePanel;
    private javax.swing.JPasswordField changersmgr;
    private javax.swing.JButton changerx_itbutton;
    private javax.swing.JMenu depositDropEditMenu;
    private javax.swing.JMenu depositReportsMenu;
    private javax.swing.JPanel depositsPanel;
    private com.toedter.calendar.JDateChooser dt4;
    private com.toedter.calendar.JDateChooser dt5;
    private com.toedter.calendar.JDateChooser dt6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem jimsfav7days;
    private javax.swing.JMenuItem jimsfavdoneday;
    private javax.swing.JMenuItem menu_dropedit_D_Type1;
    private javax.swing.JMenuItem menu_dropedit_Dsales_Type;
    private javax.swing.JMenuItem menu_dropedit_deprt;
    private javax.swing.JMenuItem monthToDateJimsrpt;
    private javax.swing.JMenuItem monthToDaterpt1;
    private javax.swing.JComboBox<String> pDouts_1;
    private javax.swing.JComboBox<String> pDouts_2;
    private javax.swing.JComboBox<String> pDouts_3;
    private javax.swing.JPanel paidoutsPanel;
    private javax.swing.JMenu pdDropEditMenu;
    private javax.swing.JMenu pdOutReportsMenu;
    private javax.swing.JTable pdoutsTable;
    private javax.swing.JButton pdoutsaddbutton;
    private javax.swing.JTextField pdoutsch10;
    private javax.swing.JFormattedTextField pdoutsch13;
    private javax.swing.JButton pdoutsdelbutton;
    private javax.swing.JPasswordField pdoutsmgr;
    private javax.swing.JButton pdoutsxitbutton;
    private javax.swing.JButton report;
    private javax.swing.JButton report1;
    private javax.swing.JButton report2;
    private javax.swing.JTable salesTable;
    private javax.swing.JButton salesaddbutton;
    private javax.swing.JFormattedTextField salesch12;
    private javax.swing.JButton salesdelbutton;
    private javax.swing.JMenuItem salesjMenuItem1;
    private javax.swing.JMenuItem salesjMenuItem2;
    private javax.swing.JPasswordField salesmgr;
    private javax.swing.JMenuItem salesmonthToDaterpt;
    private javax.swing.JComboBox<String> salespDouts_1;
    private javax.swing.JComboBox<String> salespDouts_2;
    private javax.swing.JComboBox<String> salespDouts_3;
    private javax.swing.JMenuItem salessevenday;
    private javax.swing.JMenuItem salesthirtydays;
    private javax.swing.JMenuItem salesthirtyonedays;
    private javax.swing.JButton salesxitbutton;
    private javax.swing.JMenuItem salesyestday;
    // End of variables declaration//GEN-END:variables

    /*@Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if (newValue.equals(0)) {
            pdOutReportsMenu.setVisible(false);
            pdDropEditMenu.setVisible(false);
            depositReportsMenu.setVisible(true);
            depositDropEditMenu.setVisible(true);
            salesrefreshJboxes();
            salesresetfields();
            //salesTableRefresh();
            salespDouts_1.grabFocus();
        }
        if (newValue.equals(1)) {
            depositReportsMenu.setVisible(false);
            depositDropEditMenu.setVisible(false);
            pdOutReportsMenu.setVisible(true);
            pdDropEditMenu.setVisible(true);
            pdoutsrefreshJboxes();
            pdoutsresetfields();
            //pdoutsTableRefresh();
            pDouts_1.grabFocus();
        }
        if (newValue.equals(2)) {
            pdOutReportsMenu.setVisible(false);
            pdDropEditMenu.setVisible(false);
            depositReportsMenu.setVisible(false);
            depositDropEditMenu.setVisible(false);
            //changerTableRefresh();
            changerresetfields();
            atm.grabFocus();
        }
        
    }*/


    public void stateChanged(ChangeEvent  e, Object newValue) {
        if (newValue.equals(0)) {
            pdOutReportsMenu.setVisible(false);
            pdDropEditMenu.setVisible(false);
            depositReportsMenu.setVisible(true);
            depositDropEditMenu.setVisible(true);
            salesrefreshJboxes();
            salesresetfields();
            //salesTableRefresh();
            salespDouts_1.grabFocus();
        }
        if (newValue.equals(1)) {
            depositReportsMenu.setVisible(false);
            depositDropEditMenu.setVisible(false);
            pdOutReportsMenu.setVisible(true);
            pdDropEditMenu.setVisible(true);
            pdoutsrefreshJboxes();
            pdoutsresetfields();
            //pdoutsTableRefresh();
            pDouts_1.grabFocus();
        }
        if (newValue.equals(2)) {
            pdOutReportsMenu.setVisible(false);
            pdDropEditMenu.setVisible(false);
            depositReportsMenu.setVisible(false);
            depositDropEditMenu.setVisible(false);
            //changerTableRefresh();
            changerresetfields();
            atm.grabFocus();
        }
        
    }





    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override

    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
