package hu.elte.bidAndWin.controller;

import static hu.elte.bidAndWin.domain.User.Role.ADMIN;
import static hu.elte.bidAndWin.domain.User.Role.USER;

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
import hu.elte.bidAndWin.domain.Bid;
import hu.elte.bidAndWin.service.BidNotValidException;
import hu.elte.bidAndWin.service.BidService;
import hu.elte.bidAndWin.service.UserNotValidException;
import hu.elte.bidAndWin.service.UserService;

@RestController
@RequestMapping(value = "/api/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    @Autowired
    private UserService userService;

    @Role({ADMIN, USER})
    @GetMapping(value = "/all")
    public ResponseEntity<List<Bid>> getAllBids() {
        return ResponseEntity.ok(bidService.getAllBids());
    }

    @Role({ADMIN, USER})
    @GetMapping(value = "/mybids")
    public ResponseEntity<List<Bid>> getMyBids() {
        try {
            return ResponseEntity.ok(bidService.getMyBids(userService.getLoggedInUser()));
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Role({ADMIN, USER})
    @GetMapping(value = "/{id}")
    public ResponseEntity<Bid> getBid(@PathVariable(value = "id") long id) {
        try {
            return ResponseEntity.ok(bidService.getBid(id, userService.getLoggedInUser()));
        } catch (UserNotValidException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Role({ADMIN, USER})
    @PostMapping("/makebid")
    private ResponseEntity<Bid> makeBid(@RequestBody Bid bid) {
        try {
            return ResponseEntity.ok(bidService.makeBid(bid, userService.getLoggedInUser()));
        } catch (BidNotValidException | NullPointerException | UserNotValidException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
