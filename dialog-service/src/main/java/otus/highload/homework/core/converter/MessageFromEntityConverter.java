package otus.highload.homework.core.converter;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import otus.highload.homework.core.model.DialogMessage;
import otus.highload.homework.core.persistence.entity.MessageEntity;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.ERROR
)
public interface MessageFromEntityConverter {

    DialogMessage convert(MessageEntity source);

    List<DialogMessage> convertAll(List<MessageEntity> source);
}
