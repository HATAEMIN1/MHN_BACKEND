package com.project.mhnbackend.common.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageDeserializer extends JsonDeserializer<Page<?>> {
    @Override
    public Page<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        List<?> content = node.has("content") ?
                mapper.convertValue(node.get("content"), List.class) :
                new ArrayList<>();

        int pageNumber = 0;
        int pageSize = 20; // 기본값 설정
        Sort sort = Sort.unsorted();

        if (node.has("pageable") && !node.get("pageable").isNull()) {
            JsonNode pageableNode = node.get("pageable");
            pageNumber = pageableNode.has("pageNumber") ? pageableNode.get("pageNumber").asInt() : 0;
            pageSize = pageableNode.has("pageSize") ? pageableNode.get("pageSize").asInt() : 20;

            if (pageableNode.has("sort") && !pageableNode.get("sort").isNull()) {
                JsonNode sortNode = pageableNode.get("sort");
                List<Sort.Order> orders = new ArrayList<>();
                if (sortNode.isArray()) {
                    for (JsonNode orderNode : sortNode) {
                        orders.add(createSortOrder(orderNode));
                    }
                } else {
                    orders.add(createSortOrder(sortNode));
                }
                sort = Sort.by(orders);
            }
        }

        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);

        long totalElements = node.has("totalElements") ? node.get("totalElements").asLong() : content.size();

        return new PageImpl<>(content, pageable, totalElements);
    }

    private Sort.Order createSortOrder(JsonNode orderNode) {
        String property = orderNode.get("property").asText();
        Sort.Direction direction = orderNode.has("direction") ?
                Sort.Direction.fromString(orderNode.get("direction").asText()) :
                Sort.Direction.ASC;
        return new Sort.Order(direction, property);
    }
}

//public class PageDeserializer extends JsonDeserializer<Page<?>> {
//    @Override
//    public Page<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//        ObjectMapper mapper = (ObjectMapper) p.getCodec();
//        JsonNode node = mapper.readTree(p);
//
//        List<?> content = mapper.convertValue(node.get("content"), List.class);
//
//        JsonNode pageableNode = node.get("pageable");
//        int pageNumber = pageableNode.get("pageNumber").asInt();
//        int pageSize = pageableNode.get("pageSize").asInt();
//
//        JsonNode sortNode = pageableNode.get("sort");
//        Sort sort = Sort.unsorted();
//        if (sortNode != null && !sortNode.isNull()) {
//            String sortDirection = sortNode.get("direction").asText();
//            String sortProperty = sortNode.get("property").asText();
//            sort = Sort.by(Sort.Direction.fromString(sortDirection), sortProperty);
//        }
//
//        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);
//
//        long totalElements = node.get("totalElements").asLong();
//
//        return new PageImpl<>(content, pageable, totalElements);
//    }
//}