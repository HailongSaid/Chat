package chat;


import java.awt.FileDialog;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class Mainfun_Panel extends javax.swing.JPanel implements Runnable ,ActionListener {
    ServerSocket serversocket;
    Vector pv = new Vector();
    Vector pvFriend = new Vector();
    Vector pvjDialog2 = new Vector();
    Node user = new Node() ;
    void caidan1(){
        main1.getJMenuItem(1).setText("个人信息");
        main1.getJMenuItem(2).setText("修改信息");
        main1.getJMenuItem(3).setText("退出");
        main1.getJMenuItem(4).setText("添加好友");
        main1.getJMenuItem(5).setText("删除好友");
        main1.getJMenu(1).setText("用户");
        main1.getJMenu(2).setText("好友");
        main1.getJMenu(3).setText("帮助");
        main1.getJMenuItem(1).addActionListener(this);
        main1.getJMenuItem(2).addActionListener(this);
        main1.getJMenuItem(3).addActionListener(this);
        main1.getJMenuItem(4).addActionListener(this);
        main1.getJMenuItem(5).addActionListener(this);
    }
    public void actionPerformed(ActionEvent e){
        
        if(e.getSource()== main1.getJMenuItem(4)) {
            jPanel5.setVisible(true);
        }
        else if(e.getSource()==main1.getJMenuItem(3)) {	
               System.exit(0);
        }
        else if(e.getSource()==main1.getJMenuItem(1)) {	
            jDialog3.setTitle("个人信息");
            jDialog3.setVisible(true);
            jTextField1.setEnabled(false);
            jTextField2.setEnabled(false);
            jTextField3.setEnabled(false);
            jPasswordField1.setEnabled(false);
            jTextField1.setText(user.ID);
            jTextField2.setText(user.name);
            jPasswordField1.setText(user.password);
            jTextField3.setText(user.BirthDay);
            jButton9.setVisible(false);
            jButton10.setVisible(false);
            jLabel5.setVisible(false);
        }
        else if(e.getSource()==main1.getJMenuItem(2)) {	
            jDialog3.setTitle("信息修改");
            jDialog3.setVisible(true);
            jTextField1.setEnabled(false);
            jTextField2.setEnabled(false);
            jTextField3.setEnabled(true);
            jPasswordField1.setEnabled(true);
            jTextField1.setText(user.ID);
            jTextField2.setText(user.name);
            jPasswordField1.setText(user.password);
            jTextField3.setText(user.BirthDay);
            jButton9.setVisible(true);
            jButton10.setVisible(true);
            jLabel5.setVisible(true);
        }
    }
    public Mainfun_Panel(final Main_Frame main1) {
        this.main1 = main1;
        main1.setBounds(100, 100, 400, 400);
        initComponents();
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea2.setLineWrap(true);
        jDialog1.setVisible(false);
        jDialog1.setBounds(100, 100, 500,400);
        
        jDialog3.setVisible(false);
        jDialog3.setBounds(100, 100, 500,400);
        jPanel5.setVisible(false);
        
        caidan1();
       
        readFile();
        information();
//        try {
//            serversocket = new ServerSocket(4333);
//        } catch (IOException ex) {
//            Logger.getLogger(Mainfun_Panel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        接受消息服务器.start();
//        
        接受消息.start();
        main1.addWindowListener(new WindowAdapter() {
        public void windowClosing (WindowEvent we) {
            try {
                out.writeUTF("4  "+user.ID+" "+user.name+" "+user.password+" "+0+" "+user.BirthDay );
            } catch (IOException ex) {
                Logger.getLogger(Mainfun_Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
            main1.dispose();
            
        }
});
    }
    void information(){
        try{
          // String address = InetAddress.getLocalHost().getHostAddress();    
            in = main1.in;
            out = main1.out;
            out.writeUTF("2  "+user.ID+" "+user.name+" "+user.Ip+" "+user.port);
         }
         catch(IOException e){
         JOptionPane.showMessageDialog(null,  "网络未连接好！","提示信息", JOptionPane.INFORMATION_MESSAGE);
         System.out.println(e);
        }
    }
    void readFile(){
        StringTokenizer st = new StringTokenizer(main1.userfriend," ");
        System.out.println("全部信息:"+main1.userfriend);
        st.nextToken();
        user.ID = st.nextToken();
        user.name = st.nextToken();
        user.password = st.nextToken();
        user.Ip = st.nextToken();
        user.port = Integer.parseInt(st.nextToken());
        user.Status = 1;
        user.BirthDay = st.nextToken();
        main1.setTitle(user.name );
        jDialog1.setTitle(user.name);
        while(st.hasMoreTokens()){
           String user_inf[] = new String[6];
           for(int i=0;i<6;i++)
                 user_inf[i] = st.nextToken();
           DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(user_inf[1]);//"用户名"+user_inf[1]+"(ID"+user_inf[0]+"IP"+user_inf[2]+"PORT"+user_inf[3]+")");
           rt2.add(newNode);
           pvFriend.addElement(new Node(user_inf));
        }
        jTree1.updateUI();
         
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        root1 = new DefaultMutableTreeNode("群成员");
        DefaultMutableTreeNode rt1 = new DefaultMutableTreeNode("自己");
        root1.add(rt1);
        jTree2 = new javax.swing.JTree(root1);
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jMenuItem1 = new javax.swing.JMenuItem();
        jDialog3 = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        root = new DefaultMutableTreeNode("聊天");
        DefaultMutableTreeNode rt = new DefaultMutableTreeNode("自己");
        root.add(rt);
        rt2 = new DefaultMutableTreeNode("我的好友");
        root.add(rt2);
        rt3 = new DefaultMutableTreeNode("群聊");
        root.add(rt3);
        jTree1 = new javax.swing.JTree(root);
        jPanel5 = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

        jTree2.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree2ValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(jTree2);

        jButton1.setText("发送");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("关闭");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setText("聊天计录");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton1))))
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jMenuItem1.setText("jMenuItem1");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setText("账号：");

        jLabel2.setText("用户名：");

        jLabel3.setText("密码：");

        jLabel4.setText("出生日期：");

        jButton9.setText("修改");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("取消");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel5.setText("xxxx(年)-xx(月)-xx(日)");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jButton9)
                        .addGap(71, 71, 71)
                        .addComponent(jButton10))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                                .addComponent(jPasswordField1)
                                .addComponent(jTextField3)
                                .addComponent(jTextField1)))))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(jButton10))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
        );

        setLayout(new java.awt.BorderLayout());

        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.setPreferredSize(new java.awt.Dimension(262, 60));

        jButton13.setText("关闭");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton12.setText("加为好友");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel6.setText("好友账号：");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton13)
                .addGap(30, 30, 30))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13)
                    .addComponent(jButton12)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(116, 116, 116))
        );

        add(jPanel5, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents
    Node get(Vector pv,int i){
        return (Node)(pv.elementAt(i));
    }
    Node findFriend(String s){
        for(int i=0;i<pvFriend.size();i++){
            if(get(pvFriend,i).name.equals(s))
                return get(pvFriend,i);
        }
        return new Node();
    }
    private void jTree2ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree2ValueChanged
        // TODO add your handling code here:
       
    }//GEN-LAST:event_jTree2ValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if("".equals(jTextArea2.getText())){
            JOptionPane.showMessageDialog(null,  "发送消息不能为空！","提示信息", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String sdate=df.format(date);//获取当前系统时间
        String msg=user.name+"     "+sdate+"\n"+jTextArea2.getText().trim();//要连接数据库，只要登入成功，就可以记下用户的名字
        try {
            //out1.writeUTF("0 "+msg);
            out.writeUTF("6 "+msg);
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null,  "发送失败！","提示信息", JOptionPane.INFORMATION_MESSAGE);

        }
        jTextArea2.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //jTextArea2.setText("");
        jDialog1.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed
   
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
                out.writeUTF("4  "+user.ID+" "+user.name+" "+jPasswordField1.getText().trim()+" "+1+" "+jTextField3.getText().trim() );
            } catch (IOException ex) {
                Logger.getLogger(Mainfun_Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
        user.password = jPasswordField1.getText().trim();
        user.BirthDay = jTextField3.getText().trim();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
       jPasswordField1.setText(user.password);
       jTextField3.setText(user.BirthDay);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        // TODO add your handling code here:
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        if (node.equals(rt3)){
            try {
                //out1.writeUTF("2 "+user.name+" "+user.Ip);
                out.writeUTF("3 "+user.name+" "+user.Ip);
            }
            catch (IOException ex) {
                Logger.getLogger(Mainfun_Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
//            接受消息.start();
            jDialog1.setVisible(true);

        }
        else if (rt2.isNodeChild(node)){
            String friendname = node.toString();
            Whisper_Frame talk = new Whisper_Frame(this,friendname);
            talk.setVisible(true);
            talk.setBounds(100, 100, 400, 400);
            pvjDialog2.add(talk);
        }
        
    }//GEN-LAST:event_jTree1ValueChanged

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        jPanel5.setVisible(false);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        String number = jTextField4.getText().trim();
        if(!number.matches("[0-9]+")){
        JOptionPane.showMessageDialog(null,  "输入的数据不规范，请重新输入！","提示信息", JOptionPane.INFORMATION_MESSAGE);
        jTextField4.setText("");
        }
        else
        try {
            out.writeUTF("7 "+number);
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(number);//"用户名"+user_inf[1]+"(ID"+user_inf[0]+"IP"+user_inf[2]+"PORT"+user_inf[3]+")");
            rt2.add(newNode);
            jTree1.updateUI();
        } catch (IOException ex) {
            Logger.getLogger(Mainfun_Panel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed
    void inshow_LiaoTian(){
        
    }
     void count_Use(String s){
        StringTokenizer st = new StringTokenizer(s," ");
        st.nextToken();
        while(st.hasMoreTokens()){
            Node node = new Node(st.nextToken(),st.nextToken());
            JOptionPane.showMessageDialog(null, node.name+"  "+node.Ip+"上线了","提示信息", JOptionPane.INFORMATION_MESSAGE);
            pv.addElement(node);
     
        }
        
    }
     void onlin_Use(String s){
        StringTokenizer st = new StringTokenizer(s," ");
        st.nextToken();
        while(st.hasMoreTokens()){
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(st.nextToken());
            st.nextToken();
            root1.add(newNode);
            jTree2.updateUI();          
        }
    }
    void addFriendNode(String s){
        StringTokenizer st = new StringTokenizer(s," ");
        String user_inf[] = new String[6];
        for(int i=0;i<6;i++)
             user_inf[i] = st.nextToken();
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(user_inf[1]);//"用户名"+user_inf[1]+"(ID"+user_inf[0]+"IP"+user_inf[2]+"PORT"+user_inf[3]+")");
        rt2.add(newNode);
        pvFriend.addElement(new Node(user_inf));
        jTree1.updateUI();
         
    }
     void count_Use1(String s){
        StringTokenizer st = new StringTokenizer(s," ");
        st.nextToken();
        String freendID = st.nextToken();
        String freendname = st.nextToken();
        String freendIp = st.nextToken();
        int freendport = Integer.parseInt(st.nextToken());
        System.out.println("用户名："+ freendname+"上线了");
        user.name = freendname;
        user.ID = freendID;
        user.Ip = freendIp;
        user.port = freendport;
        findFriend(freendname).set(freendID,freendname,freendIp, freendport);
        
       }
        Whisper_Frame getjDialog(Vector pv,int index){
            return  (Whisper_Frame)(pv.elementAt(index));
        }
        int findjDialogByName(String name1){
           for(int i=0;i<pvjDialog2.size();i++)
                if(getjDialog(pvjDialog2,i).isVisible()&&getjDialog(pvjDialog2,i).name.equals(name1))
                    return  i;
           return -1;
        }   
    public void run(){
        while(true){
            if(Thread.currentThread()==接受消息){
                try{
                  String s1 = in.readUTF();
                  System.out.println("消息："+s1);
                  if(s1.charAt(0)=='2'){
                      count_Use(s1);
                      JOptionPane.showMessageDialog(null, s1+"0000000上线了","提示信息", JOptionPane.INFORMATION_MESSAGE);
                      }
                  else if(s1.charAt(0)=='9'){
                    StringTokenizer st = new StringTokenizer(s1," ");
                    st.nextToken();
                    String name1 =  st.nextToken();
                    int index = findjDialogByName(name1);
                    if(index!=-1)
                        getjDialog(pvjDialog2,index).getjTextArea3().append(s1.substring(2)+"\n");
                    else{
                        int n = JOptionPane.showConfirmDialog(null,"好友"+name1+"发来消息，是否查看？","消息", JOptionPane.YES_NO_OPTION);
                        if(n==JOptionPane.YES_OPTION){
                            Whisper_Frame talk = new Whisper_Frame(this,name1);
                            talk.setVisible(true);
                            talk.setBounds(100, 100, 400, 400);
                            talk.getjTextArea3().append(s1.substring(2)+"\n");
                            pvjDialog2.add(talk);
                         }
                      }
                     // jTextArea3.append(str.substring(2)+"\n"); 
                   }
                else if(s1.charAt(0)=='4'){
                      if(s1.substring(2).equals("susscess"))
                           JOptionPane.showMessageDialog(null,"修改成功","提示信息", JOptionPane.INFORMATION_MESSAGE);
                      else 
                           JOptionPane.showMessageDialog(null,"修改不成功，请重新修改","提示信息", JOptionPane.INFORMATION_MESSAGE);
                   }
                   else if(s1.charAt(0)=='5')
                      onlin_Use(s1);
                   else if(s1.charAt(0)=='6'){
                       System.out.println("群消息"+s1);
                      jTextArea1.append(s1.substring(2)+"\n");
                     }
                   else if(s1.charAt(0)=='7'){
                       if(s1.charAt(2)==' ')
                           JOptionPane.showMessageDialog(null,"账号不存在，请重新修改","添加不成功", JOptionPane.INFORMATION_MESSAGE);
                       else{
                           addFriendNode(s1.substring(2));
                      }
                      
                   }
                }
                 catch(IOException e){
                 }
            }
      }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton9;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTree jTree1;
    private javax.swing.JTree jTree2;
    // End of variables declaration//GEN-END:variables
    Main_Frame main1;
    DefaultMutableTreeNode root,root1,rt2,rt3;
    Socket socket = null,socket1 = null,socketToFriend = null;
    DataInputStream in = null,in1 = null,inToFriend = null;
    DataOutputStream out = null,out1 = null,outToFriend = null;
    Thread 接受消息 = new Thread(this);

}
