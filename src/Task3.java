import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

public class Task3 {

    static Scanner text_scanner;

    public static void main(String[] args) throws IOException {

        text_scanner = new Scanner(System.in);

        int max_pass, op = -1;
        boolean valid_input = false;
        String temp;

        //get the max size of password
        max_pass = get_max_pass();

        //get whether or not the user wants to crack the hashed file or the salted file
        System.out.println("""
                Would you like to crack a password from the hashed file or the salted file
                1) Hashed
                2) Salted
                """);

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
            crack_hash(max_pass);
        }
        else crack_salt(max_pass);

    }

    private static void crack_hash(int max) throws IOException {
        String test_pass = "";
        BufferedReader reader;
        FileReader in_file = new FileReader("hashed.txt");
        int random_line = ThreadLocalRandom.current().nextInt(0, 99 + 1);
        String line;
        int line_no = 0;
        reader = new BufferedReader(in_file);
        while ((line = reader.readLine()) != null){
            if (line_no == random_line){
                break;
            }
            else {
                line_no++;
            }
        }
        if (line == null){
            System.out.println("Error line is null, exiting early...");
            System.exit(0);
        }
        String[] split_string = line.split(" ");
        String username = split_string[0];
        String pass_from_file = split_string[1];

        //start brute forcing passwords up to size max
        while (true){

            //hash it

            //compare


            if (test_pass.equals(pass_from_file)){
                break;
            }
        }
    }

    private static void crack_salt(int max) throws IOException {
        String test_pass = "";
        BufferedReader reader;
        FileReader in_file = new FileReader("salted.txt");
        int random_line = ThreadLocalRandom.current().nextInt(0, 99 + 1);
        String line;
        int line_no = 0;
        reader = new BufferedReader(in_file);
        while ((line = reader.readLine()) != null){
            if (line_no == random_line){
                break;
            }
            else {
                line_no++;
            }
        }
        if (line == null){
            System.out.println("Error line is null, exiting early...");
            System.exit(0);
        }
        String[] split_string = line.split(" ");
        String username = split_string[0];
        char salt = split_string[1].charAt(0);
        String pass_from_file = split_string[2];


        //start brute forcing passwords up to size max
        while (true){
            //salt it

            //hash it

            //compare

            if (test_pass.equals(pass_from_file)){
                break;
            }
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
        return max_pass;
    }

}
