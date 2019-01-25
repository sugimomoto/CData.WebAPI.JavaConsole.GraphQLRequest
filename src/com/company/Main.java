package com.company;

import okhttp3.*;
import java.util.List;

public class Main {
    public static final String url = "https://sampledbforjavawebapi.herokuapp.com/v1alpha1/graphql";

    public static final String authHeaderName = "X-Hasura-Access-Key";
    public static final String authHeaderValue = "XXXXXXXXXXX";

    public static void main(String[] args) throws Exception {

        String graphqlRequest = GraphQLSchema.query(query -> query.orders(
                orders -> orders
                        .orderId()
                        .orderDate()
                        .customerId()
                        .employeeId()
                        .orderDetails(orderDetails -> orderDetails
                                .productId()
                                .unitPrice()
                                .quantity()
                                .discount()
                        )
        )).toString();

        OkHttpClient client = new OkHttpClient();
        MediaType MIMEType= MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(MIMEType,"{\"query\": \"" + graphqlRequest + "\"}");

        Response response = client.newCall(
                new Request.Builder().post(requestBody).addHeader(authHeaderName,authHeaderValue).url(url).build()
        ).execute();

        GraphQLSchema.QueryResponse queryResponse = GraphQLSchema.QueryResponse.fromJson(response.body().string());

        List<GraphQLSchema.orders> orderList = queryResponse.getData().getOrders();

        for (GraphQLSchema.orders order: orderList) {
            System.out.println("-----------------------------------------");
            System.out.println("OrderId : " + order.getOrderId());
            System.out.println("OrderDate : " + order.getOrderDate());
            System.out.println("CustomerId : " + order.getCustomerId());
            System.out.println("EmployeedId : " + order.getEmployeeId());

            for(GraphQLSchema.order_details order_detail : order.getOrderDetails()){
                System.out.println("order_details ProderctId : " + order_detail.getProductId());
                System.out.println("order_details Discount : " + order_detail.getDiscount());
                System.out.println("order_details Quantity : " + order_detail.getQuantity());
                System.out.println("order_details UnitPrice : " + order_detail.getUnitPrice());
            }
        }
    }
}
