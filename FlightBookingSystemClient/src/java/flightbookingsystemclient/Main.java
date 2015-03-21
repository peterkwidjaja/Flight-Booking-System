/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightbookingsystemclient;

import ejb.ServerBeanRemote;
import java.util.Scanner;
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
        Scanner sc = new Scanner(System.in);
        boolean flag = true;
        while(flag){
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
}
