package hu.elte.bidAndWin.service;

import hu.elte.bidAndWin.domain.Bid;
import hu.elte.bidAndWin.domain.Category;
import hu.elte.bidAndWin.domain.Image;
import hu.elte.bidAndWin.domain.Item;
import hu.elte.bidAndWin.domain.User;
import hu.elte.bidAndWin.repository.UserRepository;
import java.sql.Timestamp;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestUserService {

    @Test
    public void TestUser() {
        assertTrue(true);
    }

    @Mock
    UserRepository userRepositoryMock;

    @InjectMocks
    UserService userService;

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

    @Test(expected = NullPointerException.class)
    public void testRegister_NullPointerException() {
        userService.register(userNull);
    }

    @Test
    public void testRegister_ReturnUser() {
        doReturn(userNotEmpty).when(userRepositoryMock).save(userNotEmpty);
        assertEquals(userService.register(userNotEmpty), userNotEmpty);
    }

    @Test(expected = NullPointerException.class)
    public void testLogin_NullPointerException() throws UserNotValidException {
        userService.login(userNull);
    }

    @Test(expected = UserNotValidException.class)
    public void testLogin_UserNotValidExceptionThrown() throws UserNotValidException {
        String username = userNotEmpty.getUsername();
        String password = userNotEmpty.getPassword();
        Optional<User> optionalEmpty = Optional.ofNullable(null);
        doReturn(optionalEmpty).when(userRepositoryMock).findByUsernameAndPassword(username, password);
        userService.login(userNotEmpty);
    }

    @Test
    public void testLogin_ReturnUser() throws UserNotValidException {
        String username = userNotEmpty.getUsername();
        String password = userNotEmpty.getPassword();
        Optional<User> optionalNoEmpty = Optional.of(userNotEmpty);
        doReturn(optionalNoEmpty).when(userRepositoryMock).findByUsernameAndPassword(username, password);
        doReturn(userNotEmpty).when(userRepositoryMock).findByUsername(username);
        assertEquals(userService.login(userNotEmpty), userNotEmpty);
    }

    @Test
    public void testIsLoggedIn_ReturnFalse() throws UserNotValidException {
        UserService userServiceNullUser = new UserService();
        assertEquals(userServiceNullUser.isLoggedIn(), false);

    }

    @Test
    public void testIsLoggedIn_ReturnTrue() throws UserNotValidException {
        UserService userServiceNotEmptyUser = new UserService(userNotEmpty, userRepositoryMock);
        assertEquals(userServiceNotEmptyUser.isLoggedIn(), true);
    }

    @Test
    public void testGetLoggedInUser_ReturnNull() {
        UserService userServiceNullUser = new UserService();
        assertEquals(userServiceNullUser.getLoggedInUser(), null);
    }

    @Test
    public void testGetLoggedInUser_ReturnUser() {
        UserService userServiceNotEmptyUser = new UserService(userNotEmpty, userRepositoryMock);
        assertEquals(userServiceNotEmptyUser.getLoggedInUser(), userNotEmpty);
    }

    @Test
    public void testLogout_ReturnNull() {
        UserService userServiceEmptyUser = new UserService();
        assertEquals(userServiceEmptyUser.logout(), null);
    }

    @Test
    public void testLogout_ReturnNull2() {
        UserService userServiceNotEmptyUser = new UserService(userNotEmpty, userRepositoryMock);
        assertEquals(userServiceNotEmptyUser.logout(), null);
    }
}
