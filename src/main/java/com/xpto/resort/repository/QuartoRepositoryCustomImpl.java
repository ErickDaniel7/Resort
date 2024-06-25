package com.xpto.resort.repository;



import com.xpto.resort.model.Quarto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class QuartoRepositoryCustomImpl implements QuartoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Quarto> findQuartosDinamico(Boolean vistaMar, Integer quantidadeQuartos) {
        StringBuilder jpql = new StringBuilder("SELECT q FROM Quarto q  WHERE (1=1)");

        if (vistaMar != null) {
            jpql.append(" AND q.vistaMar = :vistaMar");
        }

        if (quantidadeQuartos != null) {
            jpql.append(" AND q.capacidade = :quantidadeQuartos");
        }

        TypedQuery<Quarto> query = entityManager.createQuery(jpql.toString(), Quarto.class);


        if (vistaMar != null) {
            query.setParameter("vistaMar", vistaMar);
        }

        if (quantidadeQuartos != null) {
            query.setParameter("quantidadeQuartos", quantidadeQuartos);
        }

        return query.getResultList();
    }
}
