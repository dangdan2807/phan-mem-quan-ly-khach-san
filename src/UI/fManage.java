package UI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

import DAO.*;
import entity.*;
import entity.Menu;

public class fManage extends JFrame implements ActionListener, MouseListener, ItemListener {
    JButton[] btnTableList;
    int pnShowTableWidth = 310;
    int heightPhong = 140;

    private JPanel pnMain, pnShowTable;
    private DefaultTableModel modelTableBill, modelTableProduct;
    private JTable tableBill, tableProduct;
    private JLabel lbShowTime;
    private JButton btnMoveTable, btnRefresh, btnExit, btnSearch, btnTamTinh, btnTotalPrice, btnAdd, btnDelete;
    private JTextField txtBillID, txtTableID, txtTotalPrice, txtProductName;
    private JComboBox<String> cboCategory;
    private JSpinner spinCount;

    ImageIcon transferIcon = new ImageIcon("img/transfer_16.png");
    ImageIcon logOutIcon = new ImageIcon("img/logout_16.png");
    ImageIcon refreshIcon = new ImageIcon("img/refresh_16.png");
    ImageIcon paymentIcon = new ImageIcon("img/payment_16.png");
    ImageIcon searchIcon = new ImageIcon("img/search_16.png");
    ImageIcon coffeeActionIcon = new ImageIcon("img/coffee_32_action.png");
    ImageIcon coffeeDisableIcon = new ImageIcon("img/coffee_32_disable.png");
    ImageIcon addIcon = new ImageIcon("img/blueAdd_16.png");
    ImageIcon trashIcon = new ImageIcon("img/trash_16.png");

    int viTri = -1;

    public fManage() {
        setTitle("Phần Mềm Quản Lý Quán Cafe");
        setSize(1280, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        // setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        createMenuBar();
        createFromQLKS();
        // setCloseAction(this);
    }

    public void createMenuBar() {
    }

    public void createFromQLKS() {
        pnMain = new JPanel();
        pnMain.setBackground(Color.WHITE);
        pnMain.setLayout(null);

        JPanel pnTitle = new JPanel();
        pnTitle.setBounds(0, 0, 1264, 39);
        pnMain.add(pnTitle);
        pnTitle.setBackground(Color.decode("#d0e1fd"));

        JLabel lbTitle = new JLabel("Quản Lý Bán Hàng");
        lbTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        lbTitle.setForeground(Color.decode("#1a66e3"));
        pnTitle.add(lbTitle);

        JPanel pnNameEmp = new JPanel();
        pnNameEmp.setBackground(Color.WHITE);
        pnNameEmp.setBorder(new TitledBorder(null, "Nhân Viên: ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        pnNameEmp.setBounds(0, 39, 330, 40);
        pnMain.add(pnNameEmp);
        pnNameEmp.setLayout(null);

        JLabel lbTenNV = new JLabel("name");
        lbTenNV.setBounds(12, 12, 306, 21);
        pnNameEmp.add(lbTenNV);
        lbTenNV.setFont(new Font("Dialog", Font.BOLD, 18));

        JPanel pnTableList = new JPanel();
        pnTableList.setBackground(Color.WHITE);
        pnTableList.setBorder(new TitledBorder(null, "Bàn", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        pnTableList.setBounds(0, 80, 330, 581);
        pnMain.add(pnTableList);
        pnTableList.setLayout(new BorderLayout(0, 0));

        JPanel pnControlTable = new JPanel();
        pnControlTable.setBackground(Color.WHITE);
        pnTableList.add(pnControlTable, BorderLayout.NORTH);
        pnControlTable.setLayout(null);
        pnControlTable.setPreferredSize(new Dimension(330, 60));

        btnMoveTable = new JButton("Chuyển bàn", transferIcon);
        btnMoveTable.setBounds(12, 33, 296, 27);
        btnMoveTable.setBackground(Color.decode("#d0e1fd"));
        btnMoveTable.setForeground(Color.decode("#1a66e3"));
        pnControlTable.add(btnMoveTable);

        btnRefresh = new JButton("Làm mới", refreshIcon);
        btnRefresh.setBounds(12, 0, 142, 27);
        btnRefresh.setBackground(Color.decode("#d0e1fd"));
        btnRefresh.setForeground(Color.decode("#1a66e3"));
        pnControlTable.add(btnRefresh);

        btnExit = new JButton("Thoát", logOutIcon);
        btnExit.setBounds(166, 0, 142, 26);
        btnExit.setBackground(Color.decode("#d0e1fd"));
        btnExit.setForeground(Color.decode("#1a66e3"));
        pnControlTable.add(btnExit);

        pnShowTable = new JPanel();
        pnShowTable.setBackground(Color.WHITE);
        FlowLayout flShowTable = new FlowLayout(FlowLayout.LEFT);
        flShowTable.setHgap(6);
        flShowTable.setVgap(6);
        pnShowTable.setLayout(flShowTable);
        pnShowTable.setPreferredSize(new Dimension(pnShowTableWidth, heightPhong));

        JScrollPane scpShowTable = new JScrollPane(pnShowTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pnTableList.add(scpShowTable, BorderLayout.CENTER);
        scpShowTable.setBorder(new TitledBorder(null, "Danh sách bàn"));
        scpShowTable.setBackground(Color.WHITE);
        scpShowTable.getVerticalScrollBar().setUnitIncrement(10);

        JPanel pnBill = new JPanel();
        pnBill.setBackground(Color.WHITE);
        pnBill.setBounds(330, 40, 488, 622);

        JPanel pnBillInfo = new JPanel();
        pnBillInfo.setBackground(Color.WHITE);
        pnBillInfo.setBorder(
                new TitledBorder(null, "Thông tin hóa đơn", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        pnBillInfo.setLayout(null);
        pnBillInfo.setPreferredSize(new Dimension(488, 120));

        JPanel pnBillList = new JPanel();
        pnBillList.setBackground(Color.WHITE);
        pnBillList.setBorder(
                new TitledBorder(null, "Danh sách order", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        String[] colsDV = { "STT", "Tên sản phẩm", "Đơn giá", "Số Lượng", "Thành tiền" };
        modelTableBill = new DefaultTableModel(colsDV, 0) {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        pnBillList.setLayout(new BorderLayout(0, 0));
        tableBill = new JTable(modelTableBill);
        tableBill.setBackground(Color.WHITE);

        JScrollPane scpTableBill = new JScrollPane(tableBill, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scpTableBill.setBackground(Color.WHITE);
        scpTableBill.getViewport().setBackground(Color.WHITE);
        pnBillList.add(scpTableBill, BorderLayout.CENTER);
        pnBill.setLayout(new BorderLayout(0, 0));

        pnBill.add(pnBillInfo, BorderLayout.NORTH);

        JLabel lbMaHD = new JLabel("Mã hóa đơn: ");
        lbMaHD.setBounds(12, 16, 85, 20);
        pnBillInfo.add(lbMaHD);

        txtBillID = new JTextField();
        txtBillID.setFont(new Font("Tahoma", Font.BOLD, 11));
        txtBillID.setEditable(false);
        txtBillID.setBounds(90, 16, 148, 20);
        txtBillID.setBackground(Color.WHITE);
        pnBillInfo.add(txtBillID);
        txtBillID.setColumns(10);

        JLabel lblMaBan = new JLabel("Bàn số: ");
        lblMaBan.setBounds(256, 16, 57, 20);
        pnBillInfo.add(lblMaBan);

        txtTableID = new JTextField();
        txtTableID.setFont(new Font("Tahoma", Font.BOLD, 11));
        txtTableID.setEditable(false);
        txtTableID.setBounds(331, 16, 130, 20);
        txtTableID.setBackground(Color.WHITE);
        pnBillInfo.add(txtTableID);
        txtTableID.setColumns(10);

        JLabel lbTime = new JLabel("Thời gian: ");
        lbTime.setBounds(12, 53, 85, 16);
        pnBillInfo.add(lbTime);

        lbShowTime = new JLabel("dd/MM/yyyy HH:mm:ss");
        lbShowTime.setBounds(90, 53, 223, 16);
        pnBillInfo.add(lbShowTime);

        btnTamTinh = new JButton("Tạm tính");
        btnTamTinh.setBounds(331, 48, 130, 26);
        btnTamTinh.setBackground(Color.decode("#d0e1fd"));
        btnTamTinh.setForeground(Color.decode("#1a66e3"));
        pnBillInfo.add(btnTamTinh);

        btnTotalPrice = new JButton("Thanh toán", paymentIcon);
        btnTotalPrice.setBounds(331, 86, 130, 26);
        btnTotalPrice.setBackground(Color.decode("#d0e1fd"));
        btnTotalPrice.setForeground(Color.decode("#1a66e3"));
        pnBillInfo.add(btnTotalPrice);

        JLabel lblTongTien = new JLabel("Tổng tiền: ");
        lblTongTien.setBounds(12, 89, 85, 20);
        pnBillInfo.add(lblTongTien);

        txtTotalPrice = new JTextField();
        txtTotalPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
        txtTotalPrice.setHorizontalAlignment(SwingConstants.RIGHT);
        txtTotalPrice.setText("0.0");
        txtTotalPrice.setEditable(false);
        txtTotalPrice.setBounds(90, 89, 148, 20);
        txtTotalPrice.setBackground(Color.WHITE);
        pnBillInfo.add(txtTotalPrice);
        txtTotalPrice.setColumns(10);

        JLabel lbVND = new JLabel("(VND)");
        lbVND.setBounds(245, 89, 57, 20);
        pnBillInfo.add(lbVND);
        pnBill.add(pnBillList);
        pnMain.add(pnBill);
        getContentPane().add(pnMain);

        JPanel pnProduct = new JPanel();
        pnProduct.setBackground(Color.WHITE);
        pnProduct.setBorder(
                new TitledBorder(null, "Thông tin sản phẩm", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        pnProduct.setBounds(819, 40, 445, 622);
        pnMain.add(pnProduct);
        pnProduct.setLayout(new BorderLayout(0, 0));

        JPanel pnControlProduct = new JPanel();
        pnControlProduct.setPreferredSize(new Dimension(445, 105));
        pnControlProduct.setBackground(Color.WHITE);
        pnProduct.add(pnControlProduct, BorderLayout.NORTH);
        pnControlProduct.setLayout(null);

        JLabel lbNameProduct = new JLabel("Tên sản phẩm: ");
        lbNameProduct.setBounds(12, 12, 90, 16);
        pnControlProduct.add(lbNameProduct);

        txtProductName = new JTextField();
        txtProductName.setBounds(110, 10, 170, 20);
        pnControlProduct.add(txtProductName);
        txtProductName.setColumns(10);

        btnSearch = new JButton("Tìm", searchIcon);
        btnSearch.setBounds(292, 7, 131, 26);
        btnSearch.setBackground(Color.decode("#d0e1fd"));
        btnSearch.setForeground(Color.decode("#1a66e3"));
        pnControlProduct.add(btnSearch);

        JLabel lnLoaiSp = new JLabel("Loại sản phẩm: ");
        lnLoaiSp.setBounds(12, 70, 100, 16);
        pnControlProduct.add(lnLoaiSp);

        cboCategory = new JComboBox<String>();
        cboCategory.setBounds(110, 67, 170, 20);
        cboCategory.setBackground(Color.WHITE);
        pnControlProduct.add(cboCategory);

        JLabel lbCount = new JLabel("Số lượng: ");
        lbCount.setBounds(12, 38, 90, 16);
        pnControlProduct.add(lbCount);

        spinCount = new JSpinner();
        spinCount.setModel(new SpinnerNumberModel(1, 1, null, 1));
        spinCount.setBounds(110, 35, 170, 20);
        pnControlProduct.add(spinCount);

        btnAdd = new JButton("Thêm", addIcon);
        btnAdd.setBackground(Color.decode("#d0e1fd"));
        btnAdd.setForeground(Color.decode("#1a66e3"));
        btnAdd.setBounds(292, 40, 131, 26);
        pnControlProduct.add(btnAdd);

        btnDelete = new JButton("Hủy", trashIcon);
        btnDelete.setBackground(Color.decode("#d0e1fd"));
        btnDelete.setForeground(Color.decode("#1a66e3"));
        btnDelete.setBounds(292, 76, 131, 26);
        pnControlProduct.add(btnDelete);

        JPanel pnProductList = new JPanel();
        pnProductList.setBackground(Color.WHITE);
        pnProductList.setBorder(
                new TitledBorder(null, "Danh sách sản phẩm", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        pnProduct.add(pnProductList, BorderLayout.CENTER);

        String[] colsProduct = { "STT", "Tên sản phẩm", "Đơn giá" };
        modelTableProduct = new DefaultTableModel(colsProduct, 0) {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        pnProductList.setLayout(new BorderLayout(0, 0));
        tableProduct = new JTable(modelTableProduct);

        JScrollPane scpProductList = new JScrollPane(tableProduct, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scpProductList.getViewport().setBackground(Color.WHITE);
        pnProductList.add(scpProductList);

        btnMoveTable.addActionListener(this);
        btnRefresh.addActionListener(this);
        btnSearch.addActionListener(this);
        btnTamTinh.addActionListener(this);
        btnTotalPrice.addActionListener(this);
        btnExit.addActionListener(this);
        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);

        btnMoveTable.addMouseListener(this);
        btnRefresh.addMouseListener(this);
        btnSearch.addMouseListener(this);
        btnTamTinh.addMouseListener(this);
        btnTotalPrice.addMouseListener(this);
        btnExit.addMouseListener(this);
        btnAdd.addMouseListener(this);
        btnDelete.addMouseListener(this);
        tableProduct.addMouseListener(this);
        tableBill.addMouseListener(this);

        cboCategory.addItemListener(this);

        LoadTable();
        reSizeColumnTableBillInfo();
        loadCategory();
        reSizeColumnTableProduct();
    }

    public static void main(String[] args) {
        new fManage().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnExit)) {
            fLogin f = new fLogin();
            this.setVisible(false);
            f.setVisible(true);
        } else if (o.equals(btnAdd) || o.equals(btnDelete)) {
            int billID = -1;
            int tableID = -1;
            if (txtTableID.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Bạn cần phải chọn bàn trước");
            } else {
                int count = (int) spinCount.getValue();
                if (o.equals(btnDelete))
                    count *= -1;
                String productName = txtProductName.getText().trim();
                Product product = ProductDAO.getInstance().getProductByProductName(productName);
                int productID = product.getId();
                if (!(txtBillID.getText().trim().equals("")))
                    billID = Integer.parseInt(txtBillID.getText());
                tableID = Integer.parseInt(txtTableID.getText().trim().split(" ")[1]);
                // create new bill
                if (billID == -1) {
                    BillDAO.getInstance().insertBill(tableID);
                    billID = BillDAO.getInstance().getMaxIDBill();
                    BillInfoDAO.getInstance().insertBillInfo(billID, productID, count);
                } else {
                    BillInfoDAO.getInstance().insertBillInfo(billID, productID, count);
                }
                showBill(tableID);
            }
        } else if (o.equals(btnSearch)) {
            String productName = txtProductName.getText().trim();
            if (productName.equals("")) {
                JOptionPane.showMessageDialog(this, "Tên sản phẩm không được rỗng");
            } else {
                ArrayList<Product> productList = ProductDAO.getInstance().getListProductByProductName(productName);
                loadProductListToTable(productList);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if (o.equals(tableProduct)) {
            int row = tableProduct.getSelectedRow();
            String productName = modelTableProduct.getValueAt(row, 1).toString();
            txtProductName.setText(productName);
            spinCount.setValue(1);
        } else if (o.equals(tableBill)) {
            int row = tableBill.getSelectedRow();
            String productName = modelTableBill.getValueAt(row, 1).toString();
            txtProductName.setText(productName);
            int count = Integer.parseInt(modelTableBill.getValueAt(row, 3).toString());
            spinCount.setValue(count);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Object o = e.getSource();
        if (o.equals(btnMoveTable)) {
            btnMoveTable.setBackground(Color.decode("#a3c5fb"));
            btnMoveTable.setForeground(Color.WHITE);
        } else if (o.equals(btnRefresh)) {
            btnRefresh.setBackground(Color.decode("#a3c5fb"));
            btnRefresh.setForeground(Color.WHITE);
        } else if (o.equals(btnSearch)) {
            btnSearch.setBackground(Color.decode("#a3c5fb"));
            btnSearch.setForeground(Color.WHITE);
        } else if (o.equals(btnTamTinh)) {
            btnTamTinh.setBackground(Color.decode("#a3c5fb"));
            btnTamTinh.setForeground(Color.WHITE);
        } else if (o.equals(btnTotalPrice)) {
            btnTotalPrice.setBackground(Color.decode("#a3c5fb"));
            btnTotalPrice.setForeground(Color.WHITE);
        } else if (o.equals(btnAdd)) {
            btnAdd.setBackground(Color.decode("#a3c5fb"));
            btnAdd.setForeground(Color.WHITE);
        } else if (o.equals(btnDelete)) {
            btnDelete.setBackground(Color.decode("#a3c5fb"));
            btnDelete.setForeground(Color.WHITE);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Object o = e.getSource();
        if (o.equals(btnMoveTable)) {
            btnMoveTable.setBackground(Color.decode("#d0e1fd"));
            btnMoveTable.setForeground(Color.decode("#1a66e3"));
        } else if (o.equals(btnRefresh)) {
            btnRefresh.setBackground(Color.decode("#d0e1fd"));
            btnRefresh.setForeground(Color.decode("#1a66e3"));
        } else if (o.equals(btnSearch)) {
            btnSearch.setBackground(Color.decode("#d0e1fd"));
            btnSearch.setForeground(Color.decode("#1a66e3"));
        } else if (o.equals(btnTamTinh)) {
            btnTamTinh.setBackground(Color.decode("#d0e1fd"));
            btnTamTinh.setForeground(Color.decode("#1a66e3"));
        } else if (o.equals(btnTotalPrice)) {
            btnTotalPrice.setBackground(Color.decode("#d0e1fd"));
            btnTotalPrice.setForeground(Color.decode("#1a66e3"));
        } else if (o.equals(btnExit)) {
            btnExit.setBackground(Color.decode("#d0e1fd"));
            btnExit.setForeground(Color.decode("#1a66e3"));
        } else if (o.equals(btnAdd)) {
            btnAdd.setBackground(Color.decode("#d0e1fd"));
            btnAdd.setForeground(Color.decode("#1a66e3"));
        } else if (o.equals(btnDelete)) {
            btnDelete.setBackground(Color.decode("#d0e1fd"));
            btnDelete.setForeground(Color.decode("#1a66e3"));
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object o = e.getSource();
        if (o.equals(cboCategory)) {
            String categoryName = cboCategory.getSelectedItem().toString();
            loadProductListByCategoryName(categoryName);
        }
    }

    // mô tả: Bắt sự kiện khi click btn close(x), sẽ show 1 form xác nhận đăng xuất
    // hay thoát chương trình
    public void setCloseAction(JFrame jframe) {
        jframe.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                fLogin f = new fLogin();
                jframe.setVisible(false);
                f.setVisible(true);
            }
        });
    }

    public void LoadTable() {
        Border lineBlue = new LineBorder(Color.RED, 2);
        Border lineGray = new LineBorder(Color.GRAY, 1);
        ArrayList<Table> tableList = TableDAO.getInstance().getListTable();
        int sizeTableList = tableList.size();
        btnTableList = new JButton[sizeTableList];
        for (int i = 0; i < sizeTableList; i++) {
            final int selection = i;
            Table item = tableList.get(i);
            String name = item.getName();
            String status = item.getStatus();
            String nameBtn = "<html><p style='text-align: center;'>" + name + "</p></br><p style='text-align: center;'>"
                    + status + "</p></html>";
            JButton btn = new JButton(nameBtn);
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setPreferredSize(new Dimension(TableDAO.TABLE_WIDTH, TableDAO.TABLE_HEIGHT));
            btn.setBorder(lineGray);
            switch (status) {
                case "Trống":
                    btn.setBackground(Color.CYAN);
                    btn.setIcon(coffeeActionIcon);
                    break;
                default:
                    btn.setBackground(Color.decode("#E0FFFF"));
                    btn.setIcon(coffeeDisableIcon);
                    break;
            }
            if ((i + 1) % 3 == 0) {
                heightPhong += TableDAO.TABLE_HEIGHT;
                pnShowTable.setPreferredSize(new Dimension(pnShowTableWidth, heightPhong));
            }
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (viTri != -1) {
                        btnTableList[viTri].setBorder(lineGray);
                    }
                    viTri = selection;
                    btnTableList[selection].setBorder(lineBlue);
                    int tableID = item.getId();
                    showBill(tableID);
                    txtTableID.setText(name);
                    int billID = BillDAO.getInstance().getUncheckBillByTableID(tableID);
                    if (billID != -1)
                        txtBillID.setText(String.valueOf(billID));
                    else
                        txtBillID.setText("");
                    txtProductName.setText("");
                    spinCount.setValue(1);
                }
            });
            btn.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(Color.YELLOW);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    switch (status) {
                        case "Trống":
                            btn.setBackground(Color.CYAN);
                            break;
                        default:
                            btn.setBackground(Color.decode("#E0FFFF"));
                            break;
                    }
                }
            });
            btnTableList[i] = btn;
            pnShowTable.add(btnTableList[i]);
        }
    }

    private void showBill(int idTable) {
        ArrayList<Menu> dataList = MenuDAO.getInstance().getListMenuByTableID(idTable);
        DecimalFormat df = new DecimalFormat("#,###.##");
        int i = 1;
        modelTableBill.getDataVector().removeAllElements();
        modelTableBill.fireTableDataChanged();
        double totalPrice = 0;
        for (Menu item : dataList) {
            totalPrice += item.getTotalPrice();
            String stt = df.format(i++);
            String totalPriceStr = df.format(item.getTotalPrice());
            String priceStr = df.format(item.getPrice());
            String countStr = df.format(item.getCount());
            modelTableBill.addRow(new Object[] { stt, item.getProductName(), priceStr, countStr, totalPriceStr });
        }
        txtTotalPrice.setText(df.format(totalPrice));
    }

    private void loadCategory() {
        ArrayList<Category> dataList = CategoryDAO.getInstance().getListCategory();
        for (Category item : dataList) {
            cboCategory.addItem(item.getName());
        }
    }

    public void loadProductListToTable(ArrayList<Product> dataList) {
        DecimalFormat df = new DecimalFormat("#,###.##");
        int i = 1;
        modelTableProduct.getDataVector().removeAllElements();
        modelTableProduct.fireTableDataChanged();
        for (Product item : dataList) {
            String stt = df.format(i++);
            String priceStr = df.format(item.getPrice());
            modelTableProduct.addRow(new Object[] { stt, item.getName(), priceStr });
        }
    }

    private void loadProductListByCategoryName(String categoryName) {
        ArrayList<Product> dataList = ProductDAO.getInstance().getListProductByCategoryName(categoryName);
        loadProductListToTable(dataList);
    }

    private void reSizeColumnTableBillInfo() {
        tableBill.getColumnModel().getColumn(0).setPreferredWidth(15);
        tableBill.getColumnModel().getColumn(1).setPreferredWidth(110);
        tableBill.getColumnModel().getColumn(2).setPreferredWidth(70);
        tableBill.getColumnModel().getColumn(3).setPreferredWidth(45);
        tableBill.getColumnModel().getColumn(4).setPreferredWidth(80);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        tableBill.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableBill.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tableBill.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableBill.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
    }

    private void reSizeColumnTableProduct() {
        tableProduct.getColumnModel().getColumn(0).setPreferredWidth(15);
        tableProduct.getColumnModel().getColumn(1).setPreferredWidth(210);
        tableProduct.getColumnModel().getColumn(2).setPreferredWidth(80);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        tableProduct.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableProduct.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
    }

}