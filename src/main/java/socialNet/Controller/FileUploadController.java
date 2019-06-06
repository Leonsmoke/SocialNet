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
import socialNet.Entity.Community;
import socialNet.Entity.UserEntity;
import socialNet.Storage.StorageFileNotFoundException;
import socialNet.Storage.StorageService;
import socialNet.repos.CommentRepo;
import socialNet.repos.CommunityRepo;
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
    private CommunityRepo communityRepo;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

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

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload/profile")
    public String handleFileUploadProfileAva(@RequestParam("file") MultipartFile file,
                                             @AuthenticationPrincipal UserEntity currentUser) {
        String addedStringToAvaName = "user"+currentUser.getId();
        storageService.store(file,addedStringToAvaName);
        String newPathToAva = addedStringToAvaName+file.getOriginalFilename();
        currentUser.setAvatar(newPathToAva);
        postRepo.changeAllPostsAva(newPathToAva,currentUser.getId());
        commentRepo.changeAllCommentsAva(newPathToAva,currentUser.getId());
        userRepo.save(currentUser);
        return "redirect:/user";
    }

    @PostMapping("/upload/community{id}")
    public String handleFileUploadCommunityAva(@RequestParam("file") MultipartFile file,
                                               @PathVariable int id, @AuthenticationPrincipal UserEntity currentUser) {
        Community community = communityRepo.findById(id);
        String addedStringToAvaName = "comm"+community.getId();
        if (community.getAdmin_id()==currentUser.getId()){
            storageService.store(file,addedStringToAvaName);
            String newPathToAva = addedStringToAvaName+file.getOriginalFilename();
            community.setAvatar(newPathToAva);
            communityRepo.save(community);
        }

        //postRepo.changeAllPostsAva(file.getOriginalFilename(),currentUser.getId());
        //commentRepo.changeAllCommentsAva(file.getOriginalFilename(),currentUser.getId());
        return "redirect:/community/"+id;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
