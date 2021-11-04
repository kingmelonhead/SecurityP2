import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.io.FileWriter;
import java.io.IOException;

public class Task2 {

    static Scanner text_scanner;

    //program to make random passwords

    static int max_user_len = 10;

    static int max_pass_len = -1, min_pass_len = -1;

    public static void main(String args[]){

        text_scanner = new Scanner(System.in);

        //get min pass len
        min_pass_len = get_min_pass();
        //get max pass len
        max_pass_len = get_max_pass();

        //generate 100 usernames and passwords
        for (int i = 0; i < 100; i++){
            //generate username of max size (default 10, can be changed in the code)

            //generate password between max and min

            //store in plain text

            //hash and store in hash file

            //salt and store in salt file
        }
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
        if (max_pass <= min_pass_len) {
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
        return max_pass;
    }
}
