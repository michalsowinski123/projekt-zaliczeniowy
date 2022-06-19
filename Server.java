import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Server {

    public static void main(String args[])throws Exception{
        ServerSocket ss=new ServerSocket(3333);
        Socket s=ss.accept();
        DataInputStream din=new DataInputStream(s.getInputStream());
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());

        HashMap<String, String> flashCards = new HashMap<String, String>();
        ArrayList<String> englishWords = new ArrayList();
        initializeFlashcards(englishWords,flashCards);

        String str="";
        dout.writeUTF("Hi player. Type how many flashcards you want to get ?"
                +flashCards.size() + " - maximum" + " type 'stop'  to exit");
        dout.flush();
        while(!str.equals("stop")){


            str=din.readUTF();
            System.out.println("client says: "+str);

            if(isNumeric(str)){
                if(Double.parseDouble(str) > flashCards.size() || Integer.parseInt(str)<0 || str.equals("0")) {
                    dout.writeUTF("Your number is greater than: " + flashCards.size() + " or <=0 type again");
                    dout.flush();
                }else{
                    int steps = Integer.parseInt(str);
                    int points =0;
                    for(int i=0;i<steps;i++){
                        String flashcard = getRandomFlashCard(englishWords);
                        dout.writeUTF(flashcard);
                        dout.flush();
                        String answer=din.readUTF();
                        System.out.println("Client answer: "+answer);
                        System.out.println("Correct Answer: "+flashCards.get(flashcard));
                        if(flashCards.get(flashcard)!=null){
                            String correctAnswer = flashCards.get(flashcard);
                            if(correctAnswer.equals(answer)) {
                                points+=1;
                            }
                        }
                        System.out.println("current points : " + points);
                    }
                    dout.writeUTF("Points Summary: " + points+ "/" + steps
                    + "Your rating: " + ((double)points/steps) * 100 + "%"
                    +
                            " Play again by providing number of flashcard to check or 'stop' for exit"        );
                    dout.flush();
                }
            }
            else{
                dout.writeUTF("This is not a number type again");
                dout.flush();
            }

        }
        din.close();
        s.close();
        ss.close();
    }

    private static void initializeFlashcards(ArrayList<String> englishWords, HashMap<String, String> flashCards) {

        englishWords.add("England");
        flashCards.put("England", "Anglia");
        englishWords.add("Germany");
        flashCards.put("Germany", "Niemcy");
        englishWords.add("Norway");
        flashCards.put("Norway", "Norwegia");
        englishWords.add("Poland");
        flashCards.put("Poland", "Polska");
        englishWords.add("Cat");
        flashCards.put("Cat","Kot");
        englishWords.add("Dog");
        flashCards.put("Dog","Pies");
        englishWords.add("Beer");
        flashCards.put("Beer","Piwo");
        englishWords.add("Wheat");
        flashCards.put("Wheat","Pszenica");
    }
    private static String getRandomFlashCard(ArrayList<String> englishWords){
        Random R = new Random();
        return englishWords.get(R.nextInt(englishWords.size()));
    }
    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
