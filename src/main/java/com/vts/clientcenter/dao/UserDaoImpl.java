package com.vts.clientcenter.dao;

import com.vts.clientcenter.domain.Authority;
import com.vts.clientcenter.domain.User;
import com.vts.clientcenter.service.dto.UserAuthorizedResponseDto;
import com.vts.clientcenter.service.dto.UserDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void handleRemoveFromRole(String role) {

        Authority authority = entityManager.find(Authority.class, role);

        Query q = entityManager.createNativeQuery("select jua.user_id from jhi_user_authority jua where jua.authority_name = ?");
        q.setParameter(1, authority.getName());
        List<Integer> userIds = (List<Integer>)q.getResultList();


        q = entityManager.createNativeQuery("delete from jhi_user_authority where jhi_user_authority.user_id in (:ides) and jhi_user_authority.authority_name = (:roleName)");
        q.setParameter("ides", userIds);
        q.setParameter("roleName", authority.getName());
        q.executeUpdate();

        // user in another roles not delete

        q = entityManager.createNativeQuery("select jua.id from tv_role_permission jua where jua.role_name = ?");
        q.setParameter(1, authority.getName());
        List<Integer> rolePermissionIds = (List<Integer>)q.getResultList();

        q = entityManager.createNativeQuery("delete from tv_role_permission where id in (:ides)");
        q.setParameter("ides", rolePermissionIds);
        q.executeUpdate();

        entityManager.remove(authority);
    }

}
