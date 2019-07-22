package app.ccb.domain.dtos.importXml;

import javax.naming.Name;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "card")
@XmlAccessorType(XmlAccessType.FIELD)
public class CardDto {

    @XmlAttribute
    private String status;

    @XmlAttribute(name = "account-number")
    private String accountNumber;

    @XmlElement(name = "card-number")
    private String cardNumber;

    public CardDto(){
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
