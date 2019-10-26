package gigaherz.util.nbt.serialization;

import gigaherz.util.nbt.serialization.mappers.*;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class NBTSerializer
{
    public static final int PRIORITY_INTERFACE = Integer.MAX_VALUE;
    public static final int PRIORITY_USER = 0;
    public static final int PRIORITY_PRIMITIVE = -200;
    public static final int PRIORITY_COLLECTION = -300;

    static final List<MapperBase> mappers = new ArrayList<MapperBase>();
    static final GenericObjectMapper generic = new GenericObjectMapper(Integer.MIN_VALUE);

    static
    {
        // Must have highest priority so that something such as "extends Map implements ICustomNBTSerializable",
        // favor the interface over the base class
        registerNBTMapper(new CustomSerializableMapper(PRIORITY_INTERFACE));
        registerNBTMapper(new PrimitiveTypeMapper(PRIORITY_PRIMITIVE));
        registerNBTMapper(new StringMapper(PRIORITY_PRIMITIVE));
        registerNBTMapper(new EnumMapper(PRIORITY_PRIMITIVE));
        registerNBTMapper(new ArrayMapper(PRIORITY_PRIMITIVE));
        registerNBTMapper(new ListMapper(PRIORITY_COLLECTION));
        registerNBTMapper(new MapMapper(PRIORITY_COLLECTION));
        registerNBTMapper(new SetMapper(PRIORITY_COLLECTION));
        registerNBTMapper(new TagMapper(PRIORITY_COLLECTION + 1));
    }

    public static void registerNBTMapper(MapperBase mapper)
    {
        int prio = mapper.getPriority();
        for (int i = 0; i < mappers.size(); i++)
        {
            MapperBase existing = mappers.get(i);
            if (existing.getPriority() < prio)
            {
                mappers.add(i, mapper);
                return;
            }
            else if (existing.equals(mapper))
            {
                throw new IllegalArgumentException();
            }
        }
        mappers.add(mapper);
    }

    private static MapperBase findTopFieldMapperForClass(Class<?> clazz)
    {
        for (MapperBase mapper : mappers)
        {
            if (mapper.canMapToField(clazz))
            {
                return mapper;
            }
        }

        return null;
    }

    private static MapperBase findTopCompoundMapperForClass(Class<?> clazz)
    {
        for (MapperBase mapper : mappers)
        {
            if (mapper.canMapToCompound(clazz))
            {
                return mapper;
            }
        }

        return null;
    }

    // ==============================================================================================================
    // Serializing
    public static void serializeTo(CompoundNBT tag, String fieldName, Object object)
            throws ReflectiveOperationException
    {
        if (object != null)
        {
            MapperBase mapper = findTopFieldMapperForClass(object.getClass());
            if (mapper != null)
            {
                mapper.serializeField(tag, fieldName, object);
                return;
            }
        }

        generic.serializeField(tag, fieldName, object);
    }

    public static CompoundNBT serialize(Object object)
            throws ReflectiveOperationException
    {
        if (object != null)
        {
            MapperBase mapper = findTopCompoundMapperForClass(object.getClass());
            if (mapper != null)
            {
                return mapper.serializeCompound(object);
            }
        }

        return generic.serializeCompound(object);
    }

    // ==============================================================================================================
    // Deserializing
    public static <T> T deserialize(Class<? extends T> clazz, CompoundNBT tag)
            throws ReflectiveOperationException
    {
        MapperBase mapper = findTopCompoundMapperForClass(clazz);
        if (mapper != null)
        {
            return (T)mapper.deserializeCompound(tag, clazz);
        }

        return (T)generic.deserializeCompound(tag, clazz);
    }

    public static Object deserializeFrom(CompoundNBT parent, String fieldName, Class<?> clazz, Object currentValue)
            throws ReflectiveOperationException
    {
        if (!parent.contains(fieldName))
            return currentValue;

        MapperBase mapper = findTopFieldMapperForClass(clazz);
        if (mapper != null)
        {
            return mapper.deserializeField(parent, fieldName, clazz);
        }

        return generic.deserializeField(parent, fieldName, clazz);
    }
}
