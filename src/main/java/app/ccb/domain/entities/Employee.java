package app.ccb.domain.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {

    private String firstName;
    private String lastName;
    private BigDecimal salary;
    private String startedOn;
    private Branch branch;
    private List<Client> clients;

    public Employee(){}

    @Column(name = "first_name", nullable = false)
    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    @NotNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "salary")
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Column(name = "started_on")
    public String getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(String startedOn) {
        this.startedOn = startedOn;
    }

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "employees")
    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}

