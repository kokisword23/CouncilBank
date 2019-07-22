package app.ccb.domain.dtos.importXml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "bank-accounts")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankAccountRootDto {

    @XmlElement(name = "bank-account")
    private List<BankAccountDto> bankAccountDtos;

    public BankAccountRootDto(){}

    public List<BankAccountDto> getBankAccountDtos() {
        return bankAccountDtos;
    }

    public void setBankAccountDtos(List<BankAccountDto> bankAccountDtos) {
        this.bankAccountDtos = bankAccountDtos;
    }
}
