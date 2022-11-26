package MS_Quokka.Controller;

import MS_Quokka.Domain.*;
import MS_Quokka.Mapper.*;
import MS_Quokka.Utils.ExclusiveWriteManager;
import MS_Quokka.Utils.Role;
import MS_Quokka.Utils.UnitOfWork;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "PurchasesServlet", value = "/purchases")
public class PurchasesController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println(request.getQueryString());

        ArrayList<Purchase> purchases;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new UserMapper().readOneByEmail(authentication.getName());

        if (user.getRole() == Role.ADMIN) {
            System.out.println("getting purchase list");
            purchases = new PurchaseMapper().readList();
            System.out.println("received purchase list");
        } else {
            System.out.println("hello");
            purchases = new PurchaseMapper().readListByBuyerID(user.getId());
            if (user.getSellerGroup() != null) {
                ArrayList<Purchase> sales = new PurchaseMapper().readListBySellerID(user.getSellerGroup().getId());
                request.setAttribute("sales", sales);
            }
        }
        System.out.println("past");
        request.setAttribute("purchases", purchases);
        request.getRequestDispatcher("purchases.jsp").forward(request, response);
    }

    // Responsible for the creation of a purchase from a listing
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println(request.getParameter("id"));

        String id = request.getParameter("id");
        String listingType = request.getParameter("listingType");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        Listing listing;
        String buyerId;
        String fixedPriceId = null;
        String auctionId = null;

        // Start of transaction, acquire the write lock
        ExclusiveWriteManager manager = ExclusiveWriteManager.getInstance();

        // Acquires lock for the listing record
        manager.acquireLock(id, Thread.currentThread().getName());

        /* Now that we have acquired the lock, read from the database to get the most updated
         listing object (depends on its type) */
        if (listingType.equals("auction")) {
            buyerId = request.getParameter("buyer");
            listing = new AuctionListingMapper().readOne(id);
            auctionId = id;
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("From authentication: " + authentication.getName());
            User buyer = new UserMapper().readOneByEmail(authentication.getName());
            buyerId = buyer.getId();
            listing = new FixPriceListingMapper().readOne(id);
            fixedPriceId = id;
        }

        try {
            /* There is a possibility that someone else has already updated the object between read + asking for lock.
             * If that's the case, check whether this request is still valid */
            if (listing.getArchive()) {
                throw new DatabaseAlreadyUpdated("Sorry, the admin has already archived this listing.");
            }
            if (listing.getQuantity() < quantity) {
                throw new DatabaseAlreadyUpdated("Sorry, this item does not have enough stock.");
            }

            /* Use Unit Of Work to update the listing (we have got its write lock), and
            create a new purchase (no lock required for this) */
            UnitOfWork.newCurrent();
            UnitOfWork uow = UnitOfWork.getCurrent();

            listing.updateQuantity(-quantity);
            uow.registerDirty(listing);

            Purchase purchase = new Purchase(listing.getListingTitle(), quantity, listing.getPrice() * quantity, buyerId, "placed", listing.getProduct().getId(), listing.getSellerGroup().getId(), fixedPriceId, auctionId);
            uow.registerNew(purchase);

            uow.commit();

        } catch (InvalidQuantityException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DatabaseAlreadyUpdated e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } finally {
            // Regardless of the outcome, release the lock
            manager.releaseLock(id, Thread.currentThread().getName());
        }

        response.sendRedirect("purchases");
    }
}
