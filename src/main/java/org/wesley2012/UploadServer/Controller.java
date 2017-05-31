package org.wesley2012.UploadServer;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RestController
public class Controller {
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Usage: POST /\n"
                + "form variables: \n"
                + "file: the uploading content\n"
                + "target: the target file path, optional; ending with / means a directory\n";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "multipart/form-data")
    public void upload(@RequestParam("file") MultipartFile file,
                       @RequestParam(value = "target", required = false) String target){
        try {
            String dir;

            String cwd = System.getProperty("user.dir");

            if (target != null){
                if (target.endsWith("/")){
                    dir = cwd + "/" + target;
                    target = dir + file.getOriginalFilename();
                }
                else {
                    target = cwd + "/" + target;
                    dir = new File(target).getParent();
                }
                if (Files.notExists(new File(dir).toPath())){
                    Files.createDirectories(new File(dir).toPath());
                }
            }
            else {
                target = cwd + "/" + file.getOriginalFilename();
            }

            file.transferTo(new File(target));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
