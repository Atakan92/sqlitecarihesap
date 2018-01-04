package Siniflar;


public class ValidationKontrol {
    
     public boolean isStringKontrol(String ad) {
        for (char harf : ad.toCharArray()) {
            if (Character.isDigit(harf)) {      //sayi girmesin bir zahmet 
                return false;
            } else if (!Character.isAlphabetic(harf) && !Character.isWhitespace(harf)) {
                return false;       //saçma sapan karakterler kullanma bir zahmet boşluk girmesin b zahmet 
            }
        }
        return true;
    }
    
     public boolean isTelKontrol(String tel){
         for (char harf : tel.toCharArray()) {
             if (!Character.isDigit(harf)) { //harf girmesin bi zahmet
                return false;
            }
         }
        if (!tel.substring(0, 1).equals("0")) {  // ilk hane 0 olsun bi zahmet 
            return false;
        }
             
        if(tel.length()!=11){ // 11 haneli olsun bir zahmet 
            return false;
        }
        
         
        return true;
    }
     
     public boolean isNumericKontrol(String aString ){
      for (char harf : aString.toCharArray()) {
             if (!Character.isDigit(harf)) { //harf girmesin bi zahmet boş girmesin bi zahmet 
                return false;
            }
             
             
         }
      return true;
     } 
     
     public boolean isBoslukkontrol(String bosluk){
           for (char harf : bosluk.toCharArray()) {
             if (!Character.isWhitespace(harf)) { //boş girmesin bi zahmet 
                return false;
            }
           }
         return true;
     }     
     public boolean isBosGecilemezKontrol(String bosGecilemez){
         if(!bosGecilemez.equals("")){
             return false;
         }
         return true;
     }
     
     
      public boolean isOzelHarfKontrol(String bosluk){
           for (char harf : bosluk.toCharArray()) {
             if (Character.isAlphabetic(harf)) { //boş girmesin bi zahmet 
                return false;
            }
           }
         return true;
     }     
    
}
    
    
    