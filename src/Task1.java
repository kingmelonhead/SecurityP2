import java.util.Scanner;  // Import the Scanner class

public class Task1 {

    static Scanner text_scanner;

    public static void main(String args[]){
        // get whether or not the user wants to create or authenticate

        text_scanner = new Scanner(System.in);

        int op = -1;
        boolean valid_input = false;
        String temp;

        System.out.println("Would you like to authenticate or create account?\n" +
                "1) Authenticate\n2) Create Account\n");

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
            authenticate();
        }
        else create();

    }

    private static void authenticate(){
        //get username

        //get password

        //compare with plain text, print result

        //hash input

        //compare with hash, print result

        //find salt by username, salt and hash password, compare with salt file

    }


    private static void create(){
        //get username

        //get password

        //output to the 3 files

        //A plaintext username password pair, stored in text in a file

        //A username and a hashed password, stored in some format in the file

        //A username, a salt and the result of the hashed (password + salt), stored in some format in the file

    }
}
