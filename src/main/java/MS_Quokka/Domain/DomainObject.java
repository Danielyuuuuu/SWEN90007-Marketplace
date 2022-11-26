package MS_Quokka.Domain;

public abstract class DomainObject {

    private final String id;

    DomainObject(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
