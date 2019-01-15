import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*
此程序通过默认路径对文件目录进行备份，修改默认代码在主函数中filePath数组进行修改。
备份的保存地点按照当天的日期命名（YY.MM.DD）。
导出为.jar之后就不能改默认的了，修改路径可以通过文本框改。备份完成之后还会打开备份完成文件夹。
*/

public class BackupU {
	public static String date() {
		//此方法用于获取当前日期，返回（YY.MM.DD）的String型
		Calendar cal = Calendar.getInstance();
		String sDate=String.valueOf(cal.get(Calendar.YEAR))+".";
		sDate+=String.valueOf(cal.get(Calendar.MONTH)+1)+".";
		sDate+=String.valueOf(cal.get(Calendar.DATE));
		return sDate.substring(2);	//去掉"20XX"前的"20"
	}
	
	public static String newFile(String path,String[] a) {
		//此方法用于在指定的备份到目录下新建以日期命名的文件夹
		path+="/";
		path+=date();
		File file=new File(path);
		if(file.exists()) {
			a[2]="0";
			return path;
		}
		file.mkdirs();
		a[2]="1";
		return path;
	} 
	
	public static void copyDir(String sourcePath, String newPath) throws IOException {
		//此方法用于复制目录下所有文件
		File file = new File(sourcePath);
		String[] filePath = file.list();

		if (!(new File(newPath)).exists()) {
		    (new File(newPath)).mkdir();
		}

		for (int i = 0; i < filePath.length; i++) {
		    if ((new File(sourcePath + file.separator + filePath[i])).isDirectory()) {
			copyDir(sourcePath  + file.separator  + filePath[i], newPath  + file.separator + filePath[i]);
		    }

		    if (new File(sourcePath  + file.separator + filePath[i]).isFile()) {
			copyFile(sourcePath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
		    }

		}
    	}
	
	public static void copyFile(String oldPath, String newPath) throws IOException {
		//此方法用于复制文件
		File oldFile = new File(oldPath);
		File file = new File(newPath);
		FileInputStream in = new FileInputStream(oldFile);
		FileOutputStream out = new FileOutputStream(file);;

		byte[] buffer=new byte[2097152];
		int readByte = 0;
		while((readByte = in.read(buffer)) != -1){
		    out.write(buffer, 0, readByte);
		}

		in.close();
		out.close();
	}

	public static void openFile(String path) {
		//此方法用于打开指定目录的文件夹
		try {
            		java.awt.Desktop.getDesktop().open(new File(path));
        	} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		String[] filePath= {"G:","F:/备份/U盘备份","0"};	//[0]:备份自路径;[1]:备份到路径;[2]:用来判断备份到路径文件是否存在
		
		// 创建 JFrame 实例
		//窗口与容器初始化
		JFrame frame = new JFrame("U盘备份     JAVA v2.0");
		frame.setSize(330, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.add(panel);
		panel.setLayout(null);
		//标签与文本框
		JLabel YLabel = new JLabel("备份  自：");
		YLabel.setBounds(10,20,80,25);
		panel.add(YLabel);
		JTextField YText = new JTextField(filePath[0]);
		YText.setBounds(100,20,150,25);
		panel.add(YText);

		JLabel MLabel = new JLabel("备份  到：");
		MLabel.setBounds(10,50,80,25);
		panel.add(MLabel);
		JTextField MText = new JTextField(filePath[1]);
		MText.setBounds(100,50,150,25);
		panel.add(MText);

		JLabel author = new JLabel("作者：EticosCheng   当前日期："+date());
		author.setBounds(10,80,280,25);
		panel.add(author);

		// 创建开始与默认按钮
		JButton beginButton = new JButton("开始");
		beginButton.setBounds(50, 110, 80, 25);
		panel.add(beginButton);
		beginButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String MfilePath=newFile(MText.getText(),filePath);
					File yPath=new File(YText.getText());	//判断备份自目录是否存在
					if(!yPath.exists()) {
						JOptionPane.showMessageDialog(null, "失败：备份自目录不存在！\n","U盘备份     JAVA v2.0",0);
						System.exit(0);
					}
					if(filePath[2].equals("0")) {
						JOptionPane.showMessageDialog(null, "失败：备份文件夹已存在！\n"+MfilePath,"U盘备份     JAVA v2.0",0);
						openFile(MText.getText());
						System.exit(0);
					}

					try {
						long starTime=System.currentTimeMillis();
						copyDir(filePath[0],MfilePath);
						long endTime=System.currentTimeMillis();
						JOptionPane.showMessageDialog(null, "备份完成！\n耗时："+(endTime-starTime)+"ms\n"+MfilePath,"U盘备份     JAVA v2.0",1);
					} catch (IOException e1) {
						e1.printStackTrace();
					} finally {
						openFile(MText.getText());
						System.exit(0);
					}
				}
			});

		JButton resetButton = new JButton("默认");
		resetButton.setBounds(160, 110, 80, 25);
		panel.add(resetButton);
		resetButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					YText.setText(filePath[0]);
					MText.setText(filePath[1]);
				}
			});

		// 设置界面可见
		frame.setVisible(true);
        
	}

}
