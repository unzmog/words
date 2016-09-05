package lt.example;



import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.File;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
	
    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("woop");
    }
    
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView uploadtest(@RequestParam("file") MultipartFile[] submissions) throws IOException, InterruptedException{
    	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    	for (MultipartFile mf : submissions){
    		FileReadTask task = new FileReadTask(mf);
    		executor.execute(task);
    	}
    	executor.shutdown();
    	while (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
    		System.out.println("waiting!");
    		}
    	Map<String, Integer> map = FileReadTask.getData();
    	FileReadTask.freeMap();
    	
    	FileWriter file1 = new FileWriter("file1.txt");
    	FileWriter file2 = new FileWriter("file2.txt");
    	FileWriter file3 = new FileWriter("file3.txt");
    	FileWriter file4 = new FileWriter("file4.txt");
    	String newLine = System.getProperty("line.separator");
    	
    	for (Map.Entry<String, Integer> entry : map.entrySet()){
    		char c = entry.getKey().charAt(0);
    		if (c >= 'a' && c <= 'g'){
    			file1.write(entry.getKey() + " - " + entry.getValue().intValue() + newLine);
    		}
    		if (c >= 'h' && c <= 'n'){
    			file2.write(entry.getKey() + " - " + entry.getValue().intValue() + newLine);
    		}
    		if (c >= 'o' && c <= 'u'){
    			file3.write(entry.getKey() + " - " + entry.getValue().intValue() + newLine);
    		}
    		if (c >= 'v' && c <= 'z'){
    			file4.write(entry.getKey() + " - " + entry.getValue().intValue() + newLine);
    		}
    
    	}
    	file1.flush();
    	file2.flush();
    	file3.flush();
    	file4.flush();
    	
    	file1.close();
    	file2.close();
    	file3.close();
    	file4.close();
    	
    	ModelAndView mav = new ModelAndView("upload", "files", map);
    	mav.addObject("file1", file1);
    	mav.addObject("file2", file2);
    	mav.addObject("file3", file3);
    	mav.addObject("file4", file4);
		return mav;
    	
    }

}
