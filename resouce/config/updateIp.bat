netsh interface ip set address name="无线网络连接"   static 10.10.10.189 255.255.255.0 10.10.10.1  
netsh interface ip add dns "无线网络连接" 202.100.96.68 index=1  
netsh interface ip add dns "无线网络连接" 222.75.152.129 index=2 