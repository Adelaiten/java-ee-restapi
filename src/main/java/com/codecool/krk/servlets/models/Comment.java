package models;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue
    @Expose
    private int id;

    @ManyToOne
    private Note note;

    @Transient
    @Expose
    private int note_id;

    @ManyToOne
    private User user;

    @Transient
    @Expose
    private int user_id;

    @Expose
    private String content;

    @Temporal(TemporalType.DATE)
    @Expose
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setIds(){
        this.note_id = this.note.getNoteId();
        this.user_id = this.user.getId();
    }
}
