package MS_Quokka.Controller;

import MS_Quokka.Domain.User;
import MS_Quokka.Mapper.UserMapper;
import MS_Quokka.Utils.DBConnPool;
import MS_Quokka.Utils.LockedMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "user", value = "/user")

public class UserController extends HttpServlet {
    private String message;
    private UserMapper userMapper;


    public void init() {
        // Instantiate a UserMapper mapper object
        userMapper = new UserMapper();
    }

    // Handle display user profile request
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User user = userMapper.readOneByEmail(email);
        request.setAttribute("user", user);
        request.getRequestDispatcher("userProfile.jsp").forward(request, response);
    }

    // Handle update user profile request
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userId = request.getParameter("id");
        String newFirstname = request.getParameter("changeFirstname");
        String newLastname = request.getParameter("changeLastname");
        String newPassword = request.getParameter("changePassword");
        String newShippingAddress = request.getParameter("changeShippingAddress");

        Connection conn = DBConnPool.getInstance().getConnection();

        // Acquires lock for the listing record
        try (LockedMapper<User> lockedMapper = new LockedMapper<User>(userId, User.class)) {
            User userToUpdate = lockedMapper.readOne(userId);

            if (newFirstname != null && !newFirstname.equals("")) {
                userToUpdate.setFirstname(newFirstname);
            }

            if (newLastname != null && !newLastname.equals("")) {
                userToUpdate.setLastname(newLastname);
            }

            if (newShippingAddress != null && !newShippingAddress.equals("")) {
                userToUpdate.setShippingAddress(newShippingAddress);
            }

            if (newPassword != null && !newPassword.equals("")) {
                userToUpdate.setPassword(newPassword);
            }

            lockedMapper.update(userToUpdate, conn);

            response.sendRedirect("/user");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnPool.getInstance().releaseConnection(conn);
        }
    }

}
