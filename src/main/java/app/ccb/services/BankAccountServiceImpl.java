package app.ccb.services;

import app.ccb.domain.dtos.importXml.BankAccountDto;
import app.ccb.domain.dtos.importXml.BankAccountRootDto;
import app.ccb.domain.entities.BankAccount;
import app.ccb.domain.entities.Client;
import app.ccb.repositories.BankAccountRepository;
import app.ccb.repositories.ClientRepository;
import app.ccb.util.FileUtil;
import app.ccb.util.ValidationUtil;
import app.ccb.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final static String XML_FILE_PATH = "C:\\SoftUni\\DB\\ColonialCouncilBank\\src\\main\\resources\\files\\xml\\bank-accounts.xml";

    private final BankAccountRepository bankAccountRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, ClientRepository clientRepository, ModelMapper modelMapper, ValidationUtil validationUtil, FileUtil fileUtil, XmlParser xmlParser) {
        this.bankAccountRepository = bankAccountRepository;
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public Boolean bankAccountsAreImported() {
        return this.bankAccountRepository.count() != 0;
    }

    @Override
    public String readBankAccountsXmlFile() throws IOException {
        return this.fileUtil.readFile(XML_FILE_PATH);
    }

    @Override
    public String importBankAccounts() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        BankAccountRootDto bankAccountRootDto = this.xmlParser.importXMl(BankAccountRootDto.class, XML_FILE_PATH);

        for (BankAccountDto bankAccountDto : bankAccountRootDto.getBankAccountDtos()) {
            BankAccount bankAccount = this.modelMapper.map(bankAccountDto, BankAccount.class);
            Client client = this.clientRepository.findByFullName(bankAccountDto.getClient());

            if (!this.validationUtil.isValid(bankAccount) || client == null){
                sb.append("Error: Incorrect Data!").append(System.lineSeparator());

                continue;
            }

            bankAccount.setClient(client);
            this.bankAccountRepository.saveAndFlush(bankAccount);

            sb.append(String.format("Successfully imported Bank Account - %s",bankAccount.getAccountNumber()))
                    .append(System.lineSeparator());
        }

        return sb.toString().trim();
    }
}
