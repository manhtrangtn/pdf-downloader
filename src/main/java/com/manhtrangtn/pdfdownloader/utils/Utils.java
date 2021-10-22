package com.manhtrangtn.pdfdownloader.utils;

import com.manhtrangtn.pdfdownloader.model.DownloadBody;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Utils {

    public List<String> generateUrl(DownloadBody body) {
        //Template: http://tailieuso.tlu.edu.vn/flowpaper/services/view.php?doc=46026802428415270067639569448162191235&format=jpg&page=2&subfolder=46/02/68/
        StringBuilder url;
        List<String> pages = new ArrayList<>();
        for (int page = 1; page <= body.getNumberOfPages(); page++) {
            url = new StringBuilder();

            url.append(Constance.BASE_URL);
            url.append("?doc=");
            url.append(body.getDocId());
            url.append("&format=jpg&page=");
            url.append(page);
            url.append("&subfolder=");
            url.append(body.getSubFolder());

            pages.add(url.toString());
        }
        return pages;
    }

    public StringBuilder generateFileName(int pageNumber){
        StringBuilder fileName = new StringBuilder();

        fileName.append(Constance.DATA_PATH);
        fileName.append(pageNumber);
        fileName.append(Constance.IMAGE_NAME);

        return fileName;
    }
}
