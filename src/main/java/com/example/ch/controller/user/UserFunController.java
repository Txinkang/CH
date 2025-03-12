package com.example.ch.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.Cart;
import com.example.ch.model.entity.ForumComment;
import com.example.ch.model.entity.ForumPost;
import com.example.ch.model.entity.Orders;
import com.example.ch.model.entity.Product;
import com.example.ch.model.entity.ProductComment;
import com.example.ch.service.user.UserFunService;

@RestController
@RequestMapping("/user")
public class UserFunController {

    @Autowired
    private UserFunService funManageService;

    @GetMapping("/getBanner")
    public Result getBanner() {
        return funManageService.getBanner();
    }

    @GetMapping("/getAnnouncement")
    public Result getAnnouncement(
        @RequestParam("announcement_title") String announcementTitle,
        @RequestParam("page_num") int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return funManageService.getAnnouncement(announcementTitle, pageNum, pageSize);
    }


    //=======================================商品=======================================
    @GetMapping("/getProduct")
    public Result getProduct(
        @RequestParam(value = "product_name", required = false) String productName,
        @RequestParam(value = "page_num", required = true) int pageNum,
        @RequestParam(value = "page_size", required = true) int pageSize
    ) {
        return funManageService.getProduct(productName, pageNum, pageSize);
    }

    @GetMapping("/getDescription")
    public Result getDescription(
        @RequestParam(value = "product_id", required = true) String productId
    ) {
        return funManageService.getDescription(productId);
    }

    @PostMapping("/addCart")
    public Result addCart(@RequestBody Cart cart) {
        return funManageService.addCart(cart);
    }
    @PostMapping("/pay")
    public Result pay(@RequestBody Orders order) {
        return funManageService.pay(order);
    }

    @GetMapping("/getOrder")
    public Result getOrder(@RequestParam("page_num") int pageNum, @RequestParam("page_size") int pageSize) {
        return funManageService.getOrder(pageNum, pageSize);
    }

    @PostMapping("/evaluateProduct")
    public Result evaluateProduct(@RequestBody ProductComment productComment) {
        return funManageService.evaluateProduct(productComment);
    }

    @GetMapping("/getProductComment")
    public Result getProductComment(@RequestParam("product_id") String productId, @RequestParam("page_num") int pageNum, @RequestParam("page_size") int pageSize) {
        return funManageService.getProductComment(productId, pageNum, pageSize);
    }
    //=======================================购物车=======================================
    @DeleteMapping("/deleteCart")
    public Result deleteCart(@RequestParam("cart_id") String cartId) {
        return funManageService.deleteCart(cartId);
    }

    @GetMapping("/getCart")
    public Result getCart() {
        return funManageService.getCart();
    }


    //=======================================论坛=======================================
    @GetMapping("/getPost")
    public Result getPost(
        @RequestParam("post_title") String postTitle,
        @RequestParam("page_num") int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return funManageService.getPost(postTitle, pageNum, pageSize);
    }

    @GetMapping("/getComment")
    public Result getComment(@RequestParam("post_id") String postId) {
        return funManageService.getComment(postId);
    }

    @PostMapping("/sendComment")
    public Result sendComment(@RequestBody ForumComment addComment) {
        return funManageService.sendComment(addComment);
    }


}
