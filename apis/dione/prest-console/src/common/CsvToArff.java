package common;

import console.PrestConsoleApp;
import org.apache.log4j.Logger;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Allows PREST to use the Weka library in order to convert CSV files into the ARFF format, which is the format
 * used by Weka for its various machine learning algorithms and tools.
 */
public class CsvToArff {
    private static Logger logger = Logger.getLogger(PrestConsoleApp.class.getName());

    /**
     * Converts a CSV file into the ARFF format and saves the ARFF file in the same folder
     *
     * @param csvFile Absolute or relative path to the CSV file
     */
    public static void convert(Path csvFile) {
        CsvToArff.convert(csvFile, csvFile.getParent());
    }

    /**
     * Converts a CSV file into the ARFF format
     *
     * @param csvFile Absolute or relative path to the CSV file
     * @param outputFolder Absolute or relative path to the output folder for the ARFF file
     */
    public static void convert(Path csvFile, Path outputFolder) {
        // error handling for bad file input parameters
        if (csvFile == null) {
            logger.error("First argument of CsvToArff.convert(Path csvFile, Path outputFolder) is null!");
            return;
        } else if (outputFolder == null) {
            logger.error("Second argument of CsvToArff.convert(Path csvFile, Path outputFolder) is null!");
            return;
        }

        // check if parameter is a CSV file
        if (!getFileExtension(csvFile).equals("csv")) {
            logger.error("-convertCsvToArff command only works with CSV files!");
            return;
        }

        // convert the CSV file to an ARFF file
        BufferedWriter writer;
        try {
            CSVLoader loader = new CSVLoader();
            loader.setSource(csvFile.toFile());
            Instances data = loader.getDataSet();

            // the ARFF file should have the same name as the CSV file, but with a different file extension
            String csvFilenameWithoutExtension = getFilenameWithoutExtension(csvFile);

            String arffFilenameWithExtension = csvFilenameWithoutExtension + ".arff";
            Path arffPath = Paths.get(outputFolder.toString(), arffFilenameWithExtension);

            writer = new BufferedWriter(new FileWriter(arffPath.toFile()));
            writer.write(data.toString());
            writer.flush();
            writer.close();
            logger.info(csvFile.toString() + " converted successfully.");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Converts all of the CSV files found in the 'parse_results' folder of a project into the ARFF format, and saves
     * the ARFF files in the 'arff_files' folder.
     *
     * @param projectName Name of the project
     */
    public static void convertProject(String projectName) {
        Path repository = Paths.get(ApplicationProperties.get("repositorylocation"));

        // check if the project has been added to PREST
        Path projectPath = Paths.get(repository.toString(), projectName);
        if (!projectPath.toFile().exists()) {
            logger.error("Error: project hasn't been added to repository!");
            return;
        }

        // iterate through all CSV files found in project path folder
        Path parseFolder = Paths.get(repository.toString(), projectName, "parse_results");
        Path arffFolder = Paths.get(repository.toString(), projectName, "arff_files");

        parseFolder.toFile().mkdirs();    // create the parse_results folder if it doesn't already exist

        for (File file : parseFolder.toFile().listFiles()) {
            if (getFileExtension(file.toPath()).equals("csv")) {
                convert(file.toPath(), arffFolder);
            }
        }
    }

    /**
     * A private helper method for obtaining a file's name without its extension
     *
     * @param file Absolute or relative path to the file
     * @return Name of the file without the file extension
     */
    private static String getFilenameWithoutExtension(Path file) {
        String filename = file.getFileName().toString();
        return filename.substring(0, filename.lastIndexOf("."));
    }

    /**
     * A private helper method for obtaining a file's extension name
     *
     * @param file Absolute or relative path to the file
     * @return Extension of the file (without the period)
     */
    private static String getFileExtension(Path file) {
        String filename = file.getFileName().toString();
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}