package app;

import javax.swing.*;

import DAO.TaiKhoanDAO;

// import javax.swing.event.*;
// import java.awt.*;
import java.awt.event.*;

public class fLogin extends JFrame implements ActionListener {
    // chưa bật sự kiện phím enter để submit
    private JTextField txtUserName, txtPassWord;
    private JButton btnLogin, btnExit;

    public fLogin() {
        setTitle("Đăng nhập");
        setSize(300, 150);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        createFormLogin();
    }

    public void createFormLogin() {
        JPanel pnMain = new JPanel();
        pnMain.setLayout(null);
        pnMain.setBounds(0, 0, 300, 150);

        JLabel lbUserName, lbPassWord;
        lbUserName = new JLabel("Tên đăng nhập: ");
        lbPassWord = new JLabel("Mật khẩu: ");
        txtUserName = new JTextField();
        txtPassWord = new JPasswordField();
        btnLogin = new JButton("Đăng nhập");
        // btnLogin.setMnemonic(KeyEvent.VK_ENTER);
        btnExit = new JButton("Thoát");

        pnMain.add(lbUserName);
        pnMain.add(txtUserName);
        pnMain.add(lbPassWord);
        pnMain.add(txtPassWord);
        pnMain.add(btnLogin);
        pnMain.add(btnExit);

        int w1 = 110, w2 = 170, h = 20;
        lbUserName.setBounds(10, 10, w1, h);
        txtUserName.setBounds(w1, 10, w2, h);
        lbPassWord.setBounds(10, 40, w1, h);
        txtPassWord.setBounds(w1, 40, w2, h);
        btnLogin.setBounds(10, 80, 130, h);
        btnExit.setBounds(150, 80, 130, h);
        this.add(pnMain);

        btnExit.addActionListener(this);
        btnLogin.addActionListener(this);
    }

    public static void main(String[] args) {
        new fLogin().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnExit)) {
            int select = JOptionPane.showConfirmDialog(this, "Bạn có muốn thoát chương trình", "Thông báo",
                    JOptionPane.OK_CANCEL_OPTION);
            if (select == JOptionPane.OK_OPTION) {
                System.exit(1);
            }
        } else if (o.equals(btnLogin)) {
            String userName = txtUserName.getText();
            String passWord = txtPassWord.getText();
            if (login(userName, passWord) == true) {
                fQLKhachSan f = new fQLKhachSan();
                this.setVisible(false);
                f.setVisible(true);
            } else {
                JOptionPane.showConfirmDialog(this, "Sai tài khoản hoặc mật khẩu", "Thông báo",
                        JOptionPane.OK_CANCEL_OPTION);
            }
        }
    }

    private boolean login(String userName, String passWord) {
        TaiKhoanDAO.getInstance();
        boolean result = TaiKhoanDAO.Login(userName, passWord);
        System.out.println(result);
        return result;
    }
}
