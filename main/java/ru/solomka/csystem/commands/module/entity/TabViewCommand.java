package ru.solomka.csystem.commands.module.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class TabViewCommand {
    private final int index;
    private final Object[] toViews;
}
