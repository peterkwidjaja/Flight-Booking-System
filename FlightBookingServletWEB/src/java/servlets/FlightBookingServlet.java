/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import ejb.ServerBeanRemote;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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
            data=null;  //Reset data
            Cookie[] cookies = request.getCookies();
            if(cookies!=null)
                processCookie(cookies);
            
            request.setAttribute("user", currentUser);
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
                    System.out.println("Register error!");
                }
            }
            else if(page.equals("login")){
                System.out.println("Opening login page");
                String method = request.getMethod();
                if("POST".equals(method)){
                    String hashedPass = login(request);
                    if(!hashedPass.equals("")){
                        request.setAttribute("status", true);
                        Cookie loginCookie = new Cookie("UID",hashedPass+currentUser); //The cookie contains hashed password and username. This is a very vulnerable method but is used for simplicity purpose.
                        response.addCookie(loginCookie);
                        System.out.println("Login Successful");
                    }
                    else{
                        request.setAttribute("status", false);
                        System.out.println("Login Unsuccessful");
                    }                    
                }
            }
            else if(page.equals("logout")){
                logout(request, response);
                page = "index";
            }
            else if(page.equals("search")){
                String method = request.getMethod();
                if("POST".equals(method)){
                    search(request,response);
                    request.setAttribute("data", data);
                }
                else{
                    request.setAttribute("data", data);
                }
            }
            else if(page.equals("book")){
                if("POST".equals(request.getMethod())){
                    ArrayList<Object> list = new ArrayList<>();
                    int seats = Integer.parseInt(request.getParameter("seats"));
                    String scheduleChoice = request.getParameter("optionRadios");
                    list.add(seats);
                    list.add(scheduleChoice);
                    request.setAttribute("data", list);
                    page = "index";
                }
            }
            else if(page.equals("confirmBook")){
                if("POST".equals(request.getMethod())){
                    confirmBook(request);
                }
            }
            
            else if(page.equals("payment")){
                if("POST".equals(request.getMethod())){
                    
                }
                else{
                    viewPayment(request,response);
                }
            }
            else if(page.equals("makePayment")){
                if("GET".equals(request.getMethod())){
                    
                }
                else{
                    makePayment(request,response);
                }
            }
            else if(page.equals("update")){
                data = getUserInfo();
                request.setAttribute("data", data);
            }
            else if(page.equals("changePass")){
                int result = changePassword(request);
                data = getUserInfo();
                request.setAttribute("data", data);
                request.setAttribute("resultPass", result);
                page = "update";
            }
            else if(page.equals("updateDetails")){
                int result = changeUserDetails(request);
                data = getUserInfo();
                request.setAttribute("data", data);
                request.setAttribute("resultDetails", result);
                page = "update";
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
    private void search(HttpServletRequest request, HttpServletResponse response){
        String departureCity = request.getParameter("departCity");
        String departDate = request.getParameter("departDate");
        String dates[] = departDate.split("-");
        departDate = dates[2]+"/"+dates[1]+"/"+dates[0];
        String arrivalCity = request.getParameter("arrivCity");
        String seats = request.getParameter("passenger");
        
        ArrayList<Vector> list = (ArrayList) serverBean.searchSchedule(departDate, departureCity, arrivalCity, Integer.parseInt(seats));
        data = list;
        request.setAttribute("seats", seats);
    }
    
    private void confirmBook(HttpServletRequest request){
        String[] schedule = request.getParameter("schedule").split(" ");
        int seats = Integer.parseInt(request.getParameter("seats"));
        ArrayList<Vector> list= new ArrayList<Vector>();
        for(int i=1; i<=seats; i++){
            Vector v = new Vector();
            String name = request.getParameter("name"+i);
            String passport = request.getParameter("passport"+i);
            String gender = request.getParameter("gender"+i);
            String dob = request.getParameter("dob"+i);
            String dates[] = dob.split("-");
            dob = dates[2]+"/"+dates[1]+"/"+dates[0];
            v.add(name);
            v.add(passport);
            v.add(gender);
            v.add(dob);
            list.add(v);
        }
        int[] scheduleID = new int[schedule.length];
        for(int i=0; i<scheduleID.length; i++){
            scheduleID[i]=Integer.parseInt(schedule[i]);
        }
        serverBean.createBooking(currentUser, scheduleID, seats, list);
        
    }
    private void processCookie(Cookie[] cookies){
        for(Cookie cookie: cookies){
            if("UID".equals(cookie.getName())){
                String uid = cookie.getValue();
                String id = uid.substring(32);
                uid = uid.substring(0,32);
                login(id, uid);
            }
        }
    }
    
    private boolean register(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try{
            password = hash(password);
        }
        catch(NoSuchAlgorithmException e){
            System.err.println(e);
        }
        int contactNo = Integer.parseInt(request.getParameter("contactNo"));
        String email = request.getParameter("email");
        if(serverBean.addUser(username, password, contactNo, email)){
            return true;
        }
        return false;
    }
    private String login(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try{
            password = hash(password);
        }
        catch(NoSuchAlgorithmException e){
            System.err.println(e);
        }
        if(serverBean.login(username, password)){
            currentUser = username;
            return password;
        }
        return "";
    }
    private void login(String username, String password){
        if(serverBean.login(username, password)){
            currentUser = username;
        }
    }
    
    private String hash(String password) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<bytes.length; i++){
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    
    private void logout(HttpServletRequest request, HttpServletResponse response){
        currentUser = "";
        Cookie logoutCookie = new Cookie("UID","");
        logoutCookie.setMaxAge(0);
        response.addCookie(logoutCookie);
        request.setAttribute("user", currentUser);
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
        if(newPass.equals("")){
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
    
    private void viewPayment(HttpServletRequest request, HttpServletResponse response){
        List<Vector> result = serverBean.getUnpaidBooking(currentUser);
        request.setAttribute("data", result);
    }
    private void makePayment(HttpServletRequest request, HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("bookingID"));
        String cardtype = request.getParameter("cardType");
        long cardNo = Long.parseLong(request.getParameter("cardNo"));
        String name = request.getParameter("name");
        serverBean.makePayment(id, cardtype, cardNo, name);
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
