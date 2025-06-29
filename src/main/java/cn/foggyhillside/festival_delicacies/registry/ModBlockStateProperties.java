package cn.foggyhillside.festival_delicacies.registry;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockStateProperties {
    public static final IntegerProperty COOKING_TYPE = IntegerProperty.create("cooking_type", 1, 7);
    public static final IntegerProperty START_BUTTON = IntegerProperty.create("start_button", 0, 1);
    public static final IntegerProperty RED_STONE = IntegerProperty.create("red_stone", 0, 1);
    public static final IntegerProperty PLATE_ITEM_TYPE = IntegerProperty.create("plate_item_type", 0, 8);
    public static final IntegerProperty AMOUNT = IntegerProperty.create("amount", 0, 6);
    public static final BooleanProperty KNIFE_INSERTED = BooleanProperty.create("knife_inserted");
    public static final BooleanProperty WITH_KNIFE = BooleanProperty.create("with_knife");
}
