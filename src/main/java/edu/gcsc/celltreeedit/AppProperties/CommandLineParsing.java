package edu.gcsc.celltreeedit.AppProperties;


import org.apache.commons.cli.*;
import java.io.File;

/**
 * parses Arguments passed to application on start
 */
public class CommandLineParsing {

    public static AppProperties parseArguments(final String[] args) {
        final Options allowedOptions = new Options();
        // Add all options defined in AppParameter to Options-Object
        for (final AppParameter parameter : AppParameter.values()) {
            allowedOptions.addOption(parameter.shortname, parameter.name,
                    parameter.hasArgs, parameter.helptext);
        }

        AppProperties properties = AppProperties.getInstance();

        try {
            // parse CommandLineArguments
            final CommandLineParser parser = new DefaultParser(); // new PosixParser();
            final CommandLine line = parser.parse(allowedOptions, args);

            // check if option is used
            if (hasOption(line, AppParameter.CALC_TYPE)) {
                properties.calcType = extractInt(line, AppParameter.CALC_TYPE);
            }

            validateCommandLineArguments(line, properties.calcType);

            if (hasOption(line, AppParameter.BASE_DIRECTORY)) {
                properties.baseDirectory = extractFile(line, AppParameter.BASE_DIRECTORY);
            }
            if (hasOption(line, AppParameter.DESTINATION_DIRECTORY)) {
                properties.destinationDirectory = extractFile(line, AppParameter.DESTINATION_DIRECTORY);
            }
            if (hasOption(line, AppParameter.JSON_FILE)) {
                properties.jsonFile = extractFile(line, AppParameter.JSON_FILE);
            }
            if (hasOption(line, AppParameter.JSON_NAME)) {
                properties.jsonName = extractString(line, AppParameter.JSON_NAME);
            }
            if (hasOption(line, AppParameter.MATRIX_NAME)) {
                properties.matrixName = extractString(line, AppParameter.MATRIX_NAME);
            }
        } catch (final ParseException exp) {
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

    private static String extractString(final CommandLine line, final AppParameter parameter) {
        return line.getOptionValue(parameter.name);
    }

    private static void printHelp(final Options allowedOptions) {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CellTreeEditDistance.jar", allowedOptions);
    }

    private static void validateCommandLineArguments(CommandLine line, int calcType) throws ParseException {
        switch (calcType) {
            case 0:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY) || hasOption(line, AppParameter.DESTINATION_DIRECTORY) || hasOption(line, AppParameter.JSON_FILE) || hasOption(line, AppParameter.JSON_NAME) || hasOption(line, AppParameter.MATRIX_NAME)) {
                    throw new ParseException("calc=0: Argument 'base' needed. All others not allowed.");
                }
            case 1:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY) || hasOption(line, AppParameter.DESTINATION_DIRECTORY) || hasOption(line, AppParameter.JSON_FILE) || hasOption(line, AppParameter.MATRIX_NAME)) {
                    throw new ParseException("calc=1: Argument 'base' needed. Argument 'jsonname' optional. All others not allowed.");
                }
                break;
            case 2:
                if (!hasOption(line, AppParameter.DESTINATION_DIRECTORY) || hasOption(line, AppParameter.BASE_DIRECTORY) || hasOption(line, AppParameter.JSON_FILE) || hasOption(line, AppParameter.MATRIX_NAME)) {
                    throw new ParseException("calc=2: Argument 'destination' needed. Argument 'jsonname' optional. All others not allowed.");
                }
                break;
            case 3:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY) || hasOption(line, AppParameter.DESTINATION_DIRECTORY) || hasOption(line, AppParameter.JSON_FILE) || hasOption(line, AppParameter.MATRIX_NAME)) {
                    throw new ParseException("calc=3: Argument 'base' needed. Argument 'jsonname' optional. All others not allowed.");
                }
                break;
            case 4:
                if (!hasOption(line, AppParameter.JSON_FILE) || hasOption(line, AppParameter.DESTINATION_DIRECTORY) || hasOption(line, AppParameter.BASE_DIRECTORY) || hasOption(line, AppParameter.JSON_NAME) || hasOption(line, AppParameter.MATRIX_NAME)) {
                    throw new ParseException("calc=4: Argument 'jsonfile' needed. Argument 'matrixname' optional. All others not allowed.");
                }
                break;
            case 5:
                if (!hasOption(line, AppParameter.JSON_FILE) || hasOption(line, AppParameter.DESTINATION_DIRECTORY) || hasOption(line, AppParameter.BASE_DIRECTORY) || hasOption(line, AppParameter.JSON_NAME) || hasOption(line, AppParameter.MATRIX_NAME)) {
                    throw new ParseException("calc=5: Argument 'jsonfile' needed. Argument 'matrixname' optional. All others not allowed.");
                }
                break;
            case 6:
                if (hasOption(line, AppParameter.BASE_DIRECTORY) || hasOption(line, AppParameter.DESTINATION_DIRECTORY) || hasOption(line, AppParameter.JSON_FILE) || hasOption(line, AppParameter.JSON_NAME) || hasOption(line, AppParameter.MATRIX_NAME)) {
                    throw new ParseException("calc=6: No other arguments allowed.");
                }
                break;
            case 7:
                if (hasOption(line, AppParameter.BASE_DIRECTORY) || hasOption(line, AppParameter.DESTINATION_DIRECTORY) || hasOption(line, AppParameter.JSON_FILE) || hasOption(line, AppParameter.JSON_NAME) || hasOption(line, AppParameter.MATRIX_NAME)) {
                    throw new ParseException("calc=7: No other arguments allowed.");
                }
                break;
            case 8:
                if (hasOption(line, AppParameter.BASE_DIRECTORY) || hasOption(line, AppParameter.DESTINATION_DIRECTORY) || hasOption(line, AppParameter.JSON_FILE) || hasOption(line, AppParameter.JSON_NAME) || hasOption(line, AppParameter.MATRIX_NAME)) {
                    throw new ParseException("calc=8: No other arguments allowed.");
                }
                break;
            default:
                throw new ParseException("'calc' can only have value 0, 1, 2 or 3");
        }
    }
}
