
package admins;

import config.dbConnector;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.proteanit.sql.DbUtils;
import transition.TransitionsForm;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class sales extends  TransitionsForm{

    public sales() {
        initComponents();
        init();
        displayData();
//        productSold();
        countOfAllProducts();
        totalSales();
        dailySales();
        
        back.setVisible(false);
    }
    
    public static String getCurrentDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return today.format(formatter);
    }
    
    public void displayMonthlySales() {
        try {
            dbConnector dbc = new dbConnector();

            ResultSet rs = dbc.getData(
                "SELECT YEAR(sales.Date) AS Year, MONTH(sales.Date) AS Month, " +
                "SUM(product_table.Price * sales.Quantity_Sold) AS Total_Sales " +
                "FROM sales " +
                "JOIN product_table ON sales.products_Id = product_table.Id " +
                "GROUP BY YEAR(sales.Date), MONTH(sales.Date) " +
                "ORDER BY Year DESC, Month DESC"
            );

            sales_list.setModel(DbUtils.resultSetToTableModel(rs));
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }
    
    public void displayDailySales() {
        try {
            dbConnector dbc = new dbConnector();

            ResultSet rs = dbc.getData(
                "SELECT DATE(sales.Date) AS Sale_Date, " +
                "SUM(product_table.Price * sales.Quantity_Sold) AS Total_Sales " +
                "FROM sales " +
                "JOIN product_table ON sales.products_Id = product_table.Id " +
                "GROUP BY DATE(sales.Date) " +
                "ORDER BY Sale_Date DESC"
            );

            sales_list.setModel(DbUtils.resultSetToTableModel(rs));
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }


    
//    private void init(){
//        product.fixTable(scrollPaneWin111);
//    }
//    
//    public boolean refresh = false;
//    
//    public void displayData(){
//        try{
//            dbConnector dbc = new dbConnector();
//            ResultSet rs = dbc.getData("SELECT Id, Product_Name, Category, Price, Quantity FROM product_table");
//            product.setModel(DbUtils.resultSetToTableModel(rs));
//            rs.close();
//        }catch(SQLException ex){
//            System.out.println("Errors: "+ex.getMessage());
//        }
//    }
    
    private void init(){
        sales_list.fixTable(jScrollPane1);
    }
    
//    public void displayData(){
//        try{
//            dbConnector dbc = new dbConnector();
//            ResultSet rs = dbc.getData("SELECT sales.Id AS Sale_Id, product_table.Product_Name, product_table.Category, " +
//                       "product_table.Price, sales.Quantity_Sold " +
//                       "FROM sales " +
//                       "JOIN product_table ON sales.products_Id = product_table.Id");
//            sales_list.setModel(DbUtils.resultSetToTableModel(rs));
//            rs.close();
//        }catch(SQLException ex){
//            System.out.println("Errors: "+ex.getMessage());
//        }
//    }
//    public void displayData(){
//        try {
//            dbConnector dbc = new dbConnector();
//            ResultSet rs = dbc.getData(
//                "SELECT sales.Id AS Sale_Id, product_table.Product_Name, product_table.Category, " +
//                "product_table.Price, sales.Quantity_Sold " +
//                "FROM sales " +
//                "JOIN product_table ON sales.products_Id = product_table.Id " +
//                "ORDER BY sales.Quantity_Sold DESC" // Order by Quantity_Sold in descending order
//            );
//            sales_list.setModel(DbUtils.resultSetToTableModel(rs));
//            rs.close();
//        } catch (SQLException ex) {
//            System.out.println("Errors: " + ex.getMessage());
//        }
//    }
    
    public void displayData() {
        try {
            dbConnector dbc = new dbConnector();
            ResultSet rs = dbc.getData(
                "SELECT product_table.Product_Name, product_table.Category, " +
                "product_table.Price, SUM(sales.Quantity_Sold) AS Total_Quantity_Sold, " +
                "SUM(product_table.Price * sales.Quantity_Sold) AS Total_Sales " +
                "FROM sales " +
                "JOIN product_table ON sales.products_Id = product_table.Id " +
                "GROUP BY product_table.Product_Name, product_table.Category, product_table.Price " +
                "ORDER BY Total_Quantity_Sold DESC"
            );
            sales_list.setModel(DbUtils.resultSetToTableModel(rs));
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }


    public void productSold() {
    try {
        dbConnector dbc = new dbConnector();

        // Query to count distinct products sold
        ResultSet rs = dbc.getData(
            "SELECT COUNT(DISTINCT products_Id) AS NROWS FROM sales"
        );

        int rowCount = 0;
        if (rs.next()) {
            rowCount = rs.getInt("NROWS");
        } else {
            // If the result set is empty, you can handle it
            System.out.println("No records found in the sales table.");
        }

        // Display the count of distinct products sold
        topSale.setText("" + rowCount + " ");
    } catch (SQLException ex) {
        System.out.println("Errors: " + ex.getMessage());
    }
}



    
    
    public void totalSales() {
        try {
            dbConnector dbc = new dbConnector();

            Calendar calendar = Calendar.getInstance();
            int currentMonth = calendar.get(Calendar.MONTH) + 1; 
            int currentYear = calendar.get(Calendar.YEAR);

            ResultSet rs = dbc.getData(
                "SELECT SUM(product_table.Price * sales.Quantity_Sold) AS TotalSales " +
                "FROM sales " +
                "JOIN product_table ON sales.products_Id = product_table.Id " +
                "WHERE MONTH(sales.Date) = " + currentMonth + " " + 
                "AND YEAR(sales.Date) = " + currentYear
            );

            double totalSales = 0;
            if (rs.next()) {
                totalSales = rs.getDouble("TotalSales");
            }
            totalSale.setText("₱" + String.format("%.2f", totalSales));
        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }
    
//    public void totalSales() {
//        try {
//            dbConnector dbc = new dbConnector();
//            ResultSet rs = dbc.getData(
//                "SELECT SUM(product_table.Price * sales.Quantity_Sold) AS TotalSales " +
//                "FROM sales " +
//                "JOIN product_table ON sales.products_Id = product_table.Id"
//            );
//            double totalSales = 0;
//            if (rs.next()) {
//                totalSales = rs.getDouble("TotalSales"); 
//            }
//            totalSale.setText("₱" + String.format("%.2f", totalSales)); 
//        } catch (SQLException ex) {
//            System.out.println("Errors: " + ex.getMessage());
//        }
//    }
    
//    public void dailySales() {
//        try {
//            dbConnector dbc = new dbConnector();
//            ResultSet rs = dbc.getData(
//                "SELECT SUM(product_table.Price * sales.Quantity_Sold) AS TotalSales " +
//                "FROM sales " +
//                "JOIN product_table ON sales.products_Id = product_table.Id"
//            );
//            double totalSales = 0;
//            if (rs.next()) {
//                totalSales = rs.getDouble("TotalSales"); 
//            }
//            dailySales.setText("₱" + String.format("%.2f", totalSales)); 
//        } catch (SQLException ex) {
//            System.out.println("Errors: " + ex.getMessage());
//        }
//    }
    public void dailySales() {
        try {
            dbConnector dbc = new dbConnector();
            String currentDate = getCurrentDate();
            ResultSet rs = dbc.getData(
                "SELECT SUM(product_table.Price * sales.Quantity_Sold) AS TotalSales " +
                "FROM sales " +
                "JOIN product_table ON sales.products_Id = product_table.Id " +
                "WHERE sales.Date = '" + currentDate + "'" 
            );
            
            double totalSales = 0;
            if (rs.next()) {
                totalSales = rs.getDouble("TotalSales"); 
            }
            dailySales.setText("₱" + String.format("%.2f", totalSales)); 
        } catch (SQLException ex) {
            System.out.println("Errors: " + ex.getMessage());
        }
    }
    
   public void countOfAllProducts() {
    try {
        dbConnector dbc = new dbConnector();

        // Query to count distinct products sold (based on products_Id)
        ResultSet rs = dbc.getData(
            "SELECT COUNT(DISTINCT products_Id) AS UniqueProductCount " +
            "FROM sales"
        );
        
        int uniqueProductCount = 0;
        if (rs.next()) {
            uniqueProductCount = rs.getInt("UniqueProductCount");
        }
        
        // Update the label with the count of distinct products sold
        topSale.setText("" + uniqueProductCount + " ");
        rs.close();
    } catch (SQLException ex) {
        System.out.println("Errors: " + ex.getMessage());
    }
}



    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        roundPanel5 = new panelRoundComponents.RoundPanel();
        jScrollPane1 = new scrollPane.ScrollPaneWin11();
        sales_list = new table.Table();
        label = new javax.swing.JLabel();
        print = new panelRoundComponents.PanelRound();
        jLabel5 = new javax.swing.JLabel();
        back = new javax.swing.JLabel();
        searchBar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        panelRound1 = new panelRoundComponents.PanelRound();
        jLabel2 = new javax.swing.JLabel();
        totalSale = new javax.swing.JLabel();
        panelRound2 = new panelRoundComponents.PanelRound();
        jLabel3 = new javax.swing.JLabel();
        topSale = new javax.swing.JLabel();
        panelRound3 = new panelRoundComponents.PanelRound();
        jLabel4 = new javax.swing.JLabel();
        dailySales = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1030, 527));

        jPanel1.setBackground(new java.awt.Color(248, 248, 248));

        jPanel2.setBackground(new java.awt.Color(245, 245, 245));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1030, 490));

        roundPanel5.setBackground(new java.awt.Color(255, 255, 255));
        roundPanel5.setRound(10);

        jScrollPane1.setBorder(null);

        sales_list.setForeground(new java.awt.Color(153, 153, 153));
        sales_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        sales_list.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        sales_list.setShowVerticalLines(false);
        jScrollPane1.setViewportView(sales_list);

        label.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        label.setForeground(new java.awt.Color(153, 153, 153));
        label.setText("Top Sale's");

        print.setBackground(new java.awt.Color(83, 215, 105));
        print.setRoundBottomLeft(10);
        print.setRoundBottomRight(10);
        print.setRoundTopLeft(10);
        print.setRoundTopRight(10);
        print.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Print");

        javax.swing.GroupLayout printLayout = new javax.swing.GroupLayout(print);
        print.setLayout(printLayout);
        printLayout.setHorizontalGroup(
            printLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap())
        );
        printLayout.setVerticalGroup(
            printLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/back-arrow.png"))); // NOI18N
        back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                backMouseClicked(evt);
            }
        });

        searchBar.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        searchBar.setForeground(new java.awt.Color(102, 102, 102));
        searchBar.setText("Search");
        searchBar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(245, 245, 245), 2, true));
        searchBar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchBarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchBarFocusLost(evt);
            }
        });
        searchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBarActionPerformed(evt);
            }
        });
        searchBar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchBarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchBarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout roundPanel5Layout = new javax.swing.GroupLayout(roundPanel5);
        roundPanel5.setLayout(roundPanel5Layout);
        roundPanel5Layout.setHorizontalGroup(
            roundPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addGroup(roundPanel5Layout.createSequentialGroup()
                        .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(roundPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(roundPanel5Layout.createSequentialGroup()
                    .addGap(307, 307, 307)
                    .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(308, Short.MAX_VALUE)))
        );
        roundPanel5Layout.setVerticalGroup(
            roundPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(back, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundPanel5Layout.createSequentialGroup()
                        .addGroup(roundPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(roundPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(roundPanel5Layout.createSequentialGroup()
                    .addGap(136, 136, 136)
                    .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(137, Short.MAX_VALUE)))
        );

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("Dashboard");

        panelRound1.setBackground(new java.awt.Color(255, 255, 255));
        panelRound1.setForeground(new java.awt.Color(0, 204, 0));
        panelRound1.setRoundBottomLeft(20);
        panelRound1.setRoundBottomRight(20);
        panelRound1.setRoundTopLeft(20);
        panelRound1.setRoundTopRight(20);
        panelRound1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRound1MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 128, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Total Sales");

        totalSale.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        totalSale.setForeground(new java.awt.Color(0, 128, 0));
        totalSale.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalSale.setText("0");

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(totalSale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(totalSale)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        panelRound2.setBackground(new java.awt.Color(255, 255, 255));
        panelRound2.setForeground(new java.awt.Color(0, 204, 0));
        panelRound2.setRoundBottomLeft(20);
        panelRound2.setRoundBottomRight(20);
        panelRound2.setRoundTopLeft(20);
        panelRound2.setRoundTopRight(20);
        panelRound2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRound2MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(41, 123, 250));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Product");

        topSale.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        topSale.setForeground(new java.awt.Color(41, 123, 250));
        topSale.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topSale.setText("0");

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(topSale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(topSale)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        panelRound3.setBackground(new java.awt.Color(255, 255, 255));
        panelRound3.setForeground(new java.awt.Color(0, 204, 0));
        panelRound3.setRoundBottomLeft(20);
        panelRound3.setRoundBottomRight(20);
        panelRound3.setRoundTopLeft(20);
        panelRound3.setRoundTopRight(20);
        panelRound3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panelRound3MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(252, 61, 57));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Daily Sales");

        dailySales.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        dailySales.setForeground(new java.awt.Color(252, 61, 57));
        dailySales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dailySales.setText("0");

        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                    .addComponent(dailySales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelRound3Layout.setVerticalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(dailySales)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(roundPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(41, 41, 41)
                            .addComponent(panelRound2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelRound2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(roundPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void printMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printMouseClicked
        try {
            boolean print = sales_list.print();
            if (!print) {
                JOptionPane.showMessageDialog(null, "Unable To Print !!..");
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_printMouseClicked

    private void panelRound1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound1MouseClicked
        displayMonthlySales();
        label.setText("Monthly Sales");
        back.setVisible(true);
    }//GEN-LAST:event_panelRound1MouseClicked

    private void backMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backMouseClicked
        displayData();
        label.setText("Top Sale's");
        back.setVisible(false);
    }//GEN-LAST:event_backMouseClicked

    private void panelRound3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound3MouseClicked
        displayDailySales();
        label.setText("Daily Sales");
        back.setVisible(true);
    }//GEN-LAST:event_panelRound3MouseClicked

    private void searchBarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchBarFocusGained
        if (searchBar.getText().equals("Search")){
            searchBar.setText("");
            searchBar.setForeground(new Color(153,153,153));
        }
    }//GEN-LAST:event_searchBarFocusGained

    private void searchBarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchBarFocusLost
        if (searchBar.getText().equals("")){
            searchBar.setText("Search");
            searchBar.setForeground(new Color(153,153,153));
        }
    }//GEN-LAST:event_searchBarFocusLost

    private void searchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBarActionPerformed
        
    }//GEN-LAST:event_searchBarActionPerformed

    private void searchBarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBarKeyPressed
        
    }//GEN-LAST:event_searchBarKeyPressed

    private void searchBarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBarKeyReleased
        
     

    }//GEN-LAST:event_searchBarKeyReleased

    private void panelRound2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MouseClicked
        displayData();
        back.setVisible(false);
        label.setText("Top Sale's");
    }//GEN-LAST:event_panelRound2MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel back;
    private javax.swing.JLabel dailySales;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label;
    private panelRoundComponents.PanelRound panelRound1;
    private panelRoundComponents.PanelRound panelRound2;
    private panelRoundComponents.PanelRound panelRound3;
    private panelRoundComponents.PanelRound print;
    private panelRoundComponents.RoundPanel roundPanel5;
    private table.Table sales_list;
    private javax.swing.JTextField searchBar;
    private javax.swing.JLabel topSale;
    private javax.swing.JLabel totalSale;
    // End of variables declaration//GEN-END:variables
}
