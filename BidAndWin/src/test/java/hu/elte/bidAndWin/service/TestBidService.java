package hu.elte.bidAndWin.service;

import hu.elte.bidAndWin.domain.Bid;
import hu.elte.bidAndWin.domain.Category;
import hu.elte.bidAndWin.domain.Image;
import hu.elte.bidAndWin.domain.Item;
import hu.elte.bidAndWin.domain.User;
import hu.elte.bidAndWin.repository.BidRepository;
import hu.elte.bidAndWin.repository.ItemRepository;
import hu.elte.bidAndWin.repository.UserRepository;
import java.sql.Timestamp;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestBidService {

    @Mock
    BidRepository bidRepositoryMock;
    @Mock
    UserRepository userRepositoryMock;
    @Mock
    ItemRepository itemRepositoryMock;

    @InjectMocks
    BidService bidService;

    List<Bid> listBidEmpty;
    List<Image> listImageEmpty;
    List<Category> listCategoryEmpty;
    List<Item> listItemEmpty;
    List<Bid> listBidNotEmpty;
    List<Bid> listBidNotEmptyTwo;
    List<Image> listImageNotEmpty;
    List<Category> listCategoryNotEmpty;
    List<Item> listItemNotEmpty;

    Timestamp timestampFuture;
    byte[] byteEmpty;

    Bid bidNull;
    Category categoryNull;
    Item itemNull;
    Image imageNull;
    User userNull;

    Bid bidNotEmpty;
    Bid bidNotEmptyOriginal;
    User userNotEmpty;
    User userNotEmptyAdmin;
    Category categoryNotEmpty;
    Category categoryNotEmptyTwo;
    Item itemNotEmpty;
    Image imageNotEmpty;
    Image imageNotEmptyTwo;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        listBidEmpty = new LinkedList<>();
        listImageEmpty = new LinkedList<>();
        listCategoryEmpty = new LinkedList<>();
        listItemEmpty = new LinkedList<>();
        listBidNotEmpty = new LinkedList<>();
        listBidNotEmptyTwo = new LinkedList<>();
        listImageNotEmpty = new LinkedList<>();
        listCategoryNotEmpty = new LinkedList<>();
        listItemNotEmpty = new LinkedList<>();

        timestampFuture = new Timestamp(1000000000000000000L);

        byteEmpty = "".getBytes();

        userNotEmpty = spy(new User(listItemEmpty, listBidNotEmptyTwo, 2, "david", "2222", "2@2", User.Role.USER));
        userNotEmptyAdmin = spy(new User(listItemNotEmpty, listBidNotEmpty, 1, "zoli", "1111", "1@1", User.Role.ADMIN));
        categoryNotEmpty = spy(new Category(listItemNotEmpty, 1, "auto"));
        categoryNotEmptyTwo = spy(new Category(listItemEmpty, 2, "szamitogep"));
        itemNotEmpty = spy(new Item(listImageNotEmpty, userNotEmptyAdmin, categoryNotEmpty, "trabant", "jokocsi", 0, 1000000, timestampFuture, 100));
        imageNotEmpty = spy(new Image("autoitem", "path", itemNotEmpty));
        imageNotEmptyTwo = spy(new Image("autoitem2", "path2", itemNotEmpty));
        bidNotEmpty = spy(new Bid(itemNotEmpty, userNotEmpty, 2, 1000));
        bidNotEmptyOriginal = spy(new Bid(itemNotEmpty, userNotEmptyAdmin, 1, 500));

        listBidNotEmpty.add(bidNotEmptyOriginal);
        listBidNotEmptyTwo.add(bidNotEmpty);
        listImageNotEmpty.add(imageNotEmpty);
        listItemNotEmpty.add(itemNotEmpty);
        listCategoryNotEmpty.add(categoryNotEmpty);
    }

    @Test
    public void testGetAllBids_ReturnBidList() {
        doReturn(listBidNotEmpty).when(bidRepositoryMock).findAll();
        assertEquals(bidService.getAllBids(), listBidNotEmpty);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMyBids_NullPointerException() {
        bidService.getMyBids(userNull);
    }

    @Test
    public void testGetMyBids_ReturnBidList() {
        long userId = userNotEmpty.getId();
        doReturn(listBidNotEmpty).when(bidRepositoryMock).findByUserId(userId);
        assertEquals(bidService.getMyBids(userNotEmpty), listBidNotEmpty);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBid_NullPointerException() throws UserNotValidException {
        bidService.getBid(1, userNull);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBid_NullPointerException2() throws UserNotValidException {
        doReturn(bidNull).when(bidRepositoryMock).findById(1);
        bidService.getBid(1, userNotEmpty);
    }

    @Test(expected = UserNotValidException.class)
    public void testGetBid_UserNotValidException() throws UserNotValidException {
        doReturn(bidNotEmpty).when(bidRepositoryMock).findById(1);
        bidService.getBid(1, userNotEmpty);
    }

    @Test
    public void testGetBid_ReturnBid() throws UserNotValidException {
        doReturn(bidNotEmpty).when(bidRepositoryMock).findById(1);
        assertEquals(bidService.getBid(1, userNotEmptyAdmin), bidNotEmpty);
    }

    @Test(expected = NullPointerException.class)
    public void testMakeBid_NullPointerException() throws BidNotValidException, UserNotValidException {
        bidService.makeBid(bidNotEmpty, userNull);
    }

    @Test(expected = NullPointerException.class)
    public void testMakeBid_NullPointerException2() throws BidNotValidException, UserNotValidException {
        bidService.makeBid(bidNull, userNotEmpty);
    }

    @Test(expected = NullPointerException.class)
    public void testMakeBid_NullPointerException3() throws BidNotValidException, UserNotValidException {
        long id = bidNotEmpty.getItem().getId();
        doReturn(itemNull).when(itemRepositoryMock).findById(id);
        bidService.makeBid(bidNotEmpty, userNotEmpty);
    }

    @Test(expected = UserNotValidException.class)
    public void testMakeBid_UserNotValidException() throws BidNotValidException, UserNotValidException {
        long id = bidNotEmpty.getItem().getId();
        doReturn(itemNotEmpty).when(itemRepositoryMock).findById(id);
        doReturn(bidNotEmpty).when(bidRepositoryMock).findFirstByItemIdOrderByBidOfferDesc(id);
        bidService.makeBid(bidNotEmpty, userNotEmptyAdmin);
    }

    @Test(expected = BidNotValidException.class)
    public void testMakeBid_BidNotValidException() throws BidNotValidException, UserNotValidException {
        long id = bidNotEmpty.getItem().getId();
        doReturn(itemNotEmpty).when(itemRepositoryMock).findById(id);
        doReturn(bidNotEmptyOriginal).when(bidRepositoryMock).findFirstByItemIdOrderByBidOfferDesc(id);
        bidNotEmpty.getItem().setEndTime(new Timestamp(0));
        bidService.makeBid(bidNotEmpty, userNotEmpty);
    }

    @Test(expected = BidNotValidException.class)
    public void testMakeBid_BidNotValidException2() throws BidNotValidException, UserNotValidException {
        long id = bidNotEmpty.getItem().getId();
        doReturn(itemNotEmpty).when(itemRepositoryMock).findById(id);
        doReturn(bidNotEmptyOriginal).when(bidRepositoryMock).findFirstByItemIdOrderByBidOfferDesc(id);
        bidNotEmpty.setBidOffer(0);
        bidService.makeBid(bidNotEmpty, userNotEmpty);
    }

    @Test
    public void testMakeBid_ReturnBid() throws BidNotValidException, UserNotValidException {
        long id = bidNotEmpty.getItem().getId();
        doReturn(itemNotEmpty).when(itemRepositoryMock).findById(id);
        doReturn(bidNotEmptyOriginal).when(bidRepositoryMock).findFirstByItemIdOrderByBidOfferDesc(id);
        doReturn(bidNotEmptyOriginal).when(bidRepositoryMock).save(bidNotEmptyOriginal);
        bidNotEmptyOriginal.setUser(userNotEmpty); 
        assertEquals(bidService.makeBid(bidNotEmpty, userNotEmpty), bidNotEmptyOriginal);
    }

    @Test
    public void testMakeBid_ReturnBid2() throws BidNotValidException, UserNotValidException {
        long id = bidNotEmpty.getItem().getId();
        doReturn(itemNotEmpty).when(itemRepositoryMock).findById(id);
        doReturn(bidNotEmptyOriginal).when(bidRepositoryMock).findFirstByItemIdOrderByBidOfferDesc(id);
        doReturn(bidNotEmpty).when(bidRepositoryMock).save(bidNotEmpty);
        doReturn(itemNotEmpty).when(itemRepositoryMock).save(itemNotEmpty);
        assertEquals(bidService.makeBid(bidNotEmpty, userNotEmpty), bidNotEmpty);
    }

}
