package app.ccb.domain.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client extends BaseEntity{

    private String fullName;
    private Integer age;
    private BankAccount bankAccount;
    private List<Employee> employees;

    public Client(){
        this.employees = new ArrayList<>();
    }


    @Column(name = "full_name", nullable = false)
    @NotNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @OneToOne(mappedBy = "client")
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @ManyToMany
    @JoinTable(name = "clients_employees",
                joinColumns = @JoinColumn(name = "client_id",referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"))
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}

