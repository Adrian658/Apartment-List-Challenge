import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Apartment List Coding Challenge
 * Runtime using Strings and booleans: 21s
 */
public class ApartmentList {
  
  /** Stores the number of friends */
  int count = 0;
  
  /**
   * Class representing a Word object with a name and boolean value if it has been added to the count already
   */
  private class Word {
    
    /** The word string associated with the Word object */
    String word;
    
    /** Whether the word has been counted as a friend already */
    boolean visited = false;
    
    /**
     * Constructor
     * @param string the name of the Word as a string
     */
    public Word(String string) {
      word = string;
    }
    
    /**
     * Returns the word
     * @return word the objects word
     */
    public String getWord() {
      return word;
    }
    
    /**
     * Returns whether the Word has been counted as a friend already
     * @return visited status of the Word
     */
    public boolean getVisited() {
      return visited;
    }
    
    /**
     * Changes the boolean value of visited
     */
    public void changeVisited() {
      visited = !visited;
    }
    
  }
  
  /**
   * Creates an array containing all the words in the specified .txt file
   * @param fileName name of file to parse
   * @return list the list of words contained in the specified file
   * @throws IOException
   */
  public ArrayList<Word> createInitialList(String fileName) throws IOException {
    
    int numberWords = 0; //number of words in the file
    ArrayList<Word> list = new ArrayList<Word>(); //list of Words representing the words contained in the file
    BufferedReader br = new BufferedReader(new FileReader(fileName));
    while (br.readLine() != null) //counts the number of words in the file
      numberWords++;
    br = new BufferedReader(new FileReader(fileName));
    for (int i = 0; i <= numberWords - 1; i++) { //converts each word in the file into a Word and adds it to the array list
      list.add(new Word(br.readLine()));
    }
    return list;
    
  }
  
  /**
   * Counts the number of friends a given word has in the dictionary using recursion
   * @param dictionaryList the list of dictionary words in arrayList format
   * @param word the string to search how many friends they have
   */
  public void countFriends(ArrayList<Word> dictionaryList, String word) {
    
    ArrayList<Word> friends = new ArrayList<Word>(); //list of friends the passed in word has
    for (int i = 0; i < dictionaryList.size(); i++) { //iterates over the dictionaryList
      Word currentWord = dictionaryList.get(i); //current word being analyzed
      /* if the current word is within 1 Levenshtein distance from the passed in word:
       * -increase the friends count by 1
       * -change the words visited status
       * -add the word to the array of friends */
      if (findDifference(word, currentWord.getWord()) && !currentWord.getVisited()) {
        friends.add(currentWord);
        addCount();
        currentWord.changeVisited();
      }
    }
    if (friends.size() == 0) //if the passed in word has no friends that have not been visited
      return;
    /* for each element in friends, find the amount of friends that element has */
    for (int i = 0; i < friends.size(); i++)
      countFriends(dictionaryList, friends.get(i).getWord());
    
  }
  
  /**
   * Determines whether two words are 'Levenshtein friends'
   * @param word1 first word to compare
   * @param word2 second word to compare
   * @return boolean whether the words are friends or not
   */
  public boolean findDifference(String word1, String word2) {
    
    int word1Length = word1.length(); int word2Length = word2.length();
    /* If the words have a difference in length of more than 1 letter return false because they can not be friends */
    if (word1Length > word2Length + 1 || word2Length > word1Length + 1)
      return false;
    
    String smallerWord; String largerWord;
    /* Determines which word is shorter if they are of different lengths, if not both words are equal length */
    if (word1.length() <= word2.length()) {
      smallerWord = word1; largerWord = word2;
    }
    else {
      smallerWord = word2; largerWord = word1;
    }
    
    int differentLetters = 0; //number of letters the words have different
    /* Loops through smaller word to find letters that differ with larger word */
    for (int i = 0; i < smallerWord.length(); i++) {
      if (!(smallerWord.charAt(i) == largerWord.charAt(i))) { //if the letters at the current index do not match, increment the corresponding field
        differentLetters++;
        if (differentLetters > 1) //if the words have more than 1 different letters, they can not be friends
          return false;
      }
    }
    /* edge case where the word not being looped through is longer and the loop only catches 1 different letter.
     * In this case the words would differ by two letters so false is returned */
    if (differentLetters == 1 && largerWord.length() > smallerWord.length()) 
      return false;
    else
      return true;
  }
  
  /**
   * Main method
   * Testing the algorithm with a .txt file
   * Make sure 1st parameter entered is name of file in string format and 2nd parameter is the word to find friends for
   * @param params input parameters
   */
  public static void main(String[] params) {
    ApartmentList instance = new ApartmentList();
    try {
      instance.countFriends(instance.createInitialList(params[0]), params[1]);
      System.out.println(instance.getCount());
    }
    catch(IOException e) { //if file name is in wrong format
      System.out.println(e + ". Use format: java ApartmentList your_file_name.txt 'word'");
    }
  }
  
  /**
   * Returns the number of friends
   * @return count number of friends
   */
  public int getCount() {
    return count; 
  }
  
  /**
   * Increases the count by 1
   */
  public void addCount() {
    count++;
  }
  
}