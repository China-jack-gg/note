import java.util.*;
import java.util.regex.Pattern;
import java.io.*;
import java.sql.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

interface Database{
	static final String Driver="com.mysql.cj.jdbc.Driver";//引擎
	static final String Url="jdbc:mysql://localhost:3306/test?useSSL=true&serverTimezone=UTC&characterEncoding=UTF-8";//数据库连接
}
class Login{//登录窗口
	JFrame frame = new JFrame("My Note");//主窗口
	JPanel pan1 = new JPanel();
	JPanel pan2 = new JPanel();
	JPanel pan3 = new JPanel();
	JLabel lab1 = new JLabel("My Note 登录",JLabel.CENTER);
	JLabel lab2 = new JLabel("用户名：");
	JLabel lab3 = new JLabel("密码：");
	JTextField tuser = new JTextField();//用户名
	JPasswordField tpassword = new JPasswordField();//密码
	JButton btn1 = new JButton("确定");
	JButton btn2 = new JButton("清除");
	JButton btn3 = new JButton("注册");
	
	public Login(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//设置显示风格为当前系统风格
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		btn1.setFocusable(false);//设置按钮不能获得焦点
		btn1.setSize(10,30);
		btn2.setFocusable(false);
		btn3.setFocusable(false);
		
		lab1.setForeground(Color.white);//设置背景
		pan1.setBackground(Color.blue);
		pan1.add(lab1);
		
		pan2.setLayout(new GridLayout(2,2));
		pan2.add(lab2);
		pan2.add(tuser);
		pan2.add(lab3);
		pan2.add(tpassword);
		
		pan3.add(btn1);
		pan3.add(btn2);
		pan3.add(btn3);
		
		tuser.addActionListener(new ActionListener() {//用户名文本框
			@Override
			public void actionPerformed(ActionEvent e) {
				ensure();
			}
		});
		
		tpassword.addActionListener(new ActionListener() {//密码文本框：按回车可直接确认登录
			@Override
			public void actionPerformed(ActionEvent e) {
				ensure();
			}
		});
		
		btn1.addActionListener(new ActionListener() {//主界面的确定按钮，功能是进行确认登录
			@Override
			public void actionPerformed(ActionEvent e) {
				ensure();
			}
		});
		
		btn2.addActionListener(new ActionListener() {//主界面的清除
			@Override
			public void actionPerformed(ActionEvent e) {
				tuser.setText("");
				tpassword.setText("");
			}
		});
		
		btn3.addActionListener(new ActionListener() {//主界面的注册按钮

			@Override
			public void actionPerformed(ActionEvent e) {
				close();
				JFrame frame = new JFrame("新用户注册");
				JLabel lab1 = new JLabel("输入新用户名：",JLabel.RIGHT);
				JLabel lab2 = new JLabel("输入密码：",JLabel.RIGHT);
				JLabel lab3 = new JLabel("重新输入密码：",JLabel.RIGHT);
				JLabel lab4 = new JLabel("");//提示信息框
				
				JTextField tuser = new JTextField();//用户
				
				JPasswordField tpassword = new JPasswordField();//密码
				JPasswordField tsureword = new JPasswordField();//确认密码
				JButton btn1 = new JButton("确定");
				JButton btn2 = new JButton("清除");
				
				lab4.setForeground(Color.red);//提示信息字体颜色设置
					
				btn1.addActionListener(new ActionListener() {//注册里面的确定按钮
					@Override
					public void actionPerformed(ActionEvent e) {
						String s1=tuser.getText();//获取用户输入内容
						String s2=tpassword.getText();
						String s3=tsureword.getText();
						
						//正则表达式
						String pattern1="^[A-z]{1,10}$";//用户名只能是10个字母
						String pattern2="^\\w{8,15}$";//密码只能是字母数字下划线
						if(!Pattern.matches(pattern1,s1)) {
							lab4.setText("用户名格式不正确!");
							tuser.setText("");
							tpassword.setText("");
							tsureword.setText("");
						}
						else if(!Pattern.matches(pattern2,s2)) {
							lab4.setText("密码格式不正确!");
							tpassword.setText("");
							tsureword.setText("");
						}
						else if(s2.compareTo(s3)!=0) {
							lab4.setText("俩次密码不一样!");
							tpassword.setText("");
							tsureword.setText("");
						}
						else {//账户密码匹配
							String root="root";
							String password="zq212320";
							Connection con = null;//连接数据库类
							Statement st = null;//进行数据库操作的类
							
							boolean f=false;//判断用户名是否冲突
							try {//异常处理
								String sql="select root from account";
								con = ConnectMysql.getCon(root, password);//实例化数据库类
								st = con.createStatement();//获取进行数据库操作的对象
								ResultSet set = st.executeQuery(sql);//获取查询结果
								
								while(set.next()) {/**set是查询结果集，set.next()是第一次走到第一行*/
									if(set.getString(1).compareTo(s1)==0) {
											lab4.setText("用户名冲突!");
											tuser.setText("");
											tpassword.setText("");
											tsureword.setText("");
											f=true;//用户名冲突
											break;
										}
									}
									if(f==false) {//用户名没有冲突
										sql="insert into account(root,password) values('"+s1+"','"+s2+"')";//插入数据库用户记录数据库代码
										st.executeUpdate(sql);//更改数据库，插入用户记录
										frame.dispose();//注册窗口关闭
										JOptionPane.showMessageDialog(frame, "注册成功", "My Note", JOptionPane.INFORMATION_MESSAGE);//提示信息框
										open();//打开“My Note”窗口
									}
									con.close();//关闭数据库连接
									st.close();//关闭进行数据库操作类
								} catch (SQLException e1) {}
								
							}
						}
						
					});
					
					btn2.addActionListener(new ActionListener() {//注册里面的清除按钮

						@Override
						public void actionPerformed(ActionEvent e) {
							lab4.setText("");
							tuser.setText("");
							tpassword.setText("");
							tsureword.setText("");
						}
						
					});
					
					frame.addWindowListener(new WindowAdapter() {//关闭窗口
						public void windowClosing(WindowEvent e) {
							frame.dispose();
							open();
						}
					});
					frame.setLayout(new GridLayout(6,2));//设置注册界面
					frame.add(new JLabel());
					frame.add(new JLabel());
					frame.add(lab1);
					frame.add(tuser);
					frame.add(lab2);
					frame.add(tpassword);
					frame.add(lab3);
					frame.add(tsureword);
					frame.add(new JLabel());
					frame.add(lab4);
					frame.add(btn1);
					frame.add(btn2);
					frame.setLocation(600,300);
					frame.setSize(500,400);
					frame.setResizable(false);
					frame.setVisible(true);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
			
		});
		
		frame.add(pan1,BorderLayout.NORTH);//设置登录界面
		frame.add(pan2,BorderLayout.CENTER);
		frame.add(pan3,BorderLayout.SOUTH);
		frame.setLocation(650,400);
		frame.setSize(500,210);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void ensure() {//链接登录功能
		String root="root";
		String password="zq212320";
		Connection con = null;//连接数据库类
		Statement st = null;//数据库操作类
		String s1 = tuser.getText();
		String s2 = tpassword.getText();
		boolean f = false;//判断用户名或密码是否正确
		
		try {
			String sql="select * from account";
			con = ConnectMysql.getCon(root, password);//获取数据库连接
			st = con.createStatement();//获取数据库操作对象
			ResultSet set = st.executeQuery(sql);//数据库查询
			
			while(set.next()) {
				if(set.getString(1).compareTo(s1)==0 && set.getString(2).compareTo(s2)==0) f = true ;//用户名和密码都正确才行
			}
			if(f) {
				JOptionPane.showMessageDialog(frame, "登录成功", "My Note", JOptionPane.INFORMATION_MESSAGE);
				frame.dispose();//关闭登录窗口
				new Note().iniv();//创建记事本
			}
			else JOptionPane.showMessageDialog(frame, "用户名或密码不正确", "My Note", JOptionPane.ERROR_MESSAGE);
			con.close();
			st.close();
		} catch (SQLException e1) {}
	}
	
	public void close() {//隐藏主窗口
		this.frame.setVisible(false);
	}
	
	public void open()  {//显示主窗口
		this.frame.setVisible(true);
	}
}

public class ConnectMysql{//链接数据库
	Connection con = null;//连接数据库的类
	Statement st = null;//进行数据库操作的类
	public static Connection getCon(String root,String password) {//连接数据库
		Connection con = null;
		try {
			Class.forName(Database.Driver);//数据库启动驱动
			con = DriverManager.getConnection(Database.Url,root,password);//连接数据库
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("出错");
			e.printStackTrace();
		}
		return con;
	}
	public static void main(String[] args){
		new Login();
	}
}