package MS_Quokka.Controller;

import MS_Quokka.Domain.*;
import MS_Quokka.Mapper.PurchaseMapper;
import MS_Quokka.Mapper.UserMapper;
import MS_Quokka.Utils.DBConnPool;
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
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name="PurchaseServlet", value="/purchase")
public class PurchaseController extends HttpServlet {

    Purchase.Status placed = Purchase.Status.placed;
    Purchase.Status processed = Purchase.Status.processed;
    Purchase.Status fulfilled = Purchase.Status.fulfilled;
    Purchase.Status cancelled = Purchase.Status.cancelled;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("id");
        System.out.println(id);

        Purchase purchase = new PurchaseMapper().readOne(id);
        request.setAttribute("purchase", purchase);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = new UserMapper().readOneByEmail(email);
        if (user.getRole().equals(Role.SELLER) && user.getSellerGroup().getId().equals(purchase.getSeller().getId())) {
            request.setAttribute("displayConfirmButton", true);
        } else if (user.getRole().equals(Role.USER) && !user.getId().equals(purchase.getBuyer().getId())) {
            SecurityContextHolder.clearContext();
            response.sendRedirect("login.jsp");
            return;
        }
        else {
            request.setAttribute("displayConfirmButton", false);
        }

        request.getRequestDispatcher("purchase.jsp").forward(request, response);
    }

    // Responsible for updating a purchase
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("id");
        String status = request.getParameter("status");
        String originalQuantity = request.getParameter("originalQuantity");
        Purchase purchase = new PurchaseMapper().readOne(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = new UserMapper().readOneByEmail(email);

        try {
            // Case 1: Simply updating the status (e.g. cancel it or confirm it)
            if (status != null) {

                // Case 1a: Cancel the purchase: set the status of order and add the quantity back to the associated listing
                if (status.equals("cancelled")) {
                    if ((user.getRole().equals(Role.SELLER) && user.getSellerGroup().getId().equals(purchase.getSeller().getId())) || (user.getRole().equals(Role.USER) && purchase.getBuyer().getId().equals(user.getId()))) {
                        cancelPurchase(status, id);
                    } else {
                        SecurityContextHolder.clearContext();
                        response.sendRedirect("login.jsp");
                        return;
                    }
                }
                // Case 1b,1c,1d are handled differently to 1a
                else {
                    if ((user.getRole().equals(Role.SELLER) && user.getSellerGroup().getId().equals(purchase.getSeller().getId()))) {
                        updateStatus(status, Integer.parseInt(originalQuantity), id);
                    } else {
                        SecurityContextHolder.clearContext();
                        response.sendRedirect("login.jsp");
                        return;
                    }
                }
            }

            // Case 2: Update the order by updating the quantity
            else {
                if ((user.getRole().equals(Role.SELLER) && user.getSellerGroup().getId().equals(purchase.getSeller().getId())) || (user.getRole().equals(Role.USER) && purchase.getBuyer().getId().equals(user.getId()) && purchase.getStatus().equals(Purchase.Status.placed))) {
                    int change = Integer.parseInt(request.getParameter("quantity"));
                    updateQuantity(id, Integer.parseInt(originalQuantity), change);
                } else {
                    SecurityContextHolder.clearContext();
                    response.sendRedirect("login.jsp");
                    return;
                }
            }

        } catch (InvalidQuantityException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (DatabaseAlreadyUpdated e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

        response.sendRedirect("purchases");
    }

    /* Update the purchase status to 'cancelled'. A lock is required for the purchase object + the associated listing object
     Args:
        status: the new status that the user wants to update the purchase to
        purchaseId: the id of the purchase object the request sends
    */
    private void cancelPurchase(String status, String purchaseId) throws DatabaseAlreadyUpdated, InvalidQuantityException {

        Boolean listingLockRequired = false;

        // Start of transaction, acquire the write lock
        ExclusiveWriteManager manager = ExclusiveWriteManager.getInstance();

        // Acquires lock for the purchase object
        manager.acquireLock(purchaseId, Thread.currentThread().getName());

        /* Now that we have the lock, read the purchase object from the database to get the most updated
         purchase object */
        Purchase purchase = new PurchaseMapper().readOne(purchaseId);
        Purchase.Status purchase_status_db = purchase.getStatus();

        try{

            // Handle Concurrency. If someone else has updated the status to 'fulfilled', cannot cancel it
            if (purchase_status_db.equals(fulfilled)) {
                throw new DatabaseAlreadyUpdated("Sorry, someone else has already fulfilled the order. You cannot cancel it anymore");
            }
            // Otherwise, proceed to cancel the purchase. If the status is already "cancelled", do nothing.
            else if (purchase_status_db.equals(placed) || purchase_status_db.equals(processed)) {

                // Firstly, acquire the lock for the listing associated with the purchase
                listingLockRequired = true;
                manager.acquireLock(purchase.getListing().getId(), Thread.currentThread().getName());

                // Start Unit Of Work
                UnitOfWork.newCurrent();
                UnitOfWork uow = UnitOfWork.getCurrent();

                int quantity_to_add_back = purchase.getQuantity();

                // Step 1: Update the listing
                // Lazy load the quantity of the listing
                purchase.getListing().getQuantity();

                purchase.getListing().updateQuantity(quantity_to_add_back);
                uow.registerDirty(purchase.getListing());

                // Step 2: Update the order
                purchase.setStatus(status);
                uow.registerDirty(purchase);

                // Ready to commit
                uow.commit();
            }

            // If the status is already cancel, do nothing. No code here.
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            manager.releaseLock(purchaseId, Thread.currentThread().getName());
            if (listingLockRequired) {
                manager.releaseLock(purchase.getListing().getId(), Thread.currentThread().getName());
            }
        }
    }

    /* Update the status of a purchase to either processed or fulfilled. Only a lock to the purchase object is required.
    Args:
        status: status: the new status that the user wants to update the purchase to
        purchaseId: the id of the purchase object the request sends
     */
    private void updateStatus(String status, int originalQuantity, String purchaseId) throws DatabaseAlreadyUpdated {

        Connection conn = null;
        // Start of transaction, acquire the write lock
        ExclusiveWriteManager manager = ExclusiveWriteManager.getInstance();

        // Acquires lock for the purchase object
        manager.acquireLock(purchaseId, Thread.currentThread().getName());

        /* Now that we have the lock, read the purchase object from the database to get the most updated
         purchase object */
        Purchase purchase = new PurchaseMapper().readOne(purchaseId);
        Purchase.Status purchase_status_db = purchase.getStatus();

        try {

            // Case 1b: Update the order to fulfilled. Update the order object only
            if (status.equals("fulfilled")){

                /* If the purchase quantity read from the database differs from the original one read, then it means
                 * someone else has updated the database, and this change has to be aborted.*/
                if (originalQuantity != purchase.getQuantity()) {
                    throw new DatabaseAlreadyUpdated("Sorry, someone else has updated the order quantity. Refresh the page, check the values, and update it again.");
                }

                // Handle concurrency. If someone else has already cancelled the order, cannot fulfill it.
                if (purchase_status_db.equals(cancelled)) {
                    throw new DatabaseAlreadyUpdated("Sorry, someone else has already cancelled the order. You cannot fulfill it anymore");
                } else {
                    purchase.setStatus(status);
                    conn = DBConnPool.getInstance().getConnection();
                    new PurchaseMapper().update(purchase, conn);
                }
            }
            // Case 1c: Update the order to processed. Update the order object only
            else if (status.equals("processed")){
                    /* Handle concurrency. If someone else has already
                    1.cancelled the order, cannot process it.
                    2.fulfilled the order, cannot process it (this means status is updated backwards which is not allowed)
                    3. If the quantity has been updated in the database, do NOT process. Refresh the page and check again. 
                    */
                if (purchase_status_db.equals(cancelled) || purchase_status_db.equals(fulfilled)) {
                    throw new DatabaseAlreadyUpdated("Sorry, someone else has already cancelled or fulfilled the order. You cannot process it anymore");
                } else if (purchase.getQuantity() != originalQuantity) {
                    throw new DatabaseAlreadyUpdated("Sorry, someone else has already updated the quantity. Refresh the page and check again");
                } else {
                    purchase.setStatus(status);
                    conn = DBConnPool.getInstance().getConnection();
                    new PurchaseMapper().update(purchase, conn);
                }
            }

            // Case 1d: Bad request. Should not update the status to 'placed' at this stage of the business logic.
            else if (status.equals("placed")) {
                throw new Exception("Bad request. Should not update the status to 'placed'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            manager.releaseLock(purchaseId, Thread.currentThread().getName());
            if (conn != null) {
                DBConnPool.getInstance().releaseConnection(conn);
            }
        }
    }

    /* Update the quantity of a purchase. A lock is required for both the purchase object and the associated listing object
    Args:
        purchaseId: the id of the purchase object the request sends
        originalQuantity: the quantity of the purchase that the user read before sending this POST request
        change: the change of quantity that the user wants for this purchase
    */
    private void updateQuantity(String purchaseId, int originalQuantity, int change) throws InvalidQuantityException, DatabaseAlreadyUpdated{

        Boolean listingLockRequired = false;

        // Start of transaction, acquire the write lock
        ExclusiveWriteManager manager = ExclusiveWriteManager.getInstance();

        // Acquires lock for the purchase object
        manager.acquireLock(purchaseId, Thread.currentThread().getName());

        /* Now that we have the lock, read the purchase object from the database to get the most updated
         purchase object */
        Purchase purchase = new PurchaseMapper().readOne(purchaseId);



        try {
            /* If the purchase quantity read from the database differs from the original one read, then it means
             * someone else has updated the database, and this change has to be aborted.*/
            if (originalQuantity != purchase.getQuantity()) {
                throw new DatabaseAlreadyUpdated("Sorry, someone else has updated the order quantity. Refresh the page, check the values, and update it again.");
            }

            // If someone else has already cancelled/fulfilled/processed the purchase status, cannot update quantity
            if (purchase.getStatus().equals(cancelled) || purchase.getStatus().equals(fulfilled)) {
                throw new DatabaseAlreadyUpdated("Sorry, someone else has either cancelled, processed or fulfilled this order");
            }

            /* Status is OK. Need to check the associated listing's remaining quantity.
            Acquire the lock for the listing associated with the purchase */
            listingLockRequired = true;
            manager.acquireLock(purchase.getListing().getId(), Thread.currentThread().getName());

            // If user wants to buy more, make sure someone else hasn't bought too much (Lazy load happen here too)
            if (change > purchase.getListing().getQuantity()) {
                throw new InvalidQuantityException("Sorry, not enough left.");
            }

            // There is enough stock left. Proceed to update the order quantity
            UnitOfWork.newCurrent();
            UnitOfWork uow = UnitOfWork.getCurrent();

            purchase.updateQuantity(change);
            uow.registerDirty(purchase);

            purchase.getListing().updateQuantity(-change);
            uow.registerDirty(purchase.getListing());
            uow.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            manager.releaseLock(purchaseId, Thread.currentThread().getName());
            if (listingLockRequired) {
                manager.releaseLock(purchase.getListing().getId(), Thread.currentThread().getName());
            }
        }

    }



}
