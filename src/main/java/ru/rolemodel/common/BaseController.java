package ru.rolemodel.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by VBelov on 21.10.2017.
 */
public class BaseController {
    @Autowired
    protected ObjectMapper objectMapper;

//    private static final DateFormat DF = new SimpleDateFormat("dd.MM.yyyy");

    @InitBinder
    public void initBinder(WebDataBinder binder) {
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(DF, true));
        registerEditors(binder);
    }

    protected void registerEditors(WebDataBinder binder) {

    }

    protected <T> PropertyEditorSupport getPropertyEditor(final Class<T> clazz) {
        return new PropertyEditorSupport() {
            T value;

            @Override
            public T getValue() {
                return value;
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    if (!StringUtils.isEmpty(text))
                        value = objectMapper.readValue(text, clazz);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
