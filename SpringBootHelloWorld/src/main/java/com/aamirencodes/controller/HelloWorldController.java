package com.aamirencodes.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloWorldController {
	@RequestMapping("/")  
	public String hello()   
	{  
	return "Hello from the other side...";  
	} 
	
	@RequestMapping("/imagedownload")
	public void imageDownloadApi() { 
		//return "Image Download API Successfully called...";
		
		RestTemplate restTemplate = new RestTemplate();
		String url = "https://enterpriseuat.viacomcbs.com/customrestservice/paramountuwf/largeFileDownload/5fbf58eb0252a1f1fb52d97d8183bc9a549de432";

		// Optional Accept header
		RequestCallback requestCallback = request -> request.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));

		// Streams the response instead of loading it all in memory
		ResponseExtractor<Void> responseExtractor = response -> {
		    // Here I write the response to a file but do what you like
		    Path path = Paths.get("/home/aamir/Downloads/aamir_1.jpg");
		    Files.copy(response.getBody(), path);
		    System.out.println("File download Completed...");
		    return null;
		};
		restTemplate.execute(URI.create(url), HttpMethod.GET, requestCallback, responseExtractor);
	}
	
	@RequestMapping("/imagedownload2")
	public String imageDownloadApi2(HttpServletRequest req, HttpServletResponse res) {
		
		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = "https://enterpriseuat.viacomcbs.com/customrestservice/paramountuwf/largeFileDownload/3f8c4507d8dcce94f3ae5b4da04030d53fe508b9";
			
            /*if (restTemplate == null)
                logger.info("******* rest template is null***********************");*/
            RequestCallback requestCallback = request -> request.getHeaders()
                    .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));

            // Streams the response instead of loading it all in memory
            ResponseExtractor<ResponseEntity<InputStream>> responseExtractor = response -> {

                String contentDisposition = response.getHeaders().getFirst("Content-Disposition");
                if (contentDisposition != null) {
                    // Temporary location for files that will be downloaded from micro service and
                    // act as final source of download to user
                    String filePath = "/home/aamir/Downloads/" + contentDisposition.split("=")[1];
                    Path path = Paths.get(filePath);
                    Files.copy(response.getBody(), path, StandardCopyOption.REPLACE_EXISTING);

                    // Create a new input stream from temporary location and use it for downloading
                    /*InputStream inputStream = new FileInputStream(filePath);
                    String type = req.getServletContext().getMimeType(filePath);
                    res.setHeader("Content-Disposition", "attachment; filename=" + contentDisposition.split("=")[1]);
                    res.setHeader("Content-Type", type);

                    byte[] outputBytes = new byte[1024*4];
                    OutputStream os = res.getOutputStream();
                    int read = 0;
                    while ((read = inputStream.read(outputBytes)) != -1) {
                        os.write(outputBytes, 0, read);
                    }
                    os.flush();
                    os.close();
                    inputStream.close();*/
                }
                return null;
            };
            restTemplate.execute(url, HttpMethod.GET, requestCallback, responseExtractor);
        } catch (Exception e) {
            throw e;
        }
		
		return "Image Download API 2 completed";
	}
}
