package com.apporiented.algorithm.clustering.visualization;

import java.awt.*;
import java.util.regex.Pattern;

public class ClusterColorRegex {
    Pattern regex;
    Color color;

    public ClusterColorRegex(Pattern regex, Color color) {
        this.regex = regex;
        this.color = color;
    }
}
