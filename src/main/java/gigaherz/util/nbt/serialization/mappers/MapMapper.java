package gigaherz.util.nbt.serialization.mappers;

import gigaherz.util.nbt.serialization.NBTSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.SerializationException;

import java.util.Map;

@SuppressWarnings("unchecked")
public class MapMapper extends MapperBase
{
    public MapMapper(int priority)
    {
        super(priority);
    }

    @Override
    public boolean canMapToField(Class<?> clazz)
    {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean canMapToCompound(Class<?> clazz)
    {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public void serializeField(CompoundNBT parent, String fieldName, Object object)
            throws ReflectiveOperationException
    {
        parent.put(fieldName, serializeMap((Map) object));
    }

    @Override
    public Object deserializeField(CompoundNBT parent, String fieldName, Class<?> clazz)
            throws ReflectiveOperationException
    {
        CompoundNBT tag2 = (CompoundNBT) parent.get(fieldName);
        return deserializeMap(tag2, (Class<? extends Map>) clazz);
    }

    @Override
    public CompoundNBT serializeCompound(Object object)
            throws ReflectiveOperationException
    {
        return serializeMap((Map) object);
    }

    @Override
    public Object deserializeCompound(CompoundNBT self, Class<?> clazz)
            throws ReflectiveOperationException
    {
        return deserializeMap(self, (Class<? extends Map>) clazz);
    }

    private CompoundNBT serializeMap(Map<Object, Object> m)
            throws ReflectiveOperationException
    {
        CompoundNBT tag = getTypeCompound(m, "map");

        ListNBT list = new ListNBT();
        for (Map.Entry e : m.entrySet())
        {
            CompoundNBT tag2 = new CompoundNBT();
            Object key = e.getKey();
            Object value = e.getValue();
            if (key != null)
            {
                NBTSerializer.serializeTo(tag2, "keyClass", key.getClass().getName());
                NBTSerializer.serializeTo(tag2, "key", key);
            }
            if (value != null)
            {
                NBTSerializer.serializeTo(tag2, "valueClass", value.getClass().getName());
                NBTSerializer.serializeTo(tag2, "value", value);
            }
            list.add(tag2);
        }
        tag.put("elements", list);
        return tag;
    }

    private Map deserializeMap(CompoundNBT tag, Class<? extends Map> clazz)
            throws ReflectiveOperationException
    {
        if (!tag.getString("type").equals("map"))
            throw new SerializationException();

        Class<?> actual = Class.forName(tag.getString("className"));
        if (!clazz.isAssignableFrom(actual))
            throw new SerializationException();

        Map m = (Map) actual.newInstance();

        ListNBT list = tag.getList("elements", Constants.NBT.TAG_COMPOUND);
        for (int ii = 0; ii < list.size(); ii++)
        {
            CompoundNBT tag2 = (CompoundNBT) list.get(ii);

            Object key = null;
            Object value = null;

            if (tag2.contains("key"))
            {
                Class<?> clsk = Class.forName(tag2.getString("keyClass"));
                key = NBTSerializer.deserializeFrom(tag2, "key", clsk, null);
            }


            if (tag2.contains("value"))
            {
                Class<?> cls = Class.forName(tag2.getString("valueClass"));
                value = NBTSerializer.deserializeFrom(tag2, "value", cls, null);
            }

            m.put(key, value);
        }

        return m;
    }
}
