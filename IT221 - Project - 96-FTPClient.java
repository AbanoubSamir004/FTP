import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FTPClient {

    public static void main(String[] args) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println(ip);
            Socket port1 = new Socket(ip, 2300);
            Socket port2 = new Socket(ip, 2301);

            ClientHandler clientSock = new ClientHandler(port1, port2);
            new Thread(clientSock).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class ClientHandler implements Runnable {
        Socket clientSocket;
        Socket clientsocket2;
        public ClientHandler(Socket socket , Socket s2)
        {
            this.clientSocket = socket;
            this.clientsocket2=s2;

        }

        public void run()
        {
            Scanner input = new Scanner(System.in);
            try {
                DataInputStream input_other = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream output_other = new DataOutputStream(clientSocket.getOutputStream());
                int check;
                String loginname, loginpass,msgs;
                msgs=input_other.readUTF();
                System.out.println(msgs);
                loginname = input.nextLine();
                output_other.writeUTF(loginname);
                check = input_other.readInt();
                if (check == 1) {
                    msgs=input_other.readUTF();
                    System.out.println(msgs);
                    loginpass = input.nextLine();
                    output_other.writeUTF(loginpass);
                    check = input_other.readInt();

                    if (check == 1) {
                        msgs=input_other.readUTF();
                        System.out.println(msgs);
                        try {
                            while (true) {
                                DataInputStream input_other2 = new DataInputStream(clientsocket2.getInputStream());
                                DataOutputStream output_other2 = new DataOutputStream(clientsocket2.getOutputStream());
                                String C,s;
                                s=input.nextLine();
                                output_other2.writeUTF(s);
                                if(s.equalsIgnoreCase("show my directories")){
                                    C=input_other2.readUTF();
                                    System.out.println(C);
                                    //Server replies with the available dirs
                                     for (int i = 0; i < 3; i++) {
                                        s = input_other2.readUTF();
                                        System.out.println(s);
                                    }
                                    C=input_other2.readUTF();
                                    System.out.println(C);
                                    String dir = input.nextLine();
                                    output_other2.writeUTF(dir);
                                    
                                    C=input_other2.readUTF();
                                    System.out.println(C);
                                    //Server replies with the available Files
                                    for (int i = 0; i < 3; i++) {
                                        s = input_other2.readUTF();
                                        System.out.println(s);
                                    }
                                    //Server replies with the available content
                                    C=input_other2.readUTF();
                                    System.out.println(C);
                                    s = input.nextLine();
                                    output_other2.writeUTF(s);
                                    C=input_other2.readUTF();
                                    System.out.println(C);
                                    
                                    int s2=input_other2.readInt();
                                    for(int J=0;J<s2;J++){
                                        s=input_other2.readUTF();
                                        System.out.println("Server: "+s);
                                        }
                                    C=input_other2.readUTF();
                                    System.out.println("Server: "+C);
                                    s = input.nextLine();
                                    output_other2.writeUTF(s);
                                      
                                }
                                
                                else if(s.equalsIgnoreCase("close")){
                                     C=input_other2.readUTF();
                                    System.out.println("Server: "+C);
                                    clientSocket.close();
                                    clientsocket2.close();
                                    break;
                                }
                                C=input_other2.readUTF();
                                System.out.println(C);
                            }
                                
                               
                        }catch (IOException e) {   
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Server: Login failed and the connection will terminate");
                        clientSocket.close();
                        clientsocket2.close();
                        
                    }
                } else {
                    System.out.println("Server: Login failed and the connection will terminate");
                    clientSocket.close();
                    clientsocket2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                
            }
        }
    }
}