package MS_Quokka.Controller;

import MS_Quokka.Domain.User;
import MS_Quokka.Mapper.UserMapper;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import MS_Quokka.Utils.DBConnPool;
import MS_Quokka.Utils.Role;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@WebServlet(name = "registerUserController", value = "/register")
public class RegisterUserController extends HttpServlet {
    private String message;
    private UserMapper userMapper;


    public void init() {
        message = "Hello World from User!";
        // Instantiate a UserMapper mapper object
        userMapper = new UserMapper();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("registerUser.jsp");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String password = request.getParameter("password");
        String shippingAddress = request.getParameter("shipping_address");

        System.out.println("Email: " + email);
        System.out.println("Firstname: " + firstname);
        System.out.println("Lastname: " + lastname);
        System.out.println("Password: " + password);
        System.out.println("shippingAddress: " + shippingAddress);

        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        encoder.setEncodeHashAsBase64(false);

        String encodedPassword = encoder.encode(password);

        System.out.println("encodedPassword: " + encodedPassword);

        Connection conn = DBConnPool.getInstance().getConnection();

        UserMapper um = new UserMapper();
        User user = null;
        try {
            if (!um.checkIfEmailExist(email)){
                UUID id = UUID.randomUUID();
                user = new User(id.toString(), email, firstname, lastname, encodedPassword, shippingAddress, Role.USER, null);
                userMapper.create(user, conn);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBConnPool.getInstance().releaseConnection(conn);

        if (user != null){
            response.sendRedirect("login.jsp");
        }
        else{
            request.setAttribute("alertMsg", "Email already exist");
            request.getRequestDispatcher("registerUser.jsp").forward(request, response);
        }


    }

    public void destroy() {
    }
}