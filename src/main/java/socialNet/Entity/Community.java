package socialNet.Entity;


import javax.persistence.*;

@Entity
@Table(name="communities")
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    
}
