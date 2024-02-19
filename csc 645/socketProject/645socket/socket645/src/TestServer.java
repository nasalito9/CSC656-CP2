import java.io.*;
import java.net.*;
import java.util.HashMap;

class TestServer {

    private static HashMap<String,String> userAccount = new HashMap<>();
    private static HashMap<String, StringBuilder> userMessages = new HashMap<>();
    public static void main(String argv[]) throws Exception
    {
        //populate hashmap
        userAccount.put("Alice", "1234");
        userAccount.put("Bob", "5678");
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(1234);
        welcomeSocket.setSoTimeout(10000); // Set a timeout of 10 seconds

        try {
            Socket connectionSocket = welcomeSocket.accept();
            // Rest of your code for handling the connection


        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        PrintWriter printWriter = new PrintWriter(connectionSocket.getOutputStream());

        System.out.println("SERVER is running ... ");
        while(true) {
            clientSentence = inFromClient.readLine();
            //Connect To Server
            System.out.println("User passed in : " + clientSentence);
            if(clientSentence.equals("0")){
                String username = inFromClient.readLine();
                String password = inFromClient.readLine();
                System.out.println("entered : " + username + " " + password);
                if(userAccount.containsKey(username) && userAccount.containsValue(password)){
                    System.out.println("Success");
                    outToClient.writeBytes("Access Granted");
                    System.out.println("byteswritten");
                }
                else{
                    System.out.println("Declined");
                    printWriter.println("Access Denied - Username/Password Incorrect");
                    System.out.println("msg sent");
                }
            }
            //Get List of Users
            else if(clientSentence.equals("1")){
                String userList = String.join(", ", userAccount.keySet());
                printWriter.println(userList);
                outToClient.writeBytes(userList);
            }
            //Send Message
            else if(clientSentence.equals("2")){
                String sender = inFromClient.readLine();
                String receiver = inFromClient.readLine();
                String message = inFromClient.readLine();
                StringBuilder existingMessages = userMessages.getOrDefault(receiver, new StringBuilder());
                userMessages.put(receiver, existingMessages.append(sender).append(": ").append(message).append("\n"));
            }
            //Get Messages
            else if(clientSentence.equals("3")){
                String username = inFromClient.readLine();
                System.out.println("username: " + username);
                StringBuilder messageList = userMessages.getOrDefault(username, new StringBuilder());
                System.out.println("messageList" + messageList);
                printWriter.println(messageList);
            }
            //exit
            else if(clientSentence.equals("4")){
                printWriter.println("Exiting");
                break;
            }
        }
        } catch (SocketTimeoutException e) {
            System.err.println("Server socket timed out waiting for a connection.");
        }
    }
}