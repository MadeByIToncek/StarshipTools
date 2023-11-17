/*
 * Copyright (c) 2023 - IToncek
 *
 * All rights to modifying this source code are granted, except for changing licence.
 * Any and all products generated from this source code must be shared with a link
 * to the original creator with clear and well-defined mention of the original creator.
 * This applies to any lower level copies, that are doing approximately the same thing.
 * If you are not sure, if your usage is within these boundaries, please contact the
 * author on their public email address.
 */

package cz.iqlandia.iqplanetarium.graphics;

import org.jetbrains.annotations.NotNull;

public record PanelMeta(int x, int y, int fps, boolean refresh, String name, int order) implements Comparable<PanelMeta>{
    @Override
    public int compareTo(@NotNull PanelMeta o) {
        return this.order - o.order;
    }
}
