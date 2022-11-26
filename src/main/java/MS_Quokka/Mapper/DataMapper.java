package MS_Quokka.Mapper;

import MS_Quokka.Domain.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DataMapper {

    public static <T extends Mapper<U>, U extends DomainObject> T getMapper(Class<U> c ) {
        if (c == AuctionListing.class) return (T) new AuctionListingMapper();
        else if (c == Category.class) return (T) new CategoryMapper();
        else if (c == FixPriceListing.class) return (T) new FixPriceListingMapper();
        else if (c == Product.class) return (T) new ProductMapper();
        else if (c == Purchase.class) return (T) new PurchaseMapper();
        else if (c == SellerGroup.class) return (T) new SellerGroupMapper();
        return (T) new UserMapper(); // must be user
    }

    public static <T extends DomainObject> void create(T obj, Connection conn) throws SQLException {
        Mapper<T> mapper = getMapper((Class<T>) obj.getClass());
        mapper.create(obj, conn);
    }

    public static <T extends DomainObject> void update(T obj, Connection conn) throws SQLException {
        Mapper<T> mapper = getMapper((Class<T>) obj.getClass());
        mapper.update(obj, conn);
    }

}
