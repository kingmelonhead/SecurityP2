import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Task3 {

    static Scanner text_scanner;
    static FileWriter logfile;
    static String[] split_string;
    static String username, pass_from_file, hashedCrack;
    static char[] working_string;
    static char salt;
    static int line_no, max_pass, depth = 0;
    static long hash_start_time, hash_end_time, salt_duration, hash_duration, salt_start_time, salt_end_time;
    static boolean beenCracked = false;
    static ArrayList<String[]> saltAccounts;
    static ArrayList<String[]> hashAccounts;

    public static void main(String[] args) throws IOException {

        saltAccounts = new ArrayList<>();
        hashAccounts = new ArrayList<>();

        text_scanner = new Scanner(System.in);

        logfile = new FileWriter("log.txt");

        max_pass = get_max_pass();
        int op = getOpt();

        if (op == 1){
            System.out.println("Starting hash cracker");
            readHash();
            crack_hash();
        }
        else if (op == 2){
            System.out.println("Starting salt cracker");
            readSalt();
            crack_salt();
        }

        logfile.close();
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


    private static void crack_hash() throws IOException {
        //this function tries to crack a random line from the hashed file
        //start brute forcing passwords up to size max
        hash_start_time = System.nanoTime();
        for (int i = 0; i < max_pass; i++){
            working_string = new char[i + 1];
            depth = 1;
            try {
                bruteHash(i + 1, depth);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (beenCracked) break;
        }
        beenCracked = false;
    }

    private static void crack_salt() throws IOException {
        //this function attempts to crack a random line from the salt and hashed file
        salt_start_time = System.nanoTime();
        for (int i = 0; i < max_pass; i++){
            working_string = new char[i + 1];
            depth = 1;
            try {
                bruteSalt(i + 1, depth);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (beenCracked) break;
        }
        beenCracked = false;
    }

    private static void bruteHash(int len, int level) throws NoSuchAlgorithmException {
        //recursively goes through creating every possible password string and checks it against the file
        if (len == level) {
            for (int i = 0; i < 10; i++) {
                working_string[level - 1] = intToChar(i);
                if (searchHash()) {
                    System.out.println("A password has been cracked!");
                    beenCracked = true;
                    printCrackerInfo(working_string);
                    return;
                }
                if (beenCracked) return;
            }
        } else {
            for (int i = 0; i < 10; i++) {
                working_string[level - 1] = intToChar(i);
                bruteHash(len, level + 1);
                if (beenCracked) return;
            }
        }
    }

    private static void bruteSalt(int len, int level) throws NoSuchAlgorithmException {
        //recursively goes through creating every possible password string and checks it against the file
        if (len == level) {
            for (int i = 0; i < 10; i++) {
                working_string[level - 1] = intToChar(i);

                if (searchSalt()) {
                    System.out.println("A password has been cracked!");
                    beenCracked = true;
                    printCrackerInfoSalt(working_string);
                    return;
                }
                if (beenCracked) return;
            }
        } else {
            for (int i = 0; i < 10; i++) {
                working_string[level - 1] = intToChar(i);
                bruteSalt(len, level + 1);
                if (beenCracked) return;
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

    public static void printCrackerInfo(char[] str){
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Line number in file: " + line_no);
        System.out.println("Password length: " + new String(str).length());
        System.out.println("String produced by cracker: " + new String(str));
        System.out.println("String produced by cracker after hash: " + hashedCrack);
        System.out.println("Hashed password from the file: " + pass_from_file);
        System.out.println("Time to crack: " + hash_duration + " ns");
        System.out.println("Time to crack: " + (double)(hash_duration/1000000) + " ms");
        System.out.println("-------------------------------------------------------------------------------");
    }

    public static void printCrackerInfoSalt(char[] str){
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Line number in file: " + line_no);
        System.out.println("Password length: " + new String(str).length());
        System.out.println("String produced by cracker: " + new String(str));
        System.out.println("Salt from file: " + salt);
        System.out.println("String produced by cracker after hash + salt: " + hashedCrack);
        System.out.println("Hashed + salted password from the file: " + pass_from_file);
        System.out.println("Time to crack: " + salt_duration + " ns");
        System.out.println("Time to crack: " + (double)(salt_duration/1000000) + " ms");
        System.out.println("-------------------------------------------------------------------------------");
    }

    public static int getOpt(){
        //get whether or not the user wants to crack the hashed file or the salted file
        System.out.println("Would you like to crack a password from the hashed file or the salted file \n1) Hashed \n2) Salted\n");
        String temp;
        while (true){
            temp = text_scanner.nextLine();
            if (Objects.equals(temp, "1")) return 1;
            else if (Objects.equals(temp, "2")) return 2;
            else if (Objects.equals(temp, "3")) return 3;
            else System.out.println("'" + temp + "' is not a valid option try again\n");
        }
    }

    public static void readHash() throws IOException {
        //reads hashed file to vector
        BufferedReader reader;
        FileReader in_file;
        String temp;
        try {
            in_file = new FileReader("hashed.txt");
        } catch (FileNotFoundException e) {
            System.out.println("hashed.txt hasn't been created yet, run task1 or task2 first");
            return;
        }
        reader = new BufferedReader(in_file);

        while ((temp = reader.readLine()) != null){
            split_string = temp.split(" ");
            hashAccounts.add(split_string);
        }
    }

    public static void readSalt() throws IOException {
        //reads salt file to vector
        BufferedReader reader;
        FileReader in_file;
        String temp;
        try {
            in_file = new FileReader("salted.txt");
        } catch (FileNotFoundException e) {
            System.out.println("salted.txt hasn't been created yet, run task1 or task2 first");
            return;
        }
        reader = new BufferedReader(in_file);

        while ((temp = reader.readLine()) != null){
            split_string = temp.split(" ");
            saltAccounts.add(split_string);
        }
    }

    public static boolean searchHash() throws NoSuchAlgorithmException {
        line_no = 0;
        hashedCrack = bytesToString(getHash(new String(working_string)));
        for (String[] temp : hashAccounts) {
            line_no++;
            if (checkPass(temp[1], hashedCrack)){
                hash_end_time = System.nanoTime();
                hash_duration = hash_end_time - hash_start_time;
                username = temp[0];
                pass_from_file = temp[1];
                return true;
            }
        }
        return false;
    }

    public static boolean searchSalt() throws NoSuchAlgorithmException {
        line_no = 0;
        for (String[] temp : saltAccounts) {
            line_no++;
            hashedCrack = bytesToString(getHash(new String(working_string) + temp[1]));
            if (checkPass(temp[2], hashedCrack)){
                salt_end_time  = System.nanoTime();
                salt_duration = salt_end_time - salt_start_time;
                username = temp[0];
                salt = temp[1].charAt(0);
                pass_from_file = temp[2];
                return true;
            }
        }
        return false;
    }
}
