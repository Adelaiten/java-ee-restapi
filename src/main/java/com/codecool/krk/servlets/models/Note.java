package models;

import com.google.gson.annotations.Expose;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Notes")
@NamedNativeQuery(name="allNotesQuery", query="select * from notes", resultClass=Note.class)
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private int noteId;

    @ManyToOne(cascade = {CascadeType.ALL})
    private User user;

    @Expose
    private String content;

    @Expose
    private String title;

    @Expose
    @Temporal(TemporalType.DATE)
    private Date date;

    @Transient
    @Expose
    private int user_id;

    @OneToMany(cascade={CascadeType.ALL}, mappedBy = "note")
    @ElementCollection
    private List<Comment> comments;

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserId() {return user_id;}

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
