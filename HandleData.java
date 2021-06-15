  
 public class HandleData {


      static String getAlphaNumericString()
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

       public static void main(String args[]){  
            

            String a=getAlphaNumericString();
            String b=getAlphaNumericString();
            int i=0;
            while(!a.equals(b))
            {
                  b=getAlphaNumericString();
                  i++;
            }
            System.out.println(i);
        } 
}  