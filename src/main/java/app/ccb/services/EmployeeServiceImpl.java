package app.ccb.services;

import app.ccb.domain.dtos.importJson.EmployeeDto;
import app.ccb.domain.entities.Branch;
import app.ccb.domain.entities.Client;
import app.ccb.domain.entities.Employee;
import app.ccb.repositories.BranchRepository;
import app.ccb.repositories.EmployeeRepository;
import app.ccb.util.FileUtil;
import app.ccb.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final static String JSON_FILE_PATH = "C:\\SoftUni\\DB\\ColonialCouncilBank\\src\\main\\resources\\files\\json\\employees.json";

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, BranchRepository branchRepository, ModelMapper modelMapper, FileUtil fileUtil, ValidationUtil validationUtil, Gson gson) {
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() != 0;
    }

    @Override
    public String readEmployeesJsonFile() throws IOException {
        return this.fileUtil.readFile(JSON_FILE_PATH);
    }

    @Override
    public String importEmployees(String employees) throws IOException {
        StringBuilder sb = new StringBuilder();

        EmployeeDto[] employeeDtos = this.gson.fromJson(employees, EmployeeDto[].class);
        for (EmployeeDto employeeDto : employeeDtos) {
            Employee employee = this.modelMapper.map(employeeDto, Employee.class);
            String[] names = employeeDto.getFullName().split("\\s+");
            employee.setFirstName(names[0]);
            employee.setLastName(names[1]);
            Branch branch = this.branchRepository.findByName(employeeDto.getBranchName());
            employee.setBranch(branch);

            if (!this.validationUtil.isValid(employee)) {
                sb.append("Error: Incorrect Data!").append(System.lineSeparator());

                continue;
            }

            this.employeeRepository.saveAndFlush(employee);
            sb.append(String.format("Successfully imported Employee - %s %s", employee.getFirstName(), employee.getLastName()))
                    .append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    @Override
    public String exportTopEmployees() {
        StringBuilder sb = new StringBuilder();
        List<Employee> employeeList = this.employeeRepository.findTopEmployees();

        for (Employee employee : employeeList) {
            sb.append(String.format("Full Name: %s %s", employee.getFirstName(), employee.getLastName())).append(System.lineSeparator())
                    .append(String.format("Salary: %s", employee.getSalary())).append(System.lineSeparator())
                    .append(String.format("Stared On: %s", employee.getStartedOn())).append(System.lineSeparator())
                    .append("Clients").append(System.lineSeparator());
            for (Client client : employee.getClients()) {
                sb.append(String.format("\t%s", client.getFullName())).append(System.lineSeparator());
            }
        }

        return sb.toString().trim();
    }
}
