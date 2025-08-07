/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import Model.MySQL;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PassDueReports extends javax.swing.JPanel {

    /**
     * Creates new form PassDueReports
     */
    public PassDueReports() {
        initComponents();
        loadTable();
    }
    
    
    
    public void search(){
        
       String search_cus_nic = jTextField4.getText();
       System.out.println(search_cus_nic);
       
        try {

            ResultSet rs = MySQL.execute("SELECT * FROM `loan_details` "
                    + " INNER JOIN `customer_details` ON `customer_details`.`cus_id` = `loan_details`.`cus_id`"
                    + " WHERE `cus_nic` LIKE '%" + search_cus_nic + "%' ");
            
            if (rs.next()) {
                 System.out.println("have search_cus_nic");
               String loanAmount = rs.getString("loan_amount");               
               String loanBalance = rs.getString("loan_balance");
               String remainingd_preimium = rs.getString("paid_loan_period");

               jTextField6.setText(loanAmount);
               jTextField2.setText(loanBalance);
               jTextField3.setText(remainingd_preimium);
               
                
                
            }

//            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
//            model.setRowCount(0);
//
//            while (rs.next()) {
//                Vector v = new Vector();
//                v.add(rs.getString("cus_id"));
//                v.add(rs.getString("cus_nic"));
//                v.add(rs.getString("cus_full_name"));
//                v.add(rs.getString("mobile"));
//                v.add(rs.getString("address"));
//                v.add(rs.getString("register_date"));
//
//                model.addRow(v);
//            }
//            jTable1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");
    
    public void loadTable() {

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String currentDate = dateFormat.format(date);

        String[] parts = currentDate.split("-");
        int year = Integer.parseInt(parts[0].trim());
        int month = Integer.parseInt(parts[1].trim());
        int day = Integer.parseInt(parts[2].trim());

        try {
            ResultSet rs = MySQL.execute("SELECT * FROM `loan_details` "
                    + " INNER JOIN `customer_details` ON `customer_details`.`cus_id` = `loan_details`.`cus_id` ");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                String loanRegisterDate = rs.getString("loan_activated_date");

                String[] loanRegisterParts = loanRegisterDate.split("-");
                int registerYear = Integer.parseInt(loanRegisterParts[0].trim());
                int registermonth = Integer.parseInt(loanRegisterParts[1].trim());
                int registerDate = Integer.parseInt(loanRegisterParts[2].trim());

                LocalDate loanStartDate = LocalDate.of(registerYear, registermonth, registerDate);

                LocalDate currentDate2 = LocalDate.now();

                long monthsBetween = ChronoUnit.MONTHS.between(loanStartDate, currentDate2);

                System.out.println(monthsBetween);

                int loanAmount = (int) Double.parseDouble(rs.getString("loan_amount"));
                int period = (int) Double.parseDouble(rs.getString("loan_period"));

                double installment = (double) loanAmount / period;

                System.out.println(installment);

                double paidByNowAmount = (double) installment * monthsBetween;

                System.out.println("paid By Now Amount is :" + paidByNowAmount);

                double paid_amount = Double.parseDouble(rs.getString("loan_amount")) - Double.parseDouble(rs.getString("loan_balance"));
                double not_paid_amount = paidByNowAmount - paid_amount;

                System.out.println("not paid amount is :" + not_paid_amount);

                if (not_paid_amount > 0) {
                    System.out.println("in the table");

                    int not_paid_installments = (int) Math.round(not_paid_amount / installment);
                    System.out.println("not_paid_installments is:" + not_paid_installments);

                    Vector<String> v = new Vector<>();
                    v.add(rs.getString("cus_nic"));
                    v.add(rs.getString("cus_full_name"));
                    v.add(rs.getString("loan_amount"));

                    if (not_paid_installments == 1) {
                        System.out.println("1a awaaa");
                        v.add(String.valueOf(df.format(not_paid_amount)));
                        v.add("");
                        v.add("");
                        v.add("");
                        v.add("");
                        v.add("");
                    } else if (not_paid_installments == 2) {
                        v.add("");
                        v.add(String.valueOf(df.format(not_paid_amount)));
                        v.add("");
                        v.add("");
                        v.add("");
                        v.add("");
                    } else if (not_paid_installments == 3) {
                        v.add("");
                        v.add("");
                        v.add(String.valueOf(df.format(not_paid_amount)));
                        v.add("");
                        v.add("");
                        v.add("");
                    } else if (not_paid_installments == 4) {
                        v.add("");
                        v.add("");
                        v.add("");
                        v.add(String.valueOf(df.format(not_paid_amount)));
                        v.add("");
                        v.add("");
                    } else if (not_paid_installments == 5) {
                        v.add("");
                        v.add("");
                        v.add("");
                        v.add("");
                        v.add(String.valueOf(df.format(not_paid_amount)));
                        v.add("");
                    } else if (not_paid_installments > 5) {
                        v.add("");
                        v.add("");
                        v.add("");
                        v.add("");
                        v.add("");
                        v.add(String.valueOf(df.format(not_paid_amount)));
                    }

                    v.add(rs.getString("mobile"));
                    model.addRow(v);

                } else {
                    System.out.println("out the table");
                }

            }
            jTable1.setModel(model);

        } catch (Exception ex) {
            Logger.getLogger(PassDueReports.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Customer NIC", "Cstomer Name", "Loan Amount ", "1 Premium", "2 Premium", "3 Premium", "4 Premium", "5 Premium", "5 Premium <", "Contact NO"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Print");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Customer NIC:");

        jLabel2.setText("Loan Amount");

        jLabel3.setText("Loan Balance");

        jLabel4.setText("Oustanding Premium");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(214, 214, 214)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton2))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String currentDate = dateFormat.format(date);
        
        HashMap<String , Object> map = new HashMap<>();
        map.put("Parameter1", currentDate);
        
        try {
                
                JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());
                JasperPrint reports = (JasperFillManager.fillReport(getClass().getResourceAsStream("/reports/pass_dev_reports.jasper"), map, dataSource));
                JasperViewer.viewReport(reports, false);
                
                JasperExportManager.exportReportToPdfFile(reports, "Pass_dev Reports.pdf");
                
            } catch (Exception e) {
                e.printStackTrace();
            }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        search();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
