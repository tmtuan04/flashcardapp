package project.flashcardapp.Controller.LearningMode;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.flashcardapp.Controller.DeckInfoController;
import project.flashcardapp.Model.Deck;
import project.flashcardapp.Model.Selector;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

//Chức năng ôn tập
public class ReviewModeController implements Initializable {
    @FXML
    private Label questionLabel;
    @FXML
    private Label answerLabel;
    @FXML
    private Button backButton;

    private static int currentIndex = 0;
    private boolean showingQuestion = true;
    private Deck deck;
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    @FXML
    private StackPane CardPane;

    private void updateCard() {
        if (deck.getCards().getSize() == 0) {
            System.out.println("Card list is empty!");
            return;
        }

        questionLabel.setText(deck.getCards().getCard(currentIndex).getQuestion());
        answerLabel.setText(deck.getCards().getCard(currentIndex).getAnswer());
        showingQuestion = true;
        questionLabel.setVisible(true);
        answerLabel.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.deck = DeckInfoController.deck;
        updateCard();
    }

    @FXML
    void showNextCard(MouseEvent event) {
        if (currentIndex < deck.getCards().getSize() - 1) {
            currentIndex++;
            updateCard();
        }
    }

    @FXML
    void showPreviousCard(MouseEvent event) {
        if (currentIndex > 0) {
            currentIndex--;
            updateCard();
        }
    }

    @FXML
    private void flipCard() {
        Timeline timeline = new Timeline();

        // Nửa đầu của lật: quay tới 90 độ
//        KeyFrame kf1 = new KeyFrame(Duration.seconds(0.25),
//                new KeyValue(CardPane.rotateProperty(), 90));

        // Tạo một khoảng dừng để thay đổi nội dung thẻ
        PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
        pause.setOnFinished(event -> {
            if (showingQuestion) {
                questionLabel.setVisible(false);
                answerLabel.setVisible(true);
                showingQuestion = false;
            } else {
                questionLabel.setVisible(true);
                answerLabel.setVisible(false);
                showingQuestion = true;
            }
        });
        // Nửa sau của lật: quay từ 90 đến 180 độ
//        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.5),
//                new KeyValue(CardPane.rotateProperty(), 180));
//
//        timeline.getKeyFrames().addAll(kf1, kf2);
//
//        timeline.setOnFinished(event -> {
//            CardPane.setRotate(0);
//            showingQuestion = !showingQuestion;
//        });
        timeline.play();
        pause.playFromStart();
    }

    @FXML
    void isEasy(MouseEvent event) throws IOException {
        deck.getCards().getCard(currentIndex).getSelector().update(Selector.AnswerType.CORRECT,deck.getEasyCard(), deck.getMediumCard(), deck.getHardCard());
        if (currentIndex < deck.getCards().getSize() - 1) {
            currentIndex++;
            updateCard();
        }
        if(currentIndex == deck.getCards().getSize() - 1){
            goToResult();
            currentIndex = 0;
        }
    }

    @FXML
    void isHard(MouseEvent event) throws IOException {
        deck.getCards().getCard(currentIndex).getSelector().update(Selector.AnswerType.FAILURE,deck.getEasyCard(), deck.getMediumCard(), deck.getHardCard());
        if (currentIndex < deck.getCards().getSize() - 1) {
            currentIndex++;
            updateCard();
        }
        if(currentIndex == deck.getCards().getSize() - 1){
            goToResult();
            currentIndex = 0;
        }
    }

    @FXML
    void isMedium(MouseEvent event) throws IOException {
        deck.getCards().getCard(currentIndex).getSelector().update(Selector.AnswerType.MEDIUM,deck.getEasyCard(), deck.getMediumCard(), deck.getHardCard());
        if (currentIndex < deck.getCards().getSize() - 1) {
            currentIndex++;
            updateCard();
        }
        if(currentIndex == deck.getCards().getSize() - 1){
            goToResult();
            currentIndex = 0;
        }
    }

    public void backToDeckInfoWindow(MouseEvent mouseEvent) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Do you want to stop reviewing?");
        alert.setContentText("Click OK to confirm, or Cancel to continue.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/flashcardapp/deckinfo.fxml"));
            Parent detailSceneRoot = loader.load();
            Scene detailScene = new Scene(detailSceneRoot);
            Stage stage = (Stage)CardPane.getScene().getWindow();
            stage.setResizable(false);
            stage.setTitle(deck.getDeckName());
            stage.setScene(detailScene);
            stage.show();
        }
    }

    void goToResult() throws IOException {
        Stage stage = (Stage) answerLabel.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/project/flashcardapp/result_review_mode.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

}






