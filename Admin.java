import java.io.*;
import java.sql.*;
import java.util.*;
import java.net.*;
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar; 


class Food
{
    private String  foodName ;
    private int price, serviceTime;
    Food( String foodName,int price, int serviceTime)
    {
        this.foodName=foodName;
        this.price=price;
        this.serviceTime=serviceTime;
    }
    String getFoodDetails()
    {
        return foodName+"             \t"+ price+"  \t        "+ serviceTime;
    }
    String getFoodName()
    {
        return foodName;
    }
    int getPrice()
    {
        return price;
    }
    int getServiceTime()
    {
        return serviceTime;
    }

}

class Agent
{
    private String agentName, agentAddress , agentNumber;
    Boolean status;
    Agent(String agentName, String agentAddress,String agentNumber)
    {
        this.agentName = agentName;
        this.agentAddress = agentAddress;
        this.agentNumber = agentNumber;
    }
    String getAgentName(){
        return agentName;
    }
    String getAgentAddress(){
        return agentAddress;
    }
    String getAgentNumber(){
        return agentNumber;
    }
    String getAgentDetails()
    {
        return agentName+"\t"+agentAddress+"\t"+agentNumber;
    }
    void setStatus(boolean status)
    {
        this.status = status;
    }
    boolean getStatus()
    {
        return status;
    }


}


class Customer{

    String customerName, customerAddress, favFood , customerNumber;
    
    Customer (String customerName, String customerAddress, String customerNumber)
    {
        this.customerName=customerName;
        this.customerAddress=customerAddress;
        this.customerNumber=customerNumber;
    }
    String getCustomerName(){
        return customerName;
    }
    String getCustomerAddress(){
        return customerAddress;
    }
    String getCustomerNumber(){
        return customerNumber;
    }
    String getfavFood(){
        return favFood;
    }
    void setfavFood(String favFood)
    {
        this.favFood = favFood;
    }

}



class PendingOrder
{
    String restArea, restaurantName, customerName, customerNumber, foodName, assignedAgent, agentNumber, orderId , orderTime ;
    int foodPrice, deleveryTime;
    PendingOrder( String customerName,String customerNumber, String restaurantName, String restArea, String assignedAgent,String agentNumber,String foodName, int foodPrice, String orderId, String orderTime, int deleveryTime)
    {
        this.customerName = customerName;
        this.customerNumber=customerNumber;
        this.restaurantName = restaurantName;
        this.restArea=restArea;
        this.assignedAgent = assignedAgent;
        this.agentNumber=agentNumber;
        this.foodName = foodName;  
        this.foodPrice = foodPrice;
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.deleveryTime = deleveryTime;
    }
    String getOrderId() {
        return orderId;
    }
    String getOrderTime() {
        return orderTime;
    }
    String getCustomerName(){
        return customerName;
    }
    String getFood()
    {
        return foodName;
    } 

}





class Restaurant
{
    private String restName , restArea , openingTime;
    private int deleveryTime;
    ArrayList<Food> foodList = new ArrayList<Food> ();
    Restaurant(String restName , String restArea , int deleveryTime , String openingTime)
    {
        this.restName=restName;
        this.restArea=restArea;
        this.openingTime=openingTime;
        this.deleveryTime=deleveryTime;

    }
    String getRestaurantDetails()
    {
        return restName+"    \t\t "+restArea+"     \t  "+deleveryTime+" \t\t"+openingTime;
    }
    String getName ()
    {
        return restName;
    }
    String getArea ()
    {
        return restArea;
    }
    int getDeleveryTime ()
    {
        return deleveryTime;
    }
    String getOpenTime ()
    {
        return openingTime;
    }
    void setFood(Scanner fileReader)
    {
        String foodName ;
        int price, serviceTime;
        String data=fileReader.nextLine();
        
        while (!data.equals("end")) 
        {   
            foodName= data;
            price=Integer.parseInt(fileReader.nextLine());
            serviceTime =Integer.parseInt(fileReader.nextLine());
            Food food = new Food(foodName, price, serviceTime);
            foodList.add(food);
            data=fileReader.nextLine();
        }
    }
    String getFoodList() {
        int i=0;
        String ans="";
        while(i<foodList.size()) {
        ans=ans+foodList.get(i).getFoodDetails()+"\n";            
            i++;
        }
        return ans;

    }
    
}



class RunningProcess extends Thread
{
   
    PendingOrder pn=null;
    String orderId1=null; 
    Connection con=null;

    RunningProcess (String orderId, PendingOrder pn)
    {
        orderId1=orderId;
        this.pn=pn;
    }
    @Override
    public void run() {

        try
        {
            int i=0;
            while(i<Admin.pendingOrder.size())
            {
                if(orderId1.equals(Admin.pendingOrder.get(i).getOrderId()))
                {
                    break; 
                }
                i++;
            }
            String msg="Thank you "+ pn.customerName+" for your order\n";    
            int oldHour=Integer.parseInt(Admin.pendingOrder.get(i).getOrderTime().substring(0,2)),  oldMinute=Integer.parseInt(Admin.pendingOrder.get(i).getOrderTime().substring(3,5));
            String currentTime=getTime();
            int currentHour=Integer.parseInt(currentTime.substring(0,2)),  currentMinute=Integer.parseInt(currentTime.substring(3,5));
            if(oldHour<currentHour)
            {
                currentMinute+=60;
            }
            int dTime=Admin.pendingOrder.get(i).deleveryTime;
            oldHour+=(oldMinute+dTime)%60;

            runScript(pn.customerNumber,oldHour,oldMinute+dTime,msg);

            while(true)
            {
                currentTime=getTime();
                currentHour=Integer.parseInt(currentTime.substring(0,2));
                currentMinute=Integer.parseInt(currentTime.substring(3,5));
                if(currentMinute-oldMinute>=dTime)
                {
                    System.out.println(pn.customerName+" Has recieved the food");
                    break;
                } 
                Thread.sleep(10000);
            }
            Connection con=Admin.conn;         
            String sql="INSERT INTO "+pn.customerName+" ( RestaurantName , RestaurantAddress , AgentName , AgentNumber , FoodName , OrderTime ,FoodPrice , DeleveryTime  )" + 
            " VALUES (? , ? , ? , ? , ? , ? , ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,pn.restaurantName.toUpperCase());
            ps.setString(2,pn.restArea.toUpperCase());
            ps.setString(3,pn.assignedAgent.toUpperCase());
            ps.setString(4,pn.agentNumber);
            ps.setString(5,pn.foodName.toUpperCase());
            ps.setString(6,pn.orderTime);
            ps.setInt(7, pn.foodPrice);
            ps.setInt(8, pn.deleveryTime);
            try
            {
                ps.execute();
                System.out.println("History Updated");

            }catch(Exception e){System.out.println("Data has not uploaded");}     
       
            
        }catch(Exception e){}
    }

    String getTime(){
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
        String strDate = dateFormat.format(date);  
        return strDate.substring(11, 16);
    }

    public void runScript(String number,int hour,int minute,String msg){
        Process p=null;
        try{
            p = Runtime.getRuntime().exec("python SendMsg.py +91"+number+" msg "+hour+" "+minute);
            p.waitFor();
            BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            System.out.println("sa");
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
            while ((line = bre.readLine()) != null) {
                System.out.println(line);
            }
            bre.close();
            p.waitFor(); 
            p.destroy();
        }catch(Exception e) {
           System.out.println("Exception Raised " + e.toString());
        } 
        
 
     }


}

class ServerAction
{
    
    
    DataOutputStream dos=null;
    DataInputStream dis=null;
    Customer customer=null;
    ServerAction ( DataInputStream dis, DataOutputStream dos, Customer customer)
    {
        this.dis=dis;
        this.dos=dos;
        this.customer=customer;
    }
    
    


    void ShowRestaurants()
    {
        System.out.println(customer.getCustomerName()+" want to see All Available Restaurant");
        System.out.println("Sending all the details....");
        String details="Restaurant Name \t Adrress \t deleveryTime \t openingTime\n";
        int i=0;
        while(i<Admin.restaurant.size())
        {
            details += Admin.restaurant.get(i).getRestaurantDetails()+"\n";
            i++;
        }
        try {
            dos.writeUTF(details);    
        } catch (Exception e) {
        }
        System.out.println("All details has been sended");
    }   




    void ShowAreaRestaurants()
    {
        String area=null;
        try{
            area=dis.readUTF();
        } catch(Exception e){}
        System.out.println(customer.getCustomerName()+" want to see All Available Restaurant in "+area);
        String details="Restaurant Name \t Adrress \t deleveryTime \t openingTime\n";
        int i=0;
        boolean found=false;
        while(i<Admin.restaurant.size())
        {
            if(Admin.restaurant.get(i).getArea().equalsIgnoreCase(area))
            {
                details+=Admin.restaurant.get(i).getRestaurantDetails()+"\n";
                found=true;
            }
            i++;
        }
        if(!found)
        {
            details="Sorry! No Restaurant is available in "+area+" We will work on that\n" ;
            System.out.println("No Restaurant is available in "+area+"Add some");
        }
        else
        {
            System.out.println("All details has been sended"); 
        }
        try {
            dos.writeUTF(details);    
        } catch (Exception e) {
        }
    }
    




    void ShowRestaurantsArea()
    {
        String rest=null;
        try{
            rest=dis.readUTF();
        } catch(Exception e){}
        System.out.println(customer.getCustomerName()+" want to see all the place where "+rest+" is available");
        String details="Restaurant Name \t Adrress \t deleveryTime \t openingTime\n";
        int i=0;
        boolean found=false;
        while(i<Admin.restaurant.size())
        {
            if(Admin.restaurant.get(i).getName().equalsIgnoreCase(rest))
            {
                details+=Admin.restaurant.get(i).getRestaurantDetails()+"\n";
                found=true;
            }
            i++;
        }
        if(!found)
        {
            details="Sorry! "+rest+ " is not available in any area\n" ;
            System.out.println(rest+" was not available in any place");
        }
        else
        {
            System.out.println("All details has been sended"); 
        }
        try {
            dos.writeUTF(details);    
        } catch (Exception e) {
            //TODO: handle exception
        }
    }







    void showMenu()
    {
        String rest=null;
        String area=null;
        try{
            rest=dis.readUTF();
            area=dis.readUTF();
        } catch(Exception e){}
        System.out.println(customer.getCustomerName()+" want to see all available cusine in "+rest+" "+area );
        String details="Food Name \t \t   Price    \t    ServiceTime\n";
        int i=0;
        boolean found=false;
        while(i<Admin.restaurant.size())
        {
            if(Admin.restaurant.get(i).getName().equalsIgnoreCase(rest) && Admin.restaurant.get(i).getArea().equalsIgnoreCase(area))
            {
                details+= Admin.restaurant.get(i).getFoodList();
                found=true;
                break;
            }
            i++;
        }
        if(!found)
        {
            
            details="Sorry! maybe "+rest+ " is not available in "+area+" \n" ;
            System.out.println(rest+" was not available in "+area);
        }
        else
        {
            System.out.println("All details has been sended"); 
        }
        try {
            dos.writeUTF(details);    
        } catch (Exception e) {
            //TODO: handle exception
        }
    }







    void SearchCusine()
    {
        String area=null;
        String cusine=null;
        try{
            cusine=dis.readUTF();
            area=dis.readUTF();
        } catch(Exception e){}
        System.out.println(customer.getCustomerName()+" want to search "+cusine +" in "+area );
        String details="Availability of "+cusine+" \nPrice  \t   ServiceTime \t Restaurant Name \n";
        int i=0;
        boolean found=false;
        while(i<Admin.restaurant.size())
        {
            if(Admin.restaurant.get(i).getArea().equalsIgnoreCase(area))
            {
                int j=0;
                while(j<Admin.restaurant.get(i).foodList.size())
                {
                    if(Admin.restaurant.get(i).foodList.get(j).getFoodName().equalsIgnoreCase(cusine))
                    {
                        details+= Admin.restaurant.get(i).foodList.get(j).getPrice() +" \t    "+Admin.restaurant.get(i).foodList.get(j).getServiceTime()+" \t\t ";
                        details+= Admin.restaurant.get(i).getName()+" \n";
                        found=true;
                        break;
                    }
                    j++;
                }
            }
            i++;
        }
        if(!found)
        {
            
            details="Sorry! "+cusine+ " is not available in "+area+" \n" ;
            System.out.println(cusine+" was not available in "+area);
        }
        else
        {
            System.out.println("All details has been sended"); 
        }
        try {
            dos.writeUTF(details);    
        } catch (Exception e) { }
        
    }







    void ShowMenuArea()
    {
        String area=null;
        try{
            area=dis.readUTF();
        } catch(Exception e){}
        System.out.println(customer.getCustomerName()+" want to see all the cusine available in "+area);
        String details="Restaurant Name \t Adrress \t deleveryTime \t openingTime\n\nFood Name \t \t   Price    \t    ServiceTime\n\n";
        int i=0;
        boolean found=false;
        while(i<Admin.restaurant.size())
        {
            if(Admin.restaurant.get(i).getArea().equalsIgnoreCase(area))
            {
                details+= Admin.restaurant.get(i).getRestaurantDetails()+"\n\n";
                details+= Admin.restaurant.get(i).getFoodList()+"\n\n";
                found=true;
            }
            i++;
        }
        if(!found)
        {
            details="Sorry! No Restaurant is available in "+area+" We will work on that\n" ;
            System.out.println("No Restaurant is available in "+area+"Add some");
        }
        else
        {
            System.out.println("All details has been sended"); 
        }
        try {
            dos.writeUTF(details);    
        } catch (Exception e) {

        }
    }




    
    void searchMinCostFood()
    {
        String area=null;
        String cusine=null;
        try{
            cusine=dis.readUTF();
            area=dis.readUTF();
        } catch(Exception e){}
        System.out.println(customer.getCustomerName()+" want to minimum price "+cusine +" in "+area );
        int i=0;
        boolean found=false;
        int minimum=10000;
        String rest=null;
        while(i<Admin.restaurant.size())
        {
            if(Admin.restaurant.get(i).getArea().equalsIgnoreCase(area))
            {
                int j=0;
                while(j<Admin.restaurant.get(i).foodList.size())
                {
                    if(Admin.restaurant.get(i).foodList.get(j).getFoodName().equalsIgnoreCase(cusine))
                    {
                        int price=Admin.restaurant.get(i).foodList.get(j).getPrice();
                        if(minimum>price)
                        {
                            rest=Admin.restaurant.get(i).getName();
                            minimum = price;
                        }
                        found=true;
                        break;
                    }
                    j++;
                }
            }
            i++;
        }
        String details="Minimum Price of "+cusine+" is "+ minimum +" Available in "+rest;
        if(!found)
        {
            
            details="Sorry! "+cusine+ " is not available in "+area+" \n" ;
            System.out.println(cusine+" was not available in "+area);
        }
        else
        {
            System.out.println("All details has been sended"); 
        }
        try {
            dos.writeUTF(details);    
        } catch (Exception e) {
        }
        
    }





    void placeOrder()
    {
        String rest=null;
        String area=null; 
        String agentName=null;
        String agentNumber=null;
        try{
            rest=dis.readUTF();
            area=dis.readUTF();
        } catch(Exception e){}
        System.out.println(customer.getCustomerName()+" want to Order some food from "+rest+" "+area );
        String details="Food Name \t \t   Price    \t    ServiceTime\n";
        int i=0;
        boolean found=false;
        while(i<Admin.restaurant.size())
        {
            if(Admin.restaurant.get(i).getName().equalsIgnoreCase(rest) && Admin.restaurant.get(i).getArea().equalsIgnoreCase(area))
            {
                details+= Admin.restaurant.get(i).getFoodList();
                found=true;
                break;
            }
            i++;
        }
        if(!found)
        {
            
            details="Sorry! "+rest+ " is not available in "+area+" \n" ;
            
            try {
                dos.writeBoolean(false);
                dos.writeBoolean(true);
                dos.writeUTF(details);
            } catch (Exception e) {
            }
            
            System.out.println(rest+" was not available in "+area);
        }
        else
        {
            int k=0;
            found=false;
            while(k<Admin.agents.size())
            {
                if(Admin.agents.get(k).getAgentAddress().equalsIgnoreCase(area) && !(Admin.agents.get(k).getStatus()))
                {
                    
                    agentName=Admin.agents.get(k).getAgentName();
                    agentNumber=Admin.agents.get(k).getAgentNumber();
                    Admin.agents.get(k).setStatus(true);
                    found=true;
                    break;
                }
                k++;
            }
            if(found)
            {
                try {
                    dos.writeBoolean(true);
                    dos.writeUTF(details);
                    String food=null;
                    System.out.println("Menu has been displayed to "+customer.getCustomerName() );
                    System.out.println("Waiting for Order.....");
                    food=dis.readUTF();
                    int j=0;
                    found=false;
                    while(j<Admin.restaurant.get(i).foodList.size())
                    {
                        if(Admin.restaurant.get(i).foodList.get(j).getFoodName().equalsIgnoreCase(food))
                        {
                            found=true;
                            break;
                        }
                        j++;
                    }
                    if(found)
                    {
                        int time=Admin.restaurant.get(i).foodList.get(j).getServiceTime()+Admin.restaurant.get(i).getDeleveryTime();
                        int amount=Admin.restaurant.get(i).foodList.get(j).getPrice();
                        details= "Total Amount to be paid: "+amount;
                        details+=" and Delivery time : "+time;
                        dos.writeBoolean(true);
                        dos.writeUTF(details);
                        dos.writeInt(amount);
                        System.out.println("Food Details has been sent to "+customer.getCustomerName()+" Waiting for Confimation");
                        if(dis.readBoolean())
                        {
                            String timeAt=getTime();
                            System.out.println(customer.getCustomerName()+" has confirmed their order at "+timeAt );
                            System.out.println("Total Amount received "+amount);
                            details="Your order has been confirmed at "+timeAt+" It will be delivered in "+time+" minutes.\n";
                            details+=agentName+" is Your Delivery Agent You can contact at "+agentNumber;
                            details+="\nNow you can check your status..\n";
                            
                            dos.writeUTF(details);
                            String orderId=getAlphaNumericString();
                            dos.writeUTF(orderId);
                            Admin.agents.get(k).setStatus(true);
                            PendingOrder pn = new PendingOrder(customer.getCustomerName(),customer.getCustomerNumber(),rest , area,agentName, agentNumber,food ,amount, orderId,timeAt,time);
                            Admin.pendingOrder.add(pn);
                            Thread rp = new RunningProcess(orderId , pn);
                            rp.start();
                        }  
                        else
                        {
                            System.out.println(customer.getCustomerName()+" Not want to confirm their order");
                            Admin.agents.get(k).setStatus(false);
                            dos.writeUTF("Thank you for having us Please visit again");
                        }

                    }
                    else
                    {
                        System.out.println("Requested food was not found");
                        dos.writeBoolean(false);
                        Admin.agents.get(k).setStatus(false);
                        dos.writeUTF("You did'nt select the food from given menu");

                    }

                } catch(Exception e){}
            }
            else
            {
                details="Sorry! No Delivery agent is available Now Try later\n" ;
                try {
                    dos.writeBoolean(false);
                    dos.writeBoolean(false);
                    dos.writeUTF(details);
                    System.out.println("No agent available in "+area);
                } catch (Exception e) {
                }
            }

        }
        
    }






    void checkOrder()
    {
        boolean status=false;
        String details="";
        try
        {
        String orderId1=dis.readUTF();
        int i=0;
        boolean found=false;
        while(i<Admin.pendingOrder.size())
        {
            if(orderId1.equals(Admin.pendingOrder.get(i).getOrderId()))
            {   
                found=true;
                break;
            } 
            i++;
        }    
        if(found)
        {
            System.out.println(customer.getCustomerName()+" is checking their order status");
            String currentTime=getTime();
            int currentHour=Integer.parseInt(currentTime.substring(0,2)),  currentMinute=Integer.parseInt(currentTime.substring(3,5));
            int oldHour=Integer.parseInt(Admin.pendingOrder.get(i).getOrderTime().substring(0,2)),  oldMinute=Integer.parseInt(Admin.pendingOrder.get(i).getOrderTime().substring(3,5));
            String food=Admin.pendingOrder.get(i).getFood() ;
            int deleveryTime=Admin.pendingOrder.get(i).deleveryTime;
            if(oldHour<currentHour)
            {
                currentMinute+=60;
            }
            if(currentMinute-oldMinute>=deleveryTime)
            {
                details="Your " + food +" has been successfully delivered";
                status= false;
            } 
            else
            {
                details=(deleveryTime-(currentMinute-oldMinute))+" minute left for delivery";
                status=true;
            }
            System.out.println(details);
            
            
        }
        else
        {
            // dos.writeBoolean(false);
            details="You have entered the wrong orderId please Enter again";
            status=true;
        }
        dos.writeUTF(details);
        dos.writeBoolean(status);
        } catch(Exception e){}
        
    }




    void showHistory(){{
        Connection con=Admin.conn;
        String restArea, restaurantName, foodName, assignedAgent, agentNumber, orderTime ;
        int foodPrice, deleveryTime;
        boolean found=false;
        try{
            PreparedStatement ps=con.prepareStatement("SELECT * FROM `"+customer.getCustomerName()+"`");
            ResultSet result = ps.executeQuery();
            String details="RestaurantName  RestaurantAddress  AgentName  AgentNumber  FoodName  OrderTime  FoodPrice  DeleveryTime\n";
            while(result.next())
            {
                restaurantName=result.getString("RestaurantName");
                restArea=result.getString("RestaurantAddress");
                assignedAgent=result.getString("AgentName");
                agentNumber= result.getString("AgentNumber");
                foodName=result.getString("FoodName");
                orderTime=result.getString("OrderTime");
                foodPrice=result.getInt("FoodPrice");
                deleveryTime=result.getInt("DeleveryTime");
                details+="   "+restaurantName+"\t\t"+restArea+" \t   "+assignedAgent+" \t"+agentNumber+"   "+foodName+"    "+orderTime+" \t "+foodPrice+" \t    "+deleveryTime+" \n";
                found=true;

            }
            if(found)
            {
                dos.writeUTF(details);
                System.out.println("History details has been sent to "+customer.getCustomerName());
            }
            else{
                dos.writeUTF("No Previuos History found");
                System.out.println("No History found");
            }
        } catch(Exception e){}

    }}
      



    String getAlphaNumericString()
    {
        int n= 6;
        String AlphaNumericString ="0123456789";
  
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }




    String getTime(){
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
        String strDate = dateFormat.format(date);  
        return strDate.substring(11, 16);
    }




}




class UserConnection extends Thread 
{

    Connection con=null;
    static Statement stmt = null;
    PreparedStatement ps;
    Socket clientSock=null;
    DataOutputStream dos=null;
    DataInputStream dis=null;
    String customerName;
    String customerAddress;
    String customerNumber;


    UserConnection(Socket clientSock,DataInputStream dis, DataOutputStream dos,String customerName)
    {
        this.dis=dis;
        this.dos=dos;
        this.customerName=customerName;
        this.clientSock=clientSock;
    }   


    public void run()
    {
        try
        {
            if(createConnection())
            {
                boolean flag=true; 
                System.out.println("Ready for action ...");
                Customer customer = new Customer(customerName, customerAddress, customerNumber);
                ServerAction action = new ServerAction(dis , dos , customer);
                dos.writeBoolean(true);
                while(flag)
                {
                    int option=dis.readInt();
                    switch(option)
                    {
                        case 0:
                        {
                            System.out.println(customerName+" has completed with their request");
                            flag=false;
                            Admin.id--;
                            System.out.println("Total left User to the network"+Admin.id);
                            break;
                        }
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
                            action.placeOrder();
                            break;
                        }
                        case 9:
                            action.checkOrder();
                            break; 
                        case 10:
                            action.showHistory();       
                        default:
                    }
                    if(option!=0)
                    System.out.println("Ready for next request");

                }
                try {
                    clientSock.close();
                } catch (Exception e) {
                }
            }
            else
            {
                System.out.println("Server not working fine ...");
                dos.writeBoolean(false);
            }
        }catch(IOException e){} 
        
    }





    boolean createConnection()
    {
        boolean answer = false;
        try{   

            con=Admin.conn;
            ps=con.prepareStatement("SELECT * FROM `user` WHERE  `name` = ?");
            ps.setString(1,customerName); 
            ResultSet result = ps.executeQuery();
            if(result.next())
            {
                System.out.println("A old customer has logged in");
                customerAddress = result.getString("address");
                customerNumber = result.getString("number");
                dos.writeBoolean(true);
                dos.writeUTF(customerAddress);
                dos.writeUTF(customerNumber);
                System.out.println(customerName+" logged in");
                System.out.println("Address :" + customerAddress + "\t Number :" + customerNumber);
                dos.writeUTF("Welcome "+ customerName +" Your Address is "+customerAddress+" and your Number is "+customerNumber);
                answer= true;
            }
            else
            {
                dos.writeBoolean(false);
                System.out.println("A new customer has logged in");
                customerNumber =dis.readUTF();
                customerAddress =dis.readUTF();
                String name=customerName;
                String address=customerAddress;
                String number=customerNumber;
                stmt = con.createStatement();
                String ttttt="user";
                int i=stmt.executeUpdate("INSERT INTO "+ttttt+" (name , address, number )" + " VALUES ('"+name+"' , '"+address+"', '"+number+"')");     
                if(i>0)
                {
                    System.out.println("A new customer "+ customerName + " has been connected");
                    dos.writeUTF("Welcome "+ customerName+" you have successfully registered");
                    answer= true;
                    stmt = con.createStatement();
                    String sql = "CREATE TABLE " + customerName + 
                    " (RestaurantName VARCHAR(50) , "+
                    "RestaurantAddress VARCHAR(50) , "+
                    "AgentName VARCHAR(50) , "+
                    "AgentNumber VARCHAR(50) , "+
                    "FoodName VARCHAR(50) , "+
                    "OrderTime VARCHAR(6) , "+
                    "FoodPrice INTEGER , "+
                    "DeleveryTime INTEGER )";
                    stmt.executeUpdate(sql);
                }
                else
                {
                    System.out.println(" Something went wrong"); 
                    dos.writeUTF("Something went wrong login again"); 
                }
            }
            
        }catch (SQLException se)
        {
        }catch(Exception e) {
        }
        finally
        {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
        return answer;
    }

}


class DataHandler
{


    Scanner sc=null;

    DataHandler(Scanner sc)
    {
        this.sc=sc;
    }
    void Datamanipulator()
    {
        System.out.println("Select Opration");
        System.out.println("1. Show existing data ");
        System.out.println("2. Add new restaurant data ");
        System.out.println("3. Update Restaurant details ");
        System.out.println("4. Update Restaurant Menu");
        System.out.println("5. Delete Restaurant all details");
        System.out.println("6. Delete some Restaurant Menu");
        int exe=sc.nextInt();
        switch(exe)
        {
            case 1:
                showRestaurants();
                break;
            case 2:
                addRestaurants();
                break;
            case 3:
                updateRestaurants();
                break;
            case 4:
                updateMenu();
                break;
            case 5:
                deleteRestaurants();
                break;
            case 6:
                deleteMenu();
                break;
            default:                                
        }
    }



    void showRestaurants(){
        if(Admin.restaurant.size()>0)
        {
            int i=0;
            System.out.println("Already Existing dataset");
            String details="Restaurant Name \t Adrress \t deleveryTime \t openingTime\n";
            while(i<Admin.restaurant.size())
            {
                details += Admin.restaurant.get(i).getRestaurantDetails()+"\n";
                i++;
            }
            System.out.println(details);
        }
        else
        {
            System.out.println("No data existing previously");
        }
    }



    void addRestaurants()
    {
        String input=null;
        try(FileWriter fw = new FileWriter("myFile.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
                {
                    while(true)
                    {
                        
                        System.out.println("Enter Restaurant name: ");
                        input=sc.nextLine().trim().toUpperCase();
                        input=sc.nextLine().trim().toUpperCase();
                        out.println();
                        out.println(input);

                        System.out.println("Enter Restaurant Address: ");
                        input=sc.nextLine().trim().toUpperCase();
                        out.println(input);

                        System.out.println("Enter Restaurant Opening time: ");
                        input=sc.nextLine().trim();
                        out.println(input);

                        System.out.println("Enter Restaurt service time: ");
                        input=sc.nextLine().trim();
                        out.println(input);
                        System.out.println("Enter Restaurant Menu");
                        int temp=0;
                        while(true)
                        {
                            System.out.println("Enter food name");
                            if(temp!=0)
                                input=sc.nextLine().trim().toUpperCase();
                            input=sc.nextLine().trim().toUpperCase();   
                            out.println(input);

                            System.out.println("Enter food price");
                            input=sc.nextLine().trim();
                            out.println(input);

                            System.out.println("Enter Service time: ");
                            input=sc.nextLine().trim();
                            out.println(input); 
                            System.out.println("Want to Add more food Y/N");
                            char ch=sc.next().charAt(0);
                            temp++;
                            if(ch=='N'|| ch=='n')
                                break;    
                            }
                            out.print("end");
                            System.out.println("Want to Add more Restaurant Y/N");
                            char ch=sc.next().charAt(0);
                            if(ch=='N'|| ch=='n')
                                break;   
                    }
                    System.out.println("Data has been added successfully");
                } catch (IOException e) {
                    System.out.println("Data has not been added error: " + e.getMessage());
                }

    }





    void updateRestaurants()
    {
        System.out.println("yet to complete");
    }

    void updateMenu()
    {
        System.out.println("yet to complete");
    }


    void deleteRestaurants()
    {
        System.out.println("yet to complete");
    }


    
    void deleteMenu()
    {
        System.out.println("yet to complete");
    }

}



class Admin 
{   
    static ArrayList<Restaurant> restaurant = new ArrayList<Restaurant>();
    static ArrayList<Agent> agents = new ArrayList<Agent>();
    static ArrayList<PendingOrder> pendingOrder = new ArrayList<PendingOrder>();
    private volatile boolean exit = true;
    static int id = 0;
    File file=null;
    static Connection conn=null;
    Scanner fileReader=null;

    Admin()
    {
        if(initializedRestaurant() && initializedAgent())
            System.out.println("Data has been initialized into program");
        else
            exit=false;
    }


    boolean initializedRestaurant()
    {
        String restName , restArea , openingTime;
        int deleveryTime;   
        try{
                
                file = new File("RestaurantData.txt");
                Scanner fileReader = new Scanner(file);
                while (fileReader.hasNextLine()) 
                {
                    restName = fileReader.nextLine();
                    restArea = fileReader.nextLine();   
                    openingTime = fileReader.nextLine();
                    deleveryTime=Integer.parseInt(fileReader.nextLine());
                    Restaurant rs = new Restaurant(restName, restArea , deleveryTime , openingTime);
                    rs.setFood(fileReader);
                    restaurant.add(rs);
                }
                return true;
            }catch(Exception e)
            {
                System.out.println(e);
                return false;
            }

    }



    boolean initializedAgent()
    {
        String agentName, agentAddress , agentNumber;
        try {
            file = new File("agentlist.txt");
            fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) 
            {
                agentName = fileReader.nextLine();
                agentAddress = fileReader.nextLine();   
                agentNumber = fileReader.nextLine();
                Agent agent = new Agent(agentName, agentAddress, agentNumber);
                agent.setStatus(false);
                agents.add(agent);
            }
            return true;
        } catch (Exception e) {
           System.out.println(e);
           return false;
        }
        
    }


    void start() 
    {
        ServerSocket serverSock=null;
        if(exit){
            try{
                Class.forName("com.mysql.jdbc.Driver");  
                conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant","root","password");
                System.out.println("Database has been initialized");
            } catch (Exception e) {}
            try 
            {
                serverSock = new ServerSocket(5050);
                System.out.println("Server has been started");
                
                while (exit) 
                {  
                    System.out.println("Total Number Of User has Connected "+id);
                    Socket clientSock = serverSock.accept();          
                    DataInputStream dis = new DataInputStream(clientSock.getInputStream());
                    DataOutputStream dos  = new DataOutputStream(clientSock.getOutputStream());
                    String userName=dis.readUTF();
                    Thread listner = new UserConnection(clientSock ,dis, dos, userName);
                    System.out.println("A new User is trying to connect");             
                    listner.start();
                    id++;
                }
                System.out.println("Server has been stopped");
            }
            catch (Exception ex)
            {
                System.out.print("error");
            }
            finally
            {
                try {
                    serverSock.close();
                } catch (Exception e) {
                    
                }
                try
                {
                    conn.close();
                }catch (Exception e){}
            }
        }
        else
        {
            System.out.println("There is some problem with data initialization");
            System.out.println("1> Maybe File has not been found ");
            System.out.println("2> File is not ready to read");
            System.out.println("Please start the server program again");
        }    
    }


    public static void main(String args[])
    {
        Scanner sc= new Scanner(System.in);
        System.out.println("Start the server or Work on the Database");
        String answer = sc.nextLine();
        Admin init = new Admin(); 
        if(answer.equalsIgnoreCase("start"))
        {   
            init.start();
        } 
        else
        {
            DataHandler dh = new DataHandler(sc);
            dh.Datamanipulator();
        }

        try {
            sc.close();
        } catch (Exception e) {
            
        }
    }
}

