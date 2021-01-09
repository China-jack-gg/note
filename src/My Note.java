import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

class Note {
	private JFrame frame = new JFrame("My Note");//创建一个窗口，标题为“My Note”
	
	private JScrollPane center ;//中间滚轮容器
	private JTextArea area = new JTextArea();//中间文本区域
	
	private String url ="C:\\Users\\Lenovo\\Desktop\\TXT测试";//默认路径文件夹
	private File F = new File(url+"\\My Note.txt");//默认打开文件
	
	private boolean change = false;//是否作了改动
	private boolean New = false;//是否新建了
	private UndoManager undom ;//类似于撤销重复监听器
	
	private JMenuBar bar = new JMenuBar();//菜单组
	
	private JMenu file = new JMenu("文件(F)");//构建选项菜单
	private JMenu compile = new JMenu("编辑(E)");
	private JMenu layout = new JMenu("格式(O)");
	private JMenu look = new JMenu("查看(V)");
	private JMenu help = new JMenu("帮助(H)");
	
	private JMenuItem news = new JMenuItem("新建");//构建文件菜单项
	private JMenuItem open = new JMenuItem("打开");
	private JMenuItem save = new JMenuItem("保存");
	private JMenuItem other = new JMenuItem("另存为");
	private JMenuItem exit = new JMenuItem("退出");
	
	private JMenuItem recall = new JMenuItem("撤回");//构建编辑菜单项
	private JMenuItem repet = new JMenuItem("恢复");
	private JMenuItem cut = new JMenuItem("剪切");
	private JMenuItem copy = new JMenuItem("复制");
	private JMenuItem paste = new JMenuItem("粘贴");
	private JMenuItem delete = new JMenuItem("删除");
	private JMenuItem find = new JMenuItem("查找");
	private JMenuItem replace = new JMenuItem("替换");
	private JMenuItem allsel = new JMenuItem("全选");
	private JMenuItem datetime = new JMenuItem("时间日期");
	
	private JCheckBoxMenuItem wrap = new JCheckBoxMenuItem("自动换行");//构建格式菜单项
	private JMenuItem font = new JMenuItem("字体...");
	
	private JMenuItem bebig = new JMenuItem("放大");//构建查看菜单项
	private JMenuItem besmall = new JMenuItem("缩小");
	private JMenuItem bedefu = new JMenuItem("恢复默认缩放");
	
	private JMenuItem about = new JMenuItem("关于");//构建帮助菜单项
	
	private JPopupMenu pmenu = new JPopupMenu();//右键菜单
	
	private JMenuItem scut = new JMenuItem("剪切");//构建鼠标右击菜单项
	private JMenuItem scopy = new JMenuItem("复制");
	private JMenuItem spaste = new JMenuItem("粘贴");
	private JMenuItem sdelete = new JMenuItem("删除");
	private JMenuItem sallsel = new JMenuItem("全选");

	
	
//初始化函数（核心）
	public void iniv() {
		
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
		
		readFont();//读取字体格式
		center = new JScrollPane(area);

		bar.add(file);//将选项菜单加入菜单组
		bar.add(compile);
		bar.add(layout);
		bar.add(look);
		bar.add(help);
		
		file.add(news);//构建文件菜单项
		file.add(open);
		file.add(save);
		file.add(other);
		file.addSeparator();//添加分割线
		file.add(exit);
		
		compile.add(recall);//构建编辑菜单项
		compile.add(repet);
		compile.addSeparator();
		compile.add(cut);
		compile.add(copy);
		compile.add(paste);
		compile.add(delete);
		compile.addSeparator();
		compile.add(find);
		compile.add(replace);
		compile.addSeparator();
		compile.add(allsel);
		compile.addSeparator();
		compile.add(datetime);
		
		layout.add(wrap);//构建格式菜单项
		layout.add(font);
		
		look.add(bebig);//构建查看菜单
		look.add(besmall);
		look.addSeparator();
		look.add(bedefu);
		
		help.add(about);//构建帮助菜单项
		
		pmenu.add(scut);//鼠标右击菜单
		pmenu.add(scopy);
		pmenu.add(spaste);
		pmenu.add(sdelete);
		pmenu.addSeparator();
		pmenu.add(sallsel);
		area.add(pmenu);
		
		file.setMnemonic(KeyEvent.VK_F);//设置菜单栏热键，如alt+F打开“文件”的下拉菜单
		compile.setMnemonic(KeyEvent.VK_E);
		layout.setMnemonic(KeyEvent.VK_O);
		look.setMnemonic(KeyEvent.VK_V);
		help.setMnemonic(KeyEvent.VK_H);
		
		open.setAccelerator(KeyStroke.getKeyStroke("control O"));//文件菜单项设置快捷键
		news.setAccelerator(KeyStroke.getKeyStroke("control N"));
		save.setAccelerator(KeyStroke.getKeyStroke("control S"));
		other.setAccelerator(KeyStroke.getKeyStroke("shift control released S"));
		exit.setAccelerator(KeyStroke.getKeyStroke("control E"));
		
		recall.setAccelerator(KeyStroke.getKeyStroke("control Z"));//编辑菜单项设置快捷键
		repet.setAccelerator(KeyStroke.getKeyStroke("control Y"));
		copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
		cut.setAccelerator(KeyStroke.getKeyStroke("control X"));
		paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
		delete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
		allsel.setAccelerator(KeyStroke.getKeyStroke("control A"));
		datetime.setAccelerator(KeyStroke.getKeyStroke("control shift released D"));
		
		font.setAccelerator(KeyStroke.getKeyStroke("control F"));//字体选项卡
		
		bebig.setAccelerator(KeyStroke.getKeyStroke("control shift released MINUS"));//查看菜单项设置快捷键
		besmall.setAccelerator(KeyStroke.getKeyStroke("control MINUS"));
		
		about.setAccelerator(KeyStroke.getKeyStroke("control shift released H"));//关于
		
		scopy.setAccelerator(KeyStroke.getKeyStroke("control C"));//右键菜单设置快捷键
		scut.setAccelerator(KeyStroke.getKeyStroke("control X"));
		spaste.setAccelerator(KeyStroke.getKeyStroke("control V"));
		sdelete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
		sallsel.setAccelerator(KeyStroke.getKeyStroke("control A"));

//各个按钮的监听（匿名类）
		news.addActionListener(new ActionListener() {//新建
			@Override
			public void actionPerformed(ActionEvent e) {//监听事件
				if(change) {//改动了
					if(!New) {//不是新建的
						int f=JOptionPane.showConfirmDialog(frame, "是否保存原文件？", "My Note", JOptionPane.YES_NO_OPTION);
						if(f==JOptionPane.YES_OPTION) 
							Save(F);//保存到默认文件
					}else {//是新建的
						int f=JOptionPane.showConfirmDialog(frame, "你想要将更改保存到无标题吗？", "My Note", JOptionPane.YES_NO_OPTION);
						if(f==JOptionPane.YES_OPTION) Save(new File(url+"\\无标题.txt"));//保存到默认路径无标题
					}
				}
				New = true;//新建的
				area.setText("");//清空
				undom = new UndoManager();
				area.getDocument().addUndoableEditListener(undom);//新建也要重新加入撤销监听
				change = false;//新建后的都不是活动状态
				frame.setTitle("无标题");//新建的未保存的没有标题
			}
		});
		
		open.addActionListener(new ActionListener() {//打开
			@Override
			public void actionPerformed(ActionEvent e) {
				if(change) {
					if(New) {//是新建的需要保存
						int f=JOptionPane.showConfirmDialog(frame, "你想要将更改保存到无标题吗？", "My Note", JOptionPane.YES_NO_OPTION);
						if(f==JOptionPane.YES_OPTION) Save(new File(url+"\\无标题.txt"));//保存到默认路径无标题
					}else {
						int f=JOptionPane.showConfirmDialog(frame, "是否保存原文件？", "My Note", JOptionPane.YES_NO_OPTION);
						if(f==JOptionPane.YES_OPTION)
							Save(F);//保存到默认文件
					}
				}
				int result=0;//获取他打开文本框后选择哪个按钮的返回值
				JFileChooser filec=new JFileChooser(url);//文件选择器，默认文件打开位置
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt文件(*.txt)", "txt");//添加可打开文件类型类
			   	filec.setFileFilter(filter);//增加文件过滤器
				filec.setDialogTitle("打开");//设置文本选择框标题
				
				result=filec.showOpenDialog(frame);//result获取的返回值
				
				if(result==JFileChooser.APPROVE_OPTION) {//表示选择了打开按钮
					F=filec.getSelectedFile();//更新默认的文件位置
					Read(F);//读取默认文件
					New = false;//意味着不是新建的了
				}
			}
		});
		
		save.addActionListener(new ActionListener() {//保存
			@Override
			public void actionPerformed(ActionEvent e) {
				if(change) {
					if(New) {//是新建的文件
						int result=0;//获取他保存文本框后选择哪个按钮的返回值
						
						JFileChooser filec=new JFileChooser(url);//文件选择器
						FileNameExtensionFilter filter = new FileNameExtensionFilter("txt文件(*.txt)", "txt");//添加可打开文件类型
					   	filec.setFileFilter(filter);//增加文件过滤器
					   	
						String defaultFileName="My Note"+".txt";//默认文件夹名字
						filec.setSelectedFile(new File(defaultFileName)); //设置默认文件名
						
						filec.setDialogTitle("另存为");//设置文本选择框标题
						result=filec.showSaveDialog(frame);//获取返回值
						
						if(result==JFileChooser.APPROVE_OPTION) {//表示选择了确定按钮
							
							File doc = filec.getCurrentDirectory();//目录文件夹(TXT测试)
							File file = filec.getSelectedFile();//要保存的文件
							
							String[] st = doc.list();//获取文件夹下的所有文件名字
							boolean falg = false;//判断是不是覆盖
							
							for(String x:st) {//遍历
								if(x.compareTo(file.getName())==0) falg = true;//目录下存在名字一样的文件，只获取名字
							}
							
					      if(falg) {
								int f=JOptionPane.showConfirmDialog(frame, "是否覆盖？", "My Note", JOptionPane.YES_NO_OPTION);
								if(f==JOptionPane.YES_OPTION) {
									F=file;//更改默认文件位置
									Save(F);
									New=false;//保存后不是新建的
								}
							}
							  else {
								F=filec.getSelectedFile();//更改默认文件
								Save(F);
								New=false;//保存后不是新建的了
							}
						}
					}
					else {//不是新建的文件
						Save(F);
						change = false;//保存过后就没有改动了
						frame.setTitle(F.getName());//窗口状态
					}
				}
			}
		});
		
		other.addActionListener(new ActionListener() {//另存为
			@Override
			public void actionPerformed(ActionEvent e) {
				int result=0;
				JFileChooser filec=new JFileChooser(url);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt文件(*.txt)", "txt");
			   	filec.setFileFilter(filter);//增加文件过滤器
				String defaultFileName="My Note"+".txt";//默认文件夹名字
				filec.setSelectedFile(new File(defaultFileName)); //设置默认文件名
				filec.setDialogTitle("另存为");//设置文本选择框标题
				result=filec.showSaveDialog(frame);
				if(result==JFileChooser.APPROVE_OPTION) {//表示选择了确定按钮
					File doc = filec.getCurrentDirectory();//目录
					File file = filec.getSelectedFile();//要保存的文件
					String[] st = doc.list();
					boolean falg = false;
					for(String x:st) {
						if(x.compareTo(file.getName())==0)falg = true;//目录下存在名字一样的文件
					}
					if(falg) {
						int f=JOptionPane.showConfirmDialog(null, "是否覆盖？", "My Note", JOptionPane.YES_NO_OPTION);
						if(f==JOptionPane.YES_OPTION) {
							F=file;//更改默认文件位置
							Save(F);
							New=false;//保存后不是新建的了
						}
					}
					else {
						F=filec.getSelectedFile();//更改默认文件
						Save(F);
						New=false;//保存后不是新建的了
					}
				}
			}
		});
		
		exit.addActionListener(new ActionListener(){//退出
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFont();//保存字体格式
				if(change) {//是否改动了
					if(New) {//是新建的
						int res=JOptionPane.showConfirmDialog(frame, "是否退出？", "My Note", JOptionPane.YES_NO_OPTION);
						if(res==JOptionPane.YES_OPTION) {
							int f=JOptionPane.showConfirmDialog(frame, "是否保存到：" + url + "\\无标题.txt", 
																								"My Note", JOptionPane.YES_NO_OPTION);
							if(f==JOptionPane.YES_OPTION)Save(new File(url+"\\无标题.txt"));
							System.exit(0);
						}
					} else {//不是新建的
						int res=JOptionPane.showConfirmDialog(frame, "是否退出？", "My Note", JOptionPane.YES_NO_OPTION);
						if(res==JOptionPane.YES_OPTION) {
							int f=JOptionPane.showConfirmDialog(frame, "是否保存到："+F.getPath(), "My Note", JOptionPane.YES_NO_OPTION);
							if(f==JOptionPane.YES_OPTION)
								Save(F);
							System.exit(0);
						}
					}
				}
				else {//没有改动
					System.exit(0);
				}
			}
		});
		
		recall.addActionListener(new ActionListener() {//撤销
			@Override
			public void actionPerformed(ActionEvent e) {
				if(undom.canUndo()) {//判断是否能撤销
					undom.undo();//撤销操作
				}
			}
		});
		
		repet.addActionListener(new ActionListener() {//恢复
			@Override
			public void actionPerformed(ActionEvent e) {
				if(undom.canRedo()) {//判断是否能恢复
					undom.redo();//恢复操作
				}
			}
		});
		
		cut.addActionListener(new ActionListener() {//剪切
			@Override
			public void actionPerformed(ActionEvent e) {
				area.cut();//JTextArea自带
			}
		});
		
		scut.addActionListener(new ActionListener() {//右击剪切
			@Override
			public void actionPerformed(ActionEvent e) {
				area.cut();
			}
		});
		
		copy.addActionListener(new ActionListener() {//复制
			@Override
			public void actionPerformed(ActionEvent e) {
				area.copy();
			}
		});
		
		scopy.addActionListener(new ActionListener() {//右击复制
			@Override
			public void actionPerformed(ActionEvent e) {
				area.copy();
			}
		});
		
		paste.addActionListener(new ActionListener() {//粘贴
			@Override
			public void actionPerformed(ActionEvent e) {
				area.paste();
			}
		});
		
		spaste.addActionListener(new ActionListener() {//粘贴
			@Override
			public void actionPerformed(ActionEvent e) {
				area.paste();
			}
		});
		
		delete.addActionListener(new ActionListener() {//删除
			@Override
			public void actionPerformed(ActionEvent e) {
				area.replaceSelection("");//把选择的内容替换为空
			}
		});
		
		sdelete.addActionListener(new ActionListener() {//删除
			@Override
			public void actionPerformed(ActionEvent e) {
				area.replaceSelection("");
			}
		});
		
		allsel.addActionListener(new ActionListener() {//全选
			@Override
			public void actionPerformed(ActionEvent e) {
				area.selectAll();
			}
		});
		
		sallsel.addActionListener(new ActionListener() {//全选
			@Override
			public void actionPerformed(ActionEvent e) {
				area.selectAll();
			}
		});
		
		datetime.addActionListener(new ActionListener() {//日期
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy 年 MM 月 dd 日  HH 时 mm 分 ss 秒 ");//设置时间格式
				Date date = new Date(System.currentTimeMillis());//获取当前时间
				String data = formatter.format(date);//得到时间String形式
				JOptionPane.showMessageDialog(frame,data, "当前时间", JOptionPane.INFORMATION_MESSAGE);//提示对话框
			}
		});
		
		wrap.addActionListener(new ActionListener() {//自动换行
			@Override
			public void actionPerformed(ActionEvent e) {
				if(wrap.getState())area.setLineWrap(true);//自带
				else area.setLineWrap(false);
			}
		});
		
		font.addActionListener(new ActionListener() {//字体
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame fm = new JFrame("字体");//字体窗口
				
				String t1[] = new String[] {"宋体","楷体","隶书","微软雅黑","黑体","仿宋","华文彩云"};
				String t2[] = new String[] {"常规","倾斜","加粗","加粗倾斜"};
				String t3[] = new String[] {"初号","小初","一号","小一","二号","小二","三号","小三","四号","小四","五号","小五","六号","小六"};
				
				JComboBox<String> j1 = new JComboBox<String>(t1);//构建下拉列表
				JComboBox<String> j2 = new JComboBox<String>(t2);
				JComboBox<String> j3 = new JComboBox<String>(t3);
				
				j3.setSelectedIndex(5);//设置字体大小下拉列表显示项
				
				JPanel pan1 = new JPanel();//轻型控件容器
				JPanel pan2 = new JPanel();
				JPanel pan3 = new JPanel();
				
				JLabel lab = new JLabel("我的文本文档");//添加“我的文本文档”标签
				
				JButton ensure = new JButton("确定");//添加“确定”按钮
				JButton cancel = new JButton("取消");//添加“取消”按钮
				
				pan1.setLayout(new GridLayout(2,3));//网格布局（两行三列）
				pan1.add(new JLabel("字体："));//添加“字体”标签
				pan1.add(new JLabel("字形："));//添加“字形”标签
				pan1.add(new JLabel("大小："));//添加“大小”标签
				
				pan1.add(j1);//在容器pan1里面添加下拉列表项
				pan1.add(j2);
				pan1.add(j3);
				
				pan2.add(new JLabel("示例："));//添加“示例”标签
				pan2.add(lab);
				
				pan3.setLayout(new GridLayout(2,4));//网格布局（两行四列）
				pan3.add(new JLabel());//空东西
				pan3.add(new JLabel());//空东西
				pan3.add(new JLabel());//空东西
				pan3.add(new JLabel());//空东西
				pan3.add(new JLabel());//空东西
				pan3.add(new JLabel());//空东西
				
				pan3.add(ensure);
				pan3.add(cancel);
			  	
				lab.setBorder(BorderFactory.createBevelBorder(1));//设置样例边框
				lab.setFont(new Font("宋体",Font.PLAIN,18));//设置默认字体
				
				j1.addActionListener(new ActionListener() {//第一个下拉框
					@Override
					public void actionPerformed(ActionEvent e) {
						lab.setFont(new Font((String)j1.getSelectedItem(),lab.getFont().getStyle(),lab.getFont().getSize()));
					}//样例显示
				});
				
				j2.addActionListener(new ActionListener() {//第二个下拉框
					@Override
					public void actionPerformed(ActionEvent e) {
						
						String type = (String)j2.getSelectedItem();//选择的参数
						if(type.compareTo("常规") == 0)
							lab.setFont(new Font(lab.getFont().getFamily(),Font.PLAIN,lab.getFont().getSize()));//Font.PLAIN是字体的参数
						if(type.compareTo("加粗") == 0)
							lab.setFont(new Font(lab.getFont().getFamily(),Font.BOLD,lab.getFont().getSize()));
						if(type.compareTo("倾斜") == 0)
							lab.setFont(new Font(lab.getFont().getFamily(),Font.ITALIC,lab.getFont().getSize()));
						if(type.compareTo("加粗倾斜") == 0)
							lab.setFont(new Font(lab.getFont().getFamily(),Font.BOLD+Font.ITALIC,lab.getFont().getSize()));
					}
				});
				
				j3.addActionListener(new ActionListener() {//第三个下拉框
					@Override
					public void actionPerformed(ActionEvent e) {
						String type = (String)j3.getSelectedItem();//选择的参数
						int size = 0;
						if(type.compareTo("初号") == 0)size=36;
						if(type.compareTo("小初") == 0)size=30;
						if(type.compareTo("一号") == 0)size=26;
						if(type.compareTo("小一") == 0)size=24;
						if(type.compareTo("二号") == 0)size=22;
						if(type.compareTo("小二") == 0)size=18;
						if(type.compareTo("三号") == 0)size=17;
						if(type.compareTo("小三") == 0)size=16;
						if(type.compareTo("四号") == 0)size=15;
						if(type.compareTo("小四") == 0)size=14;
						if(type.compareTo("五号") == 0)size=13;
						if(type.compareTo("小五") == 0)size=12;
						if(type.compareTo("六号") == 0)size=11;
						if(type.compareTo("小六") == 0)size=10;
						lab.setFont(new Font(lab.getFont().getFamily(),lab.getFont().getStyle(),size));//设置字号
					}
				});
				
				ensure.addActionListener(new ActionListener() {//确认按钮
					@Override
					public void actionPerformed(ActionEvent e) {
						area.setFont(lab.getFont());//文本区域的字体换成样例的字体
						fm.dispose();//关闭字体对话框
					}
				});
				
				cancel.addActionListener(new ActionListener() {//取消按钮
					@Override
					public void actionPerformed(ActionEvent e) {
						fm.dispose();//关闭字体对话框
					}
				});
			//字体对话框的参数设置
				fm.setLayout(new GridLayout(3,1));//网格布局（三行一列）
				fm.add(pan1);
				fm.add(pan2);
				fm.add(pan3);
				fm.setLocation(500,150);//字体窗口位置
				fm.setSize(600,300);//字体窗口大小
				fm.setResizable(false);
				fm.setVisible(true);//设置字体窗口可见
				fm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//隐藏当前窗口，并释放窗体占有的其他资源
			}
		});
		
		bebig.addActionListener(new ActionListener() {//放大
			@Override
			public void actionPerformed(ActionEvent e) {
				Font font = area.getFont();//获取文本区域的字体格式
				if(font.getSize() < 1000) {
					area.setFont(new Font(font.getFamily(),font.getStyle(),font.getSize()+1));//控制大小不超限
				}
			}
		});
		
		besmall.addActionListener(new ActionListener() {//缩小
			@Override
			public void actionPerformed(ActionEvent e) {
				Font font = area.getFont();
				if(font.getSize()>10) {
					area.setFont(new Font(font.getFamily(),font.getStyle(),font.getSize()-1));//控制大小不超限
				}
			}
		});
		
		bedefu.addActionListener(new ActionListener() {//恢复默认缩放
			@Override
			public void actionPerformed(ActionEvent e) {//接受操作事件的侦听器接口
				area.setFont(new Font(area.getFont().getFamily(),area.getFont().getStyle(),18));
			}
		});
		
		about.addActionListener(new ActionListener() {//关于
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFrame frame = new JFrame("关于");//创建窗口
				JLabel lab = new JLabel("My Note So Simple !");//添加标签
				lab.setHorizontalAlignment(JLabel.CENTER);//设置文本对齐方式
				
				frame.add(lab,BorderLayout.CENTER);//设置边框对齐方式
				frame.setLocation(350,150);//窗口位置
				frame.setSize(400,100);//窗口大小
				frame.setResizable(false);
				frame.setVisible(true);//设置窗口可见
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//隐藏当前窗口，并释放窗体占有的其他资源
			}
		});
		
		Read(F);//打开时读取默认文件
		
		area.getDocument().addDocumentListener(new DocumentListener() {//获取文本——文本监听器   触发了表示作了改动（重写函数）
			@Override
			public void removeUpdate(DocumentEvent e) {//当文本删除时
				change = true;//发生了改动
				if(frame.getTitle().compareTo("无标题")==0 || frame.getTitle().compareTo("*无标题")==0)
					frame.setTitle("*无标题");//都要改成活动状态无标题
				else frame.setTitle("*"+F.getName());//未保存状态
				recall.setEnabled(true);//动了就是可以撤销的
			}
			@Override
			public void insertUpdate(DocumentEvent e) {//插入
				change = true;//发生了改动
				if(frame.getTitle().compareTo("无标题")==0 || frame.getTitle().compareTo("*无标题")==0)
					frame.setTitle("*无标题");//都要改成活动状态无标题
				else frame.setTitle("*"+F.getName());//未保存状态
				recall.setEnabled(true);//动了就是可以撤销的
			}
			@Override
			public void changedUpdate(DocumentEvent e) {//改变
				change = true;//发生了改动
				if(frame.getTitle().compareTo("无标题")==0 || frame.getTitle().compareTo("*无标题")==0)
					frame.setTitle("*无标题");//都要改成活动状态无标题
				else frame.setTitle("*"+F.getName());//未保存状态
				recall.setEnabled(true);//动了就是可以撤销的
			}
		});
		
		area.addMouseListener(new MouseAdapter() {//右键菜单
			@Override
			public void mouseClicked(MouseEvent e) {//鼠标单击事件
				if(e.isMetaDown())//鼠标右击
					pmenu.show(area,e.getX(),e.getY());//在右击的地方显示
			}
		});
		
		frame.addWindowListener(new WindowAdapter() {//关闭窗口和退出操作基本一样
			@Override
			public void windowClosing(WindowEvent e) {//监听事件
				saveFont();//保存字体格式
				if(change) {//如果改动了
					if(New) {//新建的
						int res=JOptionPane.showConfirmDialog(frame, "是否退出？", "My Note", JOptionPane.YES_NO_OPTION);//确认框的操作
						if(res==JOptionPane.YES_OPTION) {
							int f=JOptionPane.showConfirmDialog(frame, "是否保存到：" + url + "\\无标题.txt", 
																								"My Note", JOptionPane.YES_NO_OPTION);
							if(f==JOptionPane.YES_OPTION)Save(new File(url+"\\无标题.txt"));
							System.exit(0);
						}
					} else {//不是新建的
						int res=JOptionPane.showConfirmDialog(frame, "是否退出？", "My Note", JOptionPane.YES_NO_OPTION);
						if(res==JOptionPane.YES_OPTION) {
							int f=JOptionPane.showConfirmDialog(frame, "是否保存到："+F.getPath(), "My Note", JOptionPane.YES_NO_OPTION);
							if(f==JOptionPane.YES_OPTION)Save(F);
							System.exit(0);
						}
					}
				}
				else {//没有改动
					System.exit(0);
				}
			}
		});
		
		
		MouseWheelListener wheel = center.getMouseWheelListeners()[0];//获取滚动事件
		center.removeMouseWheelListener(wheel);//移除滚动事件（避免冲突）
		center.addMouseWheelListener(new MouseAdapter() {//添加新的滚动事件
			@Override
		    public void mouseWheelMoved(MouseWheelEvent e){ 
				if(e.isControlDown()){//当ctrl键被按下，滚动为放大缩小 
				Font font = area.getFont(); //获取当前文本域字体
				
		        if(e.getWheelRotation()<0 && font.getSize() < 1000){//如果滚动条向前就放大文本
		        	area.setFont(new Font(font.getFamily(),font.getStyle(),font.getSize()+1));
		        	
		        	if(area.getFont().getSize() > 10)  besmall.setEnabled(true);//按钮可以使用
		        	if(area.getFont().getSize() >= 1000)  bebig.setEnabled(false);
		        }else 
		        	if(e.getWheelRotation()>0 && font.getSize() > 10){//滚动条向后就缩小文本 
		        	area.setFont(new Font(font.getFamily(),font.getStyle(),font.getSize()-1)); 
		        	if(area.getFont().getSize() < 1000)  bebig.setEnabled(true);
		        	if(area.getFont().getSize() <= 10)   besmall.setEnabled(false);
		        } 
		      } else {
		    	  center.addMouseWheelListener(wheel);
		    	  wheel.mouseWheelMoved(e);//触发滚动事件
		    	  center.removeMouseWheelListener(wheel);
		      }
		    } 
		});
		
		
//设置窗口的参数
		frame.setJMenuBar(bar);//将菜单组加入容器
		frame.add(center,BorderLayout.CENTER);
		frame.setLocation(300,100);//窗口的位置，单位：像素
		frame.setSize(1300,700);//窗口的大小
		frame.setVisible(true);//设置窗口的可见
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口，并停止程序
	}
	
	public void Read(File f) {//读到文本区域：把文件里面的内容读到记事本上面
		if(!f.exists()) {//文件不存在时
			JOptionPane.showMessageDialog(frame, "文件不存在", "My Note", JOptionPane.ERROR_MESSAGE);//提示信息错误对话框
		}else {
			try {
				FileInputStream in = new FileInputStream(f);//字节输入流
				int len = (int )f.length()*2;/**因为一个字节占两个字符，让它有空间容纳 */
				byte bt[] = new byte[len];//文件所有长度
				int l = in.read(bt);//读取文件的长度
				area.setText("");//清空
				area.setText(new String(bt,0,l));
				change = false;//7
				undom = new UndoManager();//每次读取后就添加一个新的撤销监听
				area.getDocument().addUndoableEditListener(undom);//读取之后再添加撤销,监听文本是否能撤销或者重复
				frame.setTitle(F.getName());
				in.close();
			} catch (IOException e) {}
		}
	}
	
	public void Save(File f) {//保存到文件
		frame.setTitle(F.getName());//保存后都不是活动状态
		try {
			FileOutputStream out = new FileOutputStream(f);//打开要保存到的目标文件
			byte[] bt = area.getText().getBytes();//得到文本框里面的内容的比特形式
			change = false;//保存后都没活动过
			out.write(bt);//写入文件
			out.close();
		} catch (IOException e) {}
	}
	
	public void saveFont() {//保存字体格式
		FileOutputStream file = null ;
		try {
			file = new FileOutputStream("C:\\Users\\Lenovo\\Desktop\\TXT测试\\font.txt");//字节输出流
			DataOutputStream filedata = new DataOutputStream(file);//数据输出流
			filedata.writeUTF(area.getFont().getFamily());//保存String类型：将字体字形字体大小陆续写进去
			file.write(area.getFont().getStyle());
			file.write(area.getFont().getSize());
			file.close();
			filedata.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readFont() {//读取系统字体
		FileInputStream file = null ;
		try {//读取字体格式
			file = new FileInputStream("C:\\Users\\Lenovo\\Desktop\\TXT测试\\font.txt");
			DataInputStream filedata = new DataInputStream(file);
			String f1 = filedata.readUTF();//读取字体
			int f2 = filedata.read();
			int f3 = filedata.read();
			area.setFont(new Font(f1,f2,f3));
			file.close();//关闭文件
			filedata.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	public static void main(String[] args) {
		new Note().iniv();
	}
	*/
}

