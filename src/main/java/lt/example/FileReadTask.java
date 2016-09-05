package lt.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class FileReadTask implements Runnable{
	
	private MultipartFile file;
	private static Map<String, Integer> data = new HashMap<String, Integer>();
	
	public static synchronized void countWord(String word){
		if (!data.containsKey(word)){
			data.put(word, 1);
		} else {
			data.put(word, data.get(word).intValue()+1);
		}
	}
	
	public static Map<String, Integer> getData(){
		return data;
	}
	
	public static void freeMap(){
		data = new HashMap<String, Integer>();
	}
	
	public FileReadTask(MultipartFile file) {
		this.file = file;
	}
	
	

	public MultipartFile getFile() {
		return file;
	}



	@Override
	public void run() {
		
		try {
			MultipartFile file = this.getFile();
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null){
				String[] words = line.toLowerCase().replaceAll("[^a-z\\s]", "").split(" ");
				
				for (String word : words){
					countWord(word);
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
