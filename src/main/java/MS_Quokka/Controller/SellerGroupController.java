package MS_Quokka.Controller;

import MS_Quokka.Domain.User;
import MS_Quokka.Mapper.UserMapper;
import MS_Quokka.Mapper.SellerGroupMapper;
import MS_Quokka.Domain.SellerGroup;
import MS_Quokka.Utils.DBConnPool;
import MS_Quokka.Utils.ExclusiveWriteManager;
import MS_Quokka.Utils.Role;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "sellGroupServlet", value = "/sellerGroup")
public class SellerGroupController extends HttpServlet {
    private String message;
    private SellerGroupMapper sellerGroupMapper;
    private UserMapper userMapper;
    ExclusiveWriteManager lockManager;

    public void init() {
        sellerGroupMapper = new SellerGroupMapper();
        userMapper = new UserMapper();
        lockManager = ExclusiveWriteManager.getInstance();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // No parameter: Get all seller groups and display that on the frontend page
        if (request.getParameterMap().isEmpty()) {
            System.out.println("Executing request.getParamMap.isEmpty");
            ArrayList<SellerGroup> allSellerGroups;

            allSellerGroups = sellerGroupMapper.readList();
            System.out.println("The length of allSellerGroups is: " + Integer.toString(allSellerGroups.size()));
            request.setAttribute("allSellerGroups", allSellerGroups);
            request.getRequestDispatcher("sellerGroup.jsp").forward(request, response);


        }
        // Parameters are present - get the specific SellerGroup
        else {
            if (request.getParameterMap().containsKey("id") && request.getParameterMap().containsKey("name")){
                // Get the Seller Group's Id and name from the request
                String sellerGroupId = request.getParameter("id");
                String sellerGroupName = request.getParameter("name");

                // Find all users who belong to this Seller Group
                ArrayList<User> usersInSellerGroup = null;
                try {
                    usersInSellerGroup = userMapper.findUsersInSellerGroup(sellerGroupId);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                SellerGroup sellerGroup = new SellerGroup(sellerGroupId, sellerGroupName);

                // Send the frontend the data it need about this Seller Group
                request.setAttribute("sellerGroup", sellerGroup);
                request.setAttribute("usersInSellerGroup", usersInSellerGroup);
                request.getRequestDispatcher("oneSellerGroup.jsp").forward(request, response);
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // POST method - create a new seller Group
        if (request.getParameter("_method").equals("POST")) {
            String newName = request.getParameter("name");
            UUID id = UUID.randomUUID();
            SellerGroup sellerGroup = new SellerGroup(id.toString(), newName);

            Connection conn = DBConnPool.getInstance().getConnection();

            try {
                sellerGroupMapper.create(sellerGroup, conn);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBConnPool.getInstance().releaseConnection(conn);
            }

            // After creating the new Seller Group in db, go back to the Seller Group dashboard
            response.sendRedirect(request.getContextPath() + "/sellerGroup");
        }
        // PUT method - modify the seller group
        else if (request.getParameter("_method").equals("PUT")) {
            doPUT(request, response);
        }

    }

    public void doPUT(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("Executing inside doPut");

        // Getting parameters from frontend. Set up necessary attributes
        String sellerGroupId = request.getParameter("_id");
        String sellerGroupName = request.getParameter("_originalName");
        SellerGroup sellerGroup;
        User userToAdd, userToRemove;
        Boolean legalAction = true;

        Connection conn = DBConnPool.getInstance().getConnection();

        // We need to make sure this action is legal - it must be an existing user who is NOT a seller yet.
        String newSellerEmail = request.getParameter("newSellerEmail");
        if (!newSellerEmail.isEmpty()){
            try {
                if (!userMapper.checkIfEmailExist(newSellerEmail)){
                    legalAction = false;
                }
                else {
                    if (alreadyASeller(newSellerEmail)) {
                        legalAction = false;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            // The action is not legal - throw error
            if (!legalAction) {
                throw new ServletException("400 Bad Request");
            }
            // The action is legal - proceed
            else {
                // Update the SellerGroup name (i.e. the Seller Group object)
                if (!request.getParameter("newName").isEmpty()) {
                    sellerGroupName = request.getParameter("newName");

                    sellerGroup = new SellerGroup(sellerGroupId, sellerGroupName);
                    try {
                        sellerGroupMapper.update(sellerGroup, conn);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Update successful!");
                } else {
                    sellerGroup = new SellerGroup(sellerGroupId, sellerGroupName);
                }

                // Add user to the Seller Group
                if (!newSellerEmail.isEmpty()){

                    String userID = userMapper.readOneByEmail(newSellerEmail).getId();

                    // Acquires lock for the user record
                    lockManager.acquireLock(userID, Thread.currentThread().getName());

                    userToAdd = constructUserByEmail(newSellerEmail, sellerGroup);
                    try {
                        userMapper.update(userToAdd, conn);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        // Regardless of the outcome, release the lock
                        lockManager.releaseLock(userID, Thread.currentThread().getName());
                    }
                }

                // Remove the seller from the SellerGroup
                String removeSellerId = "";
                if (request.getParameter("removeSellerId") != null && !request.getParameter("removeSellerId").isEmpty()) {
                    removeSellerId = request.getParameter("removeSellerId");

                    // Acquires lock for the user record
                    lockManager.acquireLock(removeSellerId, Thread.currentThread().getName());

                    userToRemove = constructUserByID(removeSellerId);
                    userToRemove.setIsEditing();
                    try {
                        userMapper.update(userToRemove, conn);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        // Regardless of the outcome, release the lock
                        lockManager.releaseLock(removeSellerId, Thread.currentThread().getName());
                    }
                }

                // Redirect back to the Seller Group Page
                String relativePath = String.format("sellerGroup?id=%s&name=%s", sellerGroupId, sellerGroupName);
                response.sendRedirect(request.getContextPath() + "/" + relativePath);
            }
        } catch(Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } finally {
            DBConnPool.getInstance().releaseConnection(conn);
        }
    }

    // Check that a user with the given email is already a Seller
    private Boolean alreadyASeller(String email) throws SQLException {
        User userInDb = userMapper.readOneByEmail(email);
        if (userInDb.getSellerGroup().getId() == null) {
            return false;
        } else {
            return true;
        }
    }


    private User constructUserByEmail(String sellerEmail, SellerGroup newSellerGroup) {
        User userFromDB = userMapper.readOneByEmail(sellerEmail);
        return new User(userFromDB.getId(), sellerEmail, userFromDB.getFirstname(), userFromDB.getLastname(), userFromDB.getPassword(), userFromDB.getShippingAddress(), Role.SELLER, newSellerGroup);
    }

    private User constructUserByID(String userID) {
        User userFromDB = null;
        userFromDB = userMapper.readOne(userID);
        return new User(userID, userFromDB.getEmail(), userFromDB.getFirstname(), userFromDB.getLastname(), userFromDB.getPassword(), userFromDB.getShippingAddress(), Role.USER, null);
    }

}