package app.ccb.domain.dtos.importJson;

import com.google.gson.annotations.Expose;

public class BranchDto {

    @Expose
    private String name;

    public BranchDto(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
