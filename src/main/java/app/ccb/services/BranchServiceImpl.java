package app.ccb.services;

import app.ccb.domain.dtos.importJson.BranchDto;
import app.ccb.domain.entities.Branch;
import app.ccb.repositories.BranchRepository;
import app.ccb.util.FileUtil;
import app.ccb.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BranchServiceImpl implements BranchService {

    private final static String JSON_FILE_PATH = "C:\\SoftUni\\DB\\ColonialCouncilBank\\src\\main\\resources\\files\\json\\branches.json";

    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, ModelMapper modelMapper, FileUtil fileUtil, ValidationUtil validationUtil, Gson gson) {
        this.branchRepository = branchRepository;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count() != 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return this.fileUtil.readFile(JSON_FILE_PATH);
    }

    @Override
    public String importBranches(String branchesJson) throws IOException {
        StringBuilder sb = new StringBuilder();

        BranchDto[] branchDtos = this.gson.fromJson(branchesJson, BranchDto[].class);
        for (BranchDto branchDto : branchDtos) {
            Branch branch = this.modelMapper.map(branchDto, Branch.class);

            if (!this.validationUtil.isValid(branch)){
                sb.append("Error: Incorrect Data!").append(System.lineSeparator());

                continue;
            }

            this.branchRepository.saveAndFlush(branch);
            sb.append(String.format("Successfully imported Branch - %s",branch.getName()))
                    .append(System.lineSeparator());
        }

        return sb.toString().trim();
    }
}
