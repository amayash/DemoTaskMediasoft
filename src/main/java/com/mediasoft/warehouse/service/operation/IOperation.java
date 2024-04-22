package com.mediasoft.warehouse.service.operation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mediasoft.warehouse.model.enums.FieldName;
import org.springframework.data.jpa.domain.Specification;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OperationStringImpl.class, name = "String"),
        @JsonSubTypes.Type(value = OperationNumberImpl.class, name = "Number")
})
public interface IOperation<T, P> {
    Specification<P> likeOperation(T searchParam, FieldName field);

    Specification<P> equalsOperation(T searchParam, FieldName field);

    Specification<P> greaterThanOrEqualsOperation(T searchParam, FieldName field);

    Specification<P> lessThanOrEqualsOperation(T searchParam, FieldName field);
}
