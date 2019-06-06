package edu.gcsc.celltreeedit;

enum AppParameter
{
    CALC_SHORT("c="), CALC("calc="),
    JSON_SHORT("j="), JSON("json="),
    SHOW_SHORT("s="), SHOW("show=");

    final String paramName;
    final int valueOffset;

    AppParameter(final String name)
    {
        this.paramName = "-" + name;
        this.valueOffset = paramName.length();
    }
}
