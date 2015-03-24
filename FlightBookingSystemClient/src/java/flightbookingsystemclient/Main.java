/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightbookingsystemclient;

import ejb.ServerBeanRemote;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import javax.ejb.EJB;

/**
 *
 * @author Peter K W
 */
public class Main {
    @EJB
    private static ServerBeanRemote serverBean;

    public static void main(String[] args) {
        // TODO code application logic here
        Main client = new Main();
        
        boolean flag = true;
        while(flag){
            Scanner sc = new Scanner(System.in);
            System.out.println("\n1. Add User\n2. Delete User\n3. Add Flights\n4. Update Flights\n5. Delete Flights\n6. Add Schedules");
            int command = sc.nextInt();
            sc.nextLine();
            switch(command){
                case 1:
                    client.addUser(sc);
                    break;
                case 2:
                    client.delUser(sc);
                    break;
                case 3:
                    client.addFlight(sc);
                    break;
                case 4:
                    client.updateFlight(sc);
                    break;
                case 5:
                    client.deleteFlight(sc);
                    break;
                case 6:
                    client.addSchedule(sc);
                    break;
                default:
                    break;
            }
        }
    }
    public void addUser(Scanner sc){
        System.out.print("Enter user name: ");
        String username = sc.next();
        System.out.print("Enter default password: ");
        String password = sc.next();
        System.out.print("Enter contact number: ");
        int contact = sc.nextInt();
        System.out.print("Enter email address: ");
        String email = sc.next().trim();
        if(serverBean.addUser(username, password, contact, email)){
            System.out.println("Account has been successfully created!");
        }
        else{
            System.out.println("Error! Account exists!");
        }
    }
    public void delUser(Scanner sc){
        System.out.print("Enter username to delete: ");
        String username = sc.next();
        int result = serverBean.delUser(username);
        if(result==1){
            System.out.println("Account has been successfully deleted!");
        }
        else if(result==2) {
            System.out.println("Error! Accoung does not exist!");
        }
        else{
            System.out.println("Error! Account is associated with booking or request, cannot be deleted!");
        }
    }
    public void addFlight(Scanner sc){
        System.out.print("Enter flight number: ");
        String flightNo = sc.nextLine();
        System.out.print("Enter departure city: ");
        String depart = sc.nextLine();
        System.out.print("Enter arrival city: ");
        String arriv = sc.nextLine();
        System.out.print("Enter aircraft type: ");
        String aircraft = sc.nextLine();
        System.out.print("Enter total seats: ");
        int seats = sc.nextInt();
        if(serverBean.addFlight(flightNo, depart, arriv, aircraft, seats)){
            System.out.println("Flight has been successfully created!");
        }
        else{
            System.out.println("Error! Flight exists!");
        }
    }
    public void updateFlight(Scanner sc){
        System.out.print("Enter flight number: ");
        String flightNo = sc.nextLine();
        int check = serverBean.searchFlight(flightNo);
        if(check==0){
            System.out.println("Flight is not found");
            return;
        }
        if(check==1){
            System.out.print("Enter new departure city: ");
            String depart = sc.nextLine();
            System.out.print("Enter new arrival city: ");
            String arriv = sc.nextLine();
            System.out.print("Enter new aircraft type: ");
            String aircraft = sc.nextLine();
            System.out.print("Enter new total seats: ");
            int seats = sc.nextInt();
            serverBean.updateFlightAll(flightNo, depart, arriv, aircraft, seats);
            System.out.println("Flight has been successfully updated.");
        }
        else{
            System.out.print("Enter new departure city: ");
            String depart = sc.nextLine();
            System.out.print("Enter new aircraft type: ");
            String aircraft = sc.nextLine();
            System.out.print("Enter new total seats: ");
            int seats = sc.nextInt();
            if(serverBean.updateFlightSchedule(flightNo, aircraft, seats)){
                System.out.println("Flight has been successfully updated.");
            }
            else{
                System.out.println("Error! Total seats are less than booked seats!");
            }
        }
    }
    public void deleteFlight(Scanner sc){
        System.out.print("Enter flight number: ");
        String flightNo = sc.nextLine();
        int result = serverBean.deleteFlight(flightNo);
        if(result==0){
            System.out.println("Error! Flight does not exist!");
        }
        else if(result == 1){
            System.out.println("Flight has been successfully deleted!");
        }
        else{
            System.out.println("Error! Flight is associated with schedule!");
        }
    }
    public void addSchedule(Scanner sc){
        System.out.print("Enter flight number: ");
        String flightNo = sc.nextLine();
        System.out.print("Enter departure time: ");
        String depart = sc.nextLine();
        int check = serverBean.checkSchedule(flightNo, depart);
        if(check==0){
            System.out.println("Error! Flight does not exist!");
        }
        else if(check==1){
            System.out.print("Enter arrival time: ");
            String arriv = sc.nextLine();
            System.out.print("Enter price: ");
            double price = sc.nextDouble();
            serverBean.addSchedule(flightNo, depart, arriv, price);
            System.out.println("Schedule has been successfully created.");
        }
        else{
            System.out.println("Error! Flight has been scheduled on the same day!");
        }
    }
    public void updateSchedule(Scanner sc){
        System.out.print("Enter flight number: ");
        String flightNo = sc.nextLine();
        System.out.print("Enter departure date: ");
        String depart = sc.nextLine();
        int check1 = serverBean.checkScheduleBooking(flightNo, depart);
        if(check1==0){
            System.out.println("Error! Flight does not exist!");
        }
        else if(check1==1){
            System.out.print("Enter new departure time: ");
            String newDepart = sc.nextLine();
            int check2 = serverBean.checkSchedule(flightNo, newDepart);
            if(check2==1){
                System.out.print("Enter new arrival time: ");
                String newArriv = sc.nextLine();
                System.out.print("Enter new price: ");
                double newPrice = sc.nextDouble();
                serverBean.updateScheduleAll(flightNo, depart, newDepart, newArriv, newPrice);
                System.out.println("Flight is successfully updated!");
            }
            else{
                System.out.println("Error! Flight has been scheduled on the same day.");
            }
        }
        else{
            System.out.println("Schedule is associated with booking!");
            System.out.print("Enter new price: ");
            double newPrice = sc.nextDouble();
            serverBean.updateScheduleBooking(flightNo, depart, newPrice);
            System.out.println("Flight is successfully updated!");
        }
    }
    public void deleteSchedule(Scanner sc){
        System.out.print("Enter flight number: ");
        String flightNo = sc.nextLine();
        System.out.print("Enter departure date: ");
        String depart = sc.nextLine();
        int result = serverBean.deleteSchedule(flightNo, depart);
        if(result==0){
            System.out.println("Error! Schedule does not exist!");
        }
        else if(result==1){
            System.out.println("Schedule has been successfully deleted.");
        }
        else{
            System.out.println("Error! Schedule is associated with booking.");
        }
    }
    public void viewBookings(){
        List<Vector> list = serverBean.viewBookings();
        int count = 1;
        for(Object o: list){
            Vector temp = (Vector) o;
            
            System.out.println("\n\n============== Booking "+count+" ===============");
            System.out.println("Booking number: "+temp.get(0));
            System.out.println("Booking time: "+temp.get(1));
            System.out.println("Username: "+temp.get(2));
            System.out.println("Contact number: "+temp.get(3));
            System.out.println("Email: "+temp.get(4));
            List<Vector> schedules = (List<Vector>) temp.get(5);
            System.out.print("\n");
            for(int i=0; i<schedules.size(); i++){
                Vector sch = schedules.get(i);
                System.out.println("Flight "+(i+1)+" number: "+sch.get(0));
                System.out.println("Flight "+(i+1)+" departure time: "+sch.get(1));
            }
            System.out.println("--------- Passengers ---------");
            List<Vector> passengers = (List<Vector>) temp.get(6);
            for(int i=0; i<passengers.size(); i++){
                Vector psg = passengers.get(i);
                System.out.println((i+1)+" Passport number: "+psg.get(0));
                System.out.println((i+1)+" Name: "+ psg.get(1));
                System.out.println((i+1)+" Gender: "+psg.get(2));
                System.out.println((i+1)+" Date of birth: "+psg.get(3));
                System.out.print("\n");
            }
            System.out.println("--------- Payment ---------");
            Vector payment = (Vector) temp.get(7);
            System.out.println("Total amount: $"+payment.get(0));
            if((Boolean)payment.get(1)){
                System.out.println("Payment status: Paid");
                System.out.println("Payment time: "+payment.get(2));
                System.out.println("Card type: "+payment.get(3));
                System.out.println("Card number: "+payment.get(4));
                System.out.println("Holder name: "+payment.get(5));
            }
            else{
                System.out.println("Payment status: Unpaid");
            }
            count++;
        }
    }
    public void viewSchedules(){
        int count=1;
        for(Object o: serverBean.viewSchedules()){
            Vector temp = (Vector) o;
            System.out.println("\n========== Schedule "+count+" ==========");
            System.out.println("Flight number: "+temp.get(0));
            System.out.println("Departure time: "+temp.get(1));
            System.out.println("Arrival time: " + temp.get(2));
            System.out.println("Available seats: "+temp.get(3));
            System.out.println("Price: "+temp.get(4));
            count++;
        }
    }
    public void viewFlights(){
        int count=1;
        for(Object o: serverBean.viewFlights()){
            Vector temp = (Vector) o;
            System.out.println("\n========== Flight "+count+" ==========");
            System.out.println("Flight number: "+temp.get(0));
            System.out.println("Departure city: "+temp.get(1));
            System.out.println("Arrival city: " + temp.get(2));
            System.out.println("Aircraft type: "+temp.get(3));
            System.out.println("Total seats: "+temp.get(4));
        }
    }
    public void processRequest(Scanner sc){
        for(Object o: serverBean.viewRequests()){
            Vector temp = (Vector) o;
            System.out.println(temp.get(0)+": "+temp.get(1)+", "+temp.get(2)+", \""+ temp.get(3)+"\"");
            
        }
        System.out.print("\nEnter the request ID: ");
        int id = sc.nextInt();
        System.out.print("Change the status to: ");
        String status = sc.next();
        sc.nextLine();
        System.out.print("Comments: ");
        String comment = sc.nextLine();
        if(serverBean.processRequest(id, status, comment)){
            System.out.println("Request has been updated.");
        }
        else{
            System.out.println("ID cannot be found.");
        }
    }
}
