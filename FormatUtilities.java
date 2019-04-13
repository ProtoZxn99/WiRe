/*999999999999999999999999999999
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.util.Base64;

/**
 *
 * @author Patrick
 */
public class FormatUtilities {
    
    //Give left spacing with a character to a String, making a uniform-lengthed String
    public java.lang.String LeftPad(java.lang.String originalString, int length, char padCharacter) {
      StringBuilder sb = new StringBuilder();
      while (sb.length() + originalString.length() < length) {
         sb.append(padCharacter);
      }
      sb.append(originalString);
      java.lang.String paddedString = sb.toString();
      return paddedString;
   }
    
    public boolean findSpace(String text){
        return (text.lastIndexOf(' ')>0)?true:false;
    }
    
    public String base64Encode(byte [] b){
        String res = Base64.getEncoder().encodeToString(b);
        return res;
    }
    
    public byte [] base64Decode(String text){
        byte [] res = Base64.getDecoder().decode(text);
        return res;
    }
    
    public byte [] base64Decode(byte [] text){
        byte [] res = Base64.getDecoder().decode(text);
        return res;
    }
}
