package app.ccb.services;

import app.ccb.domain.dtos.importXml.CardDto;
import app.ccb.domain.dtos.importXml.CardRootDto;
import app.ccb.domain.entities.BankAccount;
import app.ccb.domain.entities.Card;
import app.ccb.repositories.BankAccountRepository;
import app.ccb.repositories.CardRepository;
import app.ccb.util.FileUtil;
import app.ccb.util.ValidationUtil;
import app.ccb.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
public class CardServiceImpl implements CardService {

    private final static String XML_FILE_PATH = "C:\\SoftUni\\DB\\ColonialCouncilBank\\src\\main\\resources\\files\\xml\\cards.xml";

    private final CardRepository cardRepository;
    private final BankAccountRepository bankAccountRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, BankAccountRepository bankAccountRepository, ModelMapper modelMapper, ValidationUtil validationUtil, FileUtil fileUtil, XmlParser xmlParser) {
        this.cardRepository = cardRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public Boolean cardsAreImported() {
        return this.cardRepository.count() != 0;
    }

    @Override
    public String readCardsXmlFile() throws IOException {
       return this.fileUtil.readFile(XML_FILE_PATH);
    }

    @Override
    public String importCards() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        CardRootDto cardRootDto = this.xmlParser.importXMl(CardRootDto.class,XML_FILE_PATH);
        for (CardDto cardDto : cardRootDto.getCards()) {
            Card card = this.modelMapper.map(cardDto, Card.class);
            BankAccount bankAccount = this.bankAccountRepository.findByAccountNumber(cardDto.getAccountNumber());
            card.setBankAccount(bankAccount);

            if (!this.validationUtil.isValid(card) || bankAccount == null){
                sb.append("Error: Incorrect Data!").append(System.lineSeparator());

                continue;
            }

            this.cardRepository.saveAndFlush(card);
            sb.append(String.format("Successfully imported Card - %s",card.getCardNumber()))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
