package rent.vehicle.workerserviceapp.common;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import rent.vehicle.workerservicemodel.dto.specification.GenericSearchRequest;
import rent.vehicle.workerservicemodel.dto.specification.SearchCriteria;
import rent.vehicle.workerservicemodel.enums.Operations;


import java.util.ArrayList;
import java.util.List;

@Component
public class SearchCriteriaParser {

    private List<SearchCriteria> searchCriteriaParse(String filter) {
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();

        if (filter == null || filter.isBlank()) {
            return searchCriteriaList;
        }

        // Сначала разделяем по запятым на отдельные критерии
        String[] criteriaArray = filter.trim().split(",");

        for (String criteria : criteriaArray) {
            // Затем каждый критерий разделяем по двоеточиям
            String[] parts = criteria.trim().split(":", 3); // 3 - чтобы value могло содержать ":"

            if (parts.length == 3 && !parts[0].isBlank()) {
                try {
                    SearchCriteria searchCriteria = new SearchCriteria();
                    searchCriteria.setFilter(parts[0].trim());
                    searchCriteria.setOperation(Operations.valueOf(parts[1].trim().toUpperCase()));
                    searchCriteria.setValue(parts[2].trim());
                    searchCriteriaList.add(searchCriteria);
                } catch (IllegalArgumentException e) {
                 e.getMessage();
                 throw e;
                }
            }
        }

        return searchCriteriaList;
    }
    public GenericSearchRequest buildSearchRequest(String filter, Pageable pageable) {
        String sortString = buildSortString(pageable.getSort());
        return GenericSearchRequest.builder()
                .searchCriteriaList(searchCriteriaParse(filter))
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .sort(sortString)
                .build();
    }

    private String buildSortString(Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return "id,desc"; // Дефолтная сортировка
        }

        // Берем только первый элемент сортировки для простоты
        // Если нужна множественная сортировка, придется изменить логику в сервисе
        return sort.stream()
                .findFirst()
                .map(order -> order.getProperty() + "," + order.getDirection().name().toLowerCase())
                .orElse("id,desc");
    }
}
