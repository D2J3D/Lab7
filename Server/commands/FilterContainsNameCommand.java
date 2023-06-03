package Server.commands;

import Common.Response;
import Server.collection_utils.CollectionManager;

import java.io.Serializable;
import java.util.stream.Collectors;

public class FilterContainsNameCommand implements Command, Serializable{
        private final CollectionManager cm;
        private String nameParam;

        public FilterContainsNameCommand(CollectionManager cm, String nameParam){
            this.cm = cm;
            this.nameParam = nameParam;
        }

        public FilterContainsNameCommand(CollectionManager cm){
            this.cm = cm;
        }

        @Override
        public void setParams(String[] params)
        {
            this.nameParam = params[0];
        }

        @Override
        public Object execute() {
            return new Response("Солдаты, содержащие в имени подстроку " + nameParam + ": " + cm.filterContainsName(nameParam));
        }

        @Override
        public String descr() {
            return "filter_contains_name";
        }
}

