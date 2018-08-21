package cs2410.assn6;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Helper Class file for Assignment #6. You can use this without any modification. It's the file I used to
 * create the program in the demo video.
 *
 * @author Chad
 * @version 1.0
 */
public class ToolPane extends HBox {
    private Text fillText = new Text("Fill");
    private final ColorPicker fillPicker = new ColorPicker();
    private Text strokeText = new Text("Stroke");
    private final ColorPicker strokePicker = new ColorPicker();
    //This ComboBox contains three Integer objects with values 1, 3, and 5
    private ComboBox<Integer> strokeSize = new ComboBox<>(FXCollections.observableArrayList(1, 3, 5));
    private ToggleButton editBtn = new ToggleButton("Edit");
    private ToggleButton eraseBtn = new ToggleButton("Erase");
    private ToggleButton ellBtn = new ToggleButton("Ellipse");
    private ToggleButton rectBtn = new ToggleButton("Rectangle");
    private ToggleButton freeBtn = new ToggleButton("Freehand");

    public ToolPane() {
        this.getChildren().addAll(fillText, fillPicker, strokeText, strokePicker, strokeSize, editBtn, eraseBtn,
                ellBtn, rectBtn, freeBtn);

        ToggleGroup toggleGroup = new ToggleGroup();

        //adding ToggleButtons to a ToggleGroup makes it so only one can be selected at a time.
        toggleGroup.getToggles().addAll(editBtn, eraseBtn,
                ellBtn, rectBtn, freeBtn);
        ellBtn.setSelected(true);

        fillPicker.setValue(Color.WHITE);
        strokePicker.setValue(Color.BLACK);
        fillPicker.setStyle("-fx-color-label-visible: false");
        strokePicker.setStyle("-fx-color-label-visible: false");
        strokeSize.setValue(1);

        this.setPadding(new Insets(5, 5, 5, 5));
        this.setSpacing(5);
    }

    public void setFillPickerAction(EventHandler<ActionEvent> event) {
        fillPicker.setOnAction(event);
    }

    public void setFillPickerValue(Color color) {
        fillPicker.setValue(color);
    }

    public Color getFillPickerValue() {
        return fillPicker.getValue();
    }

    public void setStrokePickerAction(EventHandler<ActionEvent> event) {
        strokePicker.setOnAction(event);
    }

    public void setStrokePickerValue(Color color) {
        strokePicker.setValue(color);
    }

    public Color getStrokePickerValue() {
        return strokePicker.getValue();
    }

    public void setStrokeSizeAction(EventHandler<ActionEvent> event ) {
        strokeSize.setOnAction(event);
    }

    public void setStrokeSizeValue(Integer val) {
        strokeSize.setValue(val);
    }

    public Integer getStrokeSizeValue() {
        return strokeSize.getValue();
    }

    public boolean editBtnSelected() {
        return editBtn.isSelected();
    }

    public boolean eraseBtnSelected() {
        return eraseBtn.isSelected();
    }

    public boolean ellBtnSelected() {
        return ellBtn.isSelected();
    }

    public boolean rectBtnSelected() {
        return rectBtn.isSelected();
    }

    public boolean freeBtnSelected() {
        return freeBtn.isSelected();
    }
}
