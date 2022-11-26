package MS_Quokka.Controller;

import MS_Quokka.Domain.*;
import MS_Quokka.Mapper.*;
import MS_Quokka.Utils.DBConnPool;
import MS_Quokka.Utils.ExclusiveWriteManager;
import MS_Quokka.Utils.Role;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "Listing Page", value = "/listing")
public class ListingController extends HttpServlet {
    private FixPriceListingMapper fixPriceListingMapper = new FixPriceListingMapper();
    private AuctionListingMapper auctionListingMapper = new AuctionListingMapper();
    private SellerGroupMapper sellerGroupMapper = new SellerGroupMapper();
    private ProductMapper productMapper = new ProductMapper();
    private UserMapper userMapper = new UserMapper();
    private BidMapper bidMapper = new BidMapper();
    private ExclusiveWriteManager lockManager = ExclusiveWriteManager.getInstance();


    public void init() {}


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //Pre setup
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd;

        //get params
        String page = request.getParameter("page");

        //Auth - pre setup
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Role role;

        try{
            switch(page){

                //retrieve createListing page
                case "createListing":

                    //access control
                    role = getUserRole();
                    if(role == Role.SELLER){

                        //get user details
                        String userEmail = authentication.getName();
                        User user = userMapper.readOneByEmail(userEmail);

                        //get user seller group details
                        ArrayList<SellerGroup> sellerGroups = new ArrayList<SellerGroup>();
                        sellerGroups.add(sellerGroupMapper.readOne(user.getSellerGroup().getId()));

                        //get all products
                        ArrayList<Product> products = productMapper.readList();

                        //set up the jsp view
                        request.setAttribute("sellerGroups", sellerGroups);
                        request.setAttribute("products", products);
                        rd=request.getRequestDispatcher("createListing.jsp");
                        rd.include(request, response);
                    }else{
                        SecurityContextHolder.clearContext();
                        response.sendRedirect("login.jsp");
                        break;
                    }

                    break;

                //view listing page, either from the searching result or all listing
                case "viewListings":

                    //access control
                    role = getUserRole();

                    String search = request.getParameter("search");

                    ArrayList<FixPriceListing> fixPriceListings = null;
                    ArrayList<AuctionListing> auctionListings = null;

                    //display searching result if using has input the searching keywords
                    if(search == null){
                        fixPriceListings = fixPriceListingMapper.readListBuyer();
                        auctionListings = auctionListingMapper.readListBuyer();

                    //display all listing
                    }else{
                        fixPriceListings = fixPriceListingMapper.search(search);
                        auctionListings = auctionListingMapper.search(search);
                    }

                    request.setAttribute("fixPriceListings", fixPriceListings);
                    request.setAttribute("auctionListings", auctionListings);
                    rd=request.getRequestDispatcher("viewListings.jsp");
                    rd.include(request, response);

                    break;

                //view one particular listing by listing id
                case "viewListing":

                    //access control
                    role = getUserRole();

                    String listingId = request.getParameter("id");
                    String listingType = request.getParameter("listingType");

                    if(listingType.equals("fixPrice")) {

                        FixPriceListing fixPriceListing = new FixPriceListing(listingId, null, null, null, null, null, null, null);

                        if( !fixPriceListing.isVisibleForBuyer()) throw new ServletException("410 The requested item is no longer available");

                        request.setAttribute("fixPriceListing", fixPriceListing);
                        rd=request.getRequestDispatcher("viewListingFixPrice.jsp");
                        rd.include(request, response);

                    }else{
                        AuctionListing auctionListing = new AuctionListing(listingId, null, null, null, null, null, null, null, null, null, null, null);

                        if( !auctionListing.isVisibleForBuyer()) throw new ServletException("410 The requested item is no longer available");

                        request.setAttribute("auctionListing", auctionListing);
                        rd=request.getRequestDispatcher("viewListingAuction.jsp");
                        rd.include(request, response);
                    }

                    break;

                case "manageListings":

                    //access control
                    role = getUserRole();

                    if(role == Role.USER) {
                        SecurityContextHolder.clearContext();
                        response.sendRedirect("login.jsp");
                        break;
                    }

                    fixPriceListings = new ArrayList<FixPriceListing>();
                    auctionListings = new ArrayList<AuctionListing>();

                    if(role == Role.SELLER){

                        //get user details
                        String userEmail = authentication.getName();
                        User user = userMapper.readOneByEmail(userEmail);

                        fixPriceListings = fixPriceListingMapper.readListSeller(user.getSellerGroup().getId());
                        auctionListings = auctionListingMapper.readListSeller(user.getSellerGroup().getId());
                    }
                    if(role == Role.ADMIN){

                        fixPriceListings = fixPriceListingMapper.readList();
                        auctionListings = auctionListingMapper.readList();
                    }

                    request.setAttribute("userRole", role);
                    request.setAttribute("fixPriceListings", fixPriceListings);
                    request.setAttribute("auctionListings", auctionListings);
                    rd=request.getRequestDispatcher("manageListings.jsp");
                    rd.include(request, response);

                    break;

                case "manageListing":

                    //access control
                    role = getUserRole();

                    if(role == Role.USER) {
                        SecurityContextHolder.clearContext();
                        response.sendRedirect("login.jsp");
                        break;
                    }

                    listingId = request.getParameter("id");
                    listingType = request.getParameter("listingType");

                    if(listingType.equals("fixPrice")) {
                        FixPriceListing fixPriceListing = fixPriceListingMapper.readOne(listingId);

                        if( role == Role.SELLER && !fixPriceListing.isVisibleForSeller() ) throw new ServletException("403 forbidden");

                        request.setAttribute("userRole", role);
                        request.setAttribute("fixPriceListing", fixPriceListing);
                        rd=request.getRequestDispatcher("manageListingFixPrice.jsp");
                        rd.include(request, response);
                    }else{
                        AuctionListing auctionListing = auctionListingMapper.readOne(listingId);

                        if( role == Role.SELLER && !auctionListing.isVisibleForSeller() ) throw new ServletException("403 forbidden");

                        request.setAttribute("userRole", role);
                        request.setAttribute("auctionListing", auctionListing);
                        rd=request.getRequestDispatcher("manageListingAuction.jsp");
                        rd.include(request, response);
                    }

                    break;

                case "manageAuctionBids":

                    //access control
                    role = getUserRole();

                    if(role == Role.USER) {
                        SecurityContextHolder.clearContext();
                        response.sendRedirect("login.jsp");
                        break;
                    }

                    listingId = request.getParameter("id");

                    ArrayList<String> bids = bidMapper.getAllUsersThatHasMadeABid(listingId);

                    request.setAttribute("allBids", bids);
                    rd=request.getRequestDispatcher("manageAuctionBids.jsp");
                    rd.include(request, response);

                    break;

                default:
                    throw new ServletException("404 page not found");
            }
        }catch(Exception e){
            request.setAttribute("errorMessage", e.getMessage());
            rd=request.getRequestDispatcher("error.jsp");
            rd.include(request, response);
        };
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //pre setup
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd;

        //Post set up
        String postType = request.getParameter("postType");

        //Auth - pre setup
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Role role;

        Connection conn = DBConnPool.getInstance().getConnection();

        try {
            switch(postType) {
                case "createListing":

                    role = getUserRole();

                    if(role != Role.SELLER) {
                        SecurityContextHolder.clearContext();
                        response.sendRedirect("login.jsp");
                    } else {
                        System.out.println("after the role");
                        //get inputs
                        String sellerGroupId = request.getParameter("sellerGroupId");
                        String listingType = request.getParameter("listingType");
                        String listingTitle = request.getParameter("listingTitle");
                        String productId = request.getParameter("productId");
                        Integer quantity = Integer.valueOf(request.getParameter("quantity"));
                        Double price = Double.valueOf(request.getParameter("price"));
                        String description = request.getParameter("description");

                        //input validation
                        if (price <= 0 || price == null || quantity == null || sellerGroupId == "" || listingType == ""
                                || listingTitle == "" || productId == "" || quantity <= 0)
                            throw new IllegalArgumentException("input error");

                        if ( listingType.equals("auction") && quantity != 1)  throw new IllegalArgumentException("input error - quantity should equal to 1 for auction listing");

                        Product product = productMapper.readOne(productId);
                        SellerGroup sellerGroup = sellerGroupMapper.readOne(sellerGroupId);

                        if (listingType.equals("fixPrice")) {
                            FixPriceListing fixPriceListing = new FixPriceListing(listingTitle, quantity, price, description, sellerGroup, product);
                            fixPriceListingMapper.create(fixPriceListing, conn);
                            System.out.println("fixPriceListing created: id = " + fixPriceListing.getId());

                        } else {
                            AuctionListing auctionListing = new AuctionListing(listingTitle, quantity, price, description, sellerGroup, product);
                            auctionListingMapper.create(auctionListing, conn);
                            System.out.println("auctionListing created: id = " + auctionListing.getId());
                        }

                        response.sendRedirect(request.getContextPath() + "/listing?page=manageListings");
                    }

                    break;

                case "placeBid":

                    //access control
                    role = getUserRole();
                    if(role == Role.ADMIN) throw new ServletException("403 forbidden");

                    //get inputs
                    double price = Double.valueOf(request.getParameter("price"));
                    String id = request.getParameter("id");

                    //get user details
                    String userEmail = authentication.getName();
                    User user = userMapper.readOneByEmail(userEmail);

                    lockManager.acquireLock(id, Thread.currentThread().getName());

                    AuctionListing auctionListing = auctionListingMapper.readOne(id);
                    auctionListing.placeBid(price, user, conn);

                    lockManager.releaseLock(id, Thread.currentThread().getName());

                    response.sendRedirect(request.getContextPath() + "/listing?page=viewListing&listingType=auction&id=" + auctionListing.getId());

                    break;


                //Admin is able to delete a listing
                case "archiveAuction":

                    //access control
                    role = getUserRole();
                    if(role != Role.ADMIN) throw new ServletException("403 forbidden");

                    id = request.getParameter("id");

                    lockManager.acquireLock(id, Thread.currentThread().getName());

                    auctionListing = auctionListingMapper.readOne(id);
                    auctionListing.setArchive(true);
                    auctionListingMapper.update(auctionListing, conn);

                    lockManager.releaseLock(id, Thread.currentThread().getName());

                    response.sendRedirect(request.getContextPath() + "/listing?page=manageListing&listingType=auction&id=" + auctionListing.getId());

                    break;

                case "archiveFixPrice":

                    //access control
                    role = getUserRole();

                    if(role != Role.ADMIN) throw new ServletException("403 forbidden");

                    id = request.getParameter("id");
                    lockManager.acquireLock(id, Thread.currentThread().getName());

                    FixPriceListing fixPriceListing = fixPriceListingMapper.readOne(id);
                    fixPriceListing.setArchive(true);
                    fixPriceListingMapper.update(fixPriceListing, conn);

                    lockManager.releaseLock(id, Thread.currentThread().getName());

                    response.sendRedirect(request.getContextPath() + "/listing?page=manageListing&listingType=fixPrice&id=" + fixPriceListing.getId());

                    break;

                default:
                    throw new ServletException("404 resource not found");
            }

        }catch(NumberFormatException e) {
            request.setAttribute("errorMessage", "please enter a valid number");
            rd=request.getRequestDispatcher("error.jsp");
            rd.include(request, response);

        } catch(Exception e){
            request.setAttribute("errorMessage", e.getMessage());
            rd=request.getRequestDispatcher("error.jsp");
            rd.include(request, response);
        } finally {
            if (conn != null) {
                DBConnPool.getInstance().releaseConnection(conn);
            }

            lockManager.releaseAllLock(Thread.currentThread().getName());

        }
    }

    //retrieve the user role from the database
    private Role getUserRole() throws AuthenticationCredentialsNotFoundException{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Role role = null;
        try {
            role = userMapper.readOneByEmail(authentication.getName()).getRole();
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("403 Token Not Found");
        }
        return role;
    }

}
