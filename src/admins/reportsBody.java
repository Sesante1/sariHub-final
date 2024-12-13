
package admins;

import config.dbConnector;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
import transition.TransitionsForm;


public class reportsBody extends TransitionsForm {

    public reportsBody() {
        initComponents();
        init();
        countOfAllProducts();
        availableStocks();
        outOfStocks();
    }
    
    public void countOfAllProducts() {
        try {
            dbConnector dbc = new dbConnector();

            ResultSet rs = dbc.getData(
                "SELECT COUNT(*) AS NROWS " +
                "FROM product_table " +
                "WHERE stat != 'archived'" 
            );
            int rowCount = 0;
            if (rs.next()) {
                rowCount = rs.getInt("NROWS");
            }
            productCount.setText("" + rowCount + " products");
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }
    
    public void availableStocks() {
        try {
            dbConnector dbc = new dbConnector();

            ResultSet rs = dbc.getData("SELECT SUM(Quantity) AS totalQuantity FROM product_table WHERE status = 'Available' AND stat != 'archived'");
            int totalQuantity = 0;
            if (rs.next()) {
                totalQuantity = rs.getInt("totalQuantity");
            }
            availableStocks.setText("" + totalQuantity + " " + "stocks");
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }


    
    public void outOfStocks() {
        try {
            dbConnector dbc = new dbConnector();

            ResultSet rs = dbc.getData("SELECT COUNT(*) AS NROWS FROM product_table WHERE status = 'Out of stock' AND stat != 'archived'");
            int rowCount = 0;
            if (rs.next()) {
                rowCount = rs.getInt("NROWS");
            }
            outOfStocks.setText("" + rowCount + " " + "products");
        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }


//    public void countProduct(){
//        try{
//            dbConnector dbc = new dbConnector();
//            ResultSet rs = dbc.getData("SELECT Id, Product_Name, Price, Quantity, status FROM product_table WHERE stat != 'archived'");
//            products.setModel(DbUtils.resultSetToTableModel(rs));
//            rs.close();
//        }catch(SQLException ex){
//            System.out.println("Errors: "+ex.getMessage());
//        }
//    }
    public void countProduct() {
    try {
        dbConnector dbc = new dbConnector();
        ResultSet rs = dbc.getData("SELECT Id, Product_Name, Price, Quantity, status FROM product_table WHERE stat != 'archived'");
        products.setModel(DbUtils.resultSetToTableModel(rs));

        // Set custom cell renderer for the status column
        products.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                // Get the default cell renderer component
                java.awt.Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Check the value in the status column
                String status = value != null ? value.toString() : "";

                // Change the font color based on the status
                if ("available".equalsIgnoreCase(status)) {
                    cell.setForeground(java.awt.Color.GREEN); // Green text for available
                } else if ("out of stock".equalsIgnoreCase(status)) {
                    cell.setForeground(java.awt.Color.RED); // Red text for out of stock
                } else {
                    cell.setForeground(java.awt.Color.BLACK); // Default black text for other statuses
                }

                return cell;
            }
        });

        rs.close();
    } catch (SQLException ex) {
        System.out.println("Errors: " + ex.getMessage());
    }
}



    
    public void availableStock() {
        try {
            dbConnector dbc = new dbConnector();
            
            ResultSet rs = dbc.getData("SELECT Id, Product_Name, Price, Quantity FROM product_table WHERE status = 'Available' AND Quantity > 0 AND stat != 'archived'");
            products.setModel(DbUtils.resultSetToTableModel(rs));
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }


    public void outOfStock() {
        try {
            dbConnector dbc = new dbConnector();
            
            ResultSet rs = dbc.getData("SELECT Id, Product_Name, Price, Quantity FROM product_table WHERE Quantity <= 0 AND stat != 'archived'");
            products.setModel(DbUtils.resultSetToTableModel(rs));
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }


    
    private void init(){
        products.fixTable(jScrollPane1);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel1 = new panelRoundComponents.RoundPanel();
        lbDescription = new javax.swing.JLabel();
        productCount = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        roundPanel2 = new panelRoundComponents.RoundPanel();
        lbDescription1 = new javax.swing.JLabel();
        availableStocks = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        roundPanel5 = new panelRoundComponents.RoundPanel();
        jScrollPane1 = new scrollPane.ScrollPaneWin11();
        products = new table.Table();
        label = new javax.swing.JLabel();
        roundPanel3 = new panelRoundComponents.RoundPanel();
        lbDescription2 = new javax.swing.JLabel();
        outOfStocks = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(245, 245, 245));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        roundPanel1.setBackground(new java.awt.Color(255, 255, 255));
        roundPanel1.setRound(15);

        lbDescription.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbDescription.setForeground(new java.awt.Color(0, 128, 0));
        lbDescription.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDescription.setText("Total Products");

        productCount.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        productCount.setForeground(new java.awt.Color(0, 128, 0));
        productCount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productCount.setText(" 0");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(128, 128, 128));
        jLabel1.setText("View");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel1Layout.createSequentialGroup()
                        .addGap(0, 202, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addContainerGap())
            .addComponent(lbDescription, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(lbDescription)
                .addGap(28, 28, 28)
                .addComponent(productCount)
                .addGap(26, 26, 26))
        );

        add(roundPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 20, -1, -1));

        roundPanel2.setBackground(new java.awt.Color(255, 255, 255));
        roundPanel2.setRound(15);

        lbDescription1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbDescription1.setForeground(new java.awt.Color(41, 123, 250));
        lbDescription1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDescription1.setText("Quantity per product");

        availableStocks.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        availableStocks.setForeground(new java.awt.Color(41, 123, 250));
        availableStocks.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        availableStocks.setText(" 0");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(128, 128, 128));
        jLabel2.setText("View");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout roundPanel2Layout = new javax.swing.GroupLayout(roundPanel2);
        roundPanel2.setLayout(roundPanel2Layout);
        roundPanel2Layout.setHorizontalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(availableStocks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel2Layout.createSequentialGroup()
                        .addGap(0, 202, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addContainerGap())
            .addComponent(lbDescription1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        roundPanel2Layout.setVerticalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbDescription1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(availableStocks)
                .addGap(28, 28, 28))
        );

        add(roundPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(387, 20, -1, 142));

        roundPanel5.setBackground(new java.awt.Color(255, 255, 255));
        roundPanel5.setRound(10);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        products.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        products.setForeground(new java.awt.Color(153, 153, 153));
        products.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        products.setEnabled(false);
        products.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        products.setShowVerticalLines(false);
        jScrollPane1.setViewportView(products);

        label.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        label.setForeground(new java.awt.Color(153, 153, 153));
        label.setText("Product's");

        javax.swing.GroupLayout roundPanel5Layout = new javax.swing.GroupLayout(roundPanel5);
        roundPanel5.setLayout(roundPanel5Layout);
        roundPanel5Layout.setHorizontalGroup(
            roundPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
                    .addGroup(roundPanel5Layout.createSequentialGroup()
                        .addComponent(label)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        roundPanel5Layout.setVerticalGroup(
            roundPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(roundPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 212, -1, -1));

        roundPanel3.setBackground(new java.awt.Color(255, 255, 255));
        roundPanel3.setRound(15);

        lbDescription2.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbDescription2.setForeground(new java.awt.Color(252, 61, 57));
        lbDescription2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDescription2.setText(" Out of stock");

        outOfStocks.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        outOfStocks.setForeground(new java.awt.Color(252, 61, 57));
        outOfStocks.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        outOfStocks.setText(" 0");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(128, 128, 128));
        jLabel3.setText("View");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout roundPanel3Layout = new javax.swing.GroupLayout(roundPanel3);
        roundPanel3.setLayout(roundPanel3Layout);
        roundPanel3Layout.setHorizontalGroup(
            roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outOfStocks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel3Layout.createSequentialGroup()
                        .addGap(0, 202, Short.MAX_VALUE)
                        .addComponent(jLabel3)))
                .addContainerGap())
            .addComponent(lbDescription2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        roundPanel3Layout.setVerticalGroup(
            roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbDescription2)
                .addGap(27, 27, 27)
                .addComponent(outOfStocks)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        add(roundPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(702, 20, -1, 142));

        jPanel1.setBackground(new java.awt.Color(245, 245, 245));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1030, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 510));
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        countProduct();
        label.setText("Count of all product's");
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        availableStock();
        label.setText("Available stock's");
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        outOfStock();
        label.setText("Out of stock's");
    }//GEN-LAST:event_jLabel3MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel availableStocks;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label;
    private javax.swing.JLabel lbDescription;
    private javax.swing.JLabel lbDescription1;
    private javax.swing.JLabel lbDescription2;
    private javax.swing.JLabel outOfStocks;
    private javax.swing.JLabel productCount;
    private table.Table products;
    private panelRoundComponents.RoundPanel roundPanel1;
    private panelRoundComponents.RoundPanel roundPanel2;
    private panelRoundComponents.RoundPanel roundPanel3;
    private panelRoundComponents.RoundPanel roundPanel5;
    // End of variables declaration//GEN-END:variables
}
