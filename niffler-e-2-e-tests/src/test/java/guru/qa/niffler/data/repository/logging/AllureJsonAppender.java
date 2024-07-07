package guru.qa.niffler.data.repository.logging;

import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

public class AllureJsonAppender {
    private final String templateName = "json.ftl";
    private final AttachmentProcessor<AttachmentData> processor = new DefaultAttachmentProcessor();

    public void logJson(String name, Object json) {
        if (json != null) {
            JsonRequestAttachment jsonRequestAttachment = new JsonRequestAttachment(name, json);

            processor.addAttachment(jsonRequestAttachment, new FreemarkerAttachmentRenderer(templateName));
        }
    }
}
