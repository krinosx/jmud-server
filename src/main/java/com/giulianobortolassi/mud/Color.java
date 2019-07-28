package com.giulianobortolassi.mud;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Color - Define color codes to use with text output.
 */
public enum Color {

    RESET("text_reset","&tn", "[0;0m"), 
    FG_RESET("fg_reset", "&fn", "[0m"), 
    
    FG_BLACK("fg_dark_black","&fK","[30m"),
    FG_LIGHT_BLACK("fg_light_black","&fk","[90m"),
    
    // Red
    FG_DARK_RED("fg_dark_red", "&fR","[31m"),
    FG_LIGHT_RED("fg_light_red", "&fr","[91m"),

    // Green
    FG_DARK_GREEN("fg_dark_green", "&fG","[32m"),
    FG_LIGHT_GREEN("fg_light_green", "&fg","[92m"),
    
    // Green
    FG_DARK_YELLOW("fg_dark_yellow", "&fY","[33m"),
    FG_LIGHT_YELLOW("fg_light_yellow", "&fy","[93m"),
    
    // Green
    FG_DARK_BLUE("fg_dark_blue", "&fB","[34m"),
    FG_LIGHT_BLUE("fg_light_blue", "&fb","[94m"),
        
    // Magenta
    FG_DARK_MAGENTA("fg_dark_magenta", "&fM","[35m"),
    FG_LIGHT_MAGENTA("fg_light_magenta", "&fm","[95m"),
    
    // Cyan
    FG_DARK_CYAN("fg_dark_cyan", "&fC","[36m"),
    FG_LIGHT_CYAN("fg_light_cyan", "&fc","[96m"), // bold
    
    // White
    FG_DARK_WHITE("fg_dark_white", "&fW","[37m"),
    FG_LIGHT_WHITE("fg_light_white", "&fw","[97m"),

    /* BACKGROUND COLORS */
    
    BG_DARK_BLACK("bg_dark_black","&bK","[40m"),
    BG_LIGHT_BLACK("bg_light_black","&bk","[100m"),
    
    // Red
    BG_DARK_RED("bg_dark_red", "&bR","[41m"),
    BG_LIGHT_RED("bg_light_red", "&br","[101m"),

    // Green
    BG_DARK_GREEN("bg_dark_green", "&bG","[42m"),
    BG_LIGHT_GREEN("bg_light_green", "&bg","[102m"),
    
    // Green
    BG_DARK_YELLOW("bg_dark_yellow", "&bY","[43m"),
    BG_LIGHT_YELLOW("bg_light_yellow", "&by","[103m"),
    
    // Green
    BG_DARK_BLUE("bg_dark_blue", "&bB","[44m"),
    BG_LIGHT_BLUE("bg_light_blue", "&bb","[104m"),
        
    // Magenta
    BG_DARK_MAGENTA("bg_dark_magenta", "&bM","[45m"),
    BG_LIGHT_MAGENTA("bg_light_magenta", "&bm","[105m"),
    
    // Cyan
    BG_DARK_CYAN("bg_dark_cyan", "&bC","[46m"),
    BG_LIGHT_CYAN("bg_light_cyan", "&bc","[106m"), // bold
    
    // White
    BG_DARK_WHITE("bg_dark_white", "&bW","[47m"),
    BG_LIGHT_WHITE("bg_light_white", "&bw","[107m"),



    
    // Extras
    EX_FLASH("ex_flash", "&ef","[1;5;37m"),
    EX_UNDERLINE("ex_underline", "&eu","[4m"),
    EX_INVERSE("ex_inverse", "&ei","[7m"),
    EX_BOLD("ex_bold", "&eb","[1m"),



    FG_EXPERIMENTAL("fg_exp", "&f0", "[38;5;207m"),
    FG_EXPERIMENTAL_2("fg_exp", "&f1", "[7m"),
    FG_EXPERIMENTAL_3("fg_exp", "&f2", "[11m")

    
    ;   

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
        // FG COLORS
        symbolIndex.put(FG_BLACK.symbol, FG_BLACK);
        symbolIndex.put(FG_LIGHT_BLACK.symbol, FG_BLACK);
        symbolIndex.put(FG_DARK_RED.symbol, FG_DARK_RED);
        symbolIndex.put(FG_LIGHT_RED.symbol, FG_LIGHT_RED);
        symbolIndex.put(FG_DARK_GREEN.symbol, FG_DARK_GREEN);
        symbolIndex.put(FG_LIGHT_GREEN.symbol, FG_LIGHT_GREEN);
        symbolIndex.put(FG_DARK_YELLOW.symbol, FG_DARK_YELLOW);
        symbolIndex.put(FG_LIGHT_YELLOW.symbol, FG_LIGHT_YELLOW);
        symbolIndex.put(FG_DARK_BLUE.symbol, FG_DARK_BLUE);
        symbolIndex.put(FG_LIGHT_BLUE.symbol, FG_LIGHT_BLUE);
        symbolIndex.put(FG_DARK_MAGENTA.symbol, FG_DARK_MAGENTA);
        symbolIndex.put(FG_LIGHT_MAGENTA.symbol, FG_LIGHT_MAGENTA);
        symbolIndex.put(FG_DARK_CYAN.symbol, FG_DARK_CYAN);
        symbolIndex.put(FG_LIGHT_CYAN.symbol, FG_LIGHT_CYAN);
        symbolIndex.put(FG_DARK_WHITE.symbol, FG_DARK_WHITE);
        symbolIndex.put(FG_LIGHT_WHITE.symbol, FG_LIGHT_WHITE);
        // BG COLORS
        symbolIndex.put(BG_DARK_BLACK.symbol, BG_DARK_BLACK);
        symbolIndex.put(BG_LIGHT_BLACK.symbol, BG_DARK_BLACK);
        symbolIndex.put(BG_DARK_RED.symbol, BG_DARK_RED);
        symbolIndex.put(BG_LIGHT_RED.symbol, BG_LIGHT_RED);
        symbolIndex.put(BG_DARK_GREEN.symbol, BG_DARK_GREEN);
        symbolIndex.put(BG_LIGHT_GREEN.symbol, BG_LIGHT_GREEN);
        symbolIndex.put(BG_DARK_YELLOW.symbol, BG_DARK_YELLOW);
        symbolIndex.put(BG_LIGHT_YELLOW.symbol, BG_LIGHT_YELLOW);
        symbolIndex.put(BG_DARK_BLUE.symbol, BG_DARK_BLUE);
        symbolIndex.put(BG_LIGHT_BLUE.symbol, BG_LIGHT_BLUE);
        symbolIndex.put(BG_DARK_MAGENTA.symbol, BG_DARK_MAGENTA);
        symbolIndex.put(BG_LIGHT_MAGENTA.symbol, BG_LIGHT_MAGENTA);
        symbolIndex.put(BG_DARK_CYAN.symbol, BG_DARK_CYAN);
        symbolIndex.put(BG_LIGHT_CYAN.symbol, BG_LIGHT_CYAN);
        symbolIndex.put(BG_DARK_WHITE.symbol, BG_DARK_WHITE);
        symbolIndex.put(BG_LIGHT_WHITE.symbol, BG_LIGHT_WHITE);        


        // Extras
        symbolIndex.put(EX_FLASH.symbol, EX_FLASH);
        symbolIndex.put(EX_UNDERLINE.symbol, EX_UNDERLINE);        
        symbolIndex.put(EX_INVERSE.symbol, EX_INVERSE);       
        symbolIndex.put(EX_BOLD.symbol, EX_BOLD);
        
        // Experimental
        symbolIndex.put(FG_EXPERIMENTAL.symbol, FG_EXPERIMENTAL);
        symbolIndex.put(FG_EXPERIMENTAL_2.symbol, FG_EXPERIMENTAL_2);
        symbolIndex.put(FG_EXPERIMENTAL_3.symbol, FG_EXPERIMENTAL_3);
    
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
}