package socialNet.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import socialNet.Entity.UserEntity;
import socialNet.Storage.StorageFileNotFoundException;
import socialNet.Storage.StorageService;
import socialNet.repos.CommentRepo;
import socialNet.repos.PostRepo;
import socialNet.repos.UserRepo;

import java.io.IOException;

@Controller
@PreAuthorize("hasAuthority('USER')")

public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {


        return "upload";
    }

    @GetMapping("upload/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserEntity currentUser) {
        storageService.store(file);
        currentUser.setAvatar(file.getOriginalFilename());
        postRepo.changeAllPostsAva(file.getOriginalFilename(),currentUser.getId());
        commentRepo.changeAllCommentsAva(file.getOriginalFilename(),currentUser.getId());
        userRepo.save(currentUser);
        return "redirect:/user";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
