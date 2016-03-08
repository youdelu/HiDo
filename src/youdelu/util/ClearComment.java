/**  
 *	   @company  澳亚卫视
 *	   @author  游德禄
 *     @Email youdelu@sina.cn
 *     @date  2015年10月28日 下午4:45:07 
 *     @version 1.0 
 *     @parameter  
 *     @return  
 *     
 */
package youdelu.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * 
 * 清除注释
 * @author 游德禄
 *
 */
public class ClearComment {   
   /**  
    * @param currentDir  
    *            当前目录  
    * @param currentFileName  
    *            当前文件名  
    * @throws FileNotFoundException  
    * @throws UnsupportedEncodingException  
    */  
   /**  
    * @param filePathAndName  
    * @throws FileNotFoundException  
    * @throws UnsupportedEncodingException  
    */  
   public static String clearComment(String filePathAndName)   
           throws FileNotFoundException, UnsupportedEncodingException {   
       StringBuffer buffer = new StringBuffer();   
       String line = null;  
       InputStream is = new FileInputStream(filePathAndName);   
       @SuppressWarnings("resource")
       BufferedReader reader = new BufferedReader(new InputStreamReader(is));   
       try {   
           line = reader.readLine();   
       } catch (IOException e) {   
           e.printStackTrace();   
       }  
       while (line != null) {  
           buffer.append(line);   
           buffer.append("\r\n"); 
           try {   
               line = reader.readLine();   
           } catch (IOException e) {   
               e.printStackTrace();   
           }  
       }   
        String filecontent = buffer.toString();   
        Map<String, String> patterns = new HashMap<String, String>();   
        patterns.put("([^:])\\/\\/.*", "$1"); 
        patterns.put("\\s+\\/\\/.*", ""); 
        patterns.put("^\\/\\/.*", "");   
        patterns.put("^\\/\\*\\*.*\\*\\/$", "");   
        patterns.put("\\/\\*.*\\*\\/", "");   
        patterns.put("/\\*(\\s*\\*\\s*.*\\s*?)*\\*\\/", "");   
        Iterator<String> keys = patterns.keySet().iterator();   
        String key = null, value = "";   
        while (keys.hasNext()) {   
            key = keys.next();   
            value = patterns.get(key);   
            filecontent = replaceAll(filecontent, key, value);   
        }   
        return filecontent;   
    }   
  
    /**  
     * @param fileContent  
     *            内容  
     * @param patternString  
     *            匹配的正则表达式  
     * @param replace  
     *            替换的内容  
     * @return  
     */  
    public static String replaceAll(String fileContent, String patternString,   
            String replace) {   
        String str = "";   
        Matcher m = null;   
        Pattern p = null;   
        try {   
            p = Pattern.compile(patternString);   
            m = p.matcher(fileContent);   
            str = m.replaceAll(replace);   
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally {   
            m = null;   
            p = null;   
        }   
        return str;   
    }   
} 
