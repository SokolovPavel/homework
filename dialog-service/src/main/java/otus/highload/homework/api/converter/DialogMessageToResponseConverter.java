package otus.highload.homework.api.converter;

import otus.highload.homework.api.schema.DialogMessageResponse;
import otus.highload.homework.core.model.DialogMessage;

import java.util.List;

public interface DialogMessageToResponseConverter {
    DialogMessageResponse convert(DialogMessage source);

    List<DialogMessageResponse> convertAll(List<DialogMessage> source);
}
