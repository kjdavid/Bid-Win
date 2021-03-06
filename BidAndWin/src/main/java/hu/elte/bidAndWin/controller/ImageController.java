package hu.elte.bidAndWin.controller;

import static hu.elte.bidAndWin.domain.User.Role.ADMIN;
import static hu.elte.bidAndWin.domain.User.Role.USER;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.bidAndWin.annotation.Role;
import hu.elte.bidAndWin.domain.Image;
import hu.elte.bidAndWin.service.ImageService;
import hu.elte.bidAndWin.service.UserNotValidException;
import hu.elte.bidAndWin.service.UserService;

@RestController
@RequestMapping(value = "/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Role({ADMIN, USER})
    @PostMapping("/uploadimage")
    public ResponseEntity<Image> createImage(@RequestBody Image image) {
        try {
            return ResponseEntity.ok(imageService.uploadImage(image, userService.getLoggedInUser()));
        } catch (UserNotValidException | IOException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Role({ADMIN, USER})
    @GetMapping(value = "/{id}")
    public ResponseEntity<Image> getImageByItemId(@PathVariable(value = "id") long id) {
        try {
            return ResponseEntity.ok(imageService.getImageByItemId(id, userService.getLoggedInUser()));
        } catch (UserNotValidException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @Role({ADMIN, USER})
    @GetMapping("/all")
    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }
}
