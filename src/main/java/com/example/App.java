/**
* This JavaFX application allows the user to select a .mdl file and then renders a drawing of the shapes and lines contained within that .mdl file.

To use this application: 
1. Run the App.java file. This will open a window prompting the user to select a .mdl file.
2. Click the "Browse" button and select a .mdl file to open.  
3. The .mdl file path will be displayed in the text field.
4. Click the "Open" button.
5. A new window will open showing a rendered drawing of the shapes and lines from the .mdl file.
6. You can zoom in and out of the drawing using Ctrl + mouse scroll.
7. The application handles validation to ensure a valid .mdl file is selected.

The code performs the following:
- Opens an initial window to select a .mdl file
- Displays a "Browse" button to open a file chooser
- Gets the selected .mdl file path and displays it in a text field
- Disables the "Open" button until a .mdl file is selected
- When "Open" is clicked:
   - Extracts the XML from the .mdl file
   - Converts the XML to Shape and Line objects
   - Adds the shapes and lines to a Pane
   - Adds zooming and panning features to the ScrollPane
- Handles exceptions and shows error messages if needed
- Shows credits for the application authors
*/

package com.example;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaFX App
 */

public class App extends Application {

    public void setIconStage(Stage stage) {

        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        // set title for first stage
        primaryStage.setTitle("Choose File Path");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.setResizable(false);
        setIconStage(primaryStage);
        // create a pane
        GridPane pane = new GridPane();
        pane.setHgap(8); // Set horizontal gap
        pane.setVgap(32); // Set vertical gap

        pane.setPadding(new Insets(48, 48, 48, 48));
        pane.setStyle("-fx-background-color:white;");
        pane.setAlignment(Pos.CENTER);

        // create a textfield
        TextField textField = new TextField("Please Choose a path...");
        textField.setPrefSize(600, 30);
        // choose width of textfield
        textField.setDisable(true);

        // create buttons
        Button btOpen = new Button("Open");
        btOpen.setDisable(true);

        Button btBrowse = new Button("Browse");

        // when button is pressed
        btBrowse.setOnAction((ActionEvent e) -> {
            // create a File chooser
            FileChooser fileChooser = new FileChooser();

            // get the file selected
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file == null) {
                return;
            }
            if (!file.getAbsolutePath().endsWith(".mdl")) {
                textField.setText("File is not valid");
                return;
            }

            textField.setText(file.getAbsolutePath());
            btOpen.setDisable(false);
        });

        btOpen.setOnAction((ActionEvent e) -> {

            runAppAndPaint(textField.getText(), primaryStage);
        });
        Label creditsLabel = new Label();

        creditsLabel.setText(
                "Created By:\n- Andrew Ayman Samir 2000003\n- Youssef Saad Gobran 2001440\n- Tadros Wagih Shaker 2001785\n- Salma Waleed Ismail 2000308\n- Philopateer Sameh Rasmy 2000955");
        pane.add(textField, 0, 0, 2, 1);
        pane.add(btBrowse, 0, 1);
        pane.add(btOpen, 1, 1);
        pane.add(creditsLabel, 0, 2, 2, 1);
        Scene selectPathScene = new Scene(pane);
        primaryStage.setScene(selectPathScene);
        primaryStage.show();

    }

    public static void main(String[] args) throws IOException {

        launch(args);

    }

    public void runAppAndPaint(String path, Stage primaryStage) {

        String[] pathSplitted = path.split("\\\\");
        primaryStage.setTitle(pathSplitted[pathSplitted.length - 1] + " - Drawing");

        primaryStage.setWidth(1600);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);

        Pane pane = new Pane();
        pane.setPadding(new Insets(48, 48, 48, 48));
        pane.setStyle("-fx-background-color:white;");

        try {
            String xmlPath = Extraction.extractSystemXML(path);

            Object[] objects = Converting.convertXMLToObjects(xmlPath);
            Block[] blockArr = (Block[]) objects[0];
            Map<Integer, Block> blockMap = new HashMap<Integer, Block>();
            for (int i = 0; i < blockArr.length; i++) {
                blockMap.put(blockArr[i].getId(), blockArr[i]);
                pane.getChildren().addAll(blockArr[i].getRectangleDrawing(), blockArr[i].getLabelDrawing());
            }
            Line[] lineArr = (Line[]) objects[1];
            for (int i = 0; i < lineArr.length; i++) {
                lineArr[i].generateLineDrawing(pane, blockMap);

            }
            ScrollPane scrollPane = new ScrollPane(pane);

            // Enabling zoom and pan features on the scroll pane
            scrollPane.setPannable(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            scrollPane.addEventFilter(javafx.scene.input.ScrollEvent.ANY, event -> {
                if (event.isControlDown()) {
                    double zoomFactor = 1.05;
                    if (event.getDeltaY() < 0) {
                        // Zoom out
                        zoomFactor = 1 / zoomFactor;
                    }
                    double scale = scrollPane.getScaleX() * zoomFactor;
                    scrollPane.setScaleX(scale);
                    scrollPane.setScaleY(scale);
                    event.consume();
                }
            });

            Scene scene = new Scene(scrollPane);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.out.println(String.format("Error within xml with %s", path));
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}