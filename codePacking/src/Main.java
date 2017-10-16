import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		try {
			String changeLog = ConfigurationRead.getInstance().getConfigItem("changeLog").trim();
			File file = new File(changeLog);
			Scanner in = new Scanner(file,"GBK");
			String xiangmu = ConfigurationRead.getInstance().getConfigItem("xiangmu").trim();
			String workpath =ConfigurationRead.getInstance().getConfigItem("workpath").trim();
			String outpath = ConfigurationRead.getInstance().getConfigItem("outpath");
			String despath = outpath+xiangmu;
			String xiangmutime="";
//			File file1 = new File(despath);
//			if(file1.exists()&&xiangmu!="")
//				deleteFile(file1);
			Map<String,List<String>> classpathmap = new HashMap<String,List<String>>();
			while(in.hasNextLine()){
				String str = in.nextLine().trim();
				System.out.println(str);
				if(!xiangmu.equals("")&&!str.contains("/"+xiangmu+"/")||!str.contains("."))
					continue;
				String filepathpre = workpath+xiangmu;
				
				
				if(str.contains("/src/")){
					String filepathend = str.substring(str.indexOf("/src/")+5,str.length());
					
					if(xiangmu.equals("")){
//						String s1 = str.substring(str.indexOf("/trunk/")+7,str.length());
//						xiangmu = s1.substring(0,s1.indexOf("/"));
						Pattern pattern1 = Pattern.compile(".*/(.*)/src");
						Matcher m  = pattern1.matcher(str);
						if(m.find()){
							xiangmu=m.group(1);
						}	
						filepathpre=workpath+xiangmu;
						despath = outpath+xiangmu;
					}
					String filepath = filepathpre+"/WebRoot/WEB-INF/classes/"+filepathend;
					filepath = filepath.replace(".java", ".class");
					if(xiangmutime.equals("")){
						xiangmutime = getXiangmutime(filepath);
						despath = despath+"/"+xiangmu+" "+xiangmutime;
					}
					
					if(filepath.endsWith(".class")){
						filepathend = filepathend.replace(".java", ".class");
						String keys = filepath.substring(0,filepath.lastIndexOf("/")+1);
						List<String> l = classpathmap.get(keys);
						if(l==null){
							l = new ArrayList<String>();
						}
						l.add(filepathend);
						classpathmap.put(keys,l);
						copyFile(filepath, despath+"/WEB-INF/classes/"+filepathend);
					}else{
						copyFile(filepath, despath+"/WEB-INF/classes/"+filepathend);
					}
				}else{
					String filepathend = str.substring(str.indexOf("/WebRoot/"),str.length());
					if(xiangmu.equals("")){
//						String s1 = str.substring(str.indexOf("/trunk/")+7,str.length());
//						xiangmu = s1.substring(0,s1.indexOf("/"));
						Pattern pattern1 = Pattern.compile(".*/(.*)/WebRoot");
						Matcher m  = pattern1.matcher(str);
						if(m.find()){
							xiangmu=m.group(1);
						}	
						filepathpre=workpath+xiangmu;
						despath = outpath+xiangmu;
					}
					
					String filepath = filepathpre+filepathend;
					if(xiangmutime.equals("")){
						xiangmutime = getXiangmutime(filepath);
						despath = despath+"/"+xiangmu+" "+xiangmutime;
					}
					copyFile(filepath, despath+filepathend.replace("/WebRoot/", "/"));
				}
			}
			
			//内部类
			for(String s:classpathmap.keySet()){
				File fileneibu = new File(s);
				for(File fileneibuf:fileneibu.listFiles()){
					if(fileneibuf.isDirectory())
						continue;
					for(String ls:classpathmap.get(s)){
						String filename = ls.substring(ls.lastIndexOf("/")+1,ls.length());
						if(fileneibuf.getName().startsWith(filename+"$")){
							copyFile(fileneibuf.getAbsolutePath(),despath+"/WEB-INF/classes/"+ls.replace(filename, "")+fileneibuf.getName());
						}
					}
				}
			}
			
			File file2  = new File(despath+File.separator+ConfigurationRead.getInstance().getConfigItem("txt_info")+".txt");
			file2.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static String getXiangmutime(String filepath){
		File file2 = new File(filepath);
		Date modifytime = new Date(file2.lastModified());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = sdf.format(modifytime);
		return times;
	}
	
	
	
	
	
	
	
	
	
	public static void copyFile(String oldPath, String newPath) { 
	       try { 
	    	   if(oldPath.endsWith(".properties")||oldPath.endsWith("spring-quartz.xml"))
	    		   System.out.println("配置文件有更新："+newPath);
	           int bytesum = 0; 
	           int byteread = 0; 
	           File oldfile = new File(oldPath); 
	           if (oldfile.exists()) { //文件存在时 
	               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
	               File newfile = new File(newPath.substring(0,newPath.lastIndexOf("/")));
	               if(!newfile.exists())
	            	   newfile.mkdirs();
	               FileOutputStream fs = new FileOutputStream(newPath); 
	               byte[] buffer = new byte[1444]; 
	               int length; 
	               while ( (byteread = inStream.read(buffer)) != -1) { 
	                   bytesum += byteread; //字节数 文件大小 
	                   fs.write(buffer, 0, byteread); 
	               } 
	               inStream.close(); 
	           } else{
	        	   System.out.println("原文件不存在请检查路径");
	        	   System.out.println(oldPath);
	           }
	       } 
	       catch (Exception e) { 
	           System.out.println("复制单个文件操作出错"); 
	           e.printStackTrace(); 

	       } 

	   } 
	 private static void deleteFile(File file){
		       if(file.isDirectory()){
		            File[] files = file.listFiles();
		            for(int i=0; i<files.length; i++){
		                 deleteFile(files[i]);
		            }
		       }
		       file.delete();
		  }
}	
