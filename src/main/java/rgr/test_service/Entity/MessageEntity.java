package rgr.test_service.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int sender_id;
    private int recipient_id;
    private String textMessage;
    private String timeMessage;
    private short readed;

    public MessageEntity(int sender_id, int recipient_id, String textMessage) {
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
        this.textMessage = textMessage;
        this.timeMessage = LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute();
        this.readed = 0;
    }

    public MessageEntity() {
    }

    public MessageEntity(int sender_id, int recipient_id) {
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
    }

    public MessageEntity(int sender_id, int recipient_id, String textMessage, String timeMessage, short readed) {
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
        this.textMessage = textMessage;
        this.timeMessage = timeMessage;
        this.readed = readed;
    }

    public String getTimeMessage() {
        return timeMessage;
    }

    public void setTimeMessage(String timeMessage) {
        this.timeMessage = timeMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(int recipient_id) {
        this.recipient_id = recipient_id;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public short getReaded() {
        return readed;
    }

    public void setReaded(short readed) {
        this.readed = readed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEntity that = (MessageEntity) o;
        return sender_id == that.sender_id &&
                recipient_id == that.recipient_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender_id, recipient_id);
    }
}
