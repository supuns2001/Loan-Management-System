/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import Model.MySQL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author user
 */
public class CashSummaryReports extends javax.swing.JPanel {

    /**
     * Creates new form CashSummaryReports
     */
    public CashSummaryReports() {
        initComponents();
        loadTable();
    }

    private static double loansTotal;
    private static double installmentTotal;
    private static double profit;

    public void loadTable() {
        try {
//            ResultSet rs = MySQL.execute("SELECT * FROM `cash_summary` "
//                    + " INNER JOIN `loan_details` ON `loan_details`.`loan_id` = `cash_summary`.`loan_id`"
//                    + " INNER JOIN `customer_details` ON `customer_details`.`cus_id` = `loan_details`.`cus_id`"
//                    + " INNER JOIN `installments` ON `installments`.`cus_id` = `customer_details`.`cus_id` ");

            ResultSet rs = MySQL.execute("SELECT * FROM `cash_reports` ");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {

                if (Integer.parseInt(rs.getString("summary_status_id")) == 1) {
                    System.out.println("loan");
                    System.out.println("loan amount " + rs.getString("amount"));
                    loansTotal += Double.parseDouble(rs.getString("amount"));
                    System.out.println("loans " + loansTotal);
                    Vector<String> v = new Vector<>();
                    v.add(rs.getString("c_nic"));
                    v.add(rs.getString("c_name"));
                    v.add(rs.getString("l_id"));
                    v.add(rs.getString("r_id"));
                    v.add(rs.getString("description"));
                    v.add(rs.getString("amount"));
                    v.add(rs.getString("date"));

                    model.addRow(v);

                } else if (Integer.parseInt(rs.getString("summary_status_id")) == 2) {
                    System.out.println("installment");
                    System.out.println("installment isss " + rs.getString("amount"));
                    installmentTotal += Double.parseDouble(rs.getString("amount"));
                    System.out.println("installment is " + installmentTotal);
                    Vector<String> v = new Vector<>();
                    v.add(rs.getString("c_nic"));
                    v.add(rs.getString("c_name"));
                    v.add(rs.getString("l_id"));
                    v.add(rs.getString("r_id"));
                    v.add(rs.getString("description"));
                    v.add(rs.getString("amount"));
                    v.add(rs.getString("date"));

                    model.addRow(v);
                }

            }
            jTable1.setModel(model);
            jLabel9.setText(String.valueOf(loansTotal));
            jLabel7.setText(String.valueOf(installmentTotal));

            jLabel1.setText(String.valueOf(installmentTotal - loansTotal));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search() {

        loansTotal = 0;
        installmentTotal = 0;
        profit = 0;

        try {

            String cs_nic = jTextPane1.getText();
            String loanId = jTextPane2.getText();

            

            Date to_date = jDateChooser1.getDate();
            Date from_date = jDateChooser2.getDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");

//            System.out.println(toDate);
//            System.out.println(fromDate);
            Vector<String> qv = new Vector<>();

            // customer NIC
            if (cs_nic.isEmpty()) {
            } else {
                qv.add("`c_nic`='" + cs_nic + "'");
            }

            // Loan ID
            if (loanId.isEmpty()) {
            } else {
                qv.add("`l_id`='" + loanId + "'");
            }

            // Loan or Installment
            if (jCheckBox1.isSelected() && !jCheckBox2.isSelected()) {
                qv.add("`summary_status_id`='1'"); //Loans
            } else if (!jCheckBox1.isSelected() && jCheckBox2.isSelected()) {
                qv.add("`summary_status_id`='2'"); //Installments
            } else {
                
            }

            //toDate
            if (to_date != null && from_date != null) {
                String fromDate = dateFormat.format(from_date);
                String toDate = dateFormat.format(to_date);
//                qv.add("`loan_details`.`loan_activated_date` BETWEEN '" + toDate + "' AND '" + fromDate + "'");
                qv.add("`date` BETWEEN '" + toDate + "' AND '" + fromDate + "'");

            } else if (to_date != null && from_date == null) {
                String toDate = dateFormat.format(to_date);
                qv.add("`date` = '" + toDate + "'");

            } else if (to_date == null && from_date != null) {
                String fromDate = dateFormat.format(from_date);
                qv.add("`date` = '" + fromDate + "'");
            } else {
                System.out.println("empty date fields");
            }

            String wherequery = " ";

            for (int i = 0; i < qv.size(); i++) {
                System.out.println(qv.size());
                if (i == 0) {
                    wherequery += "WHERE";
                }
                wherequery += " ";
                wherequery += qv.get(i);
                wherequery += " ";
                if (i != qv.size() - 1) {
                    wherequery += "AND";
                }
            }

            System.out.println(wherequery);

            ResultSet rs = MySQL.execute("SELECT * FROM `cash_reports` " + wherequery + "");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {

                if (Integer.parseInt(rs.getString("summary_status_id")) == 1) { //loan
                    loansTotal += Double.parseDouble(rs.getString("amount"));
                    System.out.println(loansTotal);
                    System.out.println("search ok2");
                    Vector<String> v = new Vector<>();
                    v.add(rs.getString("c_nic"));
                    v.add(rs.getString("c_name"));
                    v.add(rs.getString("l_id"));
                    v.add(rs.getString("r_id"));
                    v.add(rs.getString("description"));
                    v.add(rs.getString("amount"));
                    v.add(rs.getString("date"));

                    model.addRow(v);

                } else if (Integer.parseInt(rs.getString("summary_status_id")) == 2) { // installment
                    installmentTotal += Double.parseDouble(rs.getString("amount"));
                    System.out.println(installmentTotal);
                    Vector<String> v = new Vector<>();
                    v.add(rs.getString("c_nic"));
                    v.add(rs.getString("c_name"));
                    v.add(rs.getString("l_id"));
                    v.add(rs.getString("r_id"));
                    v.add(rs.getString("description"));
                    v.add(rs.getString("amount"));
                    v.add(rs.getString("date"));

                    model.addRow(v);
                }

            }
            jTable1.setModel(model);

            jLabel9.setText(String.valueOf(loansTotal));
            jLabel7.setText(String.valueOf(installmentTotal));

            jLabel1.setText(String.valueOf(installmentTotal - loansTotal));

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
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollBar1 = new javax.swing.JScrollBar();

        setPreferredSize(new java.awt.Dimension(1136, 583));

        jScrollPane2.setViewportView(jTextPane1);

        jScrollPane3.setViewportView(jTextPane2);

        jLabel3.setText("to Date");

        jLabel4.setText("from Date");

        jButton2.setText("Search");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel5.setText("Customer NIC");

        jLabel6.setText("Loan ID");

        jCheckBox1.setText("Loans");

        jCheckBox2.setText("Installments");

        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox1)))
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 419, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(53, 53, 53))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jCheckBox1)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(7, 7, 7))
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jCheckBox2)))
                                .addComponent(jLabel6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Customer NIC", "Customer Name", "Loan ID", "Receipt ID", "Description", "Amount", "Date"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("125000.00");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Cash Balance:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("125000.00");

        jLabel8.setText("Installments:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("125000.00");

        jLabel10.setText("Loans:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1052, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8)
                        .addComponent(jLabel9)
                        .addComponent(jLabel10))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        search();

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String currentDate = dateFormat.format(date);
        
        

        HashMap<String, Object> reportMap = new HashMap<>();
        reportMap.put("Parameter1", currentDate);
        
        // Loan or Installment
            if (jCheckBox1.isSelected() && !jCheckBox2.isSelected()) {
                reportMap.put("Parameter2", "Loans");
            } else if (!jCheckBox1.isSelected() && jCheckBox2.isSelected()) {
                reportMap.put("Parameter2", "Installments");
            } else {
                reportMap.put("Parameter2", " ");
            }
        reportMap.put("Parameter3", String.valueOf(jLabel9.getText()));
        reportMap.put("Parameter4", String.valueOf(jLabel7.getText()));
        reportMap.put("Parameter5", String.valueOf(jLabel1.getText()));
            
        

        try {

            JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());
            JasperPrint reports = (JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/cash_summary_report.jasper"), reportMap, dataSource));
            JasperViewer.viewReport(reports, false);

            JasperExportManager.exportReportToPdfFile(reports, "Cash Summary Report.pdf");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    // End of variables declaration//GEN-END:variables
}
