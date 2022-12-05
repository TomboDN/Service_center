package mirea.db.service_center.specification;

import com.google.common.base.Joiner;
import mirea.db.service_center.model.Service;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecificationBuilder<T> {
    private List<SpecSearchCriteria> params;

    public final SpecificationBuilder<T> with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public final SpecificationBuilder<T> with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, op, value));
        }
        return this;
    }

    public Specification<T> build() {
        if (params.size() == 0)
            return null;

        Specification<T> result = new TypeSpecification<>(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new TypeSpecification<>(params.get(i)))
                    : Specification.where(result).and(new TypeSpecification<>(params.get(i)));
        }

        return result;
    }

    public Specification<T> buildFromString(String search) {
        SpecificationBuilder<T> builder = new SpecificationBuilder<>();
        String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5));
        }

        return builder.build();
    }

    public final SpecificationBuilder<T> with(TypeSpecification<T> spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public final SpecificationBuilder<T> with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }
}
