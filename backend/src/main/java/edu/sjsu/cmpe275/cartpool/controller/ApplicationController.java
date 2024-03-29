package edu.sjsu.cmpe275.cartpool.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import edu.sjsu.cmpe275.cartpool.Constants;
import edu.sjsu.cmpe275.cartpool.dto.Address;
import edu.sjsu.cmpe275.cartpool.dto.CreatePoolRequestBodyModel;
import edu.sjsu.cmpe275.cartpool.dto.CreateProductRequestBodyModel;
import edu.sjsu.cmpe275.cartpool.dto.CreateStoreRequestBodyModel;
import edu.sjsu.cmpe275.cartpool.dto.CreateUserRequestBodyModel;
import edu.sjsu.cmpe275.cartpool.dto.DeferredOrderRequestModel;
import edu.sjsu.cmpe275.cartpool.dto.EditProductRequestBodyModel;
import edu.sjsu.cmpe275.cartpool.dto.JoinPoolRequestBodyModel;
import edu.sjsu.cmpe275.cartpool.dto.Order;
import edu.sjsu.cmpe275.cartpool.dto.Pool;
import edu.sjsu.cmpe275.cartpool.dto.PoolRequest;
import edu.sjsu.cmpe275.cartpool.dto.Product;
import edu.sjsu.cmpe275.cartpool.dto.SelfPickupOrderRequestModel;
import edu.sjsu.cmpe275.cartpool.dto.SendMessageRequestBodyModel;
import edu.sjsu.cmpe275.cartpool.dto.Store;
import edu.sjsu.cmpe275.cartpool.dto.UpdateStoreRequestBodyModel;
import edu.sjsu.cmpe275.cartpool.dto.UpdateUserProfileRequestBodyModel;
import edu.sjsu.cmpe275.cartpool.dto.User;
import edu.sjsu.cmpe275.cartpool.repository.PoolRepository;
import edu.sjsu.cmpe275.cartpool.repository.StoreRepository;
import edu.sjsu.cmpe275.cartpool.repository.UserRepository;
import edu.sjsu.cmpe275.cartpool.service.OrderService;
import edu.sjsu.cmpe275.cartpool.service.PoolService;
import edu.sjsu.cmpe275.cartpool.service.ProductService;
import edu.sjsu.cmpe275.cartpool.service.StoreService;
import edu.sjsu.cmpe275.cartpool.service.UserService;

@Controller
@RequestMapping(path = "/cartpool")
@CrossOrigin(origins = Constants.BACKEND_URL)
public class ApplicationController {

	@Autowired
	private UserService userService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private ProductService productService;

	@Autowired
	private PoolService poolService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PoolRepository poolRepository;

	@GetMapping("/store/all")
	@ResponseBody
	public List<Store> getAllStores() {
		return storeService.getAllStores();
	}

	@GetMapping("/store/{id}")
	@ResponseBody
	public Store getStore(@PathVariable("id") String id) {
		return storeRepository.findById(Long.parseLong(id)).get();
	}

	@GetMapping("/product/{storeId}")
	@ResponseBody
	public List<Product> getProducts(@PathVariable("storeId") long storeId) {
		return productService.getProductsByStore(storeId);
	}

	@GetMapping("/user")
	@ResponseBody
	public User getUserByEmail(@RequestParam String email) {
		return userService.getUserByEmail(email);
	}

	@GetMapping("/product/search/{searchString}")
	@ResponseBody
	public Set<Product> productSearch(@PathVariable("searchString") String searchString) {
		return productService.searchProducts(searchString);
	}

	@GetMapping("/user/verify")
	@ResponseBody
	public String verifyUser(@RequestParam String email, @RequestParam int code) {
		return userService.verifyUser(email, code);
	}

	@PostMapping("/user/updateProfile")
	@ResponseBody
	public User updateUserProfile(@Valid @RequestBody UpdateUserProfileRequestBodyModel request) {
		User user = new User();
		Address address = new Address();
		user.setEmail(request.getEmail());
		user.setScreenName(request.getScreenName());
		user.setNickName(request.getNickName());
		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setZip(request.getZip());
		user.setAddress(address);
		return userService.updateUserProfile(user);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/user/setProfile")
	@ResponseBody
	public User setUserProfile(@Valid @RequestBody UpdateUserProfileRequestBodyModel request) {
		System.out.println("in api:    " + request);
		User user = new User();
		Address address = new Address();
		user.setEmail(request.getEmail());
		user.setScreenName(request.getScreenName());
		user.setNickName(request.getNickName());
		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setZip(request.getZip());
		user.setAddress(address);
		System.out.println("the user is" + user);
		return userService.setUserProfile(user);
	}

	@PostMapping("/user/register")
	@ResponseBody
	public User createUser(@Valid @RequestBody CreateUserRequestBodyModel createUserRequestBody) {
		System.out.println("in api:    " + createUserRequestBody);
		User user = new User();
		if (createUserRequestBody.getPassword() != null) {
			user.setPassword(createUserRequestBody.getPassword());
		}
		user.setEmail(createUserRequestBody.getEmail());
		user.setUid(createUserRequestBody.getUid());

		return userService.createUser(user);
	}

	@PostMapping("/store/create")
	@ResponseBody
	public Store createStore(@Valid @RequestBody CreateStoreRequestBodyModel createStoreRequestBody) {
		System.out.println("in api:    " + createStoreRequestBody);
		Store store = new Store();
		Address address = new Address();
		store.setName(createStoreRequestBody.getName());
		address.setStreet(createStoreRequestBody.getStreet());
		address.setCity(createStoreRequestBody.getCity());
		address.setState(createStoreRequestBody.getState());
		address.setZip(createStoreRequestBody.getZip());
		store.setAddress(address);
		return storeService.createStore(store);
	}

	@PostMapping("/store/updateStore")
	@ResponseBody
	public Store updateStore(@Valid @RequestBody UpdateStoreRequestBodyModel request) {
		System.out.println("in api:    " + request);
		Store store = new Store();
		Address address = new Address();
		store.setName(request.getName());
		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setZip(request.getZip());
		store.setAddress(address);
		return storeService.updateStore(store);
	}

	@DeleteMapping("/store/{id}")
	@ResponseBody
	public Store deleteStore(@PathVariable(required = true) long id) {
		System.out.println("in api deleteStore:" + id);
		return storeService.deleteStore(id);
	}

	@PostMapping("/product/add")
	@ResponseBody
	public CreateProductRequestBodyModel addProduct(
			@Valid @ModelAttribute CreateProductRequestBodyModel createProductRequestBody) {
		System.out.println("in api:    " + createProductRequestBody);

		for (long storeId : createProductRequestBody.getStores()) {
			Optional<Store> storeObj = storeRepository.findStoreById(storeId);
			if (storeObj.isPresent()) {
				Store store = storeObj.get();
				System.out.println("store => " + store);
				Product product = new Product();
				product.setStore(store);
				product.setSku(createProductRequestBody.getSku());
				product.setName(createProductRequestBody.getName());
				product.setDescription(createProductRequestBody.getDescription());
				product.setBrand(createProductRequestBody.getBrand());
				product.setPrice(createProductRequestBody.getPrice());
				product.setUnit(createProductRequestBody.getUnit());
				product.setAvailable(Constants.TRUE);
				productService.createProduct(product, createProductRequestBody.getImage());
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found");
			}
		}
		return createProductRequestBody;
	}

	@PostMapping("/product/edit/{productId}")
	@ResponseBody
	public Product editproduct(@PathVariable("productId") long productId,
			@Valid @RequestBody EditProductRequestBodyModel editProductRequestBodyModel) {
		Product product = new Product();
		product.setId(productId);
		product.setName(editProductRequestBodyModel.getName());
		product.setDescription(editProductRequestBodyModel.getDescription());
		product.setBrand(editProductRequestBodyModel.getBrand());
		product.setPrice(editProductRequestBodyModel.getPrice());
		product.setUnit(editProductRequestBodyModel.getUnit());
		product.setImageURL(editProductRequestBodyModel.getImageURL());
		return productService.editProduct(product);
	}

	@PostMapping("/pool/create")
	@ResponseBody
	public Pool createPool(@Valid @RequestBody CreatePoolRequestBodyModel createPoolRequestBodyModel) {
		System.out.println("createPoolRequestBodyModel:    " + createPoolRequestBodyModel);

		Optional<User> userObj = userRepository.findById(createPoolRequestBodyModel.getLeaderId());

		if (userObj.isPresent()) {
			User user = userObj.get();
			System.out.println("user => " + user);

			if (user.getPoolId() == null) {
				Pool pool = new Pool();
				pool.setPoolId(createPoolRequestBodyModel.getPoolId());
				pool.setName(createPoolRequestBodyModel.getName());
				pool.setNeighborhoodName(createPoolRequestBodyModel.getNeighborhoodName());
				pool.setDescription(createPoolRequestBodyModel.getDescription());
				pool.setZip(createPoolRequestBodyModel.getZip());
				pool.setLeaderId(createPoolRequestBodyModel.getLeaderId());

				return poolService.createPool(pool);
			} else {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"User is already associated with an existing pool");
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User(Leader) not found");
	}

	@GetMapping("/pool")
	@ResponseBody
	public Pool getPoolByPoolId(@RequestParam String poolId) {
		return poolRepository.findByPoolId(poolId);
	}

	@GetMapping("/pool/search/{searchString}")
	@ResponseBody
	public Set<Pool> poolSearch(@PathVariable("searchString") String searchString) {
		return poolRepository.findByNameContainingOrNeighborhoodNameContainingOrZipContaining(searchString,
				searchString, searchString);
	}

	@PostMapping("/pool/join")
	@ResponseBody
	public String joinPool(@Valid @RequestBody JoinPoolRequestBodyModel requestModel) {
		return poolService.joinPool(requestModel);
	}

	@PostMapping("/pool/approverType/{userId}")
	@ResponseBody
	public String getApproverType(@PathVariable("userId") long userId) {
		return poolService.getApproverType(userId);
	}

	@GetMapping("/pool/joinrequest/{referrerScreenName}")
	@ResponseBody
	public List<PoolRequest> getPoolRequests(@PathVariable("referrerScreenName") String referrerScreenName) {
		return poolService.getPoolRequests(referrerScreenName);
	}

	@GetMapping("/pool/leader/joinrequest/{leaderScreenName}")
	@ResponseBody
	public List<PoolRequest> getPoolRequestsForLeader(@PathVariable("leaderScreenName") String leaderScreenName) {
		return poolService.getPoolRequestsForLeader(leaderScreenName);
	}

	@PostMapping("/pool/referral/approvejoinrequest/{requestId}")
	@ResponseBody
	public String approveReferralRequest(@PathVariable("requestId") long requestId) {
		return poolService.approveReferralRequest(requestId);
	}

	@PostMapping("/pool/rejectjoinrequest/{requestId}")
	@ResponseBody
	public String rejectJoinRequest(@PathVariable("requestId") long requestId) {
		return poolService.rejectJoinRequest(requestId);
	}

	@PostMapping("/pool/leader/approvejoinrequest/{requestId}")
	@ResponseBody
	public String approveJoinRequestForLeader(@PathVariable("requestId") long requestId) {
		return poolService.approveJoinRequestForLeader(requestId);
	}

	@DeleteMapping("/pool/delete/{poolId}")
	@ResponseBody
	public Pool deletePool(@PathVariable("poolId") String poolId) {
		return poolService.deletePool(poolId);
	}
	
	@PostMapping("/pool/leave")
	@ResponseBody
	public boolean leavePool(@RequestParam("poolId") String poolId, @RequestParam("userId") long userId) {
		return poolService.leavePool(poolId, userId);
	}

	@PostMapping("/order/defer")
	@ResponseBody
	public Order createDeferredOrder(@Valid @RequestBody DeferredOrderRequestModel deferredOrderRequestModel) {
		return orderService.createDeferredOrder(deferredOrderRequestModel);
	}

	@GetMapping("/orders/fellowpoolers/{userId}/store/{storeId}")
	@ResponseBody
	public List<Order> getFellowPoolerOrders(@PathVariable("userId") long userId, @PathVariable("storeId") long storeId) {
		return orderService.getFellowPoolerOrders(userId, storeId);
	}

	@PostMapping("/order/self")
	@ResponseBody
	public Order createSelfPickupOrder(@Valid @RequestBody SelfPickupOrderRequestModel selfPickupOrderRequestModel) {
		return orderService.createSelfPickupOrder(selfPickupOrderRequestModel);
	}

	/*
	@PostMapping("order/pickup")
	@ResponseBody
	public Boolean pickupOrder(@Valid @RequestBody OrderPickupRequestModel model) {
		return orderService.pickupOrder(model);
	}*/

	@GetMapping("/orders/{userId}")
	@ResponseBody
	public List<Order> getOrders(@PathVariable("userId") long userId) {
		return orderService.getOrders(userId);
	}

	@GetMapping("/pickuporders/{userId}")
	@ResponseBody
	public List<Order> getPickupOrders(@PathVariable("userId") long userId) {
		return orderService.getPickupOrders(userId);
	}

	@PostMapping("/pickuporders/markpickedup/{groupid}")
	@ResponseBody
	public String markpickedup(@PathVariable("groupid") int groupId) {
		return orderService.markpickedup(groupId);
	}

	@PostMapping("/order/marknotdelivered/{orderId}")
	@ResponseBody
	public Order markOrderNotDelivered(@PathVariable("orderId") long orderId) {
		return orderService.markOrderNotDelivered(orderId);
	}

	@GetMapping("/orders/{userId}/pickedUp")
	@ResponseBody
	public List<Order> getPickedUpOrders(@PathVariable("userId") long userId) {
		return orderService.getPickedUpOrder(userId);
	}

	@PostMapping("/order/markdelivered/{orderId}")
	@ResponseBody
	public Order markOrderDelivered(@PathVariable("orderId") long orderId) {
		return orderService.markOrderDelivered(orderId);
	}

	@PostMapping("/user/message")
	@ResponseBody
	public Boolean sendMessage(@RequestBody SendMessageRequestBodyModel sendMessageRequestBodyModel) {
		return userService.sendMessage(sendMessageRequestBodyModel);
	}

	@DeleteMapping("/product/{productId}")
	@ResponseBody
	public Product deleteProduct(@PathVariable("productId") long productId) {
		return productService.deleteProduct(productId);
	}
}