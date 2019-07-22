package app.ccb.domain.dtos.importXml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cards")
@XmlAccessorType(XmlAccessType.FIELD)
public class CardRootDto {

    @XmlElement(name = "card")
    private List<CardDto> cards;

    public CardRootDto(){}

    public List<CardDto> getCards() {
        return cards;
    }

    public void setCards(List<CardDto> cards) {
        this.cards = cards;
    }
}
