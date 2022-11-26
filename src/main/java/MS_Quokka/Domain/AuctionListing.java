package MS_Quokka.Domain;

import MS_Quokka.Mapper.AuctionListingMapper;
import MS_Quokka.Mapper.BidMapper;
import MS_Quokka.Mapper.ProductMapper;
import MS_Quokka.Mapper.SellerGroupMapper;
import MS_Quokka.Utils.DBConnPool;
import MS_Quokka.Utils.UnitOfWork;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class AuctionListing extends Listing {

    //set the time limit to 3 minutes
    final private Integer auctionDuration = (1000 * 60 * 3);

    protected Integer bidCounter;
    protected User highestBidder;
    protected Timestamp startTime;
    protected Timestamp endTime;

    //load a new actionListing from existing data (database)
    public AuctionListing(String id, String listingTitle, Integer quantity, Timestamp startTime, Timestamp endTime,
                          Double price, String description, Integer bidCounter, SellerGroup sellerGroup,
                          Product product, User highestBidder, Boolean archive) {

        super(id, listingTitle, quantity, price, description, sellerGroup, product, archive);

        this.startTime = startTime;
        this.endTime = endTime;
        this.bidCounter = bidCounter;
        this.highestBidder = highestBidder;

    }

    //create a new actionListing
    public AuctionListing(String listingTitle, Integer quantity, Double price, String description, SellerGroup sellerGroup, Product product) {
        super(UUID.randomUUID().toString(), listingTitle, quantity, price, description, sellerGroup, product, false);

        this.startTime = new Timestamp(System.currentTimeMillis());
        this.endTime = new Timestamp(startTime.getTime() + auctionDuration);
        this.bidCounter = 0;
        this.highestBidder = null;
    }

    public AuctionListing(String id) {
        super(id);
        startTime = null;
        endTime = null;
        price = null;
        bidCounter = null;
        highestBidder = null;
    }

    public Integer getBidCounter() {
        if(this.bidCounter == null) load();
        return bidCounter;
    }

    public User getHighestBidder() {
        if(getBidCounter() != 0 && this.highestBidder == null) load();
        return highestBidder;
    }

    public Timestamp getStartTime() {
        if(this.startTime == null) load();
        return startTime;
    }

    public Timestamp getEndTime() {
        if(this.endTime == null) load();
        return endTime;
    }

    @Override
    public void load() {

        AuctionListingMapper auctionListingMapper = new AuctionListingMapper();
        AuctionListing auctionListing = auctionListingMapper.readOne(this.getId());

        if(this.listingTitle == null) setListingTitle(auctionListing.getListingTitle());
        if(this.quantity == null) setQuantity(auctionListing.getQuantity());
        if(this.startTime == null) setStartTime(auctionListing.getStartTime());
        if(this.endTime == null) setEndTime(auctionListing.getEndTime());
        if(this.price == null) setPrice(auctionListing.getPrice());
        if(this.description == null) setDescription(auctionListing.getDescription());
        if(this.bidCounter == null) setBidCounter(auctionListing.getBidCounter());
        if(this.sellerGroup == null) setSellerGroup(auctionListing.getSellerGroup());
        if(this.product == null) setProduct(auctionListing.getProduct());
        if(this.highestBidder == null) setHighestBidder(auctionListing.getHighestBidder());
        if(this.archive == null) setArchive(auctionListing.getArchive());
    }

    private void setBidCounter(Integer bidCounter) { this.bidCounter = bidCounter; }
    private void setHighestBidder(User highestBidder) { this.highestBidder = highestBidder; }
    private void setStartTime(Timestamp startTime) { this.startTime = startTime; }
    private void setEndTime(Timestamp endTime) { this.endTime = endTime; }



    public Boolean isExpire(){
        return this.getEndTime().before(new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public Boolean isVisibleForBuyer(){
        return !(isExpire() || this.getArchive() || (this.getQuantity() <= 0));
    }

    public Boolean isOrderCreate(){
        return (isExpire() && (this.getQuantity() == 0));
    }

    public void placeBid(Double price, User user, Connection conn) throws SQLException {

        AuctionListingMapper auctionListingMapper = new AuctionListingMapper();
        BidMapper bidMapper = new BidMapper();

        if(this.getArchive()) throw new IllegalArgumentException("The auction listing has been deleted");

        if(price == null || user == null) throw new NullPointerException("Price or user should not be null");

        if(price < this.getPrice() + 1 ) throw new IllegalArgumentException("Input price should greater or equal to current price + 1 or you have been outbid");

        if(isExpire()) throw new RuntimeException("The auction listing has ended");

        //execute the bids
        setPrice(price);

        /* Here, we need to update two tables in db - the auction_listing table as well as the bid table.
        * Note that we did not use uow here. The reason is that uow registers an object in one of the new/dirty list,
        * however we do not have a bid domain object. The bid table is used in the association table mapping pattern to
        * handle many-to-many relationships between user and auction listing.
        * Nevertheless, we still want to make sure that both updates to the database either both succeed or
        * roll back if either fails. Therefore we come up with the following solution.
        * */
        try {
            conn.setAutoCommit(false);
            System.out.println("We have set the autocommit to false");
            auctionListingMapper.update(this, conn);
            bidMapper.create(price, user.getId(), this.getId(), conn);
            conn.commit();
        } catch (SQLException e) {
            try{
                conn.rollback();
                System.out.println("Rolling back the transaction");
                e.printStackTrace();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    //Timestamp
    //https://mkyong.com/java/how-to-get-current-timestamps-in-java/

}
