package com.maven.project.sampledatamanagement.dataaccess.api;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@javax.persistence.Table(name = "SAMPLEDATA")
public class SampleDataEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "MAIL")
    private String mail;

    private static final long serialVersionUID = 1L;

    /**
     * @return name
     */
    public String getName() {

        return name;
    }

    /**
     * @param name
     *            new value of {@link #getname}.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * @return surname
     */
    public String getSurname() {

        return surname;
    }

    /**
     * @param surname
     *            new value of {@link #getsurname}.
     */
    public void setSurname(String surname) {

        this.surname = surname;
    }

    /**
     * @return email
     */
    public String getMail() {

        return mail;
    }

    /**
     * @param email
     *            new value of {@link #getemail}.
     */
    public void setMail(String email) {

        mail = email;
    }

    /**
     * @return age
     */
    public Integer getAge() {

        return age;
    }

    /**
     * @param age
     *            new value of {@link #getage}.
     */
    public void setAge(Integer age) {

        this.age = age;
    }

}
