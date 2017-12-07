package kraksat.pl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements ChangeListener, Initializable {

    @FXML
    public Label centerPositionText;

    @FXML
    public Label objectPositionText;
    @FXML
    public Label objectDegreePositionText;

    @FXML
    public Label objectRotationSpeedText;

    @FXML
    public Button setCenterButton;

    @FXML
    private ChoiceBox metodChoiceBox;

    @FXML
    private Button startButton;

    @FXML
    private Button startBinarizationButton;

    @FXML
    private Button startTrackingButton;

    @FXML
    private ImageView currentFrame;

    @FXML
    private ImageView modifyCurrentFrame;

    @FXML
    private Slider sliderVal1Min;

    @FXML
    private Slider sliderVal1Max;

    @FXML
    private Slider sliderVal2Min;

    @FXML
    private Slider sliderVal2Max;

    @FXML
    private Slider sliderVal3Min;

    @FXML
    private Slider sliderVal3Max;

    private static VideoHandler handler = new VideoHandler();

    @FXML
    protected void startCamera(ActionEvent event) {
        handler.setMinPixVal((int) sliderVal1Min.getValue(), (int) sliderVal2Min.getValue(), (int) sliderVal3Min.getValue());
        handler.setMaxPixVal((int) sliderVal1Max.getValue(), (int) sliderVal2Max.getValue(), (int) sliderVal3Max.getValue());
        if (!handler.isCameraActive()) {
            handler.handleVideo(currentFrame, modifyCurrentFrame, centerPositionText, objectPositionText,objectDegreePositionText,objectRotationSpeedText);
            if (handler.isCameraActive())
                this.startButton.setText("Stop camera");
        } else {
            this.startButton.setText("Start camera");
            handler.stopAcquisition();
        }
    }

    protected void setClosed() {
        handler.stopAcquisition();
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        handler.setMinPixVal((int) sliderVal1Min.getValue(), (int) sliderVal2Min.getValue(), (int) sliderVal3Min.getValue());
        handler.setMaxPixVal((int) sliderVal1Max.getValue(), (int) sliderVal2Max.getValue(), (int) sliderVal3Max.getValue());
        handler.setConversionMated(metodChoiceBox.getValue().toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sliderVal1Max.valueProperty().addListener(this);
        sliderVal1Min.valueProperty().addListener(this);
        sliderVal2Max.valueProperty().addListener(this);
        sliderVal2Min.valueProperty().addListener(this);
        sliderVal3Max.valueProperty().addListener(this);
        sliderVal3Min.valueProperty().addListener(this);
        metodChoiceBox.valueProperty().addListener(this);
    }

    public void startBinarization(ActionEvent actionEvent) {
        if (!handler.isBinarizationFlag()) {
            handler.setBinarizationFlag(true);
            startBinarizationButton.setText("Stop binarization");
        } else {
            handler.setBinarizationFlag(false);
            startBinarizationButton.setText("Start binarization");
        }
    }

    public void centerSet(ActionEvent actionEvent) {
        handler.setSetCenterOfRotationFlag(true);
    }

    public void objectTrack(ActionEvent actionEvent) {
        if (!handler.isTrackingFlag()) {
            handler.setTrackingFlag(true);
            startTrackingButton.setText("Stop tracking");
        } else {
            handler.setTrackingFlag(false);
            startTrackingButton.setText("Start tracking");
        }
    }
}
