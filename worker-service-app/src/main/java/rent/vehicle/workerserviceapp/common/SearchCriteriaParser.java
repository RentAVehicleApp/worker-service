package rent.vehicle.workerserviceapp.common;

import org.springframework.data.domain.Pageable;
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
                    System.out.println(e.getMessage());
                    throw e;
                }
            }
        }

        return searchCriteriaList;
    }
    public GenericSearchRequest buildSearchRequest(String filter, Pageable pageable) {
        return GenericSearchRequest.builder()
                .searchCriteriaList(searchCriteriaParse(filter))
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .sort(pageable.getSort() != null ? pageable.getSort().toString() : "id,desc")
                .build();
    }
}
