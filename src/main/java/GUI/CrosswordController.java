package GUI;

import Application.Word;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static GUI.Utility.*;



public class CrosswordController {

    private List<String> crosswordStrings = null;
    private List<String> answerStrings = new ArrayList<>();
    char[][] crossword = null;

    private int w;
    private int h;
    int[] wordWidth;
    private boolean check = false;
    CrosswordPanel panel = null;
    @FXML
    public BorderPane borderPane;
    @FXML
    public Button generateButton;
    @FXML
    public Button hintsButton;
    @FXML
    public Button submitButton;

    @FXML
    public void generateCrossword(MouseEvent mouseEvent) {
        panel = new CrosswordPanel();
        borderPane.setCenter(panel);
        generate(panel);
    };

    @FXML
    public void submit(MouseEvent mouseEvent) {
        panel.compare();
        System.out.println(check);
    }


    private void generate(CrosswordPanel panel) {
        crosswordGenerator.buildCrossword();
        crosswordStrings = new ArrayList<>();
        for (Word word : crosswordGenerator.getCrossword()) {
            crosswordStrings.add(word.getTargetWord());
        };
        crosswordGenerator.print();
        w = crosswordGenerator.getLength() * 2 + 1;
        h = crosswordStrings.size();
        crossword = new char[w][h];
        wordWidth = new int[w];
        for (int y = 0; y < h; y++) {
            String temp = crosswordStrings.get(y);
            wordWidth[y] = temp.length();
            for (int x = 0; x < temp.length(); x++) {
                crossword[x][y] = temp.charAt(x);
            }
        }
        panel.setCrossword(crossword);
    }

    private class CrosswordPanel extends GridPane {
        private TextField[][] grid;

        void setCrossword(char[][] array) {
            getChildren().clear();

            grid = new TextField[w][h];

            for (int y = 0; y < h; y++) {
                int width = wordWidth[y];
                Random random = new Random();
                int rand = random.nextInt(width);
                while (array[rand][y] == ' ') {
                    rand = random.nextInt(width);
                }
                for (int x = 0; x < width; x++) {
                    char character = array[x][y];
                    if (character != ' ' && x != rand) {
                        grid[x][y] = new TextField(String.valueOf(character));
                        grid[x][y].setFont(Font.font("Arial", 20));
                        add(grid[x][y], x, y);
                    } else if (x != rand) {
                        add(new Label(), x, y);
                    } else {
                        grid[x][y] = new TextField();
                        grid[x][y].setFont(Font.font("Arial", 20));
                        add(grid[x][y], x, y);
                    }
                }

            }
        }
        void compare(){
            if (!answerStrings.isEmpty()) {
                answerStrings.clear();
            }

            for (int i = 0; i < h; i++) {
                String temp = "";
                for (int j = 0; j < wordWidth[i]; j++) {
                    if (grid[j][i] != null) {
                        temp += grid[j][i].getText();
                    }
                }
                answerStrings.add(temp);
            }
            for (int i = 0; i < answerStrings.size(); i++) {
                if (!answerStrings.get(i).equalsIgnoreCase(crosswordStrings.get(i).trim())) {
                    check = false;
                    return;
                }
            }
            check = true;
        }
    }
}