package otus.highload.homework.api.converter;

import org.mapstruct.*;
import otus.highload.homework.api.schema.DialogMessageResponse;
import otus.highload.homework.core.model.DialogMessage;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.ERROR
)
public interface DialogMessageToResponseConverter {
    @Mapping(source = "fromId", target = "from")
    @Mapping(source = "toId", target = "to")
    DialogMessageResponse convert(DialogMessage source);

    List<DialogMessageResponse> convertAll(List<DialogMessage> source);
}
