package com.ztesoft.ip.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import eclipse.wb.swt.SWTResourceManager;

public class LayoutUtils {

	// 全局颜色控制器

	static List<Composite> compList = new ArrayList<Composite>();
	private static int headHeight;
	private static int footHeight;
	private static int rgbRed;
	private static Color backColortarget = null;// 变色换肤产生的值
	private static RGB backColorRGB = null;// 变色换肤产生的值
	private static int rgbGree;
	private static int rgbBlue;
	static {
		PropertiesUtil.load("resouce/config", "value.config");

		headHeight = PropertiesUtil.getInt("headHeight", 25);
		footHeight = PropertiesUtil.getInt("footHeight", 25);
		rgbRed = PropertiesUtil.getInt("rgbRed", 0);
		rgbGree = PropertiesUtil.getInt("rgbGree", 0);
		rgbBlue = PropertiesUtil.getInt("rgbBlue", 0);
		backColorRGB = new RGB(rgbRed, rgbGree, rgbBlue);
		backColortarget = SWTResourceManager.getColor(backColorRGB);
	}

	/**
	 * @param args
	 */
	public static void center(Shell shell) {
		// TODO Auto-generated method stub
		shell.setLocation(Display.getCurrent().getClientArea().width / 2 - shell.getShell().getSize().x / 2,
				Display.getCurrent().getClientArea().height / 2 - shell.getSize().y / 2);
	}

	public static Composite centerDWmax(final Shell shell, String dialogTitle, boolean hasFoot, boolean canTrag) {
		// TODO Auto-generated method stub
		shell.setLocation(Display.getCurrent().getClientArea().width / 2 - shell.getShell().getSize().x / 2,
				Display.getCurrent().getClientArea().height / 2 - shell.getSize().y / 2);
		shell.setBounds(0, 0, shell.getDisplay().getClientArea().width, shell.getDisplay().getClientArea().height);

		shell.setLayout(new FormLayout());
		shell.setText(dialogTitle);
		Listener l = new Listener() {
			int x, y;

			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				if (e.type == SWT.MouseDown && e.button == 1) {
					System.out.println("mouse down");
					x = e.x;
					y = e.y;
				}
				if (e.type == SWT.MouseMove && (e.stateMask & SWT.BUTTON1) != 0) {
					System.out.println("mouse move");

					Point p = shell.toDisplay(e.x, e.y);
					p.x -= x;
					p.y -= y;
					shell.setLocation(p);
				}
			}
		};

		final Composite compHead = new Composite(shell, SWT.NONE);
		compHead.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				// TODO Auto-generated method stub
				if (null != compList && compList.size() > 0) {
					Iterator<Composite> it = compList.iterator();
					while (it.hasNext()) {
						if (it.next().equals(e.widget)) {
							it.remove();
							System.out.println("remove ok");
						}
					}
				}
			}
		});
		compHead.setBackground(backColortarget);
		compHead.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(100);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		fd_composite.height = headHeight;
		compHead.setLayoutData(fd_composite);

		CLabel btClose = new CLabel(compHead, SWT.NONE);
		btClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.close();
			}
		});
		btClose.setAlignment(SWT.CENTER);
		btClose.setText("X");
		btClose.setBackground(backColortarget);
		btClose.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		FormData fd_button = new FormData();
		fd_button.top = new FormAttachment(0);
		fd_button.right = new FormAttachment(100);
		fd_button.bottom = new FormAttachment(100);
		fd_button.width = 40;
		btClose.setLayoutData(fd_button);

		CLabel btMin = new CLabel(compHead, SWT.NONE);
		btMin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.setMinimized(true);
			}
		});
		btMin.setAlignment(SWT.CENTER);
		btMin.setText(" 一");
		btMin.setBackground(backColortarget);
		btMin.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		FormData fd_button_1 = new FormData();
		fd_button_1.right = new FormAttachment(btClose);
		fd_button_1.top = new FormAttachment(0);
		fd_button_1.bottom = new FormAttachment(100);
		fd_button_1.width = 40;
		btMin.setLayoutData(fd_button_1);

		CLabel btColor = new CLabel(compHead, SWT.NONE);
		btColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// change bg color
				doChangeColor(shell);
			}
		});
		btColor.setAlignment(SWT.CENTER);
		btColor.setText("☞");
		btColor.setBackground(backColortarget);
		btColor.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		FormData fd_button_2 = new FormData();
		fd_button_2.top = new FormAttachment(0);
		fd_button_2.right = new FormAttachment(btMin);
		fd_button_2.bottom = new FormAttachment(100);
		fd_button_2.width = 40;
		btColor.setLayoutData(fd_button_2);

		CLabel dlgTitle = new CLabel(compHead, SWT.NONE);

		dlgTitle.setAlignment(SWT.LEFT);
		dlgTitle.setText(dialogTitle);
		dlgTitle.setBackground(backColortarget);
		dlgTitle.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		FormData fd_dlgTitle = new FormData();
		fd_dlgTitle.top = new FormAttachment(0);
		fd_dlgTitle.left = new FormAttachment(0);
		fd_dlgTitle.bottom = new FormAttachment(100);
		fd_dlgTitle.width = 400;
		dlgTitle.setLayoutData(fd_dlgTitle);

		Composite content = new Composite(shell, 0);
		content.setLayout(new FormLayout());
		FormData fd_content = new FormData();
		fd_content.top = new FormAttachment(compHead);
		fd_content.bottom = new FormAttachment(100);

		if (hasFoot) {

			Composite compFoot = new Composite(shell, SWT.NONE);

			compFoot.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
					// TODO Auto-generated method stub
					if (null != compList && compList.size() > 0) {
						Iterator<Composite> it = compList.iterator();
						while (it.hasNext()) {
							if (it.next().equals(e.widget)) {
								it.remove();
								System.out.println("remove ok");
							}
						}
					}
				}
			});
			compFoot.setBackground(backColortarget);
			FormData fd_composite_1 = new FormData();
			fd_composite_1.right = new FormAttachment(100);
			fd_composite_1.bottom = new FormAttachment(100);
			fd_composite_1.left = new FormAttachment(0);
			fd_composite_1.height = footHeight;
			compFoot.setLayoutData(fd_composite_1);

			compList.add(compFoot);
			if (canTrag) {
				compFoot.addListener(SWT.MouseDown, l);
				compFoot.addListener(SWT.MouseMove, l);
			}
			fd_content.bottom = new FormAttachment(compFoot);
			content.setLayoutData(fd_content);
		} else {
			fd_content.bottom = new FormAttachment(100);
			content.setLayoutData(fd_content);
		}

		compList.add(compHead);

		if (canTrag) {
			compHead.addListener(SWT.MouseDown, l);
			compHead.addListener(SWT.MouseMove, l);

			dlgTitle.addListener(SWT.MouseDown, l);
			dlgTitle.addListener(SWT.MouseMove, l);
		}
		return content;
	}

	public static Composite centerDWdefult(final Shell shell, String dialogTitle, boolean hasFoot, boolean canTrag) {
		// TODO Auto-generated method stub
		shell.setLocation(Display.getCurrent().getClientArea().width / 2 - shell.getShell().getSize().x / 2,
				Display.getCurrent().getClientArea().height / 2 - shell.getSize().y / 2);
		shell.setLayout(new FormLayout());
		shell.setText(dialogTitle);
		Listener l = new Listener() {
			int x, y;

			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				if (e.type == SWT.MouseDown && e.button == 1) {
					System.out.println("mouse down");
					x = e.x;
					y = e.y;
				}
				if (e.type == SWT.MouseMove && (e.stateMask & SWT.BUTTON1) != 0) {
					System.out.println("mouse move");

					Point p = shell.toDisplay(e.x, e.y);
					p.x -= x;
					p.y -= y;
					shell.setLocation(p);
				}
			}
		};

		final Composite compHead = new Composite(shell, SWT.NONE);
		compHead.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				// TODO Auto-generated method stub
				if (null != compList && compList.size() > 0) {
					Iterator<Composite> it = compList.iterator();
					while (it.hasNext()) {
						if (it.next().equals(e.widget)) {
							it.remove();
							System.out.println("remove ok");
						}
					}
				}
			}
		});
		compHead.setBackground(backColortarget);
		compHead.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(100);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		fd_composite.height = headHeight;
		compHead.setLayoutData(fd_composite);

		CLabel btClose = new CLabel(compHead, SWT.NONE);
		btClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.close();
			}
		});
		btClose.setAlignment(SWT.CENTER);
		btClose.setText("X");
		btClose.setBackground(backColortarget);
		btClose.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		FormData fd_button = new FormData();
		fd_button.top = new FormAttachment(0);
		fd_button.right = new FormAttachment(100);
		fd_button.bottom = new FormAttachment(100);
		fd_button.width = 40;
		btClose.setLayoutData(fd_button);

		CLabel btMin = new CLabel(compHead, SWT.NONE);
		btMin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.setMinimized(true);
			}
		});
		btMin.setAlignment(SWT.CENTER);
		btMin.setText(" 一");
		btMin.setBackground(backColortarget);
		btMin.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		FormData fd_button_1 = new FormData();
		fd_button_1.right = new FormAttachment(btClose);
		fd_button_1.top = new FormAttachment(0);
		fd_button_1.bottom = new FormAttachment(100);
		fd_button_1.width = 40;
		btMin.setLayoutData(fd_button_1);

		CLabel btColor = new CLabel(compHead, SWT.NONE);
		btColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				// change bg color
				doChangeColor(shell);
			}
		});
		btColor.setAlignment(SWT.CENTER);
		btColor.setText("☞");
		btColor.setBackground(backColortarget);
		btColor.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		FormData fd_button_2 = new FormData();
		fd_button_2.top = new FormAttachment(0);
		fd_button_2.right = new FormAttachment(btMin);
		fd_button_2.bottom = new FormAttachment(100);
		fd_button_2.width = 40;
		btColor.setLayoutData(fd_button_2);

		CLabel dlgTitle = new CLabel(compHead, SWT.NONE);

		dlgTitle.setAlignment(SWT.LEFT);
		dlgTitle.setText(dialogTitle);
		dlgTitle.setBackground(backColortarget);
		dlgTitle.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		FormData fd_dlgTitle = new FormData();
		fd_dlgTitle.top = new FormAttachment(0);
		fd_dlgTitle.left = new FormAttachment(0);
		fd_dlgTitle.bottom = new FormAttachment(100);
		fd_dlgTitle.width = 400;
		dlgTitle.setLayoutData(fd_dlgTitle);

		Composite content = new Composite(shell, 0);
		content.setLayout(new FormLayout());
		FormData fd_content = new FormData();
		fd_content.top = new FormAttachment(compHead);
		fd_content.bottom = new FormAttachment(100);
		fd_content.left = new FormAttachment(0);
		fd_content.right = new FormAttachment(100);

		if (hasFoot) {

			Composite compFoot = new Composite(shell, SWT.NONE);

			compFoot.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {
					// TODO Auto-generated method stub
					if (null != compList && compList.size() > 0) {
						Iterator<Composite> it = compList.iterator();
						while (it.hasNext()) {
							if (it.next().equals(e.widget)) {
								it.remove();
								System.out.println("remove ok");
							}
						}
					}
				}
			});
			compFoot.setBackground(backColortarget);
			FormData fd_composite_1 = new FormData();
			fd_composite_1.right = new FormAttachment(100);
			fd_composite_1.bottom = new FormAttachment(100);
			fd_composite_1.left = new FormAttachment(0);
			fd_composite_1.height = footHeight;
			compFoot.setLayoutData(fd_composite_1);

			compList.add(compFoot);
			if (canTrag) {
				compFoot.addListener(SWT.MouseDown, l);
				compFoot.addListener(SWT.MouseMove, l);
			}
			fd_content.bottom = new FormAttachment(compFoot);
			content.setLayoutData(fd_content);
		} else {
			fd_content.bottom = new FormAttachment(100);
			content.setLayoutData(fd_content);
		}

		compList.add(compHead);

		if (canTrag) {
			compHead.addListener(SWT.MouseDown, l);
			compHead.addListener(SWT.MouseMove, l);

			dlgTitle.addListener(SWT.MouseDown, l);
			dlgTitle.addListener(SWT.MouseMove, l);
		}
		return content;
	}

	protected static void doChangeColor(Shell s) {
		// TODO Auto-generated method stub
		ColorDialog d = new ColorDialog(s, SWT.PRIMARY_MODAL);
		RGB rgb = d.open();
		System.out.println(rgb.toString());
		if (null != rgb) {
			Color c = SWTResourceManager.getColor(rgb);
			backColortarget = c;
			PropertiesUtil.setProperty("rgbRed", backColortarget.getRed() + "");
			PropertiesUtil.setProperty("rgbGree", backColortarget.getGreen() + "");
			PropertiesUtil.setProperty("rgbBlue", backColortarget.getBlue() + "");
			if (null != compList && compList.size() > 0) {
				for (Composite cd : compList) {
					cd.setBackground(c);
					Control[] cts = cd.getChildren();
					if (cts.length > 0) {
						for (Control cs : cts) {
							if (cs instanceof CLabel) {
								cs.setBackground(c);
							}
						}
					}
				}
			}
		}

	}
}
