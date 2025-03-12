package com.example.ch.service.impl.user;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ch.common.Response.PageResponse;
import com.example.ch.common.Response.Result;
import com.example.ch.common.Response.ResultCode;
import com.example.ch.common.userCommon.ThreadLocalUtil;
import com.example.ch.constant.OrderConstantdata;
import com.example.ch.model.entity.Announcement;
import com.example.ch.model.entity.Banner;
import com.example.ch.model.entity.Cart;
import com.example.ch.model.entity.ForumComment;
import com.example.ch.model.entity.ForumPost;
import com.example.ch.model.entity.Orders;
import com.example.ch.model.entity.Product;
import com.example.ch.model.entity.ProductComment;
import com.example.ch.model.entity.User;
import com.example.ch.repository.AnnouncementRepository;
import com.example.ch.repository.BannerRepository;
import com.example.ch.repository.CartRepository;
import com.example.ch.repository.ForumCommentRepository;
import com.example.ch.repository.ForumPostRepository;
import com.example.ch.repository.OrdersRepository;
import com.example.ch.repository.ProductCommentRepository;
import com.example.ch.repository.ProductRepository;
import com.example.ch.repository.UserRepository;
import com.example.ch.service.user.UserFunService;
import com.example.ch.utils.LogUtil;

@Service
public class UserFunServiceImpl implements UserFunService {
    private static final LogUtil logUtil = LogUtil.getLogger(UserFunServiceImpl.class);

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductCommentRepository productCommentRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private ForumCommentRepository forumCommentRepository;

    @Override
    public Result getBanner() {
        try {
            List<Banner> bannerList = bannerRepository.findAll();
            return new Result(ResultCode.R_Ok, bannerList);
        } catch (Exception e) {
            logUtil.error("获取轮播图失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getAnnouncement(String announcementTitle, int pageNum, int pageSize) {
        try {
            PageResponse<Announcement> pageResponse = new PageResponse<>();
            if(announcementTitle != null && !announcementTitle.isEmpty()){
                List<Announcement> announcementList = new ArrayList<>();
                Announcement announcement = announcementRepository.findByAnnouncementTitle(announcementTitle);
                if(announcement != null){
                    announcementList.add(announcement);
                }
                pageResponse.setTotal_item(announcementList.size());
                pageResponse.setData(announcementList);
            } else {
                PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
                Page<Announcement> page = announcementRepository.findAll(pageRequest);
                pageResponse.setTotal_item(page.getTotalElements());
                pageResponse.setData(page.getContent());
            }
            return new Result(ResultCode.R_Ok, pageResponse);
        } catch (Exception e) {
            logUtil.error("获取公告失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    //=======================================商品=======================================
    @Override
    public Result getProduct(String productName, int pageNum, int pageSize) {
        try {
            PageResponse<Product> pageResponse = new PageResponse<>();
            if(productName != null && !productName.isEmpty()){
                List<Product> productList = new ArrayList<>();
                Product product = productRepository.findByProductName(productName);
                if(product != null){
                    productList.add(product);
                }
                pageResponse.setTotal_item(productList.size());
                pageResponse.setData(productList);
            } else {
                PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
                Page<Product> page = productRepository.findAll(pageRequest);
                pageResponse.setTotal_item(page.getTotalElements());
                pageResponse.setData(page.getContent());
            }
            return new Result(ResultCode.R_Ok, pageResponse);
        } catch (Exception e) {
            logUtil.error("获取商品失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getDescription(String productId) {
        try {
            Product product = productRepository.findByProductId(productId);
            if(product == null){
                return new Result(ResultCode.R_FileNotFound);
            }
            return new Result(ResultCode.R_Ok, product.getProductDescription());
        } catch (Exception e) {
            logUtil.error("获取商品描述失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result addCart(Cart cart) {
        try {
            String userId = ThreadLocalUtil.getUserId();
            if(userId == null){
                return new Result(ResultCode.R_UserNotFound);
            }
            cart.setUserId(userId);
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            cartRepository.save(cart);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("添加购物车失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result pay(Orders order) {
        try {
            String userId = ThreadLocalUtil.getUserId();
            if(userId == null){
                return new Result(ResultCode.R_UserNotFound);
            }
            //查看商品是否存在
            Product product = productRepository.findByProductId(order.getProductId());
            if(product == null){
                return new Result(ResultCode.R_FileNotFound);
            }
            //查看余额
            User user = userRepository.findByUserId(userId);
            if(user == null){
                return new Result(ResultCode.R_UserNotFound);
            }
            if(user.getUserBalance().compareTo(BigDecimal.valueOf(order.getTotalPrice())) < 0){
                return new Result(ResultCode.R_BalanceNotEnough);
            }
            //扣除余额
            user.setUserBalance(user.getUserBalance().subtract(BigDecimal.valueOf(order.getTotalPrice())));
            userRepository.save(user);
            //生成订单
            order.setUserId(userId);
            order.setOrderId(UUID.randomUUID().toString());
            order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            order.setStatus(OrderConstantdata.ORDER_STATUS_SUCCESS);
            ordersRepository.save(order);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            order.setStatus(OrderConstantdata.ORDER_STATUS_FAILED);
            ordersRepository.save(order);
            logUtil.error("支付失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }
    
    @Override
    public Result getOrder(int pageNum, int pageSize) {
        try {
            String userId = ThreadLocalUtil.getUserId();
            if(userId == null){
                return new Result(ResultCode.R_UserNotFound);
            }
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
            Page<Orders> page = ordersRepository.findByUserId(userId, pageRequest);
            PageResponse<Orders> pageResponse = new PageResponse<>();
            pageResponse.setTotal_item(page.getTotalElements());
            pageResponse.setData(page.getContent());
            return new Result(ResultCode.R_Ok, pageResponse);
        } catch (Exception e) {
            logUtil.error("获取订单失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result evaluateProduct(ProductComment productComment) {
        try {
            String userId = ThreadLocalUtil.getUserId();
            if(userId == null){
                return new Result(ResultCode.R_UserNotFound);
            }
            productComment.setUserId(userId);
            productComment.setProductCommentId(UUID.randomUUID().toString());
            productComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            productCommentRepository.save(productComment);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("评价商品失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getProductComment(String productId, int pageNum, int pageSize) {
        try {
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
            Page<ProductComment> page = productCommentRepository.findByProductId(productId, pageRequest);
            return new Result(ResultCode.R_Ok, page.getContent());
        } catch (Exception e) {
            logUtil.error("获取商品评价失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }
    //=======================================购物车=======================================
    @Override
    public Result deleteCart(String cartId) {
        try {
            Cart cart = cartRepository.findByCartId(cartId);
            if(cart == null){
                return new Result(ResultCode.R_FileNotFound);
            }
            cartRepository.delete(cart);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("删除购物车失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getCart() {
        try {
            String userId = ThreadLocalUtil.getUserId();
            if(userId == null){
                return new Result(ResultCode.R_UserNotFound);
            }
            List<Cart> cartList = cartRepository.findByUserId(userId);
            return new Result(ResultCode.R_Ok, cartList);
        } catch (Exception e) {
            logUtil.error("获取购物车失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }


    //=======================================论坛=======================================
    @Override
    public Result getPost(String postTitle, int pageNum, int pageSize) {
        try {
            PageResponse<ForumPost> pageResponse = new PageResponse<>();
            if(postTitle != null && !postTitle.isEmpty()){
                List<ForumPost> postList = new ArrayList<>();
                ForumPost post = forumPostRepository.findByPostTitle(postTitle);
                if(post != null){
                    postList.add(post);
                }
                pageResponse.setTotal_item(postList.size());
                pageResponse.setData(postList);
            } else {
                PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
                Page<ForumPost> page = forumPostRepository.findAll(pageRequest);
                pageResponse.setTotal_item(page.getTotalElements());
                pageResponse.setData(page.getContent());
            }
            return new Result(ResultCode.R_Ok, pageResponse);
        } catch (Exception e) {
            logUtil.error("获取论坛帖子失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getComment(String postId) {
        try {
            List<ForumComment> commentList = forumCommentRepository.findByPostId(postId);
            return new Result(ResultCode.R_Ok, commentList);
        } catch (Exception e) {
            logUtil.error("获取论坛评论失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result sendComment(ForumComment forumComment) {
        try {
            String userId = ThreadLocalUtil.getUserId();
            if(userId == null){
                return new Result(ResultCode.R_UserNotFound);
            }
            forumComment.setUserId(userId);
            forumComment.setCommentId(UUID.randomUUID().toString());
            forumComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            forumCommentRepository.save(forumComment);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("发送论坛评论失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

}
