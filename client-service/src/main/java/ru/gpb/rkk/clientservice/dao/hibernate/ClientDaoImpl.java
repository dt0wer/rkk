package ru.gpb.rkk.clientservice.dao.hibernate;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gpb.rkk.clientservice.dao.ClientDao;
import ru.gpb.rkk.clientservice.domain.ClientEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.text.MessageFormat;

@Slf4j
@Component
public class ClientDaoImpl implements ClientDao {

    private SessionFactory sessionFactory;

    public ClientDaoImpl( @Autowired SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ClientEntity create(ClientEntity clientEntity) {
        log.debug("Creating a new client " + clientEntity);
        Long id = (Long) sessionFactory.getCurrentSession().save(clientEntity);
        return getById(id);
    }

    @Override
    public ClientEntity getById(Long id) {
        log.debug("Looking for a client with id " + id);
        ClientEntity clientEntity =sessionFactory.getCurrentSession().get(ClientEntity.class, id);
        log.debug("Found a client " + clientEntity);
        return sessionFactory.getCurrentSession().get(ClientEntity.class, id);
    }

    @Override
    public ClientEntity update(ClientEntity clientEntity) {
        log.debug("Updating a client " + clientEntity);
        sessionFactory.getCurrentSession().update(clientEntity);
        return getById(clientEntity.getClientId());
    }

    @Override
    public List<ClientEntity> getClientsByNameBirthDateAndIdentityDoc(String firstName, String lastName, String patronymicName, String documentType, String documentSeries, String documentNumber, Date birthDate) {
        String message = MessageFormat.format("Looking for a client with {0} {1} {2} date of birth: {3}, document type: {4},  document series: {5}, document number: {}.",
                lastName, firstName, patronymicName, birthDate, documentType, documentSeries, documentNumber);
        log.debug(message);
        CriteriaBuilder builder=sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<ClientEntity> criteria = builder.createQuery(ClientEntity.class);

        Root<ClientEntity> root = criteria.from(ClientEntity.class);
        criteria.select(root);
        criteria.where(builder.and(
                builder.equal(root.get("name"), firstName),
                builder.equal(root.get("surname"), lastName),
                builder.equal(root.get("patronymic"), patronymicName),
                builder.equal(root.get("documentType"), documentType),
                builder.equal(root.get("documentSeries"), documentSeries),
                builder.equal(root.get("documentNumber"), documentNumber),
                builder.equal(root.get("birthdate"), birthDate)
        ));

        List<ClientEntity> clientEntities = sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
        clientEntities.stream().forEach(client -> log.debug("Found a client " + client));
        return clientEntities;
    }
}
