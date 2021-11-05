import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Task2 {

    static Scanner text_scanner;
    static FileWriter plain_out, hash_out, salt_out;

    //program to make random passwords

    static int max_users;

    static int max_pass_len = -1, min_pass_len = -1;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        //open files
        plain_out = new FileWriter("plaintext.txt");
        hash_out = new FileWriter("hashed.txt");
        salt_out = new FileWriter("salted.txt");

        text_scanner = new Scanner(System.in);

        String username, password, hashed_pass, salt_hash_pass;
        byte[] salt_pass_bytes, hashed_password_bytes;
        char salt;

        getNoUsers();

        //get min pass len
        min_pass_len = get_min_pass();
        //get max pass len
        max_pass_len = get_max_pass();


        //generate 100 usernames and passwords
        for (int i = 0; i < max_users; i++){
            //generate username of max size (default 10, can be changed in the code)
            username = genUsername();

            //generate password between max and min
            password = genPassword();

            //store in plain text
            plain_out.write(username + " " + password + "\n");

            //hash and store in hash file
            //hash password
            hashed_password_bytes = getHash(password);
            hashed_pass = bytesToString(hashed_password_bytes);
            hash_out.write(username + " " + hashed_pass + "\n");

            //get salt
            salt = getSaltByte();

            //salt password
            String salted_pass = password + salt;

            //hash salted password
            salt_pass_bytes = getHash(salted_pass);
            salt_hash_pass = bytesToString(salt_pass_bytes);

            //print to salted file
            salt_out.write(username + " " + salt + " " + salt_hash_pass + "\n");
        }

        closeFiles();
    }


    private static int get_max_pass(){
        String temp;
        System.out.println("What is the max length of the passwords?\n");
        temp = text_scanner.nextLine();
        int max_pass;
        try {
            max_pass = Integer.parseInt(temp);
        } catch (NumberFormatException nfe) {
            System.out.println("Number given was not integer, try again\n");
            max_pass = get_max_pass();
        }
        if (max_pass < min_pass_len) {
            System.out.println("Max password length must be more than minimum, try again\n");
            max_pass = get_max_pass();
        }
        return max_pass;
    }


    private static int get_min_pass(){
        String temp;
        System.out.println("What is the min length of the passwords?\n");
        temp = text_scanner.nextLine();
        int max_pass;
        try {
            max_pass = Integer.parseInt(temp);
        } catch (NumberFormatException nfe) {
            System.out.println("Number given was not integer, try again\n");
            max_pass = get_max_pass();
        }
        if (max_pass == 0) {
            System.out.println("0 is not a valid response, try again");
            max_pass = get_max_pass();
        }
        return max_pass;
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

    public static void closeFiles() throws IOException {
        plain_out.close();
        hash_out.close();
        salt_out.close();
    }


    public static String genUsername(){
        StringBuilder usr = new StringBuilder("usr");
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        int usrLen = ThreadLocalRandom.current().nextInt(1, 7 + 1);
        for (int i = 0; i < usrLen; i++){
            int alpha_index = ThreadLocalRandom.current().nextInt(0, 25 + 1);
            usr.append(alpha.charAt(alpha_index));
        }
        return usr.toString();
    }

    public static String genPassword(){
        StringBuilder pass = new StringBuilder();
        String nums = "0123456789";
        int passLen = ThreadLocalRandom.current().nextInt(min_pass_len, max_pass_len + 1);
        for (int i = 0; i < passLen; i++){
            int alpha_index = ThreadLocalRandom.current().nextInt(0, 9 + 1);
            pass.append(nums.charAt(alpha_index));
        }
        return pass.toString();
    }

    public static void getNoUsers(){
        String temp;
        System.out.println("How many accounts would you like to generate?\n");
        temp = text_scanner.nextLine();
        while (true){
            if (temp.matches("[0-9]+")){
                max_users = Integer.parseInt(temp);
                if (max_users == 0) System.out.println("0 is not a valid response, try again");
                else break;
            }
            else System.out.println("Please enter valid input, only numbers are allowed");
        }
    }
}
