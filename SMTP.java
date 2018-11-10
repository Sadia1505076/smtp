
package smtp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SMTP {

    public static void main(String[] args) throws UnknownHostException, IOException {
        String mailServer = "webmail.buet.ac.bd";
        //InetAddress mailHost = InetAddress.getByName(mailServer);
        InetAddress mailHost = InetAddress.getLocalHost();
        InetAddress localHost = InetAddress.getLocalHost();
        Socket smtpSocket = new Socket(mailHost,25);
        smtpSocket.setSoTimeout(20000);
        BufferedReader in =  new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
        PrintWriter pr = new PrintWriter(smtpSocket.getOutputStream(),true);
        Scanner sc = new Scanner(System.in);
        Scanner line = new Scanner(System.in);

        while( true ){
            String state1 = "";
            try{
                state1 = in.readLine();
            }catch(java.net.SocketTimeoutException e){
                System.out.println("Timeout!");
            }
            System.out.println(state1);
            if( state1.substring(0, 3).equals("220") )
                break;
        }
        
        //FileReader fr = new FileReader("E:\\Study\\LEVEL 3 TERM 2\\CSE 322\\Socket Programming Contents\\input.txt");
        //BufferedReader file = new BufferedReader(fr);
        
        System.out.print("Enter HELO command: ");
        while( true ){
            //String helo = file.readLine();
            //System.out.println(helo);
            String helo = sc.next();
            pr.println(helo + " " + localHost.getHostName());
            //pr.flush();
            String state2 = "";
            try{
                state2 = in.readLine();
            }
            catch(java.net.SocketTimeoutException e){
                System.out.println("Timeout!");
            }
            System.out.println(state2); 
            if( state2.substring(0, 3).equals("250") )
                break;
            else
                System.out.println("Sorry! Wrong command. Please enter HELO command again.");
        }
        
        while(true){
            System.out.print("From: ");
            String from;
            while( true ){
                from = sc.next();
                pr.println("MAIL FROM:<" + from + ">");
                String state3 = "";
                try{
                    state3 = in.readLine();
                }
                catch(java.net.SocketTimeoutException e){
                    System.out.println("Timeout!");
                }
                System.out.println(state3); 
                if( state3.substring(0, 3).equals("250") )
                    break;
                else
                    System.out.println("Sorry! Wrong input. Please enter from again.");
            }
            
            String[] to = new String[20];
            int i = 0;
            System.out.print("To: ");
            while( true ){
                String recepient = sc.next();
                if( recepient.equals("x"))
                    break;
                else{
                    to[i] = recepient;
                    pr.println("RCPT TO:<" + to[i] + ">");
                    String state4 = "";
                    try{
                        state4 = in.readLine();
                    }
                    catch(java.net.SocketTimeoutException e){
                        System.out.println("Timeout!");
                    }
                    System.out.println(state4);
                    i++;
                    if( state4.substring(0, 3).equals("250") )
                        System.out.println("CC (Or press x to enter text): ");
                    else
                        System.out.println("Sorry! Wrong recepient. Please enter recepient again.");
                }
            }
            
            System.out.print("Enter data command: ");
            while( true ){
                String data = sc.next();           
                pr.println(data);
                String state5 = "";
                try{
                    state5 = in.readLine();
                }
                catch(java.net.SocketTimeoutException e){
                    System.out.println("Timeout!");
                }
                System.out.println(state5); 
                if( state5.substring(0, 3).equals("354") )
                    break;
                else
                    System.out.println("Sorry! Wrong command. Please enter data command again.");
                            
            }
            
            System.out.print("Subject: ");
            String subject = line.nextLine();
            System.out.println("");
            String text = line.nextLine();
            while( true ){
                pr.println("Subject: " + subject);
                pr.println("From: " + from + "@" + localHost.getHostName());
                pr.println("To: " + to[0]);
                if( i > 1 ){
                    String cc = "";
                    for( int j = 1; j < i-1; j++ ){
                        cc = cc + to[j] + ", ";
                    }
                    cc = cc + to[i-1];
                    pr.println("Cc: " + cc);
                }          
                pr.println("");                 
                pr.println(text);
                pr.println(".");
                String state6 = "";
                try{
                    state6 = in.readLine();
                }
                catch(java.net.SocketTimeoutException e){
                    System.out.println("Timeout!");
                }
                System.out.println(state6); 
                if( state6.substring(0, 3).equals("250") )
                    break;
            }
            
            System.out.println("Do you want to quit? (y/n): ");
            String quit = sc.next();
            if( quit.equals("y") )
                break;
        }
        
        pr.println("QUIT");
        String state7 = "";
        try{
            state7 = in.readLine();
        }
        catch(java.net.SocketTimeoutException e){
            System.out.println("Timeout!");
        }
        while( true ){
            System.out.println(state7); 
            if( state7.substring(0, 3).equals("221") )
                break;
        }
    }
}
