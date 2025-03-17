package com.example.ch.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.Announcement;
import com.example.ch.model.entity.Banner;
import com.example.ch.model.entity.ForumPost;
import com.example.ch.model.entity.Product;
import com.example.ch.service.admin.FunManageService;

@RestController
@RequestMapping("/admin")
public class FunManageController {
    @Autowired
    private FunManageService funManageService;


    //=======================================轮播图=======================================
    @GetMapping("/getBanner")
    public Result getBanner(
        @RequestParam("banner_name") String bannerName,
        @RequestParam("page_num") int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return funManageService.getBanner(bannerName, pageNum, pageSize);
    }

    @PostMapping("/addBanner")
    public Result addBanner(
        @RequestParam("banner_name") String bannerName,
        @RequestParam("banner_image") MultipartFile bannerImage
    ) {
        return funManageService.addBanner(bannerName, bannerImage);
    }

    @PatchMapping("/updateBanner")
    public Result updateBanner(
        @RequestParam(value = "banner_id", required = true) String bannerId,
        @RequestParam(value = "banner_name", required = false) String bannerName,
        @RequestParam(value = "banner_image", required = false) MultipartFile bannerImage
    ) {
        return funManageService.updateBanner(bannerId, bannerName, bannerImage);
    }

    @DeleteMapping("/deleteBanner")
    public Result deleteBanner(
        @RequestParam("banner_id") String bannerId
    ) {
        return funManageService.deleteBanner(bannerId);
    }



    //=======================================公告=======================================
    @PostMapping("/addAnnouncement")
    public Result addAnnouncement(@RequestBody Announcement addAnnouncement) {
        return funManageService.addAnnouncement(addAnnouncement);
    }

    @PatchMapping("/updateAnnouncement")
    public Result updateAnnouncement(@RequestBody Announcement updateAnnouncement) {
        return funManageService.updateAnnouncement(updateAnnouncement);
    }

    @GetMapping("/getAnnouncement")
    public Result getAnnouncement(
        @RequestParam("announcement_title") String announcementTitle,
        @RequestParam("page_num") int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return funManageService.getAnnouncement(announcementTitle, pageNum, pageSize);
    }

    @DeleteMapping("/deleteAnnouncement")
    public Result deleteAnnouncement(@RequestParam("announcement_id") String announcementId) {
        return funManageService.deleteAnnouncement(announcementId);
    }



    //=======================================论坛=======================================
    @PostMapping("/addPost")
    public Result addPost(@RequestBody ForumPost addPost) {
        return funManageService.addPost(addPost);
    }

    @PatchMapping("/updatePost")
    public Result updatePost(@RequestBody ForumPost updatePost) {
        return funManageService.updatePost(updatePost);
    }

    @GetMapping("/getPost")
    public Result getPost(
        @RequestParam("post_title") String postTitle,
        @RequestParam("page_num") int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return funManageService.getPost(postTitle, pageNum, pageSize);
    }

    @DeleteMapping("/deletePost")
    public Result deletePost(@RequestParam("post_id") String postId) {
        return funManageService.deletePost(postId);
    }



    //=======================================商品=======================================
    @PostMapping("/addProduct")
    public Result addProduct(
        @RequestParam("product_name") String productName,
        @RequestParam("product_description") String productDescription,
        @RequestParam("product_price") double productPrice,
        @RequestParam("product_image") MultipartFile[] productImages
    ) {
        return funManageService.addProduct(productName, productDescription, productPrice, productImages);
    }

    @PatchMapping("/updateProduct")
    public Result updateProduct(
        @RequestParam(value = "product_id", required = true) String productId,
        @RequestParam(value = "product_name", required = false) String productName,
        @RequestParam(value = "product_description", required = false) String productDescription,
        @RequestParam(value = "product_price", required = false) double productPrice,
        @RequestParam(value = "product_image", required = false) MultipartFile[] productImages
    ) {
        return funManageService.updateProduct(productId, productName, productDescription, productPrice, productImages);
    }

    @GetMapping("/getProduct")
    public Result getProduct(
        @RequestParam("product_name") String productName,
        @RequestParam("page_num") int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return funManageService.getProduct(productName, pageNum, pageSize);
    }

    @DeleteMapping("/deleteProduct")
    public Result deleteProduct(@RequestParam("product_id") String productId) {
        return funManageService.deleteProduct(productId);
    }
    @GetMapping("/getProductComments")
    public Result getProductComment(@RequestParam("product_id") String productId, @RequestParam("page_num") int pageNum, @RequestParam("page_size") int pageSize) {
        return funManageService.getProductComment(productId, pageNum, pageSize);
    }

    //=======================================订单=======================================
    @GetMapping("/getOrder")
    public Result getOrder(
        @RequestParam(value = "user_account", required = false) String userAccount,
        @RequestParam(value = "product_name", required = false) String productName,
        @RequestParam(value = "order_id", required = false) String orderId, 
        @RequestParam(value = "page_num", required = true) int pageNum, 
        @RequestParam(value = "page_size", required = true) int pageSize) {
        return funManageService.getOrder(userAccount, productName, orderId, pageNum, pageSize);
    }

}
