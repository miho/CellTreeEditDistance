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
            final CommandLineParser parser = new PosixParser();
            final CommandLine line = parser.parse(allowedOptions, args);

            // check if option is used
            if (hasOption(line, AppParameter.CALC_TYPE)) {
                properties.calcType = extractInt(line, AppParameter.CALC_TYPE);
            }

            validateCommandLineArguments(line, properties.calcType);

            if (hasOption(line, AppParameter.BASE_DIRECTORY)) {
                properties.baseDirectory = extractFile(line, AppParameter.BASE_DIRECTORY);
            }
            if (hasOption(line, AppParameter.JSON_DIRECTORY)) {
                properties.jsonDirectory = extractFile(line, AppParameter.JSON_DIRECTORY);
            }
            if (hasOption(line, AppParameter.SHOW)) {
                properties.show = extractBoolean(line, AppParameter.SHOW);
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

    private static void printHelp(final Options allowedOptions) {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CellTreeEditDistance.jar", allowedOptions);
    }

    private static void validateCommandLineArguments(CommandLine line, int calcType) throws ParseException {
        switch (calcType) {
            case 0:
                break;
            case 1:
                if (hasOption(line, AppParameter.SHOW)) {
                    throw new ParseException("Argument 'show' not allowed for calc=1");
                }
                break;
            case 2:
                if (hasOption(line, AppParameter.BASE_DIRECTORY) || hasOption(line, AppParameter.JSON_DIRECTORY) || hasOption(line, AppParameter.SHOW)) {
                    throw new ParseException("No other arguments allowed for calc=2");
                }
                break;
            case 3:
                if (hasOption(line, AppParameter.JSON_DIRECTORY) || hasOption(line, AppParameter.SHOW)) {
                    throw new ParseException("Combination of arguments not allowed for calc=3");
                }
                break;
            default:
                throw new ParseException("'calc' can only have value 0, 1, 2 or 3");
        }
    }
}
