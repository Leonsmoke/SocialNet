package socialNet.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;
import socialNet.Entity.Community;
import socialNet.Entity.UserEntity;
import socialNet.Storage.StorageFileNotFoundException;
import socialNet.Storage.StorageService;
import socialNet.repos.CommentRepo;
import socialNet.repos.CommunityRepo;
import socialNet.repos.PostRepo;
import socialNet.repos.UserRepo;


@Service
public class UploadService {

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
    public UploadService(StorageService storageService) {
        this.storageService = storageService;
    }

    public ResponseEntity<Resource> serveFile(String filename){
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    public void uploadProfileAva(UserEntity currentUser, MultipartFile file){
        String addedStringToAvaName = "user"+currentUser.getId();
        storageService.store(file,addedStringToAvaName);
        String newPathToAva = addedStringToAvaName+file.getOriginalFilename();
        currentUser.setAvatar(newPathToAva);
        postRepo.changeAllPostsAva(newPathToAva,currentUser.getId());
        commentRepo.changeAllCommentsAva(newPathToAva,currentUser.getId());
        userRepo.save(currentUser);
    }

    public void uploadCommunityAva(int id, UserEntity currentUser, MultipartFile file){
        Community community = communityRepo.findById(id);
        String addedStringToAvaName = "comm"+community.getId();
        if (community.getAdmin_id()==currentUser.getId()){
            storageService.store(file,addedStringToAvaName);
            String newPathToAva = addedStringToAvaName+file.getOriginalFilename();
            community.setAvatar(newPathToAva);
            communityRepo.save(community);
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
