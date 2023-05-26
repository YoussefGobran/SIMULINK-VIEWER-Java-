/*
 * README - Extraction.java
 *
 * Description:
 *
 * The Extraction.java file is a utility class designed to extract the System XML content from a given input file,
 * usually representing a Simulink model. The primary function of this class is extractSystemXML, which takes an input
 * file path and returns the output file path where the System XML content is saved.
 *
 * Functions:
 *
 * 1. extractSystemXML(String inputPath): The main function of this class, responsible for extracting the System XML
 * content from the input file. The function can be broken down into the following steps:
 *
 *    a. Reading the input file: Uses a Scanner object to read the input file line by line.
 *
 *    b. Identifying System XML content: Tracks whether the current line is inside the System XML content by checking
 *    for the opening "<System>" and closing "</System>" tags.
 *
 *    c. Saving System XML content: Appends each line inside the System XML content to a string variable.
 *
 *    d. Writing System XML content to a new file: Adds an XML declaration to the beginning of the System XML content,
 *    creates a new file named "Parsed.xml", and writes the System XML content to this new file using a FileWriter
 *    object.
 *
 * Usage:
 *
 * To use this class, call the extractSystemXML function with the path to the input file containing the System XML
 * content. The function will return the path to the output file where the extracted System XML content is saved.
 *
 * Example:
 *
 * String outputPath = Extraction.extractSystemXML("path/to/your/input/file.mdl");
 *
 */
package com.example;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Extraction {

    // This method extracts the System XML from a file.
    static String extractSystemXML(String inputPath) throws IOException {

        // Create a boolean variable to track whether we are currently inside the System
        // XML.
        boolean isInsideSystem = false;

        // Create a string to store the System XML.
        String systemXML = "";

        // Create a Scanner object to read the input file.
        Scanner scanner = new Scanner(new File(inputPath));

        // While there are more lines to read...
        while (scanner.hasNextLine()) {

            // Get the next line.
            String line = scanner.nextLine();

            // If the line starts with "<System>", then set the boolean variable to true.
            if (line.startsWith("<System>")) {
                isInsideSystem = true;
            }

            // If the boolean variable is true, then add the line to the string.
            if (isInsideSystem) {
                systemXML += line + '\n';
            }

            // If the line starts with "</System>", then set the boolean variable to false.
            if (line.startsWith("</System>")) {
                isInsideSystem = false;
                break;
            }

        }

        // Close the Scanner object.
        scanner.close();

        // Add the XML declaration to the beginning of the string.
        systemXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + systemXML;

        // Create a new file to store the extracted XML.
        String outputPath = "Parsed.xml";
        File myFile = new File(outputPath);

        // Create the new file.
        myFile.createNewFile();

        // Create a FileWriter object to write the extracted XML to the new file.
        FileWriter myFileWriter = new FileWriter(myFile);

        // Write the extracted XML to the new file.
        myFileWriter.write(systemXML);

        // Close the FileWriter object.
        myFileWriter.close();

        // Return the path to the new file.
        return outputPath;

    }

}