import java.io.*;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

public class Task3 {

    static Scanner text_scanner;
    static int line_no;
    static String[] split_string;
    static String username;
    static String pass_from_file;
    static int max_pass;
    static char[] working_string;
    static int depth = 0;
    static FileWriter logfile;
    static char salt;
    static String hashedCrack;
    static int no_accounts;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        text_scanner = new Scanner(System.in);
        logfile = new FileWriter("log.txt");
        int op;

        //get the max size of password
        max_pass = get_max_pass();

        getNumAccounts();

        op = getOpt();

        if (op == 1){
            System.out.println("Starting hash cracker");
            crack_hash(max_pass);
        }
        else {
            System.out.println("Starting salt cracker");
            crack_salt(max_pass);
        }
    }

    private static void crack_hash(int max) throws IOException {
        BufferedReader reader;
        FileReader in_file = null;
        int random_line = ThreadLocalRandom.current().nextInt(1, no_accounts + 1);
        System.out.println("random line no: " + random_line);
        String temp, line = null;
        line_no = 1;

        try {
            in_file = new FileReader("hashed.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert in_file != null;
        reader = new BufferedReader(in_file);

        while ((temp = reader.readLine()) != null){
            if (line_no == random_line){
                line = temp;
                break;
            }
            line_no++;
        }

        if (line == null){
            System.out.println("Error: the number of accounts input earlier exceeds number of accounts in file, exiting early...");
            System.exit(0);
        }

        split_string = line.split(" ");
        username = split_string[0];
        pass_from_file = split_string[1];

        reader.close();
        in_file.close();

        //start brute forcing passwords up to size max
        for (int i = 0; i < max_pass; i++){
            working_string = new char[i + 1];
            depth = 1;
            try {
                brute(i + 1, depth, false);
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void crack_salt(int max) throws IOException, NoSuchAlgorithmException {
        BufferedReader reader;
        FileReader in_file = new FileReader("salted.txt");
        int random_line = ThreadLocalRandom.current().nextInt(1, no_accounts + 1);
        String temp, line = null;
        int line_no = 1;
        reader = new BufferedReader(in_file);

        while ((temp = reader.readLine()) != null){
            if (line_no == random_line){
                line = temp;
                break;
            }
            line_no++;
        }

        if (line == null){
            System.out.println("Error: the number of accounts input earlier exceeds number of accounts in file, exiting early...");
            System.exit(0);
        }

        split_string = line.split(" ");
        username = split_string[0];
        salt = split_string[1].charAt(0);
        pass_from_file = split_string[2];


        //start brute forcing passwords up to size max
        for (int i = 0; i < max_pass; i++){
            working_string = new char[i + 1];
            depth = 1;
            try {
                brute(i + 1, depth, true);
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static int get_max_pass(){
        String temp;
        System.out.println("What is the max length of the passwords?\n");
        temp = text_scanner.nextLine();
        int n;
        try {
            n = Integer.parseInt(temp);
        } catch (NumberFormatException nfe) {
            System.out.println("Number given was not integer, try again\n");
            n = get_max_pass();
        }
        return n;
    }

    private static void brute(int len, int level, boolean addSalt) throws NoSuchAlgorithmException, IOException {
        if (!addSalt) {
            if (len == level) {
                for (int i = 0; i < 10; i++) {
                    working_string[level - 1] = intToChar(i);
                    logfile.write(new String(working_string) + "\n");
                    hashedCrack = bytesToString(getHash(new String(working_string)));
                    if (checkPass(hashedCrack, pass_from_file)) {
                        System.out.println("Password has been cracked!");
                        printCrackerInfo(working_string);
                        closeLog();
                        System.exit(0);
                    }
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    working_string[level - 1] = intToChar(i);
                    brute(len, level + 1, false);
                }
            }
        }
        else { // addSalt is true
            if (len == level) {
                for (int i = 0; i < 10; i++) {
                    working_string[level - 1] = intToChar(i);
                    logfile.write(new String(working_string) + "\n");
                    hashedCrack = bytesToString(getHash(new String(working_string) + salt));
                    if (checkPass(hashedCrack, pass_from_file)) {
                        System.out.println("Password has been cracked!");
                        printCrackerInfoSalt(working_string);
                        closeLog();
                        System.exit(0);
                    }
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    working_string[level - 1] = intToChar(i);
                    brute(len, level + 1, true);
                }
            }
        }
    }

    private static boolean checkPass(String bruteforcedHash, String hashFromFile){
        return bruteforcedHash.equals(hashFromFile);
    }

    private static char intToChar(int n){
        if (n == 0) return '0';
        else if (n == 1) return '1';
        else if (n == 2) return '2';
        else if (n == 3) return '3';
        else if (n == 4) return '4';
        else if (n == 5) return '5';
        else if (n == 6) return '6';
        else if (n == 7) return '7';
        else if (n == 8) return '8';
        else if (n == 9) return '9';
        System.out.println("Error: issue in intToChar, exiting...");
        System.exit(0);
        return 'z';
    }

    private static byte[] getHash(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToString(byte[] hash)
    {
        // Convert byte array into string representation
        BigInteger number = new BigInteger(1, hash);
        // Convert message digest into string value
        StringBuilder string = new StringBuilder(number.toString(16));
        // Pad with leading zeros
        while (string.length() < 32)
        {
            string.insert(0, '0');
        }
        return string.toString();
    }


    public static void closeLog() throws IOException {
        if (logfile != null) logfile.close();
    }

    public static void printCrackerInfo(char[] str){
        System.out.println("line number: " + line_no);
        System.out.println("String produced by cracker: " + new String(str));
        System.out.println("String produced by cracker after hash: " + hashedCrack);
        System.out.println("Hashed password from the file: " + pass_from_file);
    }

    public static void printCrackerInfoSalt(char[] str){
        System.out.println("line number: " + line_no);
        System.out.println("String produced by cracker: " + new String(str));
        System.out.println("Salt from file: " + salt);
        System.out.println("String produced by cracker after hash + salt: " + hashedCrack);
        System.out.println("Hashed + salted password from the file: " + pass_from_file);
    }

    public static void getNumAccounts(){
        String temp;
        System.out.println("How many accounts exist in the file?\n");
        temp = text_scanner.nextLine();
        while (true){
            if (temp.matches("[0-9]+")){
                no_accounts = Integer.parseInt(temp);
                if (no_accounts == 0) System.out.println("0 is not a valid response, try again");
                else break;
            }
            else System.out.println("Please enter valid input, only numbers are allowed");
        }
    }

    public static int getOpt(){
        //get whether or not the user wants to crack the hashed file or the salted file
        System.out.println("Would you like to crack a password from the hashed file or the salted file \n1) Hashed \n2) Salted");
        String temp;
        while (true){
            temp = text_scanner.nextLine();
            if (Objects.equals(temp, "1")){
                return 1;
            }
            else if (Objects.equals(temp, "2")){
                return 2;
            }
            else {
                System.out.println("'" + temp + "' is not a valid option try again\n");
            }
        }
    }
}
