/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import ejb.ServerBeanRemote;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Peter K W
 */
@WebServlet(name = "FlightBookingServlet", urlPatterns = {"/FlightBooking"})
public class FlightBookingServlet extends HttpServlet {
    @EJB
    private ServerBeanRemote serverBean;
    private ArrayList data;
    private String currentUser = "";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void init(){
        System.out.println("FlightBookingServlet: init()");
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FlightBookingServlet: processRequest()");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            RequestDispatcher dispatcher;
            ServletContext servletContext = getServletContext();
            String page = request.getPathInfo().substring(1); //get request page
            
            if(page.equals("register")){
                
            }
            else if(page.equals("registerStatus")){
                if(register(request)){
                    page = "register";
                    request.setAttribute("message", "Success");
                    System.out.println("Register successful!");
                }
                else{
                    page = "register";
                    request.setAttribute("message", "Error");
                    System.out.println("Resgister error!");
                }
            }
            else if(page.equals("login")){
                
            }
            else if(page.equals("loginStatus")){
                if(login(request)){
                    request.setAttribute("status", true);
                    request.setAttribute("user", currentUser);
                    page="index";
                }
                else{
                    request.setAttribute("status", false);
                    request.setAttribute("user", currentUser);
                    page="login";
                }
            }
            else if(page.equals("search")){
                
            }
            else if(page.equals("update")){
                data = getUserInfo();
                request.setAttribute("data", data);
            }
            else if(page.equals("changePass")){
                int result = changePassword(request);
                request.setAttribute("resultPass", result);
                page = "update";
            }
            else if(page.equals("updateDetails")){
                
            }
            else if(page.equals("index") || page.equals("")){
                System.out.println("Open index page...");
                request.setAttribute("user", currentUser);
                page="index";
            }
            else{
                page = "error";
            }
          
            dispatcher = servletContext.getNamedDispatcher(page);
            if(dispatcher == null){
                dispatcher = servletContext.getNamedDispatcher("error");
            }
            dispatcher.forward(request, response);
        }
        catch(Exception e){
            log("Exception in FlightBookingServlet: processRequest()"+e);
        }
    }
    
    private boolean register(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int contactNo = Integer.parseInt(request.getParameter("contactNo"));
        String email = request.getParameter("email");
        if(serverBean.addUser(username, password, contactNo, email)){
            return true;
        }
        return false;
    }
    private boolean login(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(serverBean.login(username, password)){
            currentUser = username;
            return true;
        }
        return false;
    }
    
    private void logout(HttpServletRequest request){
        currentUser = "";
    }
    private boolean isLoggedIn(){
        return !currentUser.equals("");
    }
    
    private ArrayList getUserInfo(){
        if(!currentUser.equals("")){
            ArrayList temp = (ArrayList) serverBean.getUserInfo(currentUser);
            return temp;
        }
        else{
            return null;
        }
    }
    private int changePassword(HttpServletRequest request){
        String oldPass = request.getParameter("oldPass");
        String newPass = request.getParameter("newPass");
        String newPass2 = request.getParameter("newPass2");
        if(!newPass.equals("")){
            return 2;
        }
        if(!newPass.equals(newPass2)){
            return 3;
        }
        if(!serverBean.login(currentUser, oldPass)){
            return 1;
        }
        serverBean.changeUserPass(currentUser, newPass);
        return 0;
    }
    private int changeUserDetails(HttpServletRequest request){
        String contactNo = request.getParameter("contactNo");
        String email = request.getParameter("email");
        if(contactNo.equals("")){
            return 1;
        }
        if(email.equals("")){
            return 2;
        }
        serverBean.changeUserDetails(currentUser, Integer.parseInt(contactNo), email);
        return 0;
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
