package com.vts.clientcenter.service.impl;

import com.vts.clientcenter.repository.search.UserSearchRepository;
import com.vts.clientcenter.service.UserSearchService;
import com.vts.clientcenter.service.dto.UserDTO;
import com.vts.clientcenter.service.mapper.UserMapper;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
public class UserSearchServiceImpl implements UserSearchService {

    private static final String PRODUCT_INDEX = "user_index";

    private final UserSearchRepository userSearchRepository;

    private final UserMapper userMapper;

    public UserSearchServiceImpl(UserSearchRepository userSearchRepository, UserMapper userMapper) {
        this.userSearchRepository = userSearchRepository;
        this.userMapper = userMapper;
    }


    @Override
    public List<UserDTO> searchUserByKeyword(String query) {
        return StreamSupport
            .stream(userSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(userMapper::userToUserDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> processSearchNameByKeyword(String query) {
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(query, "email")
            .fuzziness(Fuzziness.AUTO);
        return StreamSupport
            .stream(userSearchRepository.search(queryBuilder).spliterator(), false)
            .map(userMapper::userToUserDTO)
            .collect(Collectors.toList());
    }
}
