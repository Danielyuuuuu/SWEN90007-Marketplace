package MS_Quokka.Domain;

import java.util.UUID;

public class Category extends DomainObject {

    private String name;

    public Category(String id, String name){
        super(id);
        this.name = name;
    }

    public Category(String name){
        super(UUID.randomUUID().toString());
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() { return "Category: " + getId() + " " + name + " |";}
}
