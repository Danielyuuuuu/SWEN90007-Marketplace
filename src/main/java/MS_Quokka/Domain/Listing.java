package MS_Quokka.Domain;


public abstract class Listing extends DomainObject {

    protected String id;
    protected String listingTitle;
    protected Integer quantity;
    protected Double price;
    protected String description;
    protected Product product;
    protected SellerGroup sellerGroup;
    protected Boolean archive;


    public Listing(String id, String listingTitle, Integer quantity, Double price, String description, SellerGroup sellerGroup, Product product, Boolean archive) {
        super(id);
        this.listingTitle = listingTitle;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.product = product;
        this.sellerGroup = sellerGroup;
        this.archive = archive;
    };

    public Listing(String id) {
        super(id);
        listingTitle = null;
        quantity = null;
        description = null;
        product = null;
        sellerGroup = null;
        archive = null;
    }


    public Integer getQuantity() {

        if (this.quantity == null){
            load();
        }
        return this.quantity;
    }

    public String getDescription() {

        if (this.description == null){
            load();
        }
        return this.description;
    }

    public Product getProduct() {

        if (this.product == null){
            load();
        }
        return product;
    }

    public SellerGroup getSellerGroup() {

        if (this.sellerGroup == null){
            load();
        }
        return sellerGroup;
    }

    public String getListingTitle() {

        if (this.listingTitle == null){
            load();
        }
        return listingTitle;
    }

    public Double getPrice(){

        if (this.price == null){
            load();
        }
        return price;
    }

    public Boolean getArchive() {

        if (this.archive == null){
            load();
        }
        return archive;
    }

    public abstract void load();

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void updateQuantity(int change) throws InvalidQuantityException {
        if (quantity + change < 0) {
            throw new InvalidQuantityException("Listing quantity must be a positive number");
        }
        quantity += change;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    protected void setProduct(Product product) {
        this.product = product;
    }

    protected void setSellerGroup(SellerGroup sellerGroup) {
        this.sellerGroup = sellerGroup;
    }

    public abstract Boolean isVisibleForBuyer();

    public Boolean isVisibleForSeller(){
        return !this.getArchive();
    }

    public String toString(){
        return "Listing: " + listingTitle + " " + quantity.toString() + " " + " " +
                description + " " + product.toString();
    }
}
