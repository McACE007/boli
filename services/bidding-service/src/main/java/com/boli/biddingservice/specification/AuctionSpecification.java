//package com.boli.auctionservice.specification;
//
//import com.boli.auctionservice.enums.AuctionStatus;
//import com.boli.auctionservice.model.Bid;
//import org.springframework.data.jpa.domain.Specification;
//
//public class AuctionSpecification {
//    public static Specification<Bid> hasStatus(AuctionStatus status) {
//        return (root, query, cb) ->
//                status == null ? null : cb.equal(root.get("status"), status);
//    }
//
//    public static Specification<Bid> maxPrice(Double max) {
//        return (root, query, cb) ->
//                max == null ? null : cb.lessThanOrEqualTo(root.get("startingPrice"), max);
//    }
//
//    public static Specification<Bid> minPrice(Double min) {
//        return (root, query, cb) ->
//                min == null ? null : cb.greaterThanOrEqualTo(root.get("startingPrice"), min);
//    }
//}
