package com.lmm.base.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.lmm.base.dto.TestDto;
import com.lmm.base.dto.TestList;
import com.lmm.base.mapper.TestMapper;
import com.lmm.base.model.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lmm.base.service.helper.ServiceHelper;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TestService {
    static final ILogger logger = SLoggerFactory.getLogger(TestService.class);

    @Autowired
    TestMapper testMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    ServiceHelper serviceHelper;

    public TestList listAdmins() {
        // validate and will throw exception if not found

        List<Test> list = testMapper.selectByExample(null);

        List<TestDto> testDtoList = list.stream().map(test -> convertToDto(test)).collect(toList());

        return TestList.builder()
                .tests(testDtoList)
                .build();
    }


    private TestDto convertToDto(Test test) {
        return modelMapper.map(test, TestDto.class);
    }

    private Test convertToModel(TestDto companyDto) {
        return modelMapper.map(companyDto, Test.class);
    }

}
