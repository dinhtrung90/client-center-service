package com.vts.clientcenter.repository.search;

import com.vts.clientcenter.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

public interface UserSearchRepository extends ElasticsearchRepository<User, String> {

}
