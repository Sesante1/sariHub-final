
package admins;

import config.dbConnector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;


public class barGraph extends  TransitionsForm{

    public barGraph() {
        initComponents();
        displayBarChart();
        displayDailySalesBarChart();
    }
    
    public static String getCurrentDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return today.format(formatter);
    }
    
    public void displayBarChart() {
        try {
            // Get today's date
            LocalDate today = LocalDate.now();
            String todayDate = Date.valueOf(today).toString();

            dbConnector dbc = new dbConnector();

            // Query to get total sales for each product for today
            ResultSet rs = dbc.getData(
                "SELECT p.Product_Name AS ProductName, " +
                "SUM(p.Price * s.Quantity_Sold) AS TotalSales " +
                "FROM sales s " +
                "JOIN product_table p ON s.products_id = p.Id " +
                "WHERE s.Date = '" + todayDate + "' " +
                "GROUP BY p.Product_Name " +
                "ORDER BY TotalSales DESC"
            );

            // Create a dataset for the bar chart
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // Populate the dataset with data from the result set
            while (rs.next()) {
                String productName = rs.getString("ProductName");
                double totalSales = rs.getDouble("TotalSales");
                dataset.addValue(totalSales, "Total Sales", productName);
            }

            // If no data is found for today's sales, add a default value
            if (dataset.getRowCount() == 0) {
                dataset.addValue(0, "Total Sales", "No Sales Today");
            }

            // Create the bar chart using the dataset
            JFreeChart barChart = ChartFactory.createBarChart(
                "Earnings Statistics",  
                null,  
                null,  
                dataset,  // Dataset for the chart
                PlotOrientation.VERTICAL,
                false,  // No legend
                true,   // Tooltips enabled
                false   // URLs disabled
            );

            // Customize the chart appearance
            CategoryPlot categoryPlot = barChart.getCategoryPlot();
            categoryPlot.setBackgroundPaint(Color.WHITE); // Set background color
            categoryPlot.setOutlineVisible(false); // Remove outline
            categoryPlot.setDomainGridlinesVisible(false); // Hide X-axis gridlines
            categoryPlot.setRangeGridlinesVisible(false);  // Hide Y-axis gridlines

            // Customize the renderer (bar color)
            BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
            renderer.setSeriesPaint(0, new Color(68,203,149)); // Green bars
            renderer.setBarPainter(new StandardBarPainter()); // Solid bars, no gradient
            renderer.setShadowVisible(false); // No shadow effect

            // Create a ChartPanel to hold the chart
            ChartPanel chartPanel = new ChartPanel(barChart);
            chartPanel.setPreferredSize(new Dimension(600, 400)); // Set panel size

            // Clear and add the chart to the "barChart" panel
            barChartPanel.removeAll();  // Clear existing content in the panel
            barChartPanel.setLayout(new BorderLayout());
            barChartPanel.add(chartPanel, BorderLayout.CENTER); // Add the chart to the panel
            barChartPanel.validate(); // Revalidate the panel to apply changes
            barChartPanel.repaint(); // Repaint the panel to refresh the display

            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    public void displayDailySalesBarChart() {
        try {
            dbConnector dbc = new dbConnector();

            // Query to get daily sales grouped by date
            ResultSet rs = dbc.getData(
                "SELECT sales.Date AS SaleDate, SUM(product_table.Price * sales.Quantity_Sold) AS TotalSales " +
                "FROM sales " +
                "JOIN product_table ON sales.products_Id = product_table.Id " +
                "GROUP BY sales.Date " +
                "ORDER BY sales.Date ASC"
            );

            // Create a dataset for the bar chart
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            ArrayList<String> formattedLabels = new ArrayList<>(); // Store formatted labels

            // Populate the dataset with daily sales data and formatted labels
            while (rs.next()) {
                String saleDate = rs.getString("SaleDate");
                double totalSales = rs.getDouble("TotalSales");
                dataset.addValue(totalSales, "Daily Sales", saleDate);

                // Split the date and format it
                String[] dateParts = saleDate.split("-");
                String day = dateParts[2];  // Day part
                String month = getMonthName(Integer.parseInt(dateParts[1])); // Month part (converted to string)

                // Create custom label with Month and Day
                formattedLabels.add(month + " " + day);  // Format as "Dec 10"
            }

            // Create the bar chart
            JFreeChart barChart = ChartFactory.createBarChart(
                "Daily Sales",      // Chart title
                null,               // X-axis label
                null,  // Y-axis label
                dataset,            // Dataset
                PlotOrientation.VERTICAL,
                false,              // No legend
                true,               // Tooltips enabled
                false               // URLs disabled
            );

            // Customize the chart appearance
            CategoryPlot categoryPlot = barChart.getCategoryPlot();
            categoryPlot.setBackgroundPaint(Color.WHITE); // Set background color
            categoryPlot.setOutlineVisible(false);        // Remove outline
            categoryPlot.setDomainGridlinesVisible(false); // Hide X-axis gridlines
            categoryPlot.setRangeGridlinesVisible(true);  // Show Y-axis gridlines

            // Customize the renderer (bar color)
            BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
            renderer.setSeriesPaint(0, new Color(68,203,149)); // Green bar
            renderer.setBarPainter(new StandardBarPainter()); // Solid bars
            renderer.setShadowVisible(false);                // No shadow effect

            // Set tooltips for the bars using CategoryToolTipGenerator
            renderer.setToolTipGenerator(new CategoryToolTipGenerator() {
                @Override
                public String generateToolTip(CategoryDataset dataset, int row, int column) {
                    // Use the formatted label for tooltips
                    return formattedLabels.get(column);
                }
            });

            // Set customized labels using CategoryAxis tick labels
            CategoryAxis domainAxis = categoryPlot.getDomainAxis();

            // Update the labels manually using the formatted labels
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // Rotate labels for better readability

            // Add the chart to a panel
            ChartPanel chartPanel = new ChartPanel(barChart);
            chartPanel.setPreferredSize(new Dimension(800, 600)); // Set panel size

            // Add the chart panel to the "dailyBarChart" panel
            dailyBarChart.removeAll();  // Remove existing components if any
            dailyBarChart.setLayout(new BorderLayout());
            dailyBarChart.add(chartPanel, BorderLayout.CENTER);  // Add the chart to the panel
            dailyBarChart.validate();
            dailyBarChart.repaint(); // Update the display to show the chart

            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error in displayDailySalesBarChart: " + ex.getMessage());
        }
    }
   
    private String getMonthName(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[month - 1];
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        roundPanel1 = new panelRoundComponents.RoundPanel();
        barChartPanel = new javax.swing.JPanel();
        dailyBarChart = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(1030, 527));

        jPanel1.setBackground(new java.awt.Color(248, 248, 248));

        jPanel2.setBackground(new java.awt.Color(245, 245, 245));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1030, 490));

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("Graph dashboard");

        roundPanel1.setBackground(new java.awt.Color(255, 255, 255));

        barChartPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(245, 245, 245), 2, true));
        barChartPanel.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        barChartPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dailyBarChart.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(245, 245, 245), 2, true));
        dailyBarChart.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        dailyBarChart.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(barChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(dailyBarChart, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel1Layout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dailyBarChart, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(barChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(72, 72, 72))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(roundPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addComponent(roundPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel barChartPanel;
    private javax.swing.JPanel dailyBarChart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private panelRoundComponents.RoundPanel roundPanel1;
    // End of variables declaration//GEN-END:variables
}
