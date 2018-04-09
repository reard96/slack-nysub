package gs.nysub.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NYSubWebService {

    @Autowired
    private NYSubwayService nySubwayService;

    @Autowired
    private DateTimeService dateTimeService;

    public String generateStatus() {
        List<String> lineStatusList = nySubwayService.processWebRequest();
        return "<HTML>" +
                formatPageResponseHeader() +
                formatDateAndTimeOfRequest(generateRequestDateAndTime()) +
                formatStatusOfLines(lineStatusList) +
                "</HTML>";
    }

    private String formatPageResponseHeader() {
        return "<H2>MTA Subway Status</H2>";
    }

    private String formatDateAndTimeOfRequest(List<String> dateTime) {
        String requestDateTime = "<H3>Request Date: " + dateTime.get(0) + "</H3><H3>Request Time: " + dateTime.get(1) + "</H3>";
        return requestDateTime;
    }

    private List<String> generateRequestDateAndTime() {
        return dateTimeService.getDateTime();
    }

    private String formatStatusOfLines(List<String> lineStatusList) {
        List<String> formattedLineStatusList = new ArrayList();

        for (int index = 0; index < lineStatusList.size(); index++) {
            String line = lineStatusList.get(index).replaceAll("\n------\n", "");
            String[] lineParts = line.split("\\*: \\*");
            formattedLineStatusList.add(
                    buildLineStatus(
                            lineParts[0].replaceAll("\\*", ""),
                            lineParts[1].replaceAll("\\*", "")
                    )
            );
        }

        return formattedLineStatusList.stream().collect(Collectors.joining());
    }

    private String buildLineStatus(String lineID, String lineStatusSummary) {
        return "<span style=\"font-weight:bold\">" + lineID + "</span> " +
                "<span style=\"font-weight:bold\">" + lineStatusSummary + "</span><br/>";
    }
}
