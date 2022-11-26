package MS_Quokka.Domain;

import MS_Quokka.Mapper.FixPriceListingMapper;

import java.sql.Timestamp;
import java.util.UUID;

public class FixPriceListing extends Listing{

    //load a new fixPriceListing from existing data (database)
    public FixPriceListing(String id,String listingTitle, Integer quantity, Double price, String description, SellerGroup sellerGroup, Product product, Boolean achive) {
        super(id, listingTitle, quantity, price, description, sellerGroup, product, achive);
    }

    //create a new fixPriceListing
    public FixPriceListing(String listingTitle, Integer quantity, Double price, String description, SellerGroup sellerGroup, Product product) {
        super(UUID.randomUUID().toString(), listingTitle, quantity, price, description, sellerGroup, product, false);
    }

    public FixPriceListing(String id) {
        super(id);
    }

    public Double getPrice() {
        return price;
    }
    
    @Override
    public void load() {

        FixPriceListingMapper fixPriceListingMapper = new FixPriceListingMapper();
        FixPriceListing fixPriceListing = fixPriceListingMapper.readOne(this.getId());

        if(this.listingTitle == null) setListingTitle(fixPriceListing.getListingTitle());
        if(this.quantity == null) setQuantity(fixPriceListing.getQuantity());
        if(this.price == null) setPrice(fixPriceListing.getPrice());
        if(this.description == null) setDescription(fixPriceListing.getDescription());
        if(this.sellerGroup== null) setSellerGroup(fixPriceListing.getSellerGroup());
        if(this.product == null) setProduct(fixPriceListing.getProduct());
        if(this.archive == null) setArchive(fixPriceListing.getArchive());

    }

    @Override
    public Boolean isVisibleForBuyer() {
        return !((this.getQuantity() <=0) || this.getArchive());
    }
}
