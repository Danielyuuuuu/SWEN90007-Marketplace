package MS_Quokka.Domain;

import java.util.UUID;

public class Purchase extends DomainObject {

    public enum Status {
        placed,
        processed,
        fulfilled,
        cancelled
    }

    private final String title;
    private int quantity;
    private double price;
    private final User buyer;
    private Status status;
    private final Product product;
    private final SellerGroup seller;
    private final Listing listing;

    public Purchase(String id, String title, int quantity, double price, String buyerId, String status, String productId, String sellerId, String fixedPriceId, String auctionId) {
        super(id);
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.buyer = new User(buyerId);
        this.status = Status.valueOf(status);
        this.product = new Product(productId);
        this.seller = new SellerGroup(sellerId);
        if (fixedPriceId != null) {
            listing = new FixPriceListing(fixedPriceId);
        } else if (auctionId != null) {
            listing = new AuctionListing(auctionId);
        } else {
            listing = null;
        }
    }

    public Purchase(String title, int quantity, double price, String buyerId, String status, String productId, String sellerId, String fixedPriceId, String auctionId) {
        this(UUID.randomUUID().toString(), title, quantity, price, buyerId, status, productId, sellerId, fixedPriceId, auctionId);
    }

    public String getTitle() {
        return title;
    }

    public Listing getListing() {
        return listing;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public User getBuyer() {
        return buyer;
    }

    public Status getStatus() {
        return status;
    }

    public Product getProduct() {
        return product;
    }

    public SellerGroup getSeller() {
        return seller;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

    public void updateQuantity(int change) throws InvalidQuantityException {
        if (quantity + change < 1) {
            throw new InvalidQuantityException("Purchase quantity must be at least 1");
        }
        price *= (double) (quantity + change) / quantity;
        quantity += change;
    }
}
