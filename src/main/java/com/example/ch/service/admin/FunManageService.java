package com.example.ch.service.admin;

import org.springframework.web.multipart.MultipartFile;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.Announcement;
import com.example.ch.model.entity.Banner;
import com.example.ch.model.entity.ForumPost;

public interface FunManageService {
    Result addBanner(String bannerName, MultipartFile bannerImage);

    Result updateBanner(String bannerId, String bannerName, MultipartFile bannerImage);

    Result getBanner(String bannerName, int pageNum, int pageSize);

    Result deleteBanner(String bannerId);

    Result addAnnouncement(Announcement addAnnouncement);

    Result updateAnnouncement(Announcement updateAnnouncement);

    Result getAnnouncement(String announcementTitle, int pageNum, int pageSize);

    Result deleteAnnouncement(String announcementId);

    Result addPost(ForumPost addPost);

    Result updatePost(ForumPost updatePost);

    Result getPost(String postTitle, int pageNum, int pageSize);

    Result deletePost(String postId);

    Result addProduct(String productName, String productDescription, double productPrice, MultipartFile[] productImages);

    Result updateProduct(String productId, String productName, String productDescription, double productPrice,
            MultipartFile[] productImages);

    Result getProduct(String productName, int pageNum, int pageSize);

    Result deleteProduct(String productId);

    Result getProductComment(String productId, int pageNum, int pageSize);

    Result getOrder(String userAccount, String productName, String orderId, int pageNum, int pageSize);
}
