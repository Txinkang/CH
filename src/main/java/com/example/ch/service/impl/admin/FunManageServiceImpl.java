package com.example.ch.service.impl.admin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.ch.common.Response.PageResponse;
import com.example.ch.common.Response.Result;
import com.example.ch.common.Response.ResultCode;
import com.example.ch.model.entity.Announcement;
import com.example.ch.model.entity.Banner;
import com.example.ch.model.entity.ForumPost;
import com.example.ch.model.entity.Orders;
import com.example.ch.model.entity.Product;
import com.example.ch.model.entity.ProductComment;
import com.example.ch.model.entity.User;
import com.example.ch.repository.AnnouncementRepository;
import com.example.ch.repository.BannerRepository;
import com.example.ch.repository.ForumCommentRepository;
import com.example.ch.repository.ForumPostRepository;
import com.example.ch.repository.OrdersRepository;
import com.example.ch.repository.ProductCommentRepository;
import com.example.ch.repository.ProductRepository;
import com.example.ch.repository.UserRepository;
import com.example.ch.service.admin.FunManageService;
import com.example.ch.utils.FileUtil;
import com.example.ch.utils.LogUtil;

@Service
public class FunManageServiceImpl implements FunManageService {
    private static final LogUtil logUtil = LogUtil.getLogger(FunManageServiceImpl.class);
    
    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCommentRepository productCommentRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumCommentRepository forumCommentRepository;

    @Value("${uploadFilePath.bannerPicturesPath}")
    private String bannerPicturesPath;

    @Value("${uploadFilePath.productPicturesPath}")
    private String productPicturesPath;

    //=======================================轮播图=======================================
    @Override
    public Result addBanner(String bannerName, MultipartFile bannerImage) {
        try {
            // 验证参数
            if (Strings.isEmpty(bannerName) || bannerImage == null) {
                return new Result(ResultCode.R_ParamError);
            }
            Banner banner = bannerRepository.findByBannerName(bannerName);
            if (banner != null) {
                return new Result(ResultCode.R_FileExists);
            }
            // 保存轮播图
            banner = new Banner();
            banner.setBannerName(bannerName);
            String uniqueFileName = FileUtil.saveFile(bannerImage, bannerPicturesPath);
            if (uniqueFileName == null) {
                return new Result(ResultCode.R_SaveFileError);
            }
            banner.setBannerId(UUID.randomUUID().toString());
            banner.setBannerImage(uniqueFileName);
            banner.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            banner.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            bannerRepository.save(banner);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("添加轮播图失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result updateBanner(String bannerId, String bannerName, MultipartFile bannerImage) {
        try {
            // 验证参数
            if (Strings.isEmpty(bannerId)) {
                return new Result(ResultCode.R_ParamError);
            }
            Banner banner = bannerRepository.findByBannerId(bannerId);
            if (banner == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            if (!Strings.isEmpty(bannerName)) {
                Banner queryBannerName = bannerRepository.findByBannerName(bannerName);
                if (queryBannerName != null && !queryBannerName.getBannerId().equals(bannerId)) {
                    return new Result(ResultCode.R_FileNameExists);
                }
                banner.setBannerName(bannerName);
            }
            // 更新轮播图
            if (bannerImage != null) {
                String uniqueFileName = FileUtil.saveFile(bannerImage, bannerPicturesPath);
                if (uniqueFileName == null) {
                    return new Result(ResultCode.R_SaveFileError);
                }
                String oldBannerImage = banner.getBannerImage();
                if (oldBannerImage != null) {
                    FileUtil.deleteFile(oldBannerImage, bannerPicturesPath);
                }
                banner.setBannerImage(uniqueFileName);
            }
            banner.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            bannerRepository.save(banner);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("更新轮播图失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getBanner(String bannerName, int pageNum, int pageSize) {
        try {
            PageResponse<Banner> pageResponse = new PageResponse<>();
            if(bannerName != null && !bannerName.isEmpty()){
                List<Banner> bannerList = new ArrayList<>();
                Banner banner = bannerRepository.findByBannerName(bannerName);
                if(banner != null){
                    bannerList.add(banner);
                }
                pageResponse.setTotal_item(bannerList.size());
                pageResponse.setData(bannerList);
            } else {
                PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
                Page<Banner> page = bannerRepository.findAll(pageRequest);
                pageResponse.setTotal_item(page.getTotalElements());
                pageResponse.setData(page.getContent());
            }
            return new Result(ResultCode.R_Ok, pageResponse);
        } catch (Exception e) {
            logUtil.error("获取轮播图失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result deleteBanner(String bannerId) {
        try {
            // 验证参数
            if (Strings.isEmpty(bannerId)) {
                return new Result(ResultCode.R_ParamError);
            }
            Banner banner = bannerRepository.findByBannerId(bannerId);
            if (banner == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            String bannerImage = banner.getBannerImage();
            if (bannerImage != null) {
                FileUtil.deleteFile(bannerImage, bannerPicturesPath);
            }
            bannerRepository.delete(banner);
            return new Result(ResultCode.R_Ok);
        }catch(Exception exception){
            logUtil.error("删除轮播图失败", exception);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }


    //=======================================公告=======================================
    @Override
    public Result addAnnouncement(Announcement addAnnouncement) {
        try {
            // 验证参数
            if (Strings.isEmpty(addAnnouncement.getAnnouncementTitle()) || Strings.isEmpty(addAnnouncement.getAnnouncementContent())) {
                return new Result(ResultCode.R_ParamError);
            }
            Announcement announcement = announcementRepository.findByAnnouncementTitle(addAnnouncement.getAnnouncementTitle());
            if (announcement != null) {
                return new Result(ResultCode.R_FileExists);
            }
            addAnnouncement.setAnnouncementId(UUID.randomUUID().toString());
            addAnnouncement.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            addAnnouncement.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            announcementRepository.save(addAnnouncement);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("添加公告失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result updateAnnouncement(Announcement updateAnnouncement) {
        try {
            // 验证参数
            if (Strings.isEmpty(updateAnnouncement.getAnnouncementId())) {
                return new Result(ResultCode.R_ParamError);
            }
            Announcement announcement = announcementRepository.findByAnnouncementId(updateAnnouncement.getAnnouncementId());
            if (announcement == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            if (!Strings.isEmpty(updateAnnouncement.getAnnouncementTitle())) {
                Announcement queryAnnouncementTitle = announcementRepository.findByAnnouncementTitle(updateAnnouncement.getAnnouncementTitle());
                if (queryAnnouncementTitle != null && !queryAnnouncementTitle.getAnnouncementId().equals(announcement.getAnnouncementId())) {
                    return new Result(ResultCode.R_FileNameExists);
                }
                announcement.setAnnouncementTitle(updateAnnouncement.getAnnouncementTitle());
            }
            if (!Strings.isEmpty(updateAnnouncement.getAnnouncementContent())) {
                announcement.setAnnouncementContent(updateAnnouncement.getAnnouncementContent());
            }
            announcement.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            announcementRepository.save(announcement);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("更新公告失败", e);
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

    @Override
    public Result deleteAnnouncement(String announcementId) {
        try {
            // 验证参数
            if (Strings.isEmpty(announcementId)) {
                return new Result(ResultCode.R_ParamError);
            }
            Announcement announcement = announcementRepository.findByAnnouncementId(announcementId);
            if (announcement == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            announcementRepository.delete(announcement);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("删除公告失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }



    //=======================================论坛=======================================
    @Override
    public Result addPost(ForumPost addPost) {
        try {
            // 验证参数
            if (Strings.isEmpty(addPost.getPostTitle()) || Strings.isEmpty(addPost.getPostContent())) {
                return new Result(ResultCode.R_ParamError);
            }
            ForumPost post = forumPostRepository.findByPostTitle(addPost.getPostTitle());
            if (post != null) {
                return new Result(ResultCode.R_FileExists);
            }
            addPost.setPostId(UUID.randomUUID().toString());
            addPost.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            addPost.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            forumPostRepository.save(addPost);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("添加论坛帖子失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result updatePost(ForumPost updatePost) {
        try {
            // 验证参数
            if (Strings.isEmpty(updatePost.getPostId())) {
                return new Result(ResultCode.R_ParamError);
            }
            ForumPost post = forumPostRepository.findByPostId(updatePost.getPostId());
            if (post == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            if (!Strings.isEmpty(updatePost.getPostTitle())) {
                ForumPost queryPostTitle = forumPostRepository.findByPostTitle(updatePost.getPostTitle());
                if (queryPostTitle != null && !queryPostTitle.getPostId().equals(post.getPostId())) {
                    return new Result(ResultCode.R_FileNameExists);
                }
                post.setPostTitle(updatePost.getPostTitle());
            }
            if (!Strings.isEmpty(updatePost.getPostContent())) {
                post.setPostContent(updatePost.getPostContent());
            }
            post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            forumPostRepository.save(post);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("更新论坛帖子失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

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
    public Result deletePost(String postId) {
        try {
            // 验证参数
            if (Strings.isEmpty(postId)) {
                return new Result(ResultCode.R_ParamError);
            }
            ForumPost post = forumPostRepository.findByPostId(postId);
            if (post == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            forumPostRepository.delete(post);
            forumCommentRepository.deleteByPostId(postId);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("删除论坛帖子失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }



    //=======================================商品=======================================
    @Override
    public Result addProduct(String productName, String productDescription, double productPrice,
            MultipartFile[] productImages) {
        try {
            // 验证参数
            if (Strings.isEmpty(productName) || Strings.isEmpty(productDescription) || productPrice <= 0 || productImages == null) {
                return new Result(ResultCode.R_ParamError);
            }   
            Product product = productRepository.findByProductName(productName);
            if (product != null) {
                return new Result(ResultCode.R_FileExists);
            }
            // 保存商品
            StringBuilder imageNames = new StringBuilder();
            for (MultipartFile productImage : productImages) {
                String uniqueFileName = FileUtil.saveFile(productImage, productPicturesPath);
                if (uniqueFileName == null) {
                    return new Result(ResultCode.R_SaveFileError);
                }
                imageNames.append(uniqueFileName).append("|");
            }
            String finalImageNames = imageNames.toString();
            product = new Product();
            product.setProductId(UUID.randomUUID().toString());
            product.setProductName(productName);
            product.setProductDescription(productDescription);
            product.setProductPrice(productPrice);
            product.setProductImage(finalImageNames);
            product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            productRepository.save(product);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("添加商品失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result updateProduct(String productId, String productName, String productDescription, double productPrice,
            MultipartFile[] productImages) {
        try {
            // 验证参数
            if (Strings.isEmpty(productId)) {
                return new Result(ResultCode.R_ParamError);
            }
            Product product = productRepository.findByProductId(productId);
            if (product == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            if (!Strings.isEmpty(productName) && productName != null) {
                Product queryProductName = productRepository.findByProductName(productName);
                if (queryProductName != null && !queryProductName.getProductId().equals(product.getProductId())) {
                    return new Result(ResultCode.R_FileNameExists);
                }
                product.setProductName(productName);
            }
            if (!Strings.isEmpty(productDescription) && productDescription != null) {
                product.setProductDescription(productDescription);
            }
            if (productPrice > 0) {
                product.setProductPrice(productPrice);
            }
            if (productImages != null && productImages.length > 0) {
                StringBuilder imageNames = new StringBuilder();
                for (MultipartFile productImage : productImages) {
                    String uniqueFileName = FileUtil.saveFile(productImage, productPicturesPath);
                    if (uniqueFileName == null) {
                        return new Result(ResultCode.R_SaveFileError);
                    }
                    imageNames.append(uniqueFileName).append("|");
                }
                String finalImageNames = imageNames.toString();
                String oldProductImage = product.getProductImage();
                if (oldProductImage != null) {
                    FileUtil.deleteFiles(oldProductImage, productPicturesPath);
                }
                product.setProductImage(finalImageNames);
            }
            product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            productRepository.save(product);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("更新商品失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

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
    @Transactional
    public Result deleteProduct(String productId) {
        try {
            // 验证参数
            if (Strings.isEmpty(productId)) {
                return new Result(ResultCode.R_ParamError);
            }
            Product product = productRepository.findByProductId(productId);
            if (product == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            String productImage = product.getProductImage();
            if (productImage != null) {
                FileUtil.deleteFiles(productImage, productPicturesPath);
            }
            productRepository.delete(product);
            // 删除商品评价
            productCommentRepository.deleteByProductId(productId);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("删除商品失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getProductComment(String productId, int pageNum, int pageSize) {
        try {
            //PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
            List<ProductComment> productCommentList = productCommentRepository.findByProductIdOrderByCreatedAtDesc(productId);
            return new Result(ResultCode.R_Ok, productCommentList);
        } catch (Exception e) {
            logUtil.error("获取商品评价失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getOrder(String userAccount, String productName, String orderId, int pageNum, int pageSize) {
        try {
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);  
            PageResponse<Map<String, Object>> pageResponse = new PageResponse<>();
            // // 先查询用户账号
            // User user = null;
            // if(userAccount != null && !userAccount.isEmpty()){
            //     user = userRepository.findByUserAccount(userAccount);
            //     if (user == null) {
            //         return new Result(ResultCode.R_Ok, new ArrayList<>());
            //     }
            // }

            // // 查询商品
            // Product product = null;
            // if(productName != null && !productName.isEmpty()){
            //     product = productRepository.findByProductName(productName); 
            //     if (product == null) {
            //         return new Result(ResultCode.R_Ok, new ArrayList<>());
            //     }
            // }

            // 查询订单
            Orders order = null;
            if(orderId != null && !orderId.isEmpty()){
                order = ordersRepository.findByOrderId(orderId, pageRequest).getContent().get(0);
                if (order == null) {
                    return new Result(ResultCode.R_Ok, new ArrayList<>());
                }
            }

            // 组合查询订单
            // Page<Orders> page;
            // if (user != null && product != null && order != null) {
            //     page = ordersRepository.findByUserIdAndProductIdAndOrderId(user.getUserId(), product.getProductId(), order.getOrderId(), pageRequest);
            // } else if (user != null && product != null) {
            //     page = ordersRepository.findByUserIdAndProductId(user.getUserId(), product.getProductId(), pageRequest);
            // } else if (user != null && order != null) {
            //     page = ordersRepository.findByUserIdAndOrderId(user.getUserId(), order.getOrderId(), pageRequest);
            // } else if (product != null && order != null) {
            //     page = ordersRepository.findByProductIdAndOrderId(product.getProductId(), order.getOrderId(), pageRequest);
            // } else if (user != null) {
            //     page = ordersRepository.findByUserId(user.getUserId(), pageRequest);
            // } else if (product != null) {
            //     page = ordersRepository.findByProductId(product.getProductId(), pageRequest);
            // } else if (order != null) {
            //     page = ordersRepository.findByOrderId(order.getOrderId(), pageRequest);
            // } else {
            //     page = ordersRepository.findAll(pageRequest);
            // }

            Page<Orders> page;
            if (order != null) {
                page = ordersRepository.findByOrderId(order.getOrderId(), pageRequest);
            } else {
                page = ordersRepository.findAll(pageRequest);
            }

            // 创建包含商品名称的返回对象
            List<Orders> orders = page.getContent();
            List<Map<String, Object>> orderWithProducts = new ArrayList<>();
            
            for (Orders o : orders) {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("orderId", o.getOrderId());
                orderMap.put("userId", o.getUserId());
                orderMap.put("name", o.getName());
                orderMap.put("phone", o.getPhone());
                orderMap.put("address", o.getAddress());
                orderMap.put("productId", o.getProductId());
                orderMap.put("productAmount", o.getProductAmount());
                orderMap.put("singlePrice", o.getSinglePrice());
                orderMap.put("totalPrice", o.getTotalPrice());
                orderMap.put("status", o.getStatus());
                orderMap.put("createdAt", o.getCreatedAt());
                orderMap.put("updatedAt", o.getUpdatedAt());
                Product p = productRepository.findById(o.getProductId()).orElse(null);
                if (p != null) {
                    orderMap.put("productName", p.getProductName());
                } else {
                    orderMap.put("productName", "");
                }
                
                orderWithProducts.add(orderMap);
            }

            pageResponse.setTotal_item(page.getTotalElements());
            pageResponse.setData(orderWithProducts);
            return new Result(ResultCode.R_Ok, pageResponse);
            
        } catch (Exception e) {
            logUtil.error("获取订单失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }


}
