package cn.yclv.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

/** 
 * 从网络获取图片到本地 
 */  
public class FileUtil {  
	
    /** 
     * 测试 
     * @param args 
     */  
    public static void main(String[] args) {  
        String url = "http://www.baidu.com/img/baidu_sylogo1.gif";  
        byte[] btImg = getImageFromNetByUrl(url);  
        if(null != btImg && btImg.length > 0){  
            System.out.println("读取到：" + btImg.length + " 字节");  
            String fileName = "百度.gif";  
            writeImageToDisk(btImg, fileName);  
        }else{  
            System.out.println("没有从该连接获得内容");  
        }  
    }
    
    
    public static String downloadPicture(String picUrl, String savePath, String picName) {  
        try {
        	URL url = new URL(picUrl);  
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            File file = createFile(savePath, picName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);  
            byte[] buffer = new byte[1024];  
            int length;  
            while ((length = dataInputStream.read(buffer)) > 0) {  
                fileOutputStream.write(buffer, 0, length);
            }  
            dataInputStream.close();  
            fileOutputStream.close();
            return savePath + picName;
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
        return "";
    }
    
    
    public static File createFile(String filePath) throws IOException{
    	File file = new File(filePath);
    	checkFile(file);
    	return file;
    }

    
	public static File createFile(String savePath, String picName) throws IOException {
		File file = new File(savePath + picName);
		checkFile(file);
		return file;
	}

	
	public static void checkFile(File file) throws IOException {
		if(file.isDirectory()){
			file.mkdirs();
		}
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		if(!file.exists()){
			file.createNewFile();
		}
	}  
    
    
    /** 
     * 将图片写入到磁盘 
     * @param img 图片数据流 
     * @param fileName 文件保存时的名称 
     */  
    public static void writeImageToDisk(byte[] img, String fileName){  
        try {  
            File file = new File("E:\\java" + fileName);
            if(file.exists()){
//            	file.
            }
            FileOutputStream fops = new FileOutputStream(file);  
            fops.write(img);  
            fops.flush();  
            fops.close();  
            System.out.println("图片已经写入到D盘");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    
    /** 
     * 根据地址获得数据的字节流 
     * @param strUrl 网络连接地址 
     * @return 
     */  
    public static byte[] getImageFromNetByUrl(String strUrl){  
        try {  
            URL url = new URL(strUrl);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(5 * 1000);  
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
            return btImg;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }
    
    
    /** 
     * 从输入流中获取数据 
     * @param inStream 输入流 
     * @return 
     * @throws Exception 
     */  
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1 ){  
            outStream.write(buffer, 0, len);  
        }  
        inStream.close();  
        return outStream.toByteArray();  
    }  
    
    
    /**
	 * 读取txt文档，保存成string
	 * @return
	 */
	public static String readTxt(String path) {
		File file = new File(path);
		BufferedReader br;
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(fis);
			br = new BufferedReader(reader);
			String lineText = null;
			while((lineText = br.readLine()) != null){
				sb.append(lineText + "\r");
			}
			br.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return sb.toString();
	}
	
	
	/**
	 * 读取txt文档，保存成string
	 * @return
	 */
	public static String readTxtReplace(String path, String lineNeedToBeReplaced) {
		File file = new File(path);
		BufferedReader br;
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(fis);
			br = new BufferedReader(reader);
			String lineText = null;
			while((lineText = br.readLine()) != null){
				if(lineText.contains(lineNeedToBeReplaced) /*|| lineText.contains("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />"*/){
					String replace = lineText.replace(lineNeedToBeReplaced, "../images");
					sb.append(replace + "\r");
				}else{
					sb.append(lineText + "\r");
				}
			}
			br.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return sb.toString();
	}
	
	
	
	/**
	 * 写入文件
	 * @return
	 */
	public static boolean writeTxt(String content, String path) {
		File file = new File(path);
		BufferedWriter output = null;
		try {
			if(file.exists()){
				System.out.println(file.getName() + "存在");
			}else{
				file.createNewFile();
			}
			output = new BufferedWriter(new FileWriter(file));  
			output.write(content);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/** 
     * 复制单个文件 
     *  
     * @param srcFileName 
     *            待复制的文件名 
     * @param descFileName 
     *            目标文件名 
     * @param overlay 
     *            如果目标文件存在，是否覆盖 
     * @return 如果复制成功返回true，否则返回false 
     */  
    public static boolean copyFile(String srcFileName, String destFileName,  
            boolean overlay) {  
        File srcFile = new File(srcFileName);  
        if (!srcFile.exists()) {  
            String msg = "源文件：" + srcFileName + "不存在！";  
            JOptionPane.showMessageDialog(null, msg);  
            return false;  
        } else if (!srcFile.isFile()) {  
            String msg = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";  
            JOptionPane.showMessageDialog(null, msg);  
            return false;  
        }  
        File destFile = new File(destFileName);  
        if (destFile.exists()) {  
            if (overlay) {  
                new File(destFileName).delete();  
            }  
        } else {  
            if (!destFile.getParentFile().exists()) { 
                if (!destFile.getParentFile().mkdirs()) {
                    return false;
                }  
            }  
        }  
  
        int byteread = 0; 
        InputStream in = null;  
        OutputStream out = null;  
  
        try {  
            in = new FileInputStream(srcFile);  
            out = new FileOutputStream(destFile);  
            byte[] buffer = new byte[1024];  
  
            while ((byteread = in.read(buffer)) != -1) {  
                out.write(buffer, 0, byteread);  
            }  
            return true;  
        } catch (FileNotFoundException e) {  
            return false;  
        } catch (IOException e) {  
            return false;  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }
    
    
    
    /** 
     * 复制整个目录的内容 
     *  
     * @param srcDirName 
     *            待复制目录的目录名 
     * @param destDirName 
     *            目标目录名 
     * @param overlay 
     *            如果目标目录存在，是否覆盖 
     * @return 如果复制成功返回true，否则返回false 
     */  
    public static boolean copyDirectory(String srcDirName, String destDirName,  
            boolean overlay) {
    	String msg  = "";
        File srcDir = new File(srcDirName);  
        if (!srcDir.exists()) {  
        	msg = "复制目录失败：源目录" + srcDirName + "不存在！";  
            JOptionPane.showMessageDialog(null, msg);  
            return false;  
        } else if (!srcDir.isDirectory()) {  
        	msg = "复制目录失败：" + srcDirName + "不是目录！";  
            JOptionPane.showMessageDialog(null, msg);  
            return false;  
        }  
        if (!destDirName.endsWith(File.separator)) {  
            destDirName = destDirName + File.separator;  
        }  
        File destDir = new File(destDirName);  
        if (destDir.exists()) {  
            if (overlay) {  
                new File(destDirName).delete();  
            } else {  
            	msg = "复制目录失败：目的目录" + destDirName + "已存在！";  
                JOptionPane.showMessageDialog(null, msg);  
                return false;  
            }  
        } else {  
            System.out.println("目的目录不存在，准备创建。。。");  
            if (!destDir.mkdirs()) {  
                System.out.println("复制目录失败：创建目的目录失败！");  
                return false;  
            }  
        }  
        boolean flag = true;  
        File[] files = srcDir.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            if (files[i].isFile()) {  
                flag = FileUtil.copyFile(files[i].getAbsolutePath(),  
                        destDirName + files[i].getName(), overlay);  
                if (!flag)  
                    break;  
            } else if (files[i].isDirectory()) {  
                flag = FileUtil.copyDirectory(files[i].getAbsolutePath(),  
                        destDirName + files[i].getName(), overlay);  
                if (!flag)  
                    break;  
            }  
        }  
        if (!flag) {  
            msg = "复制目录" + srcDirName + "至" + destDirName + "失败！";  
            JOptionPane.showMessageDialog(null, msg);  
            return false;  
        } else {  
            return true;  
        }  
    }
    
    
    /** 
     * 剪切单个文件 
     *  
     * @param srcFileName 
     *            待剪切的文件名 
     * @param descFileName 
     *            剪切文件名 
     * @param overlay 
     *            如果目标文件存在，是否覆盖 
     * @return 如果剪切成功返回true，否则返回false 
     */  
    public static boolean moveFile(String srcFileName, String destFileName,  
            boolean overlay) {  
        File srcFile = new File(srcFileName);  
        if (!srcFile.exists()) {  
            String msg = "源文件：" + srcFileName + "不存在！";  
//            JOptionPane.showMessageDialog(null, msg);  
            return false;  
        } else if (!srcFile.isFile()) {  
            String msg = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";  
//            JOptionPane.showMessageDialog(null, msg);  
            return false;  
        }  
        File destFile = new File(destFileName);  
        if (destFile.exists()) {  
            if (overlay) {  
                new File(destFileName).delete();  
            }  
        } else {  
            if (!destFile.getParentFile().exists()) { 
                if (!destFile.getParentFile().mkdirs()) {
                    return false;
                }  
            }  
        }  
        int byteread = 0;
        InputStream in = null;  
        OutputStream out = null;  
        try {  
            in = new FileInputStream(srcFile);  
            out = new FileOutputStream(destFile);  
            byte[] buffer = new byte[1024];  
  
            while ((byteread = in.read(buffer)) != -1) {  
                out.write(buffer, 0, byteread);  
            }
            if(in != null)
            in.close();
            if(out != null)
            out.close();
            boolean res = srcFile.delete();
            System.out.println(srcFileName + "是否已删除:" + res);
            return true;  
        } catch (FileNotFoundException e) {  
            return false;  
        } catch (IOException e) {  
            return false;  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }
}  