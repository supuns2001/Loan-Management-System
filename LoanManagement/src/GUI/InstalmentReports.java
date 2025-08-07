/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import Model.MySQL;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Vector;
import javax.swing.ButtonModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 *
 * @author user
 */
public class InstalmentReports extends javax.swing.JPanel {

    private String cusId;

    /**
     * Creates new form InstalmentReports
     */
    public InstalmentReports() {
        initComponents();
        loadTable();
        clearFields();
    }

    public JTextField getjTextField3() {
        return jTextField3;
    }

    public JTextField getjTextField4() {
        return jTextField4;
    }

    public JTextField getjTextField5() {
        return jTextField5;
    }

    public void clearFields() {

        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jFormattedTextField1.setText("");
        jTextField7.setText("B");
        jRadioButton1.setSelected(false);
        jRadioButton2.setSelected(true);

        jTextField1.grabFocus();
        jTextField2.setEditable(false);

    }

    public void loadTable() {

        try {

            ResultSet rs = MySQL.execute("SELECT * FROM `installments` "
                    + " INNER JOIN `customer_details` ON `customer_details`.`cus_id` = `installments`.`cus_id` "
                    + " INNER JOIN `loan_details` ON `loan_details`.`loan_id` = `installments`.`loan_id`  "
                    + " INNER JOIN `loan_status` ON `loan_status`.`id` = `loan_details`.`status_id` "
                    + " INNER JOIN `deposit_type` ON `deposit_type`.`d_id` = `installments`.`d_id` "
                    + " ORDER BY `date_time`  ASC ");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("receipt_id"));
                v.add(rs.getString("cus_nic"));
                v.add(rs.getString("deposit_type.d_name"));
                v.add(rs.getString("amount"));
                v.add(rs.getString("loan_id"));
                v.add(rs.getString("loan_balance"));
                v.add(rs.getString("date_time"));

                model.addRow(v);
            }
            jTable1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String rId;

    public void genarateRecieptNmber() {

        System.out.println("cash");

        LocalDate today = LocalDate.now();

        // Extract year, month, and day
        String year = today.format(DateTimeFormatter.ofPattern("yy"));
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        System.out.println("Year: " + year);
        System.out.println("Month: " + month);
        System.out.println("Day: " + day);

        Random random = new Random();

        // Generate a random 6-digit integer between 100000 and 999999
        int threeDigitNumber = 100 + random.nextInt(900); // This gives a number between 100000 and 999999

        System.out.println(threeDigitNumber);

        String receiptnumber = "C" + year + month + day + threeDigitNumber;
//                C" + year + month + day + threeDigitNumber;

        this.rId = receiptnumber;
    }

    public void searchInstallment() {

        String searchCusNic = jTextField8.getText();
        String searchLoanId = jTextField10.getText();

        try {

            if (!searchCusNic.isEmpty() && searchLoanId.isEmpty()) {

                ResultSet rs = MySQL.execute("SELECT * FROM `installments` "
                        + " INNER JOIN `customer_details` ON `customer_details`.`cus_id` = `installments`.`cus_id`"
                        + " INNER JOIN `loan_details` ON `loan_details`.`loan_id` = `installments`.`loan_id` "
                        + " INNER JOIN `deposit_type` ON `deposit_type`.`d_id` = `installments`.`d_id`  WHERE `cus_nic` LIKE '%" + searchCusNic + "%'  ORDER BY `register_date`  ASC ");

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                while (rs.next()) {
                    Vector<String> v = new Vector<>();
                    v.add(rs.getString("receipt_id"));
                    v.add(rs.getString("cus_nic"));
                    v.add(rs.getString("d_name"));
                    v.add(rs.getString("amount"));
                    v.add(rs.getString("loan_id"));
                    v.add(rs.getString("loan_balance"));
                    v.add(rs.getString("loan_activated_date"));

                    model.addRow(v);
                }
                jTable1.setModel(model);

            } else if (!searchCusNic.isEmpty() && !searchLoanId.isEmpty()) {

                ResultSet rs = MySQL.execute("SELECT * FROM `installments` "
                        + " INNER JOIN `customer_details` ON `customer_details`.`cus_id` = `installments`.`cus_id`"
                        + " INNER JOIN `loan_details` ON `loan_details`.`loan_id` = `installments`.`loan_id` "
                        + " INNER JOIN `deposit_type` ON `deposit_type`.`d_id` = `installments`.`d_id` "
                        + "  WHERE `cus_nic` LIKE '%" + searchCusNic + "%' AND `loan_details`.`loan_id` LIKE '%" + searchLoanId + "%'  ORDER BY `register_date`  ASC ");

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                while (rs.next()) {
                    Vector<String> v = new Vector<>();
                    v.add(rs.getString("receipt_id"));
                    v.add(rs.getString("cus_nic"));
                    v.add(rs.getString("d_name"));
                    v.add(rs.getString("amount"));
                    v.add(rs.getString("loan_id"));
                    v.add(rs.getString("loan_balance"));
                    v.add(rs.getString("loan_activated_date"));

                    model.addRow(v);
                }
                jTable1.setModel(model);

            } else if (searchCusNic.isEmpty() && !searchLoanId.isEmpty()) {

                ResultSet rs = MySQL.execute("SELECT * FROM `installments` "
                        + " INNER JOIN `customer_details` ON `customer_details`.`cus_id` = `installments`.`cus_id`"
                        + " INNER JOIN `loan_details` ON `loan_details`.`loan_id` = `installments`.`loan_id` "
                        + " INNER JOIN `deposit_type` ON `deposit_type`.`d_id` = `installments`.`d_id` "
                        + "  WHERE `loan_details`.`loan_id` LIKE '%" + searchLoanId + "%'  ORDER BY `register_date`  ASC ");

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                while (rs.next()) {
                    Vector<String> v = new Vector<>();
                    v.add(rs.getString("receipt_id"));
                    v.add(rs.getString("cus_nic"));
                    v.add(rs.getString("d_name"));
                    v.add(rs.getString("amount"));
                    v.add(rs.getString("loan_id"));
                    v.add(rs.getString("loan_balance"));
                    v.add(rs.getString("loan_activated_date"));

                    model.addRow(v);
                }
                jTable1.setModel(model);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        utilCalendarModel1 = new org.jdatepicker.impl.UtilCalendarModel();
        sqlDateModel1 = new org.jdatepicker.impl.SqlDateModel();
        dateChooserDialog1 = new datechooser.beans.DateChooserDialog();
        dateComponentFormatter1 = new org.jdatepicker.impl.DateComponentFormatter();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jTextField7 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Customer NIC:");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.setPreferredSize(new java.awt.Dimension(64, 30));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Loan ID:");

        jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Loan Amount");

        jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Loan Balanace:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Amount:");

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("Pay ");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);

        jRadioButton1.setText("Cash");
        jRadioButton1.setActionCommand("1");
        jRadioButton1.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton1.setText("Cash");
        jRadioButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton1ItemStateChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);

        jRadioButton2.setActionCommand("2");
        jRadioButton2.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton2.setText("Bank");
        jRadioButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton2ItemStateChanged(evt);
            }
        });
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jTextField7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Reciept ID:");

        jButton1.setText("Select Loan ID");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jTextField2)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 372, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jRadioButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton2))
                            .addComponent(jTextField7)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))))
                .addGap(50, 50, 50))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Receipt ID", "Customer NIC", "Bank / Cash", "Installment", "Loan ID", "Loan Balance", "Date time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton5.setText("Search");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jTextField8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField8KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField8KeyReleased(evt);
            }
        });

        jTextField10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField10KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField10KeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Customer NIC");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Loan ID");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        String cusNIC = jTextField1.getText();
        String loanId = jTextField3.getText();
        String cusID = this.cusId;

        String amount = jFormattedTextField1.getText();

        System.out.println(amount);

        ButtonModel dipositeSelection = buttonGroup1.getSelection();

        if (cusNIC.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Customer NIC ", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (loanId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Loan ID is Empty. Please Select the Loan", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (amount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please Enter the Amount", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (dipositeSelection == null) {
            JOptionPane.showMessageDialog(this, "Please Select The Deposite Type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter formatterDate2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            String formattedDateTime = currentDateTime.format(formatter);
            String cashDate = currentDateTime.format(formatterDate2);

            try {
                ResultSet rs = MySQL.execute("SELECT * FROM `loan_details` WHERE `loan_id`  = '" + loanId + "' AND `cus_id` = '" + cusID + "' ");

                if (rs.next()) {
                    // have loan
                    double amountBalance = Double.parseDouble(amount);

                    double loan_balance = Double.parseDouble(rs.getString("loan_balance"));

                    if (loan_balance >= amountBalance) {

                        if (loan_balance == 0) {
                            JOptionPane.showMessageDialog(this, "This loan is already Completed", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else {

                            if (dipositeSelection.getActionCommand() == "1") {
                                //1 = cash

                                ResultSet rs2 = MySQL.execute("INSERT INTO `installments` (`receipt_id`,`cus_id`,`d_id`,`amount`,`loan_id`,`date_time`) "
                                        + " VALUES ('" + this.rId + "','" + cusID + "','1','" + amountBalance + "','" + loanId + "','" + formattedDateTime + "')");

                                double nowBalance = Double.parseDouble(rs.getString("loan_balance")) - amountBalance;
                                int remaining_months = Integer.parseInt(rs.getString("paid_loan_period")) - 1;
                                ResultSet rs3 = MySQL.execute("UPDATE `loan_details` SET  `loan_balance` = '" + nowBalance + "'  , `paid_loan_period` = '" + remaining_months + "'  WHERE `loan_id` = '" + loanId + "'  ");

                                ResultSet cashReportsDetails = MySQL.execute("SELECT * FROM `customer_details` WHERE `cus_id` = '" + cusID + "'");

                                if (cashReportsDetails.next()) {

                                    int cash_summary_status = 2; //installment
                                    String c_name = cashReportsDetails.getString("cus_full_name");
                                    String loan_id = "...";
                                    String description = "Installment";

                                    MySQL.execute("INSERT INTO `cash_reports` (`summary_status_id`,`c_name`,`c_nic`,`l_id`,`r_id`,`description`,`amount`,`date`)"
                                            + "VALUES ('" + cash_summary_status + "','" + c_name + "','" + cusNIC + "','" + loanId + "','" + this.rId + "','" + description + "','" + amountBalance + "','" + cashDate + "') ");

                                }

                                loadTable();
                                clearFields();

                                JOptionPane.showMessageDialog(this, "Instalment Successful", "Success", JOptionPane.INFORMATION_MESSAGE);

                            } else if (dipositeSelection.getActionCommand() == "2") {
                                //2 = Bank

                                String bankreceiptId = jTextField7.getText();

                                ResultSet rs3 = MySQL.execute("SELECT * FROM `installments` WHERE `receipt_id` = '" + bankreceiptId + "'");

                                if (rs3.next()) {
                                    JOptionPane.showMessageDialog(this, "Bank Reciept ID alreay exsists. Please check the Receipt ID", "Warning", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    ResultSet rs4 = MySQL.execute("INSERT INTO `installments` (`receipt_id`,`cus_id`,`d_id`,`amount`,`loan_id`,`date_time`) "
                                            + " VALUES ('" + bankreceiptId + "','" + cusID + "','2','" + amountBalance + "','" + loanId + "','" + formattedDateTime + "')");

                                    ResultSet cashReportsDetails = MySQL.execute("SELECT * FROM `customer_details` WHERE `cus_id` = '" + cusID + "'");

                                    if (cashReportsDetails.next()) {

                                        int cash_summary_status = 2; //installment
                                        String c_name = cashReportsDetails.getString("cus_full_name");
                                        String loan_id = "...";
                                        String description = "Installment";

                                        MySQL.execute("INSERT INTO `cash_reports` (`summary_status_id`,`c_name`,`c_nic`,`l_id`,`r_id`,`description`,`amount`,`date`)"
                                                + "VALUES ('" + cash_summary_status + "','" + c_name + "','" + cusNIC + "','" + loanId + "','" + bankreceiptId + "','" + description + "','" + amountBalance + "','" + cashDate + "') ");

                                    }

                                    loadTable();
                                    clearFields();
                                    
                                   JOptionPane.showMessageDialog(this, "Instalment Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                                }

                                System.out.println("Bank");
                            }

                        }

                    } else {

                        JOptionPane.showMessageDialog(this, "Availible loan balance is " + loan_balance, "Warning", JOptionPane.WARNING_MESSAGE);

                    }

                } else {
                    // not have loan
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        searchInstallment();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String cusId = this.cusId;
        ViewActiveLoans vl = new ViewActiveLoans(this, true, cusId);
        vl.setInstalmentReports(this);
        vl.setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            String cusNic = jTextField1.getText();

            if (cusNic.isEmpty()) {
                JOptionPane.showMessageDialog(this, "please enter Customer NIC", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                try {

                    ResultSet rs = MySQL.execute("SELECT * FROM `customer_details` WHERE `cus_nic` = '" + cusNic + "' ");

                    if (rs.next()) {
                        jTextField2.setText(rs.getString("cus_full_name"));

                        this.cusId = rs.getString("cus_id");

                    } else {

                        JOptionPane.showMessageDialog(this, "Invalid Customer NIC. Please try again later", "Warning", JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jRadioButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton1ItemStateChanged

        ButtonModel dipositeSelection = buttonGroup1.getSelection();

        genarateRecieptNmber();

        if (dipositeSelection.isSelected()) {

            try {

                ResultSet rs = MySQL.execute("SELECT * FROM `installments` WHERE `receipt_id` = '" + rId + "' ");

                if (rs.next()) {
                    int response = JOptionPane.showConfirmDialog(null, "Receipt ID alrady exsists. Do you want to regenarate receipt ID? ", "Confirmation", JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        // Code to execute if the user clicked Yes
                        genarateRecieptNmber();

                        jTextField7.setText(rId);
                        jTextField7.setEditable(false);
                    } else if (response == JOptionPane.NO_OPTION) {
                        // Code to execute if the user clicked No
                        genarateRecieptNmber();

                        jTextField7.setText(rId);
                        jTextField7.setEditable(false);

                    }

                } else {

                    jTextField7.setText(rId);
                    jTextField7.setEditable(false);

                }

            } catch (Exception e) {
            }

        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ItemStateChanged

    private void jRadioButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton2ItemStateChanged

        jTextField7.setText("B");
        jTextField7.setEditable(true);

        jTextField7.grabFocus();

        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ItemStateChanged

    private void jTextField8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyPressed

        searchInstallment();

// TODO add your handling code here:
    }//GEN-LAST:event_jTextField8KeyPressed

    private void jTextField10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyPressed

        searchInstallment();

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10KeyPressed

    private void jTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyReleased

        searchInstallment();
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8KeyReleased

    private void jTextField10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10KeyReleased

        searchInstallment();
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        String cusNic = jTextField1.getText();

            if (cusNic.isEmpty()) {
                JOptionPane.showMessageDialog(this, "please enter Customer NIC", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {

                try {

                    ResultSet rs = MySQL.execute("SELECT * FROM `customer_details` WHERE `cus_nic` = '" + cusNic + "' ");

                    if (rs.next()) {
                        jTextField2.setText(rs.getString("cus_full_name"));

                        this.cusId = rs.getString("cus_id");

                    } else {

                        JOptionPane.showMessageDialog(this, "Invalid Customer NIC. Please try again later", "Warning", JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private datechooser.beans.DateChooserDialog dateChooserDialog1;
    private org.jdatepicker.impl.DateComponentFormatter dateComponentFormatter1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private org.jdatepicker.impl.SqlDateModel sqlDateModel1;
    private org.jdatepicker.impl.UtilCalendarModel utilCalendarModel1;
    // End of variables declaration//GEN-END:variables
}
