package app.ccb.services;

import app.ccb.domain.dtos.importJson.ClientDto;
import app.ccb.domain.entities.Card;
import app.ccb.domain.entities.Client;
import app.ccb.domain.entities.Employee;
import app.ccb.repositories.ClientRepository;
import app.ccb.repositories.EmployeeRepository;
import app.ccb.util.FileUtil;
import app.ccb.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ClientServiceImpl implements ClientService {

    private final static String JSON_FILE_PATH = "C:\\SoftUni\\DB\\ColonialCouncilBank\\src\\main\\resources\\files\\json\\clients.json";

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper, FileUtil fileUtil, ValidationUtil validationUtil, Gson gson) {
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public Boolean clientsAreImported() {
        return this.clientRepository.count() != 0;
    }

    @Override
    public String readClientsJsonFile() throws IOException {
       return this.fileUtil.readFile(JSON_FILE_PATH);
    }

    @Override
    public String importClients(String clients) {
        StringBuilder sb = new StringBuilder();

        ClientDto[] clientDtos = this.gson.fromJson(clients, ClientDto[].class);
        for (ClientDto clientDto : clientDtos) {
            Client client = this.modelMapper.map(clientDto, Client.class);
            client.setFullName(clientDto.getFirstName() + " " + clientDto.getLastName());

            String[] eNames = clientDto.getAppointedEmployee().split("\\s+");
            Employee employee = this.employeeRepository.findByFirstNameAndLastName(eNames[0], eNames[1]);

            if (employee == null ||!this.validationUtil.isValid(client)){
                sb.append("Error: Incorrect Data!").append(System.lineSeparator());

                continue;
            }

            Client clientInDb = this.clientRepository.findByFullName(client.getFullName());
            if (clientInDb != null){
                sb.append("Error: Duplicate data!").append(System.lineSeparator());

                continue;
            }

            client.getEmployees().add(employee);
            this.clientRepository.saveAndFlush(client);

            sb.append(String.format("Successfully imported Client - %s",client.getFullName())).append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    @Override
    public String exportFamilyGuy() {
        StringBuilder sb = new StringBuilder();

        Client client = this.clientRepository.familyGuy();
        sb.append(String.format("Full Name: %s",client.getFullName())).append(System.lineSeparator())
                .append(String.format("Age: %d",client.getAge())).append(System.lineSeparator())
                .append(String.format("Bank Account: %s",client.getBankAccount().getAccountNumber())).append(System.lineSeparator());
        for (Card card : client.getBankAccount().getCards()) {
            sb.append(String.format("\tCard Number: %s",card.getCardNumber())).append(System.lineSeparator())
            .append(String.format("\tCard Status: %s",card.getCardStatus())).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
