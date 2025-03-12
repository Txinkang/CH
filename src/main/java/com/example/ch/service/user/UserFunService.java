package com.example.ch.service.user;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.Cart;
import com.example.ch.model.entity.ForumComment;
import com.example.ch.model.entity.Orders;
import com.example.ch.model.entity.ProductComment;

public interface UserFunService {
    Result getBanner();

    Result getAnnouncement(String announcementTitle, int pageNum, int pageSize);

    Result getProduct(String productName, int pageNum, int pageSize);

    Result getDescription(String productId);

    Result addCart(Cart cart);

    Result deleteCart(String cartId);

    Result getCart();

    Result pay(Orders order);

    Result getOrder(int pageNum, int pageSize);

    Result evaluateProduct(ProductComment productComment);

    Result getProductComment(String productId, int pageNum, int pageSize);

    Result getPost(String postTitle, int pageNum, int pageSize);

    Result sendComment(ForumComment addComment);

    Result getComment(String postId);
}
