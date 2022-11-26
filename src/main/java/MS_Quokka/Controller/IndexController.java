package MS_Quokka.Controller;

import MS_Quokka.Domain.User;
import MS_Quokka.Mapper.UserMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "index", value = "/index")
public class IndexController extends HttpServlet {
    private String message;
    private UserMapper userMapper;

    public void init() {

        userMapper = new UserMapper();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("In index controller!!!");
        response.sendRedirect("index.jsp");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

}
