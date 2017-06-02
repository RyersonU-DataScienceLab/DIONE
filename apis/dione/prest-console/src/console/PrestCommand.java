package console;

import common.ApplicationProperties;
import common.CsvToArff;
import executor.ParserExecutor;
import org.apache.log4j.Logger;
import predictor.WekaRunner;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class contains static methods that can perform some PREST command, which lets the user perform some useful
 * work or task.
 */
public class PrestCommand {
    private static Logger logger = Logger.getLogger(PrestConsoleApp.class.getName());

    /**
     * Tells whether or not a project folder exists inside the repository path on the user's file system.
     *
     * @param projectName Name of the project added by user
     * @return True if project folder exists, false otherwise
     */
    public boolean doesRepositoryExist(String projectName) {
        Path repository = Paths.get(ApplicationProperties.get("repositorylocation"), projectName);
        return repository.toFile().exists();
    }

    /**
     * Lets the user add a project folder to the repository location, for future use with other PREST commands.
     *
     * @param projectPath Absolute or relative path to the project folder
     */
    public void addNewProjectCmd(Path projectPath) {
        String projectName = projectPath.getFileName().toString();

        // check if the project name has already been added to PREST, and create the project folder inside of
        // the repository
        if (!doesRepositoryExist(projectName)) {
            String repositoryLocation = ApplicationProperties.get("repositorylocation");
            Path projectDir = Paths.get(repositoryLocation, projectName);
            Path projectParseResults = Paths.get(repositoryLocation, projectName, "parse_results");
            Path projectArffFiles = Paths.get(repositoryLocation, projectName, "arff_files");

            projectDir.toFile().mkdirs();
            projectParseResults.toFile().mkdirs();
            projectArffFiles.toFile().mkdirs();
        } else {
            // logger.warn("The project you tried to add has already been added!");
        }
    }

    /**
     * Lets the user parse a project folder of source files into CSV containing metrics results
     *
     * @param arg Name of the project added by user
     */
    public void parseManualCmd(String arg) {
        File path = new File(arg);
        String fileName = path.getName();

        parseManualCmd(arg, fileName, "", "", "");
    }

    /**
     * A private helper method for parsing a project
     *
     * @param projectDirectoryStr Absolute or relative path to the project directory containing source files
     * @param freeze              Absolute or relative path inside the repository in which to generate results files
     * @param fileCsvPath         Absolute or relative path inside the repository in which to generate file granularity
     *                            results, or ""/null to use same path as freeze parameter
     * @param methodCsvPath       Absolute or relative path inside the repository in which to generate method granularity
     *                            results, or ""/null to use same path as freeze parameter
     * @param classCsvPath        Absolute or relative path inside the repository in which to generate class granularity
     *                            results, or ""/null to use same path as freeze parameter
     */
    private void parseManualCmd(String projectDirectoryStr, String freeze, String fileCsvPath, String methodCsvPath, String classCsvPath) {
        File projectDirectory = new File(projectDirectoryStr);

        // check to see if the absolute/relative path to the folder exists
        if (!projectDirectory.exists()) {
            logger.error("Error: project path does not exist!");
            return;
        } else if (!projectDirectory.isDirectory()) {
            logger.error("Error: project path is not a directory!");
            return;
        }

        // if the parse_results and arff_files folders in the repository for this project don't exist, create them
        String repository = ApplicationProperties.get("repositorylocation");
        String projectName = projectDirectory.getName().toString();
        Path arff_files_folder = Paths.get(repository, projectName, "parse_results");
        Path parse_results_folder = Paths.get(repository, projectName, "arff_files");

        arff_files_folder.toFile().mkdirs();
        parse_results_folder.toFile().mkdirs();

        // perform parsing
        boolean result = ParserExecutor.parseDirectoryCmd(projectDirectory, fileCsvPath, methodCsvPath, classCsvPath, freeze);
        if (result == true) {
            // logger.info("Project parsed successfully.");
        } else {
            logger.error("There was an error while parsing the project.");
        }
    }

    /**
     * Lets the user convert a specific CSV file to ARFF format by specifying its file path
     *
     * @param fileName Absolute or relative path to the CSV file
     */
    public void convertCsvToArff(Path fileName) {
        try {
            CsvToArff.convert(fileName);
        } catch (Exception eCtoArff) {
            logger.error("CSV file name wrong or file corrupt!");
        }
    }

    /**
     * Lets the user convert all of the CSV files generated for a project into ARFF format
     *
     * @param projectName Name of the project as added by user
     */
    public void convertProjectCsvToArff(String projectName) {
        CsvToArff.convertProject(projectName);
    }

    /**
     * Applies log filtering algorithm to an ARFF file.
     *
     * @param filePath Absolute or relative path to the ARFF file
     * @return True if successful, false otherwise
     */
    public boolean logFiltering(String filePath) {
        ArffLoader loader = new ArffLoader();
        Instances data;
        BufferedWriter writer = null;
        boolean methodLevel = false;
        try {
            // logger.info(filePath);
            loader.setSource(new File(filePath));
            data = loader.getDataSet();
            data.setClassIndex(data.numAttributes() - 1);
            if (data.attribute(2).name().equals("Method Name"))
                methodLevel = true;

            Instances newdata = new Instances(data, data.numInstances());

            for (int j = 0; j < data.numAttributes(); j++) {
                for (int i = 0; i < data.numInstances(); i++) {
                    if (j == 0) //Filename
                    {
                        newdata.add(data.instance(i));
                        newdata.instance(i).setValue(j, data.instance(i).stringValue(j));
                    } else if (j == 1) //File ID
                        newdata.instance(i).setValue(j, data.instance(i).value(j));
                    else if (j == data.numAttributes() - 1) //Class attribute
                        newdata.instance(i).setClassValue(data.instance(i).stringValue(j));
                    else {
                        if (methodLevel) {
                            if (j != 2 && j != 3) {
                                if (data.instance(i).value(j) != 0)
                                    newdata.instance(i).setValue(j, Math.log(data.instance(i).value(j)));
                                else
                                    newdata.instance(i).setValue(j, Math.log(0.0001));
                            }
                        } else {
                            if (data.instance(i).value(j) != 0)
                                newdata.instance(i).setValue(j, Math.log(data.instance(i).value(j)));
                            else
                                newdata.instance(i).setValue(j, Math.log(0.0001));
                        }
                    }
                }
            }

            // logger.info("Writing to new file...");
            String outFile = filePath.substring(0, filePath.lastIndexOf("."));
            // loggerfo(outFile);
            writer = new BufferedWriter(new FileWriter(outFile + "_LF.arff"));

            writer.write(newdata.toString());
            writer.flush();
            writer.close();
            // logger.info("Log filter is applied to " + filePath + " successfully");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("Log filter could not be processed due to IOException");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Lets the user perform defect prediction on a pair of training and testing ARFF files.
     *
     * @param trainFile  Absolute or relative path to the training ARFF file
     * @param testFile   Absolute or relative path to the testing ARFF file
     * @param resultPath The absolute or relative path of where the output files should
     *                   be generated
     * @param fileFilter What filter algorithm to apply
     * @param changeDiff Number of change differences
     */
    public void predict(String trainFile, String testFile, String resultPath, String fileFilter, String changeDiff) {
        int diffSpan;
        try {
            diffSpan = Integer.parseInt(changeDiff);
        } catch (Exception e) {
            diffSpan = 5;
        }
        WekaRunner.performDefectPrediction(trainFile, testFile, "Naive Bayes", "none", "no", "no", resultPath, fileFilter, diffSpan);
    }

    /**
     * Sets the location of the user's repository folder, which is where all output files generated will be located.
     * The repository location can be obtained using <pre><code>ApplicationProperties.get("repositorylocation")</code></pre>
     *
     * @param repoPath Absolute or relative path to the repository folder.
     */
    public void setRepoCmd(String repoPath) {
        ApplicationProperties.setRepositoryLocation(null, repoPath);
    }
}
