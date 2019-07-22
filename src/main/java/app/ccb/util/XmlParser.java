package app.ccb.util;

import javax.xml.bind.JAXBException;

public interface XmlParser {

    <O> O importXMl(Class<O> objectClass,String path) throws JAXBException;
}
