package SpellChecker;

import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpellcheckerSuggestion {

    public static void main(String[] args) {
        // Scanner To get the user input
        Scanner fileUserInput = new Scanner(System.in);
        //Telling the user to enter a file name
        System.out.println("Enter a File Name To Input");
        // Storing the user name in a String
        String fileName = fileUserInput.next();
        //Opening the input file
        File userInputFile = new File(fileName + ".txt");
        //allows user to choose name for output file.
        System.out.println("Choose a file name that you want it to save as");
        //getting the name from the user
        String fileName2 = fileUserInput.next();
        //new file created
        File userInputFile2 = new File(fileName2 + ".txt");

        //Opening the Dictonary
        File file = new File("Dictionary.txt");
        //Storing the dictionary in an array
        ArrayList<String> dictionaryList = new ArrayList<String>();
        //Storing the input file in a array
        ArrayList<String> textList = new ArrayList<String>();
        //Creating the suggestion array
        ArrayList<String> suggestions = new ArrayList<String>();
        //Append String
        String append = "";
        //The userWord
        String userWord = "";
        //The outPut of the Dictionary
        String dicOutput = "";
        //Append for the outPut Suggestion
        String append2 = "";

        Scanner input;
        Scanner userInput;
        Scanner choice;
        Scanner indexgetter = new Scanner(System.in);
        //try this set of code
        try {

            input = new Scanner(file);
            choice = new Scanner(System.in);
            String decision = "";

            while (input.hasNextLine()) {

                dictionaryList.add(input.nextLine().toLowerCase());
            }

            userInput = new Scanner(userInputFile);

            while (userInput.hasNext()) {
                userWord = userInput.next();
                textList.add(userWord);
                if (spellCheck(userWord, dictionaryList)) {
                    System.out.println("No Errors");
                } else {

                    System.out.println("Press 1 To Re-Enter A new Word ");
                    System.out.println("Press 2 To Add the Word to the Dictionary");
                    System.out.println("Press 3 To Replace With a Suggestion");
                    System.out.println("Press 4 To Exit");
                    decision = choice.next();
                    //For this if statement, if the user enters 1, the program allows user to enter a new word
                    if (decision.equals("1")) {
                        //loops through the word list
                        for (int i = 0; i < textList.size(); i++) {
                            //if users word is in text list prompts user to enter new word
                            if (textList.get(i).equals(userWord)) {

                                System.out.println("Please Enter A New Word");
                                String word = choice.next();
                                //takes word from user and replaces it with the word in textList array.
                                textList.set(i, word);

                            }

                        }

                        FileWriter writer = new FileWriter(userInputFile2);
                        //for each string in array, prints out the word
                        for (String word : textList) {
                            writer.write(word + " ");
                        }
                        writer.close();
                        //Pressing 2 will allow user to add word to dictionary
                    } else if (decision.equals("2")) {
                        //adds users words to dictionary in lower case
                        dictionaryList.add(userWord.toLowerCase());
                        System.out.println("Adding......");
                        //sorts the dictionary alphabetically using collection sort
                        Collections.sort(dictionaryList);
                        //Replaces existing dictionary file with updated new one.
                        FileWriter writer = new FileWriter("Dictionary.txt");
                        for (String word : dictionaryList) {
                            writer.write(word + System.lineSeparator());
                        }
                        //closes file writer
                        writer.close();
                        System.out.println("Dictonary Updated");
                        //Pressing 3 will bring up similar word suggestions
                        //Using the soundex algorithm to suggest similar words.
                    } else if (decision.equals("3")) {

                        String output = soundEx(userWord);
                        //loops through dictionary
                        for (int i = 0; i < dictionaryList.size(); i++) {
                            //if theres any words in dictionary that have same soundex code as user input
                            if (soundEx(dictionaryList.get(i)).equals(output)) {

                                String a = dictionaryList.get(i);
                                //add to suggestions array
                                suggestions.add(a);

                            }

                        }
                        //displaying the suggestions to the user
                        for (int i = 0; i < suggestions.size(); i++) {
                            String a = suggestions.get(i);
                            System.out.println(i + " " + a);

                        }
                        //once similar words are suggested, pressing the desired number will change the existing word.
                        System.out.println("Press The number to replace with that word");
                        int index = indexgetter.nextInt();

                        for (int i = 0; i < textList.size(); i++) {
                            //in text list array the word that has the same code replace it with existing word
                            String a = soundEx(textList.get(i));
                            String b = soundEx(suggestions.get(index));
                            if (a.equals(b)) {
                                textList.set(i, suggestions.get(index).toString());

                            }
                        }
                        //writes it to user output file.
                        FileWriter writer = new FileWriter(userInputFile2);
                        for (String word : textList) {
                            writer.write(word + " ");
                        }
                        writer.close();
                        suggestions.clear();
                        //pressing 4 will allow user to exit.
                    } else if (decision.equals("4")) {
                        //goes through words in text file
                        while (userInput.hasNext()) {
                            userWord = userInput.next();
                            textList.add(userWord);
                        }
                        //writes it to user output file
                        FileWriter writer = new FileWriter(userInputFile2);
                        for (String word : textList) {
                            writer.write(word + " ");

                        }

                        writer.close();
                        break;
                    } else {
                        System.out.println("No valid input");
                    }

                }

            }
            //writes file to the output
            FileWriter writer = new FileWriter(userInputFile2);
            for (String word : textList) {
                writer.write(word + " ");
            }
            writer.close();
            //if theres any errors it catches it here
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    public static boolean spellCheck(String input, ArrayList<String> dictionaryList) {
        //Gets input with a string
        //checks theres no spaces in a word
        //compares each char with the dictionary
        String currentCheck = "";
        boolean noErrors = true;
        Scanner spellChecker = new Scanner(input);
        spellChecker.useDelimiter("\\s+");
        while (spellChecker.hasNext()) {
            currentCheck = spellChecker.next();
            if (!isSpecial(currentCheck)) {

                if (!checkWord(currentCheck, dictionaryList)) {
                    System.out.println(currentCheck + " Is Spelt Incorrectly");
                    noErrors = false;
                }
            }
        }

        return noErrors;
    }

    public static boolean isSpecial(String input) {
        //This checks for any special characters in the file.
        //Only takes letters in the alphabet and numbers 0-9
        Pattern pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(input);
        return match.find();
    }

    public static boolean checkWord(String input, ArrayList<String> dictionaryList) {
        //Takes a string value and array value, and checks to see if its in the array.
        boolean valid = false;
        int length = dictionaryList.size();
        int i = 0;
        while (!valid && i < length) {

            if (input.trim().equalsIgnoreCase(dictionaryList.get(i))) {
                valid = true;

            }

            i++;
        }

        return valid;
    }

    public static final char[] map = {
        //map that gives each string value a number.
        //from: AEHIOUWYBFPVCGJKQSXZDTLMNR
        //to:   00000000111122222222334556
        '0', '1', '2', '3', '0', '1', '2', '0', '0', '2', '2', '4', '5', '5', '0', '1', '2', '6', '2', '3', '0', '1', '0', '2', '0', '2'
    };

    public static char mapChar(char c) {
        return map[c - 'A'];
    }

    public static String soundEx(String s) {
        //soundEx algorithm that gives similar word suggestions based on how similar they sound
        //algorithm works on uppercase
        String code = "";
        char prev = '?';

        if (s.length() == 0) {
            return null;
        } else {
            String t = s.toUpperCase();
            for (int i = 0; i < t.length() && code.length() < 4; i++) {
                char c = t.charAt(i);
                if (Character.isLetter(c)) {
                    if (i == 0) {
                        code = code + c;
                    } else {
                        if (c != prev) {
                            char m = mapChar(c);
                            prev = c;
                            if (m != '0') {
                                code = code + m;
                            }

                        }
                    }
                }
            }
            while (code.length() < 4) {
                code = code + '0';

            }
            return code;
        }
    }

}
