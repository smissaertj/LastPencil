package lastpencil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Set Game Env Variable
        System.out.println("How many pencils would you like to use:");
        PencilBox pencilBox = new PencilBox(scanner);
        pencilBox.setPencils();

        // Set Players
        System.out.println("Who will be the first (John, Jack):");
        String name;
        while (true) {
            name = scanner.nextLine();
            if (!name.equals("John") && !name.equals("Jack")) {
                System.out.println("Choose between 'John' and 'Jack'");
            } else {
                break;
            }
        }

        // Determine who is the human and who is the bot, based on the previous input
        Player playerOne = name.equals("John") ? new Player("John", scanner) : new Bot("Jack", scanner);
        Player playerTwo = playerOne.getName().equals("John") ? new Bot("Jack", scanner) : new Player("John", scanner);

        // Start the game
        playGame(playerOne, playerTwo, pencilBox);

        // close the open scanner object
        scanner.close();
    }

    static void playGame(Player playerOne, Player playerTwo, PencilBox pencilBox) {
        int roundCounter = 1;

        while (true) {
            // Print out the starting stack of pencils
            if (pencilBox.getPencils() > 0) {
                for (int i = 0; i < pencilBox.getPencils(); i++) {
                    System.out.print("|");
                }
                System.out.println();
            }

            // Check if we have a winning round before the game continues
            String winner = roundCounter % 2 == 0 ? playerTwo.getName() : playerOne.getName();
            if (pencilBox.checkWinner(winner)) {
                // We exit the loop/game when there's a winner.
                break;
            }

            // Determine who's turn it is
            Player currentPlayer;
            if (roundCounter % 2 == 0) {
                currentPlayer = playerTwo;
            } else {
                currentPlayer = playerOne;
            }

            // Ask the player how many pencils to take
            currentPlayer.takePencils(pencilBox);

            // Move to the next round
            roundCounter++;
        }
    }
}


class PencilBox {
    private int pencils;
    private final Scanner scanner;

    public PencilBox(Scanner scanner) {
        this.pencils = 0;
        this.scanner = scanner;
    }

    public int getPencils() {
        return this.pencils;
    }

    public void setPencils() {
        int pencils;
        while (true) {
            try {
                pencils = Integer.parseInt(this.scanner.nextLine());
                if (pencils <= 0) {
                    System.out.println("The number of pencils should be positive");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("The number of pencils should be numeric");

            }
        }
        this.pencils = pencils;
    }

    public void decrementPencils(int pencils) {
        while (true) {
            if (this.getPencils() - pencils < 0) {
                System.out.println("Too many pencils were taken");
                pencils = Integer.parseInt(this.scanner.nextLine());
            } else {
                this.pencils -= pencils;
                break;
            }
        }
    }

    public boolean checkWinner(String winner) {
        if (this.getPencils() == 0) {
            System.out.println(winner + " won!");
            return true;
        }
        return false;
    }
}


class Player {
    private final String name;
    private final Scanner scanner;

    public Player(String name, Scanner scanner) {
        this.name = name;
        this.scanner = scanner;
    }

    public String getName() {
        return this.name;
    }

    public void takePencils(PencilBox pencilBox) {
        int numberOfPencils;
        int numberOfPencilsRemaining = pencilBox.getPencils();
        System.out.println(this.getName() + "'s turn!");

        while (true) {
            try {
                numberOfPencils = Integer.parseInt(scanner.nextLine());
                if (numberOfPencils < 1 || numberOfPencils > 3) {
                    System.out.println("Possible values: '1', '2' or '3'");
                    continue;
                } else if (numberOfPencils > numberOfPencilsRemaining) {
                    System.out.println("Too many pencils were taken. Please enter a valid number of pencils to take.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Possible values: '1', '2' or '3'");
            }
        }
        pencilBox.decrementPencils(numberOfPencils);
    }
}


class Bot extends Player {
    public Bot(String name, Scanner scanner) {
        super(name, scanner);
    }

    @Override
    public void takePencils(PencilBox pencilBox) {
        int numberOfPencilsRemaining = pencilBox.getPencils();
        int pencils;
        System.out.println(this.getName() + "'s turn!");

        if (numberOfPencilsRemaining > 1) {
            if (numberOfPencilsRemaining % 4 == 0) {
                pencils = 3;
            } else if (numberOfPencilsRemaining % 2 == 1) {
                pencils = 2;
            } else {
                pencils = 1;
            }
        } else {
            pencils = 1;
        }
        System.out.println(pencils);
        pencilBox.decrementPencils(pencils);
    }
}
