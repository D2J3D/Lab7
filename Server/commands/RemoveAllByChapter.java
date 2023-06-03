package Server.commands;

import Common.Response;
import Common.core.Chapter;
import Server.collection_utils.CollectionManager;

import java.io.Serializable;

public class RemoveAllByChapter implements Command, Serializable {
    private final CollectionManager cm;
    private Chapter chapter;

    public RemoveAllByChapter(CollectionManager cm){
        this.cm = cm;
    }
    public RemoveAllByChapter(CollectionManager cm, Chapter chapter){
        this.cm = cm;
        this.chapter = chapter;
    }
    @Override
    public Object execute() {
        cm.removeAllByChapter(chapter);
        return new Response("Команда " + this.descr() + "выполена успешно");
    }

    @Override
    public String descr() {
        return "remove_all_by_chapter";
    }

    @Override
    public void setParams(String[] params) {
        this.chapter = new Chapter(params[0]);
    }
}