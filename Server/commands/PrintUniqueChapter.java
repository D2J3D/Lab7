package Server.commands;

import Server.collection_utils.CollectionManager;

import Common.Response;
import Server.collection_utils.CollectionManager;

import java.io.Serializable;
import java.util.stream.Collectors;

public class PrintUniqueChapter implements Command, Serializable {
    private final CollectionManager cm;
    public PrintUniqueChapter(CollectionManager cm){
        this.cm = cm;
    }
    @Override
    public Object execute() {
        return new Response(cm.printUniqueChapter());
    }

    @Override
    public String descr() {
        return "print_unique_chapter";
    }
}