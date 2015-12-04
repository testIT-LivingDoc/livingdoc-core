package info.novatec.testit.livingdoc.util;

import org.apache.commons.lang3.StringUtils;


public class NotBlank implements CollectionUtil.Predicate<String> {
    @Override
    public boolean isVerifiedBy(String element) {
        return ! StringUtils.isBlank(element);
    }
}
