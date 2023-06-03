package Server.collection_utils;

import Common.core.Chapter;
import Common.core.SpaceMarine;
import Common.core.SpaceMarines;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionManager {
    //private SpaceMarines spaceMarines;
    private List<SpaceMarine> synchronizedMarines;
    private int collectionId;

    public CollectionManager(SpaceMarines allSpaceMarines){
        this.synchronizedMarines = allSpaceMarines.getSynchronizedMarines();
        this.collectionId = allSpaceMarines.getIdOfCollection();
        Stream<SpaceMarine> stream = synchronizedMarines.stream();
    }

    public void setSynchronizedMarines(List<SpaceMarine> synchronizedMarines) {
        this.synchronizedMarines = synchronizedMarines;
    }

    public List<SpaceMarine> getSynchronizedMarines() {
        return synchronizedMarines;
    }

    public List<SpaceMarine> removeAllById(Integer id){
        Stream<SpaceMarine> stream = synchronizedMarines.stream();
        List<SpaceMarine> newSynList = (stream.filter(x -> !(x.getId().equals(id))).collect(Collectors.toCollection(() -> Collections.synchronizedList(new LinkedList<>()))));
        this.setSynchronizedMarines(newSynList);
        synchronizedMarines = newSynList;
        return newSynList;
    }

    public String removeAllByChapter(Chapter chapter){
        Stream<SpaceMarine> stream = synchronizedMarines.stream();
        List<SpaceMarine> newSynList = (stream.filter(x -> !(x.getChapter().getName().equals(chapter.getName()))).collect(Collectors.toCollection(() -> Collections.synchronizedList(new LinkedList<>()))));
        this.setSynchronizedMarines(newSynList);
        synchronizedMarines = newSynList;
        return "All items with chapter " + chapter.getName() + " are removed";
    }

    public List<SpaceMarine> showMarines(){
        if (synchronizedMarines.size() == 0){
            System.out.println("Collection is empty");
        }
        return synchronizedMarines;
    }
    public void clearMarines(){
        synchronizedMarines.clear();
        this.setSynchronizedMarines(synchronizedMarines);
    }

    public String filterContainsName(String nameParam){
        Stream<SpaceMarine> stream = synchronizedMarines.stream();
        return stream.filter(x -> x.getName().contains(nameParam)).map(Object::toString).collect(Collectors.joining("\n"));
    }

    public String showCollectionInfo() {
        return "Type: SpaceMarine. Length = " + synchronizedMarines.size();
    }

    public String printUniqueChapter() {
        Stream<Chapter> stream = synchronizedMarines.stream().map(x -> x.getChapter());
        return stream.collect(Collectors.groupingBy(Chapter::getName, Collectors.counting())).entrySet().stream().map(e -> e.getKey()).collect(Collectors.joining(", "));
    }

    public void addMarine(SpaceMarine newMarine){
        Stream<SpaceMarine> stream = synchronizedMarines.stream();
        stream = Stream.concat(stream, Stream.of(newMarine));
        this.setSynchronizedMarines(stream.collect(Collectors.toCollection(() -> Collections.synchronizedList(new LinkedList<>()))));
    }
    public void removeGreater(SpaceMarine sample){
        Stream<SpaceMarine> stream = synchronizedMarines.stream();
        List<SpaceMarine> newSynList = stream.filter(x -> x.compareTo(sample)<=0).collect(Collectors.toCollection(() -> Collections.synchronizedList(new LinkedList<>())));
        this.setSynchronizedMarines(newSynList);
    }
}
