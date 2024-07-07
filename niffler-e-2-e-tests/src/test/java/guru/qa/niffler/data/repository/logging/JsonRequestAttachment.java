package guru.qa.niffler.data.repository.logging;

import io.qameta.allure.attachment.AttachmentData;

public class JsonRequestAttachment implements AttachmentData {
    private final String name;
    private final Object json;

    public JsonRequestAttachment(String name, Object json) {
        this.name = name;
        this.json = json;
    }

    @Override
    public String getName() {
        return name;
    }

    public Object getJson() {
        return json;
    }
}
