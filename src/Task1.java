import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Task1 {

    static Scanner text_scanner;
    static FileWriter out_file = null;
    static FileReader in_file = null;

    static int min_pass = 3, max_pass = 8;

    public static void main(String[] args){
        // get whether the user wants to create or authenticate

        text_scanner = new Scanner(System.in);

        int op = -1;
        boolean valid_input = false;
        String temp;

        System.out.println("Would you like to authenticate or create account?\n1) Authenticate\n2) Create Account");

        //get the valid option from the user
        while (!valid_input){
            temp = text_scanner.nextLine();
            if (Objects.equals(temp, "1")){
                op = 1;
                valid_input = true;
            }
            else if (Objects.equals(temp, "2")){
                op = 2;
                valid_input = true;
            }
            else {
                System.out.println("'" + temp + "' is not a valid option try again\n");
            }
        }

        if (op == 1){
            try {
                authenticate();
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                System.out.println("Error occurred when authenticating an account...\n");
            }
        }
        else{
            try {
                create();
            }
            catch (IOException | NoSuchAlgorithmException e){
                e.printStackTrace();
                System.out.println("Error occurred when creating an account...\n");
            }
        }
    }

    private static void authenticate() throws IOException, NoSuchAlgorithmException {

        String current_line;
        int line_no;
        char salt;
        BufferedReader reader;

        //get username
        String username = getUsername();

        //get password
        String password = getPassword(min_pass, max_pass);

        //hash password
        byte[] hashed_password_bytes = getHash(password);
        String hashed_password = bytesToString(hashed_password_bytes);

        //open salted file and look for matching usernames
        openInFile("salted.txt");
        System.out.println("Checking username and password against the salted file...");
        reader = new BufferedReader(in_file);
        line_no = 0;

        while ((current_line = reader.readLine()) != null) {
            line_no++;
            String[] split_string = current_line.split(" ");
            if (Objects.equals(split_string[0], username)){
                System.out.println(username + " found at line " + line_no);
                salt = split_string[1].charAt(0);
                String salted_pass = password + salt;
                byte[] salt_hash_bytes = getHash(salted_pass);
                String salt_and_hash = bytesToString(salt_hash_bytes);
                if (salt_and_hash.equals(split_string[2])){
                    System.out.println("passwords matched!");
                }
                else System.out.println("Passwords did not match!");
            }
        }
        reader.close();
        closeInFile("salted.txt");

        //open plain text, compare with plain text, print result
        openInFile("plaintext.txt");
        System.out.println("Checking username and password against the plaintext file...");
        reader = new BufferedReader(in_file);
        line_no = 0;

        while ((current_line = reader.readLine()) != null) {
            line_no++;
            String[] split_string = current_line.split(" ");
            if (Objects.equals(split_string[0], username)){
                System.out.println(username + " found at line " + line_no);
                if (password.equals(split_string[1])){
                    System.out.println("passwords matched!");
                }
                else System.out.println("Passwords did not match!");
            }
        }
        reader.close();
        closeInFile("plaintext.txt");

        //compare with hash, print result
        openInFile("hashed.txt");
        System.out.println("Checking username and password against the hashed file...");
        reader = new BufferedReader(in_file);
        line_no = 0;
        while ((current_line = reader.readLine()) != null) {
            line_no++;
            String[] split_string = current_line.split(" ");
            if (Objects.equals(split_string[0], username)){
                System.out.println(username + " found at line " + line_no);
                if (hashed_password.equals(split_string[1])){
                    System.out.println("passwords matched!");
                }
                else System.out.println("Passwords did not match!");
            }
        }
        reader.close();
        closeInFile("hashed.txt");
    }


    private static void create() throws IOException, NoSuchAlgorithmException {
        //get username
        String username = getUsername();

        //get password
        String password = getPassword(min_pass, max_pass);

        //hash password
        byte[] hashed_password_bytes = getHash(password);
        String hashed_password = bytesToString(hashed_password_bytes);

        //get salt
        char salt = getSaltByte();

        //salt password
        String salted_pass = password + salt;

        //hash salted password
        byte[] salt_hash_bytes = getHash(salted_pass);
        String salt_and_hash = bytesToString(salt_hash_bytes);

        //output to the 3 files
        //A plaintext username password pair, stored in text in a file
        openOutFile("plaintext.txt");
        out_file.write(username + " " + password + "\n");
        closeOutFile("plaintext.txt");

        //A username and a hashed password, stored in some format in the file
        openOutFile("hashed.txt");
        out_file.write(username + " " + hashed_password + "\n");
        closeOutFile("hashed.txt");

        //A username, a salt and the result of the hashed (password + salt), stored in some format in the file
        openOutFile("salted.txt");
        out_file.write(username + " " + salt + " " + salt_and_hash + "\n");
        closeOutFile("salted.txt");

    }


    public static byte[] getHash(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static char getSaltByte(){
        String salt_pool = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random rand = new Random();
        int index = (int)(rand.nextFloat() * salt_pool.length());
        return salt_pool.charAt(index);
    }

    public static String getUsername(){
        boolean valid_input = false;
        String username = "";
        while (!valid_input){
            System.out.println("Enter a username of only alphabetic characters, of length 10 or less\n");
            username = text_scanner.nextLine();
            if (username.length() > 10) System.out.println("Username must be 10 or less characters, [a-z A-Z]\n");
            else if (!username.matches("[a-zA-Z]+")) System.out.println("Username must be 10 or less characters, [a-z A-Z]\n");
            else valid_input = true;
        }
        return  username;
    }

    public static String getPassword(int min, int max){
        String pass;
        System.out.println("Enter a password between " + min + " and " + max + " characters long\n");
        pass = text_scanner.nextLine();
        if (pass.length() > max || pass.length() < min){
            System.out.println("Password too short or too long try again\n");
            pass = getPassword(min, max);
        }
        else if (!pass.matches("[0-9]+")){
            System.out.println("Password must only contain numbers\n");
            pass = getPassword(min, max);
        }
        return pass;
    }

    public static void openInFile(String fileName){
        try {
            in_file = null;
            in_file = new FileReader(fileName);
        }
        catch (FileNotFoundException fe)
        {
            System.out.println(fileName + " not found, exiting program...\n");
            System.exit(0);
        }
    }

    public static void openOutFile(String fileName){
        try
        {
            out_file = new FileWriter(fileName, true);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Issue creating creating file: " + fileName + ", exiting program...\n");
            System.exit(0);
        }
    }

    public static void closeInFile(String name){
        try{
            in_file.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error when closing input file: " + name + ", exiting program...\n");
            System.exit(0);
        }
    }

    public static void closeOutFile(String name){
        try{
            out_file.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error when closing output file: " + name + ", exiting program...\n");
            System.exit(0);
        }
    }

    public static String bytesToString(byte[] hash)
    {
        // Convert byte array into string representation
        BigInteger number = new BigInteger(1, hash);
        // Convert message digest into hex value
        StringBuilder string = new StringBuilder(number.toString(16));
        // Pad with leading zeros
        while (string.length() < 32)
        {
            string.insert(0, '0');
        }
        return string.toString();
    }
}
