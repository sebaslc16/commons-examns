package com.commonsexamns.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "examenes")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 4, max = 30)
    private String nombre;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    //orphanRemoval, para que cuando se vaya a eliminar un examen tambien se eliminen sus preguntas, ya que la relacion es bidireccional
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "examen")
    @JsonIgnoreProperties(value = {"examen"}, allowSetters = true)
    private List<Question> preguntas;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Asignatura asignatura;

    @Transient
    private boolean respondido;

    public Exam() {
        this.preguntas = new ArrayList<>();
    }

    public List<Question> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Question> preguntas) {
        //reseat la lista de pregunta
        this.preguntas.clear();
        preguntas.forEach(p -> {
            //ciclo para mantener la relacion bidireccional,
            //se guarda la pregunta en la lista de preguntas del exmane
            //y se va asginando el examen a cada pregunta.
            this.addQuestion(p);
        });
    }

    @PrePersist
    public void prePersistDate() {
        this.createAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public void addQuestion(Question pregunta) {
        this.preguntas.add(pregunta);
        //this (instancia) para asociar tambien el examen a la pregunta,
        //para no dejar fk_examen en null
        pregunta.setExamen(this);
    }

    public void removeQuestion(Question pregunta) {
        this.preguntas.remove(pregunta);

        // null para eliminar la pregunta del examen
        pregunta.setExamen(null);
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public boolean isRespondido() {
        return respondido;
    }

    public void setRespondido(boolean respondido) {
        this.respondido = respondido;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if(!(obj instanceof Exam)){
            return false;
        }
        Exam examen = (Exam) obj;
        return this.id != null && this.id.equals(examen.getId());
    }
}
