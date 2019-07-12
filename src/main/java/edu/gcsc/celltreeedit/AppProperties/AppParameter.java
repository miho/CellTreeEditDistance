package edu.gcsc.celltreeedit.AppProperties;


/**
 * enum that defines commandline options
 */
enum AppParameter {
    CALC_TYPE("c", "calc", true, "defines what program should do"),
    BASE_DIRECTORY("b", "base", true, "defines the base directory of the application"),
    DESTINATION_DIRECTORY("d", "destination", true, "defines the directory where files shall be saved if no base directory is given"),
    JSON_FILE("f", "jsonfile", true, "defines the json-file describing the swc-files used for calculating TED"),
    JSON_NAME("j", "jsonname", true, "defines the name used for naming the swc-file which stores the query-results"),
    MATRIX_NAME("m", "matrixname", true, "defines the name used for naming the matrix-file which stores the TED-results");

    final String shortname;
    final String name;
    final boolean hasArgs;
    final String helptext;

    AppParameter(final String shortname, final String name, final boolean hasArgs, final String helptext) {
        this.shortname = shortname;
        this.name = name;
        this.hasArgs = hasArgs;
        this.helptext = helptext;
    }
}
