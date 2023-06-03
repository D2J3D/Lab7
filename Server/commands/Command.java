package Server.commands;

import Common.core.SpaceMarine;

import java.io.Serializable;

public interface Command extends Serializable {
    Object execute();
    String descr();
    default void setParams(String[] param){};
    default void setMarine(SpaceMarine spaceMarine){};

}
