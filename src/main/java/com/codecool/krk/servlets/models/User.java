package models;

import com.google.gson.annotations.Expose;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity(name = "Users")
public class User {
    @Id
    @GeneratedValue
    @Expose
    private int id;
    @Expose
    private String nick;
    @Expose
    private String name;
    @Expose
    private String surname;
    @Expose
    private String email;

    @Transient
    @Expose

    private List<Integer> notesIds;

    @OneToMany(cascade={CascadeType.ALL}, mappedBy = "user")
    @ElementCollection
    private List<Note> notes;

    @OneToMany(cascade={CascadeType.ALL}, mappedBy = "user")
    @ElementCollection
    private List<Comment> comments;

    public User() {

    }

    public User(int id, String nick, String name, String surname, String email, List<Note> notes) {
        this.id = id;
        this.nick = nick;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.notes = notes;
    }
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Integer> getNotesIds() {
        return notesIds;
    }

    public void setNotesIds() {
        this.notesIds = new ArrayList<>();
        for(Note note : this.notes) {
            this.notesIds.add(note.getNoteId());
        }
    }
}
