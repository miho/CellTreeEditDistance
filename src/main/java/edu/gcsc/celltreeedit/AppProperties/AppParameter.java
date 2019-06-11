package edu.gcsc.celltreeedit.AppProperties;

enum AppParameter
{
    CALC_TYPE("c", "calc", true, "define what program should do"),
    BASE_DIRECTORY("b", "base", true, "define where the base directory is located"),
    JSON_DIRECTORY("j", "json", true, "define where json file containing filenames is located"),
    SHOW("s", "show", true, "define whether matrix, dendrogram etc are openend in a new window");


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
