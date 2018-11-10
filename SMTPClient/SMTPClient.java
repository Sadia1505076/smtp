package smtpclient;

//import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Scanner;

public class SMTPClient {

    public static void main(String[] args) throws UnknownHostException, IOException {
//        String mailServer = "webmail.buet.ac.bd";
//        InetAddress mailHost = InetAddress.getByName(mailServer);
//        //InetAddress mailHost = InetAddress.getLocalHost();
//        InetAddress localHost = InetAddress.getLocalHost();
//        Socket smtpSocket = new Socket(mailHost, 25);

//        BufferedWriter out = null;
//
//        try {
//            FileWriter fstream = new FileWriter("output.txt", true); //true tells to append data.
//            out = new BufferedWriter(fstream);
//            //out.write("\nsue");
//        } catch (IOException e) {
//            System.err.println("Error: " + e.getMessage());
//        }

        
        String mailServer = "webmail.buet.ac.bd";
        InetAddress mailHost = InetAddress.getByName(mailServer);
        InetAddress localHost = InetAddress.getLocalHost();
        Socket smtpSocket = new Socket(mailHost, 25);
        smtpSocket.setSoTimeout(20000);
        BufferedReader in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
        PrintWriter pr = new PrintWriter(smtpSocket.getOutputStream(), true);
        File file = new File("input.txt");
//        Scanner sc = new Scanner(System.in);
        //  Scanner sc = new Scanner(file);

        Scanner line = new Scanner(file);
        String[] to = new String[20];
        int i = 0;
        String mainstate = "";
        String from = "";
        String nextcommand = "";
        //String attach="";

        while (true) {
            String state1 = "";
            try {
                state1 = in.readLine();
            } catch (java.net.SocketTimeoutException e) {
                System.out.println("Timeout!");
            }
            System.out.println(state1);
            if (state1.substring(0, 3).equals("220")) {
                break;
            }
        }

        //FileReader fr = new FileReader("E:\\Study\\LEVEL 3 TERM 2\\CSE 322\\Socket Programming Contents\\input.txt");
        //BufferedReader file = new BufferedReader(fr);
        System.out.print("Enter HELO command: ");
        while (true) {
            //String helo = file.readLine();
            //System.out.println(helo);
            String helo = line.nextLine();
            pr.println(helo + " " + localHost.getHostName());
            //pr.flush();
            String state2 = "";
            try {
                state2 = in.readLine();
            } catch (java.net.SocketTimeoutException e) {
                System.out.println("Timeout!");
            }
            System.out.println(state2);
            if (state2.substring(0, 3).equals("250")) {
                break;
            } else {
                System.out.println("Sorry! Wrong command. Please enter HELO command again.");
            }
        }

        mainstate = "helodone";
        while (true) {
            if (mainstate.equals("helodone")) {
                System.out.print("enter MAIL FROM:<sender mail> :");

                while (true) {
                    from = line.nextLine();
                    System.out.println(from);
                    pr.println(from);
                    String state3 = "";
                    try {
                        state3 = in.readLine();
                    } catch (java.net.SocketTimeoutException e) {
                        System.out.println("Timeout!");
                    }
                    System.out.println(state3);
                    if (state3.substring(0, 3).equals("250")) {
                        mainstate = "from mail done";
                        if (from.equals("RSET")) {
                            mainstate = "helodone";
                        }
                        break;
                    } else if (from.equals("RSET")) {
                        mainstate = "helodone";
                        break;
                    } else {
                        System.out.println("Sorry! Wrong input. Please enter from again.");
                    }
                }
            }

            if (mainstate.equals("from mail done")) {
                System.out.print("enter RCPT TO:<receiver mail> ");
                String recepient;
                while (true) {
                    recepient = line.nextLine();
                    pr.println(recepient);
                    String state41st = "";
                    try {
                        state41st = in.readLine();
                    } catch (java.net.SocketTimeoutException e) {
                        System.out.println("Timeout!");
                    }
                    System.out.println(state41st);
                    if (state41st.substring(0, 3).equals("250")) {
                        //System.out.println("next command");
                        //nextcommand = sc.next();
                        if (!(recepient.equals("RSET"))) {
                            to[i] = recepient.substring(9, recepient.length() - 1);
                            i++;
                            break;
                        } else if (recepient.equals("RSET")) {
                            mainstate = "helodone";
                            break;
                        }

                    } else if (recepient.equals("RSET")) {
                        mainstate = "helodone";
                        break;
                    } else {
                        System.out.println("Sorry! Wrong recepient. Please enter recepient again.");
                    }
                }

                while (mainstate.equals("from mail done") && ((nextcommand = line.nextLine()).contains("RCPT TO:") || !(nextcommand.equals("DATA")))) {
                    //if()
                    pr.println(nextcommand);
                    String state4 = "";
                    try {
                        state4 = in.readLine();
                    } catch (java.net.SocketTimeoutException e) {
                        System.out.println("Timeout!");
                    }
                    System.out.println(state4);
                    if (state4.substring(0, 3).equals("250")) {
                        if (nextcommand.equals("RSET")) {
                            mainstate = "helodone";
                            break;
                        } else {
                            to[i] = nextcommand.substring(9, nextcommand.length() - 1);
                            i++;
                        }

                    } else if (nextcommand.equals("RSET")) {
                        mainstate = "helodone";
                        break;
                    } else {
                        System.out.println("Sorry! Wrong recepient. Please enter recepient again.");

                    }
                    //}
                }

            }
            if (nextcommand.equals("DATA")) {
                mainstate = "rcpt to done";
            }

            if (mainstate.equals("rcpt to done")) {
                while (true) {
                    String data = nextcommand;
                    pr.println(data);
                    String state5 = "";
                    try {
                        state5 = in.readLine();
                    } catch (java.net.SocketTimeoutException e) {
                        System.out.println("Timeout!");
                    }
                    System.out.println(state5);
                    if (state5.substring(0, 3).equals("354")) {
                        if (data.equals("RSET")) {
                            mainstate = "helodone";
                        }
                        break;
                    } else if (data.equals("RSET")) {
                        mainstate = "helodone";
                        break;
                    } else {
                        System.out.println("Sorry! Wrong command. Please enter data command again.");
                        nextcommand = line.nextLine();
                    }

                }

            }
            if (mainstate.equals("rcpt to done")) {
                System.out.print("Subject: ");
                String subject = line.nextLine();

                String contenttype = line.nextLine();
                String dashStart = line.nextLine();

                //int attachFlag = 0;
                System.out.println("");
                String text = "";

                // text += "\n";
                String temp;
                while (!((temp = line.nextLine()).equals("."))) {
                    if (temp.startsWith("attachment")) {
                        // attach=temp;
                        //attachFlag=1;
                        String attachments[] = temp.split(" ");
                        //for(int k=1 ; k< attachments.length ; k++){
                        File f = new File(attachments[1]);
                        if (f.exists()) {
                            text += attachmentsending(f,attachments[1]);
                            //System.out.println(attachmentsending(f));
                        } else {
                            System.out.println("Doesn't exist");
                        }
                        //}
                    } else {
                        text += temp;
                        text += "\n";
                    }

                }
                System.out.println(text);

                while (true) {
                    pr.println("Subject: " + subject);
                   
                    pr.println("From: " + from.substring(11, from.length() - 1));
                    
                    pr.println("To: " + to[0]);
                   
                    if (i > 1) {
                        String cc = "";
                        for (int j = 1; j < i - 1; j++) {
                            cc = cc + to[j] + ", ";
                        }
                        cc = cc + to[i - 1];
                        pr.println("Cc: " + cc);
                       
                    }
                    pr.println(contenttype);
                   
                    pr.println(dashStart);
                  
                    pr.println("");
                   
                    pr.println(text);
                  
                    pr.println(".");
                    
                    String state6 = "";
                    try {
                        state6 = in.readLine();
                    } catch (java.net.SocketTimeoutException e) {
                        System.out.println("Timeout!");
                    }
                    System.out.println(state6);
                    if (state6.substring(0, 3).equals("250")) {
                        break;
                    }
                }
            }
            if (!(mainstate.equals("helodone"))) {
                System.out.println("Do you want to quit? (y/n): ");
                String quit = line.nextLine();
                if (quit.equals("y")) {
                    break;
                } else {
                    mainstate = "helodone";
                }
            }

        }

        pr.println("QUIT");
        String state7 = "";
        try {
            state7 = in.readLine();
        } catch (java.net.SocketTimeoutException e) {
            System.out.println("Timeout!");
        }
        while (true) {
            System.out.println(state7);
            if (state7.substring(0, 3).equals("221")) {
                break;
            }
        }
    }
    // private void frommailtodata

    private static String attachmentsending(File f,String fname) throws IOException {
        
        String tmp = "\n--sadiatasnim\n"
                + "Content--Type: application/octet-stream; name=\""+fname+"\"\n"
                + "Content-Disposition: attachment; filename=\""+fname+"\"\n"
                + "Content-Transfer-Encoding: base64\n\n\n";

        byte bytearr[] = Files.readAllBytes(f.toPath());
        tmp += Base64.encode(bytearr);
        tmp += "\n...";
        return tmp;
    }
}
