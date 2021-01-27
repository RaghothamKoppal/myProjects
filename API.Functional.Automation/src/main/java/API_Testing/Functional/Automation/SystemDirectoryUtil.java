package API_Testing.Functional.Automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import com.sun.media.jfxmedia.logging.Logger;

public class SystemDirectoryUtil {
	
	public static boolean createDir(String path){
	    boolean result = false;
	    File directory = new File(path);
	    if (directory.exists()) {
	        System.out.println("Folder already exists :"+path);
	    } else {
	        result = directory.mkdirs();
	        System.out.println("Directory created in "+path);
	    }
	    return result;
	}
	
	public static void deleteDir(String path){
		File index = new File(path);
		if (index.exists())
	    	index.delete();
	}
	
	public static boolean isDirectoryExists(String path){
		File directory = new File(path);
		 if (directory.exists())
			 return true;
		 else
			 return false;
	}
	
	
	public static void zipDir(String srcFolder, String destZipFile) throws Exception {
		 ZipOutputStream zip = null;
		 FileOutputStream fileWriter = null;
		 fileWriter = new FileOutputStream(destZipFile);
		 zip = new ZipOutputStream(fileWriter);
		 addFolderToZip("", srcFolder, zip);
		 zip.flush();
		 zip.close();
	 }
	private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		 File folder = new File(srcFolder);
		 for (String fileName : folder.list()) {
			 if(!fileName.startsWith(".")){
				 if (path.equals(""))
					 addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
				 else
					 addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			 }
		 }
	 }
	private static void addFileToZip(String path, String srcFile, ZipOutputStream zip)throws Exception {
		 File folder = new File(srcFile);
		 if (folder.isDirectory()) {
			 addFolderToZip(path, srcFile, zip);
		 } else {
			 byte[] buf = new byte[1024];
			 int len;
			 FileInputStream in = new FileInputStream(srcFile);
			 zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
			 while ((len = in.read(buf)) > 0) {
				 zip.write(buf, 0, len);
			 }
		 }
	 }
	
	
	public static void copyFilesToDirectory(String[] files, String directory) throws IOException
	{
		for(int i=0; i<files.length; i++)
		{
			FileUtils.copyFileToDirectory(new File(files[i]),
					new File(directory));
		}
	}	
	
	
	
}
