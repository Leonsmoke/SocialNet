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

import static socialNet.constant.pages.*;

@Controller
@PreAuthorize("hasAuthority('USER')||hasAuthority('ADMIN')")

public class FileUploadController {

    @Autowired
    UploadService uploadService;

    @GetMapping("/upload")
    public String uploadAvaPage(Model model, @AuthenticationPrincipal UserEntity currentUser) throws IOException {
        model.addAttribute("type","/profile");
        model.addAttribute("user",currentUser);
        return UPLOAD_PAGE;
    }

    @GetMapping("/upload/community{id}")
    public String uploadAvaforCommunities(Model model, @PathVariable int id, @AuthenticationPrincipal UserEntity currentUser) throws IOException {
        model.addAttribute("type","/community"+id);
        model.addAttribute("user",currentUser);
        return UPLOAD_PAGE;
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
        return REDIRECT_TO_PROFILE;
    }

    @PostMapping("/upload/community{id}")
    public String handleFileUploadCommunityAva(@RequestParam("file") MultipartFile file,
                                               @PathVariable int id, @AuthenticationPrincipal UserEntity currentUser) {
        uploadService.uploadCommunityAva(id,currentUser,file);
        return REDIRECT+COMMUNITY_LINK+"/"+id;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
