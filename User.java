import java.io.*;
import java.util.*;
import java.net.*;

class UserHistory
{
    String restaurantName, customerName, foodName;
    int price, time;
    UserHistory(String restaurantName, String customerName, String foodName, int price, int time)
    {
        this.restaurantName = restaurantName;
        this.customerName = customerName;
        this.foodName = foodName;
        this.price = price;
        this.time = time;
    }
    void getHistory()
    {
        System.out.println(restaurantName);
    }

}

class Action
{
    DataOutputStream dos=null;
    DataInputStream dis=null;
    Customer customer=null;
    Scanner sc= new Scanner(System.in);
    // static String orderId=null;
    Action (DataInputStream dis, DataOutputStream dos , Customer customer)
    {
        this.dis=dis;
        this.dos=dos;
        this.customer=customer;
    }
                  
    void ShowRestaurants()
    {
        System.out.println("All the Available Restaurant Details");
        try {
            dos.writeInt(1);
            System.out.println(dis.readUTF());  
        } catch (Exception e) {}
        
    }    
    void ShowAreaRestaurants()
    {
        System.out.println("Enter the area name you are looking for");
        String area=sc.nextLine();
        try {
            dos.writeInt(2);
            dos.writeUTF(area);
            System.out.println(dis.readUTF());
        } catch (Exception e) {}
        
    }
    
    void ShowRestaurantsArea()
    {
        System.out.println("Enter the Restaurant name you are looking for");
        String rest=sc.nextLine();
        try {
            dos.writeInt(3);
            dos.writeUTF(rest);
            System.out.println(dis.readUTF());
        } catch (Exception e) {}
        
    }
    void showMenu()
    {

        System.out.println("Enter the Restaurant name and area you are looking for");
        String rest=sc.nextLine();
        String area=sc.nextLine();
        try {
            dos.writeInt(4);
            dos.writeUTF(rest);
            dos.writeUTF(area);
            System.out.println(dis.readUTF());
        } catch (Exception e) {}
    }
    
    void SearchCusine()
    {
        System.out.println("Enter the cusine name and area you are looking for");
        String cusineName =sc.nextLine();
        String area=sc.nextLine();
        try {
            dos.writeInt(5);
            dos.writeUTF(cusineName);
            dos.writeUTF(area);
            System.out.println(dis.readUTF());
        } catch (Exception e) {}
    }
    void ShowMenuArea()
    {
        System.out.println("Enter the area name you are looking for");
        String area=sc.nextLine();
        try {
            dos.writeInt(6);
            dos.writeUTF(area);
            System.out.println(dis.readUTF());
        } catch (Exception e) {}
        
    }
    
    void searchMinCostFood()
    {
        System.out.println("Enter the cusine name and area you are looking for");
        String cusineName =sc.nextLine();
        String area=sc.nextLine();
        try {
            dos.writeInt(7);
            dos.writeUTF(cusineName);
            dos.writeUTF(area);
            System.out.println(dis.readUTF());
        } catch (Exception e) {}
        
    }
    boolean placeOrder()
    {
        System.out.println("Enter the Restaurant name and area you are looking for");
        boolean status = false;
        String rest=sc.nextLine();
        String area=sc.nextLine();
        try {
            dos.writeInt(8);
            dos.writeUTF(rest);
            dos.writeUTF(area);
            if(dis.readBoolean())
            {
                System.out.println(dis.readUTF());
                System.out.println("Select Your food from Menu ");
                String food = sc.nextLine();
                dos.writeUTF(food);
                if(dis.readBoolean())
                {
                    System.out.println(dis.readUTF());
                    int price=dis.readInt();
                    boolean flag=false;
                    boolean confirmation=false;
                    do
                    {
                        flag=false;
                        System.out.println("Please Enter the "+price+" rupees to confirm Your Order");
                        int amount=sc.nextInt();
                        if(amount==price)
                        {
                            confirmation=true;                            
                        }
                        else
                        {
                            flag=true;
                            System.out.println("Please Enter correct amount");
                            System.out.println("If You want to quit the order, enter Y else N");
                            char confirm=sc.next().charAt(0);
                            if(confirm=='Y')
                            {
                                flag=false;
                            }
                        }
                    }while(flag);

                    if(confirmation)
                    {
                        dos.writeBoolean(true);
                        System.out.println(dis.readUTF());
                        System.out.println("Keep this Id to Check your food delivery status: "+dis.readUTF());
                        status = true;
                    }

                    else
                    {
                        dos.writeBoolean(false);
                        System.out.println(dis.readUTF());
                    } 
                }
                else
                {
                    System.out.println(dis.readUTF());
                }
            }
            else if(dis.readBoolean())
            {
                System.out.println(dis.readUTF());
                System.out.println("Try Some another restaurant from following list");
                ShowRestaurants();
            }
            else
            {
                System.out.println(dis.readUTF());
            }
            
        } 
        catch (Exception e) {}
        return status;
        
    }
    boolean checkOrder()
    {
        boolean status=false;
        try {
            dos.writeInt(9);
            
            System.out.println("Enter Your Order id");
            String orderId1=sc.nextLine();
            // orderId1=sc.nextLine();
            dos.writeUTF(orderId1);
            // if(dis.readBoolean())
            // {
                System.out.println(dis.readUTF());
            // }
            // else
            // {
                // System.out.println(dis.readUTF());
        //    / }
            
            status=dis.readBoolean();
        } catch (Exception e) {}

        return status; 
    }
    void showHistory()
    {
        try {
            dos.writeInt(10);
            //dos.writeUTF(customer.getCustomerName());
            System.out.println(dis.readUTF());
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
                    
}


// class Customer{

//     String customerName, customerAddress, favFood , customerNumber;
    
//     Customer (String customerName, String customerAddress, String customerNumber)
//     {
//         this.customerName=customerName;
//         this.customerAddress=customerAddress; 
//         this.customerNumber=customerNumber;
//     }
//     String getCustomerName(){
//         return customerName;
//     }
//     String getCustomerAddress(){
//         return customerAddress;
//     }
//     String getCustomerNumber(){
//         return customerNumber;
//     }
//     String getfavFood(){
//         return favFood;
//     }
//     void setfavFood(String favFood)
//     {
//         this.favFood = favFood;
//     }

// }


class User
{
    Socket s=null;
    DataOutputStream dos=null;
    DataInputStream dis=null;
    String name=null;
    String address=null;
    String number=null;
    void start()
    {
        Scanner sc= new Scanner(System.in);  
        try
        {
            s = new Socket("localhost",5050);
            System.out.println("Connecting...");
            dos  = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            System.out.println("Enter Your Name");
            name= sc.nextLine();
            dos.writeUTF(name);
            if(!dis.readBoolean())
            {
                System.out.println("You are not a registered Customer");
                System.out.println("Please enter you Mobile Number and Address also");
                number= sc.nextLine();
                address= sc.nextLine();
                dos.writeUTF(number);
                dos.writeUTF(address);
            }
            else
            {
                address=dis.readUTF();
                number= dis.readUTF();
            }
            System.out.println(dis.readUTF());  
        } 
        catch(Exception u) 
        { 
            System.out.println(u); 
        }
        Customer customer = new Customer(name, address,number);
        try
        {
            if(dis.readBoolean())
            {
                Action action = new Action(dis,dos,customer);
                String answer ;
                System.out.println("Server working fine Go with your options");
                boolean placed=false;
                do
                { 
                    System.out.println("Enter your request");
                    System.out.println("1. See all available restaurants");
                    System.out.println("2. See areas wise available restaurants");
                    System.out.println("3. Search the availability of a restaurant");
                    System.out.println("4. See menu of a restaurant");
                    System.out.println("5. Search a cusine in a Area");
                    System.out.println("6. See all the cusine in an area");
                    System.out.println("7. Search a minimum cost particular food restaurant");
                    if(placed)
                        System.out.println("8. Check your Order");
                    else
                        System.out.println("8. Place your Order");
                    System.out.println("9. Show My Order History");    

                    int option= sc.nextInt();
                    switch(option)
                    {
                        
                        case 1:
                            action.ShowRestaurants();
                            break;
                        case 2:
                            action.ShowAreaRestaurants();
                            break;
                        case 3:
                            action.ShowRestaurantsArea();
                            break;
                        case 4:
                            action.showMenu();
                            break;
                        case 5:
                            action.SearchCusine();
                            break;
                        case 6:
                            action.ShowMenuArea();
                            break;
                        case 7:
                            action.searchMinCostFood();
                            break;
                        case 8:
                        {
                            if(placed)
                                placed=action.checkOrder();
                            else 
                                placed=action.placeOrder();
                            break;
                        }
                        case 9:
                            action.showHistory();
                            break;
                        default:

                    }
                System.out.println("next or Ouit?");
                answer= sc.nextLine();
                answer = sc.nextLine();
                answer= answer.toLowerCase();
                if(!answer.equals("next"))
                {
                    dos.writeInt(0);
                }    
               }while(answer.equals("next"));
            }
            else
            {
                System.out.println("Server is not working");
            }
        }catch(Exception e){}
    }
    
    public static void main(String[] args) {
    User user = new User();  
    user.start();     
    }
}