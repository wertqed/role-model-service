package ru.rolemodel.model.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by VBelov on 15.12.2017.
 */
public class UserPermissionSources implements Serializable {
    private static final long serialVersionUID = 4682732321619915839L;

    private Long userId;
    private String idService;
    private String namePermission;
    private List<Long> idSources;

    public String getNamePermission() {
        return namePermission;
    }

    public void setNamePermission(String namePermission) {
        this.namePermission = namePermission;
    }

    public List<Long> getIdSources() {
        return idSources;
    }

    public void setIdSources(List<Long> idSources) {
        this.idSources = idSources;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
