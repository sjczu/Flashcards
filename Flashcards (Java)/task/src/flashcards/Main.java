package flashcards;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {


    public static void main(String[] args){

        STATUS status = STATUS.RUNNING;
        ArrayList<String> terms = new ArrayList<String>();
        ArrayList<String> definitions = new ArrayList<String>();

        while(status.equals(STATUS.RUNNING)) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Input the action (add, remove, import, export, ask, exit):");
            String option = scanner.nextLine();

            switch(option) {

                case "add":
                    System.out.println("The card:");
                    String term = scanner.nextLine();

                    if (terms.contains(term)) {
                        System.out.println("The card \"" + term + "\" already exists.");
                        break;
                    }

                    System.out.println("The definition of the card:");
                    String definition = scanner.nextLine();

                    if (definitions.contains(definition)) {
                        System.out.println("The definition \"" + definition + "\" already exists.");
                        break;
                    }

                    terms.add(term);
                    definitions.add(definition);

                    System.out.println("The pair (\"" + term + "\":\"" + definition + "\") has been added.");

                    break;

                case "remove":
                    System.out.println("Which card?");
                    String remTerm = scanner.nextLine();
                    int remIndex = terms.indexOf(remTerm);

                    if (terms.contains(remTerm)) {
                        terms.remove(remTerm);
                        definitions.remove(remIndex);
                        System.out.println("The card has been removed");
                        break;
                    }
                    System.out.println("Can't remove \"" + remTerm + "\": there is no such card");
                    break;

                case "import":
                    System.out.println("File name:");
                    String importFileName = scanner.nextLine();
                    File importFile = new File(importFileName);

                    if(!importFile.exists()) {
                        System.out.println("File not found.");
                        break;
                    }

                    try(Scanner fileScanner = new Scanner(importFile)) {
                        int importCount = 0;
                        while(fileScanner.hasNextLine()) {
                            String temp = fileScanner.nextLine();
//                            System.out.println("1"+temp);
                            if(terms.contains(temp)){
                                String temp2 = fileScanner.nextLine();
//                                System.out.println("2"+temp2);
                                definitions.set(terms.indexOf(temp), temp2);
                            } else {
                                terms.add(temp);
                                definitions.add(fileScanner.nextLine());
                            }
                            importCount++;
                        }
                        System.out.println(importCount + " cards have been loaded.");

                        break;
                    }catch(Exception e) {
                        System.out.println("Exception! " + e.getMessage());
                        break;
                    }

                case "export":
                    System.out.println("File name:");
                    String exportFileName = scanner.nextLine();
                    File exportFile = new File(exportFileName);

                    try(PrintWriter printWriter = new PrintWriter(exportFile)){
                        int countExport = 0;

                        for(int i = 0; i<terms.size();i++){
                            printWriter.println(terms.get(i));
                            printWriter.println(definitions.get(i));
                            countExport++;
                        }

                        System.out.println(countExport + " cards have been saved.");

                        break;
                    }catch (IOException e){
                        System.out.println("Exception! " + e.getMessage());
                        break;
                    }

                case "ask":
                    System.out.println("How many times to ask?");
                    int numOfCards = Integer.parseInt(scanner.nextLine());

                    Random random = new Random(numOfCards);
                    int index = 0;

                    String answer = null;
                    for(int i = 0;i<numOfCards;i++){

                        index = random.nextInt(terms.size());

                        Card card = new Card(terms.get(index),definitions.get(index));

                        System.out.println("Print the definition of \"" + card.getTerm() + "\":");
                        answer = scanner.nextLine();

                        if(card.getDefinition().equals(answer)){
                            System.out.println("Correct!");
                        } else {
                            if(!definitions.contains(answer)){
                                System.out.println("Wrong. The right answer is \"" + definitions.get(terms.indexOf(card.getTerm())) + "\".");
                            } else {
                                System.out.println("Wrong. The right answer is \"" + definitions.get(definitions.indexOf(card.getDefinition())) + "\", but your definition is correct for \"" + terms.get(definitions.indexOf(answer)) + "\".");
                            }
                        }
                    }
                    break;

                case "exit":
                    System.out.println("Bye bye!");
                    status = STATUS.CLOSING;
                    break;

                default:
                    status = STATUS.EXITED;
                    break;
            }
        }
    }
}

class Card{
    private String term;
    private String definition;

    public Card(String term, String definition){
        this.term = term;
        this.definition = definition;
    }

    void setTerm(String term){
        this.term = term;
    }

    void setDefinition(String definition){
        this.definition = definition;
    }

    String getTerm(){
        return this.term;
    }

    String getDefinition(){
        return this.definition;
    }

}


enum STATUS{
    RUNNING,
    CLOSING,
    EXITED
}