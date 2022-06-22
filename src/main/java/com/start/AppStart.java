package com.start;


import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.*;

import static ch.ethz.ssh2.ChannelCondition.EOF;
import static ch.ethz.ssh2.ChannelCondition.EXIT_STATUS;

public class AppStart {

    public static void main(String[] args) {

        linuxExecute();
        /*public Process exec(String[] cmdArray);
        // Linux下
        Runtime.getRuntime().exec(new String[]{/bin/sh","-c"});

                // Windows下
                Runtime.getRuntime().exec(new String[]{"cmd","/c",cmds});

        private static Process p = null;
        p = Runtime.getRuntime().exec("notepad.exe");
        p.waitFor();
        System.out.println("---------------我被执行了");*/
    }

    public static void  linuxExecute(){
        // IP
        String ip = "10.191.241.31";
        // 端口
        int port = 22;
        //用户名
        String username = "root";
        //密码
        String password = "Gtkvn@2022$";
        //创建连接ip,端口(port)
        Connection conn = new Connection(ip,port);

        //连接
        try {
            conn.connect();
            //验证用户名密码是否正确
            boolean UserPwd = conn.authenticateWithPassword(username,password);
            if(UserPwd){
                System.out.println("正确。。。");
                //创建session会话
                Session session = conn.openSession();
                //建立虚拟终端
                session.requestPTY("bash");
                //打开一个shell
                session.startShell();
                //将屏幕上的文字全部打印出来
                InputStream stdOut = new StreamGobbler(session.getStdout());
                BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdOut));
                //准备输入命令
                try(PrintWriter out = new PrintWriter(session.getStdin())){
                    //输入待执行命令
                    out.println("cd /u01Tms/apache-tomcat-edi/logs/");
                    out.println("tail -f catalina.out");
                    out.println("exit");
                }catch (Exception e){
                    e.getMessage();
                }
                //等待&;除非1.连接关闭&#xff0c;2.输入数据传送完毕 进程状态为退出
                session.waitForCondition(ChannelCondition.CLOSED ,EOF);
                //读取数据进入死循环
                while (true){
                    String line = stdoutReader.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }

            }

           // BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
           /* StringBuffer sb = new StringBuffer();
            if (in.available() != 0) {
                while (true) {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    sb.append(line).append(System.getProperty("line.separator"));
                }
            }
            return sb.toString();*/

        } catch (IOException e) {
            e.printStackTrace();
        }




    }

        /*public void linuxExecute(){



            try{


                if(UserPwd){


                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    conn.close();
                }

            }
        }*/
}
