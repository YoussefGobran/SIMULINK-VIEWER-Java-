# SIMULINK-VIEWER [Java]
This JavaFX application allows the user to select a .mdl file and then renders a drawing of the shapes and lines contained within that .mdl file.

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
- Shows credits for the application authors
- When "Open" is clicked:
   - Extracts the XML from the .mdl file
   - Converts the XML to Block and Line with branches objects
   - Adds the shapes and lines to a Pane
   - Adds zooming and panning features to the ScrollPane
- Handles exceptions and shows error messages if needed
