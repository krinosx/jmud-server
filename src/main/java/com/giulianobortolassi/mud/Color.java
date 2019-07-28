package com.giulianobortolassi.mud;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Color - Define color codes to use with text output.
 */
public enum Color {

    RESET("text_reset","&tn", "[0;0m"), 
    FG_RESET("fg_reset", "&fn", "[0m"), 
    
    FG_BLACK("fg_black","&fb","[0;30m"),
    // Red
    FG_DARK_RED("fg_dark_red", "&fR","[0;31m"),
    FG_LIGHT_RED("fg_light_red", "&fr","[1;31m"),
    // Green
    FG_DARK_GREEN("fg_dark_green", "&fG","[0;32m"),
    FG_LIGHT_GREEN("fg_light_green", "&fg","[1;32m"),
    // Blue
    FG_DARK_BLUE("fg_dark_blue", "&fB","[0;34m"),
    FG_LIGHT_BLUE("fg_light_blue", "&fb","[1;34m"),
    // White
    FG_DARK_WHITE("fg_dark_white", "&fW","[0;37m"),
    FG_LIGHT_WHITE("fg_light_white", "&fw","[1;37m");   

    private String name;
    private String symbol;
    private String colorDefinition;
    private byte[] telnetCode;
    private static HashMap<String, Color> symbolIndex = new HashMap<>();
    private static final byte ESC = 27;

    private Color(String name, String symbol, String colorDefinition){
        this.name = name;
        this.symbol = symbol;
        this.colorDefinition = colorDefinition;
        
        byte[] colorBytes = colorDefinition.getBytes();
        ByteBuffer escapeBytes = ByteBuffer.allocate(colorBytes.length + 1 );
        escapeBytes.put(ESC).put(colorBytes);
        escapeBytes.flip();
        telnetCode = escapeBytes.array();
    }

    static {
        symbolIndex.put(RESET.symbol, RESET);
        symbolIndex.put(FG_RESET.symbol, FG_RESET);
        symbolIndex.put(FG_BLACK.symbol, FG_BLACK);
        symbolIndex.put(FG_DARK_RED.symbol, FG_DARK_RED);
        symbolIndex.put(FG_LIGHT_RED.symbol, FG_LIGHT_RED);
        symbolIndex.put(FG_DARK_GREEN.symbol, FG_DARK_GREEN);
        symbolIndex.put(FG_LIGHT_GREEN.symbol, FG_LIGHT_GREEN);
        symbolIndex.put(FG_DARK_BLUE.symbol, FG_DARK_BLUE);
        symbolIndex.put(FG_LIGHT_BLUE.symbol, FG_LIGHT_BLUE);
        symbolIndex.put(FG_DARK_WHITE.symbol, FG_DARK_WHITE);
        symbolIndex.put(FG_LIGHT_WHITE.symbol, FG_LIGHT_WHITE);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return the telnetCode
     */
    public byte[] getTelnetCode() {
        return telnetCode;
    }

    public static Color findByCode(String colorCode){
        return symbolIndex.get(colorCode);
    }

    /**
     * @return the colorDefinition
     */
    public String getColorDefinition() {
        return colorDefinition;
    }

    // TODO: Map all colors
    // #define CNRM  "\x1B[0;0m"     /* "Normal"                            */ 
    // #define CNUL  ""              /* No Change                           */ 
    // #define KNRM  "\x1B[0m"       /* Foreground "Normal"                 */ 
    // #define KBLK  "\x1b[0;30m"    /* Foreground Black                    */ 
    // #define KRED  "\x1B[0;31m"    /* Foreground Dark Red                 */ 
    // #define KGRN  "\x1B[0;32m"    /* Foreground Dark Green               */ 
    // #define KYEL  "\x1B[0;33m"    /* Foreground Dark Yellow              */ 
    // #define KBLU  "\x1B[0;34m"    /* Foreground Dark Blue                */ 
    // #define KMAG  "\x1B[0;35m"    /* Foreground Dark Magenta             */ 
    // #define KCYN  "\x1B[0;36m"    /* Foreground Dark Cyan                */ 
    // #define KWHT  "\x1B[0;37m"    /* Foreground Dark White (Light Gray)  */ 
    // #define KNUL  ""              /* Foreground No Change                */ 
    // #define BBLK  "\x1B[1;30m"    /* Foreground Bright Black (Dark Gray) */ 
    // #define BRED  "\x1B[1;31m"    /* Foreground Bright Red               */ 
    // #define BGRN  "\x1B[1;32m"    /* Foreground Bright Green             */ 
    // #define BYEL  "\x1B[1;33m"    /* Foreground Bright Yellow            */ 
    // #define BBLU  "\x1B[1;34m"    /* Foreground Bright Blue              */ 
    // #define BMAG  "\x1B[1;35m"    /* Foreground Bright Magenta           */ 
    // #define BCYN  "\x1B[1;36m"    /* Foreground Bright Cyan              */ 
    // #define BWHT  "\x1B[1;37m"    /* Foreground Bright White             */ 
    
    // #define BKBLK  "\x1B[40m"     /* Background Black                    */ 
    // #define BKRED  "\x1B[41m"     /* Background Dark Red                 */ 
    // #define BKGRN  "\x1B[42m"     /* Background Dark Green               */ 
    // #define BKYEL  "\x1B[43m"     /* Background Dark Yellow              */ 
    // #define BKBLU  "\x1B[44m"     /* Background Dark Blue                */ 
    // #define BKMAG  "\x1B[45m"     /* Background Dark Magenta             */ 
    // #define BKCYN  "\x1B[46m"     /* Background Dark Cyan                */ 
    // #define BKWHT  "\x1B[47m"     /* Background Dark White (Light Gray)  */ 
    
    // #define FBLK  "\x1B[5;30m"    /* Foreground Flashing Black (silly)   */ 
    // #define FRED  "\x1B[5;31m"    /* Foreground Flashing Dark Red        */ 
    // #define FGRN  "\x1B[5;32m"    /* Foreground Flashing Dark Green      */ 
    // #define FYEL  "\x1B[5;33m"    /* Foreground Flashing Dark Yellow     */ 
    // #define FBLU  "\x1B[5;34m"    /* Foreground Flashing Dark Blue       */ 
    // #define FMAG  "\x1B[5;35m"    /* Foreground Flashing Dark Magenta    */ 
    // #define FCYN  "\x1B[5;36m"    /* Foreground Flashing Dark Cyan       */ 
    // #define FWHT  "\x1B[5;37m"    /* Foreground Flashing Light Gray      */ 
    
    // #define BFBLK  "\x1B[1;5;30m" /* Foreground Flashing Dark Gray       */ 
    // #define BFRED  "\x1B[1;5;31m" /* Foreground Flashing Bright Red      */ 
    // #define BFGRN  "\x1B[1;5;32m" /* Foreground Flashing Bright Green    */ 
    // #define BFYEL  "\x1B[1;5;33m" /* Foreground Flashing Bright Yellow   */ 
    // #define BFBLU  "\x1B[1;5;34m" /* Foreground Flashing Bright Blue     */ 
    // #define BFMAG  "\x1B[1;5;35m" /* Foreground Flashing Bright Magenta  */ 
    // #define BFCYN  "\x1B[1;5;36m" /* Foreground Flashing Bright Cyan     */ 
    // #define BFWHT  "\x1B[1;5;37m" /* Foreground Flashing Bright White    */ 



    
}