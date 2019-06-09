package socialNet.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import socialNet.Entity.UserEntity;
import socialNet.Service.UploadService;
import socialNet.Storage.StorageFileNotFoundException;

import java.io.IOException;

@Controller
@PreAuthorize("hasAuthority('USER')")

public class FileUploadController {

    @Autowired
    UploadService uploadService;

    @GetMapping("/upload")
    public String uploadAvaPage(Model model) throws IOException {
        model.addAttribute("type","/profile");
        return "upload";
    }

    @GetMapping("/upload/community{id}")
    public String uploadAvaforCommunities(Model model, @PathVariable int id) throws IOException {
        model.addAttribute("type","/community"+id);
        return "upload";
    }

    @GetMapping("upload/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        return uploadService.serveFile(filename);
    }

    @PostMapping("/upload/profile")
    public String handleFileUploadProfileAva(@RequestParam("file") MultipartFile file,
                                             @AuthenticationPrincipal UserEntity currentUser) {
        uploadService.uploadProfileAva(currentUser,file);
        return "redirect:/user";
    }

    @PostMapping("/upload/community{id}")
    public String handleFileUploadCommunityAva(@RequestParam("file") MultipartFile file,
                                               @PathVariable int id, @AuthenticationPrincipal UserEntity currentUser) {
        uploadService.uploadCommunityAva(id,currentUser,file);
        return "redirect:/community/"+id;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
