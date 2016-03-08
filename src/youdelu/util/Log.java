/**  
 *	   @company  澳亚卫视
 *	   @author  游德禄
 *     @Email youdelu@sina.cn
 *     @date  2015年10月30日 上午9:40:24 
 *     @version 1.0 
 *     @parameter  
 *     @return  
 *     
 */
package youdelu.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import youdelu.dao.Data;

/**
 * 输出日志
 * @author 游德禄
 *
 */
public class Log {
	public static void p(String s){
		if(Data.isRun&&Data.getInstance().showLog())
			return ;
		p(s,false);
	}
	public static void p(String s,boolean err) {
		if(Data.isRun&&Data.getInstance().showLog())
			return ;
		SimpleDateFormat format = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
		String str = format.format(Calendar.getInstance().getTime());
		if(err){
			System.err.println(str + " HiDo：ERROR --> " + s);
		}else{
			System.out.println(str + " HiDo：" + s);
		}
	}
	public static boolean e(boolean b){
		if(Data.isRun&&!Data.getInstance().showLog()){
			if(!b) p("建议将上面的SQL代码放到数据库里执行一遍看是否有问题");
		}
		return b;
	}

}
