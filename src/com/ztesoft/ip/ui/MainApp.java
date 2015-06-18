package com.ztesoft.ip.ui;

import java.io.File;
import java.io.IOException;

import jodd.util.StringUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ztesoft.ip.utils.LayoutUtils;
import com.ztesoft.ip.utils.PropertiesUtil;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Combo;

public class MainApp {
	protected Object result;
	protected static Shell shello;
	protected static Composite shell;
	private static StyledText ip;
	private static StyledText ym;
	private static StyledText wg;
	private static StyledText dns1;
	private static StyledText dns2;
	private static String namestr;
	private static String dns2str;
	private static String dns1str;
	private static String wgstr;
	private static String ymstr;
	private static String ipstr;
	private static Combo name;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			Display display = Display.getDefault();
			shello = new Shell(display, 0);
			shello.setSize(636, 435);
			shello.setLayout(new FormLayout());
			{
				shell = LayoutUtils.centerDWdefult(shello, "局域网网络环境配置工具", true, false);
			}
			CTabFolder tabFolder = new CTabFolder(shell, SWT.BORDER);
			FormData fd_tabFolder = new FormData();
			fd_tabFolder.left = new FormAttachment(0);
			fd_tabFolder.right = new FormAttachment(100);
			fd_tabFolder.top = new FormAttachment(0);
			fd_tabFolder.bottom = new FormAttachment(100);
			tabFolder.setLayoutData(fd_tabFolder);
			tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

			CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
			tabItem.setText("自动获取");

			Composite composite = new Composite(tabFolder, SWT.NONE);
			tabItem.setControl(composite);
			composite.setLayout(new FormLayout());

			CTabItem tabItem_1 = new CTabItem(tabFolder, SWT.NONE);
			tabItem_1.setText("手动获取");

			Composite composite_1 = new Composite(tabFolder, SWT.NONE);
			tabItem_1.setControl(composite_1);
			composite_1.setLayout(null);

			CLabel lblIp = new CLabel(composite_1, SWT.NONE);
			lblIp.setBounds(93, 54, 70, 23);
			lblIp.setText("ip 地址:");

			CLabel label = new CLabel(composite_1, SWT.NONE);
			label.setBounds(93, 103, 70, 23);
			label.setText("子网掩码:");

			CLabel label_1 = new CLabel(composite_1, SWT.NONE);
			label_1.setBounds(93, 147, 70, 23);
			label_1.setText("默认网关:");

			CLabel lbldns = new CLabel(composite_1, SWT.NONE);
			lbldns.setBounds(93, 195, 70, 23);
			lbldns.setText("首选DNS:");

			CLabel lbldns_1 = new CLabel(composite_1, SWT.NONE);
			lbldns_1.setBounds(93, 242, 70, 23);
			lbldns_1.setText("备用DNS:");

			Button button = new Button(composite_1, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					doset();
				}
			});
			button.setBounds(193, 304, 40, 27);
			button.setText("设 置");

			Button button_1 = new Button(composite_1, SWT.NONE);
			button_1.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					shello.close();
				}
			});
			button_1.setBounds(296, 304, 40, 27);
			button_1.setText("取 消");

			Button button_2 = new Button(composite_1, SWT.NONE);
			button_2.setBounds(394, 304, 60, 27);
			button_2.setText("在线查询");

			ip = new StyledText(composite_1, SWT.BORDER);
			ip.setBounds(172, 56, 180, 21);

			ym = new StyledText(composite_1, SWT.BORDER);
			ym.setBounds(172, 105, 180, 21);

			wg = new StyledText(composite_1, SWT.BORDER);
			wg.setBounds(172, 149, 180, 21);

			dns1 = new StyledText(composite_1, SWT.BORDER);
			dns1.setBounds(172, 197, 180, 21);

			dns2 = new StyledText(composite_1, SWT.BORDER);
			dns2.setBounds(172, 244, 180, 21);

			CLabel label_2 = new CLabel(composite_1, SWT.NONE);
			label_2.setText("网络名称");
			label_2.setBounds(93, 10, 70, 23);

			name = new Combo(composite_1, SWT.NONE);
			name.setBounds(172, 10, 180, 25);

			initDate();
			shello.open();
			shello.layout();

			while (!shello.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void runbat(String batName) {
		try {
			Runtime.getRuntime().exec(batName);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		System.out.println("child thread done");
	}

	protected static void doset() {
		// TODO Auto-generated method stub
		String c1 = " netsh interface ip set address name=\"" + namestr + "\" static " + ipstr + " " + ymstr + " " + wgstr + " >nul ";
		String c2 = " netsh interface ip add dns \"" + namestr + "\" " + dns1str + " index=1 >nul ";
		String c3 = " netsh interface ip add dns \"" + namestr + "\" " + dns2str + " index=2 >nul ";
		String[] cmds = { c1, c2, c3 };

		try {
			Runtime.getRuntime().exec(c1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		/*
		 * File f = new File("resouce/config/updateIp.bat");
		 * System.out.println(f.getAbsolutePath()); String cmd1 =
		 * f.getAbsolutePath();// pass runbat(cmd1);
		 */
	}

	private static void initDate() {
		// TODO Auto-generated method stub

		PropertiesUtil.load("resouce/config", "value.config");

		namestr = PropertiesUtil.getProperty("name");
		// #1 无线网络连接 2 本地连接
		if (StringUtil.equals(namestr, "1")) {
			namestr = "无线网络连接";
		}
		if (StringUtil.equals(namestr, "2")) {
			namestr = "本地连接";
		}
		ipstr = PropertiesUtil.getProperty("ip");
		ymstr = PropertiesUtil.getProperty("ym");
		wgstr = PropertiesUtil.getProperty("wg");
		dns1str = PropertiesUtil.getProperty("dns1");
		dns2str = PropertiesUtil.getProperty("dns2");

		name.setText(namestr);
		ip.setText(ipstr);
		ym.setText(ymstr);
		wg.setText(wgstr);
		dns1.setText(dns1str);
		dns2.setText(dns2str);

	}
}
