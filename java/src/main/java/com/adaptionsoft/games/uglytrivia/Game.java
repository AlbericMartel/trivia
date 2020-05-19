package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {
    private static final int MAX_NB_PLAYERS = 6;
    private static final int WINNING_NB_GOLD_COINS = 6;
    private static final int NB_PLACES = 12;

    List<Player> players = new ArrayList<>();
    int[] purses = new int[MAX_NB_PLAYERS];
    boolean[] inPenaltyBox = new boolean[MAX_NB_PLAYERS];

    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public Game() {
        for (int i = 0; i < 50; i++) {
            popQuestions.addLast(createQuestion(Category.POP_CATEGORY, i));
            scienceQuestions.addLast(createQuestion(Category.SCIENCE_CATEGORY, i));
            sportsQuestions.addLast(createQuestion(Category.SPORTS_CATEGORY, i));
            rockQuestions.addLast(createQuestion(Category.ROCK_CATEGORY, i));
        }
    }

    public String createQuestion(Category category, int index) {
        return category.getLabel() + " Question " + index;
    }

    public boolean add(String playerName) {
        players.add(new Player(playerName));

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public void roll(int roll) {
        System.out.println(players.get(currentPlayer).getPlayerName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.println(players.get(currentPlayer).getPlayerName() + " is getting out of the penalty box");
                moveAndAskQuestion(roll);
            } else {
                System.out.println(players.get(currentPlayer).getPlayerName() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {
            moveAndAskQuestion(roll);
        }

    }

    private void moveAndAskQuestion(int roll) {
        players.get(currentPlayer).move(roll);

        System.out.println(players.get(currentPlayer).getPlayerName()
                + "'s new location is "
                + players.get(currentPlayer).getPlace());
        System.out.println("The category is " + currentCategory().getLabel());
        askQuestion();
    }

    private void askQuestion() {
        if (currentCategory().equals(Category.POP_CATEGORY))
            System.out.println(popQuestions.removeFirst());
        if (currentCategory().equals(Category.SCIENCE_CATEGORY))
            System.out.println(scienceQuestions.removeFirst());
        if (currentCategory().equals(Category.SPORTS_CATEGORY))
            System.out.println(sportsQuestions.removeFirst());
        if (currentCategory().equals(Category.ROCK_CATEGORY))
            System.out.println(rockQuestions.removeFirst());
    }


    private Category currentCategory() {
        switch (players.get(currentPlayer).getPlace() % 4) {
            case 0:
                return Category.POP_CATEGORY;
            case 1:
                return Category.SCIENCE_CATEGORY;
            case 2:
                return Category.SPORTS_CATEGORY;
            case 3:
                return Category.ROCK_CATEGORY;
            default:
                throw new IllegalStateException();
        }
    }

    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                addGoldCoinToPurse();
                return didPlayerWin();
            } else {
                return true;
            }
        } else {
            addGoldCoinToPurse();
            return didPlayerWin();
        }
    }

    public void moveToNextPlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    private void addGoldCoinToPurse() {
        System.out.println("Answer was correct!!!!");
        purses[currentPlayer]++;
        System.out.println(players.get(currentPlayer).getPlayerName()
                + " now has "
                + purses[currentPlayer]
                + " Gold Coins.");
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(players.get(currentPlayer).getPlayerName() + " was sent to the penalty box");
        inPenaltyBox[currentPlayer] = true;

        return true;
    }


    private boolean didPlayerWin() {
        return !(purses[currentPlayer] == WINNING_NB_GOLD_COINS);
    }

    private enum Category {
        POP_CATEGORY("Pop"),
        SCIENCE_CATEGORY("Science"),
        SPORTS_CATEGORY("Sports"),
        ROCK_CATEGORY("Rock");

        private final String label;

        Category(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private static class Player {
        private final String playerName;
        private int place;
        private int purse;
        private boolean inPenaltyBox;

        public Player(String playerName) {
            this.playerName = playerName;
        }

        public void move(int roll) {
            this.place = (this.place + roll) % NB_PLACES;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getPlace() {
            return place;
        }

        public int getPurse() {
            return purse;
        }

        public boolean isInPenaltyBox() {
            return inPenaltyBox;
        }
    }
}
