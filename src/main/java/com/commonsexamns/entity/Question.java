package com.commonsexamns.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "preguntas")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;

    @JsonIgnoreProperties(value = {"preguntas"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_examen")
    private Exam examen;

    public Exam getExamen() {
        return examen;
    }

    public void setExamen(Exam examen) {
        this.examen = examen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return false;
        }
        if(!(obj instanceof Question)) {
            return false;
        }

        Question q = (Question) obj;

        return this.id != null && this.id.equals(q.getId());
    }
}
