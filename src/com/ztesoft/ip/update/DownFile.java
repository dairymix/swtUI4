package com.ztesoft.ip.update;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.ztesoft.ip.utils.LayoutUtils;
import com.ztesoft.ip.utils.PropertiesUtil;

import eclipse.wb.swt.SWTResourceManager;

public class DownFile extends Dialog {

	private static String downurl;
	private static String fileName;
	private static String filePath;
	protected Object result;
	protected Shell shell;
	private ProgressBar updatepro;
	private Button begin;
	private Button cacel;
	static {
		PropertiesUtil.load("resouce/config", "value.config");

		downurl = PropertiesUtil.getProperty("updateUrl");
		fileName = PropertiesUtil.getProperty("downloadFilename");
		filePath = PropertiesUtil.getProperty("downloadFilefolder");
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public DownFile(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		LayoutUtils.center(shell);

		updatepro = new ProgressBar(shell, SWT.HORIZONTAL);

		updatepro.setBounds(109, 21, 337, 17);
		updatepro.setMaximum(100);
		updatepro.setMinimum(0);

		Label label = new Label(shell, SWT.NONE);
		label.setBounds(42, 21, 61, 17);
		label.setText("下载进度");

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(152, 84, 294, 17);
		label_1.setText("下载完成将自动重启更新!");

		begin = new Button(shell, SWT.NONE);
		begin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				down();
			}
		});
		begin.setBounds(80, 127, 80, 27);
		begin.setText("开 始");

		cacel = new Button(shell, SWT.NONE);
		cacel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		cacel.setBounds(284, 127, 80, 27);
		cacel.setText("取 消");
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	protected void down() {
		// TODO Auto-generated method stub
		cacel.setEnabled(false);
		HttpURLConnection httpURLConnection = null;
		URL url = null;
		BufferedInputStream bis = null;
		byte[] buf = new byte[10240];
		int size = 0;
		File filea = new File(filePath);
		if (!filea.exists()) {
			filea.mkdir();
		}
		String remoteUrl = downurl;

		// 检查本地文件
		RandomAccessFile rndFile = null;
		File file = new File(filePath + "//" + fileName);
		long remoteFileSize = getRemoteFileSzie(remoteUrl);
		long nPos = 0;

		System.out.println("remote size" + remoteFileSize + "--" + (int) remoteFileSize);
		long every = remoteFileSize / 100;
		System.out.println("every size" + every);

		if (file.exists()) {
			long localFileSzie = file.length();
			if (localFileSzie < remoteFileSize) {
				System.out.println("文件续传...");
				nPos = localFileSzie;
			} else {
				System.out.println("文件存在，重新下载...");
				file.delete();
				try {
					file.createNewFile();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		} else {
			// 建立文件
			try {
				file.createNewFile();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		// 下载文件
		try {
			url = new URL(remoteUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			// 设置User-Agent
			httpURLConnection.setRequestProperty("User-Agent", "Net");
			// 设置续传开始
			httpURLConnection.setRequestProperty("Range", "bytes=" + nPos + "-");
			// 获取输入流
			bis = new BufferedInputStream(httpURLConnection.getInputStream());
			rndFile = new RandomAccessFile(filePath + "\\" + fileName, "rw");
			rndFile.seek(nPos);
			long upsize = 0L;// 递增计数
			int num = 0;
			while ((size = bis.read(buf)) != -1) {
				// if (i > 500) break;
				rndFile.write(buf, 0, size);
				upsize += size;
				num = (int) (upsize / every);
				System.out.println(num + "///////////");
				updatepro.setSelection(num);
			}
			updatepro.setSelection(100);
			httpURLConnection.disconnect();

			dorestart();
		} catch (Exception e) {
			// TODO: handle exception
			MessageDialog.openError(shell, "网络异常", "网络连接失败,请检查网络环境!");
			return;
		}
	}

	private void dorestart() {
		// TODO Auto-generated method stub
		File thisfile = new File("update_fat.jar");
		System.out.println(thisfile.getAbsolutePath());
		thisfile.renameTo(new File("update_fat-old.jar"));
		copy("updatefolder/update_fat.jar", "update_fat.jar");
	}

	private void copy(String here, String to) {
		// TODO Auto-generated method stub
		File s = new File(here);
		File t = new File(to);
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.BORDER | SWT.TITLE | SWT.PRIMARY_MODAL);
		shell.setSize(498, 192);
		shell.setText("更新程序");
		// 禁止shell的关闭按钮 esc

		shell.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println(e.keyCode);
				e.doit = false;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println(e.keyCode);
				e.doit = false;
			}
		});
	}

	public static long getRemoteFileSzie(String url) {
		long size = 0;
		try {
			HttpURLConnection httpUrl = (HttpURLConnection) (new URL(url)).openConnection();
			size = httpUrl.getContentLength();
			httpUrl.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return size;
	}
}
