package com.fofoisoninternet.multiplayerlobby;

/**
 *
 * @author fofoisoninternet
 */
public enum Mod implements MultiplayerObjectEnum{
    HR("Hard Rock","HR",0),
    DT("Double time","DT",0),
    FL("Flash light","FL",0),
    HD("Hidden","HD",0),
    EZ("Easy","EZ",0),
    FREEMOD("Free mod","Freemod",0),
    NF("No Fail","NF",0),
    NONE("No mod(NM)","",0);
    
    private final String name;
    private final int number;
    private final String shortName;
    
    private Mod(String name,String shortName,int number){
        this.name = name;
        this.number = number;
        this.shortName = shortName;
    }
    
    @Override
    public int toInt(){
        return number;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    /**
     * Indicates the value to use in !mp Mod
     * @return A string with the initials 
     */
    public String shortName(){
        return shortName;
    }
}
