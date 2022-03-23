import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity//помечаем бин как сущность
@Table(name = "fruit_table")//в этой аннотации можно указать имя создаваемой таблицы
public class FruitEntity {

    @Id//аннотация из пакета avax.persistence.*, помечает поле как id
    @Column(name = "id_fruit")//в этой аннотации можно указать имя поля
    @GenericGenerator(name = "generator", strategy = "increment")//незаметно добрались до hibernate,
// здесь указывается что id будет автоматически увеличиваться при новых записях
    @GeneratedValue(generator = "generator")//аннотация генерации id
    private Integer id;

    @Column(name = "fruit_name")
    private String fruitName;

    @Column(name = "provider_code")
    private Integer providerCode;

   //что бы в с классом можно было совершать манипуляции создается
  //пустой конструктор, геттеры, сеттеры и переопределяется метод toString()

  public FruitEntity(){ //пустой конструктор

 }

public Integer getId() {
    return id;
}

 //геттеры, сеттеры
public String getFruitName() {
    return fruitName;
}

public FruitEntity setFruitName(String fruitName) {
    this.fruitName = fruitName;
    return this;
}

public Integer getProviderCode() {
    return providerCode;
}

public FruitEntity setProviderCode(Integer providerCode) {
    this.providerCode = providerCode;
    return this;
}

//переопределяем toString()
@Override
public String toString() {
    return "FruitEntity{" +
            "id=" + id +
            ", fruitName='" + fruitName + '\'' +
            ", providerCode=" + providerCode +
            '}';
}
}
