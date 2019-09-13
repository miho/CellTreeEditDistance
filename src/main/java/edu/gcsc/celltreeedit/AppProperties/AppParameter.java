package edu.gcsc.celltreeedit.AppProperties;


/**
 * enum that defines commandline options
 */
enum AppParameter {
    CALC_TYPE("c", "calc", true, "defines what program should do."),
    BASE_DIRECTORY("b", "base", true, "defines the base-directory of the application."),
    FILE_INPUT("f", "fileInput", true, "defines the file used for calculation (can be a json-file containing the swc-files used for TED-calculation or a txt-file containing the distance-matrix for dendrogram-calculation."),
    NAME_OUTPUT("n", "nameOutput", true, "defines the basic name used for naming the output. appendices might be added. no fileextension needed."),
    LABEL("l", "label", true, "defines the label used for TED-Calculation."),
    RENAME_DENDROGRAM("r", "renameDendrogram", false, "if used, filenames in dendrogram are replaced by a combination of neuronMetadata."),
    SAVE_OUTPUT("s", "saveOutput", false, "if used, results are saved to output-directory (might include distance-matrix, dendrogram, filemapping).");

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
