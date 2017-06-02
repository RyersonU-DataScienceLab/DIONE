package executor;

import common.ApplicationProperties;
import console.PrestConsoleApp;
import org.apache.log4j.Logger;
import parser.C.CParser;
import parser.Cpp.cppParser.CPPParserExecutor;
import parser.Java.JavaParserRelatedFiles.JavaParser;
import parser.PLSql.PLSqlParserRelatedFiles.PLSqlParserExecuter;
import parser.enumeration.Language;
import parser.parserinterface.IParser;
import parser.parserinterface.ParserInterfaceAndFileList;
import parser.Java.MetricsRelatedFiles.ClassContainerExt;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Contains the logic for the parsing and metrics extraction of source code files, typically for an entire project.
 */
public class ParserExecutor {
    private static Logger logger = Logger.getLogger(PrestConsoleApp.class.getName());

    /**
     * Performs metrics extraction on a folder containing source code files and the output results files in four
     * granularities: package, file, class, and method.
     *
     * @param projectDirectory Absolute or relative path to the project directory containing source code files
     * @param fileCsvPath Absolute or relative path for file output for file granularity
     * @param methodCsvPath Absolute or relative path for file output for method granularity
     * @param classCsvPath Absolute or relative path for file output for class granularity
     * @param freezeName Name of the project
     * @return True if successful, false otherwise
     */
    public static boolean parseDirectoryCmd(File projectDirectory, String fileCsvPath, String methodCsvPath,
                                            String classCsvPath, String freezeName)
    {
        List<ParserInterfaceAndFileList> parserList;
        parserList = findAppropriateParsers(projectDirectory);

        //if freeze parameter provided project name set to freeze name
        String projDir = "";
        if (freezeName.equals(""))
            projDir = projectDirectory.getName();
        else
            projDir = freezeName;

        if (parserList == null) {
            return false;
        } else {
            for (ParserInterfaceAndFileList parserAndFiles : parserList) {
                parseProject(parserAndFiles.getParser(), parserAndFiles.getFileList(), projDir, fileCsvPath, methodCsvPath, classCsvPath);
            }
            return true;
        }
    }

    /**
     * Determines a list of all the languages used in a folder by looking at their file extensions, and returns
     * them along with the name of every file found recursively.
     *
     * @param projectDirectory Absolute or relative path to the project folder
     * @return A list containing all languages used and names of every file
     */
    private static List<ParserInterfaceAndFileList> findAppropriateParsers(File projectDirectory) {
        List<ParserInterfaceAndFileList> parserList = new ArrayList<ParserInterfaceAndFileList>();

        for (Language lang : Language.LIST) {
            List<File> fileList = new ArrayList<File>();
            for (String l : lang.getExtensions()) {
                List<File> filesVisited = listChildrenRecursively(projectDirectory, l,new ArrayList<>());

                fileList.addAll(filesVisited);
            }

            if (fileList != null && fileList.size() > 0) {
                IParser parser = constructParser(lang);
                if (parser != null) {
                    parser.setLanguage(lang);
                    ParserInterfaceAndFileList pf = new ParserInterfaceAndFileList();
                    pf.setParser(parser);
                    pf.setFileList(fileList);
                    parserList.add(pf);
                }
            }
        }
        return parserList;
    }

    /**
     * Returns an appropriate IParser object that can parse a specified language
     *
     * @param lang Language for which the appropriate parser should be returned
     * @return The parser
     */
    private static IParser constructParser(Language lang) {
        if (lang.equals(Language.JAVA)) {

            return new JavaParser(System.in);
        } else if (lang.equals(Language.C)) {
            return new CParser();
        } else if (lang.equals(Language.CPP)) {
            return new CPPParserExecutor();
        } else if (lang.equals(Language.PLSQL)) {
            return new PLSqlParserExecuter();
        } else {
            return null;
        }
    }

    /**
     * Performs metrics extraction on a list of files
     *
     * @param aParser Parser to use on the project
     * @param fileList List of all files in the project
     * @param projectName Name of the project
     * @param fileCsvPath Absolute or relative path to the output file for file granularity
     * @param methodCsvPath Absolute or relative path to the output file for method granularity
     * @param classCsvPath Absolute or relative path to the output file for class granularity
     */
    public static void parseProject(IParser aParser, List<File> fileList, String projectName, String fileCsvPath,
                                           String methodCsvPath, String classCsvPath)
    {
        if (aParser != null && fileList != null) {
            String[] fileNames = new String[fileList.size()];
            for (int index = 0; index < fileList.size(); index++) {
                fileNames[index] = fileList.get(index).getAbsolutePath();
              }

            try {
                Date now = new Date();
                String tempFileName = fileNames[0];
                String javaFileName =tempFileName.substring(tempFileName.lastIndexOf(projectName)+projectName.length()+1,tempFileName.length());
                javaFileName = javaFileName.replaceAll("."+aParser.getLanguage().getLangName().toLowerCase(),"");
                DateFormat df = DateFormat.getDateTimeInstance();
                String nowStr = df.format(now);
                nowStr = nowStr.replaceAll(" ", "-");
                nowStr = nowStr.replaceAll(":", ".");
                String abcFilePath = ApplicationProperties.get("repositorylocation") + projectName
                        + File.separator + "parse_results" + File.separator + javaFileName +nowStr+ File.separator;
                File abcFile = new File(abcFilePath);
                if(!abcFile.exists())
                {
                  abcFile.mkdirs();
                }
                String xmlFileName = abcFilePath + "parseResult" + "_"
                        + aParser.getLanguage().getLangName() + "_" + ".xml";
                String packageCsvFileName = abcFilePath + "parseResult" + "_"
                        + aParser.getLanguage().getLangName() + "_" + "PACKAGE.csv";

                String methodCsvFileName = "";
                if (methodCsvPath.equals("")) {
                    methodCsvFileName = abcFilePath + "parseResult" + "_"
                            + aParser.getLanguage().getLangName() + "_" + "METHOD.csv";
                } else {
                    methodCsvFileName = methodCsvPath;
                }

                String fileCsvFileName = "";

                if (fileCsvPath.equals("")) {
                    fileCsvFileName = abcFilePath + "parseResult" + "_"
                            + aParser.getLanguage().getLangName() + "_" + "FILE.csv";
                } else {
                    fileCsvFileName = fileCsvPath;
                }

                String classCsvFileName = "";

                if (classCsvPath.equals("")) {
                    classCsvFileName = abcFilePath + "parseResult" + "_"
                            + aParser.getLanguage().getLangName() + "_" + "CLASS.csv";
                } else {
                    classCsvFileName = classCsvPath;
                }

                aParser.startExecution(fileNames, projectName, xmlFileName, packageCsvFileName, fileCsvFileName,
                        classCsvFileName, methodCsvFileName);
                ClassContainerExt.writeToFileAsJson(packageCsvFileName, fileCsvFileName,
                        classCsvFileName, methodCsvFileName);
            } catch (Exception e) {
                logger.error("Error while writing to files.");
            }

        }
    }

    /**
     * Lists all files found recursively in a folder directory, excluding files that have the specified file
     * extension
     *
     * @param dir Absolute or relative path to folder directory
     * @param extensionToFilter Files which have this extension will not be listed
     * @return A list of all the files found in this folder, filtered
     */
    private static List<File> listChildrenRecursively(File dir, String extensionToFilter,List<File> results) {

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                listChildrenRecursively(new File(dir, children[i]), extensionToFilter,results);
            }
        } else {
            if (dir.getName().endsWith(extensionToFilter) && !results.contains(dir)) {
                results.add(dir);
            }
        }
        return results;
    }
}
