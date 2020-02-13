package com.shengbao.application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.shengbao.framework.socket.GameClient;
import com.shengbao.framework.socket.RequestHandler;

public class MainView {

    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private JTextField packetId;
    private JTextField txt_port;
    private JTextField txt_hostIp;
    private JTextField txt_name;
    private JButton btn_start;
    private JButton btn_login;
    private JButton btn_stop;
    private JButton btn_send;
    private JButton btn_reset;
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel eastPanel;
    private JScrollPane rightScroll;
    private JPanel leftScroll;
    private JSplitPane centerSplit;

    private boolean isConnected = false;

    private GameClient client;

    // 构造方法
    public MainView(GameClient client) {
        this.client = client;

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setForeground(Color.blue);
        textField = new JTextField();
        packetId = new JTextField();
        txt_port = new JTextField("9999");
        txt_hostIp = new JTextField("127.0.0.1");
        txt_name = new JTextField("shengbao");
        btn_start = new JButton("连接");
        btn_login = new JButton("登陆");
        btn_stop = new JButton("断开");
        btn_reset = new JButton("清空");
        btn_send = new JButton("发送");

        northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 9));
        northPanel.add(new JLabel("端口"));
        northPanel.add(txt_port);
        northPanel.add(new JLabel("服务器IP"));
        northPanel.add(txt_hostIp);
        northPanel.add(new JLabel("用户名"));
        northPanel.add(txt_name);
        northPanel.add(btn_start);
        northPanel.add(btn_login);
        northPanel.add(btn_stop);
        northPanel.add(btn_reset);
        northPanel.setBorder(new TitledBorder("连接信息"));

        southPanel = new JPanel(new BorderLayout());
        southPanel.add(textField, "Center");
        southPanel.add(btn_send, "East");
        southPanel.setBorder(new TitledBorder("传参"));
        rightScroll = new JScrollPane(textArea);
        rightScroll.setBorder(new TitledBorder("消息显示区"));
        eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(southPanel, "North");
        eastPanel.add(rightScroll, "Center");

        leftScroll = new JPanel(new GridLayout(8, 1));
        leftScroll.setBorder(new TitledBorder("类名和方法名"));
        leftScroll.add(new JLabel("packetId"));
        leftScroll.add(packetId);

        centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll, eastPanel);
        centerSplit.setDividerLocation(100);

        frame = new JFrame("测试机");
        frame.setLayout(new BorderLayout());
        frame.add(northPanel, "North");
        frame.add(centerSplit, "Center");
        frame.setSize(800, 400);
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        frame.setLocation((screen_width - frame.getWidth()) / 2, (screen_height - frame.getHeight()) / 2);
        frame.setVisible(true);

        // 回车键时事件
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                send();
            }
        });

        // 单击发送按钮时事件
        btn_send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                send();
            }
        });

        // 单击连接按钮时事件
        btn_start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int port;
                if (isConnected) {
                    JOptionPane.showMessageDialog(frame, "已处于连接上状态，不要重复连接!", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    try {
                        port = Integer.parseInt(txt_port.getText().trim());
                    } catch (NumberFormatException e2) {
                        throw new Exception("端口号不符合要求!端口为整数!");
                    }
                    String hostIp = txt_hostIp.getText().trim();
                    String name = txt_name.getText().trim();
                    if (name.equals("") || hostIp.equals("")) {
                        throw new Exception("姓名、服务器IP不能为空!");
                    }
                    isConnected = connectServer(port, hostIp, name);
                    if (isConnected) {
                        textArea.append("连接成功\r\n");
                    } else {
                        textArea.append("连接失败\r\n");
                    }
                    frame.setTitle(name);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(frame, exc.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 单击登陆按钮时事件
        btn_login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        // 单击断开按钮时事件
        btn_stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        // 单击断开按钮时事件
        btn_reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });

        // 关闭窗口时事件
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);// 退出程序
            }
        });
    }

    protected void close() {
        isConnected = false;
    }

    /**
     * 连接服务器
     *
     * @param port
     * @param name
     * @throws InterruptedException
     */
    public boolean connectServer(int port, String host, String name)  {
        try {
            client.connect(host, port);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void login() {
        if (isConnected) {
            String trim = txt_name.getText().trim();
            if (trim == null || trim == "") {
                JOptionPane.showMessageDialog(frame, "用户名不能为Null", "错误", JOptionPane.ERROR_MESSAGE);
            }
            String account = "string:" + trim;
            sendMessage("3", account);
        } else {
            JOptionPane.showMessageDialog(frame, "还没有连接服务器，无法登陆！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    // 执行发送
    public void send() {
        if (!isConnected) {
            JOptionPane.showMessageDialog(frame, "还没有连接服务器，无法发送消息！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String requestId = packetId.getText().trim();
        if (requestId == null || requestId.equals("")) {
            JOptionPane.showMessageDialog(frame, "包名不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String prarms = textField.getText().trim();
        sendMessage(requestId, prarms);
        textArea.append("");
    }

    /**
     * 发送消息
     */
    public void sendMessage(String packetId, String body) {
        client.write(Integer.valueOf(packetId), body);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public void appendMessage(String str) {
        textArea.append(str);
    }

}
