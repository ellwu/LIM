package lim.util;

public class StringUtil {
	public static String removeFirstLetter(String str){
		if(!isEmpty(str) && str.length() > 1){
			return str.substring(1);
		}else{
			return "";
		}
	}
	
	public static boolean isEmpty(String str){
		return str == null || "".equals(str.trim());
	}
	
	public static String empty(String str){
		return str == null || "".equals(str.trim()) ? "" : str;
	}
	
	public static String empty(String str, String de){
		return str == null || "".equals(str.trim()) ? de : str;
	}
	
	public static void main(String[] args) {
		System.out.println(StringUtil.removeFirstLetter("/10.20.109.66:123"));
	}
}
