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

/**
 * enum that defines commandline options
 */
enum AppParameter {
    CALC_TYPE("c", "calc", true, "defines what program should do."),
    BASE_DIRECTORY("b", "base", true, "defines the base-directory of the application."),
    FILE_INPUT("f", "fileInput", true, "defines the file used for calculation (can be a json-file containing the swc-files used for TED-calculation or a txt-file containing the distance-matrix for dendrogram-calculation."),
    NAME_OUTPUT("n", "nameOutput", true, "defines the basic name used for naming the output. appendices might be added. no fileextension needed."),
    LABEL("l", "label", true, "defines the label used for TED-Calculation."),
    RENAME_DENDROGRAM("e", "rEnameDendrogram", false, "if used, filenames in dendrogram are replaced by a combination of neuronMetadata."),
    SAVE_OUTPUT("s", "saveOutput", false, "if used, results are saved to output-directory (might include distance-matrix, dendrogram, filemapping)."),
    ROWS("r", "rowsPerCall", true, "used for TED-Calculation on a Cluster. Defines the number of matrix-rows for which TED is calculated per call. To guarantee division in same problem sizes only about half of the columns are calculated."),
    ITERATION("i", "iteration", true, "used for TED-Calculation on a Cluster. Defines the actual iteration of a TED-Calculation divided for multiple cluster-nodes."),
    DIRECTORY_INPUT("d", "directoryInput", true, "defines the directory that contains the clustermatrices to be reassembled to complete distance-matrix.");

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
