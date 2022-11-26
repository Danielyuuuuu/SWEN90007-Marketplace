package MS_Quokka.Domain;

import MS_Quokka.Mapper.SellerGroupMapper;

public class SellerGroup extends DomainObject {

    public String name;


    public SellerGroup(String id) {
        super(id);
        name = null;
    }

    public SellerGroup(String id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        if (name == null){
            load();
        }
        return name;
    }

    private void load() {
        SellerGroupMapper sellerGroupMapper = new SellerGroupMapper();
        SellerGroup sellerGroup = sellerGroupMapper.readOne(getId());
        name = sellerGroup.getName();
    }

}
