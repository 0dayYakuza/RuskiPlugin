package eu.darkbot.ruski.modules.oreseller;

import eu.darkbot.api.config.annotations.Dropdown;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Maps implements Dropdown.Options<String> {
    public static final List<String> OPTIONS = Arrays.asList("X-1", "X-8", "5-2");

    public String getText(String option) {
        return option;
    }

    public Collection<String> options() {
        return OPTIONS;
    }
}
