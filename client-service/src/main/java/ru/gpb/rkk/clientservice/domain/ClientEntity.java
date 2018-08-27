package ru.gpb.rkk.clientservice.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "client")
public class ClientEntity implements Serializable {

    private static final long serialVersionUID = -6650267383987778434L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="client_id")
    private Long clientId;

    @Column(name="version")
    private int version;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="patronymic")
    private String patronymic;

    @Column(name="birth_date")
    private Date birthdate;

    @Column(name="document_type")
    private String documentType;

    @Column(name="document_series")
    private String documentSeries;

    @Column(name="document_number")
    private String documentNumber;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentSeries() {
        return documentSeries;
    }

    public void setDocumentSeries(String documentSeries) {
        this.documentSeries = documentSeries;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @Override
    public String toString() {
        return "ClientEntity{" +
                "clientId=" + clientId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthdate=" + birthdate +
                ", documentType='" + documentType + '\'' +
                ", documentSeries='" + documentSeries + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                '}';
    }
}
