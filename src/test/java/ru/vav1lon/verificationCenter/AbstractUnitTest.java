package ru.vav1lon.verificationCenter;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class AbstractUnitTest {

    @Value("${local.filePath}")
    protected String filePath;

}
