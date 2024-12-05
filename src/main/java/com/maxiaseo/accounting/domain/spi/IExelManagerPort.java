package com.maxiaseo.accounting.domain.spi;

import java.io.IOException;
import java.util.List;

public interface IExelManagerPort {

    List<List<String>> getDataFromExcelFileInMemory(byte[] inMemoryFile)throws IOException;
}
