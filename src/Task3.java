import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.io.FileWriter;
import java.io.IOException;

public class Task3 {

    static Scanner text_scanner;

    public static void main(String args[]){

        text_scanner = new Scanner(System.in);

        int max_pass, op = -1;
        boolean valid_input = false;
        String temp;

        //get the max size of password
        max_pass = get_max_pass();

        //get whether or not the user wants to crack the hashed file or the salted file
        System.out.println("Would you like to crack a password from the hashed file or the salted file\n" +
                "1) Hashed\n2) Salted\n");

        //get the valid option from the user
        while (!valid_input){
            temp = text_scanner.nextLine();
            if (temp == "1"){
                op = 1;
                valid_input = true;
            }
            else if (temp == "2"){
                op = 2;
                valid_input = true;
            }
            else {
                System.out.println("'" + temp + "' is not a valid option try again\n");
            }
        }

        if (op == 1){
            crack_hash(max_pass);
        }
        else crack_salt(max_pass);

    }

    private static void crack_hash(int max){
        //start brute forcing passwords up to size max

        //hash it

        //compare
    }

    private static void crack_salt(int max){
        //take the salt from the line in file

        //start brute forcing passwords up to size max

        //salt it

        //hash it

        //compare
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
        return max_pass;
    }

}
