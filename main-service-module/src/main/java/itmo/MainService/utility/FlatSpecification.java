package itmo.MainService.utility;

import itmo.MainService.entity.Flat;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;

import java.util.List;

public class FlatSpecification {
    public static Specification<Flat> filterByMultipleCriteria(List<FilterCriteria> filterCriteriaList, Sort sort){
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = new Predicate[filterCriteriaList.size()];

            for (int i = 0; i < filterCriteriaList.size(); i++){
                FilterCriteria filterCriteria = filterCriteriaList.get(i);
                String fieldName = filterCriteria.getFieldName();
                FilterOperator operator = filterCriteria.getOperator();
                String value = filterCriteria.getValue();

                switch(operator){
                    case MORE:
                        predicates[i] = criteriaBuilder.greaterThan(root.get(fieldName), Double.parseDouble(value));
                        break;
                    case LESS:
                        predicates[i] = criteriaBuilder.lessThan(root.get(fieldName), Double.parseDouble(value));
                        break;
                    case MORE_AND_EQUALS:
                        predicates[i] = criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), Double.parseDouble(value));
                        break;
                    case LESS_AND_EQUALS:
                        predicates[i] = criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), Double.parseDouble(value));
                        break;
                    default:
                        predicates[i] = criteriaBuilder.equal(root.get(fieldName), value);
                }
            }
            if (sort != null){
                query.orderBy(QueryUtils.toOrders(sort, root, criteriaBuilder));
            }
            return criteriaBuilder.and(predicates);
        };
    }
}
