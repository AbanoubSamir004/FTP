import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class FTPServer {
    public static void main(String[] args) {
        try {
            ServerSocket server= new ServerSocket(2300);
            ServerSocket server2= new ServerSocket(2301);
            System.out.println("Server is booted up and is waiting for clients to connect.");
            Socket client=null;
            Socket client2=null;
           
            while(true)
            {
                client = server.accept();
                
                client2=server2.accept();
                ClientHandler clientSock = new ClientHandler(client ,client2);
                new Thread(clientSock).start();

            }

        }
        catch (IOException e)
        {
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
            try{
                int user_found = 0;
                int count=0,counter=1;
                DataInputStream input = new DataInputStream (clientSocket.getInputStream());
                DataOutputStream output = new DataOutputStream (clientSocket.getOutputStream());
                String username,password,msg;
                msg="Server: Please enter user's name";
                output.writeUTF(msg);
                //output.writeUTF("You are connected");
                //output.writeUTF("Enter the data ");
                Users accounts[]; // an array of Users to store  the name and password of the users
                accounts = new Users[40];
                String Name, PW;
                try {
                    int i = 0;
                    File userFile = new File("Users\\users.txt"); // reading the usernames and password from the file.txt
                    try (Scanner user_read = new Scanner(userFile)) // as cin , try btt3amel m3 el file
                    {
                        while (user_read.hasNext()) //tol m feh line gwa el file.txt
                        {
                            Scanner accounts_reader = new Scanner(user_read.nextLine()); // tmshe 3la line line f el file
                            accounts_reader.useDelimiter("#"); //as a break point to lines ,3shan tfsl el name 3n el password b kol hashtag
                            Name = accounts_reader.next();
                            PW = accounts_reader.next();
                            accounts[i] = new Users(Name, PW);
                            i++;
                            count++;
                        }

                    }

                }
                catch (FileNotFoundException e)
                {
                    System.out.println("File not found");
                }
                try{
                    Users OurUser = new Users(null,null);
                    username = input.readUTF();
                    for (int i = 0; i < count; i++)
                    {
                        if (accounts[i].name.trim().equalsIgnoreCase(username))
                        {
                            OurUser.name = accounts[i].name;
                            OurUser.password = accounts[i].password;
                            user_found = 1;
                        }
                    }
                    output.writeInt(user_found);
                    msg="Server: Username OK, password required";
                    output.writeUTF(msg);
                    if(user_found == 1)
                    {
                        password = input.readUTF();
                        if (OurUser.password.trim().equalsIgnoreCase(password))
                        {
                            user_found = 1;
                            output.writeInt(user_found);
                            msg="Server: Login successfully";
                            output.writeUTF(msg);
                            for(int i=0;i<count;i++){
                                if (accounts[i].name.trim().equalsIgnoreCase(username))break;
                                counter++;
                            }
                            System.out.print("a new client is connected:");
                            System.out.println("Client_"+counter);
                        }
                        else
                        {
                            user_found = 0;
                            output.writeInt(user_found);
                            input.close();
                            clientSocket.close();
                            clientsocket2.close();
                        }
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                    clientSocket.close();
                    clientsocket2.close();
                }

                if(user_found == 1){
                    DataInputStream input2 = new DataInputStream (clientsocket2.getInputStream());
                    DataOutputStream output2 = new DataOutputStream (clientsocket2.getOutputStream());
                    String client_name,directory,location,file1,KK;
                    int II=0;
                    while (true) {
                        String show;
                        show=input2.readUTF();
                        FileInputStream IN=null;
                        FileOutputStream OS=null;

                        if (show.equalsIgnoreCase("show my directories")) {
                            if(II>0){
                                if(show.equalsIgnoreCase("Show my directories"))
                                    System.out.println("Client_"+counter+": connect agian");
                            }
                            
                            KK="Server: your Directories: ";
                            output2.writeUTF(KK);
                            System.out.println("Client_"+counter+": "+show);

                            client_name = "Client_" + counter;
                            location = "data\\" + client_name;
                            File directoryPath = new File(location);
                            //List of all files and directories
                            File file1List[] = directoryPath.listFiles();
                            for (File file : file1List) {
                                output2.writeUTF(file.getName());
                            }
                            KK="Server: Please Choose Your Directory";
                            output2.writeUTF(KK);
                            directory = input2.readUTF();
                            System.out.println("Client_"+counter+" Show my: "+directory);
                            location = "data\\" + client_name + "\\" + directory;
                            File filePath = new File(location);
                            //List of all files and directories
                            KK="Server: Your Files: ";
                            output2.writeUTF(KK);
                            File file2List[] = filePath.listFiles();
                            for (File file : file2List) {
                                output2.writeUTF(file.getName());
                            }

                            KK="Server: Please Choose Your File";
                            output2.writeUTF(KK);
                            file1 = input2.readUTF();
                            KK="Server: Your Contents: ";
                            output2.writeUTF(KK);
                            System.out.println("Client_"+counter+": Show my "+file1);
                            location = "data\\" + client_name + "\\" + directory + "\\" + file1;
                            File file3Path = new File(location);
                            //List of all files and directories
                            String Content = null;
                            System.out.println("Client_"+counter+": Please Send my Contents");

                            File file3List[] = file3Path.listFiles();
                            int i=0;
                            for (File file : file3List) {
                                i++;
                                //output.writeUTF(file.getName());
                                Content = file.getName();
                                location = "data\\" + client_name + "\\" + directory + "\\" + file1 + "\\" + Content;
                                IN=new FileInputStream(location);
                                OS=new FileOutputStream("C:\\Downloads_"+counter+"\\"+Content);
                                int j;
                                byte b[]=new byte[20*4096];
                                while((j=IN.read(b))>0)
                                    OS.write(b,0,j);
                            }
                            OS.close();
                            IN.close();
                            output2.writeInt(i);
                            for (File file : file3List) {
                            Content = file.getName();
                            String Path="C:\\Downloads_"+counter+"\\"+Content;
                            String s=Content+" is Downloaded at Client's PC and it's path: "+Path;
                            output2.writeUTF(s);
                            }
                            String C2="DO YOU WANT TO OPEN YOUR CONTENTS ON YOUR SCREEN NOW? (YES/NO)";
                            output2.writeUTF(C2);
                            C2=input2.readUTF();
                            if(C2.equalsIgnoreCase("Yes")){
                                System.out.println("Client_"+counter+": Open My Contents On My Screen Now");
                                for (File file : file3List) {
                                    Content=file.getName();
                                    String Path="C:\\Downloads_"+counter+"\\"+Content;
                                    Desktop DATA=Desktop.getDesktop();
                                    // File data=new File(location);
                                    DATA.open(new File(Path));
                                }
                            }
                        }
                        else if(show.equalsIgnoreCase("Close")){
                            System.out.println("Client_"+counter+" is closed ");
                            String closed="Connection is terminated";
                            output2.writeUTF(closed);

                            break;
                        }                  
                        String ch="Server: DO YOU WANT TO(SHOW YOUR DIRECTORIES OR CLOSE THE CONNECTION)?";
                        output2.writeUTF(ch);
                        II++;
                    }
                   
                }
                 
            }
            catch (IOException e)
            {
                
                e.printStackTrace();
            }
        }
    }
}