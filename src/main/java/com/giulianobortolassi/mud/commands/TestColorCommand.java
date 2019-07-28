package com.giulianobortolassi.mud.commands;

import com.giulianobortolassi.mud.Color;
import com.giulianobortolassi.mud.Entity;
import com.giulianobortolassi.mud.Server;


/**
 * TestColorCommand
 */
public class TestColorCommand implements MudCommand {

    @Override
    public boolean checkRequirements(Entity entity) {
        return true; 
    }


    /**
     * Show a table with all available collors
     */
    @Override
	public Entity execute(Entity entity, String[] args, Server server) {
        
        
        StringBuffer output = new StringBuffer();
        output.append("To use collor add &<color_code> to your text. For ex &xx&yyMyText&zz \r\n");
        output.append("Available color_codes: \r\n")
        .append(" ---- FG COLORS ---- \r\n")
        .append(Color.FG_BLACK.getSymbol()).append("DARK BLACK ").append(Color.FG_RESET.getSymbol()).append(" \t fK").append("\r\n")
        .append(Color.FG_LIGHT_BLACK.getSymbol()).append("LIGHT BLACK").append(Color.FG_RESET.getSymbol()).append(" \t fk").append("\r\n")
        
        .append(Color.FG_DARK_RED.getSymbol()).append("DARK RED ").append(Color.FG_RESET.getSymbol()).append(" \t fR").append("\r\n")
        .append(Color.FG_LIGHT_RED.getSymbol()).append("LIGHT RED").append(Color.FG_RESET.getSymbol()).append(" \t fr").append("\r\n")
        
        .append(Color.FG_DARK_GREEN.getSymbol()).append("DARK GREEN ").append(Color.FG_RESET.getSymbol()).append(" \t fG").append("\r\n")
        .append(Color.FG_LIGHT_GREEN.getSymbol()).append("LIGHT GREEN").append(Color.FG_RESET.getSymbol()).append(" \t fg").append("\r\n")
       
        .append(Color.FG_DARK_YELLOW.getSymbol()).append("DARK YELLOW ").append(Color.FG_RESET.getSymbol()).append(" \t fY").append("\r\n")
        .append(Color.FG_LIGHT_YELLOW.getSymbol()).append("LIGHT YELLOW").append(Color.FG_RESET.getSymbol()).append(" \t fy").append("\r\n")
       
        .append(Color.FG_DARK_BLUE.getSymbol()).append("DARK BLUE ").append(Color.FG_RESET.getSymbol()).append(" \t fB").append("\r\n")
        .append(Color.FG_LIGHT_BLUE.getSymbol()).append("LIGHT BLUE").append(Color.FG_RESET.getSymbol()).append(" \t fb").append("\r\n")

        .append(Color.FG_DARK_MAGENTA.getSymbol()).append("DARK MAGENTA").append(Color.FG_RESET.getSymbol()).append(" \t fM").append("\r\n")
        .append(Color.FG_LIGHT_MAGENTA.getSymbol()).append("LIGHT MAGENTA").append(Color.FG_RESET.getSymbol()).append(" \t fm").append("\r\n")

        .append(Color.FG_DARK_CYAN.getSymbol()).append("DARK CYAN").append(Color.FG_RESET.getSymbol()).append(" \t fC").append("\r\n")
        .append(Color.FG_LIGHT_CYAN.getSymbol()).append("LIGHT CYAN").append(Color.FG_RESET.getSymbol()).append(" \t fc").append("\r\n")

        .append(Color.FG_DARK_WHITE.getSymbol()).append("DARK WHITE").append(Color.FG_RESET.getSymbol()).append(" \t fW").append("\r\n")
        .append(Color.FG_LIGHT_WHITE.getSymbol()).append("LIGHT WHITE").append(Color.FG_RESET.getSymbol()).append(" \t fw").append("\r\n")
        
        .append(" ---- BG COLORS ---- \r\n")

        .append(Color.BG_DARK_BLACK.getSymbol()).append("DARK BLACK").append(Color.FG_RESET.getSymbol()).append(" \t bK").append("\r\n")
        .append(Color.BG_LIGHT_BLACK.getSymbol()).append("LIGHT BLACK").append(Color.FG_RESET.getSymbol()).append(" \t bk").append("\r\n")
        
        .append(Color.BG_DARK_RED.getSymbol()).append("DARK RED").append(Color.FG_RESET.getSymbol()).append(" \t bR").append("\r\n")
        .append(Color.BG_LIGHT_RED.getSymbol()).append("LIGHT RED").append(Color.FG_RESET.getSymbol()).append(" \t br").append("\r\n")
        
        .append(Color.BG_DARK_GREEN.getSymbol()).append("DARK GREEN").append(Color.FG_RESET.getSymbol()).append(" \t bG").append("\r\n")
        .append(Color.BG_LIGHT_GREEN.getSymbol()).append("LIGHT GREEN").append(Color.FG_RESET.getSymbol()).append(" \t bg").append("\r\n")
       
        .append(Color.BG_DARK_YELLOW.getSymbol()).append("DARK YELLOW").append(Color.FG_RESET.getSymbol()).append(" \t bY").append("\r\n")
        .append(Color.BG_LIGHT_YELLOW.getSymbol()).append("LIGHT YELLOW").append(Color.FG_RESET.getSymbol()).append(" \t by").append("\r\n")
       
        .append(Color.BG_DARK_BLUE.getSymbol()).append("DARK BLUE").append(Color.FG_RESET.getSymbol()).append(" \t bB").append("\r\n")
        .append(Color.BG_LIGHT_BLUE.getSymbol()).append("LIGHT BLUE").append(Color.FG_RESET.getSymbol()).append(" \t bb").append("\r\n")

        .append(Color.BG_DARK_MAGENTA.getSymbol()).append("DARK MAGENTA").append(Color.FG_RESET.getSymbol()).append(" \t bM").append("\r\n")
        .append(Color.BG_LIGHT_MAGENTA.getSymbol()).append("LIGHT MAGENTA").append(Color.FG_RESET.getSymbol()).append(" \t bm").append("\r\n")

        .append(Color.BG_DARK_CYAN.getSymbol()).append("DARK CYAN").append(Color.FG_RESET.getSymbol()).append(" \t bC").append("\r\n")
        .append(Color.BG_LIGHT_CYAN.getSymbol()).append("LIGHT CYAN").append(Color.FG_RESET.getSymbol()).append(" \t bc").append("\r\n")

        .append(Color.BG_DARK_WHITE.getSymbol()).append("DARK WHITE").append(Color.FG_RESET.getSymbol()).append(" \t bW").append("\r\n")
        .append(Color.BG_LIGHT_WHITE.getSymbol()).append("LIGHT WHITE").append(Color.FG_RESET.getSymbol()).append(" \t bw").append("\r\n")
        
        .append(" ---- EXTRA MODIFIERS ---- \r\n")

        .append(Color.EX_BOLD.getSymbol()).append("BOLD TEXT").append(Color.FG_RESET.getSymbol()).append(" \t eb").append("\r\n")
        .append(Color.EX_INVERSE.getSymbol()).append("INVERSE COLORS").append(Color.FG_RESET.getSymbol()).append(" \t ei").append("\r\n")
        .append(Color.EX_UNDERLINE.getSymbol()).append("UNDERLINE TEXT").append(Color.FG_RESET.getSymbol()).append(" \t eu").append("\r\n")
        .append(Color.EX_FLASH.getSymbol()).append("BLINKING      ").append(Color.FG_RESET.getSymbol()).append(" \t ef").append("\r\n")
        
        .append("To reset colors, use 'fn' as a color code. ")
        .append("\r\n");

        entity.addResponse(output.toString());
        
        return entity;
	}
}