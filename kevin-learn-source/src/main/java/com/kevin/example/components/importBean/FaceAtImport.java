package com.kevin.example.components.importBean;

import com.kevin.example.components.helpBean.KevinAtImportClass;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Import({KevinAtImportClass.class})
public class FaceAtImport {

}
