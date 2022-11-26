package MS_Quokka.Controller;

import MS_Quokka.Domain.SellerGroup;
import MS_Quokka.Domain.User;
import MS_Quokka.Mapper.UserMapper;
import MS_Quokka.Utils.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "admin", value = "/admin")
public class AdminController extends HttpServlet {
    private String message;
    private UserMapper userMapper;


    public void init() {
        // Instantiate a UserMapper mapper object
        userMapper = new UserMapper();
    }

    // Handle get requests
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        // handle view all users request
        if (action != null && action.equals("users")){
            ArrayList<User> users = null;
            users = userMapper.readList();
            request.setAttribute("users", users);
            request.getRequestDispatcher("viewAllUsers.jsp").forward(request, response);
        }
        // Handle view one user in details request
        else if (request.getParameter("viewAllInfo") != null){
            String userID = request.getParameter("userID");
            String email = request.getParameter("email");
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String shippingAddress = request.getParameter("shippingAddress");
            Role role = Role.valueOf(request.getParameter("role"));
            String sellerGroupID = request.getParameter("sellerGroupID");

            System.out.println("userID is: " + userID);
            System.out.println("email is: " + email);
            System.out.println("firstname is: " + firstname);
            System.out.println("lastname is: " + lastname);
            System.out.println("shippingAddress is: " + shippingAddress);
            System.out.println("sellerGroupID is: " + sellerGroupID);

            SellerGroup sellerGroup = null;
            if (sellerGroupID != null){
                sellerGroup = new SellerGroup(sellerGroupID, null);
            }

            User user = new User(userID, email, firstname, lastname, null, shippingAddress, role, sellerGroup);
            if (user.getRole() == Role.SELLER) {
                user.getSellerGroup().getName();
            }

            request.setAttribute("user", user);
            request.getRequestDispatcher("viewOneUser.jsp").forward(request, response);

        }
        // Handle view welcome page request
        else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User admin = userMapper.readOneByEmail(authentication.getName());

            request.setAttribute("admin", admin);
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        }
    }
}
