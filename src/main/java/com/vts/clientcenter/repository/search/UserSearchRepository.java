package com.vts.clientcenter.repository.search;

import com.vts.clientcenter.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserSearchRepository extends ElasticsearchRepository<User, String> {

}
