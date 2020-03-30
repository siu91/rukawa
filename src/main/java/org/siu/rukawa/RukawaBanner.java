package org.siu.rukawa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;


/**
 * 启动打印banner
 *
 * @Author Siu
 * @Date 2020/3/28 20:53
 * @Version 0.0.1
 */
@Slf4j
public class RukawaBanner {

    private static final String[] BANNER = {
            "",
            " _____ ",
            "| ___ \\    | |",
            "| |_/ /   _| | ____ ___      ____ _",
            "|    / | | | |/ / _` \\ \\ /\\ / / _` |",
            "| |\\ \\ |_| |   < (_| |\\ V  V / (_| |",
            "\\_| \\_\\__,_|_|\\_\\__,_| \\_/\\_/ \\__,_|"};

    private static final String RUKAWA = " :: Rukawa :: ";

    private static final int STRAP_LINE_SIZE = 42;

    public static void printBanner() {
        for (String line : BANNER) {
           log.info(line);
        }
        String version = RukawaVersion.getVersion();
        version = (version != null) ? " (v" + version + ")" : "";
        StringBuilder padding = new StringBuilder();
        while (padding.length() < STRAP_LINE_SIZE - (version.length() + RUKAWA.length())) {
            padding.append(" ");
        }

       log.info(AnsiOutput.toString(AnsiColor.GREEN, RUKAWA, AnsiColor.DEFAULT, padding.toString(),
                AnsiStyle.FAINT, version));
       log.info("");
    }
}
