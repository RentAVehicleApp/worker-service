package rent.vehicle.supportserviceapp.config;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.beans.PropertyDescriptor;

public class QueryParamUtil {

    public static MultiValueMap<String, String> convertToQueryParams(Object obj, Pageable pageable) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (obj != null) {
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);

            for (PropertyDescriptor pd : beanWrapper.getPropertyDescriptors()) {
                String propertyName = pd.getName();
                Object value = beanWrapper.getPropertyValue(propertyName);
                if (value != null && !"class".equals(propertyName)) {
                    queryParams.add(propertyName, value.toString());
                }
            }
        }

        if (pageable != null) {
            queryParams.add("page", pageable.getPageNumber() + "");
            queryParams.add("size", pageable.getPageSize() + "");
            pageable.getSort().forEach(order -> {
                String sortParam = order.getProperty() + "," + order.getDirection(); // 👈 правильно
                queryParams.add("sort", sortParam);
            });
        }

        return queryParams;
    }
}
