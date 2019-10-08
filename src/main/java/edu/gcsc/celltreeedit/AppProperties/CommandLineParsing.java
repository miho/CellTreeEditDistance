/*
 * Copyright 2018-2019 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 * Copyright 2018-2019 Goethe Center for Scientific Computing, University Frankfurt. All rights reserved.
 * Copyright 2018 Erid Guga. All rights reserved.
 * Copyright 2019 Lukas Maurer. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */
package edu.gcsc.celltreeedit.AppProperties;


import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

/**
 * parses Arguments passed to application on start
 */
public class CommandLineParsing {

    private static final AppProperties properties = AppProperties.getInstance();

    public static void parseArguments(final String[] args) throws ParseException {
        final Options allowedOptions = new Options();
        // Add all options defined in AppParameter to Options-Object
        for (final AppParameter parameter : AppParameter.values()) {
            allowedOptions.addOption(parameter.shortname, parameter.name,
                    parameter.hasArgs, parameter.helptext);
        }

        try {
            // parse CommandLineArguments
            final CommandLineParser parser = new DefaultParser(); // new PosixParser();
            final CommandLine line = parser.parse(allowedOptions, args);

            // check if option is used
            if (hasOption(line, AppParameter.CALC_TYPE)) {
                properties.calcType = extractInt(line, AppParameter.CALC_TYPE);
            } else {
                throw new ParseException("Argument 'calc' needed.");
            }

            validateCommandLineArguments(line, properties.calcType);

            if (hasOption(line, AppParameter.BASE_DIRECTORY)) {
                properties.baseDirectory = extractFile(line, AppParameter.BASE_DIRECTORY);
                setDirectoriesAccordingToBaseDirectory(properties.baseDirectory.getAbsolutePath());
            }
            if (hasOption(line, AppParameter.FILE_INPUT)) {
                properties.fileInput = extractFile(line, AppParameter.FILE_INPUT);
            }
            if (hasOption(line, AppParameter.DIRECTORY_INPUT)) {
                properties.directoryInput = extractFile(line, AppParameter.DIRECTORY_INPUT);
            }
            if (hasOption(line, AppParameter.NAME_OUTPUT)) {
                properties.nameOutput = FilenameUtils.removeExtension(extractString(line, AppParameter.NAME_OUTPUT));
            }
            if (hasOption(line, AppParameter.LABEL)) {
                properties.label = extractInt(line, AppParameter.LABEL);
            }
            if (hasOption(line, AppParameter.ROWS)) {
                properties.rows = extractInt(line, AppParameter.ROWS);
            }
            if (hasOption(line, AppParameter.ITERATION)) {
                properties.iteration = extractInt(line, AppParameter.ITERATION);
            }
            if (hasOption(line, AppParameter.RENAME_DENDROGRAM)) {
                properties.renameDendrogram = true;
            }
            if (hasOption(line, AppParameter.SAVE_OUTPUT)) {
                properties.saveOutput = true;
            }
        } catch (final ParseException exp) {
            printHelp(allowedOptions);
            System.out.flush();
            System.err.flush();
            throw new RuntimeException(exp);
        }
    }

    private static void setDirectoriesAccordingToBaseDirectory(String baseDirectory) {
        properties.metadataDirectory = new File(baseDirectory + "/Data/Metadata");
        properties.swcFileDirectory = new File(baseDirectory + "/Data/SWCFiles");
        properties.workingDirectory = new File(baseDirectory + "/WorkingDir");
        properties.outputDirectory = new File(baseDirectory + "/Output");
    }

    private static boolean hasOption(final CommandLine line, final AppParameter parameter) {
        return line.hasOption(parameter.name);
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
                if (!hasOption(line, AppParameter.BASE_DIRECTORY)) {
                    throw new ParseException("calc=0: Argument 'base' needed. All others will be ignored.");
                }
            case 1:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY)) {
                    throw new ParseException("calc=1: Argument 'base' needed. Argument 'nameOutput' optional. All others will be ignored.");
                }
                break;
            case 2:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY)) {
                    throw new ParseException("calc=2: Argument 'base' needed. Argument 'nameOutput' optional. All others will be ignored.");
                }
                break;
            case 3:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY) || !hasOption(line, AppParameter.FILE_INPUT)) {
                    throw new ParseException("calc=3: Argument 'base' and 'fileInput' needed. Argument 'label' optional. All others will be ignored.");
                }
                break;
            case 4:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY) || !hasOption(line, AppParameter.FILE_INPUT)) {
                    throw new ParseException("calc=4: Argument 'base' and 'fileInput' needed. Arguments 'label', 'save', 'renameDendrogram' optional. All others will be ignored.");
                }
                break;
            case 5:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY) || !hasOption(line, AppParameter.FILE_INPUT)) {
                    throw new ParseException("calc=5: Argument 'base' and 'fileInput' needed. Arguments 'save', 'renameDendrogram' optional. All others will be ignored.");
                }
                break;
            case 6:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY)) {
                    throw new ParseException("calc=6: Argument 'base' needed. All others will be ignored.");
                }
                break;
            case 7:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY)) {
                    throw new ParseException("calc=7: Argument 'base' needed. Arguments 'save', 'renameDendrogram' optional. All others will be ignored.");
                }
                break;
            case 8:
                break;
            case 9:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY)) {
                    throw new ParseException("calc=9: Argument 'base' needed. All others will be ignored.");
                }
                break;
            case 10:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY)) {
                    throw new ParseException("calc=10: Argument 'base' needed. All others will be ignored.");
                }
                break;
            case 11:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY)) {
                    throw new ParseException("calc=11: Argument 'base' needed. All others will be ignored.");
                }
                break;
            case 12:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY) || !hasOption(line, AppParameter.FILE_INPUT) || !hasOption(line, AppParameter.ROWS) || !hasOption(line, AppParameter.ITERATION)) {
                    throw new ParseException("calc=12: Argument 'base', 'fileInput', 'rows' and 'iteration' needed. All others will be ignored.");
                }
                break;
            case 13:
                if (!hasOption(line, AppParameter.BASE_DIRECTORY) || !hasOption(line, AppParameter.FILE_INPUT) || !hasOption(line, AppParameter.DIRECTORY_INPUT)) {
                    throw new ParseException("calc=13: Argument 'base', 'fileInput' and 'directoryInput' needed. All others will be ignored.");
                }
                break;
            default:
                throw new ParseException("'calc' can only have value between 0 and 13");
        }
    }
}
