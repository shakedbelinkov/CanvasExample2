package com.example.Shcrible;

import java.util.Random;

public class Word {
    private String word,clue="";
    private String[] words = {"apple", "banana", "cherry", "dog", "elephant", "flower", "grape", "house", "island", "jungle", "kite", "lemon", "mountain", "notebook", "orange", "parrot", "queen", "rose", "sunflower", "tree", "umbrella", "violet", "watermelon", "xylophone", "yellow", "zebra", "ant", "ball", "cat", "doll", "egg", "fish", "guitar", "hat", "igloo", "jackal", "koala", "lighthouse", "monkey", "noodles", "octopus", "piano", "quilt", "rabbit", "snake", "tiger", "unicorn", "vase", "wolf", "x-ray", "yacht", "zeppelin", "airplane", "butterfly", "candle", "doghouse", "elephant", "flame", "glove", "homework", "insect", "jacket", "king", "light", "moon", "notebook", "owl", "pen", "queen", "rose", "snow", "turtle", "universe", "vampire", "whale", "xenon", "yellowstone", "zoo", "angel", "balloon", "chicken", "drum", "elephant", "fruit", "guitar", "hotdog", "ink", "jellyfish", "kitchen", "leaf", "monster", "night", "ocean", "pencil", "quiche", "rocket", "sun", "treehouse", "umbrella", "vulture", "whale", "xerox", "yoga", "zebra"};
    private int lastIndex,clues=0;
    public Word()
    {

    }
    public void ChooseWord()
    {
        //choose random word from "words"
        Random random=new Random();
        clue="";
        clues=0;
        int num=random.nextInt(99);
        if (num!=lastIndex) {
            word = words[num];
            lastIndex=num;
        }
        else
            ChooseWord();
    }
    public boolean isRight(String answer)
    {
        return answer.equals(word);
    }
    public String Clue()
    {
        //give a clue- string in this structure (first letter+"_"*size of the word)
        clue="";
        for (int i=0;i<word.length();i++)
        {
            if (i<=clues)
                clue+=word.substring(i,i+1);
            else
                clue+=" _ ";
        }
        return clue;
    }
    public void setWord(String s)
    {
        this.word=s;
    }
    public String getWord()
    {
        return word;
    }

}
