package chat;



import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
class Node{
    String ID;
    String name;
    String password;
    String Ip;
    int port;
    int Status;
    String BirthDay;
    Node(){
        
    }
    Node(String[] s){
        ID = s[0];
        name = s[1];
        Ip = s[2];
        System.out.println("ssss"+s[3]);
        port = Integer.parseInt(s[3]) ;
        Status = Integer.parseInt(s[4]);
        BirthDay = s[5];
    }
    Node(String name,String address){
        this.Ip = address;
        this.name= name;
    }
    void set(String id,String name,String ip,int port){
        this.ID = id;
        this.name = name;
        this.Ip = ip;
        this.port = port;
        this.Status = 1;
    }
}




public class Serveror{
    int num = 0;
    ServerSocket serversocket,serversocket1;
    Vector<Client> clients = new Vector<Client>();
     Vector<Client> client = new Vector<Client>();
	public static void main(String[] args) {
		new Serveror().begin();
		}

	public void begin()
	{
		try {
                    serversocket = new ServerSocket(4331);
                    serversocket1 = new ServerSocket(4332);
                }
		catch(IOException e){ 
                }
                while(true){
		try {
                    Socket s = serversocket.accept(); 
                    num++;              
                    System.out.println("\n"+num);
                    Client c = new Client(s);
                    new Thread(c).start();
                    
                    
                }
		catch(IOException e){ 
                }
                }
	 }
  class Client implements Runnable {//ChatServer内部类
        Connection con ;
        Statement stat; 
        ResultSet rs;
	private Socket socket;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
        Node info = new Node();
        public Client(Socket s) throws IOException
	{
         client.add(this);
         this.socket = s;
	 dis = new DataInputStream(socket.getInputStream());
	 dos = new DataOutputStream(socket.getOutputStream());
         info.Ip = s.getInetAddress().getHostAddress();
         info.port = s.getPort();
	 }
        
        void inform_online(String s){
           StringTokenizer st = new StringTokenizer(s," ");
                st.nextToken();
                info.ID = st.nextToken();
                info.name = st.nextToken();
                info.Ip = st.nextToken();
                info.port = Integer.parseInt(st.nextToken());
            
                try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/QQ_DataBase", "root","root");
                stat = con.createStatement();
                String ss = "select * from user where Status =1 and ID in (select FriendId  from  friends_list "
                             + "where  ID =  '" +s+"')";
                rs = stat.executeQuery(ss);
                while(rs.next()){
                    int port = 4333;
                    String address = rs.getString(4);
                    Socket socket = new Socket(address,port);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    String ss1 = "2 "+info.ID+" "+info.name+" "+info.Ip+" "+info.port ;
                    out.writeUTF(ss1);
                    JOptionPane.showMessageDialog(null,ss1,"提示信息", JOptionPane.INFORMATION_MESSAGE);
            
                }
            }
            catch(SQLException e){
              JOptionPane.showMessageDialog(null,  "网络未连接好！","提示信息", JOptionPane.INFORMATION_MESSAGE);
            } 
            catch (ClassNotFoundException ex) {
            }
            catch (IOException ex) {
            }
            
        }
        void can_load(String s){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/QQ_DataBase", "root","root");
                stat = con.createStatement();
                StringTokenizer st = new StringTokenizer(s," ");
                st.nextToken();
                String user_inf[] = new String[4];
                int i = 0;
                while(st.hasMoreTokens()){
                    user_inf[i] = st.nextToken();
                    i++;
                }
                System.out.println("用户ID:"+user_inf[0]+" 密码："+
                        user_inf[1]+" IP："+info.Ip+" PORT： "+info.port);
                String ss = "select * from user where ID = '" +user_inf[0]+
                        "' and PassWord = '"+user_inf[1]+"' and Status =1";
                rs = stat.executeQuery(ss);
                if(rs.next()){
                    this.send("已经在线");
                }
                else {
                ss = "select * from user where ID = '" +user_inf[0]+
                        "' and PassWord = '"+user_inf[1]+"'";
                rs = stat.executeQuery(ss);
                if(rs.next()){
                    String friend = "1 ";
                    friend = friend +rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)
                                + " "+rs.getString(4)+" "+rs.getInt(5)+" "+rs.getString(7)+" ";
                    int port = info.port;
                     ss = "update  user set IP = '"+user_inf[2]+"' where ID = '" +user_inf[0]+ "'";
                     stat.executeUpdate(ss);
                     ss = "update  user set Port = "+port+" where ID = '" +user_inf[0]+ "'";
                     stat.executeUpdate(ss);
                     ss = "update  user set Status = 1 where ID = '" +user_inf[0]+ "'";
                     stat.executeUpdate(ss);
                    ss = "select * from user where ID in (select FriendId  from  friends_list "
                             + "where  ID =  '" +user_inf[0]+"')";
                     rs = stat.executeQuery(ss);
                     info.ID = user_inf[0];
                     while(rs.next()){
                         String mm = rs.getString(1)+" "+rs.getString(2)+" "
                                 +rs.getString(4)+" "+rs.getInt(5)+" "
                                 +rs.getInt(6)+" "+rs.getString(7)+" ";
                         System.out.println("好友信息: "+mm);
                         friend = friend + mm;
                      }
                     this.send(friend);
                     con.close();
                }
                else 
                   this.send("cannotLoad");
              }
               
            } catch (SQLException ex) {
                Logger.getLogger(Serveror.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Serveror.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        void  can_login(String s){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/QQ_DataBase", "root","root");
                stat = con.createStatement();
                StringTokenizer st = new StringTokenizer(s," ");
                st.nextToken();
                String user_inf[] = new String[4];
                int i = 0;
                while(st.hasMoreTokens()){
                    user_inf[i] = st.nextToken();
                    i++;
                }
                System.out.println("用户ID:"+user_inf[0]+" 用户名："+user_inf[1]+" 密码："+
                        user_inf[2]+"出生日期： "+user_inf[3]);
                String ss = "select * from user where ID = '" +user_inf[0]+"'";
                rs = stat.executeQuery(ss);
                if(rs.next())
                    this.send("id");
                else{
                    ss = "select * from user where Name = '" +user_inf[1]+"'";
                    rs = stat.executeQuery(ss);
                    if(rs.next())
                        this.send("name");
                    else{
                        System.out.println(info.Ip);
                        ss = "insert into user values ( '"
                        +user_inf[0]+"','"+ user_inf[1] +"', '"
                        + user_inf[2] +"','"+info.Ip+"',"+info.port+",0,'"
                                + user_inf[3]+"');";
                        stat.executeUpdate(ss);
                        this.send("succeed");
                    }
                       
                }
              con.close();
            }
            catch (ClassNotFoundException ex) {
               
            }
            catch (SQLException ex) {
               
            } 
            
        }
        public void send(String s){
            try {
                dos.writeUTF(s);
            } 
            catch (IOException ex) {
            }

         }
        void count_User(String str){
            StringTokenizer st = new StringTokenizer(str," ");
                st.nextToken();
                info.name = st.nextToken();
                info.Ip = st.nextToken();
                System.out.println("用户 "+info.name+" "+info.Ip);
                if(clients.size()>1){
                    String s2 = "";
                    for(int i=0; i<clients.size()-1; i++){
                        Client c = clients.elementAt(i);
                        s2 = s2+c.info.name+" "+c.info.Ip+" ";
                        c.send("5 "+this.info.name+" "+this.info.Ip);
                        System.out.println("群聊");
                }
                this.send("5 "+s2);
                }
        }
        void updataUseInfor(String s){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/QQ_DataBase", "root","root");
                stat = con.createStatement();
                StringTokenizer st = new StringTokenizer(s," ");
                st.nextToken();
                String user_inf[] = new String[5];
                int i = 0;
                while(st.hasMoreTokens()){
                    user_inf[i] = st.nextToken();
                    i++;
                }
             String ss = "update  user set Name = '"+user_inf[1]+"' where ID = '" +user_inf[0]+ "'";
             stat.executeUpdate(ss);
             ss = "update  user set Password = '"+user_inf[2]+"' where ID = '" +user_inf[0]+ "'";
             stat.executeUpdate(ss);
             ss = "update  user set Status = "+user_inf[3]+" where ID = '" +user_inf[0]+ "'";
             stat.executeUpdate(ss);
             ss = "update  user set BirthDay = '"+user_inf[4]+"' where ID = '" +user_inf[0]+ "'";
             stat.executeUpdate(ss);
             this.send("4 susscess");
             con.close();
            } 
            catch (SQLException ex) {
                Logger.getLogger(Serveror.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (ClassNotFoundException ex) {
                Logger.getLogger(Serveror.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        void add_Friend(String s){
            String fri = s.substring(2);
            
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/QQ_DataBase", "root","root");
                stat = con.createStatement();
               String ss = "insert into friends_list values ( '"
                        +fri+"','"+ info.ID +"', ' ');";
               stat.executeUpdate(ss);
               ss = "insert into friends_list values ( '"
                        +info.ID+"','"+ fri +"', ' ');";
               stat.executeUpdate(ss);
               ss = "select * from user where ID = '" +fri+"' ";
               rs = stat.executeQuery(ss);
               if(rs.next()){
                   this.send("7 "+ rs.getString(1)+" "+rs.getString(2)+" "
                                 +rs.getString(4)+" "+rs.getInt(5)+" "
                                 +rs.getInt(6)+" "+rs.getString(7));
               }
               else
                   this.send("7 ");
               con.close();
            } 
            catch (SQLException ex) {
                Logger.getLogger(Serveror.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (ClassNotFoundException ex) {
                Logger.getLogger(Serveror.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        void whisper(String s){
            StringTokenizer st = new StringTokenizer(s," ");
            String toFreind = st.nextToken();
            s = s.substring(toFreind.length()+1);
            if(client.size()>1)
            for(int i=0; i<client.size(); i++)
                if(client.elementAt(i).info.name.equals(toFreind))
                 client.elementAt(i).send("9 "+s);
        }
	public void run()
	{
            while(true){
            try {
                String str = dis.readUTF();
                if(str.charAt(0)=='1'){      //负责注册
                    can_login(str);
                }
                else if(str.charAt(0)=='0'){ //负责登入
                    can_load(str);
                }
                else if(str.charAt(0)=='4'){    //修改信息
                    
                    updataUseInfor(str);
                    
                }
                else if(str.charAt(0)=='2'){ //负责通知新的在线的好友
                    inform_online(str);
                   
                }
                else if(str.charAt(0)=='3'){    //群聊
                    clients.add(this);
                    count_User(str);
                    
                }
                else if(str.charAt(0)=='7'){    //添加好友
                    add_Friend(str);
                }
                else if(str.charAt(0)=='9'){   //私聊
                     System.out.println("私聊 消息 "+str);
                    whisper(str.substring(2));
                }
                else if(str.charAt(0)=='6'){
                    System.out.println("群聊 消息 "+str);
                    if(clients.size()>1)
                    for(int i=0; i<clients.size(); i++)
                            clients.elementAt(i).send(str);
                            
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            
	   }
        }
    }       
}
