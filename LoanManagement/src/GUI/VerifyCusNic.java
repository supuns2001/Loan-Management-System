/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI;

import Model.MySQL;
import com.mysql.cj.protocol.Resultset;
import java.sql.ResultSet;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
public class VerifyCusNic extends javax.swing.JDialog {

    private RegisterCustomer registerCustomer;

    private File nicF;
    private File nicB;
    private File signature;

    private String userId;

    /**
     * Creates new form VerifyCusNic
     */
    public VerifyCusNic(javax.swing.JPanel parent, boolean modal, String customerID) {
        super((javax.swing.JFrame) SwingUtilities.getWindowAncestor(parent), modal);
        initComponents();
        this.userId = customerID;
        loadName();
        loadVerifyDetails();
    }
    
    public void loadName(){
        try {
            ResultSet name_rs = MySQL.execute("SELECT * FROM `customer_details` WHERE `cus_id` = '"+userId+"'");
            
            if (name_rs.next()) {
                String name= name_rs.getString("cus_full_name");
            System.out.println(name);
             jLabel5.setText(name+"'s verification details");
//             jLabel5.setText(name+"'s verification details(NIC and Signature) need to be include in this page.If not,add it immedialtely.");

            }else{
                System.out.println("no customer");
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadVerifyDetails() {
        loadName();
        try {
            // Fetch the result set
            ResultSet rs = MySQL.execute("SELECT * FROM `customer_verify_details` WHERE `cus_id` = '" + userId + "'");

            if (rs.next()) {
                // Retrieve and process NIC Front Side Image
                String nicF_image = rs.getString("nic_f_side");
                String actualNicFImageName = nicF_image.replaceFirst("verifyImg", "");
                loadImageToLabel(jLabel4, actualNicFImageName, "C:\\Users\\user\\Documents\\NetBeansProjects\\LoanManagement\\verifyImg\\");

                // Retrieve and process NIC Back Side Image
                String nicB_image = rs.getString("nic_b_side");
                String actualNicBImageName = nicB_image.replaceFirst("verifyImg", "");
                loadImageToLabel(jLabel2, actualNicBImageName, "C:\\Users\\user\\Documents\\NetBeansProjects\\LoanManagement\\verifyImg\\");

                // Retrieve and process Signature Image
                String signature_image = rs.getString("signature_img");
                String actualSignatureImageName = signature_image.replaceFirst("verifyImg", "");
                loadImageToLabel(jLabel3, actualSignatureImageName, "C:\\Users\\user\\Documents\\NetBeansProjects\\LoanManagement\\verifyImg\\");
            } else {
                System.out.println("No result set: Customer verify details not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while loading verification details.");
        }
    }

    /**
     * Helper method to load an image into a JLabel.
     *
     * @param label the JLabel to set the image on
     * @param imageName the image file name
     * @param basePath the base directory where images are stored
     */
    private void loadImageToLabel(JLabel label, String imageName, String basePath) {
        try {
            File imageFile = new File(basePath + imageName);

            if (imageFile.exists()) {
                ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = imageIcon.getImage().getScaledInstance(170, 133, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImage));
                System.out.println("Image successfully loaded: " + imageFile);
            } else {
                System.out.println("File not found: " + imageFile);
                label.setIcon(null); // Clear JLabel or set a default/fallback image
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while loading an image.");
        }
    }

    public void setRegisterCustomer(RegisterCustomer registerCustomer) {
        this.registerCustomer = registerCustomer;
    }

    public static String getFileExtension(File nicF) {
        String fileName = nicF.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            return ""; // No extension found
        }
    }

    private void saveVerifyImg(File nicF, File nicB, File signature, String userId) {
        System.out.println("okkk");
        try {

            // Create "verifyImage" directory if it doesn't exist
            Path imageFolder = Paths.get("verifyImg");
            if (!Files.exists(imageFolder)) {
                Files.createDirectory(imageFolder);
            }

            // Step 2: Rename the image file to match the user ID and save it to "verifyImage" folder
            String fileExtension = getFileExtension(nicF);  // Get the file extension
            String newFileName = userId + "Front." + fileExtension;
            Path destination = imageFolder.resolve(newFileName);  // Set the new file path

            Files.copy(nicF.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            String fileExtension2 = getFileExtension(nicB);  // Get the file extension
            String newFileName2 = userId + "Back." + fileExtension2;
            Path destination2 = imageFolder.resolve(newFileName2);  // Set the new file path

            Files.copy(nicB.toPath(), destination2, StandardCopyOption.REPLACE_EXISTING);

            String fileExtension3 = getFileExtension(signature);  // Get the file extension
            String newFileName3 = userId + "Signature." + fileExtension3;
            Path destination3 = imageFolder.resolve(newFileName3);  // Set the new file path

            Files.copy(signature.toPath(), destination3, StandardCopyOption.REPLACE_EXISTING);

            String relativePathNicF = destination.toString();
            String relativePathNicB = destination2.toString();
            String relativePathSignature = destination3.toString();

            ResultSet rs = MySQL.execute("INSERT INTO `customer_verify_details` (`cus_id`,`nic_f_side`,`nic_b_side`,`signature_img`) "
                    + " VALUES ('" + userId + "','" + relativePathNicF + "','" + relativePathNicB + "','" + relativePathSignature + "') ");
            
            int customer_status = 1;//Active ID
            MySQL.execute("UPDATE `customer_details` SET `status_id` = '"+customer_status+"' WHERE `cus_id` = '"+userId+"' ");
            int result = JOptionPane.showConfirmDialog(this, "Identity Verification Success", "Success", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                this.dispose();
            } else if (result == JOptionPane.CANCEL_OPTION) {
                System.out.println("User clicked Cancel.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadverifydetails() {

        try {
            ResultSet rs1 = MySQL.execute("SELECT * FROM `customer_verify_details` WHERE `cus_id` = '" + userId + "' ");

            if (rs1.next()) {

                String frontSide = rs1.getString("nic_f_side");

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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Verifications");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("NIC Front Side");

        jLabel2.setBackground(new java.awt.Color(102, 102, 102));
        jLabel2.setOpaque(true);

        jLabel3.setBackground(new java.awt.Color(102, 102, 102));
        jLabel3.setOpaque(true);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("NIC Back Side");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Enter Customer Signature");

        jLabel4.setBackground(new java.awt.Color(102, 102, 102));
        jLabel4.setOpaque(true);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("jButton1");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Add Verification details");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("jButton1");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(11, 11, 11)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel9))))
                            .addGap(33, 33, 33)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addGap(43, 43, 43)
                                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2))
                    .addComponent(jLabel7))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        this.nicF = f;
        if (f != null) {
            String path = f.getAbsolutePath();

            try {
                BufferedImage bi = ImageIO.read(new File(path));
                Image img = bi.getScaledInstance(170, 133, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(img);
                jLabel4.setIcon(icon);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Process the file path as needed
        } else {
            System.out.println("No file selected.");
            JOptionPane.showMessageDialog(this, "Customer details or Customer ID Number Already Registered. please try Again Later", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        this.nicB = f;
        if (f != null) {
            String path = f.getAbsolutePath();
            try {
                BufferedImage bi = ImageIO.read(new File(path));
                Image img = bi.getScaledInstance(170, 133, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(img);
                jLabel2.setIcon(icon);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Process the file path as needed
        } else {
            System.out.println("No file selected.");
            JOptionPane.showMessageDialog(this, "Customer details or Customer ID Number Already Registered. please try Again Later", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if (nicF != null && nicB != null && signature != null) {
            // Replace with actual user ID
            saveVerifyImg(nicF, nicB, signature, userId);
        } else {
            JOptionPane.showMessageDialog(null, "Please select the images");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        this.signature = f;
        if (f != null) {
            String path = f.getAbsolutePath();
            try {
                BufferedImage bi = ImageIO.read(new File(path));
                Image img = bi.getScaledInstance(170, 133, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(img);
                jLabel3.setIcon(icon);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Process the file path as needed
        } else {
            System.out.println("No file selected.");
            JOptionPane.showMessageDialog(this, "Customer details or Customer ID Number Already Registered. please try Again Later", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
