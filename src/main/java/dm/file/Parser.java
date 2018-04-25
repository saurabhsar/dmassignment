package dm.file;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parser {

    private static boolean isHeaderWritten = false;
    private static String processedFileName = "processedFile.csv";

    private static Instances readCSVFile(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);

        CSVLoader csvLoader = new CSVLoader();
        csvLoader.setSource(inputStream);

        return csvLoader.getDataSet();
    }

    /**
     * This method would make sure that readCSVFile doesn't break
     * @param filePath File source
     */
    private static void preProcess(String filePath, boolean append) throws IOException {
        CSVParser csvParser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT);

        BufferedWriter writer = new BufferedWriter(new FileWriter(processedFileName, append));

        StringBuilder toWrite;

        for (CSVRecord csvRecord : csvParser.getRecords()) {
            toWrite = new StringBuilder();
            if (csvRecord.get(csvRecord.size() - 21).equals("wmc")) {
                continue;
            }

            if (!isHeaderWritten) {
                String header = "wmc,dit,noc,cbo,rfc,lcom,ca,ce,npm,lcom3,loc,dam,moa,mfa,cam,ic,cbm,amc,max_cc,avg_cc,bug\n";
                toWrite.append(header);
                isHeaderWritten = true;
            }

            /*
             * wmc, dit, noc, cbo, rfc, lcom, ca, ce, npm, lcom3, loc, dam, moa, mfa, cam, ic, cbm, amc, max_cc, avg_cc
             * Total I/P : 20
             *
             * O/P : 1
             */

            for (int i = csvRecord.size() - 21 ; i < csvRecord.size(); i++) {
                if (i != csvRecord.size() -1) {
                    toWrite.append(csvRecord.get(i));
                    toWrite.append(",");
                }
                if (i == csvRecord.size() - 1 && csvRecord.get(i).equals("0")) {
                    toWrite.append("0");
                } else if (i == csvRecord.size() - 1) {
                    toWrite.append("1");
                }
            }

            toWrite.append("\n");
            writer.write(toWrite.toString());
        }
        writer.close();

    }

    public static List<String> getFileList(String basePath) {
        List<String> retval = new ArrayList<>();
        File directory = new File(basePath);
        for (File file : Objects.requireNonNull(directory.listFiles())){
            if (file.getPath().contains(".csv")) {
                retval.add(file.getPath());
            }
        }

        return retval;
    }

    private static Instances generateInstancesAndDelete(String processedFileName) throws IOException {
        Instances instances = readCSVFile(processedFileName);

        File file = new File(processedFileName);
        file.delete();

        return instances;

    }

    public static Instances processAndFetchInstances(String fileName) throws IOException {
        preProcess(fileName, false);
        isHeaderWritten = false;

        return generateInstancesAndDelete(processedFileName);
    }

    public static Instances processAsLumpsum(List<String> files) throws IOException {
        for (String fileName : files) {
            preProcess(fileName, true);
        }

        return generateInstancesAndDelete(processedFileName);
    }
}
