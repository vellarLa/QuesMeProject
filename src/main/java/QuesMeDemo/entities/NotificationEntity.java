package QuesMeDemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "NOTIFICATION", schema = "public")
public class NotificationEntity {

    @Id
    @Column(name = "id_notification")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idNotification;

    @ManyToOne(cascade = CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_owner")
    private UserEntity owner;

    @Column(name = "type")
    private String type;

    @Column(name = "content", length = 300)
    private String content;

    public NotificationEntity(UserEntity owner, String type, int ban)
    {
        this.owner = owner;
        this.type = type;
        this.content = "Администратор одобрил жалобу на один из заданных Вами вопросов. Актуальное число банов: " + ban;
    }

    public NotificationEntity(String type, QuestionEntity question)
    {
        //this.owner = owner;
        this.owner = question.getReceiver();
        this.type = type;
        if (type.equalsIgnoreCase("Новый вопрос")){
            if (question.getAnonymous()=='Y')
                this.content = "Анонимный пользователь прислал вопрос: " + question.getText();
            else
            {
                if (question.getSender().getSex().equals('М'))
                    this.content = question.getSender().getNickname() + " прислал вопрос: " + question.getText();
                else
                    this.content = question.getSender().getNickname() + " прислала вопрос: " + question.getText();
            }
        }
        if (type.equalsIgnoreCase("Ответ"))
        {
            if (question.getReceiver().getSex().equals('М'))
                this.content = "Пользователь " + question.getReceiver() + " ответил на вопрос: " + question.getText();
            else
                this.content = "Пользователь " + question.getReceiver() + " ответила на вопрос: " + question.getText();
        }
        if (type.equalsIgnoreCase("Жалоба на вопрос"))
        {
            this.content = "Была подана жалоба на вопрос '" + question.getText() + "'";
        }
    }
    public NotificationEntity(String type, QuestionEntity question, String complaintStatus)
    {
        this.type = type;
        this.owner = question.getReceiver();
        if (complaintStatus.equalsIgnoreCase("Принята"))
            this.content = "Жалоба на вопрос " + question.getText() + " принята администратором";
        if (complaintStatus.equalsIgnoreCase("Отклонена"))
            this.content = "Жалоба на вопрос " + question.getText() + " отклонена администратором";
    }
}
