package com.giulianobortolassi.mud;

import java.util.LinkedList;

/**
 * Entity
 */
public class Entity {

    private Connection connection;
    private String name;
    private String sex;

    private LinkedList<String> commandQueue = new LinkedList<>();
    
    public Entity(){
        connection = null;
        name = "Noone";
        sex = "Undefined";

    }

    public Entity(Connection con, String name, String sex){
        this.connection = con;
        this.name = name;
        this.sex = sex;
    }


    /**
     * @param connection the connection to set
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }
    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /** Check the input from entity and return a command
     * 
     * TODO: Is it a good idea to refactor and find the command object here?
     *  
     */
	public String getCurrentCommand() {
        if( this.connection != null ){
            String input = this.connection.getInput();
            // Parse something?
            if( input != null ) {
                this.commandQueue.add(input);
            }   
        }
        
        return !this.commandQueue.isEmpty() ? this.commandQueue.pop() : null;
	}

    // Some game system may insert commands into entities
    public void addCommand(String command) {
        this.commandQueue.add(command);
    }

	public void addResponse(String commandResult) {
        if( this.connection != null) {
            connection.prepareData(commandResult);
        }
	}

}