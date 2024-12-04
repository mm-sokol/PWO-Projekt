package org.pwo;

import org.pwo.utils.PointUtils;

public class Generate {
    public static void main(String[] args) {
        int version = 6;
        int problemSize = 60;
        String filename = "src/main/resources/problem"+version+".point2d";
        PointUtils.writeList(filename, problemSize);
    }
}
