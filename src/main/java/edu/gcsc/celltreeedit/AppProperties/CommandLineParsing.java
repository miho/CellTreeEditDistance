package edu.gcsc.celltreeedit.AppProperties;


import org.apache.commons.cli.*;
import java.io.File;

public class CommandLineParsing {

    public static AppProperties parseArguments(final String[] args) {
        final Options allowedOptions = new Options();
        // Alle Options basierend auf enum AppParameter hinzufügen
        for (final AppParameter parameter : AppParameter.values()) {
            allowedOptions.addOption(parameter.shortname, parameter.name,
                    parameter.hasArgs, parameter.helptext);
        }

        // Test mit festdefinierten Werten
        final String[] sampleArgs = new String[] { "-c", "1", "-j", "swcFile.json",
                "--show", "false", "-b", "/media/rooooooooot" };
        final AppProperties properties = new AppProperties();
        try
        {
            // Kommandozeilenparameter parsen
            final CommandLineParser parser = new PosixParser();
            final CommandLine line = parser.parse(allowedOptions, sampleArgs);

            // Prüfen der Optionen
            if (hasOption(line, AppParameter.CALC_TYPE)) {
                properties.calcType = extractInt(line, AppParameter.CALC_TYPE);
            }
            if (hasOption(line, AppParameter.BASE_DIRECTORY)) {
                properties.baseDirectory = extractFile(line, AppParameter.BASE_DIRECTORY);
            }
            if (hasOption(line, AppParameter.JSON_DIRECTORY)) {
                properties.jsonDirectory = extractFile(line, AppParameter.JSON_DIRECTORY);
            }
            if (hasOption(line, AppParameter.SHOW)) {
                properties.show = extractBoolean(line, AppParameter.SHOW);
            }
        }
        catch (final ParseException exp)
        {
            printHelp(allowedOptions);
        }
        return properties;
    }
    private static boolean hasOption(final CommandLine line, final AppParameter parameter) {
        return line.hasOption(parameter.name);
    }
    private static boolean extractBoolean(final CommandLine line, final AppParameter parameter) {
        return Boolean.parseBoolean(line.getOptionValue(parameter.name));
    }
    private static int extractInt(final CommandLine line, final AppParameter parameter) {
        return Integer.parseInt(line.getOptionValue(parameter.name));
    }
    private static File extractFile(final CommandLine line, final AppParameter parameter) {
        return new File(line.getOptionValue(parameter.name));
    }
    private static void printHelp(final Options allowedOptions)
    {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CellTreeEditDistance.jar", allowedOptions);
    }

        // calcType
        // 0 (default) -> calculate everything
        // 1 -> only calc matrix with minimum resources
        // 2 -> choose for json-file
        // 3 -> preprocess swc-files
}
