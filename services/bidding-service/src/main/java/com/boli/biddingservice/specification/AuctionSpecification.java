//package com.boli.auctionservice.specification;
//
//import com.boli.auctionservice.enums.AuctionStatus;
//import com.boli.auctionservice.model.Auction;
//import org.springframework.data.jpa.domain.Specification;
//
//public class AuctionSpecification {
//    public static Specification<Auction> hasStatus(AuctionStatus status) {
//        return (root, query, cb) ->
//                status == null ? null : cb.equal(root.get("status"), status);
//    }
//
//    public static Specification<Auction> maxPrice(Double max) {
//        return (root, query, cb) ->
//                max == null ? null : cb.lessThanOrEqualTo(root.get("startingPrice"), max);
//    }
//
//    public static Specification<Auction> minPrice(Double min) {
//        return (root, query, cb) ->
//                min == null ? null : cb.greaterThanOrEqualTo(root.get("startingPrice"), min);
//    }
//}
