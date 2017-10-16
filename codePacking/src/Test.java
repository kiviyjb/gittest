import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {
	public static void main(String[] args) {
//		Map<String,String> classpathmap = new HashMap<String,String>();
//		classpathmap.put("1", "1");
//		classpathmap.put("1", "2");
//		for(String s:classpathmap.values()){
//			System.out.println(s);
//		}
		String s = "/zwy/trunk/oa/mywork_local/src/com/office/DaoImp/TdcoumentSearchImpl.java";
		Pattern pattern1 = Pattern.compile(".*/(.*)/src");
		Matcher m  = pattern1.matcher(s);
		if(m.find()){
				System.out.println(m.group(0));
		}	}
}
