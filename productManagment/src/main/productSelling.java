/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package main;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author mmamm
 */
public class productSelling extends javax.swing.JFrame {

    /**
     * Creates new form productSelling
     */
    public productSelling() {
        initComponents();
        tabaleDesign();
        loadData();
        defaut();
        // genarateProductInvoice();
        //genarteSalseDetails(bill);
    }

    int number = 1;
    float netTotal = 0;   
    

    private void genarteSalseDetails(int bill) {
        try {
            Connection con = dataBase.db.getcConnection();

            // Map for adding parameters to the report
            HashMap<String, Object> params = new HashMap<>();

            params.put("bill_Id", bill+"");
            // Load report
            JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("/reports/Sale invoice.jasper"));

            // Fill report with data and parameters
            JasperPrint jasper = JasperFillManager.fillReport(report, params, con);

            JasperViewer.viewReport(jasper, false);
            //JasperPrintManager.printReport(jasper, false);
        } catch (JRException ex) {
            Logger.getLogger(productSelling.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadData() {

        try {
            Connection con = dataBase.db.getcConnection();
            String sql = "SELECT `name` FROM product_management_system.products;";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String productName = rs.getString("name");
                cmbProduct.addItem(productName);
            }

        } catch (SQLException ex) {
            Logger.getLogger(productSelling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void tabaleDesign() {
        tblDetails.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        tblDetails.getTableHeader().setForeground(new Color(255, 255, 255));
        tblDetails.getTableHeader().setBackground(new Color(102, 102, 102));
        tblDetails.getTableHeader().setOpaque(false);
        tblDetails.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblDetails.setShowHorizontalLines(true);
        tblDetails.setShowVerticalLines(true);
        tblDetails.setRowHeight(25);
        tblDetails.setSelectionBackground(new Color(153, 153, 153));
    }

    public void fullReset() {
        cmbProduct.setSelectedIndex(0);
        txtUnitPrice.setText("");
        txtQuantity.setText("1");
        txtUnitPrice.setText("");
        txtDiscount.setText("0");
        txtTotal.setText("0");

        txtSideDiscount.setText("0");
        txtCashPaid.setText("0");
        txtBalance.setText("");
    }

    public void reset() {
        txtUnitPrice.setText("");
        txtQuantity.setText("1");
        txtUnitPrice.setText("");
        txtDiscount.setText("0");
        txtTotal.setText("0");
    }

    public void defaut() {
        txtQuantity.setEnabled(false);
        txtUnitPrice.setEnabled(false);
        txtDiscount.setEnabled(false);
    }

    public void selectData() {
        String selection = cmbProduct.getSelectedItem().toString();
        try {
            Connection con = dataBase.db.getcConnection();
            String sql = "SELECT price FROM product_management_system.products where `name`='" + selection + "';";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Float price = rs.getFloat("price");
                reset();

                txtTotal.setText(price + "");
                txtUnitPrice.setText(price + "");

                txtQuantity.setEnabled(true);
                txtUnitPrice.setEnabled(true);
                txtDiscount.setEnabled(true);
                btnAdd.setEnabled(true);
            } else {
                btnAdd.setEnabled(false);
                reset();
                defaut();
            }
        } catch (SQLException ex) {
            Logger.getLogger(productSelling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void total() {
        //seleced product
        String selection = cmbProduct.getSelectedItem().toString();

        //unitPrice
        float unitPrice = Float.valueOf(txtUnitPrice.getText());

        //quantity
        int quantity = 1;
        try {
            quantity = Integer.valueOf(txtQuantity.getText());
        } catch (NumberFormatException e) {
            quantity = 1;
        }

        //Discount
        int discount = 0;
        try {
            discount = Integer.valueOf(txtDiscount.getText());
        } catch (NumberFormatException e) {
            discount = 0;
        }

        //Chack Stock Limit
        try {
            Connection con = dataBase.db.getcConnection();
            String sql = "SELECT Stock FROM product_management_system.products where `name`='" + selection + "';";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int Stock = rs.getInt("Stock");
                if (Stock < quantity) {
                    lblError.setText(selection + "'s Stock is Over !");
                    txtQuantity.setForeground(Color.red);
                    btnAdd.setEnabled(false);
                } else {
                    lblError.setText("");
                    btnAdd.setEnabled(true);
                    txtQuantity.setForeground(Color.black);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(productSelling.class.getName()).log(Level.SEVERE, null, ex);
        }

        txtTotal.setText((quantity * unitPrice) * (100 - discount) / 100 + "");
    }

    public void addToTable() {

        int productId = 0;
        String product = cmbProduct.getSelectedItem().toString();

        //Selecting product id
        try {
            Connection con = dataBase.db.getcConnection();
            String sql = "SELECT id FROM product_management_system.products where `name`='" + product + "' ;";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                productId = rs.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(productSelling.class.getName()).log(Level.SEVERE, null, ex);
        }

        String quantity = txtQuantity.getText();
        String unitprice = txtUnitPrice.getText();
        String discount = txtDiscount.getText();

        float currntTtl = Float.valueOf(txtTotal.getText());

        netTotal = netTotal + currntTtl;
        txtNetTotal.setText(netTotal + "");
        txtSideTotal.setText(netTotal + "");

        txtSubTotal.setText(currntTtl + "");

        DefaultTableModel tm = (DefaultTableModel) tblDetails.getModel();

        Vector addData = new Vector();

        addData.add(number);
        addData.add(product);
        addData.add(quantity);
        addData.add(unitprice);
        addData.add(discount);
        addData.add(currntTtl);
        addData.add(productId);

        tm.addRow(addData);

        btnPrint.setEnabled(true);

    }

    public void sideDiscount() {
        //int netTotal = Integer.valueOf(txtNetTotal.getText());

        int Discount = 0;
        try {
            Discount = Integer.valueOf(txtSideDiscount.getText());
        } catch (NumberFormatException e) {
            Discount = 0;
        }

        txtSideTotal.setText((netTotal) * (100 - Discount) / 100 + "");
    }

    public void productBillDetails() {
        int billId = 0;

        //insert Data into Bill Details
        float netTotal = Float.valueOf(txtNetTotal.getText());
        int netDiscount = Integer.valueOf(txtSideDiscount.getText());
        int cashPaid = Integer.valueOf(txtCashPaid.getText());
        String customerName = txtCustomerName.getText();
        String cashierName = txtCashierName.getText();

        try {
            // create connection to database
            Connection con = dataBase.db.getcConnection();
            // Create prepared statement with the sql query
            String sql = "INSERT INTO `bill_details` (`netTtl`, `netDic`, `cashPaid`, `custName`, `cashier`) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement st1 = con.prepareStatement(sql);
            st1.setFloat(1, netTotal);
            st1.setInt(2, netDiscount);
            st1.setInt(3, cashPaid);
            st1.setString(4, customerName);
            st1.setString(5, cashierName);
            // execute query
            st1.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(productSelling.class.getName()).log(Level.SEVERE, null, ex);
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////
        //insert Data into salse Details
        try {
            Connection con = dataBase.db.getcConnection();
            String sql = "SELECT max(billID) FROM product_management_system.bill_details;";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                billId = rs.getInt("max(billID)");
            }

        } catch (SQLException ex) {
            Logger.getLogger(productSelling.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Getting table data for salse
        for (int i = 0; i < tblDetails.getRowCount(); i++) {

            DefaultTableModel tm = (DefaultTableModel) tblDetails.getModel();
            int quntity = Integer.valueOf(tm.getValueAt(i, 2).toString());
            int discount = Integer.valueOf(tm.getValueAt(i, 4).toString());
            int productid = Integer.valueOf(tm.getValueAt(i, 6).toString());

            //Insert to Sale Details
            try {
                // create connection to database
                Connection con = dataBase.db.getcConnection();
                // Create prepared statement with the sql query
                String sql = "INSERT INTO `product_management_system`.`sale_details` (`productID`, `quantity`, `discount`, `billId`) VALUES (?, ?, ?, ?);";
                PreparedStatement st1 = con.prepareStatement(sql);
                st1.setInt(1, productid);
                st1.setInt(2, quntity);
                st1.setInt(3, discount);
                st1.setInt(4, billId);
                droppingQuantity(productid, quntity);

                // execute query
                st1.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(productSelling.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        genarteSalseDetails(billId);

    }

    public void droppingQuantity(int productId, int quantity) {

        int stock = 0;

        //Chack Stock Limit
        try {
            Connection con = dataBase.db.getcConnection();
            String sql = "SELECT Stock FROM product_management_system.products where `id`='" + productId + "';";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                stock = rs.getInt("Stock");
            }
        } catch (SQLException ex) {
            Logger.getLogger(productSelling.class.getName()).log(Level.SEVERE, null, ex);
        }

        int droppedStock = stock - quantity;
        System.out.println(droppedStock);

        //////////////////////////////////////////////////////////////////////////////////////////////////
        //Update Stock Limit
        try {
            // create connection to database
            Connection con = dataBase.db.getcConnection();
            // Create prepared statement with the sql query
            PreparedStatement st1 = con.prepareStatement("UPDATE `product_management_system`.`products` SET `Stock` = ? WHERE (`id` = '" + productId + "');");
            // execute query
            st1.setInt(1, droppedStock);
            st1.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(productSelling.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void newCustomer() {
        netTotal = 0;
        number = 1;
        cmbProduct.setSelectedIndex(0);
        txtUnitPrice.setText("");
        txtQuantity.setText("1");
        txtUnitPrice.setText("");
        txtDiscount.setText("0");
        txtTotal.setText("0");
        txtCustomerName.setText("");
        txtSubTotal.setText("");
        txtNetTotal.setText("");
        txtSideTotal.setText("");

        txtSideDiscount.setText("0");
        txtCashPaid.setText("0");
        txtBalance.setText("");

        DefaultTableModel tm = (DefaultTableModel) tblDetails.getModel();
        tm.setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenu2 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbProduct = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtUnitPrice = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        lblError = new javax.swing.JLabel();
        btnRemove = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtDiscount = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetails = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtNetTotal = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtSideDiscount = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtSideTotal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtCashPaid = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtBalance = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtCashierName = new javax.swing.JTextField();

        jMenu2.setText("jMenu2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 51, 153));

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 22)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Product Management System");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Product Selling");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setText("Product :");

        cmbProduct.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        cmbProduct.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "not selected" }));
        cmbProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProductActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setText("Quantity :");

        txtQuantity.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtQuantity.setText("1");
        txtQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQuantityFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQuantityFocusLost(evt);
            }
        });
        txtQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantityActionPerformed(evt);
            }
        });
        txtQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQuantityKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setText("Unit Price :");

        txtUnitPrice.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtUnitPrice.setFocusable(false);

        txtTotal.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        txtTotal.setText("0");
        txtTotal.setFocusable(false);

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jLabel5.setText("Total :");

        btnAdd.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        btnAdd.setText("Add");
        btnAdd.setEnabled(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnPrint.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        btnPrint.setText("Print");
        btnPrint.setEnabled(false);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        lblError.setBackground(new java.awt.Color(255, 51, 51));
        lblError.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblError.setForeground(new java.awt.Color(255, 51, 51));
        lblError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnRemove.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        btnRemove.setText("Remove");
        btnRemove.setEnabled(false);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setText("Discount  :");

        txtDiscount.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtDiscount.setText("0");
        txtDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDiscountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountFocusLost(evt);
            }
        });
        txtDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiscountKeyReleased(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 15)); // NOI18N
        jButton4.setText("New Customer  ");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbProduct, 0, 186, Short.MAX_VALUE)
                                    .addComponent(txtQuantity)
                                    .addComponent(txtUnitPrice)
                                    .addComponent(txtDiscount)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(lblError, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txtUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnPrint)
                    .addComponent(btnRemove))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tblDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Product", "Quantity", "Price", "Dicount", "Total", "prodctId"
            }
        ));
        tblDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetailsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDetails);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setText("Sub Total :");

        txtSubTotal.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtSubTotal.setFocusable(false);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setText("Net Total :");

        txtNetTotal.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtNetTotal.setFocusable(false);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setText("Discount :");

        txtSideDiscount.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtSideDiscount.setText("0");
        txtSideDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSideDiscountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSideDiscountFocusLost(evt);
            }
        });
        txtSideDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSideDiscountKeyReleased(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setText("Total :");

        txtSideTotal.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtSideTotal.setFocusable(false);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel11.setText("Cash Paid :");

        txtCashPaid.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtCashPaid.setText("0");
        txtCashPaid.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCashPaidFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCashPaidFocusLost(evt);
            }
        });
        txtCashPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCashPaidKeyReleased(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jLabel12.setText("Balance :");

        txtBalance.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        txtBalance.setFocusable(false);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel14.setText("Custamor :");

        txtCustomerName.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jLabel15.setText("Cashier Name");

        txtCashierName.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtCashierName.setText("Admin");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(8, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCashPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSideDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNetTotal)
                                    .addComponent(txtSideTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))))
                        .addGap(14, 14, 14))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCashierName, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtCashierName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtNetTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtSideDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtSideTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtCashPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProductActionPerformed
        selectData();
    }//GEN-LAST:event_cmbProductActionPerformed

    private void txtQuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuantityKeyReleased
        total();
    }//GEN-LAST:event_txtQuantityKeyReleased

    private void txtDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiscountKeyReleased
        total();
    }//GEN-LAST:event_txtDiscountKeyReleased

    private void txtQuantityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuantityFocusGained
        txtQuantity.setText("");
    }//GEN-LAST:event_txtQuantityFocusGained

    private void txtQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuantityFocusLost
        if (txtQuantity.getText().equals("")) {
            txtQuantity.setText("1");
            total();
        }
    }//GEN-LAST:event_txtQuantityFocusLost

    private void txtDiscountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountFocusGained
        txtDiscount.setText("");
    }//GEN-LAST:event_txtDiscountFocusGained

    private void txtDiscountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountFocusLost
        if (txtDiscount.getText().equals("")) {
            txtDiscount.setText("0");
            total();
        }
    }//GEN-LAST:event_txtDiscountFocusLost

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        addToTable();
        number++;
        fullReset();

    }//GEN-LAST:event_btnAddActionPerformed

    private void txtCashPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCashPaidKeyReleased
        float total = Float.valueOf(txtSideTotal.getText());

        float cash = 0;
        try {
            cash = Float.valueOf(txtCashPaid.getText());
        } catch (NumberFormatException e) {
            cash = 0;
        }

        txtBalance.setText(cash - total + "");
    }//GEN-LAST:event_txtCashPaidKeyReleased

    private void txtSideDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSideDiscountKeyReleased
        sideDiscount();
    }//GEN-LAST:event_txtSideDiscountKeyReleased

    private void txtSideDiscountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSideDiscountFocusGained
        txtSideDiscount.setText("");
    }//GEN-LAST:event_txtSideDiscountFocusGained

    private void txtSideDiscountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSideDiscountFocusLost
        if (txtSideDiscount.getText().equals("")) {
            txtSideDiscount.setText("0");
            sideDiscount();
        }
    }//GEN-LAST:event_txtSideDiscountFocusLost

    private void txtCashPaidFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCashPaidFocusLost
        if (txtCashPaid.getText().equals("")) {
            txtCashPaid.setText("0");
        }
        if (txtCashPaid.getText().equals("0")) {
            txtBalance.setText("");
        }
    }//GEN-LAST:event_txtCashPaidFocusLost

    private void txtCashPaidFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCashPaidFocusGained
        txtCashPaid.setText("");
    }//GEN-LAST:event_txtCashPaidFocusGained

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        productBillDetails();
        btnPrint.setEnabled(false);
        newCustomer();

    }//GEN-LAST:event_btnPrintActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        newCustomer();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantityActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        int row = tblDetails.getSelectedRow();
        DefaultTableModel tm = (DefaultTableModel) tblDetails.getModel();
        tm.removeRow(row);
        btnRemove.setEnabled(false);
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void tblDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetailsMouseClicked
        btnRemove.setEnabled(true);
    }//GEN-LAST:event_tblDetailsMouseClicked

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(productSelling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(productSelling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(productSelling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(productSelling.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new productSelling().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnRemove;
    private javax.swing.JComboBox<String> cmbProduct;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblError;
    private javax.swing.JTable tblDetails;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtCashPaid;
    private javax.swing.JTextField txtCashierName;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtNetTotal;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSideDiscount;
    private javax.swing.JTextField txtSideTotal;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtUnitPrice;
    // End of variables declaration//GEN-END:variables
}
